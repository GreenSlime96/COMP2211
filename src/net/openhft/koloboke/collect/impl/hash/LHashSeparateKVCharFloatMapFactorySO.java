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


public abstract class LHashSeparateKVCharFloatMapFactorySO
        extends CharacterLHashFactory 
        implements HashCharFloatMapFactory {

    LHashSeparateKVCharFloatMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashSeparateKVCharFloatMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVCharFloatMap();
    }
     UpdatableLHashSeparateKVCharFloatMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVCharFloatMap();
    }
     ImmutableLHashSeparateKVCharFloatMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVCharFloatMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVCharFloatMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVCharFloatMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharFloatMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVCharFloatMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharFloatMapGO newUpdatableMap(
            Map<Character, Float> map) {
        if (map instanceof CharFloatMap) {
            if (map instanceof SeparateKVCharFloatLHash) {
                SeparateKVCharFloatLHash hash = (SeparateKVCharFloatLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVCharFloatMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVCharFloatMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVCharFloatMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Character, Float> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

