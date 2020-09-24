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

import com.chemaxon.calculations.TestUtil;
import com.chemaxon.calculations.reports.dataset.Dataset;
import com.chemaxon.calculations.reports.dataset.DatasetRecord;
import java.util.Arrays;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * @author Daniel Hari
 */
public class DatasetJsonSerializerTest {

    DatasetJsonSerializer serializer = new DatasetJsonSerializer();
    
    private Dataset dataset() {
        return new Dataset("TEST1", "LogP", "10.0", Arrays.asList(
                new DatasetRecord("0", "C", 1.0, 1.1),
                new DatasetRecord("1", "CC", 2.0, 2.1)
                ));
    }

    @Test
    public void testFull() throws Exception {
        Dataset dataset = dataset();
        
        String json = serializer.serialize(dataset);
        Dataset parsed = serializer.deserialize(json);
        
        assertThat(parsed, TestUtil.jsonEquals(dataset));
    }
    
}
