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


public abstract class QHashParallelKVDoubleLongMapFactorySO
        extends DoubleQHashFactory 
        implements HashDoubleLongMapFactory {

    QHashParallelKVDoubleLongMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    

    


     MutableQHashParallelKVDoubleLongMapGO uninitializedMutableMap() {
        return new MutableQHashParallelKVDoubleLongMap();
    }
     UpdatableQHashParallelKVDoubleLongMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashParallelKVDoubleLongMap();
    }
     ImmutableQHashParallelKVDoubleLongMapGO uninitializedImmutableMap() {
        return new ImmutableQHashParallelKVDoubleLongMap();
    }

    @Override
    @Nonnull
    public  MutableQHashParallelKVDoubleLongMapGO newMutableMap(int expectedSize) {
        MutableQHashParallelKVDoubleLongMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVDoubleLongMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashParallelKVDoubleLongMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVDoubleLongMapGO newUpdatableMap(
            Map<Double, Long> map) {
        if (map instanceof DoubleLongMap) {
            if (map instanceof ParallelKVDoubleLongQHash) {
                ParallelKVDoubleLongQHash hash = (ParallelKVDoubleLongQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashParallelKVDoubleLongMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashParallelKVDoubleLongMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashParallelKVDoubleLongMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Double, Long> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

