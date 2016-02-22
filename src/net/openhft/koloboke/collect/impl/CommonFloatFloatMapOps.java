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

import net.openhft.koloboke.function.FloatFloatPredicate;
import net.openhft.koloboke.function.FloatFloatConsumer;
import net.openhft.koloboke.collect.map.FloatFloatMap;

import java.util.Map;


public final class CommonFloatFloatMapOps {

    public static boolean containsAllEntries(final InternalFloatFloatMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof FloatFloatMap) {
            FloatFloatMap m2 = (FloatFloatMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalFloatFloatMapOps) {
                    //noinspection unchecked
                    return ((InternalFloatFloatMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   FloatFloatPredicate() {
                @Override
                public boolean test(float a, float b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Float) e.getKey(),
                    (Float) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalFloatFloatMapOps map,
            Map<? extends Float, ? extends Float> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof FloatFloatMap) {
            if (another instanceof InternalFloatFloatMapOps) {
                ((InternalFloatFloatMapOps) another).reversePutAllTo(map);
            } else {
                ((FloatFloatMap) another).forEach(new FloatFloatConsumer() {
                    @Override
                    public void accept(float key, float value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Float, ? extends Float> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonFloatFloatMapOps() {}
}

