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

import com.chemaxon.calculations.reports.dataset.DatasetRecord;
import com.chemaxon.calculations.reports.statistics.RangedDeltaStat.RangedDeltaResult;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Hari
 */
public class OneSetReport {

    public List<DatasetRecord> records;
    public Map<Double, Integer> distributionMap;
    public Map<Double, Integer> deltaDistributionMap;
    public String calculatorName;
    public String calculatorVersion;
    public double rmse;
    public double pearson;
    public RangedDeltaResult rangedDelta;
    public DrawBounds drawBounds;
    
    public static class DrawBounds {
        public double expMin;
        public double expMax;
        public double actMin;
        public double actMax;
    }
    
}
