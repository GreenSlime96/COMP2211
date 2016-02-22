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


public abstract class QHashParallelKVCharCharMapFactorySO
        extends CharacterQHashFactory 
                        <MutableQHashParallelKVCharCharMapGO>
        implements HashCharCharMapFactory {

    QHashParallelKVCharCharMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashParallelKVCharCharMapGO createNewMutable(
            int expectedSize, char free, char removed) {
        MutableQHashParallelKVCharCharMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashParallelKVCharCharMapGO uninitializedMutableMap() {
        return new MutableQHashParallelKVCharCharMap();
    }
     UpdatableQHashParallelKVCharCharMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashParallelKVCharCharMap();
    }
     ImmutableQHashParallelKVCharCharMapGO uninitializedImmutableMap() {
        return new ImmutableQHashParallelKVCharCharMap();
    }

    @Override
    @Nonnull
    public  MutableQHashParallelKVCharCharMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashParallelKVCharCharMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVCharCharMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashParallelKVCharCharMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVCharCharMapGO newUpdatableMap(
            Map<Character, Character> map) {
        if (map instanceof CharCharMap) {
            if (map instanceof ParallelKVCharCharQHash) {
                ParallelKVCharCharQHash hash = (ParallelKVCharCharQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashParallelKVCharCharMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashParallelKVCharCharMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashParallelKVCharCharMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Character, Character> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

