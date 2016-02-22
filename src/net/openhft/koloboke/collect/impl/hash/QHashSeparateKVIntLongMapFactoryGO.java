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
import net.openhft.koloboke.collect.map.hash.HashIntLongMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashIntLongMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashSeparateKVIntLongMapFactoryGO
        extends QHashSeparateKVIntLongMapFactorySO {

    QHashSeparateKVIntLongMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashIntLongMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    abstract HashIntLongMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    abstract HashIntLongMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    @Override
    public final HashIntLongMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashIntLongMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashIntLongMapFactory withDomain(int lower, int upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashIntLongMapFactory withKeysDomain(int lower, int upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashIntLongMapFactory withKeysDomainComplement(int lower, int upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((int) (upper + 1), (int) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashIntLongMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashIntLongMapFactory) {
            HashIntLongMapFactory factory = (HashIntLongMapFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory) &&
                    // boxing to treat NaNs correctly
                   ((Long) getDefaultValue()).equals(factory.getDefaultValue())
                    ;
        } else {
            return false;
        }
    }

    @Override
    public long getDefaultValue() {
        return 0L;
    }

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashSeparateKVIntLongMapGO shrunk(
            UpdatableQHashSeparateKVIntLongMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashSeparateKVIntLongMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMap(
            Map<Integer, Long> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMap(
            Map<Integer, Long> map1, Map<Integer, Long> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMap(
            Map<Integer, Long> map1, Map<Integer, Long> map2,
            Map<Integer, Long> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMap(
            Map<Integer, Long> map1, Map<Integer, Long> map2,
            Map<Integer, Long> map3, Map<Integer, Long> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMap(
            Map<Integer, Long> map1, Map<Integer, Long> map2,
            Map<Integer, Long> map3, Map<Integer, Long> map4,
            Map<Integer, Long> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMap(
            Map<Integer, Long> map1, Map<Integer, Long> map2,
            int expectedSize) {
        UpdatableQHashSeparateKVIntLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMap(
            Map<Integer, Long> map1, Map<Integer, Long> map2,
            Map<Integer, Long> map3, int expectedSize) {
        UpdatableQHashSeparateKVIntLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMap(
            Map<Integer, Long> map1, Map<Integer, Long> map2,
            Map<Integer, Long> map3, Map<Integer, Long> map4,
            int expectedSize) {
        UpdatableQHashSeparateKVIntLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMap(
            Map<Integer, Long> map1, Map<Integer, Long> map2,
            Map<Integer, Long> map3, Map<Integer, Long> map4,
            Map<Integer, Long> map5, int expectedSize) {
        UpdatableQHashSeparateKVIntLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.IntLongConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.IntLongConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashSeparateKVIntLongMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.IntLongConsumer() {
             @Override
             public void accept(int k, long v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMap(
            int[] keys, long[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMap(
            int[] keys, long[] values, int expectedSize) {
        UpdatableQHashSeparateKVIntLongMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMap(
            Integer[] keys, Long[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMap(
            Integer[] keys, Long[] values, int expectedSize) {
        UpdatableQHashSeparateKVIntLongMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMap(
            Iterable<Integer> keys, Iterable<Long> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMap(
            Iterable<Integer> keys, Iterable<Long> values, int expectedSize) {
        UpdatableQHashSeparateKVIntLongMapGO map = newUpdatableMap(expectedSize);
        Iterator<Integer> keysIt = keys.iterator();
        Iterator<Long> valuesIt = values.iterator();
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
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMapOf(
            int k1, long v1) {
        UpdatableQHashSeparateKVIntLongMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMapOf(
            int k1, long v1, int k2, long v2) {
        UpdatableQHashSeparateKVIntLongMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMapOf(
            int k1, long v1, int k2, long v2,
            int k3, long v3) {
        UpdatableQHashSeparateKVIntLongMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMapOf(
            int k1, long v1, int k2, long v2,
            int k3, long v3, int k4, long v4) {
        UpdatableQHashSeparateKVIntLongMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVIntLongMapGO newUpdatableMapOf(
            int k1, long v1, int k2, long v2,
            int k3, long v3, int k4, long v4,
            int k5, long v5) {
        UpdatableQHashSeparateKVIntLongMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashIntLongMap newMutableMap(Map<Integer, Long> map1,
            Map<Integer, Long> map2, int expectedSize) {
        MutableQHashSeparateKVIntLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newMutableMap(Map<Integer, Long> map1,
            Map<Integer, Long> map2, Map<Integer, Long> map3, int expectedSize) {
        MutableQHashSeparateKVIntLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newMutableMap(Map<Integer, Long> map1,
            Map<Integer, Long> map2, Map<Integer, Long> map3,
            Map<Integer, Long> map4, int expectedSize) {
        MutableQHashSeparateKVIntLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newMutableMap(Map<Integer, Long> map1,
            Map<Integer, Long> map2, Map<Integer, Long> map3,
            Map<Integer, Long> map4, Map<Integer, Long> map5, int expectedSize) {
        MutableQHashSeparateKVIntLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntLongMap newMutableMap(
            Consumer<net.openhft.koloboke.function.IntLongConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashSeparateKVIntLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newMutableMap(int[] keys,
            long[] values, int expectedSize) {
        MutableQHashSeparateKVIntLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newMutableMap(
            Integer[] keys, Long[] values, int expectedSize) {
        MutableQHashSeparateKVIntLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newMutableMap(Iterable<Integer> keys,
            Iterable<Long> values, int expectedSize) {
        MutableQHashSeparateKVIntLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashIntLongMap newMutableMap(
            Map<Integer, Long> map) {
        MutableQHashSeparateKVIntLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newMutableMap(Map<Integer, Long> map1,
            Map<Integer, Long> map2) {
        MutableQHashSeparateKVIntLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newMutableMap(Map<Integer, Long> map1,
            Map<Integer, Long> map2, Map<Integer, Long> map3) {
        MutableQHashSeparateKVIntLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newMutableMap(Map<Integer, Long> map1,
            Map<Integer, Long> map2, Map<Integer, Long> map3,
            Map<Integer, Long> map4) {
        MutableQHashSeparateKVIntLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newMutableMap(Map<Integer, Long> map1,
            Map<Integer, Long> map2, Map<Integer, Long> map3,
            Map<Integer, Long> map4, Map<Integer, Long> map5) {
        MutableQHashSeparateKVIntLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntLongMap newMutableMap(
            Consumer<net.openhft.koloboke.function.IntLongConsumer> entriesSupplier
            ) {
        MutableQHashSeparateKVIntLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newMutableMap(int[] keys,
            long[] values) {
        MutableQHashSeparateKVIntLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newMutableMap(
            Integer[] keys, Long[] values) {
        MutableQHashSeparateKVIntLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newMutableMap(Iterable<Integer> keys,
            Iterable<Long> values) {
        MutableQHashSeparateKVIntLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashIntLongMap newMutableMapOf(int k1, long v1) {
        MutableQHashSeparateKVIntLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newMutableMapOf(int k1, long v1,
             int k2, long v2) {
        MutableQHashSeparateKVIntLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newMutableMapOf(int k1, long v1,
             int k2, long v2, int k3, long v3) {
        MutableQHashSeparateKVIntLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newMutableMapOf(int k1, long v1,
             int k2, long v2, int k3, long v3,
             int k4, long v4) {
        MutableQHashSeparateKVIntLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newMutableMapOf(int k1, long v1,
             int k2, long v2, int k3, long v3,
             int k4, long v4, int k5, long v5) {
        MutableQHashSeparateKVIntLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMap(Map<Integer, Long> map1,
            Map<Integer, Long> map2, int expectedSize) {
        ImmutableQHashSeparateKVIntLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMap(Map<Integer, Long> map1,
            Map<Integer, Long> map2, Map<Integer, Long> map3, int expectedSize) {
        ImmutableQHashSeparateKVIntLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMap(Map<Integer, Long> map1,
            Map<Integer, Long> map2, Map<Integer, Long> map3,
            Map<Integer, Long> map4, int expectedSize) {
        ImmutableQHashSeparateKVIntLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMap(Map<Integer, Long> map1,
            Map<Integer, Long> map2, Map<Integer, Long> map3,
            Map<Integer, Long> map4, Map<Integer, Long> map5, int expectedSize) {
        ImmutableQHashSeparateKVIntLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.IntLongConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashSeparateKVIntLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMap(int[] keys,
            long[] values, int expectedSize) {
        ImmutableQHashSeparateKVIntLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMap(
            Integer[] keys, Long[] values, int expectedSize) {
        ImmutableQHashSeparateKVIntLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMap(Iterable<Integer> keys,
            Iterable<Long> values, int expectedSize) {
        ImmutableQHashSeparateKVIntLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMap(
            Map<Integer, Long> map) {
        ImmutableQHashSeparateKVIntLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMap(Map<Integer, Long> map1,
            Map<Integer, Long> map2) {
        ImmutableQHashSeparateKVIntLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMap(Map<Integer, Long> map1,
            Map<Integer, Long> map2, Map<Integer, Long> map3) {
        ImmutableQHashSeparateKVIntLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMap(Map<Integer, Long> map1,
            Map<Integer, Long> map2, Map<Integer, Long> map3,
            Map<Integer, Long> map4) {
        ImmutableQHashSeparateKVIntLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMap(Map<Integer, Long> map1,
            Map<Integer, Long> map2, Map<Integer, Long> map3,
            Map<Integer, Long> map4, Map<Integer, Long> map5) {
        ImmutableQHashSeparateKVIntLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.IntLongConsumer> entriesSupplier
            ) {
        ImmutableQHashSeparateKVIntLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMap(int[] keys,
            long[] values) {
        ImmutableQHashSeparateKVIntLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMap(
            Integer[] keys, Long[] values) {
        ImmutableQHashSeparateKVIntLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMap(Iterable<Integer> keys,
            Iterable<Long> values) {
        ImmutableQHashSeparateKVIntLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMapOf(int k1, long v1) {
        ImmutableQHashSeparateKVIntLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMapOf(int k1, long v1,
             int k2, long v2) {
        ImmutableQHashSeparateKVIntLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMapOf(int k1, long v1,
             int k2, long v2, int k3, long v3) {
        ImmutableQHashSeparateKVIntLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMapOf(int k1, long v1,
             int k2, long v2, int k3, long v3,
             int k4, long v4) {
        ImmutableQHashSeparateKVIntLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntLongMap newImmutableMapOf(int k1, long v1,
             int k2, long v2, int k3, long v3,
             int k4, long v4, int k5, long v5) {
        ImmutableQHashSeparateKVIntLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

