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


public abstract class UpdatableParallelKVObjLHashSO<E> extends UpdatableLHash
        implements ParallelKVObjLHash, LHash {

    Object[] table;

    void copy(ParallelKVObjLHash hash) {
        super.copy(hash);
        table = hash.table().clone();
    }

    void move(ParallelKVObjLHash hash) {
        super.copy(hash);
        table = hash.table();
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
            Object[] tab = table;
            int capacityMask, index;
            E cur;
            if ((cur = (E) tab[index = ParallelKVObjKeyMixing.mix(keyHashCode(k)) & (capacityMask = tab.length - 2)]) == k) {
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
                            if ((cur = (E) tab[(index = (index - 2) & capacityMask)]) == k) {
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
        Object[] tab = table;
        int capacityMask, index;
        E cur;
        if ((cur = (E) tab[index = 0]) == null) {
            // key is present
            return index;
        } else {
            if (cur == FREE) {
                // key is absent
                return -1;
            } else {
                capacityMask = tab.length - 2;
                while (true) {
                    if ((cur = (E) tab[(index = (index - 2) & capacityMask)]) == null) {
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
        table = new Object[capacity * 2];
        fillFree();
    }

    @Override
    public void clear() {
        super.clear();
        Object[] tab = table;
        for (int i = 0; i < tab.length; i += 2) {
            tab[i] = FREE;
            tab[i + 1] = null;
        }
    }

    private void fillFree() {
        Object[] tab = table;
        for (int i = 0; i < tab.length; i += 2) {
            tab[i] = FREE;
        }
    }

}

