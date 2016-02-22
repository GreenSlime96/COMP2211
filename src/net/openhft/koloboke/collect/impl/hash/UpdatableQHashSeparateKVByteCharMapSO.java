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
import net.openhft.koloboke.collect.map.hash.HashByteCharMap;
import javax.annotation.Nonnull;


public abstract class UpdatableQHashSeparateKVByteCharMapSO
        extends UpdatableQHashSeparateKVByteKeyMap
        implements HashByteCharMap, InternalByteCharMapOps, SeparateKVByteCharQHash {

    char[] values;

    void copy(SeparateKVByteCharQHash hash) {
        super.copy(hash);
        values = hash.valueArray().clone();
    }

    void move(SeparateKVByteCharQHash hash) {
        super.move(hash);
        values = hash.valueArray();
    }

    @Override
    @Nonnull
    public char[] valueArray() {
        return values;
    }

    
    int valueIndex(char value) {
        if (isEmpty())
            return -1;
        int index = -1;
        int mc = modCount();
        byte free = freeValue;
        byte[] keys = set;
        char[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            if (keys[i] != free) {
                if (value == vals[i]) {
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
    boolean containsValue(char value) {
        return valueIndex(value) >= 0;
    }

    boolean removeValue(char value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(((Character) value).charValue());
    }

    int insert(byte key, char value) {
        byte free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        byte[] keys = set;
        int capacity, index;
        byte cur;
        keyAbsent:
        if ((cur = keys[index = SeparateKVByteKeyMixing.mix(key) % (capacity = keys.length)]) != free) {
            if (cur == key) {
                // key is present
                return index;
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == free) {
                        index = bIndex;
                        break keyAbsent;
                    } else if (cur == key) {
                        // key is present
                        return bIndex;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == free) {
                        index = fIndex;
                        break keyAbsent;
                    } else if (cur == key) {
                        // key is present
                        return fIndex;
                    }
                    step += 2;
                }
            }
        }
        // key is absent
        incrementModCount();
        keys[index] = key;
        values[index] = value;
        postInsertHook();
        return -1;
    }


    @Override
    void allocateArrays(int capacity) {
        super.allocateArrays(capacity);
        values = new char[capacity];
    }
}

