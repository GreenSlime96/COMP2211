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
import net.openhft.koloboke.collect.map.hash.HashIntCharMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashIntCharMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashSeparateKVIntCharMapFactoryGO
        extends LHashSeparateKVIntCharMapFactorySO {

    LHashSeparateKVIntCharMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashIntCharMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    abstract HashIntCharMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    abstract HashIntCharMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    @Override
    public final HashIntCharMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashIntCharMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashIntCharMapFactory withDomain(int lower, int upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashIntCharMapFactory withKeysDomain(int lower, int upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashIntCharMapFactory withKeysDomainComplement(int lower, int upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((int) (upper + 1), (int) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashIntCharMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashIntCharMapFactory) {
            HashIntCharMapFactory factory = (HashIntCharMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashSeparateKVIntCharMapGO shrunk(
            UpdatableLHashSeparateKVIntCharMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashSeparateKVIntCharMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMap(
            Map<Integer, Character> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMap(
            Map<Integer, Character> map1, Map<Integer, Character> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMap(
            Map<Integer, Character> map1, Map<Integer, Character> map2,
            Map<Integer, Character> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMap(
            Map<Integer, Character> map1, Map<Integer, Character> map2,
            Map<Integer, Character> map3, Map<Integer, Character> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMap(
            Map<Integer, Character> map1, Map<Integer, Character> map2,
            Map<Integer, Character> map3, Map<Integer, Character> map4,
            Map<Integer, Character> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMap(
            Map<Integer, Character> map1, Map<Integer, Character> map2,
            int expectedSize) {
        UpdatableLHashSeparateKVIntCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMap(
            Map<Integer, Character> map1, Map<Integer, Character> map2,
            Map<Integer, Character> map3, int expectedSize) {
        UpdatableLHashSeparateKVIntCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMap(
            Map<Integer, Character> map1, Map<Integer, Character> map2,
            Map<Integer, Character> map3, Map<Integer, Character> map4,
            int expectedSize) {
        UpdatableLHashSeparateKVIntCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMap(
            Map<Integer, Character> map1, Map<Integer, Character> map2,
            Map<Integer, Character> map3, Map<Integer, Character> map4,
            Map<Integer, Character> map5, int expectedSize) {
        UpdatableLHashSeparateKVIntCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.IntCharConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.IntCharConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashSeparateKVIntCharMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.IntCharConsumer() {
             @Override
             public void accept(int k, char v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMap(
            int[] keys, char[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMap(
            int[] keys, char[] values, int expectedSize) {
        UpdatableLHashSeparateKVIntCharMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMap(
            Integer[] keys, Character[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMap(
            Integer[] keys, Character[] values, int expectedSize) {
        UpdatableLHashSeparateKVIntCharMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMap(
            Iterable<Integer> keys, Iterable<Character> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMap(
            Iterable<Integer> keys, Iterable<Character> values, int expectedSize) {
        UpdatableLHashSeparateKVIntCharMapGO map = newUpdatableMap(expectedSize);
        Iterator<Integer> keysIt = keys.iterator();
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
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMapOf(
            int k1, char v1) {
        UpdatableLHashSeparateKVIntCharMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMapOf(
            int k1, char v1, int k2, char v2) {
        UpdatableLHashSeparateKVIntCharMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMapOf(
            int k1, char v1, int k2, char v2,
            int k3, char v3) {
        UpdatableLHashSeparateKVIntCharMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMapOf(
            int k1, char v1, int k2, char v2,
            int k3, char v3, int k4, char v4) {
        UpdatableLHashSeparateKVIntCharMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVIntCharMapGO newUpdatableMapOf(
            int k1, char v1, int k2, char v2,
            int k3, char v3, int k4, char v4,
            int k5, char v5) {
        UpdatableLHashSeparateKVIntCharMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashIntCharMap newMutableMap(Map<Integer, Character> map1,
            Map<Integer, Character> map2, int expectedSize) {
        MutableLHashSeparateKVIntCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newMutableMap(Map<Integer, Character> map1,
            Map<Integer, Character> map2, Map<Integer, Character> map3, int expectedSize) {
        MutableLHashSeparateKVIntCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newMutableMap(Map<Integer, Character> map1,
            Map<Integer, Character> map2, Map<Integer, Character> map3,
            Map<Integer, Character> map4, int expectedSize) {
        MutableLHashSeparateKVIntCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newMutableMap(Map<Integer, Character> map1,
            Map<Integer, Character> map2, Map<Integer, Character> map3,
            Map<Integer, Character> map4, Map<Integer, Character> map5, int expectedSize) {
        MutableLHashSeparateKVIntCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntCharMap newMutableMap(
            Consumer<net.openhft.koloboke.function.IntCharConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashSeparateKVIntCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newMutableMap(int[] keys,
            char[] values, int expectedSize) {
        MutableLHashSeparateKVIntCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newMutableMap(
            Integer[] keys, Character[] values, int expectedSize) {
        MutableLHashSeparateKVIntCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newMutableMap(Iterable<Integer> keys,
            Iterable<Character> values, int expectedSize) {
        MutableLHashSeparateKVIntCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashIntCharMap newMutableMap(
            Map<Integer, Character> map) {
        MutableLHashSeparateKVIntCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newMutableMap(Map<Integer, Character> map1,
            Map<Integer, Character> map2) {
        MutableLHashSeparateKVIntCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newMutableMap(Map<Integer, Character> map1,
            Map<Integer, Character> map2, Map<Integer, Character> map3) {
        MutableLHashSeparateKVIntCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newMutableMap(Map<Integer, Character> map1,
            Map<Integer, Character> map2, Map<Integer, Character> map3,
            Map<Integer, Character> map4) {
        MutableLHashSeparateKVIntCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newMutableMap(Map<Integer, Character> map1,
            Map<Integer, Character> map2, Map<Integer, Character> map3,
            Map<Integer, Character> map4, Map<Integer, Character> map5) {
        MutableLHashSeparateKVIntCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntCharMap newMutableMap(
            Consumer<net.openhft.koloboke.function.IntCharConsumer> entriesSupplier
            ) {
        MutableLHashSeparateKVIntCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newMutableMap(int[] keys,
            char[] values) {
        MutableLHashSeparateKVIntCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newMutableMap(
            Integer[] keys, Character[] values) {
        MutableLHashSeparateKVIntCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newMutableMap(Iterable<Integer> keys,
            Iterable<Character> values) {
        MutableLHashSeparateKVIntCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashIntCharMap newMutableMapOf(int k1, char v1) {
        MutableLHashSeparateKVIntCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newMutableMapOf(int k1, char v1,
             int k2, char v2) {
        MutableLHashSeparateKVIntCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newMutableMapOf(int k1, char v1,
             int k2, char v2, int k3, char v3) {
        MutableLHashSeparateKVIntCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newMutableMapOf(int k1, char v1,
             int k2, char v2, int k3, char v3,
             int k4, char v4) {
        MutableLHashSeparateKVIntCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newMutableMapOf(int k1, char v1,
             int k2, char v2, int k3, char v3,
             int k4, char v4, int k5, char v5) {
        MutableLHashSeparateKVIntCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMap(Map<Integer, Character> map1,
            Map<Integer, Character> map2, int expectedSize) {
        ImmutableLHashSeparateKVIntCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMap(Map<Integer, Character> map1,
            Map<Integer, Character> map2, Map<Integer, Character> map3, int expectedSize) {
        ImmutableLHashSeparateKVIntCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMap(Map<Integer, Character> map1,
            Map<Integer, Character> map2, Map<Integer, Character> map3,
            Map<Integer, Character> map4, int expectedSize) {
        ImmutableLHashSeparateKVIntCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMap(Map<Integer, Character> map1,
            Map<Integer, Character> map2, Map<Integer, Character> map3,
            Map<Integer, Character> map4, Map<Integer, Character> map5, int expectedSize) {
        ImmutableLHashSeparateKVIntCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.IntCharConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashSeparateKVIntCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMap(int[] keys,
            char[] values, int expectedSize) {
        ImmutableLHashSeparateKVIntCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMap(
            Integer[] keys, Character[] values, int expectedSize) {
        ImmutableLHashSeparateKVIntCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMap(Iterable<Integer> keys,
            Iterable<Character> values, int expectedSize) {
        ImmutableLHashSeparateKVIntCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMap(
            Map<Integer, Character> map) {
        ImmutableLHashSeparateKVIntCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMap(Map<Integer, Character> map1,
            Map<Integer, Character> map2) {
        ImmutableLHashSeparateKVIntCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMap(Map<Integer, Character> map1,
            Map<Integer, Character> map2, Map<Integer, Character> map3) {
        ImmutableLHashSeparateKVIntCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMap(Map<Integer, Character> map1,
            Map<Integer, Character> map2, Map<Integer, Character> map3,
            Map<Integer, Character> map4) {
        ImmutableLHashSeparateKVIntCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMap(Map<Integer, Character> map1,
            Map<Integer, Character> map2, Map<Integer, Character> map3,
            Map<Integer, Character> map4, Map<Integer, Character> map5) {
        ImmutableLHashSeparateKVIntCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.IntCharConsumer> entriesSupplier
            ) {
        ImmutableLHashSeparateKVIntCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMap(int[] keys,
            char[] values) {
        ImmutableLHashSeparateKVIntCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMap(
            Integer[] keys, Character[] values) {
        ImmutableLHashSeparateKVIntCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMap(Iterable<Integer> keys,
            Iterable<Character> values) {
        ImmutableLHashSeparateKVIntCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMapOf(int k1, char v1) {
        ImmutableLHashSeparateKVIntCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMapOf(int k1, char v1,
             int k2, char v2) {
        ImmutableLHashSeparateKVIntCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMapOf(int k1, char v1,
             int k2, char v2, int k3, char v3) {
        ImmutableLHashSeparateKVIntCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMapOf(int k1, char v1,
             int k2, char v2, int k3, char v3,
             int k4, char v4) {
        ImmutableLHashSeparateKVIntCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashIntCharMap newImmutableMapOf(int k1, char v1,
             int k2, char v2, int k3, char v3,
             int k4, char v4, int k5, char v5) {
        ImmutableLHashSeparateKVIntCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

