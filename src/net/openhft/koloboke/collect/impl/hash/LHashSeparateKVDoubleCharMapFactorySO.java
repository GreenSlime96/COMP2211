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
import net.openhft.koloboke.collect.map.DoubleCharMap;
import net.openhft.koloboke.collect.map.hash.HashDoubleCharMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashSeparateKVDoubleCharMapFactorySO
        extends DoubleLHashFactory 
        implements HashDoubleCharMapFactory {

    LHashSeparateKVDoubleCharMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    

    


     MutableLHashSeparateKVDoubleCharMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVDoubleCharMap();
    }
     UpdatableLHashSeparateKVDoubleCharMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVDoubleCharMap();
    }
     ImmutableLHashSeparateKVDoubleCharMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVDoubleCharMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVDoubleCharMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVDoubleCharMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVDoubleCharMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMap(
            Map<Double, Character> map) {
        if (map instanceof DoubleCharMap) {
            if (map instanceof SeparateKVDoubleCharLHash) {
                SeparateKVDoubleCharLHash hash = (SeparateKVDoubleCharLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVDoubleCharMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVDoubleCharMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVDoubleCharMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Double, Character> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

