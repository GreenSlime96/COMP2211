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
import net.openhft.koloboke.collect.map.ShortByteMap;
import net.openhft.koloboke.collect.map.hash.HashShortByteMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashSeparateKVShortByteMapFactorySO
        extends ShortLHashFactory 
        implements HashShortByteMapFactory {

    LHashSeparateKVShortByteMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashSeparateKVShortByteMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVShortByteMap();
    }
     UpdatableLHashSeparateKVShortByteMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVShortByteMap();
    }
     ImmutableLHashSeparateKVShortByteMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVShortByteMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVShortByteMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVShortByteMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortByteMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVShortByteMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortByteMapGO newUpdatableMap(
            Map<Short, Byte> map) {
        if (map instanceof ShortByteMap) {
            if (map instanceof SeparateKVShortByteLHash) {
                SeparateKVShortByteLHash hash = (SeparateKVShortByteLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVShortByteMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVShortByteMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVShortByteMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Short, Byte> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

