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


public abstract class LHashSeparateKVByteCharMapFactorySO
        extends ByteLHashFactory 
        implements HashByteCharMapFactory {

    LHashSeparateKVByteCharMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashSeparateKVByteCharMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVByteCharMap();
    }
     UpdatableLHashSeparateKVByteCharMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVByteCharMap();
    }
     ImmutableLHashSeparateKVByteCharMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVByteCharMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVByteCharMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVByteCharMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVByteCharMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVByteCharMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVByteCharMapGO newUpdatableMap(
            Map<Byte, Character> map) {
        if (map instanceof ByteCharMap) {
            if (map instanceof SeparateKVByteCharLHash) {
                SeparateKVByteCharLHash hash = (SeparateKVByteCharLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVByteCharMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVByteCharMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVByteCharMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Byte, Character> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

