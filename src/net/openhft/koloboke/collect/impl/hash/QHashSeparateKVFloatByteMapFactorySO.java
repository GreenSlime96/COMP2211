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


public abstract class QHashSeparateKVFloatByteMapFactorySO
        extends FloatQHashFactory 
        implements HashFloatByteMapFactory {

    QHashSeparateKVFloatByteMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    

    


     MutableQHashSeparateKVFloatByteMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVFloatByteMap();
    }
     UpdatableQHashSeparateKVFloatByteMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVFloatByteMap();
    }
     ImmutableQHashSeparateKVFloatByteMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVFloatByteMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVFloatByteMapGO newMutableMap(int expectedSize) {
        MutableQHashSeparateKVFloatByteMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVFloatByteMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMap(
            Map<Float, Byte> map) {
        if (map instanceof FloatByteMap) {
            if (map instanceof SeparateKVFloatByteQHash) {
                SeparateKVFloatByteQHash hash = (SeparateKVFloatByteQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVFloatByteMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVFloatByteMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVFloatByteMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Float, Byte> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

