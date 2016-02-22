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


public abstract class QHashParallelKVShortCharMapFactoryGO
        extends QHashParallelKVShortCharMapFactorySO {

    QHashParallelKVShortCharMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashParallelKVShortCharMapGO shrunk(
            UpdatableQHashParallelKVShortCharMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashParallelKVShortCharMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMap(
            Map<Short, Character> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMap(
            Map<Short, Character> map1, Map<Short, Character> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMap(
            Map<Short, Character> map1, Map<Short, Character> map2,
            Map<Short, Character> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMap(
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
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMap(
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
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMap(
            Map<Short, Character> map1, Map<Short, Character> map2,
            int expectedSize) {
        UpdatableQHashParallelKVShortCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMap(
            Map<Short, Character> map1, Map<Short, Character> map2,
            Map<Short, Character> map3, int expectedSize) {
        UpdatableQHashParallelKVShortCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMap(
            Map<Short, Character> map1, Map<Short, Character> map2,
            Map<Short, Character> map3, Map<Short, Character> map4,
            int expectedSize) {
        UpdatableQHashParallelKVShortCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMap(
            Map<Short, Character> map1, Map<Short, Character> map2,
            Map<Short, Character> map3, Map<Short, Character> map4,
            Map<Short, Character> map5, int expectedSize) {
        UpdatableQHashParallelKVShortCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ShortCharConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ShortCharConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashParallelKVShortCharMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMap(
            short[] keys, char[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMap(
            short[] keys, char[] values, int expectedSize) {
        UpdatableQHashParallelKVShortCharMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMap(
            Short[] keys, Character[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMap(
            Short[] keys, Character[] values, int expectedSize) {
        UpdatableQHashParallelKVShortCharMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMap(
            Iterable<Short> keys, Iterable<Character> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMap(
            Iterable<Short> keys, Iterable<Character> values, int expectedSize) {
        UpdatableQHashParallelKVShortCharMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMapOf(
            short k1, char v1) {
        UpdatableQHashParallelKVShortCharMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMapOf(
            short k1, char v1, short k2, char v2) {
        UpdatableQHashParallelKVShortCharMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMapOf(
            short k1, char v1, short k2, char v2,
            short k3, char v3) {
        UpdatableQHashParallelKVShortCharMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMapOf(
            short k1, char v1, short k2, char v2,
            short k3, char v3, short k4, char v4) {
        UpdatableQHashParallelKVShortCharMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVShortCharMapGO newUpdatableMapOf(
            short k1, char v1, short k2, char v2,
            short k3, char v3, short k4, char v4,
            short k5, char v5) {
        UpdatableQHashParallelKVShortCharMapGO map = newUpdatableMap(5);
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
        MutableQHashParallelKVShortCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3, int expectedSize) {
        MutableQHashParallelKVShortCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3,
            Map<Short, Character> map4, int expectedSize) {
        MutableQHashParallelKVShortCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3,
            Map<Short, Character> map4, Map<Short, Character> map5, int expectedSize) {
        MutableQHashParallelKVShortCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ShortCharConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(short[] keys,
            char[] values, int expectedSize) {
        MutableQHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(
            Short[] keys, Character[] values, int expectedSize) {
        MutableQHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(Iterable<Short> keys,
            Iterable<Character> values, int expectedSize) {
        MutableQHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(
            Map<Short, Character> map) {
        MutableQHashParallelKVShortCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2) {
        MutableQHashParallelKVShortCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3) {
        MutableQHashParallelKVShortCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3,
            Map<Short, Character> map4) {
        MutableQHashParallelKVShortCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3,
            Map<Short, Character> map4, Map<Short, Character> map5) {
        MutableQHashParallelKVShortCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ShortCharConsumer> entriesSupplier
            ) {
        MutableQHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(short[] keys,
            char[] values) {
        MutableQHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(
            Short[] keys, Character[] values) {
        MutableQHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMap(Iterable<Short> keys,
            Iterable<Character> values) {
        MutableQHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashShortCharMap newMutableMapOf(short k1, char v1) {
        MutableQHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMapOf(short k1, char v1,
             short k2, char v2) {
        MutableQHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMapOf(short k1, char v1,
             short k2, char v2, short k3, char v3) {
        MutableQHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMapOf(short k1, char v1,
             short k2, char v2, short k3, char v3,
             short k4, char v4) {
        MutableQHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newMutableMapOf(short k1, char v1,
             short k2, char v2, short k3, char v3,
             short k4, char v4, short k5, char v5) {
        MutableQHashParallelKVShortCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, int expectedSize) {
        ImmutableQHashParallelKVShortCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3, int expectedSize) {
        ImmutableQHashParallelKVShortCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3,
            Map<Short, Character> map4, int expectedSize) {
        ImmutableQHashParallelKVShortCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3,
            Map<Short, Character> map4, Map<Short, Character> map5, int expectedSize) {
        ImmutableQHashParallelKVShortCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ShortCharConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(short[] keys,
            char[] values, int expectedSize) {
        ImmutableQHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(
            Short[] keys, Character[] values, int expectedSize) {
        ImmutableQHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(Iterable<Short> keys,
            Iterable<Character> values, int expectedSize) {
        ImmutableQHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(
            Map<Short, Character> map) {
        ImmutableQHashParallelKVShortCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2) {
        ImmutableQHashParallelKVShortCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3) {
        ImmutableQHashParallelKVShortCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3,
            Map<Short, Character> map4) {
        ImmutableQHashParallelKVShortCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(Map<Short, Character> map1,
            Map<Short, Character> map2, Map<Short, Character> map3,
            Map<Short, Character> map4, Map<Short, Character> map5) {
        ImmutableQHashParallelKVShortCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ShortCharConsumer> entriesSupplier
            ) {
        ImmutableQHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(short[] keys,
            char[] values) {
        ImmutableQHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(
            Short[] keys, Character[] values) {
        ImmutableQHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMap(Iterable<Short> keys,
            Iterable<Character> values) {
        ImmutableQHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMapOf(short k1, char v1) {
        ImmutableQHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMapOf(short k1, char v1,
             short k2, char v2) {
        ImmutableQHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMapOf(short k1, char v1,
             short k2, char v2, short k3, char v3) {
        ImmutableQHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMapOf(short k1, char v1,
             short k2, char v2, short k3, char v3,
             short k4, char v4) {
        ImmutableQHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortCharMap newImmutableMapOf(short k1, char v1,
             short k2, char v2, short k3, char v3,
             short k4, char v4, short k5, char v5) {
        ImmutableQHashParallelKVShortCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

