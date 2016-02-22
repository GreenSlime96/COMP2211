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
import net.openhft.koloboke.collect.map.ByteDoubleMap;
import net.openhft.koloboke.collect.map.hash.HashByteDoubleMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVByteDoubleMapFactorySO
        extends ByteQHashFactory 
                        <MutableQHashSeparateKVByteDoubleMapGO>
        implements HashByteDoubleMapFactory {

    QHashSeparateKVByteDoubleMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVByteDoubleMapGO createNewMutable(
            int expectedSize, byte free, byte removed) {
        MutableQHashSeparateKVByteDoubleMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashSeparateKVByteDoubleMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVByteDoubleMap();
    }
     UpdatableQHashSeparateKVByteDoubleMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVByteDoubleMap();
    }
     ImmutableQHashSeparateKVByteDoubleMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVByteDoubleMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVByteDoubleMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVByteDoubleMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVByteDoubleMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMap(
            Map<Byte, Double> map) {
        if (map instanceof ByteDoubleMap) {
            if (map instanceof SeparateKVByteDoubleQHash) {
                SeparateKVByteDoubleQHash hash = (SeparateKVByteDoubleQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVByteDoubleMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVByteDoubleMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVByteDoubleMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Byte, Double> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

