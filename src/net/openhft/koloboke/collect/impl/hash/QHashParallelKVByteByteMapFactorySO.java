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
import net.openhft.koloboke.collect.map.ByteByteMap;
import net.openhft.koloboke.collect.map.hash.HashByteByteMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashParallelKVByteByteMapFactorySO
        extends ByteQHashFactory 
                        <MutableQHashParallelKVByteByteMapGO>
        implements HashByteByteMapFactory {

    QHashParallelKVByteByteMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashParallelKVByteByteMapGO createNewMutable(
            int expectedSize, byte free, byte removed) {
        MutableQHashParallelKVByteByteMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashParallelKVByteByteMapGO uninitializedMutableMap() {
        return new MutableQHashParallelKVByteByteMap();
    }
     UpdatableQHashParallelKVByteByteMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashParallelKVByteByteMap();
    }
     ImmutableQHashParallelKVByteByteMapGO uninitializedImmutableMap() {
        return new ImmutableQHashParallelKVByteByteMap();
    }

    @Override
    @Nonnull
    public  MutableQHashParallelKVByteByteMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashParallelKVByteByteMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVByteByteMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashParallelKVByteByteMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVByteByteMapGO newUpdatableMap(
            Map<Byte, Byte> map) {
        if (map instanceof ByteByteMap) {
            if (map instanceof ParallelKVByteByteQHash) {
                ParallelKVByteByteQHash hash = (ParallelKVByteByteQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashParallelKVByteByteMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashParallelKVByteByteMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashParallelKVByteByteMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Byte, Byte> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

