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
import net.openhft.koloboke.collect.map.hash.HashFloatByteMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashFloatByteMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashSeparateKVFloatByteMapFactoryGO
        extends QHashSeparateKVFloatByteMapFactorySO {

    QHashSeparateKVFloatByteMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    abstract HashFloatByteMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashFloatByteMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashFloatByteMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    @Override
    public final HashFloatByteMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
    }

    @Override
    public final HashFloatByteMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                );
    }


    @Override
    public String toString() {
        return "HashFloatByteMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashFloatByteMapFactory) {
            HashFloatByteMapFactory factory = (HashFloatByteMapFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory) &&
                    // boxing to treat NaNs correctly
                   ((Byte) getDefaultValue()).equals(factory.getDefaultValue())
                    ;
        } else {
            return false;
        }
    }

    @Override
    public byte getDefaultValue() {
        return (byte) 0;
    }

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashSeparateKVFloatByteMapGO shrunk(
            UpdatableQHashSeparateKVFloatByteMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashSeparateKVFloatByteMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMap(
            Map<Float, Byte> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMap(
            Map<Float, Byte> map1, Map<Float, Byte> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMap(
            Map<Float, Byte> map1, Map<Float, Byte> map2,
            Map<Float, Byte> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMap(
            Map<Float, Byte> map1, Map<Float, Byte> map2,
            Map<Float, Byte> map3, Map<Float, Byte> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMap(
            Map<Float, Byte> map1, Map<Float, Byte> map2,
            Map<Float, Byte> map3, Map<Float, Byte> map4,
            Map<Float, Byte> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMap(
            Map<Float, Byte> map1, Map<Float, Byte> map2,
            int expectedSize) {
        UpdatableQHashSeparateKVFloatByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMap(
            Map<Float, Byte> map1, Map<Float, Byte> map2,
            Map<Float, Byte> map3, int expectedSize) {
        UpdatableQHashSeparateKVFloatByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMap(
            Map<Float, Byte> map1, Map<Float, Byte> map2,
            Map<Float, Byte> map3, Map<Float, Byte> map4,
            int expectedSize) {
        UpdatableQHashSeparateKVFloatByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMap(
            Map<Float, Byte> map1, Map<Float, Byte> map2,
            Map<Float, Byte> map3, Map<Float, Byte> map4,
            Map<Float, Byte> map5, int expectedSize) {
        UpdatableQHashSeparateKVFloatByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.FloatByteConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.FloatByteConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashSeparateKVFloatByteMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.FloatByteConsumer() {
             @Override
             public void accept(float k, byte v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMap(
            float[] keys, byte[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMap(
            float[] keys, byte[] values, int expectedSize) {
        UpdatableQHashSeparateKVFloatByteMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMap(
            Float[] keys, Byte[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMap(
            Float[] keys, Byte[] values, int expectedSize) {
        UpdatableQHashSeparateKVFloatByteMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMap(
            Iterable<Float> keys, Iterable<Byte> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMap(
            Iterable<Float> keys, Iterable<Byte> values, int expectedSize) {
        UpdatableQHashSeparateKVFloatByteMapGO map = newUpdatableMap(expectedSize);
        Iterator<Float> keysIt = keys.iterator();
        Iterator<Byte> valuesIt = values.iterator();
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
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMapOf(
            float k1, byte v1) {
        UpdatableQHashSeparateKVFloatByteMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMapOf(
            float k1, byte v1, float k2, byte v2) {
        UpdatableQHashSeparateKVFloatByteMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMapOf(
            float k1, byte v1, float k2, byte v2,
            float k3, byte v3) {
        UpdatableQHashSeparateKVFloatByteMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMapOf(
            float k1, byte v1, float k2, byte v2,
            float k3, byte v3, float k4, byte v4) {
        UpdatableQHashSeparateKVFloatByteMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVFloatByteMapGO newUpdatableMapOf(
            float k1, byte v1, float k2, byte v2,
            float k3, byte v3, float k4, byte v4,
            float k5, byte v5) {
        UpdatableQHashSeparateKVFloatByteMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMap(Map<Float, Byte> map1,
            Map<Float, Byte> map2, int expectedSize) {
        MutableQHashSeparateKVFloatByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMap(Map<Float, Byte> map1,
            Map<Float, Byte> map2, Map<Float, Byte> map3, int expectedSize) {
        MutableQHashSeparateKVFloatByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMap(Map<Float, Byte> map1,
            Map<Float, Byte> map2, Map<Float, Byte> map3,
            Map<Float, Byte> map4, int expectedSize) {
        MutableQHashSeparateKVFloatByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMap(Map<Float, Byte> map1,
            Map<Float, Byte> map2, Map<Float, Byte> map3,
            Map<Float, Byte> map4, Map<Float, Byte> map5, int expectedSize) {
        MutableQHashSeparateKVFloatByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMap(
            Consumer<net.openhft.koloboke.function.FloatByteConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashSeparateKVFloatByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMap(float[] keys,
            byte[] values, int expectedSize) {
        MutableQHashSeparateKVFloatByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMap(
            Float[] keys, Byte[] values, int expectedSize) {
        MutableQHashSeparateKVFloatByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMap(Iterable<Float> keys,
            Iterable<Byte> values, int expectedSize) {
        MutableQHashSeparateKVFloatByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMap(
            Map<Float, Byte> map) {
        MutableQHashSeparateKVFloatByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMap(Map<Float, Byte> map1,
            Map<Float, Byte> map2) {
        MutableQHashSeparateKVFloatByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMap(Map<Float, Byte> map1,
            Map<Float, Byte> map2, Map<Float, Byte> map3) {
        MutableQHashSeparateKVFloatByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMap(Map<Float, Byte> map1,
            Map<Float, Byte> map2, Map<Float, Byte> map3,
            Map<Float, Byte> map4) {
        MutableQHashSeparateKVFloatByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMap(Map<Float, Byte> map1,
            Map<Float, Byte> map2, Map<Float, Byte> map3,
            Map<Float, Byte> map4, Map<Float, Byte> map5) {
        MutableQHashSeparateKVFloatByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMap(
            Consumer<net.openhft.koloboke.function.FloatByteConsumer> entriesSupplier
            ) {
        MutableQHashSeparateKVFloatByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMap(float[] keys,
            byte[] values) {
        MutableQHashSeparateKVFloatByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMap(
            Float[] keys, Byte[] values) {
        MutableQHashSeparateKVFloatByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMap(Iterable<Float> keys,
            Iterable<Byte> values) {
        MutableQHashSeparateKVFloatByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMapOf(float k1, byte v1) {
        MutableQHashSeparateKVFloatByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMapOf(float k1, byte v1,
             float k2, byte v2) {
        MutableQHashSeparateKVFloatByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMapOf(float k1, byte v1,
             float k2, byte v2, float k3, byte v3) {
        MutableQHashSeparateKVFloatByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMapOf(float k1, byte v1,
             float k2, byte v2, float k3, byte v3,
             float k4, byte v4) {
        MutableQHashSeparateKVFloatByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newMutableMapOf(float k1, byte v1,
             float k2, byte v2, float k3, byte v3,
             float k4, byte v4, float k5, byte v5) {
        MutableQHashSeparateKVFloatByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMap(Map<Float, Byte> map1,
            Map<Float, Byte> map2, int expectedSize) {
        ImmutableQHashSeparateKVFloatByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMap(Map<Float, Byte> map1,
            Map<Float, Byte> map2, Map<Float, Byte> map3, int expectedSize) {
        ImmutableQHashSeparateKVFloatByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMap(Map<Float, Byte> map1,
            Map<Float, Byte> map2, Map<Float, Byte> map3,
            Map<Float, Byte> map4, int expectedSize) {
        ImmutableQHashSeparateKVFloatByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMap(Map<Float, Byte> map1,
            Map<Float, Byte> map2, Map<Float, Byte> map3,
            Map<Float, Byte> map4, Map<Float, Byte> map5, int expectedSize) {
        ImmutableQHashSeparateKVFloatByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.FloatByteConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashSeparateKVFloatByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMap(float[] keys,
            byte[] values, int expectedSize) {
        ImmutableQHashSeparateKVFloatByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMap(
            Float[] keys, Byte[] values, int expectedSize) {
        ImmutableQHashSeparateKVFloatByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMap(Iterable<Float> keys,
            Iterable<Byte> values, int expectedSize) {
        ImmutableQHashSeparateKVFloatByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMap(
            Map<Float, Byte> map) {
        ImmutableQHashSeparateKVFloatByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMap(Map<Float, Byte> map1,
            Map<Float, Byte> map2) {
        ImmutableQHashSeparateKVFloatByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMap(Map<Float, Byte> map1,
            Map<Float, Byte> map2, Map<Float, Byte> map3) {
        ImmutableQHashSeparateKVFloatByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMap(Map<Float, Byte> map1,
            Map<Float, Byte> map2, Map<Float, Byte> map3,
            Map<Float, Byte> map4) {
        ImmutableQHashSeparateKVFloatByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMap(Map<Float, Byte> map1,
            Map<Float, Byte> map2, Map<Float, Byte> map3,
            Map<Float, Byte> map4, Map<Float, Byte> map5) {
        ImmutableQHashSeparateKVFloatByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.FloatByteConsumer> entriesSupplier
            ) {
        ImmutableQHashSeparateKVFloatByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMap(float[] keys,
            byte[] values) {
        ImmutableQHashSeparateKVFloatByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMap(
            Float[] keys, Byte[] values) {
        ImmutableQHashSeparateKVFloatByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMap(Iterable<Float> keys,
            Iterable<Byte> values) {
        ImmutableQHashSeparateKVFloatByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMapOf(float k1, byte v1) {
        ImmutableQHashSeparateKVFloatByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMapOf(float k1, byte v1,
             float k2, byte v2) {
        ImmutableQHashSeparateKVFloatByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMapOf(float k1, byte v1,
             float k2, byte v2, float k3, byte v3) {
        ImmutableQHashSeparateKVFloatByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMapOf(float k1, byte v1,
             float k2, byte v2, float k3, byte v3,
             float k4, byte v4) {
        ImmutableQHashSeparateKVFloatByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatByteMap newImmutableMapOf(float k1, byte v1,
             float k2, byte v2, float k3, byte v3,
             float k4, byte v4, float k5, byte v5) {
        ImmutableQHashSeparateKVFloatByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

