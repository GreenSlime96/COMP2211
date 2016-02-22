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
import net.openhft.koloboke.collect.map.LongIntMap;
import net.openhft.koloboke.collect.map.hash.HashLongIntMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVLongIntMapFactorySO
        extends LongQHashFactory 
                        <MutableQHashSeparateKVLongIntMapGO>
        implements HashLongIntMapFactory {

    QHashSeparateKVLongIntMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVLongIntMapGO createNewMutable(
            int expectedSize, long free, long removed) {
        MutableQHashSeparateKVLongIntMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashSeparateKVLongIntMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVLongIntMap();
    }
     UpdatableQHashSeparateKVLongIntMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVLongIntMap();
    }
     ImmutableQHashSeparateKVLongIntMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVLongIntMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVLongIntMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVLongIntMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVLongIntMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMap(
            Map<Long, Integer> map) {
        if (map instanceof LongIntMap) {
            if (map instanceof SeparateKVLongIntQHash) {
                SeparateKVLongIntQHash hash = (SeparateKVLongIntQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVLongIntMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVLongIntMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVLongIntMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Long, Integer> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

