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
import net.openhft.koloboke.collect.map.ShortObjMap;
import net.openhft.koloboke.collect.map.hash.HashShortObjMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVShortObjMapFactorySO<V>
        extends ShortQHashFactory 
                        <MutableQHashSeparateKVShortObjMapGO<V>>
        implements HashShortObjMapFactory<V> {

    QHashSeparateKVShortObjMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVShortObjMapGO<V> createNewMutable(
            int expectedSize, short free, short removed) {
        MutableQHashSeparateKVShortObjMapGO<V> map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

    <V2 extends V> MutableQHashSeparateKVShortObjMapGO<V2> uninitializedMutableMap() {
        return new MutableQHashSeparateKVShortObjMap<V2>();
    }
    <V2 extends V> UpdatableQHashSeparateKVShortObjMapGO<V2> uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVShortObjMap<V2>();
    }
    <V2 extends V> ImmutableQHashSeparateKVShortObjMapGO<V2> uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVShortObjMap<V2>();
    }

    @Override
    @Nonnull
    public <V2 extends V> MutableQHashSeparateKVShortObjMapGO<V2> newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVShortObjMapGO<V2>) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVShortObjMapGO<V2> newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVShortObjMapGO<V2> map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVShortObjMapGO<V2> newUpdatableMap(
            Map<Short, ? extends V2> map) {
        if (map instanceof ShortObjMap) {
            if (map instanceof SeparateKVShortObjQHash) {
                SeparateKVShortObjQHash hash = (SeparateKVShortObjQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVShortObjMapGO<V2> res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVShortObjMapGO<V2> res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVShortObjMapGO<V2> res = newUpdatableMap(map.size());
        for (Map.Entry<Short, ? extends V2> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

