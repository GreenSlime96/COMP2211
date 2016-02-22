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
import net.openhft.koloboke.collect.map.CharCharMap;
import net.openhft.koloboke.collect.map.hash.HashCharCharMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashParallelKVCharCharMapFactorySO
        extends CharacterLHashFactory 
        implements HashCharCharMapFactory {

    LHashParallelKVCharCharMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashParallelKVCharCharMapGO uninitializedMutableMap() {
        return new MutableLHashParallelKVCharCharMap();
    }
     UpdatableLHashParallelKVCharCharMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashParallelKVCharCharMap();
    }
     ImmutableLHashParallelKVCharCharMapGO uninitializedImmutableMap() {
        return new ImmutableLHashParallelKVCharCharMap();
    }

    @Override
    @Nonnull
    public  MutableLHashParallelKVCharCharMapGO newMutableMap(int expectedSize) {
        MutableLHashParallelKVCharCharMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashParallelKVCharCharMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMap(
            Map<Character, Character> map) {
        if (map instanceof CharCharMap) {
            if (map instanceof ParallelKVCharCharLHash) {
                ParallelKVCharCharLHash hash = (ParallelKVCharCharLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashParallelKVCharCharMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashParallelKVCharCharMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashParallelKVCharCharMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Character, Character> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

