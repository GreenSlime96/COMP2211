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
import net.openhft.koloboke.collect.map.hash.HashFloatLongMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashFloatLongMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashSeparateKVFloatLongMapFactoryGO
        extends QHashSeparateKVFloatLongMapFactorySO {

    QHashSeparateKVFloatLongMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    abstract HashFloatLongMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashFloatLongMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashFloatLongMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    @Override
    public final HashFloatLongMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
    }

    @Override
    public final HashFloatLongMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                );
    }


    @Override
    public String toString() {
        return "HashFloatLongMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashFloatLongMapFactory) {
            HashFloatLongMapFactory factory = (HashFloatLongMapFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory) &&
                    // boxing to treat NaNs correctly
                   ((Long) getDefaultValue()).equals(factory.getDefaultValue())
                    ;
        } else {
            return false;
        }
    }

    @Override
    public long getDefaultValue() {
        return 0L;
    }

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashSeparateKVFloatLongMapGO shrunk(
            UpdatableQHashSeparateKVFloatLongMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashSeparateKVFloatLongMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMap(
            Map<Float, Long> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMap(
            Map<Float, Long> map1, Map<Float, Long> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMap(
            Map<Float, Long> map1, Map<Float, Long> map2,
            Map<Float, Long> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMap(
            Map<Float, Long> map1, Map<Float, Long> map2,
            Map<Float, Long> map3, Map<Float, Long> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMap(
            Map<Float, Long> map1, Map<Float, Long> map2,
            Map<Float, Long> map3, Map<Float, Long> map4,
            Map<Float, Long> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMap(
            Map<Float, Long> map1, Map<Float, Long> map2,
            int expectedSize) {
        UpdatableQHashSeparateKVFloatLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMap(
            Map<Float, Long> map1, Map<Float, Long> map2,
            Map<Float, Long> map3, int expectedSize) {
        UpdatableQHashSeparateKVFloatLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMap(
            Map<Float, Long> map1, Map<Float, Long> map2,
            Map<Float, Long> map3, Map<Float, Long> map4,
            int expectedSize) {
        UpdatableQHashSeparateKVFloatLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMap(
            Map<Float, Long> map1, Map<Float, Long> map2,
            Map<Float, Long> map3, Map<Float, Long> map4,
            Map<Float, Long> map5, int expectedSize) {
        UpdatableQHashSeparateKVFloatLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.FloatLongConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.FloatLongConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashSeparateKVFloatLongMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.FloatLongConsumer() {
             @Override
             public void accept(float k, long v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMap(
            float[] keys, long[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMap(
            float[] keys, long[] values, int expectedSize) {
        UpdatableQHashSeparateKVFloatLongMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMap(
            Float[] keys, Long[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMap(
            Float[] keys, Long[] values, int expectedSize) {
        UpdatableQHashSeparateKVFloatLongMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMap(
            Iterable<Float> keys, Iterable<Long> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMap(
            Iterable<Float> keys, Iterable<Long> values, int expectedSize) {
        UpdatableQHashSeparateKVFloatLongMapGO map = newUpdatableMap(expectedSize);
        Iterator<Float> keysIt = keys.iterator();
        Iterator<Long> valuesIt = values.iterator();
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
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMapOf(
            float k1, long v1) {
        UpdatableQHashSeparateKVFloatLongMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMapOf(
            float k1, long v1, float k2, long v2) {
        UpdatableQHashSeparateKVFloatLongMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMapOf(
            float k1, long v1, float k2, long v2,
            float k3, long v3) {
        UpdatableQHashSeparateKVFloatLongMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMapOf(
            float k1, long v1, float k2, long v2,
            float k3, long v3, float k4, long v4) {
        UpdatableQHashSeparateKVFloatLongMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatLongMapGO newUpdatableMapOf(
            float k1, long v1, float k2, long v2,
            float k3, long v3, float k4, long v4,
            float k5, long v5) {
        UpdatableQHashSeparateKVFloatLongMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMap(Map<Float, Long> map1,
            Map<Float, Long> map2, int expectedSize) {
        MutableQHashSeparateKVFloatLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMap(Map<Float, Long> map1,
            Map<Float, Long> map2, Map<Float, Long> map3, int expectedSize) {
        MutableQHashSeparateKVFloatLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMap(Map<Float, Long> map1,
            Map<Float, Long> map2, Map<Float, Long> map3,
            Map<Float, Long> map4, int expectedSize) {
        MutableQHashSeparateKVFloatLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMap(Map<Float, Long> map1,
            Map<Float, Long> map2, Map<Float, Long> map3,
            Map<Float, Long> map4, Map<Float, Long> map5, int expectedSize) {
        MutableQHashSeparateKVFloatLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMap(
            Consumer<net.openhft.koloboke.function.FloatLongConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashSeparateKVFloatLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMap(float[] keys,
            long[] values, int expectedSize) {
        MutableQHashSeparateKVFloatLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMap(
            Float[] keys, Long[] values, int expectedSize) {
        MutableQHashSeparateKVFloatLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMap(Iterable<Float> keys,
            Iterable<Long> values, int expectedSize) {
        MutableQHashSeparateKVFloatLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMap(
            Map<Float, Long> map) {
        MutableQHashSeparateKVFloatLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMap(Map<Float, Long> map1,
            Map<Float, Long> map2) {
        MutableQHashSeparateKVFloatLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMap(Map<Float, Long> map1,
            Map<Float, Long> map2, Map<Float, Long> map3) {
        MutableQHashSeparateKVFloatLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMap(Map<Float, Long> map1,
            Map<Float, Long> map2, Map<Float, Long> map3,
            Map<Float, Long> map4) {
        MutableQHashSeparateKVFloatLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMap(Map<Float, Long> map1,
            Map<Float, Long> map2, Map<Float, Long> map3,
            Map<Float, Long> map4, Map<Float, Long> map5) {
        MutableQHashSeparateKVFloatLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMap(
            Consumer<net.openhft.koloboke.function.FloatLongConsumer> entriesSupplier
            ) {
        MutableQHashSeparateKVFloatLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMap(float[] keys,
            long[] values) {
        MutableQHashSeparateKVFloatLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMap(
            Float[] keys, Long[] values) {
        MutableQHashSeparateKVFloatLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMap(Iterable<Float> keys,
            Iterable<Long> values) {
        MutableQHashSeparateKVFloatLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMapOf(float k1, long v1) {
        MutableQHashSeparateKVFloatLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMapOf(float k1, long v1,
             float k2, long v2) {
        MutableQHashSeparateKVFloatLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMapOf(float k1, long v1,
             float k2, long v2, float k3, long v3) {
        MutableQHashSeparateKVFloatLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMapOf(float k1, long v1,
             float k2, long v2, float k3, long v3,
             float k4, long v4) {
        MutableQHashSeparateKVFloatLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newMutableMapOf(float k1, long v1,
             float k2, long v2, float k3, long v3,
             float k4, long v4, float k5, long v5) {
        MutableQHashSeparateKVFloatLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMap(Map<Float, Long> map1,
            Map<Float, Long> map2, int expectedSize) {
        ImmutableQHashSeparateKVFloatLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMap(Map<Float, Long> map1,
            Map<Float, Long> map2, Map<Float, Long> map3, int expectedSize) {
        ImmutableQHashSeparateKVFloatLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMap(Map<Float, Long> map1,
            Map<Float, Long> map2, Map<Float, Long> map3,
            Map<Float, Long> map4, int expectedSize) {
        ImmutableQHashSeparateKVFloatLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMap(Map<Float, Long> map1,
            Map<Float, Long> map2, Map<Float, Long> map3,
            Map<Float, Long> map4, Map<Float, Long> map5, int expectedSize) {
        ImmutableQHashSeparateKVFloatLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.FloatLongConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashSeparateKVFloatLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMap(float[] keys,
            long[] values, int expectedSize) {
        ImmutableQHashSeparateKVFloatLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMap(
            Float[] keys, Long[] values, int expectedSize) {
        ImmutableQHashSeparateKVFloatLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMap(Iterable<Float> keys,
            Iterable<Long> values, int expectedSize) {
        ImmutableQHashSeparateKVFloatLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMap(
            Map<Float, Long> map) {
        ImmutableQHashSeparateKVFloatLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMap(Map<Float, Long> map1,
            Map<Float, Long> map2) {
        ImmutableQHashSeparateKVFloatLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMap(Map<Float, Long> map1,
            Map<Float, Long> map2, Map<Float, Long> map3) {
        ImmutableQHashSeparateKVFloatLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMap(Map<Float, Long> map1,
            Map<Float, Long> map2, Map<Float, Long> map3,
            Map<Float, Long> map4) {
        ImmutableQHashSeparateKVFloatLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMap(Map<Float, Long> map1,
            Map<Float, Long> map2, Map<Float, Long> map3,
            Map<Float, Long> map4, Map<Float, Long> map5) {
        ImmutableQHashSeparateKVFloatLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.FloatLongConsumer> entriesSupplier
            ) {
        ImmutableQHashSeparateKVFloatLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMap(float[] keys,
            long[] values) {
        ImmutableQHashSeparateKVFloatLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMap(
            Float[] keys, Long[] values) {
        ImmutableQHashSeparateKVFloatLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMap(Iterable<Float> keys,
            Iterable<Long> values) {
        ImmutableQHashSeparateKVFloatLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMapOf(float k1, long v1) {
        ImmutableQHashSeparateKVFloatLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMapOf(float k1, long v1,
             float k2, long v2) {
        ImmutableQHashSeparateKVFloatLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMapOf(float k1, long v1,
             float k2, long v2, float k3, long v3) {
        ImmutableQHashSeparateKVFloatLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMapOf(float k1, long v1,
             float k2, long v2, float k3, long v3,
             float k4, long v4) {
        ImmutableQHashSeparateKVFloatLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatLongMap newImmutableMapOf(float k1, long v1,
             float k2, long v2, float k3, long v3,
             float k4, long v4, float k5, long v5) {
        ImmutableQHashSeparateKVFloatLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}
