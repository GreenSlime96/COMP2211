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

import net.openhft.koloboke.collect.*;
import net.openhft.koloboke.collect.hash.*;
import net.openhft.koloboke.collect.map.ObjObjMap;
import net.openhft.koloboke.collect.map.hash.HashObjObjMapFactory;

import javax.annotation.Nonnull;

import java.util.Map;


public abstract class LHashParallelKVObjObjMapFactorySO<K, V>
        extends ObjHashFactorySO<K>
        implements HashObjObjMapFactory<K, V> {

    LHashParallelKVObjObjMapFactorySO(HashConfig hashConf, int defaultExpectedSize,
            boolean isNullKeyAllowed) {
        super(hashConf, defaultExpectedSize, isNullKeyAllowed);
    }

    @Nonnull
    @Override
    public Equivalence<K> getKeyEquivalence() {
        return Equivalence.defaultEquality();
    }

    @Nonnull
    @Override
    Equivalence<K> getEquivalence() {
        return getKeyEquivalence();
    }

    String keySpecialString() {
        return ",keyEquivalence=" + getKeyEquivalence() +
                ",nullKeyAllowed=" + isNullKeyAllowed();
    }

    boolean keySpecialEquals(HashObjObjMapFactory<?, ?> other) {
        return getKeyEquivalence().equals(other.getKeyEquivalence()) &&
                isNullKeyAllowed() == other.isNullKeyAllowed();
    }

    

    

    

    

    

    

    

    <K2 extends K, V2 extends V> MutableLHashParallelKVObjObjMapGO<K2, V2>
    uninitializedMutableMap() {
        return new MutableLHashParallelKVObjObjMap<K2, V2>();
    }
    <K2 extends K, V2 extends V> UpdatableLHashParallelKVObjObjMapGO<K2, V2>
    uninitializedUpdatableMap() {
        return new UpdatableLHashParallelKVObjObjMap<K2, V2>();
    }
    <K2 extends K, V2 extends V> ImmutableLHashParallelKVObjObjMapGO<K2, V2>
    uninitializedImmutableMap() {
        return new ImmutableLHashParallelKVObjObjMap<K2, V2>();
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> MutableLHashParallelKVObjObjMapGO<K2, V2> newMutableMap(
            int expectedSize) {
        MutableLHashParallelKVObjObjMapGO<K2, V2> map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableLHashParallelKVObjObjMapGO<K2, V2> newUpdatableMap(
            int expectedSize) {
        UpdatableLHashParallelKVObjObjMapGO<K2, V2> map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableLHashParallelKVObjObjMapGO<K2, V2> newUpdatableMap(
            Map<? extends K2, ? extends V2> map, int expectedSize) {
        if (map instanceof ObjObjMap) {
            // noinspection unchecked
            ObjObjMap<K2, V2> objObjMap = (ObjObjMap<K2, V2>) map;
            if (map instanceof ParallelKVObjObjLHash) {
                ParallelKVObjObjLHash hash = (ParallelKVObjObjLHash) map;
                if (hash.hashConfig().equals(hashConf) &&
                        objObjMap.keyEquivalence().equals(getKeyEquivalence())) {
                    UpdatableLHashParallelKVObjObjMapGO<K2, V2> res =
                            uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashParallelKVObjObjMapGO<K2, V2> res = newUpdatableMap(expectedSize);
            res.putAll(map);
            return res;
        } else {
            UpdatableLHashParallelKVObjObjMapGO<K2, V2> res = newUpdatableMap(expectedSize);
            for (Map.Entry<? extends K2, ? extends V2> entry : map.entrySet()) {
                res.put(entry.getKey(), entry.getValue());
            }
            return res;
        }
    }
}

