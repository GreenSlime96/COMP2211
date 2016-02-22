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


public abstract class QHashIntSetFactorySO
        extends IntegerQHashFactory
            <MutableQHashIntSetGO>
        implements HashIntSetFactory {

    QHashIntSetFactorySO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    MutableQHashIntSetGO createNewMutable(int expectedSize, int free, int removed) {
        MutableQHashIntSet set = new MutableQHashIntSet();
        set.init(configWrapper, expectedSize, free, removed);
        return set;
    }

    MutableQHashIntSetGO uninitializedMutableSet() {
        return new MutableQHashIntSet();
    }
    UpdatableQHashIntSetGO uninitializedUpdatableSet() {
        return new UpdatableQHashIntSet();
    }
    ImmutableQHashIntSetGO uninitializedImmutableSet() {
        return new ImmutableQHashIntSet();
    }

    @Override
    @Nonnull
    public MutableQHashIntSetGO newMutableSet(int expectedSize) {
        return newMutableHash(expectedSize);
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSet(int expectedSize) {
        UpdatableQHashIntSetGO set = new UpdatableQHashIntSet();
        set.init(configWrapper, expectedSize, getFree());
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSet(Iterable<Integer> elements, int expectedSize) {
        if (elements instanceof IntCollection) {
            if (elements instanceof SeparateKVIntQHash) {
                SeparateKVIntQHash hash = (SeparateKVIntQHash) elements;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashIntSet set = new UpdatableQHashIntSet();
                    set.copy(hash);
                    return set;
                }
            }
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableQHashIntSetGO set = newUpdatableSet(size);
            set.addAll((Collection<Integer>) elements);
            return set;
        } else {
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableQHashIntSetGO set = newUpdatableSet(size);
            for (int e : elements) {
                set.add(e);
            }
            return set;
        }
    }
}

