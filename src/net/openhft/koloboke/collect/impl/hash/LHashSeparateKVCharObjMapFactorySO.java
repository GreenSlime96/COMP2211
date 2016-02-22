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
import net.openhft.koloboke.collect.map.CharObjMap;
import net.openhft.koloboke.collect.map.hash.HashCharObjMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashSeparateKVCharObjMapFactorySO<V>
        extends CharacterLHashFactory 
        implements HashCharObjMapFactory<V> {

    LHashSeparateKVCharObjMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


    <V2 extends V> MutableLHashSeparateKVCharObjMapGO<V2> uninitializedMutableMap() {
        return new MutableLHashSeparateKVCharObjMap<V2>();
    }
    <V2 extends V> UpdatableLHashSeparateKVCharObjMapGO<V2> uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVCharObjMap<V2>();
    }
    <V2 extends V> ImmutableLHashSeparateKVCharObjMapGO<V2> uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVCharObjMap<V2>();
    }

    @Override
    @Nonnull
    public <V2 extends V> MutableLHashSeparateKVCharObjMapGO<V2> newMutableMap(int expectedSize) {
        MutableLHashSeparateKVCharObjMapGO<V2> map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public <V2 extends V> UpdatableLHashSeparateKVCharObjMapGO<V2> newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVCharObjMapGO<V2> map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public <V2 extends V> UpdatableLHashSeparateKVCharObjMapGO<V2> newUpdatableMap(
            Map<Character, ? extends V2> map) {
        if (map instanceof CharObjMap) {
            if (map instanceof SeparateKVCharObjLHash) {
                SeparateKVCharObjLHash hash = (SeparateKVCharObjLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVCharObjMapGO<V2> res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVCharObjMapGO<V2> res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVCharObjMapGO<V2> res = newUpdatableMap(map.size());
        for (Map.Entry<Character, ? extends V2> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

