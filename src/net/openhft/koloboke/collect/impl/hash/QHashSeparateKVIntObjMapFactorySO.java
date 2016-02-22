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


public abstract class QHashSeparateKVIntObjMapFactorySO<V>
        extends IntegerQHashFactory 
                        <MutableQHashSeparateKVIntObjMapGO<V>>
        implements HashIntObjMapFactory<V> {

    QHashSeparateKVIntObjMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVIntObjMapGO<V> createNewMutable(
            int expectedSize, int free, int removed) {
        MutableQHashSeparateKVIntObjMapGO<V> map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

    <V2 extends V> MutableQHashSeparateKVIntObjMapGO<V2> uninitializedMutableMap() {
        return new MutableQHashSeparateKVIntObjMap<V2>();
    }
    <V2 extends V> UpdatableQHashSeparateKVIntObjMapGO<V2> uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVIntObjMap<V2>();
    }
    <V2 extends V> ImmutableQHashSeparateKVIntObjMapGO<V2> uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVIntObjMap<V2>();
    }

    @Override
    @Nonnull
    public <V2 extends V> MutableQHashSeparateKVIntObjMapGO<V2> newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVIntObjMapGO<V2>) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVIntObjMapGO<V2> newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVIntObjMapGO<V2> map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVIntObjMapGO<V2> newUpdatableMap(
            Map<Integer, ? extends V2> map) {
        if (map instanceof IntObjMap) {
            if (map instanceof SeparateKVIntObjQHash) {
                SeparateKVIntObjQHash hash = (SeparateKVIntObjQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVIntObjMapGO<V2> res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVIntObjMapGO<V2> res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVIntObjMapGO<V2> res = newUpdatableMap(map.size());
        for (Map.Entry<Integer, ? extends V2> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

