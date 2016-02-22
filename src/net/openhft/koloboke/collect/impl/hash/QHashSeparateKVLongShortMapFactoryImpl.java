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


public final class QHashSeparateKVLongShortMapFactoryImpl
        extends QHashSeparateKVLongShortMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public QHashSeparateKVLongShortMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , Long.MIN_VALUE, Long.MAX_VALUE);
    }

    

    

    

    QHashSeparateKVLongShortMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashLongShortMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
        return new QHashSeparateKVLongShortMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashLongShortMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
        return new QHashSeparateKVLongShortMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }
    @Override
    HashLongShortMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
        return new LHashSeparateKVLongShortMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }


    @Override
    @Nonnull
    public HashLongShortMapFactory withDefaultValue(short defaultValue) {
        if (defaultValue == (short) 0)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
    }


    static final class WithCustomDefaultValue
            extends QHashSeparateKVLongShortMapFactoryGO {
        private final short defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, long lower, long upper, short defaultValue) {
            super(hashConf, defaultExpectedSize, lower, upper);
            this.defaultValue = defaultValue;
        }

        @Override
        public short getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableQHashSeparateKVLongShortMapGO uninitializedMutableMap() {
            MutableQHashSeparateKVLongShortMap.WithCustomDefaultValue map =
                    new MutableQHashSeparateKVLongShortMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableQHashSeparateKVLongShortMapGO uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVLongShortMap.WithCustomDefaultValue map =
                    new UpdatableQHashSeparateKVLongShortMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableQHashSeparateKVLongShortMapGO uninitializedImmutableMap() {
            ImmutableQHashSeparateKVLongShortMap.WithCustomDefaultValue map =
                    new ImmutableQHashSeparateKVLongShortMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashLongShortMapFactory withDefaultValue(short defaultValue) {
            if (defaultValue == (short) 0)
                return new QHashSeparateKVLongShortMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
        }

        @Override
        HashLongShortMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }

        @Override
        HashLongShortMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
            return new QHashSeparateKVLongShortMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
        @Override
        HashLongShortMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
            return new LHashSeparateKVLongShortMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
    }

}

