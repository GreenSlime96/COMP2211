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
import net.openhft.koloboke.collect.map.hash.HashDoubleDoubleMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashDoubleDoubleMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashParallelKVDoubleDoubleMapFactoryGO
        extends LHashParallelKVDoubleDoubleMapFactorySO {

    LHashParallelKVDoubleDoubleMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    abstract HashDoubleDoubleMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashDoubleDoubleMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashDoubleDoubleMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    @Override
    public final HashDoubleDoubleMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
    }

    @Override
    public final HashDoubleDoubleMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                );
    }


    @Override
    public String toString() {
        return "HashDoubleDoubleMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashDoubleDoubleMapFactory) {
            HashDoubleDoubleMapFactory factory = (HashDoubleDoubleMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashParallelKVDoubleDoubleMapGO shrunk(
            UpdatableLHashParallelKVDoubleDoubleMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashParallelKVDoubleDoubleMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMap(
            Map<Double, Double> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMap(
            Map<Double, Double> map1, Map<Double, Double> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMap(
            Map<Double, Double> map1, Map<Double, Double> map2,
            Map<Double, Double> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMap(
            Map<Double, Double> map1, Map<Double, Double> map2,
            Map<Double, Double> map3, Map<Double, Double> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMap(
            Map<Double, Double> map1, Map<Double, Double> map2,
            Map<Double, Double> map3, Map<Double, Double> map4,
            Map<Double, Double> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMap(
            Map<Double, Double> map1, Map<Double, Double> map2,
            int expectedSize) {
        UpdatableLHashParallelKVDoubleDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMap(
            Map<Double, Double> map1, Map<Double, Double> map2,
            Map<Double, Double> map3, int expectedSize) {
        UpdatableLHashParallelKVDoubleDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMap(
            Map<Double, Double> map1, Map<Double, Double> map2,
            Map<Double, Double> map3, Map<Double, Double> map4,
            int expectedSize) {
        UpdatableLHashParallelKVDoubleDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMap(
            Map<Double, Double> map1, Map<Double, Double> map2,
            Map<Double, Double> map3, Map<Double, Double> map4,
            Map<Double, Double> map5, int expectedSize) {
        UpdatableLHashParallelKVDoubleDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.DoubleDoubleConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.DoubleDoubleConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashParallelKVDoubleDoubleMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.DoubleDoubleConsumer() {
             @Override
             public void accept(double k, double v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMap(
            double[] keys, double[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMap(
            double[] keys, double[] values, int expectedSize) {
        UpdatableLHashParallelKVDoubleDoubleMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMap(
            Double[] keys, Double[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMap(
            Double[] keys, Double[] values, int expectedSize) {
        UpdatableLHashParallelKVDoubleDoubleMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMap(
            Iterable<Double> keys, Iterable<Double> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMap(
            Iterable<Double> keys, Iterable<Double> values, int expectedSize) {
        UpdatableLHashParallelKVDoubleDoubleMapGO map = newUpdatableMap(expectedSize);
        Iterator<Double> keysIt = keys.iterator();
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
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMapOf(
            double k1, double v1) {
        UpdatableLHashParallelKVDoubleDoubleMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMapOf(
            double k1, double v1, double k2, double v2) {
        UpdatableLHashParallelKVDoubleDoubleMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMapOf(
            double k1, double v1, double k2, double v2,
            double k3, double v3) {
        UpdatableLHashParallelKVDoubleDoubleMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMapOf(
            double k1, double v1, double k2, double v2,
            double k3, double v3, double k4, double v4) {
        UpdatableLHashParallelKVDoubleDoubleMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleDoubleMapGO newUpdatableMapOf(
            double k1, double v1, double k2, double v2,
            double k3, double v3, double k4, double v4,
            double k5, double v5) {
        UpdatableLHashParallelKVDoubleDoubleMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMap(Map<Double, Double> map1,
            Map<Double, Double> map2, int expectedSize) {
        MutableLHashParallelKVDoubleDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMap(Map<Double, Double> map1,
            Map<Double, Double> map2, Map<Double, Double> map3, int expectedSize) {
        MutableLHashParallelKVDoubleDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMap(Map<Double, Double> map1,
            Map<Double, Double> map2, Map<Double, Double> map3,
            Map<Double, Double> map4, int expectedSize) {
        MutableLHashParallelKVDoubleDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMap(Map<Double, Double> map1,
            Map<Double, Double> map2, Map<Double, Double> map3,
            Map<Double, Double> map4, Map<Double, Double> map5, int expectedSize) {
        MutableLHashParallelKVDoubleDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMap(
            Consumer<net.openhft.koloboke.function.DoubleDoubleConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashParallelKVDoubleDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMap(double[] keys,
            double[] values, int expectedSize) {
        MutableLHashParallelKVDoubleDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMap(
            Double[] keys, Double[] values, int expectedSize) {
        MutableLHashParallelKVDoubleDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMap(Iterable<Double> keys,
            Iterable<Double> values, int expectedSize) {
        MutableLHashParallelKVDoubleDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMap(
            Map<Double, Double> map) {
        MutableLHashParallelKVDoubleDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMap(Map<Double, Double> map1,
            Map<Double, Double> map2) {
        MutableLHashParallelKVDoubleDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMap(Map<Double, Double> map1,
            Map<Double, Double> map2, Map<Double, Double> map3) {
        MutableLHashParallelKVDoubleDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMap(Map<Double, Double> map1,
            Map<Double, Double> map2, Map<Double, Double> map3,
            Map<Double, Double> map4) {
        MutableLHashParallelKVDoubleDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMap(Map<Double, Double> map1,
            Map<Double, Double> map2, Map<Double, Double> map3,
            Map<Double, Double> map4, Map<Double, Double> map5) {
        MutableLHashParallelKVDoubleDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMap(
            Consumer<net.openhft.koloboke.function.DoubleDoubleConsumer> entriesSupplier
            ) {
        MutableLHashParallelKVDoubleDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMap(double[] keys,
            double[] values) {
        MutableLHashParallelKVDoubleDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMap(
            Double[] keys, Double[] values) {
        MutableLHashParallelKVDoubleDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMap(Iterable<Double> keys,
            Iterable<Double> values) {
        MutableLHashParallelKVDoubleDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMapOf(double k1, double v1) {
        MutableLHashParallelKVDoubleDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMapOf(double k1, double v1,
             double k2, double v2) {
        MutableLHashParallelKVDoubleDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMapOf(double k1, double v1,
             double k2, double v2, double k3, double v3) {
        MutableLHashParallelKVDoubleDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMapOf(double k1, double v1,
             double k2, double v2, double k3, double v3,
             double k4, double v4) {
        MutableLHashParallelKVDoubleDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newMutableMapOf(double k1, double v1,
             double k2, double v2, double k3, double v3,
             double k4, double v4, double k5, double v5) {
        MutableLHashParallelKVDoubleDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMap(Map<Double, Double> map1,
            Map<Double, Double> map2, int expectedSize) {
        ImmutableLHashParallelKVDoubleDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMap(Map<Double, Double> map1,
            Map<Double, Double> map2, Map<Double, Double> map3, int expectedSize) {
        ImmutableLHashParallelKVDoubleDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMap(Map<Double, Double> map1,
            Map<Double, Double> map2, Map<Double, Double> map3,
            Map<Double, Double> map4, int expectedSize) {
        ImmutableLHashParallelKVDoubleDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMap(Map<Double, Double> map1,
            Map<Double, Double> map2, Map<Double, Double> map3,
            Map<Double, Double> map4, Map<Double, Double> map5, int expectedSize) {
        ImmutableLHashParallelKVDoubleDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.DoubleDoubleConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashParallelKVDoubleDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMap(double[] keys,
            double[] values, int expectedSize) {
        ImmutableLHashParallelKVDoubleDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMap(
            Double[] keys, Double[] values, int expectedSize) {
        ImmutableLHashParallelKVDoubleDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMap(Iterable<Double> keys,
            Iterable<Double> values, int expectedSize) {
        ImmutableLHashParallelKVDoubleDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMap(
            Map<Double, Double> map) {
        ImmutableLHashParallelKVDoubleDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMap(Map<Double, Double> map1,
            Map<Double, Double> map2) {
        ImmutableLHashParallelKVDoubleDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMap(Map<Double, Double> map1,
            Map<Double, Double> map2, Map<Double, Double> map3) {
        ImmutableLHashParallelKVDoubleDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMap(Map<Double, Double> map1,
            Map<Double, Double> map2, Map<Double, Double> map3,
            Map<Double, Double> map4) {
        ImmutableLHashParallelKVDoubleDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMap(Map<Double, Double> map1,
            Map<Double, Double> map2, Map<Double, Double> map3,
            Map<Double, Double> map4, Map<Double, Double> map5) {
        ImmutableLHashParallelKVDoubleDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.DoubleDoubleConsumer> entriesSupplier
            ) {
        ImmutableLHashParallelKVDoubleDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMap(double[] keys,
            double[] values) {
        ImmutableLHashParallelKVDoubleDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMap(
            Double[] keys, Double[] values) {
        ImmutableLHashParallelKVDoubleDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMap(Iterable<Double> keys,
            Iterable<Double> values) {
        ImmutableLHashParallelKVDoubleDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMapOf(double k1, double v1) {
        ImmutableLHashParallelKVDoubleDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMapOf(double k1, double v1,
             double k2, double v2) {
        ImmutableLHashParallelKVDoubleDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMapOf(double k1, double v1,
             double k2, double v2, double k3, double v3) {
        ImmutableLHashParallelKVDoubleDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMapOf(double k1, double v1,
             double k2, double v2, double k3, double v3,
             double k4, double v4) {
        ImmutableLHashParallelKVDoubleDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleDoubleMap newImmutableMapOf(double k1, double v1,
             double k2, double v2, double k3, double v3,
             double k4, double v4, double k5, double v5) {
        ImmutableLHashParallelKVDoubleDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

