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

import net.openhft.koloboke.function.IntFloatPredicate;
import net.openhft.koloboke.function.IntFloatConsumer;
import net.openhft.koloboke.collect.map.IntFloatMap;

import java.util.Map;


public final class CommonIntFloatMapOps {

    public static boolean containsAllEntries(final InternalIntFloatMapOps map,
            Map<?, ?> another) {
        if ( map == another )
            throw new IllegalArgumentException();
        if (another instanceof IntFloatMap) {
            IntFloatMap m2 = (IntFloatMap) another;
                if (map.size() < m2.size())
                    return false;
                if (m2 instanceof InternalIntFloatMapOps) {
                    //noinspection unchecked
                    return ((InternalIntFloatMapOps) m2).allEntriesContainingIn(map);
                }
            return m2.forEachWhile(new
                   IntFloatPredicate() {
                @Override
                public boolean test(int a, float b) {
                    return map.containsEntry(a, b);
                }
            });
        }
        for ( Map.Entry<?, ?> e : another.entrySet() ) {
            if ( !map.containsEntry((Integer) e.getKey(),
                    (Float) e.getValue()))
                return false;
        }
        return true;
    }

    public static  void putAll(final InternalIntFloatMapOps map,
            Map<? extends Integer, ? extends Float> another) {
        if (map == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = map.sizeAsLong() + Containers.sizeAsLong(another);
        map.ensureCapacity(maxPossibleSize);
        if (another instanceof IntFloatMap) {
            if (another instanceof InternalIntFloatMapOps) {
                ((InternalIntFloatMapOps) another).reversePutAllTo(map);
            } else {
                ((IntFloatMap) another).forEach(new IntFloatConsumer() {
                    @Override
                    public void accept(int key, float value) {
                        map.justPut(key, value);
                    }
                });
            }
        } else {
            for (Map.Entry<? extends Integer, ? extends Float> e : another.entrySet()) {
                map.justPut(e.getKey(), e.getValue());
            }
        }
    }

    private CommonIntFloatMapOps() {}
}

