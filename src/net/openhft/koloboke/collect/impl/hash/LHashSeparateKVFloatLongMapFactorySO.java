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
import net.openhft.koloboke.collect.map.FloatLongMap;
import net.openhft.koloboke.collect.map.hash.HashFloatLongMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashSeparateKVFloatLongMapFactorySO
        extends FloatLHashFactory 
        implements HashFloatLongMapFactory {

    LHashSeparateKVFloatLongMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    

    


     MutableLHashSeparateKVFloatLongMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVFloatLongMap();
    }
     UpdatableLHashSeparateKVFloatLongMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVFloatLongMap();
    }
     ImmutableLHashSeparateKVFloatLongMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVFloatLongMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVFloatLongMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVFloatLongMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatLongMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVFloatLongMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatLongMapGO newUpdatableMap(
            Map<Float, Long> map) {
        if (map instanceof FloatLongMap) {
            if (map instanceof SeparateKVFloatLongLHash) {
                SeparateKVFloatLongLHash hash = (SeparateKVFloatLongLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVFloatLongMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVFloatLongMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVFloatLongMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Float, Long> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

