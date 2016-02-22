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
import net.openhft.koloboke.collect.map.hash.HashDoubleCharMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashDoubleCharMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashSeparateKVDoubleCharMapFactoryGO
        extends LHashSeparateKVDoubleCharMapFactorySO {

    LHashSeparateKVDoubleCharMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    abstract HashDoubleCharMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashDoubleCharMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashDoubleCharMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    @Override
    public final HashDoubleCharMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
    }

    @Override
    public final HashDoubleCharMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                );
    }


    @Override
    public String toString() {
        return "HashDoubleCharMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashDoubleCharMapFactory) {
            HashDoubleCharMapFactory factory = (HashDoubleCharMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashSeparateKVDoubleCharMapGO shrunk(
            UpdatableLHashSeparateKVDoubleCharMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashSeparateKVDoubleCharMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMap(
            Map<Double, Character> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMap(
            Map<Double, Character> map1, Map<Double, Character> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMap(
            Map<Double, Character> map1, Map<Double, Character> map2,
            Map<Double, Character> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMap(
            Map<Double, Character> map1, Map<Double, Character> map2,
            Map<Double, Character> map3, Map<Double, Character> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMap(
            Map<Double, Character> map1, Map<Double, Character> map2,
            Map<Double, Character> map3, Map<Double, Character> map4,
            Map<Double, Character> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMap(
            Map<Double, Character> map1, Map<Double, Character> map2,
            int expectedSize) {
        UpdatableLHashSeparateKVDoubleCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMap(
            Map<Double, Character> map1, Map<Double, Character> map2,
            Map<Double, Character> map3, int expectedSize) {
        UpdatableLHashSeparateKVDoubleCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMap(
            Map<Double, Character> map1, Map<Double, Character> map2,
            Map<Double, Character> map3, Map<Double, Character> map4,
            int expectedSize) {
        UpdatableLHashSeparateKVDoubleCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMap(
            Map<Double, Character> map1, Map<Double, Character> map2,
            Map<Double, Character> map3, Map<Double, Character> map4,
            Map<Double, Character> map5, int expectedSize) {
        UpdatableLHashSeparateKVDoubleCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.DoubleCharConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.DoubleCharConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashSeparateKVDoubleCharMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.DoubleCharConsumer() {
             @Override
             public void accept(double k, char v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMap(
            double[] keys, char[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMap(
            double[] keys, char[] values, int expectedSize) {
        UpdatableLHashSeparateKVDoubleCharMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMap(
            Double[] keys, Character[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMap(
            Double[] keys, Character[] values, int expectedSize) {
        UpdatableLHashSeparateKVDoubleCharMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMap(
            Iterable<Double> keys, Iterable<Character> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMap(
            Iterable<Double> keys, Iterable<Character> values, int expectedSize) {
        UpdatableLHashSeparateKVDoubleCharMapGO map = newUpdatableMap(expectedSize);
        Iterator<Double> keysIt = keys.iterator();
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
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMapOf(
            double k1, char v1) {
        UpdatableLHashSeparateKVDoubleCharMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMapOf(
            double k1, char v1, double k2, char v2) {
        UpdatableLHashSeparateKVDoubleCharMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMapOf(
            double k1, char v1, double k2, char v2,
            double k3, char v3) {
        UpdatableLHashSeparateKVDoubleCharMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMapOf(
            double k1, char v1, double k2, char v2,
            double k3, char v3, double k4, char v4) {
        UpdatableLHashSeparateKVDoubleCharMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVDoubleCharMapGO newUpdatableMapOf(
            double k1, char v1, double k2, char v2,
            double k3, char v3, double k4, char v4,
            double k5, char v5) {
        UpdatableLHashSeparateKVDoubleCharMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMap(Map<Double, Character> map1,
            Map<Double, Character> map2, int expectedSize) {
        MutableLHashSeparateKVDoubleCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMap(Map<Double, Character> map1,
            Map<Double, Character> map2, Map<Double, Character> map3, int expectedSize) {
        MutableLHashSeparateKVDoubleCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMap(Map<Double, Character> map1,
            Map<Double, Character> map2, Map<Double, Character> map3,
            Map<Double, Character> map4, int expectedSize) {
        MutableLHashSeparateKVDoubleCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMap(Map<Double, Character> map1,
            Map<Double, Character> map2, Map<Double, Character> map3,
            Map<Double, Character> map4, Map<Double, Character> map5, int expectedSize) {
        MutableLHashSeparateKVDoubleCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMap(
            Consumer<net.openhft.koloboke.function.DoubleCharConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashSeparateKVDoubleCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMap(double[] keys,
            char[] values, int expectedSize) {
        MutableLHashSeparateKVDoubleCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMap(
            Double[] keys, Character[] values, int expectedSize) {
        MutableLHashSeparateKVDoubleCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMap(Iterable<Double> keys,
            Iterable<Character> values, int expectedSize) {
        MutableLHashSeparateKVDoubleCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMap(
            Map<Double, Character> map) {
        MutableLHashSeparateKVDoubleCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMap(Map<Double, Character> map1,
            Map<Double, Character> map2) {
        MutableLHashSeparateKVDoubleCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMap(Map<Double, Character> map1,
            Map<Double, Character> map2, Map<Double, Character> map3) {
        MutableLHashSeparateKVDoubleCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMap(Map<Double, Character> map1,
            Map<Double, Character> map2, Map<Double, Character> map3,
            Map<Double, Character> map4) {
        MutableLHashSeparateKVDoubleCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMap(Map<Double, Character> map1,
            Map<Double, Character> map2, Map<Double, Character> map3,
            Map<Double, Character> map4, Map<Double, Character> map5) {
        MutableLHashSeparateKVDoubleCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMap(
            Consumer<net.openhft.koloboke.function.DoubleCharConsumer> entriesSupplier
            ) {
        MutableLHashSeparateKVDoubleCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMap(double[] keys,
            char[] values) {
        MutableLHashSeparateKVDoubleCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMap(
            Double[] keys, Character[] values) {
        MutableLHashSeparateKVDoubleCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMap(Iterable<Double> keys,
            Iterable<Character> values) {
        MutableLHashSeparateKVDoubleCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMapOf(double k1, char v1) {
        MutableLHashSeparateKVDoubleCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMapOf(double k1, char v1,
             double k2, char v2) {
        MutableLHashSeparateKVDoubleCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMapOf(double k1, char v1,
             double k2, char v2, double k3, char v3) {
        MutableLHashSeparateKVDoubleCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMapOf(double k1, char v1,
             double k2, char v2, double k3, char v3,
             double k4, char v4) {
        MutableLHashSeparateKVDoubleCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newMutableMapOf(double k1, char v1,
             double k2, char v2, double k3, char v3,
             double k4, char v4, double k5, char v5) {
        MutableLHashSeparateKVDoubleCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMap(Map<Double, Character> map1,
            Map<Double, Character> map2, int expectedSize) {
        ImmutableLHashSeparateKVDoubleCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMap(Map<Double, Character> map1,
            Map<Double, Character> map2, Map<Double, Character> map3, int expectedSize) {
        ImmutableLHashSeparateKVDoubleCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMap(Map<Double, Character> map1,
            Map<Double, Character> map2, Map<Double, Character> map3,
            Map<Double, Character> map4, int expectedSize) {
        ImmutableLHashSeparateKVDoubleCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMap(Map<Double, Character> map1,
            Map<Double, Character> map2, Map<Double, Character> map3,
            Map<Double, Character> map4, Map<Double, Character> map5, int expectedSize) {
        ImmutableLHashSeparateKVDoubleCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.DoubleCharConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashSeparateKVDoubleCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMap(double[] keys,
            char[] values, int expectedSize) {
        ImmutableLHashSeparateKVDoubleCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMap(
            Double[] keys, Character[] values, int expectedSize) {
        ImmutableLHashSeparateKVDoubleCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMap(Iterable<Double> keys,
            Iterable<Character> values, int expectedSize) {
        ImmutableLHashSeparateKVDoubleCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMap(
            Map<Double, Character> map) {
        ImmutableLHashSeparateKVDoubleCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMap(Map<Double, Character> map1,
            Map<Double, Character> map2) {
        ImmutableLHashSeparateKVDoubleCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMap(Map<Double, Character> map1,
            Map<Double, Character> map2, Map<Double, Character> map3) {
        ImmutableLHashSeparateKVDoubleCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMap(Map<Double, Character> map1,
            Map<Double, Character> map2, Map<Double, Character> map3,
            Map<Double, Character> map4) {
        ImmutableLHashSeparateKVDoubleCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMap(Map<Double, Character> map1,
            Map<Double, Character> map2, Map<Double, Character> map3,
            Map<Double, Character> map4, Map<Double, Character> map5) {
        ImmutableLHashSeparateKVDoubleCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.DoubleCharConsumer> entriesSupplier
            ) {
        ImmutableLHashSeparateKVDoubleCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMap(double[] keys,
            char[] values) {
        ImmutableLHashSeparateKVDoubleCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMap(
            Double[] keys, Character[] values) {
        ImmutableLHashSeparateKVDoubleCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMap(Iterable<Double> keys,
            Iterable<Character> values) {
        ImmutableLHashSeparateKVDoubleCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMapOf(double k1, char v1) {
        ImmutableLHashSeparateKVDoubleCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMapOf(double k1, char v1,
             double k2, char v2) {
        ImmutableLHashSeparateKVDoubleCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMapOf(double k1, char v1,
             double k2, char v2, double k3, char v3) {
        ImmutableLHashSeparateKVDoubleCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMapOf(double k1, char v1,
             double k2, char v2, double k3, char v3,
             double k4, char v4) {
        ImmutableLHashSeparateKVDoubleCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleCharMap newImmutableMapOf(double k1, char v1,
             double k2, char v2, double k3, char v3,
             double k4, char v4, double k5, char v5) {
        ImmutableLHashSeparateKVDoubleCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

