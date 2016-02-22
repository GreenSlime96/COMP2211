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


public class MutableQHashObjSetGO<E> extends MutableObjQHashSetSO<E>
        implements HashObjSet<E>, InternalObjCollectionOps<E> {

    @Override
    final void copy(SeparateKVObjQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVObjQHash hash) {
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
            int capacity, index;
            E cur;
            keyAbsentFreeSlot:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(key)) % (capacity = keys.length)]) != FREE) {
                if (cur == key) {
                    // key is present
                    return false;
                } else {
                    int firstRemoved;
                    if (cur != REMOVED) {
                        if (!keyEquals(key, cur)) {
                            if (noRemoved()) {
                                int bIndex = index, fIndex = index, step = 1;
                                while (true) {
                                    if ((bIndex -= step) < 0) bIndex += capacity;
                                    if ((cur = keys[bIndex]) == FREE) {
                                        index = bIndex;
                                        break keyAbsentFreeSlot;
                                    } else if (cur == key || (keyEquals(key, cur))) {
                                        // key is present
                                        return false;
                                    }
                                    int t;
                                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                                    if ((cur = keys[fIndex]) == FREE) {
                                        index = fIndex;
                                        break keyAbsentFreeSlot;
                                    } else if (cur == key || (keyEquals(key, cur))) {
                                        // key is present
                                        return false;
                                    }
                                    step += 2;
                                }
                            } else {
                                firstRemoved = -1;
                            }
                        } else {
                            // key is present
                            return false;
                        }
                    } else {
                        firstRemoved = index;
                    }
                    keyAbsentRemovedSlot: {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == FREE) {
                                if (firstRemoved < 0) {
                                    index = bIndex;
                                    break keyAbsentFreeSlot;
                                } else {
                                    break keyAbsentRemovedSlot;
                                }
                            } else if (cur == key) {
                                // key is present
                                return false;
                            } else if (cur != REMOVED) {
                                if (keyEquals(key, cur)) {
                                    // key is present
                                    return false;
                                }
                            } else if (firstRemoved < 0) {
                                firstRemoved = bIndex;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == FREE) {
                                if (firstRemoved < 0) {
                                    index = fIndex;
                                    break keyAbsentFreeSlot;
                                } else {
                                    break keyAbsentRemovedSlot;
                                }
                            } else if (cur == key) {
                                // key is present
                                return false;
                            } else if (cur != REMOVED) {
                                if (keyEquals(key, cur)) {
                                    // key is present
                                    return false;
                                }
                            } else if (firstRemoved < 0) {
                                firstRemoved = fIndex;
                            }
                            step += 2;
                        }
                    }
                    // key is absent, removed slot
                    incrementModCount();
                    keys[firstRemoved] = key;
                    postRemovedSlotInsertHook();
                    return true;
                }
            }
            // key is absent, free slot
            incrementModCount();
            keys[index] = key;
            postFreeSlotInsertHook();
            return true;
        } else {
            return addNullKey();
        }
    }

    private boolean addNullKey() {
        // noinspection unchecked
        E[] keys = (E[]) set;
        int capacity = keys.length;
        int index;
        E cur;
        keyAbsentFreeSlot:
        if ((cur = keys[index = 0]) != FREE) {
            if (cur == null) {
                // key is present
                return false;
            } else {
                int firstRemoved;
                if (cur != REMOVED) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == FREE) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else if (cur == null) {
                                // key is present
                                return false;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == FREE) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else if (cur == null) {
                                // key is present
                                return false;
                            }
                            step += 2;
                        }
                    } else {
                        firstRemoved = -1;
                    }
                } else {
                    firstRemoved = index;
                }
                keyAbsentRemovedSlot: {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = keys[bIndex]) == FREE) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == null) {
                            // key is present
                            return false;
                        } else if (cur == REMOVED && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == FREE) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == null) {
                            // key is present
                            return false;
                        } else if (cur == REMOVED && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 2;
                    }
                }
                // key is absent, removed slot
                incrementModCount();
                keys[firstRemoved] = null;
                postRemovedSlotInsertHook();
                return true;
            }
        }
        // key is absent, free slot
        incrementModCount();
        keys[index] = null;
        postFreeSlotInsertHook();
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
            int capacity, index;
            E cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(k)) % (capacity = keys.length)]) != k) {
                if (cur == FREE) {
                    // key is absent, free slot
                    return false;
                } else {
                    if (cur != REMOVED) {
                        if (keyEquals(k, cur)) {
                            break keyPresent;
                        } else {
                            if (noRemoved()) {
                                int bIndex = index, fIndex = index, step = 1;
                                while (true) {
                                    if ((bIndex -= step) < 0) bIndex += capacity;
                                    if ((cur = keys[bIndex]) == k) {
                                        index = bIndex;
                                        break keyPresent;
                                    } else if (cur == FREE) {
                                        // key is absent, free slot
                                        return false;
                                    }
                                    else if (keyEquals(k, cur)) {
                                        index = bIndex;
                                        break keyPresent;
                                    }
                                    int t;
                                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                                    if ((cur = keys[fIndex]) == k) {
                                        index = fIndex;
                                        break keyPresent;
                                    } else if (cur == FREE) {
                                        // key is absent, free slot
                                        return false;
                                    }
                                    else if (keyEquals(k, cur)) {
                                        index = fIndex;
                                        break keyPresent;
                                    }
                                    step += 2;
                                }
                            }
                        }
                    }
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = keys[bIndex]) == k) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == FREE) {
                            // key is absent, free slot
                            return false;
                        }
                        else if (cur != REMOVED && keyEquals(k, cur)) {
                            index = bIndex;
                            break keyPresent;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == k) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == FREE) {
                            // key is absent, free slot
                            return false;
                        }
                        else if (cur != REMOVED && keyEquals(k, cur)) {
                            index = fIndex;
                            break keyPresent;
                        }
                        step += 2;
                    }
                }
            }
            // key is present
            incrementModCount();
            ((Object[]) keys)[index] = REMOVED;
            postRemoveHook();
            return true;
        } else {
            return removeNullKey();
        }
    }

    private boolean removeNullKey() {
        // noinspection unchecked
        E[] keys = (E[]) set;
        int capacity = keys.length;
        int index;
        E cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            if (cur == FREE) {
                // key is absent, free slot
                return false;
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == null) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        // key is absent, free slot
                        return false;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == null) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        // key is absent, free slot
                        return false;
                    }
                    step += 2;
                }
            }
        }
        // key is present
        incrementModCount();
        ((Object[]) keys)[index] = REMOVED;
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

