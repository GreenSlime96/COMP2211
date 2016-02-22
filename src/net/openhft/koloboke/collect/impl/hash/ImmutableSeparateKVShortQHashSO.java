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

import net.openhft.koloboke.collect.hash.HashOverflowException;
import net.openhft.koloboke.collect.impl.*;

import java.util.*;
import java.util.concurrent
     .ThreadLocalRandom;


public abstract class ImmutableSeparateKVShortQHashSO extends ImmutableQHash
        implements SeparateKVShortQHash, PrimitiveConstants, UnsafeConstants {

    short freeValue;

    short[] set;

    void copy(SeparateKVShortQHash hash) {
        super.copy(hash);
        freeValue = hash.freeValue();

        set = hash.keys().clone();

    }

    void move(SeparateKVShortQHash hash) {
        super.copy(hash);
        freeValue = hash.freeValue();

        set = hash.keys();

    }

    final void init(HashConfigWrapper configWrapper, int size, short freeValue
            ) {
        this.freeValue = freeValue;
        // calls allocateArrays, fill keys with this.freeValue => assign it before
        super.init(configWrapper, size);
    }


    @Override
    public short freeValue() {
        return freeValue;
    }

    @Override
    public boolean supportRemoved() {
        return false;
    }

    @Override
    public short removedValue() {
        throw new UnsupportedOperationException();
    }

    public boolean contains(Object key) {
        return contains(((Short) key).shortValue());
    }

    public boolean contains(short key) {
        return index(key) >= 0;
    }

    int index(short key) {
        short free;
        if (key != (free = freeValue)) {
            short[] keys = set;
            int capacity, index;
            short cur;
            if ((cur = keys[index = SeparateKVShortKeyMixing.mix(key) % (capacity = keys.length)]) == key) {
                // key is present
                return index;
            } else {
                if (cur == free) {
                    // key is absent
                    return -1;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = keys[bIndex]) == key) {
                            // key is present
                            return bIndex;
                        } else if (cur == free) {
                            // key is absent
                            return -1;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == key) {
                            // key is present
                            return fIndex;
                        } else if (cur == free) {
                            // key is absent
                            return -1;
                        }
                        step += 2;
                    }
                }
            }
        } else {
            // key is absent
            return -1;
        }
    }

}

