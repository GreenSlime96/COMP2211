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

import net.openhft.koloboke.function.IntBytePredicate;
import net.openhft.koloboke.function.IntByteConsumer;
import net.openhft.koloboke.collect.map.IntByteMap;

import java.util.Map;


public final class CommonIntByteMapOps {

    public static boolean containsAllEntries(final InternalIntByteMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof IntByteMap) {
            IntByteMap m2 = (IntByteMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalIntByteMapOps) {
                    //noinspection unchecked
                    return ((InternalIntByteMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   IntBytePredicate() {
                @Override
                public boolean test(int a, byte b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Integer) e.getKey(),
                    (Byte) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalIntByteMapOps map,
            Map<? extends Integer, ? extends Byte> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof IntByteMap) {
            if (another instanceof InternalIntByteMapOps) {
                ((InternalIntByteMapOps) another).reversePutAllTo(map);
            } else {
                ((IntByteMap) another).forEach(new IntByteConsumer() {
                    @Override
                    public void accept(int key, byte value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Integer, ? extends Byte> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonIntByteMapOps() {}
}

