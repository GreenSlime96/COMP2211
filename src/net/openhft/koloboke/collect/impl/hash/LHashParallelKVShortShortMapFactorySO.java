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
import net.openhft.koloboke.collect.map.ShortShortMap;
import net.openhft.koloboke.collect.map.hash.HashShortShortMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashParallelKVShortShortMapFactorySO
        extends ShortLHashFactory 
        implements HashShortShortMapFactory {

    LHashParallelKVShortShortMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashParallelKVShortShortMapGO uninitializedMutableMap() {
        return new MutableLHashParallelKVShortShortMap();
    }
     UpdatableLHashParallelKVShortShortMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashParallelKVShortShortMap();
    }
     ImmutableLHashParallelKVShortShortMapGO uninitializedImmutableMap() {
        return new ImmutableLHashParallelKVShortShortMap();
    }

    @Override
    @Nonnull
    public  MutableLHashParallelKVShortShortMapGO newMutableMap(int expectedSize) {
        MutableLHashParallelKVShortShortMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashParallelKVShortShortMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMap(
            Map<Short, Short> map) {
        if (map instanceof ShortShortMap) {
            if (map instanceof ParallelKVShortShortLHash) {
                ParallelKVShortShortLHash hash = (ParallelKVShortShortLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashParallelKVShortShortMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashParallelKVShortShortMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashParallelKVShortShortMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Short, Short> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

