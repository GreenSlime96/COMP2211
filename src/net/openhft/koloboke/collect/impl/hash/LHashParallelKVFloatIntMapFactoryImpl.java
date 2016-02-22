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


public final class LHashParallelKVFloatIntMapFactoryImpl
        extends LHashParallelKVFloatIntMapFactoryGO {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public LHashParallelKVFloatIntMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            );
    }

    

    

    

    LHashParallelKVFloatIntMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize) {
        super(hashConf, defaultExpectedSize);
    }

    @Override
    HashFloatIntMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new LHashParallelKVFloatIntMapFactoryImpl(hashConf, defaultExpectedSize);
    }

    @Override
    HashFloatIntMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new QHashParallelKVFloatIntMapFactoryImpl(hashConf, defaultExpectedSize);
    }
    @Override
    HashFloatIntMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new LHashParallelKVFloatIntMapFactoryImpl(hashConf, defaultExpectedSize);
    }


    @Override
    @Nonnull
    public HashFloatIntMapFactory withDefaultValue(int defaultValue) {
        if (defaultValue == 0)
            return this;
        return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        , defaultValue);
    }


    static final class WithCustomDefaultValue
            extends LHashParallelKVFloatIntMapFactoryGO {
        private final int defaultValue;

        WithCustomDefaultValue(HashConfig hashConf, int defaultExpectedSize, int defaultValue) {
            super(hashConf, defaultExpectedSize);
            this.defaultValue = defaultValue;
        }

        @Override
        public int getDefaultValue() {
            return defaultValue;
        }

        @Override
         MutableLHashParallelKVFloatIntMapGO uninitializedMutableMap() {
            MutableLHashParallelKVFloatIntMap.WithCustomDefaultValue map =
                    new MutableLHashParallelKVFloatIntMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         UpdatableLHashParallelKVFloatIntMapGO uninitializedUpdatableMap() {
            UpdatableLHashParallelKVFloatIntMap.WithCustomDefaultValue map =
                    new UpdatableLHashParallelKVFloatIntMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }
        @Override
         ImmutableLHashParallelKVFloatIntMapGO uninitializedImmutableMap() {
            ImmutableLHashParallelKVFloatIntMap.WithCustomDefaultValue map =
                    new ImmutableLHashParallelKVFloatIntMap.WithCustomDefaultValue();
            map.defaultValue = defaultValue;
            return map;
        }


        @Override
        @Nonnull
        public HashFloatIntMapFactory withDefaultValue(int defaultValue) {
            if (defaultValue == 0)
                return new LHashParallelKVFloatIntMapFactoryImpl(getHashConfig(), getDefaultExpectedSize()
        );
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue(getHashConfig(), getDefaultExpectedSize()
        , defaultValue);
        }

        @Override
        HashFloatIntMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new WithCustomDefaultValue(hashConf, defaultExpectedSize, defaultValue);
        }

        @Override
        HashFloatIntMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new QHashParallelKVFloatIntMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, defaultValue);
        }
        @Override
        HashFloatIntMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new LHashParallelKVFloatIntMapFactoryImpl.WithCustomDefaultValue(
                    hashConf, defaultExpectedSize, defaultValue);
        }
    }

}

