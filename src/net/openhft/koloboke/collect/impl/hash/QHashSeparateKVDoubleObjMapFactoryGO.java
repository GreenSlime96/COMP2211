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
import net.openhft.koloboke.collect.map.hash.HashDoubleObjMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashDoubleObjMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashSeparateKVDoubleObjMapFactoryGO<V>
        extends QHashSeparateKVDoubleObjMapFactorySO<V> {

    QHashSeparateKVDoubleObjMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    abstract HashDoubleObjMapFactory<V> thisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashDoubleObjMapFactory<V> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashDoubleObjMapFactory<V> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    @Override
    public final HashDoubleObjMapFactory<V> withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
    }

    @Override
    public final HashDoubleObjMapFactory<V> withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                );
    }


    @Override
    public String toString() {
        return "HashDoubleObjMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashDoubleObjMapFactory) {
            HashDoubleObjMapFactory factory = (HashDoubleObjMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> shrunk(
            UpdatableQHashSeparateKVDoubleObjMapGO<V2> map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public <V2 extends V> MutableQHashSeparateKVDoubleObjMapGO<V2> newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMap(
            Map<Double, ? extends V2> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMap(
            Map<Double, ? extends V2> map1, Map<Double, ? extends V2> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMap(
            Map<Double, ? extends V2> map1, Map<Double, ? extends V2> map2,
            Map<Double, ? extends V2> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMap(
            Map<Double, ? extends V2> map1, Map<Double, ? extends V2> map2,
            Map<Double, ? extends V2> map3, Map<Double, ? extends V2> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMap(
            Map<Double, ? extends V2> map1, Map<Double, ? extends V2> map2,
            Map<Double, ? extends V2> map3, Map<Double, ? extends V2> map4,
            Map<Double, ? extends V2> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMap(
            Map<Double, ? extends V2> map1, Map<Double, ? extends V2> map2,
            int expectedSize) {
        UpdatableQHashSeparateKVDoubleObjMapGO<V2> map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMap(
            Map<Double, ? extends V2> map1, Map<Double, ? extends V2> map2,
            Map<Double, ? extends V2> map3, int expectedSize) {
        UpdatableQHashSeparateKVDoubleObjMapGO<V2> map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMap(
            Map<Double, ? extends V2> map1, Map<Double, ? extends V2> map2,
            Map<Double, ? extends V2> map3, Map<Double, ? extends V2> map4,
            int expectedSize) {
        UpdatableQHashSeparateKVDoubleObjMapGO<V2> map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMap(
            Map<Double, ? extends V2> map1, Map<Double, ? extends V2> map2,
            Map<Double, ? extends V2> map3, Map<Double, ? extends V2> map4,
            Map<Double, ? extends V2> map5, int expectedSize) {
        UpdatableQHashSeparateKVDoubleObjMapGO<V2> map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMap(
            Consumer<net.openhft.koloboke.function.DoubleObjConsumer<V2>> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMap(
            Consumer<net.openhft.koloboke.function.DoubleObjConsumer<V2>> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashSeparateKVDoubleObjMapGO<V2> map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.DoubleObjConsumer<V2>() {
             @Override
             public void accept(double k, V2 v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMap(
            double[] keys, V2[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMap(
            double[] keys, V2[] values, int expectedSize) {
        UpdatableQHashSeparateKVDoubleObjMapGO<V2> map = newUpdatableMap(expectedSize);
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
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMap(
            Double[] keys, V2[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMap(
            Double[] keys, V2[] values, int expectedSize) {
        UpdatableQHashSeparateKVDoubleObjMapGO<V2> map = newUpdatableMap(expectedSize);
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
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMap(
            Iterable<Double> keys, Iterable<? extends V2> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMap(
            Iterable<Double> keys, Iterable<? extends V2> values, int expectedSize) {
        UpdatableQHashSeparateKVDoubleObjMapGO<V2> map = newUpdatableMap(expectedSize);
        Iterator<Double> keysIt = keys.iterator();
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
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMapOf(
            double k1, V2 v1) {
        UpdatableQHashSeparateKVDoubleObjMapGO<V2> map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMapOf(
            double k1, V2 v1, double k2, V2 v2) {
        UpdatableQHashSeparateKVDoubleObjMapGO<V2> map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMapOf(
            double k1, V2 v1, double k2, V2 v2,
            double k3, V2 v3) {
        UpdatableQHashSeparateKVDoubleObjMapGO<V2> map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMapOf(
            double k1, V2 v1, double k2, V2 v2,
            double k3, V2 v3, double k4, V2 v4) {
        UpdatableQHashSeparateKVDoubleObjMapGO<V2> map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2> newUpdatableMapOf(
            double k1, V2 v1, double k2, V2 v2,
            double k3, V2 v3, double k4, V2 v4,
            double k5, V2 v5) {
        UpdatableQHashSeparateKVDoubleObjMapGO<V2> map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMap(Map<Double, ? extends V2> map1,
            Map<Double, ? extends V2> map2, int expectedSize) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMap(Map<Double, ? extends V2> map1,
            Map<Double, ? extends V2> map2, Map<Double, ? extends V2> map3, int expectedSize) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMap(Map<Double, ? extends V2> map1,
            Map<Double, ? extends V2> map2, Map<Double, ? extends V2> map3,
            Map<Double, ? extends V2> map4, int expectedSize) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMap(Map<Double, ? extends V2> map1,
            Map<Double, ? extends V2> map2, Map<Double, ? extends V2> map3,
            Map<Double, ? extends V2> map4, Map<Double, ? extends V2> map5, int expectedSize) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMap(
            Consumer<net.openhft.koloboke.function.DoubleObjConsumer<V2>> entriesSupplier
            , int expectedSize) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMap(double[] keys,
            V2[] values, int expectedSize) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMap(
            Double[] keys, V2[] values, int expectedSize) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMap(Iterable<Double> keys,
            Iterable<? extends V2> values, int expectedSize) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMap(
            Map<Double, ? extends V2> map) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMap(Map<Double, ? extends V2> map1,
            Map<Double, ? extends V2> map2) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMap(Map<Double, ? extends V2> map1,
            Map<Double, ? extends V2> map2, Map<Double, ? extends V2> map3) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMap(Map<Double, ? extends V2> map1,
            Map<Double, ? extends V2> map2, Map<Double, ? extends V2> map3,
            Map<Double, ? extends V2> map4) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMap(Map<Double, ? extends V2> map1,
            Map<Double, ? extends V2> map2, Map<Double, ? extends V2> map3,
            Map<Double, ? extends V2> map4, Map<Double, ? extends V2> map5) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMap(
            Consumer<net.openhft.koloboke.function.DoubleObjConsumer<V2>> entriesSupplier
            ) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMap(double[] keys,
            V2[] values) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMap(
            Double[] keys, V2[] values) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMap(Iterable<Double> keys,
            Iterable<? extends V2> values) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMapOf(double k1, V2 v1) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMapOf(double k1, V2 v1,
             double k2, V2 v2) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMapOf(double k1, V2 v1,
             double k2, V2 v2, double k3, V2 v3) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMapOf(double k1, V2 v1,
             double k2, V2 v2, double k3, V2 v3,
             double k4, V2 v4) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newMutableMapOf(double k1, V2 v1,
             double k2, V2 v2, double k3, V2 v3,
             double k4, V2 v4, double k5, V2 v5) {
        MutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMap(Map<Double, ? extends V2> map1,
            Map<Double, ? extends V2> map2, int expectedSize) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMap(Map<Double, ? extends V2> map1,
            Map<Double, ? extends V2> map2, Map<Double, ? extends V2> map3, int expectedSize) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMap(Map<Double, ? extends V2> map1,
            Map<Double, ? extends V2> map2, Map<Double, ? extends V2> map3,
            Map<Double, ? extends V2> map4, int expectedSize) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMap(Map<Double, ? extends V2> map1,
            Map<Double, ? extends V2> map2, Map<Double, ? extends V2> map3,
            Map<Double, ? extends V2> map4, Map<Double, ? extends V2> map5, int expectedSize) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMap(
            Consumer<net.openhft.koloboke.function.DoubleObjConsumer<V2>> entriesSupplier
            , int expectedSize) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMap(double[] keys,
            V2[] values, int expectedSize) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMap(
            Double[] keys, V2[] values, int expectedSize) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMap(Iterable<Double> keys,
            Iterable<? extends V2> values, int expectedSize) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMap(
            Map<Double, ? extends V2> map) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMap(Map<Double, ? extends V2> map1,
            Map<Double, ? extends V2> map2) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMap(Map<Double, ? extends V2> map1,
            Map<Double, ? extends V2> map2, Map<Double, ? extends V2> map3) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMap(Map<Double, ? extends V2> map1,
            Map<Double, ? extends V2> map2, Map<Double, ? extends V2> map3,
            Map<Double, ? extends V2> map4) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMap(Map<Double, ? extends V2> map1,
            Map<Double, ? extends V2> map2, Map<Double, ? extends V2> map3,
            Map<Double, ? extends V2> map4, Map<Double, ? extends V2> map5) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMap(
            Consumer<net.openhft.koloboke.function.DoubleObjConsumer<V2>> entriesSupplier
            ) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMap(double[] keys,
            V2[] values) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMap(
            Double[] keys, V2[] values) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMap(Iterable<Double> keys,
            Iterable<? extends V2> values) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMapOf(double k1, V2 v1) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMapOf(double k1, V2 v1,
             double k2, V2 v2) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMapOf(double k1, V2 v1,
             double k2, V2 v2, double k3, V2 v3) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMapOf(double k1, V2 v1,
             double k2, V2 v2, double k3, V2 v3,
             double k4, V2 v4) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashDoubleObjMap<V2> newImmutableMapOf(double k1, V2 v1,
             double k2, V2 v2, double k3, V2 v3,
             double k4, V2 v4, double k5, V2 v5) {
        ImmutableQHashSeparateKVDoubleObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

