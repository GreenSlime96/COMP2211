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

import net.openhft.koloboke.function.FloatObjPredicate;
import net.openhft.koloboke.function.FloatObjConsumer;
import net.openhft.koloboke.collect.map.FloatObjMap;

import java.util.Map;


public final class CommonFloatObjMapOps {

    public static boolean containsAllEntries(final InternalFloatObjMapOps<?> map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof FloatObjMap) {
            FloatObjMap m2 = (FloatObjMap) another;
            if (
                
                    m2.valueEquivalence().equals(map.valueEquivalence())
            ) {
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalFloatObjMapOps) {
                    //noinspection unchecked
                    return ((InternalFloatObjMapOps) m2).allEntriesContainingIn(map);
                }
            }
            // noinspection unchecked
            return m2.forEachWhile(new
                   FloatObjPredicate() {
                @Override
                public boolean test(float a, Object b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Float) e.getKey(),
                    e.getValue()))
                return false;
        }
        return true;
    }

    public static <V> void putAll(final InternalFloatObjMapOps<V> map,
            Map<? extends Float, ? extends V> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof FloatObjMap) {
            if (another instanceof InternalFloatObjMapOps) {
                ((InternalFloatObjMapOps) another).reversePutAllTo(map);
            } else {
                ((FloatObjMap) another).forEach(new FloatObjConsumer<V>() {
                    @Override
                    public void accept(float key, V value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Float, ? extends V> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonFloatObjMapOps() {}
}

