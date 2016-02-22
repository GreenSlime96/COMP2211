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


public final class QHashSeparateKVByteDoubleMapFactoryImpl
        extends QHashSeparateKVByteDoubleMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public QHashSeparateKVByteDoubleMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    

    

    

    QHashSeparateKVByteDoubleMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashByteDoubleMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
        return new QHashSeparateKVByteDoubleMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashByteDoubleMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
        return new QHashSeparateKVByteDoubleMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }
    @Override
    HashByteDoubleMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
        return new LHashSeparateKVByteDoubleMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }


    @Override
    @Nonnull
    public HashByteDoubleMapFactory withDefaultValue(double defaultValue) {
        if (defaultValue == 0.0)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
    }


    static final class WithCustomDefaultValue
            extends QHashSeparateKVByteDoubleMapFactoryGO {
        private final double defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper, double defaultValue) {
            super(hashConf, defaultExpectedSize, lower, upper);
            this.defaultValue = defaultValue;
        }

        @Override
        public double getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableQHashSeparateKVByteDoubleMapGO uninitializedMutableMap() {
            MutableQHashSeparateKVByteDoubleMap.WithCustomDefaultValue map =
                    new MutableQHashSeparateKVByteDoubleMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableQHashSeparateKVByteDoubleMapGO uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVByteDoubleMap.WithCustomDefaultValue map =
                    new UpdatableQHashSeparateKVByteDoubleMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableQHashSeparateKVByteDoubleMapGO uninitializedImmutableMap() {
            ImmutableQHashSeparateKVByteDoubleMap.WithCustomDefaultValue map =
                    new ImmutableQHashSeparateKVByteDoubleMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashByteDoubleMapFactory withDefaultValue(double defaultValue) {
            if (defaultValue == 0.0)
                return new QHashSeparateKVByteDoubleMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
        }

        @Override
        HashByteDoubleMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }

        @Override
        HashByteDoubleMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
            return new QHashSeparateKVByteDoubleMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
        @Override
        HashByteDoubleMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
            return new LHashSeparateKVByteDoubleMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
    }

}

