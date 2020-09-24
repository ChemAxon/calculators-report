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

package com.chemaxon.calculations.reports.dataset.transform;

import com.chemaxon.calculations.reports.dataset.DatasetRecord;
import com.chemaxon.calculations.reports.util.InvalidMoleculeFormatException;
import com.chemaxon.calculations.reports.util.MoleculeFormats;
import java.math.BigDecimal;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author Daniel Hari
 */
public class DatasetConverters {

    private static final String SMILES_FORMAT = "SMILES";
    private static final String CXSMILES_FORMAT = "cxsmiles:-H";

    /**
     * Replaces mols with smiles.
     * 
     * If smiles is not applicable then cxsmiles, or an "invalid format" text. 
     * 
     * @param records is manipulated(WARN)
     */
    public static List<DatasetRecord> replaceWithSmilesOrCxSmiles(List<DatasetRecord> records) {
        records.forEach(r -> r.molecule = toSmilesOrCxSmiles(r.molecule));
        return records;
    }

    private static String toSmilesOrCxSmiles(String molecule) {
        try {
            return MoleculeFormats.convertFormat(molecule, SMILES_FORMAT);
        } catch (InvalidMoleculeFormatException e) {
            try {
                return MoleculeFormats.convertFormat(molecule, CXSMILES_FORMAT);
            } catch (InvalidMoleculeFormatException e2) {
                return "<Invalid molecule format>";
            }
        }
    }
    
    /** <exp, act> */
    public static List<Pair<Double, Double>> valuesToPairs(List<DatasetRecord> records) {
        return records.stream().map(r -> valuesToPair(r)).collect(toList());
    }
    
    private static Pair<Double, Double> valuesToPair(DatasetRecord record) {
        return Pair.of(record.expValue, record.actValue);
    }

    public static List<DatasetRecord> roundActValues(List<DatasetRecord> records, int decimalPlaces) {
        records.forEach(r -> r.actValue = roundValue(r.actValue, decimalPlaces));
        return records;
    }

    private static Double roundValue(Double value, int decimalPlaces) {
        return Double.isNaN(value)? value :
                new BigDecimal(String.valueOf(value)).setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
