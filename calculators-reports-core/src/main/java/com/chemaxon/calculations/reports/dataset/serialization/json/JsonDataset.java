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

package com.chemaxon.calculations.reports.dataset.serialization.json;

import com.google.common.collect.Lists;
import java.util.List;

/**
 * @author Daniel Hari
 */
class JsonDataset {

    public String molsetName;
    public String calcType;
    public String calcVersion;
    
    public JsonDataset(String molsetName, String calcType, String calcVersion) {
        this.calcType = calcType;
        this.calcVersion = calcVersion;
        this.molsetName = molsetName;
    }
    
    public List<String> ids = Lists.newArrayList();
    public List<String> molecules = Lists.newArrayList();
    public List<Double> expValues = Lists.newArrayList(); //experimental Value
    public List<Double> actValues = Lists.newArrayList(); //actual Value
    
}
