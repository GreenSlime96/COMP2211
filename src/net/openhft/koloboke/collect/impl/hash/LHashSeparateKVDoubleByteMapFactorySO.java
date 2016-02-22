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


public abstract class LHashSeparateKVDoubleByteMapFactorySO
        extends DoubleLHashFactory 
        implements HashDoubleByteMapFactory {

    LHashSeparateKVDoubleByteMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    

    


     MutableLHashSeparateKVDoubleByteMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVDoubleByteMap();
    }
     UpdatableLHashSeparateKVDoubleByteMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVDoubleByteMap();
    }
     ImmutableLHashSeparateKVDoubleByteMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVDoubleByteMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVDoubleByteMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVDoubleByteMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVDoubleByteMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMap(
            Map<Double, Byte> map) {
        if (map instanceof DoubleByteMap) {
            if (map instanceof SeparateKVDoubleByteLHash) {
                SeparateKVDoubleByteLHash hash = (SeparateKVDoubleByteLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVDoubleByteMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVDoubleByteMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVDoubleByteMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Double, Byte> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

