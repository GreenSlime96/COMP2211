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
import net.openhft.koloboke.collect.map.DoubleByteMap;
import net.openhft.koloboke.collect.map.hash.HashDoubleByteMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVDoubleByteMapFactorySO
        extends DoubleQHashFactory 
        implements HashDoubleByteMapFactory {

    QHashSeparateKVDoubleByteMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    

    


     MutableQHashSeparateKVDoubleByteMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVDoubleByteMap();
    }
     UpdatableQHashSeparateKVDoubleByteMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVDoubleByteMap();
    }
     ImmutableQHashSeparateKVDoubleByteMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVDoubleByteMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVDoubleByteMapGO newMutableMap(int expectedSize) {
        MutableQHashSeparateKVDoubleByteMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleByteMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVDoubleByteMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleByteMapGO newUpdatableMap(
            Map<Double, Byte> map) {
        if (map instanceof DoubleByteMap) {
            if (map instanceof SeparateKVDoubleByteQHash) {
                SeparateKVDoubleByteQHash hash = (SeparateKVDoubleByteQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVDoubleByteMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVDoubleByteMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVDoubleByteMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Double, Byte> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

