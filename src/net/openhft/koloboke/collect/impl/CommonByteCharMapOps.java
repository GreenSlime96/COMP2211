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

import net.openhft.koloboke.function.ByteCharPredicate;
import net.openhft.koloboke.function.ByteCharConsumer;
import net.openhft.koloboke.collect.map.ByteCharMap;

import java.util.Map;


public final class CommonByteCharMapOps {

    public static boolean containsAllEntries(final InternalByteCharMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof ByteCharMap) {
            ByteCharMap m2 = (ByteCharMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalByteCharMapOps) {
                    //noinspection unchecked
                    return ((InternalByteCharMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   ByteCharPredicate() {
                @Override
                public boolean test(byte a, char b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Byte) e.getKey(),
                    (Character) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalByteCharMapOps map,
            Map<? extends Byte, ? extends Character> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof ByteCharMap) {
            if (another instanceof InternalByteCharMapOps) {
                ((InternalByteCharMapOps) another).reversePutAllTo(map);
            } else {
                ((ByteCharMap) another).forEach(new ByteCharConsumer() {
                    @Override
                    public void accept(byte key, char value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Byte, ? extends Character> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonByteCharMapOps() {}
}

