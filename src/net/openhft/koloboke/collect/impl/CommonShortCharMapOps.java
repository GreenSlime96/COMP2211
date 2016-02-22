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

import net.openhft.koloboke.function.ShortCharPredicate;
import net.openhft.koloboke.function.ShortCharConsumer;
import net.openhft.koloboke.collect.map.ShortCharMap;

import java.util.Map;


public final class CommonShortCharMapOps {

    public static boolean containsAllEntries(final InternalShortCharMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof ShortCharMap) {
            ShortCharMap m2 = (ShortCharMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalShortCharMapOps) {
                    //noinspection unchecked
                    return ((InternalShortCharMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   ShortCharPredicate() {
                @Override
                public boolean test(short a, char b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Short) e.getKey(),
                    (Character) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalShortCharMapOps map,
            Map<? extends Short, ? extends Character> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof ShortCharMap) {
            if (another instanceof InternalShortCharMapOps) {
                ((InternalShortCharMapOps) another).reversePutAllTo(map);
            } else {
                ((ShortCharMap) another).forEach(new ShortCharConsumer() {
                    @Override
                    public void accept(short key, char value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Short, ? extends Character> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonShortCharMapOps() {}
}

