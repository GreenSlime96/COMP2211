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

import net.openhft.koloboke.function.DoubleDoublePredicate;
import net.openhft.koloboke.function.DoubleDoubleConsumer;
import net.openhft.koloboke.collect.map.DoubleDoubleMap;

import java.util.Map;


public final class CommonDoubleDoubleMapOps {

    public static boolean containsAllEntries(final InternalDoubleDoubleMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof DoubleDoubleMap) {
            DoubleDoubleMap m2 = (DoubleDoubleMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalDoubleDoubleMapOps) {
                    //noinspection unchecked
                    return ((InternalDoubleDoubleMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   DoubleDoublePredicate() {
                @Override
                public boolean test(double a, double b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Double) e.getKey(),
                    (Double) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalDoubleDoubleMapOps map,
            Map<? extends Double, ? extends Double> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof DoubleDoubleMap) {
            if (another instanceof InternalDoubleDoubleMapOps) {
                ((InternalDoubleDoubleMapOps) another).reversePutAllTo(map);
            } else {
                ((DoubleDoubleMap) another).forEach(new DoubleDoubleConsumer() {
                    @Override
                    public void accept(double key, double value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Double, ? extends Double> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonDoubleDoubleMapOps() {}
}

