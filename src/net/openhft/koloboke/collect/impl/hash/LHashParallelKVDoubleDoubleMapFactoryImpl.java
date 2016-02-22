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


public final class LHashParallelKVDoubleDoubleMapFactoryImpl
        extends LHashParallelKVDoubleDoubleMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public LHashParallelKVDoubleDoubleMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            );
    }

    

    

    

    LHashParallelKVDoubleDoubleMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize) {
        super(hashConf, defaultExpectedSize);
    }

    @Override
    HashDoubleDoubleMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new LHashParallelKVDoubleDoubleMapFactoryImpl(hashConf, defaultExpectedSize);
    }

    @Override
    HashDoubleDoubleMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new QHashParallelKVDoubleDoubleMapFactoryImpl(hashConf, defaultExpectedSize);
    }
    @Override
    HashDoubleDoubleMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new LHashParallelKVDoubleDoubleMapFactoryImpl(hashConf, defaultExpectedSize);
    }


    @Override
    @Nonnull
    public HashDoubleDoubleMapFactory withDefaultValue(double defaultValue) {
        if (defaultValue == 0.0)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        , defaultValue);
    }


    static final class WithCustomDefaultValue
            extends LHashParallelKVDoubleDoubleMapFactoryGO {
        private final double defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, double defaultValue) {
            super(hashConf, defaultExpectedSize);
            this.defaultValue = defaultValue;
        }

        @Override
        public double getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableLHashParallelKVDoubleDoubleMapGO uninitializedMutableMap() {
            MutableLHashParallelKVDoubleDoubleMap.WithCustomDefaultValue map =
                    new MutableLHashParallelKVDoubleDoubleMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableLHashParallelKVDoubleDoubleMapGO uninitializedUpdatableMap() {
            UpdatableLHashParallelKVDoubleDoubleMap.WithCustomDefaultValue map =
                    new UpdatableLHashParallelKVDoubleDoubleMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableLHashParallelKVDoubleDoubleMapGO uninitializedImmutableMap() {
            ImmutableLHashParallelKVDoubleDoubleMap.WithCustomDefaultValue map =
                    new ImmutableLHashParallelKVDoubleDoubleMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashDoubleDoubleMapFactory withDefaultValue(double defaultValue) {
            if (defaultValue == 0.0)
                return new LHashParallelKVDoubleDoubleMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        );
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        , defaultValue);
        }

        @Override
        HashDoubleDoubleMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, defaultValue);
        }

        @Override
        HashDoubleDoubleMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new QHashParallelKVDoubleDoubleMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, defaultValue);
        }
        @Override
        HashDoubleDoubleMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new LHashParallelKVDoubleDoubleMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, defaultValue);
        }
    }

}

