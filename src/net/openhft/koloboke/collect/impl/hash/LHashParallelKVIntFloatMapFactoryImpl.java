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


public final class LHashParallelKVIntFloatMapFactoryImpl
        extends LHashParallelKVIntFloatMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public LHashParallelKVIntFloatMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    

    

    

    LHashParallelKVIntFloatMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashIntFloatMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
        return new LHashParallelKVIntFloatMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashIntFloatMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
        return new QHashParallelKVIntFloatMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }
    @Override
    HashIntFloatMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
        return new LHashParallelKVIntFloatMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }


    @Override
    @Nonnull
    public HashIntFloatMapFactory withDefaultValue(float defaultValue) {
        if (defaultValue == 0.0f)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
    }


    static final class WithCustomDefaultValue
            extends LHashParallelKVIntFloatMapFactoryGO {
        private final float defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, int lower, int upper, float defaultValue) {
            super(hashConf, defaultExpectedSize, lower, upper);
            this.defaultValue = defaultValue;
        }

        @Override
        public float getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableLHashParallelKVIntFloatMapGO uninitializedMutableMap() {
            MutableLHashParallelKVIntFloatMap.WithCustomDefaultValue map =
                    new MutableLHashParallelKVIntFloatMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableLHashParallelKVIntFloatMapGO uninitializedUpdatableMap() {
            UpdatableLHashParallelKVIntFloatMap.WithCustomDefaultValue map =
                    new UpdatableLHashParallelKVIntFloatMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableLHashParallelKVIntFloatMapGO uninitializedImmutableMap() {
            ImmutableLHashParallelKVIntFloatMap.WithCustomDefaultValue map =
                    new ImmutableLHashParallelKVIntFloatMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashIntFloatMapFactory withDefaultValue(float defaultValue) {
            if (defaultValue == 0.0f)
                return new LHashParallelKVIntFloatMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
        }

        @Override
        HashIntFloatMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }

        @Override
        HashIntFloatMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
            return new QHashParallelKVIntFloatMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
        @Override
        HashIntFloatMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
            return new LHashParallelKVIntFloatMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
    }

}

