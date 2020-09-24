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
public class Histogram extends Content {

    public String title;
    
    public Axis xAxis = new Axis();
    public Axis yAxis = new Axis();
    public ViewType viewType;

    public String tableId;
    public String xColumnId; //the column name in the table that holds all the x values
    
    public List<DataSet> dataSets = Lists.newArrayList();
    
    public Histogram(ViewType viewType) {
        super(Content.Type.HISTOGRAM);
        this.viewType = viewType;
    }

    public static class DataSet {
        public String color;
        public String yColumnId; //the column id in the table that holds all the y values of this set
        public DataSet(String color, String yColumnId) {
            this.color = color;
            this.yColumnId = yColumnId;
        }
    }

    public enum ViewType {NORMAL, STACKED}
    
}
