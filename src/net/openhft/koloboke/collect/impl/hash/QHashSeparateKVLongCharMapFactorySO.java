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
import net.openhft.koloboke.collect.map.LongCharMap;
import net.openhft.koloboke.collect.map.hash.HashLongCharMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVLongCharMapFactorySO
        extends LongQHashFactory 
                        <MutableQHashSeparateKVLongCharMapGO>
        implements HashLongCharMapFactory {

    QHashSeparateKVLongCharMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVLongCharMapGO createNewMutable(
            int expectedSize, long free, long removed) {
        MutableQHashSeparateKVLongCharMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashSeparateKVLongCharMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVLongCharMap();
    }
     UpdatableQHashSeparateKVLongCharMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVLongCharMap();
    }
     ImmutableQHashSeparateKVLongCharMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVLongCharMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVLongCharMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVLongCharMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVLongCharMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMap(
            Map<Long, Character> map) {
        if (map instanceof LongCharMap) {
            if (map instanceof SeparateKVLongCharQHash) {
                SeparateKVLongCharQHash hash = (SeparateKVLongCharQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVLongCharMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVLongCharMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVLongCharMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Long, Character> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

