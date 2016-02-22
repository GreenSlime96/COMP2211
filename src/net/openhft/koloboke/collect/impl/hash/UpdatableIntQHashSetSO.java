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


public abstract class UpdatableIntQHashSetSO
        extends UpdatableSeparateKVIntQHashGO {



    @Override
    void rehash(int newCapacity) {
        int mc = modCount();
        int free = freeValue;
        int[] keys = set;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        int[] newKeys = set;
        int capacity = newKeys.length;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) != free) {
                int index;
                if (newKeys[index = SeparateKVIntKeyMixing.mix(key) % capacity] != free) {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if (newKeys[bIndex] == free) {
                            index = bIndex;
                            break;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if (newKeys[fIndex] == free) {
                            index = fIndex;
                            break;
                        }
                        step += 2;
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

