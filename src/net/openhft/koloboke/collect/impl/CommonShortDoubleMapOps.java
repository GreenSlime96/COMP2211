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

import net.openhft.koloboke.function.ShortDoublePredicate;
import net.openhft.koloboke.function.ShortDoubleConsumer;
import net.openhft.koloboke.collect.map.ShortDoubleMap;

import java.util.Map;


public final class CommonShortDoubleMapOps {

    public static boolean containsAllEntries(final InternalShortDoubleMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof ShortDoubleMap) {
            ShortDoubleMap m2 = (ShortDoubleMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalShortDoubleMapOps) {
                    //noinspection unchecked
                    return ((InternalShortDoubleMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   ShortDoublePredicate() {
                @Override
                public boolean test(short a, double b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Short) e.getKey(),
                    (Double) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalShortDoubleMapOps map,
            Map<? extends Short, ? extends Double> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof ShortDoubleMap) {
            if (another instanceof InternalShortDoubleMapOps) {
                ((InternalShortDoubleMapOps) another).reversePutAllTo(map);
            } else {
                ((ShortDoubleMap) another).forEach(new ShortDoubleConsumer() {
                    @Override
                    public void accept(short key, double value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Short, ? extends Double> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonShortDoubleMapOps() {}
}

