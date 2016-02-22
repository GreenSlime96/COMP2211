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


public abstract class UpdatableParallelKVCharLHashSO extends UpdatableLHash
        implements ParallelKVCharLHash, PrimitiveConstants, UnsafeConstants {

    char freeValue;

    int[] table;

    void copy(ParallelKVCharLHash hash) {
        super.copy(hash);
        freeValue = hash.freeValue();

        table = hash.table().clone();

    }

    void move(ParallelKVCharLHash hash) {
        super.copy(hash);
        freeValue = hash.freeValue();

        table = hash.table();

    }

    final void init(HashConfigWrapper configWrapper, int size, char freeValue
            ) {
        this.freeValue = freeValue;
        // calls allocateArrays, fill keys with this.freeValue => assign it before
        super.init(configWrapper, size);
    }


    @Override
    public char freeValue() {
        return freeValue;
    }

    @Override
    public boolean supportRemoved() {
        return false;
    }

    @Override
    public char removedValue() {
        throw new UnsupportedOperationException();
    }

    public boolean contains(Object key) {
        return contains(((Character) key).charValue());
    }

    public boolean contains(char key) {
        return index(key) >= 0;
    }

    int index(char key) {
        char free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            char cur;
            int entry;
            if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
                // key is present
                return index;
            } else {
                if (cur == free) {
                    // key is absent
                    return -1;
                } else {
                    while (true) {
                        if ((cur = (char) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
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

    private char findNewFreeOrRemoved() {
        int mc = modCount();
        int size = size();
        if (size >= CHAR_CARDINALITY -
                1) {
            throw new HashOverflowException();
        }
        char free = this.freeValue;
        
        Random random = ThreadLocalRandom.current();
        char newFree;
        searchForFree:
        if (size > CHAR_CARDINALITY * 3 / 4) {
            int nf = random.nextInt(CHAR_CARDINALITY) * CHAR_PERMUTATION_STEP;
            for (int i = 0; i < CHAR_CARDINALITY; i++) {
                nf = nf + CHAR_PERMUTATION_STEP;
                newFree = (char) nf;
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
                newFree = (char) random.nextInt()
                                        ;
            } while (newFree == free ||
                    
                    index(newFree) >= 0);
        }
        return newFree;
    }


    char changeFree() {
        int mc = modCount();
        char newFree = findNewFreeOrRemoved();
        incrementModCount();
        mc++;
        CharArrays.replaceAllKeys(table, freeValue, newFree);
        this.freeValue = newFree;
        if (mc != modCount())
            throw new ConcurrentModificationException();
        return newFree;
    }


    @Override
    void allocateArrays(int capacity) {
        table = new int[capacity];
        if (freeValue != 0)
            CharArrays.fillKeys(table, freeValue);
    }

    @Override
    public void clear() {
        super.clear();
        CharArrays.fillKeys(table, freeValue);
    }

}

