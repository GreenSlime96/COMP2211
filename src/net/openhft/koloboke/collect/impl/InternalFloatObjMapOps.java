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

import net.openhft.koloboke.collect.map.FloatObjMap;


public interface InternalFloatObjMapOps<V>
        extends FloatObjMap<V>, InternalMapOps<Float, V> {

    boolean containsEntry(float key, Object value);

    void justPut(float key, V value);

    boolean containsEntry(int key, Object value);

    void justPut(int key, V value);

    boolean allEntriesContainingIn(InternalFloatObjMapOps<?> map);

    void reversePutAllTo(InternalFloatObjMapOps<? super V> map);
}

