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


import net.openhft.koloboke.collect.LongCollection;
import net.openhft.koloboke.collect.hash.*;
import net.openhft.koloboke.collect.set.hash.HashLongSetFactory;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;


public abstract class LHashLongSetFactorySO
        extends LongLHashFactory
            
        implements HashLongSetFactory {

    LHashLongSetFactorySO(HashConfig hashConf, int defaultExpectedSize
            , long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }


    MutableLHashLongSetGO uninitializedMutableSet() {
        return new MutableLHashLongSet();
    }
    UpdatableLHashLongSetGO uninitializedUpdatableSet() {
        return new UpdatableLHashLongSet();
    }
    ImmutableLHashLongSetGO uninitializedImmutableSet() {
        return new ImmutableLHashLongSet();
    }

    @Override
    @Nonnull
    public MutableLHashLongSetGO newMutableSet(int expectedSize) {
        MutableLHashLongSetGO set = new MutableLHashLongSet();
        set.init(configWrapper, expectedSize, getFree());
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashLongSetGO newUpdatableSet(int expectedSize) {
        UpdatableLHashLongSetGO set = new UpdatableLHashLongSet();
        set.init(configWrapper, expectedSize, getFree());
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashLongSetGO newUpdatableSet(Iterable<Long> elements, int expectedSize) {
        if (elements instanceof LongCollection) {
            if (elements instanceof SeparateKVLongLHash) {
                SeparateKVLongLHash hash = (SeparateKVLongLHash) elements;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashLongSet set = new UpdatableLHashLongSet();
                    set.copy(hash);
                    return set;
                }
            }
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableLHashLongSetGO set = newUpdatableSet(size);
            set.addAll((Collection<Long>) elements);
            return set;
        } else {
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableLHashLongSetGO set = newUpdatableSet(size);
            for (long e : elements) {
                set.add(e);
            }
            return set;
        }
    }
}

