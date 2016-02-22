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
import net.openhft.koloboke.collect.map.hash.HashFloatDoubleMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashFloatDoubleMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashSeparateKVFloatDoubleMapFactoryGO
        extends LHashSeparateKVFloatDoubleMapFactorySO {

    LHashSeparateKVFloatDoubleMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    abstract HashFloatDoubleMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashFloatDoubleMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashFloatDoubleMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    @Override
    public final HashFloatDoubleMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
    }

    @Override
    public final HashFloatDoubleMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                );
    }


    @Override
    public String toString() {
        return "HashFloatDoubleMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashFloatDoubleMapFactory) {
            HashFloatDoubleMapFactory factory = (HashFloatDoubleMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashSeparateKVFloatDoubleMapGO shrunk(
            UpdatableLHashSeparateKVFloatDoubleMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashSeparateKVFloatDoubleMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMap(
            Map<Float, Double> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMap(
            Map<Float, Double> map1, Map<Float, Double> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMap(
            Map<Float, Double> map1, Map<Float, Double> map2,
            Map<Float, Double> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMap(
            Map<Float, Double> map1, Map<Float, Double> map2,
            Map<Float, Double> map3, Map<Float, Double> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMap(
            Map<Float, Double> map1, Map<Float, Double> map2,
            Map<Float, Double> map3, Map<Float, Double> map4,
            Map<Float, Double> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMap(
            Map<Float, Double> map1, Map<Float, Double> map2,
            int expectedSize) {
        UpdatableLHashSeparateKVFloatDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMap(
            Map<Float, Double> map1, Map<Float, Double> map2,
            Map<Float, Double> map3, int expectedSize) {
        UpdatableLHashSeparateKVFloatDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMap(
            Map<Float, Double> map1, Map<Float, Double> map2,
            Map<Float, Double> map3, Map<Float, Double> map4,
            int expectedSize) {
        UpdatableLHashSeparateKVFloatDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMap(
            Map<Float, Double> map1, Map<Float, Double> map2,
            Map<Float, Double> map3, Map<Float, Double> map4,
            Map<Float, Double> map5, int expectedSize) {
        UpdatableLHashSeparateKVFloatDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.FloatDoubleConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.FloatDoubleConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashSeparateKVFloatDoubleMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.FloatDoubleConsumer() {
             @Override
             public void accept(float k, double v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMap(
            float[] keys, double[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMap(
            float[] keys, double[] values, int expectedSize) {
        UpdatableLHashSeparateKVFloatDoubleMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMap(
            Float[] keys, Double[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMap(
            Float[] keys, Double[] values, int expectedSize) {
        UpdatableLHashSeparateKVFloatDoubleMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMap(
            Iterable<Float> keys, Iterable<Double> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMap(
            Iterable<Float> keys, Iterable<Double> values, int expectedSize) {
        UpdatableLHashSeparateKVFloatDoubleMapGO map = newUpdatableMap(expectedSize);
        Iterator<Float> keysIt = keys.iterator();
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
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMapOf(
            float k1, double v1) {
        UpdatableLHashSeparateKVFloatDoubleMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMapOf(
            float k1, double v1, float k2, double v2) {
        UpdatableLHashSeparateKVFloatDoubleMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMapOf(
            float k1, double v1, float k2, double v2,
            float k3, double v3) {
        UpdatableLHashSeparateKVFloatDoubleMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMapOf(
            float k1, double v1, float k2, double v2,
            float k3, double v3, float k4, double v4) {
        UpdatableLHashSeparateKVFloatDoubleMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatDoubleMapGO newUpdatableMapOf(
            float k1, double v1, float k2, double v2,
            float k3, double v3, float k4, double v4,
            float k5, double v5) {
        UpdatableLHashSeparateKVFloatDoubleMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMap(Map<Float, Double> map1,
            Map<Float, Double> map2, int expectedSize) {
        MutableLHashSeparateKVFloatDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMap(Map<Float, Double> map1,
            Map<Float, Double> map2, Map<Float, Double> map3, int expectedSize) {
        MutableLHashSeparateKVFloatDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMap(Map<Float, Double> map1,
            Map<Float, Double> map2, Map<Float, Double> map3,
            Map<Float, Double> map4, int expectedSize) {
        MutableLHashSeparateKVFloatDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMap(Map<Float, Double> map1,
            Map<Float, Double> map2, Map<Float, Double> map3,
            Map<Float, Double> map4, Map<Float, Double> map5, int expectedSize) {
        MutableLHashSeparateKVFloatDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMap(
            Consumer<net.openhft.koloboke.function.FloatDoubleConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashSeparateKVFloatDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMap(float[] keys,
            double[] values, int expectedSize) {
        MutableLHashSeparateKVFloatDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMap(
            Float[] keys, Double[] values, int expectedSize) {
        MutableLHashSeparateKVFloatDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMap(Iterable<Float> keys,
            Iterable<Double> values, int expectedSize) {
        MutableLHashSeparateKVFloatDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMap(
            Map<Float, Double> map) {
        MutableLHashSeparateKVFloatDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMap(Map<Float, Double> map1,
            Map<Float, Double> map2) {
        MutableLHashSeparateKVFloatDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMap(Map<Float, Double> map1,
            Map<Float, Double> map2, Map<Float, Double> map3) {
        MutableLHashSeparateKVFloatDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMap(Map<Float, Double> map1,
            Map<Float, Double> map2, Map<Float, Double> map3,
            Map<Float, Double> map4) {
        MutableLHashSeparateKVFloatDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMap(Map<Float, Double> map1,
            Map<Float, Double> map2, Map<Float, Double> map3,
            Map<Float, Double> map4, Map<Float, Double> map5) {
        MutableLHashSeparateKVFloatDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMap(
            Consumer<net.openhft.koloboke.function.FloatDoubleConsumer> entriesSupplier
            ) {
        MutableLHashSeparateKVFloatDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMap(float[] keys,
            double[] values) {
        MutableLHashSeparateKVFloatDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMap(
            Float[] keys, Double[] values) {
        MutableLHashSeparateKVFloatDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMap(Iterable<Float> keys,
            Iterable<Double> values) {
        MutableLHashSeparateKVFloatDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMapOf(float k1, double v1) {
        MutableLHashSeparateKVFloatDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMapOf(float k1, double v1,
             float k2, double v2) {
        MutableLHashSeparateKVFloatDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMapOf(float k1, double v1,
             float k2, double v2, float k3, double v3) {
        MutableLHashSeparateKVFloatDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMapOf(float k1, double v1,
             float k2, double v2, float k3, double v3,
             float k4, double v4) {
        MutableLHashSeparateKVFloatDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newMutableMapOf(float k1, double v1,
             float k2, double v2, float k3, double v3,
             float k4, double v4, float k5, double v5) {
        MutableLHashSeparateKVFloatDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMap(Map<Float, Double> map1,
            Map<Float, Double> map2, int expectedSize) {
        ImmutableLHashSeparateKVFloatDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMap(Map<Float, Double> map1,
            Map<Float, Double> map2, Map<Float, Double> map3, int expectedSize) {
        ImmutableLHashSeparateKVFloatDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMap(Map<Float, Double> map1,
            Map<Float, Double> map2, Map<Float, Double> map3,
            Map<Float, Double> map4, int expectedSize) {
        ImmutableLHashSeparateKVFloatDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMap(Map<Float, Double> map1,
            Map<Float, Double> map2, Map<Float, Double> map3,
            Map<Float, Double> map4, Map<Float, Double> map5, int expectedSize) {
        ImmutableLHashSeparateKVFloatDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.FloatDoubleConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashSeparateKVFloatDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMap(float[] keys,
            double[] values, int expectedSize) {
        ImmutableLHashSeparateKVFloatDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMap(
            Float[] keys, Double[] values, int expectedSize) {
        ImmutableLHashSeparateKVFloatDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMap(Iterable<Float> keys,
            Iterable<Double> values, int expectedSize) {
        ImmutableLHashSeparateKVFloatDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMap(
            Map<Float, Double> map) {
        ImmutableLHashSeparateKVFloatDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMap(Map<Float, Double> map1,
            Map<Float, Double> map2) {
        ImmutableLHashSeparateKVFloatDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMap(Map<Float, Double> map1,
            Map<Float, Double> map2, Map<Float, Double> map3) {
        ImmutableLHashSeparateKVFloatDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMap(Map<Float, Double> map1,
            Map<Float, Double> map2, Map<Float, Double> map3,
            Map<Float, Double> map4) {
        ImmutableLHashSeparateKVFloatDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMap(Map<Float, Double> map1,
            Map<Float, Double> map2, Map<Float, Double> map3,
            Map<Float, Double> map4, Map<Float, Double> map5) {
        ImmutableLHashSeparateKVFloatDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.FloatDoubleConsumer> entriesSupplier
            ) {
        ImmutableLHashSeparateKVFloatDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMap(float[] keys,
            double[] values) {
        ImmutableLHashSeparateKVFloatDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMap(
            Float[] keys, Double[] values) {
        ImmutableLHashSeparateKVFloatDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMap(Iterable<Float> keys,
            Iterable<Double> values) {
        ImmutableLHashSeparateKVFloatDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMapOf(float k1, double v1) {
        ImmutableLHashSeparateKVFloatDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMapOf(float k1, double v1,
             float k2, double v2) {
        ImmutableLHashSeparateKVFloatDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMapOf(float k1, double v1,
             float k2, double v2, float k3, double v3) {
        ImmutableLHashSeparateKVFloatDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMapOf(float k1, double v1,
             float k2, double v2, float k3, double v3,
             float k4, double v4) {
        ImmutableLHashSeparateKVFloatDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatDoubleMap newImmutableMapOf(float k1, double v1,
             float k2, double v2, float k3, double v3,
             float k4, double v4, float k5, double v5) {
        ImmutableLHashSeparateKVFloatDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

