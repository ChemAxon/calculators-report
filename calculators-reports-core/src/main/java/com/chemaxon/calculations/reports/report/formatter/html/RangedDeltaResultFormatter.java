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

package com.chemaxon.calculations.reports.report.formatter.html;

import com.chemaxon.calculations.reports.html.dto.PieChart;
import com.chemaxon.calculations.reports.html.dto.Table;
import com.chemaxon.calculations.reports.html.dto.Table.Column;
import com.chemaxon.calculations.reports.html.dto.Table.DataType;
import com.chemaxon.calculations.reports.statistics.RangedDeltaStat.RangedDeltaResult;
import java.text.MessageFormat;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author Daniel Hari
 */
class RangedDeltaResultFormatter {

    public static String mdFormatLikeTable(double thresholdLow, double thresholdHigh, RangedDeltaResult result) {
        String mdtext = 
                "**Ranged delta distribution:**\n" +
                MessageFormat.format("Δ < {0} | {0} < Δ < {1} |  Δ > {1}", thresholdLow,  thresholdHigh) + " \n" +
                String.format("%.0f%%   %.0f%%   %.0f%%", result.underRate * 100, result.inRate * 100, result.overRate * 100);
        return mdtext;
    }
    
    public static String mdFormatLikeProperties(RangedDeltaResult result) {
        String mdtext = 
                "**Ranged delta distribution**" +
                        String.format("\n\t *      Δ < %s      : **%.0f%%**", result.params.thresholdLow, result.underRate * 100) +
                        String.format("\n\t * %s < Δ < %s : **%.0f%%**", result.params.thresholdLow, result.params.thresholdHigh, result.inRate * 100) + 
                        String.format("\n\t *      Δ > %s      : **%.0f%%**", result.params.thresholdHigh, result.overRate * 100);
        return mdtext;
    }
    
    public static Pair<Table, PieChart> formatAsPieChart(RangedDeltaResult result) {
        Table table = new Table("pie");
        table.addColumn(new Column(DataType.STRING, "label", "Range"));
        table.addColumn(new Column(DataType.DOUBLE, "value", ""));
        table.addColumn(new Column(DataType.DOUBLE, "upperLimit", ""));
        table.addColumn(new Column(DataType.STRING, "color", "")); //+color

        table.addDataRow(String.format("Δ < %s", result.params.thresholdLow), result.underRate, result.params.thresholdLow, "#67b95e");
        table.addDataRow(String.format("%s < Δ < %s", result.params.thresholdLow, result.params.thresholdHigh), result.inRate, result.params.thresholdHigh, "#ecb32a");
        table.addDataRow(String.format("Δ > %s", result.params.thresholdHigh), result.overRate, null, "#E46651");
        
        PieChart pieChart = new PieChart();
        pieChart.tableId = "pie";
        pieChart.labelColumnId = "label";
        pieChart.valueColumnId = "value";
        pieChart.colorColumnId = "color";
        return Pair.of(table, pieChart);
    }

}
