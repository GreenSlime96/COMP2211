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

import net.openhft.koloboke.collect.impl.*;
import net.openhft.koloboke.collect.map.hash.HashDoubleLongMap;
import javax.annotation.Nonnull;


public abstract class MutableQHashParallelKVDoubleLongMapSO
        extends MutableQHashParallelKVDoubleKeyMap
        implements HashDoubleLongMap, InternalDoubleLongMapOps, ParallelKVDoubleLongQHash {


    
    int valueIndex(long value) {
        if (isEmpty())
            return -1;
        int index = -1;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            if (tab[i] < FREE_BITS) {
                if (value == tab[i + 1]) {
                    index = i;
                    break;
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return index;
    }

    @Override public
    boolean containsValue(long value) {
        return valueIndex(value) >= 0;
    }

    boolean removeValue(long value) {
        int index = valueIndex(value);
        if (index >= 0) {
            removeAt(index);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(((Long) value).longValue());
    }

    int insert(long key, long value) {
        long[] tab = table;
        int capacity, index;
        long cur;
        keyAbsentFreeSlot:
        if ((cur = tab[index = ParallelKVDoubleKeyMixing.mix(key) % (capacity = tab.length)]) != FREE_BITS) {
            if (cur == key) {
                // key is present
                return index;
            } else {
                int firstRemoved;
                if (cur <= FREE_BITS) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 2;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = tab[bIndex]) == FREE_BITS) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else if (cur == key) {
                                // key is present
                                return bIndex;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = tab[fIndex]) == FREE_BITS) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else if (cur == key) {
                                // key is present
                                return fIndex;
                            }
                            step += 4;
                        }
                    } else {
                        firstRemoved = -1;
                    }
                } else {
                    firstRemoved = index;
                }
                keyAbsentRemovedSlot: {
                    int bIndex = index, fIndex = index, step = 2;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = tab[bIndex]) == FREE_BITS) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == key) {
                            // key is present
                            return bIndex;
                        } else if (cur > FREE_BITS && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = tab[fIndex]) == FREE_BITS) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == key) {
                            // key is present
                            return fIndex;
                        } else if (cur > FREE_BITS && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 4;
                    }
                }
                // key is absent, removed slot
                incrementModCount();
                tab[firstRemoved] = key;
                tab[firstRemoved + 1] = value;
                postRemovedSlotInsertHook();
                return -1;
            }
        }
        // key is absent, free slot
        incrementModCount();
        tab[index] = key;
        tab[index + 1] = value;
        postFreeSlotInsertHook();
        return -1;
    }


}

