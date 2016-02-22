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
import net.openhft.koloboke.collect.map.hash.HashShortByteMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashShortByteMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashSeparateKVShortByteMapFactoryGO
        extends QHashSeparateKVShortByteMapFactorySO {

    QHashSeparateKVShortByteMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashShortByteMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper);

    abstract HashShortByteMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper);

    abstract HashShortByteMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper);

    @Override
    public final HashShortByteMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashShortByteMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashShortByteMapFactory withDomain(short lower, short upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashShortByteMapFactory withKeysDomain(short lower, short upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashShortByteMapFactory withKeysDomainComplement(short lower, short upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((short) (upper + 1), (short) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashShortByteMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashShortByteMapFactory) {
            HashShortByteMapFactory factory = (HashShortByteMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashSeparateKVShortByteMapGO shrunk(
            UpdatableQHashSeparateKVShortByteMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashSeparateKVShortByteMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMap(
            Map<Short, Byte> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMap(
            Map<Short, Byte> map1, Map<Short, Byte> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMap(
            Map<Short, Byte> map1, Map<Short, Byte> map2,
            Map<Short, Byte> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMap(
            Map<Short, Byte> map1, Map<Short, Byte> map2,
            Map<Short, Byte> map3, Map<Short, Byte> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMap(
            Map<Short, Byte> map1, Map<Short, Byte> map2,
            Map<Short, Byte> map3, Map<Short, Byte> map4,
            Map<Short, Byte> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMap(
            Map<Short, Byte> map1, Map<Short, Byte> map2,
            int expectedSize) {
        UpdatableQHashSeparateKVShortByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMap(
            Map<Short, Byte> map1, Map<Short, Byte> map2,
            Map<Short, Byte> map3, int expectedSize) {
        UpdatableQHashSeparateKVShortByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMap(
            Map<Short, Byte> map1, Map<Short, Byte> map2,
            Map<Short, Byte> map3, Map<Short, Byte> map4,
            int expectedSize) {
        UpdatableQHashSeparateKVShortByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMap(
            Map<Short, Byte> map1, Map<Short, Byte> map2,
            Map<Short, Byte> map3, Map<Short, Byte> map4,
            Map<Short, Byte> map5, int expectedSize) {
        UpdatableQHashSeparateKVShortByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ShortByteConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ShortByteConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashSeparateKVShortByteMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.ShortByteConsumer() {
             @Override
             public void accept(short k, byte v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMap(
            short[] keys, byte[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMap(
            short[] keys, byte[] values, int expectedSize) {
        UpdatableQHashSeparateKVShortByteMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMap(
            Short[] keys, Byte[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMap(
            Short[] keys, Byte[] values, int expectedSize) {
        UpdatableQHashSeparateKVShortByteMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMap(
            Iterable<Short> keys, Iterable<Byte> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMap(
            Iterable<Short> keys, Iterable<Byte> values, int expectedSize) {
        UpdatableQHashSeparateKVShortByteMapGO map = newUpdatableMap(expectedSize);
        Iterator<Short> keysIt = keys.iterator();
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
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMapOf(
            short k1, byte v1) {
        UpdatableQHashSeparateKVShortByteMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMapOf(
            short k1, byte v1, short k2, byte v2) {
        UpdatableQHashSeparateKVShortByteMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMapOf(
            short k1, byte v1, short k2, byte v2,
            short k3, byte v3) {
        UpdatableQHashSeparateKVShortByteMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMapOf(
            short k1, byte v1, short k2, byte v2,
            short k3, byte v3, short k4, byte v4) {
        UpdatableQHashSeparateKVShortByteMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVShortByteMapGO newUpdatableMapOf(
            short k1, byte v1, short k2, byte v2,
            short k3, byte v3, short k4, byte v4,
            short k5, byte v5) {
        UpdatableQHashSeparateKVShortByteMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashShortByteMap newMutableMap(Map<Short, Byte> map1,
            Map<Short, Byte> map2, int expectedSize) {
        MutableQHashSeparateKVShortByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newMutableMap(Map<Short, Byte> map1,
            Map<Short, Byte> map2, Map<Short, Byte> map3, int expectedSize) {
        MutableQHashSeparateKVShortByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newMutableMap(Map<Short, Byte> map1,
            Map<Short, Byte> map2, Map<Short, Byte> map3,
            Map<Short, Byte> map4, int expectedSize) {
        MutableQHashSeparateKVShortByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newMutableMap(Map<Short, Byte> map1,
            Map<Short, Byte> map2, Map<Short, Byte> map3,
            Map<Short, Byte> map4, Map<Short, Byte> map5, int expectedSize) {
        MutableQHashSeparateKVShortByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashShortByteMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ShortByteConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashSeparateKVShortByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newMutableMap(short[] keys,
            byte[] values, int expectedSize) {
        MutableQHashSeparateKVShortByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newMutableMap(
            Short[] keys, Byte[] values, int expectedSize) {
        MutableQHashSeparateKVShortByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newMutableMap(Iterable<Short> keys,
            Iterable<Byte> values, int expectedSize) {
        MutableQHashSeparateKVShortByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashShortByteMap newMutableMap(
            Map<Short, Byte> map) {
        MutableQHashSeparateKVShortByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newMutableMap(Map<Short, Byte> map1,
            Map<Short, Byte> map2) {
        MutableQHashSeparateKVShortByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newMutableMap(Map<Short, Byte> map1,
            Map<Short, Byte> map2, Map<Short, Byte> map3) {
        MutableQHashSeparateKVShortByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newMutableMap(Map<Short, Byte> map1,
            Map<Short, Byte> map2, Map<Short, Byte> map3,
            Map<Short, Byte> map4) {
        MutableQHashSeparateKVShortByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newMutableMap(Map<Short, Byte> map1,
            Map<Short, Byte> map2, Map<Short, Byte> map3,
            Map<Short, Byte> map4, Map<Short, Byte> map5) {
        MutableQHashSeparateKVShortByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashShortByteMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ShortByteConsumer> entriesSupplier
            ) {
        MutableQHashSeparateKVShortByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newMutableMap(short[] keys,
            byte[] values) {
        MutableQHashSeparateKVShortByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newMutableMap(
            Short[] keys, Byte[] values) {
        MutableQHashSeparateKVShortByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newMutableMap(Iterable<Short> keys,
            Iterable<Byte> values) {
        MutableQHashSeparateKVShortByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashShortByteMap newMutableMapOf(short k1, byte v1) {
        MutableQHashSeparateKVShortByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newMutableMapOf(short k1, byte v1,
             short k2, byte v2) {
        MutableQHashSeparateKVShortByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newMutableMapOf(short k1, byte v1,
             short k2, byte v2, short k3, byte v3) {
        MutableQHashSeparateKVShortByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newMutableMapOf(short k1, byte v1,
             short k2, byte v2, short k3, byte v3,
             short k4, byte v4) {
        MutableQHashSeparateKVShortByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newMutableMapOf(short k1, byte v1,
             short k2, byte v2, short k3, byte v3,
             short k4, byte v4, short k5, byte v5) {
        MutableQHashSeparateKVShortByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMap(Map<Short, Byte> map1,
            Map<Short, Byte> map2, int expectedSize) {
        ImmutableQHashSeparateKVShortByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMap(Map<Short, Byte> map1,
            Map<Short, Byte> map2, Map<Short, Byte> map3, int expectedSize) {
        ImmutableQHashSeparateKVShortByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMap(Map<Short, Byte> map1,
            Map<Short, Byte> map2, Map<Short, Byte> map3,
            Map<Short, Byte> map4, int expectedSize) {
        ImmutableQHashSeparateKVShortByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMap(Map<Short, Byte> map1,
            Map<Short, Byte> map2, Map<Short, Byte> map3,
            Map<Short, Byte> map4, Map<Short, Byte> map5, int expectedSize) {
        ImmutableQHashSeparateKVShortByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ShortByteConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashSeparateKVShortByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMap(short[] keys,
            byte[] values, int expectedSize) {
        ImmutableQHashSeparateKVShortByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMap(
            Short[] keys, Byte[] values, int expectedSize) {
        ImmutableQHashSeparateKVShortByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMap(Iterable<Short> keys,
            Iterable<Byte> values, int expectedSize) {
        ImmutableQHashSeparateKVShortByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMap(
            Map<Short, Byte> map) {
        ImmutableQHashSeparateKVShortByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMap(Map<Short, Byte> map1,
            Map<Short, Byte> map2) {
        ImmutableQHashSeparateKVShortByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMap(Map<Short, Byte> map1,
            Map<Short, Byte> map2, Map<Short, Byte> map3) {
        ImmutableQHashSeparateKVShortByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMap(Map<Short, Byte> map1,
            Map<Short, Byte> map2, Map<Short, Byte> map3,
            Map<Short, Byte> map4) {
        ImmutableQHashSeparateKVShortByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMap(Map<Short, Byte> map1,
            Map<Short, Byte> map2, Map<Short, Byte> map3,
            Map<Short, Byte> map4, Map<Short, Byte> map5) {
        ImmutableQHashSeparateKVShortByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ShortByteConsumer> entriesSupplier
            ) {
        ImmutableQHashSeparateKVShortByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMap(short[] keys,
            byte[] values) {
        ImmutableQHashSeparateKVShortByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMap(
            Short[] keys, Byte[] values) {
        ImmutableQHashSeparateKVShortByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMap(Iterable<Short> keys,
            Iterable<Byte> values) {
        ImmutableQHashSeparateKVShortByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMapOf(short k1, byte v1) {
        ImmutableQHashSeparateKVShortByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMapOf(short k1, byte v1,
             short k2, byte v2) {
        ImmutableQHashSeparateKVShortByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMapOf(short k1, byte v1,
             short k2, byte v2, short k3, byte v3) {
        ImmutableQHashSeparateKVShortByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMapOf(short k1, byte v1,
             short k2, byte v2, short k3, byte v3,
             short k4, byte v4) {
        ImmutableQHashSeparateKVShortByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortByteMap newImmutableMapOf(short k1, byte v1,
             short k2, byte v2, short k3, byte v3,
             short k4, byte v4, short k5, byte v5) {
        ImmutableQHashSeparateKVShortByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

