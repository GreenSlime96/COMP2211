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
import net.openhft.koloboke.collect.map.hash.HashByteByteMap;
import javax.annotation.Nonnull;


public abstract class UpdatableLHashParallelKVByteByteMapSO
        extends UpdatableLHashParallelKVByteKeyMap
        implements HashByteByteMap, InternalByteByteMapOps, ParallelKVByteByteLHash {


    
    int valueIndex(byte value) {
        if (isEmpty())
            return -1;
        int index = -1;
        int mc = modCount();
        byte free = freeValue;
        char[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            if ((byte) (entry = tab[i]) != free) {
                if (value == (byte) (entry >>> 8)) {
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
    boolean containsValue(byte value) {
        return valueIndex(value) >= 0;
    }

    boolean removeValue(byte value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(((Byte) value).byteValue());
    }

    int insert(byte key, byte value) {
        byte free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        char[] tab = table;
        int capacityMask, index;
        byte cur;
        int entry;
        keyAbsent:
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) != free) {
            if (cur == key) {
                // key is present
                return index;
            } else {
                while (true) {
                    if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == free) {
                        break keyAbsent;
                    } else if (cur == key) {
                        // key is present
                        return index;
                    }
                }
            }
        }
        // key is absent
        incrementModCount();
        tab[index] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
        postInsertHook();
        return -1;
    }


}

