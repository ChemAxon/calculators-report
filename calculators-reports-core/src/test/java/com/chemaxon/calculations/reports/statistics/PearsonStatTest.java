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

package com.chemaxon.calculations.reports.statistics;

import com.google.common.collect.Lists;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * @author Daniel Hari
 */
public class PearsonStatTest {

    @Test
    public void test() throws Exception {
        List<Pair<Double, Double>> pairs = Lists.newArrayList(
                Pair.of(1.0, 1.5),
                Pair.of(1.0, 0.5),
                Pair.of(2.0, 0.0));

        assertThat(PearsonStat.from(pairs), closeTo(-0.75, 0.01));
    }
}
