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

import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Calculate 3 delta ranges like this: 
 * Δ < 0.5     0.5 < Δ < 1.0   Δ > 1.0          
 * 
 * @author Daniel Hari
 */
public class RangedDeltaStat {
    private List<Pair<Double, Double>> pairs;
    private RangedDeltaRanges params;

    private int countUnder = 0;
    private int countIn = 0;
    private int countOver = 0;

    public RangedDeltaStat(List<Pair<Double, Double>> pairs, RangedDeltaRanges params) {
        this.pairs = pairs;
        this.params = params;
    }

    public RangedDeltaResult execute() {

        for (Pair<Double, Double> pair : pairs) {
            addCount(getDelta(pair));
        }

        return toResult();
    }

    private static double getDelta(Pair<Double, Double> pair) {
        return Math.abs(pair.getRight() - pair.getLeft());
    }

    private void addCount(double delta) {
        if (delta < params.thresholdLow) {
            countUnder++;
        } else if (delta > params.thresholdHigh) {
            countOver++;
        } else {
            countIn++;
        }
    }

    private RangedDeltaResult toResult() {
        int totalCount = pairs.size();
    
        return new RangedDeltaResult(
                params,
                (double) countUnder / totalCount,
                (double) countIn / totalCount,
                (double) countOver / totalCount);
    }

    public static class RangedDeltaResult {
        public RangedDeltaRanges params;
        public double underRate;
        public double inRate;
        public double overRate;

        public RangedDeltaResult(RangedDeltaRanges params, double underRate, double inRate, double overRate) {
            this.params = params;
            this.underRate = underRate;
            this.inRate = inRate;
            this.overRate = overRate;
        }
    }

    public static class RangedDeltaRanges {
        public double thresholdLow;
        public double thresholdHigh;
        public RangedDeltaRanges(double thresholdLow, double thresholdHigh) {
            this.thresholdLow = thresholdLow;
            this.thresholdHigh = thresholdHigh;
        }
        @Override
        public String toString() {
            return "\"" + thresholdLow + ", " + thresholdHigh + "\"";
        }
    }

}
