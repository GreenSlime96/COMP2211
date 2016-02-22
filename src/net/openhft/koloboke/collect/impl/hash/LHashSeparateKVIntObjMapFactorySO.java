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
import net.openhft.koloboke.collect.map.IntObjMap;
import net.openhft.koloboke.collect.map.hash.HashIntObjMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashSeparateKVIntObjMapFactorySO<V>
        extends IntegerLHashFactory 
        implements HashIntObjMapFactory<V> {

    LHashSeparateKVIntObjMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


    <V2 extends V> MutableLHashSeparateKVIntObjMapGO<V2> uninitializedMutableMap() {
        return new MutableLHashSeparateKVIntObjMap<V2>();
    }
    <V2 extends V> UpdatableLHashSeparateKVIntObjMapGO<V2> uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVIntObjMap<V2>();
    }
    <V2 extends V> ImmutableLHashSeparateKVIntObjMapGO<V2> uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVIntObjMap<V2>();
    }

    @Override
    @Nonnull
    public <V2 extends V> MutableLHashSeparateKVIntObjMapGO<V2> newMutableMap(int expectedSize) {
        MutableLHashSeparateKVIntObjMapGO<V2> map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public <V2 extends V> UpdatableLHashSeparateKVIntObjMapGO<V2> newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVIntObjMapGO<V2> map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public <V2 extends V> UpdatableLHashSeparateKVIntObjMapGO<V2> newUpdatableMap(
            Map<Integer, ? extends V2> map) {
        if (map instanceof IntObjMap) {
            if (map instanceof SeparateKVIntObjLHash) {
                SeparateKVIntObjLHash hash = (SeparateKVIntObjLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVIntObjMapGO<V2> res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVIntObjMapGO<V2> res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVIntObjMapGO<V2> res = newUpdatableMap(map.size());
        for (Map.Entry<Integer, ? extends V2> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

