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
import net.openhft.koloboke.collect.map.hash.HashIntShortMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashIntShortMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashSeparateKVIntShortMapFactoryGO
        extends LHashSeparateKVIntShortMapFactorySO {

    LHashSeparateKVIntShortMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashIntShortMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    abstract HashIntShortMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    abstract HashIntShortMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    @Override
    public final HashIntShortMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashIntShortMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashIntShortMapFactory withDomain(int lower, int upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashIntShortMapFactory withKeysDomain(int lower, int upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashIntShortMapFactory withKeysDomainComplement(int lower, int upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((int) (upper + 1), (int) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashIntShortMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashIntShortMapFactory) {
            HashIntShortMapFactory factory = (HashIntShortMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashSeparateKVIntShortMapGO shrunk(
            UpdatableLHashSeparateKVIntShortMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashSeparateKVIntShortMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMap(
            Map<Integer, Short> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMap(
            Map<Integer, Short> map1, Map<Integer, Short> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMap(
            Map<Integer, Short> map1, Map<Integer, Short> map2,
            Map<Integer, Short> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMap(
            Map<Integer, Short> map1, Map<Integer, Short> map2,
            Map<Integer, Short> map3, Map<Integer, Short> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMap(
            Map<Integer, Short> map1, Map<Integer, Short> map2,
            Map<Integer, Short> map3, Map<Integer, Short> map4,
            Map<Integer, Short> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMap(
            Map<Integer, Short> map1, Map<Integer, Short> map2,
            int expectedSize) {
        UpdatableLHashSeparateKVIntShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMap(
            Map<Integer, Short> map1, Map<Integer, Short> map2,
            Map<Integer, Short> map3, int expectedSize) {
        UpdatableLHashSeparateKVIntShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMap(
            Map<Integer, Short> map1, Map<Integer, Short> map2,
            Map<Integer, Short> map3, Map<Integer, Short> map4,
            int expectedSize) {
        UpdatableLHashSeparateKVIntShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMap(
            Map<Integer, Short> map1, Map<Integer, Short> map2,
            Map<Integer, Short> map3, Map<Integer, Short> map4,
            Map<Integer, Short> map5, int expectedSize) {
        UpdatableLHashSeparateKVIntShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.IntShortConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.IntShortConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashSeparateKVIntShortMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.IntShortConsumer() {
             @Override
             public void accept(int k, short v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMap(
            int[] keys, short[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMap(
            int[] keys, short[] values, int expectedSize) {
        UpdatableLHashSeparateKVIntShortMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMap(
            Integer[] keys, Short[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMap(
            Integer[] keys, Short[] values, int expectedSize) {
        UpdatableLHashSeparateKVIntShortMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMap(
            Iterable<Integer> keys, Iterable<Short> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMap(
            Iterable<Integer> keys, Iterable<Short> values, int expectedSize) {
        UpdatableLHashSeparateKVIntShortMapGO map = newUpdatableMap(expectedSize);
        Iterator<Integer> keysIt = keys.iterator();
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
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMapOf(
            int k1, short v1) {
        UpdatableLHashSeparateKVIntShortMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMapOf(
            int k1, short v1, int k2, short v2) {
        UpdatableLHashSeparateKVIntShortMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMapOf(
            int k1, short v1, int k2, short v2,
            int k3, short v3) {
        UpdatableLHashSeparateKVIntShortMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMapOf(
            int k1, short v1, int k2, short v2,
            int k3, short v3, int k4, short v4) {
        UpdatableLHashSeparateKVIntShortMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntShortMapGO newUpdatableMapOf(
            int k1, short v1, int k2, short v2,
            int k3, short v3, int k4, short v4,
            int k5, short v5) {
        UpdatableLHashSeparateKVIntShortMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashIntShortMap newMutableMap(Map<Integer, Short> map1,
            Map<Integer, Short> map2, int expectedSize) {
        MutableLHashSeparateKVIntShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newMutableMap(Map<Integer, Short> map1,
            Map<Integer, Short> map2, Map<Integer, Short> map3, int expectedSize) {
        MutableLHashSeparateKVIntShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newMutableMap(Map<Integer, Short> map1,
            Map<Integer, Short> map2, Map<Integer, Short> map3,
            Map<Integer, Short> map4, int expectedSize) {
        MutableLHashSeparateKVIntShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newMutableMap(Map<Integer, Short> map1,
            Map<Integer, Short> map2, Map<Integer, Short> map3,
            Map<Integer, Short> map4, Map<Integer, Short> map5, int expectedSize) {
        MutableLHashSeparateKVIntShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntShortMap newMutableMap(
            Consumer<net.openhft.koloboke.function.IntShortConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashSeparateKVIntShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newMutableMap(int[] keys,
            short[] values, int expectedSize) {
        MutableLHashSeparateKVIntShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newMutableMap(
            Integer[] keys, Short[] values, int expectedSize) {
        MutableLHashSeparateKVIntShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newMutableMap(Iterable<Integer> keys,
            Iterable<Short> values, int expectedSize) {
        MutableLHashSeparateKVIntShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashIntShortMap newMutableMap(
            Map<Integer, Short> map) {
        MutableLHashSeparateKVIntShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newMutableMap(Map<Integer, Short> map1,
            Map<Integer, Short> map2) {
        MutableLHashSeparateKVIntShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newMutableMap(Map<Integer, Short> map1,
            Map<Integer, Short> map2, Map<Integer, Short> map3) {
        MutableLHashSeparateKVIntShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newMutableMap(Map<Integer, Short> map1,
            Map<Integer, Short> map2, Map<Integer, Short> map3,
            Map<Integer, Short> map4) {
        MutableLHashSeparateKVIntShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newMutableMap(Map<Integer, Short> map1,
            Map<Integer, Short> map2, Map<Integer, Short> map3,
            Map<Integer, Short> map4, Map<Integer, Short> map5) {
        MutableLHashSeparateKVIntShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntShortMap newMutableMap(
            Consumer<net.openhft.koloboke.function.IntShortConsumer> entriesSupplier
            ) {
        MutableLHashSeparateKVIntShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newMutableMap(int[] keys,
            short[] values) {
        MutableLHashSeparateKVIntShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newMutableMap(
            Integer[] keys, Short[] values) {
        MutableLHashSeparateKVIntShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newMutableMap(Iterable<Integer> keys,
            Iterable<Short> values) {
        MutableLHashSeparateKVIntShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashIntShortMap newMutableMapOf(int k1, short v1) {
        MutableLHashSeparateKVIntShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newMutableMapOf(int k1, short v1,
             int k2, short v2) {
        MutableLHashSeparateKVIntShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newMutableMapOf(int k1, short v1,
             int k2, short v2, int k3, short v3) {
        MutableLHashSeparateKVIntShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newMutableMapOf(int k1, short v1,
             int k2, short v2, int k3, short v3,
             int k4, short v4) {
        MutableLHashSeparateKVIntShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newMutableMapOf(int k1, short v1,
             int k2, short v2, int k3, short v3,
             int k4, short v4, int k5, short v5) {
        MutableLHashSeparateKVIntShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMap(Map<Integer, Short> map1,
            Map<Integer, Short> map2, int expectedSize) {
        ImmutableLHashSeparateKVIntShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMap(Map<Integer, Short> map1,
            Map<Integer, Short> map2, Map<Integer, Short> map3, int expectedSize) {
        ImmutableLHashSeparateKVIntShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMap(Map<Integer, Short> map1,
            Map<Integer, Short> map2, Map<Integer, Short> map3,
            Map<Integer, Short> map4, int expectedSize) {
        ImmutableLHashSeparateKVIntShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMap(Map<Integer, Short> map1,
            Map<Integer, Short> map2, Map<Integer, Short> map3,
            Map<Integer, Short> map4, Map<Integer, Short> map5, int expectedSize) {
        ImmutableLHashSeparateKVIntShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.IntShortConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashSeparateKVIntShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMap(int[] keys,
            short[] values, int expectedSize) {
        ImmutableLHashSeparateKVIntShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMap(
            Integer[] keys, Short[] values, int expectedSize) {
        ImmutableLHashSeparateKVIntShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMap(Iterable<Integer> keys,
            Iterable<Short> values, int expectedSize) {
        ImmutableLHashSeparateKVIntShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMap(
            Map<Integer, Short> map) {
        ImmutableLHashSeparateKVIntShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMap(Map<Integer, Short> map1,
            Map<Integer, Short> map2) {
        ImmutableLHashSeparateKVIntShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMap(Map<Integer, Short> map1,
            Map<Integer, Short> map2, Map<Integer, Short> map3) {
        ImmutableLHashSeparateKVIntShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMap(Map<Integer, Short> map1,
            Map<Integer, Short> map2, Map<Integer, Short> map3,
            Map<Integer, Short> map4) {
        ImmutableLHashSeparateKVIntShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMap(Map<Integer, Short> map1,
            Map<Integer, Short> map2, Map<Integer, Short> map3,
            Map<Integer, Short> map4, Map<Integer, Short> map5) {
        ImmutableLHashSeparateKVIntShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.IntShortConsumer> entriesSupplier
            ) {
        ImmutableLHashSeparateKVIntShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMap(int[] keys,
            short[] values) {
        ImmutableLHashSeparateKVIntShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMap(
            Integer[] keys, Short[] values) {
        ImmutableLHashSeparateKVIntShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMap(Iterable<Integer> keys,
            Iterable<Short> values) {
        ImmutableLHashSeparateKVIntShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMapOf(int k1, short v1) {
        ImmutableLHashSeparateKVIntShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMapOf(int k1, short v1,
             int k2, short v2) {
        ImmutableLHashSeparateKVIntShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMapOf(int k1, short v1,
             int k2, short v2, int k3, short v3) {
        ImmutableLHashSeparateKVIntShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMapOf(int k1, short v1,
             int k2, short v2, int k3, short v3,
             int k4, short v4) {
        ImmutableLHashSeparateKVIntShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntShortMap newImmutableMapOf(int k1, short v1,
             int k2, short v2, int k3, short v3,
             int k4, short v4, int k5, short v5) {
        ImmutableLHashSeparateKVIntShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}
