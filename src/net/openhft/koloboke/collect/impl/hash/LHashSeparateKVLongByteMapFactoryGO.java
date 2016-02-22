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
import net.openhft.koloboke.collect.map.hash.HashLongByteMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashLongByteMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashSeparateKVLongByteMapFactoryGO
        extends LHashSeparateKVLongByteMapFactorySO {

    LHashSeparateKVLongByteMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashLongByteMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper);

    abstract HashLongByteMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper);

    abstract HashLongByteMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper);

    @Override
    public final HashLongByteMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashLongByteMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashLongByteMapFactory withDomain(long lower, long upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashLongByteMapFactory withKeysDomain(long lower, long upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashLongByteMapFactory withKeysDomainComplement(long lower, long upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((long) (upper + 1), (long) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashLongByteMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashLongByteMapFactory) {
            HashLongByteMapFactory factory = (HashLongByteMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashSeparateKVLongByteMapGO shrunk(
            UpdatableLHashSeparateKVLongByteMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashSeparateKVLongByteMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMap(
            Map<Long, Byte> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMap(
            Map<Long, Byte> map1, Map<Long, Byte> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMap(
            Map<Long, Byte> map1, Map<Long, Byte> map2,
            Map<Long, Byte> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMap(
            Map<Long, Byte> map1, Map<Long, Byte> map2,
            Map<Long, Byte> map3, Map<Long, Byte> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMap(
            Map<Long, Byte> map1, Map<Long, Byte> map2,
            Map<Long, Byte> map3, Map<Long, Byte> map4,
            Map<Long, Byte> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMap(
            Map<Long, Byte> map1, Map<Long, Byte> map2,
            int expectedSize) {
        UpdatableLHashSeparateKVLongByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMap(
            Map<Long, Byte> map1, Map<Long, Byte> map2,
            Map<Long, Byte> map3, int expectedSize) {
        UpdatableLHashSeparateKVLongByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMap(
            Map<Long, Byte> map1, Map<Long, Byte> map2,
            Map<Long, Byte> map3, Map<Long, Byte> map4,
            int expectedSize) {
        UpdatableLHashSeparateKVLongByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMap(
            Map<Long, Byte> map1, Map<Long, Byte> map2,
            Map<Long, Byte> map3, Map<Long, Byte> map4,
            Map<Long, Byte> map5, int expectedSize) {
        UpdatableLHashSeparateKVLongByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.LongByteConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.LongByteConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashSeparateKVLongByteMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.LongByteConsumer() {
             @Override
             public void accept(long k, byte v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMap(
            long[] keys, byte[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMap(
            long[] keys, byte[] values, int expectedSize) {
        UpdatableLHashSeparateKVLongByteMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMap(
            Long[] keys, Byte[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMap(
            Long[] keys, Byte[] values, int expectedSize) {
        UpdatableLHashSeparateKVLongByteMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMap(
            Iterable<Long> keys, Iterable<Byte> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMap(
            Iterable<Long> keys, Iterable<Byte> values, int expectedSize) {
        UpdatableLHashSeparateKVLongByteMapGO map = newUpdatableMap(expectedSize);
        Iterator<Long> keysIt = keys.iterator();
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
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMapOf(
            long k1, byte v1) {
        UpdatableLHashSeparateKVLongByteMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMapOf(
            long k1, byte v1, long k2, byte v2) {
        UpdatableLHashSeparateKVLongByteMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMapOf(
            long k1, byte v1, long k2, byte v2,
            long k3, byte v3) {
        UpdatableLHashSeparateKVLongByteMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMapOf(
            long k1, byte v1, long k2, byte v2,
            long k3, byte v3, long k4, byte v4) {
        UpdatableLHashSeparateKVLongByteMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongByteMapGO newUpdatableMapOf(
            long k1, byte v1, long k2, byte v2,
            long k3, byte v3, long k4, byte v4,
            long k5, byte v5) {
        UpdatableLHashSeparateKVLongByteMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashLongByteMap newMutableMap(Map<Long, Byte> map1,
            Map<Long, Byte> map2, int expectedSize) {
        MutableLHashSeparateKVLongByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newMutableMap(Map<Long, Byte> map1,
            Map<Long, Byte> map2, Map<Long, Byte> map3, int expectedSize) {
        MutableLHashSeparateKVLongByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newMutableMap(Map<Long, Byte> map1,
            Map<Long, Byte> map2, Map<Long, Byte> map3,
            Map<Long, Byte> map4, int expectedSize) {
        MutableLHashSeparateKVLongByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newMutableMap(Map<Long, Byte> map1,
            Map<Long, Byte> map2, Map<Long, Byte> map3,
            Map<Long, Byte> map4, Map<Long, Byte> map5, int expectedSize) {
        MutableLHashSeparateKVLongByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongByteMap newMutableMap(
            Consumer<net.openhft.koloboke.function.LongByteConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashSeparateKVLongByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newMutableMap(long[] keys,
            byte[] values, int expectedSize) {
        MutableLHashSeparateKVLongByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newMutableMap(
            Long[] keys, Byte[] values, int expectedSize) {
        MutableLHashSeparateKVLongByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newMutableMap(Iterable<Long> keys,
            Iterable<Byte> values, int expectedSize) {
        MutableLHashSeparateKVLongByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashLongByteMap newMutableMap(
            Map<Long, Byte> map) {
        MutableLHashSeparateKVLongByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newMutableMap(Map<Long, Byte> map1,
            Map<Long, Byte> map2) {
        MutableLHashSeparateKVLongByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newMutableMap(Map<Long, Byte> map1,
            Map<Long, Byte> map2, Map<Long, Byte> map3) {
        MutableLHashSeparateKVLongByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newMutableMap(Map<Long, Byte> map1,
            Map<Long, Byte> map2, Map<Long, Byte> map3,
            Map<Long, Byte> map4) {
        MutableLHashSeparateKVLongByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newMutableMap(Map<Long, Byte> map1,
            Map<Long, Byte> map2, Map<Long, Byte> map3,
            Map<Long, Byte> map4, Map<Long, Byte> map5) {
        MutableLHashSeparateKVLongByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongByteMap newMutableMap(
            Consumer<net.openhft.koloboke.function.LongByteConsumer> entriesSupplier
            ) {
        MutableLHashSeparateKVLongByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newMutableMap(long[] keys,
            byte[] values) {
        MutableLHashSeparateKVLongByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newMutableMap(
            Long[] keys, Byte[] values) {
        MutableLHashSeparateKVLongByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newMutableMap(Iterable<Long> keys,
            Iterable<Byte> values) {
        MutableLHashSeparateKVLongByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashLongByteMap newMutableMapOf(long k1, byte v1) {
        MutableLHashSeparateKVLongByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newMutableMapOf(long k1, byte v1,
             long k2, byte v2) {
        MutableLHashSeparateKVLongByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newMutableMapOf(long k1, byte v1,
             long k2, byte v2, long k3, byte v3) {
        MutableLHashSeparateKVLongByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newMutableMapOf(long k1, byte v1,
             long k2, byte v2, long k3, byte v3,
             long k4, byte v4) {
        MutableLHashSeparateKVLongByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newMutableMapOf(long k1, byte v1,
             long k2, byte v2, long k3, byte v3,
             long k4, byte v4, long k5, byte v5) {
        MutableLHashSeparateKVLongByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMap(Map<Long, Byte> map1,
            Map<Long, Byte> map2, int expectedSize) {
        ImmutableLHashSeparateKVLongByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMap(Map<Long, Byte> map1,
            Map<Long, Byte> map2, Map<Long, Byte> map3, int expectedSize) {
        ImmutableLHashSeparateKVLongByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMap(Map<Long, Byte> map1,
            Map<Long, Byte> map2, Map<Long, Byte> map3,
            Map<Long, Byte> map4, int expectedSize) {
        ImmutableLHashSeparateKVLongByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMap(Map<Long, Byte> map1,
            Map<Long, Byte> map2, Map<Long, Byte> map3,
            Map<Long, Byte> map4, Map<Long, Byte> map5, int expectedSize) {
        ImmutableLHashSeparateKVLongByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.LongByteConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashSeparateKVLongByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMap(long[] keys,
            byte[] values, int expectedSize) {
        ImmutableLHashSeparateKVLongByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMap(
            Long[] keys, Byte[] values, int expectedSize) {
        ImmutableLHashSeparateKVLongByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMap(Iterable<Long> keys,
            Iterable<Byte> values, int expectedSize) {
        ImmutableLHashSeparateKVLongByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMap(
            Map<Long, Byte> map) {
        ImmutableLHashSeparateKVLongByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMap(Map<Long, Byte> map1,
            Map<Long, Byte> map2) {
        ImmutableLHashSeparateKVLongByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMap(Map<Long, Byte> map1,
            Map<Long, Byte> map2, Map<Long, Byte> map3) {
        ImmutableLHashSeparateKVLongByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMap(Map<Long, Byte> map1,
            Map<Long, Byte> map2, Map<Long, Byte> map3,
            Map<Long, Byte> map4) {
        ImmutableLHashSeparateKVLongByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMap(Map<Long, Byte> map1,
            Map<Long, Byte> map2, Map<Long, Byte> map3,
            Map<Long, Byte> map4, Map<Long, Byte> map5) {
        ImmutableLHashSeparateKVLongByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.LongByteConsumer> entriesSupplier
            ) {
        ImmutableLHashSeparateKVLongByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMap(long[] keys,
            byte[] values) {
        ImmutableLHashSeparateKVLongByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMap(
            Long[] keys, Byte[] values) {
        ImmutableLHashSeparateKVLongByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMap(Iterable<Long> keys,
            Iterable<Byte> values) {
        ImmutableLHashSeparateKVLongByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMapOf(long k1, byte v1) {
        ImmutableLHashSeparateKVLongByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMapOf(long k1, byte v1,
             long k2, byte v2) {
        ImmutableLHashSeparateKVLongByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMapOf(long k1, byte v1,
             long k2, byte v2, long k3, byte v3) {
        ImmutableLHashSeparateKVLongByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMapOf(long k1, byte v1,
             long k2, byte v2, long k3, byte v3,
             long k4, byte v4) {
        ImmutableLHashSeparateKVLongByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongByteMap newImmutableMapOf(long k1, byte v1,
             long k2, byte v2, long k3, byte v3,
             long k4, byte v4, long k5, byte v5) {
        ImmutableLHashSeparateKVLongByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

