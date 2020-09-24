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

import com.chemaxon.calculations.TestUtil;
import java.util.Arrays;
import java.util.SortedMap;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Daniel Hari
 */
public class DistributionCalculatorTest {

    @Test
    public void test() throws Exception {
        SortedMap<Double, Integer> map = new DistributionCalculator(Arrays.asList(1.0, 1.4, 1.5, 1.6, 2.0, 2.5, 4.5, 8.5), 10)
                .calculate();
        assertThat(map.entrySet(), hasSize(10));
    }
    
    @Ignore
    @Test
    public void tryDoubleAliasing() throws Exception {
        SortedMap<Double, Integer> map = new DistributionCalculator(Arrays.asList(1.0, 1.4, 1.5, 1.6, 2.0, 2.5, 4.5, 8.5), 
                34, -5.1, 5.1)
                .calculate();
        TestUtil.printAsJson(map);
    }
    
}
