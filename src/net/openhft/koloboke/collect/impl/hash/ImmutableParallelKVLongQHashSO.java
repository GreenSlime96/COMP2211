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

package net.openhft.koloboke.collect.impl.hash;

import net.openhft.koloboke.collect.hash.HashOverflowException;
import net.openhft.koloboke.collect.impl.*;

import java.util.*;
import java.util.concurrent
     .ThreadLocalRandom;


public abstract class ImmutableParallelKVLongQHashSO extends ImmutableQHash
        implements ParallelKVLongQHash, PrimitiveConstants, UnsafeConstants {

    long freeValue;

    long[] table;

    void copy(ParallelKVLongQHash hash) {
        super.copy(hash);
        freeValue = hash.freeValue();

        table = hash.table().clone();

    }

    void move(ParallelKVLongQHash hash) {
        super.copy(hash);
        freeValue = hash.freeValue();

        table = hash.table();

    }

    final void init(HashConfigWrapper configWrapper, int size, long freeValue
            ) {
        this.freeValue = freeValue;
        // calls allocateArrays, fill keys with this.freeValue => assign it before
        super.init(configWrapper, size);
    }


    @Override
    public long freeValue() {
        return freeValue;
    }

    @Override
    public boolean supportRemoved() {
        return false;
    }

    @Override
    public long removedValue() {
        throw new UnsupportedOperationException();
    }

    public boolean contains(Object key) {
        return contains(((Long) key).longValue());
    }

    public boolean contains(long key) {
        return index(key) >= 0;
    }

    int index(long key) {
        long free;
        if (key != (free = freeValue)) {
            long[] tab = table;
            int capacity, index;
            long cur;
            if ((cur = tab[index = ParallelKVLongKeyMixing.mix(key) % (capacity = tab.length)]) == key) {
                // key is present
                return index;
            } else {
                if (cur == free) {
                    // key is absent
                    return -1;
                } else {
                    int bIndex = index, fIndex = index, step = 2;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = tab[bIndex]) == key) {
                            // key is present
                            return bIndex;
                        } else if (cur == free) {
                            // key is absent
                            return -1;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = tab[fIndex]) == key) {
                            // key is present
                            return fIndex;
                        } else if (cur == free) {
                            // key is absent
                            return -1;
                        }
                        step += 4;
                    }
                }
            }
        } else {
            // key is absent
            return -1;
        }
    }

}
