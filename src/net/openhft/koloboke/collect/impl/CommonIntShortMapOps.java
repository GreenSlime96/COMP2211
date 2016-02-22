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

import net.openhft.koloboke.function.IntShortPredicate;
import net.openhft.koloboke.function.IntShortConsumer;
import net.openhft.koloboke.collect.map.IntShortMap;

import java.util.Map;


public final class CommonIntShortMapOps {

    public static boolean containsAllEntries(final InternalIntShortMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof IntShortMap) {
            IntShortMap m2 = (IntShortMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalIntShortMapOps) {
                    //noinspection unchecked
                    return ((InternalIntShortMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   IntShortPredicate() {
                @Override
                public boolean test(int a, short b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Integer) e.getKey(),
                    (Short) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalIntShortMapOps map,
            Map<? extends Integer, ? extends Short> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof IntShortMap) {
            if (another instanceof InternalIntShortMapOps) {
                ((InternalIntShortMapOps) another).reversePutAllTo(map);
            } else {
                ((IntShortMap) another).forEach(new IntShortConsumer() {
                    @Override
                    public void accept(int key, short value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Integer, ? extends Short> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonIntShortMapOps() {}
}

