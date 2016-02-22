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

import net.openhft.koloboke.function.LongCharPredicate;
import net.openhft.koloboke.function.LongCharConsumer;
import net.openhft.koloboke.collect.map.LongCharMap;

import java.util.Map;


public final class CommonLongCharMapOps {

    public static boolean containsAllEntries(final InternalLongCharMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof LongCharMap) {
            LongCharMap m2 = (LongCharMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalLongCharMapOps) {
                    //noinspection unchecked
                    return ((InternalLongCharMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   LongCharPredicate() {
                @Override
                public boolean test(long a, char b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Long) e.getKey(),
                    (Character) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalLongCharMapOps map,
            Map<? extends Long, ? extends Character> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof LongCharMap) {
            if (another instanceof InternalLongCharMapOps) {
                ((InternalLongCharMapOps) another).reversePutAllTo(map);
            } else {
                ((LongCharMap) another).forEach(new LongCharConsumer() {
                    @Override
                    public void accept(long key, char value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Long, ? extends Character> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonLongCharMapOps() {}
}

