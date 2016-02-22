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

import net.openhft.koloboke.function.CharCharPredicate;
import net.openhft.koloboke.function.CharCharConsumer;
import net.openhft.koloboke.collect.map.CharCharMap;

import java.util.Map;


public final class CommonCharCharMapOps {

    public static boolean containsAllEntries(final InternalCharCharMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof CharCharMap) {
            CharCharMap m2 = (CharCharMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalCharCharMapOps) {
                    //noinspection unchecked
                    return ((InternalCharCharMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   CharCharPredicate() {
                @Override
                public boolean test(char a, char b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Character) e.getKey(),
                    (Character) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalCharCharMapOps map,
            Map<? extends Character, ? extends Character> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof CharCharMap) {
            if (another instanceof InternalCharCharMapOps) {
                ((InternalCharCharMapOps) another).reversePutAllTo(map);
            } else {
                ((CharCharMap) another).forEach(new CharCharConsumer() {
                    @Override
                    public void accept(char key, char value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Character, ? extends Character> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonCharCharMapOps() {}
}

