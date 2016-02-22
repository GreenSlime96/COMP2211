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
import net.openhft.koloboke.collect.map.hash.HashByteShortMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashByteShortMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashSeparateKVByteShortMapFactoryGO
        extends QHashSeparateKVByteShortMapFactorySO {

    QHashSeparateKVByteShortMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashByteShortMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper);

    abstract HashByteShortMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper);

    abstract HashByteShortMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper);

    @Override
    public final HashByteShortMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashByteShortMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashByteShortMapFactory withDomain(byte lower, byte upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashByteShortMapFactory withKeysDomain(byte lower, byte upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashByteShortMapFactory withKeysDomainComplement(byte lower, byte upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((byte) (upper + 1), (byte) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashByteShortMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashByteShortMapFactory) {
            HashByteShortMapFactory factory = (HashByteShortMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashSeparateKVByteShortMapGO shrunk(
            UpdatableQHashSeparateKVByteShortMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashSeparateKVByteShortMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMap(
            Map<Byte, Short> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMap(
            Map<Byte, Short> map1, Map<Byte, Short> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMap(
            Map<Byte, Short> map1, Map<Byte, Short> map2,
            Map<Byte, Short> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMap(
            Map<Byte, Short> map1, Map<Byte, Short> map2,
            Map<Byte, Short> map3, Map<Byte, Short> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMap(
            Map<Byte, Short> map1, Map<Byte, Short> map2,
            Map<Byte, Short> map3, Map<Byte, Short> map4,
            Map<Byte, Short> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMap(
            Map<Byte, Short> map1, Map<Byte, Short> map2,
            int expectedSize) {
        UpdatableQHashSeparateKVByteShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMap(
            Map<Byte, Short> map1, Map<Byte, Short> map2,
            Map<Byte, Short> map3, int expectedSize) {
        UpdatableQHashSeparateKVByteShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMap(
            Map<Byte, Short> map1, Map<Byte, Short> map2,
            Map<Byte, Short> map3, Map<Byte, Short> map4,
            int expectedSize) {
        UpdatableQHashSeparateKVByteShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMap(
            Map<Byte, Short> map1, Map<Byte, Short> map2,
            Map<Byte, Short> map3, Map<Byte, Short> map4,
            Map<Byte, Short> map5, int expectedSize) {
        UpdatableQHashSeparateKVByteShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ByteShortConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ByteShortConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashSeparateKVByteShortMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.ByteShortConsumer() {
             @Override
             public void accept(byte k, short v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMap(
            byte[] keys, short[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMap(
            byte[] keys, short[] values, int expectedSize) {
        UpdatableQHashSeparateKVByteShortMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMap(
            Byte[] keys, Short[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMap(
            Byte[] keys, Short[] values, int expectedSize) {
        UpdatableQHashSeparateKVByteShortMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMap(
            Iterable<Byte> keys, Iterable<Short> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMap(
            Iterable<Byte> keys, Iterable<Short> values, int expectedSize) {
        UpdatableQHashSeparateKVByteShortMapGO map = newUpdatableMap(expectedSize);
        Iterator<Byte> keysIt = keys.iterator();
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
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMapOf(
            byte k1, short v1) {
        UpdatableQHashSeparateKVByteShortMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMapOf(
            byte k1, short v1, byte k2, short v2) {
        UpdatableQHashSeparateKVByteShortMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMapOf(
            byte k1, short v1, byte k2, short v2,
            byte k3, short v3) {
        UpdatableQHashSeparateKVByteShortMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMapOf(
            byte k1, short v1, byte k2, short v2,
            byte k3, short v3, byte k4, short v4) {
        UpdatableQHashSeparateKVByteShortMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteShortMapGO newUpdatableMapOf(
            byte k1, short v1, byte k2, short v2,
            byte k3, short v3, byte k4, short v4,
            byte k5, short v5) {
        UpdatableQHashSeparateKVByteShortMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashByteShortMap newMutableMap(Map<Byte, Short> map1,
            Map<Byte, Short> map2, int expectedSize) {
        MutableQHashSeparateKVByteShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newMutableMap(Map<Byte, Short> map1,
            Map<Byte, Short> map2, Map<Byte, Short> map3, int expectedSize) {
        MutableQHashSeparateKVByteShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newMutableMap(Map<Byte, Short> map1,
            Map<Byte, Short> map2, Map<Byte, Short> map3,
            Map<Byte, Short> map4, int expectedSize) {
        MutableQHashSeparateKVByteShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newMutableMap(Map<Byte, Short> map1,
            Map<Byte, Short> map2, Map<Byte, Short> map3,
            Map<Byte, Short> map4, Map<Byte, Short> map5, int expectedSize) {
        MutableQHashSeparateKVByteShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteShortMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ByteShortConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashSeparateKVByteShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newMutableMap(byte[] keys,
            short[] values, int expectedSize) {
        MutableQHashSeparateKVByteShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newMutableMap(
            Byte[] keys, Short[] values, int expectedSize) {
        MutableQHashSeparateKVByteShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newMutableMap(Iterable<Byte> keys,
            Iterable<Short> values, int expectedSize) {
        MutableQHashSeparateKVByteShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashByteShortMap newMutableMap(
            Map<Byte, Short> map) {
        MutableQHashSeparateKVByteShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newMutableMap(Map<Byte, Short> map1,
            Map<Byte, Short> map2) {
        MutableQHashSeparateKVByteShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newMutableMap(Map<Byte, Short> map1,
            Map<Byte, Short> map2, Map<Byte, Short> map3) {
        MutableQHashSeparateKVByteShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newMutableMap(Map<Byte, Short> map1,
            Map<Byte, Short> map2, Map<Byte, Short> map3,
            Map<Byte, Short> map4) {
        MutableQHashSeparateKVByteShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newMutableMap(Map<Byte, Short> map1,
            Map<Byte, Short> map2, Map<Byte, Short> map3,
            Map<Byte, Short> map4, Map<Byte, Short> map5) {
        MutableQHashSeparateKVByteShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteShortMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ByteShortConsumer> entriesSupplier
            ) {
        MutableQHashSeparateKVByteShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newMutableMap(byte[] keys,
            short[] values) {
        MutableQHashSeparateKVByteShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newMutableMap(
            Byte[] keys, Short[] values) {
        MutableQHashSeparateKVByteShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newMutableMap(Iterable<Byte> keys,
            Iterable<Short> values) {
        MutableQHashSeparateKVByteShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashByteShortMap newMutableMapOf(byte k1, short v1) {
        MutableQHashSeparateKVByteShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newMutableMapOf(byte k1, short v1,
             byte k2, short v2) {
        MutableQHashSeparateKVByteShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newMutableMapOf(byte k1, short v1,
             byte k2, short v2, byte k3, short v3) {
        MutableQHashSeparateKVByteShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newMutableMapOf(byte k1, short v1,
             byte k2, short v2, byte k3, short v3,
             byte k4, short v4) {
        MutableQHashSeparateKVByteShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newMutableMapOf(byte k1, short v1,
             byte k2, short v2, byte k3, short v3,
             byte k4, short v4, byte k5, short v5) {
        MutableQHashSeparateKVByteShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMap(Map<Byte, Short> map1,
            Map<Byte, Short> map2, int expectedSize) {
        ImmutableQHashSeparateKVByteShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMap(Map<Byte, Short> map1,
            Map<Byte, Short> map2, Map<Byte, Short> map3, int expectedSize) {
        ImmutableQHashSeparateKVByteShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMap(Map<Byte, Short> map1,
            Map<Byte, Short> map2, Map<Byte, Short> map3,
            Map<Byte, Short> map4, int expectedSize) {
        ImmutableQHashSeparateKVByteShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMap(Map<Byte, Short> map1,
            Map<Byte, Short> map2, Map<Byte, Short> map3,
            Map<Byte, Short> map4, Map<Byte, Short> map5, int expectedSize) {
        ImmutableQHashSeparateKVByteShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ByteShortConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashSeparateKVByteShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMap(byte[] keys,
            short[] values, int expectedSize) {
        ImmutableQHashSeparateKVByteShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMap(
            Byte[] keys, Short[] values, int expectedSize) {
        ImmutableQHashSeparateKVByteShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMap(Iterable<Byte> keys,
            Iterable<Short> values, int expectedSize) {
        ImmutableQHashSeparateKVByteShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMap(
            Map<Byte, Short> map) {
        ImmutableQHashSeparateKVByteShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMap(Map<Byte, Short> map1,
            Map<Byte, Short> map2) {
        ImmutableQHashSeparateKVByteShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMap(Map<Byte, Short> map1,
            Map<Byte, Short> map2, Map<Byte, Short> map3) {
        ImmutableQHashSeparateKVByteShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMap(Map<Byte, Short> map1,
            Map<Byte, Short> map2, Map<Byte, Short> map3,
            Map<Byte, Short> map4) {
        ImmutableQHashSeparateKVByteShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMap(Map<Byte, Short> map1,
            Map<Byte, Short> map2, Map<Byte, Short> map3,
            Map<Byte, Short> map4, Map<Byte, Short> map5) {
        ImmutableQHashSeparateKVByteShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ByteShortConsumer> entriesSupplier
            ) {
        ImmutableQHashSeparateKVByteShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMap(byte[] keys,
            short[] values) {
        ImmutableQHashSeparateKVByteShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMap(
            Byte[] keys, Short[] values) {
        ImmutableQHashSeparateKVByteShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMap(Iterable<Byte> keys,
            Iterable<Short> values) {
        ImmutableQHashSeparateKVByteShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMapOf(byte k1, short v1) {
        ImmutableQHashSeparateKVByteShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMapOf(byte k1, short v1,
             byte k2, short v2) {
        ImmutableQHashSeparateKVByteShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMapOf(byte k1, short v1,
             byte k2, short v2, byte k3, short v3) {
        ImmutableQHashSeparateKVByteShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMapOf(byte k1, short v1,
             byte k2, short v2, byte k3, short v3,
             byte k4, short v4) {
        ImmutableQHashSeparateKVByteShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteShortMap newImmutableMapOf(byte k1, short v1,
             byte k2, short v2, byte k3, short v3,
             byte k4, short v4, byte k5, short v5) {
        ImmutableQHashSeparateKVByteShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

