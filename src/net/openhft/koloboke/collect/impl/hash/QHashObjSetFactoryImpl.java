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

import net.openhft.koloboke.collect.Equivalence;
import net.openhft.koloboke.collect.hash.*;
import net.openhft.koloboke.collect.set.hash.HashObjSetFactory;

import javax.annotation.Nonnull;


public final class QHashObjSetFactoryImpl<E> extends QHashObjSetFactoryGO<E> {

    /** For ServiceLoader */
    public QHashObjSetFactoryImpl() {
        this(HashConfig.getDefault(), 10, false);
    }

    

    

    

    public QHashObjSetFactoryImpl(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed) {
        super(hashConf, defaultExpectedSize, isNullKeyAllowed);
    }

    @Override
    @Nonnull
    public HashObjSetFactory<E> withEquivalence(@Nonnull Equivalence<? super E> equivalence) {
        if (equivalence.equals(Equivalence.defaultEquality())) {
            // noinspection unchecked
            return (HashObjSetFactory<E>) this;
        }
        return new WithCustomEquivalence<E>(getHashConfig(), getDefaultExpectedSize(), isNullKeyAllowed(), (Equivalence<E>) equivalence);
    }

    @Override
    @Nonnull
    public HashObjSetFactory<E> withNullKeyAllowed(boolean nullAllowed) {
        if (nullAllowed == isNullKeyAllowed())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), nullAllowed);
    }

    @Override
    HashObjSetFactory<E> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed) {
        return new QHashObjSetFactoryImpl<E>(hashConf, defaultExpectedSize, isNullKeyAllowed);
    }

    @Override
    HashObjSetFactory<E> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed) {
        return new QHashObjSetFactoryImpl<E>(hashConf, defaultExpectedSize, isNullKeyAllowed);
    }
    @Override
    HashObjSetFactory<E> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed) {
        return new LHashObjSetFactoryImpl<E>(hashConf, defaultExpectedSize, isNullKeyAllowed);
    }

    static final class WithCustomEquivalence<E> extends QHashObjSetFactoryGO<E> {
        final Equivalence<E> equivalence;

        public WithCustomEquivalence(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed, Equivalence<E> equivalence) {
            super(hashConf, defaultExpectedSize, isNullKeyAllowed);
            this.equivalence = equivalence;
        }

        @Override
        @Nonnull
        public Equivalence<E> getEquivalence() {
            return equivalence;
        }

        @Override
        <E2 extends E> MutableQHashObjSetGO<E2> uninitializedMutableSet() {
            MutableQHashObjSet.WithCustomEquivalence<E2> set =
                    new MutableQHashObjSet.WithCustomEquivalence<E2>();
            set.equivalence = equivalence;
            return set;
        }
        @Override
        <E2 extends E> UpdatableQHashObjSetGO<E2> uninitializedUpdatableSet() {
            UpdatableQHashObjSet.WithCustomEquivalence<E2> set =
                    new UpdatableQHashObjSet.WithCustomEquivalence<E2>();
            set.equivalence = equivalence;
            return set;
        }
        @Override
        <E2 extends E> ImmutableQHashObjSetGO<E2> uninitializedImmutableSet() {
            ImmutableQHashObjSet.WithCustomEquivalence<E2> set =
                    new ImmutableQHashObjSet.WithCustomEquivalence<E2>();
            set.equivalence = equivalence;
            return set;
        }

        @Override
        @Nonnull
        public HashObjSetFactory<E> withEquivalence(@Nonnull Equivalence<? super E> equivalence) {
            if (equivalence.equals(Equivalence.defaultEquality()))
                return new QHashObjSetFactoryImpl<E>(getHashConfig(), getDefaultExpectedSize(), isNullKeyAllowed());
            if (this.equivalence.equals(equivalence)) {
                // noinspection unchecked
                return (HashObjSetFactory<E>) this;
            }
            return new WithCustomEquivalence<E>(getHashConfig(), getDefaultExpectedSize(), isNullKeyAllowed(), (Equivalence<E>) equivalence);
        }

        @Override
        @Nonnull
        public HashObjSetFactory<E> withNullKeyAllowed(boolean nullAllowed) {
            if (nullAllowed == isNullKeyAllowed())
                return this;
            return thisWith(getHashConfig(), getDefaultExpectedSize(), nullAllowed);
        }

        @Override
        HashObjSetFactory<E> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed) {
            return new WithCustomEquivalence<E>(hashConf, defaultExpectedSize, isNullKeyAllowed, equivalence);
        }

        @Override
        HashObjSetFactory<E> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed) {
            return new QHashObjSetFactoryImpl.WithCustomEquivalence<E>(
                hashConf, defaultExpectedSize, isNullKeyAllowed, equivalence);
        }
        @Override
        HashObjSetFactory<E> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed) {
            return new LHashObjSetFactoryImpl.WithCustomEquivalence<E>(
                hashConf, defaultExpectedSize, isNullKeyAllowed, equivalence);
        }
    }
}

