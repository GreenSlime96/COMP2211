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
import net.openhft.koloboke.collect.map.IntFloatMap;
import net.openhft.koloboke.collect.map.hash.HashIntFloatMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashParallelKVIntFloatMapFactorySO
        extends IntegerQHashFactory 
                        <MutableQHashParallelKVIntFloatMapGO>
        implements HashIntFloatMapFactory {

    QHashParallelKVIntFloatMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashParallelKVIntFloatMapGO createNewMutable(
            int expectedSize, int free, int removed) {
        MutableQHashParallelKVIntFloatMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashParallelKVIntFloatMapGO uninitializedMutableMap() {
        return new MutableQHashParallelKVIntFloatMap();
    }
     UpdatableQHashParallelKVIntFloatMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashParallelKVIntFloatMap();
    }
     ImmutableQHashParallelKVIntFloatMapGO uninitializedImmutableMap() {
        return new ImmutableQHashParallelKVIntFloatMap();
    }

    @Override
    @Nonnull
    public  MutableQHashParallelKVIntFloatMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashParallelKVIntFloatMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashParallelKVIntFloatMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMap(
            Map<Integer, Float> map) {
        if (map instanceof IntFloatMap) {
            if (map instanceof ParallelKVIntFloatQHash) {
                ParallelKVIntFloatQHash hash = (ParallelKVIntFloatQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashParallelKVIntFloatMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashParallelKVIntFloatMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashParallelKVIntFloatMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Integer, Float> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

