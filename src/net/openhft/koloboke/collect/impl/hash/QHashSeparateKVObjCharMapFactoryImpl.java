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


public final class QHashSeparateKVObjCharMapFactoryImpl<K>
        extends QHashSeparateKVObjCharMapFactoryGO<K> {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public QHashSeparateKVObjCharMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , false
            );
    }

    

    

    

    QHashSeparateKVObjCharMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
        super(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }

    @Override
    HashObjCharMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
        return new QHashSeparateKVObjCharMapFactoryImpl<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }

    @Override
    HashObjCharMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
        return new QHashSeparateKVObjCharMapFactoryImpl<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }
    @Override
    HashObjCharMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
        return new LHashSeparateKVObjCharMapFactoryImpl<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }

    @Override
    @Nonnull
    public HashObjCharMapFactory<K> withKeyEquivalence(
            @Nonnull Equivalence<? super K> keyEquivalence) {
        if (keyEquivalence.equals(Equivalence.defaultEquality())) {
            // noinspection unchecked
            return (HashObjCharMapFactory<K>) this;
        }
        return new WithCustomKeyEquivalence<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(),
                (Equivalence<K>) keyEquivalence);
    }

    @Override
    @Nonnull
    public HashObjCharMapFactory<K> withDefaultValue(char defaultValue) {
        if (defaultValue == (char) 0)
            return this;
        return new WithCustomDefaultValue<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), defaultValue);
    }

    static class WithCustomKeyEquivalence<K>
            extends QHashSeparateKVObjCharMapFactoryGO<K> {

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
        <K2 extends K> MutableQHashSeparateKVObjCharMapGO<K2>
        uninitializedMutableMap() {
            MutableQHashSeparateKVObjCharMap.WithCustomKeyEquivalence<K2> map =
                    new MutableQHashSeparateKVObjCharMap.WithCustomKeyEquivalence<K2>();
            map.keyEquivalence = keyEquivalence;
            return map;
        }
        @Override
        <K2 extends K> UpdatableQHashSeparateKVObjCharMapGO<K2>
        uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVObjCharMap.WithCustomKeyEquivalence<K2> map =
                    new UpdatableQHashSeparateKVObjCharMap.WithCustomKeyEquivalence<K2>();
            map.keyEquivalence = keyEquivalence;
            return map;
        }
        @Override
        <K2 extends K> ImmutableQHashSeparateKVObjCharMapGO<K2>
        uninitializedImmutableMap() {
            ImmutableQHashSeparateKVObjCharMap.WithCustomKeyEquivalence<K2> map =
                    new ImmutableQHashSeparateKVObjCharMap.WithCustomKeyEquivalence<K2>();
            map.keyEquivalence = keyEquivalence;
            return map;
        }

        @Override
        @Nonnull
        public HashObjCharMapFactory<K> withKeyEquivalence(
                @Nonnull Equivalence<? super K> keyEquivalence) {
            if (keyEquivalence.equals(Equivalence.defaultEquality()))
                return new QHashSeparateKVObjCharMapFactoryImpl<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed());
            if (keyEquivalence.equals(this.keyEquivalence)) {
                // noinspection unchecked
                return (HashObjCharMapFactory<K>) this;
            }
            return new WithCustomKeyEquivalence<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(),
                    (Equivalence<K>) keyEquivalence);
        }

        @Override
        @Nonnull
        public HashObjCharMapFactory<K> withDefaultValue(char defaultValue) {
            if (defaultValue == (char) 0)
                return this;
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(
                    getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), keyEquivalence, defaultValue);
        }

        @Override
        HashObjCharMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new WithCustomKeyEquivalence<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            , keyEquivalence);
        }

        @Override
        HashObjCharMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new QHashSeparateKVObjCharMapFactoryImpl.WithCustomKeyEquivalence<K>(
                    hashConf, defaultExpectedSize, isNullKeyAllowed
            , keyEquivalence);
        }
        @Override
        HashObjCharMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new LHashSeparateKVObjCharMapFactoryImpl.WithCustomKeyEquivalence<K>(
                    hashConf, defaultExpectedSize, isNullKeyAllowed
            , keyEquivalence);
        }
    }

    static final class WithCustomDefaultValue<K>
            extends QHashSeparateKVObjCharMapFactoryGO<K> {
        private final char defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            , char defaultValue) {
            super(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
            this.defaultValue = defaultValue;
        }

        @Override
        public char getDefaultValue() {
            return defaultValue;
        }

        @Override
        <K2 extends K>
     MutableQHashSeparateKVObjCharMapGO<K2> uninitializedMutableMap() {
            MutableQHashSeparateKVObjCharMap.WithCustomDefaultValue<K2> map =
                    new MutableQHashSeparateKVObjCharMap.WithCustomDefaultValue<K2>();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
        <K2 extends K>
     UpdatableQHashSeparateKVObjCharMapGO<K2> uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVObjCharMap.WithCustomDefaultValue<K2> map =
                    new UpdatableQHashSeparateKVObjCharMap.WithCustomDefaultValue<K2>();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
        <K2 extends K>
     ImmutableQHashSeparateKVObjCharMapGO<K2> uninitializedImmutableMap() {
            ImmutableQHashSeparateKVObjCharMap.WithCustomDefaultValue<K2> map =
                    new ImmutableQHashSeparateKVObjCharMap.WithCustomDefaultValue<K2>();
            map.defaultValue = defaultValue;
            return map;
        }

        @Override
        @Nonnull
        public HashObjCharMapFactory<K> withKeyEquivalence(
                @Nonnull Equivalence<? super K> keyEquivalence) {
            if (keyEquivalence.equals(Equivalence.defaultEquality())) {
                // noinspection unchecked
                return (HashObjCharMapFactory<K>) this;
            }
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(),
                    (Equivalence<K>) keyEquivalence, defaultValue);
        }

        @Override
        @Nonnull
        public HashObjCharMapFactory<K> withDefaultValue(char defaultValue) {
            if (defaultValue == (char) 0)
                return new QHashSeparateKVObjCharMapFactoryImpl<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), defaultValue);
        }

        @Override
        HashObjCharMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new WithCustomDefaultValue<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            , defaultValue);
        }

        @Override
        HashObjCharMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new QHashSeparateKVObjCharMapFactoryImpl.WithCustomDefaultValue<K>(
                    hashConf, defaultExpectedSize, isNullKeyAllowed
            , defaultValue);
        }
        @Override
        HashObjCharMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new LHashSeparateKVObjCharMapFactoryImpl.WithCustomDefaultValue<K>(
                    hashConf, defaultExpectedSize, isNullKeyAllowed
            , defaultValue);
        }
    }

    static final class WithCustomKeyEquivalenceAndDefaultValue<K>
            extends QHashSeparateKVObjCharMapFactoryGO<K> {
        private final Equivalence<K> keyEquivalence;
        private final char defaultValue;

        WithCustomKeyEquivalenceAndDefaultValue(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ,
                Equivalence<K> keyEquivalence, char defaultValue) {
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
        public char getDefaultValue() {
            return defaultValue;
        }

        @Override
        <K2 extends K> MutableQHashSeparateKVObjCharMapGO<K2> uninitializedMutableMap() {
            MutableQHashSeparateKVObjCharMap.WithCustomKeyEquivalenceAndDefaultValue<K2> map =
                    new MutableQHashSeparateKVObjCharMap
                            .WithCustomKeyEquivalenceAndDefaultValue<K2>();
            map.keyEquivalence = keyEquivalence;
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
        <K2 extends K> UpdatableQHashSeparateKVObjCharMapGO<K2> uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVObjCharMap.WithCustomKeyEquivalenceAndDefaultValue<K2> map =
                    new UpdatableQHashSeparateKVObjCharMap
                            .WithCustomKeyEquivalenceAndDefaultValue<K2>();
            map.keyEquivalence = keyEquivalence;
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
        <K2 extends K> ImmutableQHashSeparateKVObjCharMapGO<K2> uninitializedImmutableMap() {
            ImmutableQHashSeparateKVObjCharMap.WithCustomKeyEquivalenceAndDefaultValue<K2> map =
                    new ImmutableQHashSeparateKVObjCharMap
                            .WithCustomKeyEquivalenceAndDefaultValue<K2>();
            map.keyEquivalence = keyEquivalence;
            map.defaultValue = defaultValue;
            return map;
        }

        @Override
        @Nonnull
        public HashObjCharMapFactory<K> withKeyEquivalence(
                @Nonnull Equivalence<? super K> keyEquivalence) {
            if (keyEquivalence.equals(Equivalence.defaultEquality()))
                return new WithCustomDefaultValue<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), defaultValue);
            if (keyEquivalence.equals(this.keyEquivalence)) {
                // noinspection unchecked
                return (HashObjCharMapFactory<K>) this;
            }
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(
                    getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), (Equivalence<K>) keyEquivalence, defaultValue);
        }

        @Override
        @Nonnull
        public HashObjCharMapFactory<K> withDefaultValue(char defaultValue) {
            if (defaultValue == (char) 0)
                return new WithCustomKeyEquivalence<K>(getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), keyEquivalence);
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(
                    getHashConfig(), getDefaultExpectedSize()
        , isNullKeyAllowed(), keyEquivalence, defaultValue);
        }

        @Override
        HashObjCharMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            ,
                    keyEquivalence, defaultValue);
        }

        @Override
        HashObjCharMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new QHashSeparateKVObjCharMapFactoryImpl
                        .WithCustomKeyEquivalenceAndDefaultValue<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            ,
                    keyEquivalence, defaultValue);
        }
        @Override
        HashObjCharMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            ) {
            return new LHashSeparateKVObjCharMapFactoryImpl
                        .WithCustomKeyEquivalenceAndDefaultValue<K>(hashConf, defaultExpectedSize, isNullKeyAllowed
            ,
                    keyEquivalence, defaultValue);
        }
    }
}

