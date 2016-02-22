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
import net.openhft.koloboke.collect.map.hash.HashLongDoubleMap;
import javax.annotation.Nonnull;


public abstract class UpdatableQHashParallelKVLongDoubleMapSO
        extends UpdatableQHashParallelKVLongKeyMap
        implements HashLongDoubleMap, InternalLongDoubleMapOps, ParallelKVLongDoubleQHash {


    
    int valueIndex(long value) {
        if (isEmpty())
            return -1;
        int index = -1;
        int mc = modCount();
        long free = freeValue;
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            if (tab[i] != free) {
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

    
    boolean containsValue(long value) {
        return valueIndex(value) >= 0;
    }

    boolean removeValue(long value) {
        throw new UnsupportedOperationException();
    }
    
    int valueIndex(double value) {
        if (isEmpty())
            return -1;
        long val = Double.doubleToLongBits(value);
        int index = -1;
        int mc = modCount();
        long free = freeValue;
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            if (tab[i] != free) {
                if (val == tab[i + 1]) {
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
    boolean containsValue(double value) {
        return valueIndex(value) >= 0;
    }

    boolean removeValue(double value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(((Double) value).doubleValue());
    }

    int insert(long key, long value) {
        long free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        long[] tab = table;
        int capacity, index;
        long cur;
        keyAbsent:
        if ((cur = tab[index = ParallelKVLongKeyMixing.mix(key) % (capacity = tab.length)]) != free) {
            if (cur == key) {
                // key is present
                return index;
            } else {
                int bIndex = index, fIndex = index, step = 2;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = tab[bIndex]) == free) {
                        index = bIndex;
                        break keyAbsent;
                    } else if (cur == key) {
                        // key is present
                        return bIndex;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = tab[fIndex]) == free) {
                        index = fIndex;
                        break keyAbsent;
                    } else if (cur == key) {
                        // key is present
                        return fIndex;
                    }
                    step += 4;
                }
            }
        }
        // key is absent
        incrementModCount();
        tab[index] = key;
        tab[index + 1] = value;
        postInsertHook();
        return -1;
    }


}

