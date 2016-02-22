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
import net.openhft.koloboke.collect.map.hash.HashIntFloatMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashIntFloatMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashParallelKVIntFloatMapFactoryGO
        extends QHashParallelKVIntFloatMapFactorySO {

    QHashParallelKVIntFloatMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashIntFloatMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    abstract HashIntFloatMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    abstract HashIntFloatMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    @Override
    public final HashIntFloatMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashIntFloatMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashIntFloatMapFactory withDomain(int lower, int upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashIntFloatMapFactory withKeysDomain(int lower, int upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashIntFloatMapFactory withKeysDomainComplement(int lower, int upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((int) (upper + 1), (int) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashIntFloatMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashIntFloatMapFactory) {
            HashIntFloatMapFactory factory = (HashIntFloatMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashParallelKVIntFloatMapGO shrunk(
            UpdatableQHashParallelKVIntFloatMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashParallelKVIntFloatMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMap(
            Map<Integer, Float> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMap(
            Map<Integer, Float> map1, Map<Integer, Float> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMap(
            Map<Integer, Float> map1, Map<Integer, Float> map2,
            Map<Integer, Float> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMap(
            Map<Integer, Float> map1, Map<Integer, Float> map2,
            Map<Integer, Float> map3, Map<Integer, Float> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMap(
            Map<Integer, Float> map1, Map<Integer, Float> map2,
            Map<Integer, Float> map3, Map<Integer, Float> map4,
            Map<Integer, Float> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMap(
            Map<Integer, Float> map1, Map<Integer, Float> map2,
            int expectedSize) {
        UpdatableQHashParallelKVIntFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMap(
            Map<Integer, Float> map1, Map<Integer, Float> map2,
            Map<Integer, Float> map3, int expectedSize) {
        UpdatableQHashParallelKVIntFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMap(
            Map<Integer, Float> map1, Map<Integer, Float> map2,
            Map<Integer, Float> map3, Map<Integer, Float> map4,
            int expectedSize) {
        UpdatableQHashParallelKVIntFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMap(
            Map<Integer, Float> map1, Map<Integer, Float> map2,
            Map<Integer, Float> map3, Map<Integer, Float> map4,
            Map<Integer, Float> map5, int expectedSize) {
        UpdatableQHashParallelKVIntFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.IntFloatConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.IntFloatConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashParallelKVIntFloatMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.IntFloatConsumer() {
             @Override
             public void accept(int k, float v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMap(
            int[] keys, float[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMap(
            int[] keys, float[] values, int expectedSize) {
        UpdatableQHashParallelKVIntFloatMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMap(
            Integer[] keys, Float[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMap(
            Integer[] keys, Float[] values, int expectedSize) {
        UpdatableQHashParallelKVIntFloatMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMap(
            Iterable<Integer> keys, Iterable<Float> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMap(
            Iterable<Integer> keys, Iterable<Float> values, int expectedSize) {
        UpdatableQHashParallelKVIntFloatMapGO map = newUpdatableMap(expectedSize);
        Iterator<Integer> keysIt = keys.iterator();
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
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMapOf(
            int k1, float v1) {
        UpdatableQHashParallelKVIntFloatMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMapOf(
            int k1, float v1, int k2, float v2) {
        UpdatableQHashParallelKVIntFloatMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMapOf(
            int k1, float v1, int k2, float v2,
            int k3, float v3) {
        UpdatableQHashParallelKVIntFloatMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMapOf(
            int k1, float v1, int k2, float v2,
            int k3, float v3, int k4, float v4) {
        UpdatableQHashParallelKVIntFloatMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVIntFloatMapGO newUpdatableMapOf(
            int k1, float v1, int k2, float v2,
            int k3, float v3, int k4, float v4,
            int k5, float v5) {
        UpdatableQHashParallelKVIntFloatMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMap(Map<Integer, Float> map1,
            Map<Integer, Float> map2, int expectedSize) {
        MutableQHashParallelKVIntFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMap(Map<Integer, Float> map1,
            Map<Integer, Float> map2, Map<Integer, Float> map3, int expectedSize) {
        MutableQHashParallelKVIntFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMap(Map<Integer, Float> map1,
            Map<Integer, Float> map2, Map<Integer, Float> map3,
            Map<Integer, Float> map4, int expectedSize) {
        MutableQHashParallelKVIntFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMap(Map<Integer, Float> map1,
            Map<Integer, Float> map2, Map<Integer, Float> map3,
            Map<Integer, Float> map4, Map<Integer, Float> map5, int expectedSize) {
        MutableQHashParallelKVIntFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMap(
            Consumer<net.openhft.koloboke.function.IntFloatConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashParallelKVIntFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMap(int[] keys,
            float[] values, int expectedSize) {
        MutableQHashParallelKVIntFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMap(
            Integer[] keys, Float[] values, int expectedSize) {
        MutableQHashParallelKVIntFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMap(Iterable<Integer> keys,
            Iterable<Float> values, int expectedSize) {
        MutableQHashParallelKVIntFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMap(
            Map<Integer, Float> map) {
        MutableQHashParallelKVIntFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMap(Map<Integer, Float> map1,
            Map<Integer, Float> map2) {
        MutableQHashParallelKVIntFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMap(Map<Integer, Float> map1,
            Map<Integer, Float> map2, Map<Integer, Float> map3) {
        MutableQHashParallelKVIntFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMap(Map<Integer, Float> map1,
            Map<Integer, Float> map2, Map<Integer, Float> map3,
            Map<Integer, Float> map4) {
        MutableQHashParallelKVIntFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMap(Map<Integer, Float> map1,
            Map<Integer, Float> map2, Map<Integer, Float> map3,
            Map<Integer, Float> map4, Map<Integer, Float> map5) {
        MutableQHashParallelKVIntFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMap(
            Consumer<net.openhft.koloboke.function.IntFloatConsumer> entriesSupplier
            ) {
        MutableQHashParallelKVIntFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMap(int[] keys,
            float[] values) {
        MutableQHashParallelKVIntFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMap(
            Integer[] keys, Float[] values) {
        MutableQHashParallelKVIntFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMap(Iterable<Integer> keys,
            Iterable<Float> values) {
        MutableQHashParallelKVIntFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMapOf(int k1, float v1) {
        MutableQHashParallelKVIntFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMapOf(int k1, float v1,
             int k2, float v2) {
        MutableQHashParallelKVIntFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMapOf(int k1, float v1,
             int k2, float v2, int k3, float v3) {
        MutableQHashParallelKVIntFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMapOf(int k1, float v1,
             int k2, float v2, int k3, float v3,
             int k4, float v4) {
        MutableQHashParallelKVIntFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newMutableMapOf(int k1, float v1,
             int k2, float v2, int k3, float v3,
             int k4, float v4, int k5, float v5) {
        MutableQHashParallelKVIntFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMap(Map<Integer, Float> map1,
            Map<Integer, Float> map2, int expectedSize) {
        ImmutableQHashParallelKVIntFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMap(Map<Integer, Float> map1,
            Map<Integer, Float> map2, Map<Integer, Float> map3, int expectedSize) {
        ImmutableQHashParallelKVIntFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMap(Map<Integer, Float> map1,
            Map<Integer, Float> map2, Map<Integer, Float> map3,
            Map<Integer, Float> map4, int expectedSize) {
        ImmutableQHashParallelKVIntFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMap(Map<Integer, Float> map1,
            Map<Integer, Float> map2, Map<Integer, Float> map3,
            Map<Integer, Float> map4, Map<Integer, Float> map5, int expectedSize) {
        ImmutableQHashParallelKVIntFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.IntFloatConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashParallelKVIntFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMap(int[] keys,
            float[] values, int expectedSize) {
        ImmutableQHashParallelKVIntFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMap(
            Integer[] keys, Float[] values, int expectedSize) {
        ImmutableQHashParallelKVIntFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMap(Iterable<Integer> keys,
            Iterable<Float> values, int expectedSize) {
        ImmutableQHashParallelKVIntFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMap(
            Map<Integer, Float> map) {
        ImmutableQHashParallelKVIntFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMap(Map<Integer, Float> map1,
            Map<Integer, Float> map2) {
        ImmutableQHashParallelKVIntFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMap(Map<Integer, Float> map1,
            Map<Integer, Float> map2, Map<Integer, Float> map3) {
        ImmutableQHashParallelKVIntFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMap(Map<Integer, Float> map1,
            Map<Integer, Float> map2, Map<Integer, Float> map3,
            Map<Integer, Float> map4) {
        ImmutableQHashParallelKVIntFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMap(Map<Integer, Float> map1,
            Map<Integer, Float> map2, Map<Integer, Float> map3,
            Map<Integer, Float> map4, Map<Integer, Float> map5) {
        ImmutableQHashParallelKVIntFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.IntFloatConsumer> entriesSupplier
            ) {
        ImmutableQHashParallelKVIntFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMap(int[] keys,
            float[] values) {
        ImmutableQHashParallelKVIntFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMap(
            Integer[] keys, Float[] values) {
        ImmutableQHashParallelKVIntFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMap(Iterable<Integer> keys,
            Iterable<Float> values) {
        ImmutableQHashParallelKVIntFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMapOf(int k1, float v1) {
        ImmutableQHashParallelKVIntFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMapOf(int k1, float v1,
             int k2, float v2) {
        ImmutableQHashParallelKVIntFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMapOf(int k1, float v1,
             int k2, float v2, int k3, float v3) {
        ImmutableQHashParallelKVIntFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMapOf(int k1, float v1,
             int k2, float v2, int k3, float v3,
             int k4, float v4) {
        ImmutableQHashParallelKVIntFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntFloatMap newImmutableMapOf(int k1, float v1,
             int k2, float v2, int k3, float v3,
             int k4, float v4, int k5, float v5) {
        ImmutableQHashParallelKVIntFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

