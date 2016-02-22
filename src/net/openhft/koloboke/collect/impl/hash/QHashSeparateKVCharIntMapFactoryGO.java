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
import net.openhft.koloboke.collect.map.hash.HashCharIntMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashCharIntMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashSeparateKVCharIntMapFactoryGO
        extends QHashSeparateKVCharIntMapFactorySO {

    QHashSeparateKVCharIntMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashCharIntMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper);

    abstract HashCharIntMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper);

    abstract HashCharIntMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper);

    @Override
    public final HashCharIntMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashCharIntMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashCharIntMapFactory withDomain(char lower, char upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashCharIntMapFactory withKeysDomain(char lower, char upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashCharIntMapFactory withKeysDomainComplement(char lower, char upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((char) (upper + 1), (char) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashCharIntMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashCharIntMapFactory) {
            HashCharIntMapFactory factory = (HashCharIntMapFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory) &&
                    // boxing to treat NaNs correctly
                   ((Integer) getDefaultValue()).equals(factory.getDefaultValue())
                    ;
        } else {
            return false;
        }
    }

    @Override
    public int getDefaultValue() {
        return 0;
    }

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashSeparateKVCharIntMapGO shrunk(
            UpdatableQHashSeparateKVCharIntMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashSeparateKVCharIntMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMap(
            Map<Character, Integer> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMap(
            Map<Character, Integer> map1, Map<Character, Integer> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMap(
            Map<Character, Integer> map1, Map<Character, Integer> map2,
            Map<Character, Integer> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMap(
            Map<Character, Integer> map1, Map<Character, Integer> map2,
            Map<Character, Integer> map3, Map<Character, Integer> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMap(
            Map<Character, Integer> map1, Map<Character, Integer> map2,
            Map<Character, Integer> map3, Map<Character, Integer> map4,
            Map<Character, Integer> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMap(
            Map<Character, Integer> map1, Map<Character, Integer> map2,
            int expectedSize) {
        UpdatableQHashSeparateKVCharIntMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMap(
            Map<Character, Integer> map1, Map<Character, Integer> map2,
            Map<Character, Integer> map3, int expectedSize) {
        UpdatableQHashSeparateKVCharIntMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMap(
            Map<Character, Integer> map1, Map<Character, Integer> map2,
            Map<Character, Integer> map3, Map<Character, Integer> map4,
            int expectedSize) {
        UpdatableQHashSeparateKVCharIntMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMap(
            Map<Character, Integer> map1, Map<Character, Integer> map2,
            Map<Character, Integer> map3, Map<Character, Integer> map4,
            Map<Character, Integer> map5, int expectedSize) {
        UpdatableQHashSeparateKVCharIntMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.CharIntConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.CharIntConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashSeparateKVCharIntMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.CharIntConsumer() {
             @Override
             public void accept(char k, int v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMap(
            char[] keys, int[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMap(
            char[] keys, int[] values, int expectedSize) {
        UpdatableQHashSeparateKVCharIntMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMap(
            Character[] keys, Integer[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMap(
            Character[] keys, Integer[] values, int expectedSize) {
        UpdatableQHashSeparateKVCharIntMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMap(
            Iterable<Character> keys, Iterable<Integer> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMap(
            Iterable<Character> keys, Iterable<Integer> values, int expectedSize) {
        UpdatableQHashSeparateKVCharIntMapGO map = newUpdatableMap(expectedSize);
        Iterator<Character> keysIt = keys.iterator();
        Iterator<Integer> valuesIt = values.iterator();
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
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMapOf(
            char k1, int v1) {
        UpdatableQHashSeparateKVCharIntMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMapOf(
            char k1, int v1, char k2, int v2) {
        UpdatableQHashSeparateKVCharIntMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMapOf(
            char k1, int v1, char k2, int v2,
            char k3, int v3) {
        UpdatableQHashSeparateKVCharIntMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMapOf(
            char k1, int v1, char k2, int v2,
            char k3, int v3, char k4, int v4) {
        UpdatableQHashSeparateKVCharIntMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVCharIntMapGO newUpdatableMapOf(
            char k1, int v1, char k2, int v2,
            char k3, int v3, char k4, int v4,
            char k5, int v5) {
        UpdatableQHashSeparateKVCharIntMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashCharIntMap newMutableMap(Map<Character, Integer> map1,
            Map<Character, Integer> map2, int expectedSize) {
        MutableQHashSeparateKVCharIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newMutableMap(Map<Character, Integer> map1,
            Map<Character, Integer> map2, Map<Character, Integer> map3, int expectedSize) {
        MutableQHashSeparateKVCharIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newMutableMap(Map<Character, Integer> map1,
            Map<Character, Integer> map2, Map<Character, Integer> map3,
            Map<Character, Integer> map4, int expectedSize) {
        MutableQHashSeparateKVCharIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newMutableMap(Map<Character, Integer> map1,
            Map<Character, Integer> map2, Map<Character, Integer> map3,
            Map<Character, Integer> map4, Map<Character, Integer> map5, int expectedSize) {
        MutableQHashSeparateKVCharIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharIntMap newMutableMap(
            Consumer<net.openhft.koloboke.function.CharIntConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashSeparateKVCharIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newMutableMap(char[] keys,
            int[] values, int expectedSize) {
        MutableQHashSeparateKVCharIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newMutableMap(
            Character[] keys, Integer[] values, int expectedSize) {
        MutableQHashSeparateKVCharIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newMutableMap(Iterable<Character> keys,
            Iterable<Integer> values, int expectedSize) {
        MutableQHashSeparateKVCharIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashCharIntMap newMutableMap(
            Map<Character, Integer> map) {
        MutableQHashSeparateKVCharIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newMutableMap(Map<Character, Integer> map1,
            Map<Character, Integer> map2) {
        MutableQHashSeparateKVCharIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newMutableMap(Map<Character, Integer> map1,
            Map<Character, Integer> map2, Map<Character, Integer> map3) {
        MutableQHashSeparateKVCharIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newMutableMap(Map<Character, Integer> map1,
            Map<Character, Integer> map2, Map<Character, Integer> map3,
            Map<Character, Integer> map4) {
        MutableQHashSeparateKVCharIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newMutableMap(Map<Character, Integer> map1,
            Map<Character, Integer> map2, Map<Character, Integer> map3,
            Map<Character, Integer> map4, Map<Character, Integer> map5) {
        MutableQHashSeparateKVCharIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharIntMap newMutableMap(
            Consumer<net.openhft.koloboke.function.CharIntConsumer> entriesSupplier
            ) {
        MutableQHashSeparateKVCharIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newMutableMap(char[] keys,
            int[] values) {
        MutableQHashSeparateKVCharIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newMutableMap(
            Character[] keys, Integer[] values) {
        MutableQHashSeparateKVCharIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newMutableMap(Iterable<Character> keys,
            Iterable<Integer> values) {
        MutableQHashSeparateKVCharIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashCharIntMap newMutableMapOf(char k1, int v1) {
        MutableQHashSeparateKVCharIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newMutableMapOf(char k1, int v1,
             char k2, int v2) {
        MutableQHashSeparateKVCharIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newMutableMapOf(char k1, int v1,
             char k2, int v2, char k3, int v3) {
        MutableQHashSeparateKVCharIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newMutableMapOf(char k1, int v1,
             char k2, int v2, char k3, int v3,
             char k4, int v4) {
        MutableQHashSeparateKVCharIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newMutableMapOf(char k1, int v1,
             char k2, int v2, char k3, int v3,
             char k4, int v4, char k5, int v5) {
        MutableQHashSeparateKVCharIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMap(Map<Character, Integer> map1,
            Map<Character, Integer> map2, int expectedSize) {
        ImmutableQHashSeparateKVCharIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMap(Map<Character, Integer> map1,
            Map<Character, Integer> map2, Map<Character, Integer> map3, int expectedSize) {
        ImmutableQHashSeparateKVCharIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMap(Map<Character, Integer> map1,
            Map<Character, Integer> map2, Map<Character, Integer> map3,
            Map<Character, Integer> map4, int expectedSize) {
        ImmutableQHashSeparateKVCharIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMap(Map<Character, Integer> map1,
            Map<Character, Integer> map2, Map<Character, Integer> map3,
            Map<Character, Integer> map4, Map<Character, Integer> map5, int expectedSize) {
        ImmutableQHashSeparateKVCharIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.CharIntConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashSeparateKVCharIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMap(char[] keys,
            int[] values, int expectedSize) {
        ImmutableQHashSeparateKVCharIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMap(
            Character[] keys, Integer[] values, int expectedSize) {
        ImmutableQHashSeparateKVCharIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMap(Iterable<Character> keys,
            Iterable<Integer> values, int expectedSize) {
        ImmutableQHashSeparateKVCharIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMap(
            Map<Character, Integer> map) {
        ImmutableQHashSeparateKVCharIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMap(Map<Character, Integer> map1,
            Map<Character, Integer> map2) {
        ImmutableQHashSeparateKVCharIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMap(Map<Character, Integer> map1,
            Map<Character, Integer> map2, Map<Character, Integer> map3) {
        ImmutableQHashSeparateKVCharIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMap(Map<Character, Integer> map1,
            Map<Character, Integer> map2, Map<Character, Integer> map3,
            Map<Character, Integer> map4) {
        ImmutableQHashSeparateKVCharIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMap(Map<Character, Integer> map1,
            Map<Character, Integer> map2, Map<Character, Integer> map3,
            Map<Character, Integer> map4, Map<Character, Integer> map5) {
        ImmutableQHashSeparateKVCharIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.CharIntConsumer> entriesSupplier
            ) {
        ImmutableQHashSeparateKVCharIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMap(char[] keys,
            int[] values) {
        ImmutableQHashSeparateKVCharIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMap(
            Character[] keys, Integer[] values) {
        ImmutableQHashSeparateKVCharIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMap(Iterable<Character> keys,
            Iterable<Integer> values) {
        ImmutableQHashSeparateKVCharIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMapOf(char k1, int v1) {
        ImmutableQHashSeparateKVCharIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMapOf(char k1, int v1,
             char k2, int v2) {
        ImmutableQHashSeparateKVCharIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMapOf(char k1, int v1,
             char k2, int v2, char k3, int v3) {
        ImmutableQHashSeparateKVCharIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMapOf(char k1, int v1,
             char k2, int v2, char k3, int v3,
             char k4, int v4) {
        ImmutableQHashSeparateKVCharIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharIntMap newImmutableMapOf(char k1, int v1,
             char k2, int v2, char k3, int v3,
             char k4, int v4, char k5, int v5) {
        ImmutableQHashSeparateKVCharIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

