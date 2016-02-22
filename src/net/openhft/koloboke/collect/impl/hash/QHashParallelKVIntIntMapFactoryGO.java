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
import net.openhft.koloboke.collect.map.hash.HashIntIntMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashIntIntMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashParallelKVIntIntMapFactoryGO
        extends QHashParallelKVIntIntMapFactorySO {

    QHashParallelKVIntIntMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashIntIntMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    abstract HashIntIntMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    abstract HashIntIntMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    @Override
    public final HashIntIntMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashIntIntMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashIntIntMapFactory withDomain(int lower, int upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashIntIntMapFactory withKeysDomain(int lower, int upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashIntIntMapFactory withKeysDomainComplement(int lower, int upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((int) (upper + 1), (int) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashIntIntMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashIntIntMapFactory) {
            HashIntIntMapFactory factory = (HashIntIntMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashParallelKVIntIntMapGO shrunk(
            UpdatableQHashParallelKVIntIntMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashParallelKVIntIntMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMap(
            Map<Integer, Integer> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMap(
            Map<Integer, Integer> map1, Map<Integer, Integer> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMap(
            Map<Integer, Integer> map1, Map<Integer, Integer> map2,
            Map<Integer, Integer> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMap(
            Map<Integer, Integer> map1, Map<Integer, Integer> map2,
            Map<Integer, Integer> map3, Map<Integer, Integer> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMap(
            Map<Integer, Integer> map1, Map<Integer, Integer> map2,
            Map<Integer, Integer> map3, Map<Integer, Integer> map4,
            Map<Integer, Integer> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMap(
            Map<Integer, Integer> map1, Map<Integer, Integer> map2,
            int expectedSize) {
        UpdatableQHashParallelKVIntIntMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMap(
            Map<Integer, Integer> map1, Map<Integer, Integer> map2,
            Map<Integer, Integer> map3, int expectedSize) {
        UpdatableQHashParallelKVIntIntMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMap(
            Map<Integer, Integer> map1, Map<Integer, Integer> map2,
            Map<Integer, Integer> map3, Map<Integer, Integer> map4,
            int expectedSize) {
        UpdatableQHashParallelKVIntIntMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMap(
            Map<Integer, Integer> map1, Map<Integer, Integer> map2,
            Map<Integer, Integer> map3, Map<Integer, Integer> map4,
            Map<Integer, Integer> map5, int expectedSize) {
        UpdatableQHashParallelKVIntIntMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.IntIntConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.IntIntConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashParallelKVIntIntMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.IntIntConsumer() {
             @Override
             public void accept(int k, int v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMap(
            int[] keys, int[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMap(
            int[] keys, int[] values, int expectedSize) {
        UpdatableQHashParallelKVIntIntMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMap(
            Integer[] keys, Integer[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMap(
            Integer[] keys, Integer[] values, int expectedSize) {
        UpdatableQHashParallelKVIntIntMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMap(
            Iterable<Integer> keys, Iterable<Integer> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMap(
            Iterable<Integer> keys, Iterable<Integer> values, int expectedSize) {
        UpdatableQHashParallelKVIntIntMapGO map = newUpdatableMap(expectedSize);
        Iterator<Integer> keysIt = keys.iterator();
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
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMapOf(
            int k1, int v1) {
        UpdatableQHashParallelKVIntIntMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMapOf(
            int k1, int v1, int k2, int v2) {
        UpdatableQHashParallelKVIntIntMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMapOf(
            int k1, int v1, int k2, int v2,
            int k3, int v3) {
        UpdatableQHashParallelKVIntIntMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMapOf(
            int k1, int v1, int k2, int v2,
            int k3, int v3, int k4, int v4) {
        UpdatableQHashParallelKVIntIntMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntIntMapGO newUpdatableMapOf(
            int k1, int v1, int k2, int v2,
            int k3, int v3, int k4, int v4,
            int k5, int v5) {
        UpdatableQHashParallelKVIntIntMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashIntIntMap newMutableMap(Map<Integer, Integer> map1,
            Map<Integer, Integer> map2, int expectedSize) {
        MutableQHashParallelKVIntIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newMutableMap(Map<Integer, Integer> map1,
            Map<Integer, Integer> map2, Map<Integer, Integer> map3, int expectedSize) {
        MutableQHashParallelKVIntIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newMutableMap(Map<Integer, Integer> map1,
            Map<Integer, Integer> map2, Map<Integer, Integer> map3,
            Map<Integer, Integer> map4, int expectedSize) {
        MutableQHashParallelKVIntIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newMutableMap(Map<Integer, Integer> map1,
            Map<Integer, Integer> map2, Map<Integer, Integer> map3,
            Map<Integer, Integer> map4, Map<Integer, Integer> map5, int expectedSize) {
        MutableQHashParallelKVIntIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntIntMap newMutableMap(
            Consumer<net.openhft.koloboke.function.IntIntConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashParallelKVIntIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newMutableMap(int[] keys,
            int[] values, int expectedSize) {
        MutableQHashParallelKVIntIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newMutableMap(
            Integer[] keys, Integer[] values, int expectedSize) {
        MutableQHashParallelKVIntIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newMutableMap(Iterable<Integer> keys,
            Iterable<Integer> values, int expectedSize) {
        MutableQHashParallelKVIntIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashIntIntMap newMutableMap(
            Map<Integer, Integer> map) {
        MutableQHashParallelKVIntIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newMutableMap(Map<Integer, Integer> map1,
            Map<Integer, Integer> map2) {
        MutableQHashParallelKVIntIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newMutableMap(Map<Integer, Integer> map1,
            Map<Integer, Integer> map2, Map<Integer, Integer> map3) {
        MutableQHashParallelKVIntIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newMutableMap(Map<Integer, Integer> map1,
            Map<Integer, Integer> map2, Map<Integer, Integer> map3,
            Map<Integer, Integer> map4) {
        MutableQHashParallelKVIntIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newMutableMap(Map<Integer, Integer> map1,
            Map<Integer, Integer> map2, Map<Integer, Integer> map3,
            Map<Integer, Integer> map4, Map<Integer, Integer> map5) {
        MutableQHashParallelKVIntIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntIntMap newMutableMap(
            Consumer<net.openhft.koloboke.function.IntIntConsumer> entriesSupplier
            ) {
        MutableQHashParallelKVIntIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newMutableMap(int[] keys,
            int[] values) {
        MutableQHashParallelKVIntIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newMutableMap(
            Integer[] keys, Integer[] values) {
        MutableQHashParallelKVIntIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newMutableMap(Iterable<Integer> keys,
            Iterable<Integer> values) {
        MutableQHashParallelKVIntIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashIntIntMap newMutableMapOf(int k1, int v1) {
        MutableQHashParallelKVIntIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newMutableMapOf(int k1, int v1,
             int k2, int v2) {
        MutableQHashParallelKVIntIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newMutableMapOf(int k1, int v1,
             int k2, int v2, int k3, int v3) {
        MutableQHashParallelKVIntIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newMutableMapOf(int k1, int v1,
             int k2, int v2, int k3, int v3,
             int k4, int v4) {
        MutableQHashParallelKVIntIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newMutableMapOf(int k1, int v1,
             int k2, int v2, int k3, int v3,
             int k4, int v4, int k5, int v5) {
        MutableQHashParallelKVIntIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMap(Map<Integer, Integer> map1,
            Map<Integer, Integer> map2, int expectedSize) {
        ImmutableQHashParallelKVIntIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMap(Map<Integer, Integer> map1,
            Map<Integer, Integer> map2, Map<Integer, Integer> map3, int expectedSize) {
        ImmutableQHashParallelKVIntIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMap(Map<Integer, Integer> map1,
            Map<Integer, Integer> map2, Map<Integer, Integer> map3,
            Map<Integer, Integer> map4, int expectedSize) {
        ImmutableQHashParallelKVIntIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMap(Map<Integer, Integer> map1,
            Map<Integer, Integer> map2, Map<Integer, Integer> map3,
            Map<Integer, Integer> map4, Map<Integer, Integer> map5, int expectedSize) {
        ImmutableQHashParallelKVIntIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.IntIntConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashParallelKVIntIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMap(int[] keys,
            int[] values, int expectedSize) {
        ImmutableQHashParallelKVIntIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMap(
            Integer[] keys, Integer[] values, int expectedSize) {
        ImmutableQHashParallelKVIntIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMap(Iterable<Integer> keys,
            Iterable<Integer> values, int expectedSize) {
        ImmutableQHashParallelKVIntIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMap(
            Map<Integer, Integer> map) {
        ImmutableQHashParallelKVIntIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMap(Map<Integer, Integer> map1,
            Map<Integer, Integer> map2) {
        ImmutableQHashParallelKVIntIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMap(Map<Integer, Integer> map1,
            Map<Integer, Integer> map2, Map<Integer, Integer> map3) {
        ImmutableQHashParallelKVIntIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMap(Map<Integer, Integer> map1,
            Map<Integer, Integer> map2, Map<Integer, Integer> map3,
            Map<Integer, Integer> map4) {
        ImmutableQHashParallelKVIntIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMap(Map<Integer, Integer> map1,
            Map<Integer, Integer> map2, Map<Integer, Integer> map3,
            Map<Integer, Integer> map4, Map<Integer, Integer> map5) {
        ImmutableQHashParallelKVIntIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.IntIntConsumer> entriesSupplier
            ) {
        ImmutableQHashParallelKVIntIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMap(int[] keys,
            int[] values) {
        ImmutableQHashParallelKVIntIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMap(
            Integer[] keys, Integer[] values) {
        ImmutableQHashParallelKVIntIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMap(Iterable<Integer> keys,
            Iterable<Integer> values) {
        ImmutableQHashParallelKVIntIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMapOf(int k1, int v1) {
        ImmutableQHashParallelKVIntIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMapOf(int k1, int v1,
             int k2, int v2) {
        ImmutableQHashParallelKVIntIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMapOf(int k1, int v1,
             int k2, int v2, int k3, int v3) {
        ImmutableQHashParallelKVIntIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMapOf(int k1, int v1,
             int k2, int v2, int k3, int v3,
             int k4, int v4) {
        ImmutableQHashParallelKVIntIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntIntMap newImmutableMapOf(int k1, int v1,
             int k2, int v2, int k3, int v3,
             int k4, int v4, int k5, int v5) {
        ImmutableQHashParallelKVIntIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

