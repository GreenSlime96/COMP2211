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

import net.openhft.koloboke.collect.hash.*;
import net.openhft.koloboke.collect.map.FloatFloatMap;
import net.openhft.koloboke.collect.map.hash.HashFloatFloatMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashParallelKVFloatFloatMapFactorySO
        extends FloatLHashFactory 
        implements HashFloatFloatMapFactory {

    LHashParallelKVFloatFloatMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    

    


     MutableLHashParallelKVFloatFloatMapGO uninitializedMutableMap() {
        return new MutableLHashParallelKVFloatFloatMap();
    }
     UpdatableLHashParallelKVFloatFloatMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashParallelKVFloatFloatMap();
    }
     ImmutableLHashParallelKVFloatFloatMapGO uninitializedImmutableMap() {
        return new ImmutableLHashParallelKVFloatFloatMap();
    }

    @Override
    @Nonnull
    public  MutableLHashParallelKVFloatFloatMapGO newMutableMap(int expectedSize) {
        MutableLHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashParallelKVFloatFloatMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMap(
            Map<Float, Float> map) {
        if (map instanceof FloatFloatMap) {
            if (map instanceof ParallelKVFloatFloatLHash) {
                ParallelKVFloatFloatLHash hash = (ParallelKVFloatFloatLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashParallelKVFloatFloatMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashParallelKVFloatFloatMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashParallelKVFloatFloatMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Float, Float> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

