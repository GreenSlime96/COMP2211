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
import net.openhft.koloboke.collect.map.IntDoubleMap;
import net.openhft.koloboke.collect.map.hash.HashIntDoubleMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVIntDoubleMapFactorySO
        extends IntegerQHashFactory 
                        <MutableQHashSeparateKVIntDoubleMapGO>
        implements HashIntDoubleMapFactory {

    QHashSeparateKVIntDoubleMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVIntDoubleMapGO createNewMutable(
            int expectedSize, int free, int removed) {
        MutableQHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashSeparateKVIntDoubleMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVIntDoubleMap();
    }
     UpdatableQHashSeparateKVIntDoubleMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVIntDoubleMap();
    }
     ImmutableQHashSeparateKVIntDoubleMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVIntDoubleMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVIntDoubleMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVIntDoubleMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVIntDoubleMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Map<Integer, Double> map) {
        if (map instanceof IntDoubleMap) {
            if (map instanceof SeparateKVIntDoubleQHash) {
                SeparateKVIntDoubleQHash hash = (SeparateKVIntDoubleQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVIntDoubleMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVIntDoubleMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVIntDoubleMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Integer, Double> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

