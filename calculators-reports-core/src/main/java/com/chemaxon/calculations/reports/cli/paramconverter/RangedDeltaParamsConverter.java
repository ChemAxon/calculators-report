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

package com.chemaxon.calculations.reports.cli.paramconverter;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;
import com.chemaxon.calculations.reports.cli.util.CliUtil;
import com.chemaxon.calculations.reports.statistics.RangedDeltaStat.RangedDeltaRanges;
import java.util.List;

/**
 * @author Daniel Hari
 */
public class RangedDeltaParamsConverter implements IStringConverter<RangedDeltaRanges> {

    @Override
    public RangedDeltaRanges convert(String value) {
        return getDistRanges(value);
    }

    private RangedDeltaRanges getDistRanges(String string) {
        List<Double> distRanges = CliUtil.toDoublesOrThrow(CliUtil.paramSplitByCommaOrSpace(string));
        if(distRanges.size() != 2) {
            throw new ParameterException("distRanges parameter should be 2 double value.");
        }
        return new RangedDeltaRanges(distRanges.get(0), distRanges.get(1));
    }

}
