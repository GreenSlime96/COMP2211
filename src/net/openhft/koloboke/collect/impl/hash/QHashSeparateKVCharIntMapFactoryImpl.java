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


public final class QHashSeparateKVCharIntMapFactoryImpl
        extends QHashSeparateKVCharIntMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public QHashSeparateKVCharIntMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , Character.MIN_VALUE, Character.MAX_VALUE);
    }

    

    

    

    QHashSeparateKVCharIntMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashCharIntMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
        return new QHashSeparateKVCharIntMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashCharIntMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
        return new QHashSeparateKVCharIntMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }
    @Override
    HashCharIntMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
        return new LHashSeparateKVCharIntMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }


    @Override
    @Nonnull
    public HashCharIntMapFactory withDefaultValue(int defaultValue) {
        if (defaultValue == 0)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
    }


    static final class WithCustomDefaultValue
            extends QHashSeparateKVCharIntMapFactoryGO {
        private final int defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, char lower, char upper, int defaultValue) {
            super(hashConf, defaultExpectedSize, lower, upper);
            this.defaultValue = defaultValue;
        }

        @Override
        public int getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableQHashSeparateKVCharIntMapGO uninitializedMutableMap() {
            MutableQHashSeparateKVCharIntMap.WithCustomDefaultValue map =
                    new MutableQHashSeparateKVCharIntMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableQHashSeparateKVCharIntMapGO uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVCharIntMap.WithCustomDefaultValue map =
                    new UpdatableQHashSeparateKVCharIntMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableQHashSeparateKVCharIntMapGO uninitializedImmutableMap() {
            ImmutableQHashSeparateKVCharIntMap.WithCustomDefaultValue map =
                    new ImmutableQHashSeparateKVCharIntMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashCharIntMapFactory withDefaultValue(int defaultValue) {
            if (defaultValue == 0)
                return new QHashSeparateKVCharIntMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
        }

        @Override
        HashCharIntMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }

        @Override
        HashCharIntMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
            return new QHashSeparateKVCharIntMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
        @Override
        HashCharIntMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
            return new LHashSeparateKVCharIntMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
    }

}

