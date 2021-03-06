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

package com.chemaxon.calculations.reports.dataset.transform;

import com.chemaxon.calculations.reports.dataset.DatasetRecord;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Daniel Hari
 */
public class DatasetConvertersTest {

    
    @Test
    public void testRoundValues() throws Exception {
        DatasetRecord record = new DatasetRecord(null, null, null, 10/3d);
        
        List<DatasetRecord> records = DatasetConverters.roundActValues(Arrays.asList(record), 2);
        
        assertThat(records.get(0).actValue.toString(), is("3.33"));
    }
    
}
