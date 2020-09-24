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

import chemaxon.util.Errors;
import com.google.common.collect.Streams;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import static java.util.stream.Collectors.toList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author Daniel Hari
 */
public class CSVReader extends AbstractReader {

    public static CSVReader from(InputStream csvStream) {
        CSVParser csvParser = Errors.unCheck(() -> CSVParser.parse(csvStream, StandardCharsets.UTF_8, CSVFormat.EXCEL
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()));
    
        return new CSVReader(csvParser);
    }

    private CSVReader(CSVParser csvParser) {
        this.csvParser = csvParser;
    }

    private CSVParser csvParser;
    private Function<CSVRecord, String> molGetter;
    private Function<CSVRecord, List<String>> valueGetter;

    /** @param colIndex 0 based */
    public CSVReader molColumn(int colIndex) {
        molGetter = (rec) -> rec.get(colIndex);
        return this;
    }

    public CSVReader molColumn(String colName) {
        molGetter = rec -> rec.get(colName);
        return this;
    }

    public CSVReader valueColumns(int... colIndexes) {
        valueGetter = (rec) -> Arrays.stream(colIndexes).mapToObj(rec::get).collect(toList());
        return this;
    }

    public CSVReader valueColumns(String... colNames) {
        valueGetter = (rec) -> Arrays.stream(colNames).map(rec::get).collect(toList());
        return this;
    }

    @Override
    public List<Pair<String, List<String>>> read() {
        validate();
        try {
            return doRead();
        } catch (IllegalArgumentException e) {
            throw new ReaderException(e.getMessage());
        }
    }

    private void validate() {
        if (molGetter == null || valueGetter == null) {
            throw new IllegalStateException("mol or value column is not setted.");
        }
    }

    private List<Pair<String, List<String>>> doRead() {
        return Streams.stream(csvParser).map((record) -> {
            String mol = molGetter.apply(record);
            List<String> value = valueGetter.apply(record);
            return Pair.of(mol, value);
        }).collect(toList());
    }
    
}
