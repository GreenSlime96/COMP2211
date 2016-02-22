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
import net.openhft.koloboke.collect.map.IntIntMap;
import net.openhft.koloboke.collect.map.hash.HashIntIntMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashParallelKVIntIntMapFactorySO
        extends IntegerQHashFactory 
                        <MutableQHashParallelKVIntIntMapGO>
        implements HashIntIntMapFactory {

    QHashParallelKVIntIntMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashParallelKVIntIntMapGO createNewMutable(
            int expectedSize, int free, int removed) {
        MutableQHashParallelKVIntIntMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashParallelKVIntIntMapGO uninitializedMutableMap() {
        return new MutableQHashParallelKVIntIntMap();
    }
     UpdatableQHashParallelKVIntIntMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashParallelKVIntIntMap();
    }
     ImmutableQHashParallelKVIntIntMapGO uninitializedImmutableMap() {
        return new ImmutableQHashParallelKVIntIntMap();
    }

    @Override
    @Nonnull
    public  MutableQHashParallelKVIntIntMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashParallelKVIntIntMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashParallelKVIntIntMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMap(
            Map<Integer, Integer> map) {
        if (map instanceof IntIntMap) {
            if (map instanceof ParallelKVIntIntQHash) {
                ParallelKVIntIntQHash hash = (ParallelKVIntIntQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashParallelKVIntIntMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashParallelKVIntIntMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashParallelKVIntIntMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

