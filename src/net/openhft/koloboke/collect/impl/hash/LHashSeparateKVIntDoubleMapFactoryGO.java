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


public abstract class LHashSeparateKVIntDoubleMapFactoryGO
        extends LHashSeparateKVIntDoubleMapFactorySO {

    LHashSeparateKVIntDoubleMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashSeparateKVIntDoubleMapGO shrunk(
            UpdatableLHashSeparateKVIntDoubleMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashSeparateKVIntDoubleMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Map<Integer, Double> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Map<Integer, Double> map1, Map<Integer, Double> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Map<Integer, Double> map1, Map<Integer, Double> map2,
            Map<Integer, Double> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMap(
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
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMap(
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
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Map<Integer, Double> map1, Map<Integer, Double> map2,
            int expectedSize) {
        UpdatableLHashSeparateKVIntDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Map<Integer, Double> map1, Map<Integer, Double> map2,
            Map<Integer, Double> map3, int expectedSize) {
        UpdatableLHashSeparateKVIntDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Map<Integer, Double> map1, Map<Integer, Double> map2,
            Map<Integer, Double> map3, Map<Integer, Double> map4,
            int expectedSize) {
        UpdatableLHashSeparateKVIntDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Map<Integer, Double> map1, Map<Integer, Double> map2,
            Map<Integer, Double> map3, Map<Integer, Double> map4,
            Map<Integer, Double> map5, int expectedSize) {
        UpdatableLHashSeparateKVIntDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.IntDoubleConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.IntDoubleConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashSeparateKVIntDoubleMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMap(
            int[] keys, double[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMap(
            int[] keys, double[] values, int expectedSize) {
        UpdatableLHashSeparateKVIntDoubleMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Integer[] keys, Double[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Integer[] keys, Double[] values, int expectedSize) {
        UpdatableLHashSeparateKVIntDoubleMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Iterable<Integer> keys, Iterable<Double> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMap(
            Iterable<Integer> keys, Iterable<Double> values, int expectedSize) {
        UpdatableLHashSeparateKVIntDoubleMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMapOf(
            int k1, double v1) {
        UpdatableLHashSeparateKVIntDoubleMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMapOf(
            int k1, double v1, int k2, double v2) {
        UpdatableLHashSeparateKVIntDoubleMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMapOf(
            int k1, double v1, int k2, double v2,
            int k3, double v3) {
        UpdatableLHashSeparateKVIntDoubleMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMapOf(
            int k1, double v1, int k2, double v2,
            int k3, double v3, int k4, double v4) {
        UpdatableLHashSeparateKVIntDoubleMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntDoubleMapGO newUpdatableMapOf(
            int k1, double v1, int k2, double v2,
            int k3, double v3, int k4, double v4,
            int k5, double v5) {
        UpdatableLHashSeparateKVIntDoubleMapGO map = newUpdatableMap(5);
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
        MutableLHashSeparateKVIntDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3, int expectedSize) {
        MutableLHashSeparateKVIntDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3,
            Map<Integer, Double> map4, int expectedSize) {
        MutableLHashSeparateKVIntDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3,
            Map<Integer, Double> map4, Map<Integer, Double> map5, int expectedSize) {
        MutableLHashSeparateKVIntDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(
            Consumer<net.openhft.koloboke.function.IntDoubleConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(int[] keys,
            double[] values, int expectedSize) {
        MutableLHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(
            Integer[] keys, Double[] values, int expectedSize) {
        MutableLHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(Iterable<Integer> keys,
            Iterable<Double> values, int expectedSize) {
        MutableLHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(
            Map<Integer, Double> map) {
        MutableLHashSeparateKVIntDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2) {
        MutableLHashSeparateKVIntDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3) {
        MutableLHashSeparateKVIntDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3,
            Map<Integer, Double> map4) {
        MutableLHashSeparateKVIntDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3,
            Map<Integer, Double> map4, Map<Integer, Double> map5) {
        MutableLHashSeparateKVIntDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(
            Consumer<net.openhft.koloboke.function.IntDoubleConsumer> entriesSupplier
            ) {
        MutableLHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(int[] keys,
            double[] values) {
        MutableLHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(
            Integer[] keys, Double[] values) {
        MutableLHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMap(Iterable<Integer> keys,
            Iterable<Double> values) {
        MutableLHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMapOf(int k1, double v1) {
        MutableLHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMapOf(int k1, double v1,
             int k2, double v2) {
        MutableLHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMapOf(int k1, double v1,
             int k2, double v2, int k3, double v3) {
        MutableLHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMapOf(int k1, double v1,
             int k2, double v2, int k3, double v3,
             int k4, double v4) {
        MutableLHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newMutableMapOf(int k1, double v1,
             int k2, double v2, int k3, double v3,
             int k4, double v4, int k5, double v5) {
        MutableLHashSeparateKVIntDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, int expectedSize) {
        ImmutableLHashSeparateKVIntDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3, int expectedSize) {
        ImmutableLHashSeparateKVIntDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3,
            Map<Integer, Double> map4, int expectedSize) {
        ImmutableLHashSeparateKVIntDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3,
            Map<Integer, Double> map4, Map<Integer, Double> map5, int expectedSize) {
        ImmutableLHashSeparateKVIntDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.IntDoubleConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(int[] keys,
            double[] values, int expectedSize) {
        ImmutableLHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(
            Integer[] keys, Double[] values, int expectedSize) {
        ImmutableLHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(Iterable<Integer> keys,
            Iterable<Double> values, int expectedSize) {
        ImmutableLHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(
            Map<Integer, Double> map) {
        ImmutableLHashSeparateKVIntDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2) {
        ImmutableLHashSeparateKVIntDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3) {
        ImmutableLHashSeparateKVIntDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3,
            Map<Integer, Double> map4) {
        ImmutableLHashSeparateKVIntDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(Map<Integer, Double> map1,
            Map<Integer, Double> map2, Map<Integer, Double> map3,
            Map<Integer, Double> map4, Map<Integer, Double> map5) {
        ImmutableLHashSeparateKVIntDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.IntDoubleConsumer> entriesSupplier
            ) {
        ImmutableLHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(int[] keys,
            double[] values) {
        ImmutableLHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(
            Integer[] keys, Double[] values) {
        ImmutableLHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMap(Iterable<Integer> keys,
            Iterable<Double> values) {
        ImmutableLHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMapOf(int k1, double v1) {
        ImmutableLHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMapOf(int k1, double v1,
             int k2, double v2) {
        ImmutableLHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMapOf(int k1, double v1,
             int k2, double v2, int k3, double v3) {
        ImmutableLHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMapOf(int k1, double v1,
             int k2, double v2, int k3, double v3,
             int k4, double v4) {
        ImmutableLHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntDoubleMap newImmutableMapOf(int k1, double v1,
             int k2, double v2, int k3, double v3,
             int k4, double v4, int k5, double v5) {
        ImmutableLHashSeparateKVIntDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

