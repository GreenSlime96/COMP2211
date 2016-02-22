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

import net.openhft.koloboke.function.ByteBytePredicate;
import net.openhft.koloboke.function.ByteByteConsumer;
import net.openhft.koloboke.collect.map.ByteByteMap;

import java.util.Map;


public final class CommonByteByteMapOps {

    public static boolean containsAllEntries(final InternalByteByteMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof ByteByteMap) {
            ByteByteMap m2 = (ByteByteMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalByteByteMapOps) {
                    //noinspection unchecked
                    return ((InternalByteByteMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   ByteBytePredicate() {
                @Override
                public boolean test(byte a, byte b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Byte) e.getKey(),
                    (Byte) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalByteByteMapOps map,
            Map<? extends Byte, ? extends Byte> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof ByteByteMap) {
            if (another instanceof InternalByteByteMapOps) {
                ((InternalByteByteMapOps) another).reversePutAllTo(map);
            } else {
                ((ByteByteMap) another).forEach(new ByteByteConsumer() {
                    @Override
                    public void accept(byte key, byte value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Byte, ? extends Byte> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonByteByteMapOps() {}
}

