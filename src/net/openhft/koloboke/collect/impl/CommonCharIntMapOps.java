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

import net.openhft.koloboke.function.CharIntPredicate;
import net.openhft.koloboke.function.CharIntConsumer;
import net.openhft.koloboke.collect.map.CharIntMap;

import java.util.Map;


public final class CommonCharIntMapOps {

    public static boolean containsAllEntries(final InternalCharIntMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof CharIntMap) {
            CharIntMap m2 = (CharIntMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalCharIntMapOps) {
                    //noinspection unchecked
                    return ((InternalCharIntMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   CharIntPredicate() {
                @Override
                public boolean test(char a, int b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Character) e.getKey(),
                    (Integer) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalCharIntMapOps map,
            Map<? extends Character, ? extends Integer> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof CharIntMap) {
            if (another instanceof InternalCharIntMapOps) {
                ((InternalCharIntMapOps) another).reversePutAllTo(map);
            } else {
                ((CharIntMap) another).forEach(new CharIntConsumer() {
                    @Override
                    public void accept(char key, int value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Character, ? extends Integer> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonCharIntMapOps() {}
}

