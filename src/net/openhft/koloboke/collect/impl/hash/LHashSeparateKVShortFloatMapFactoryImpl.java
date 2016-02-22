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


public final class LHashSeparateKVShortFloatMapFactoryImpl
        extends LHashSeparateKVShortFloatMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public LHashSeparateKVShortFloatMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , Short.MIN_VALUE, Short.MAX_VALUE);
    }

    

    

    

    LHashSeparateKVShortFloatMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashShortFloatMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
        return new LHashSeparateKVShortFloatMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashShortFloatMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
        return new QHashSeparateKVShortFloatMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }
    @Override
    HashShortFloatMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
        return new LHashSeparateKVShortFloatMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }


    @Override
    @Nonnull
    public HashShortFloatMapFactory withDefaultValue(float defaultValue) {
        if (defaultValue == 0.0f)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
    }


    static final class WithCustomDefaultValue
            extends LHashSeparateKVShortFloatMapFactoryGO {
        private final float defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, short lower, short upper, float defaultValue) {
            super(hashConf, defaultExpectedSize, lower, upper);
            this.defaultValue = defaultValue;
        }

        @Override
        public float getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableLHashSeparateKVShortFloatMapGO uninitializedMutableMap() {
            MutableLHashSeparateKVShortFloatMap.WithCustomDefaultValue map =
                    new MutableLHashSeparateKVShortFloatMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableLHashSeparateKVShortFloatMapGO uninitializedUpdatableMap() {
            UpdatableLHashSeparateKVShortFloatMap.WithCustomDefaultValue map =
                    new UpdatableLHashSeparateKVShortFloatMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableLHashSeparateKVShortFloatMapGO uninitializedImmutableMap() {
            ImmutableLHashSeparateKVShortFloatMap.WithCustomDefaultValue map =
                    new ImmutableLHashSeparateKVShortFloatMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashShortFloatMapFactory withDefaultValue(float defaultValue) {
            if (defaultValue == 0.0f)
                return new LHashSeparateKVShortFloatMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
        }

        @Override
        HashShortFloatMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }

        @Override
        HashShortFloatMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
            return new QHashSeparateKVShortFloatMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
        @Override
        HashShortFloatMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
            return new LHashSeparateKVShortFloatMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
    }

}

