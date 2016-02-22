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
import net.openhft.koloboke.collect.map.hash.HashFloatFloatMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashFloatFloatMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashParallelKVFloatFloatMapFactoryGO
        extends QHashParallelKVFloatFloatMapFactorySO {

    QHashParallelKVFloatFloatMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    abstract HashFloatFloatMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashFloatFloatMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashFloatFloatMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    @Override
    public final HashFloatFloatMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
    }

    @Override
    public final HashFloatFloatMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                );
    }


    @Override
    public String toString() {
        return "HashFloatFloatMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashFloatFloatMapFactory) {
            HashFloatFloatMapFactory factory = (HashFloatFloatMapFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory) &&
                    // boxing to treat NaNs correctly
                   ((Float) getDefaultValue()).equals(factory.getDefaultValue())
                    ;
        } else {
            return false;
        }
    }

    @Override
    public float getDefaultValue() {
        return 0.0f;
    }

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashParallelKVFloatFloatMapGO shrunk(
            UpdatableQHashParallelKVFloatFloatMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashParallelKVFloatFloatMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMap(
            Map<Float, Float> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMap(
            Map<Float, Float> map1, Map<Float, Float> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMap(
            Map<Float, Float> map1, Map<Float, Float> map2,
            Map<Float, Float> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMap(
            Map<Float, Float> map1, Map<Float, Float> map2,
            Map<Float, Float> map3, Map<Float, Float> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMap(
            Map<Float, Float> map1, Map<Float, Float> map2,
            Map<Float, Float> map3, Map<Float, Float> map4,
            Map<Float, Float> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMap(
            Map<Float, Float> map1, Map<Float, Float> map2,
            int expectedSize) {
        UpdatableQHashParallelKVFloatFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMap(
            Map<Float, Float> map1, Map<Float, Float> map2,
            Map<Float, Float> map3, int expectedSize) {
        UpdatableQHashParallelKVFloatFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMap(
            Map<Float, Float> map1, Map<Float, Float> map2,
            Map<Float, Float> map3, Map<Float, Float> map4,
            int expectedSize) {
        UpdatableQHashParallelKVFloatFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMap(
            Map<Float, Float> map1, Map<Float, Float> map2,
            Map<Float, Float> map3, Map<Float, Float> map4,
            Map<Float, Float> map5, int expectedSize) {
        UpdatableQHashParallelKVFloatFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.FloatFloatConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.FloatFloatConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashParallelKVFloatFloatMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.FloatFloatConsumer() {
             @Override
             public void accept(float k, float v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMap(
            float[] keys, float[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMap(
            float[] keys, float[] values, int expectedSize) {
        UpdatableQHashParallelKVFloatFloatMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMap(
            Float[] keys, Float[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMap(
            Float[] keys, Float[] values, int expectedSize) {
        UpdatableQHashParallelKVFloatFloatMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMap(
            Iterable<Float> keys, Iterable<Float> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMap(
            Iterable<Float> keys, Iterable<Float> values, int expectedSize) {
        UpdatableQHashParallelKVFloatFloatMapGO map = newUpdatableMap(expectedSize);
        Iterator<Float> keysIt = keys.iterator();
        Iterator<Float> valuesIt = values.iterator();
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
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMapOf(
            float k1, float v1) {
        UpdatableQHashParallelKVFloatFloatMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMapOf(
            float k1, float v1, float k2, float v2) {
        UpdatableQHashParallelKVFloatFloatMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMapOf(
            float k1, float v1, float k2, float v2,
            float k3, float v3) {
        UpdatableQHashParallelKVFloatFloatMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMapOf(
            float k1, float v1, float k2, float v2,
            float k3, float v3, float k4, float v4) {
        UpdatableQHashParallelKVFloatFloatMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashParallelKVFloatFloatMapGO newUpdatableMapOf(
            float k1, float v1, float k2, float v2,
            float k3, float v3, float k4, float v4,
            float k5, float v5) {
        UpdatableQHashParallelKVFloatFloatMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, int expectedSize) {
        MutableQHashParallelKVFloatFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3, int expectedSize) {
        MutableQHashParallelKVFloatFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3,
            Map<Float, Float> map4, int expectedSize) {
        MutableQHashParallelKVFloatFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3,
            Map<Float, Float> map4, Map<Float, Float> map5, int expectedSize) {
        MutableQHashParallelKVFloatFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(
            Consumer<net.openhft.koloboke.function.FloatFloatConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(float[] keys,
            float[] values, int expectedSize) {
        MutableQHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(
            Float[] keys, Float[] values, int expectedSize) {
        MutableQHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(Iterable<Float> keys,
            Iterable<Float> values, int expectedSize) {
        MutableQHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(
            Map<Float, Float> map) {
        MutableQHashParallelKVFloatFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2) {
        MutableQHashParallelKVFloatFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3) {
        MutableQHashParallelKVFloatFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3,
            Map<Float, Float> map4) {
        MutableQHashParallelKVFloatFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3,
            Map<Float, Float> map4, Map<Float, Float> map5) {
        MutableQHashParallelKVFloatFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(
            Consumer<net.openhft.koloboke.function.FloatFloatConsumer> entriesSupplier
            ) {
        MutableQHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(float[] keys,
            float[] values) {
        MutableQHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(
            Float[] keys, Float[] values) {
        MutableQHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(Iterable<Float> keys,
            Iterable<Float> values) {
        MutableQHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMapOf(float k1, float v1) {
        MutableQHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMapOf(float k1, float v1,
             float k2, float v2) {
        MutableQHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMapOf(float k1, float v1,
             float k2, float v2, float k3, float v3) {
        MutableQHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMapOf(float k1, float v1,
             float k2, float v2, float k3, float v3,
             float k4, float v4) {
        MutableQHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMapOf(float k1, float v1,
             float k2, float v2, float k3, float v3,
             float k4, float v4, float k5, float v5) {
        MutableQHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, int expectedSize) {
        ImmutableQHashParallelKVFloatFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3, int expectedSize) {
        ImmutableQHashParallelKVFloatFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3,
            Map<Float, Float> map4, int expectedSize) {
        ImmutableQHashParallelKVFloatFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3,
            Map<Float, Float> map4, Map<Float, Float> map5, int expectedSize) {
        ImmutableQHashParallelKVFloatFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.FloatFloatConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(float[] keys,
            float[] values, int expectedSize) {
        ImmutableQHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(
            Float[] keys, Float[] values, int expectedSize) {
        ImmutableQHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(Iterable<Float> keys,
            Iterable<Float> values, int expectedSize) {
        ImmutableQHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(
            Map<Float, Float> map) {
        ImmutableQHashParallelKVFloatFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2) {
        ImmutableQHashParallelKVFloatFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3) {
        ImmutableQHashParallelKVFloatFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3,
            Map<Float, Float> map4) {
        ImmutableQHashParallelKVFloatFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3,
            Map<Float, Float> map4, Map<Float, Float> map5) {
        ImmutableQHashParallelKVFloatFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.FloatFloatConsumer> entriesSupplier
            ) {
        ImmutableQHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(float[] keys,
            float[] values) {
        ImmutableQHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(
            Float[] keys, Float[] values) {
        ImmutableQHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(Iterable<Float> keys,
            Iterable<Float> values) {
        ImmutableQHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMapOf(float k1, float v1) {
        ImmutableQHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMapOf(float k1, float v1,
             float k2, float v2) {
        ImmutableQHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMapOf(float k1, float v1,
             float k2, float v2, float k3, float v3) {
        ImmutableQHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMapOf(float k1, float v1,
             float k2, float v2, float k3, float v3,
             float k4, float v4) {
        ImmutableQHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMapOf(float k1, float v1,
             float k2, float v2, float k3, float v3,
             float k4, float v4, float k5, float v5) {
        ImmutableQHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

