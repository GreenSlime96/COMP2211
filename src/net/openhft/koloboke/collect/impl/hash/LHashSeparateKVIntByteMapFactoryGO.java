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
import net.openhft.koloboke.collect.map.hash.HashIntByteMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashIntByteMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashSeparateKVIntByteMapFactoryGO
        extends LHashSeparateKVIntByteMapFactorySO {

    LHashSeparateKVIntByteMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashIntByteMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    abstract HashIntByteMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    abstract HashIntByteMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    @Override
    public final HashIntByteMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashIntByteMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashIntByteMapFactory withDomain(int lower, int upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashIntByteMapFactory withKeysDomain(int lower, int upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashIntByteMapFactory withKeysDomainComplement(int lower, int upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((int) (upper + 1), (int) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashIntByteMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashIntByteMapFactory) {
            HashIntByteMapFactory factory = (HashIntByteMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashSeparateKVIntByteMapGO shrunk(
            UpdatableLHashSeparateKVIntByteMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashSeparateKVIntByteMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMap(
            Map<Integer, Byte> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMap(
            Map<Integer, Byte> map1, Map<Integer, Byte> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMap(
            Map<Integer, Byte> map1, Map<Integer, Byte> map2,
            Map<Integer, Byte> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMap(
            Map<Integer, Byte> map1, Map<Integer, Byte> map2,
            Map<Integer, Byte> map3, Map<Integer, Byte> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMap(
            Map<Integer, Byte> map1, Map<Integer, Byte> map2,
            Map<Integer, Byte> map3, Map<Integer, Byte> map4,
            Map<Integer, Byte> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMap(
            Map<Integer, Byte> map1, Map<Integer, Byte> map2,
            int expectedSize) {
        UpdatableLHashSeparateKVIntByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMap(
            Map<Integer, Byte> map1, Map<Integer, Byte> map2,
            Map<Integer, Byte> map3, int expectedSize) {
        UpdatableLHashSeparateKVIntByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMap(
            Map<Integer, Byte> map1, Map<Integer, Byte> map2,
            Map<Integer, Byte> map3, Map<Integer, Byte> map4,
            int expectedSize) {
        UpdatableLHashSeparateKVIntByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMap(
            Map<Integer, Byte> map1, Map<Integer, Byte> map2,
            Map<Integer, Byte> map3, Map<Integer, Byte> map4,
            Map<Integer, Byte> map5, int expectedSize) {
        UpdatableLHashSeparateKVIntByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.IntByteConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.IntByteConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashSeparateKVIntByteMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.IntByteConsumer() {
             @Override
             public void accept(int k, byte v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMap(
            int[] keys, byte[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMap(
            int[] keys, byte[] values, int expectedSize) {
        UpdatableLHashSeparateKVIntByteMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMap(
            Integer[] keys, Byte[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMap(
            Integer[] keys, Byte[] values, int expectedSize) {
        UpdatableLHashSeparateKVIntByteMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMap(
            Iterable<Integer> keys, Iterable<Byte> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMap(
            Iterable<Integer> keys, Iterable<Byte> values, int expectedSize) {
        UpdatableLHashSeparateKVIntByteMapGO map = newUpdatableMap(expectedSize);
        Iterator<Integer> keysIt = keys.iterator();
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
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMapOf(
            int k1, byte v1) {
        UpdatableLHashSeparateKVIntByteMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMapOf(
            int k1, byte v1, int k2, byte v2) {
        UpdatableLHashSeparateKVIntByteMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMapOf(
            int k1, byte v1, int k2, byte v2,
            int k3, byte v3) {
        UpdatableLHashSeparateKVIntByteMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMapOf(
            int k1, byte v1, int k2, byte v2,
            int k3, byte v3, int k4, byte v4) {
        UpdatableLHashSeparateKVIntByteMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntByteMapGO newUpdatableMapOf(
            int k1, byte v1, int k2, byte v2,
            int k3, byte v3, int k4, byte v4,
            int k5, byte v5) {
        UpdatableLHashSeparateKVIntByteMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashIntByteMap newMutableMap(Map<Integer, Byte> map1,
            Map<Integer, Byte> map2, int expectedSize) {
        MutableLHashSeparateKVIntByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newMutableMap(Map<Integer, Byte> map1,
            Map<Integer, Byte> map2, Map<Integer, Byte> map3, int expectedSize) {
        MutableLHashSeparateKVIntByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newMutableMap(Map<Integer, Byte> map1,
            Map<Integer, Byte> map2, Map<Integer, Byte> map3,
            Map<Integer, Byte> map4, int expectedSize) {
        MutableLHashSeparateKVIntByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newMutableMap(Map<Integer, Byte> map1,
            Map<Integer, Byte> map2, Map<Integer, Byte> map3,
            Map<Integer, Byte> map4, Map<Integer, Byte> map5, int expectedSize) {
        MutableLHashSeparateKVIntByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntByteMap newMutableMap(
            Consumer<net.openhft.koloboke.function.IntByteConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashSeparateKVIntByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newMutableMap(int[] keys,
            byte[] values, int expectedSize) {
        MutableLHashSeparateKVIntByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newMutableMap(
            Integer[] keys, Byte[] values, int expectedSize) {
        MutableLHashSeparateKVIntByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newMutableMap(Iterable<Integer> keys,
            Iterable<Byte> values, int expectedSize) {
        MutableLHashSeparateKVIntByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashIntByteMap newMutableMap(
            Map<Integer, Byte> map) {
        MutableLHashSeparateKVIntByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newMutableMap(Map<Integer, Byte> map1,
            Map<Integer, Byte> map2) {
        MutableLHashSeparateKVIntByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newMutableMap(Map<Integer, Byte> map1,
            Map<Integer, Byte> map2, Map<Integer, Byte> map3) {
        MutableLHashSeparateKVIntByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newMutableMap(Map<Integer, Byte> map1,
            Map<Integer, Byte> map2, Map<Integer, Byte> map3,
            Map<Integer, Byte> map4) {
        MutableLHashSeparateKVIntByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newMutableMap(Map<Integer, Byte> map1,
            Map<Integer, Byte> map2, Map<Integer, Byte> map3,
            Map<Integer, Byte> map4, Map<Integer, Byte> map5) {
        MutableLHashSeparateKVIntByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntByteMap newMutableMap(
            Consumer<net.openhft.koloboke.function.IntByteConsumer> entriesSupplier
            ) {
        MutableLHashSeparateKVIntByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newMutableMap(int[] keys,
            byte[] values) {
        MutableLHashSeparateKVIntByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newMutableMap(
            Integer[] keys, Byte[] values) {
        MutableLHashSeparateKVIntByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newMutableMap(Iterable<Integer> keys,
            Iterable<Byte> values) {
        MutableLHashSeparateKVIntByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashIntByteMap newMutableMapOf(int k1, byte v1) {
        MutableLHashSeparateKVIntByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newMutableMapOf(int k1, byte v1,
             int k2, byte v2) {
        MutableLHashSeparateKVIntByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newMutableMapOf(int k1, byte v1,
             int k2, byte v2, int k3, byte v3) {
        MutableLHashSeparateKVIntByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newMutableMapOf(int k1, byte v1,
             int k2, byte v2, int k3, byte v3,
             int k4, byte v4) {
        MutableLHashSeparateKVIntByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newMutableMapOf(int k1, byte v1,
             int k2, byte v2, int k3, byte v3,
             int k4, byte v4, int k5, byte v5) {
        MutableLHashSeparateKVIntByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMap(Map<Integer, Byte> map1,
            Map<Integer, Byte> map2, int expectedSize) {
        ImmutableLHashSeparateKVIntByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMap(Map<Integer, Byte> map1,
            Map<Integer, Byte> map2, Map<Integer, Byte> map3, int expectedSize) {
        ImmutableLHashSeparateKVIntByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMap(Map<Integer, Byte> map1,
            Map<Integer, Byte> map2, Map<Integer, Byte> map3,
            Map<Integer, Byte> map4, int expectedSize) {
        ImmutableLHashSeparateKVIntByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMap(Map<Integer, Byte> map1,
            Map<Integer, Byte> map2, Map<Integer, Byte> map3,
            Map<Integer, Byte> map4, Map<Integer, Byte> map5, int expectedSize) {
        ImmutableLHashSeparateKVIntByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.IntByteConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashSeparateKVIntByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMap(int[] keys,
            byte[] values, int expectedSize) {
        ImmutableLHashSeparateKVIntByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMap(
            Integer[] keys, Byte[] values, int expectedSize) {
        ImmutableLHashSeparateKVIntByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMap(Iterable<Integer> keys,
            Iterable<Byte> values, int expectedSize) {
        ImmutableLHashSeparateKVIntByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMap(
            Map<Integer, Byte> map) {
        ImmutableLHashSeparateKVIntByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMap(Map<Integer, Byte> map1,
            Map<Integer, Byte> map2) {
        ImmutableLHashSeparateKVIntByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMap(Map<Integer, Byte> map1,
            Map<Integer, Byte> map2, Map<Integer, Byte> map3) {
        ImmutableLHashSeparateKVIntByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMap(Map<Integer, Byte> map1,
            Map<Integer, Byte> map2, Map<Integer, Byte> map3,
            Map<Integer, Byte> map4) {
        ImmutableLHashSeparateKVIntByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMap(Map<Integer, Byte> map1,
            Map<Integer, Byte> map2, Map<Integer, Byte> map3,
            Map<Integer, Byte> map4, Map<Integer, Byte> map5) {
        ImmutableLHashSeparateKVIntByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.IntByteConsumer> entriesSupplier
            ) {
        ImmutableLHashSeparateKVIntByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMap(int[] keys,
            byte[] values) {
        ImmutableLHashSeparateKVIntByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMap(
            Integer[] keys, Byte[] values) {
        ImmutableLHashSeparateKVIntByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMap(Iterable<Integer> keys,
            Iterable<Byte> values) {
        ImmutableLHashSeparateKVIntByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMapOf(int k1, byte v1) {
        ImmutableLHashSeparateKVIntByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMapOf(int k1, byte v1,
             int k2, byte v2) {
        ImmutableLHashSeparateKVIntByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMapOf(int k1, byte v1,
             int k2, byte v2, int k3, byte v3) {
        ImmutableLHashSeparateKVIntByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMapOf(int k1, byte v1,
             int k2, byte v2, int k3, byte v3,
             int k4, byte v4) {
        ImmutableLHashSeparateKVIntByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntByteMap newImmutableMapOf(int k1, byte v1,
             int k2, byte v2, int k3, byte v3,
             int k4, byte v4, int k5, byte v5) {
        ImmutableLHashSeparateKVIntByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

