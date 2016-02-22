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
import net.openhft.koloboke.collect.map.IntShortMap;
import net.openhft.koloboke.collect.map.hash.HashIntShortMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVIntShortMapFactorySO
        extends IntegerQHashFactory 
                        <MutableQHashSeparateKVIntShortMapGO>
        implements HashIntShortMapFactory {

    QHashSeparateKVIntShortMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVIntShortMapGO createNewMutable(
            int expectedSize, int free, int removed) {
        MutableQHashSeparateKVIntShortMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashSeparateKVIntShortMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVIntShortMap();
    }
     UpdatableQHashSeparateKVIntShortMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVIntShortMap();
    }
     ImmutableQHashSeparateKVIntShortMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVIntShortMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVIntShortMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVIntShortMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntShortMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVIntShortMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntShortMapGO newUpdatableMap(
            Map<Integer, Short> map) {
        if (map instanceof IntShortMap) {
            if (map instanceof SeparateKVIntShortQHash) {
                SeparateKVIntShortQHash hash = (SeparateKVIntShortQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVIntShortMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVIntShortMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVIntShortMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Integer, Short> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

