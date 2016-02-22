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
import net.openhft.koloboke.collect.map.hash.HashDoubleFloatMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashDoubleFloatMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashSeparateKVDoubleFloatMapFactoryGO
        extends QHashSeparateKVDoubleFloatMapFactorySO {

    QHashSeparateKVDoubleFloatMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    abstract HashDoubleFloatMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashDoubleFloatMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashDoubleFloatMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    @Override
    public final HashDoubleFloatMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
    }

    @Override
    public final HashDoubleFloatMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                );
    }


    @Override
    public String toString() {
        return "HashDoubleFloatMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashDoubleFloatMapFactory) {
            HashDoubleFloatMapFactory factory = (HashDoubleFloatMapFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory) &&
                    // boxing to treat NaNs correctly
                   ((Float) getDefaultValue()).equals(factory.getDefaultValue())
                    ;
        } else {
            return false;
        }
    }

    @Override
    public float getDefaultValue() {
        return 0.0f;
    }

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashSeparateKVDoubleFloatMapGO shrunk(
            UpdatableQHashSeparateKVDoubleFloatMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashSeparateKVDoubleFloatMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMap(
            Map<Double, Float> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMap(
            Map<Double, Float> map1, Map<Double, Float> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMap(
            Map<Double, Float> map1, Map<Double, Float> map2,
            Map<Double, Float> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMap(
            Map<Double, Float> map1, Map<Double, Float> map2,
            Map<Double, Float> map3, Map<Double, Float> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMap(
            Map<Double, Float> map1, Map<Double, Float> map2,
            Map<Double, Float> map3, Map<Double, Float> map4,
            Map<Double, Float> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMap(
            Map<Double, Float> map1, Map<Double, Float> map2,
            int expectedSize) {
        UpdatableQHashSeparateKVDoubleFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMap(
            Map<Double, Float> map1, Map<Double, Float> map2,
            Map<Double, Float> map3, int expectedSize) {
        UpdatableQHashSeparateKVDoubleFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMap(
            Map<Double, Float> map1, Map<Double, Float> map2,
            Map<Double, Float> map3, Map<Double, Float> map4,
            int expectedSize) {
        UpdatableQHashSeparateKVDoubleFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMap(
            Map<Double, Float> map1, Map<Double, Float> map2,
            Map<Double, Float> map3, Map<Double, Float> map4,
            Map<Double, Float> map5, int expectedSize) {
        UpdatableQHashSeparateKVDoubleFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.DoubleFloatConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.DoubleFloatConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashSeparateKVDoubleFloatMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.DoubleFloatConsumer() {
             @Override
             public void accept(double k, float v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMap(
            double[] keys, float[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMap(
            double[] keys, float[] values, int expectedSize) {
        UpdatableQHashSeparateKVDoubleFloatMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMap(
            Double[] keys, Float[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMap(
            Double[] keys, Float[] values, int expectedSize) {
        UpdatableQHashSeparateKVDoubleFloatMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMap(
            Iterable<Double> keys, Iterable<Float> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMap(
            Iterable<Double> keys, Iterable<Float> values, int expectedSize) {
        UpdatableQHashSeparateKVDoubleFloatMapGO map = newUpdatableMap(expectedSize);
        Iterator<Double> keysIt = keys.iterator();
        Iterator<Float> valuesIt = values.iterator();
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
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMapOf(
            double k1, float v1) {
        UpdatableQHashSeparateKVDoubleFloatMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMapOf(
            double k1, float v1, double k2, float v2) {
        UpdatableQHashSeparateKVDoubleFloatMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMapOf(
            double k1, float v1, double k2, float v2,
            double k3, float v3) {
        UpdatableQHashSeparateKVDoubleFloatMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMapOf(
            double k1, float v1, double k2, float v2,
            double k3, float v3, double k4, float v4) {
        UpdatableQHashSeparateKVDoubleFloatMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleFloatMapGO newUpdatableMapOf(
            double k1, float v1, double k2, float v2,
            double k3, float v3, double k4, float v4,
            double k5, float v5) {
        UpdatableQHashSeparateKVDoubleFloatMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMap(Map<Double, Float> map1,
            Map<Double, Float> map2, int expectedSize) {
        MutableQHashSeparateKVDoubleFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMap(Map<Double, Float> map1,
            Map<Double, Float> map2, Map<Double, Float> map3, int expectedSize) {
        MutableQHashSeparateKVDoubleFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMap(Map<Double, Float> map1,
            Map<Double, Float> map2, Map<Double, Float> map3,
            Map<Double, Float> map4, int expectedSize) {
        MutableQHashSeparateKVDoubleFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMap(Map<Double, Float> map1,
            Map<Double, Float> map2, Map<Double, Float> map3,
            Map<Double, Float> map4, Map<Double, Float> map5, int expectedSize) {
        MutableQHashSeparateKVDoubleFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMap(
            Consumer<net.openhft.koloboke.function.DoubleFloatConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashSeparateKVDoubleFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMap(double[] keys,
            float[] values, int expectedSize) {
        MutableQHashSeparateKVDoubleFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMap(
            Double[] keys, Float[] values, int expectedSize) {
        MutableQHashSeparateKVDoubleFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMap(Iterable<Double> keys,
            Iterable<Float> values, int expectedSize) {
        MutableQHashSeparateKVDoubleFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMap(
            Map<Double, Float> map) {
        MutableQHashSeparateKVDoubleFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMap(Map<Double, Float> map1,
            Map<Double, Float> map2) {
        MutableQHashSeparateKVDoubleFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMap(Map<Double, Float> map1,
            Map<Double, Float> map2, Map<Double, Float> map3) {
        MutableQHashSeparateKVDoubleFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMap(Map<Double, Float> map1,
            Map<Double, Float> map2, Map<Double, Float> map3,
            Map<Double, Float> map4) {
        MutableQHashSeparateKVDoubleFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMap(Map<Double, Float> map1,
            Map<Double, Float> map2, Map<Double, Float> map3,
            Map<Double, Float> map4, Map<Double, Float> map5) {
        MutableQHashSeparateKVDoubleFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMap(
            Consumer<net.openhft.koloboke.function.DoubleFloatConsumer> entriesSupplier
            ) {
        MutableQHashSeparateKVDoubleFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMap(double[] keys,
            float[] values) {
        MutableQHashSeparateKVDoubleFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMap(
            Double[] keys, Float[] values) {
        MutableQHashSeparateKVDoubleFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMap(Iterable<Double> keys,
            Iterable<Float> values) {
        MutableQHashSeparateKVDoubleFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMapOf(double k1, float v1) {
        MutableQHashSeparateKVDoubleFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMapOf(double k1, float v1,
             double k2, float v2) {
        MutableQHashSeparateKVDoubleFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMapOf(double k1, float v1,
             double k2, float v2, double k3, float v3) {
        MutableQHashSeparateKVDoubleFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMapOf(double k1, float v1,
             double k2, float v2, double k3, float v3,
             double k4, float v4) {
        MutableQHashSeparateKVDoubleFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newMutableMapOf(double k1, float v1,
             double k2, float v2, double k3, float v3,
             double k4, float v4, double k5, float v5) {
        MutableQHashSeparateKVDoubleFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMap(Map<Double, Float> map1,
            Map<Double, Float> map2, int expectedSize) {
        ImmutableQHashSeparateKVDoubleFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMap(Map<Double, Float> map1,
            Map<Double, Float> map2, Map<Double, Float> map3, int expectedSize) {
        ImmutableQHashSeparateKVDoubleFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMap(Map<Double, Float> map1,
            Map<Double, Float> map2, Map<Double, Float> map3,
            Map<Double, Float> map4, int expectedSize) {
        ImmutableQHashSeparateKVDoubleFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMap(Map<Double, Float> map1,
            Map<Double, Float> map2, Map<Double, Float> map3,
            Map<Double, Float> map4, Map<Double, Float> map5, int expectedSize) {
        ImmutableQHashSeparateKVDoubleFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.DoubleFloatConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashSeparateKVDoubleFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMap(double[] keys,
            float[] values, int expectedSize) {
        ImmutableQHashSeparateKVDoubleFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMap(
            Double[] keys, Float[] values, int expectedSize) {
        ImmutableQHashSeparateKVDoubleFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMap(Iterable<Double> keys,
            Iterable<Float> values, int expectedSize) {
        ImmutableQHashSeparateKVDoubleFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMap(
            Map<Double, Float> map) {
        ImmutableQHashSeparateKVDoubleFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMap(Map<Double, Float> map1,
            Map<Double, Float> map2) {
        ImmutableQHashSeparateKVDoubleFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMap(Map<Double, Float> map1,
            Map<Double, Float> map2, Map<Double, Float> map3) {
        ImmutableQHashSeparateKVDoubleFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMap(Map<Double, Float> map1,
            Map<Double, Float> map2, Map<Double, Float> map3,
            Map<Double, Float> map4) {
        ImmutableQHashSeparateKVDoubleFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMap(Map<Double, Float> map1,
            Map<Double, Float> map2, Map<Double, Float> map3,
            Map<Double, Float> map4, Map<Double, Float> map5) {
        ImmutableQHashSeparateKVDoubleFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.DoubleFloatConsumer> entriesSupplier
            ) {
        ImmutableQHashSeparateKVDoubleFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMap(double[] keys,
            float[] values) {
        ImmutableQHashSeparateKVDoubleFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMap(
            Double[] keys, Float[] values) {
        ImmutableQHashSeparateKVDoubleFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMap(Iterable<Double> keys,
            Iterable<Float> values) {
        ImmutableQHashSeparateKVDoubleFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMapOf(double k1, float v1) {
        ImmutableQHashSeparateKVDoubleFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMapOf(double k1, float v1,
             double k2, float v2) {
        ImmutableQHashSeparateKVDoubleFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMapOf(double k1, float v1,
             double k2, float v2, double k3, float v3) {
        ImmutableQHashSeparateKVDoubleFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMapOf(double k1, float v1,
             double k2, float v2, double k3, float v3,
             double k4, float v4) {
        ImmutableQHashSeparateKVDoubleFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleFloatMap newImmutableMapOf(double k1, float v1,
             double k2, float v2, double k3, float v3,
             double k4, float v4, double k5, float v5) {
        ImmutableQHashSeparateKVDoubleFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

