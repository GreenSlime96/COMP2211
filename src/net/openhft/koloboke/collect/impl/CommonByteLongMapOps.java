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

import net.openhft.koloboke.function.ByteLongPredicate;
import net.openhft.koloboke.function.ByteLongConsumer;
import net.openhft.koloboke.collect.map.ByteLongMap;

import java.util.Map;


public final class CommonByteLongMapOps {

    public static boolean containsAllEntries(final InternalByteLongMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof ByteLongMap) {
            ByteLongMap m2 = (ByteLongMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalByteLongMapOps) {
                    //noinspection unchecked
                    return ((InternalByteLongMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   ByteLongPredicate() {
                @Override
                public boolean test(byte a, long b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Byte) e.getKey(),
                    (Long) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalByteLongMapOps map,
            Map<? extends Byte, ? extends Long> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof ByteLongMap) {
            if (another instanceof InternalByteLongMapOps) {
                ((InternalByteLongMapOps) another).reversePutAllTo(map);
            } else {
                ((ByteLongMap) another).forEach(new ByteLongConsumer() {
                    @Override
                    public void accept(byte key, long value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Byte, ? extends Long> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonByteLongMapOps() {}
}

