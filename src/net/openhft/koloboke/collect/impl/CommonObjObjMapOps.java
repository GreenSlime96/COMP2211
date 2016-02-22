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

import java.util.function.BiPredicate;
import java.util.function.BiConsumer;
import net.openhft.koloboke.collect.map.ObjObjMap;

import java.util.Map;


public final class CommonObjObjMapOps {

    public static boolean containsAllEntries(final InternalObjObjMapOps<?, ?> map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof ObjObjMap) {
            ObjObjMap m2 = (ObjObjMap) another;
            if (
                    m2.keyEquivalence().equals(map.keyEquivalence())
                 && 
                    m2.valueEquivalence().equals(map.valueEquivalence())
            ) {
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalObjObjMapOps) {
                    //noinspection unchecked
                    return ((InternalObjObjMapOps) m2).allEntriesContainingIn(map);
                }
            }
            // noinspection unchecked
            return m2.forEachWhile(new
                   BiPredicate() {
                @Override
                public boolean test(Object a, Object b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry(e.getKey(),
                    e.getValue()))
                return false;
        }
        return true;
    }

    public static <K, V> void putAll(final InternalObjObjMapOps<K, V> map,
            Map<? extends K, ? extends V> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof ObjObjMap) {
            if (another instanceof InternalObjObjMapOps) {
                ((InternalObjObjMapOps) another).reversePutAllTo(map);
            } else {
                ((ObjObjMap) another).forEach(new BiConsumer<K, V>() {
                    @Override
                    public void accept(K key, V value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends K, ? extends V> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonObjObjMapOps() {}
}

