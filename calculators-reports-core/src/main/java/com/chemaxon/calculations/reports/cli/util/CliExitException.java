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

/**
 * Exit from Cli, by printing message and/or help.
 * 
 * @author Daniel Hari
 */
public class CliExitException extends RuntimeException {

    public final boolean printHelp;

    private CliExitException(boolean printHelp) {
        super();
        this.printHelp = printHelp;
    }
    
    private CliExitException(String message, boolean printHelp) {
        super(message);
        this.printHelp = printHelp;
    }

    public static CliExitException withHelp() {
        return new CliExitException(true);
    }
    
    public static CliExitException withMessageAndHelp(String message) {
        return new CliExitException(message, true);
    }
    
    public static CliExitException withMessage(String message) {
        return new CliExitException(message, false);
    }
    
    public static CliExitException withMessage(Throwable e) {
        return new CliExitException(e.toString(), false);
    }
    
}
