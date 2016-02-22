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
import net.openhft.koloboke.collect.map.hash.HashCharDoubleMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashCharDoubleMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashSeparateKVCharDoubleMapFactoryGO
        extends LHashSeparateKVCharDoubleMapFactorySO {

    LHashSeparateKVCharDoubleMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashCharDoubleMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper);

    abstract HashCharDoubleMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper);

    abstract HashCharDoubleMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper);

    @Override
    public final HashCharDoubleMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashCharDoubleMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashCharDoubleMapFactory withDomain(char lower, char upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashCharDoubleMapFactory withKeysDomain(char lower, char upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashCharDoubleMapFactory withKeysDomainComplement(char lower, char upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((char) (upper + 1), (char) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashCharDoubleMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashCharDoubleMapFactory) {
            HashCharDoubleMapFactory factory = (HashCharDoubleMapFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory) &&
                    // boxing to treat NaNs correctly
                   ((Double) getDefaultValue()).equals(factory.getDefaultValue())
                    ;
        } else {
            return false;
        }
    }

    @Override
    public double getDefaultValue() {
        return 0.0;
    }

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashSeparateKVCharDoubleMapGO shrunk(
            UpdatableLHashSeparateKVCharDoubleMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashSeparateKVCharDoubleMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMap(
            Map<Character, Double> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMap(
            Map<Character, Double> map1, Map<Character, Double> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMap(
            Map<Character, Double> map1, Map<Character, Double> map2,
            Map<Character, Double> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMap(
            Map<Character, Double> map1, Map<Character, Double> map2,
            Map<Character, Double> map3, Map<Character, Double> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMap(
            Map<Character, Double> map1, Map<Character, Double> map2,
            Map<Character, Double> map3, Map<Character, Double> map4,
            Map<Character, Double> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMap(
            Map<Character, Double> map1, Map<Character, Double> map2,
            int expectedSize) {
        UpdatableLHashSeparateKVCharDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMap(
            Map<Character, Double> map1, Map<Character, Double> map2,
            Map<Character, Double> map3, int expectedSize) {
        UpdatableLHashSeparateKVCharDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMap(
            Map<Character, Double> map1, Map<Character, Double> map2,
            Map<Character, Double> map3, Map<Character, Double> map4,
            int expectedSize) {
        UpdatableLHashSeparateKVCharDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMap(
            Map<Character, Double> map1, Map<Character, Double> map2,
            Map<Character, Double> map3, Map<Character, Double> map4,
            Map<Character, Double> map5, int expectedSize) {
        UpdatableLHashSeparateKVCharDoubleMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.CharDoubleConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.CharDoubleConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashSeparateKVCharDoubleMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.CharDoubleConsumer() {
             @Override
             public void accept(char k, double v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMap(
            char[] keys, double[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMap(
            char[] keys, double[] values, int expectedSize) {
        UpdatableLHashSeparateKVCharDoubleMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMap(
            Character[] keys, Double[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMap(
            Character[] keys, Double[] values, int expectedSize) {
        UpdatableLHashSeparateKVCharDoubleMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMap(
            Iterable<Character> keys, Iterable<Double> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMap(
            Iterable<Character> keys, Iterable<Double> values, int expectedSize) {
        UpdatableLHashSeparateKVCharDoubleMapGO map = newUpdatableMap(expectedSize);
        Iterator<Character> keysIt = keys.iterator();
        Iterator<Double> valuesIt = values.iterator();
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
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMapOf(
            char k1, double v1) {
        UpdatableLHashSeparateKVCharDoubleMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMapOf(
            char k1, double v1, char k2, double v2) {
        UpdatableLHashSeparateKVCharDoubleMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMapOf(
            char k1, double v1, char k2, double v2,
            char k3, double v3) {
        UpdatableLHashSeparateKVCharDoubleMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMapOf(
            char k1, double v1, char k2, double v2,
            char k3, double v3, char k4, double v4) {
        UpdatableLHashSeparateKVCharDoubleMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVCharDoubleMapGO newUpdatableMapOf(
            char k1, double v1, char k2, double v2,
            char k3, double v3, char k4, double v4,
            char k5, double v5) {
        UpdatableLHashSeparateKVCharDoubleMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMap(Map<Character, Double> map1,
            Map<Character, Double> map2, int expectedSize) {
        MutableLHashSeparateKVCharDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMap(Map<Character, Double> map1,
            Map<Character, Double> map2, Map<Character, Double> map3, int expectedSize) {
        MutableLHashSeparateKVCharDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMap(Map<Character, Double> map1,
            Map<Character, Double> map2, Map<Character, Double> map3,
            Map<Character, Double> map4, int expectedSize) {
        MutableLHashSeparateKVCharDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMap(Map<Character, Double> map1,
            Map<Character, Double> map2, Map<Character, Double> map3,
            Map<Character, Double> map4, Map<Character, Double> map5, int expectedSize) {
        MutableLHashSeparateKVCharDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMap(
            Consumer<net.openhft.koloboke.function.CharDoubleConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashSeparateKVCharDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMap(char[] keys,
            double[] values, int expectedSize) {
        MutableLHashSeparateKVCharDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMap(
            Character[] keys, Double[] values, int expectedSize) {
        MutableLHashSeparateKVCharDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMap(Iterable<Character> keys,
            Iterable<Double> values, int expectedSize) {
        MutableLHashSeparateKVCharDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMap(
            Map<Character, Double> map) {
        MutableLHashSeparateKVCharDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMap(Map<Character, Double> map1,
            Map<Character, Double> map2) {
        MutableLHashSeparateKVCharDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMap(Map<Character, Double> map1,
            Map<Character, Double> map2, Map<Character, Double> map3) {
        MutableLHashSeparateKVCharDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMap(Map<Character, Double> map1,
            Map<Character, Double> map2, Map<Character, Double> map3,
            Map<Character, Double> map4) {
        MutableLHashSeparateKVCharDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMap(Map<Character, Double> map1,
            Map<Character, Double> map2, Map<Character, Double> map3,
            Map<Character, Double> map4, Map<Character, Double> map5) {
        MutableLHashSeparateKVCharDoubleMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMap(
            Consumer<net.openhft.koloboke.function.CharDoubleConsumer> entriesSupplier
            ) {
        MutableLHashSeparateKVCharDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMap(char[] keys,
            double[] values) {
        MutableLHashSeparateKVCharDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMap(
            Character[] keys, Double[] values) {
        MutableLHashSeparateKVCharDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMap(Iterable<Character> keys,
            Iterable<Double> values) {
        MutableLHashSeparateKVCharDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMapOf(char k1, double v1) {
        MutableLHashSeparateKVCharDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMapOf(char k1, double v1,
             char k2, double v2) {
        MutableLHashSeparateKVCharDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMapOf(char k1, double v1,
             char k2, double v2, char k3, double v3) {
        MutableLHashSeparateKVCharDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMapOf(char k1, double v1,
             char k2, double v2, char k3, double v3,
             char k4, double v4) {
        MutableLHashSeparateKVCharDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newMutableMapOf(char k1, double v1,
             char k2, double v2, char k3, double v3,
             char k4, double v4, char k5, double v5) {
        MutableLHashSeparateKVCharDoubleMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMap(Map<Character, Double> map1,
            Map<Character, Double> map2, int expectedSize) {
        ImmutableLHashSeparateKVCharDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMap(Map<Character, Double> map1,
            Map<Character, Double> map2, Map<Character, Double> map3, int expectedSize) {
        ImmutableLHashSeparateKVCharDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMap(Map<Character, Double> map1,
            Map<Character, Double> map2, Map<Character, Double> map3,
            Map<Character, Double> map4, int expectedSize) {
        ImmutableLHashSeparateKVCharDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMap(Map<Character, Double> map1,
            Map<Character, Double> map2, Map<Character, Double> map3,
            Map<Character, Double> map4, Map<Character, Double> map5, int expectedSize) {
        ImmutableLHashSeparateKVCharDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.CharDoubleConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashSeparateKVCharDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMap(char[] keys,
            double[] values, int expectedSize) {
        ImmutableLHashSeparateKVCharDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMap(
            Character[] keys, Double[] values, int expectedSize) {
        ImmutableLHashSeparateKVCharDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMap(Iterable<Character> keys,
            Iterable<Double> values, int expectedSize) {
        ImmutableLHashSeparateKVCharDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMap(
            Map<Character, Double> map) {
        ImmutableLHashSeparateKVCharDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMap(Map<Character, Double> map1,
            Map<Character, Double> map2) {
        ImmutableLHashSeparateKVCharDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMap(Map<Character, Double> map1,
            Map<Character, Double> map2, Map<Character, Double> map3) {
        ImmutableLHashSeparateKVCharDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMap(Map<Character, Double> map1,
            Map<Character, Double> map2, Map<Character, Double> map3,
            Map<Character, Double> map4) {
        ImmutableLHashSeparateKVCharDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMap(Map<Character, Double> map1,
            Map<Character, Double> map2, Map<Character, Double> map3,
            Map<Character, Double> map4, Map<Character, Double> map5) {
        ImmutableLHashSeparateKVCharDoubleMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.CharDoubleConsumer> entriesSupplier
            ) {
        ImmutableLHashSeparateKVCharDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMap(char[] keys,
            double[] values) {
        ImmutableLHashSeparateKVCharDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMap(
            Character[] keys, Double[] values) {
        ImmutableLHashSeparateKVCharDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMap(Iterable<Character> keys,
            Iterable<Double> values) {
        ImmutableLHashSeparateKVCharDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMapOf(char k1, double v1) {
        ImmutableLHashSeparateKVCharDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMapOf(char k1, double v1,
             char k2, double v2) {
        ImmutableLHashSeparateKVCharDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMapOf(char k1, double v1,
             char k2, double v2, char k3, double v3) {
        ImmutableLHashSeparateKVCharDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMapOf(char k1, double v1,
             char k2, double v2, char k3, double v3,
             char k4, double v4) {
        ImmutableLHashSeparateKVCharDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashCharDoubleMap newImmutableMapOf(char k1, double v1,
             char k2, double v2, char k3, double v3,
             char k4, double v4, char k5, double v5) {
        ImmutableLHashSeparateKVCharDoubleMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

