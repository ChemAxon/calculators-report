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

package com.chemaxon.calculations.reports.html.dto;

import com.google.common.collect.Lists;
import java.util.List;

/**
 * @author Daniel Hari
 */
public class ScatterPlot extends Content {

    public String title;

    public Axis xAxis = new Axis();
    public Axis yAxis = new Axis();
    //public boolean lockAxisRanges;
    public PieChart diagonalRanges = null; //nullable
    public List<DataSet> dataSets = Lists.newArrayList();
    
    public ScatterPlot() {
        super(Content.Type.SCATTERPLOT);
    }

    public ScatterPlot(String title, String xAxisTitle, String yAxisTitle) {
        this();
        this.title = title;
        this.xAxis.title = xAxisTitle;
        this.yAxis.title = yAxisTitle;
    }

    public static class DataSet {
        public String tableId;
        public String xColumnId;
        public String yColumnId;
        public String color;
        public String displayName; //nullable
        
        public DataSet() {}
        public DataSet(String tableId, String xColumnId, String yColumnId, String color) {
            super();
            this.tableId = tableId;
            this.xColumnId = xColumnId;
            this.yColumnId = yColumnId;
            this.color = color;
        }
    }

    public DataSet addDataSet(DataSet dataSet) {
        dataSets.add(dataSet);
        return dataSet;
    }

}
