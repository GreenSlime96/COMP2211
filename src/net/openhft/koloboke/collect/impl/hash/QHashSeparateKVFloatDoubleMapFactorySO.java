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
import net.openhft.koloboke.collect.map.FloatDoubleMap;
import net.openhft.koloboke.collect.map.hash.HashFloatDoubleMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVFloatDoubleMapFactorySO
        extends FloatQHashFactory 
        implements HashFloatDoubleMapFactory {

    QHashSeparateKVFloatDoubleMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    

    


     MutableQHashSeparateKVFloatDoubleMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVFloatDoubleMap();
    }
     UpdatableQHashSeparateKVFloatDoubleMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVFloatDoubleMap();
    }
     ImmutableQHashSeparateKVFloatDoubleMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVFloatDoubleMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVFloatDoubleMapGO newMutableMap(int expectedSize) {
        MutableQHashSeparateKVFloatDoubleMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatDoubleMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVFloatDoubleMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatDoubleMapGO newUpdatableMap(
            Map<Float, Double> map) {
        if (map instanceof FloatDoubleMap) {
            if (map instanceof SeparateKVFloatDoubleQHash) {
                SeparateKVFloatDoubleQHash hash = (SeparateKVFloatDoubleQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVFloatDoubleMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVFloatDoubleMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVFloatDoubleMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Float, Double> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

