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
import net.openhft.koloboke.collect.map.ObjDoubleMap;
import net.openhft.koloboke.collect.map.hash.HashObjDoubleMapFactory;

import javax.annotation.Nonnull;

import java.util.Map;


public abstract class LHashSeparateKVObjDoubleMapFactorySO<K>
        extends ObjHashFactorySO<K>
        implements HashObjDoubleMapFactory<K> {

    LHashSeparateKVObjDoubleMapFactorySO(HashConfig hashConf, int defaultExpectedSize,
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

    boolean keySpecialEquals(HashObjDoubleMapFactory<?> other) {
        return getKeyEquivalence().equals(other.getKeyEquivalence()) &&
                isNullKeyAllowed() == other.isNullKeyAllowed();
    }

    

    

    

    

    

    

    

    <K2 extends K>
     MutableLHashSeparateKVObjDoubleMapGO<K2>
    uninitializedMutableMap() {
        return new MutableLHashSeparateKVObjDoubleMap<K2>();
    }
    <K2 extends K>
     UpdatableLHashSeparateKVObjDoubleMapGO<K2>
    uninitializedUpdatableMap() {
        return new UpdatableLHashSeparateKVObjDoubleMap<K2>();
    }
    <K2 extends K>
     ImmutableLHashSeparateKVObjDoubleMapGO<K2>
    uninitializedImmutableMap() {
        return new ImmutableLHashSeparateKVObjDoubleMap<K2>();
    }

    @Override
    @Nonnull
    public <K2 extends K>
     MutableLHashSeparateKVObjDoubleMapGO<K2> newMutableMap(
            int expectedSize) {
        MutableLHashSeparateKVObjDoubleMapGO<K2> map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjDoubleMapGO<K2> newUpdatableMap(
            int expectedSize) {
        UpdatableLHashSeparateKVObjDoubleMapGO<K2> map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjDoubleMapGO<K2> newUpdatableMap(
            Map<? extends K2, Double> map, int expectedSize) {
        if (map instanceof ObjDoubleMap) {
            // noinspection unchecked
            ObjDoubleMap<K2> objDoubleMap = (ObjDoubleMap<K2>) map;
            if (map instanceof SeparateKVObjDoubleLHash) {
                SeparateKVObjDoubleLHash hash = (SeparateKVObjDoubleLHash) map;
                if (hash.hashConfig().equals(hashConf) &&
                        objDoubleMap.keyEquivalence().equals(getKeyEquivalence())) {
                    UpdatableLHashSeparateKVObjDoubleMapGO<K2> res =
                            uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableLHashSeparateKVObjDoubleMapGO<K2> res = newUpdatableMap(expectedSize);
            res.putAll(map);
            return res;
        } else {
            UpdatableLHashSeparateKVObjDoubleMapGO<K2> res = newUpdatableMap(expectedSize);
            for (Map.Entry<? extends K2, Double> entry : map.entrySet()) {
                res.put(entry.getKey(), entry.getValue());
            }
            return res;
        }
    }
}

