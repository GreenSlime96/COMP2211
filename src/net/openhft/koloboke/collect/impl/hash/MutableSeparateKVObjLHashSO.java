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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.*;


public abstract class MutableSeparateKVObjLHashSO<E> extends MutableLHash
        implements SeparateKVObjLHash, LHash {

    Object[] set;

    void copy(SeparateKVObjLHash hash) {
        super.copy(hash);
        set = hash.keys().clone();
    }

    void move(SeparateKVObjLHash hash) {
        super.copy(hash);
        set = hash.keys();
    }

    boolean nullableKeyEquals(@Nullable E a, @Nullable E b) {
        return a == b || (a != null && a.equals(b));
    }

    boolean keyEquals(@Nonnull E a, @Nullable E b) {
        return a.equals(b);
    }

    int nullableKeyHashCode(@Nullable E key) {
        return key != null ? key.hashCode() : 0;
    }

    int keyHashCode(@Nonnull E key) {
        return key.hashCode();
    }


    public boolean contains(@Nullable Object key) {
        return index(key) >= 0;
    }

    int index(@Nullable Object key) {
        if (key != null) {
            // noinspection unchecked
            E k = (E) key;
            // noinspection unchecked
            E[] keys = (E[]) set;
            int capacityMask, index;
            E cur;
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(k)) & (capacityMask = keys.length - 1)]) == k) {
                // key is present
                return index;
            } else {
                if (cur == FREE) {
                    // key is absent
                    return -1;
                } else {
                    if (keyEquals(k, cur)) {
                        // key is present
                        return index;
                    } else {
                        while (true) {
                            if ((cur = keys[(index = (index - 1) & capacityMask)]) == k) {
                                // key is present
                                return index;
                            } else if (cur == FREE) {
                                // key is absent
                                return -1;
                            }
                            else if (keyEquals(k, cur)) {
                                // key is present
                                return index;
                            }
                        }
                    }
                }
            }
        } else {
            return indexNullKey();
        }
    }

    int indexNullKey() {
        // noinspection unchecked
        E[] keys = (E[]) set;
        int capacityMask, index;
        E cur;
        if ((cur = keys[index = 0]) == null) {
            // key is present
            return index;
        } else {
            if (cur == FREE) {
                // key is absent
                return -1;
            } else {
                capacityMask = keys.length - 1;
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == null) {
                        // key is present
                        return index;
                    } else if (cur == FREE) {
                        // key is absent
                        return -1;
                    }
                }
            }
        }
    }


    @Override
    void allocateArrays(int capacity) {
        set = new Object[capacity];
        fillFree();
    }

    @Override
    public void clear() {
        super.clear();
        fillFree();
    }

    private void fillFree() {
        Arrays.fill(set, FREE);
    }

}

