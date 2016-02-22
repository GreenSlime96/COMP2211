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
import net.openhft.koloboke.collect.map.DoubleShortMap;
import net.openhft.koloboke.collect.map.hash.HashDoubleShortMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashSeparateKVDoubleShortMapFactorySO
        extends DoubleLHashFactory 
        implements HashDoubleShortMapFactory {

    LHashSeparateKVDoubleShortMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    

    


     MutableLHashSeparateKVDoubleShortMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVDoubleShortMap();
    }
     UpdatableLHashSeparateKVDoubleShortMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVDoubleShortMap();
    }
     ImmutableLHashSeparateKVDoubleShortMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVDoubleShortMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVDoubleShortMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVDoubleShortMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleShortMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVDoubleShortMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleShortMapGO newUpdatableMap(
            Map<Double, Short> map) {
        if (map instanceof DoubleShortMap) {
            if (map instanceof SeparateKVDoubleShortLHash) {
                SeparateKVDoubleShortLHash hash = (SeparateKVDoubleShortLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVDoubleShortMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVDoubleShortMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVDoubleShortMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Double, Short> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}
