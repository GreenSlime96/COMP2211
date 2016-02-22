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

import net.openhft.koloboke.function.FloatIntPredicate;
import net.openhft.koloboke.function.FloatIntConsumer;
import net.openhft.koloboke.collect.map.FloatIntMap;

import java.util.Map;


public final class CommonFloatIntMapOps {

    public static boolean containsAllEntries(final InternalFloatIntMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof FloatIntMap) {
            FloatIntMap m2 = (FloatIntMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalFloatIntMapOps) {
                    //noinspection unchecked
                    return ((InternalFloatIntMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   FloatIntPredicate() {
                @Override
                public boolean test(float a, int b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Float) e.getKey(),
                    (Integer) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalFloatIntMapOps map,
            Map<? extends Float, ? extends Integer> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof FloatIntMap) {
            if (another instanceof InternalFloatIntMapOps) {
                ((InternalFloatIntMapOps) another).reversePutAllTo(map);
            } else {
                ((FloatIntMap) another).forEach(new FloatIntConsumer() {
                    @Override
                    public void accept(float key, int value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Float, ? extends Integer> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonFloatIntMapOps() {}
}

