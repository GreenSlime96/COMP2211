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


import net.openhft.koloboke.collect.ByteCollection;
import net.openhft.koloboke.collect.hash.*;
import net.openhft.koloboke.collect.set.hash.HashByteSetFactory;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;


public abstract class LHashByteSetFactorySO
        extends ByteLHashFactory
            
        implements HashByteSetFactory {

    LHashByteSetFactorySO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }


    MutableLHashByteSetGO uninitializedMutableSet() {
        return new MutableLHashByteSet();
    }
    UpdatableLHashByteSetGO uninitializedUpdatableSet() {
        return new UpdatableLHashByteSet();
    }
    ImmutableLHashByteSetGO uninitializedImmutableSet() {
        return new ImmutableLHashByteSet();
    }

    @Override
    @Nonnull
    public MutableLHashByteSetGO newMutableSet(int expectedSize) {
        MutableLHashByteSetGO set = new MutableLHashByteSet();
        set.init(configWrapper, expectedSize, getFree());
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSet(int expectedSize) {
        UpdatableLHashByteSetGO set = new UpdatableLHashByteSet();
        set.init(configWrapper, expectedSize, getFree());
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSet(Iterable<Byte> elements, int expectedSize) {
        if (elements instanceof ByteCollection) {
            if (elements instanceof SeparateKVByteLHash) {
                SeparateKVByteLHash hash = (SeparateKVByteLHash) elements;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashByteSet set = new UpdatableLHashByteSet();
                    set.copy(hash);
                    return set;
                }
            }
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableLHashByteSetGO set = newUpdatableSet(size);
            set.addAll((Collection<Byte>) elements);
            return set;
        } else {
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableLHashByteSetGO set = newUpdatableSet(size);
            for (byte e : elements) {
                set.add(e);
            }
            return set;
        }
    }
}

