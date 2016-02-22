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
import net.openhft.koloboke.collect.set.hash.HashFloatSet;
import javax.annotation.Nonnull;

import java.util.*;


public class MutableQHashFloatSetGO extends MutableFloatQHashSetSO
        implements HashFloatSet, InternalFloatCollectionOps {

    @Override
    final void copy(SeparateKVFloatQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVFloatQHash hash) {
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
        return CommonFloatCollectionOps.containsAll(this, c);
    }

    @Nonnull
    @Override
    public FloatCursor cursor() {
        return setCursor();
    }


    @Override
    public boolean add(Float e) {
        return add(e.floatValue());
    }

    @Override
    public boolean add(int key) {
        int[] keys = set;
        int capacity, index;
        int cur;
        keyAbsentFreeSlot:
        if ((cur = keys[index = SeparateKVFloatKeyMixing.mix(key) % (capacity = keys.length)]) != FREE_BITS) {
            if (cur == key) {
                // key is present
                return false;
            } else {
                int firstRemoved;
                if (cur <= FREE_BITS) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == FREE_BITS) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else if (cur == key) {
                                // key is present
                                return false;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == FREE_BITS) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else if (cur == key) {
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
                        if ((cur = keys[bIndex]) == FREE_BITS) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == key) {
                            // key is present
                            return false;
                        } else if (cur > FREE_BITS && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == FREE_BITS) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == key) {
                            // key is present
                            return false;
                        } else if (cur > FREE_BITS && firstRemoved < 0) {
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
    }

    @Override
    public boolean add(float key) {
        return add(Float.floatToIntBits(key));
    }

    @Override
    public boolean addAll(@Nonnull Collection<? extends Float> c) {
        return CommonFloatCollectionOps.addAll(this, c);
    }

    @Override
    public boolean remove(Object key) {
        return removeFloat(((Float) key).floatValue());
    }


    @Override
    boolean justRemove(int key) {
        return removeFloat(key);
    }

    @Override
    public boolean removeFloat(int key) {
        int[] keys = set;
        int capacity, index;
        int cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVFloatKeyMixing.mix(key) % (capacity = keys.length)]) != key) {
            if (cur == FREE_BITS) {
                // key is absent, free slot
                return false;
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == key) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent, free slot
                        return false;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == key) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent, free slot
                        return false;
                    }
                    step += 2;
                }
            }
        }
        // key is present
        incrementModCount();
        keys[index] = REMOVED_BITS;
        postRemoveHook();
        return true;
    }

    @Override
    public boolean removeFloat(float key) {
        return removeFloat(Float.floatToIntBits(key));
    }


    @Override
    public boolean removeAll(@Nonnull Collection<?> c) {
        if (c instanceof FloatCollection) {
            if (c instanceof InternalFloatCollectionOps) {
                InternalFloatCollectionOps c2 = (InternalFloatCollectionOps) c;
                if (c2.size() < this.size()) {
                    
                    return c2.reverseRemoveAllFrom(this);
                }
            }
            return removeAll(this, (FloatCollection) c);
        }
        return removeAll(this, c);
    }

    @Override
    public boolean retainAll(@Nonnull Collection<?> c) {
        return retainAll(this, c);
    }
}

