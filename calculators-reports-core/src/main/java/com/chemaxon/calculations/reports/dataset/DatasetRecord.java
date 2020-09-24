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

/**
 * @author Daniel Hari
 */
public class DatasetRecord {
    public String id; //mol id or index
    public String molecule; //can be smiles or other format
    public Double expValue; //experimental Value //maybe ref value?
    public Double actValue; //actual Value

    
    public DatasetRecord(String id, String molecule, Double expValue, Double actValue) {
        this.id = id;
        this.molecule = molecule;
        this.expValue = expValue;
        this.actValue = actValue;
    }

}