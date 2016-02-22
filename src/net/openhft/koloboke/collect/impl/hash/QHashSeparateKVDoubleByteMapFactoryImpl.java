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


public final class QHashSeparateKVDoubleByteMapFactoryImpl
        extends QHashSeparateKVDoubleByteMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public QHashSeparateKVDoubleByteMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            );
    }

    

    

    

    QHashSeparateKVDoubleByteMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize) {
        super(hashConf, defaultExpectedSize);
    }

    @Override
    HashDoubleByteMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new QHashSeparateKVDoubleByteMapFactoryImpl(hashConf, defaultExpectedSize);
    }

    @Override
    HashDoubleByteMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new QHashSeparateKVDoubleByteMapFactoryImpl(hashConf, defaultExpectedSize);
    }
    @Override
    HashDoubleByteMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new LHashSeparateKVDoubleByteMapFactoryImpl(hashConf, defaultExpectedSize);
    }


    @Override
    @Nonnull
    public HashDoubleByteMapFactory withDefaultValue(byte defaultValue) {
        if (defaultValue == (byte) 0)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        , defaultValue);
    }


    static final class WithCustomDefaultValue
            extends QHashSeparateKVDoubleByteMapFactoryGO {
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
         MutableQHashSeparateKVDoubleByteMapGO uninitializedMutableMap() {
            MutableQHashSeparateKVDoubleByteMap.WithCustomDefaultValue map =
                    new MutableQHashSeparateKVDoubleByteMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableQHashSeparateKVDoubleByteMapGO uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVDoubleByteMap.WithCustomDefaultValue map =
                    new UpdatableQHashSeparateKVDoubleByteMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableQHashSeparateKVDoubleByteMapGO uninitializedImmutableMap() {
            ImmutableQHashSeparateKVDoubleByteMap.WithCustomDefaultValue map =
                    new ImmutableQHashSeparateKVDoubleByteMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashDoubleByteMapFactory withDefaultValue(byte defaultValue) {
            if (defaultValue == (byte) 0)
                return new QHashSeparateKVDoubleByteMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        );
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        , defaultValue);
        }

        @Override
        HashDoubleByteMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, defaultValue);
        }

        @Override
        HashDoubleByteMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new QHashSeparateKVDoubleByteMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, defaultValue);
        }
        @Override
        HashDoubleByteMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new LHashSeparateKVDoubleByteMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, defaultValue);
        }
    }

}

