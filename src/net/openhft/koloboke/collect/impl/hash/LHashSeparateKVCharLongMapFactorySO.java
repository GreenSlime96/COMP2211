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


public abstract class LHashSeparateKVCharLongMapFactorySO
        extends CharacterLHashFactory 
        implements HashCharLongMapFactory {

    LHashSeparateKVCharLongMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashSeparateKVCharLongMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVCharLongMap();
    }
     UpdatableLHashSeparateKVCharLongMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVCharLongMap();
    }
     ImmutableLHashSeparateKVCharLongMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVCharLongMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVCharLongMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVCharLongMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVCharLongMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMap(
            Map<Character, Long> map) {
        if (map instanceof CharLongMap) {
            if (map instanceof SeparateKVCharLongLHash) {
                SeparateKVCharLongLHash hash = (SeparateKVCharLongLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVCharLongMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVCharLongMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVCharLongMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Character, Long> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

