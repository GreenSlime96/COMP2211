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


public final class QHashSeparateKVObjFloatMapFactoryImpl<K>
        extends QHashSeparateKVObjFloatMapFactoryGO<K> {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public QHashSeparateKVObjFloatMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , false
            );
    }

    

    

    

    QHashSeparateKVObjFloatMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
        super(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }

    @Override
    HashObjFloatMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
        return new QHashSeparateKVObjFloatMapFactoryImpl<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }

    @Override
    HashObjFloatMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
        return new QHashSeparateKVObjFloatMapFactoryImpl<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }
    @Override
    HashObjFloatMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
        return new LHashSeparateKVObjFloatMapFactoryImpl<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }

    @Override
    @Nonnull
    public HashObjFloatMapFactory<K> withKeyEquivalence(
            @Nonnull Equivalence<? super K> keyEquivalence) {
        if (keyEquivalence.equals(Equivalence.defaultEquality())) {
            // noinspection unchecked
            return (HashObjFloatMapFactory<K>) this;
        }
        return new WithCustomKeyEquivalence<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(),
                (Equivalence<K>) keyEquivalence);
    }

    @Override
    @Nonnull
    public HashObjFloatMapFactory<K> withDefaultValue(float defaultValue) {
        if (defaultValue == 0.0f)
            return this;
        return new WithCustomDefaultValue<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), defaultValue);
    }

    static class WithCustomKeyEquivalence<K>
            extends QHashSeparateKVObjFloatMapFactoryGO<K> {

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
        <K2 extends K> MutableQHashSeparateKVObjFloatMapGO<K2>
        uninitializedMutableMap() {
            MutableQHashSeparateKVObjFloatMap.WithCustomKeyEquivalence<K2> map =
                    new MutableQHashSeparateKVObjFloatMap.WithCustomKeyEquivalence<K2>();
            map.keyEquivalence = keyEquivalence;
            return map;
        }
        @Override
        <K2 extends K> UpdatableQHashSeparateKVObjFloatMapGO<K2>
        uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVObjFloatMap.WithCustomKeyEquivalence<K2> map =
                    new UpdatableQHashSeparateKVObjFloatMap.WithCustomKeyEquivalence<K2>();
            map.keyEquivalence = keyEquivalence;
            return map;
        }
        @Override
        <K2 extends K> ImmutableQHashSeparateKVObjFloatMapGO<K2>
        uninitializedImmutableMap() {
            ImmutableQHashSeparateKVObjFloatMap.WithCustomKeyEquivalence<K2> map =
                    new ImmutableQHashSeparateKVObjFloatMap.WithCustomKeyEquivalence<K2>();
            map.keyEquivalence = keyEquivalence;
            return map;
        }

        @Override
        @Nonnull
        public HashObjFloatMapFactory<K> withKeyEquivalence(
                @Nonnull Equivalence<? super K> keyEquivalence) {
            if (keyEquivalence.equals(Equivalence.defaultEquality()))
                return new QHashSeparateKVObjFloatMapFactoryImpl<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed());
            if (keyEquivalence.equals(this.keyEquivalence)) {
                // noinspection unchecked
                return (HashObjFloatMapFactory<K>) this;
            }
            return new WithCustomKeyEquivalence<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(),
                    (Equivalence<K>) keyEquivalence);
        }

        @Override
        @Nonnull
        public HashObjFloatMapFactory<K> withDefaultValue(float defaultValue) {
            if (defaultValue == 0.0f)
                return this;
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(
                    getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), keyEquivalence, defaultValue);
        }

        @Override
        HashObjFloatMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new WithCustomKeyEquivalence<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            , keyEquivalence);
        }

        @Override
        HashObjFloatMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new QHashSeparateKVObjFloatMapFactoryImpl.WithCustomKeyEquivalence<K>(
                    hashConf, defaultExpectedSize, isNullKeyAllowed
            , keyEquivalence);
        }
        @Override
        HashObjFloatMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new LHashSeparateKVObjFloatMapFactoryImpl.WithCustomKeyEquivalence<K>(
                    hashConf, defaultExpectedSize, isNullKeyAllowed
            , keyEquivalence);
        }
    }

    static final class WithCustomDefaultValue<K>
            extends QHashSeparateKVObjFloatMapFactoryGO<K> {
        private final float defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            , float defaultValue) {
            super(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
            this.defaultValue = defaultValue;
        }

        @Override
        public float getDefaultValue() {
            return defaultValue;
        }

        @Override
        <K2 extends K>
     MutableQHashSeparateKVObjFloatMapGO<K2> uninitializedMutableMap() {
            MutableQHashSeparateKVObjFloatMap.WithCustomDefaultValue<K2> map =
                    new MutableQHashSeparateKVObjFloatMap.WithCustomDefaultValue<K2>();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
        <K2 extends K>
     UpdatableQHashSeparateKVObjFloatMapGO<K2> uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVObjFloatMap.WithCustomDefaultValue<K2> map =
                    new UpdatableQHashSeparateKVObjFloatMap.WithCustomDefaultValue<K2>();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
        <K2 extends K>
     ImmutableQHashSeparateKVObjFloatMapGO<K2> uninitializedImmutableMap() {
            ImmutableQHashSeparateKVObjFloatMap.WithCustomDefaultValue<K2> map =
                    new ImmutableQHashSeparateKVObjFloatMap.WithCustomDefaultValue<K2>();
            map.defaultValue = defaultValue;
            return map;
        }

        @Override
        @Nonnull
        public HashObjFloatMapFactory<K> withKeyEquivalence(
                @Nonnull Equivalence<? super K> keyEquivalence) {
            if (keyEquivalence.equals(Equivalence.defaultEquality())) {
                // noinspection unchecked
                return (HashObjFloatMapFactory<K>) this;
            }
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(),
                    (Equivalence<K>) keyEquivalence, defaultValue);
        }

        @Override
        @Nonnull
        public HashObjFloatMapFactory<K> withDefaultValue(float defaultValue) {
            if (defaultValue == 0.0f)
                return new QHashSeparateKVObjFloatMapFactoryImpl<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), defaultValue);
        }

        @Override
        HashObjFloatMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new WithCustomDefaultValue<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            , defaultValue);
        }

        @Override
        HashObjFloatMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new QHashSeparateKVObjFloatMapFactoryImpl.WithCustomDefaultValue<K>(
                    hashConf, defaultExpectedSize, isNullKeyAllowed
            , defaultValue);
        }
        @Override
        HashObjFloatMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new LHashSeparateKVObjFloatMapFactoryImpl.WithCustomDefaultValue<K>(
                    hashConf, defaultExpectedSize, isNullKeyAllowed
            , defaultValue);
        }
    }

    static final class WithCustomKeyEquivalenceAndDefaultValue<K>
            extends QHashSeparateKVObjFloatMapFactoryGO<K> {
        private final Equivalence<K> keyEquivalence;
        private final float defaultValue;

        WithCustomKeyEquivalenceAndDefaultValue(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ,
                Equivalence<K> keyEquivalence, float defaultValue) {
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
        public float getDefaultValue() {
            return defaultValue;
        }

        @Override
        <K2 extends K> MutableQHashSeparateKVObjFloatMapGO<K2> uninitializedMutableMap() {
            MutableQHashSeparateKVObjFloatMap.WithCustomKeyEquivalenceAndDefaultValue<K2> map =
                    new MutableQHashSeparateKVObjFloatMap
                            .WithCustomKeyEquivalenceAndDefaultValue<K2>();
            map.keyEquivalence = keyEquivalence;
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
        <K2 extends K> UpdatableQHashSeparateKVObjFloatMapGO<K2> uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVObjFloatMap.WithCustomKeyEquivalenceAndDefaultValue<K2> map =
                    new UpdatableQHashSeparateKVObjFloatMap
                            .WithCustomKeyEquivalenceAndDefaultValue<K2>();
            map.keyEquivalence = keyEquivalence;
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
        <K2 extends K> ImmutableQHashSeparateKVObjFloatMapGO<K2> uninitializedImmutableMap() {
            ImmutableQHashSeparateKVObjFloatMap.WithCustomKeyEquivalenceAndDefaultValue<K2> map =
                    new ImmutableQHashSeparateKVObjFloatMap
                            .WithCustomKeyEquivalenceAndDefaultValue<K2>();
            map.keyEquivalence = keyEquivalence;
            map.defaultValue = defaultValue;
            return map;
        }

        @Override
        @Nonnull
        public HashObjFloatMapFactory<K> withKeyEquivalence(
                @Nonnull Equivalence<? super K> keyEquivalence) {
            if (keyEquivalence.equals(Equivalence.defaultEquality()))
                return new WithCustomDefaultValue<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), defaultValue);
            if (keyEquivalence.equals(this.keyEquivalence)) {
                // noinspection unchecked
                return (HashObjFloatMapFactory<K>) this;
            }
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(
                    getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), (Equivalence<K>) keyEquivalence, defaultValue);
        }

        @Override
        @Nonnull
        public HashObjFloatMapFactory<K> withDefaultValue(float defaultValue) {
            if (defaultValue == 0.0f)
                return new WithCustomKeyEquivalence<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), keyEquivalence);
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(
                    getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), keyEquivalence, defaultValue);
        }

        @Override
        HashObjFloatMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            ,
                    keyEquivalence, defaultValue);
        }

        @Override
        HashObjFloatMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new QHashSeparateKVObjFloatMapFactoryImpl
                        .WithCustomKeyEquivalenceAndDefaultValue<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            ,
                    keyEquivalence, defaultValue);
        }
        @Override
        HashObjFloatMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new LHashSeparateKVObjFloatMapFactoryImpl
                        .WithCustomKeyEquivalenceAndDefaultValue<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            ,
                    keyEquivalence, defaultValue);
        }
    }
}

