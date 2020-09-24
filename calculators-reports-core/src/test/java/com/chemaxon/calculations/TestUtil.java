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

package com.chemaxon.calculations;

import com.chemaxon.calculations.reports.util.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.InputStream;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import static org.junit.Assert.fail;

/**
 * Static utils for testing.
 * 
 * @author Daniel Hari
 */
public class TestUtil {

    /**
     * Passes if given exception class thrown. (same as JUnit5 method)
     */
    public static void assertThrows(Class<? extends Throwable> throwableClass, Runnable method) {
        try {
            method.run();
            fail("Expected exception: " + throwableClass.getName());
        } catch (Throwable e) {
            if (throwableClass.isAssignableFrom(e.getClass())) {
                return; //pass
            } else {
                throw e; //other exception
            }
        }
    }

    /**
     * prints as json to sysout
     */
    public static <T> T printAsJson(T object) {
        return GsonUtil.printAsJson(object);
    }

    public static InputStream getResource(String resource) {
        return TestUtil.class.getResourceAsStream(resource);
    }

    /**
     * Return matcher that compares objects with their json representation.<p/>
     * Helpful if you want deep equals through the stucture without overridden equals/hashcode methods.
     */
    public static <T> org.hamcrest.Matcher<T> jsonEquals(T target) {
        return new JsonEqualsMatcher<T>(target);
    }

}

class JsonEqualsMatcher<T> extends BaseMatcher<T> {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final T target;

    public JsonEqualsMatcher(T target) {
        this.target = target;
    }

    @Override
    public boolean matches(Object item) {
        return GSON.toJson(item).equals(GSON.toJson(target));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("json equals ").appendText(GSON.toJson(target));
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        description.appendText("was ").appendText(GSON.toJson(item));
    }
}
