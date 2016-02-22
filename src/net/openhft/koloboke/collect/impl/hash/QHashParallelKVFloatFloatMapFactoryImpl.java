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


public final class QHashParallelKVFloatFloatMapFactoryImpl
        extends QHashParallelKVFloatFloatMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public QHashParallelKVFloatFloatMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            );
    }

    

    

    

    QHashParallelKVFloatFloatMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize) {
        super(hashConf, defaultExpectedSize);
    }

    @Override
    HashFloatFloatMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new QHashParallelKVFloatFloatMapFactoryImpl(hashConf, defaultExpectedSize);
    }

    @Override
    HashFloatFloatMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new QHashParallelKVFloatFloatMapFactoryImpl(hashConf, defaultExpectedSize);
    }
    @Override
    HashFloatFloatMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new LHashParallelKVFloatFloatMapFactoryImpl(hashConf, defaultExpectedSize);
    }


    @Override
    @Nonnull
    public HashFloatFloatMapFactory withDefaultValue(float defaultValue) {
        if (defaultValue == 0.0f)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        , defaultValue);
    }


    static final class WithCustomDefaultValue
            extends QHashParallelKVFloatFloatMapFactoryGO {
        private final float defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, float defaultValue) {
            super(hashConf, defaultExpectedSize);
            this.defaultValue = defaultValue;
        }

        @Override
        public float getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableQHashParallelKVFloatFloatMapGO uninitializedMutableMap() {
            MutableQHashParallelKVFloatFloatMap.WithCustomDefaultValue map =
                    new MutableQHashParallelKVFloatFloatMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableQHashParallelKVFloatFloatMapGO uninitializedUpdatableMap() {
            UpdatableQHashParallelKVFloatFloatMap.WithCustomDefaultValue map =
                    new UpdatableQHashParallelKVFloatFloatMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableQHashParallelKVFloatFloatMapGO uninitializedImmutableMap() {
            ImmutableQHashParallelKVFloatFloatMap.WithCustomDefaultValue map =
                    new ImmutableQHashParallelKVFloatFloatMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashFloatFloatMapFactory withDefaultValue(float defaultValue) {
            if (defaultValue == 0.0f)
                return new QHashParallelKVFloatFloatMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        );
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        , defaultValue);
        }

        @Override
        HashFloatFloatMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, defaultValue);
        }

        @Override
        HashFloatFloatMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new QHashParallelKVFloatFloatMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, defaultValue);
        }
        @Override
        HashFloatFloatMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new LHashParallelKVFloatFloatMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, defaultValue);
        }
    }

}

