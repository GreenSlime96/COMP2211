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


public abstract class MutableLHashParallelKVObjObjMapSO<K, V>
        extends MutableLHashParallelKVObjKeyMap<K>
        implements HashObjObjMap<K, V>,
        InternalObjObjMapOps<K, V>, ParallelKVObjObjLHash {


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
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            // noinspection unchecked
            if ((K) tab[i] != FREE) {
                if (valueEquals(val, (V) tab[i + 1])) {
                    index = i;
                    break;
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
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            // noinspection unchecked
            if ((K) tab[i] != FREE) {
                if (tab[i + 1] == null) {
                    index = i;
                    break;
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
            int capacityMask, index;
            K cur;
            keyAbsent:
            if ((cur = (K) tab[index = ParallelKVObjKeyMixing.mix(keyHashCode(key)) & (capacityMask = tab.length - 2)]) != FREE) {
                if (cur == key || keyEquals(key, cur)) {
                    // key is present
                    return index;
                } else {
                    while (true) {
                        if ((cur = (K) tab[(index = (index - 2) & capacityMask)]) == FREE) {
                            break keyAbsent;
                        } else if (cur == key || (keyEquals(key, cur))) {
                            // key is present
                            return index;
                        }
                    }
                }
            }
            // key is absent
            incrementModCount();
            tab[index] = key;
            tab[index + 1] = value;
            postInsertHook();
            return -1;
        } else {
            return insertNullKey(value);
        }
    }

    int insertNullKey(V value) {
        Object[] tab = table;
        int capacityMask, index;
        K cur;
        keyAbsent:
        if ((cur = (K) tab[index = 0]) != FREE) {
            if (cur == null) {
                // key is present
                return index;
            } else {
                capacityMask = tab.length - 2;
                while (true) {
                    if ((cur = (K) tab[(index = (index - 2) & capacityMask)]) == FREE) {
                        break keyAbsent;
                    } else if (cur == null) {
                        // key is present
                        return index;
                    }
                }
            }
        }
        // key is absent
        incrementModCount();
        tab[index] = null;
        tab[index + 1] = value;
        postInsertHook();
        return -1;
    }

}

