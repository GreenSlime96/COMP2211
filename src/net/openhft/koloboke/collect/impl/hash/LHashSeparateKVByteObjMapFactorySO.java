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
import net.openhft.koloboke.collect.map.ByteObjMap;
import net.openhft.koloboke.collect.map.hash.HashByteObjMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashSeparateKVByteObjMapFactorySO<V>
        extends ByteLHashFactory 
        implements HashByteObjMapFactory<V> {

    LHashSeparateKVByteObjMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


    <V2 extends V> MutableLHashSeparateKVByteObjMapGO<V2> uninitializedMutableMap() {
        return new MutableLHashSeparateKVByteObjMap<V2>();
    }
    <V2 extends V> UpdatableLHashSeparateKVByteObjMapGO<V2> uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVByteObjMap<V2>();
    }
    <V2 extends V> ImmutableLHashSeparateKVByteObjMapGO<V2> uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVByteObjMap<V2>();
    }

    @Override
    @Nonnull
    public <V2 extends V> MutableLHashSeparateKVByteObjMapGO<V2> newMutableMap(int expectedSize) {
        MutableLHashSeparateKVByteObjMapGO<V2> map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public <V2 extends V> UpdatableLHashSeparateKVByteObjMapGO<V2> newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVByteObjMapGO<V2> map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public <V2 extends V> UpdatableLHashSeparateKVByteObjMapGO<V2> newUpdatableMap(
            Map<Byte, ? extends V2> map) {
        if (map instanceof ByteObjMap) {
            if (map instanceof SeparateKVByteObjLHash) {
                SeparateKVByteObjLHash hash = (SeparateKVByteObjLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVByteObjMapGO<V2> res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVByteObjMapGO<V2> res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVByteObjMapGO<V2> res = newUpdatableMap(map.size());
        for (Map.Entry<Byte, ? extends V2> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

