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
import net.openhft.koloboke.collect.map.ByteFloatMap;
import net.openhft.koloboke.collect.map.hash.HashByteFloatMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVByteFloatMapFactorySO
        extends ByteQHashFactory 
                        <MutableQHashSeparateKVByteFloatMapGO>
        implements HashByteFloatMapFactory {

    QHashSeparateKVByteFloatMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVByteFloatMapGO createNewMutable(
            int expectedSize, byte free, byte removed) {
        MutableQHashSeparateKVByteFloatMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashSeparateKVByteFloatMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVByteFloatMap();
    }
     UpdatableQHashSeparateKVByteFloatMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVByteFloatMap();
    }
     ImmutableQHashSeparateKVByteFloatMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVByteFloatMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVByteFloatMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVByteFloatMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVByteFloatMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMap(
            Map<Byte, Float> map) {
        if (map instanceof ByteFloatMap) {
            if (map instanceof SeparateKVByteFloatQHash) {
                SeparateKVByteFloatQHash hash = (SeparateKVByteFloatQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVByteFloatMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVByteFloatMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVByteFloatMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Byte, Float> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

