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


public final class LHashSeparateKVFloatLongMapFactoryImpl
        extends LHashSeparateKVFloatLongMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public LHashSeparateKVFloatLongMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            );
    }

    

    

    

    LHashSeparateKVFloatLongMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize) {
        super(hashConf, defaultExpectedSize);
    }

    @Override
    HashFloatLongMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new LHashSeparateKVFloatLongMapFactoryImpl(hashConf, defaultExpectedSize);
    }

    @Override
    HashFloatLongMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new QHashSeparateKVFloatLongMapFactoryImpl(hashConf, defaultExpectedSize);
    }
    @Override
    HashFloatLongMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new LHashSeparateKVFloatLongMapFactoryImpl(hashConf, defaultExpectedSize);
    }


    @Override
    @Nonnull
    public HashFloatLongMapFactory withDefaultValue(long defaultValue) {
        if (defaultValue == 0L)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        , defaultValue);
    }


    static final class WithCustomDefaultValue
            extends LHashSeparateKVFloatLongMapFactoryGO {
        private final long defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, long defaultValue) {
            super(hashConf, defaultExpectedSize);
            this.defaultValue = defaultValue;
        }

        @Override
        public long getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableLHashSeparateKVFloatLongMapGO uninitializedMutableMap() {
            MutableLHashSeparateKVFloatLongMap.WithCustomDefaultValue map =
                    new MutableLHashSeparateKVFloatLongMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableLHashSeparateKVFloatLongMapGO uninitializedUpdatableMap() {
            UpdatableLHashSeparateKVFloatLongMap.WithCustomDefaultValue map =
                    new UpdatableLHashSeparateKVFloatLongMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableLHashSeparateKVFloatLongMapGO uninitializedImmutableMap() {
            ImmutableLHashSeparateKVFloatLongMap.WithCustomDefaultValue map =
                    new ImmutableLHashSeparateKVFloatLongMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashFloatLongMapFactory withDefaultValue(long defaultValue) {
            if (defaultValue == 0L)
                return new LHashSeparateKVFloatLongMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        );
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        , defaultValue);
        }

        @Override
        HashFloatLongMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, defaultValue);
        }

        @Override
        HashFloatLongMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new QHashSeparateKVFloatLongMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, defaultValue);
        }
        @Override
        HashFloatLongMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new LHashSeparateKVFloatLongMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, defaultValue);
        }
    }

}
