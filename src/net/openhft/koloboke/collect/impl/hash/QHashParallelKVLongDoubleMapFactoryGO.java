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
import net.openhft.koloboke.collect.map.hash.HashLongDoubleMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashLongDoubleMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashParallelKVLongDoubleMapFactoryGO
        extends QHashParallelKVLongDoubleMapFactorySO {

    QHashParallelKVLongDoubleMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashLongDoubleMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper);

    abstract HashLongDoubleMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper);

    abstract HashLongDoubleMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper);

    @Override
    public final HashLongDoubleMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashLongDoubleMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashLongDoubleMapFactory withDomain(long lower, long upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashLongDoubleMapFactory withKeysDomain(long lower, long upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashLongDoubleMapFactory withKeysDomainComplement(long lower, long upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((long) (upper + 1), (long) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashLongDoubleMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashLongDoubleMapFactory) {
            HashLongDoubleMapFactory factory = (HashLongDoubleMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashParallelKVLongDoubleMapGO shrunk(
            UpdatableQHashParallelKVLongDoubleMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashParallelKVLongDoubleMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMap(
            Map<Long, Double> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMap(
            Map<Long, Double> map1, Map<Long, Double> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMap(
            Map<Long, Double> map1, Map<Long, Double> map2,
            Map<Long, Double> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMap(
            Map<Long, Double> map1, Map<Long, Double> map2,
            Map<Long, Double> map3, Map<Long, Double> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMap(
            Map<Long, Double> map1, Map<Long, Double> map2,
            Map<Long, Double> map3, Map<Long, Double> map4,
            Map<Long, Double> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMap(
            Map<Long, Double> map1, Map<Long, Double> map2,
            int expectedSize) {
        UpdatableQHashParallelKVLongDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMap(
            Map<Long, Double> map1, Map<Long, Double> map2,
            Map<Long, Double> map3, int expectedSize) {
        UpdatableQHashParallelKVLongDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMap(
            Map<Long, Double> map1, Map<Long, Double> map2,
            Map<Long, Double> map3, Map<Long, Double> map4,
            int expectedSize) {
        UpdatableQHashParallelKVLongDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMap(
            Map<Long, Double> map1, Map<Long, Double> map2,
            Map<Long, Double> map3, Map<Long, Double> map4,
            Map<Long, Double> map5, int expectedSize) {
        UpdatableQHashParallelKVLongDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.LongDoubleConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.LongDoubleConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashParallelKVLongDoubleMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.LongDoubleConsumer() {
             @Override
             public void accept(long k, double v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMap(
            long[] keys, double[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMap(
            long[] keys, double[] values, int expectedSize) {
        UpdatableQHashParallelKVLongDoubleMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMap(
            Long[] keys, Double[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMap(
            Long[] keys, Double[] values, int expectedSize) {
        UpdatableQHashParallelKVLongDoubleMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMap(
            Iterable<Long> keys, Iterable<Double> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMap(
            Iterable<Long> keys, Iterable<Double> values, int expectedSize) {
        UpdatableQHashParallelKVLongDoubleMapGO map = newUpdatableMap(expectedSize);
        Iterator<Long> keysIt = keys.iterator();
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
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMapOf(
            long k1, double v1) {
        UpdatableQHashParallelKVLongDoubleMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMapOf(
            long k1, double v1, long k2, double v2) {
        UpdatableQHashParallelKVLongDoubleMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMapOf(
            long k1, double v1, long k2, double v2,
            long k3, double v3) {
        UpdatableQHashParallelKVLongDoubleMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMapOf(
            long k1, double v1, long k2, double v2,
            long k3, double v3, long k4, double v4) {
        UpdatableQHashParallelKVLongDoubleMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVLongDoubleMapGO newUpdatableMapOf(
            long k1, double v1, long k2, double v2,
            long k3, double v3, long k4, double v4,
            long k5, double v5) {
        UpdatableQHashParallelKVLongDoubleMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, int expectedSize) {
        MutableQHashParallelKVLongDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3, int expectedSize) {
        MutableQHashParallelKVLongDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3,
            Map<Long, Double> map4, int expectedSize) {
        MutableQHashParallelKVLongDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3,
            Map<Long, Double> map4, Map<Long, Double> map5, int expectedSize) {
        MutableQHashParallelKVLongDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(
            Consumer<net.openhft.koloboke.function.LongDoubleConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(long[] keys,
            double[] values, int expectedSize) {
        MutableQHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(
            Long[] keys, Double[] values, int expectedSize) {
        MutableQHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(Iterable<Long> keys,
            Iterable<Double> values, int expectedSize) {
        MutableQHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(
            Map<Long, Double> map) {
        MutableQHashParallelKVLongDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2) {
        MutableQHashParallelKVLongDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3) {
        MutableQHashParallelKVLongDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3,
            Map<Long, Double> map4) {
        MutableQHashParallelKVLongDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3,
            Map<Long, Double> map4, Map<Long, Double> map5) {
        MutableQHashParallelKVLongDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(
            Consumer<net.openhft.koloboke.function.LongDoubleConsumer> entriesSupplier
            ) {
        MutableQHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(long[] keys,
            double[] values) {
        MutableQHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(
            Long[] keys, Double[] values) {
        MutableQHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(Iterable<Long> keys,
            Iterable<Double> values) {
        MutableQHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMapOf(long k1, double v1) {
        MutableQHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMapOf(long k1, double v1,
             long k2, double v2) {
        MutableQHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMapOf(long k1, double v1,
             long k2, double v2, long k3, double v3) {
        MutableQHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMapOf(long k1, double v1,
             long k2, double v2, long k3, double v3,
             long k4, double v4) {
        MutableQHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMapOf(long k1, double v1,
             long k2, double v2, long k3, double v3,
             long k4, double v4, long k5, double v5) {
        MutableQHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, int expectedSize) {
        ImmutableQHashParallelKVLongDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3, int expectedSize) {
        ImmutableQHashParallelKVLongDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3,
            Map<Long, Double> map4, int expectedSize) {
        ImmutableQHashParallelKVLongDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3,
            Map<Long, Double> map4, Map<Long, Double> map5, int expectedSize) {
        ImmutableQHashParallelKVLongDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.LongDoubleConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(long[] keys,
            double[] values, int expectedSize) {
        ImmutableQHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(
            Long[] keys, Double[] values, int expectedSize) {
        ImmutableQHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(Iterable<Long> keys,
            Iterable<Double> values, int expectedSize) {
        ImmutableQHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(
            Map<Long, Double> map) {
        ImmutableQHashParallelKVLongDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2) {
        ImmutableQHashParallelKVLongDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3) {
        ImmutableQHashParallelKVLongDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3,
            Map<Long, Double> map4) {
        ImmutableQHashParallelKVLongDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3,
            Map<Long, Double> map4, Map<Long, Double> map5) {
        ImmutableQHashParallelKVLongDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.LongDoubleConsumer> entriesSupplier
            ) {
        ImmutableQHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(long[] keys,
            double[] values) {
        ImmutableQHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(
            Long[] keys, Double[] values) {
        ImmutableQHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(Iterable<Long> keys,
            Iterable<Double> values) {
        ImmutableQHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMapOf(long k1, double v1) {
        ImmutableQHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMapOf(long k1, double v1,
             long k2, double v2) {
        ImmutableQHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMapOf(long k1, double v1,
             long k2, double v2, long k3, double v3) {
        ImmutableQHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMapOf(long k1, double v1,
             long k2, double v2, long k3, double v3,
             long k4, double v4) {
        ImmutableQHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMapOf(long k1, double v1,
             long k2, double v2, long k3, double v3,
             long k4, double v4, long k5, double v5) {
        ImmutableQHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

