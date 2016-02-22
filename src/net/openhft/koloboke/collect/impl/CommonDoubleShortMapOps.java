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

import net.openhft.koloboke.function.DoubleShortPredicate;
import net.openhft.koloboke.function.DoubleShortConsumer;
import net.openhft.koloboke.collect.map.DoubleShortMap;

import java.util.Map;


public final class CommonDoubleShortMapOps {

    public static boolean containsAllEntries(final InternalDoubleShortMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof DoubleShortMap) {
            DoubleShortMap m2 = (DoubleShortMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalDoubleShortMapOps) {
                    //noinspection unchecked
                    return ((InternalDoubleShortMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   DoubleShortPredicate() {
                @Override
                public boolean test(double a, short b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Double) e.getKey(),
                    (Short) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalDoubleShortMapOps map,
            Map<? extends Double, ? extends Short> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof DoubleShortMap) {
            if (another instanceof InternalDoubleShortMapOps) {
                ((InternalDoubleShortMapOps) another).reversePutAllTo(map);
            } else {
                ((DoubleShortMap) another).forEach(new DoubleShortConsumer() {
                    @Override
                    public void accept(double key, short value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Double, ? extends Short> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonDoubleShortMapOps() {}
}

