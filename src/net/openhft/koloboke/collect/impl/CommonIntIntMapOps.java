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

import net.openhft.koloboke.function.IntIntPredicate;
import net.openhft.koloboke.function.IntIntConsumer;
import net.openhft.koloboke.collect.map.IntIntMap;

import java.util.Map;


public final class CommonIntIntMapOps {

    public static boolean containsAllEntries(final InternalIntIntMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof IntIntMap) {
            IntIntMap m2 = (IntIntMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalIntIntMapOps) {
                    //noinspection unchecked
                    return ((InternalIntIntMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   IntIntPredicate() {
                @Override
                public boolean test(int a, int b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Integer) e.getKey(),
                    (Integer) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalIntIntMapOps map,
            Map<? extends Integer, ? extends Integer> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof IntIntMap) {
            if (another instanceof InternalIntIntMapOps) {
                ((InternalIntIntMapOps) another).reversePutAllTo(map);
            } else {
                ((IntIntMap) another).forEach(new IntIntConsumer() {
                    @Override
                    public void accept(int key, int value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Integer, ? extends Integer> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonIntIntMapOps() {}
}

