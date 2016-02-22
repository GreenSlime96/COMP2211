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
import net.openhft.koloboke.collect.map.ShortIntMap;
import net.openhft.koloboke.collect.map.hash.HashShortIntMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class QHashSeparateKVShortIntMapFactorySO
        extends ShortQHashFactory 
                        <MutableQHashSeparateKVShortIntMapGO>
        implements HashShortIntMapFactory {

    QHashSeparateKVShortIntMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVShortIntMapGO createNewMutable(
            int expectedSize, short free, short removed) {
        MutableQHashSeparateKVShortIntMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashSeparateKVShortIntMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVShortIntMap();
    }
     UpdatableQHashSeparateKVShortIntMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVShortIntMap();
    }
     ImmutableQHashSeparateKVShortIntMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVShortIntMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVShortIntMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVShortIntMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortIntMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVShortIntMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortIntMapGO newUpdatableMap(
            Map<Short, Integer> map) {
        if (map instanceof ShortIntMap) {
            if (map instanceof SeparateKVShortIntQHash) {
                SeparateKVShortIntQHash hash = (SeparateKVShortIntQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVShortIntMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVShortIntMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVShortIntMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Short, Integer> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

