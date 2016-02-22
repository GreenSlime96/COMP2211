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


public final class QHashSeparateKVFloatCharMapFactoryImpl
        extends QHashSeparateKVFloatCharMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public QHashSeparateKVFloatCharMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            );
    }

    

    

    

    QHashSeparateKVFloatCharMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize) {
        super(hashConf, defaultExpectedSize);
    }

    @Override
    HashFloatCharMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new QHashSeparateKVFloatCharMapFactoryImpl(hashConf, defaultExpectedSize);
    }

    @Override
    HashFloatCharMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new QHashSeparateKVFloatCharMapFactoryImpl(hashConf, defaultExpectedSize);
    }
    @Override
    HashFloatCharMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new LHashSeparateKVFloatCharMapFactoryImpl(hashConf, defaultExpectedSize);
    }


    @Override
    @Nonnull
    public HashFloatCharMapFactory withDefaultValue(char defaultValue) {
        if (defaultValue == (char) 0)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        , defaultValue);
    }


    static final class WithCustomDefaultValue
            extends QHashSeparateKVFloatCharMapFactoryGO {
        private final char defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, char defaultValue) {
            super(hashConf, defaultExpectedSize);
            this.defaultValue = defaultValue;
        }

        @Override
        public char getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableQHashSeparateKVFloatCharMapGO uninitializedMutableMap() {
            MutableQHashSeparateKVFloatCharMap.WithCustomDefaultValue map =
                    new MutableQHashSeparateKVFloatCharMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableQHashSeparateKVFloatCharMapGO uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVFloatCharMap.WithCustomDefaultValue map =
                    new UpdatableQHashSeparateKVFloatCharMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableQHashSeparateKVFloatCharMapGO uninitializedImmutableMap() {
            ImmutableQHashSeparateKVFloatCharMap.WithCustomDefaultValue map =
                    new ImmutableQHashSeparateKVFloatCharMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashFloatCharMapFactory withDefaultValue(char defaultValue) {
            if (defaultValue == (char) 0)
                return new QHashSeparateKVFloatCharMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        );
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        , defaultValue);
        }

        @Override
        HashFloatCharMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, defaultValue);
        }

        @Override
        HashFloatCharMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new QHashSeparateKVFloatCharMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, defaultValue);
        }
        @Override
        HashFloatCharMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new LHashSeparateKVFloatCharMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, defaultValue);
        }
    }

}

