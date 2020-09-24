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
import com.beust.jcommander.Parameter;

/**
 * @author Daniel Hari
 */
public abstract class AbstractCliParams {

    @Parameter(names = { "-h", "--help" }, help = true)
    public boolean help;

    public void usage() {
        new JCommander(this).usage();
    }
}
