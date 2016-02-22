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
import net.openhft.koloboke.collect.map.hash.HashLongCharMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashLongCharMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashSeparateKVLongCharMapFactoryGO
        extends QHashSeparateKVLongCharMapFactorySO {

    QHashSeparateKVLongCharMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashLongCharMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper);

    abstract HashLongCharMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper);

    abstract HashLongCharMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper);

    @Override
    public final HashLongCharMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashLongCharMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashLongCharMapFactory withDomain(long lower, long upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashLongCharMapFactory withKeysDomain(long lower, long upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashLongCharMapFactory withKeysDomainComplement(long lower, long upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((long) (upper + 1), (long) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashLongCharMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashLongCharMapFactory) {
            HashLongCharMapFactory factory = (HashLongCharMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashSeparateKVLongCharMapGO shrunk(
            UpdatableQHashSeparateKVLongCharMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashSeparateKVLongCharMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMap(
            Map<Long, Character> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMap(
            Map<Long, Character> map1, Map<Long, Character> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMap(
            Map<Long, Character> map1, Map<Long, Character> map2,
            Map<Long, Character> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMap(
            Map<Long, Character> map1, Map<Long, Character> map2,
            Map<Long, Character> map3, Map<Long, Character> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMap(
            Map<Long, Character> map1, Map<Long, Character> map2,
            Map<Long, Character> map3, Map<Long, Character> map4,
            Map<Long, Character> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMap(
            Map<Long, Character> map1, Map<Long, Character> map2,
            int expectedSize) {
        UpdatableQHashSeparateKVLongCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMap(
            Map<Long, Character> map1, Map<Long, Character> map2,
            Map<Long, Character> map3, int expectedSize) {
        UpdatableQHashSeparateKVLongCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMap(
            Map<Long, Character> map1, Map<Long, Character> map2,
            Map<Long, Character> map3, Map<Long, Character> map4,
            int expectedSize) {
        UpdatableQHashSeparateKVLongCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMap(
            Map<Long, Character> map1, Map<Long, Character> map2,
            Map<Long, Character> map3, Map<Long, Character> map4,
            Map<Long, Character> map5, int expectedSize) {
        UpdatableQHashSeparateKVLongCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.LongCharConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.LongCharConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashSeparateKVLongCharMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.LongCharConsumer() {
             @Override
             public void accept(long k, char v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMap(
            long[] keys, char[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMap(
            long[] keys, char[] values, int expectedSize) {
        UpdatableQHashSeparateKVLongCharMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMap(
            Long[] keys, Character[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMap(
            Long[] keys, Character[] values, int expectedSize) {
        UpdatableQHashSeparateKVLongCharMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMap(
            Iterable<Long> keys, Iterable<Character> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMap(
            Iterable<Long> keys, Iterable<Character> values, int expectedSize) {
        UpdatableQHashSeparateKVLongCharMapGO map = newUpdatableMap(expectedSize);
        Iterator<Long> keysIt = keys.iterator();
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
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMapOf(
            long k1, char v1) {
        UpdatableQHashSeparateKVLongCharMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMapOf(
            long k1, char v1, long k2, char v2) {
        UpdatableQHashSeparateKVLongCharMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMapOf(
            long k1, char v1, long k2, char v2,
            long k3, char v3) {
        UpdatableQHashSeparateKVLongCharMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMapOf(
            long k1, char v1, long k2, char v2,
            long k3, char v3, long k4, char v4) {
        UpdatableQHashSeparateKVLongCharMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVLongCharMapGO newUpdatableMapOf(
            long k1, char v1, long k2, char v2,
            long k3, char v3, long k4, char v4,
            long k5, char v5) {
        UpdatableQHashSeparateKVLongCharMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashLongCharMap newMutableMap(Map<Long, Character> map1,
            Map<Long, Character> map2, int expectedSize) {
        MutableQHashSeparateKVLongCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newMutableMap(Map<Long, Character> map1,
            Map<Long, Character> map2, Map<Long, Character> map3, int expectedSize) {
        MutableQHashSeparateKVLongCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newMutableMap(Map<Long, Character> map1,
            Map<Long, Character> map2, Map<Long, Character> map3,
            Map<Long, Character> map4, int expectedSize) {
        MutableQHashSeparateKVLongCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newMutableMap(Map<Long, Character> map1,
            Map<Long, Character> map2, Map<Long, Character> map3,
            Map<Long, Character> map4, Map<Long, Character> map5, int expectedSize) {
        MutableQHashSeparateKVLongCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongCharMap newMutableMap(
            Consumer<net.openhft.koloboke.function.LongCharConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashSeparateKVLongCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newMutableMap(long[] keys,
            char[] values, int expectedSize) {
        MutableQHashSeparateKVLongCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newMutableMap(
            Long[] keys, Character[] values, int expectedSize) {
        MutableQHashSeparateKVLongCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newMutableMap(Iterable<Long> keys,
            Iterable<Character> values, int expectedSize) {
        MutableQHashSeparateKVLongCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashLongCharMap newMutableMap(
            Map<Long, Character> map) {
        MutableQHashSeparateKVLongCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newMutableMap(Map<Long, Character> map1,
            Map<Long, Character> map2) {
        MutableQHashSeparateKVLongCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newMutableMap(Map<Long, Character> map1,
            Map<Long, Character> map2, Map<Long, Character> map3) {
        MutableQHashSeparateKVLongCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newMutableMap(Map<Long, Character> map1,
            Map<Long, Character> map2, Map<Long, Character> map3,
            Map<Long, Character> map4) {
        MutableQHashSeparateKVLongCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newMutableMap(Map<Long, Character> map1,
            Map<Long, Character> map2, Map<Long, Character> map3,
            Map<Long, Character> map4, Map<Long, Character> map5) {
        MutableQHashSeparateKVLongCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongCharMap newMutableMap(
            Consumer<net.openhft.koloboke.function.LongCharConsumer> entriesSupplier
            ) {
        MutableQHashSeparateKVLongCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newMutableMap(long[] keys,
            char[] values) {
        MutableQHashSeparateKVLongCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newMutableMap(
            Long[] keys, Character[] values) {
        MutableQHashSeparateKVLongCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newMutableMap(Iterable<Long> keys,
            Iterable<Character> values) {
        MutableQHashSeparateKVLongCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashLongCharMap newMutableMapOf(long k1, char v1) {
        MutableQHashSeparateKVLongCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newMutableMapOf(long k1, char v1,
             long k2, char v2) {
        MutableQHashSeparateKVLongCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newMutableMapOf(long k1, char v1,
             long k2, char v2, long k3, char v3) {
        MutableQHashSeparateKVLongCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newMutableMapOf(long k1, char v1,
             long k2, char v2, long k3, char v3,
             long k4, char v4) {
        MutableQHashSeparateKVLongCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newMutableMapOf(long k1, char v1,
             long k2, char v2, long k3, char v3,
             long k4, char v4, long k5, char v5) {
        MutableQHashSeparateKVLongCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMap(Map<Long, Character> map1,
            Map<Long, Character> map2, int expectedSize) {
        ImmutableQHashSeparateKVLongCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMap(Map<Long, Character> map1,
            Map<Long, Character> map2, Map<Long, Character> map3, int expectedSize) {
        ImmutableQHashSeparateKVLongCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMap(Map<Long, Character> map1,
            Map<Long, Character> map2, Map<Long, Character> map3,
            Map<Long, Character> map4, int expectedSize) {
        ImmutableQHashSeparateKVLongCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMap(Map<Long, Character> map1,
            Map<Long, Character> map2, Map<Long, Character> map3,
            Map<Long, Character> map4, Map<Long, Character> map5, int expectedSize) {
        ImmutableQHashSeparateKVLongCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.LongCharConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashSeparateKVLongCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMap(long[] keys,
            char[] values, int expectedSize) {
        ImmutableQHashSeparateKVLongCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMap(
            Long[] keys, Character[] values, int expectedSize) {
        ImmutableQHashSeparateKVLongCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMap(Iterable<Long> keys,
            Iterable<Character> values, int expectedSize) {
        ImmutableQHashSeparateKVLongCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMap(
            Map<Long, Character> map) {
        ImmutableQHashSeparateKVLongCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMap(Map<Long, Character> map1,
            Map<Long, Character> map2) {
        ImmutableQHashSeparateKVLongCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMap(Map<Long, Character> map1,
            Map<Long, Character> map2, Map<Long, Character> map3) {
        ImmutableQHashSeparateKVLongCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMap(Map<Long, Character> map1,
            Map<Long, Character> map2, Map<Long, Character> map3,
            Map<Long, Character> map4) {
        ImmutableQHashSeparateKVLongCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMap(Map<Long, Character> map1,
            Map<Long, Character> map2, Map<Long, Character> map3,
            Map<Long, Character> map4, Map<Long, Character> map5) {
        ImmutableQHashSeparateKVLongCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.LongCharConsumer> entriesSupplier
            ) {
        ImmutableQHashSeparateKVLongCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMap(long[] keys,
            char[] values) {
        ImmutableQHashSeparateKVLongCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMap(
            Long[] keys, Character[] values) {
        ImmutableQHashSeparateKVLongCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMap(Iterable<Long> keys,
            Iterable<Character> values) {
        ImmutableQHashSeparateKVLongCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMapOf(long k1, char v1) {
        ImmutableQHashSeparateKVLongCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMapOf(long k1, char v1,
             long k2, char v2) {
        ImmutableQHashSeparateKVLongCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMapOf(long k1, char v1,
             long k2, char v2, long k3, char v3) {
        ImmutableQHashSeparateKVLongCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMapOf(long k1, char v1,
             long k2, char v2, long k3, char v3,
             long k4, char v4) {
        ImmutableQHashSeparateKVLongCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongCharMap newImmutableMapOf(long k1, char v1,
             long k2, char v2, long k3, char v3,
             long k4, char v4, long k5, char v5) {
        ImmutableQHashSeparateKVLongCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

