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
import net.openhft.koloboke.collect.map.hash.HashCharFloatMap;
import javax.annotation.Nonnull;


public abstract class ImmutableQHashSeparateKVCharFloatMapSO
        extends ImmutableQHashSeparateKVCharKeyMap
        implements HashCharFloatMap, InternalCharFloatMapOps, SeparateKVCharFloatQHash {

    int[] values;

    void copy(SeparateKVCharFloatQHash hash) {
        super.copy(hash);
        values = hash.valueArray().clone();
    }

    void move(SeparateKVCharFloatQHash hash) {
        super.move(hash);
        values = hash.valueArray();
    }

    @Override
    @Nonnull
    public int[] valueArray() {
        return values;
    }

    
    int valueIndex(int value) {
        if (isEmpty())
            return -1;
        int index = -1;
        char free = freeValue;
        char[] keys = set;
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            if (keys[i] != free) {
                if (value == vals[i]) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    
    boolean containsValue(int value) {
        return valueIndex(value) >= 0;
    }

    boolean removeValue(int value) {
        throw new UnsupportedOperationException();
    }
    
    int valueIndex(float value) {
        if (isEmpty())
            return -1;
        int val = Float.floatToIntBits(value);
        int index = -1;
        char free = freeValue;
        char[] keys = set;
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            if (keys[i] != free) {
                if (val == vals[i]) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    @Override public
    boolean containsValue(float value) {
        return valueIndex(value) >= 0;
    }

    boolean removeValue(float value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(((Float) value).floatValue());
    }

}

