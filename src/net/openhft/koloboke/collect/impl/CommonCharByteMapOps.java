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

import net.openhft.koloboke.function.CharBytePredicate;
import net.openhft.koloboke.function.CharByteConsumer;
import net.openhft.koloboke.collect.map.CharByteMap;

import java.util.Map;


public final class CommonCharByteMapOps {

    public static boolean containsAllEntries(final InternalCharByteMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof CharByteMap) {
            CharByteMap m2 = (CharByteMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalCharByteMapOps) {
                    //noinspection unchecked
                    return ((InternalCharByteMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   CharBytePredicate() {
                @Override
                public boolean test(char a, byte b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Character) e.getKey(),
                    (Byte) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalCharByteMapOps map,
            Map<? extends Character, ? extends Byte> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof CharByteMap) {
            if (another instanceof InternalCharByteMapOps) {
                ((InternalCharByteMapOps) another).reversePutAllTo(map);
            } else {
                ((CharByteMap) another).forEach(new CharByteConsumer() {
                    @Override
                    public void accept(char key, byte value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Character, ? extends Byte> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonCharByteMapOps() {}
}

