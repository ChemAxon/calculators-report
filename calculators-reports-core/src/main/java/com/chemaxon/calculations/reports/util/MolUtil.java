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

import chemaxon.marvin.io.MPropHandler;
import chemaxon.struc.Molecule;
import static com.chemaxon.calculations.reports.util.StreamUtil.toLinkedHashMap;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * @author Daniel Hari
 */
public class MolUtil {

    /**
     * @return property or null if not exists
     * 
     * @throws InvalidMoleculeFormatException
     */
    public static String extractTrimmedPropertyOrNull(String mol, String propertyName) {
        return extractPropertyOrNull(MoleculeFormats.importMolOrThrow(mol), propertyName);
    }
    
    public static String extractTrimmedPropertyOrNull(Molecule mol, String propertyName) {
        return Optional.ofNullable(extractPropertyOrNull(mol, propertyName)).map(String::trim).orElse(null);
    }
    
    /**
     * ordering keeps source order
     * 
     * @return map of [propName, propValue] >
     */
    public static Map<String, String> extractTrimmedProperties(Molecule mol) {
        return Arrays.stream(mol.properties().getKeys())
            .collect(toLinkedHashMap(k -> k, k -> extractTrimmedPropertyOrNull(mol, k)));
    }
    
    private static String extractPropertyOrNull(Molecule mol, String propertyName) {
        return MPropHandler.convertToString(mol.properties(), propertyName);
    }
    
    //This is wrong here
    public static Double parseDoubleOrNull(String valueOrNull) {
        try {
            return Double.parseDouble(valueOrNull);
        } catch (NullPointerException e) {
            return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
