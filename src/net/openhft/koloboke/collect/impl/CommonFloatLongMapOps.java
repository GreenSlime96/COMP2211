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

import net.openhft.koloboke.function.FloatLongPredicate;
import net.openhft.koloboke.function.FloatLongConsumer;
import net.openhft.koloboke.collect.map.FloatLongMap;

import java.util.Map;


public final class CommonFloatLongMapOps {

    public static boolean containsAllEntries(final InternalFloatLongMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof FloatLongMap) {
            FloatLongMap m2 = (FloatLongMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalFloatLongMapOps) {
                    //noinspection unchecked
                    return ((InternalFloatLongMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   FloatLongPredicate() {
                @Override
                public boolean test(float a, long b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Float) e.getKey(),
                    (Long) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalFloatLongMapOps map,
            Map<? extends Float, ? extends Long> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof FloatLongMap) {
            if (another instanceof InternalFloatLongMapOps) {
                ((InternalFloatLongMapOps) another).reversePutAllTo(map);
            } else {
                ((FloatLongMap) another).forEach(new FloatLongConsumer() {
                    @Override
                    public void accept(float key, long value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Float, ? extends Long> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonFloatLongMapOps() {}
}

