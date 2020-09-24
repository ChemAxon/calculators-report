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
 * @author Daniel Hari
 */
public class RMSEStat {
    private List<Pair<Double, Double>> pairs;

    public RMSEStat(List<Pair<Double, Double>> pairs) {
        this.pairs = pairs;
    }

    public static double from(List<Pair<Double, Double>> pairs) {
        return new RMSEStat(pairs).execute();
    }

    private double execute() {
        double a = pairs.stream()
            .mapToDouble(p -> p.getRight() - p.getLeft())
            .map(d -> d * d)
            .sum();
        
        return Math.sqrt(a / pairs.size());
    }

}
