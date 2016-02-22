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


public final class LHashSeparateKVCharDoubleMapFactoryImpl
        extends LHashSeparateKVCharDoubleMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public LHashSeparateKVCharDoubleMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , Character.MIN_VALUE, Character.MAX_VALUE);
    }

    

    

    

    LHashSeparateKVCharDoubleMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashCharDoubleMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
        return new LHashSeparateKVCharDoubleMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashCharDoubleMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
        return new QHashSeparateKVCharDoubleMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }
    @Override
    HashCharDoubleMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
        return new LHashSeparateKVCharDoubleMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }


    @Override
    @Nonnull
    public HashCharDoubleMapFactory withDefaultValue(double defaultValue) {
        if (defaultValue == 0.0)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
    }


    static final class WithCustomDefaultValue
            extends LHashSeparateKVCharDoubleMapFactoryGO {
        private final double defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, char lower, char upper, double defaultValue) {
            super(hashConf, defaultExpectedSize, lower, upper);
            this.defaultValue = defaultValue;
        }

        @Override
        public double getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableLHashSeparateKVCharDoubleMapGO uninitializedMutableMap() {
            MutableLHashSeparateKVCharDoubleMap.WithCustomDefaultValue map =
                    new MutableLHashSeparateKVCharDoubleMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableLHashSeparateKVCharDoubleMapGO uninitializedUpdatableMap() {
            UpdatableLHashSeparateKVCharDoubleMap.WithCustomDefaultValue map =
                    new UpdatableLHashSeparateKVCharDoubleMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableLHashSeparateKVCharDoubleMapGO uninitializedImmutableMap() {
            ImmutableLHashSeparateKVCharDoubleMap.WithCustomDefaultValue map =
                    new ImmutableLHashSeparateKVCharDoubleMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashCharDoubleMapFactory withDefaultValue(double defaultValue) {
            if (defaultValue == 0.0)
                return new LHashSeparateKVCharDoubleMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
        }

        @Override
        HashCharDoubleMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }

        @Override
        HashCharDoubleMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
            return new QHashSeparateKVCharDoubleMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
        @Override
        HashCharDoubleMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
            return new LHashSeparateKVCharDoubleMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
    }

}

