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

import net.openhft.koloboke.function.DoubleBytePredicate;
import net.openhft.koloboke.function.DoubleByteConsumer;
import net.openhft.koloboke.collect.map.DoubleByteMap;

import java.util.Map;


public final class CommonDoubleByteMapOps {

    public static boolean containsAllEntries(final InternalDoubleByteMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof DoubleByteMap) {
            DoubleByteMap m2 = (DoubleByteMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalDoubleByteMapOps) {
                    //noinspection unchecked
                    return ((InternalDoubleByteMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   DoubleBytePredicate() {
                @Override
                public boolean test(double a, byte b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Double) e.getKey(),
                    (Byte) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalDoubleByteMapOps map,
            Map<? extends Double, ? extends Byte> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof DoubleByteMap) {
            if (another instanceof InternalDoubleByteMapOps) {
                ((InternalDoubleByteMapOps) another).reversePutAllTo(map);
            } else {
                ((DoubleByteMap) another).forEach(new DoubleByteConsumer() {
                    @Override
                    public void accept(double key, byte value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Double, ? extends Byte> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonDoubleByteMapOps() {}
}

