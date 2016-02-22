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
import net.openhft.koloboke.collect.map.hash.HashFloatCharMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashFloatCharMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashSeparateKVFloatCharMapFactoryGO
        extends QHashSeparateKVFloatCharMapFactorySO {

    QHashSeparateKVFloatCharMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    abstract HashFloatCharMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashFloatCharMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashFloatCharMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    @Override
    public final HashFloatCharMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
    }

    @Override
    public final HashFloatCharMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                );
    }


    @Override
    public String toString() {
        return "HashFloatCharMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashFloatCharMapFactory) {
            HashFloatCharMapFactory factory = (HashFloatCharMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashSeparateKVFloatCharMapGO shrunk(
            UpdatableQHashSeparateKVFloatCharMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashSeparateKVFloatCharMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMap(
            Map<Float, Character> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMap(
            Map<Float, Character> map1, Map<Float, Character> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMap(
            Map<Float, Character> map1, Map<Float, Character> map2,
            Map<Float, Character> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMap(
            Map<Float, Character> map1, Map<Float, Character> map2,
            Map<Float, Character> map3, Map<Float, Character> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMap(
            Map<Float, Character> map1, Map<Float, Character> map2,
            Map<Float, Character> map3, Map<Float, Character> map4,
            Map<Float, Character> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMap(
            Map<Float, Character> map1, Map<Float, Character> map2,
            int expectedSize) {
        UpdatableQHashSeparateKVFloatCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMap(
            Map<Float, Character> map1, Map<Float, Character> map2,
            Map<Float, Character> map3, int expectedSize) {
        UpdatableQHashSeparateKVFloatCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMap(
            Map<Float, Character> map1, Map<Float, Character> map2,
            Map<Float, Character> map3, Map<Float, Character> map4,
            int expectedSize) {
        UpdatableQHashSeparateKVFloatCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMap(
            Map<Float, Character> map1, Map<Float, Character> map2,
            Map<Float, Character> map3, Map<Float, Character> map4,
            Map<Float, Character> map5, int expectedSize) {
        UpdatableQHashSeparateKVFloatCharMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.FloatCharConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.FloatCharConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashSeparateKVFloatCharMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.FloatCharConsumer() {
             @Override
             public void accept(float k, char v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMap(
            float[] keys, char[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMap(
            float[] keys, char[] values, int expectedSize) {
        UpdatableQHashSeparateKVFloatCharMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMap(
            Float[] keys, Character[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMap(
            Float[] keys, Character[] values, int expectedSize) {
        UpdatableQHashSeparateKVFloatCharMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMap(
            Iterable<Float> keys, Iterable<Character> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMap(
            Iterable<Float> keys, Iterable<Character> values, int expectedSize) {
        UpdatableQHashSeparateKVFloatCharMapGO map = newUpdatableMap(expectedSize);
        Iterator<Float> keysIt = keys.iterator();
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
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMapOf(
            float k1, char v1) {
        UpdatableQHashSeparateKVFloatCharMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMapOf(
            float k1, char v1, float k2, char v2) {
        UpdatableQHashSeparateKVFloatCharMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMapOf(
            float k1, char v1, float k2, char v2,
            float k3, char v3) {
        UpdatableQHashSeparateKVFloatCharMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMapOf(
            float k1, char v1, float k2, char v2,
            float k3, char v3, float k4, char v4) {
        UpdatableQHashSeparateKVFloatCharMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatCharMapGO newUpdatableMapOf(
            float k1, char v1, float k2, char v2,
            float k3, char v3, float k4, char v4,
            float k5, char v5) {
        UpdatableQHashSeparateKVFloatCharMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMap(Map<Float, Character> map1,
            Map<Float, Character> map2, int expectedSize) {
        MutableQHashSeparateKVFloatCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMap(Map<Float, Character> map1,
            Map<Float, Character> map2, Map<Float, Character> map3, int expectedSize) {
        MutableQHashSeparateKVFloatCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMap(Map<Float, Character> map1,
            Map<Float, Character> map2, Map<Float, Character> map3,
            Map<Float, Character> map4, int expectedSize) {
        MutableQHashSeparateKVFloatCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMap(Map<Float, Character> map1,
            Map<Float, Character> map2, Map<Float, Character> map3,
            Map<Float, Character> map4, Map<Float, Character> map5, int expectedSize) {
        MutableQHashSeparateKVFloatCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMap(
            Consumer<net.openhft.koloboke.function.FloatCharConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashSeparateKVFloatCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMap(float[] keys,
            char[] values, int expectedSize) {
        MutableQHashSeparateKVFloatCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMap(
            Float[] keys, Character[] values, int expectedSize) {
        MutableQHashSeparateKVFloatCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMap(Iterable<Float> keys,
            Iterable<Character> values, int expectedSize) {
        MutableQHashSeparateKVFloatCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMap(
            Map<Float, Character> map) {
        MutableQHashSeparateKVFloatCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMap(Map<Float, Character> map1,
            Map<Float, Character> map2) {
        MutableQHashSeparateKVFloatCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMap(Map<Float, Character> map1,
            Map<Float, Character> map2, Map<Float, Character> map3) {
        MutableQHashSeparateKVFloatCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMap(Map<Float, Character> map1,
            Map<Float, Character> map2, Map<Float, Character> map3,
            Map<Float, Character> map4) {
        MutableQHashSeparateKVFloatCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMap(Map<Float, Character> map1,
            Map<Float, Character> map2, Map<Float, Character> map3,
            Map<Float, Character> map4, Map<Float, Character> map5) {
        MutableQHashSeparateKVFloatCharMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMap(
            Consumer<net.openhft.koloboke.function.FloatCharConsumer> entriesSupplier
            ) {
        MutableQHashSeparateKVFloatCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMap(float[] keys,
            char[] values) {
        MutableQHashSeparateKVFloatCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMap(
            Float[] keys, Character[] values) {
        MutableQHashSeparateKVFloatCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMap(Iterable<Float> keys,
            Iterable<Character> values) {
        MutableQHashSeparateKVFloatCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMapOf(float k1, char v1) {
        MutableQHashSeparateKVFloatCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMapOf(float k1, char v1,
             float k2, char v2) {
        MutableQHashSeparateKVFloatCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMapOf(float k1, char v1,
             float k2, char v2, float k3, char v3) {
        MutableQHashSeparateKVFloatCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMapOf(float k1, char v1,
             float k2, char v2, float k3, char v3,
             float k4, char v4) {
        MutableQHashSeparateKVFloatCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newMutableMapOf(float k1, char v1,
             float k2, char v2, float k3, char v3,
             float k4, char v4, float k5, char v5) {
        MutableQHashSeparateKVFloatCharMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMap(Map<Float, Character> map1,
            Map<Float, Character> map2, int expectedSize) {
        ImmutableQHashSeparateKVFloatCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMap(Map<Float, Character> map1,
            Map<Float, Character> map2, Map<Float, Character> map3, int expectedSize) {
        ImmutableQHashSeparateKVFloatCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMap(Map<Float, Character> map1,
            Map<Float, Character> map2, Map<Float, Character> map3,
            Map<Float, Character> map4, int expectedSize) {
        ImmutableQHashSeparateKVFloatCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMap(Map<Float, Character> map1,
            Map<Float, Character> map2, Map<Float, Character> map3,
            Map<Float, Character> map4, Map<Float, Character> map5, int expectedSize) {
        ImmutableQHashSeparateKVFloatCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.FloatCharConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashSeparateKVFloatCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMap(float[] keys,
            char[] values, int expectedSize) {
        ImmutableQHashSeparateKVFloatCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMap(
            Float[] keys, Character[] values, int expectedSize) {
        ImmutableQHashSeparateKVFloatCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMap(Iterable<Float> keys,
            Iterable<Character> values, int expectedSize) {
        ImmutableQHashSeparateKVFloatCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMap(
            Map<Float, Character> map) {
        ImmutableQHashSeparateKVFloatCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMap(Map<Float, Character> map1,
            Map<Float, Character> map2) {
        ImmutableQHashSeparateKVFloatCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMap(Map<Float, Character> map1,
            Map<Float, Character> map2, Map<Float, Character> map3) {
        ImmutableQHashSeparateKVFloatCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMap(Map<Float, Character> map1,
            Map<Float, Character> map2, Map<Float, Character> map3,
            Map<Float, Character> map4) {
        ImmutableQHashSeparateKVFloatCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMap(Map<Float, Character> map1,
            Map<Float, Character> map2, Map<Float, Character> map3,
            Map<Float, Character> map4, Map<Float, Character> map5) {
        ImmutableQHashSeparateKVFloatCharMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.FloatCharConsumer> entriesSupplier
            ) {
        ImmutableQHashSeparateKVFloatCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMap(float[] keys,
            char[] values) {
        ImmutableQHashSeparateKVFloatCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMap(
            Float[] keys, Character[] values) {
        ImmutableQHashSeparateKVFloatCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMap(Iterable<Float> keys,
            Iterable<Character> values) {
        ImmutableQHashSeparateKVFloatCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMapOf(float k1, char v1) {
        ImmutableQHashSeparateKVFloatCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMapOf(float k1, char v1,
             float k2, char v2) {
        ImmutableQHashSeparateKVFloatCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMapOf(float k1, char v1,
             float k2, char v2, float k3, char v3) {
        ImmutableQHashSeparateKVFloatCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMapOf(float k1, char v1,
             float k2, char v2, float k3, char v3,
             float k4, char v4) {
        ImmutableQHashSeparateKVFloatCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatCharMap newImmutableMapOf(float k1, char v1,
             float k2, char v2, float k3, char v3,
             float k4, char v4, float k5, char v5) {
        ImmutableQHashSeparateKVFloatCharMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

