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
import com.chemaxon.calculations.reports.report.OneSetReport.DrawBounds;
import com.chemaxon.calculations.reports.statistics.DistributionCalculator;
import com.chemaxon.calculations.reports.statistics.PearsonStat;
import com.chemaxon.calculations.reports.statistics.RMSEStat;
import com.chemaxon.calculations.reports.statistics.RangedDeltaStat;
import com.chemaxon.calculations.reports.statistics.RangedDeltaStat.RangedDeltaRanges;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author Daniel Hari
 */
public class OneSetReporter {
    private static final int DIST_BIN_MULTIPLIER = 2; //how much bins for each label of the x axis on the chart
    
    /** <exp, act> */
    private List<Pair<Double, Double>> valuePairs; 
    private RangedDeltaRanges rangedDeltaRanges;
    private Double deltaBinWidth;

    private OneSetReport report = new OneSetReport();

    public OneSetReporter(Dataset dataset, RangedDeltaRanges rangedDeltaRanges, Double deltaBinWidth) {
        this.valuePairs = DatasetConverters.valuesToPairs(dataset.records);
        this.rangedDeltaRanges = rangedDeltaRanges;
        this.deltaBinWidth = deltaBinWidth;

        report.records = dataset.records;
        report.calculatorName = dataset.calcType;
        report.calculatorVersion = dataset.calcVersion;
    }

    public OneSetReport report() {
        report.rmse = RMSEStat.from(valuePairs);
        report.pearson = PearsonStat.from(valuePairs);
        report.rangedDelta = new RangedDeltaStat(valuePairs, rangedDeltaRanges).execute();
        report.drawBounds = squarifyByMinMax(drawBounds());  
        report.distributionMap = distributionMap();
        report.deltaDistributionMap = deltaDistributionMap(deltaBinWidth);
        
        return report;
    }

    private DrawBounds drawBounds() {
        DoubleSummaryStatistics expStats = report.records.stream()
                .map(r -> r.expValue)
                .collect(Collectors.summarizingDouble(Double::doubleValue));
        
        DoubleSummaryStatistics actStats = report.records.stream()
                .map(r -> r.actValue)
                .collect(Collectors.summarizingDouble(Double::doubleValue));
        
        DrawBounds drawBounds = new DrawBounds();
        
        drawBounds.expMin = Math.floor(expStats.getMin());  
        drawBounds.expMax = Math.ceil(expStats.getMax());
        drawBounds.actMin = Math.floor(actStats.getMin());  
        drawBounds.actMax = Math.ceil(actStats.getMax());
        
        return drawBounds;
    }

    private DrawBounds squarifyByMinMax(DrawBounds drawBounds) {
        double min = Math.min(drawBounds.actMin, drawBounds.expMin);
        double max = Math.max(drawBounds.actMax, drawBounds.expMax);
        
        drawBounds.expMin = min;  
        drawBounds.expMax = max;
        drawBounds.actMin = min;  
        drawBounds.actMax = max;
        
        return drawBounds;
    }

    private SortedMap<Double, Integer> distributionMap() {
        List<Double> expValues = valuePairs.stream().map(p -> p.getLeft()).collect(toList());
        double min = report.drawBounds.expMin;
        double max = report.drawBounds.expMax;
        int binCount = (int) (max - min) * DIST_BIN_MULTIPLIER;
        return new DistributionCalculator(expValues, binCount, min, max).calculate();
    }

    private Map<Double, Integer> deltaDistributionMap(double binWidth) {
        List<Double> deltas = valuePairs.stream().map(p -> p.getLeft() - p.getRight()).collect(toList());

        DoubleSummaryStatistics stats = deltas.stream().collect(Collectors.summarizingDouble(d -> d));
        double RawMin = stats.getMin();
        double RawMax = stats.getMax();
        
        //make the center to ZERO!
        double absMax = Math.max(Math.abs(RawMin), Math.abs(RawMax));
        
        //adjust min max to bin width
        double min = Math.floor((-absMax / binWidth)) * binWidth;
        double max = Math.ceil((absMax / binWidth)) * binWidth;
        int binCount = Math.round((int) ((max - min) / binWidth));
        
        return new DistributionCalculator(deltas, binCount, min, max).calculate();
    }
    
}
