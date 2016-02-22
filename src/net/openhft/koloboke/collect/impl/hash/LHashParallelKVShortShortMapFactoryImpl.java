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


public final class LHashParallelKVShortShortMapFactoryImpl
        extends LHashParallelKVShortShortMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public LHashParallelKVShortShortMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , Short.MIN_VALUE, Short.MAX_VALUE);
    }

    

    

    

    LHashParallelKVShortShortMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashShortShortMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
        return new LHashParallelKVShortShortMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashShortShortMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
        return new QHashParallelKVShortShortMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }
    @Override
    HashShortShortMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
        return new LHashParallelKVShortShortMapFactoryImpl(hashConf, defaultExpectedSize, lower, upper);
    }


    @Override
    @Nonnull
    public HashShortShortMapFactory withDefaultValue(short defaultValue) {
        if (defaultValue == (short) 0)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
    }


    static final class WithCustomDefaultValue
            extends LHashParallelKVShortShortMapFactoryGO {
        private final short defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, short lower, short upper, short defaultValue) {
            super(hashConf, defaultExpectedSize, lower, upper);
            this.defaultValue = defaultValue;
        }

        @Override
        public short getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableLHashParallelKVShortShortMapGO uninitializedMutableMap() {
            MutableLHashParallelKVShortShortMap.WithCustomDefaultValue map =
                    new MutableLHashParallelKVShortShortMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableLHashParallelKVShortShortMapGO uninitializedUpdatableMap() {
            UpdatableLHashParallelKVShortShortMap.WithCustomDefaultValue map =
                    new UpdatableLHashParallelKVShortShortMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableLHashParallelKVShortShortMapGO uninitializedImmutableMap() {
            ImmutableLHashParallelKVShortShortMap.WithCustomDefaultValue map =
                    new ImmutableLHashParallelKVShortShortMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashShortShortMapFactory withDefaultValue(short defaultValue) {
            if (defaultValue == (short) 0)
                return new LHashParallelKVShortShortMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound());
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(), defaultValue);
        }

        @Override
        HashShortShortMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }

        @Override
        HashShortShortMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
            return new QHashParallelKVShortShortMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
        @Override
        HashShortShortMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
            return new LHashParallelKVShortShortMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, lower, upper, defaultValue);
        }
    }

}

