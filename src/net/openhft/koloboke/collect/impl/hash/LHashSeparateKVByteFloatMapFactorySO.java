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


public abstract class LHashSeparateKVByteFloatMapFactorySO
        extends ByteLHashFactory 
        implements HashByteFloatMapFactory {

    LHashSeparateKVByteFloatMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashSeparateKVByteFloatMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVByteFloatMap();
    }
     UpdatableLHashSeparateKVByteFloatMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVByteFloatMap();
    }
     ImmutableLHashSeparateKVByteFloatMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVByteFloatMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVByteFloatMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVByteFloatMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVByteFloatMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVByteFloatMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVByteFloatMapGO newUpdatableMap(
            Map<Byte, Float> map) {
        if (map instanceof ByteFloatMap) {
            if (map instanceof SeparateKVByteFloatLHash) {
                SeparateKVByteFloatLHash hash = (SeparateKVByteFloatLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVByteFloatMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVByteFloatMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVByteFloatMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Byte, Float> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

