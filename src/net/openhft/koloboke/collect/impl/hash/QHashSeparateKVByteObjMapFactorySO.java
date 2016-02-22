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


public abstract class QHashSeparateKVByteObjMapFactorySO<V>
        extends ByteQHashFactory 
                        <MutableQHashSeparateKVByteObjMapGO<V>>
        implements HashByteObjMapFactory<V> {

    QHashSeparateKVByteObjMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVByteObjMapGO<V> createNewMutable(
            int expectedSize, byte free, byte removed) {
        MutableQHashSeparateKVByteObjMapGO<V> map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

    <V2 extends V> MutableQHashSeparateKVByteObjMapGO<V2> uninitializedMutableMap() {
        return new MutableQHashSeparateKVByteObjMap<V2>();
    }
    <V2 extends V> UpdatableQHashSeparateKVByteObjMapGO<V2> uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVByteObjMap<V2>();
    }
    <V2 extends V> ImmutableQHashSeparateKVByteObjMapGO<V2> uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVByteObjMap<V2>();
    }

    @Override
    @Nonnull
    public <V2 extends V> MutableQHashSeparateKVByteObjMapGO<V2> newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVByteObjMapGO<V2>) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVByteObjMapGO<V2> newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVByteObjMapGO<V2> map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVByteObjMapGO<V2> newUpdatableMap(
            Map<Byte, ? extends V2> map) {
        if (map instanceof ByteObjMap) {
            if (map instanceof SeparateKVByteObjQHash) {
                SeparateKVByteObjQHash hash = (SeparateKVByteObjQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVByteObjMapGO<V2> res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVByteObjMapGO<V2> res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVByteObjMapGO<V2> res = newUpdatableMap(map.size());
        for (Map.Entry<Byte, ? extends V2> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

