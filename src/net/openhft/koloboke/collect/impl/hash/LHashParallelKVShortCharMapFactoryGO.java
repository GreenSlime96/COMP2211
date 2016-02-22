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
import net.openhft.koloboke.collect.map.hash.HashShortCharMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashShortCharMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashParallelKVShortCharMapFactoryGO
        extends LHashParallelKVShortCharMapFactorySO {

    LHashParallelKVShortCharMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashShortCharMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper);

    abstract HashShortCharMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper);

    abstract HashShortCharMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper);

    @Override
    public final HashShortCharMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashShortCharMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashShortCharMapFactory withDomain(short lower, short upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashShortCharMapFactory withKeysDomain(short lower, short upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashShortCharMapFactory withKeysDomainComplement(short lower, short upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((short) (upper + 1), (short) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashShortCharMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashShortCharMapFactory) {
            HashShortCharMapFactory factory = (HashShortCharMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashParallelKVShortCharMapGO shrunk(
            UpdatableLHashParallelKVShortCharMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashParallelKVShortCharMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMap(
            Map<Short, Character> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMap(
            Map<Short, Character> map1, Map<Short, Character> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMap(
            Map<Short, Character> map1, Map<Short, Character> map2,
            Map<Short, Character> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMap(
            Map<Short, Character> map1, Map<Short, Character> map2,
            Map<Short, Character> map3, Map<Short, Character> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMap(
            Map<Short, Character> map1, Map<Short, Character> map2,
            Map<Short, Character> map3, Map<Short, Character> map4,
            Map<Short, Character> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMap(
            Map<Short, Character> map1, Map<Short, Character> map2,
            int expectedSize) {
        UpdatableLHashParallelKVShortCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMap(
            Map<Short, Character> map1, Map<Short, Character> map2,
            Map<Short, Character> map3, int expectedSize) {
        UpdatableLHashParallelKVShortCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMap(
            Map<Short, Character> map1, Map<Short, Character> map2,
            Map<Short, Character> map3, Map<Short, Character> map4,
            int expectedSize) {
        UpdatableLHashParallelKVShortCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMap(
            Map<Short, Character> map1, Map<Short, Character> map2,
            Map<Short, Character> map3, Map<Short, Character> map4,
            Map<Short, Character> map5, int expectedSize) {
        UpdatableLHashParallelKVShortCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ShortCharConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ShortCharConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashParallelKVShortCharMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.ShortCharConsumer() {
             @Override
             public void accept(short k, char v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMap(
            short[] keys, char[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMap(
            short[] keys, char[] values, int expectedSize) {
        UpdatableLHashParallelKVShortCharMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMap(
            Short[] keys, Character[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMap(
            Short[] keys, Character[] values, int expectedSize) {
        UpdatableLHashParallelKVShortCharMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMap(
            Iterable<Short> keys, Iterable<Character> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMap(
            Iterable<Short> keys, Iterable<Character> values, int expectedSize) {
        UpdatableLHashParallelKVShortCharMapGO map = newUpdatableMap(expectedSize);
        Iterator<Short> keysIt = keys.iterator();
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
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMapOf(
            short k1, char v1) {
        UpdatableLHashParallelKVShortCharMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMapOf(
            short k1, char v1, short k2, char v2) {
        UpdatableLHashParallelKVShortCharMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMapOf(
            short k1, char v1, short k2, char v2,
            short k3, char v3) {
        UpdatableLHashParallelKVShortCharMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMapOf(
            short k1, char v1, short k2, char v2,
            short k3, char v3, short k4, char v4) {
        UpdatableLHashParallelKVShortCharMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortCharMapGO newUpdatableMapOf(
            short k1, char v1, short k2, char v2,
            short k3, char v3, short k4, char v4,
            short k5, char v5) {
        UpdatableLHashParallelKVShortCharMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, int expectedSize) {
        MutableLHashParallelKVShortCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3, int expectedSize) {
        MutableLHashParallelKVShortCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3,
            Map<Short, Character> map4, int expectedSize) {
        MutableLHashParallelKVShortCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3,
            Map<Short, Character> map4, Map<Short, Character> map5, int expectedSize) {
        MutableLHashParallelKVShortCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ShortCharConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(short[] keys,
            char[] values, int expectedSize) {
        MutableLHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(
            Short[] keys, Character[] values, int expectedSize) {
        MutableLHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(Iterable<Short> keys,
            Iterable<Character> values, int expectedSize) {
        MutableLHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(
            Map<Short, Character> map) {
        MutableLHashParallelKVShortCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2) {
        MutableLHashParallelKVShortCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3) {
        MutableLHashParallelKVShortCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3,
            Map<Short, Character> map4) {
        MutableLHashParallelKVShortCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3,
            Map<Short, Character> map4, Map<Short, Character> map5) {
        MutableLHashParallelKVShortCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ShortCharConsumer> entriesSupplier
            ) {
        MutableLHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(short[] keys,
            char[] values) {
        MutableLHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(
            Short[] keys, Character[] values) {
        MutableLHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(Iterable<Short> keys,
            Iterable<Character> values) {
        MutableLHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashShortCharMap newMutableMapOf(short k1, char v1) {
        MutableLHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMapOf(short k1, char v1,
             short k2, char v2) {
        MutableLHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMapOf(short k1, char v1,
             short k2, char v2, short k3, char v3) {
        MutableLHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMapOf(short k1, char v1,
             short k2, char v2, short k3, char v3,
             short k4, char v4) {
        MutableLHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMapOf(short k1, char v1,
             short k2, char v2, short k3, char v3,
             short k4, char v4, short k5, char v5) {
        MutableLHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, int expectedSize) {
        ImmutableLHashParallelKVShortCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3, int expectedSize) {
        ImmutableLHashParallelKVShortCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3,
            Map<Short, Character> map4, int expectedSize) {
        ImmutableLHashParallelKVShortCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3,
            Map<Short, Character> map4, Map<Short, Character> map5, int expectedSize) {
        ImmutableLHashParallelKVShortCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ShortCharConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(short[] keys,
            char[] values, int expectedSize) {
        ImmutableLHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(
            Short[] keys, Character[] values, int expectedSize) {
        ImmutableLHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(Iterable<Short> keys,
            Iterable<Character> values, int expectedSize) {
        ImmutableLHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(
            Map<Short, Character> map) {
        ImmutableLHashParallelKVShortCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2) {
        ImmutableLHashParallelKVShortCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3) {
        ImmutableLHashParallelKVShortCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3,
            Map<Short, Character> map4) {
        ImmutableLHashParallelKVShortCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3,
            Map<Short, Character> map4, Map<Short, Character> map5) {
        ImmutableLHashParallelKVShortCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ShortCharConsumer> entriesSupplier
            ) {
        ImmutableLHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(short[] keys,
            char[] values) {
        ImmutableLHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(
            Short[] keys, Character[] values) {
        ImmutableLHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(Iterable<Short> keys,
            Iterable<Character> values) {
        ImmutableLHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMapOf(short k1, char v1) {
        ImmutableLHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMapOf(short k1, char v1,
             short k2, char v2) {
        ImmutableLHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMapOf(short k1, char v1,
             short k2, char v2, short k3, char v3) {
        ImmutableLHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMapOf(short k1, char v1,
             short k2, char v2, short k3, char v3,
             short k4, char v4) {
        ImmutableLHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMapOf(short k1, char v1,
             short k2, char v2, short k3, char v3,
             short k4, char v4, short k5, char v5) {
        ImmutableLHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

