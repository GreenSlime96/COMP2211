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
import net.openhft.koloboke.collect.map.hash.HashIntFloatMap;
import javax.annotation.Nonnull;


public abstract class MutableQHashParallelKVIntFloatMapSO
        extends MutableQHashParallelKVIntKeyMap
        implements HashIntFloatMap, InternalIntFloatMapOps, ParallelKVIntFloatQHash {


    
    int valueIndex(int value) {
        if (isEmpty())
            return -1;
        int index = -1;
        int mc = modCount();
        int free = freeValue;
        int removed = removedValue;
        long[] tab = table;
        long entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
                    if (value == (int) (entry >>> 32)) {
                        index = i;
                        break;
                    }
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free && key != removed) {
                    if (value == (int) (entry >>> 32)) {
                        index = i;
                        break;
                    }
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return index;
    }

    
    boolean containsValue(int value) {
        return valueIndex(value) >= 0;
    }

    boolean removeValue(int value) {
        int index = valueIndex(value);
        if (index >= 0) {
            removeAt(index);
            return true;
        } else {
            return false;
        }
    }
    
    int valueIndex(float value) {
        if (isEmpty())
            return -1;
        int val = Float.floatToIntBits(value);
        int index = -1;
        int mc = modCount();
        int free = freeValue;
        int removed = removedValue;
        long[] tab = table;
        long entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
                    if (val == (int) (entry >>> 32)) {
                        index = i;
                        break;
                    }
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free && key != removed) {
                    if (val == (int) (entry >>> 32)) {
                        index = i;
                        break;
                    }
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return index;
    }

    @Override public
    boolean containsValue(float value) {
        return valueIndex(value) >= 0;
    }

    boolean removeValue(float value) {
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
        return containsValue(((Float) value).floatValue());
    }

    int insert(int key, int value) {
        int free;
        int removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        keyAbsentFreeSlot:
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) % (capacity = tab.length)])) != free) {
            if (cur == key) {
                // key is present
                return index;
            } else {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (int) (entry = tab[bIndex])) == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else if (cur == key) {
                                // key is present
                                return bIndex;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (int) (entry = tab[fIndex])) == free) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else if (cur == key) {
                                // key is present
                                return fIndex;
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
                        if ((cur = (int) (entry = tab[bIndex])) == free) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == key) {
                            // key is present
                            return bIndex;
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == free) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == key) {
                            // key is present
                            return fIndex;
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 2;
                    }
                }
                // key is absent, removed slot
                incrementModCount();
                tab[firstRemoved] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                postRemovedSlotInsertHook();
                return -1;
            }
        }
        // key is absent, free slot
        incrementModCount();
        tab[index] = ((((long) key) & INT_MASK) | (((long) value) << 32));
        postFreeSlotInsertHook();
        return -1;
    }


}

