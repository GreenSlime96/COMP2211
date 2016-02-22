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


public final class LHashSeparateKVByteShortMapFactoryImpl
        extends LHashSeparateKVByteShortMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public LHashSeparateKVByteShortMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    

    

    

    LHashSeparateKVByteShortMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashByteShortMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
        return new LHashSeparateKVByteShortMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashByteShortMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
        return new QHashSeparateKVByteShortMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }
    @Override
    HashByteShortMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
        return new LHashSeparateKVByteShortMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }


    @Override
    @Nonnull
    public HashByteShortMapFactory withDefaultValue(short defaultValue) {
        if (defaultValue == (short) 0)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
    }


    static final class WithCustomDefaultValue
            extends LHashSeparateKVByteShortMapFactoryGO {
        private final short defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper, short defaultValue) {
            super(hashConf, defaultExpectedSize, lower, upper);
            this.defaultValue = defaultValue;
        }

        @Override
        public short getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableLHashSeparateKVByteShortMapGO uninitializedMutableMap() {
            MutableLHashSeparateKVByteShortMap.WithCustomDefaultValue map =
                    new MutableLHashSeparateKVByteShortMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableLHashSeparateKVByteShortMapGO uninitializedUpdatableMap() {
            UpdatableLHashSeparateKVByteShortMap.WithCustomDefaultValue map =
                    new UpdatableLHashSeparateKVByteShortMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableLHashSeparateKVByteShortMapGO uninitializedImmutableMap() {
            ImmutableLHashSeparateKVByteShortMap.WithCustomDefaultValue map =
                    new ImmutableLHashSeparateKVByteShortMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashByteShortMapFactory withDefaultValue(short defaultValue) {
            if (defaultValue == (short) 0)
                return new LHashSeparateKVByteShortMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
        }

        @Override
        HashByteShortMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }

        @Override
        HashByteShortMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
            return new QHashSeparateKVByteShortMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
        @Override
        HashByteShortMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
            return new LHashSeparateKVByteShortMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
    }

}

