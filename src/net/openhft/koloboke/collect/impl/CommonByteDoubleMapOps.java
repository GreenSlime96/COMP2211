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

import net.openhft.koloboke.function.ByteDoublePredicate;
import net.openhft.koloboke.function.ByteDoubleConsumer;
import net.openhft.koloboke.collect.map.ByteDoubleMap;

import java.util.Map;


public final class CommonByteDoubleMapOps {

    public static boolean containsAllEntries(final InternalByteDoubleMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof ByteDoubleMap) {
            ByteDoubleMap m2 = (ByteDoubleMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalByteDoubleMapOps) {
                    //noinspection unchecked
                    return ((InternalByteDoubleMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   ByteDoublePredicate() {
                @Override
                public boolean test(byte a, double b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Byte) e.getKey(),
                    (Double) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalByteDoubleMapOps map,
            Map<? extends Byte, ? extends Double> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof ByteDoubleMap) {
            if (another instanceof InternalByteDoubleMapOps) {
                ((InternalByteDoubleMapOps) another).reversePutAllTo(map);
            } else {
                ((ByteDoubleMap) another).forEach(new ByteDoubleConsumer() {
                    @Override
                    public void accept(byte key, double value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Byte, ? extends Double> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonByteDoubleMapOps() {}
}

