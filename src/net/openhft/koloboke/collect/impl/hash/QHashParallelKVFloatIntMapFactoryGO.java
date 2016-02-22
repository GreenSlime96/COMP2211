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
import net.openhft.koloboke.collect.map.hash.HashFloatIntMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashFloatIntMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashParallelKVFloatIntMapFactoryGO
        extends QHashParallelKVFloatIntMapFactorySO {

    QHashParallelKVFloatIntMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    abstract HashFloatIntMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashFloatIntMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashFloatIntMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    @Override
    public final HashFloatIntMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
    }

    @Override
    public final HashFloatIntMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                );
    }


    @Override
    public String toString() {
        return "HashFloatIntMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashFloatIntMapFactory) {
            HashFloatIntMapFactory factory = (HashFloatIntMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashParallelKVFloatIntMapGO shrunk(
            UpdatableQHashParallelKVFloatIntMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashParallelKVFloatIntMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMap(
            Map<Float, Integer> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMap(
            Map<Float, Integer> map1, Map<Float, Integer> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMap(
            Map<Float, Integer> map1, Map<Float, Integer> map2,
            Map<Float, Integer> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMap(
            Map<Float, Integer> map1, Map<Float, Integer> map2,
            Map<Float, Integer> map3, Map<Float, Integer> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMap(
            Map<Float, Integer> map1, Map<Float, Integer> map2,
            Map<Float, Integer> map3, Map<Float, Integer> map4,
            Map<Float, Integer> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMap(
            Map<Float, Integer> map1, Map<Float, Integer> map2,
            int expectedSize) {
        UpdatableQHashParallelKVFloatIntMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMap(
            Map<Float, Integer> map1, Map<Float, Integer> map2,
            Map<Float, Integer> map3, int expectedSize) {
        UpdatableQHashParallelKVFloatIntMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMap(
            Map<Float, Integer> map1, Map<Float, Integer> map2,
            Map<Float, Integer> map3, Map<Float, Integer> map4,
            int expectedSize) {
        UpdatableQHashParallelKVFloatIntMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMap(
            Map<Float, Integer> map1, Map<Float, Integer> map2,
            Map<Float, Integer> map3, Map<Float, Integer> map4,
            Map<Float, Integer> map5, int expectedSize) {
        UpdatableQHashParallelKVFloatIntMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.FloatIntConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.FloatIntConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashParallelKVFloatIntMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.FloatIntConsumer() {
             @Override
             public void accept(float k, int v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMap(
            float[] keys, int[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMap(
            float[] keys, int[] values, int expectedSize) {
        UpdatableQHashParallelKVFloatIntMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMap(
            Float[] keys, Integer[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMap(
            Float[] keys, Integer[] values, int expectedSize) {
        UpdatableQHashParallelKVFloatIntMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMap(
            Iterable<Float> keys, Iterable<Integer> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMap(
            Iterable<Float> keys, Iterable<Integer> values, int expectedSize) {
        UpdatableQHashParallelKVFloatIntMapGO map = newUpdatableMap(expectedSize);
        Iterator<Float> keysIt = keys.iterator();
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
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMapOf(
            float k1, int v1) {
        UpdatableQHashParallelKVFloatIntMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMapOf(
            float k1, int v1, float k2, int v2) {
        UpdatableQHashParallelKVFloatIntMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMapOf(
            float k1, int v1, float k2, int v2,
            float k3, int v3) {
        UpdatableQHashParallelKVFloatIntMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMapOf(
            float k1, int v1, float k2, int v2,
            float k3, int v3, float k4, int v4) {
        UpdatableQHashParallelKVFloatIntMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatIntMapGO newUpdatableMapOf(
            float k1, int v1, float k2, int v2,
            float k3, int v3, float k4, int v4,
            float k5, int v5) {
        UpdatableQHashParallelKVFloatIntMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMap(Map<Float, Integer> map1,
            Map<Float, Integer> map2, int expectedSize) {
        MutableQHashParallelKVFloatIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMap(Map<Float, Integer> map1,
            Map<Float, Integer> map2, Map<Float, Integer> map3, int expectedSize) {
        MutableQHashParallelKVFloatIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMap(Map<Float, Integer> map1,
            Map<Float, Integer> map2, Map<Float, Integer> map3,
            Map<Float, Integer> map4, int expectedSize) {
        MutableQHashParallelKVFloatIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMap(Map<Float, Integer> map1,
            Map<Float, Integer> map2, Map<Float, Integer> map3,
            Map<Float, Integer> map4, Map<Float, Integer> map5, int expectedSize) {
        MutableQHashParallelKVFloatIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMap(
            Consumer<net.openhft.koloboke.function.FloatIntConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashParallelKVFloatIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMap(float[] keys,
            int[] values, int expectedSize) {
        MutableQHashParallelKVFloatIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMap(
            Float[] keys, Integer[] values, int expectedSize) {
        MutableQHashParallelKVFloatIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMap(Iterable<Float> keys,
            Iterable<Integer> values, int expectedSize) {
        MutableQHashParallelKVFloatIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMap(
            Map<Float, Integer> map) {
        MutableQHashParallelKVFloatIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMap(Map<Float, Integer> map1,
            Map<Float, Integer> map2) {
        MutableQHashParallelKVFloatIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMap(Map<Float, Integer> map1,
            Map<Float, Integer> map2, Map<Float, Integer> map3) {
        MutableQHashParallelKVFloatIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMap(Map<Float, Integer> map1,
            Map<Float, Integer> map2, Map<Float, Integer> map3,
            Map<Float, Integer> map4) {
        MutableQHashParallelKVFloatIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMap(Map<Float, Integer> map1,
            Map<Float, Integer> map2, Map<Float, Integer> map3,
            Map<Float, Integer> map4, Map<Float, Integer> map5) {
        MutableQHashParallelKVFloatIntMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMap(
            Consumer<net.openhft.koloboke.function.FloatIntConsumer> entriesSupplier
            ) {
        MutableQHashParallelKVFloatIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMap(float[] keys,
            int[] values) {
        MutableQHashParallelKVFloatIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMap(
            Float[] keys, Integer[] values) {
        MutableQHashParallelKVFloatIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMap(Iterable<Float> keys,
            Iterable<Integer> values) {
        MutableQHashParallelKVFloatIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMapOf(float k1, int v1) {
        MutableQHashParallelKVFloatIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMapOf(float k1, int v1,
             float k2, int v2) {
        MutableQHashParallelKVFloatIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMapOf(float k1, int v1,
             float k2, int v2, float k3, int v3) {
        MutableQHashParallelKVFloatIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMapOf(float k1, int v1,
             float k2, int v2, float k3, int v3,
             float k4, int v4) {
        MutableQHashParallelKVFloatIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newMutableMapOf(float k1, int v1,
             float k2, int v2, float k3, int v3,
             float k4, int v4, float k5, int v5) {
        MutableQHashParallelKVFloatIntMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMap(Map<Float, Integer> map1,
            Map<Float, Integer> map2, int expectedSize) {
        ImmutableQHashParallelKVFloatIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMap(Map<Float, Integer> map1,
            Map<Float, Integer> map2, Map<Float, Integer> map3, int expectedSize) {
        ImmutableQHashParallelKVFloatIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMap(Map<Float, Integer> map1,
            Map<Float, Integer> map2, Map<Float, Integer> map3,
            Map<Float, Integer> map4, int expectedSize) {
        ImmutableQHashParallelKVFloatIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMap(Map<Float, Integer> map1,
            Map<Float, Integer> map2, Map<Float, Integer> map3,
            Map<Float, Integer> map4, Map<Float, Integer> map5, int expectedSize) {
        ImmutableQHashParallelKVFloatIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.FloatIntConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashParallelKVFloatIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMap(float[] keys,
            int[] values, int expectedSize) {
        ImmutableQHashParallelKVFloatIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMap(
            Float[] keys, Integer[] values, int expectedSize) {
        ImmutableQHashParallelKVFloatIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMap(Iterable<Float> keys,
            Iterable<Integer> values, int expectedSize) {
        ImmutableQHashParallelKVFloatIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMap(
            Map<Float, Integer> map) {
        ImmutableQHashParallelKVFloatIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMap(Map<Float, Integer> map1,
            Map<Float, Integer> map2) {
        ImmutableQHashParallelKVFloatIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMap(Map<Float, Integer> map1,
            Map<Float, Integer> map2, Map<Float, Integer> map3) {
        ImmutableQHashParallelKVFloatIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMap(Map<Float, Integer> map1,
            Map<Float, Integer> map2, Map<Float, Integer> map3,
            Map<Float, Integer> map4) {
        ImmutableQHashParallelKVFloatIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMap(Map<Float, Integer> map1,
            Map<Float, Integer> map2, Map<Float, Integer> map3,
            Map<Float, Integer> map4, Map<Float, Integer> map5) {
        ImmutableQHashParallelKVFloatIntMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.FloatIntConsumer> entriesSupplier
            ) {
        ImmutableQHashParallelKVFloatIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMap(float[] keys,
            int[] values) {
        ImmutableQHashParallelKVFloatIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMap(
            Float[] keys, Integer[] values) {
        ImmutableQHashParallelKVFloatIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMap(Iterable<Float> keys,
            Iterable<Integer> values) {
        ImmutableQHashParallelKVFloatIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMapOf(float k1, int v1) {
        ImmutableQHashParallelKVFloatIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMapOf(float k1, int v1,
             float k2, int v2) {
        ImmutableQHashParallelKVFloatIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMapOf(float k1, int v1,
             float k2, int v2, float k3, int v3) {
        ImmutableQHashParallelKVFloatIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMapOf(float k1, int v1,
             float k2, int v2, float k3, int v3,
             float k4, int v4) {
        ImmutableQHashParallelKVFloatIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatIntMap newImmutableMapOf(float k1, int v1,
             float k2, int v2, float k3, int v3,
             float k4, int v4, float k5, int v5) {
        ImmutableQHashParallelKVFloatIntMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

