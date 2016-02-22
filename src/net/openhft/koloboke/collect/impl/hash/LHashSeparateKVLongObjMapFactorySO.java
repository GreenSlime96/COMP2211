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


public abstract class LHashSeparateKVLongObjMapFactorySO<V>
        extends LongLHashFactory 
        implements HashLongObjMapFactory<V> {

    LHashSeparateKVLongObjMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


    <V2 extends V> MutableLHashSeparateKVLongObjMapGO<V2> uninitializedMutableMap() {
        return new MutableLHashSeparateKVLongObjMap<V2>();
    }
    <V2 extends V> UpdatableLHashSeparateKVLongObjMapGO<V2> uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVLongObjMap<V2>();
    }
    <V2 extends V> ImmutableLHashSeparateKVLongObjMapGO<V2> uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVLongObjMap<V2>();
    }

    @Override
    @Nonnull
    public <V2 extends V> MutableLHashSeparateKVLongObjMapGO<V2> newMutableMap(int expectedSize) {
        MutableLHashSeparateKVLongObjMapGO<V2> map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public <V2 extends V> UpdatableLHashSeparateKVLongObjMapGO<V2> newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVLongObjMapGO<V2> map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public <V2 extends V> UpdatableLHashSeparateKVLongObjMapGO<V2> newUpdatableMap(
            Map<Long, ? extends V2> map) {
        if (map instanceof LongObjMap) {
            if (map instanceof SeparateKVLongObjLHash) {
                SeparateKVLongObjLHash hash = (SeparateKVLongObjLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVLongObjMapGO<V2> res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVLongObjMapGO<V2> res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVLongObjMapGO<V2> res = newUpdatableMap(map.size());
        for (Map.Entry<Long, ? extends V2> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

