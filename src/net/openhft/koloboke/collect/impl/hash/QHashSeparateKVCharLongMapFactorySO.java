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
import net.openhft.koloboke.collect.map.CharLongMap;
import net.openhft.koloboke.collect.map.hash.HashCharLongMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVCharLongMapFactorySO
        extends CharacterQHashFactory 
                        <MutableQHashSeparateKVCharLongMapGO>
        implements HashCharLongMapFactory {

    QHashSeparateKVCharLongMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVCharLongMapGO createNewMutable(
            int expectedSize, char free, char removed) {
        MutableQHashSeparateKVCharLongMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashSeparateKVCharLongMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVCharLongMap();
    }
     UpdatableQHashSeparateKVCharLongMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVCharLongMap();
    }
     ImmutableQHashSeparateKVCharLongMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVCharLongMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVCharLongMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVCharLongMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharLongMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVCharLongMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharLongMapGO newUpdatableMap(
            Map<Character, Long> map) {
        if (map instanceof CharLongMap) {
            if (map instanceof SeparateKVCharLongQHash) {
                SeparateKVCharLongQHash hash = (SeparateKVCharLongQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVCharLongMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVCharLongMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVCharLongMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Character, Long> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

