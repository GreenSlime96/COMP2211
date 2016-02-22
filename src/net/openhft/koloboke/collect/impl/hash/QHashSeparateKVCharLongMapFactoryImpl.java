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


public final class QHashSeparateKVCharLongMapFactoryImpl
        extends QHashSeparateKVCharLongMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public QHashSeparateKVCharLongMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , Character.MIN_VALUE, Character.MAX_VALUE);
    }

    

    

    

    QHashSeparateKVCharLongMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashCharLongMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
        return new QHashSeparateKVCharLongMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashCharLongMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
        return new QHashSeparateKVCharLongMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }
    @Override
    HashCharLongMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
        return new LHashSeparateKVCharLongMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }


    @Override
    @Nonnull
    public HashCharLongMapFactory withDefaultValue(long defaultValue) {
        if (defaultValue == 0L)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
    }


    static final class WithCustomDefaultValue
            extends QHashSeparateKVCharLongMapFactoryGO {
        private final long defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, char lower, char upper, long defaultValue) {
            super(hashConf, defaultExpectedSize, lower, upper);
            this.defaultValue = defaultValue;
        }

        @Override
        public long getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableQHashSeparateKVCharLongMapGO uninitializedMutableMap() {
            MutableQHashSeparateKVCharLongMap.WithCustomDefaultValue map =
                    new MutableQHashSeparateKVCharLongMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableQHashSeparateKVCharLongMapGO uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVCharLongMap.WithCustomDefaultValue map =
                    new UpdatableQHashSeparateKVCharLongMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableQHashSeparateKVCharLongMapGO uninitializedImmutableMap() {
            ImmutableQHashSeparateKVCharLongMap.WithCustomDefaultValue map =
                    new ImmutableQHashSeparateKVCharLongMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashCharLongMapFactory withDefaultValue(long defaultValue) {
            if (defaultValue == 0L)
                return new QHashSeparateKVCharLongMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
        }

        @Override
        HashCharLongMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }

        @Override
        HashCharLongMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
            return new QHashSeparateKVCharLongMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
        @Override
        HashCharLongMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
            return new LHashSeparateKVCharLongMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
    }

}

