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


public abstract class LHashCharSetFactorySO
        extends CharacterLHashFactory
            
        implements HashCharSetFactory {

    LHashCharSetFactorySO(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }


    MutableLHashCharSetGO uninitializedMutableSet() {
        return new MutableLHashCharSet();
    }
    UpdatableLHashCharSetGO uninitializedUpdatableSet() {
        return new UpdatableLHashCharSet();
    }
    ImmutableLHashCharSetGO uninitializedImmutableSet() {
        return new ImmutableLHashCharSet();
    }

    @Override
    @Nonnull
    public MutableLHashCharSetGO newMutableSet(int expectedSize) {
        MutableLHashCharSetGO set = new MutableLHashCharSet();
        set.init(configWrapper, expectedSize, getFree());
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashCharSetGO newUpdatableSet(int expectedSize) {
        UpdatableLHashCharSetGO set = new UpdatableLHashCharSet();
        set.init(configWrapper, expectedSize, getFree());
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashCharSetGO newUpdatableSet(Iterable<Character> elements, int expectedSize) {
        if (elements instanceof CharCollection) {
            if (elements instanceof SeparateKVCharLHash) {
                SeparateKVCharLHash hash = (SeparateKVCharLHash) elements;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashCharSet set = new UpdatableLHashCharSet();
                    set.copy(hash);
                    return set;
                }
            }
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableLHashCharSetGO set = newUpdatableSet(size);
            set.addAll((Collection<Character>) elements);
            return set;
        } else {
            int size = elements instanceof Set ? ((Set) elements).size() : expectedSize;
            UpdatableLHashCharSetGO set = newUpdatableSet(size);
            for (char e : elements) {
                set.add(e);
            }
            return set;
        }
    }
}

