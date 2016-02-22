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
import net.openhft.koloboke.collect.map.ByteCharMap;
import net.openhft.koloboke.collect.map.hash.HashByteCharMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVByteCharMapFactorySO
        extends ByteQHashFactory 
                        <MutableQHashSeparateKVByteCharMapGO>
        implements HashByteCharMapFactory {

    QHashSeparateKVByteCharMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVByteCharMapGO createNewMutable(
            int expectedSize, byte free, byte removed) {
        MutableQHashSeparateKVByteCharMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashSeparateKVByteCharMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVByteCharMap();
    }
     UpdatableQHashSeparateKVByteCharMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVByteCharMap();
    }
     ImmutableQHashSeparateKVByteCharMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVByteCharMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVByteCharMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVByteCharMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVByteCharMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMap(
            Map<Byte, Character> map) {
        if (map instanceof ByteCharMap) {
            if (map instanceof SeparateKVByteCharQHash) {
                SeparateKVByteCharQHash hash = (SeparateKVByteCharQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVByteCharMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVByteCharMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVByteCharMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Byte, Character> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

