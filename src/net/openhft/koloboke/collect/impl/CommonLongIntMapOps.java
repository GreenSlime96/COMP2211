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

import net.openhft.koloboke.function.LongIntPredicate;
import net.openhft.koloboke.function.LongIntConsumer;
import net.openhft.koloboke.collect.map.LongIntMap;

import java.util.Map;


public final class CommonLongIntMapOps {

    public static boolean containsAllEntries(final InternalLongIntMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof LongIntMap) {
            LongIntMap m2 = (LongIntMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalLongIntMapOps) {
                    //noinspection unchecked
                    return ((InternalLongIntMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   LongIntPredicate() {
                @Override
                public boolean test(long a, int b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Long) e.getKey(),
                    (Integer) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalLongIntMapOps map,
            Map<? extends Long, ? extends Integer> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof LongIntMap) {
            if (another instanceof InternalLongIntMapOps) {
                ((InternalLongIntMapOps) another).reversePutAllTo(map);
            } else {
                ((LongIntMap) another).forEach(new LongIntConsumer() {
                    @Override
                    public void accept(long key, int value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Long, ? extends Integer> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonLongIntMapOps() {}
}

