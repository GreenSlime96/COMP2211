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

import net.openhft.koloboke.function.ByteFloatPredicate;
import net.openhft.koloboke.function.ByteFloatConsumer;
import net.openhft.koloboke.collect.map.ByteFloatMap;

import java.util.Map;


public final class CommonByteFloatMapOps {

    public static boolean containsAllEntries(final InternalByteFloatMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof ByteFloatMap) {
            ByteFloatMap m2 = (ByteFloatMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalByteFloatMapOps) {
                    //noinspection unchecked
                    return ((InternalByteFloatMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   ByteFloatPredicate() {
                @Override
                public boolean test(byte a, float b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Byte) e.getKey(),
                    (Float) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalByteFloatMapOps map,
            Map<? extends Byte, ? extends Float> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof ByteFloatMap) {
            if (another instanceof InternalByteFloatMapOps) {
                ((InternalByteFloatMapOps) another).reversePutAllTo(map);
            } else {
                ((ByteFloatMap) another).forEach(new ByteFloatConsumer() {
                    @Override
                    public void accept(byte key, float value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Byte, ? extends Float> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonByteFloatMapOps() {}
}

