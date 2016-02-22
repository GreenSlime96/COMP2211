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
import net.openhft.koloboke.collect.map.ObjShortMap;
import net.openhft.koloboke.collect.map.hash.HashObjShortMapFactory;

import javax.annotation.Nonnull;

import java.util.Map;


public abstract class QHashSeparateKVObjShortMapFactorySO<K>
        extends ObjHashFactorySO<K>
        implements HashObjShortMapFactory<K> {

    QHashSeparateKVObjShortMapFactorySO(HashConfig hashConf, int defaultExpectedSize,
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

    boolean keySpecialEquals(HashObjShortMapFactory<?> other) {
        return getKeyEquivalence().equals(other.getKeyEquivalence()) &&
                isNullKeyAllowed() == other.isNullKeyAllowed();
    }

    

    

    

    

    

    

    

    <K2 extends K>
     MutableQHashSeparateKVObjShortMapGO<K2>
    uninitializedMutableMap() {
        return new MutableQHashSeparateKVObjShortMap<K2>();
    }
    <K2 extends K>
     UpdatableQHashSeparateKVObjShortMapGO<K2>
    uninitializedUpdatableMap() {
        return new UpdatableQHashSeparateKVObjShortMap<K2>();
    }
    <K2 extends K>
     ImmutableQHashSeparateKVObjShortMapGO<K2>
    uninitializedImmutableMap() {
        return new ImmutableQHashSeparateKVObjShortMap<K2>();
    }

    @Override
    @Nonnull
    public <K2 extends K>
     MutableQHashSeparateKVObjShortMapGO<K2> newMutableMap(
            int expectedSize) {
        MutableQHashSeparateKVObjShortMapGO<K2> map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableQHashSeparateKVObjShortMapGO<K2> newUpdatableMap(
            int expectedSize) {
        UpdatableQHashSeparateKVObjShortMapGO<K2> map = uninitializedUpdatableMap();
        map.init(configWrapper, expectedSize);
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableQHashSeparateKVObjShortMapGO<K2> newUpdatableMap(
            Map<? extends K2, Short> map, int expectedSize) {
        if (map instanceof ObjShortMap) {
            // noinspection unchecked
            ObjShortMap<K2> objShortMap = (ObjShortMap<K2>) map;
            if (map instanceof SeparateKVObjShortQHash) {
                SeparateKVObjShortQHash hash = (SeparateKVObjShortQHash) map;
                if (hash.hashConfig().equals(hashConf) &&
                        objShortMap.keyEquivalence().equals(getKeyEquivalence())) {
                    UpdatableQHashSeparateKVObjShortMapGO<K2> res =
                            uninitializedUpdatableMap();
                    res.copy(hash);
                    return res;
                }
            }
            UpdatableQHashSeparateKVObjShortMapGO<K2> res = newUpdatableMap(expectedSize);
            res.putAll(map);
            return res;
        } else {
            UpdatableQHashSeparateKVObjShortMapGO<K2> res = newUpdatableMap(expectedSize);
            for (Map.Entry<? extends K2, Short> entry : map.entrySet()) {
                res.put(entry.getKey(), entry.getValue());
            }
            return res;
        }
    }
}

