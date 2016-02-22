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


public abstract class LHashSeparateKVByteLongMapFactorySO
        extends ByteLHashFactory 
        implements HashByteLongMapFactory {

    LHashSeparateKVByteLongMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashSeparateKVByteLongMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVByteLongMap();
    }
     UpdatableLHashSeparateKVByteLongMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVByteLongMap();
    }
     ImmutableLHashSeparateKVByteLongMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVByteLongMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVByteLongMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVByteLongMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVByteLongMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVByteLongMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVByteLongMapGO newUpdatableMap(
            Map<Byte, Long> map) {
        if (map instanceof ByteLongMap) {
            if (map instanceof SeparateKVByteLongLHash) {
                SeparateKVByteLongLHash hash = (SeparateKVByteLongLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVByteLongMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVByteLongMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVByteLongMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Byte, Long> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

