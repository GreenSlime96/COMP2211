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
import net.openhft.koloboke.collect.map.LongObjMap;
import net.openhft.koloboke.collect.map.hash.HashLongObjMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVLongObjMapFactorySO<V>
        extends LongQHashFactory 
                        <MutableQHashSeparateKVLongObjMapGO<V>>
        implements HashLongObjMapFactory<V> {

    QHashSeparateKVLongObjMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVLongObjMapGO<V> createNewMutable(
            int expectedSize, long free, long removed) {
        MutableQHashSeparateKVLongObjMapGO<V> map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

    <V2 extends V> MutableQHashSeparateKVLongObjMapGO<V2> uninitializedMutableMap() {
        return new MutableQHashSeparateKVLongObjMap<V2>();
    }
    <V2 extends V> UpdatableQHashSeparateKVLongObjMapGO<V2> uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVLongObjMap<V2>();
    }
    <V2 extends V> ImmutableQHashSeparateKVLongObjMapGO<V2> uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVLongObjMap<V2>();
    }

    @Override
    @Nonnull
    public <V2 extends V> MutableQHashSeparateKVLongObjMapGO<V2> newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVLongObjMapGO<V2>) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVLongObjMapGO<V2> newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVLongObjMapGO<V2> map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVLongObjMapGO<V2> newUpdatableMap(
            Map<Long, ? extends V2> map) {
        if (map instanceof LongObjMap) {
            if (map instanceof SeparateKVLongObjQHash) {
                SeparateKVLongObjQHash hash = (SeparateKVLongObjQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVLongObjMapGO<V2> res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVLongObjMapGO<V2> res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVLongObjMapGO<V2> res = newUpdatableMap(map.size());
        for (Map.Entry<Long, ? extends V2> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

