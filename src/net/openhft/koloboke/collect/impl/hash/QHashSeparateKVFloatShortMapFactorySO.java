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
import net.openhft.koloboke.collect.map.FloatShortMap;
import net.openhft.koloboke.collect.map.hash.HashFloatShortMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVFloatShortMapFactorySO
        extends FloatQHashFactory 
        implements HashFloatShortMapFactory {

    QHashSeparateKVFloatShortMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    

    


     MutableQHashSeparateKVFloatShortMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVFloatShortMap();
    }
     UpdatableQHashSeparateKVFloatShortMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVFloatShortMap();
    }
     ImmutableQHashSeparateKVFloatShortMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVFloatShortMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVFloatShortMapGO newMutableMap(int expectedSize) {
        MutableQHashSeparateKVFloatShortMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatShortMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVFloatShortMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatShortMapGO newUpdatableMap(
            Map<Float, Short> map) {
        if (map instanceof FloatShortMap) {
            if (map instanceof SeparateKVFloatShortQHash) {
                SeparateKVFloatShortQHash hash = (SeparateKVFloatShortQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVFloatShortMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVFloatShortMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVFloatShortMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Float, Short> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

