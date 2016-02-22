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

import net.openhft.koloboke.function.CharDoublePredicate;
import net.openhft.koloboke.function.CharDoubleConsumer;
import net.openhft.koloboke.collect.map.CharDoubleMap;

import java.util.Map;


public final class CommonCharDoubleMapOps {

    public static boolean containsAllEntries(final InternalCharDoubleMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof CharDoubleMap) {
            CharDoubleMap m2 = (CharDoubleMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalCharDoubleMapOps) {
                    //noinspection unchecked
                    return ((InternalCharDoubleMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   CharDoublePredicate() {
                @Override
                public boolean test(char a, double b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Character) e.getKey(),
                    (Double) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalCharDoubleMapOps map,
            Map<? extends Character, ? extends Double> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof CharDoubleMap) {
            if (another instanceof InternalCharDoubleMapOps) {
                ((InternalCharDoubleMapOps) another).reversePutAllTo(map);
            } else {
                ((CharDoubleMap) another).forEach(new CharDoubleConsumer() {
                    @Override
                    public void accept(char key, double value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Character, ? extends Double> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonCharDoubleMapOps() {}
}

