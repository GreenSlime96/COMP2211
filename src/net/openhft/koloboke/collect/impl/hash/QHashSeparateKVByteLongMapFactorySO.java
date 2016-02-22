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
import net.openhft.koloboke.collect.map.ByteLongMap;
import net.openhft.koloboke.collect.map.hash.HashByteLongMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVByteLongMapFactorySO
        extends ByteQHashFactory 
                        <MutableQHashSeparateKVByteLongMapGO>
        implements HashByteLongMapFactory {

    QHashSeparateKVByteLongMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVByteLongMapGO createNewMutable(
            int expectedSize, byte free, byte removed) {
        MutableQHashSeparateKVByteLongMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashSeparateKVByteLongMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVByteLongMap();
    }
     UpdatableQHashSeparateKVByteLongMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVByteLongMap();
    }
     ImmutableQHashSeparateKVByteLongMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVByteLongMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVByteLongMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVByteLongMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVByteLongMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMap(
            Map<Byte, Long> map) {
        if (map instanceof ByteLongMap) {
            if (map instanceof SeparateKVByteLongQHash) {
                SeparateKVByteLongQHash hash = (SeparateKVByteLongQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVByteLongMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVByteLongMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVByteLongMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Byte, Long> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

