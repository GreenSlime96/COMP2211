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


public final class QHashSeparateKVShortLongMapFactoryImpl
        extends QHashSeparateKVShortLongMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public QHashSeparateKVShortLongMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , Short.MIN_VALUE, Short.MAX_VALUE);
    }

    

    

    

    QHashSeparateKVShortLongMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashShortLongMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
        return new QHashSeparateKVShortLongMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashShortLongMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
        return new QHashSeparateKVShortLongMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }
    @Override
    HashShortLongMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
        return new LHashSeparateKVShortLongMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }


    @Override
    @Nonnull
    public HashShortLongMapFactory withDefaultValue(long defaultValue) {
        if (defaultValue == 0L)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
    }


    static final class WithCustomDefaultValue
            extends QHashSeparateKVShortLongMapFactoryGO {
        private final long defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, short lower, short upper, long defaultValue) {
            super(hashConf, defaultExpectedSize, lower, upper);
            this.defaultValue = defaultValue;
        }

        @Override
        public long getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableQHashSeparateKVShortLongMapGO uninitializedMutableMap() {
            MutableQHashSeparateKVShortLongMap.WithCustomDefaultValue map =
                    new MutableQHashSeparateKVShortLongMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableQHashSeparateKVShortLongMapGO uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVShortLongMap.WithCustomDefaultValue map =
                    new UpdatableQHashSeparateKVShortLongMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableQHashSeparateKVShortLongMapGO uninitializedImmutableMap() {
            ImmutableQHashSeparateKVShortLongMap.WithCustomDefaultValue map =
                    new ImmutableQHashSeparateKVShortLongMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashShortLongMapFactory withDefaultValue(long defaultValue) {
            if (defaultValue == 0L)
                return new QHashSeparateKVShortLongMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
        }

        @Override
        HashShortLongMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }

        @Override
        HashShortLongMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
            return new QHashSeparateKVShortLongMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
        @Override
        HashShortLongMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
            return new LHashSeparateKVShortLongMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
    }

}
