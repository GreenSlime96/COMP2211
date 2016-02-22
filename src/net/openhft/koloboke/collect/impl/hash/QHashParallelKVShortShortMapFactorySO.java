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


public abstract class QHashParallelKVShortShortMapFactorySO
        extends ShortQHashFactory 
                        <MutableQHashParallelKVShortShortMapGO>
        implements HashShortShortMapFactory {

    QHashParallelKVShortShortMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashParallelKVShortShortMapGO createNewMutable(
            int expectedSize, short free, short removed) {
        MutableQHashParallelKVShortShortMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashParallelKVShortShortMapGO uninitializedMutableMap() {
        return new MutableQHashParallelKVShortShortMap();
    }
     UpdatableQHashParallelKVShortShortMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashParallelKVShortShortMap();
    }
     ImmutableQHashParallelKVShortShortMapGO uninitializedImmutableMap() {
        return new ImmutableQHashParallelKVShortShortMap();
    }

    @Override
    @Nonnull
    public  MutableQHashParallelKVShortShortMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashParallelKVShortShortMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVShortShortMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashParallelKVShortShortMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVShortShortMapGO newUpdatableMap(
            Map<Short, Short> map) {
        if (map instanceof ShortShortMap) {
            if (map instanceof ParallelKVShortShortQHash) {
                ParallelKVShortShortQHash hash = (ParallelKVShortShortQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashParallelKVShortShortMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashParallelKVShortShortMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashParallelKVShortShortMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Short, Short> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

