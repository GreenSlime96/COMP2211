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

import net.openhft.koloboke.function.LongObjPredicate;
import net.openhft.koloboke.function.LongObjConsumer;
import net.openhft.koloboke.collect.map.LongObjMap;

import java.util.Map;


public final class CommonLongObjMapOps {

    public static boolean containsAllEntries(final InternalLongObjMapOps<?> map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof LongObjMap) {
            LongObjMap m2 = (LongObjMap) another;
            if (
                
                    m2.valueEquivalence().equals(map.valueEquivalence())
            ) {
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalLongObjMapOps) {
                    //noinspection unchecked
                    return ((InternalLongObjMapOps) m2).allEntriesContainingIn(map);
                }
            }
            // noinspection unchecked
            return m2.forEachWhile(new
                   LongObjPredicate() {
                @Override
                public boolean test(long a, Object b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Long) e.getKey(),
                    e.getValue()))
                return false;
        }
        return true;
    }

    public static <V> void putAll(final InternalLongObjMapOps<V> map,
            Map<? extends Long, ? extends V> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof LongObjMap) {
            if (another instanceof InternalLongObjMapOps) {
                ((InternalLongObjMapOps) another).reversePutAllTo(map);
            } else {
                ((LongObjMap) another).forEach(new LongObjConsumer<V>() {
                    @Override
                    public void accept(long key, V value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Long, ? extends V> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonLongObjMapOps() {}
}

