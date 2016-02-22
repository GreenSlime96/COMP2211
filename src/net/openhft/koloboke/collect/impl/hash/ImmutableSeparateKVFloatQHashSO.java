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


public abstract class ImmutableSeparateKVFloatQHashSO extends ImmutableQHash
        implements SeparateKVFloatQHash, PrimitiveConstants, UnsafeConstants {

    int[] set;

    void copy(SeparateKVFloatQHash hash) {
        super.copy(hash);
        set = hash.keys().clone();
    }

    void move(SeparateKVFloatQHash hash) {
        super.copy(hash);
        set = hash.keys();
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
        int[] keys = set;
        int capacity, index;
        int cur;
        if ((cur = keys[index = SeparateKVFloatKeyMixing.mix(key) % (capacity = keys.length)]) == key) {
            // key is present
            return index;
        } else {
            if (cur == FREE_BITS) {
                // key is absent
                return -1;
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == key) {
                        // key is present
                        return bIndex;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return -1;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == key) {
                        // key is present
                        return fIndex;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return -1;
                    }
                    step += 2;
                }
            }
        }
    }



}

