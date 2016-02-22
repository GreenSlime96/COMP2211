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

import java.util.Arrays;


public abstract class ImmutableParallelKVFloatLHashSO extends ImmutableLHash
        implements ParallelKVFloatLHash, PrimitiveConstants, UnsafeConstants {

    long[] table;

    void copy(ParallelKVFloatLHash hash) {
        super.copy(hash);
        table = hash.table().clone();
    }

    void move(ParallelKVFloatLHash hash) {
        super.copy(hash);
        table = hash.table();
    }


    public boolean contains(Object key) {
        return contains(((Float) key).floatValue());
    }

    public boolean contains(float key) {
        return index(Float.floatToIntBits(key)) >= 0;
    }

    public boolean contains(int key) {
        return index(key) >= 0;
    }

    int index(int key) {
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
            // key is present
            return index;
        } else {
            if (cur == FREE_BITS) {
                // key is absent
                return -1;
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                        // key is present
                        return index;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return -1;
                    }
                }
            }
        }
    }



}

