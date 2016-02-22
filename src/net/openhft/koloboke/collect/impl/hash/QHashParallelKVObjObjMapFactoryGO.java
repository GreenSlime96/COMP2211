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
import net.openhft.koloboke.collect.impl.*;
import net.openhft.koloboke.collect.map.hash.HashObjObjMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashParallelKVObjObjMapFactoryGO<K, V>
        extends QHashParallelKVObjObjMapFactorySO<K, V> {

    QHashParallelKVObjObjMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , boolean isNullKeyAllowed
            ) {
        super(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }

    

    abstract HashObjObjMapFactory<K, V> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            );

    abstract HashObjObjMapFactory<K, V> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            );

    abstract HashObjObjMapFactory<K, V> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            );

    @Override
    public final HashObjObjMapFactory<K, V> withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            , isNullKeyAllowed());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            , isNullKeyAllowed());
    }

    @Override
    public final HashObjObjMapFactory<K, V> withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                , isNullKeyAllowed());
    }

    @Override
    public final HashObjObjMapFactory<K, V> withNullKeyAllowed(boolean nullKeyAllowed) {
        if (nullKeyAllowed == isNullKeyAllowed())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), nullKeyAllowed);
    }

    @Override
    public String toString() {
        return "HashObjObjMapFactory[" + commonString() + keySpecialString() +
                ",valueEquivalence=" + getValueEquivalence() +
                
        "]";
    }

    @Override
    public int hashCode() {
        int hashCode = keySpecialHashCode(commonHashCode());
        hashCode = hashCode * 31 + getValueEquivalence().hashCode();
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof HashObjObjMapFactory) {
            HashObjObjMapFactory factory = (HashObjObjMapFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory) &&
                    getValueEquivalence().equals(factory.getValueEquivalence())
;
        } else {
            return false;
        }
    }

    @Override
    @Nonnull
    public Equivalence<V> getValueEquivalence() {
        return Equivalence.defaultEquality();
    }

    

    

    

    

    

    

    
    

    
    

    private <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> shrunk(
            UpdatableQHashParallelKVObjObjMapGO<K2, V2> map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> MutableQHashParallelKVObjObjMapGO<K2, V2> newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMap(
            Map<? extends K2, ? extends V2> map) {
        return newUpdatableMap(map, map.size());
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMap(
            Map<? extends K2, ? extends V2> map1, Map<? extends K2, ? extends V2> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMap(
            Map<? extends K2, ? extends V2> map1, Map<? extends K2, ? extends V2> map2,
            Map<? extends K2, ? extends V2> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMap(
            Map<? extends K2, ? extends V2> map1, Map<? extends K2, ? extends V2> map2,
            Map<? extends K2, ? extends V2> map3, Map<? extends K2, ? extends V2> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMap(
            Map<? extends K2, ? extends V2> map1, Map<? extends K2, ? extends V2> map2,
            Map<? extends K2, ? extends V2> map3, Map<? extends K2, ? extends V2> map4,
            Map<? extends K2, ? extends V2> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMap(
            Map<? extends K2, ? extends V2> map, int expectedSize) {
        return shrunk(super.newUpdatableMap(map, expectedSize));
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMap(
            Map<? extends K2, ? extends V2> map1, Map<? extends K2, ? extends V2> map2,
            int expectedSize) {
        UpdatableQHashParallelKVObjObjMapGO<K2, V2> map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMap(
            Map<? extends K2, ? extends V2> map1, Map<? extends K2, ? extends V2> map2,
            Map<? extends K2, ? extends V2> map3, int expectedSize) {
        UpdatableQHashParallelKVObjObjMapGO<K2, V2> map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMap(
            Map<? extends K2, ? extends V2> map1, Map<? extends K2, ? extends V2> map2,
            Map<? extends K2, ? extends V2> map3, Map<? extends K2, ? extends V2> map4,
            int expectedSize) {
        UpdatableQHashParallelKVObjObjMapGO<K2, V2> map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMap(
            Map<? extends K2, ? extends V2> map1, Map<? extends K2, ? extends V2> map2,
            Map<? extends K2, ? extends V2> map3, Map<? extends K2, ? extends V2> map4,
            Map<? extends K2, ? extends V2> map5, int expectedSize) {
        UpdatableQHashParallelKVObjObjMapGO<K2, V2> map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMap(
            Consumer<net.openhft.koloboke.function.BiConsumer<K2, V2>> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMap(
            Consumer<net.openhft.koloboke.function.BiConsumer<K2, V2>> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashParallelKVObjObjMapGO<K2, V2> map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.BiConsumer<K2, V2>() {
             @Override
             public void accept(K2 k, V2 v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMap(
            K2[] keys, V2[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMap(
            K2[] keys, V2[] values, int expectedSize) {
        UpdatableQHashParallelKVObjObjMapGO<K2, V2> map = newUpdatableMap(expectedSize);
        int keysLen = keys.length;
        if (keysLen != values.length)
            throw new IllegalArgumentException("keys and values arrays must have the same size");
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }
        return shrunk(map);
    }


    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMap(
            Iterable<? extends K2> keys, Iterable<? extends V2> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMap(
            Iterable<? extends K2> keys, Iterable<? extends V2> values, int expectedSize) {
        UpdatableQHashParallelKVObjObjMapGO<K2, V2> map = newUpdatableMap(expectedSize);
        Iterator<? extends K2> keysIt = keys.iterator();
        Iterator<? extends V2> valuesIt = values.iterator();
        try {
            while (keysIt.hasNext()) {
                map.put(keysIt.next(), valuesIt.next());
            }
            return shrunk(map);
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException(
                    "keys and values iterables must have the same size", e);
        }
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMapOf(
            K2 k1, V2 v1) {
        UpdatableQHashParallelKVObjObjMapGO<K2, V2> map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMapOf(
            K2 k1, V2 v1, K2 k2, V2 v2) {
        UpdatableQHashParallelKVObjObjMapGO<K2, V2> map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMapOf(
            K2 k1, V2 v1, K2 k2, V2 v2,
            K2 k3, V2 v3) {
        UpdatableQHashParallelKVObjObjMapGO<K2, V2> map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMapOf(
            K2 k1, V2 v1, K2 k2, V2 v2,
            K2 k3, V2 v3, K2 k4, V2 v4) {
        UpdatableQHashParallelKVObjObjMapGO<K2, V2> map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> UpdatableQHashParallelKVObjObjMapGO<K2, V2> newUpdatableMapOf(
            K2 k1, V2 v1, K2 k2, V2 v2,
            K2 k3, V2 v3, K2 k4, V2 v4,
            K2 k5, V2 v5) {
        UpdatableQHashParallelKVObjObjMapGO<K2, V2> map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newMutableMap(
            Map<? extends K2, ? extends V2> map, int expectedSize) {
        MutableQHashParallelKVObjObjMapGO<K2, V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newMutableMap(Map<? extends K2, ? extends V2> map1,
            Map<? extends K2, ? extends V2> map2, int expectedSize) {
        MutableQHashParallelKVObjObjMapGO<K2, V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newMutableMap(Map<? extends K2, ? extends V2> map1,
            Map<? extends K2, ? extends V2> map2, Map<? extends K2, ? extends V2> map3, int expectedSize) {
        MutableQHashParallelKVObjObjMapGO<K2, V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newMutableMap(Map<? extends K2, ? extends V2> map1,
            Map<? extends K2, ? extends V2> map2, Map<? extends K2, ? extends V2> map3,
            Map<? extends K2, ? extends V2> map4, int expectedSize) {
        MutableQHashParallelKVObjObjMapGO<K2, V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newMutableMap(Map<? extends K2, ? extends V2> map1,
            Map<? extends K2, ? extends V2> map2, Map<? extends K2, ? extends V2> map3,
            Map<? extends K2, ? extends V2> map4, Map<? extends K2, ? extends V2> map5, int expectedSize) {
        MutableQHashParallelKVObjObjMapGO<K2, V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newMutableMap(
            Consumer<net.openhft.koloboke.function.BiConsumer<K2, V2>> entriesSupplier
            , int expectedSize) {
        MutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newMutableMap(K2[] keys,
            V2[] values, int expectedSize) {
        MutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }


    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newMutableMap(Iterable<? extends K2> keys,
            Iterable<? extends V2> values, int expectedSize) {
        MutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newMutableMap(
            Map<? extends K2, ? extends V2> map) {
        MutableQHashParallelKVObjObjMapGO<K2, V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newMutableMap(Map<? extends K2, ? extends V2> map1,
            Map<? extends K2, ? extends V2> map2) {
        MutableQHashParallelKVObjObjMapGO<K2, V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newMutableMap(Map<? extends K2, ? extends V2> map1,
            Map<? extends K2, ? extends V2> map2, Map<? extends K2, ? extends V2> map3) {
        MutableQHashParallelKVObjObjMapGO<K2, V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newMutableMap(Map<? extends K2, ? extends V2> map1,
            Map<? extends K2, ? extends V2> map2, Map<? extends K2, ? extends V2> map3,
            Map<? extends K2, ? extends V2> map4) {
        MutableQHashParallelKVObjObjMapGO<K2, V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newMutableMap(Map<? extends K2, ? extends V2> map1,
            Map<? extends K2, ? extends V2> map2, Map<? extends K2, ? extends V2> map3,
            Map<? extends K2, ? extends V2> map4, Map<? extends K2, ? extends V2> map5) {
        MutableQHashParallelKVObjObjMapGO<K2, V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newMutableMap(
            Consumer<net.openhft.koloboke.function.BiConsumer<K2, V2>> entriesSupplier
            ) {
        MutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newMutableMap(K2[] keys,
            V2[] values) {
        MutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newMutableMap(Iterable<? extends K2> keys,
            Iterable<? extends V2> values) {
        MutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newMutableMapOf(K2 k1, V2 v1) {
        MutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newMutableMapOf(K2 k1, V2 v1,
             K2 k2, V2 v2) {
        MutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newMutableMapOf(K2 k1, V2 v1,
             K2 k2, V2 v2, K2 k3, V2 v3) {
        MutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newMutableMapOf(K2 k1, V2 v1,
             K2 k2, V2 v2, K2 k3, V2 v3,
             K2 k4, V2 v4) {
        MutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newMutableMapOf(K2 k1, V2 v1,
             K2 k2, V2 v2, K2 k3, V2 v3,
             K2 k4, V2 v4, K2 k5, V2 v5) {
        MutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newImmutableMap(
            Map<? extends K2, ? extends V2> map, int expectedSize) {
        ImmutableQHashParallelKVObjObjMapGO<K2, V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newImmutableMap(Map<? extends K2, ? extends V2> map1,
            Map<? extends K2, ? extends V2> map2, int expectedSize) {
        ImmutableQHashParallelKVObjObjMapGO<K2, V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newImmutableMap(Map<? extends K2, ? extends V2> map1,
            Map<? extends K2, ? extends V2> map2, Map<? extends K2, ? extends V2> map3, int expectedSize) {
        ImmutableQHashParallelKVObjObjMapGO<K2, V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newImmutableMap(Map<? extends K2, ? extends V2> map1,
            Map<? extends K2, ? extends V2> map2, Map<? extends K2, ? extends V2> map3,
            Map<? extends K2, ? extends V2> map4, int expectedSize) {
        ImmutableQHashParallelKVObjObjMapGO<K2, V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newImmutableMap(Map<? extends K2, ? extends V2> map1,
            Map<? extends K2, ? extends V2> map2, Map<? extends K2, ? extends V2> map3,
            Map<? extends K2, ? extends V2> map4, Map<? extends K2, ? extends V2> map5, int expectedSize) {
        ImmutableQHashParallelKVObjObjMapGO<K2, V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newImmutableMap(
            Consumer<net.openhft.koloboke.function.BiConsumer<K2, V2>> entriesSupplier
            , int expectedSize) {
        ImmutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newImmutableMap(K2[] keys,
            V2[] values, int expectedSize) {
        ImmutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }


    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newImmutableMap(Iterable<? extends K2> keys,
            Iterable<? extends V2> values, int expectedSize) {
        ImmutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newImmutableMap(
            Map<? extends K2, ? extends V2> map) {
        ImmutableQHashParallelKVObjObjMapGO<K2, V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newImmutableMap(Map<? extends K2, ? extends V2> map1,
            Map<? extends K2, ? extends V2> map2) {
        ImmutableQHashParallelKVObjObjMapGO<K2, V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newImmutableMap(Map<? extends K2, ? extends V2> map1,
            Map<? extends K2, ? extends V2> map2, Map<? extends K2, ? extends V2> map3) {
        ImmutableQHashParallelKVObjObjMapGO<K2, V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newImmutableMap(Map<? extends K2, ? extends V2> map1,
            Map<? extends K2, ? extends V2> map2, Map<? extends K2, ? extends V2> map3,
            Map<? extends K2, ? extends V2> map4) {
        ImmutableQHashParallelKVObjObjMapGO<K2, V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newImmutableMap(Map<? extends K2, ? extends V2> map1,
            Map<? extends K2, ? extends V2> map2, Map<? extends K2, ? extends V2> map3,
            Map<? extends K2, ? extends V2> map4, Map<? extends K2, ? extends V2> map5) {
        ImmutableQHashParallelKVObjObjMapGO<K2, V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newImmutableMap(
            Consumer<net.openhft.koloboke.function.BiConsumer<K2, V2>> entriesSupplier
            ) {
        ImmutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newImmutableMap(K2[] keys,
            V2[] values) {
        ImmutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newImmutableMap(Iterable<? extends K2> keys,
            Iterable<? extends V2> values) {
        ImmutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newImmutableMapOf(K2 k1, V2 v1) {
        ImmutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newImmutableMapOf(K2 k1, V2 v1,
             K2 k2, V2 v2) {
        ImmutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newImmutableMapOf(K2 k1, V2 v1,
             K2 k2, V2 v2, K2 k3, V2 v3) {
        ImmutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newImmutableMapOf(K2 k1, V2 v1,
             K2 k2, V2 v2, K2 k3, V2 v3,
             K2 k4, V2 v4) {
        ImmutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K, V2 extends V> HashObjObjMap<K2, V2> newImmutableMapOf(K2 k1, V2 v1,
             K2 k2, V2 v2, K2 k3, V2 v3,
             K2 k4, V2 v4, K2 k5, V2 v5) {
        ImmutableQHashParallelKVObjObjMapGO<K2, V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

