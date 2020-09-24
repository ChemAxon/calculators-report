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

package com.chemaxon.calculations.reports.util;

import chemaxon.formats.MolExporter;
import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;
import chemaxon.struc.MPropertyContainer;
import chemaxon.struc.Molecule;
import chemaxon.util.Errors;
import com.chemaxon.calculations.io.Segmenters;
import com.google.common.base.Charsets;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import static java.util.stream.Collectors.toMap;
import org.apache.commons.io.IOUtils;

/**
 * static tool
 * 
 * @author Daniel Hari
 */
public final class MoleculeFormats {

    /**
     * @throws InvalidMoleculeFormatException if molecule input format is invalid
     * @throws InvalidMoleculeFormatException if molecule output format is invalid
     */
    public static String convertFormat(String structure, String toFormat) {
        try {
            return convertFormat(MolImporter.importMol(structure), toFormat);
        } catch (MolFormatException e) {
            throw new InvalidMoleculeFormatException("Invalid molecule input format.", e);
        }
    }
    
    /**
     * @throws InvalidMoleculeFormatException if molecule format is invalid or other export problem
     */
    public static String convertFormat(Molecule structure, String toFormat) {
        try {
            return MolExporter.exportToFormat(structure, toFormat);
        } catch (IOException e) {
            throw new InvalidMoleculeFormatException("Molecule export error to: " + toFormat, e);
        }
    }

    /**
     * Returns iterator that runs on the segments of the input molecule format.
     * 
     * @param format molecule file format (supported formats: smiles, sdf)
     * 
     * @throws UncheckedIOException if I/O error occurred
     */
    public static Iterator<String> recordIterator(InputStream is, String format) {
            return Segmenters.ofFormat(format)
                    .plainStringSegments(Errors.unCheckIO(() -> IOUtils.lineIterator(is, Charsets.UTF_8)));
    }

    /**
     * @return a map with original ordering of mol properties
     * @throws InvalidMoleculeFormatException for wrong format
     */
    public static Map<String, String> propertiesFrom(String sdfRecord) {
        MPropertyContainer properties = importMolOrThrow(sdfRecord).properties();
        
        return Collections.unmodifiableMap(Arrays.stream(properties.getKeys())
                .collect(toMap(key -> key,
                        (key) -> properties.get(key).getPropValue().toString(),
                        (e1, e2) -> e1, LinkedHashMap::new))
                );
        
    }

    /**
     * @throws InvalidMoleculeFormatException for wrong format.
     */
    public static Molecule importMolOrThrow(String mol) {
        try {
            return MolImporter.importMol(mol);
        } catch (MolFormatException e) {
            throw new InvalidMoleculeFormatException("Can't parse molecule format: " + mol, e);
        }
    }

    public static boolean isValid(String mol) {
        try {
            MolImporter.importMol(mol);
            return true;
        } catch (MolFormatException e) {
            return false;
        }
    }

}
