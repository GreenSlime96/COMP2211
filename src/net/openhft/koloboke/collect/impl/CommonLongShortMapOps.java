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

import net.openhft.koloboke.function.LongShortPredicate;
import net.openhft.koloboke.function.LongShortConsumer;
import net.openhft.koloboke.collect.map.LongShortMap;

import java.util.Map;


public final class CommonLongShortMapOps {

    public static boolean containsAllEntries(final InternalLongShortMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof LongShortMap) {
            LongShortMap m2 = (LongShortMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalLongShortMapOps) {
                    //noinspection unchecked
                    return ((InternalLongShortMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   LongShortPredicate() {
                @Override
                public boolean test(long a, short b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Long) e.getKey(),
                    (Short) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalLongShortMapOps map,
            Map<? extends Long, ? extends Short> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof LongShortMap) {
            if (another instanceof InternalLongShortMapOps) {
                ((InternalLongShortMapOps) another).reversePutAllTo(map);
            } else {
                ((LongShortMap) another).forEach(new LongShortConsumer() {
                    @Override
                    public void accept(long key, short value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Long, ? extends Short> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonLongShortMapOps() {}
}

