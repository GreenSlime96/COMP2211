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
import net.openhft.koloboke.collect.map.CharByteMap;
import net.openhft.koloboke.collect.map.hash.HashCharByteMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVCharByteMapFactorySO
        extends CharacterQHashFactory 
                        <MutableQHashSeparateKVCharByteMapGO>
        implements HashCharByteMapFactory {

    QHashSeparateKVCharByteMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVCharByteMapGO createNewMutable(
            int expectedSize, char free, char removed) {
        MutableQHashSeparateKVCharByteMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashSeparateKVCharByteMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVCharByteMap();
    }
     UpdatableQHashSeparateKVCharByteMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVCharByteMap();
    }
     ImmutableQHashSeparateKVCharByteMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVCharByteMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVCharByteMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVCharByteMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharByteMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVCharByteMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharByteMapGO newUpdatableMap(
            Map<Character, Byte> map) {
        if (map instanceof CharByteMap) {
            if (map instanceof SeparateKVCharByteQHash) {
                SeparateKVCharByteQHash hash = (SeparateKVCharByteQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVCharByteMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVCharByteMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVCharByteMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Character, Byte> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

