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

import net.openhft.koloboke.collect.*;
import net.openhft.koloboke.collect.hash.*;
import net.openhft.koloboke.collect.set.ObjSet;
import net.openhft.koloboke.collect.set.hash.HashObjSetFactory;

import javax.annotation.Nonnull;

import java.util.Collection;
import java.util.Set;


public abstract class QHashObjSetFactorySO<E> extends ObjHashFactorySO<E>
        implements HashObjSetFactory<E> {

    QHashObjSetFactorySO(HashConfig hashConf, int defaultExpectedSize, boolean isNullAllowed) {
        super(hashConf, defaultExpectedSize, isNullAllowed);
    }

    String keySpecialString() {
        return ",equivalence=" + getEquivalence() +
                ",nullKeyAllowed=" + isNullKeyAllowed();
    }

    boolean keySpecialEquals(HashObjSetFactory<?> other) {
        return getEquivalence().equals(other.getEquivalence()) &&
                isNullKeyAllowed() == other.isNullKeyAllowed();
    }

    @Nonnull
    @Override
    public Equivalence<E> getEquivalence() {
        return Equivalence.defaultEquality();
    }

    <E2 extends E> MutableQHashObjSetGO<E2> uninitializedMutableSet() {
        return new MutableQHashObjSet<E2>();
    }
    <E2 extends E> UpdatableQHashObjSetGO<E2> uninitializedUpdatableSet() {
        return new UpdatableQHashObjSet<E2>();
    }
    <E2 extends E> ImmutableQHashObjSetGO<E2> uninitializedImmutableSet() {
        return new ImmutableQHashObjSet<E2>();
    }

    @Override
    @Nonnull
    public <E2 extends E> MutableQHashObjSetGO<E2> newMutableSet(int expectedSize) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.init(configWrapper, expectedSize);
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSet(int expectedSize) {
        UpdatableQHashObjSetGO<E2> set = uninitializedUpdatableSet();
        set.init(configWrapper, expectedSize);
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSet(Iterable<? extends E2> elements,
            int expectedSize) {
        if (elements instanceof ObjCollection) {
            int size;
            if (elements instanceof ObjSet) {
                ObjSet elemSet = (ObjSet) elements;
                if (elements instanceof SeparateKVObjQHash) {
                    SeparateKVObjQHash hash = (SeparateKVObjQHash) elements;
                    if (hash.hashConfig().equals(hashConf) &&
                            elemSet.equivalence().equals(getEquivalence())) {
                        UpdatableQHashObjSetGO<E2> set = uninitializedUpdatableSet();
                        set.copy(hash);
                        return set;
                    }
                }
                if (elemSet.equivalence().equals(getEquivalence())) {
                    size = elemSet.size();
                } else {
                    size = expectedSize;
                }
            } else {
                size = expectedSize;
            }
            UpdatableQHashObjSetGO<E2> set = newUpdatableSet(size);
            // noinspection unchecked
            set.addAll((Collection<? extends E2>) elements);
            return set;
        } else {
            int size = getEquivalence() == null && elements instanceof Set ?
                    ((Set) elements).size() :
                    expectedSize;
            UpdatableQHashObjSetGO<E2> set = newUpdatableSet(size);
            for (E2 e : elements) {
                set.add(e);
            }
            return set;
        }
    }
}

