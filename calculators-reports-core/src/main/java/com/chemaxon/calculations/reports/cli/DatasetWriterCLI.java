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
import com.chemaxon.calculations.reports.reader.ReaderException;
import com.chemaxon.calculations.reports.util.ChemicalTermFunctions;
import com.chemaxon.calculations.reports.util.ChemicalTermFunctions.ChemTermException;
import com.chemaxon.calculations.reports.util.FileUtil;
import com.chemaxon.calculations.reports.util.InvalidMoleculeFormatException;
import java.io.UncheckedIOException;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author Daniel Hari
 */
public class DatasetWriterCLI {

    private CliParams params;
    private String molsetName;
    private String calcType;
    private String calcVersion;
    private ActualValueMode mode;

    private static enum ActualValueMode {
        FROM_CHEMTERM,
        FROM_PROP
    }
    
    private static class CliParams extends AbstractCliParams {

        @Parameter(description = "[Input structure file CSV or SDF]", required = true)
        private String inputFile;

        @Parameter(names = { "-p", "--propRef" }, description = "Property or column name in sdf or csv that holds reference data.", required = true)
        private String propertyNameReference;

        @Parameter(names = { "-pa", "--propAct" }, description = "Property or column name in sdf or csv that holds actual data. (if chemterm not setted)", required = false)
        private String propertyNameActual;

        @Parameter(names = { "-v", "--version" }, description = "Calculator version in output data. (needed in case of -pa)", required = false)
        private String version;
        
        @Parameter(names = { "-c", "--chemTerm" }, description = "Chemical term to run on molecules for actual data. (if -pa not setted)", required = false)
        private String chemTerm;
        
        @Parameter(names = { "-mn", "--molsetName" }, description = "Set molset name. (if not set, default will be generated from input file)", required = false)
        private String molsetName;
        
        @Parameter(names = { "-ct", "--calcType" }, description = "Calculator type to include in output. (if not set, default will be the --propRef parameter)", required = false)
        private String calcType;
        
        @Parameter(names = { "-o", "--output" }, description = "Output json file name. (If not specified, output to console)", required = false)
        private String outputFile;
        
        @Parameter(names = { "-oa", "--outputauto" }, description = "Output to json with automatic filename.", required = false)
        private boolean outputAuto = false;
        
        public void usage() {
            StringBuilder sb = new StringBuilder(
                    "This CLI generates json dataset from a source file that contains mols with exp_value, and actual_value, \n"
                    + "or it can calculate actual_value from a chemterm with current calculator version.\n\n");
            
            new JCommander(this).usage(sb);
            System.out.println(sb.toString());
        }
    }

    public static void main(String[] args) {
        CliUtil.parseParamsAndRunOrHelpOrExit(args, new CliParams(), DatasetWriterCLI::new);
    }

    public DatasetWriterCLI(CliParams params) {
        this.params = params;
        this.molsetName = params.molsetName != null? params.molsetName : FilenameUtils.getBaseName(params.inputFile);
        this.calcType = params.calcType != null? params.calcType : params.propertyNameReference;  
        this.calcVersion = mode == ActualValueMode.FROM_CHEMTERM? 
                CliUtil.getCurrentCalcVersion() : params.version;  
        this.mode = validateParamsAndGetMode();
        run();
    }
    
    private ActualValueMode validateParamsAndGetMode() {
        if(params.chemTerm != null) {
            if(params.propertyNameActual != null) {
                throw CliExitException.withMessage("-pa should not be set if chemterm setted.");
            }
            return ActualValueMode.FROM_CHEMTERM;
            
        } else if(params.propertyNameActual != null) {
            if(params.version == null) {
                throw CliExitException.withMessage("version (-v) should be set if -pa used.");
            }
            return ActualValueMode.FROM_PROP;
        } else {
            throw CliExitException.withMessage("one of -pa or -c should be set.");
        }
    }

    private void run() {
        try {
            
            runOrThrow();

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

    private void runOrThrow() {
        List<DatasetRecord> datasetRecords = readAsRecords();

        if(datasetRecords.isEmpty()) {
            throw CliExitException.withMessage("No records. (May be wrong property name?)");
        }
        
        Dataset dataset = new Dataset(molsetName, calcType, calcVersion, datasetRecords);
        String datasetJson = new DatasetJsonSerializer().serialize(dataset);
        
        writeOutput(datasetJson);
    }

    private List<DatasetRecord> readAsRecords() {
        List<DatasetRecord> datasetRecords;
        
        if(mode == ActualValueMode.FROM_PROP) { 
            //Act value comes from property
            List<Pair<String, List<String>>> raw = CliFileInputReader.readInput(params.inputFile, params.propertyNameReference, params.propertyNameActual);
            List<DatasetRecord> unfiltered = DatasetRecordsBuilder.from(raw)
                .refValueIndex(0)
                .actValueIndex(1)
                .build();
            
            datasetRecords = DatasetFilters.removeInvalidMolecules(unfiltered);
            
        } else {
            //Act value calculated from chemterm
            List<Pair<String, List<String>>> raw = CliFileInputReader.readInput(params.inputFile, params.propertyNameReference);
            List<DatasetRecord> unfiltered = DatasetRecordsBuilder.from(raw)
                    .refValueIndex(0)
                    .build();
            List<DatasetRecord> filtered = DatasetFilters.removeInvalidMolecules(unfiltered);
            datasetRecords = CalculationRunner.on(filtered, ChemicalTermFunctions.fromExpression(params.chemTerm));
            datasetRecords = DatasetConverters.roundActValues(datasetRecords, 2);
        }
        
        datasetRecords = DatasetConverters.replaceWithSmilesOrCxSmiles(datasetRecords);
        datasetRecords = DatasetFilters.removeRecordsWithNullOrNaNValues(datasetRecords);
        return datasetRecords;
    }
    
    private void writeOutput(String content) {
        if(params.outputAuto) {
            FileUtil.writeFile(generateOutputFileName(), content);
        } else if(params.outputFile != null) {
            FileUtil.writeFile(params.outputFile, content);
        } else {
            System.out.println(content);
        }
    }

    private String generateOutputFileName() {
        return FileUtil.sanitizeFilename(String.format(
                "%s-%s-%s.json", calcType, molsetName, calcVersion));
    }

}
