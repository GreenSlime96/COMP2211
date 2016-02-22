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


import net.openhft.koloboke.collect.DoubleCollection;
import net.openhft.koloboke.collect.hash.*;
import net.openhft.koloboke.collect.set.hash.HashDoubleSetFactory;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;


public abstract class LHashDoubleSetFactorySO
        extends DoubleLHashFactory
            
        implements HashDoubleSetFactory {

    LHashDoubleSetFactorySO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }


    MutableLHashDoubleSetGO uninitializedMutableSet() {
        return new MutableLHashDoubleSet();
    }
    UpdatableLHashDoubleSetGO uninitializedUpdatableSet() {
        return new UpdatableLHashDoubleSet();
    }
    ImmutableLHashDoubleSetGO uninitializedImmutableSet() {
        return new ImmutableLHashDoubleSet();
    }

    @Override
    @Nonnull
    public MutableLHashDoubleSetGO newMutableSet(int expectedSize) {
        MutableLHashDoubleSetGO set = new MutableLHashDoubleSet();
        set.init(configWrapper, expectedSize);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSet(int expectedSize) {
        UpdatableLHashDoubleSetGO set = new UpdatableLHashDoubleSet();
        set.init(configWrapper, expectedSize);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSet(Iterable<Double> elements, int expectedSize) {
        if (elements instanceof DoubleCollection) {
            if (elements instanceof SeparateKVDoubleLHash) {
                SeparateKVDoubleLHash hash = (SeparateKVDoubleLHash) elements;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashDoubleSet set = new UpdatableLHashDoubleSet();
                    set.copy(hash);
                    return set;
                }
            }
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableLHashDoubleSetGO set = newUpdatableSet(size);
            set.addAll((Collection<Double>) elements);
            return set;
        } else {
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableLHashDoubleSetGO set = newUpdatableSet(size);
            for (double e : elements) {
                set.add(e);
            }
            return set;
        }
    }
}

