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
import net.openhft.koloboke.collect.map.IntShortMap;
import net.openhft.koloboke.collect.map.hash.HashIntShortMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashSeparateKVIntShortMapFactorySO
        extends IntegerLHashFactory 
        implements HashIntShortMapFactory {

    LHashSeparateKVIntShortMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashSeparateKVIntShortMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVIntShortMap();
    }
     UpdatableLHashSeparateKVIntShortMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVIntShortMap();
    }
     ImmutableLHashSeparateKVIntShortMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVIntShortMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVIntShortMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVIntShortMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVIntShortMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMap(
            Map<Integer, Short> map) {
        if (map instanceof IntShortMap) {
            if (map instanceof SeparateKVIntShortLHash) {
                SeparateKVIntShortLHash hash = (SeparateKVIntShortLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVIntShortMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVIntShortMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVIntShortMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Integer, Short> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

