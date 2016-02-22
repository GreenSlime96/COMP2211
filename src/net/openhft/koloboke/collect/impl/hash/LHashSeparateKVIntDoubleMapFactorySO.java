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
import net.openhft.koloboke.collect.map.IntDoubleMap;
import net.openhft.koloboke.collect.map.hash.HashIntDoubleMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashSeparateKVIntDoubleMapFactorySO
        extends IntegerLHashFactory 
        implements HashIntDoubleMapFactory {

    LHashSeparateKVIntDoubleMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashSeparateKVIntDoubleMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVIntDoubleMap();
    }
     UpdatableLHashSeparateKVIntDoubleMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVIntDoubleMap();
    }
     ImmutableLHashSeparateKVIntDoubleMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVIntDoubleMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVIntDoubleMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVIntDoubleMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Map<Integer, Double> map) {
        if (map instanceof IntDoubleMap) {
            if (map instanceof SeparateKVIntDoubleLHash) {
                SeparateKVIntDoubleLHash hash = (SeparateKVIntDoubleLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVIntDoubleMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVIntDoubleMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVIntDoubleMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Integer, Double> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

