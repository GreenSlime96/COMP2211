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

import net.openhft.koloboke.function.ByteIntPredicate;
import net.openhft.koloboke.function.ByteIntConsumer;
import net.openhft.koloboke.collect.map.ByteIntMap;

import java.util.Map;


public final class CommonByteIntMapOps {

    public static boolean containsAllEntries(final InternalByteIntMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof ByteIntMap) {
            ByteIntMap m2 = (ByteIntMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalByteIntMapOps) {
                    //noinspection unchecked
                    return ((InternalByteIntMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   ByteIntPredicate() {
                @Override
                public boolean test(byte a, int b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Byte) e.getKey(),
                    (Integer) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalByteIntMapOps map,
            Map<? extends Byte, ? extends Integer> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof ByteIntMap) {
            if (another instanceof InternalByteIntMapOps) {
                ((InternalByteIntMapOps) another).reversePutAllTo(map);
            } else {
                ((ByteIntMap) another).forEach(new ByteIntConsumer() {
                    @Override
                    public void accept(byte key, int value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Byte, ? extends Integer> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonByteIntMapOps() {}
}

