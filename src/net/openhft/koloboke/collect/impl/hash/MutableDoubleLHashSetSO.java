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

import net.openhft.koloboke.collect.Equivalence;

import java.util.ConcurrentModificationException;


public abstract class MutableDoubleLHashSetSO
        extends MutableSeparateKVDoubleLHashGO {


    @Override
    void removeAt(int index) {
        long[] keys = set;
        int capacityMask = keys.length - 1;
        incrementModCount();
        int indexToRemove = index;
        int indexToShift = indexToRemove;
        int shiftDistance = 1;
        while (true) {
            indexToShift = (indexToShift - 1) & capacityMask;
            long keyToShift;
            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                break;
            }
            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                keys[indexToRemove] = keyToShift;
                indexToRemove = indexToShift;
                shiftDistance = 1;
            } else {
                shiftDistance++;
                if (indexToShift == 1 + index) {
                    throw new java.util.ConcurrentModificationException();
                }
            }
        }
        keys[indexToRemove] = FREE_BITS;
        postRemoveHook();
    }

    @Override
    void rehash(int newCapacity) {
        int mc = modCount();
        long[] keys = set;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        long[] newKeys = set;
        int capacityMask = newKeys.length - 1;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                int index;
                if (newKeys[index = SeparateKVDoubleKeyMixing.mix(key) & capacityMask] != FREE_BITS) {
                    while (true) {
                        if (newKeys[(index = (index - 1) & capacityMask)] == FREE_BITS) {
                            break;
                        }
                    }
                }
                newKeys[index] = key;
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void clear() {
        int mc = modCount() + 1;
        super.clear();
        if (mc != modCount())
            throw new ConcurrentModificationException();
    }
}

