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

import com.chemaxon.calculations.TestUtil;
import com.chemaxon.calculations.reports.html.dto.Table.Column;
import com.chemaxon.calculations.reports.html.dto.Table.DataType;
import org.junit.Test;

/**
 * Not test, just try
 * 
 * @author Daniel Hari
 */
public class HtmlReportDataTest {
    
    @Test
    public void tryTable() throws Exception {

        HtmlReportData reportData = new HtmlReportData();
        
        Table table = reportData.addTable(new Table("table1"));
        table.addColumn(new Column(DataType.STRING, "MOL", "Molecule"));
        table.addColumn(new Column(DataType.DOUBLE, "LOGP_EXP", "Experimental LogP"));
        table.addColumn(new Column(DataType.DOUBLE, "LOGP_ACT", "Actual LogP"));
        
        table.addDataRow("C", 1.25, 1.35);
        table.addDataRow("CC", 2.25, 2.35);
        table.addDataRow("CCC", 3.25, 3.35);
        
        TestUtil.printAsJson(reportData);
    }
    
    @Test
    public void tryScatterplot() throws Exception {
        HtmlReportData reportData = new HtmlReportData();
        
        ScatterPlot scatterPlot = reportData.addContent(new ScatterPlot("Exp logP vs act logP", "Experimental", "Actual"));
        scatterPlot.addDataSet(new ScatterPlot.DataSet("table1", "LOGP_EXP", "LOGP_ACT", "RED"));
        
        TestUtil.printAsJson(reportData);
    }
    
}
