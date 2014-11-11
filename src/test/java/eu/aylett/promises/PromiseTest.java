/*
 * Copyright 2014 Andrew Aylett
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
 */

package eu.aylett.promises;

import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertNotNull;

public class PromiseTest {

    @Test
    public void testThen() {
        Promise<List<String>> result = new Promise<String>().then((String s) -> newArrayList(s));
        assertNotNull("Object should exist", result);
    }

    @Test
    public void testThenPromise() {
        Promise<List<String>> result = new Promise<String>().then((String s) -> new Promise<>());
        assertNotNull("Object should exist", result);
    }
}
