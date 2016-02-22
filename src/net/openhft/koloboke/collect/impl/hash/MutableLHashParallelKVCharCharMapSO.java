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
import net.openhft.koloboke.collect.map.hash.HashCharCharMap;
import javax.annotation.Nonnull;


public abstract class MutableLHashParallelKVCharCharMapSO
        extends MutableLHashParallelKVCharKeyMap
        implements HashCharCharMap, InternalCharCharMapOps, ParallelKVCharCharLHash {


    
    int valueIndex(char value) {
        if (isEmpty())
            return -1;
        int index = -1;
        int mc = modCount();
        char free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            if ((char) (entry = tab[i]) != free) {
                if (value == (char) (entry >>> 16)) {
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
        return containsValue(((Character) value).charValue());
    }

    int insert(char key, char value) {
        char free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacityMask, index;
        char cur;
        int entry;
        keyAbsent:
        if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) != free) {
            if (cur == key) {
                // key is present
                return index;
            } else {
                while (true) {
                    if ((cur = (char) (entry = tab[(index = (index - 1) & capacityMask)])) == free) {
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
        tab[index] = (((int) key) | (((int) value) << 16));
        postInsertHook();
        return -1;
    }


}

