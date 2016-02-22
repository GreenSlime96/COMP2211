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


public abstract class LHashParallelKVFloatFloatMapFactoryGO
        extends LHashParallelKVFloatFloatMapFactorySO {

    LHashParallelKVFloatFloatMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashParallelKVFloatFloatMapGO shrunk(
            UpdatableLHashParallelKVFloatFloatMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashParallelKVFloatFloatMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMap(
            Map<Float, Float> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMap(
            Map<Float, Float> map1, Map<Float, Float> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMap(
            Map<Float, Float> map1, Map<Float, Float> map2,
            Map<Float, Float> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMap(
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
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMap(
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
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMap(
            Map<Float, Float> map1, Map<Float, Float> map2,
            int expectedSize) {
        UpdatableLHashParallelKVFloatFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMap(
            Map<Float, Float> map1, Map<Float, Float> map2,
            Map<Float, Float> map3, int expectedSize) {
        UpdatableLHashParallelKVFloatFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMap(
            Map<Float, Float> map1, Map<Float, Float> map2,
            Map<Float, Float> map3, Map<Float, Float> map4,
            int expectedSize) {
        UpdatableLHashParallelKVFloatFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMap(
            Map<Float, Float> map1, Map<Float, Float> map2,
            Map<Float, Float> map3, Map<Float, Float> map4,
            Map<Float, Float> map5, int expectedSize) {
        UpdatableLHashParallelKVFloatFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.FloatFloatConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.FloatFloatConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashParallelKVFloatFloatMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMap(
            float[] keys, float[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMap(
            float[] keys, float[] values, int expectedSize) {
        UpdatableLHashParallelKVFloatFloatMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMap(
            Float[] keys, Float[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMap(
            Float[] keys, Float[] values, int expectedSize) {
        UpdatableLHashParallelKVFloatFloatMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMap(
            Iterable<Float> keys, Iterable<Float> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMap(
            Iterable<Float> keys, Iterable<Float> values, int expectedSize) {
        UpdatableLHashParallelKVFloatFloatMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMapOf(
            float k1, float v1) {
        UpdatableLHashParallelKVFloatFloatMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMapOf(
            float k1, float v1, float k2, float v2) {
        UpdatableLHashParallelKVFloatFloatMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMapOf(
            float k1, float v1, float k2, float v2,
            float k3, float v3) {
        UpdatableLHashParallelKVFloatFloatMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMapOf(
            float k1, float v1, float k2, float v2,
            float k3, float v3, float k4, float v4) {
        UpdatableLHashParallelKVFloatFloatMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVFloatFloatMapGO newUpdatableMapOf(
            float k1, float v1, float k2, float v2,
            float k3, float v3, float k4, float v4,
            float k5, float v5) {
        UpdatableLHashParallelKVFloatFloatMapGO map = newUpdatableMap(5);
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
        MutableLHashParallelKVFloatFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3, int expectedSize) {
        MutableLHashParallelKVFloatFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3,
            Map<Float, Float> map4, int expectedSize) {
        MutableLHashParallelKVFloatFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3,
            Map<Float, Float> map4, Map<Float, Float> map5, int expectedSize) {
        MutableLHashParallelKVFloatFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(
            Consumer<net.openhft.koloboke.function.FloatFloatConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(float[] keys,
            float[] values, int expectedSize) {
        MutableLHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(
            Float[] keys, Float[] values, int expectedSize) {
        MutableLHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(Iterable<Float> keys,
            Iterable<Float> values, int expectedSize) {
        MutableLHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(
            Map<Float, Float> map) {
        MutableLHashParallelKVFloatFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2) {
        MutableLHashParallelKVFloatFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3) {
        MutableLHashParallelKVFloatFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3,
            Map<Float, Float> map4) {
        MutableLHashParallelKVFloatFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3,
            Map<Float, Float> map4, Map<Float, Float> map5) {
        MutableLHashParallelKVFloatFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(
            Consumer<net.openhft.koloboke.function.FloatFloatConsumer> entriesSupplier
            ) {
        MutableLHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(float[] keys,
            float[] values) {
        MutableLHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(
            Float[] keys, Float[] values) {
        MutableLHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMap(Iterable<Float> keys,
            Iterable<Float> values) {
        MutableLHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMapOf(float k1, float v1) {
        MutableLHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMapOf(float k1, float v1,
             float k2, float v2) {
        MutableLHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMapOf(float k1, float v1,
             float k2, float v2, float k3, float v3) {
        MutableLHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMapOf(float k1, float v1,
             float k2, float v2, float k3, float v3,
             float k4, float v4) {
        MutableLHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newMutableMapOf(float k1, float v1,
             float k2, float v2, float k3, float v3,
             float k4, float v4, float k5, float v5) {
        MutableLHashParallelKVFloatFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, int expectedSize) {
        ImmutableLHashParallelKVFloatFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3, int expectedSize) {
        ImmutableLHashParallelKVFloatFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3,
            Map<Float, Float> map4, int expectedSize) {
        ImmutableLHashParallelKVFloatFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3,
            Map<Float, Float> map4, Map<Float, Float> map5, int expectedSize) {
        ImmutableLHashParallelKVFloatFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.FloatFloatConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(float[] keys,
            float[] values, int expectedSize) {
        ImmutableLHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(
            Float[] keys, Float[] values, int expectedSize) {
        ImmutableLHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(Iterable<Float> keys,
            Iterable<Float> values, int expectedSize) {
        ImmutableLHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(
            Map<Float, Float> map) {
        ImmutableLHashParallelKVFloatFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2) {
        ImmutableLHashParallelKVFloatFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3) {
        ImmutableLHashParallelKVFloatFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3,
            Map<Float, Float> map4) {
        ImmutableLHashParallelKVFloatFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(Map<Float, Float> map1,
            Map<Float, Float> map2, Map<Float, Float> map3,
            Map<Float, Float> map4, Map<Float, Float> map5) {
        ImmutableLHashParallelKVFloatFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.FloatFloatConsumer> entriesSupplier
            ) {
        ImmutableLHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(float[] keys,
            float[] values) {
        ImmutableLHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(
            Float[] keys, Float[] values) {
        ImmutableLHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMap(Iterable<Float> keys,
            Iterable<Float> values) {
        ImmutableLHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMapOf(float k1, float v1) {
        ImmutableLHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMapOf(float k1, float v1,
             float k2, float v2) {
        ImmutableLHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMapOf(float k1, float v1,
             float k2, float v2, float k3, float v3) {
        ImmutableLHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMapOf(float k1, float v1,
             float k2, float v2, float k3, float v3,
             float k4, float v4) {
        ImmutableLHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatFloatMap newImmutableMapOf(float k1, float v1,
             float k2, float v2, float k3, float v3,
             float k4, float v4, float k5, float v5) {
        ImmutableLHashParallelKVFloatFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

