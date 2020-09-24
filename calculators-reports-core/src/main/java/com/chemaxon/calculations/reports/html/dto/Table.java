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

package com.chemaxon.calculations.reports.html.dto;

import com.google.common.collect.Lists;
import java.util.List;

/**
 * @author Daniel Hari
 */
public class Table {

    public String id;
    public List<Table.Column> columns = Lists.newArrayList();
    
    public Table(String id) {
        this.id = id;
    }
    
    public static class Column {
        public Table.DataType type;
        public String id;
        public String displayName; //nullable
        public List<Object> rows = Lists.newArrayList();
        public Column() {}
        public Column(Table.DataType type, String id, String displayName) {
            this.type = type;
            this.id = id;
            this.displayName = displayName;
        }
    }
    
    public enum DataType {
        STRING, DOUBLE
    }

    public Table.Column addColumn(Table.Column column) {
        columns.add(column);
        return column;
    }

    /**
     * @throws IndexOutOfBoundsException if not enough columns added
     */
    public void addDataRow(Object... datas) {
        for (int col = 0; col < datas.length; col++) {
            Object data = datas[col];
            columns.get(col).rows.add(data);
        }
    }
}