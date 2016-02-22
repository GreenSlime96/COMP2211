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
import net.openhft.koloboke.collect.map.hash.HashDoubleShortMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashDoubleShortMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashSeparateKVDoubleShortMapFactoryGO
        extends QHashSeparateKVDoubleShortMapFactorySO {

    QHashSeparateKVDoubleShortMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    abstract HashDoubleShortMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashDoubleShortMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashDoubleShortMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    @Override
    public final HashDoubleShortMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
    }

    @Override
    public final HashDoubleShortMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                );
    }


    @Override
    public String toString() {
        return "HashDoubleShortMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashDoubleShortMapFactory) {
            HashDoubleShortMapFactory factory = (HashDoubleShortMapFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory) &&
                    // boxing to treat NaNs correctly
                   ((Short) getDefaultValue()).equals(factory.getDefaultValue())
                    ;
        } else {
            return false;
        }
    }

    @Override
    public short getDefaultValue() {
        return (short) 0;
    }

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashSeparateKVDoubleShortMapGO shrunk(
            UpdatableQHashSeparateKVDoubleShortMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashSeparateKVDoubleShortMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMap(
            Map<Double, Short> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMap(
            Map<Double, Short> map1, Map<Double, Short> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMap(
            Map<Double, Short> map1, Map<Double, Short> map2,
            Map<Double, Short> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMap(
            Map<Double, Short> map1, Map<Double, Short> map2,
            Map<Double, Short> map3, Map<Double, Short> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMap(
            Map<Double, Short> map1, Map<Double, Short> map2,
            Map<Double, Short> map3, Map<Double, Short> map4,
            Map<Double, Short> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMap(
            Map<Double, Short> map1, Map<Double, Short> map2,
            int expectedSize) {
        UpdatableQHashSeparateKVDoubleShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMap(
            Map<Double, Short> map1, Map<Double, Short> map2,
            Map<Double, Short> map3, int expectedSize) {
        UpdatableQHashSeparateKVDoubleShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMap(
            Map<Double, Short> map1, Map<Double, Short> map2,
            Map<Double, Short> map3, Map<Double, Short> map4,
            int expectedSize) {
        UpdatableQHashSeparateKVDoubleShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMap(
            Map<Double, Short> map1, Map<Double, Short> map2,
            Map<Double, Short> map3, Map<Double, Short> map4,
            Map<Double, Short> map5, int expectedSize) {
        UpdatableQHashSeparateKVDoubleShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.DoubleShortConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.DoubleShortConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashSeparateKVDoubleShortMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.DoubleShortConsumer() {
             @Override
             public void accept(double k, short v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMap(
            double[] keys, short[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMap(
            double[] keys, short[] values, int expectedSize) {
        UpdatableQHashSeparateKVDoubleShortMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMap(
            Double[] keys, Short[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMap(
            Double[] keys, Short[] values, int expectedSize) {
        UpdatableQHashSeparateKVDoubleShortMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMap(
            Iterable<Double> keys, Iterable<Short> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMap(
            Iterable<Double> keys, Iterable<Short> values, int expectedSize) {
        UpdatableQHashSeparateKVDoubleShortMapGO map = newUpdatableMap(expectedSize);
        Iterator<Double> keysIt = keys.iterator();
        Iterator<Short> valuesIt = values.iterator();
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
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMapOf(
            double k1, short v1) {
        UpdatableQHashSeparateKVDoubleShortMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMapOf(
            double k1, short v1, double k2, short v2) {
        UpdatableQHashSeparateKVDoubleShortMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMapOf(
            double k1, short v1, double k2, short v2,
            double k3, short v3) {
        UpdatableQHashSeparateKVDoubleShortMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMapOf(
            double k1, short v1, double k2, short v2,
            double k3, short v3, double k4, short v4) {
        UpdatableQHashSeparateKVDoubleShortMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVDoubleShortMapGO newUpdatableMapOf(
            double k1, short v1, double k2, short v2,
            double k3, short v3, double k4, short v4,
            double k5, short v5) {
        UpdatableQHashSeparateKVDoubleShortMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMap(Map<Double, Short> map1,
            Map<Double, Short> map2, int expectedSize) {
        MutableQHashSeparateKVDoubleShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMap(Map<Double, Short> map1,
            Map<Double, Short> map2, Map<Double, Short> map3, int expectedSize) {
        MutableQHashSeparateKVDoubleShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMap(Map<Double, Short> map1,
            Map<Double, Short> map2, Map<Double, Short> map3,
            Map<Double, Short> map4, int expectedSize) {
        MutableQHashSeparateKVDoubleShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMap(Map<Double, Short> map1,
            Map<Double, Short> map2, Map<Double, Short> map3,
            Map<Double, Short> map4, Map<Double, Short> map5, int expectedSize) {
        MutableQHashSeparateKVDoubleShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMap(
            Consumer<net.openhft.koloboke.function.DoubleShortConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashSeparateKVDoubleShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMap(double[] keys,
            short[] values, int expectedSize) {
        MutableQHashSeparateKVDoubleShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMap(
            Double[] keys, Short[] values, int expectedSize) {
        MutableQHashSeparateKVDoubleShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMap(Iterable<Double> keys,
            Iterable<Short> values, int expectedSize) {
        MutableQHashSeparateKVDoubleShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMap(
            Map<Double, Short> map) {
        MutableQHashSeparateKVDoubleShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMap(Map<Double, Short> map1,
            Map<Double, Short> map2) {
        MutableQHashSeparateKVDoubleShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMap(Map<Double, Short> map1,
            Map<Double, Short> map2, Map<Double, Short> map3) {
        MutableQHashSeparateKVDoubleShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMap(Map<Double, Short> map1,
            Map<Double, Short> map2, Map<Double, Short> map3,
            Map<Double, Short> map4) {
        MutableQHashSeparateKVDoubleShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMap(Map<Double, Short> map1,
            Map<Double, Short> map2, Map<Double, Short> map3,
            Map<Double, Short> map4, Map<Double, Short> map5) {
        MutableQHashSeparateKVDoubleShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMap(
            Consumer<net.openhft.koloboke.function.DoubleShortConsumer> entriesSupplier
            ) {
        MutableQHashSeparateKVDoubleShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMap(double[] keys,
            short[] values) {
        MutableQHashSeparateKVDoubleShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMap(
            Double[] keys, Short[] values) {
        MutableQHashSeparateKVDoubleShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMap(Iterable<Double> keys,
            Iterable<Short> values) {
        MutableQHashSeparateKVDoubleShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMapOf(double k1, short v1) {
        MutableQHashSeparateKVDoubleShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMapOf(double k1, short v1,
             double k2, short v2) {
        MutableQHashSeparateKVDoubleShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMapOf(double k1, short v1,
             double k2, short v2, double k3, short v3) {
        MutableQHashSeparateKVDoubleShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMapOf(double k1, short v1,
             double k2, short v2, double k3, short v3,
             double k4, short v4) {
        MutableQHashSeparateKVDoubleShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newMutableMapOf(double k1, short v1,
             double k2, short v2, double k3, short v3,
             double k4, short v4, double k5, short v5) {
        MutableQHashSeparateKVDoubleShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMap(Map<Double, Short> map1,
            Map<Double, Short> map2, int expectedSize) {
        ImmutableQHashSeparateKVDoubleShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMap(Map<Double, Short> map1,
            Map<Double, Short> map2, Map<Double, Short> map3, int expectedSize) {
        ImmutableQHashSeparateKVDoubleShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMap(Map<Double, Short> map1,
            Map<Double, Short> map2, Map<Double, Short> map3,
            Map<Double, Short> map4, int expectedSize) {
        ImmutableQHashSeparateKVDoubleShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMap(Map<Double, Short> map1,
            Map<Double, Short> map2, Map<Double, Short> map3,
            Map<Double, Short> map4, Map<Double, Short> map5, int expectedSize) {
        ImmutableQHashSeparateKVDoubleShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.DoubleShortConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashSeparateKVDoubleShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMap(double[] keys,
            short[] values, int expectedSize) {
        ImmutableQHashSeparateKVDoubleShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMap(
            Double[] keys, Short[] values, int expectedSize) {
        ImmutableQHashSeparateKVDoubleShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMap(Iterable<Double> keys,
            Iterable<Short> values, int expectedSize) {
        ImmutableQHashSeparateKVDoubleShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMap(
            Map<Double, Short> map) {
        ImmutableQHashSeparateKVDoubleShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMap(Map<Double, Short> map1,
            Map<Double, Short> map2) {
        ImmutableQHashSeparateKVDoubleShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMap(Map<Double, Short> map1,
            Map<Double, Short> map2, Map<Double, Short> map3) {
        ImmutableQHashSeparateKVDoubleShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMap(Map<Double, Short> map1,
            Map<Double, Short> map2, Map<Double, Short> map3,
            Map<Double, Short> map4) {
        ImmutableQHashSeparateKVDoubleShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMap(Map<Double, Short> map1,
            Map<Double, Short> map2, Map<Double, Short> map3,
            Map<Double, Short> map4, Map<Double, Short> map5) {
        ImmutableQHashSeparateKVDoubleShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.DoubleShortConsumer> entriesSupplier
            ) {
        ImmutableQHashSeparateKVDoubleShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMap(double[] keys,
            short[] values) {
        ImmutableQHashSeparateKVDoubleShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMap(
            Double[] keys, Short[] values) {
        ImmutableQHashSeparateKVDoubleShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMap(Iterable<Double> keys,
            Iterable<Short> values) {
        ImmutableQHashSeparateKVDoubleShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMapOf(double k1, short v1) {
        ImmutableQHashSeparateKVDoubleShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMapOf(double k1, short v1,
             double k2, short v2) {
        ImmutableQHashSeparateKVDoubleShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMapOf(double k1, short v1,
             double k2, short v2, double k3, short v3) {
        ImmutableQHashSeparateKVDoubleShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMapOf(double k1, short v1,
             double k2, short v2, double k3, short v3,
             double k4, short v4) {
        ImmutableQHashSeparateKVDoubleShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleShortMap newImmutableMapOf(double k1, short v1,
             double k2, short v2, double k3, short v3,
             double k4, short v4, double k5, short v5) {
        ImmutableQHashSeparateKVDoubleShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

