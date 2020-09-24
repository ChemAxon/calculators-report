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

package com.chemaxon.calculations.reports.dataset.builder;

import com.chemaxon.calculations.reports.dataset.DatasetRecord;
import com.chemaxon.calculations.reports.util.MolUtil;
import java.util.List;
import java.util.function.Function;
import static java.util.stream.Collectors.toList;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daniel Hari
 */
public class DatasetRecordsBuilder {
    private static final Logger logger = LoggerFactory.getLogger(DatasetRecordsBuilder.class);
    
    private int currentId = 0;
    private List<Pair<String, List<String>>> raw;
    private Function<List<String>, String> refValueGetter = (p) -> null;
    private Function<List<String>, String> actValueGetter = (p) -> null;
    private Function<List<String>, String> idGetter = (p) -> String.valueOf(currentId++);

    public DatasetRecordsBuilder(List<Pair<String, List<String>>> raw) {
        this.raw = raw;
    }

    public static DatasetRecordsBuilder from(List<Pair<String, List<String>>> raw) {
        return new DatasetRecordsBuilder(raw);
    }

    public DatasetRecordsBuilder refValueIndex(int index) {
        this.refValueGetter = props -> props.get(index);
        return this;
    }

    public DatasetRecordsBuilder actValueIndex(int index) {
        this.actValueGetter = props -> props.get(index);
        return this;
    }

    /**
     * If not set then id is generated;
     */
    public DatasetRecordsBuilder idIndex(int index) {
        this.idGetter = props -> props.get(index);
        return this;
    }

    /**
     * @throws IndexOutOfBoundsException if given indexes out of bounds
     */
    public List<DatasetRecord> build() {
        return raw.stream().map(pair -> {
            List<String> attributes = pair.getRight();
            
            String id = idGetter.apply(attributes);
            
            return new DatasetRecord(
                    id, 
                    pair.getLeft(),
                    parseOrNullAndWarn(refValueGetter.apply(attributes), id),
                    parseOrNullAndWarn(actValueGetter.apply(attributes), id)
                    );
        }).collect(toList());
    }
    
    private Double parseOrNullAndWarn(String valueOrNull, String id) {
        if(valueOrNull == null) {
            return null;
        } else {
            Double parsed = MolUtil.parseDoubleOrNull(valueOrNull);
            if(parsed == null) {
                logger.warn("Value can't be parsed at id/index {} as Double: \"{}\".", id, valueOrNull);
            }
            return parsed;
        }
    }

}
