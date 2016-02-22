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

import net.openhft.koloboke.function.IntLongPredicate;
import net.openhft.koloboke.function.IntLongConsumer;
import net.openhft.koloboke.collect.map.IntLongMap;

import java.util.Map;


public final class CommonIntLongMapOps {

    public static boolean containsAllEntries(final InternalIntLongMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof IntLongMap) {
            IntLongMap m2 = (IntLongMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalIntLongMapOps) {
                    //noinspection unchecked
                    return ((InternalIntLongMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   IntLongPredicate() {
                @Override
                public boolean test(int a, long b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Integer) e.getKey(),
                    (Long) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalIntLongMapOps map,
            Map<? extends Integer, ? extends Long> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof IntLongMap) {
            if (another instanceof InternalIntLongMapOps) {
                ((InternalIntLongMapOps) another).reversePutAllTo(map);
            } else {
                ((IntLongMap) another).forEach(new IntLongConsumer() {
                    @Override
                    public void accept(int key, long value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Integer, ? extends Long> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonIntLongMapOps() {}
}

