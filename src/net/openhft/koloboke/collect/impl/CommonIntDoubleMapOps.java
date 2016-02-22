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

import net.openhft.koloboke.function.IntDoublePredicate;
import net.openhft.koloboke.function.IntDoubleConsumer;
import net.openhft.koloboke.collect.map.IntDoubleMap;

import java.util.Map;


public final class CommonIntDoubleMapOps {

    public static boolean containsAllEntries(final InternalIntDoubleMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof IntDoubleMap) {
            IntDoubleMap m2 = (IntDoubleMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalIntDoubleMapOps) {
                    //noinspection unchecked
                    return ((InternalIntDoubleMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   IntDoublePredicate() {
                @Override
                public boolean test(int a, double b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Integer) e.getKey(),
                    (Double) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalIntDoubleMapOps map,
            Map<? extends Integer, ? extends Double> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof IntDoubleMap) {
            if (another instanceof InternalIntDoubleMapOps) {
                ((InternalIntDoubleMapOps) another).reversePutAllTo(map);
            } else {
                ((IntDoubleMap) another).forEach(new IntDoubleConsumer() {
                    @Override
                    public void accept(int key, double value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Integer, ? extends Double> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonIntDoubleMapOps() {}
}
