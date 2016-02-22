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


public abstract class UpdatableParallelKVObjQHashSO<E> extends UpdatableQHash
        implements ParallelKVObjQHash, QHash {

    Object[] table;

    void copy(ParallelKVObjQHash hash) {
        super.copy(hash);
        table = hash.table().clone();
    }

    void move(ParallelKVObjQHash hash) {
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
            int capacity, index;
            E cur;
            if ((cur = (E) tab[index = ParallelKVObjKeyMixing.mix(keyHashCode(k)) % (capacity = tab.length)]) == k) {
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
                        int bIndex = index, fIndex = index, step = 2;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (E) tab[bIndex]) == k) {
                                // key is present
                                return bIndex;
                            } else if (cur == FREE) {
                                // key is absent
                                return -1;
                            }
                            else if (keyEquals(k, cur)) {
                                // key is present
                                return bIndex;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (E) tab[fIndex]) == k) {
                                // key is present
                                return fIndex;
                            } else if (cur == FREE) {
                                // key is absent
                                return -1;
                            }
                            else if (keyEquals(k, cur)) {
                                // key is present
                                return fIndex;
                            }
                            step += 4;
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
        int capacity = tab.length;
        int index;
        E cur;
        if ((cur = (E) tab[index = 0]) == null) {
            // key is present
            return index;
        } else {
            if (cur == FREE) {
                // key is absent
                return -1;
            } else {
                int bIndex = index, fIndex = index, step = 2;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (E) tab[bIndex]) == null) {
                        // key is present
                        return bIndex;
                    } else if (cur == FREE) {
                        // key is absent
                        return -1;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (E) tab[fIndex]) == null) {
                        // key is present
                        return fIndex;
                    } else if (cur == FREE) {
                        // key is absent
                        return -1;
                    }
                    step += 4;
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

