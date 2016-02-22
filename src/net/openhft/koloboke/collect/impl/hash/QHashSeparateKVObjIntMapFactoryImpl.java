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
import net.openhft.koloboke.collect.map.hash.*;

import javax.annotation.Nonnull;


public final class QHashSeparateKVObjIntMapFactoryImpl<K>
        extends QHashSeparateKVObjIntMapFactoryGO<K> {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public QHashSeparateKVObjIntMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , false
            );
    }

    

    

    

    QHashSeparateKVObjIntMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
        super(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }

    @Override
    HashObjIntMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
        return new QHashSeparateKVObjIntMapFactoryImpl<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }

    @Override
    HashObjIntMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
        return new QHashSeparateKVObjIntMapFactoryImpl<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }
    @Override
    HashObjIntMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
        return new LHashSeparateKVObjIntMapFactoryImpl<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }

    @Override
    @Nonnull
    public HashObjIntMapFactory<K> withKeyEquivalence(
            @Nonnull Equivalence<? super K> keyEquivalence) {
        if (keyEquivalence.equals(Equivalence.defaultEquality())) {
            // noinspection unchecked
            return (HashObjIntMapFactory<K>) this;
        }
        return new WithCustomKeyEquivalence<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(),
                (Equivalence<K>) keyEquivalence);
    }

    @Override
    @Nonnull
    public HashObjIntMapFactory<K> withDefaultValue(int defaultValue) {
        if (defaultValue == 0)
            return this;
        return new WithCustomDefaultValue<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), defaultValue);
    }

    static class WithCustomKeyEquivalence<K>
            extends QHashSeparateKVObjIntMapFactoryGO<K> {

        private final Equivalence<K> keyEquivalence;

        WithCustomKeyEquivalence(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            , Equivalence<K> keyEquivalence) {
            super(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
            this.keyEquivalence = keyEquivalence;
        }

        @Override
        @Nonnull
        public Equivalence<K> getKeyEquivalence() {
            return keyEquivalence;
        }

        @Override
        <K2 extends K> MutableQHashSeparateKVObjIntMapGO<K2>
        uninitializedMutableMap() {
            MutableQHashSeparateKVObjIntMap.WithCustomKeyEquivalence<K2> map =
                    new MutableQHashSeparateKVObjIntMap.WithCustomKeyEquivalence<K2>();
            map.keyEquivalence = keyEquivalence;
            return map;
        }
        @Override
        <K2 extends K> UpdatableQHashSeparateKVObjIntMapGO<K2>
        uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVObjIntMap.WithCustomKeyEquivalence<K2> map =
                    new UpdatableQHashSeparateKVObjIntMap.WithCustomKeyEquivalence<K2>();
            map.keyEquivalence = keyEquivalence;
            return map;
        }
        @Override
        <K2 extends K> ImmutableQHashSeparateKVObjIntMapGO<K2>
        uninitializedImmutableMap() {
            ImmutableQHashSeparateKVObjIntMap.WithCustomKeyEquivalence<K2> map =
                    new ImmutableQHashSeparateKVObjIntMap.WithCustomKeyEquivalence<K2>();
            map.keyEquivalence = keyEquivalence;
            return map;
        }

        @Override
        @Nonnull
        public HashObjIntMapFactory<K> withKeyEquivalence(
                @Nonnull Equivalence<? super K> keyEquivalence) {
            if (keyEquivalence.equals(Equivalence.defaultEquality()))
                return new QHashSeparateKVObjIntMapFactoryImpl<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed());
            if (keyEquivalence.equals(this.keyEquivalence)) {
                // noinspection unchecked
                return (HashObjIntMapFactory<K>) this;
            }
            return new WithCustomKeyEquivalence<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(),
                    (Equivalence<K>) keyEquivalence);
        }

        @Override
        @Nonnull
        public HashObjIntMapFactory<K> withDefaultValue(int defaultValue) {
            if (defaultValue == 0)
                return this;
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(
                    getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), keyEquivalence, defaultValue);
        }

        @Override
        HashObjIntMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new WithCustomKeyEquivalence<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            , keyEquivalence);
        }

        @Override
        HashObjIntMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new QHashSeparateKVObjIntMapFactoryImpl.WithCustomKeyEquivalence<K>(
                    hashConf, defaultExpectedSize, isNullKeyAllowed
            , keyEquivalence);
        }
        @Override
        HashObjIntMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new LHashSeparateKVObjIntMapFactoryImpl.WithCustomKeyEquivalence<K>(
                    hashConf, defaultExpectedSize, isNullKeyAllowed
            , keyEquivalence);
        }
    }

    static final class WithCustomDefaultValue<K>
            extends QHashSeparateKVObjIntMapFactoryGO<K> {
        private final int defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            , int defaultValue) {
            super(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
            this.defaultValue = defaultValue;
        }

        @Override
        public int getDefaultValue() {
            return defaultValue;
        }

        @Override
        <K2 extends K>
     MutableQHashSeparateKVObjIntMapGO<K2> uninitializedMutableMap() {
            MutableQHashSeparateKVObjIntMap.WithCustomDefaultValue<K2> map =
                    new MutableQHashSeparateKVObjIntMap.WithCustomDefaultValue<K2>();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
        <K2 extends K>
     UpdatableQHashSeparateKVObjIntMapGO<K2> uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVObjIntMap.WithCustomDefaultValue<K2> map =
                    new UpdatableQHashSeparateKVObjIntMap.WithCustomDefaultValue<K2>();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
        <K2 extends K>
     ImmutableQHashSeparateKVObjIntMapGO<K2> uninitializedImmutableMap() {
            ImmutableQHashSeparateKVObjIntMap.WithCustomDefaultValue<K2> map =
                    new ImmutableQHashSeparateKVObjIntMap.WithCustomDefaultValue<K2>();
            map.defaultValue = defaultValue;
            return map;
        }

        @Override
        @Nonnull
        public HashObjIntMapFactory<K> withKeyEquivalence(
                @Nonnull Equivalence<? super K> keyEquivalence) {
            if (keyEquivalence.equals(Equivalence.defaultEquality())) {
                // noinspection unchecked
                return (HashObjIntMapFactory<K>) this;
            }
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(),
                    (Equivalence<K>) keyEquivalence, defaultValue);
        }

        @Override
        @Nonnull
        public HashObjIntMapFactory<K> withDefaultValue(int defaultValue) {
            if (defaultValue == 0)
                return new QHashSeparateKVObjIntMapFactoryImpl<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), defaultValue);
        }

        @Override
        HashObjIntMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new WithCustomDefaultValue<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            , defaultValue);
        }

        @Override
        HashObjIntMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new QHashSeparateKVObjIntMapFactoryImpl.WithCustomDefaultValue<K>(
                    hashConf, defaultExpectedSize, isNullKeyAllowed
            , defaultValue);
        }
        @Override
        HashObjIntMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new LHashSeparateKVObjIntMapFactoryImpl.WithCustomDefaultValue<K>(
                    hashConf, defaultExpectedSize, isNullKeyAllowed
            , defaultValue);
        }
    }

    static final class WithCustomKeyEquivalenceAndDefaultValue<K>
            extends QHashSeparateKVObjIntMapFactoryGO<K> {
        private final Equivalence<K> keyEquivalence;
        private final int defaultValue;

        WithCustomKeyEquivalenceAndDefaultValue(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ,
                Equivalence<K> keyEquivalence, int defaultValue) {
            super(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
            this.keyEquivalence = keyEquivalence;
            this.defaultValue = defaultValue;
        }

        @Override
        @Nonnull
        public Equivalence<K> getKeyEquivalence() {
            return keyEquivalence;
        }

        @Override
        public int getDefaultValue() {
            return defaultValue;
        }

        @Override
        <K2 extends K> MutableQHashSeparateKVObjIntMapGO<K2> uninitializedMutableMap() {
            MutableQHashSeparateKVObjIntMap.WithCustomKeyEquivalenceAndDefaultValue<K2> map =
                    new MutableQHashSeparateKVObjIntMap
                            .WithCustomKeyEquivalenceAndDefaultValue<K2>();
            map.keyEquivalence = keyEquivalence;
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
        <K2 extends K> UpdatableQHashSeparateKVObjIntMapGO<K2> uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVObjIntMap.WithCustomKeyEquivalenceAndDefaultValue<K2> map =
                    new UpdatableQHashSeparateKVObjIntMap
                            .WithCustomKeyEquivalenceAndDefaultValue<K2>();
            map.keyEquivalence = keyEquivalence;
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
        <K2 extends K> ImmutableQHashSeparateKVObjIntMapGO<K2> uninitializedImmutableMap() {
            ImmutableQHashSeparateKVObjIntMap.WithCustomKeyEquivalenceAndDefaultValue<K2> map =
                    new ImmutableQHashSeparateKVObjIntMap
                            .WithCustomKeyEquivalenceAndDefaultValue<K2>();
            map.keyEquivalence = keyEquivalence;
            map.defaultValue = defaultValue;
            return map;
        }

        @Override
        @Nonnull
        public HashObjIntMapFactory<K> withKeyEquivalence(
                @Nonnull Equivalence<? super K> keyEquivalence) {
            if (keyEquivalence.equals(Equivalence.defaultEquality()))
                return new WithCustomDefaultValue<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), defaultValue);
            if (keyEquivalence.equals(this.keyEquivalence)) {
                // noinspection unchecked
                return (HashObjIntMapFactory<K>) this;
            }
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(
                    getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), (Equivalence<K>) keyEquivalence, defaultValue);
        }

        @Override
        @Nonnull
        public HashObjIntMapFactory<K> withDefaultValue(int defaultValue) {
            if (defaultValue == 0)
                return new WithCustomKeyEquivalence<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), keyEquivalence);
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(
                    getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), keyEquivalence, defaultValue);
        }

        @Override
        HashObjIntMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            ,
                    keyEquivalence, defaultValue);
        }

        @Override
        HashObjIntMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new QHashSeparateKVObjIntMapFactoryImpl
                        .WithCustomKeyEquivalenceAndDefaultValue<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            ,
                    keyEquivalence, defaultValue);
        }
        @Override
        HashObjIntMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new LHashSeparateKVObjIntMapFactoryImpl
                        .WithCustomKeyEquivalenceAndDefaultValue<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            ,
                    keyEquivalence, defaultValue);
        }
    }
}

