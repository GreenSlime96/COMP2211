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
import net.openhft.koloboke.collect.map.hash.HashShortByteMap;
import javax.annotation.Nonnull;


public abstract class MutableQHashSeparateKVShortByteMapSO
        extends MutableQHashSeparateKVShortKeyMap
        implements HashShortByteMap, InternalShortByteMapOps, SeparateKVShortByteQHash {

    byte[] values;

    void copy(SeparateKVShortByteQHash hash) {
        super.copy(hash);
        values = hash.valueArray().clone();
    }

    void move(SeparateKVShortByteQHash hash) {
        super.move(hash);
        values = hash.valueArray();
    }

    @Override
    @Nonnull
    public byte[] valueArray() {
        return values;
    }

    
    int valueIndex(byte value) {
        if (isEmpty())
            return -1;
        int index = -1;
        int mc = modCount();
        short free = freeValue;
        short removed = removedValue;
        short[] keys = set;
        byte[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    if (value == vals[i]) {
                        index = i;
                        break;
                    }
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free && key != removed) {
                    if (value == vals[i]) {
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
    boolean containsValue(byte value) {
        return valueIndex(value) >= 0;
    }

    boolean removeValue(byte value) {
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
        return containsValue(((Byte) value).byteValue());
    }

    int insert(short key, byte value) {
        short free;
        short removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        short[] keys = set;
        int capacity, index;
        short cur;
        keyAbsentFreeSlot:
        if ((cur = keys[index = SeparateKVShortKeyMixing.mix(key) % (capacity = keys.length)]) != free) {
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
                            if ((cur = keys[bIndex]) == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else if (cur == key) {
                                // key is present
                                return bIndex;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == free) {
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
                        if ((cur = keys[bIndex]) == free) {
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
                        if ((cur = keys[fIndex]) == free) {
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
                keys[firstRemoved] = key;
                values[firstRemoved] = value;
                postRemovedSlotInsertHook();
                return -1;
            }
        }
        // key is absent, free slot
        incrementModCount();
        keys[index] = key;
        values[index] = value;
        postFreeSlotInsertHook();
        return -1;
    }


    @Override
    void allocateArrays(int capacity) {
        super.allocateArrays(capacity);
        values = new byte[capacity];
    }
}
