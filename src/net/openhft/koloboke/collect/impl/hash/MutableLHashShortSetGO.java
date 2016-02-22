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
import net.openhft.koloboke.collect.set.hash.HashShortSet;
import javax.annotation.Nonnull;

import java.util.*;


public class MutableLHashShortSetGO extends MutableShortLHashSetSO
        implements HashShortSet, InternalShortCollectionOps {

    @Override
    final void copy(SeparateKVShortLHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVShortLHash hash) {
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
        return CommonShortCollectionOps.containsAll(this, c);
    }

    @Nonnull
    @Override
    public ShortCursor cursor() {
        return setCursor();
    }


    @Override
    public boolean add(Short e) {
        return add(e.shortValue());
    }

    @Override
    public boolean add(short key) {
        short free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        short[] keys = set;
        int capacityMask, index;
        short cur;
        keyAbsent:
        if ((cur = keys[index = SeparateKVShortKeyMixing.mix(key) & (capacityMask = keys.length - 1)]) != free) {
            if (cur == key) {
                // key is present
                return false;
            } else {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == free) {
                        break keyAbsent;
                    } else if (cur == key) {
                        // key is present
                        return false;
                    }
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
    public boolean addAll(@Nonnull Collection<? extends Short> c) {
        return CommonShortCollectionOps.addAll(this, c);
    }

    @Override
    public boolean remove(Object key) {
        return removeShort(((Short) key).shortValue());
    }


    @Override
    boolean justRemove(short key) {
        return removeShort(key);
    }

    @Override
    public boolean removeShort(short key) {
        short free;
        if (key != (free = freeValue)) {
            short[] keys = set;
            int capacityMask = keys.length - 1;
            int index;
            short cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVShortKeyMixing.mix(key) & capacityMask]) != key) {
                if (cur == free) {
                    // key is absent
                    return false;
                } else {
                    while (true) {
                        if ((cur = keys[(index = (index - 1) & capacityMask)]) == key) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return false;
                        }
                    }
                }
            }
            // key is present
            incrementModCount();
            int indexToRemove = index;
            int indexToShift = indexToRemove;
            int shiftDistance = 1;
            while (true) {
                indexToShift = (indexToShift - 1) & capacityMask;
                short keyToShift;
                if ((keyToShift = keys[indexToShift]) == free) {
                    break;
                }
                if (((SeparateKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
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
            keys[indexToRemove] = free;
            postRemoveHook();
            return true;
        } else {
            // key is absent
            return false;
        }
    }



    @Override
    public boolean removeAll(@Nonnull Collection<?> c) {
        if (c instanceof ShortCollection) {
            if (c instanceof InternalShortCollectionOps) {
                InternalShortCollectionOps c2 = (InternalShortCollectionOps) c;
                if (c2.size() < this.size()) {
                    
                    return c2.reverseRemoveAllFrom(this);
                }
            }
            return removeAll(this, (ShortCollection) c);
        }
        return removeAll(this, c);
    }

    @Override
    public boolean retainAll(@Nonnull Collection<?> c) {
        return retainAll(this, c);
    }
}

