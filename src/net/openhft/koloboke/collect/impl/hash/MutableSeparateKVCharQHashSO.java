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


public abstract class MutableSeparateKVCharQHashSO extends MutableQHash
        implements SeparateKVCharQHash, PrimitiveConstants, UnsafeConstants {

    char freeValue;
    char removedValue;

    char[] set;

    void copy(SeparateKVCharQHash hash) {
        super.copy(hash);
        freeValue = hash.freeValue();
        if (hash.supportRemoved())
            removedValue = hash.removedValue();

        set = hash.keys().clone();

        if (!hash.supportRemoved()) {
            removedValue = freeValue;
            removedValue = findNewFreeOrRemoved();
        }
    }

    void move(SeparateKVCharQHash hash) {
        super.copy(hash);
        freeValue = hash.freeValue();
        if (hash.supportRemoved())
            removedValue = hash.removedValue();

        set = hash.keys();

        if (!hash.supportRemoved()) {
            removedValue = freeValue;
            removedValue = findNewFreeOrRemoved();
        }
    }

    final void init(HashConfigWrapper configWrapper, int size, char freeValue
            , char removedValue) {
        this.freeValue = freeValue;
        this.removedValue = removedValue;
        // calls allocateArrays, fill keys with this.freeValue => assign it before
        super.init(configWrapper, size);
    }


    @Override
    public char freeValue() {
        return freeValue;
    }

    @Override
    public boolean supportRemoved() {
        return true
                ;
    }

    @Override
    public char removedValue() {
        return removedValue;
    }

    public boolean contains(Object key) {
        return contains(((Character) key).charValue());
    }

    public boolean contains(char key) {
        return index(key) >= 0;
    }

    int index(char key) {
        char free;
        if (key != (free = freeValue) && key != removedValue) {
            char[] keys = set;
            int capacity, index;
            char cur;
            if ((cur = keys[index = SeparateKVCharKeyMixing.mix(key) % (capacity = keys.length)]) == key) {
                // key is present
                return index;
            } else {
                if (cur == free) {
                    // key is absent, free slot
                    return -1;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = keys[bIndex]) == key) {
                            // key is present
                            return bIndex;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return -1;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == key) {
                            // key is present
                            return fIndex;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return -1;
                        }
                        step += 2;
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
                2
                ) {
            throw new HashOverflowException();
        }
        char free = this.freeValue;
        char removed = this.removedValue;
        Random random = ThreadLocalRandom.current();
        char newFree;
        searchForFree:
        if (size > CHAR_CARDINALITY * 3 / 4) {
            int nf = random.nextInt(CHAR_CARDINALITY) * CHAR_PERMUTATION_STEP;
            for (int i = 0; i < CHAR_CARDINALITY; i++) {
                nf = nf + CHAR_PERMUTATION_STEP;
                newFree = (char) nf;
                if (newFree != free &&
                        newFree != removed &&
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
                    newFree == removed ||
                    index(newFree) >= 0);
        }
        return newFree;
    }


    char changeFree() {
        int mc = modCount();
        char newFree = findNewFreeOrRemoved();
        incrementModCount();
        mc++;
        CharArrays.replaceAll(set, freeValue, newFree);
        this.freeValue = newFree;
        if (mc != modCount())
            throw new ConcurrentModificationException();
        return newFree;
    }

    char changeRemoved() {
        int mc = modCount();
        char newRemoved = findNewFreeOrRemoved();
        incrementModCount();
        mc++;
        if (!noRemoved()) {
            CharArrays.replaceAll(set, removedValue, newRemoved);
        }
        this.removedValue = newRemoved;
        if (mc != modCount())
            throw new ConcurrentModificationException();
        return newRemoved;
    }

    @Override
    void allocateArrays(int capacity) {
        set = new char[capacity];
        if (freeValue != 0)
            Arrays.fill(set, freeValue);
    }

    @Override
    public void clear() {
        super.clear();
        Arrays.fill(set, freeValue);
    }

    @Override
    void removeAt(int index) {
        set[index] = removedValue;
    }
}

