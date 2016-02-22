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

import net.openhft.koloboke.function.DoubleFloatPredicate;
import net.openhft.koloboke.function.DoubleFloatConsumer;
import net.openhft.koloboke.collect.map.DoubleFloatMap;

import java.util.Map;


public final class CommonDoubleFloatMapOps {

    public static boolean containsAllEntries(final InternalDoubleFloatMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof DoubleFloatMap) {
            DoubleFloatMap m2 = (DoubleFloatMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalDoubleFloatMapOps) {
                    //noinspection unchecked
                    return ((InternalDoubleFloatMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   DoubleFloatPredicate() {
                @Override
                public boolean test(double a, float b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Double) e.getKey(),
                    (Float) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalDoubleFloatMapOps map,
            Map<? extends Double, ? extends Float> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof DoubleFloatMap) {
            if (another instanceof InternalDoubleFloatMapOps) {
                ((InternalDoubleFloatMapOps) another).reversePutAllTo(map);
            } else {
                ((DoubleFloatMap) another).forEach(new DoubleFloatConsumer() {
                    @Override
                    public void accept(double key, float value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Double, ? extends Float> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonDoubleFloatMapOps() {}
}

