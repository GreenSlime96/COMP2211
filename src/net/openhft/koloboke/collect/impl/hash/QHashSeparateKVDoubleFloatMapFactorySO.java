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


public abstract class QHashSeparateKVDoubleFloatMapFactorySO
        extends DoubleQHashFactory 
        implements HashDoubleFloatMapFactory {

    QHashSeparateKVDoubleFloatMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    

    


     MutableQHashSeparateKVDoubleFloatMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVDoubleFloatMap();
    }
     UpdatableQHashSeparateKVDoubleFloatMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVDoubleFloatMap();
    }
     ImmutableQHashSeparateKVDoubleFloatMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVDoubleFloatMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVDoubleFloatMapGO newMutableMap(int expectedSize) {
        MutableQHashSeparateKVDoubleFloatMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVDoubleFloatMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMap(
            Map<Double, Float> map) {
        if (map instanceof DoubleFloatMap) {
            if (map instanceof SeparateKVDoubleFloatQHash) {
                SeparateKVDoubleFloatQHash hash = (SeparateKVDoubleFloatQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVDoubleFloatMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVDoubleFloatMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVDoubleFloatMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Double, Float> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

