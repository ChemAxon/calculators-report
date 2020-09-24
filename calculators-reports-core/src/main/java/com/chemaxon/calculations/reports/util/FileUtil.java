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

import chemaxon.util.Errors;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Daniel Hari
 */
public class FileUtil {

    /**
     * @throws UncheckedIOException
     */
    public static void writeFile(String file, String contentString) {
        Errors.unCheckIO(() -> 
            Files.write(Paths.get(file), contentString.getBytes(StandardCharsets.UTF_8))
        );
    }

    /**
     * @throws UncheckedIOException
     */
    public static String readToString(String file) {
        return Errors.unCheckIO(() -> 
            new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8)
        );
    }

    /**
     * Replaces invalid characters as a filename;
     */
    public static String sanitizeFilename(String fileName) {
        return fileName.replaceAll("[^0-9a-zA-Z-._]", "_");
    }
    
}
