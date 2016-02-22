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
import net.openhft.koloboke.collect.map.ShortDoubleMap;
import net.openhft.koloboke.collect.map.hash.HashShortDoubleMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashSeparateKVShortDoubleMapFactorySO
        extends ShortLHashFactory 
        implements HashShortDoubleMapFactory {

    LHashSeparateKVShortDoubleMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashSeparateKVShortDoubleMapGO uninitializedMutableMap() {
        return new MutableLHashSeparateKVShortDoubleMap();
    }
     UpdatableLHashSeparateKVShortDoubleMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVShortDoubleMap();
    }
     ImmutableLHashSeparateKVShortDoubleMapGO uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVShortDoubleMap();
    }

    @Override
    @Nonnull
    public  MutableLHashSeparateKVShortDoubleMapGO newMutableMap(int expectedSize) {
        MutableLHashSeparateKVShortDoubleMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortDoubleMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashSeparateKVShortDoubleMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortDoubleMapGO newUpdatableMap(
            Map<Short, Double> map) {
        if (map instanceof ShortDoubleMap) {
            if (map instanceof SeparateKVShortDoubleLHash) {
                SeparateKVShortDoubleLHash hash = (SeparateKVShortDoubleLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashSeparateKVShortDoubleMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVShortDoubleMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashSeparateKVShortDoubleMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Short, Double> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}
