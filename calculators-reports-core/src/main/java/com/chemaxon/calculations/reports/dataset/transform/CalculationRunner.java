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

import chemaxon.struc.Molecule;
import com.chemaxon.calculations.reports.dataset.DatasetRecord;
import com.chemaxon.calculations.reports.util.InvalidMoleculeFormatException;
import com.chemaxon.calculations.reports.util.MoleculeFormats;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daniel Hari
 */
public class CalculationRunner {
    private static final Logger logger = LoggerFactory.getLogger(CalculationRunner.class);

    private List<DatasetRecord> records;
    private Function<Molecule, Double> calculatorFunction;

    public CalculationRunner(List<DatasetRecord> records, Function<Molecule, Double> calculatorFunction) {
        this.records = records;
        this.calculatorFunction = calculatorFunction;
    }

    public static List<DatasetRecord> on(List<DatasetRecord> records, Function<Molecule, Double> calculatorFunction) {
        return new CalculationRunner(records, calculatorFunction).run();
    }

    
    /**
     * Can throw any error that function throws.
     * @throws InvalidMoleculeFormatException for parsing errors of mol.
     */
    public List<DatasetRecord> run() {
        updateRecords();
        return records;
    }
    
    private void updateRecords() {
        IntStream.range(0, records.size())
                .forEach(i -> updateRecord(records.get(i), i));
    }
    
    private void updateRecord(DatasetRecord record, int index) {
        record.actValue = calcOrNaN(index, record.molecule);
    }

    /**
     * @return value or NaN if function thrown
     */
    private Double calcOrNaN(int index, String originalMol) {
        Molecule molecule = importOrThrow(index, originalMol);
        try {
            return calculatorFunction.apply(molecule);  //can throw any
        } catch (Exception e) { //calculator throw 
            logger.warn(e.getMessage() + " at index " + index);
            return Double.NaN;
        }
    }

    private Molecule importOrThrow(int index, String originalMol) {
        try {
            return MoleculeFormats.importMolOrThrow(originalMol);
        } catch (InvalidMoleculeFormatException e) {
            throw new InvalidMoleculeFormatException(e.getMessage() + " at index " + index, e); //add index to error message
        }
    }

}
