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
import net.openhft.koloboke.collect.map.LongShortMap;
import net.openhft.koloboke.collect.map.hash.HashLongShortMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashSeparateKVLongShortMapFactorySO
        extends LongLHashFactory 
        implements HashLongShortMapFactory {

    LHashSeparateKVLongShortMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashSeparateKVLongShortMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVLongShortMap();
    }
     UpdatableLHashSeparateKVLongShortMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVLongShortMap();
    }
     ImmutableLHashSeparateKVLongShortMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVLongShortMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVLongShortMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVLongShortMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVLongShortMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMap(
            Map<Long, Short> map) {
        if (map instanceof LongShortMap) {
            if (map instanceof SeparateKVLongShortLHash) {
                SeparateKVLongShortLHash hash = (SeparateKVLongShortLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVLongShortMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVLongShortMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVLongShortMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Long, Short> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

