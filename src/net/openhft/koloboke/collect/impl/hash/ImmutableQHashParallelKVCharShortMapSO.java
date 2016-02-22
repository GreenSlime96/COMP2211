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
import net.openhft.koloboke.collect.map.hash.HashCharShortMap;
import javax.annotation.Nonnull;


public abstract class ImmutableQHashParallelKVCharShortMapSO
        extends ImmutableQHashParallelKVCharKeyMap
        implements HashCharShortMap, InternalCharShortMapOps, ParallelKVCharShortQHash {


    
    int valueIndex(short value) {
        if (isEmpty())
            return -1;
        int index = -1;
        char free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            if ((char) (entry = tab[i]) != free) {
                if (value == (short) (entry >>> 16)) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    @Override public
    boolean containsValue(short value) {
        return valueIndex(value) >= 0;
    }

    boolean removeValue(short value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(((Short) value).shortValue());
    }

}

