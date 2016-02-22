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


public abstract class MutableParallelKVIntLHashSO extends MutableLHash
        implements ParallelKVIntLHash, PrimitiveConstants, UnsafeConstants {

    int freeValue;

    long[] table;

    void copy(ParallelKVIntLHash hash) {
        super.copy(hash);
        freeValue = hash.freeValue();

        table = hash.table().clone();

    }

    void move(ParallelKVIntLHash hash) {
        super.copy(hash);
        freeValue = hash.freeValue();

        table = hash.table();

    }

    final void init(HashConfigWrapper configWrapper, int size, int freeValue
            ) {
        this.freeValue = freeValue;
        // calls allocateArrays, fill keys with this.freeValue => assign it before
        super.init(configWrapper, size);
    }


    @Override
    public int freeValue() {
        return freeValue;
    }

    @Override
    public boolean supportRemoved() {
        return false;
    }

    @Override
    public int removedValue() {
        throw new UnsupportedOperationException();
    }

    public boolean contains(Object key) {
        return contains(((Integer) key).intValue());
    }

    public boolean contains(int key) {
        return index(key) >= 0;
    }

    int index(int key) {
        int free;
        if (key != (free = freeValue)) {
            long[] tab = table;
            int capacityMask, index;
            int cur;
            long entry;
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
                // key is present
                return index;
            } else {
                if (cur == free) {
                    // key is absent
                    return -1;
                } else {
                    while (true) {
                        if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
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

    private int findNewFreeOrRemoved() {
        int free = this.freeValue;
        
        Random random = ThreadLocalRandom.current();
        int newFree;
 {
            do {
                newFree = (int) random.nextInt()
                                        ;
            } while (newFree == free ||
                    
                    index(newFree) >= 0);
        }
        return newFree;
    }


    int changeFree() {
        int mc = modCount();
        int newFree = findNewFreeOrRemoved();
        incrementModCount();
        mc++;
        IntArrays.replaceAllKeys(table, freeValue, newFree);
        this.freeValue = newFree;
        if (mc != modCount())
            throw new ConcurrentModificationException();
        return newFree;
    }


    @Override
    void allocateArrays(int capacity) {
        table = new long[capacity];
        if (freeValue != 0)
            IntArrays.fillKeys(table, freeValue);
    }

    @Override
    public void clear() {
        super.clear();
        IntArrays.fillKeys(table, freeValue);
    }

}

