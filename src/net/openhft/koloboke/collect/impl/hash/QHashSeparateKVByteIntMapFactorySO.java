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
import net.openhft.koloboke.collect.map.ByteIntMap;
import net.openhft.koloboke.collect.map.hash.HashByteIntMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVByteIntMapFactorySO
        extends ByteQHashFactory 
                        <MutableQHashSeparateKVByteIntMapGO>
        implements HashByteIntMapFactory {

    QHashSeparateKVByteIntMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVByteIntMapGO createNewMutable(
            int expectedSize, byte free, byte removed) {
        MutableQHashSeparateKVByteIntMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashSeparateKVByteIntMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVByteIntMap();
    }
     UpdatableQHashSeparateKVByteIntMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVByteIntMap();
    }
     ImmutableQHashSeparateKVByteIntMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVByteIntMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVByteIntMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVByteIntMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteIntMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVByteIntMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteIntMapGO newUpdatableMap(
            Map<Byte, Integer> map) {
        if (map instanceof ByteIntMap) {
            if (map instanceof SeparateKVByteIntQHash) {
                SeparateKVByteIntQHash hash = (SeparateKVByteIntQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVByteIntMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVByteIntMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVByteIntMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Byte, Integer> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

