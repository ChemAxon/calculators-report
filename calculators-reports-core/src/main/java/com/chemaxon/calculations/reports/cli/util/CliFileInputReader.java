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

package com.chemaxon.calculations.reports.cli.util;

import com.chemaxon.calculations.reports.reader.AbstractReader;
import com.chemaxon.calculations.reports.reader.CSVReader;
import com.chemaxon.calculations.reports.reader.ReaderException;
import com.chemaxon.calculations.reports.reader.SDFReader;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Reads input from various file format.
 * 
 * @author Daniel Hari
 */
public class CliFileInputReader {

    /**
     * @throws ReaderException
     */
    public static List<Pair<String, List<String>>> readInput(String inputFilePath, String... propertyNames) {
        InputStream inputStream = CliUtil.getStreamOrThrowFromFile(inputFilePath);
        String extension = FilenameUtils.getExtension(inputFilePath).toLowerCase();
    
        return createReader(inputStream, extension, propertyNames)
                .read();
    }

    private static AbstractReader createReader(InputStream inputStream, String extension, String[] propertyNames) {
        if (extension.equals("csv")) {
            return CSVReader.from(inputStream)
                    .molColumn(0)
                    .valueColumns(propertyNames);
        }
        if (extension.equals("sdf")) {
            return SDFReader.from(inputStream)
                    .valuePropNames(propertyNames);
        } else {
            throw CliExitException.withMessage("file extension is not .csv or .sdf");
        }
    }

}
