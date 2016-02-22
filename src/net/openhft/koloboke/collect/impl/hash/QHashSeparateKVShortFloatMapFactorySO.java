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


public abstract class QHashSeparateKVShortFloatMapFactorySO
        extends ShortQHashFactory 
                        <MutableQHashSeparateKVShortFloatMapGO>
        implements HashShortFloatMapFactory {

    QHashSeparateKVShortFloatMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVShortFloatMapGO createNewMutable(
            int expectedSize, short free, short removed) {
        MutableQHashSeparateKVShortFloatMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashSeparateKVShortFloatMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVShortFloatMap();
    }
     UpdatableQHashSeparateKVShortFloatMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVShortFloatMap();
    }
     ImmutableQHashSeparateKVShortFloatMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVShortFloatMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVShortFloatMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVShortFloatMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortFloatMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVShortFloatMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortFloatMapGO newUpdatableMap(
            Map<Short, Float> map) {
        if (map instanceof ShortFloatMap) {
            if (map instanceof SeparateKVShortFloatQHash) {
                SeparateKVShortFloatQHash hash = (SeparateKVShortFloatQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVShortFloatMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVShortFloatMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVShortFloatMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Short, Float> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

