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

import net.openhft.koloboke.function.ShortBytePredicate;
import net.openhft.koloboke.function.ShortByteConsumer;
import net.openhft.koloboke.collect.map.ShortByteMap;

import java.util.Map;


public final class CommonShortByteMapOps {

    public static boolean containsAllEntries(final InternalShortByteMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof ShortByteMap) {
            ShortByteMap m2 = (ShortByteMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalShortByteMapOps) {
                    //noinspection unchecked
                    return ((InternalShortByteMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   ShortBytePredicate() {
                @Override
                public boolean test(short a, byte b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Short) e.getKey(),
                    (Byte) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalShortByteMapOps map,
            Map<? extends Short, ? extends Byte> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof ShortByteMap) {
            if (another instanceof InternalShortByteMapOps) {
                ((InternalShortByteMapOps) another).reversePutAllTo(map);
            } else {
                ((ShortByteMap) another).forEach(new ShortByteConsumer() {
                    @Override
                    public void accept(short key, byte value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Short, ? extends Byte> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonShortByteMapOps() {}
}

