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


public abstract class QHashDoubleSetFactorySO
        extends DoubleQHashFactory
            
        implements HashDoubleSetFactory {

    QHashDoubleSetFactorySO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }


    MutableQHashDoubleSetGO uninitializedMutableSet() {
        return new MutableQHashDoubleSet();
    }
    UpdatableQHashDoubleSetGO uninitializedUpdatableSet() {
        return new UpdatableQHashDoubleSet();
    }
    ImmutableQHashDoubleSetGO uninitializedImmutableSet() {
        return new ImmutableQHashDoubleSet();
    }

    @Override
    @Nonnull
    public MutableQHashDoubleSetGO newMutableSet(int expectedSize) {
        MutableQHashDoubleSetGO set = new MutableQHashDoubleSet();
        set.init(configWrapper, expectedSize);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashDoubleSetGO newUpdatableSet(int expectedSize) {
        UpdatableQHashDoubleSetGO set = new UpdatableQHashDoubleSet();
        set.init(configWrapper, expectedSize);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashDoubleSetGO newUpdatableSet(Iterable<Double> elements, int expectedSize) {
        if (elements instanceof DoubleCollection) {
            if (elements instanceof SeparateKVDoubleQHash) {
                SeparateKVDoubleQHash hash = (SeparateKVDoubleQHash) elements;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashDoubleSet set = new UpdatableQHashDoubleSet();
                    set.copy(hash);
                    return set;
                }
            }
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableQHashDoubleSetGO set = newUpdatableSet(size);
            set.addAll((Collection<Double>) elements);
            return set;
        } else {
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableQHashDoubleSetGO set = newUpdatableSet(size);
            for (double e : elements) {
                set.add(e);
            }
            return set;
        }
    }
}

