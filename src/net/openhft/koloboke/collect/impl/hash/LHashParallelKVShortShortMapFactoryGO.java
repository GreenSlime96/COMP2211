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
import net.openhft.koloboke.collect.map.hash.HashShortShortMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashShortShortMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashParallelKVShortShortMapFactoryGO
        extends LHashParallelKVShortShortMapFactorySO {

    LHashParallelKVShortShortMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashShortShortMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper);

    abstract HashShortShortMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper);

    abstract HashShortShortMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper);

    @Override
    public final HashShortShortMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashShortShortMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashShortShortMapFactory withDomain(short lower, short upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashShortShortMapFactory withKeysDomain(short lower, short upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashShortShortMapFactory withKeysDomainComplement(short lower, short upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((short) (upper + 1), (short) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashShortShortMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashShortShortMapFactory) {
            HashShortShortMapFactory factory = (HashShortShortMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashParallelKVShortShortMapGO shrunk(
            UpdatableLHashParallelKVShortShortMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashParallelKVShortShortMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMap(
            Map<Short, Short> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMap(
            Map<Short, Short> map1, Map<Short, Short> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMap(
            Map<Short, Short> map1, Map<Short, Short> map2,
            Map<Short, Short> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMap(
            Map<Short, Short> map1, Map<Short, Short> map2,
            Map<Short, Short> map3, Map<Short, Short> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMap(
            Map<Short, Short> map1, Map<Short, Short> map2,
            Map<Short, Short> map3, Map<Short, Short> map4,
            Map<Short, Short> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMap(
            Map<Short, Short> map1, Map<Short, Short> map2,
            int expectedSize) {
        UpdatableLHashParallelKVShortShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMap(
            Map<Short, Short> map1, Map<Short, Short> map2,
            Map<Short, Short> map3, int expectedSize) {
        UpdatableLHashParallelKVShortShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMap(
            Map<Short, Short> map1, Map<Short, Short> map2,
            Map<Short, Short> map3, Map<Short, Short> map4,
            int expectedSize) {
        UpdatableLHashParallelKVShortShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMap(
            Map<Short, Short> map1, Map<Short, Short> map2,
            Map<Short, Short> map3, Map<Short, Short> map4,
            Map<Short, Short> map5, int expectedSize) {
        UpdatableLHashParallelKVShortShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ShortShortConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ShortShortConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashParallelKVShortShortMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.ShortShortConsumer() {
             @Override
             public void accept(short k, short v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMap(
            short[] keys, short[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMap(
            short[] keys, short[] values, int expectedSize) {
        UpdatableLHashParallelKVShortShortMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMap(
            Short[] keys, Short[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMap(
            Short[] keys, Short[] values, int expectedSize) {
        UpdatableLHashParallelKVShortShortMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMap(
            Iterable<Short> keys, Iterable<Short> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMap(
            Iterable<Short> keys, Iterable<Short> values, int expectedSize) {
        UpdatableLHashParallelKVShortShortMapGO map = newUpdatableMap(expectedSize);
        Iterator<Short> keysIt = keys.iterator();
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
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMapOf(
            short k1, short v1) {
        UpdatableLHashParallelKVShortShortMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMapOf(
            short k1, short v1, short k2, short v2) {
        UpdatableLHashParallelKVShortShortMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMapOf(
            short k1, short v1, short k2, short v2,
            short k3, short v3) {
        UpdatableLHashParallelKVShortShortMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMapOf(
            short k1, short v1, short k2, short v2,
            short k3, short v3, short k4, short v4) {
        UpdatableLHashParallelKVShortShortMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVShortShortMapGO newUpdatableMapOf(
            short k1, short v1, short k2, short v2,
            short k3, short v3, short k4, short v4,
            short k5, short v5) {
        UpdatableLHashParallelKVShortShortMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashShortShortMap newMutableMap(Map<Short, Short> map1,
            Map<Short, Short> map2, int expectedSize) {
        MutableLHashParallelKVShortShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newMutableMap(Map<Short, Short> map1,
            Map<Short, Short> map2, Map<Short, Short> map3, int expectedSize) {
        MutableLHashParallelKVShortShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newMutableMap(Map<Short, Short> map1,
            Map<Short, Short> map2, Map<Short, Short> map3,
            Map<Short, Short> map4, int expectedSize) {
        MutableLHashParallelKVShortShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newMutableMap(Map<Short, Short> map1,
            Map<Short, Short> map2, Map<Short, Short> map3,
            Map<Short, Short> map4, Map<Short, Short> map5, int expectedSize) {
        MutableLHashParallelKVShortShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashShortShortMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ShortShortConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashParallelKVShortShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newMutableMap(short[] keys,
            short[] values, int expectedSize) {
        MutableLHashParallelKVShortShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newMutableMap(
            Short[] keys, Short[] values, int expectedSize) {
        MutableLHashParallelKVShortShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newMutableMap(Iterable<Short> keys,
            Iterable<Short> values, int expectedSize) {
        MutableLHashParallelKVShortShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashShortShortMap newMutableMap(
            Map<Short, Short> map) {
        MutableLHashParallelKVShortShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newMutableMap(Map<Short, Short> map1,
            Map<Short, Short> map2) {
        MutableLHashParallelKVShortShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newMutableMap(Map<Short, Short> map1,
            Map<Short, Short> map2, Map<Short, Short> map3) {
        MutableLHashParallelKVShortShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newMutableMap(Map<Short, Short> map1,
            Map<Short, Short> map2, Map<Short, Short> map3,
            Map<Short, Short> map4) {
        MutableLHashParallelKVShortShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newMutableMap(Map<Short, Short> map1,
            Map<Short, Short> map2, Map<Short, Short> map3,
            Map<Short, Short> map4, Map<Short, Short> map5) {
        MutableLHashParallelKVShortShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashShortShortMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ShortShortConsumer> entriesSupplier
            ) {
        MutableLHashParallelKVShortShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newMutableMap(short[] keys,
            short[] values) {
        MutableLHashParallelKVShortShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newMutableMap(
            Short[] keys, Short[] values) {
        MutableLHashParallelKVShortShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newMutableMap(Iterable<Short> keys,
            Iterable<Short> values) {
        MutableLHashParallelKVShortShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashShortShortMap newMutableMapOf(short k1, short v1) {
        MutableLHashParallelKVShortShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newMutableMapOf(short k1, short v1,
             short k2, short v2) {
        MutableLHashParallelKVShortShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newMutableMapOf(short k1, short v1,
             short k2, short v2, short k3, short v3) {
        MutableLHashParallelKVShortShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newMutableMapOf(short k1, short v1,
             short k2, short v2, short k3, short v3,
             short k4, short v4) {
        MutableLHashParallelKVShortShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newMutableMapOf(short k1, short v1,
             short k2, short v2, short k3, short v3,
             short k4, short v4, short k5, short v5) {
        MutableLHashParallelKVShortShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMap(Map<Short, Short> map1,
            Map<Short, Short> map2, int expectedSize) {
        ImmutableLHashParallelKVShortShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMap(Map<Short, Short> map1,
            Map<Short, Short> map2, Map<Short, Short> map3, int expectedSize) {
        ImmutableLHashParallelKVShortShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMap(Map<Short, Short> map1,
            Map<Short, Short> map2, Map<Short, Short> map3,
            Map<Short, Short> map4, int expectedSize) {
        ImmutableLHashParallelKVShortShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMap(Map<Short, Short> map1,
            Map<Short, Short> map2, Map<Short, Short> map3,
            Map<Short, Short> map4, Map<Short, Short> map5, int expectedSize) {
        ImmutableLHashParallelKVShortShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ShortShortConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashParallelKVShortShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMap(short[] keys,
            short[] values, int expectedSize) {
        ImmutableLHashParallelKVShortShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMap(
            Short[] keys, Short[] values, int expectedSize) {
        ImmutableLHashParallelKVShortShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMap(Iterable<Short> keys,
            Iterable<Short> values, int expectedSize) {
        ImmutableLHashParallelKVShortShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMap(
            Map<Short, Short> map) {
        ImmutableLHashParallelKVShortShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMap(Map<Short, Short> map1,
            Map<Short, Short> map2) {
        ImmutableLHashParallelKVShortShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMap(Map<Short, Short> map1,
            Map<Short, Short> map2, Map<Short, Short> map3) {
        ImmutableLHashParallelKVShortShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMap(Map<Short, Short> map1,
            Map<Short, Short> map2, Map<Short, Short> map3,
            Map<Short, Short> map4) {
        ImmutableLHashParallelKVShortShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMap(Map<Short, Short> map1,
            Map<Short, Short> map2, Map<Short, Short> map3,
            Map<Short, Short> map4, Map<Short, Short> map5) {
        ImmutableLHashParallelKVShortShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ShortShortConsumer> entriesSupplier
            ) {
        ImmutableLHashParallelKVShortShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMap(short[] keys,
            short[] values) {
        ImmutableLHashParallelKVShortShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMap(
            Short[] keys, Short[] values) {
        ImmutableLHashParallelKVShortShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMap(Iterable<Short> keys,
            Iterable<Short> values) {
        ImmutableLHashParallelKVShortShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMapOf(short k1, short v1) {
        ImmutableLHashParallelKVShortShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMapOf(short k1, short v1,
             short k2, short v2) {
        ImmutableLHashParallelKVShortShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMapOf(short k1, short v1,
             short k2, short v2, short k3, short v3) {
        ImmutableLHashParallelKVShortShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMapOf(short k1, short v1,
             short k2, short v2, short k3, short v3,
             short k4, short v4) {
        ImmutableLHashParallelKVShortShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashShortShortMap newImmutableMapOf(short k1, short v1,
             short k2, short v2, short k3, short v3,
             short k4, short v4, short k5, short v5) {
        ImmutableLHashParallelKVShortShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

