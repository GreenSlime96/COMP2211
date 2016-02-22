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


public final class LHashSeparateKVLongIntMapFactoryImpl
        extends LHashSeparateKVLongIntMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public LHashSeparateKVLongIntMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , Long.MIN_VALUE, Long.MAX_VALUE);
    }

    

    

    

    LHashSeparateKVLongIntMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashLongIntMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
        return new LHashSeparateKVLongIntMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashLongIntMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
        return new QHashSeparateKVLongIntMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }
    @Override
    HashLongIntMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
        return new LHashSeparateKVLongIntMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }


    @Override
    @Nonnull
    public HashLongIntMapFactory withDefaultValue(int defaultValue) {
        if (defaultValue == 0)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
    }


    static final class WithCustomDefaultValue
            extends LHashSeparateKVLongIntMapFactoryGO {
        private final int defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, long lower, long upper, int defaultValue) {
            super(hashConf, defaultExpectedSize, lower, upper);
            this.defaultValue = defaultValue;
        }

        @Override
        public int getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableLHashSeparateKVLongIntMapGO uninitializedMutableMap() {
            MutableLHashSeparateKVLongIntMap.WithCustomDefaultValue map =
                    new MutableLHashSeparateKVLongIntMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableLHashSeparateKVLongIntMapGO uninitializedUpdatableMap() {
            UpdatableLHashSeparateKVLongIntMap.WithCustomDefaultValue map =
                    new UpdatableLHashSeparateKVLongIntMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableLHashSeparateKVLongIntMapGO uninitializedImmutableMap() {
            ImmutableLHashSeparateKVLongIntMap.WithCustomDefaultValue map =
                    new ImmutableLHashSeparateKVLongIntMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashLongIntMapFactory withDefaultValue(int defaultValue) {
            if (defaultValue == 0)
                return new LHashSeparateKVLongIntMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
        }

        @Override
        HashLongIntMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }

        @Override
        HashLongIntMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
            return new QHashSeparateKVLongIntMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
        @Override
        HashLongIntMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
            return new LHashSeparateKVLongIntMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
    }

}

