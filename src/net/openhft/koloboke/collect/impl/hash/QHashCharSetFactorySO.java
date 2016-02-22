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


import net.openhft.koloboke.collect.CharCollection;
import net.openhft.koloboke.collect.hash.*;
import net.openhft.koloboke.collect.set.hash.HashCharSetFactory;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;


public abstract class QHashCharSetFactorySO
        extends CharacterQHashFactory
            <MutableQHashCharSetGO>
        implements HashCharSetFactory {

    QHashCharSetFactorySO(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    MutableQHashCharSetGO createNewMutable(int expectedSize, char free, char removed) {
        MutableQHashCharSet set = new MutableQHashCharSet();
        set.init(configWrapper, expectedSize, free, removed);
        return set;
    }

    MutableQHashCharSetGO uninitializedMutableSet() {
        return new MutableQHashCharSet();
    }
    UpdatableQHashCharSetGO uninitializedUpdatableSet() {
        return new UpdatableQHashCharSet();
    }
    ImmutableQHashCharSetGO uninitializedImmutableSet() {
        return new ImmutableQHashCharSet();
    }

    @Override
    @Nonnull
    public MutableQHashCharSetGO newMutableSet(int expectedSize) {
        return newMutableHash(expectedSize);
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSet(int expectedSize) {
        UpdatableQHashCharSetGO set = new UpdatableQHashCharSet();
        set.init(configWrapper, expectedSize, getFree());
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSet(Iterable<Character> elements, int expectedSize) {
        if (elements instanceof CharCollection) {
            if (elements instanceof SeparateKVCharQHash) {
                SeparateKVCharQHash hash = (SeparateKVCharQHash) elements;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashCharSet set = new UpdatableQHashCharSet();
                    set.copy(hash);
                    return set;
                }
            }
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableQHashCharSetGO set = newUpdatableSet(size);
            set.addAll((Collection<Character>) elements);
            return set;
        } else {
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableQHashCharSetGO set = newUpdatableSet(size);
            for (char e : elements) {
                set.add(e);
            }
            return set;
        }
    }
}

