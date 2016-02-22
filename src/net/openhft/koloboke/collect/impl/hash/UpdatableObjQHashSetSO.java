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


public abstract class UpdatableObjQHashSetSO<E>
        extends UpdatableSeparateKVObjQHashGO<E> {

    public Equivalence<E> equivalence() {
        return Equivalence.defaultEquality();
    }


    @Override
    void rehash(int newCapacity) {
        int mc = modCount();
        Object[] keys = set;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        Object[] newKeys = set;
        int capacity = newKeys.length;
        for (int i = keys.length - 1; i >= 0; i--) {
            E key;
            // noinspection unchecked
            if ((key = (E) keys[i]) != FREE) {
                int index;
                if (newKeys[index = SeparateKVObjKeyMixing.mix(nullableKeyHashCode(key)) % capacity] != FREE) {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if (newKeys[bIndex] == FREE) {
                            index = bIndex;
                            break;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if (newKeys[fIndex] == FREE) {
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

