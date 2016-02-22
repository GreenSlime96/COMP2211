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


public abstract class MutableSeparateKVLongLHashSO extends MutableLHash
        implements SeparateKVLongLHash, PrimitiveConstants, UnsafeConstants {

    long freeValue;

    long[] set;

    void copy(SeparateKVLongLHash hash) {
        super.copy(hash);
        freeValue = hash.freeValue();

        set = hash.keys().clone();

    }

    void move(SeparateKVLongLHash hash) {
        super.copy(hash);
        freeValue = hash.freeValue();

        set = hash.keys();

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
            long[] keys = set;
            int capacityMask, index;
            long cur;
            if ((cur = keys[index = SeparateKVLongKeyMixing.mix(key) & (capacityMask = keys.length - 1)]) == key) {
                // key is present
                return index;
            } else {
                if (cur == free) {
                    // key is absent
                    return -1;
                } else {
                    while (true) {
                        if ((cur = keys[(index = (index - 1) & capacityMask)]) == key) {
                            // key is present
                            return index;
                        } else if (cur == free) {
                            // key is absent
                            return -1;
                        }
                    }
                }
            }
        } else {
            // key is absent
            return -1;
        }
    }

    private long findNewFreeOrRemoved() {
        long free = this.freeValue;
        
        Random random = ThreadLocalRandom.current();
        long newFree;
 {
            do {
                newFree = (long) random.nextLong();
            } while (newFree == free ||
                    
                    index(newFree) >= 0);
        }
        return newFree;
    }


    long changeFree() {
        int mc = modCount();
        long newFree = findNewFreeOrRemoved();
        incrementModCount();
        mc++;
        LongArrays.replaceAll(set, freeValue, newFree);
        this.freeValue = newFree;
        if (mc != modCount())
            throw new ConcurrentModificationException();
        return newFree;
    }


    @Override
    void allocateArrays(int capacity) {
        set = new long[capacity];
        if (freeValue != 0)
            Arrays.fill(set, freeValue);
    }

    @Override
    public void clear() {
        super.clear();
        Arrays.fill(set, freeValue);
    }

}

