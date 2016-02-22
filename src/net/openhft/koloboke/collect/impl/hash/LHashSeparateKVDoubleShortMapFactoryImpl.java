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


public final class LHashSeparateKVDoubleShortMapFactoryImpl
        extends LHashSeparateKVDoubleShortMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public LHashSeparateKVDoubleShortMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            );
    }

    

    

    

    LHashSeparateKVDoubleShortMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize) {
        super(hashConf, defaultExpectedSize);
    }

    @Override
    HashDoubleShortMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new LHashSeparateKVDoubleShortMapFactoryImpl(hashConf, defaultExpectedSize);
    }

    @Override
    HashDoubleShortMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new QHashSeparateKVDoubleShortMapFactoryImpl(hashConf, defaultExpectedSize);
    }
    @Override
    HashDoubleShortMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new LHashSeparateKVDoubleShortMapFactoryImpl(hashConf, defaultExpectedSize);
    }


    @Override
    @Nonnull
    public HashDoubleShortMapFactory withDefaultValue(short defaultValue) {
        if (defaultValue == (short) 0)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        , defaultValue);
    }


    static final class WithCustomDefaultValue
            extends LHashSeparateKVDoubleShortMapFactoryGO {
        private final short defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, short defaultValue) {
            super(hashConf, defaultExpectedSize);
            this.defaultValue = defaultValue;
        }

        @Override
        public short getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableLHashSeparateKVDoubleShortMapGO uninitializedMutableMap() {
            MutableLHashSeparateKVDoubleShortMap.WithCustomDefaultValue map =
                    new MutableLHashSeparateKVDoubleShortMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableLHashSeparateKVDoubleShortMapGO uninitializedUpdatableMap() {
            UpdatableLHashSeparateKVDoubleShortMap.WithCustomDefaultValue map =
                    new UpdatableLHashSeparateKVDoubleShortMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableLHashSeparateKVDoubleShortMapGO uninitializedImmutableMap() {
            ImmutableLHashSeparateKVDoubleShortMap.WithCustomDefaultValue map =
                    new ImmutableLHashSeparateKVDoubleShortMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashDoubleShortMapFactory withDefaultValue(short defaultValue) {
            if (defaultValue == (short) 0)
                return new LHashSeparateKVDoubleShortMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        );
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        , defaultValue);
        }

        @Override
        HashDoubleShortMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, defaultValue);
        }

        @Override
        HashDoubleShortMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new QHashSeparateKVDoubleShortMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, defaultValue);
        }
        @Override
        HashDoubleShortMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new LHashSeparateKVDoubleShortMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, defaultValue);
        }
    }

}

