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

import net.openhft.koloboke.function.FloatCharPredicate;
import net.openhft.koloboke.function.FloatCharConsumer;
import net.openhft.koloboke.collect.map.FloatCharMap;

import java.util.Map;


public final class CommonFloatCharMapOps {

    public static boolean containsAllEntries(final InternalFloatCharMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof FloatCharMap) {
            FloatCharMap m2 = (FloatCharMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalFloatCharMapOps) {
                    //noinspection unchecked
                    return ((InternalFloatCharMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   FloatCharPredicate() {
                @Override
                public boolean test(float a, char b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Float) e.getKey(),
                    (Character) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalFloatCharMapOps map,
            Map<? extends Float, ? extends Character> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof FloatCharMap) {
            if (another instanceof InternalFloatCharMapOps) {
                ((InternalFloatCharMapOps) another).reversePutAllTo(map);
            } else {
                ((FloatCharMap) another).forEach(new FloatCharConsumer() {
                    @Override
                    public void accept(float key, char value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Float, ? extends Character> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonFloatCharMapOps() {}
}

