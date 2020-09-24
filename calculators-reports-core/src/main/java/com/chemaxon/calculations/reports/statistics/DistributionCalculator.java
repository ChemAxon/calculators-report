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

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import java.math.BigDecimal;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author Daniel Hari
 */
public class DistributionCalculator {

    private Iterable<Double> input;
    private BigDecimal binCount;
    private BigDecimal min;
    private BigDecimal max;

    public DistributionCalculator(Iterable<Double> input, int binCount, Double min, Double max) {
        this.input = input;
        this.binCount = new BigDecimal(binCount);
        this.min = toBigDecimal(min);
        this.max = toBigDecimal(max);
        Preconditions.checkArgument(!Iterables.isEmpty(input));
        autoMinMaxIfNeeded();
    }

    private BigDecimal toBigDecimal(Double min) {
        return min == null? null : new BigDecimal(String.valueOf(min));
    }

    public DistributionCalculator(List<Double> input, int binCount) {
        this(input, binCount, null, null);
    }

    public SortedMap<Double, Integer> calculate() {
        
        BigDecimal binWidth = (max.subtract(min)).divide(binCount);

        SortedMap<Double, Integer> freqs = new TreeMap<>();
        for (int i = 0; i < binCount.intValue(); i++) {
            BigDecimal binValue = min.add(binWidth.multiply(new BigDecimal(i + 1)));
            freqs.put(binValue.doubleValue(), 0);
        }

        for (Double d : input) {
            for (Map.Entry<Double, Integer> entry : freqs.entrySet()) {
                if (d <= entry.getKey()) {
                    entry.setValue(entry.getValue() + 1);
                    break;
                }
            }
        }

        return freqs;
    }

    private void autoMinMaxIfNeeded() {
        DoubleSummaryStatistics stats = Streams.stream(input)
                .collect(Collectors.summarizingDouble(Double::doubleValue));

        if(min == null) {
            min = toBigDecimal(stats.getMin());
        }
        if(max == null) {
            max = toBigDecimal(stats.getMax());
        }
    }

}
