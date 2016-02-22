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


import net.openhft.koloboke.collect.ShortCollection;
import net.openhft.koloboke.collect.hash.*;
import net.openhft.koloboke.collect.set.hash.HashShortSetFactory;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;


public abstract class LHashShortSetFactorySO
        extends ShortLHashFactory
            
        implements HashShortSetFactory {

    LHashShortSetFactorySO(HashConfig hashConf, int defaultExpectedSize
            , short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }


    MutableLHashShortSetGO uninitializedMutableSet() {
        return new MutableLHashShortSet();
    }
    UpdatableLHashShortSetGO uninitializedUpdatableSet() {
        return new UpdatableLHashShortSet();
    }
    ImmutableLHashShortSetGO uninitializedImmutableSet() {
        return new ImmutableLHashShortSet();
    }

    @Override
    @Nonnull
    public MutableLHashShortSetGO newMutableSet(int expectedSize) {
        MutableLHashShortSetGO set = new MutableLHashShortSet();
        set.init(configWrapper, expectedSize, getFree());
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSet(int expectedSize) {
        UpdatableLHashShortSetGO set = new UpdatableLHashShortSet();
        set.init(configWrapper, expectedSize, getFree());
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSet(Iterable<Short> elements, int expectedSize) {
        if (elements instanceof ShortCollection) {
            if (elements instanceof SeparateKVShortLHash) {
                SeparateKVShortLHash hash = (SeparateKVShortLHash) elements;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashShortSet set = new UpdatableLHashShortSet();
                    set.copy(hash);
                    return set;
                }
            }
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableLHashShortSetGO set = newUpdatableSet(size);
            set.addAll((Collection<Short>) elements);
            return set;
        } else {
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableLHashShortSetGO set = newUpdatableSet(size);
            for (short e : elements) {
                set.add(e);
            }
            return set;
        }
    }
}

