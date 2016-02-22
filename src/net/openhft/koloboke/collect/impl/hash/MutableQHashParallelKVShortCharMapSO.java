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
import net.openhft.koloboke.collect.map.hash.HashShortCharMap;
import javax.annotation.Nonnull;


public abstract class MutableQHashParallelKVShortCharMapSO
        extends MutableQHashParallelKVShortKeyMap
        implements HashShortCharMap, InternalShortCharMapOps, ParallelKVShortCharQHash {


    
    int valueIndex(char value) {
        if (isEmpty())
            return -1;
        int index = -1;
        int mc = modCount();
        short free = freeValue;
        short removed = removedValue;
        int[] tab = table;
        int entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    if (value == (char) (entry >>> 16)) {
                        index = i;
                        break;
                    }
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free && key != removed) {
                    if (value == (char) (entry >>> 16)) {
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
    boolean containsValue(char value) {
        return valueIndex(value) >= 0;
    }

    boolean removeValue(char value) {
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
        return containsValue(((Character) value).charValue());
    }

    int insert(short key, char value) {
        short free;
        short removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        int[] tab = table;
        int capacity, index;
        short cur;
        int entry;
        keyAbsentFreeSlot:
        if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(key) % (capacity = tab.length)])) != free) {
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
                            if ((cur = (short) (entry = tab[bIndex])) == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else if (cur == key) {
                                // key is present
                                return bIndex;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (short) (entry = tab[fIndex])) == free) {
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
                        if ((cur = (short) (entry = tab[bIndex])) == free) {
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
                        if ((cur = (short) (entry = tab[fIndex])) == free) {
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
                tab[firstRemoved] = ((((int) key) & SHORT_MASK) | (((int) value) << 16));
                postRemovedSlotInsertHook();
                return -1;
            }
        }
        // key is absent, free slot
        incrementModCount();
        tab[index] = ((((int) key) & SHORT_MASK) | (((int) value) << 16));
        postFreeSlotInsertHook();
        return -1;
    }


}

