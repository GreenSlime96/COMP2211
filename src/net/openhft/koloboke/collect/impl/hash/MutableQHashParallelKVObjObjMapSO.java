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
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.*;


public abstract class MutableQHashParallelKVObjObjMapSO<K, V>
        extends MutableQHashParallelKVObjKeyMap<K>
        implements HashObjObjMap<K, V>,
        InternalObjObjMapOps<K, V>, ParallelKVObjObjQHash {


    boolean nullableValueEquals(@Nullable V a, @Nullable V b) {
        return a == b || (a != null && a.equals(b));
    }

    boolean valueEquals(@Nonnull V a, @Nullable V b) {
        return a.equals(b);
    }

    int nullableValueHashCode(@Nullable V value) {
        return value != null ? value.hashCode() : 0;
    }

    int valueHashCode(@Nonnull V value) {
        return value.hashCode();
    }


    int valueIndex(@Nullable Object value) {
        if (value == null)
            return nullValueIndex();
        if (isEmpty())
            return -1;
        V val = (V) value;
        int index = -1;
        int mc = modCount();
        Object[] tab = table;
        if (noRemoved()) {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                // noinspection unchecked
                if ((K) tab[i] != FREE) {
                    if (valueEquals(val, (V) tab[i + 1])) {
                        index = i;
                        break;
                    }
                }
            }
        } else {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                Object key;
                // noinspection unchecked
                if ((key = (K) tab[i]) != FREE && key != REMOVED) {
                    if (valueEquals(val, (V) tab[i + 1])) {
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

    private int nullValueIndex() {
        if (isEmpty())
            return -1;
        int index = -1;
        int mc = modCount();
        Object[] tab = table;
        if (noRemoved()) {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                // noinspection unchecked
                if ((K) tab[i] != FREE) {
                    if (tab[i + 1] == null) {
                        index = i;
                        break;
                    }
                }
            }
        } else {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                Object key;
                // noinspection unchecked
                if ((key = (K) tab[i]) != FREE && key != REMOVED) {
                    if (tab[i + 1] == null) {
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

    @Override
    public boolean containsValue(Object value) {
        return valueIndex(value) >= 0;
    }

    boolean removeValue(@Nullable Object value) {
        int index = valueIndex(value);
        if (index >= 0) {
            removeAt(index);
            return true;
        } else {
            return false;
        }
    }


    int insert(K key, V value) {
        if (key != null) {
            Object[] tab = table;
            int capacity, index;
            K cur;
            keyAbsentFreeSlot:
            if ((cur = (K) tab[index = ParallelKVObjKeyMixing.mix(keyHashCode(key)) % (capacity = tab.length)]) != FREE) {
                if (cur == key) {
                    // key is present
                    return index;
                } else {
                    int firstRemoved;
                    if (cur != REMOVED) {
                        if (!keyEquals(key, cur)) {
                            if (noRemoved()) {
                                int bIndex = index, fIndex = index, step = 2;
                                while (true) {
                                    if ((bIndex -= step) < 0) bIndex += capacity;
                                    if ((cur = (K) tab[bIndex]) == FREE) {
                                        index = bIndex;
                                        break keyAbsentFreeSlot;
                                    } else if (cur == key || (keyEquals(key, cur))) {
                                        // key is present
                                        return bIndex;
                                    }
                                    int t;
                                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                                    if ((cur = (K) tab[fIndex]) == FREE) {
                                        index = fIndex;
                                        break keyAbsentFreeSlot;
                                    } else if (cur == key || (keyEquals(key, cur))) {
                                        // key is present
                                        return fIndex;
                                    }
                                    step += 4;
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
                        int bIndex = index, fIndex = index, step = 2;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (K) tab[bIndex]) == FREE) {
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
                            if ((cur = (K) tab[fIndex]) == FREE) {
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
        } else {
            return insertNullKey(value);
        }
    }

    int insertNullKey(V value) {
        Object[] tab = table;
        int capacity = tab.length;
        int index;
        K cur;
        keyAbsentFreeSlot:
        if ((cur = (K) tab[index = 0]) != FREE) {
            if (cur == null) {
                // key is present
                return index;
            } else {
                int firstRemoved;
                if (cur != REMOVED) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 2;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (K) tab[bIndex]) == FREE) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else if (cur == null) {
                                // key is present
                                return bIndex;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (K) tab[fIndex]) == FREE) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else if (cur == null) {
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
                        if ((cur = (K) tab[bIndex]) == FREE) {
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
                        if ((cur = (K) tab[fIndex]) == FREE) {
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
                        step += 4;
                    }
                }
                // key is absent, removed slot
                incrementModCount();
                tab[firstRemoved] = null;
                tab[firstRemoved + 1] = value;
                postRemovedSlotInsertHook();
                return -1;
            }
        }
        // key is absent, free slot
        incrementModCount();
        tab[index] = null;
        tab[index + 1] = value;
        postFreeSlotInsertHook();
        return -1;
    }

}

