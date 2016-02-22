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


public abstract class MutableParallelKVShortLHashSO extends MutableLHash
        implements ParallelKVShortLHash, PrimitiveConstants, UnsafeConstants {

    short freeValue;

    int[] table;

    void copy(ParallelKVShortLHash hash) {
        super.copy(hash);
        freeValue = hash.freeValue();

        table = hash.table().clone();

    }

    void move(ParallelKVShortLHash hash) {
        super.copy(hash);
        freeValue = hash.freeValue();

        table = hash.table();

    }

    final void init(HashConfigWrapper configWrapper, int size, short freeValue
            ) {
        this.freeValue = freeValue;
        // calls allocateArrays, fill keys with this.freeValue => assign it before
        super.init(configWrapper, size);
    }


    @Override
    public short freeValue() {
        return freeValue;
    }

    @Override
    public boolean supportRemoved() {
        return false;
    }

    @Override
    public short removedValue() {
        throw new UnsupportedOperationException();
    }

    public boolean contains(Object key) {
        return contains(((Short) key).shortValue());
    }

    public boolean contains(short key) {
        return index(key) >= 0;
    }

    int index(short key) {
        short free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            short cur;
            int entry;
            if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
                // key is present
                return index;
            } else {
                if (cur == free) {
                    // key is absent
                    return -1;
                } else {
                    while (true) {
                        if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
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

    private short findNewFreeOrRemoved() {
        int mc = modCount();
        int size = size();
        if (size >= SHORT_CARDINALITY -
                1) {
            throw new HashOverflowException();
        }
        short free = this.freeValue;
        
        Random random = ThreadLocalRandom.current();
        short newFree;
        searchForFree:
        if (size > SHORT_CARDINALITY * 3 / 4) {
            int nf = random.nextInt(SHORT_CARDINALITY) * SHORT_PERMUTATION_STEP;
            for (int i = 0; i < SHORT_CARDINALITY; i++) {
                nf = nf + SHORT_PERMUTATION_STEP;
                newFree = (short) nf;
                if (newFree != free &&
                        
                        index(newFree) < 0) {
                    break searchForFree;
                }
            }
            if (mc != modCount())
                throw new ConcurrentModificationException();
            throw new AssertionError("Impossible state");
        }
        else  {
            do {
                newFree = (short) random.nextInt()
                                        ;
            } while (newFree == free ||
                    
                    index(newFree) >= 0);
        }
        return newFree;
    }


    short changeFree() {
        int mc = modCount();
        short newFree = findNewFreeOrRemoved();
        incrementModCount();
        mc++;
        ShortArrays.replaceAllKeys(table, freeValue, newFree);
        this.freeValue = newFree;
        if (mc != modCount())
            throw new ConcurrentModificationException();
        return newFree;
    }


    @Override
    void allocateArrays(int capacity) {
        table = new int[capacity];
        if (freeValue != 0)
            ShortArrays.fillKeys(table, freeValue);
    }

    @Override
    public void clear() {
        super.clear();
        ShortArrays.fillKeys(table, freeValue);
    }

}

