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
import net.openhft.koloboke.collect.map.CharShortMap;
import net.openhft.koloboke.collect.map.hash.HashCharShortMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashParallelKVCharShortMapFactorySO
        extends CharacterLHashFactory 
        implements HashCharShortMapFactory {

    LHashParallelKVCharShortMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashParallelKVCharShortMapGO uninitializedMutableMap() {
        return new MutableLHashParallelKVCharShortMap();
    }
     UpdatableLHashParallelKVCharShortMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashParallelKVCharShortMap();
    }
     ImmutableLHashParallelKVCharShortMapGO uninitializedImmutableMap() {
        return new ImmutableLHashParallelKVCharShortMap();
    }

    @Override
    @Nonnull
    public  MutableLHashParallelKVCharShortMapGO newMutableMap(int expectedSize) {
        MutableLHashParallelKVCharShortMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashParallelKVCharShortMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMap(
            Map<Character, Short> map) {
        if (map instanceof CharShortMap) {
            if (map instanceof ParallelKVCharShortLHash) {
                ParallelKVCharShortLHash hash = (ParallelKVCharShortLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashParallelKVCharShortMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashParallelKVCharShortMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashParallelKVCharShortMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Character, Short> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

