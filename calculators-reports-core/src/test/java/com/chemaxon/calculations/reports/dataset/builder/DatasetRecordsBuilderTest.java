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

package com.chemaxon.calculations.reports.dataset.builder;

import com.chemaxon.calculations.TestUtil;
import com.chemaxon.calculations.reports.dataset.DatasetRecord;
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * 
 * @author Daniel Hari
 */
public class DatasetRecordsBuilderTest {

    private static final List<Pair<String, List<String>>> PAIRS =
            Lists.newArrayList(
                    Pair.of("C", Lists.newArrayList("5.1", "3.1")),
                    Pair.of("CC", Lists.newArrayList("5.5", "3.4")));

    @Test
    public void testName() throws Exception {

        List<DatasetRecord> records = DatasetRecordsBuilder.from(PAIRS)
                .refValueIndex(0)
                .actValueIndex(1)
                .build();

        assertThat(records, hasSize(2));

        DatasetRecord record = records.get(0);
        assertThat(record.id, is("0"));
        assertThat(record.expValue, is(5.1));
        assertThat(record.actValue, is(3.1));
        assertThat(record.molecule, is("C"));

        TestUtil.printAsJson(records);
    }

}
