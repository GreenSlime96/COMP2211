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
import net.openhft.koloboke.collect.map.ShortFloatMap;
import net.openhft.koloboke.collect.map.hash.HashShortFloatMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashSeparateKVShortFloatMapFactorySO
        extends ShortLHashFactory 
        implements HashShortFloatMapFactory {

    LHashSeparateKVShortFloatMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashSeparateKVShortFloatMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVShortFloatMap();
    }
     UpdatableLHashSeparateKVShortFloatMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVShortFloatMap();
    }
     ImmutableLHashSeparateKVShortFloatMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVShortFloatMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVShortFloatMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVShortFloatMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortFloatMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVShortFloatMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortFloatMapGO newUpdatableMap(
            Map<Short, Float> map) {
        if (map instanceof ShortFloatMap) {
            if (map instanceof SeparateKVShortFloatLHash) {
                SeparateKVShortFloatLHash hash = (SeparateKVShortFloatLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVShortFloatMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVShortFloatMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVShortFloatMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Short, Float> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

