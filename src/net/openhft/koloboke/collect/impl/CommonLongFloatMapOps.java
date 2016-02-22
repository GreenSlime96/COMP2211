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

import net.openhft.koloboke.function.LongFloatPredicate;
import net.openhft.koloboke.function.LongFloatConsumer;
import net.openhft.koloboke.collect.map.LongFloatMap;

import java.util.Map;


public final class CommonLongFloatMapOps {

    public static boolean containsAllEntries(final InternalLongFloatMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof LongFloatMap) {
            LongFloatMap m2 = (LongFloatMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalLongFloatMapOps) {
                    //noinspection unchecked
                    return ((InternalLongFloatMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   LongFloatPredicate() {
                @Override
                public boolean test(long a, float b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Long) e.getKey(),
                    (Float) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalLongFloatMapOps map,
            Map<? extends Long, ? extends Float> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof LongFloatMap) {
            if (another instanceof InternalLongFloatMapOps) {
                ((InternalLongFloatMapOps) another).reversePutAllTo(map);
            } else {
                ((LongFloatMap) another).forEach(new LongFloatConsumer() {
                    @Override
                    public void accept(long key, float value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Long, ? extends Float> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonLongFloatMapOps() {}
}

