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

import net.openhft.koloboke.function.ObjDoublePredicate;
import java.util.function.ObjDoubleConsumer;
import net.openhft.koloboke.collect.map.ObjDoubleMap;

import java.util.Map;


public final class CommonObjDoubleMapOps {

    public static boolean containsAllEntries(final InternalObjDoubleMapOps<?> map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof ObjDoubleMap) {
            ObjDoubleMap m2 = (ObjDoubleMap) another;
            if (
                    m2.keyEquivalence().equals(map.keyEquivalence())
                
            ) {
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalObjDoubleMapOps) {
                    //noinspection unchecked
                    return ((InternalObjDoubleMapOps) m2).allEntriesContainingIn(map);
                }
            }
            // noinspection unchecked
            return m2.forEachWhile(new
                   ObjDoublePredicate() {
                @Override
                public boolean test(Object a, double b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry(e.getKey(),
                    (Double) e.getValue()))
                return false;
        }
        return true;
    }

    public static <K> void putAll(final InternalObjDoubleMapOps<K> map,
            Map<? extends K, ? extends Double> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof ObjDoubleMap) {
            if (another instanceof InternalObjDoubleMapOps) {
                ((InternalObjDoubleMapOps) another).reversePutAllTo(map);
            } else {
                ((ObjDoubleMap) another).forEach(new ObjDoubleConsumer<K>() {
                    @Override
                    public void accept(K key, double value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends K, ? extends Double> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonObjDoubleMapOps() {}
}

