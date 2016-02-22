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
import net.openhft.koloboke.collect.map.LongFloatMap;
import net.openhft.koloboke.collect.map.hash.HashLongFloatMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashSeparateKVLongFloatMapFactorySO
        extends LongLHashFactory 
        implements HashLongFloatMapFactory {

    LHashSeparateKVLongFloatMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashSeparateKVLongFloatMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVLongFloatMap();
    }
     UpdatableLHashSeparateKVLongFloatMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVLongFloatMap();
    }
     ImmutableLHashSeparateKVLongFloatMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVLongFloatMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVLongFloatMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVLongFloatMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVLongFloatMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMap(
            Map<Long, Float> map) {
        if (map instanceof LongFloatMap) {
            if (map instanceof SeparateKVLongFloatLHash) {
                SeparateKVLongFloatLHash hash = (SeparateKVLongFloatLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVLongFloatMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVLongFloatMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVLongFloatMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Long, Float> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}
