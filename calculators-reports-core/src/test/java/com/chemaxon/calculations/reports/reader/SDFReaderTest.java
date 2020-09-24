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

package com.chemaxon.calculations.reports.reader;

import com.chemaxon.calculations.TestUtil;
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * @author Daniel Hari
 */
public class SDFReaderTest {

    private static final String _3MOLS_SDF = "/3mols.sdf";

    @Test
    public void test() throws Exception {
        List<Pair<String,List<String>>> list = SDFReader.from(TestUtil.getResource(_3MOLS_SDF))
            .valuePropNames("LOGP", "LOGP2")
            .read();
        
        assertThat(list, hasSize(3));
        assertThat(list.get(0).getRight(), is(Lists.newArrayList("0.20", "0.40")));
        assertThat(list.get(1).getRight(), is(Lists.newArrayList("5.22", null)));
        
    }
}
