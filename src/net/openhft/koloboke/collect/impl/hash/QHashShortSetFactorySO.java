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


public abstract class QHashShortSetFactorySO
        extends ShortQHashFactory
            <MutableQHashShortSetGO>
        implements HashShortSetFactory {

    QHashShortSetFactorySO(HashConfig hashConf, int defaultExpectedSize
            , short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    MutableQHashShortSetGO createNewMutable(int expectedSize, short free, short removed) {
        MutableQHashShortSet set = new MutableQHashShortSet();
        set.init(configWrapper, expectedSize, free, removed);
        return set;
    }

    MutableQHashShortSetGO uninitializedMutableSet() {
        return new MutableQHashShortSet();
    }
    UpdatableQHashShortSetGO uninitializedUpdatableSet() {
        return new UpdatableQHashShortSet();
    }
    ImmutableQHashShortSetGO uninitializedImmutableSet() {
        return new ImmutableQHashShortSet();
    }

    @Override
    @Nonnull
    public MutableQHashShortSetGO newMutableSet(int expectedSize) {
        return newMutableHash(expectedSize);
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSet(int expectedSize) {
        UpdatableQHashShortSetGO set = new UpdatableQHashShortSet();
        set.init(configWrapper, expectedSize, getFree());
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSet(Iterable<Short> elements, int expectedSize) {
        if (elements instanceof ShortCollection) {
            if (elements instanceof SeparateKVShortQHash) {
                SeparateKVShortQHash hash = (SeparateKVShortQHash) elements;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashShortSet set = new UpdatableQHashShortSet();
                    set.copy(hash);
                    return set;
                }
            }
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableQHashShortSetGO set = newUpdatableSet(size);
            set.addAll((Collection<Short>) elements);
            return set;
        } else {
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableQHashShortSetGO set = newUpdatableSet(size);
            for (short e : elements) {
                set.add(e);
            }
            return set;
        }
    }
}

