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
import net.openhft.koloboke.collect.map.DoubleDoubleMap;
import net.openhft.koloboke.collect.map.hash.HashDoubleDoubleMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashParallelKVDoubleDoubleMapFactorySO
        extends DoubleQHashFactory 
        implements HashDoubleDoubleMapFactory {

    QHashParallelKVDoubleDoubleMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    

    


     MutableQHashParallelKVDoubleDoubleMapGO uninitializedMutableMap() {
        return new MutableQHashParallelKVDoubleDoubleMap();
    }
     UpdatableQHashParallelKVDoubleDoubleMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashParallelKVDoubleDoubleMap();
    }
     ImmutableQHashParallelKVDoubleDoubleMapGO uninitializedImmutableMap() {
        return new ImmutableQHashParallelKVDoubleDoubleMap();
    }

    @Override
    @Nonnull
    public  MutableQHashParallelKVDoubleDoubleMapGO newMutableMap(int expectedSize) {
        MutableQHashParallelKVDoubleDoubleMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVDoubleDoubleMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashParallelKVDoubleDoubleMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVDoubleDoubleMapGO newUpdatableMap(
            Map<Double, Double> map) {
        if (map instanceof DoubleDoubleMap) {
            if (map instanceof ParallelKVDoubleDoubleQHash) {
                ParallelKVDoubleDoubleQHash hash = (ParallelKVDoubleDoubleQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashParallelKVDoubleDoubleMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashParallelKVDoubleDoubleMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashParallelKVDoubleDoubleMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Double, Double> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

