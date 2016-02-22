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

import net.openhft.koloboke.function.ByteObjPredicate;
import net.openhft.koloboke.function.ByteObjConsumer;
import net.openhft.koloboke.collect.map.ByteObjMap;

import java.util.Map;


public final class CommonByteObjMapOps {

    public static boolean containsAllEntries(final InternalByteObjMapOps<?> map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof ByteObjMap) {
            ByteObjMap m2 = (ByteObjMap) another;
            if (
                
                    m2.valueEquivalence().equals(map.valueEquivalence())
            ) {
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalByteObjMapOps) {
                    //noinspection unchecked
                    return ((InternalByteObjMapOps) m2).allEntriesContainingIn(map);
                }
            }
            // noinspection unchecked
            return m2.forEachWhile(new
                   ByteObjPredicate() {
                @Override
                public boolean test(byte a, Object b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Byte) e.getKey(),
                    e.getValue()))
                return false;
        }
        return true;
    }

    public static <V> void putAll(final InternalByteObjMapOps<V> map,
            Map<? extends Byte, ? extends V> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof ByteObjMap) {
            if (another instanceof InternalByteObjMapOps) {
                ((InternalByteObjMapOps) another).reversePutAllTo(map);
            } else {
                ((ByteObjMap) another).forEach(new ByteObjConsumer<V>() {
                    @Override
                    public void accept(byte key, V value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Byte, ? extends V> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonByteObjMapOps() {}
}

