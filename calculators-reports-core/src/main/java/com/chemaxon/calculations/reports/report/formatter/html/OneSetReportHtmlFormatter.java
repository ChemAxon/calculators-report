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

import com.chemaxon.calculations.reports.dataset.DatasetRecord;
import com.chemaxon.calculations.reports.html.dto.Content;
import com.chemaxon.calculations.reports.html.dto.Histogram;
import com.chemaxon.calculations.reports.html.dto.Histogram.DataSet;
import com.chemaxon.calculations.reports.html.dto.Histogram.ViewType;
import com.chemaxon.calculations.reports.html.dto.HtmlReportData;
import com.chemaxon.calculations.reports.html.dto.MDText;
import com.chemaxon.calculations.reports.html.dto.PieChart;
import com.chemaxon.calculations.reports.html.dto.ScatterPlot;
import com.chemaxon.calculations.reports.html.dto.Table;
import com.chemaxon.calculations.reports.html.dto.Table.Column;
import com.chemaxon.calculations.reports.html.dto.Table.DataType;
import com.chemaxon.calculations.reports.html.dto.TableContent;
import com.chemaxon.calculations.reports.report.OneSetReport;
import java.util.Arrays;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author Daniel Hari
 */
public class OneSetReportHtmlFormatter {

    private static final String RAW_TABLE = "raw_table";
    private static final String DIST_TABLE = "dist_table";
    private static final String DELTA_DIST_TABLE = "delta_dist_table";
    
    //in
    private OneSetReport report;
    private String molsetReference;     //nullable
    private String cmd;                 //nullable

    //out
    private HtmlReportData htmlReport = new HtmlReportData();

    public OneSetReportHtmlFormatter(OneSetReport report, 
            @Nullable String molsetReference, 
            @Nullable String cmd, @Nullable String xcmd) {
        this.report = report;
        this.molsetReference = molsetReference;
        this.cmd = cmd != null? cmd : xcmdToCmd(xcmd);
    }

    public HtmlReportData format() {
        htmlReport.addTable(createTable1());
        htmlReport.addTable(createDistTable());
        htmlReport.addTable(createDeltaDistTable());
        
        htmlReport.addContent(createTitle());
        htmlReport.addContent(createScatterPlot());
        htmlReport.addContent(createDistributionHistogram());
        htmlReport.addContent(createStatisticalParameters());
        addRangedDeltaPie();
        htmlReport.addContent(createDeltaDistributionHistogram());
        htmlReport.addContent(createTestSetParameters());
        addRawDataTable();
        htmlReport.addContent(createReproductionSteps());
       
        return htmlReport;
    }

    private void addRangedDeltaPie() {
        Pair<Table, PieChart> pieChart = RangedDeltaResultFormatter.formatAsPieChart(report.rangedDelta);
        htmlReport.addTable(pieChart.getLeft());
        htmlReport.addContent(pieChart.getRight());
    }
    
    private Table createTable1() {
        Table table = new Table(RAW_TABLE);
        
        table.addColumn(new Column(DataType.STRING, "MOL", "Molecule"));
        table.addColumn(new Column(DataType.DOUBLE, expColumnId(), "Experimental " + report.calculatorName));
        table.addColumn(new Column(DataType.DOUBLE, actColumnId(), "Predicted " + report.calculatorName));
        
        for (DatasetRecord inputRecord : report.records) {
            table.addDataRow(inputRecord.molecule, inputRecord.expValue, inputRecord.actValue);
        }
        return table;
    }
    
    private Table createDistTable() {
        Table table = new Table(DIST_TABLE);
        
        table.addColumn(new Column(DataType.DOUBLE, "EXP", "Experimental " + report.calculatorName));
        table.addColumn(new Column(DataType.DOUBLE, "COUNT", "Count"));
        
        for (Entry<Double, Integer> entry : report.distributionMap.entrySet()) {
            table.addDataRow(entry.getKey(), entry.getValue());
        }
        return table;
    }

    private Table createDeltaDistTable() {
        Table table = new Table(DELTA_DIST_TABLE);
        
        table.addColumn(new Column(DataType.DOUBLE, "DELTA", "Deviation from observed values"));
        table.addColumn(new Column(DataType.DOUBLE, "FREQ", "Frequency"));
        
        for (Entry<Double, Integer> entry : report.deltaDistributionMap.entrySet()) {
            table.addDataRow(entry.getKey(), entry.getValue());
        }
        return table;
    }
    
    private Content createDistributionHistogram() {
        Histogram histogram = new Histogram(ViewType.NORMAL);
        histogram.title = "Distribution of experimental " + report.calculatorName + " values";
        histogram.xAxis.title = "Binned observed values ";
        histogram.yAxis.title = "Frequency";
        histogram.tableId = DIST_TABLE;
        histogram.xColumnId = "EXP";
        histogram.dataSets.add(new DataSet("#1286c4", "COUNT"));
        return histogram;
    }
    
    private Content createDeltaDistributionHistogram() {
        Histogram histogram = new Histogram(ViewType.NORMAL);
        histogram.title = "Distribution of delta values from experimental data";
        histogram.xAxis.title = "delta (Δ)";
        histogram.yAxis.title = "Frequency";
        histogram.tableId = DELTA_DIST_TABLE;
        histogram.xColumnId = "DELTA";
        histogram.dataSets.add(new DataSet("#1286c4", "FREQ"));
        return histogram;
    }

    private Content createTitle() {
        return new MDText(
        		"# Accuracy Report: " + report.calculatorName + " prediction" + "\n"
        		+ "## Table of contents \n "
				+ "### Experimental vs predicted " + report.calculatorName + " \n  "
        		+ "### Distribution of experimental " + report.calculatorName + " values \n  "
				+ "### Statistical parameters  \n "
        		+ "### Test set parameters  \n  " 
				+ "### Raw Data  \n  " 
        		+ "### Reproduce result using the CLI Parameters  \n  " 
        		+ "### History of accuracy result changes for LTS versions  \n  "
        		);
    }

    private ScatterPlot createScatterPlot() {
        ScatterPlot scatterPlot = new ScatterPlot(
                "Experimental vs predicted " + report.calculatorName, 
                "Experimental", "Predicted");
        scatterPlot.xAxis.min = report.drawBounds.expMin;
        scatterPlot.xAxis.max = report.drawBounds.expMax;
        scatterPlot.yAxis.min = report.drawBounds.actMin;
        scatterPlot.yAxis.max = report.drawBounds.actMax;
        
        scatterPlot.addDataSet(new ScatterPlot.DataSet(RAW_TABLE, expColumnId(), actColumnId(), "#1286c4"));
        scatterPlot.diagonalRanges = scatterplotDiagonalRanges();
        return scatterPlot;
    }

    private PieChart scatterplotDiagonalRanges() {
        PieChart diagonalRanges = new PieChart();
        diagonalRanges.title = "";
        diagonalRanges.tableId = "pie";
        diagonalRanges.labelColumnId = "label";
        diagonalRanges.valueColumnId = "upperLimit";
        diagonalRanges.colorColumnId = "color";
        return diagonalRanges;
    }

    private Content createStatisticalParameters() {
        return new MDText(
                "# Statistical parameters \n"
                + " * " + createRMSEText() + "\n"
                + " * " + createPearsonText() + "\n"
                + " * " + createRangedDeltaText() + " \n"
                );
    }

    private String createRMSEText() {
        return String.format(" **RMSE**: %.2f", report.rmse);
    }

    private String createPearsonText() {
        return String.format(" **Pearson correlation**: %.2f", report.pearson);
    }

    private String createRangedDeltaText() {
        return RangedDeltaResultFormatter.mdFormatLikeProperties(report.rangedDelta);
    }

    private Content createTestSetParameters() {
        String mdtext = "# Test set parameters \n "
                + " * **n**: " + report.records.size() + "\n "
                + (molsetReference == null ? "" : " * **Reference DOI**: " + molsetReference + "\n");
        return new MDText(mdtext);
    }
    
    private Content createReproductionSteps() {
        String mdtext = "# CLI Parameters \n "
        		+ "This report was generated using the **" + report.calculatorName +"** predictor, version **" + report.calculatorVersion + "**. "
        		+ "You can use the **cxcalc** command line tool to reproduce the results above.  \n  \n  Example: `" + cmd + "`  \n\n "
        		+ "To read more about our CLI tool, [click here](https://docs.chemaxon.com/display/docs/cxcalc+command+line+tool). "
        		+" The command line tool is available for download within the [JChem Suite](https://chemaxon.com/download?dl=%2Fdata%2Fdownload%2Fjchem%2F" + report.calculatorVersion + "). \n";
        return new MDText(mdtext);
    }

    private String xcmdToCmd(String xcmd) {
        return "cxcalc " + report.calculatorName + nullable(xcmd) + " input.smi";
    }

    /**
     * @return string or empty string
     */
    private static String nullable(String nullableString) {
        return nullableString == null ? "" : " " + nullableString;
    }

    private void addRawDataTable() {       
        TableContent table = new TableContent();
        table.tableId = RAW_TABLE;
        table.columnIds = Arrays.asList("MOL", expColumnId(), actColumnId());
        table.title = "Raw data";
        htmlReport.addContent(table);
    }

    private String expColumnId() {
        return "EXP_" + report.calculatorName;
    }

    private String actColumnId() {
        return "ACT_" + report.calculatorName;
    }

}
