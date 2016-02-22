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
import net.openhft.koloboke.collect.map.CharDoubleMap;
import net.openhft.koloboke.collect.map.hash.HashCharDoubleMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashSeparateKVCharDoubleMapFactorySO
        extends CharacterLHashFactory 
        implements HashCharDoubleMapFactory {

    LHashSeparateKVCharDoubleMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashSeparateKVCharDoubleMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVCharDoubleMap();
    }
     UpdatableLHashSeparateKVCharDoubleMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVCharDoubleMap();
    }
     ImmutableLHashSeparateKVCharDoubleMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVCharDoubleMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVCharDoubleMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVCharDoubleMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVCharDoubleMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMap(
            Map<Character, Double> map) {
        if (map instanceof CharDoubleMap) {
            if (map instanceof SeparateKVCharDoubleLHash) {
                SeparateKVCharDoubleLHash hash = (SeparateKVCharDoubleLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVCharDoubleMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVCharDoubleMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVCharDoubleMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Character, Double> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

