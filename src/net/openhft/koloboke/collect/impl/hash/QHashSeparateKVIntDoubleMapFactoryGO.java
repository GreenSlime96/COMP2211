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
import net.openhft.koloboke.collect.map.hash.HashIntDoubleMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashIntDoubleMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashSeparateKVIntDoubleMapFactoryGO
        extends QHashSeparateKVIntDoubleMapFactorySO {

    QHashSeparateKVIntDoubleMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashIntDoubleMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    abstract HashIntDoubleMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    abstract HashIntDoubleMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    @Override
    public final HashIntDoubleMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashIntDoubleMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashIntDoubleMapFactory withDomain(int lower, int upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashIntDoubleMapFactory withKeysDomain(int lower, int upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashIntDoubleMapFactory withKeysDomainComplement(int lower, int upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((int) (upper + 1), (int) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashIntDoubleMapFactory[" + commonString() + keySpecialString() +
                ",defaultValue=" + getDefaultValue() +
        "]";
    }

    @Override
    public int hashCode() {
        int hashCode = keySpecialHashCode(commonHashCode());
        hashCode = hashCode * 31 + Primitives.hashCode(getDefaultValue());
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof HashIntDoubleMapFactory) {
            HashIntDoubleMapFactory factory = (HashIntDoubleMapFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory) &&
                    // boxing to treat NaNs correctly
                   ((Double) getDefaultValue()).equals(factory.getDefaultValue())
                    ;
        } else {
            return false;
        }
    }

    @Override
    public double getDefaultValue() {
        return 0.0;
    }

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashSeparateKVIntDoubleMapGO shrunk(
            UpdatableQHashSeparateKVIntDoubleMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashSeparateKVIntDoubleMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Map<Integer, Double> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Map<Integer, Double> map1, Map<Integer, Double> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Map<Integer, Double> map1, Map<Integer, Double> map2,
            Map<Integer, Double> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Map<Integer, Double> map1, Map<Integer, Double> map2,
            Map<Integer, Double> map3, Map<Integer, Double> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Map<Integer, Double> map1, Map<Integer, Double> map2,
            Map<Integer, Double> map3, Map<Integer, Double> map4,
            Map<Integer, Double> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Map<Integer, Double> map1, Map<Integer, Double> map2,
            int expectedSize) {
        UpdatableQHashSeparateKVIntDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Map<Integer, Double> map1, Map<Integer, Double> map2,
            Map<Integer, Double> map3, int expectedSize) {
        UpdatableQHashSeparateKVIntDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Map<Integer, Double> map1, Map<Integer, Double> map2,
            Map<Integer, Double> map3, Map<Integer, Double> map4,
            int expectedSize) {
        UpdatableQHashSeparateKVIntDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Map<Integer, Double> map1, Map<Integer, Double> map2,
            Map<Integer, Double> map3, Map<Integer, Double> map4,
            Map<Integer, Double> map5, int expectedSize) {
        UpdatableQHashSeparateKVIntDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.IntDoubleConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.IntDoubleConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashSeparateKVIntDoubleMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.IntDoubleConsumer() {
             @Override
             public void accept(int k, double v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMap(
            int[] keys, double[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMap(
            int[] keys, double[] values, int expectedSize) {
        UpdatableQHashSeparateKVIntDoubleMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Integer[] keys, Double[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Integer[] keys, Double[] values, int expectedSize) {
        UpdatableQHashSeparateKVIntDoubleMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Iterable<Integer> keys, Iterable<Double> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Iterable<Integer> keys, Iterable<Double> values, int expectedSize) {
        UpdatableQHashSeparateKVIntDoubleMapGO map = newUpdatableMap(expectedSize);
        Iterator<Integer> keysIt = keys.iterator();
        Iterator<Double> valuesIt = values.iterator();
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
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMapOf(
            int k1, double v1) {
        UpdatableQHashSeparateKVIntDoubleMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMapOf(
            int k1, double v1, int k2, double v2) {
        UpdatableQHashSeparateKVIntDoubleMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMapOf(
            int k1, double v1, int k2, double v2,
            int k3, double v3) {
        UpdatableQHashSeparateKVIntDoubleMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMapOf(
            int k1, double v1, int k2, double v2,
            int k3, double v3, int k4, double v4) {
        UpdatableQHashSeparateKVIntDoubleMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntDoubleMapGO newUpdatableMapOf(
            int k1, double v1, int k2, double v2,
            int k3, double v3, int k4, double v4,
            int k5, double v5) {
        UpdatableQHashSeparateKVIntDoubleMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, int expectedSize) {
        MutableQHashSeparateKVIntDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3, int expectedSize) {
        MutableQHashSeparateKVIntDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3,
            Map<Integer, Double> map4, int expectedSize) {
        MutableQHashSeparateKVIntDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3,
            Map<Integer, Double> map4, Map<Integer, Double> map5, int expectedSize) {
        MutableQHashSeparateKVIntDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(
            Consumer<net.openhft.koloboke.function.IntDoubleConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(int[] keys,
            double[] values, int expectedSize) {
        MutableQHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(
            Integer[] keys, Double[] values, int expectedSize) {
        MutableQHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(Iterable<Integer> keys,
            Iterable<Double> values, int expectedSize) {
        MutableQHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(
            Map<Integer, Double> map) {
        MutableQHashSeparateKVIntDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2) {
        MutableQHashSeparateKVIntDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3) {
        MutableQHashSeparateKVIntDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3,
            Map<Integer, Double> map4) {
        MutableQHashSeparateKVIntDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3,
            Map<Integer, Double> map4, Map<Integer, Double> map5) {
        MutableQHashSeparateKVIntDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(
            Consumer<net.openhft.koloboke.function.IntDoubleConsumer> entriesSupplier
            ) {
        MutableQHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(int[] keys,
            double[] values) {
        MutableQHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(
            Integer[] keys, Double[] values) {
        MutableQHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(Iterable<Integer> keys,
            Iterable<Double> values) {
        MutableQHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMapOf(int k1, double v1) {
        MutableQHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMapOf(int k1, double v1,
             int k2, double v2) {
        MutableQHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMapOf(int k1, double v1,
             int k2, double v2, int k3, double v3) {
        MutableQHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMapOf(int k1, double v1,
             int k2, double v2, int k3, double v3,
             int k4, double v4) {
        MutableQHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMapOf(int k1, double v1,
             int k2, double v2, int k3, double v3,
             int k4, double v4, int k5, double v5) {
        MutableQHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, int expectedSize) {
        ImmutableQHashSeparateKVIntDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3, int expectedSize) {
        ImmutableQHashSeparateKVIntDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3,
            Map<Integer, Double> map4, int expectedSize) {
        ImmutableQHashSeparateKVIntDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3,
            Map<Integer, Double> map4, Map<Integer, Double> map5, int expectedSize) {
        ImmutableQHashSeparateKVIntDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.IntDoubleConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(int[] keys,
            double[] values, int expectedSize) {
        ImmutableQHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(
            Integer[] keys, Double[] values, int expectedSize) {
        ImmutableQHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(Iterable<Integer> keys,
            Iterable<Double> values, int expectedSize) {
        ImmutableQHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(
            Map<Integer, Double> map) {
        ImmutableQHashSeparateKVIntDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2) {
        ImmutableQHashSeparateKVIntDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3) {
        ImmutableQHashSeparateKVIntDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3,
            Map<Integer, Double> map4) {
        ImmutableQHashSeparateKVIntDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3,
            Map<Integer, Double> map4, Map<Integer, Double> map5) {
        ImmutableQHashSeparateKVIntDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.IntDoubleConsumer> entriesSupplier
            ) {
        ImmutableQHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(int[] keys,
            double[] values) {
        ImmutableQHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(
            Integer[] keys, Double[] values) {
        ImmutableQHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(Iterable<Integer> keys,
            Iterable<Double> values) {
        ImmutableQHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMapOf(int k1, double v1) {
        ImmutableQHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMapOf(int k1, double v1,
             int k2, double v2) {
        ImmutableQHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMapOf(int k1, double v1,
             int k2, double v2, int k3, double v3) {
        ImmutableQHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMapOf(int k1, double v1,
             int k2, double v2, int k3, double v3,
             int k4, double v4) {
        ImmutableQHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMapOf(int k1, double v1,
             int k2, double v2, int k3, double v3,
             int k4, double v4, int k5, double v5) {
        ImmutableQHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

