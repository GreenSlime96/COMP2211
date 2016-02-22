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

import net.openhft.koloboke.function.LongLongPredicate;
import net.openhft.koloboke.function.LongLongConsumer;
import net.openhft.koloboke.collect.map.LongLongMap;

import java.util.Map;


public final class CommonLongLongMapOps {

    public static boolean containsAllEntries(final InternalLongLongMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof LongLongMap) {
            LongLongMap m2 = (LongLongMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalLongLongMapOps) {
                    //noinspection unchecked
                    return ((InternalLongLongMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   LongLongPredicate() {
                @Override
                public boolean test(long a, long b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Long) e.getKey(),
                    (Long) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalLongLongMapOps map,
            Map<? extends Long, ? extends Long> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof LongLongMap) {
            if (another instanceof InternalLongLongMapOps) {
                ((InternalLongLongMapOps) another).reversePutAllTo(map);
            } else {
                ((LongLongMap) another).forEach(new LongLongConsumer() {
                    @Override
                    public void accept(long key, long value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Long, ? extends Long> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonLongLongMapOps() {}
}

