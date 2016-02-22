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
import net.openhft.koloboke.collect.map.IntCharMap;
import net.openhft.koloboke.collect.map.hash.HashIntCharMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVIntCharMapFactorySO
        extends IntegerQHashFactory 
                        <MutableQHashSeparateKVIntCharMapGO>
        implements HashIntCharMapFactory {

    QHashSeparateKVIntCharMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVIntCharMapGO createNewMutable(
            int expectedSize, int free, int removed) {
        MutableQHashSeparateKVIntCharMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashSeparateKVIntCharMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVIntCharMap();
    }
     UpdatableQHashSeparateKVIntCharMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVIntCharMap();
    }
     ImmutableQHashSeparateKVIntCharMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVIntCharMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVIntCharMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVIntCharMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntCharMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVIntCharMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntCharMapGO newUpdatableMap(
            Map<Integer, Character> map) {
        if (map instanceof IntCharMap) {
            if (map instanceof SeparateKVIntCharQHash) {
                SeparateKVIntCharQHash hash = (SeparateKVIntCharQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVIntCharMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVIntCharMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVIntCharMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Integer, Character> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

