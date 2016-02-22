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
import net.openhft.koloboke.collect.map.hash.HashByteObjMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.*;


public abstract class ImmutableQHashSeparateKVByteObjMapSO<V>
        extends ImmutableQHashSeparateKVByteKeyMap
        implements HashByteObjMap<V>,
        InternalByteObjMapOps<V>, SeparateKVByteObjQHash {

    V[] values;

    void copy(SeparateKVByteObjQHash hash) {
        super.copy(hash);
        // noinspection unchecked
        values = (V[]) hash.valueArray().clone();
    }

    void move(SeparateKVByteObjQHash hash) {
        super.move(hash);
        // noinspection unchecked
        values = (V[]) hash.valueArray();
    }

    @Override
    @Nonnull
    public Object[] valueArray() {
        return values;
    }

    boolean nullableValueEquals(@Nullable V a, @Nullable V b) {
        return a == b || (a != null && a.equals(b));
    }

    boolean valueEquals(@Nonnull V a, @Nullable V b) {
        return a.equals(b);
    }

    int nullableValueHashCode(@Nullable V value) {
        return value != null ? value.hashCode() : 0;
    }

    int valueHashCode(@Nonnull V value) {
        return value.hashCode();
    }


    int valueIndex(@Nullable Object value) {
        if (value == null)
            return nullValueIndex();
        if (isEmpty())
            return -1;
        V val = (V) value;
        int index = -1;
        byte free = freeValue;
        byte[] keys = set;
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            if (keys[i] != free) {
                if (valueEquals(val, vals[i])) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    private int nullValueIndex() {
        if (isEmpty())
            return -1;
        int index = -1;
        byte free = freeValue;
        byte[] keys = set;
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            if (keys[i] != free) {
                if (vals[i] == null) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    @Override
    public boolean containsValue(Object value) {
        return valueIndex(value) >= 0;
    }

    boolean removeValue(@Nullable Object value) {
        throw new UnsupportedOperationException();
    }


}

