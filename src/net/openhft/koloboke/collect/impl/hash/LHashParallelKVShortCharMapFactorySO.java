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
import net.openhft.koloboke.collect.map.ShortCharMap;
import net.openhft.koloboke.collect.map.hash.HashShortCharMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class LHashParallelKVShortCharMapFactorySO
        extends ShortLHashFactory 
        implements HashShortCharMapFactory {

    LHashParallelKVShortCharMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            , short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    

    


     MutableLHashParallelKVShortCharMapGO uninitializedMutableMap() {
        return new MutableLHashParallelKVShortCharMap();
    }
     UpdatableLHashParallelKVShortCharMapGO uninitializedUpdatableMap() {
        return new UpdatableLHashParallelKVShortCharMap();
    }
     ImmutableLHashParallelKVShortCharMapGO uninitializedImmutableMap() {
        return new ImmutableLHashParallelKVShortCharMap();
    }

    @Override
    @Nonnull
    public  MutableLHashParallelKVShortCharMapGO newMutableMap(int expectedSize) {
        MutableLHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMap(int expectedSize) {
        UpdatableLHashParallelKVShortCharMapGO map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
    }

    

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMap(
            Map<Short, Character> map) {
        if (map instanceof ShortCharMap) {
            if (map instanceof ParallelKVShortCharLHash) {
                ParallelKVShortCharLHash hash = (ParallelKVShortCharLHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    UpdatableLHashParallelKVShortCharMapGO res = uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashParallelKVShortCharMapGO res = newUpdatableMap(map.size());
            res.putAll(map);
            return res;
        }
        UpdatableLHashParallelKVShortCharMapGO res = newUpdatableMap(map.size());
        for (Map.Entry<Short, Character> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}

