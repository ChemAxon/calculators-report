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

package com.chemaxon.calculations.reports.report;

import com.chemaxon.calculations.reports.dataset.Dataset;
import com.chemaxon.calculations.reports.dataset.DatasetRecord;
import com.chemaxon.calculations.reports.statistics.RangedDeltaStat.RangedDeltaRanges;
import com.chemaxon.calculations.reports.util.GsonUtil;
import com.google.common.collect.Lists;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * @author Daniel Hari
 */
public class OneSetReporterTest {

    
    @Test
    public void testName() throws Exception {

        Dataset dataset = new Dataset("molset1", "LOGP", "15.1", Lists.newArrayList(
                new DatasetRecord("0", "C", 5.16, 4.0),
                new DatasetRecord("1", "CC", 3.16, 3.90),
                new DatasetRecord("2", "CC", 1.00, 0.5)));
        

        OneSetReport report = new OneSetReporter(dataset, new RangedDeltaRanges(0.5, 1.0), 0.5)
                .report();

        assertThat(report.calculatorName, not(nullValue()));
        assertThat(report.calculatorVersion, not(nullValue()));
        assertThat(report.records, hasSize(3));
        assertThat(report.rmse, not(0.));
        assertThat(report.pearson, not(0.));
        assertThat(report.rangedDelta, not(nullValue()));
        assertThat(report.distributionMap, not(nullValue()));
        assertThat(report.deltaDistributionMap, not(nullValue()));
        
        assertThat(report.drawBounds.expMin, is(0.0));
        assertThat(report.drawBounds.expMax, is(6.0));
        assertThat(report.drawBounds.actMin, is(0.0));
        assertThat(report.drawBounds.actMax, is(6.0));
        
        System.out.println(GsonUtil.toJson(report));
    }
}
