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
import net.openhft.koloboke.collect.map.DoubleFloatMap;
import net.openhft.koloboke.collect.map.hash.HashDoubleFloatMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashSeparateKVDoubleFloatMapFactorySO
        extends DoubleLHashFactory 
        implements HashDoubleFloatMapFactory {

    LHashSeparateKVDoubleFloatMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    

    


     MutableLHashSeparateKVDoubleFloatMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVDoubleFloatMap();
    }
     UpdatableLHashSeparateKVDoubleFloatMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVDoubleFloatMap();
    }
     ImmutableLHashSeparateKVDoubleFloatMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVDoubleFloatMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVDoubleFloatMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVDoubleFloatMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleFloatMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVDoubleFloatMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleFloatMapGO newUpdatableMap(
            Map<Double, Float> map) {
        if (map instanceof DoubleFloatMap) {
            if (map instanceof SeparateKVDoubleFloatLHash) {
                SeparateKVDoubleFloatLHash hash = (SeparateKVDoubleFloatLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVDoubleFloatMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVDoubleFloatMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVDoubleFloatMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Double, Float> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

