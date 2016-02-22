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
import net.openhft.koloboke.collect.map.FloatIntMap;
import net.openhft.koloboke.collect.map.hash.HashFloatIntMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashParallelKVFloatIntMapFactorySO
        extends FloatLHashFactory 
        implements HashFloatIntMapFactory {

    LHashParallelKVFloatIntMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    

    


     MutableLHashParallelKVFloatIntMapGO uninitializedMutableMap() {
        return new MutableLHashParallelKVFloatIntMap();
    }
     UpdatableLHashParallelKVFloatIntMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashParallelKVFloatIntMap();
    }
     ImmutableLHashParallelKVFloatIntMapGO uninitializedImmutableMap() {
        return new ImmutableLHashParallelKVFloatIntMap();
    }

    @Override
    @Nonnull
    public  MutableLHashParallelKVFloatIntMapGO newMutableMap(int expectedSize) {
        MutableLHashParallelKVFloatIntMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVFloatIntMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashParallelKVFloatIntMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVFloatIntMapGO newUpdatableMap(
            Map<Float, Integer> map) {
        if (map instanceof FloatIntMap) {
            if (map instanceof ParallelKVFloatIntLHash) {
                ParallelKVFloatIntLHash hash = (ParallelKVFloatIntLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashParallelKVFloatIntMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashParallelKVFloatIntMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashParallelKVFloatIntMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Float, Integer> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

