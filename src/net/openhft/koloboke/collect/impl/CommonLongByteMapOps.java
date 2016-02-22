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

import net.openhft.koloboke.function.LongBytePredicate;
import net.openhft.koloboke.function.LongByteConsumer;
import net.openhft.koloboke.collect.map.LongByteMap;

import java.util.Map;


public final class CommonLongByteMapOps {

    public static boolean containsAllEntries(final InternalLongByteMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof LongByteMap) {
            LongByteMap m2 = (LongByteMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalLongByteMapOps) {
                    //noinspection unchecked
                    return ((InternalLongByteMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   LongBytePredicate() {
                @Override
                public boolean test(long a, byte b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Long) e.getKey(),
                    (Byte) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalLongByteMapOps map,
            Map<? extends Long, ? extends Byte> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof LongByteMap) {
            if (another instanceof InternalLongByteMapOps) {
                ((InternalLongByteMapOps) another).reversePutAllTo(map);
            } else {
                ((LongByteMap) another).forEach(new LongByteConsumer() {
                    @Override
                    public void accept(long key, byte value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Long, ? extends Byte> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonLongByteMapOps() {}
}

