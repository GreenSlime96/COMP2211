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

import net.openhft.koloboke.function.DoubleCharPredicate;
import net.openhft.koloboke.function.DoubleCharConsumer;
import net.openhft.koloboke.collect.map.DoubleCharMap;

import java.util.Map;


public final class CommonDoubleCharMapOps {

    public static boolean containsAllEntries(final InternalDoubleCharMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof DoubleCharMap) {
            DoubleCharMap m2 = (DoubleCharMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalDoubleCharMapOps) {
                    //noinspection unchecked
                    return ((InternalDoubleCharMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   DoubleCharPredicate() {
                @Override
                public boolean test(double a, char b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Double) e.getKey(),
                    (Character) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalDoubleCharMapOps map,
            Map<? extends Double, ? extends Character> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof DoubleCharMap) {
            if (another instanceof InternalDoubleCharMapOps) {
                ((InternalDoubleCharMapOps) another).reversePutAllTo(map);
            } else {
                ((DoubleCharMap) another).forEach(new DoubleCharConsumer() {
                    @Override
                    public void accept(double key, char value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Double, ? extends Character> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonDoubleCharMapOps() {}
}

