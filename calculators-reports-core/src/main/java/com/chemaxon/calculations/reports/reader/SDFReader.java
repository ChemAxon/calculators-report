/*
 * Copyright 2020 ChemAxon Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.chemaxon.calculations.reports.reader;

import chemaxon.struc.Molecule;
import com.chemaxon.calculations.reports.util.InvalidMoleculeFormatException;
import com.chemaxon.calculations.reports.util.MolUtil;
import com.chemaxon.calculations.reports.util.MoleculeFormats;
import com.google.common.collect.Streams;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author Daniel Hari
 */
public class SDFReader extends AbstractReader {
    private InputStream inputStream;
    private List<String> propNames;

    public SDFReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public static SDFReader from(InputStream inputStream) {
        return new SDFReader(inputStream);
    }

    public SDFReader valuePropNames(String... propNames) {
        this.propNames = Arrays.asList(propNames);
        return this;
    }

    /**
     * @throws ReaderException 
     */
    @Override
    public List<Pair<String, List<String>>> read() {
        try {
            Iterator<String> molStrings = MoleculeFormats.recordIterator(inputStream, "sdf");
            return extractProperties(molStrings);
        } catch (InvalidMoleculeFormatException e) {
            throw new ReaderException(e.getMessage());
        }
    }

    private List<Pair<String, List<String>>> extractProperties(Iterator<String> molStrings) throws InvalidMoleculeFormatException {
        return Streams.stream(molStrings).map(this::extractProperties).collect(toList());
    }

    private Pair<String, List<String>> extractProperties(String molString) {
        return Pair.of(molString, extractProperties(MoleculeFormats.importMolOrThrow(molString)));
    }

    private List<String> extractProperties(Molecule molecule) {
        return propNames.stream()
                .map(propName -> MolUtil.extractTrimmedPropertyOrNull(molecule, propName))
                .collect(toList());
    }

}
