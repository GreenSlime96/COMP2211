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
import net.openhft.koloboke.collect.map.hash.HashLongIntMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashLongIntMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashSeparateKVLongIntMapFactoryGO
        extends QHashSeparateKVLongIntMapFactorySO {

    QHashSeparateKVLongIntMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashLongIntMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper);

    abstract HashLongIntMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper);

    abstract HashLongIntMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper);

    @Override
    public final HashLongIntMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashLongIntMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashLongIntMapFactory withDomain(long lower, long upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashLongIntMapFactory withKeysDomain(long lower, long upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashLongIntMapFactory withKeysDomainComplement(long lower, long upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((long) (upper + 1), (long) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashLongIntMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashLongIntMapFactory) {
            HashLongIntMapFactory factory = (HashLongIntMapFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory) &&
                    // boxing to treat NaNs correctly
                   ((Integer) getDefaultValue()).equals(factory.getDefaultValue())
                    ;
        } else {
            return false;
        }
    }

    @Override
    public int getDefaultValue() {
        return 0;
    }

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashSeparateKVLongIntMapGO shrunk(
            UpdatableQHashSeparateKVLongIntMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashSeparateKVLongIntMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMap(
            Map<Long, Integer> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMap(
            Map<Long, Integer> map1, Map<Long, Integer> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMap(
            Map<Long, Integer> map1, Map<Long, Integer> map2,
            Map<Long, Integer> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMap(
            Map<Long, Integer> map1, Map<Long, Integer> map2,
            Map<Long, Integer> map3, Map<Long, Integer> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMap(
            Map<Long, Integer> map1, Map<Long, Integer> map2,
            Map<Long, Integer> map3, Map<Long, Integer> map4,
            Map<Long, Integer> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMap(
            Map<Long, Integer> map1, Map<Long, Integer> map2,
            int expectedSize) {
        UpdatableQHashSeparateKVLongIntMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMap(
            Map<Long, Integer> map1, Map<Long, Integer> map2,
            Map<Long, Integer> map3, int expectedSize) {
        UpdatableQHashSeparateKVLongIntMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMap(
            Map<Long, Integer> map1, Map<Long, Integer> map2,
            Map<Long, Integer> map3, Map<Long, Integer> map4,
            int expectedSize) {
        UpdatableQHashSeparateKVLongIntMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMap(
            Map<Long, Integer> map1, Map<Long, Integer> map2,
            Map<Long, Integer> map3, Map<Long, Integer> map4,
            Map<Long, Integer> map5, int expectedSize) {
        UpdatableQHashSeparateKVLongIntMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.LongIntConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.LongIntConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashSeparateKVLongIntMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.LongIntConsumer() {
             @Override
             public void accept(long k, int v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMap(
            long[] keys, int[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMap(
            long[] keys, int[] values, int expectedSize) {
        UpdatableQHashSeparateKVLongIntMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMap(
            Long[] keys, Integer[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMap(
            Long[] keys, Integer[] values, int expectedSize) {
        UpdatableQHashSeparateKVLongIntMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMap(
            Iterable<Long> keys, Iterable<Integer> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMap(
            Iterable<Long> keys, Iterable<Integer> values, int expectedSize) {
        UpdatableQHashSeparateKVLongIntMapGO map = newUpdatableMap(expectedSize);
        Iterator<Long> keysIt = keys.iterator();
        Iterator<Integer> valuesIt = values.iterator();
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
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMapOf(
            long k1, int v1) {
        UpdatableQHashSeparateKVLongIntMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMapOf(
            long k1, int v1, long k2, int v2) {
        UpdatableQHashSeparateKVLongIntMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMapOf(
            long k1, int v1, long k2, int v2,
            long k3, int v3) {
        UpdatableQHashSeparateKVLongIntMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMapOf(
            long k1, int v1, long k2, int v2,
            long k3, int v3, long k4, int v4) {
        UpdatableQHashSeparateKVLongIntMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongIntMapGO newUpdatableMapOf(
            long k1, int v1, long k2, int v2,
            long k3, int v3, long k4, int v4,
            long k5, int v5) {
        UpdatableQHashSeparateKVLongIntMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashLongIntMap newMutableMap(Map<Long, Integer> map1,
            Map<Long, Integer> map2, int expectedSize) {
        MutableQHashSeparateKVLongIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newMutableMap(Map<Long, Integer> map1,
            Map<Long, Integer> map2, Map<Long, Integer> map3, int expectedSize) {
        MutableQHashSeparateKVLongIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newMutableMap(Map<Long, Integer> map1,
            Map<Long, Integer> map2, Map<Long, Integer> map3,
            Map<Long, Integer> map4, int expectedSize) {
        MutableQHashSeparateKVLongIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newMutableMap(Map<Long, Integer> map1,
            Map<Long, Integer> map2, Map<Long, Integer> map3,
            Map<Long, Integer> map4, Map<Long, Integer> map5, int expectedSize) {
        MutableQHashSeparateKVLongIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongIntMap newMutableMap(
            Consumer<net.openhft.koloboke.function.LongIntConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashSeparateKVLongIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newMutableMap(long[] keys,
            int[] values, int expectedSize) {
        MutableQHashSeparateKVLongIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newMutableMap(
            Long[] keys, Integer[] values, int expectedSize) {
        MutableQHashSeparateKVLongIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newMutableMap(Iterable<Long> keys,
            Iterable<Integer> values, int expectedSize) {
        MutableQHashSeparateKVLongIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashLongIntMap newMutableMap(
            Map<Long, Integer> map) {
        MutableQHashSeparateKVLongIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newMutableMap(Map<Long, Integer> map1,
            Map<Long, Integer> map2) {
        MutableQHashSeparateKVLongIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newMutableMap(Map<Long, Integer> map1,
            Map<Long, Integer> map2, Map<Long, Integer> map3) {
        MutableQHashSeparateKVLongIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newMutableMap(Map<Long, Integer> map1,
            Map<Long, Integer> map2, Map<Long, Integer> map3,
            Map<Long, Integer> map4) {
        MutableQHashSeparateKVLongIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newMutableMap(Map<Long, Integer> map1,
            Map<Long, Integer> map2, Map<Long, Integer> map3,
            Map<Long, Integer> map4, Map<Long, Integer> map5) {
        MutableQHashSeparateKVLongIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongIntMap newMutableMap(
            Consumer<net.openhft.koloboke.function.LongIntConsumer> entriesSupplier
            ) {
        MutableQHashSeparateKVLongIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newMutableMap(long[] keys,
            int[] values) {
        MutableQHashSeparateKVLongIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newMutableMap(
            Long[] keys, Integer[] values) {
        MutableQHashSeparateKVLongIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newMutableMap(Iterable<Long> keys,
            Iterable<Integer> values) {
        MutableQHashSeparateKVLongIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashLongIntMap newMutableMapOf(long k1, int v1) {
        MutableQHashSeparateKVLongIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newMutableMapOf(long k1, int v1,
             long k2, int v2) {
        MutableQHashSeparateKVLongIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newMutableMapOf(long k1, int v1,
             long k2, int v2, long k3, int v3) {
        MutableQHashSeparateKVLongIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newMutableMapOf(long k1, int v1,
             long k2, int v2, long k3, int v3,
             long k4, int v4) {
        MutableQHashSeparateKVLongIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newMutableMapOf(long k1, int v1,
             long k2, int v2, long k3, int v3,
             long k4, int v4, long k5, int v5) {
        MutableQHashSeparateKVLongIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMap(Map<Long, Integer> map1,
            Map<Long, Integer> map2, int expectedSize) {
        ImmutableQHashSeparateKVLongIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMap(Map<Long, Integer> map1,
            Map<Long, Integer> map2, Map<Long, Integer> map3, int expectedSize) {
        ImmutableQHashSeparateKVLongIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMap(Map<Long, Integer> map1,
            Map<Long, Integer> map2, Map<Long, Integer> map3,
            Map<Long, Integer> map4, int expectedSize) {
        ImmutableQHashSeparateKVLongIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMap(Map<Long, Integer> map1,
            Map<Long, Integer> map2, Map<Long, Integer> map3,
            Map<Long, Integer> map4, Map<Long, Integer> map5, int expectedSize) {
        ImmutableQHashSeparateKVLongIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.LongIntConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashSeparateKVLongIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMap(long[] keys,
            int[] values, int expectedSize) {
        ImmutableQHashSeparateKVLongIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMap(
            Long[] keys, Integer[] values, int expectedSize) {
        ImmutableQHashSeparateKVLongIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMap(Iterable<Long> keys,
            Iterable<Integer> values, int expectedSize) {
        ImmutableQHashSeparateKVLongIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMap(
            Map<Long, Integer> map) {
        ImmutableQHashSeparateKVLongIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMap(Map<Long, Integer> map1,
            Map<Long, Integer> map2) {
        ImmutableQHashSeparateKVLongIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMap(Map<Long, Integer> map1,
            Map<Long, Integer> map2, Map<Long, Integer> map3) {
        ImmutableQHashSeparateKVLongIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMap(Map<Long, Integer> map1,
            Map<Long, Integer> map2, Map<Long, Integer> map3,
            Map<Long, Integer> map4) {
        ImmutableQHashSeparateKVLongIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMap(Map<Long, Integer> map1,
            Map<Long, Integer> map2, Map<Long, Integer> map3,
            Map<Long, Integer> map4, Map<Long, Integer> map5) {
        ImmutableQHashSeparateKVLongIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.LongIntConsumer> entriesSupplier
            ) {
        ImmutableQHashSeparateKVLongIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMap(long[] keys,
            int[] values) {
        ImmutableQHashSeparateKVLongIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMap(
            Long[] keys, Integer[] values) {
        ImmutableQHashSeparateKVLongIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMap(Iterable<Long> keys,
            Iterable<Integer> values) {
        ImmutableQHashSeparateKVLongIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMapOf(long k1, int v1) {
        ImmutableQHashSeparateKVLongIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMapOf(long k1, int v1,
             long k2, int v2) {
        ImmutableQHashSeparateKVLongIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMapOf(long k1, int v1,
             long k2, int v2, long k3, int v3) {
        ImmutableQHashSeparateKVLongIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMapOf(long k1, int v1,
             long k2, int v2, long k3, int v3,
             long k4, int v4) {
        ImmutableQHashSeparateKVLongIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongIntMap newImmutableMapOf(long k1, int v1,
             long k2, int v2, long k3, int v3,
             long k4, int v4, long k5, int v5) {
        ImmutableQHashSeparateKVLongIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

