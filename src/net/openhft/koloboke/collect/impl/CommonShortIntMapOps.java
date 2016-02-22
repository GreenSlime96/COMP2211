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

import net.openhft.koloboke.function.ShortIntPredicate;
import net.openhft.koloboke.function.ShortIntConsumer;
import net.openhft.koloboke.collect.map.ShortIntMap;

import java.util.Map;


public final class CommonShortIntMapOps {

    public static boolean containsAllEntries(final InternalShortIntMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof ShortIntMap) {
            ShortIntMap m2 = (ShortIntMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalShortIntMapOps) {
                    //noinspection unchecked
                    return ((InternalShortIntMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   ShortIntPredicate() {
                @Override
                public boolean test(short a, int b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Short) e.getKey(),
                    (Integer) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalShortIntMapOps map,
            Map<? extends Short, ? extends Integer> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof ShortIntMap) {
            if (another instanceof InternalShortIntMapOps) {
                ((InternalShortIntMapOps) another).reversePutAllTo(map);
            } else {
                ((ShortIntMap) another).forEach(new ShortIntConsumer() {
                    @Override
                    public void accept(short key, int value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Short, ? extends Integer> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonShortIntMapOps() {}
}

