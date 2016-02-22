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
import net.openhft.koloboke.collect.map.hash.HashObjShortMap;
import javax.annotation.Nonnull;


public abstract class MutableQHashSeparateKVObjShortMapSO<K>
        extends MutableQHashSeparateKVObjKeyMap<K>
        implements HashObjShortMap<K>, InternalObjShortMapOps<K>, SeparateKVObjShortQHash {

    short[] values;

    void copy(SeparateKVObjShortQHash hash) {
        super.copy(hash);
        values = hash.valueArray().clone();
    }

    void move(SeparateKVObjShortQHash hash) {
        super.move(hash);
        values = hash.valueArray();
    }

    @Override
    @Nonnull
    public short[] valueArray() {
        return values;
    }

    
    int valueIndex(short value) {
        if (isEmpty())
            return -1;
        int index = -1;
        int mc = modCount();
        Object[] keys = set;
        short[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    if (value == vals[i]) {
                        index = i;
                        break;
                    }
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE && key != REMOVED) {
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
    boolean containsValue(short value) {
        return valueIndex(value) >= 0;
    }

    boolean removeValue(short value) {
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
        return containsValue(((Short) value).shortValue());
    }

    int insert(K key, short value) {
        if (key != null) {
            // noinspection unchecked
            K[] keys = (K[]) set;
            int capacity, index;
            K cur;
            keyAbsentFreeSlot:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(key)) % (capacity = keys.length)]) != FREE) {
                if (cur == key) {
                    // key is present
                    return index;
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
                                        return bIndex;
                                    }
                                    int t;
                                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                                    if ((cur = keys[fIndex]) == FREE) {
                                        index = fIndex;
                                        break keyAbsentFreeSlot;
                                    } else if (cur == key || (keyEquals(key, cur))) {
                                        // key is present
                                        return fIndex;
                                    }
                                    step += 2;
                                }
                            } else {
                                firstRemoved = -1;
                            }
                        } else {
                            // key is present
                            return index;
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
                                return bIndex;
                            } else if (cur != REMOVED) {
                                if (keyEquals(key, cur)) {
                                    // key is present
                                    return bIndex;
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
                                return fIndex;
                            } else if (cur != REMOVED) {
                                if (keyEquals(key, cur)) {
                                    // key is present
                                    return fIndex;
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
        } else {
            return insertNullKey(value);
        }
    }

    int insertNullKey(short value) {
        // noinspection unchecked
        K[] keys = (K[]) set;
        int capacity = keys.length;
        int index;
        K cur;
        keyAbsentFreeSlot:
        if ((cur = keys[index = 0]) != FREE) {
            if (cur == null) {
                // key is present
                return index;
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
                                return bIndex;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == FREE) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else if (cur == null) {
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
                        if ((cur = keys[bIndex]) == FREE) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == null) {
                            // key is present
                            return bIndex;
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
                            return fIndex;
                        } else if (cur == REMOVED && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 2;
                    }
                }
                // key is absent, removed slot
                incrementModCount();
                keys[firstRemoved] = null;
                values[firstRemoved] = value;
                postRemovedSlotInsertHook();
                return -1;
            }
        }
        // key is absent, free slot
        incrementModCount();
        keys[index] = null;
        values[index] = value;
        postFreeSlotInsertHook();
        return -1;
    }

    @Override
    void allocateArrays(int capacity) {
        super.allocateArrays(capacity);
        values = new short[capacity];
    }
}

