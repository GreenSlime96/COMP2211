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
import net.openhft.koloboke.collect.map.hash.HashFloatShortMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashFloatShortMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashSeparateKVFloatShortMapFactoryGO
        extends LHashSeparateKVFloatShortMapFactorySO {

    LHashSeparateKVFloatShortMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    abstract HashFloatShortMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashFloatShortMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashFloatShortMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    @Override
    public final HashFloatShortMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
    }

    @Override
    public final HashFloatShortMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                );
    }


    @Override
    public String toString() {
        return "HashFloatShortMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashFloatShortMapFactory) {
            HashFloatShortMapFactory factory = (HashFloatShortMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashSeparateKVFloatShortMapGO shrunk(
            UpdatableLHashSeparateKVFloatShortMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashSeparateKVFloatShortMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMap(
            Map<Float, Short> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMap(
            Map<Float, Short> map1, Map<Float, Short> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMap(
            Map<Float, Short> map1, Map<Float, Short> map2,
            Map<Float, Short> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMap(
            Map<Float, Short> map1, Map<Float, Short> map2,
            Map<Float, Short> map3, Map<Float, Short> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMap(
            Map<Float, Short> map1, Map<Float, Short> map2,
            Map<Float, Short> map3, Map<Float, Short> map4,
            Map<Float, Short> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMap(
            Map<Float, Short> map1, Map<Float, Short> map2,
            int expectedSize) {
        UpdatableLHashSeparateKVFloatShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMap(
            Map<Float, Short> map1, Map<Float, Short> map2,
            Map<Float, Short> map3, int expectedSize) {
        UpdatableLHashSeparateKVFloatShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMap(
            Map<Float, Short> map1, Map<Float, Short> map2,
            Map<Float, Short> map3, Map<Float, Short> map4,
            int expectedSize) {
        UpdatableLHashSeparateKVFloatShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMap(
            Map<Float, Short> map1, Map<Float, Short> map2,
            Map<Float, Short> map3, Map<Float, Short> map4,
            Map<Float, Short> map5, int expectedSize) {
        UpdatableLHashSeparateKVFloatShortMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.FloatShortConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.FloatShortConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashSeparateKVFloatShortMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.FloatShortConsumer() {
             @Override
             public void accept(float k, short v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMap(
            float[] keys, short[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMap(
            float[] keys, short[] values, int expectedSize) {
        UpdatableLHashSeparateKVFloatShortMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMap(
            Float[] keys, Short[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMap(
            Float[] keys, Short[] values, int expectedSize) {
        UpdatableLHashSeparateKVFloatShortMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMap(
            Iterable<Float> keys, Iterable<Short> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMap(
            Iterable<Float> keys, Iterable<Short> values, int expectedSize) {
        UpdatableLHashSeparateKVFloatShortMapGO map = newUpdatableMap(expectedSize);
        Iterator<Float> keysIt = keys.iterator();
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
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMapOf(
            float k1, short v1) {
        UpdatableLHashSeparateKVFloatShortMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMapOf(
            float k1, short v1, float k2, short v2) {
        UpdatableLHashSeparateKVFloatShortMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMapOf(
            float k1, short v1, float k2, short v2,
            float k3, short v3) {
        UpdatableLHashSeparateKVFloatShortMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMapOf(
            float k1, short v1, float k2, short v2,
            float k3, short v3, float k4, short v4) {
        UpdatableLHashSeparateKVFloatShortMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVFloatShortMapGO newUpdatableMapOf(
            float k1, short v1, float k2, short v2,
            float k3, short v3, float k4, short v4,
            float k5, short v5) {
        UpdatableLHashSeparateKVFloatShortMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMap(Map<Float, Short> map1,
            Map<Float, Short> map2, int expectedSize) {
        MutableLHashSeparateKVFloatShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMap(Map<Float, Short> map1,
            Map<Float, Short> map2, Map<Float, Short> map3, int expectedSize) {
        MutableLHashSeparateKVFloatShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMap(Map<Float, Short> map1,
            Map<Float, Short> map2, Map<Float, Short> map3,
            Map<Float, Short> map4, int expectedSize) {
        MutableLHashSeparateKVFloatShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMap(Map<Float, Short> map1,
            Map<Float, Short> map2, Map<Float, Short> map3,
            Map<Float, Short> map4, Map<Float, Short> map5, int expectedSize) {
        MutableLHashSeparateKVFloatShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMap(
            Consumer<net.openhft.koloboke.function.FloatShortConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashSeparateKVFloatShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMap(float[] keys,
            short[] values, int expectedSize) {
        MutableLHashSeparateKVFloatShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMap(
            Float[] keys, Short[] values, int expectedSize) {
        MutableLHashSeparateKVFloatShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMap(Iterable<Float> keys,
            Iterable<Short> values, int expectedSize) {
        MutableLHashSeparateKVFloatShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMap(
            Map<Float, Short> map) {
        MutableLHashSeparateKVFloatShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMap(Map<Float, Short> map1,
            Map<Float, Short> map2) {
        MutableLHashSeparateKVFloatShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMap(Map<Float, Short> map1,
            Map<Float, Short> map2, Map<Float, Short> map3) {
        MutableLHashSeparateKVFloatShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMap(Map<Float, Short> map1,
            Map<Float, Short> map2, Map<Float, Short> map3,
            Map<Float, Short> map4) {
        MutableLHashSeparateKVFloatShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMap(Map<Float, Short> map1,
            Map<Float, Short> map2, Map<Float, Short> map3,
            Map<Float, Short> map4, Map<Float, Short> map5) {
        MutableLHashSeparateKVFloatShortMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMap(
            Consumer<net.openhft.koloboke.function.FloatShortConsumer> entriesSupplier
            ) {
        MutableLHashSeparateKVFloatShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMap(float[] keys,
            short[] values) {
        MutableLHashSeparateKVFloatShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMap(
            Float[] keys, Short[] values) {
        MutableLHashSeparateKVFloatShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMap(Iterable<Float> keys,
            Iterable<Short> values) {
        MutableLHashSeparateKVFloatShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMapOf(float k1, short v1) {
        MutableLHashSeparateKVFloatShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMapOf(float k1, short v1,
             float k2, short v2) {
        MutableLHashSeparateKVFloatShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMapOf(float k1, short v1,
             float k2, short v2, float k3, short v3) {
        MutableLHashSeparateKVFloatShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMapOf(float k1, short v1,
             float k2, short v2, float k3, short v3,
             float k4, short v4) {
        MutableLHashSeparateKVFloatShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newMutableMapOf(float k1, short v1,
             float k2, short v2, float k3, short v3,
             float k4, short v4, float k5, short v5) {
        MutableLHashSeparateKVFloatShortMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMap(Map<Float, Short> map1,
            Map<Float, Short> map2, int expectedSize) {
        ImmutableLHashSeparateKVFloatShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMap(Map<Float, Short> map1,
            Map<Float, Short> map2, Map<Float, Short> map3, int expectedSize) {
        ImmutableLHashSeparateKVFloatShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMap(Map<Float, Short> map1,
            Map<Float, Short> map2, Map<Float, Short> map3,
            Map<Float, Short> map4, int expectedSize) {
        ImmutableLHashSeparateKVFloatShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMap(Map<Float, Short> map1,
            Map<Float, Short> map2, Map<Float, Short> map3,
            Map<Float, Short> map4, Map<Float, Short> map5, int expectedSize) {
        ImmutableLHashSeparateKVFloatShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.FloatShortConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashSeparateKVFloatShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMap(float[] keys,
            short[] values, int expectedSize) {
        ImmutableLHashSeparateKVFloatShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMap(
            Float[] keys, Short[] values, int expectedSize) {
        ImmutableLHashSeparateKVFloatShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMap(Iterable<Float> keys,
            Iterable<Short> values, int expectedSize) {
        ImmutableLHashSeparateKVFloatShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMap(
            Map<Float, Short> map) {
        ImmutableLHashSeparateKVFloatShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMap(Map<Float, Short> map1,
            Map<Float, Short> map2) {
        ImmutableLHashSeparateKVFloatShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMap(Map<Float, Short> map1,
            Map<Float, Short> map2, Map<Float, Short> map3) {
        ImmutableLHashSeparateKVFloatShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMap(Map<Float, Short> map1,
            Map<Float, Short> map2, Map<Float, Short> map3,
            Map<Float, Short> map4) {
        ImmutableLHashSeparateKVFloatShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMap(Map<Float, Short> map1,
            Map<Float, Short> map2, Map<Float, Short> map3,
            Map<Float, Short> map4, Map<Float, Short> map5) {
        ImmutableLHashSeparateKVFloatShortMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.FloatShortConsumer> entriesSupplier
            ) {
        ImmutableLHashSeparateKVFloatShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMap(float[] keys,
            short[] values) {
        ImmutableLHashSeparateKVFloatShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMap(
            Float[] keys, Short[] values) {
        ImmutableLHashSeparateKVFloatShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMap(Iterable<Float> keys,
            Iterable<Short> values) {
        ImmutableLHashSeparateKVFloatShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMapOf(float k1, short v1) {
        ImmutableLHashSeparateKVFloatShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMapOf(float k1, short v1,
             float k2, short v2) {
        ImmutableLHashSeparateKVFloatShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMapOf(float k1, short v1,
             float k2, short v2, float k3, short v3) {
        ImmutableLHashSeparateKVFloatShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMapOf(float k1, short v1,
             float k2, short v2, float k3, short v3,
             float k4, short v4) {
        ImmutableLHashSeparateKVFloatShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashFloatShortMap newImmutableMapOf(float k1, short v1,
             float k2, short v2, float k3, short v3,
             float k4, short v4, float k5, short v5) {
        ImmutableLHashSeparateKVFloatShortMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

