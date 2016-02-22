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


public final class QHashSeparateKVFloatDoubleMapFactoryImpl
        extends QHashSeparateKVFloatDoubleMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public QHashSeparateKVFloatDoubleMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            );
    }

    

    

    

    QHashSeparateKVFloatDoubleMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize) {
        super(hashConf, defaultExpectedSize);
    }

    @Override
    HashFloatDoubleMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new QHashSeparateKVFloatDoubleMapFactoryImpl(hashConf, defaultExpectedSize);
    }

    @Override
    HashFloatDoubleMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new QHashSeparateKVFloatDoubleMapFactoryImpl(hashConf, defaultExpectedSize);
    }
    @Override
    HashFloatDoubleMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new LHashSeparateKVFloatDoubleMapFactoryImpl(hashConf, defaultExpectedSize);
    }


    @Override
    @Nonnull
    public HashFloatDoubleMapFactory withDefaultValue(double defaultValue) {
        if (defaultValue == 0.0)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        , defaultValue);
    }


    static final class WithCustomDefaultValue
            extends QHashSeparateKVFloatDoubleMapFactoryGO {
        private final double defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, double defaultValue) {
            super(hashConf, defaultExpectedSize);
            this.defaultValue = defaultValue;
        }

        @Override
        public double getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableQHashSeparateKVFloatDoubleMapGO uninitializedMutableMap() {
            MutableQHashSeparateKVFloatDoubleMap.WithCustomDefaultValue map =
                    new MutableQHashSeparateKVFloatDoubleMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableQHashSeparateKVFloatDoubleMapGO uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVFloatDoubleMap.WithCustomDefaultValue map =
                    new UpdatableQHashSeparateKVFloatDoubleMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableQHashSeparateKVFloatDoubleMapGO uninitializedImmutableMap() {
            ImmutableQHashSeparateKVFloatDoubleMap.WithCustomDefaultValue map =
                    new ImmutableQHashSeparateKVFloatDoubleMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashFloatDoubleMapFactory withDefaultValue(double defaultValue) {
            if (defaultValue == 0.0)
                return new QHashSeparateKVFloatDoubleMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        );
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        , defaultValue);
        }

        @Override
        HashFloatDoubleMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, defaultValue);
        }

        @Override
        HashFloatDoubleMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new QHashSeparateKVFloatDoubleMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, defaultValue);
        }
        @Override
        HashFloatDoubleMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new LHashSeparateKVFloatDoubleMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, defaultValue);
        }
    }

}

