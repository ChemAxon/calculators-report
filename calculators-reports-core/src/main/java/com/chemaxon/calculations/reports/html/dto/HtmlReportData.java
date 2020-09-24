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
public class HtmlReportData {

    public List<Table> tables = Lists.newArrayList();
    
    public List<Content> contents = Lists.newArrayList();
    
    public Table addTable(Table table) {
        tables.add(table);
        return table;
    }

    public <T extends Content> T addContent(T content) {
        contents.add(content);
        return content;
    }
    
}
