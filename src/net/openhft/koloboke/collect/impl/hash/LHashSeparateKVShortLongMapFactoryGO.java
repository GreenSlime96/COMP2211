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
import net.openhft.koloboke.collect.map.hash.HashShortLongMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashShortLongMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashSeparateKVShortLongMapFactoryGO
        extends LHashSeparateKVShortLongMapFactorySO {

    LHashSeparateKVShortLongMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashShortLongMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper);

    abstract HashShortLongMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper);

    abstract HashShortLongMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper);

    @Override
    public final HashShortLongMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashShortLongMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashShortLongMapFactory withDomain(short lower, short upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashShortLongMapFactory withKeysDomain(short lower, short upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashShortLongMapFactory withKeysDomainComplement(short lower, short upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((short) (upper + 1), (short) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashShortLongMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashShortLongMapFactory) {
            HashShortLongMapFactory factory = (HashShortLongMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashSeparateKVShortLongMapGO shrunk(
            UpdatableLHashSeparateKVShortLongMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashSeparateKVShortLongMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMap(
            Map<Short, Long> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMap(
            Map<Short, Long> map1, Map<Short, Long> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMap(
            Map<Short, Long> map1, Map<Short, Long> map2,
            Map<Short, Long> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMap(
            Map<Short, Long> map1, Map<Short, Long> map2,
            Map<Short, Long> map3, Map<Short, Long> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMap(
            Map<Short, Long> map1, Map<Short, Long> map2,
            Map<Short, Long> map3, Map<Short, Long> map4,
            Map<Short, Long> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMap(
            Map<Short, Long> map1, Map<Short, Long> map2,
            int expectedSize) {
        UpdatableLHashSeparateKVShortLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMap(
            Map<Short, Long> map1, Map<Short, Long> map2,
            Map<Short, Long> map3, int expectedSize) {
        UpdatableLHashSeparateKVShortLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMap(
            Map<Short, Long> map1, Map<Short, Long> map2,
            Map<Short, Long> map3, Map<Short, Long> map4,
            int expectedSize) {
        UpdatableLHashSeparateKVShortLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMap(
            Map<Short, Long> map1, Map<Short, Long> map2,
            Map<Short, Long> map3, Map<Short, Long> map4,
            Map<Short, Long> map5, int expectedSize) {
        UpdatableLHashSeparateKVShortLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ShortLongConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ShortLongConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashSeparateKVShortLongMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.ShortLongConsumer() {
             @Override
             public void accept(short k, long v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMap(
            short[] keys, long[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMap(
            short[] keys, long[] values, int expectedSize) {
        UpdatableLHashSeparateKVShortLongMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMap(
            Short[] keys, Long[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMap(
            Short[] keys, Long[] values, int expectedSize) {
        UpdatableLHashSeparateKVShortLongMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMap(
            Iterable<Short> keys, Iterable<Long> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMap(
            Iterable<Short> keys, Iterable<Long> values, int expectedSize) {
        UpdatableLHashSeparateKVShortLongMapGO map = newUpdatableMap(expectedSize);
        Iterator<Short> keysIt = keys.iterator();
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
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMapOf(
            short k1, long v1) {
        UpdatableLHashSeparateKVShortLongMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMapOf(
            short k1, long v1, short k2, long v2) {
        UpdatableLHashSeparateKVShortLongMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMapOf(
            short k1, long v1, short k2, long v2,
            short k3, long v3) {
        UpdatableLHashSeparateKVShortLongMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMapOf(
            short k1, long v1, short k2, long v2,
            short k3, long v3, short k4, long v4) {
        UpdatableLHashSeparateKVShortLongMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVShortLongMapGO newUpdatableMapOf(
            short k1, long v1, short k2, long v2,
            short k3, long v3, short k4, long v4,
            short k5, long v5) {
        UpdatableLHashSeparateKVShortLongMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashShortLongMap newMutableMap(Map<Short, Long> map1,
            Map<Short, Long> map2, int expectedSize) {
        MutableLHashSeparateKVShortLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newMutableMap(Map<Short, Long> map1,
            Map<Short, Long> map2, Map<Short, Long> map3, int expectedSize) {
        MutableLHashSeparateKVShortLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newMutableMap(Map<Short, Long> map1,
            Map<Short, Long> map2, Map<Short, Long> map3,
            Map<Short, Long> map4, int expectedSize) {
        MutableLHashSeparateKVShortLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newMutableMap(Map<Short, Long> map1,
            Map<Short, Long> map2, Map<Short, Long> map3,
            Map<Short, Long> map4, Map<Short, Long> map5, int expectedSize) {
        MutableLHashSeparateKVShortLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashShortLongMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ShortLongConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashSeparateKVShortLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newMutableMap(short[] keys,
            long[] values, int expectedSize) {
        MutableLHashSeparateKVShortLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newMutableMap(
            Short[] keys, Long[] values, int expectedSize) {
        MutableLHashSeparateKVShortLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newMutableMap(Iterable<Short> keys,
            Iterable<Long> values, int expectedSize) {
        MutableLHashSeparateKVShortLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashShortLongMap newMutableMap(
            Map<Short, Long> map) {
        MutableLHashSeparateKVShortLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newMutableMap(Map<Short, Long> map1,
            Map<Short, Long> map2) {
        MutableLHashSeparateKVShortLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newMutableMap(Map<Short, Long> map1,
            Map<Short, Long> map2, Map<Short, Long> map3) {
        MutableLHashSeparateKVShortLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newMutableMap(Map<Short, Long> map1,
            Map<Short, Long> map2, Map<Short, Long> map3,
            Map<Short, Long> map4) {
        MutableLHashSeparateKVShortLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newMutableMap(Map<Short, Long> map1,
            Map<Short, Long> map2, Map<Short, Long> map3,
            Map<Short, Long> map4, Map<Short, Long> map5) {
        MutableLHashSeparateKVShortLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashShortLongMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ShortLongConsumer> entriesSupplier
            ) {
        MutableLHashSeparateKVShortLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newMutableMap(short[] keys,
            long[] values) {
        MutableLHashSeparateKVShortLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newMutableMap(
            Short[] keys, Long[] values) {
        MutableLHashSeparateKVShortLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newMutableMap(Iterable<Short> keys,
            Iterable<Long> values) {
        MutableLHashSeparateKVShortLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashShortLongMap newMutableMapOf(short k1, long v1) {
        MutableLHashSeparateKVShortLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newMutableMapOf(short k1, long v1,
             short k2, long v2) {
        MutableLHashSeparateKVShortLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newMutableMapOf(short k1, long v1,
             short k2, long v2, short k3, long v3) {
        MutableLHashSeparateKVShortLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newMutableMapOf(short k1, long v1,
             short k2, long v2, short k3, long v3,
             short k4, long v4) {
        MutableLHashSeparateKVShortLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newMutableMapOf(short k1, long v1,
             short k2, long v2, short k3, long v3,
             short k4, long v4, short k5, long v5) {
        MutableLHashSeparateKVShortLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMap(Map<Short, Long> map1,
            Map<Short, Long> map2, int expectedSize) {
        ImmutableLHashSeparateKVShortLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMap(Map<Short, Long> map1,
            Map<Short, Long> map2, Map<Short, Long> map3, int expectedSize) {
        ImmutableLHashSeparateKVShortLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMap(Map<Short, Long> map1,
            Map<Short, Long> map2, Map<Short, Long> map3,
            Map<Short, Long> map4, int expectedSize) {
        ImmutableLHashSeparateKVShortLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMap(Map<Short, Long> map1,
            Map<Short, Long> map2, Map<Short, Long> map3,
            Map<Short, Long> map4, Map<Short, Long> map5, int expectedSize) {
        ImmutableLHashSeparateKVShortLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ShortLongConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashSeparateKVShortLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMap(short[] keys,
            long[] values, int expectedSize) {
        ImmutableLHashSeparateKVShortLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMap(
            Short[] keys, Long[] values, int expectedSize) {
        ImmutableLHashSeparateKVShortLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMap(Iterable<Short> keys,
            Iterable<Long> values, int expectedSize) {
        ImmutableLHashSeparateKVShortLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMap(
            Map<Short, Long> map) {
        ImmutableLHashSeparateKVShortLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMap(Map<Short, Long> map1,
            Map<Short, Long> map2) {
        ImmutableLHashSeparateKVShortLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMap(Map<Short, Long> map1,
            Map<Short, Long> map2, Map<Short, Long> map3) {
        ImmutableLHashSeparateKVShortLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMap(Map<Short, Long> map1,
            Map<Short, Long> map2, Map<Short, Long> map3,
            Map<Short, Long> map4) {
        ImmutableLHashSeparateKVShortLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMap(Map<Short, Long> map1,
            Map<Short, Long> map2, Map<Short, Long> map3,
            Map<Short, Long> map4, Map<Short, Long> map5) {
        ImmutableLHashSeparateKVShortLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ShortLongConsumer> entriesSupplier
            ) {
        ImmutableLHashSeparateKVShortLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMap(short[] keys,
            long[] values) {
        ImmutableLHashSeparateKVShortLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMap(
            Short[] keys, Long[] values) {
        ImmutableLHashSeparateKVShortLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMap(Iterable<Short> keys,
            Iterable<Long> values) {
        ImmutableLHashSeparateKVShortLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMapOf(short k1, long v1) {
        ImmutableLHashSeparateKVShortLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMapOf(short k1, long v1,
             short k2, long v2) {
        ImmutableLHashSeparateKVShortLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMapOf(short k1, long v1,
             short k2, long v2, short k3, long v3) {
        ImmutableLHashSeparateKVShortLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMapOf(short k1, long v1,
             short k2, long v2, short k3, long v3,
             short k4, long v4) {
        ImmutableLHashSeparateKVShortLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortLongMap newImmutableMapOf(short k1, long v1,
             short k2, long v2, short k3, long v3,
             short k4, long v4, short k5, long v5) {
        ImmutableLHashSeparateKVShortLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

