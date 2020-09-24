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
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daniel Hari
 */
public class DatasetFilters {
    private static final Logger logger = LoggerFactory.getLogger(DatasetFilters.class);

    /**
     * Removes records with invalid molecule formats.
     */
    public static List<DatasetRecord> removeInvalidMolecules(List<DatasetRecord> unfiltered) {
        return IntStream.range(0, unfiltered.size())
                .filter(i -> isValidOrWarn(unfiltered.get(i).molecule, i))
                .mapToObj(unfiltered::get)
                .collect(toList());
    }

    private static boolean isValidOrWarn(String mol, int index) {
        try {
            MoleculeFormats.importMolOrThrow(mol);
            return true;
        } catch (InvalidMoleculeFormatException e) {
            logger.warn(e.getMessage() + " at index " + index);
            return false;
        }
    }

    public static List<DatasetRecord> removeRecordsWithNullOrNaNValues(List<DatasetRecord> records) {
        return records.stream()
                .filter(r -> !isNullOrNaN(r.actValue))
                .filter(r -> !isNullOrNaN(r.expValue))
                .collect(toList());
    }

    private static boolean isNullOrNaN(Double value) {
        return value == null || !Double.isFinite(value);
    }

}
