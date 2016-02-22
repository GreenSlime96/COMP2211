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
import net.openhft.koloboke.collect.map.hash.HashByteCharMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashByteCharMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashSeparateKVByteCharMapFactoryGO
        extends QHashSeparateKVByteCharMapFactorySO {

    QHashSeparateKVByteCharMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashByteCharMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper);

    abstract HashByteCharMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper);

    abstract HashByteCharMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper);

    @Override
    public final HashByteCharMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashByteCharMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashByteCharMapFactory withDomain(byte lower, byte upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashByteCharMapFactory withKeysDomain(byte lower, byte upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashByteCharMapFactory withKeysDomainComplement(byte lower, byte upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((byte) (upper + 1), (byte) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashByteCharMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashByteCharMapFactory) {
            HashByteCharMapFactory factory = (HashByteCharMapFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory) &&
                    // boxing to treat NaNs correctly
                   ((Character) getDefaultValue()).equals(factory.getDefaultValue())
                    ;
        } else {
            return false;
        }
    }

    @Override
    public char getDefaultValue() {
        return (char) 0;
    }

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashSeparateKVByteCharMapGO shrunk(
            UpdatableQHashSeparateKVByteCharMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashSeparateKVByteCharMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMap(
            Map<Byte, Character> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMap(
            Map<Byte, Character> map1, Map<Byte, Character> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMap(
            Map<Byte, Character> map1, Map<Byte, Character> map2,
            Map<Byte, Character> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMap(
            Map<Byte, Character> map1, Map<Byte, Character> map2,
            Map<Byte, Character> map3, Map<Byte, Character> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMap(
            Map<Byte, Character> map1, Map<Byte, Character> map2,
            Map<Byte, Character> map3, Map<Byte, Character> map4,
            Map<Byte, Character> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMap(
            Map<Byte, Character> map1, Map<Byte, Character> map2,
            int expectedSize) {
        UpdatableQHashSeparateKVByteCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMap(
            Map<Byte, Character> map1, Map<Byte, Character> map2,
            Map<Byte, Character> map3, int expectedSize) {
        UpdatableQHashSeparateKVByteCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMap(
            Map<Byte, Character> map1, Map<Byte, Character> map2,
            Map<Byte, Character> map3, Map<Byte, Character> map4,
            int expectedSize) {
        UpdatableQHashSeparateKVByteCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMap(
            Map<Byte, Character> map1, Map<Byte, Character> map2,
            Map<Byte, Character> map3, Map<Byte, Character> map4,
            Map<Byte, Character> map5, int expectedSize) {
        UpdatableQHashSeparateKVByteCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ByteCharConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ByteCharConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashSeparateKVByteCharMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.ByteCharConsumer() {
             @Override
             public void accept(byte k, char v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMap(
            byte[] keys, char[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMap(
            byte[] keys, char[] values, int expectedSize) {
        UpdatableQHashSeparateKVByteCharMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMap(
            Byte[] keys, Character[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMap(
            Byte[] keys, Character[] values, int expectedSize) {
        UpdatableQHashSeparateKVByteCharMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMap(
            Iterable<Byte> keys, Iterable<Character> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMap(
            Iterable<Byte> keys, Iterable<Character> values, int expectedSize) {
        UpdatableQHashSeparateKVByteCharMapGO map = newUpdatableMap(expectedSize);
        Iterator<Byte> keysIt = keys.iterator();
        Iterator<Character> valuesIt = values.iterator();
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
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMapOf(
            byte k1, char v1) {
        UpdatableQHashSeparateKVByteCharMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMapOf(
            byte k1, char v1, byte k2, char v2) {
        UpdatableQHashSeparateKVByteCharMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMapOf(
            byte k1, char v1, byte k2, char v2,
            byte k3, char v3) {
        UpdatableQHashSeparateKVByteCharMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMapOf(
            byte k1, char v1, byte k2, char v2,
            byte k3, char v3, byte k4, char v4) {
        UpdatableQHashSeparateKVByteCharMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteCharMapGO newUpdatableMapOf(
            byte k1, char v1, byte k2, char v2,
            byte k3, char v3, byte k4, char v4,
            byte k5, char v5) {
        UpdatableQHashSeparateKVByteCharMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashByteCharMap newMutableMap(Map<Byte, Character> map1,
            Map<Byte, Character> map2, int expectedSize) {
        MutableQHashSeparateKVByteCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newMutableMap(Map<Byte, Character> map1,
            Map<Byte, Character> map2, Map<Byte, Character> map3, int expectedSize) {
        MutableQHashSeparateKVByteCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newMutableMap(Map<Byte, Character> map1,
            Map<Byte, Character> map2, Map<Byte, Character> map3,
            Map<Byte, Character> map4, int expectedSize) {
        MutableQHashSeparateKVByteCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newMutableMap(Map<Byte, Character> map1,
            Map<Byte, Character> map2, Map<Byte, Character> map3,
            Map<Byte, Character> map4, Map<Byte, Character> map5, int expectedSize) {
        MutableQHashSeparateKVByteCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteCharMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ByteCharConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashSeparateKVByteCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newMutableMap(byte[] keys,
            char[] values, int expectedSize) {
        MutableQHashSeparateKVByteCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newMutableMap(
            Byte[] keys, Character[] values, int expectedSize) {
        MutableQHashSeparateKVByteCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newMutableMap(Iterable<Byte> keys,
            Iterable<Character> values, int expectedSize) {
        MutableQHashSeparateKVByteCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashByteCharMap newMutableMap(
            Map<Byte, Character> map) {
        MutableQHashSeparateKVByteCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newMutableMap(Map<Byte, Character> map1,
            Map<Byte, Character> map2) {
        MutableQHashSeparateKVByteCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newMutableMap(Map<Byte, Character> map1,
            Map<Byte, Character> map2, Map<Byte, Character> map3) {
        MutableQHashSeparateKVByteCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newMutableMap(Map<Byte, Character> map1,
            Map<Byte, Character> map2, Map<Byte, Character> map3,
            Map<Byte, Character> map4) {
        MutableQHashSeparateKVByteCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newMutableMap(Map<Byte, Character> map1,
            Map<Byte, Character> map2, Map<Byte, Character> map3,
            Map<Byte, Character> map4, Map<Byte, Character> map5) {
        MutableQHashSeparateKVByteCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteCharMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ByteCharConsumer> entriesSupplier
            ) {
        MutableQHashSeparateKVByteCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newMutableMap(byte[] keys,
            char[] values) {
        MutableQHashSeparateKVByteCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newMutableMap(
            Byte[] keys, Character[] values) {
        MutableQHashSeparateKVByteCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newMutableMap(Iterable<Byte> keys,
            Iterable<Character> values) {
        MutableQHashSeparateKVByteCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashByteCharMap newMutableMapOf(byte k1, char v1) {
        MutableQHashSeparateKVByteCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newMutableMapOf(byte k1, char v1,
             byte k2, char v2) {
        MutableQHashSeparateKVByteCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newMutableMapOf(byte k1, char v1,
             byte k2, char v2, byte k3, char v3) {
        MutableQHashSeparateKVByteCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newMutableMapOf(byte k1, char v1,
             byte k2, char v2, byte k3, char v3,
             byte k4, char v4) {
        MutableQHashSeparateKVByteCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newMutableMapOf(byte k1, char v1,
             byte k2, char v2, byte k3, char v3,
             byte k4, char v4, byte k5, char v5) {
        MutableQHashSeparateKVByteCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMap(Map<Byte, Character> map1,
            Map<Byte, Character> map2, int expectedSize) {
        ImmutableQHashSeparateKVByteCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMap(Map<Byte, Character> map1,
            Map<Byte, Character> map2, Map<Byte, Character> map3, int expectedSize) {
        ImmutableQHashSeparateKVByteCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMap(Map<Byte, Character> map1,
            Map<Byte, Character> map2, Map<Byte, Character> map3,
            Map<Byte, Character> map4, int expectedSize) {
        ImmutableQHashSeparateKVByteCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMap(Map<Byte, Character> map1,
            Map<Byte, Character> map2, Map<Byte, Character> map3,
            Map<Byte, Character> map4, Map<Byte, Character> map5, int expectedSize) {
        ImmutableQHashSeparateKVByteCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ByteCharConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashSeparateKVByteCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMap(byte[] keys,
            char[] values, int expectedSize) {
        ImmutableQHashSeparateKVByteCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMap(
            Byte[] keys, Character[] values, int expectedSize) {
        ImmutableQHashSeparateKVByteCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMap(Iterable<Byte> keys,
            Iterable<Character> values, int expectedSize) {
        ImmutableQHashSeparateKVByteCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMap(
            Map<Byte, Character> map) {
        ImmutableQHashSeparateKVByteCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMap(Map<Byte, Character> map1,
            Map<Byte, Character> map2) {
        ImmutableQHashSeparateKVByteCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMap(Map<Byte, Character> map1,
            Map<Byte, Character> map2, Map<Byte, Character> map3) {
        ImmutableQHashSeparateKVByteCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMap(Map<Byte, Character> map1,
            Map<Byte, Character> map2, Map<Byte, Character> map3,
            Map<Byte, Character> map4) {
        ImmutableQHashSeparateKVByteCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMap(Map<Byte, Character> map1,
            Map<Byte, Character> map2, Map<Byte, Character> map3,
            Map<Byte, Character> map4, Map<Byte, Character> map5) {
        ImmutableQHashSeparateKVByteCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ByteCharConsumer> entriesSupplier
            ) {
        ImmutableQHashSeparateKVByteCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMap(byte[] keys,
            char[] values) {
        ImmutableQHashSeparateKVByteCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMap(
            Byte[] keys, Character[] values) {
        ImmutableQHashSeparateKVByteCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMap(Iterable<Byte> keys,
            Iterable<Character> values) {
        ImmutableQHashSeparateKVByteCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMapOf(byte k1, char v1) {
        ImmutableQHashSeparateKVByteCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMapOf(byte k1, char v1,
             byte k2, char v2) {
        ImmutableQHashSeparateKVByteCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMapOf(byte k1, char v1,
             byte k2, char v2, byte k3, char v3) {
        ImmutableQHashSeparateKVByteCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMapOf(byte k1, char v1,
             byte k2, char v2, byte k3, char v3,
             byte k4, char v4) {
        ImmutableQHashSeparateKVByteCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteCharMap newImmutableMapOf(byte k1, char v1,
             byte k2, char v2, byte k3, char v3,
             byte k4, char v4, byte k5, char v5) {
        ImmutableQHashSeparateKVByteCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

