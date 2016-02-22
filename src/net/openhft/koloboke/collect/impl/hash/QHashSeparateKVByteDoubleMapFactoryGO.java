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
import net.openhft.koloboke.collect.map.hash.HashByteDoubleMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashByteDoubleMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashSeparateKVByteDoubleMapFactoryGO
        extends QHashSeparateKVByteDoubleMapFactorySO {

    QHashSeparateKVByteDoubleMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashByteDoubleMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper);

    abstract HashByteDoubleMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper);

    abstract HashByteDoubleMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper);

    @Override
    public final HashByteDoubleMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashByteDoubleMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashByteDoubleMapFactory withDomain(byte lower, byte upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashByteDoubleMapFactory withKeysDomain(byte lower, byte upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashByteDoubleMapFactory withKeysDomainComplement(byte lower, byte upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((byte) (upper + 1), (byte) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashByteDoubleMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashByteDoubleMapFactory) {
            HashByteDoubleMapFactory factory = (HashByteDoubleMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashSeparateKVByteDoubleMapGO shrunk(
            UpdatableQHashSeparateKVByteDoubleMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashSeparateKVByteDoubleMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMap(
            Map<Byte, Double> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMap(
            Map<Byte, Double> map1, Map<Byte, Double> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMap(
            Map<Byte, Double> map1, Map<Byte, Double> map2,
            Map<Byte, Double> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMap(
            Map<Byte, Double> map1, Map<Byte, Double> map2,
            Map<Byte, Double> map3, Map<Byte, Double> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMap(
            Map<Byte, Double> map1, Map<Byte, Double> map2,
            Map<Byte, Double> map3, Map<Byte, Double> map4,
            Map<Byte, Double> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMap(
            Map<Byte, Double> map1, Map<Byte, Double> map2,
            int expectedSize) {
        UpdatableQHashSeparateKVByteDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMap(
            Map<Byte, Double> map1, Map<Byte, Double> map2,
            Map<Byte, Double> map3, int expectedSize) {
        UpdatableQHashSeparateKVByteDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMap(
            Map<Byte, Double> map1, Map<Byte, Double> map2,
            Map<Byte, Double> map3, Map<Byte, Double> map4,
            int expectedSize) {
        UpdatableQHashSeparateKVByteDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMap(
            Map<Byte, Double> map1, Map<Byte, Double> map2,
            Map<Byte, Double> map3, Map<Byte, Double> map4,
            Map<Byte, Double> map5, int expectedSize) {
        UpdatableQHashSeparateKVByteDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ByteDoubleConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ByteDoubleConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashSeparateKVByteDoubleMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.ByteDoubleConsumer() {
             @Override
             public void accept(byte k, double v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMap(
            byte[] keys, double[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMap(
            byte[] keys, double[] values, int expectedSize) {
        UpdatableQHashSeparateKVByteDoubleMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMap(
            Byte[] keys, Double[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMap(
            Byte[] keys, Double[] values, int expectedSize) {
        UpdatableQHashSeparateKVByteDoubleMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMap(
            Iterable<Byte> keys, Iterable<Double> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMap(
            Iterable<Byte> keys, Iterable<Double> values, int expectedSize) {
        UpdatableQHashSeparateKVByteDoubleMapGO map = newUpdatableMap(expectedSize);
        Iterator<Byte> keysIt = keys.iterator();
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
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMapOf(
            byte k1, double v1) {
        UpdatableQHashSeparateKVByteDoubleMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMapOf(
            byte k1, double v1, byte k2, double v2) {
        UpdatableQHashSeparateKVByteDoubleMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMapOf(
            byte k1, double v1, byte k2, double v2,
            byte k3, double v3) {
        UpdatableQHashSeparateKVByteDoubleMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMapOf(
            byte k1, double v1, byte k2, double v2,
            byte k3, double v3, byte k4, double v4) {
        UpdatableQHashSeparateKVByteDoubleMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteDoubleMapGO newUpdatableMapOf(
            byte k1, double v1, byte k2, double v2,
            byte k3, double v3, byte k4, double v4,
            byte k5, double v5) {
        UpdatableQHashSeparateKVByteDoubleMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMap(Map<Byte, Double> map1,
            Map<Byte, Double> map2, int expectedSize) {
        MutableQHashSeparateKVByteDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMap(Map<Byte, Double> map1,
            Map<Byte, Double> map2, Map<Byte, Double> map3, int expectedSize) {
        MutableQHashSeparateKVByteDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMap(Map<Byte, Double> map1,
            Map<Byte, Double> map2, Map<Byte, Double> map3,
            Map<Byte, Double> map4, int expectedSize) {
        MutableQHashSeparateKVByteDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMap(Map<Byte, Double> map1,
            Map<Byte, Double> map2, Map<Byte, Double> map3,
            Map<Byte, Double> map4, Map<Byte, Double> map5, int expectedSize) {
        MutableQHashSeparateKVByteDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ByteDoubleConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashSeparateKVByteDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMap(byte[] keys,
            double[] values, int expectedSize) {
        MutableQHashSeparateKVByteDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMap(
            Byte[] keys, Double[] values, int expectedSize) {
        MutableQHashSeparateKVByteDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMap(Iterable<Byte> keys,
            Iterable<Double> values, int expectedSize) {
        MutableQHashSeparateKVByteDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMap(
            Map<Byte, Double> map) {
        MutableQHashSeparateKVByteDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMap(Map<Byte, Double> map1,
            Map<Byte, Double> map2) {
        MutableQHashSeparateKVByteDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMap(Map<Byte, Double> map1,
            Map<Byte, Double> map2, Map<Byte, Double> map3) {
        MutableQHashSeparateKVByteDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMap(Map<Byte, Double> map1,
            Map<Byte, Double> map2, Map<Byte, Double> map3,
            Map<Byte, Double> map4) {
        MutableQHashSeparateKVByteDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMap(Map<Byte, Double> map1,
            Map<Byte, Double> map2, Map<Byte, Double> map3,
            Map<Byte, Double> map4, Map<Byte, Double> map5) {
        MutableQHashSeparateKVByteDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ByteDoubleConsumer> entriesSupplier
            ) {
        MutableQHashSeparateKVByteDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMap(byte[] keys,
            double[] values) {
        MutableQHashSeparateKVByteDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMap(
            Byte[] keys, Double[] values) {
        MutableQHashSeparateKVByteDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMap(Iterable<Byte> keys,
            Iterable<Double> values) {
        MutableQHashSeparateKVByteDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMapOf(byte k1, double v1) {
        MutableQHashSeparateKVByteDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMapOf(byte k1, double v1,
             byte k2, double v2) {
        MutableQHashSeparateKVByteDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMapOf(byte k1, double v1,
             byte k2, double v2, byte k3, double v3) {
        MutableQHashSeparateKVByteDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMapOf(byte k1, double v1,
             byte k2, double v2, byte k3, double v3,
             byte k4, double v4) {
        MutableQHashSeparateKVByteDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newMutableMapOf(byte k1, double v1,
             byte k2, double v2, byte k3, double v3,
             byte k4, double v4, byte k5, double v5) {
        MutableQHashSeparateKVByteDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMap(Map<Byte, Double> map1,
            Map<Byte, Double> map2, int expectedSize) {
        ImmutableQHashSeparateKVByteDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMap(Map<Byte, Double> map1,
            Map<Byte, Double> map2, Map<Byte, Double> map3, int expectedSize) {
        ImmutableQHashSeparateKVByteDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMap(Map<Byte, Double> map1,
            Map<Byte, Double> map2, Map<Byte, Double> map3,
            Map<Byte, Double> map4, int expectedSize) {
        ImmutableQHashSeparateKVByteDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMap(Map<Byte, Double> map1,
            Map<Byte, Double> map2, Map<Byte, Double> map3,
            Map<Byte, Double> map4, Map<Byte, Double> map5, int expectedSize) {
        ImmutableQHashSeparateKVByteDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ByteDoubleConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashSeparateKVByteDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMap(byte[] keys,
            double[] values, int expectedSize) {
        ImmutableQHashSeparateKVByteDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMap(
            Byte[] keys, Double[] values, int expectedSize) {
        ImmutableQHashSeparateKVByteDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMap(Iterable<Byte> keys,
            Iterable<Double> values, int expectedSize) {
        ImmutableQHashSeparateKVByteDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMap(
            Map<Byte, Double> map) {
        ImmutableQHashSeparateKVByteDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMap(Map<Byte, Double> map1,
            Map<Byte, Double> map2) {
        ImmutableQHashSeparateKVByteDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMap(Map<Byte, Double> map1,
            Map<Byte, Double> map2, Map<Byte, Double> map3) {
        ImmutableQHashSeparateKVByteDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMap(Map<Byte, Double> map1,
            Map<Byte, Double> map2, Map<Byte, Double> map3,
            Map<Byte, Double> map4) {
        ImmutableQHashSeparateKVByteDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMap(Map<Byte, Double> map1,
            Map<Byte, Double> map2, Map<Byte, Double> map3,
            Map<Byte, Double> map4, Map<Byte, Double> map5) {
        ImmutableQHashSeparateKVByteDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ByteDoubleConsumer> entriesSupplier
            ) {
        ImmutableQHashSeparateKVByteDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMap(byte[] keys,
            double[] values) {
        ImmutableQHashSeparateKVByteDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMap(
            Byte[] keys, Double[] values) {
        ImmutableQHashSeparateKVByteDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMap(Iterable<Byte> keys,
            Iterable<Double> values) {
        ImmutableQHashSeparateKVByteDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMapOf(byte k1, double v1) {
        ImmutableQHashSeparateKVByteDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMapOf(byte k1, double v1,
             byte k2, double v2) {
        ImmutableQHashSeparateKVByteDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMapOf(byte k1, double v1,
             byte k2, double v2, byte k3, double v3) {
        ImmutableQHashSeparateKVByteDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMapOf(byte k1, double v1,
             byte k2, double v2, byte k3, double v3,
             byte k4, double v4) {
        ImmutableQHashSeparateKVByteDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteDoubleMap newImmutableMapOf(byte k1, double v1,
             byte k2, double v2, byte k3, double v3,
             byte k4, double v4, byte k5, double v5) {
        ImmutableQHashSeparateKVByteDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

