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


public final class QHashSeparateKVIntDoubleMapFactoryImpl
        extends QHashSeparateKVIntDoubleMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public QHashSeparateKVIntDoubleMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    

    

    

    QHashSeparateKVIntDoubleMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashIntDoubleMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
        return new QHashSeparateKVIntDoubleMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashIntDoubleMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
        return new QHashSeparateKVIntDoubleMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }
    @Override
    HashIntDoubleMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
        return new LHashSeparateKVIntDoubleMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }


    @Override
    @Nonnull
    public HashIntDoubleMapFactory withDefaultValue(double defaultValue) {
        if (defaultValue == 0.0)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
    }


    static final class WithCustomDefaultValue
            extends QHashSeparateKVIntDoubleMapFactoryGO {
        private final double defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, int lower, int upper, double defaultValue) {
            super(hashConf, defaultExpectedSize, lower, upper);
            this.defaultValue = defaultValue;
        }

        @Override
        public double getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableQHashSeparateKVIntDoubleMapGO uninitializedMutableMap() {
            MutableQHashSeparateKVIntDoubleMap.WithCustomDefaultValue map =
                    new MutableQHashSeparateKVIntDoubleMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableQHashSeparateKVIntDoubleMapGO uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVIntDoubleMap.WithCustomDefaultValue map =
                    new UpdatableQHashSeparateKVIntDoubleMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableQHashSeparateKVIntDoubleMapGO uninitializedImmutableMap() {
            ImmutableQHashSeparateKVIntDoubleMap.WithCustomDefaultValue map =
                    new ImmutableQHashSeparateKVIntDoubleMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashIntDoubleMapFactory withDefaultValue(double defaultValue) {
            if (defaultValue == 0.0)
                return new QHashSeparateKVIntDoubleMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
        }

        @Override
        HashIntDoubleMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }

        @Override
        HashIntDoubleMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
            return new QHashSeparateKVIntDoubleMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
        @Override
        HashIntDoubleMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
            return new LHashSeparateKVIntDoubleMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
    }

}

