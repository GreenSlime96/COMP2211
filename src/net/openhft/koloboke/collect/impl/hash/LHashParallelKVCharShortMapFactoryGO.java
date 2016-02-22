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
import net.openhft.koloboke.collect.map.hash.HashCharShortMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashCharShortMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashParallelKVCharShortMapFactoryGO
        extends LHashParallelKVCharShortMapFactorySO {

    LHashParallelKVCharShortMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashCharShortMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper);

    abstract HashCharShortMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper);

    abstract HashCharShortMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper);

    @Override
    public final HashCharShortMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashCharShortMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashCharShortMapFactory withDomain(char lower, char upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashCharShortMapFactory withKeysDomain(char lower, char upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashCharShortMapFactory withKeysDomainComplement(char lower, char upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((char) (upper + 1), (char) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashCharShortMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashCharShortMapFactory) {
            HashCharShortMapFactory factory = (HashCharShortMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashParallelKVCharShortMapGO shrunk(
            UpdatableLHashParallelKVCharShortMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashParallelKVCharShortMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMap(
            Map<Character, Short> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMap(
            Map<Character, Short> map1, Map<Character, Short> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMap(
            Map<Character, Short> map1, Map<Character, Short> map2,
            Map<Character, Short> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMap(
            Map<Character, Short> map1, Map<Character, Short> map2,
            Map<Character, Short> map3, Map<Character, Short> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMap(
            Map<Character, Short> map1, Map<Character, Short> map2,
            Map<Character, Short> map3, Map<Character, Short> map4,
            Map<Character, Short> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMap(
            Map<Character, Short> map1, Map<Character, Short> map2,
            int expectedSize) {
        UpdatableLHashParallelKVCharShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMap(
            Map<Character, Short> map1, Map<Character, Short> map2,
            Map<Character, Short> map3, int expectedSize) {
        UpdatableLHashParallelKVCharShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMap(
            Map<Character, Short> map1, Map<Character, Short> map2,
            Map<Character, Short> map3, Map<Character, Short> map4,
            int expectedSize) {
        UpdatableLHashParallelKVCharShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMap(
            Map<Character, Short> map1, Map<Character, Short> map2,
            Map<Character, Short> map3, Map<Character, Short> map4,
            Map<Character, Short> map5, int expectedSize) {
        UpdatableLHashParallelKVCharShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.CharShortConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.CharShortConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashParallelKVCharShortMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.CharShortConsumer() {
             @Override
             public void accept(char k, short v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMap(
            char[] keys, short[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMap(
            char[] keys, short[] values, int expectedSize) {
        UpdatableLHashParallelKVCharShortMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMap(
            Character[] keys, Short[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMap(
            Character[] keys, Short[] values, int expectedSize) {
        UpdatableLHashParallelKVCharShortMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMap(
            Iterable<Character> keys, Iterable<Short> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMap(
            Iterable<Character> keys, Iterable<Short> values, int expectedSize) {
        UpdatableLHashParallelKVCharShortMapGO map = newUpdatableMap(expectedSize);
        Iterator<Character> keysIt = keys.iterator();
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
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMapOf(
            char k1, short v1) {
        UpdatableLHashParallelKVCharShortMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMapOf(
            char k1, short v1, char k2, short v2) {
        UpdatableLHashParallelKVCharShortMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMapOf(
            char k1, short v1, char k2, short v2,
            char k3, short v3) {
        UpdatableLHashParallelKVCharShortMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMapOf(
            char k1, short v1, char k2, short v2,
            char k3, short v3, char k4, short v4) {
        UpdatableLHashParallelKVCharShortMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharShortMapGO newUpdatableMapOf(
            char k1, short v1, char k2, short v2,
            char k3, short v3, char k4, short v4,
            char k5, short v5) {
        UpdatableLHashParallelKVCharShortMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashCharShortMap newMutableMap(Map<Character, Short> map1,
            Map<Character, Short> map2, int expectedSize) {
        MutableLHashParallelKVCharShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newMutableMap(Map<Character, Short> map1,
            Map<Character, Short> map2, Map<Character, Short> map3, int expectedSize) {
        MutableLHashParallelKVCharShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newMutableMap(Map<Character, Short> map1,
            Map<Character, Short> map2, Map<Character, Short> map3,
            Map<Character, Short> map4, int expectedSize) {
        MutableLHashParallelKVCharShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newMutableMap(Map<Character, Short> map1,
            Map<Character, Short> map2, Map<Character, Short> map3,
            Map<Character, Short> map4, Map<Character, Short> map5, int expectedSize) {
        MutableLHashParallelKVCharShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharShortMap newMutableMap(
            Consumer<net.openhft.koloboke.function.CharShortConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashParallelKVCharShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newMutableMap(char[] keys,
            short[] values, int expectedSize) {
        MutableLHashParallelKVCharShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newMutableMap(
            Character[] keys, Short[] values, int expectedSize) {
        MutableLHashParallelKVCharShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newMutableMap(Iterable<Character> keys,
            Iterable<Short> values, int expectedSize) {
        MutableLHashParallelKVCharShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashCharShortMap newMutableMap(
            Map<Character, Short> map) {
        MutableLHashParallelKVCharShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newMutableMap(Map<Character, Short> map1,
            Map<Character, Short> map2) {
        MutableLHashParallelKVCharShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newMutableMap(Map<Character, Short> map1,
            Map<Character, Short> map2, Map<Character, Short> map3) {
        MutableLHashParallelKVCharShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newMutableMap(Map<Character, Short> map1,
            Map<Character, Short> map2, Map<Character, Short> map3,
            Map<Character, Short> map4) {
        MutableLHashParallelKVCharShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newMutableMap(Map<Character, Short> map1,
            Map<Character, Short> map2, Map<Character, Short> map3,
            Map<Character, Short> map4, Map<Character, Short> map5) {
        MutableLHashParallelKVCharShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharShortMap newMutableMap(
            Consumer<net.openhft.koloboke.function.CharShortConsumer> entriesSupplier
            ) {
        MutableLHashParallelKVCharShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newMutableMap(char[] keys,
            short[] values) {
        MutableLHashParallelKVCharShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newMutableMap(
            Character[] keys, Short[] values) {
        MutableLHashParallelKVCharShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newMutableMap(Iterable<Character> keys,
            Iterable<Short> values) {
        MutableLHashParallelKVCharShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashCharShortMap newMutableMapOf(char k1, short v1) {
        MutableLHashParallelKVCharShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newMutableMapOf(char k1, short v1,
             char k2, short v2) {
        MutableLHashParallelKVCharShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newMutableMapOf(char k1, short v1,
             char k2, short v2, char k3, short v3) {
        MutableLHashParallelKVCharShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newMutableMapOf(char k1, short v1,
             char k2, short v2, char k3, short v3,
             char k4, short v4) {
        MutableLHashParallelKVCharShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newMutableMapOf(char k1, short v1,
             char k2, short v2, char k3, short v3,
             char k4, short v4, char k5, short v5) {
        MutableLHashParallelKVCharShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMap(Map<Character, Short> map1,
            Map<Character, Short> map2, int expectedSize) {
        ImmutableLHashParallelKVCharShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMap(Map<Character, Short> map1,
            Map<Character, Short> map2, Map<Character, Short> map3, int expectedSize) {
        ImmutableLHashParallelKVCharShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMap(Map<Character, Short> map1,
            Map<Character, Short> map2, Map<Character, Short> map3,
            Map<Character, Short> map4, int expectedSize) {
        ImmutableLHashParallelKVCharShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMap(Map<Character, Short> map1,
            Map<Character, Short> map2, Map<Character, Short> map3,
            Map<Character, Short> map4, Map<Character, Short> map5, int expectedSize) {
        ImmutableLHashParallelKVCharShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.CharShortConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashParallelKVCharShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMap(char[] keys,
            short[] values, int expectedSize) {
        ImmutableLHashParallelKVCharShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMap(
            Character[] keys, Short[] values, int expectedSize) {
        ImmutableLHashParallelKVCharShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMap(Iterable<Character> keys,
            Iterable<Short> values, int expectedSize) {
        ImmutableLHashParallelKVCharShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMap(
            Map<Character, Short> map) {
        ImmutableLHashParallelKVCharShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMap(Map<Character, Short> map1,
            Map<Character, Short> map2) {
        ImmutableLHashParallelKVCharShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMap(Map<Character, Short> map1,
            Map<Character, Short> map2, Map<Character, Short> map3) {
        ImmutableLHashParallelKVCharShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMap(Map<Character, Short> map1,
            Map<Character, Short> map2, Map<Character, Short> map3,
            Map<Character, Short> map4) {
        ImmutableLHashParallelKVCharShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMap(Map<Character, Short> map1,
            Map<Character, Short> map2, Map<Character, Short> map3,
            Map<Character, Short> map4, Map<Character, Short> map5) {
        ImmutableLHashParallelKVCharShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.CharShortConsumer> entriesSupplier
            ) {
        ImmutableLHashParallelKVCharShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMap(char[] keys,
            short[] values) {
        ImmutableLHashParallelKVCharShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMap(
            Character[] keys, Short[] values) {
        ImmutableLHashParallelKVCharShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMap(Iterable<Character> keys,
            Iterable<Short> values) {
        ImmutableLHashParallelKVCharShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMapOf(char k1, short v1) {
        ImmutableLHashParallelKVCharShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMapOf(char k1, short v1,
             char k2, short v2) {
        ImmutableLHashParallelKVCharShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMapOf(char k1, short v1,
             char k2, short v2, char k3, short v3) {
        ImmutableLHashParallelKVCharShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMapOf(char k1, short v1,
             char k2, short v2, char k3, short v3,
             char k4, short v4) {
        ImmutableLHashParallelKVCharShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharShortMap newImmutableMapOf(char k1, short v1,
             char k2, short v2, char k3, short v3,
             char k4, short v4, char k5, short v5) {
        ImmutableLHashParallelKVCharShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

