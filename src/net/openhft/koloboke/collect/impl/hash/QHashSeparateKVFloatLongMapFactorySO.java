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


public abstract class QHashSeparateKVFloatLongMapFactorySO
        extends FloatQHashFactory 
        implements HashFloatLongMapFactory {

    QHashSeparateKVFloatLongMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    

    


     MutableQHashSeparateKVFloatLongMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVFloatLongMap();
    }
     UpdatableQHashSeparateKVFloatLongMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVFloatLongMap();
    }
     ImmutableQHashSeparateKVFloatLongMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVFloatLongMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVFloatLongMapGO newMutableMap(int expectedSize) {
        MutableQHashSeparateKVFloatLongMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVFloatLongMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMap(
            Map<Float, Long> map) {
        if (map instanceof FloatLongMap) {
            if (map instanceof SeparateKVFloatLongQHash) {
                SeparateKVFloatLongQHash hash = (SeparateKVFloatLongQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVFloatLongMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVFloatLongMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVFloatLongMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Float, Long> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

