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
import net.openhft.koloboke.collect.map.hash.HashByteLongMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashByteLongMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashSeparateKVByteLongMapFactoryGO
        extends QHashSeparateKVByteLongMapFactorySO {

    QHashSeparateKVByteLongMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashByteLongMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper);

    abstract HashByteLongMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper);

    abstract HashByteLongMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper);

    @Override
    public final HashByteLongMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashByteLongMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashByteLongMapFactory withDomain(byte lower, byte upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashByteLongMapFactory withKeysDomain(byte lower, byte upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashByteLongMapFactory withKeysDomainComplement(byte lower, byte upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((byte) (upper + 1), (byte) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashByteLongMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashByteLongMapFactory) {
            HashByteLongMapFactory factory = (HashByteLongMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashSeparateKVByteLongMapGO shrunk(
            UpdatableQHashSeparateKVByteLongMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashSeparateKVByteLongMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMap(
            Map<Byte, Long> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMap(
            Map<Byte, Long> map1, Map<Byte, Long> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMap(
            Map<Byte, Long> map1, Map<Byte, Long> map2,
            Map<Byte, Long> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMap(
            Map<Byte, Long> map1, Map<Byte, Long> map2,
            Map<Byte, Long> map3, Map<Byte, Long> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMap(
            Map<Byte, Long> map1, Map<Byte, Long> map2,
            Map<Byte, Long> map3, Map<Byte, Long> map4,
            Map<Byte, Long> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMap(
            Map<Byte, Long> map1, Map<Byte, Long> map2,
            int expectedSize) {
        UpdatableQHashSeparateKVByteLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMap(
            Map<Byte, Long> map1, Map<Byte, Long> map2,
            Map<Byte, Long> map3, int expectedSize) {
        UpdatableQHashSeparateKVByteLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMap(
            Map<Byte, Long> map1, Map<Byte, Long> map2,
            Map<Byte, Long> map3, Map<Byte, Long> map4,
            int expectedSize) {
        UpdatableQHashSeparateKVByteLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMap(
            Map<Byte, Long> map1, Map<Byte, Long> map2,
            Map<Byte, Long> map3, Map<Byte, Long> map4,
            Map<Byte, Long> map5, int expectedSize) {
        UpdatableQHashSeparateKVByteLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ByteLongConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ByteLongConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashSeparateKVByteLongMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.ByteLongConsumer() {
             @Override
             public void accept(byte k, long v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMap(
            byte[] keys, long[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMap(
            byte[] keys, long[] values, int expectedSize) {
        UpdatableQHashSeparateKVByteLongMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMap(
            Byte[] keys, Long[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMap(
            Byte[] keys, Long[] values, int expectedSize) {
        UpdatableQHashSeparateKVByteLongMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMap(
            Iterable<Byte> keys, Iterable<Long> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMap(
            Iterable<Byte> keys, Iterable<Long> values, int expectedSize) {
        UpdatableQHashSeparateKVByteLongMapGO map = newUpdatableMap(expectedSize);
        Iterator<Byte> keysIt = keys.iterator();
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
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMapOf(
            byte k1, long v1) {
        UpdatableQHashSeparateKVByteLongMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMapOf(
            byte k1, long v1, byte k2, long v2) {
        UpdatableQHashSeparateKVByteLongMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMapOf(
            byte k1, long v1, byte k2, long v2,
            byte k3, long v3) {
        UpdatableQHashSeparateKVByteLongMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMapOf(
            byte k1, long v1, byte k2, long v2,
            byte k3, long v3, byte k4, long v4) {
        UpdatableQHashSeparateKVByteLongMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteLongMapGO newUpdatableMapOf(
            byte k1, long v1, byte k2, long v2,
            byte k3, long v3, byte k4, long v4,
            byte k5, long v5) {
        UpdatableQHashSeparateKVByteLongMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashByteLongMap newMutableMap(Map<Byte, Long> map1,
            Map<Byte, Long> map2, int expectedSize) {
        MutableQHashSeparateKVByteLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newMutableMap(Map<Byte, Long> map1,
            Map<Byte, Long> map2, Map<Byte, Long> map3, int expectedSize) {
        MutableQHashSeparateKVByteLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newMutableMap(Map<Byte, Long> map1,
            Map<Byte, Long> map2, Map<Byte, Long> map3,
            Map<Byte, Long> map4, int expectedSize) {
        MutableQHashSeparateKVByteLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newMutableMap(Map<Byte, Long> map1,
            Map<Byte, Long> map2, Map<Byte, Long> map3,
            Map<Byte, Long> map4, Map<Byte, Long> map5, int expectedSize) {
        MutableQHashSeparateKVByteLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteLongMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ByteLongConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashSeparateKVByteLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newMutableMap(byte[] keys,
            long[] values, int expectedSize) {
        MutableQHashSeparateKVByteLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newMutableMap(
            Byte[] keys, Long[] values, int expectedSize) {
        MutableQHashSeparateKVByteLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newMutableMap(Iterable<Byte> keys,
            Iterable<Long> values, int expectedSize) {
        MutableQHashSeparateKVByteLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashByteLongMap newMutableMap(
            Map<Byte, Long> map) {
        MutableQHashSeparateKVByteLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newMutableMap(Map<Byte, Long> map1,
            Map<Byte, Long> map2) {
        MutableQHashSeparateKVByteLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newMutableMap(Map<Byte, Long> map1,
            Map<Byte, Long> map2, Map<Byte, Long> map3) {
        MutableQHashSeparateKVByteLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newMutableMap(Map<Byte, Long> map1,
            Map<Byte, Long> map2, Map<Byte, Long> map3,
            Map<Byte, Long> map4) {
        MutableQHashSeparateKVByteLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newMutableMap(Map<Byte, Long> map1,
            Map<Byte, Long> map2, Map<Byte, Long> map3,
            Map<Byte, Long> map4, Map<Byte, Long> map5) {
        MutableQHashSeparateKVByteLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteLongMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ByteLongConsumer> entriesSupplier
            ) {
        MutableQHashSeparateKVByteLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newMutableMap(byte[] keys,
            long[] values) {
        MutableQHashSeparateKVByteLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newMutableMap(
            Byte[] keys, Long[] values) {
        MutableQHashSeparateKVByteLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newMutableMap(Iterable<Byte> keys,
            Iterable<Long> values) {
        MutableQHashSeparateKVByteLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashByteLongMap newMutableMapOf(byte k1, long v1) {
        MutableQHashSeparateKVByteLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newMutableMapOf(byte k1, long v1,
             byte k2, long v2) {
        MutableQHashSeparateKVByteLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newMutableMapOf(byte k1, long v1,
             byte k2, long v2, byte k3, long v3) {
        MutableQHashSeparateKVByteLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newMutableMapOf(byte k1, long v1,
             byte k2, long v2, byte k3, long v3,
             byte k4, long v4) {
        MutableQHashSeparateKVByteLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newMutableMapOf(byte k1, long v1,
             byte k2, long v2, byte k3, long v3,
             byte k4, long v4, byte k5, long v5) {
        MutableQHashSeparateKVByteLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMap(Map<Byte, Long> map1,
            Map<Byte, Long> map2, int expectedSize) {
        ImmutableQHashSeparateKVByteLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMap(Map<Byte, Long> map1,
            Map<Byte, Long> map2, Map<Byte, Long> map3, int expectedSize) {
        ImmutableQHashSeparateKVByteLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMap(Map<Byte, Long> map1,
            Map<Byte, Long> map2, Map<Byte, Long> map3,
            Map<Byte, Long> map4, int expectedSize) {
        ImmutableQHashSeparateKVByteLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMap(Map<Byte, Long> map1,
            Map<Byte, Long> map2, Map<Byte, Long> map3,
            Map<Byte, Long> map4, Map<Byte, Long> map5, int expectedSize) {
        ImmutableQHashSeparateKVByteLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ByteLongConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashSeparateKVByteLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMap(byte[] keys,
            long[] values, int expectedSize) {
        ImmutableQHashSeparateKVByteLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMap(
            Byte[] keys, Long[] values, int expectedSize) {
        ImmutableQHashSeparateKVByteLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMap(Iterable<Byte> keys,
            Iterable<Long> values, int expectedSize) {
        ImmutableQHashSeparateKVByteLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMap(
            Map<Byte, Long> map) {
        ImmutableQHashSeparateKVByteLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMap(Map<Byte, Long> map1,
            Map<Byte, Long> map2) {
        ImmutableQHashSeparateKVByteLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMap(Map<Byte, Long> map1,
            Map<Byte, Long> map2, Map<Byte, Long> map3) {
        ImmutableQHashSeparateKVByteLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMap(Map<Byte, Long> map1,
            Map<Byte, Long> map2, Map<Byte, Long> map3,
            Map<Byte, Long> map4) {
        ImmutableQHashSeparateKVByteLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMap(Map<Byte, Long> map1,
            Map<Byte, Long> map2, Map<Byte, Long> map3,
            Map<Byte, Long> map4, Map<Byte, Long> map5) {
        ImmutableQHashSeparateKVByteLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ByteLongConsumer> entriesSupplier
            ) {
        ImmutableQHashSeparateKVByteLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMap(byte[] keys,
            long[] values) {
        ImmutableQHashSeparateKVByteLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMap(
            Byte[] keys, Long[] values) {
        ImmutableQHashSeparateKVByteLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMap(Iterable<Byte> keys,
            Iterable<Long> values) {
        ImmutableQHashSeparateKVByteLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMapOf(byte k1, long v1) {
        ImmutableQHashSeparateKVByteLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMapOf(byte k1, long v1,
             byte k2, long v2) {
        ImmutableQHashSeparateKVByteLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMapOf(byte k1, long v1,
             byte k2, long v2, byte k3, long v3) {
        ImmutableQHashSeparateKVByteLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMapOf(byte k1, long v1,
             byte k2, long v2, byte k3, long v3,
             byte k4, long v4) {
        ImmutableQHashSeparateKVByteLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteLongMap newImmutableMapOf(byte k1, long v1,
             byte k2, long v2, byte k3, long v3,
             byte k4, long v4, byte k5, long v5) {
        ImmutableQHashSeparateKVByteLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

