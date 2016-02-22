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


public final class QHashSeparateKVIntLongMapFactoryImpl
        extends QHashSeparateKVIntLongMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public QHashSeparateKVIntLongMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    

    

    

    QHashSeparateKVIntLongMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashIntLongMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
        return new QHashSeparateKVIntLongMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashIntLongMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
        return new QHashSeparateKVIntLongMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }
    @Override
    HashIntLongMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
        return new LHashSeparateKVIntLongMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }


    @Override
    @Nonnull
    public HashIntLongMapFactory withDefaultValue(long defaultValue) {
        if (defaultValue == 0L)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
    }


    static final class WithCustomDefaultValue
            extends QHashSeparateKVIntLongMapFactoryGO {
        private final long defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, int lower, int upper, long defaultValue) {
            super(hashConf, defaultExpectedSize, lower, upper);
            this.defaultValue = defaultValue;
        }

        @Override
        public long getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableQHashSeparateKVIntLongMapGO uninitializedMutableMap() {
            MutableQHashSeparateKVIntLongMap.WithCustomDefaultValue map =
                    new MutableQHashSeparateKVIntLongMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableQHashSeparateKVIntLongMapGO uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVIntLongMap.WithCustomDefaultValue map =
                    new UpdatableQHashSeparateKVIntLongMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableQHashSeparateKVIntLongMapGO uninitializedImmutableMap() {
            ImmutableQHashSeparateKVIntLongMap.WithCustomDefaultValue map =
                    new ImmutableQHashSeparateKVIntLongMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashIntLongMapFactory withDefaultValue(long defaultValue) {
            if (defaultValue == 0L)
                return new QHashSeparateKVIntLongMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
        }

        @Override
        HashIntLongMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }

        @Override
        HashIntLongMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
            return new QHashSeparateKVIntLongMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
        @Override
        HashIntLongMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper) {
            return new LHashSeparateKVIntLongMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
    }

}

