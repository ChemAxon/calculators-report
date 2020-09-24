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

import com.google.gson.GsonBuilder;

/**
 * @author Daniel Hari
 */
public class GsonUtil {

    /**
     * prints as json to sysout
     */
    public static <T> T printAsJson(T object) {
        String json = toJson(object);
        System.out.println(json);
        return object;
    }

    public static <T> String toJson(T object) {
        return new GsonBuilder()
                .setPrettyPrinting().create()
                .toJson(object);
    }

}
