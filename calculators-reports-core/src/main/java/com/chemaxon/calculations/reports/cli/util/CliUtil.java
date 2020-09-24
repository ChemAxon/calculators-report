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

package com.chemaxon.calculations.reports.cli.util;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.chemaxon.calculations.reports.util.FileUtil;
import com.chemaxon.version.VersionInfo;
import com.google.common.base.Splitter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;
import static java.util.stream.Collectors.toList;

/**
 * Boilerplates for CLI development.
 * 
 * @author Daniel Hari
 */
public class CliUtil {

    /**
     * @param <P> param type
     * @param args main args
     * @param emptyParams a new empty parameter object
     * @param runner the method to run on successful parameter parsing. 
     * (Note: method can throw {@link CliExitException} that is handled properly)
     * 
     */
    public static <P extends AbstractCliParams> void parseParamsAndRunOrHelpOrExit(String[] args, P emptyParams, Consumer<P> runner) {
        try {
            P params = parseParamsOrThrow(args, emptyParams, p -> p.help);
            
            runner.accept(params);
            
        } catch (CliExitException e) {
            if(e.getMessage() != null) {
                System.err.println("Error: " + e.getMessage() + "\n");
            }
            if(e.printHelp) {
                emptyParams.usage();
            }
        }
    }

    /**
     * @throws CliExitException
     */
    private static <P> P parseParamsOrThrow(String[] args, P cliParams, Function<P, Boolean> helpParam) throws CliExitException {
        JCommander jCommander = new JCommander(cliParams);
    
        try {
            jCommander.parse(args);
        } catch (ParameterException e) {
            throw CliExitException.withMessageAndHelp("Parameter error: " + e.getMessage() + "\n");
        }
        
        if (helpParam.apply(cliParams)) {
            throw CliExitException.withHelp();
        }
        return cliParams;
    }

    /**
     * @throws CliExitException
     */
    public static InputStream getStreamOrThrowFromFile(String file) {
        try {
            return Files.newInputStream(Paths.get(file));
        } catch (IOException e) {//no file
            throw CliExitException.withMessage("File read error: " + e.toString());
        }
    }

    /**
     * @throws CliExitException
     */
    public static String readFileToString(String file) {
        try {
            return FileUtil.readToString(file); 
        } catch (UncheckedIOException e) {
            throw CliExitException.withMessage("File read error: " + e.toString());
        }
    }

    public static List<String> paramSplitByCommaOrSpace(String distRanges) {
        return Splitter.on(Pattern.compile("(,\\s*)|(\\s+)")).splitToList(distRanges);
    }

    public static List<Double> toDoublesOrThrow(List<String> strings) {
        try {
            return strings.stream().map(Double::valueOf).collect(toList());
        } catch (NumberFormatException e) {
            throw CliExitException.withMessage(String.format("Can't parse parameter as double: %s", e.getMessage()));
        }
    }

    public static String getCurrentCalcVersion() {
        return VersionInfo.getVersion();
    }
    
}
