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


public abstract class LHashParallelKVLongDoubleMapFactorySO
        extends LongLHashFactory 
        implements HashLongDoubleMapFactory {

    LHashParallelKVLongDoubleMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashParallelKVLongDoubleMapGO uninitializedMutableMap() {
        return new MutableLHashParallelKVLongDoubleMap();
    }
     UpdatableLHashParallelKVLongDoubleMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashParallelKVLongDoubleMap();
    }
     ImmutableLHashParallelKVLongDoubleMapGO uninitializedImmutableMap() {
        return new ImmutableLHashParallelKVLongDoubleMap();
    }

    @Override
    @Nonnull
    public  MutableLHashParallelKVLongDoubleMapGO newMutableMap(int expectedSize) {
        MutableLHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashParallelKVLongDoubleMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMap(
            Map<Long, Double> map) {
        if (map instanceof LongDoubleMap) {
            if (map instanceof ParallelKVLongDoubleLHash) {
                ParallelKVLongDoubleLHash hash = (ParallelKVLongDoubleLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashParallelKVLongDoubleMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashParallelKVLongDoubleMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashParallelKVLongDoubleMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Long, Double> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

