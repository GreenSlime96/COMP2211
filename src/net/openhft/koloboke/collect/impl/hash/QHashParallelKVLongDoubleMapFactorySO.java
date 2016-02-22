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
import net.openhft.koloboke.collect.map.LongDoubleMap;
import net.openhft.koloboke.collect.map.hash.HashLongDoubleMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashParallelKVLongDoubleMapFactorySO
        extends LongQHashFactory 
                        <MutableQHashParallelKVLongDoubleMapGO>
        implements HashLongDoubleMapFactory {

    QHashParallelKVLongDoubleMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashParallelKVLongDoubleMapGO createNewMutable(
            int expectedSize, long free, long removed) {
        MutableQHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashParallelKVLongDoubleMapGO uninitializedMutableMap() {
        return new MutableQHashParallelKVLongDoubleMap();
    }
     UpdatableQHashParallelKVLongDoubleMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashParallelKVLongDoubleMap();
    }
     ImmutableQHashParallelKVLongDoubleMapGO uninitializedImmutableMap() {
        return new ImmutableQHashParallelKVLongDoubleMap();
    }

    @Override
    @Nonnull
    public  MutableQHashParallelKVLongDoubleMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashParallelKVLongDoubleMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashParallelKVLongDoubleMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMap(
            Map<Long, Double> map) {
        if (map instanceof LongDoubleMap) {
            if (map instanceof ParallelKVLongDoubleQHash) {
                ParallelKVLongDoubleQHash hash = (ParallelKVLongDoubleQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashParallelKVLongDoubleMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashParallelKVLongDoubleMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashParallelKVLongDoubleMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Long, Double> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

