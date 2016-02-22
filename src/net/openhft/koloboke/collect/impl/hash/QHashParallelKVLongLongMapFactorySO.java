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
import net.openhft.koloboke.collect.map.LongLongMap;
import net.openhft.koloboke.collect.map.hash.HashLongLongMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashParallelKVLongLongMapFactorySO
        extends LongQHashFactory 
                        <MutableQHashParallelKVLongLongMapGO>
        implements HashLongLongMapFactory {

    QHashParallelKVLongLongMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashParallelKVLongLongMapGO createNewMutable(
            int expectedSize, long free, long removed) {
        MutableQHashParallelKVLongLongMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashParallelKVLongLongMapGO uninitializedMutableMap() {
        return new MutableQHashParallelKVLongLongMap();
    }
     UpdatableQHashParallelKVLongLongMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashParallelKVLongLongMap();
    }
     ImmutableQHashParallelKVLongLongMapGO uninitializedImmutableMap() {
        return new ImmutableQHashParallelKVLongLongMap();
    }

    @Override
    @Nonnull
    public  MutableQHashParallelKVLongLongMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashParallelKVLongLongMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongLongMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashParallelKVLongLongMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongLongMapGO newUpdatableMap(
            Map<Long, Long> map) {
        if (map instanceof LongLongMap) {
            if (map instanceof ParallelKVLongLongQHash) {
                ParallelKVLongLongQHash hash = (ParallelKVLongLongQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashParallelKVLongLongMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashParallelKVLongLongMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashParallelKVLongLongMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Long, Long> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

