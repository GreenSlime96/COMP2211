/*
 * Copyright 2014 the original author or authors.
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

package net.openhft.koloboke.collect.impl;

import net.openhft.koloboke.collect.map.FloatCharMap;


public interface InternalFloatCharMapOps
        extends FloatCharMap, InternalMapOps<Float, Character> {

    boolean containsEntry(float key, char value);

    void justPut(float key, char value);

    boolean containsEntry(int key, char value);

    void justPut(int key, char value);

    boolean allEntriesContainingIn(InternalFloatCharMapOps map);

    void reversePutAllTo(InternalFloatCharMapOps map);
}

