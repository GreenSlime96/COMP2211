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

import net.openhft.koloboke.function.ShortFloatPredicate;
import net.openhft.koloboke.function.ShortFloatConsumer;
import net.openhft.koloboke.collect.map.ShortFloatMap;

import java.util.Map;


public final class CommonShortFloatMapOps {

    public static boolean containsAllEntries(final InternalShortFloatMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof ShortFloatMap) {
            ShortFloatMap m2 = (ShortFloatMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalShortFloatMapOps) {
                    //noinspection unchecked
                    return ((InternalShortFloatMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   ShortFloatPredicate() {
                @Override
                public boolean test(short a, float b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Short) e.getKey(),
                    (Float) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalShortFloatMapOps map,
            Map<? extends Short, ? extends Float> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof ShortFloatMap) {
            if (another instanceof InternalShortFloatMapOps) {
                ((InternalShortFloatMapOps) another).reversePutAllTo(map);
            } else {
                ((ShortFloatMap) another).forEach(new ShortFloatConsumer() {
                    @Override
                    public void accept(short key, float value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Short, ? extends Float> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonShortFloatMapOps() {}
}

