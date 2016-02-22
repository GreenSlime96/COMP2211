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


import net.openhft.koloboke.collect.IntCollection;
import net.openhft.koloboke.collect.hash.*;
import net.openhft.koloboke.collect.set.hash.HashIntSetFactory;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;


public abstract class LHashIntSetFactorySO
        extends IntegerLHashFactory
            
        implements HashIntSetFactory {

    LHashIntSetFactorySO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }


    MutableLHashIntSetGO uninitializedMutableSet() {
        return new MutableLHashIntSet();
    }
    UpdatableLHashIntSetGO uninitializedUpdatableSet() {
        return new UpdatableLHashIntSet();
    }
    ImmutableLHashIntSetGO uninitializedImmutableSet() {
        return new ImmutableLHashIntSet();
    }

    @Override
    @Nonnull
    public MutableLHashIntSetGO newMutableSet(int expectedSize) {
        MutableLHashIntSetGO set = new MutableLHashIntSet();
        set.init(configWrapper, expectedSize, getFree());
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashIntSetGO newUpdatableSet(int expectedSize) {
        UpdatableLHashIntSetGO set = new UpdatableLHashIntSet();
        set.init(configWrapper, expectedSize, getFree());
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashIntSetGO newUpdatableSet(Iterable<Integer> elements, int expectedSize) {
        if (elements instanceof IntCollection) {
            if (elements instanceof SeparateKVIntLHash) {
                SeparateKVIntLHash hash = (SeparateKVIntLHash) elements;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashIntSet set = new UpdatableLHashIntSet();
                    set.copy(hash);
                    return set;
                }
            }
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableLHashIntSetGO set = newUpdatableSet(size);
            set.addAll((Collection<Integer>) elements);
            return set;
        } else {
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableLHashIntSetGO set = newUpdatableSet(size);
            for (int e : elements) {
                set.add(e);
            }
            return set;
        }
    }
}

