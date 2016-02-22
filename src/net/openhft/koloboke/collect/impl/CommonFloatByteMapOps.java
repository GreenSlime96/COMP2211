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

import net.openhft.koloboke.function.FloatBytePredicate;
import net.openhft.koloboke.function.FloatByteConsumer;
import net.openhft.koloboke.collect.map.FloatByteMap;

import java.util.Map;


public final class CommonFloatByteMapOps {

    public static boolean containsAllEntries(final InternalFloatByteMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof FloatByteMap) {
            FloatByteMap m2 = (FloatByteMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalFloatByteMapOps) {
                    //noinspection unchecked
                    return ((InternalFloatByteMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   FloatBytePredicate() {
                @Override
                public boolean test(float a, byte b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Float) e.getKey(),
                    (Byte) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalFloatByteMapOps map,
            Map<? extends Float, ? extends Byte> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof FloatByteMap) {
            if (another instanceof InternalFloatByteMapOps) {
                ((InternalFloatByteMapOps) another).reversePutAllTo(map);
            } else {
                ((FloatByteMap) another).forEach(new FloatByteConsumer() {
                    @Override
                    public void accept(float key, byte value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Float, ? extends Byte> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonFloatByteMapOps() {}
}

