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

import net.openhft.koloboke.function.CharFloatPredicate;
import net.openhft.koloboke.function.CharFloatConsumer;
import net.openhft.koloboke.collect.map.CharFloatMap;

import java.util.Map;


public final class CommonCharFloatMapOps {

    public static boolean containsAllEntries(final InternalCharFloatMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof CharFloatMap) {
            CharFloatMap m2 = (CharFloatMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalCharFloatMapOps) {
                    //noinspection unchecked
                    return ((InternalCharFloatMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   CharFloatPredicate() {
                @Override
                public boolean test(char a, float b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Character) e.getKey(),
                    (Float) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalCharFloatMapOps map,
            Map<? extends Character, ? extends Float> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof CharFloatMap) {
            if (another instanceof InternalCharFloatMapOps) {
                ((InternalCharFloatMapOps) another).reversePutAllTo(map);
            } else {
                ((CharFloatMap) another).forEach(new CharFloatConsumer() {
                    @Override
                    public void accept(char key, float value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Character, ? extends Float> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonCharFloatMapOps() {}
}

