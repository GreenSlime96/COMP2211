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
import net.openhft.koloboke.collect.map.LongByteMap;
import net.openhft.koloboke.collect.map.hash.HashLongByteMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVLongByteMapFactorySO
        extends LongQHashFactory 
                        <MutableQHashSeparateKVLongByteMapGO>
        implements HashLongByteMapFactory {

    QHashSeparateKVLongByteMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVLongByteMapGO createNewMutable(
            int expectedSize, long free, long removed) {
        MutableQHashSeparateKVLongByteMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashSeparateKVLongByteMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVLongByteMap();
    }
     UpdatableQHashSeparateKVLongByteMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVLongByteMap();
    }
     ImmutableQHashSeparateKVLongByteMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVLongByteMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVLongByteMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVLongByteMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongByteMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVLongByteMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongByteMapGO newUpdatableMap(
            Map<Long, Byte> map) {
        if (map instanceof LongByteMap) {
            if (map instanceof SeparateKVLongByteQHash) {
                SeparateKVLongByteQHash hash = (SeparateKVLongByteQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVLongByteMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVLongByteMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVLongByteMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Long, Byte> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

