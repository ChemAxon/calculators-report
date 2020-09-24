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

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.chemaxon.calculations.reports.cli.paramconverter.RangedDeltaParamsConverter;
import com.chemaxon.calculations.reports.cli.paramconverter.SemicolonSplitterConverter;
import com.chemaxon.calculations.reports.cli.util.AbstractCliParams;
import com.chemaxon.calculations.reports.cli.util.CliExitException;
import com.chemaxon.calculations.reports.cli.util.CliFileInputReader;
import com.chemaxon.calculations.reports.cli.util.CliUtil;
import com.chemaxon.calculations.reports.dataset.Dataset;
import com.chemaxon.calculations.reports.dataset.DatasetRecord;
import com.chemaxon.calculations.reports.dataset.builder.DatasetRecordsBuilder;
import com.chemaxon.calculations.reports.dataset.serialization.json.DatasetJsonSerializer;
import com.chemaxon.calculations.reports.dataset.transform.CalculationRunner;
import com.chemaxon.calculations.reports.dataset.transform.DatasetConverters;
import com.chemaxon.calculations.reports.dataset.transform.DatasetFilters;
import com.chemaxon.calculations.reports.html.dto.HtmlReportData;
import com.chemaxon.calculations.reports.reader.ReaderException;
import com.chemaxon.calculations.reports.report.MultiSetReport;
import com.chemaxon.calculations.reports.report.MultisetReporter;
import com.chemaxon.calculations.reports.report.OneSetReport;
import com.chemaxon.calculations.reports.report.OneSetReporter;
import com.chemaxon.calculations.reports.report.formatter.html.MultisetReportHtmlFormatter;
import com.chemaxon.calculations.reports.report.formatter.html.OneSetReportHtmlFormatter;
import com.chemaxon.calculations.reports.statistics.RangedDeltaStat.RangedDeltaRanges;
import com.chemaxon.calculations.reports.template.ReportTemplater;
import com.chemaxon.calculations.reports.util.ChemicalTermFunctions;
import com.chemaxon.calculations.reports.util.ChemicalTermFunctions.ChemTermException;
import com.chemaxon.calculations.reports.util.FileUtil;
import com.chemaxon.calculations.reports.util.GsonUtil;
import com.chemaxon.calculations.reports.util.InvalidMoleculeFormatException;
import java.io.UncheckedIOException;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author Daniel Hari
 */
public class HTMLReporterCLI {

    private CliParams params;
    private Mode mode;
    private String calcVersion; //eg: argon, 19.25.0
    private String calcType;    //eg: logS, logP
    private String molsetName;  //eg: martel set
    
    private enum Mode {
        DATASET_SOURCE,
        OTHER_SOURCE_ACT_INCLUDED,
        OTHER_SOURCE_ACT_CALCULATED
    }

    private static class CliParams extends AbstractCliParams {

        @Parameter(description = "[Input structure file CSV, SDF or .json (dataset)]", required = true)
        private String inputFile;

        // Note that latest jchem at the time of this project publication (20.18) ships jcommander 1.32 which does not support "order"
        @Parameter(names = { "-r", "--ref-prop" }, description = "Property or column name in sdf or csv that holds reference data.", required = false /*, order = 1 */)
        private String propertyName;

        @Parameter(names = { "-a", "--act-prop" }, description = "Property or column name in sdf or csv that holds actual data (if --chemterm option is not set).", required = false)
        private String propertyNameActual;

        @Parameter(names = { "-v", "--act-prop-version" }, description = "Calculator version in output data (needed in case of --actual option is used).", required = false)
        private String version;

        @Parameter(names = { "-c", "--act-chemterm" }, description = "Chemical Term to evaluate on molecules for actual data (if --actual option is not set).", required = false)
        private String chemTerm;
        
        @Parameter(names = { "--report-molset-ref" }, description = "Reference of the molecule set (e.g.: DOI or link). ", required = false)
        private String molsetRef;

        @Parameter(names = { "-n", "--report-calc-name" }, description = "Calculator name to include in output (if not set, default will be the --ref-prop parameter).", required = false)
        private String calcType;
        
        @Parameter(names = { "--report-cli" }, description = "Command line to display in report eg: 'cxcalc logS input.smi'.", required = false)
        private String cmd;
        
        @Parameter(names = { "--report-cli-extra" }, description = "Extra command line options to display in report eg: 'cxcalc logS <xcmd> input.smi (ignored if --cli-display is set).", required = false)
        private String xCmd;
        
        @Parameter(names = { "--report-distr-range" }, description = "Delta distribution thresholds (eg: Δ < 0.5 | 0.5 < Δ < 1.0 | Δ > 1.0).", required = false,
                converter = RangedDeltaParamsConverter.class)
        private RangedDeltaRanges distRanges = new RangedDeltaRanges(0.5, 1.0);

        @Parameter(names = { "--report-distr-bin" }, description = "The bin width of the delta distribution chart.", required = false)
        private double deltaBinWidth = 0.5;
        
        @Parameter(names = { "--report-historical-data" }, description = "Historical input dataset json files separated with semicolon.", required = false,
                listConverter = SemicolonSplitterConverter.class)
        private List<String> histFiles; //nullable
        
        @Parameter(names = { "-t", "--html-template" }, description = "Template html file (not mandatory).", required = false)
        private String templateFile;

        @Parameter(names = { "-o", "--output" }, description = "Output file name (default is stdout ff not specified).", required = false)
        private String outputFile;
        
        public void usage() {
            StringBuilder sb = new StringBuilder(
                      "This CLI generates a report from a source file that is either a \n"
                    + "   .json dataset, or\n"
                    + "   .csv or .sdf that holds ref value (-r), and actual value calculated by a provided chemterm (-c)\n"
                    + "   .csv or .sdf that holds ref value (-r), and actual value also (-a). Version also should be set in this case (-v)."
                    + "\n"
                    + "Examples:\n"
                    + "   html-reporter.sh dataset.json \n"
                    + "   html-reporter.sh -r \"logP\" -c \"logP()\" molecules.sdf \n"
                    + "   html-reporter.sh -r \"logPRef\" -a \"logPAct\" -v 1.0 molecules.csv \n"
                    + "\n");
            
            new JCommander(this).usage(sb);
            System.out.println(sb.toString());
        }
    }

    public static void main(String[] args) {
        CliUtil.parseParamsAndRunOrHelpOrExit(args, new CliParams(), p -> new HTMLReporterCLI(p));
    }

    public HTMLReporterCLI(CliParams params) {
        this.params = params;
        this.mode = validateParamsAndGetMode();
        System.out.println("Mode: " + mode + "\n");
        this.molsetName = FilenameUtils.getBaseName(params.inputFile);
        this.calcVersion = mode == Mode.OTHER_SOURCE_ACT_CALCULATED? 
                CliUtil.getCurrentCalcVersion() : params.version;  
        this.calcType = params.calcType != null ? params.calcType : params.propertyName;  

        run();
    }

    private Mode validateParamsAndGetMode() {
        String extension = FilenameUtils.getExtension(params.inputFile).toLowerCase();

        if (extension.equals("json")) {
            return Mode.DATASET_SOURCE;
        } else if (extension.equals("sdf") || extension.equals("csv")) {
            if (params.propertyName == null) {
                throw CliExitException.withMessage("-r is mandatory in case of .sdf or .csv");
            }
            if (params.chemTerm != null) {
                if (params.propertyNameActual != null) {
                    throw CliExitException.withMessage("-a should not be set if --chemterm setted.");
                }
                return Mode.OTHER_SOURCE_ACT_CALCULATED;

            } else if (params.propertyNameActual != null) {
                if (params.version == null) {
                    throw CliExitException.withMessage("version (-v) should be set if -a used.");
                }
                return Mode.OTHER_SOURCE_ACT_INCLUDED;
            } else {
                throw CliExitException.withMessage("one of -a or --chemterm should be set.");
            }
        } else {
            throw CliExitException.withMessage("input file can be only .json, .sdf or .csv");
        }
    }

    private void run() {
        try {
            
            reportOrThrow();

        } catch (ReaderException e) {
            throw CliExitException.withMessage("Read error: " + e.getMessage());
        } catch (ChemTermException e) {
            throw CliExitException.withMessage("Chemterm expression error: " + e.getMessage());
        } catch (InvalidMoleculeFormatException e) {
            throw CliExitException.withMessage("Invalid molecule format: " + e.getMessage());
        } catch (UncheckedIOException e) {
            throw CliExitException.withMessage("File IO error: " + e.getMessage());
        }
    }

    private void reportOrThrow() {
        Dataset dataset = getInputAsDataset(params.inputFile);
        
        if(dataset.records.isEmpty()) {
            throw CliExitException.withMessage("No records. (May be wrong property name?)");
        }
        
        OneSetReport oneSetReport = new OneSetReporter(dataset, params.distRanges, params.deltaBinWidth).report();
        HtmlReportData htmlReportData = new OneSetReportHtmlFormatter(oneSetReport, params.molsetRef, params.cmd, params.xCmd).format();
        
        if(params.histFiles != null) {
            htmlReportData = addMultisetReport(htmlReportData, dataset);
        }
        
        String json = GsonUtil.toJson(htmlReportData);
        String output = useTemplateIfNeeded(json);
        writeOutput(output);
    }

    private Dataset getInputAsDataset(String inputFile) {
        
        if(mode == Mode.DATASET_SOURCE) {
            return new DatasetJsonSerializer()
                    .deserialize(CliUtil.readFileToString(inputFile));
        } else {
            List<DatasetRecord> datasetRecords = readInputAsDatasetRecords();
            datasetRecords = DatasetConverters.replaceWithSmilesOrCxSmiles(datasetRecords);
            datasetRecords = DatasetFilters.removeRecordsWithNullOrNaNValues(datasetRecords);
            
            return new Dataset(molsetName, calcType, calcVersion, datasetRecords);
        }
    }

    private List<DatasetRecord> readInputAsDatasetRecords() {
        
        if (mode == Mode.OTHER_SOURCE_ACT_CALCULATED) {
            
            List<Pair<String, List<String>>> raw = CliFileInputReader.readInput(params.inputFile, params.propertyName);
            List<DatasetRecord> records = DatasetRecordsBuilder.from(raw)
                    .refValueIndex(0).build();
            
            records = DatasetFilters.removeInvalidMolecules(records);
            records = CalculationRunner.on(records, ChemicalTermFunctions.fromExpression(params.chemTerm));
            records = DatasetConverters.roundActValues(records, 2);
            
            return records;

        } else { //mode == Mode.OTHER_SOURCE_ACT_INCLUDED
            List<Pair<String, List<String>>> raw = CliFileInputReader.readInput(params.inputFile, params.propertyName, params.propertyNameActual);
            List<DatasetRecord> unfiltered = DatasetRecordsBuilder.from(raw)
                .refValueIndex(0)
                .actValueIndex(1)
                .build();
            
            return DatasetFilters.removeInvalidMolecules(unfiltered);
        }
    }

    private HtmlReportData addMultisetReport(HtmlReportData originalReport, Dataset currentDataset) {
        HtmlReportData multisetReport = createMultisetReport(currentDataset);
        
        multisetReport.tables.forEach(originalReport.tables::add);
        multisetReport.contents.forEach(originalReport.contents::add);
        
        return originalReport;
    }

    private HtmlReportData createMultisetReport(Dataset currentDataset) {
        List<Dataset> datasets = readHistoricalDatasets();
        datasets.add(currentDataset);
        Dataset firstDataset = datasets.get(0);
        
        MultiSetReport report = new MultisetReporter(datasets, firstDataset.calcType, params.distRanges).report();
        return new MultisetReportHtmlFormatter(report).format();
    }

    private List<Dataset> readHistoricalDatasets() {
        return params.histFiles.stream()
            .map(f -> toDataset(f))
            .collect(toList());
    }
    
    private Dataset toDataset(String datasetJsonFile) {
        return new DatasetJsonSerializer()
                .deserialize(CliUtil.readFileToString(datasetJsonFile));
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
            System.out.println("Write out file: " + params.outputFile);
            FileUtil.writeFile(params.outputFile, content);
        }
    }

}
