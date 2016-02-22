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


public final class QHashSeparateKVLongByteMapFactoryImpl
        extends QHashSeparateKVLongByteMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public QHashSeparateKVLongByteMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , Long.MIN_VALUE, Long.MAX_VALUE);
    }

    

    

    

    QHashSeparateKVLongByteMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashLongByteMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
        return new QHashSeparateKVLongByteMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashLongByteMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
        return new QHashSeparateKVLongByteMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }
    @Override
    HashLongByteMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
        return new LHashSeparateKVLongByteMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }


    @Override
    @Nonnull
    public HashLongByteMapFactory withDefaultValue(byte defaultValue) {
        if (defaultValue == (byte) 0)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
    }


    static final class WithCustomDefaultValue
            extends QHashSeparateKVLongByteMapFactoryGO {
        private final byte defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, long lower, long upper, byte defaultValue) {
            super(hashConf, defaultExpectedSize, lower, upper);
            this.defaultValue = defaultValue;
        }

        @Override
        public byte getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableQHashSeparateKVLongByteMapGO uninitializedMutableMap() {
            MutableQHashSeparateKVLongByteMap.WithCustomDefaultValue map =
                    new MutableQHashSeparateKVLongByteMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableQHashSeparateKVLongByteMapGO uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVLongByteMap.WithCustomDefaultValue map =
                    new UpdatableQHashSeparateKVLongByteMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableQHashSeparateKVLongByteMapGO uninitializedImmutableMap() {
            ImmutableQHashSeparateKVLongByteMap.WithCustomDefaultValue map =
                    new ImmutableQHashSeparateKVLongByteMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashLongByteMapFactory withDefaultValue(byte defaultValue) {
            if (defaultValue == (byte) 0)
                return new QHashSeparateKVLongByteMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
        }

        @Override
        HashLongByteMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }

        @Override
        HashLongByteMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
            return new QHashSeparateKVLongByteMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
        @Override
        HashLongByteMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
            return new LHashSeparateKVLongByteMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
    }

}

