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
import net.openhft.koloboke.collect.map.CharIntMap;
import net.openhft.koloboke.collect.map.hash.HashCharIntMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVCharIntMapFactorySO
        extends CharacterQHashFactory 
                        <MutableQHashSeparateKVCharIntMapGO>
        implements HashCharIntMapFactory {

    QHashSeparateKVCharIntMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVCharIntMapGO createNewMutable(
            int expectedSize, char free, char removed) {
        MutableQHashSeparateKVCharIntMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashSeparateKVCharIntMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVCharIntMap();
    }
     UpdatableQHashSeparateKVCharIntMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVCharIntMap();
    }
     ImmutableQHashSeparateKVCharIntMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVCharIntMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVCharIntMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVCharIntMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVCharIntMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMap(
            Map<Character, Integer> map) {
        if (map instanceof CharIntMap) {
            if (map instanceof SeparateKVCharIntQHash) {
                SeparateKVCharIntQHash hash = (SeparateKVCharIntQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVCharIntMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVCharIntMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVCharIntMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

