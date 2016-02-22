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


public final class LHashSeparateKVCharFloatMapFactoryImpl
        extends LHashSeparateKVCharFloatMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public LHashSeparateKVCharFloatMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , Character.MIN_VALUE, Character.MAX_VALUE);
    }

    

    

    

    LHashSeparateKVCharFloatMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashCharFloatMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
        return new LHashSeparateKVCharFloatMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashCharFloatMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
        return new QHashSeparateKVCharFloatMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }
    @Override
    HashCharFloatMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
        return new LHashSeparateKVCharFloatMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }


    @Override
    @Nonnull
    public HashCharFloatMapFactory withDefaultValue(float defaultValue) {
        if (defaultValue == 0.0f)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
    }


    static final class WithCustomDefaultValue
            extends LHashSeparateKVCharFloatMapFactoryGO {
        private final float defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, char lower, char upper, float defaultValue) {
            super(hashConf, defaultExpectedSize, lower, upper);
            this.defaultValue = defaultValue;
        }

        @Override
        public float getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableLHashSeparateKVCharFloatMapGO uninitializedMutableMap() {
            MutableLHashSeparateKVCharFloatMap.WithCustomDefaultValue map =
                    new MutableLHashSeparateKVCharFloatMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableLHashSeparateKVCharFloatMapGO uninitializedUpdatableMap() {
            UpdatableLHashSeparateKVCharFloatMap.WithCustomDefaultValue map =
                    new UpdatableLHashSeparateKVCharFloatMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableLHashSeparateKVCharFloatMapGO uninitializedImmutableMap() {
            ImmutableLHashSeparateKVCharFloatMap.WithCustomDefaultValue map =
                    new ImmutableLHashSeparateKVCharFloatMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashCharFloatMapFactory withDefaultValue(float defaultValue) {
            if (defaultValue == 0.0f)
                return new LHashSeparateKVCharFloatMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
        }

        @Override
        HashCharFloatMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }

        @Override
        HashCharFloatMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
            return new QHashSeparateKVCharFloatMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
        @Override
        HashCharFloatMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
            return new LHashSeparateKVCharFloatMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
    }

}
