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


public final class LHashParallelKVIntIntMapFactoryImpl
        extends LHashParallelKVIntIntMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public LHashParallelKVIntIntMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    

    

    

    LHashParallelKVIntIntMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashIntIntMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
        return new LHashParallelKVIntIntMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashIntIntMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
        return new QHashParallelKVIntIntMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }
    @Override
    HashIntIntMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
        return new LHashParallelKVIntIntMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }


    @Override
    @Nonnull
    public HashIntIntMapFactory withDefaultValue(int defaultValue) {
        if (defaultValue == 0)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
    }


    static final class WithCustomDefaultValue
            extends LHashParallelKVIntIntMapFactoryGO {
        private final int defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, int lower, int upper, int defaultValue) {
            super(hashConf, defaultExpectedSize, lower, upper);
            this.defaultValue = defaultValue;
        }

        @Override
        public int getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableLHashParallelKVIntIntMapGO uninitializedMutableMap() {
            MutableLHashParallelKVIntIntMap.WithCustomDefaultValue map =
                    new MutableLHashParallelKVIntIntMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableLHashParallelKVIntIntMapGO uninitializedUpdatableMap() {
            UpdatableLHashParallelKVIntIntMap.WithCustomDefaultValue map =
                    new UpdatableLHashParallelKVIntIntMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableLHashParallelKVIntIntMapGO uninitializedImmutableMap() {
            ImmutableLHashParallelKVIntIntMap.WithCustomDefaultValue map =
                    new ImmutableLHashParallelKVIntIntMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashIntIntMapFactory withDefaultValue(int defaultValue) {
            if (defaultValue == 0)
                return new LHashParallelKVIntIntMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
        }

        @Override
        HashIntIntMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }

        @Override
        HashIntIntMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
            return new QHashParallelKVIntIntMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
        @Override
        HashIntIntMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
            return new LHashParallelKVIntIntMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
    }

}

