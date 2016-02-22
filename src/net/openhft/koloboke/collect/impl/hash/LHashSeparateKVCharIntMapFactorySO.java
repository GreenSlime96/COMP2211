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


public abstract class LHashSeparateKVCharIntMapFactorySO
        extends CharacterLHashFactory 
        implements HashCharIntMapFactory {

    LHashSeparateKVCharIntMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashSeparateKVCharIntMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVCharIntMap();
    }
     UpdatableLHashSeparateKVCharIntMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVCharIntMap();
    }
     ImmutableLHashSeparateKVCharIntMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVCharIntMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVCharIntMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVCharIntMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharIntMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVCharIntMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharIntMapGO newUpdatableMap(
            Map<Character, Integer> map) {
        if (map instanceof CharIntMap) {
            if (map instanceof SeparateKVCharIntLHash) {
                SeparateKVCharIntLHash hash = (SeparateKVCharIntLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVCharIntMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVCharIntMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVCharIntMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

