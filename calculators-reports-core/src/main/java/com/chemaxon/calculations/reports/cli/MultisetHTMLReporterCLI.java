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

package com.chemaxon.calculations.reports.cli;

import com.beust.jcommander.Parameter;
import com.chemaxon.calculations.reports.cli.paramconverter.RangedDeltaParamsConverter;
import com.chemaxon.calculations.reports.cli.util.AbstractCliParams;
import com.chemaxon.calculations.reports.cli.util.CliExitException;
import com.chemaxon.calculations.reports.cli.util.CliUtil;
import com.chemaxon.calculations.reports.dataset.Dataset;
import com.chemaxon.calculations.reports.dataset.serialization.SerializationException;
import com.chemaxon.calculations.reports.dataset.serialization.json.DatasetJsonSerializer;
import com.chemaxon.calculations.reports.html.dto.HtmlReportData;
import com.chemaxon.calculations.reports.reader.ReaderException;
import com.chemaxon.calculations.reports.report.MultiSetReport;
import com.chemaxon.calculations.reports.report.MultisetReporter;
import com.chemaxon.calculations.reports.report.formatter.html.MultisetReportHtmlFormatter;
import com.chemaxon.calculations.reports.statistics.RangedDeltaStat.RangedDeltaRanges;
import com.chemaxon.calculations.reports.template.ReportTemplater;
import com.chemaxon.calculations.reports.util.ChemicalTermFunctions.ChemTermException;
import com.chemaxon.calculations.reports.util.FileUtil;
import com.chemaxon.calculations.reports.util.GsonUtil;
import com.chemaxon.calculations.reports.util.InvalidMoleculeFormatException;
import java.io.UncheckedIOException;
import java.util.List;
import static java.util.stream.Collectors.toList;

/**
 * @author Daniel Hari
 */
public class MultisetHTMLReporterCLI {

    private CliParams params;

    private static class CliParams extends AbstractCliParams {

        @Parameter(description = "[Multiple input dataset json files separated with space]", required = true)
        private List<String> inputFiles;

        @Parameter(names = { "-d", "--distRanges" }, description = "Delta distribution range edges (eg: Δ < 0.5 | 0.5 < Δ < 1.0 | Δ > 1.0).", required = false,
                converter = RangedDeltaParamsConverter.class)
        private RangedDeltaRanges distRanges = new RangedDeltaRanges(0.5, 1.0);

        @Parameter(names = { "-t", "--template" }, description = "Template html file. (Not mandatory)", required = false)
        private String templateFile;
        
        @Parameter(names = { "-o", "--output" }, description = "Output json file name. (If not specified, output to console)", required = false)
        private String outputFile;
        
    }

    public static void main(String[] args) {
        CliUtil.parseParamsAndRunOrHelpOrExit(args, new CliParams(), MultisetHTMLReporterCLI::new);
    }

    public MultisetHTMLReporterCLI(CliParams params) {
        this.params = params;
        run();
    }

    private void run() {
        try {

            reportOrThrow();

        } catch (ReaderException e) {
            throw CliExitException.withMessage("Read error: " + e.getMessage());
        } catch (SerializationException e) {
            throw CliExitException.withMessage("Input dataset error (file corrupted?): " + e.getMessage());
        } catch (ChemTermException e) {
            throw CliExitException.withMessage("Chemterm expression error: " + e.getMessage());
        } catch (InvalidMoleculeFormatException e) {
            throw CliExitException.withMessage("Invalid molecule format: " + e.getMessage());
        } catch (UncheckedIOException e) {
            throw CliExitException.withMessage("File IO error: " + e.getMessage());
        }
    }

    private void reportOrThrow() {
        List<Dataset> datasets = readDatasets();
        Dataset firstDataset = datasets.get(0);
        
        MultiSetReport report = new MultisetReporter(datasets, firstDataset.calcType, params.distRanges).report();
        HtmlReportData htmlReportData = new MultisetReportHtmlFormatter(report).format();
        
        String json = GsonUtil.toJson(htmlReportData);
        writeOutput(useTemplateIfNeeded(json));
    }


    private List<Dataset> readDatasets() {
        return params.inputFiles.stream()
            .map(f -> toDataset(f))
            .collect(toList());
    }

    private Dataset toDataset(String inputJsonFile) {
        return new DatasetJsonSerializer()
                .deserialize(CliUtil.readFileToString(inputJsonFile));
    }

    private String useTemplateIfNeeded(String json) {
        if(params.templateFile != null) {
            return ReportTemplater.replace(FileUtil.readToString(params.templateFile), json);
        } else {
            return json;
        }
    }

    private void writeOutput(String content) {
        if(params.outputFile == null) {
            System.out.println(content);
        } else {
            FileUtil.writeFile(params.outputFile, content);
        }
    }

}
