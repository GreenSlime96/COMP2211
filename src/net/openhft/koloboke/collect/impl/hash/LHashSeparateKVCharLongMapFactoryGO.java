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
import net.openhft.koloboke.collect.map.hash.HashCharLongMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashCharLongMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashSeparateKVCharLongMapFactoryGO
        extends LHashSeparateKVCharLongMapFactorySO {

    LHashSeparateKVCharLongMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashCharLongMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper);

    abstract HashCharLongMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper);

    abstract HashCharLongMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper);

    @Override
    public final HashCharLongMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashCharLongMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashCharLongMapFactory withDomain(char lower, char upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashCharLongMapFactory withKeysDomain(char lower, char upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashCharLongMapFactory withKeysDomainComplement(char lower, char upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((char) (upper + 1), (char) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashCharLongMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashCharLongMapFactory) {
            HashCharLongMapFactory factory = (HashCharLongMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashSeparateKVCharLongMapGO shrunk(
            UpdatableLHashSeparateKVCharLongMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashSeparateKVCharLongMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMap(
            Map<Character, Long> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMap(
            Map<Character, Long> map1, Map<Character, Long> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMap(
            Map<Character, Long> map1, Map<Character, Long> map2,
            Map<Character, Long> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMap(
            Map<Character, Long> map1, Map<Character, Long> map2,
            Map<Character, Long> map3, Map<Character, Long> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMap(
            Map<Character, Long> map1, Map<Character, Long> map2,
            Map<Character, Long> map3, Map<Character, Long> map4,
            Map<Character, Long> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMap(
            Map<Character, Long> map1, Map<Character, Long> map2,
            int expectedSize) {
        UpdatableLHashSeparateKVCharLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMap(
            Map<Character, Long> map1, Map<Character, Long> map2,
            Map<Character, Long> map3, int expectedSize) {
        UpdatableLHashSeparateKVCharLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMap(
            Map<Character, Long> map1, Map<Character, Long> map2,
            Map<Character, Long> map3, Map<Character, Long> map4,
            int expectedSize) {
        UpdatableLHashSeparateKVCharLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMap(
            Map<Character, Long> map1, Map<Character, Long> map2,
            Map<Character, Long> map3, Map<Character, Long> map4,
            Map<Character, Long> map5, int expectedSize) {
        UpdatableLHashSeparateKVCharLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.CharLongConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.CharLongConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashSeparateKVCharLongMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.CharLongConsumer() {
             @Override
             public void accept(char k, long v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMap(
            char[] keys, long[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMap(
            char[] keys, long[] values, int expectedSize) {
        UpdatableLHashSeparateKVCharLongMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMap(
            Character[] keys, Long[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMap(
            Character[] keys, Long[] values, int expectedSize) {
        UpdatableLHashSeparateKVCharLongMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMap(
            Iterable<Character> keys, Iterable<Long> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMap(
            Iterable<Character> keys, Iterable<Long> values, int expectedSize) {
        UpdatableLHashSeparateKVCharLongMapGO map = newUpdatableMap(expectedSize);
        Iterator<Character> keysIt = keys.iterator();
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
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMapOf(
            char k1, long v1) {
        UpdatableLHashSeparateKVCharLongMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMapOf(
            char k1, long v1, char k2, long v2) {
        UpdatableLHashSeparateKVCharLongMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMapOf(
            char k1, long v1, char k2, long v2,
            char k3, long v3) {
        UpdatableLHashSeparateKVCharLongMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMapOf(
            char k1, long v1, char k2, long v2,
            char k3, long v3, char k4, long v4) {
        UpdatableLHashSeparateKVCharLongMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharLongMapGO newUpdatableMapOf(
            char k1, long v1, char k2, long v2,
            char k3, long v3, char k4, long v4,
            char k5, long v5) {
        UpdatableLHashSeparateKVCharLongMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashCharLongMap newMutableMap(Map<Character, Long> map1,
            Map<Character, Long> map2, int expectedSize) {
        MutableLHashSeparateKVCharLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newMutableMap(Map<Character, Long> map1,
            Map<Character, Long> map2, Map<Character, Long> map3, int expectedSize) {
        MutableLHashSeparateKVCharLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newMutableMap(Map<Character, Long> map1,
            Map<Character, Long> map2, Map<Character, Long> map3,
            Map<Character, Long> map4, int expectedSize) {
        MutableLHashSeparateKVCharLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newMutableMap(Map<Character, Long> map1,
            Map<Character, Long> map2, Map<Character, Long> map3,
            Map<Character, Long> map4, Map<Character, Long> map5, int expectedSize) {
        MutableLHashSeparateKVCharLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharLongMap newMutableMap(
            Consumer<net.openhft.koloboke.function.CharLongConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashSeparateKVCharLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newMutableMap(char[] keys,
            long[] values, int expectedSize) {
        MutableLHashSeparateKVCharLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newMutableMap(
            Character[] keys, Long[] values, int expectedSize) {
        MutableLHashSeparateKVCharLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newMutableMap(Iterable<Character> keys,
            Iterable<Long> values, int expectedSize) {
        MutableLHashSeparateKVCharLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashCharLongMap newMutableMap(
            Map<Character, Long> map) {
        MutableLHashSeparateKVCharLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newMutableMap(Map<Character, Long> map1,
            Map<Character, Long> map2) {
        MutableLHashSeparateKVCharLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newMutableMap(Map<Character, Long> map1,
            Map<Character, Long> map2, Map<Character, Long> map3) {
        MutableLHashSeparateKVCharLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newMutableMap(Map<Character, Long> map1,
            Map<Character, Long> map2, Map<Character, Long> map3,
            Map<Character, Long> map4) {
        MutableLHashSeparateKVCharLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newMutableMap(Map<Character, Long> map1,
            Map<Character, Long> map2, Map<Character, Long> map3,
            Map<Character, Long> map4, Map<Character, Long> map5) {
        MutableLHashSeparateKVCharLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharLongMap newMutableMap(
            Consumer<net.openhft.koloboke.function.CharLongConsumer> entriesSupplier
            ) {
        MutableLHashSeparateKVCharLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newMutableMap(char[] keys,
            long[] values) {
        MutableLHashSeparateKVCharLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newMutableMap(
            Character[] keys, Long[] values) {
        MutableLHashSeparateKVCharLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newMutableMap(Iterable<Character> keys,
            Iterable<Long> values) {
        MutableLHashSeparateKVCharLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashCharLongMap newMutableMapOf(char k1, long v1) {
        MutableLHashSeparateKVCharLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newMutableMapOf(char k1, long v1,
             char k2, long v2) {
        MutableLHashSeparateKVCharLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newMutableMapOf(char k1, long v1,
             char k2, long v2, char k3, long v3) {
        MutableLHashSeparateKVCharLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newMutableMapOf(char k1, long v1,
             char k2, long v2, char k3, long v3,
             char k4, long v4) {
        MutableLHashSeparateKVCharLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newMutableMapOf(char k1, long v1,
             char k2, long v2, char k3, long v3,
             char k4, long v4, char k5, long v5) {
        MutableLHashSeparateKVCharLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMap(Map<Character, Long> map1,
            Map<Character, Long> map2, int expectedSize) {
        ImmutableLHashSeparateKVCharLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMap(Map<Character, Long> map1,
            Map<Character, Long> map2, Map<Character, Long> map3, int expectedSize) {
        ImmutableLHashSeparateKVCharLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMap(Map<Character, Long> map1,
            Map<Character, Long> map2, Map<Character, Long> map3,
            Map<Character, Long> map4, int expectedSize) {
        ImmutableLHashSeparateKVCharLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMap(Map<Character, Long> map1,
            Map<Character, Long> map2, Map<Character, Long> map3,
            Map<Character, Long> map4, Map<Character, Long> map5, int expectedSize) {
        ImmutableLHashSeparateKVCharLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.CharLongConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashSeparateKVCharLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMap(char[] keys,
            long[] values, int expectedSize) {
        ImmutableLHashSeparateKVCharLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMap(
            Character[] keys, Long[] values, int expectedSize) {
        ImmutableLHashSeparateKVCharLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMap(Iterable<Character> keys,
            Iterable<Long> values, int expectedSize) {
        ImmutableLHashSeparateKVCharLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMap(
            Map<Character, Long> map) {
        ImmutableLHashSeparateKVCharLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMap(Map<Character, Long> map1,
            Map<Character, Long> map2) {
        ImmutableLHashSeparateKVCharLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMap(Map<Character, Long> map1,
            Map<Character, Long> map2, Map<Character, Long> map3) {
        ImmutableLHashSeparateKVCharLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMap(Map<Character, Long> map1,
            Map<Character, Long> map2, Map<Character, Long> map3,
            Map<Character, Long> map4) {
        ImmutableLHashSeparateKVCharLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMap(Map<Character, Long> map1,
            Map<Character, Long> map2, Map<Character, Long> map3,
            Map<Character, Long> map4, Map<Character, Long> map5) {
        ImmutableLHashSeparateKVCharLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.CharLongConsumer> entriesSupplier
            ) {
        ImmutableLHashSeparateKVCharLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMap(char[] keys,
            long[] values) {
        ImmutableLHashSeparateKVCharLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMap(
            Character[] keys, Long[] values) {
        ImmutableLHashSeparateKVCharLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMap(Iterable<Character> keys,
            Iterable<Long> values) {
        ImmutableLHashSeparateKVCharLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMapOf(char k1, long v1) {
        ImmutableLHashSeparateKVCharLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMapOf(char k1, long v1,
             char k2, long v2) {
        ImmutableLHashSeparateKVCharLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMapOf(char k1, long v1,
             char k2, long v2, char k3, long v3) {
        ImmutableLHashSeparateKVCharLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMapOf(char k1, long v1,
             char k2, long v2, char k3, long v3,
             char k4, long v4) {
        ImmutableLHashSeparateKVCharLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharLongMap newImmutableMapOf(char k1, long v1,
             char k2, long v2, char k3, long v3,
             char k4, long v4, char k5, long v5) {
        ImmutableLHashSeparateKVCharLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

