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
import net.openhft.koloboke.collect.map.ByteShortMap;
import net.openhft.koloboke.collect.map.hash.HashByteShortMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashSeparateKVByteShortMapFactorySO
        extends ByteLHashFactory 
        implements HashByteShortMapFactory {

    LHashSeparateKVByteShortMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashSeparateKVByteShortMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVByteShortMap();
    }
     UpdatableLHashSeparateKVByteShortMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVByteShortMap();
    }
     ImmutableLHashSeparateKVByteShortMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVByteShortMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVByteShortMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVByteShortMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVByteShortMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVByteShortMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVByteShortMapGO newUpdatableMap(
            Map<Byte, Short> map) {
        if (map instanceof ByteShortMap) {
            if (map instanceof SeparateKVByteShortLHash) {
                SeparateKVByteShortLHash hash = (SeparateKVByteShortLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVByteShortMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVByteShortMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVByteShortMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Byte, Short> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

