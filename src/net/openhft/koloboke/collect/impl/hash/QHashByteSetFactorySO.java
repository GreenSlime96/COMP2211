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


public abstract class QHashByteSetFactorySO
        extends ByteQHashFactory
            <MutableQHashByteSetGO>
        implements HashByteSetFactory {

    QHashByteSetFactorySO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    MutableQHashByteSetGO createNewMutable(int expectedSize, byte free, byte removed) {
        MutableQHashByteSet set = new MutableQHashByteSet();
        set.init(configWrapper, expectedSize, free, removed);
        return set;
    }

    MutableQHashByteSetGO uninitializedMutableSet() {
        return new MutableQHashByteSet();
    }
    UpdatableQHashByteSetGO uninitializedUpdatableSet() {
        return new UpdatableQHashByteSet();
    }
    ImmutableQHashByteSetGO uninitializedImmutableSet() {
        return new ImmutableQHashByteSet();
    }

    @Override
    @Nonnull
    public MutableQHashByteSetGO newMutableSet(int expectedSize) {
        return newMutableHash(expectedSize);
    }

    @Override
    @Nonnull
    public UpdatableQHashByteSetGO newUpdatableSet(int expectedSize) {
        UpdatableQHashByteSetGO set = new UpdatableQHashByteSet();
        set.init(configWrapper, expectedSize, getFree());
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashByteSetGO newUpdatableSet(Iterable<Byte> elements, int expectedSize) {
        if (elements instanceof ByteCollection) {
            if (elements instanceof SeparateKVByteQHash) {
                SeparateKVByteQHash hash = (SeparateKVByteQHash) elements;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashByteSet set = new UpdatableQHashByteSet();
                    set.copy(hash);
                    return set;
                }
            }
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableQHashByteSetGO set = newUpdatableSet(size);
            set.addAll((Collection<Byte>) elements);
            return set;
        } else {
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableQHashByteSetGO set = newUpdatableSet(size);
            for (byte e : elements) {
                set.add(e);
            }
            return set;
        }
    }
}

