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

import net.openhft.koloboke.function.LongDoublePredicate;
import net.openhft.koloboke.function.LongDoubleConsumer;
import net.openhft.koloboke.collect.map.LongDoubleMap;

import java.util.Map;


public final class CommonLongDoubleMapOps {

    public static boolean containsAllEntries(final InternalLongDoubleMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof LongDoubleMap) {
            LongDoubleMap m2 = (LongDoubleMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalLongDoubleMapOps) {
                    //noinspection unchecked
                    return ((InternalLongDoubleMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   LongDoublePredicate() {
                @Override
                public boolean test(long a, double b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Long) e.getKey(),
                    (Double) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalLongDoubleMapOps map,
            Map<? extends Long, ? extends Double> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof LongDoubleMap) {
            if (another instanceof InternalLongDoubleMapOps) {
                ((InternalLongDoubleMapOps) another).reversePutAllTo(map);
            } else {
                ((LongDoubleMap) another).forEach(new LongDoubleConsumer() {
                    @Override
                    public void accept(long key, double value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Long, ? extends Double> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonLongDoubleMapOps() {}
}

