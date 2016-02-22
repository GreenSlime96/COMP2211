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


import net.openhft.koloboke.collect.FloatCollection;
import net.openhft.koloboke.collect.hash.*;
import net.openhft.koloboke.collect.set.hash.HashFloatSetFactory;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;


public abstract class LHashFloatSetFactorySO
        extends FloatLHashFactory
            
        implements HashFloatSetFactory {

    LHashFloatSetFactorySO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }


    MutableLHashFloatSetGO uninitializedMutableSet() {
        return new MutableLHashFloatSet();
    }
    UpdatableLHashFloatSetGO uninitializedUpdatableSet() {
        return new UpdatableLHashFloatSet();
    }
    ImmutableLHashFloatSetGO uninitializedImmutableSet() {
        return new ImmutableLHashFloatSet();
    }

    @Override
    @Nonnull
    public MutableLHashFloatSetGO newMutableSet(int expectedSize) {
        MutableLHashFloatSetGO set = new MutableLHashFloatSet();
        set.init(configWrapper, expectedSize);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSet(int expectedSize) {
        UpdatableLHashFloatSetGO set = new UpdatableLHashFloatSet();
        set.init(configWrapper, expectedSize);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSet(Iterable<Float> elements, int expectedSize) {
        if (elements instanceof FloatCollection) {
            if (elements instanceof SeparateKVFloatLHash) {
                SeparateKVFloatLHash hash = (SeparateKVFloatLHash) elements;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashFloatSet set = new UpdatableLHashFloatSet();
                    set.copy(hash);
                    return set;
                }
            }
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableLHashFloatSetGO set = newUpdatableSet(size);
            set.addAll((Collection<Float>) elements);
            return set;
        } else {
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableLHashFloatSetGO set = newUpdatableSet(size);
            for (float e : elements) {
                set.add(e);
            }
            return set;
        }
    }
}

