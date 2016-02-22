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
import net.openhft.koloboke.collect.map.hash.HashFloatDoubleMap;
import javax.annotation.Nonnull;


public abstract class ImmutableLHashSeparateKVFloatDoubleMapSO
        extends ImmutableLHashSeparateKVFloatKeyMap
        implements HashFloatDoubleMap, InternalFloatDoubleMapOps, SeparateKVFloatDoubleLHash {

    long[] values;

    void copy(SeparateKVFloatDoubleLHash hash) {
        super.copy(hash);
        values = hash.valueArray().clone();
    }

    void move(SeparateKVFloatDoubleLHash hash) {
        super.move(hash);
        values = hash.valueArray();
    }

    @Override
    @Nonnull
    public long[] valueArray() {
        return values;
    }

    
    int valueIndex(long value) {
        if (isEmpty())
            return -1;
        int index = -1;
        int[] keys = set;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            if (keys[i] < FREE_BITS) {
                if (value == vals[i]) {
                    index = i;
                    break;
                }
            }
        }
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
        int[] keys = set;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            if (keys[i] < FREE_BITS) {
                if (val == vals[i]) {
                    index = i;
                    break;
                }
            }
        }
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

}

