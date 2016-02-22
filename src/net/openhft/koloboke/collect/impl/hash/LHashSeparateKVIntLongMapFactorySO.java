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
import net.openhft.koloboke.collect.map.IntLongMap;
import net.openhft.koloboke.collect.map.hash.HashIntLongMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashSeparateKVIntLongMapFactorySO
        extends IntegerLHashFactory 
        implements HashIntLongMapFactory {

    LHashSeparateKVIntLongMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashSeparateKVIntLongMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVIntLongMap();
    }
     UpdatableLHashSeparateKVIntLongMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVIntLongMap();
    }
     ImmutableLHashSeparateKVIntLongMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVIntLongMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVIntLongMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVIntLongMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntLongMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVIntLongMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntLongMapGO newUpdatableMap(
            Map<Integer, Long> map) {
        if (map instanceof IntLongMap) {
            if (map instanceof SeparateKVIntLongLHash) {
                SeparateKVIntLongLHash hash = (SeparateKVIntLongLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVIntLongMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVIntLongMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVIntLongMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Integer, Long> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

