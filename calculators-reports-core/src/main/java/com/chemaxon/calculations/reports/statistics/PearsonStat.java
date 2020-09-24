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
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

/**
 * @author Daniel Hari
 */
public class PearsonStat {
    private List<Pair<Double, Double>> pairs;

    public PearsonStat(List<Pair<Double, Double>> pairs) {
        this.pairs = pairs;
    }

    /**
     * @return Pearson correlation or NaN if can't be calculated.
     */
    public static double from(List<Pair<Double, Double>> pairs) {
        return new PearsonStat(pairs).execute();
    }

    private double execute() {
        try {
            return executeOrThrow();
        } catch (MathIllegalArgumentException e) {
            return Double.NaN;  //insuficcient dimension, etc
        }
    }

    private double executeOrThrow() {
        double[] xArray = pairs.stream().mapToDouble(input -> input.getLeft()).toArray();
        double[] yArray = pairs.stream().mapToDouble(input -> input.getRight()).toArray();
        return new PearsonsCorrelation().correlation(xArray, yArray);
    }

}
