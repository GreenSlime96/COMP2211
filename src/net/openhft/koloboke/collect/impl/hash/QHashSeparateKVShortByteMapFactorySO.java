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


public abstract class QHashSeparateKVShortByteMapFactorySO
        extends ShortQHashFactory 
                        <MutableQHashSeparateKVShortByteMapGO>
        implements HashShortByteMapFactory {

    QHashSeparateKVShortByteMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    

    @Override
    MutableQHashSeparateKVShortByteMapGO createNewMutable(
            int expectedSize, short free, short removed) {
        MutableQHashSeparateKVShortByteMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }

     MutableQHashSeparateKVShortByteMapGO uninitializedMutableMap() {
        return new MutableQHashSeparateKVShortByteMap();
    }
     UpdatableQHashSeparateKVShortByteMapGO uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVShortByteMap();
    }
     ImmutableQHashSeparateKVShortByteMapGO uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVShortByteMap();
    }

    @Override
    @Nonnull
    public  MutableQHashSeparateKVShortByteMapGO newMutableMap(int expectedSize) {
        // noinspection unchecked
        return (MutableQHashSeparateKVShortByteMapGO) newMutableHash(expectedSize);
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMap(int expectedSize) {
        UpdatableQHashSeparateKVShortByteMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMap(
            Map<Short, Byte> map) {
        if (map instanceof ShortByteMap) {
            if (map instanceof SeparateKVShortByteQHash) {
                SeparateKVShortByteQHash hash = (SeparateKVShortByteQHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableQHashSeparateKVShortByteMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVShortByteMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableQHashSeparateKVShortByteMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Short, Byte> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

