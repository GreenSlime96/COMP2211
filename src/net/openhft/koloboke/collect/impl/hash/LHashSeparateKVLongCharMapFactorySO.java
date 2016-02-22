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
import net.openhft.koloboke.collect.map.LongCharMap;
import net.openhft.koloboke.collect.map.hash.HashLongCharMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashSeparateKVLongCharMapFactorySO
        extends LongLHashFactory 
        implements HashLongCharMapFactory {

    LHashSeparateKVLongCharMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashSeparateKVLongCharMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVLongCharMap();
    }
     UpdatableLHashSeparateKVLongCharMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVLongCharMap();
    }
     ImmutableLHashSeparateKVLongCharMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVLongCharMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVLongCharMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVLongCharMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongCharMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVLongCharMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongCharMapGO newUpdatableMap(
            Map<Long, Character> map) {
        if (map instanceof LongCharMap) {
            if (map instanceof SeparateKVLongCharLHash) {
                SeparateKVLongCharLHash hash = (SeparateKVLongCharLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVLongCharMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVLongCharMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVLongCharMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Long, Character> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

