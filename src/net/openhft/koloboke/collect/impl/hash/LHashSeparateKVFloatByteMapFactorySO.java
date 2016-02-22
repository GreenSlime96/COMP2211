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
import net.openhft.koloboke.collect.map.FloatByteMap;
import net.openhft.koloboke.collect.map.hash.HashFloatByteMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashSeparateKVFloatByteMapFactorySO
        extends FloatLHashFactory 
        implements HashFloatByteMapFactory {

    LHashSeparateKVFloatByteMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    

    


     MutableLHashSeparateKVFloatByteMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVFloatByteMap();
    }
     UpdatableLHashSeparateKVFloatByteMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVFloatByteMap();
    }
     ImmutableLHashSeparateKVFloatByteMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVFloatByteMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVFloatByteMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVFloatByteMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatByteMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVFloatByteMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatByteMapGO newUpdatableMap(
            Map<Float, Byte> map) {
        if (map instanceof FloatByteMap) {
            if (map instanceof SeparateKVFloatByteLHash) {
                SeparateKVFloatByteLHash hash = (SeparateKVFloatByteLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVFloatByteMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVFloatByteMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVFloatByteMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Float, Byte> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

