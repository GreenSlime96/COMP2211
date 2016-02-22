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
import net.openhft.koloboke.collect.map.IntByteMap;
import net.openhft.koloboke.collect.map.hash.HashIntByteMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVIntByteMapFactorySO
        extends IntegerQHashFactory 
                        <MutableQHashSeparateKVIntByteMapGO>
        implements HashIntByteMapFactory {

    QHashSeparateKVIntByteMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVIntByteMapGO createNewMutable(
            int expectedSize, int free, int removed) {
        MutableQHashSeparateKVIntByteMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashSeparateKVIntByteMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVIntByteMap();
    }
     UpdatableQHashSeparateKVIntByteMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVIntByteMap();
    }
     ImmutableQHashSeparateKVIntByteMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVIntByteMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVIntByteMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVIntByteMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntByteMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVIntByteMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntByteMapGO newUpdatableMap(
            Map<Integer, Byte> map) {
        if (map instanceof IntByteMap) {
            if (map instanceof SeparateKVIntByteQHash) {
                SeparateKVIntByteQHash hash = (SeparateKVIntByteQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVIntByteMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVIntByteMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVIntByteMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Integer, Byte> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

