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
import net.openhft.koloboke.collect.map.hash.HashCharByteMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashCharByteMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashSeparateKVCharByteMapFactoryGO
        extends LHashSeparateKVCharByteMapFactorySO {

    LHashSeparateKVCharByteMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashCharByteMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper);

    abstract HashCharByteMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper);

    abstract HashCharByteMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper);

    @Override
    public final HashCharByteMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashCharByteMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashCharByteMapFactory withDomain(char lower, char upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashCharByteMapFactory withKeysDomain(char lower, char upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashCharByteMapFactory withKeysDomainComplement(char lower, char upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((char) (upper + 1), (char) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashCharByteMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashCharByteMapFactory) {
            HashCharByteMapFactory factory = (HashCharByteMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashSeparateKVCharByteMapGO shrunk(
            UpdatableLHashSeparateKVCharByteMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashSeparateKVCharByteMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMap(
            Map<Character, Byte> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMap(
            Map<Character, Byte> map1, Map<Character, Byte> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMap(
            Map<Character, Byte> map1, Map<Character, Byte> map2,
            Map<Character, Byte> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMap(
            Map<Character, Byte> map1, Map<Character, Byte> map2,
            Map<Character, Byte> map3, Map<Character, Byte> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMap(
            Map<Character, Byte> map1, Map<Character, Byte> map2,
            Map<Character, Byte> map3, Map<Character, Byte> map4,
            Map<Character, Byte> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMap(
            Map<Character, Byte> map1, Map<Character, Byte> map2,
            int expectedSize) {
        UpdatableLHashSeparateKVCharByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMap(
            Map<Character, Byte> map1, Map<Character, Byte> map2,
            Map<Character, Byte> map3, int expectedSize) {
        UpdatableLHashSeparateKVCharByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMap(
            Map<Character, Byte> map1, Map<Character, Byte> map2,
            Map<Character, Byte> map3, Map<Character, Byte> map4,
            int expectedSize) {
        UpdatableLHashSeparateKVCharByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMap(
            Map<Character, Byte> map1, Map<Character, Byte> map2,
            Map<Character, Byte> map3, Map<Character, Byte> map4,
            Map<Character, Byte> map5, int expectedSize) {
        UpdatableLHashSeparateKVCharByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.CharByteConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.CharByteConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashSeparateKVCharByteMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.CharByteConsumer() {
             @Override
             public void accept(char k, byte v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMap(
            char[] keys, byte[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMap(
            char[] keys, byte[] values, int expectedSize) {
        UpdatableLHashSeparateKVCharByteMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMap(
            Character[] keys, Byte[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMap(
            Character[] keys, Byte[] values, int expectedSize) {
        UpdatableLHashSeparateKVCharByteMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMap(
            Iterable<Character> keys, Iterable<Byte> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMap(
            Iterable<Character> keys, Iterable<Byte> values, int expectedSize) {
        UpdatableLHashSeparateKVCharByteMapGO map = newUpdatableMap(expectedSize);
        Iterator<Character> keysIt = keys.iterator();
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
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMapOf(
            char k1, byte v1) {
        UpdatableLHashSeparateKVCharByteMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMapOf(
            char k1, byte v1, char k2, byte v2) {
        UpdatableLHashSeparateKVCharByteMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMapOf(
            char k1, byte v1, char k2, byte v2,
            char k3, byte v3) {
        UpdatableLHashSeparateKVCharByteMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMapOf(
            char k1, byte v1, char k2, byte v2,
            char k3, byte v3, char k4, byte v4) {
        UpdatableLHashSeparateKVCharByteMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharByteMapGO newUpdatableMapOf(
            char k1, byte v1, char k2, byte v2,
            char k3, byte v3, char k4, byte v4,
            char k5, byte v5) {
        UpdatableLHashSeparateKVCharByteMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashCharByteMap newMutableMap(Map<Character, Byte> map1,
            Map<Character, Byte> map2, int expectedSize) {
        MutableLHashSeparateKVCharByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newMutableMap(Map<Character, Byte> map1,
            Map<Character, Byte> map2, Map<Character, Byte> map3, int expectedSize) {
        MutableLHashSeparateKVCharByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newMutableMap(Map<Character, Byte> map1,
            Map<Character, Byte> map2, Map<Character, Byte> map3,
            Map<Character, Byte> map4, int expectedSize) {
        MutableLHashSeparateKVCharByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newMutableMap(Map<Character, Byte> map1,
            Map<Character, Byte> map2, Map<Character, Byte> map3,
            Map<Character, Byte> map4, Map<Character, Byte> map5, int expectedSize) {
        MutableLHashSeparateKVCharByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharByteMap newMutableMap(
            Consumer<net.openhft.koloboke.function.CharByteConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashSeparateKVCharByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newMutableMap(char[] keys,
            byte[] values, int expectedSize) {
        MutableLHashSeparateKVCharByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newMutableMap(
            Character[] keys, Byte[] values, int expectedSize) {
        MutableLHashSeparateKVCharByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newMutableMap(Iterable<Character> keys,
            Iterable<Byte> values, int expectedSize) {
        MutableLHashSeparateKVCharByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashCharByteMap newMutableMap(
            Map<Character, Byte> map) {
        MutableLHashSeparateKVCharByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newMutableMap(Map<Character, Byte> map1,
            Map<Character, Byte> map2) {
        MutableLHashSeparateKVCharByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newMutableMap(Map<Character, Byte> map1,
            Map<Character, Byte> map2, Map<Character, Byte> map3) {
        MutableLHashSeparateKVCharByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newMutableMap(Map<Character, Byte> map1,
            Map<Character, Byte> map2, Map<Character, Byte> map3,
            Map<Character, Byte> map4) {
        MutableLHashSeparateKVCharByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newMutableMap(Map<Character, Byte> map1,
            Map<Character, Byte> map2, Map<Character, Byte> map3,
            Map<Character, Byte> map4, Map<Character, Byte> map5) {
        MutableLHashSeparateKVCharByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharByteMap newMutableMap(
            Consumer<net.openhft.koloboke.function.CharByteConsumer> entriesSupplier
            ) {
        MutableLHashSeparateKVCharByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newMutableMap(char[] keys,
            byte[] values) {
        MutableLHashSeparateKVCharByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newMutableMap(
            Character[] keys, Byte[] values) {
        MutableLHashSeparateKVCharByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newMutableMap(Iterable<Character> keys,
            Iterable<Byte> values) {
        MutableLHashSeparateKVCharByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashCharByteMap newMutableMapOf(char k1, byte v1) {
        MutableLHashSeparateKVCharByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newMutableMapOf(char k1, byte v1,
             char k2, byte v2) {
        MutableLHashSeparateKVCharByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newMutableMapOf(char k1, byte v1,
             char k2, byte v2, char k3, byte v3) {
        MutableLHashSeparateKVCharByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newMutableMapOf(char k1, byte v1,
             char k2, byte v2, char k3, byte v3,
             char k4, byte v4) {
        MutableLHashSeparateKVCharByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newMutableMapOf(char k1, byte v1,
             char k2, byte v2, char k3, byte v3,
             char k4, byte v4, char k5, byte v5) {
        MutableLHashSeparateKVCharByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMap(Map<Character, Byte> map1,
            Map<Character, Byte> map2, int expectedSize) {
        ImmutableLHashSeparateKVCharByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMap(Map<Character, Byte> map1,
            Map<Character, Byte> map2, Map<Character, Byte> map3, int expectedSize) {
        ImmutableLHashSeparateKVCharByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMap(Map<Character, Byte> map1,
            Map<Character, Byte> map2, Map<Character, Byte> map3,
            Map<Character, Byte> map4, int expectedSize) {
        ImmutableLHashSeparateKVCharByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMap(Map<Character, Byte> map1,
            Map<Character, Byte> map2, Map<Character, Byte> map3,
            Map<Character, Byte> map4, Map<Character, Byte> map5, int expectedSize) {
        ImmutableLHashSeparateKVCharByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.CharByteConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashSeparateKVCharByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMap(char[] keys,
            byte[] values, int expectedSize) {
        ImmutableLHashSeparateKVCharByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMap(
            Character[] keys, Byte[] values, int expectedSize) {
        ImmutableLHashSeparateKVCharByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMap(Iterable<Character> keys,
            Iterable<Byte> values, int expectedSize) {
        ImmutableLHashSeparateKVCharByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMap(
            Map<Character, Byte> map) {
        ImmutableLHashSeparateKVCharByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMap(Map<Character, Byte> map1,
            Map<Character, Byte> map2) {
        ImmutableLHashSeparateKVCharByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMap(Map<Character, Byte> map1,
            Map<Character, Byte> map2, Map<Character, Byte> map3) {
        ImmutableLHashSeparateKVCharByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMap(Map<Character, Byte> map1,
            Map<Character, Byte> map2, Map<Character, Byte> map3,
            Map<Character, Byte> map4) {
        ImmutableLHashSeparateKVCharByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMap(Map<Character, Byte> map1,
            Map<Character, Byte> map2, Map<Character, Byte> map3,
            Map<Character, Byte> map4, Map<Character, Byte> map5) {
        ImmutableLHashSeparateKVCharByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.CharByteConsumer> entriesSupplier
            ) {
        ImmutableLHashSeparateKVCharByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMap(char[] keys,
            byte[] values) {
        ImmutableLHashSeparateKVCharByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMap(
            Character[] keys, Byte[] values) {
        ImmutableLHashSeparateKVCharByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMap(Iterable<Character> keys,
            Iterable<Byte> values) {
        ImmutableLHashSeparateKVCharByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMapOf(char k1, byte v1) {
        ImmutableLHashSeparateKVCharByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMapOf(char k1, byte v1,
             char k2, byte v2) {
        ImmutableLHashSeparateKVCharByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMapOf(char k1, byte v1,
             char k2, byte v2, char k3, byte v3) {
        ImmutableLHashSeparateKVCharByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMapOf(char k1, byte v1,
             char k2, byte v2, char k3, byte v3,
             char k4, byte v4) {
        ImmutableLHashSeparateKVCharByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharByteMap newImmutableMapOf(char k1, byte v1,
             char k2, byte v2, char k3, byte v3,
             char k4, byte v4, char k5, byte v5) {
        ImmutableLHashSeparateKVCharByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

