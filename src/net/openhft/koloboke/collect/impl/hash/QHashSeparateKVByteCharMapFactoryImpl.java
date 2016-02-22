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


public final class QHashSeparateKVByteCharMapFactoryImpl
        extends QHashSeparateKVByteCharMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public QHashSeparateKVByteCharMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    

    

    

    QHashSeparateKVByteCharMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashByteCharMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
        return new QHashSeparateKVByteCharMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashByteCharMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
        return new QHashSeparateKVByteCharMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }
    @Override
    HashByteCharMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
        return new LHashSeparateKVByteCharMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }


    @Override
    @Nonnull
    public HashByteCharMapFactory withDefaultValue(char defaultValue) {
        if (defaultValue == (char) 0)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
    }


    static final class WithCustomDefaultValue
            extends QHashSeparateKVByteCharMapFactoryGO {
        private final char defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper, char defaultValue) {
            super(hashConf, defaultExpectedSize, lower, upper);
            this.defaultValue = defaultValue;
        }

        @Override
        public char getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableQHashSeparateKVByteCharMapGO uninitializedMutableMap() {
            MutableQHashSeparateKVByteCharMap.WithCustomDefaultValue map =
                    new MutableQHashSeparateKVByteCharMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableQHashSeparateKVByteCharMapGO uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVByteCharMap.WithCustomDefaultValue map =
                    new UpdatableQHashSeparateKVByteCharMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableQHashSeparateKVByteCharMapGO uninitializedImmutableMap() {
            ImmutableQHashSeparateKVByteCharMap.WithCustomDefaultValue map =
                    new ImmutableQHashSeparateKVByteCharMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashByteCharMapFactory withDefaultValue(char defaultValue) {
            if (defaultValue == (char) 0)
                return new QHashSeparateKVByteCharMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
        }

        @Override
        HashByteCharMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }

        @Override
        HashByteCharMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
            return new QHashSeparateKVByteCharMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
        @Override
        HashByteCharMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
            return new LHashSeparateKVByteCharMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
    }

}

