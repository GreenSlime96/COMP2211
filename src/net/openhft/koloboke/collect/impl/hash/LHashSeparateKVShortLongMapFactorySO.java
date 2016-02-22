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
import net.openhft.koloboke.collect.map.ShortLongMap;
import net.openhft.koloboke.collect.map.hash.HashShortLongMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashSeparateKVShortLongMapFactorySO
        extends ShortLHashFactory 
        implements HashShortLongMapFactory {

    LHashSeparateKVShortLongMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashSeparateKVShortLongMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVShortLongMap();
    }
     UpdatableLHashSeparateKVShortLongMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVShortLongMap();
    }
     ImmutableLHashSeparateKVShortLongMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVShortLongMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVShortLongMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVShortLongMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVShortLongMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMap(
            Map<Short, Long> map) {
        if (map instanceof ShortLongMap) {
            if (map instanceof SeparateKVShortLongLHash) {
                SeparateKVShortLongLHash hash = (SeparateKVShortLongLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVShortLongMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVShortLongMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVShortLongMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Short, Long> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}
