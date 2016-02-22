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
import net.openhft.koloboke.collect.map.LongShortMap;
import net.openhft.koloboke.collect.map.hash.HashLongShortMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVLongShortMapFactorySO
        extends LongQHashFactory 
                        <MutableQHashSeparateKVLongShortMapGO>
        implements HashLongShortMapFactory {

    QHashSeparateKVLongShortMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVLongShortMapGO createNewMutable(
            int expectedSize, long free, long removed) {
        MutableQHashSeparateKVLongShortMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashSeparateKVLongShortMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVLongShortMap();
    }
     UpdatableQHashSeparateKVLongShortMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVLongShortMap();
    }
     ImmutableQHashSeparateKVLongShortMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVLongShortMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVLongShortMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVLongShortMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongShortMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVLongShortMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongShortMapGO newUpdatableMap(
            Map<Long, Short> map) {
        if (map instanceof LongShortMap) {
            if (map instanceof SeparateKVLongShortQHash) {
                SeparateKVLongShortQHash hash = (SeparateKVLongShortQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVLongShortMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVLongShortMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVLongShortMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Long, Short> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

