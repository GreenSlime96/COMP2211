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
import net.openhft.koloboke.collect.set.hash.HashLongSet;
import javax.annotation.Nonnull;

import java.util.*;


public class MutableQHashLongSetGO extends MutableLongQHashSetSO
        implements HashLongSet, InternalLongCollectionOps {

    @Override
    final void copy(SeparateKVLongQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVLongQHash hash) {
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
        return CommonLongCollectionOps.containsAll(this, c);
    }

    @Nonnull
    @Override
    public LongCursor cursor() {
        return setCursor();
    }


    @Override
    public boolean add(Long e) {
        return add(e.longValue());
    }

    @Override
    public boolean add(long key) {
        long free;
        long removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        long[] keys = set;
        int capacity, index;
        long cur;
        keyAbsentFreeSlot:
        if ((cur = keys[index = SeparateKVLongKeyMixing.mix(key) % (capacity = keys.length)]) != free) {
            if (cur == key) {
                // key is present
                return false;
            } else {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else if (cur == key) {
                                // key is present
                                return false;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == free) {
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
                        if ((cur = keys[bIndex]) == free) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == key) {
                            // key is present
                            return false;
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == free) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == key) {
                            // key is present
                            return false;
                        } else if (cur == removed && firstRemoved < 0) {
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
    public boolean addAll(@Nonnull Collection<? extends Long> c) {
        return CommonLongCollectionOps.addAll(this, c);
    }

    @Override
    public boolean remove(Object key) {
        return removeLong(((Long) key).longValue());
    }


    @Override
    boolean justRemove(long key) {
        return removeLong(key);
    }

    @Override
    public boolean removeLong(long key) {
        long free, removed;
        if (key != (free = freeValue) && key != (removed = removedValue)) {
            long[] keys = set;
            int capacity, index;
            long cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVLongKeyMixing.mix(key) % (capacity = keys.length)]) != key) {
                if (cur == free) {
                    // key is absent, free slot
                    return false;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = keys[bIndex]) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return false;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == key) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return false;
                        }
                        step += 2;
                    }
                }
            }
            // key is present
            incrementModCount();
            keys[index] = removed;
            postRemoveHook();
            return true;
        } else {
            // key is absent
            return false;
        }
    }



    @Override
    public boolean removeAll(@Nonnull Collection<?> c) {
        if (c instanceof LongCollection) {
            if (c instanceof InternalLongCollectionOps) {
                InternalLongCollectionOps c2 = (InternalLongCollectionOps) c;
                if (c2.size() < this.size()) {
                    
                    return c2.reverseRemoveAllFrom(this);
                }
            }
            return removeAll(this, (LongCollection) c);
        }
        return removeAll(this, c);
    }

    @Override
    public boolean retainAll(@Nonnull Collection<?> c) {
        return retainAll(this, c);
    }
}

