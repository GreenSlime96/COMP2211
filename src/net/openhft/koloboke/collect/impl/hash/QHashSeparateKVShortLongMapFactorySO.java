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


public abstract class QHashSeparateKVShortLongMapFactorySO
        extends ShortQHashFactory 
                        <MutableQHashSeparateKVShortLongMapGO>
        implements HashShortLongMapFactory {

    QHashSeparateKVShortLongMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVShortLongMapGO createNewMutable(
            int expectedSize, short free, short removed) {
        MutableQHashSeparateKVShortLongMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashSeparateKVShortLongMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVShortLongMap();
    }
     UpdatableQHashSeparateKVShortLongMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVShortLongMap();
    }
     ImmutableQHashSeparateKVShortLongMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVShortLongMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVShortLongMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVShortLongMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortLongMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVShortLongMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortLongMapGO newUpdatableMap(
            Map<Short, Long> map) {
        if (map instanceof ShortLongMap) {
            if (map instanceof SeparateKVShortLongQHash) {
                SeparateKVShortLongQHash hash = (SeparateKVShortLongQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVShortLongMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVShortLongMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVShortLongMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Short, Long> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

