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
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Daniel Hari
 */
public class CSVReaderTest {

    @Test
    public void testRead() throws Exception {
        List<Pair<String, List<String>>> pairs = CSVReader.from(TestUtil.getResource("/TEST1.csv"))
                .molColumn("SMILES")
                .valueColumns(1, 2)
                .read();

        assertThat(pairs, hasSize(3));
        assertThat(pairs, hasItems(
                Pair.of("CC", Lists.newArrayList("4.17", "4.18")),
                Pair.of("COCC", Lists.newArrayList("", "1.84"))));
    }

    @Test(expected = ReaderException.class)
    public void testWrongColumn() throws Exception {
        CSVReader.from(TestUtil.getResource("/TEST1.csv"))
                .molColumn("XX")
                .valueColumns(1)
                .read();
    }

}
