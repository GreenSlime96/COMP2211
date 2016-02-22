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
import net.openhft.koloboke.collect.map.hash.HashLongShortMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashLongShortMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashSeparateKVLongShortMapFactoryGO
        extends LHashSeparateKVLongShortMapFactorySO {

    LHashSeparateKVLongShortMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashLongShortMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper);

    abstract HashLongShortMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper);

    abstract HashLongShortMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper);

    @Override
    public final HashLongShortMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashLongShortMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashLongShortMapFactory withDomain(long lower, long upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashLongShortMapFactory withKeysDomain(long lower, long upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashLongShortMapFactory withKeysDomainComplement(long lower, long upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((long) (upper + 1), (long) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashLongShortMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashLongShortMapFactory) {
            HashLongShortMapFactory factory = (HashLongShortMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashSeparateKVLongShortMapGO shrunk(
            UpdatableLHashSeparateKVLongShortMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashSeparateKVLongShortMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMap(
            Map<Long, Short> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMap(
            Map<Long, Short> map1, Map<Long, Short> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMap(
            Map<Long, Short> map1, Map<Long, Short> map2,
            Map<Long, Short> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMap(
            Map<Long, Short> map1, Map<Long, Short> map2,
            Map<Long, Short> map3, Map<Long, Short> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMap(
            Map<Long, Short> map1, Map<Long, Short> map2,
            Map<Long, Short> map3, Map<Long, Short> map4,
            Map<Long, Short> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMap(
            Map<Long, Short> map1, Map<Long, Short> map2,
            int expectedSize) {
        UpdatableLHashSeparateKVLongShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMap(
            Map<Long, Short> map1, Map<Long, Short> map2,
            Map<Long, Short> map3, int expectedSize) {
        UpdatableLHashSeparateKVLongShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMap(
            Map<Long, Short> map1, Map<Long, Short> map2,
            Map<Long, Short> map3, Map<Long, Short> map4,
            int expectedSize) {
        UpdatableLHashSeparateKVLongShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMap(
            Map<Long, Short> map1, Map<Long, Short> map2,
            Map<Long, Short> map3, Map<Long, Short> map4,
            Map<Long, Short> map5, int expectedSize) {
        UpdatableLHashSeparateKVLongShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.LongShortConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.LongShortConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashSeparateKVLongShortMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.LongShortConsumer() {
             @Override
             public void accept(long k, short v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMap(
            long[] keys, short[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMap(
            long[] keys, short[] values, int expectedSize) {
        UpdatableLHashSeparateKVLongShortMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMap(
            Long[] keys, Short[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMap(
            Long[] keys, Short[] values, int expectedSize) {
        UpdatableLHashSeparateKVLongShortMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMap(
            Iterable<Long> keys, Iterable<Short> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMap(
            Iterable<Long> keys, Iterable<Short> values, int expectedSize) {
        UpdatableLHashSeparateKVLongShortMapGO map = newUpdatableMap(expectedSize);
        Iterator<Long> keysIt = keys.iterator();
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
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMapOf(
            long k1, short v1) {
        UpdatableLHashSeparateKVLongShortMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMapOf(
            long k1, short v1, long k2, short v2) {
        UpdatableLHashSeparateKVLongShortMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMapOf(
            long k1, short v1, long k2, short v2,
            long k3, short v3) {
        UpdatableLHashSeparateKVLongShortMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMapOf(
            long k1, short v1, long k2, short v2,
            long k3, short v3, long k4, short v4) {
        UpdatableLHashSeparateKVLongShortMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongShortMapGO newUpdatableMapOf(
            long k1, short v1, long k2, short v2,
            long k3, short v3, long k4, short v4,
            long k5, short v5) {
        UpdatableLHashSeparateKVLongShortMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashLongShortMap newMutableMap(Map<Long, Short> map1,
            Map<Long, Short> map2, int expectedSize) {
        MutableLHashSeparateKVLongShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newMutableMap(Map<Long, Short> map1,
            Map<Long, Short> map2, Map<Long, Short> map3, int expectedSize) {
        MutableLHashSeparateKVLongShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newMutableMap(Map<Long, Short> map1,
            Map<Long, Short> map2, Map<Long, Short> map3,
            Map<Long, Short> map4, int expectedSize) {
        MutableLHashSeparateKVLongShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newMutableMap(Map<Long, Short> map1,
            Map<Long, Short> map2, Map<Long, Short> map3,
            Map<Long, Short> map4, Map<Long, Short> map5, int expectedSize) {
        MutableLHashSeparateKVLongShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongShortMap newMutableMap(
            Consumer<net.openhft.koloboke.function.LongShortConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashSeparateKVLongShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newMutableMap(long[] keys,
            short[] values, int expectedSize) {
        MutableLHashSeparateKVLongShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newMutableMap(
            Long[] keys, Short[] values, int expectedSize) {
        MutableLHashSeparateKVLongShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newMutableMap(Iterable<Long> keys,
            Iterable<Short> values, int expectedSize) {
        MutableLHashSeparateKVLongShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashLongShortMap newMutableMap(
            Map<Long, Short> map) {
        MutableLHashSeparateKVLongShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newMutableMap(Map<Long, Short> map1,
            Map<Long, Short> map2) {
        MutableLHashSeparateKVLongShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newMutableMap(Map<Long, Short> map1,
            Map<Long, Short> map2, Map<Long, Short> map3) {
        MutableLHashSeparateKVLongShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newMutableMap(Map<Long, Short> map1,
            Map<Long, Short> map2, Map<Long, Short> map3,
            Map<Long, Short> map4) {
        MutableLHashSeparateKVLongShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newMutableMap(Map<Long, Short> map1,
            Map<Long, Short> map2, Map<Long, Short> map3,
            Map<Long, Short> map4, Map<Long, Short> map5) {
        MutableLHashSeparateKVLongShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongShortMap newMutableMap(
            Consumer<net.openhft.koloboke.function.LongShortConsumer> entriesSupplier
            ) {
        MutableLHashSeparateKVLongShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newMutableMap(long[] keys,
            short[] values) {
        MutableLHashSeparateKVLongShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newMutableMap(
            Long[] keys, Short[] values) {
        MutableLHashSeparateKVLongShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newMutableMap(Iterable<Long> keys,
            Iterable<Short> values) {
        MutableLHashSeparateKVLongShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashLongShortMap newMutableMapOf(long k1, short v1) {
        MutableLHashSeparateKVLongShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newMutableMapOf(long k1, short v1,
             long k2, short v2) {
        MutableLHashSeparateKVLongShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newMutableMapOf(long k1, short v1,
             long k2, short v2, long k3, short v3) {
        MutableLHashSeparateKVLongShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newMutableMapOf(long k1, short v1,
             long k2, short v2, long k3, short v3,
             long k4, short v4) {
        MutableLHashSeparateKVLongShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newMutableMapOf(long k1, short v1,
             long k2, short v2, long k3, short v3,
             long k4, short v4, long k5, short v5) {
        MutableLHashSeparateKVLongShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMap(Map<Long, Short> map1,
            Map<Long, Short> map2, int expectedSize) {
        ImmutableLHashSeparateKVLongShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMap(Map<Long, Short> map1,
            Map<Long, Short> map2, Map<Long, Short> map3, int expectedSize) {
        ImmutableLHashSeparateKVLongShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMap(Map<Long, Short> map1,
            Map<Long, Short> map2, Map<Long, Short> map3,
            Map<Long, Short> map4, int expectedSize) {
        ImmutableLHashSeparateKVLongShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMap(Map<Long, Short> map1,
            Map<Long, Short> map2, Map<Long, Short> map3,
            Map<Long, Short> map4, Map<Long, Short> map5, int expectedSize) {
        ImmutableLHashSeparateKVLongShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.LongShortConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashSeparateKVLongShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMap(long[] keys,
            short[] values, int expectedSize) {
        ImmutableLHashSeparateKVLongShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMap(
            Long[] keys, Short[] values, int expectedSize) {
        ImmutableLHashSeparateKVLongShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMap(Iterable<Long> keys,
            Iterable<Short> values, int expectedSize) {
        ImmutableLHashSeparateKVLongShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMap(
            Map<Long, Short> map) {
        ImmutableLHashSeparateKVLongShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMap(Map<Long, Short> map1,
            Map<Long, Short> map2) {
        ImmutableLHashSeparateKVLongShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMap(Map<Long, Short> map1,
            Map<Long, Short> map2, Map<Long, Short> map3) {
        ImmutableLHashSeparateKVLongShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMap(Map<Long, Short> map1,
            Map<Long, Short> map2, Map<Long, Short> map3,
            Map<Long, Short> map4) {
        ImmutableLHashSeparateKVLongShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMap(Map<Long, Short> map1,
            Map<Long, Short> map2, Map<Long, Short> map3,
            Map<Long, Short> map4, Map<Long, Short> map5) {
        ImmutableLHashSeparateKVLongShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.LongShortConsumer> entriesSupplier
            ) {
        ImmutableLHashSeparateKVLongShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMap(long[] keys,
            short[] values) {
        ImmutableLHashSeparateKVLongShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMap(
            Long[] keys, Short[] values) {
        ImmutableLHashSeparateKVLongShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMap(Iterable<Long> keys,
            Iterable<Short> values) {
        ImmutableLHashSeparateKVLongShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMapOf(long k1, short v1) {
        ImmutableLHashSeparateKVLongShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMapOf(long k1, short v1,
             long k2, short v2) {
        ImmutableLHashSeparateKVLongShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMapOf(long k1, short v1,
             long k2, short v2, long k3, short v3) {
        ImmutableLHashSeparateKVLongShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMapOf(long k1, short v1,
             long k2, short v2, long k3, short v3,
             long k4, short v4) {
        ImmutableLHashSeparateKVLongShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongShortMap newImmutableMapOf(long k1, short v1,
             long k2, short v2, long k3, short v3,
             long k4, short v4, long k5, short v5) {
        ImmutableLHashSeparateKVLongShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

