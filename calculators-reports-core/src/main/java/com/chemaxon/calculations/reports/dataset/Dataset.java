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

package com.chemaxon.calculations.reports.dataset;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds records of a molecule set with reference values for a specific type of calculation, and
 * a specific version of calculator's actual value.
 * 
 * @author Daniel Hari
 */
public class Dataset {
    public String molsetName; //can be null
    public String calcType;
    public String calcVersion;
    public List<DatasetRecord> records = new ArrayList<>();

    public Dataset() {}
    public Dataset(String molsetName, String calcType, String calcVersion, List<DatasetRecord> records) {
        this.molsetName = molsetName;
        this.calcType = calcType;
        this.calcVersion = calcVersion;
        this.records = records;
    }

}
