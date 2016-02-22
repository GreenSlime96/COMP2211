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


public final class LHashSeparateKVFloatByteMapFactoryImpl
        extends LHashSeparateKVFloatByteMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public LHashSeparateKVFloatByteMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            );
    }

    

    

    

    LHashSeparateKVFloatByteMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize) {
        super(hashConf, defaultExpectedSize);
    }

    @Override
    HashFloatByteMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new LHashSeparateKVFloatByteMapFactoryImpl(hashConf, defaultExpectedSize);
    }

    @Override
    HashFloatByteMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new QHashSeparateKVFloatByteMapFactoryImpl(hashConf, defaultExpectedSize);
    }
    @Override
    HashFloatByteMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new LHashSeparateKVFloatByteMapFactoryImpl(hashConf, defaultExpectedSize);
    }


    @Override
    @Nonnull
    public HashFloatByteMapFactory withDefaultValue(byte defaultValue) {
        if (defaultValue == (byte) 0)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        , defaultValue);
    }


    static final class WithCustomDefaultValue
            extends LHashSeparateKVFloatByteMapFactoryGO {
        private final byte defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, byte defaultValue) {
            super(hashConf, defaultExpectedSize);
            this.defaultValue = defaultValue;
        }

        @Override
        public byte getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableLHashSeparateKVFloatByteMapGO uninitializedMutableMap() {
            MutableLHashSeparateKVFloatByteMap.WithCustomDefaultValue map =
                    new MutableLHashSeparateKVFloatByteMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableLHashSeparateKVFloatByteMapGO uninitializedUpdatableMap() {
            UpdatableLHashSeparateKVFloatByteMap.WithCustomDefaultValue map =
                    new UpdatableLHashSeparateKVFloatByteMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableLHashSeparateKVFloatByteMapGO uninitializedImmutableMap() {
            ImmutableLHashSeparateKVFloatByteMap.WithCustomDefaultValue map =
                    new ImmutableLHashSeparateKVFloatByteMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashFloatByteMapFactory withDefaultValue(byte defaultValue) {
            if (defaultValue == (byte) 0)
                return new LHashSeparateKVFloatByteMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        );
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        , defaultValue);
        }

        @Override
        HashFloatByteMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, defaultValue);
        }

        @Override
        HashFloatByteMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new QHashSeparateKVFloatByteMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, defaultValue);
        }
        @Override
        HashFloatByteMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new LHashSeparateKVFloatByteMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, defaultValue);
        }
    }

}

