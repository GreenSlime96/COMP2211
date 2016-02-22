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
import net.openhft.koloboke.collect.map.DoubleIntMap;
import net.openhft.koloboke.collect.map.hash.HashDoubleIntMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVDoubleIntMapFactorySO
        extends DoubleQHashFactory 
        implements HashDoubleIntMapFactory {

    QHashSeparateKVDoubleIntMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    

    


     MutableQHashSeparateKVDoubleIntMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVDoubleIntMap();
    }
     UpdatableQHashSeparateKVDoubleIntMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVDoubleIntMap();
    }
     ImmutableQHashSeparateKVDoubleIntMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVDoubleIntMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVDoubleIntMapGO newMutableMap(int expectedSize) {
        MutableQHashSeparateKVDoubleIntMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleIntMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVDoubleIntMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleIntMapGO newUpdatableMap(
            Map<Double, Integer> map) {
        if (map instanceof DoubleIntMap) {
            if (map instanceof SeparateKVDoubleIntQHash) {
                SeparateKVDoubleIntQHash hash = (SeparateKVDoubleIntQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVDoubleIntMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVDoubleIntMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVDoubleIntMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Double, Integer> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}
