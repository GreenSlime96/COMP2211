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
import net.openhft.koloboke.collect.map.DoubleLongMap;
import net.openhft.koloboke.collect.map.hash.HashDoubleLongMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashParallelKVDoubleLongMapFactorySO
        extends DoubleLHashFactory 
        implements HashDoubleLongMapFactory {

    LHashParallelKVDoubleLongMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    

    


     MutableLHashParallelKVDoubleLongMapGO uninitializedMutableMap() {
        return new MutableLHashParallelKVDoubleLongMap();
    }
     UpdatableLHashParallelKVDoubleLongMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashParallelKVDoubleLongMap();
    }
     ImmutableLHashParallelKVDoubleLongMapGO uninitializedImmutableMap() {
        return new ImmutableLHashParallelKVDoubleLongMap();
    }

    @Override
    @Nonnull
    public  MutableLHashParallelKVDoubleLongMapGO newMutableMap(int expectedSize) {
        MutableLHashParallelKVDoubleLongMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashParallelKVDoubleLongMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMap(
            Map<Double, Long> map) {
        if (map instanceof DoubleLongMap) {
            if (map instanceof ParallelKVDoubleLongLHash) {
                ParallelKVDoubleLongLHash hash = (ParallelKVDoubleLongLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashParallelKVDoubleLongMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashParallelKVDoubleLongMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashParallelKVDoubleLongMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Double, Long> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

