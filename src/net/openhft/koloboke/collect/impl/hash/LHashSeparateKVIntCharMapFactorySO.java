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
import net.openhft.koloboke.collect.map.IntCharMap;
import net.openhft.koloboke.collect.map.hash.HashIntCharMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashSeparateKVIntCharMapFactorySO
        extends IntegerLHashFactory 
        implements HashIntCharMapFactory {

    LHashSeparateKVIntCharMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashSeparateKVIntCharMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVIntCharMap();
    }
     UpdatableLHashSeparateKVIntCharMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVIntCharMap();
    }
     ImmutableLHashSeparateKVIntCharMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVIntCharMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVIntCharMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVIntCharMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVIntCharMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMap(
            Map<Integer, Character> map) {
        if (map instanceof IntCharMap) {
            if (map instanceof SeparateKVIntCharLHash) {
                SeparateKVIntCharLHash hash = (SeparateKVIntCharLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVIntCharMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVIntCharMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVIntCharMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Integer, Character> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

