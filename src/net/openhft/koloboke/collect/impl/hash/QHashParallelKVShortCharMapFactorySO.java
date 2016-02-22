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
import net.openhft.koloboke.collect.map.ShortCharMap;
import net.openhft.koloboke.collect.map.hash.HashShortCharMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashParallelKVShortCharMapFactorySO
        extends ShortQHashFactory 
                        <MutableQHashParallelKVShortCharMapGO>
        implements HashShortCharMapFactory {

    QHashParallelKVShortCharMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashParallelKVShortCharMapGO createNewMutable(
            int expectedSize, short free, short removed) {
        MutableQHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashParallelKVShortCharMapGO uninitializedMutableMap() {
        return new MutableQHashParallelKVShortCharMap();
    }
     UpdatableQHashParallelKVShortCharMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashParallelKVShortCharMap();
    }
     ImmutableQHashParallelKVShortCharMapGO uninitializedImmutableMap() {
        return new ImmutableQHashParallelKVShortCharMap();
    }

    @Override
    @Nonnull
    public  MutableQHashParallelKVShortCharMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashParallelKVShortCharMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashParallelKVShortCharMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMap(
            Map<Short, Character> map) {
        if (map instanceof ShortCharMap) {
            if (map instanceof ParallelKVShortCharQHash) {
                ParallelKVShortCharQHash hash = (ParallelKVShortCharQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashParallelKVShortCharMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashParallelKVShortCharMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashParallelKVShortCharMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Short, Character> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

