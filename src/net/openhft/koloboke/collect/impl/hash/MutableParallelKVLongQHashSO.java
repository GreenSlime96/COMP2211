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


public abstract class MutableParallelKVLongQHashSO extends MutableQHash
        implements ParallelKVLongQHash, PrimitiveConstants, UnsafeConstants {

    long freeValue;
    long removedValue;

    long[] table;

    void copy(ParallelKVLongQHash hash) {
        super.copy(hash);
        freeValue = hash.freeValue();
        if (hash.supportRemoved())
            removedValue = hash.removedValue();

        table = hash.table().clone();

        if (!hash.supportRemoved()) {
            removedValue = freeValue;
            removedValue = findNewFreeOrRemoved();
        }
    }

    void move(ParallelKVLongQHash hash) {
        super.copy(hash);
        freeValue = hash.freeValue();
        if (hash.supportRemoved())
            removedValue = hash.removedValue();

        table = hash.table();

        if (!hash.supportRemoved()) {
            removedValue = freeValue;
            removedValue = findNewFreeOrRemoved();
        }
    }

    final void init(HashConfigWrapper configWrapper, int size, long freeValue
            , long removedValue) {
        this.freeValue = freeValue;
        this.removedValue = removedValue;
        // calls allocateArrays, fill keys with this.freeValue => assign it before
        super.init(configWrapper, size);
    }


    @Override
    public long freeValue() {
        return freeValue;
    }

    @Override
    public boolean supportRemoved() {
        return true
                ;
    }

    @Override
    public long removedValue() {
        return removedValue;
    }

    public boolean contains(Object key) {
        return contains(((Long) key).longValue());
    }

    public boolean contains(long key) {
        return index(key) >= 0;
    }

    int index(long key) {
        long free;
        if (key != (free = freeValue) && key != removedValue) {
            long[] tab = table;
            int capacity, index;
            long cur;
            if ((cur = tab[index = ParallelKVLongKeyMixing.mix(key) % (capacity = tab.length)]) == key) {
                // key is present
                return index;
            } else {
                if (cur == free) {
                    // key is absent, free slot
                    return -1;
                } else {
                    int bIndex = index, fIndex = index, step = 2;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = tab[bIndex]) == key) {
                            // key is present
                            return bIndex;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return -1;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = tab[fIndex]) == key) {
                            // key is present
                            return fIndex;
                        } else if (cur == free) {
                            // key is absent, free slot
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

    private long findNewFreeOrRemoved() {
        long free = this.freeValue;
        long removed = this.removedValue;
        Random random = ThreadLocalRandom.current();
        long newFree;
 {
            do {
                newFree = (long) random.nextLong();
            } while (newFree == free ||
                    newFree == removed ||
                    index(newFree) >= 0);
        }
        return newFree;
    }


    long changeFree() {
        int mc = modCount();
        long newFree = findNewFreeOrRemoved();
        incrementModCount();
        mc++;
        LongArrays.replaceAllKeys(table, freeValue, newFree);
        this.freeValue = newFree;
        if (mc != modCount())
            throw new ConcurrentModificationException();
        return newFree;
    }

    long changeRemoved() {
        int mc = modCount();
        long newRemoved = findNewFreeOrRemoved();
        incrementModCount();
        mc++;
        if (!noRemoved()) {
            LongArrays.replaceAllKeys(table, removedValue, newRemoved);
        }
        this.removedValue = newRemoved;
        if (mc != modCount())
            throw new ConcurrentModificationException();
        return newRemoved;
    }

    @Override
    void allocateArrays(int capacity) {
        table = new long[capacity * 2];
        if (freeValue != 0)
            LongArrays.fillKeys(table, freeValue);
    }

    @Override
    public void clear() {
        super.clear();
        LongArrays.fillKeys(table, freeValue);
    }

    @Override
    void removeAt(int index) {
        table[index] = removedValue;
    }
}

