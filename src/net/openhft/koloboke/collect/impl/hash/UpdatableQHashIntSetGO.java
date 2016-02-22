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

import net.openhft.koloboke.collect.*;
import net.openhft.koloboke.collect.impl.*;
import net.openhft.koloboke.collect.set.hash.HashIntSet;
import javax.annotation.Nonnull;

import java.util.*;


public class UpdatableQHashIntSetGO extends UpdatableIntQHashSetSO
        implements HashIntSet, InternalIntCollectionOps {

    @Override
    final void copy(SeparateKVIntQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVIntQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.move(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    public int hashCode() {
        return setHashCode();
    }

    @Override
    public String toString() {
        return setToString();
    }

    @Override
    public boolean equals(Object obj) {
        return CommonSetOps.equals(this, obj);
    }

    @Override
    public boolean containsAll(@Nonnull Collection<?> c) {
        return CommonIntCollectionOps.containsAll(this, c);
    }

    @Nonnull
    @Override
    public IntCursor cursor() {
        return setCursor();
    }


    @Override
    public boolean add(Integer e) {
        return add(e.intValue());
    }

    @Override
    public boolean add(int key) {
        int free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        int[] keys = set;
        int capacity, index;
        int cur;
        keyAbsent:
        if ((cur = keys[index = SeparateKVIntKeyMixing.mix(key) % (capacity = keys.length)]) != free) {
            if (cur == key) {
                // key is present
                return false;
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == free) {
                        index = bIndex;
                        break keyAbsent;
                    } else if (cur == key) {
                        // key is present
                        return false;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == free) {
                        index = fIndex;
                        break keyAbsent;
                    } else if (cur == key) {
                        // key is present
                        return false;
                    }
                    step += 2;
                }
            }
        }
        // key is absent
        incrementModCount();
        keys[index] = key;
        postInsertHook();
        return true;
    }


    @Override
    public boolean addAll(@Nonnull Collection<? extends Integer> c) {
        return CommonIntCollectionOps.addAll(this, c);
    }

    @Override
    public boolean remove(Object key) {
        return removeInt(((Integer) key).intValue());
    }


    @Override
    boolean justRemove(int key) {
        return removeInt(key);
    }

    @Override
    public boolean removeInt(int key) {
        throw new java.lang.UnsupportedOperationException();
    }



    @Override
    public boolean removeAll(@Nonnull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(@Nonnull Collection<?> c) {
        return retainAll(this, c);
    }
}

