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
import net.openhft.koloboke.collect.map.hash.HashCharCharMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashCharCharMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashParallelKVCharCharMapFactoryGO
        extends LHashParallelKVCharCharMapFactorySO {

    LHashParallelKVCharCharMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashCharCharMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper);

    abstract HashCharCharMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper);

    abstract HashCharCharMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper);

    @Override
    public final HashCharCharMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashCharCharMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashCharCharMapFactory withDomain(char lower, char upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashCharCharMapFactory withKeysDomain(char lower, char upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashCharCharMapFactory withKeysDomainComplement(char lower, char upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((char) (upper + 1), (char) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashCharCharMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashCharCharMapFactory) {
            HashCharCharMapFactory factory = (HashCharCharMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashParallelKVCharCharMapGO shrunk(
            UpdatableLHashParallelKVCharCharMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashParallelKVCharCharMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMap(
            Map<Character, Character> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMap(
            Map<Character, Character> map1, Map<Character, Character> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMap(
            Map<Character, Character> map1, Map<Character, Character> map2,
            Map<Character, Character> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMap(
            Map<Character, Character> map1, Map<Character, Character> map2,
            Map<Character, Character> map3, Map<Character, Character> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMap(
            Map<Character, Character> map1, Map<Character, Character> map2,
            Map<Character, Character> map3, Map<Character, Character> map4,
            Map<Character, Character> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMap(
            Map<Character, Character> map1, Map<Character, Character> map2,
            int expectedSize) {
        UpdatableLHashParallelKVCharCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMap(
            Map<Character, Character> map1, Map<Character, Character> map2,
            Map<Character, Character> map3, int expectedSize) {
        UpdatableLHashParallelKVCharCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMap(
            Map<Character, Character> map1, Map<Character, Character> map2,
            Map<Character, Character> map3, Map<Character, Character> map4,
            int expectedSize) {
        UpdatableLHashParallelKVCharCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMap(
            Map<Character, Character> map1, Map<Character, Character> map2,
            Map<Character, Character> map3, Map<Character, Character> map4,
            Map<Character, Character> map5, int expectedSize) {
        UpdatableLHashParallelKVCharCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.CharCharConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.CharCharConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashParallelKVCharCharMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.CharCharConsumer() {
             @Override
             public void accept(char k, char v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMap(
            char[] keys, char[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMap(
            char[] keys, char[] values, int expectedSize) {
        UpdatableLHashParallelKVCharCharMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMap(
            Character[] keys, Character[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMap(
            Character[] keys, Character[] values, int expectedSize) {
        UpdatableLHashParallelKVCharCharMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMap(
            Iterable<Character> keys, Iterable<Character> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMap(
            Iterable<Character> keys, Iterable<Character> values, int expectedSize) {
        UpdatableLHashParallelKVCharCharMapGO map = newUpdatableMap(expectedSize);
        Iterator<Character> keysIt = keys.iterator();
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
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMapOf(
            char k1, char v1) {
        UpdatableLHashParallelKVCharCharMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMapOf(
            char k1, char v1, char k2, char v2) {
        UpdatableLHashParallelKVCharCharMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMapOf(
            char k1, char v1, char k2, char v2,
            char k3, char v3) {
        UpdatableLHashParallelKVCharCharMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMapOf(
            char k1, char v1, char k2, char v2,
            char k3, char v3, char k4, char v4) {
        UpdatableLHashParallelKVCharCharMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVCharCharMapGO newUpdatableMapOf(
            char k1, char v1, char k2, char v2,
            char k3, char v3, char k4, char v4,
            char k5, char v5) {
        UpdatableLHashParallelKVCharCharMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashCharCharMap newMutableMap(Map<Character, Character> map1,
            Map<Character, Character> map2, int expectedSize) {
        MutableLHashParallelKVCharCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newMutableMap(Map<Character, Character> map1,
            Map<Character, Character> map2, Map<Character, Character> map3, int expectedSize) {
        MutableLHashParallelKVCharCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newMutableMap(Map<Character, Character> map1,
            Map<Character, Character> map2, Map<Character, Character> map3,
            Map<Character, Character> map4, int expectedSize) {
        MutableLHashParallelKVCharCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newMutableMap(Map<Character, Character> map1,
            Map<Character, Character> map2, Map<Character, Character> map3,
            Map<Character, Character> map4, Map<Character, Character> map5, int expectedSize) {
        MutableLHashParallelKVCharCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharCharMap newMutableMap(
            Consumer<net.openhft.koloboke.function.CharCharConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashParallelKVCharCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newMutableMap(char[] keys,
            char[] values, int expectedSize) {
        MutableLHashParallelKVCharCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newMutableMap(
            Character[] keys, Character[] values, int expectedSize) {
        MutableLHashParallelKVCharCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newMutableMap(Iterable<Character> keys,
            Iterable<Character> values, int expectedSize) {
        MutableLHashParallelKVCharCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashCharCharMap newMutableMap(
            Map<Character, Character> map) {
        MutableLHashParallelKVCharCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newMutableMap(Map<Character, Character> map1,
            Map<Character, Character> map2) {
        MutableLHashParallelKVCharCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newMutableMap(Map<Character, Character> map1,
            Map<Character, Character> map2, Map<Character, Character> map3) {
        MutableLHashParallelKVCharCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newMutableMap(Map<Character, Character> map1,
            Map<Character, Character> map2, Map<Character, Character> map3,
            Map<Character, Character> map4) {
        MutableLHashParallelKVCharCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newMutableMap(Map<Character, Character> map1,
            Map<Character, Character> map2, Map<Character, Character> map3,
            Map<Character, Character> map4, Map<Character, Character> map5) {
        MutableLHashParallelKVCharCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharCharMap newMutableMap(
            Consumer<net.openhft.koloboke.function.CharCharConsumer> entriesSupplier
            ) {
        MutableLHashParallelKVCharCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newMutableMap(char[] keys,
            char[] values) {
        MutableLHashParallelKVCharCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newMutableMap(
            Character[] keys, Character[] values) {
        MutableLHashParallelKVCharCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newMutableMap(Iterable<Character> keys,
            Iterable<Character> values) {
        MutableLHashParallelKVCharCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashCharCharMap newMutableMapOf(char k1, char v1) {
        MutableLHashParallelKVCharCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newMutableMapOf(char k1, char v1,
             char k2, char v2) {
        MutableLHashParallelKVCharCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newMutableMapOf(char k1, char v1,
             char k2, char v2, char k3, char v3) {
        MutableLHashParallelKVCharCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newMutableMapOf(char k1, char v1,
             char k2, char v2, char k3, char v3,
             char k4, char v4) {
        MutableLHashParallelKVCharCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newMutableMapOf(char k1, char v1,
             char k2, char v2, char k3, char v3,
             char k4, char v4, char k5, char v5) {
        MutableLHashParallelKVCharCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMap(Map<Character, Character> map1,
            Map<Character, Character> map2, int expectedSize) {
        ImmutableLHashParallelKVCharCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMap(Map<Character, Character> map1,
            Map<Character, Character> map2, Map<Character, Character> map3, int expectedSize) {
        ImmutableLHashParallelKVCharCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMap(Map<Character, Character> map1,
            Map<Character, Character> map2, Map<Character, Character> map3,
            Map<Character, Character> map4, int expectedSize) {
        ImmutableLHashParallelKVCharCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMap(Map<Character, Character> map1,
            Map<Character, Character> map2, Map<Character, Character> map3,
            Map<Character, Character> map4, Map<Character, Character> map5, int expectedSize) {
        ImmutableLHashParallelKVCharCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.CharCharConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashParallelKVCharCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMap(char[] keys,
            char[] values, int expectedSize) {
        ImmutableLHashParallelKVCharCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMap(
            Character[] keys, Character[] values, int expectedSize) {
        ImmutableLHashParallelKVCharCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMap(Iterable<Character> keys,
            Iterable<Character> values, int expectedSize) {
        ImmutableLHashParallelKVCharCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMap(
            Map<Character, Character> map) {
        ImmutableLHashParallelKVCharCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMap(Map<Character, Character> map1,
            Map<Character, Character> map2) {
        ImmutableLHashParallelKVCharCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMap(Map<Character, Character> map1,
            Map<Character, Character> map2, Map<Character, Character> map3) {
        ImmutableLHashParallelKVCharCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMap(Map<Character, Character> map1,
            Map<Character, Character> map2, Map<Character, Character> map3,
            Map<Character, Character> map4) {
        ImmutableLHashParallelKVCharCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMap(Map<Character, Character> map1,
            Map<Character, Character> map2, Map<Character, Character> map3,
            Map<Character, Character> map4, Map<Character, Character> map5) {
        ImmutableLHashParallelKVCharCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.CharCharConsumer> entriesSupplier
            ) {
        ImmutableLHashParallelKVCharCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMap(char[] keys,
            char[] values) {
        ImmutableLHashParallelKVCharCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMap(
            Character[] keys, Character[] values) {
        ImmutableLHashParallelKVCharCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMap(Iterable<Character> keys,
            Iterable<Character> values) {
        ImmutableLHashParallelKVCharCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMapOf(char k1, char v1) {
        ImmutableLHashParallelKVCharCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMapOf(char k1, char v1,
             char k2, char v2) {
        ImmutableLHashParallelKVCharCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMapOf(char k1, char v1,
             char k2, char v2, char k3, char v3) {
        ImmutableLHashParallelKVCharCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMapOf(char k1, char v1,
             char k2, char v2, char k3, char v3,
             char k4, char v4) {
        ImmutableLHashParallelKVCharCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharCharMap newImmutableMapOf(char k1, char v1,
             char k2, char v2, char k3, char v3,
             char k4, char v4, char k5, char v5) {
        ImmutableLHashParallelKVCharCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

