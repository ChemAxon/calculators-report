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
import com.chemaxon.calculations.reports.dataset.transform.DatasetConverters;
import com.chemaxon.calculations.reports.report.MultiSetReport.MultiSetReportItem;
import com.chemaxon.calculations.reports.statistics.PearsonStat;
import com.chemaxon.calculations.reports.statistics.RMSEStat;
import com.chemaxon.calculations.reports.statistics.RangedDeltaStat;
import com.chemaxon.calculations.reports.statistics.RangedDeltaStat.RangedDeltaRanges;
import com.chemaxon.calculations.reports.statistics.RangedDeltaStat.RangedDeltaResult;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author Daniel Hari
 */
public class MultisetReporter {

    private List<Dataset> datasets;
    private RangedDeltaRanges rangedDeltaRanges;

    private MultiSetReport report = new MultiSetReport();

    public MultisetReporter(List<Dataset> datasets, String calculatorName, RangedDeltaRanges rangedDeltaRanges) {
        this.datasets = datasets;
        this.rangedDeltaRanges = rangedDeltaRanges;

        report.calculatorName = calculatorName;
    }

    public MultiSetReport report() {
        report.items = datasets.stream().map(this::createItem).collect(toList());
        return report;
    }

    private MultiSetReportItem createItem(Dataset dataset) {
        List<Pair<Double, Double>> valuePairs = DatasetConverters.valuesToPairs(dataset.records);

        MultiSetReportItem item = new MultiSetReportItem();
        item.calculatorVersion = dataset.calcVersion;
        item.pearson = PearsonStat.from(valuePairs);
        item.rmse = RMSEStat.from(valuePairs);
        item.rangedDelta = calcRangedDelta(valuePairs);
        return item;
    }

    private RangedDeltaResult calcRangedDelta(List<Pair<Double, Double>> valuePairs) {
        return new RangedDeltaStat(valuePairs, rangedDeltaRanges).execute();
    }

}
