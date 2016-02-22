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


public final class LHashSeparateKVObjShortMapFactoryImpl<K>
        extends LHashSeparateKVObjShortMapFactoryGO<K> {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public LHashSeparateKVObjShortMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , false
            );
    }

    

    

    

    LHashSeparateKVObjShortMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
        super(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }

    @Override
    HashObjShortMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
        return new LHashSeparateKVObjShortMapFactoryImpl<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }

    @Override
    HashObjShortMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
        return new QHashSeparateKVObjShortMapFactoryImpl<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }
    @Override
    HashObjShortMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
        return new LHashSeparateKVObjShortMapFactoryImpl<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }

    @Override
    @Nonnull
    public HashObjShortMapFactory<K> withKeyEquivalence(
            @Nonnull Equivalence<? super K> keyEquivalence) {
        if (keyEquivalence.equals(Equivalence.defaultEquality())) {
            // noinspection unchecked
            return (HashObjShortMapFactory<K>) this;
        }
        return new WithCustomKeyEquivalence<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(),
                (Equivalence<K>) keyEquivalence);
    }

    @Override
    @Nonnull
    public HashObjShortMapFactory<K> withDefaultValue(short defaultValue) {
        if (defaultValue == (short) 0)
            return this;
        return new WithCustomDefaultValue<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), defaultValue);
    }

    static class WithCustomKeyEquivalence<K>
            extends LHashSeparateKVObjShortMapFactoryGO<K> {

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
        <K2 extends K> MutableLHashSeparateKVObjShortMapGO<K2>
        uninitializedMutableMap() {
            MutableLHashSeparateKVObjShortMap.WithCustomKeyEquivalence<K2> map =
                    new MutableLHashSeparateKVObjShortMap.WithCustomKeyEquivalence<K2>();
            map.keyEquivalence = keyEquivalence;
            return map;
        }
        @Override
        <K2 extends K> UpdatableLHashSeparateKVObjShortMapGO<K2>
        uninitializedUpdatableMap() {
            UpdatableLHashSeparateKVObjShortMap.WithCustomKeyEquivalence<K2> map =
                    new UpdatableLHashSeparateKVObjShortMap.WithCustomKeyEquivalence<K2>();
            map.keyEquivalence = keyEquivalence;
            return map;
        }
        @Override
        <K2 extends K> ImmutableLHashSeparateKVObjShortMapGO<K2>
        uninitializedImmutableMap() {
            ImmutableLHashSeparateKVObjShortMap.WithCustomKeyEquivalence<K2> map =
                    new ImmutableLHashSeparateKVObjShortMap.WithCustomKeyEquivalence<K2>();
            map.keyEquivalence = keyEquivalence;
            return map;
        }

        @Override
        @Nonnull
        public HashObjShortMapFactory<K> withKeyEquivalence(
                @Nonnull Equivalence<? super K> keyEquivalence) {
            if (keyEquivalence.equals(Equivalence.defaultEquality()))
                return new LHashSeparateKVObjShortMapFactoryImpl<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed());
            if (keyEquivalence.equals(this.keyEquivalence)) {
                // noinspection unchecked
                return (HashObjShortMapFactory<K>) this;
            }
            return new WithCustomKeyEquivalence<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(),
                    (Equivalence<K>) keyEquivalence);
        }

        @Override
        @Nonnull
        public HashObjShortMapFactory<K> withDefaultValue(short defaultValue) {
            if (defaultValue == (short) 0)
                return this;
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(
                    getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), keyEquivalence, defaultValue);
        }

        @Override
        HashObjShortMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new WithCustomKeyEquivalence<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            , keyEquivalence);
        }

        @Override
        HashObjShortMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new QHashSeparateKVObjShortMapFactoryImpl.WithCustomKeyEquivalence<K>(
                    hashConf, defaultExpectedSize, isNullKeyAllowed
            , keyEquivalence);
        }
        @Override
        HashObjShortMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new LHashSeparateKVObjShortMapFactoryImpl.WithCustomKeyEquivalence<K>(
                    hashConf, defaultExpectedSize, isNullKeyAllowed
            , keyEquivalence);
        }
    }

    static final class WithCustomDefaultValue<K>
            extends LHashSeparateKVObjShortMapFactoryGO<K> {
        private final short defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            , short defaultValue) {
            super(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
            this.defaultValue = defaultValue;
        }

        @Override
        public short getDefaultValue() {
            return defaultValue;
        }

        @Override
        <K2 extends K>
     MutableLHashSeparateKVObjShortMapGO<K2> uninitializedMutableMap() {
            MutableLHashSeparateKVObjShortMap.WithCustomDefaultValue<K2> map =
                    new MutableLHashSeparateKVObjShortMap.WithCustomDefaultValue<K2>();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
        <K2 extends K>
     UpdatableLHashSeparateKVObjShortMapGO<K2> uninitializedUpdatableMap() {
            UpdatableLHashSeparateKVObjShortMap.WithCustomDefaultValue<K2> map =
                    new UpdatableLHashSeparateKVObjShortMap.WithCustomDefaultValue<K2>();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
        <K2 extends K>
     ImmutableLHashSeparateKVObjShortMapGO<K2> uninitializedImmutableMap() {
            ImmutableLHashSeparateKVObjShortMap.WithCustomDefaultValue<K2> map =
                    new ImmutableLHashSeparateKVObjShortMap.WithCustomDefaultValue<K2>();
            map.defaultValue = defaultValue;
            return map;
        }

        @Override
        @Nonnull
        public HashObjShortMapFactory<K> withKeyEquivalence(
                @Nonnull Equivalence<? super K> keyEquivalence) {
            if (keyEquivalence.equals(Equivalence.defaultEquality())) {
                // noinspection unchecked
                return (HashObjShortMapFactory<K>) this;
            }
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(),
                    (Equivalence<K>) keyEquivalence, defaultValue);
        }

        @Override
        @Nonnull
        public HashObjShortMapFactory<K> withDefaultValue(short defaultValue) {
            if (defaultValue == (short) 0)
                return new LHashSeparateKVObjShortMapFactoryImpl<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), defaultValue);
        }

        @Override
        HashObjShortMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new WithCustomDefaultValue<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            , defaultValue);
        }

        @Override
        HashObjShortMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new QHashSeparateKVObjShortMapFactoryImpl.WithCustomDefaultValue<K>(
                    hashConf, defaultExpectedSize, isNullKeyAllowed
            , defaultValue);
        }
        @Override
        HashObjShortMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new LHashSeparateKVObjShortMapFactoryImpl.WithCustomDefaultValue<K>(
                    hashConf, defaultExpectedSize, isNullKeyAllowed
            , defaultValue);
        }
    }

    static final class WithCustomKeyEquivalenceAndDefaultValue<K>
            extends LHashSeparateKVObjShortMapFactoryGO<K> {
        private final Equivalence<K> keyEquivalence;
        private final short defaultValue;

        WithCustomKeyEquivalenceAndDefaultValue(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ,
                Equivalence<K> keyEquivalence, short defaultValue) {
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
        public short getDefaultValue() {
            return defaultValue;
        }

        @Override
        <K2 extends K> MutableLHashSeparateKVObjShortMapGO<K2> uninitializedMutableMap() {
            MutableLHashSeparateKVObjShortMap.WithCustomKeyEquivalenceAndDefaultValue<K2> map =
                    new MutableLHashSeparateKVObjShortMap
                            .WithCustomKeyEquivalenceAndDefaultValue<K2>();
            map.keyEquivalence = keyEquivalence;
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
        <K2 extends K> UpdatableLHashSeparateKVObjShortMapGO<K2> uninitializedUpdatableMap() {
            UpdatableLHashSeparateKVObjShortMap.WithCustomKeyEquivalenceAndDefaultValue<K2> map =
                    new UpdatableLHashSeparateKVObjShortMap
                            .WithCustomKeyEquivalenceAndDefaultValue<K2>();
            map.keyEquivalence = keyEquivalence;
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
        <K2 extends K> ImmutableLHashSeparateKVObjShortMapGO<K2> uninitializedImmutableMap() {
            ImmutableLHashSeparateKVObjShortMap.WithCustomKeyEquivalenceAndDefaultValue<K2> map =
                    new ImmutableLHashSeparateKVObjShortMap
                            .WithCustomKeyEquivalenceAndDefaultValue<K2>();
            map.keyEquivalence = keyEquivalence;
            map.defaultValue = defaultValue;
            return map;
        }

        @Override
        @Nonnull
        public HashObjShortMapFactory<K> withKeyEquivalence(
                @Nonnull Equivalence<? super K> keyEquivalence) {
            if (keyEquivalence.equals(Equivalence.defaultEquality()))
                return new WithCustomDefaultValue<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), defaultValue);
            if (keyEquivalence.equals(this.keyEquivalence)) {
                // noinspection unchecked
                return (HashObjShortMapFactory<K>) this;
            }
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(
                    getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), (Equivalence<K>) keyEquivalence, defaultValue);
        }

        @Override
        @Nonnull
        public HashObjShortMapFactory<K> withDefaultValue(short defaultValue) {
            if (defaultValue == (short) 0)
                return new WithCustomKeyEquivalence<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), keyEquivalence);
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(
                    getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), keyEquivalence, defaultValue);
        }

        @Override
        HashObjShortMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            ,
                    keyEquivalence, defaultValue);
        }

        @Override
        HashObjShortMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new QHashSeparateKVObjShortMapFactoryImpl
                        .WithCustomKeyEquivalenceAndDefaultValue<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            ,
                    keyEquivalence, defaultValue);
        }
        @Override
        HashObjShortMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new LHashSeparateKVObjShortMapFactoryImpl
                        .WithCustomKeyEquivalenceAndDefaultValue<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            ,
                    keyEquivalence, defaultValue);
        }
    }
}

