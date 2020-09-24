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

import chemaxon.jep.ChemJEP;
import chemaxon.jep.Evaluator;
import chemaxon.jep.context.MolContext;
import chemaxon.nfunk.jep.ParseException;
import chemaxon.struc.Molecule;
import java.util.function.Function;

/**
 * static tool
 * 
 * @author Daniel Hari
 */
public class ChemicalTermFunctions {

    /**
     * (returned function may throw {@link ChemTermException} for underlying calculation errors.)
     */
    public static Function<Molecule, Double> fromExpression(String expression) {
        return new ChemicalTermFunction(expression);
    }

    private static class ChemicalTermFunction implements Function<Molecule, Double> {

        private ChemJEP compiled;
        
        public ChemicalTermFunction(String expression) {
            try {
                Evaluator evaluator = new Evaluator();
                compiled = evaluator.compile(expression, MolContext.class);
            } catch (ParseException e) {
                throw new ChemTermException(e);
            }
        }

        @Override
        public Double apply(Molecule input) {
            try {
                Object result = compiled.evaluate(new MolContext(input));
                return Double.valueOf(result.toString());
            } catch (ParseException e) {
                throw new ChemTermException(e);
            }
        }
        
    }
    
    public static class ChemTermException extends IllegalArgumentException {
        public ChemTermException(Throwable cause) {
            super(cause);
        }
    }
}
