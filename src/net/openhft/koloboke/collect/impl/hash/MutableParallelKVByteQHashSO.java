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


public abstract class MutableParallelKVByteQHashSO extends MutableQHash
        implements ParallelKVByteQHash, PrimitiveConstants, UnsafeConstants {

    byte freeValue;
    byte removedValue;

    char[] table;

    void copy(ParallelKVByteQHash hash) {
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

    void move(ParallelKVByteQHash hash) {
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

    final void init(HashConfigWrapper configWrapper, int size, byte freeValue
            , byte removedValue) {
        this.freeValue = freeValue;
        this.removedValue = removedValue;
        // calls allocateArrays, fill keys with this.freeValue => assign it before
        super.init(configWrapper, size);
    }


    @Override
    public byte freeValue() {
        return freeValue;
    }

    @Override
    public boolean supportRemoved() {
        return true
                ;
    }

    @Override
    public byte removedValue() {
        return removedValue;
    }

    public boolean contains(Object key) {
        return contains(((Byte) key).byteValue());
    }

    public boolean contains(byte key) {
        return index(key) >= 0;
    }

    int index(byte key) {
        byte free;
        if (key != (free = freeValue) && key != removedValue) {
            char[] tab = table;
            int capacity, index;
            byte cur;
            int entry;
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) % (capacity = tab.length)])) == key) {
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
                        if ((cur = (byte) (entry = tab[bIndex])) == key) {
                            // key is present
                            return bIndex;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return -1;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (byte) (entry = tab[fIndex])) == key) {
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

    private byte findNewFreeOrRemoved() {
        int mc = modCount();
        int size = size();
        if (size >= BYTE_CARDINALITY -
                2
                ) {
            throw new HashOverflowException();
        }
        byte free = this.freeValue;
        byte removed = this.removedValue;
        Random random = ThreadLocalRandom.current();
        byte newFree;
        searchForFree:
        if (size > BYTE_CARDINALITY * 3 / 4) {
            int nf = random.nextInt(BYTE_CARDINALITY) * BYTE_PERMUTATION_STEP;
            for (int i = 0; i < BYTE_CARDINALITY; i++) {
                nf = nf + BYTE_PERMUTATION_STEP;
                newFree = (byte) nf;
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
                newFree = (byte) random.nextInt()
                                        ;
            } while (newFree == free ||
                    newFree == removed ||
                    index(newFree) >= 0);
        }
        return newFree;
    }


    byte changeFree() {
        int mc = modCount();
        byte newFree = findNewFreeOrRemoved();
        incrementModCount();
        mc++;
        ByteArrays.replaceAllKeys(table, freeValue, newFree);
        this.freeValue = newFree;
        if (mc != modCount())
            throw new ConcurrentModificationException();
        return newFree;
    }

    byte changeRemoved() {
        int mc = modCount();
        byte newRemoved = findNewFreeOrRemoved();
        incrementModCount();
        mc++;
        if (!noRemoved()) {
            ByteArrays.replaceAllKeys(table, removedValue, newRemoved);
        }
        this.removedValue = newRemoved;
        if (mc != modCount())
            throw new ConcurrentModificationException();
        return newRemoved;
    }

    @Override
    void allocateArrays(int capacity) {
        table = new char[capacity];
        if (freeValue != 0)
            ByteArrays.fillKeys(table, freeValue);
    }

    @Override
    public void clear() {
        super.clear();
        ByteArrays.fillKeys(table, freeValue);
    }

    @Override
    void removeAt(int index) {
        U.putByte(table, CHAR_BASE + BYTE_KEY_OFFSET + (((long) index) << CHAR_SCALE_SHIFT),
                removedValue);
    }
}

