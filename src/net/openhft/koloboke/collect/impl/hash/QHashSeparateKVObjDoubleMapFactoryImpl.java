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


public final class QHashSeparateKVObjDoubleMapFactoryImpl<K>
        extends QHashSeparateKVObjDoubleMapFactoryGO<K> {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public QHashSeparateKVObjDoubleMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , false
            );
    }

    

    

    

    QHashSeparateKVObjDoubleMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
        super(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }

    @Override
    HashObjDoubleMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
        return new QHashSeparateKVObjDoubleMapFactoryImpl<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }

    @Override
    HashObjDoubleMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
        return new QHashSeparateKVObjDoubleMapFactoryImpl<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }
    @Override
    HashObjDoubleMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
        return new LHashSeparateKVObjDoubleMapFactoryImpl<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }

    @Override
    @Nonnull
    public HashObjDoubleMapFactory<K> withKeyEquivalence(
            @Nonnull Equivalence<? super K> keyEquivalence) {
        if (keyEquivalence.equals(Equivalence.defaultEquality())) {
            // noinspection unchecked
            return (HashObjDoubleMapFactory<K>) this;
        }
        return new WithCustomKeyEquivalence<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(),
                (Equivalence<K>) keyEquivalence);
    }

    @Override
    @Nonnull
    public HashObjDoubleMapFactory<K> withDefaultValue(double defaultValue) {
        if (defaultValue == 0.0)
            return this;
        return new WithCustomDefaultValue<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), defaultValue);
    }

    static class WithCustomKeyEquivalence<K>
            extends QHashSeparateKVObjDoubleMapFactoryGO<K> {

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
        <K2 extends K> MutableQHashSeparateKVObjDoubleMapGO<K2>
        uninitializedMutableMap() {
            MutableQHashSeparateKVObjDoubleMap.WithCustomKeyEquivalence<K2> map =
                    new MutableQHashSeparateKVObjDoubleMap.WithCustomKeyEquivalence<K2>();
            map.keyEquivalence = keyEquivalence;
            return map;
        }
        @Override
        <K2 extends K> UpdatableQHashSeparateKVObjDoubleMapGO<K2>
        uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVObjDoubleMap.WithCustomKeyEquivalence<K2> map =
                    new UpdatableQHashSeparateKVObjDoubleMap.WithCustomKeyEquivalence<K2>();
            map.keyEquivalence = keyEquivalence;
            return map;
        }
        @Override
        <K2 extends K> ImmutableQHashSeparateKVObjDoubleMapGO<K2>
        uninitializedImmutableMap() {
            ImmutableQHashSeparateKVObjDoubleMap.WithCustomKeyEquivalence<K2> map =
                    new ImmutableQHashSeparateKVObjDoubleMap.WithCustomKeyEquivalence<K2>();
            map.keyEquivalence = keyEquivalence;
            return map;
        }

        @Override
        @Nonnull
        public HashObjDoubleMapFactory<K> withKeyEquivalence(
                @Nonnull Equivalence<? super K> keyEquivalence) {
            if (keyEquivalence.equals(Equivalence.defaultEquality()))
                return new QHashSeparateKVObjDoubleMapFactoryImpl<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed());
            if (keyEquivalence.equals(this.keyEquivalence)) {
                // noinspection unchecked
                return (HashObjDoubleMapFactory<K>) this;
            }
            return new WithCustomKeyEquivalence<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(),
                    (Equivalence<K>) keyEquivalence);
        }

        @Override
        @Nonnull
        public HashObjDoubleMapFactory<K> withDefaultValue(double defaultValue) {
            if (defaultValue == 0.0)
                return this;
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(
                    getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), keyEquivalence, defaultValue);
        }

        @Override
        HashObjDoubleMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new WithCustomKeyEquivalence<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            , keyEquivalence);
        }

        @Override
        HashObjDoubleMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new QHashSeparateKVObjDoubleMapFactoryImpl.WithCustomKeyEquivalence<K>(
                    hashConf, defaultExpectedSize, isNullKeyAllowed
            , keyEquivalence);
        }
        @Override
        HashObjDoubleMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new LHashSeparateKVObjDoubleMapFactoryImpl.WithCustomKeyEquivalence<K>(
                    hashConf, defaultExpectedSize, isNullKeyAllowed
            , keyEquivalence);
        }
    }

    static final class WithCustomDefaultValue<K>
            extends QHashSeparateKVObjDoubleMapFactoryGO<K> {
        private final double defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            , double defaultValue) {
            super(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
            this.defaultValue = defaultValue;
        }

        @Override
        public double getDefaultValue() {
            return defaultValue;
        }

        @Override
        <K2 extends K>
     MutableQHashSeparateKVObjDoubleMapGO<K2> uninitializedMutableMap() {
            MutableQHashSeparateKVObjDoubleMap.WithCustomDefaultValue<K2> map =
                    new MutableQHashSeparateKVObjDoubleMap.WithCustomDefaultValue<K2>();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
        <K2 extends K>
     UpdatableQHashSeparateKVObjDoubleMapGO<K2> uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVObjDoubleMap.WithCustomDefaultValue<K2> map =
                    new UpdatableQHashSeparateKVObjDoubleMap.WithCustomDefaultValue<K2>();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
        <K2 extends K>
     ImmutableQHashSeparateKVObjDoubleMapGO<K2> uninitializedImmutableMap() {
            ImmutableQHashSeparateKVObjDoubleMap.WithCustomDefaultValue<K2> map =
                    new ImmutableQHashSeparateKVObjDoubleMap.WithCustomDefaultValue<K2>();
            map.defaultValue = defaultValue;
            return map;
        }

        @Override
        @Nonnull
        public HashObjDoubleMapFactory<K> withKeyEquivalence(
                @Nonnull Equivalence<? super K> keyEquivalence) {
            if (keyEquivalence.equals(Equivalence.defaultEquality())) {
                // noinspection unchecked
                return (HashObjDoubleMapFactory<K>) this;
            }
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(),
                    (Equivalence<K>) keyEquivalence, defaultValue);
        }

        @Override
        @Nonnull
        public HashObjDoubleMapFactory<K> withDefaultValue(double defaultValue) {
            if (defaultValue == 0.0)
                return new QHashSeparateKVObjDoubleMapFactoryImpl<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), defaultValue);
        }

        @Override
        HashObjDoubleMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new WithCustomDefaultValue<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            , defaultValue);
        }

        @Override
        HashObjDoubleMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new QHashSeparateKVObjDoubleMapFactoryImpl.WithCustomDefaultValue<K>(
                    hashConf, defaultExpectedSize, isNullKeyAllowed
            , defaultValue);
        }
        @Override
        HashObjDoubleMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new LHashSeparateKVObjDoubleMapFactoryImpl.WithCustomDefaultValue<K>(
                    hashConf, defaultExpectedSize, isNullKeyAllowed
            , defaultValue);
        }
    }

    static final class WithCustomKeyEquivalenceAndDefaultValue<K>
            extends QHashSeparateKVObjDoubleMapFactoryGO<K> {
        private final Equivalence<K> keyEquivalence;
        private final double defaultValue;

        WithCustomKeyEquivalenceAndDefaultValue(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ,
                Equivalence<K> keyEquivalence, double defaultValue) {
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
        public double getDefaultValue() {
            return defaultValue;
        }

        @Override
        <K2 extends K> MutableQHashSeparateKVObjDoubleMapGO<K2> uninitializedMutableMap() {
            MutableQHashSeparateKVObjDoubleMap.WithCustomKeyEquivalenceAndDefaultValue<K2> map =
                    new MutableQHashSeparateKVObjDoubleMap
                            .WithCustomKeyEquivalenceAndDefaultValue<K2>();
            map.keyEquivalence = keyEquivalence;
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
        <K2 extends K> UpdatableQHashSeparateKVObjDoubleMapGO<K2> uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVObjDoubleMap.WithCustomKeyEquivalenceAndDefaultValue<K2> map =
                    new UpdatableQHashSeparateKVObjDoubleMap
                            .WithCustomKeyEquivalenceAndDefaultValue<K2>();
            map.keyEquivalence = keyEquivalence;
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
        <K2 extends K> ImmutableQHashSeparateKVObjDoubleMapGO<K2> uninitializedImmutableMap() {
            ImmutableQHashSeparateKVObjDoubleMap.WithCustomKeyEquivalenceAndDefaultValue<K2> map =
                    new ImmutableQHashSeparateKVObjDoubleMap
                            .WithCustomKeyEquivalenceAndDefaultValue<K2>();
            map.keyEquivalence = keyEquivalence;
            map.defaultValue = defaultValue;
            return map;
        }

        @Override
        @Nonnull
        public HashObjDoubleMapFactory<K> withKeyEquivalence(
                @Nonnull Equivalence<? super K> keyEquivalence) {
            if (keyEquivalence.equals(Equivalence.defaultEquality()))
                return new WithCustomDefaultValue<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), defaultValue);
            if (keyEquivalence.equals(this.keyEquivalence)) {
                // noinspection unchecked
                return (HashObjDoubleMapFactory<K>) this;
            }
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(
                    getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), (Equivalence<K>) keyEquivalence, defaultValue);
        }

        @Override
        @Nonnull
        public HashObjDoubleMapFactory<K> withDefaultValue(double defaultValue) {
            if (defaultValue == 0.0)
                return new WithCustomKeyEquivalence<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), keyEquivalence);
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(
                    getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), keyEquivalence, defaultValue);
        }

        @Override
        HashObjDoubleMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            ,
                    keyEquivalence, defaultValue);
        }

        @Override
        HashObjDoubleMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new QHashSeparateKVObjDoubleMapFactoryImpl
                        .WithCustomKeyEquivalenceAndDefaultValue<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            ,
                    keyEquivalence, defaultValue);
        }
        @Override
        HashObjDoubleMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new LHashSeparateKVObjDoubleMapFactoryImpl
                        .WithCustomKeyEquivalenceAndDefaultValue<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            ,
                    keyEquivalence, defaultValue);
        }
    }
}

