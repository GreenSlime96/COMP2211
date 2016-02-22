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
import net.openhft.koloboke.collect.map.hash.HashDoubleByteMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashDoubleByteMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashSeparateKVDoubleByteMapFactoryGO
        extends LHashSeparateKVDoubleByteMapFactorySO {

    LHashSeparateKVDoubleByteMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    abstract HashDoubleByteMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashDoubleByteMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashDoubleByteMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    @Override
    public final HashDoubleByteMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
    }

    @Override
    public final HashDoubleByteMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                );
    }


    @Override
    public String toString() {
        return "HashDoubleByteMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashDoubleByteMapFactory) {
            HashDoubleByteMapFactory factory = (HashDoubleByteMapFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory) &&
                    // boxing to treat NaNs correctly
                   ((Byte) getDefaultValue()).equals(factory.getDefaultValue())
                    ;
        } else {
            return false;
        }
    }

    @Override
    public byte getDefaultValue() {
        return (byte) 0;
    }

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashSeparateKVDoubleByteMapGO shrunk(
            UpdatableLHashSeparateKVDoubleByteMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashSeparateKVDoubleByteMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMap(
            Map<Double, Byte> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMap(
            Map<Double, Byte> map1, Map<Double, Byte> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMap(
            Map<Double, Byte> map1, Map<Double, Byte> map2,
            Map<Double, Byte> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMap(
            Map<Double, Byte> map1, Map<Double, Byte> map2,
            Map<Double, Byte> map3, Map<Double, Byte> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMap(
            Map<Double, Byte> map1, Map<Double, Byte> map2,
            Map<Double, Byte> map3, Map<Double, Byte> map4,
            Map<Double, Byte> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMap(
            Map<Double, Byte> map1, Map<Double, Byte> map2,
            int expectedSize) {
        UpdatableLHashSeparateKVDoubleByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMap(
            Map<Double, Byte> map1, Map<Double, Byte> map2,
            Map<Double, Byte> map3, int expectedSize) {
        UpdatableLHashSeparateKVDoubleByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMap(
            Map<Double, Byte> map1, Map<Double, Byte> map2,
            Map<Double, Byte> map3, Map<Double, Byte> map4,
            int expectedSize) {
        UpdatableLHashSeparateKVDoubleByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMap(
            Map<Double, Byte> map1, Map<Double, Byte> map2,
            Map<Double, Byte> map3, Map<Double, Byte> map4,
            Map<Double, Byte> map5, int expectedSize) {
        UpdatableLHashSeparateKVDoubleByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.DoubleByteConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.DoubleByteConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashSeparateKVDoubleByteMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.DoubleByteConsumer() {
             @Override
             public void accept(double k, byte v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMap(
            double[] keys, byte[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMap(
            double[] keys, byte[] values, int expectedSize) {
        UpdatableLHashSeparateKVDoubleByteMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMap(
            Double[] keys, Byte[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMap(
            Double[] keys, Byte[] values, int expectedSize) {
        UpdatableLHashSeparateKVDoubleByteMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMap(
            Iterable<Double> keys, Iterable<Byte> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMap(
            Iterable<Double> keys, Iterable<Byte> values, int expectedSize) {
        UpdatableLHashSeparateKVDoubleByteMapGO map = newUpdatableMap(expectedSize);
        Iterator<Double> keysIt = keys.iterator();
        Iterator<Byte> valuesIt = values.iterator();
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
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMapOf(
            double k1, byte v1) {
        UpdatableLHashSeparateKVDoubleByteMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMapOf(
            double k1, byte v1, double k2, byte v2) {
        UpdatableLHashSeparateKVDoubleByteMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMapOf(
            double k1, byte v1, double k2, byte v2,
            double k3, byte v3) {
        UpdatableLHashSeparateKVDoubleByteMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMapOf(
            double k1, byte v1, double k2, byte v2,
            double k3, byte v3, double k4, byte v4) {
        UpdatableLHashSeparateKVDoubleByteMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleByteMapGO newUpdatableMapOf(
            double k1, byte v1, double k2, byte v2,
            double k3, byte v3, double k4, byte v4,
            double k5, byte v5) {
        UpdatableLHashSeparateKVDoubleByteMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMap(Map<Double, Byte> map1,
            Map<Double, Byte> map2, int expectedSize) {
        MutableLHashSeparateKVDoubleByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMap(Map<Double, Byte> map1,
            Map<Double, Byte> map2, Map<Double, Byte> map3, int expectedSize) {
        MutableLHashSeparateKVDoubleByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMap(Map<Double, Byte> map1,
            Map<Double, Byte> map2, Map<Double, Byte> map3,
            Map<Double, Byte> map4, int expectedSize) {
        MutableLHashSeparateKVDoubleByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMap(Map<Double, Byte> map1,
            Map<Double, Byte> map2, Map<Double, Byte> map3,
            Map<Double, Byte> map4, Map<Double, Byte> map5, int expectedSize) {
        MutableLHashSeparateKVDoubleByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMap(
            Consumer<net.openhft.koloboke.function.DoubleByteConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashSeparateKVDoubleByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMap(double[] keys,
            byte[] values, int expectedSize) {
        MutableLHashSeparateKVDoubleByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMap(
            Double[] keys, Byte[] values, int expectedSize) {
        MutableLHashSeparateKVDoubleByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMap(Iterable<Double> keys,
            Iterable<Byte> values, int expectedSize) {
        MutableLHashSeparateKVDoubleByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMap(
            Map<Double, Byte> map) {
        MutableLHashSeparateKVDoubleByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMap(Map<Double, Byte> map1,
            Map<Double, Byte> map2) {
        MutableLHashSeparateKVDoubleByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMap(Map<Double, Byte> map1,
            Map<Double, Byte> map2, Map<Double, Byte> map3) {
        MutableLHashSeparateKVDoubleByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMap(Map<Double, Byte> map1,
            Map<Double, Byte> map2, Map<Double, Byte> map3,
            Map<Double, Byte> map4) {
        MutableLHashSeparateKVDoubleByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMap(Map<Double, Byte> map1,
            Map<Double, Byte> map2, Map<Double, Byte> map3,
            Map<Double, Byte> map4, Map<Double, Byte> map5) {
        MutableLHashSeparateKVDoubleByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMap(
            Consumer<net.openhft.koloboke.function.DoubleByteConsumer> entriesSupplier
            ) {
        MutableLHashSeparateKVDoubleByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMap(double[] keys,
            byte[] values) {
        MutableLHashSeparateKVDoubleByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMap(
            Double[] keys, Byte[] values) {
        MutableLHashSeparateKVDoubleByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMap(Iterable<Double> keys,
            Iterable<Byte> values) {
        MutableLHashSeparateKVDoubleByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMapOf(double k1, byte v1) {
        MutableLHashSeparateKVDoubleByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMapOf(double k1, byte v1,
             double k2, byte v2) {
        MutableLHashSeparateKVDoubleByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMapOf(double k1, byte v1,
             double k2, byte v2, double k3, byte v3) {
        MutableLHashSeparateKVDoubleByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMapOf(double k1, byte v1,
             double k2, byte v2, double k3, byte v3,
             double k4, byte v4) {
        MutableLHashSeparateKVDoubleByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newMutableMapOf(double k1, byte v1,
             double k2, byte v2, double k3, byte v3,
             double k4, byte v4, double k5, byte v5) {
        MutableLHashSeparateKVDoubleByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMap(Map<Double, Byte> map1,
            Map<Double, Byte> map2, int expectedSize) {
        ImmutableLHashSeparateKVDoubleByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMap(Map<Double, Byte> map1,
            Map<Double, Byte> map2, Map<Double, Byte> map3, int expectedSize) {
        ImmutableLHashSeparateKVDoubleByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMap(Map<Double, Byte> map1,
            Map<Double, Byte> map2, Map<Double, Byte> map3,
            Map<Double, Byte> map4, int expectedSize) {
        ImmutableLHashSeparateKVDoubleByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMap(Map<Double, Byte> map1,
            Map<Double, Byte> map2, Map<Double, Byte> map3,
            Map<Double, Byte> map4, Map<Double, Byte> map5, int expectedSize) {
        ImmutableLHashSeparateKVDoubleByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.DoubleByteConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashSeparateKVDoubleByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMap(double[] keys,
            byte[] values, int expectedSize) {
        ImmutableLHashSeparateKVDoubleByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMap(
            Double[] keys, Byte[] values, int expectedSize) {
        ImmutableLHashSeparateKVDoubleByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMap(Iterable<Double> keys,
            Iterable<Byte> values, int expectedSize) {
        ImmutableLHashSeparateKVDoubleByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMap(
            Map<Double, Byte> map) {
        ImmutableLHashSeparateKVDoubleByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMap(Map<Double, Byte> map1,
            Map<Double, Byte> map2) {
        ImmutableLHashSeparateKVDoubleByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMap(Map<Double, Byte> map1,
            Map<Double, Byte> map2, Map<Double, Byte> map3) {
        ImmutableLHashSeparateKVDoubleByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMap(Map<Double, Byte> map1,
            Map<Double, Byte> map2, Map<Double, Byte> map3,
            Map<Double, Byte> map4) {
        ImmutableLHashSeparateKVDoubleByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMap(Map<Double, Byte> map1,
            Map<Double, Byte> map2, Map<Double, Byte> map3,
            Map<Double, Byte> map4, Map<Double, Byte> map5) {
        ImmutableLHashSeparateKVDoubleByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.DoubleByteConsumer> entriesSupplier
            ) {
        ImmutableLHashSeparateKVDoubleByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMap(double[] keys,
            byte[] values) {
        ImmutableLHashSeparateKVDoubleByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMap(
            Double[] keys, Byte[] values) {
        ImmutableLHashSeparateKVDoubleByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMap(Iterable<Double> keys,
            Iterable<Byte> values) {
        ImmutableLHashSeparateKVDoubleByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMapOf(double k1, byte v1) {
        ImmutableLHashSeparateKVDoubleByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMapOf(double k1, byte v1,
             double k2, byte v2) {
        ImmutableLHashSeparateKVDoubleByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMapOf(double k1, byte v1,
             double k2, byte v2, double k3, byte v3) {
        ImmutableLHashSeparateKVDoubleByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMapOf(double k1, byte v1,
             double k2, byte v2, double k3, byte v3,
             double k4, byte v4) {
        ImmutableLHashSeparateKVDoubleByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleByteMap newImmutableMapOf(double k1, byte v1,
             double k2, byte v2, double k3, byte v3,
             double k4, byte v4, double k5, byte v5) {
        ImmutableLHashSeparateKVDoubleByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

