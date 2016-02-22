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
import net.openhft.koloboke.collect.map.CharFloatMap;
import net.openhft.koloboke.collect.map.hash.HashCharFloatMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVCharFloatMapFactorySO
        extends CharacterQHashFactory 
                        <MutableQHashSeparateKVCharFloatMapGO>
        implements HashCharFloatMapFactory {

    QHashSeparateKVCharFloatMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVCharFloatMapGO createNewMutable(
            int expectedSize, char free, char removed) {
        MutableQHashSeparateKVCharFloatMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashSeparateKVCharFloatMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVCharFloatMap();
    }
     UpdatableQHashSeparateKVCharFloatMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVCharFloatMap();
    }
     ImmutableQHashSeparateKVCharFloatMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVCharFloatMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVCharFloatMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVCharFloatMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharFloatMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVCharFloatMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharFloatMapGO newUpdatableMap(
            Map<Character, Float> map) {
        if (map instanceof CharFloatMap) {
            if (map instanceof SeparateKVCharFloatQHash) {
                SeparateKVCharFloatQHash hash = (SeparateKVCharFloatQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVCharFloatMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVCharFloatMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVCharFloatMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Character, Float> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

