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


public abstract class LHashParallelKVLongDoubleMapFactoryGO
        extends LHashParallelKVLongDoubleMapFactorySO {

    LHashParallelKVLongDoubleMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashParallelKVLongDoubleMapGO shrunk(
            UpdatableLHashParallelKVLongDoubleMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashParallelKVLongDoubleMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMap(
            Map<Long, Double> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMap(
            Map<Long, Double> map1, Map<Long, Double> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMap(
            Map<Long, Double> map1, Map<Long, Double> map2,
            Map<Long, Double> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMap(
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
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMap(
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
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMap(
            Map<Long, Double> map1, Map<Long, Double> map2,
            int expectedSize) {
        UpdatableLHashParallelKVLongDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMap(
            Map<Long, Double> map1, Map<Long, Double> map2,
            Map<Long, Double> map3, int expectedSize) {
        UpdatableLHashParallelKVLongDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMap(
            Map<Long, Double> map1, Map<Long, Double> map2,
            Map<Long, Double> map3, Map<Long, Double> map4,
            int expectedSize) {
        UpdatableLHashParallelKVLongDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMap(
            Map<Long, Double> map1, Map<Long, Double> map2,
            Map<Long, Double> map3, Map<Long, Double> map4,
            Map<Long, Double> map5, int expectedSize) {
        UpdatableLHashParallelKVLongDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.LongDoubleConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.LongDoubleConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashParallelKVLongDoubleMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMap(
            long[] keys, double[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMap(
            long[] keys, double[] values, int expectedSize) {
        UpdatableLHashParallelKVLongDoubleMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMap(
            Long[] keys, Double[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMap(
            Long[] keys, Double[] values, int expectedSize) {
        UpdatableLHashParallelKVLongDoubleMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMap(
            Iterable<Long> keys, Iterable<Double> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMap(
            Iterable<Long> keys, Iterable<Double> values, int expectedSize) {
        UpdatableLHashParallelKVLongDoubleMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMapOf(
            long k1, double v1) {
        UpdatableLHashParallelKVLongDoubleMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMapOf(
            long k1, double v1, long k2, double v2) {
        UpdatableLHashParallelKVLongDoubleMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMapOf(
            long k1, double v1, long k2, double v2,
            long k3, double v3) {
        UpdatableLHashParallelKVLongDoubleMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMapOf(
            long k1, double v1, long k2, double v2,
            long k3, double v3, long k4, double v4) {
        UpdatableLHashParallelKVLongDoubleMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVLongDoubleMapGO newUpdatableMapOf(
            long k1, double v1, long k2, double v2,
            long k3, double v3, long k4, double v4,
            long k5, double v5) {
        UpdatableLHashParallelKVLongDoubleMapGO map = newUpdatableMap(5);
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
        MutableLHashParallelKVLongDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3, int expectedSize) {
        MutableLHashParallelKVLongDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3,
            Map<Long, Double> map4, int expectedSize) {
        MutableLHashParallelKVLongDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3,
            Map<Long, Double> map4, Map<Long, Double> map5, int expectedSize) {
        MutableLHashParallelKVLongDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(
            Consumer<net.openhft.koloboke.function.LongDoubleConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(long[] keys,
            double[] values, int expectedSize) {
        MutableLHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(
            Long[] keys, Double[] values, int expectedSize) {
        MutableLHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(Iterable<Long> keys,
            Iterable<Double> values, int expectedSize) {
        MutableLHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(
            Map<Long, Double> map) {
        MutableLHashParallelKVLongDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2) {
        MutableLHashParallelKVLongDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3) {
        MutableLHashParallelKVLongDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3,
            Map<Long, Double> map4) {
        MutableLHashParallelKVLongDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3,
            Map<Long, Double> map4, Map<Long, Double> map5) {
        MutableLHashParallelKVLongDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(
            Consumer<net.openhft.koloboke.function.LongDoubleConsumer> entriesSupplier
            ) {
        MutableLHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(long[] keys,
            double[] values) {
        MutableLHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(
            Long[] keys, Double[] values) {
        MutableLHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMap(Iterable<Long> keys,
            Iterable<Double> values) {
        MutableLHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMapOf(long k1, double v1) {
        MutableLHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMapOf(long k1, double v1,
             long k2, double v2) {
        MutableLHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMapOf(long k1, double v1,
             long k2, double v2, long k3, double v3) {
        MutableLHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMapOf(long k1, double v1,
             long k2, double v2, long k3, double v3,
             long k4, double v4) {
        MutableLHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newMutableMapOf(long k1, double v1,
             long k2, double v2, long k3, double v3,
             long k4, double v4, long k5, double v5) {
        MutableLHashParallelKVLongDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, int expectedSize) {
        ImmutableLHashParallelKVLongDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3, int expectedSize) {
        ImmutableLHashParallelKVLongDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3,
            Map<Long, Double> map4, int expectedSize) {
        ImmutableLHashParallelKVLongDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3,
            Map<Long, Double> map4, Map<Long, Double> map5, int expectedSize) {
        ImmutableLHashParallelKVLongDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.LongDoubleConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(long[] keys,
            double[] values, int expectedSize) {
        ImmutableLHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(
            Long[] keys, Double[] values, int expectedSize) {
        ImmutableLHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(Iterable<Long> keys,
            Iterable<Double> values, int expectedSize) {
        ImmutableLHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(
            Map<Long, Double> map) {
        ImmutableLHashParallelKVLongDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2) {
        ImmutableLHashParallelKVLongDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3) {
        ImmutableLHashParallelKVLongDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3,
            Map<Long, Double> map4) {
        ImmutableLHashParallelKVLongDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(Map<Long, Double> map1,
            Map<Long, Double> map2, Map<Long, Double> map3,
            Map<Long, Double> map4, Map<Long, Double> map5) {
        ImmutableLHashParallelKVLongDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.LongDoubleConsumer> entriesSupplier
            ) {
        ImmutableLHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(long[] keys,
            double[] values) {
        ImmutableLHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(
            Long[] keys, Double[] values) {
        ImmutableLHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMap(Iterable<Long> keys,
            Iterable<Double> values) {
        ImmutableLHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMapOf(long k1, double v1) {
        ImmutableLHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMapOf(long k1, double v1,
             long k2, double v2) {
        ImmutableLHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMapOf(long k1, double v1,
             long k2, double v2, long k3, double v3) {
        ImmutableLHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMapOf(long k1, double v1,
             long k2, double v2, long k3, double v3,
             long k4, double v4) {
        ImmutableLHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongDoubleMap newImmutableMapOf(long k1, double v1,
             long k2, double v2, long k3, double v3,
             long k4, double v4, long k5, double v5) {
        ImmutableLHashParallelKVLongDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

