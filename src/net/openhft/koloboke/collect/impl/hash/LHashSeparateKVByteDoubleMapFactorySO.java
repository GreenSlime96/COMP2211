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


public abstract class LHashSeparateKVByteDoubleMapFactorySO
        extends ByteLHashFactory 
        implements HashByteDoubleMapFactory {

    LHashSeparateKVByteDoubleMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashSeparateKVByteDoubleMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVByteDoubleMap();
    }
     UpdatableLHashSeparateKVByteDoubleMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVByteDoubleMap();
    }
     ImmutableLHashSeparateKVByteDoubleMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVByteDoubleMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVByteDoubleMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVByteDoubleMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVByteDoubleMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVByteDoubleMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVByteDoubleMapGO newUpdatableMap(
            Map<Byte, Double> map) {
        if (map instanceof ByteDoubleMap) {
            if (map instanceof SeparateKVByteDoubleLHash) {
                SeparateKVByteDoubleLHash hash = (SeparateKVByteDoubleLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVByteDoubleMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVByteDoubleMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVByteDoubleMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Byte, Double> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

