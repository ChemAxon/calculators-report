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

import com.chemaxon.calculations.reports.html.dto.Content;
import com.chemaxon.calculations.reports.html.dto.Histogram;
import com.chemaxon.calculations.reports.html.dto.Histogram.DataSet;
import com.chemaxon.calculations.reports.html.dto.Histogram.ViewType;
import com.chemaxon.calculations.reports.html.dto.HtmlReportData;
import com.chemaxon.calculations.reports.html.dto.Table;
import com.chemaxon.calculations.reports.html.dto.Table.Column;
import com.chemaxon.calculations.reports.html.dto.Table.DataType;
import com.chemaxon.calculations.reports.report.MultiSetReport;
import com.chemaxon.calculations.reports.report.MultiSetReport.MultiSetReportItem;
import com.chemaxon.calculations.reports.statistics.RangedDeltaStat.RangedDeltaRanges;

/**
 * @author Daniel Hari
 */
public class MultisetReportHtmlFormatter {

    private MultiSetReport report;
    private HtmlReportData htmlReport = new HtmlReportData();

    public MultisetReportHtmlFormatter(MultiSetReport report) {
        this.report = report;
    }

    public HtmlReportData format() {
        htmlReport.addTable(createTable());
        htmlReport.addContent(createStackedHistogram());

        return htmlReport;
    }


    private Table createTable() {
        Table table = new Table("histtable");

        MultiSetReportItem firstItem = report.items.get(0);
        RangedDeltaRanges rangedDeltaRanges = firstItem.rangedDelta.params;

        table.addColumn(new Column(DataType.STRING, "VERSION", "Jchem Version"));
        table.addColumn(new Column(DataType.DOUBLE, "D0", "Δ < " + rangedDeltaRanges.thresholdLow));
        table.addColumn(new Column(DataType.DOUBLE, "D1", rangedDeltaRanges.thresholdLow + " < Δ < " + rangedDeltaRanges.thresholdHigh));
        table.addColumn(new Column(DataType.DOUBLE, "D2", rangedDeltaRanges.thresholdHigh + " < Δ"));

        for (MultiSetReportItem item : report.items) {
            table.addDataRow(item.calculatorVersion, item.rangedDelta.underRate, item.rangedDelta.inRate, item.rangedDelta.overRate);
        }

        return table;
    }

    private Content createStackedHistogram() {
        Histogram histogram = new Histogram(ViewType.STACKED);

        histogram.title = "History of accuracy result changes for LTS versions";
        histogram.xAxis.title = "Version";
        histogram.yAxis.title = "Ranged delta distribution";

        histogram.tableId = "histtable";

        histogram.xColumnId = "VERSION";

        histogram.dataSets.add(new DataSet("#67b95e", "D0"));
        histogram.dataSets.add(new DataSet("#ecb32a", "D1"));
        histogram.dataSets.add(new DataSet("#E46651", "D2"));

        return histogram;
    }

}
