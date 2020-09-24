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

package com.chemaxon.calculations.reports.dataset.serialization.json;

import com.chemaxon.calculations.reports.dataset.Dataset;
import com.chemaxon.calculations.reports.dataset.DatasetRecord;
import com.chemaxon.calculations.reports.dataset.serialization.SerializationException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

/**
 * Json version of persisting {@link Dataset}.
 * 
 * @author Daniel Hari
 */
public class DatasetJsonSerializer {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public String serialize(Dataset dataset) {
        JsonDataset jsonDataset = toDTO(dataset);
        return GSON.toJson(jsonDataset);
    }

    /**
     * @throws SerializationException
     */
    public Dataset deserialize(String json) {
        try {
            JsonDataset jsonDataset = GSON.fromJson(json, JsonDataset.class);
            return fromDTO(jsonDataset);
        } catch (RuntimeException e) {
            throw new SerializationException("Can't deserialize dataset: " + e.toString(), e);
        }
    }

    private static JsonDataset toDTO(Dataset dataset) {
        JsonDataset jsonDataset = new JsonDataset(dataset.molsetName, dataset.calcType, dataset.calcVersion);
    
        dataset.records.forEach(record -> {
            jsonDataset.ids.add(record.id);
            jsonDataset.molecules.add(record.molecule);
            jsonDataset.expValues.add(record.expValue);
            jsonDataset.actValues.add(record.actValue);
        });
        return jsonDataset;
    }

    private static Dataset fromDTO(JsonDataset jsonDataset) {
        Dataset dataset = new Dataset();
        dataset.calcType = jsonDataset.calcType;
        dataset.calcVersion = jsonDataset.calcVersion;
        dataset.molsetName = jsonDataset.molsetName;
        dataset.records =
                IntStream.range(0, jsonDataset.molecules.size())
                        .mapToObj(i -> {
                            String id = jsonDataset.ids.get(i);
                            String mol = jsonDataset.molecules.get(i);
                            Double expValue = jsonDataset.expValues.get(i);
                            Double actValue = jsonDataset.actValues.get(i);
                            return new DatasetRecord(id, mol, expValue, actValue);
                        }).collect(toList());
        return dataset;
    }

}
