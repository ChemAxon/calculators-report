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

package com.chemaxon.calculations.reports.util;

import java.util.Map;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * @author Daniel Hari
 */
public class MolUtilTest {

    private final static String TEST_MRV_RECORD =
            "\n" +
            "Mrv1730 08071813542D\n" +
            "\n" +
            "  1  0  0  0  0  0            999 V2000\n" +
            "    0.0000    0.0000    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n" +
            "M  END\n" + 
            ">  <FORMULA>\n" + 
            "C20H30O\n" + 
            "\n" + 
            ">  <ATOMCOUNT>\n" + 
            "51\n" + 
            "\n" + 
            ">  <MASS>\n" + 
            "286.459\n" + 
            "\n" + 
            ">  <LOGP>\n" + 
            "4.69\n" + 
            "\n" + 
            ">  <logs>\n" + 
            "-6.81   \n" + 
            "";
    
    @Test
    public void testExtractTrimmedProperties() throws Exception {
        Map<String, String> map = MolUtil.extractTrimmedProperties(MoleculeFormats.importMolOrThrow(TEST_MRV_RECORD));
        
        assertThat(map.keySet(), contains("FORMULA", "ATOMCOUNT", "MASS", "LOGP", "logs")); //ordering preserved
        assertThat(map.get("FORMULA"), is("C20H30O"));
        assertThat(map.get("logs"), is("-6.81"));       //trimming
    }
}
