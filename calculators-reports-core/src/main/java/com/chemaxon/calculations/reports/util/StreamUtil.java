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

package com.chemaxon.calculations.reports.util;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import static java.util.stream.Collectors.toMap;

/**
 * static util
 * 
 * @author Daniel Hari
 */
public class StreamUtil {

    /** Stream collector **/
    public static <T, K, U> Collector<T, ?, Map<K, U>> 
        toLinkedHashMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper) {
        return toMap(keyMapper, valueMapper, throwingMerger(), LinkedHashMap::new);
    }

    private static <T> BinaryOperator<T> throwingMerger() {
        return (u, v) -> {
            throw new IllegalStateException(String.format("Duplicate key %s", u));
        };
    }
    
    /**
     * Return min of a doubles of a collection that has a {@link Double} property or {@link Double#NaN} if no elements.
     */
    public static <T> Double min(Collection<T> collection, Function<? super T, Double> doubleMapper) {
        return collection.stream().map(doubleMapper).min(Double::compare)
                .orElse(Double.NaN);
    }

    /**
     * Return max of a doubles of a collection that has a {@link Double} property or {@link Double#NaN} if no elements.
     */
    public static <T> Double max(Collection<T> collection, Function<? super T, Double> doubleMapper) {
        return collection.stream().map(doubleMapper).max(Double::compare)
                .orElse(Double.NaN);
    }


}
