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

import net.openhft.koloboke.function.DoubleObjPredicate;
import net.openhft.koloboke.function.DoubleObjConsumer;
import net.openhft.koloboke.collect.map.DoubleObjMap;

import java.util.Map;


public final class CommonDoubleObjMapOps {

    public static boolean containsAllEntries(final InternalDoubleObjMapOps<?> map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof DoubleObjMap) {
            DoubleObjMap m2 = (DoubleObjMap) another;
            if (
                
                    m2.valueEquivalence().equals(map.valueEquivalence())
            ) {
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalDoubleObjMapOps) {
                    //noinspection unchecked
                    return ((InternalDoubleObjMapOps) m2).allEntriesContainingIn(map);
                }
            }
            // noinspection unchecked
            return m2.forEachWhile(new
                   DoubleObjPredicate() {
                @Override
                public boolean test(double a, Object b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Double) e.getKey(),
                    e.getValue()))
                return false;
        }
        return true;
    }

    public static <V> void putAll(final InternalDoubleObjMapOps<V> map,
            Map<? extends Double, ? extends V> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof DoubleObjMap) {
            if (another instanceof InternalDoubleObjMapOps) {
                ((InternalDoubleObjMapOps) another).reversePutAllTo(map);
            } else {
                ((DoubleObjMap) another).forEach(new DoubleObjConsumer<V>() {
                    @Override
                    public void accept(double key, V value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Double, ? extends V> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonDoubleObjMapOps() {}
}

