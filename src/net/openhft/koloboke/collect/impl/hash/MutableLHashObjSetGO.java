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
import net.openhft.koloboke.collect.set.hash.HashObjSet;
import javax.annotation.Nonnull;

import java.util.*;


public class MutableLHashObjSetGO<E> extends MutableObjLHashSetSO<E>
        implements HashObjSet<E>, InternalObjCollectionOps<E> {

    @Override
    final void copy(SeparateKVObjLHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVObjLHash hash) {
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
        return CommonObjCollectionOps.containsAll(this, c);
    }

    @Nonnull
    @Override
    public ObjCursor<E> cursor() {
        return setCursor();
    }



    @Override
    public boolean add(E key) {
        if (key != null) {
            // noinspection unchecked
            E[] keys = (E[]) set;
            int capacityMask, index;
            E cur;
            keyAbsent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(key)) & (capacityMask = keys.length - 1)]) != FREE) {
                if (cur == key || keyEquals(key, cur)) {
                    // key is present
                    return false;
                } else {
                    while (true) {
                        if ((cur = keys[(index = (index - 1) & capacityMask)]) == FREE) {
                            break keyAbsent;
                        } else if (cur == key || (keyEquals(key, cur))) {
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
        } else {
            return addNullKey();
        }
    }

    private boolean addNullKey() {
        // noinspection unchecked
        E[] keys = (E[]) set;
        int capacityMask, index;
        E cur;
        keyAbsent:
        if ((cur = keys[index = 0]) != FREE) {
            if (cur == null) {
                // key is present
                return false;
            } else {
                capacityMask = keys.length - 1;
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == FREE) {
                        break keyAbsent;
                    } else if (cur == null) {
                        // key is present
                        return false;
                    }
                }
            }
        }
        // key is absent
        incrementModCount();
        keys[index] = null;
        postInsertHook();
        return true;
    }

    @Override
    public boolean addAll(@Nonnull Collection<? extends E> c) {
        return CommonObjCollectionOps.addAll(this, c);
    }

    @Override
    public boolean remove(Object key) {
        if (key != null) {
            // noinspection unchecked
            E k = (E) key;
            // noinspection unchecked
            E[] keys = (E[]) set;
            int capacityMask = keys.length - 1;
            int index;
            E cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(k)) & capacityMask]) != k) {
                if (cur == FREE) {
                    // key is absent
                    return false;
                } else {
                    if (keyEquals(k, cur)) {
                        break keyPresent;
                    } else {
                        while (true) {
                            if ((cur = keys[(index = (index - 1) & capacityMask)]) == k) {
                                break keyPresent;
                            } else if (cur == FREE) {
                                // key is absent
                                return false;
                            }
                            else if (keyEquals(k, cur)) {
                                break keyPresent;
                            }
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
                E keyToShift;
                if ((keyToShift = keys[indexToShift]) == FREE) {
                    break;
                }
                if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                    keys[indexToRemove] = keyToShift;
                    indexToRemove = indexToShift;
                    shiftDistance = 1;
                } else {
                    shiftDistance++;
                }
            }
            ((Object[]) keys)[indexToRemove] = FREE;
            postRemoveHook();
            return true;
        } else {
            return removeNullKey();
        }
    }

    private boolean removeNullKey() {
        // noinspection unchecked
        E[] keys = (E[]) set;
        int capacityMask = keys.length - 1;
        int index;
        E cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            if (cur == FREE) {
                // key is absent
                return false;
            } else {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == null) {
                        break keyPresent;
                    } else if (cur == FREE) {
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
            E keyToShift;
            if ((keyToShift = keys[indexToShift]) == FREE) {
                break;
            }
            if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                keys[indexToRemove] = keyToShift;
                indexToRemove = indexToShift;
                shiftDistance = 1;
            } else {
                shiftDistance++;
            }
        }
        ((Object[]) keys)[indexToRemove] = FREE;
        postRemoveHook();
        return true;
    }

    @Override
    boolean justRemove(E key) {
        return remove(key);
    }



    @Override
    public boolean removeAll(@Nonnull Collection<?> c) {
            if (c instanceof InternalObjCollectionOps) {
                InternalObjCollectionOps c2 = (InternalObjCollectionOps) c;
                if (c2.size() < this.size()
                            && equivalence().equals(c2.equivalence())
                            ) {
                    // noinspection unchecked
                    return c2.reverseRemoveAllFrom(this);
                }
            }
        return removeAll(this, c);
    }

    @Override
    public boolean retainAll(@Nonnull Collection<?> c) {
        return retainAll(this, c);
    }
}

