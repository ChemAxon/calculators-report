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

package com.chemaxon.calculations.reports.report;

import com.chemaxon.calculations.reports.statistics.RangedDeltaStat.RangedDeltaResult;
import java.util.List;

/**
 * @author Daniel Hari
 */
public class MultiSetReport {

    public String calculatorName;
    public List<MultiSetReportItem> items;
    
    public static class MultiSetReportItem {
        public String calculatorVersion;
        public double rmse;
        public double pearson;
        public RangedDeltaResult rangedDelta;
        
    }
    
}
