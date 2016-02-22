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
import net.openhft.koloboke.collect.map.hash.HashFloatObjMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashFloatObjMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashSeparateKVFloatObjMapFactoryGO<V>
        extends QHashSeparateKVFloatObjMapFactorySO<V> {

    QHashSeparateKVFloatObjMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    abstract HashFloatObjMapFactory<V> thisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashFloatObjMapFactory<V> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashFloatObjMapFactory<V> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    @Override
    public final HashFloatObjMapFactory<V> withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
    }

    @Override
    public final HashFloatObjMapFactory<V> withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                );
    }


    @Override
    public String toString() {
        return "HashFloatObjMapFactory[" + commonString() + keySpecialString() +
                ",valueEquivalence=" + getValueEquivalence() +
                
        "]";
    }

    @Override
    public int hashCode() {
        int hashCode = keySpecialHashCode(commonHashCode());
        hashCode = hashCode * 31 + getValueEquivalence().hashCode();
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof HashFloatObjMapFactory) {
            HashFloatObjMapFactory factory = (HashFloatObjMapFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory) &&
                    getValueEquivalence().equals(factory.getValueEquivalence())
;
        } else {
            return false;
        }
    }

    @Override
    @Nonnull
    public Equivalence<V> getValueEquivalence() {
        return Equivalence.defaultEquality();
    }

    

    

    

    

    

    

    
    

    
    

    private <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> shrunk(
            UpdatableQHashSeparateKVFloatObjMapGO<V2> map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public <V2 extends V> MutableQHashSeparateKVFloatObjMapGO<V2> newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMap(
            Map<Float, ? extends V2> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMap(
            Map<Float, ? extends V2> map1, Map<Float, ? extends V2> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMap(
            Map<Float, ? extends V2> map1, Map<Float, ? extends V2> map2,
            Map<Float, ? extends V2> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMap(
            Map<Float, ? extends V2> map1, Map<Float, ? extends V2> map2,
            Map<Float, ? extends V2> map3, Map<Float, ? extends V2> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMap(
            Map<Float, ? extends V2> map1, Map<Float, ? extends V2> map2,
            Map<Float, ? extends V2> map3, Map<Float, ? extends V2> map4,
            Map<Float, ? extends V2> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMap(
            Map<Float, ? extends V2> map1, Map<Float, ? extends V2> map2,
            int expectedSize) {
        UpdatableQHashSeparateKVFloatObjMapGO<V2> map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMap(
            Map<Float, ? extends V2> map1, Map<Float, ? extends V2> map2,
            Map<Float, ? extends V2> map3, int expectedSize) {
        UpdatableQHashSeparateKVFloatObjMapGO<V2> map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMap(
            Map<Float, ? extends V2> map1, Map<Float, ? extends V2> map2,
            Map<Float, ? extends V2> map3, Map<Float, ? extends V2> map4,
            int expectedSize) {
        UpdatableQHashSeparateKVFloatObjMapGO<V2> map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMap(
            Map<Float, ? extends V2> map1, Map<Float, ? extends V2> map2,
            Map<Float, ? extends V2> map3, Map<Float, ? extends V2> map4,
            Map<Float, ? extends V2> map5, int expectedSize) {
        UpdatableQHashSeparateKVFloatObjMapGO<V2> map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMap(
            Consumer<net.openhft.koloboke.function.FloatObjConsumer<V2>> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMap(
            Consumer<net.openhft.koloboke.function.FloatObjConsumer<V2>> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashSeparateKVFloatObjMapGO<V2> map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.FloatObjConsumer<V2>() {
             @Override
             public void accept(float k, V2 v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMap(
            float[] keys, V2[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMap(
            float[] keys, V2[] values, int expectedSize) {
        UpdatableQHashSeparateKVFloatObjMapGO<V2> map = newUpdatableMap(expectedSize);
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
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMap(
            Float[] keys, V2[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMap(
            Float[] keys, V2[] values, int expectedSize) {
        UpdatableQHashSeparateKVFloatObjMapGO<V2> map = newUpdatableMap(expectedSize);
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
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMap(
            Iterable<Float> keys, Iterable<? extends V2> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMap(
            Iterable<Float> keys, Iterable<? extends V2> values, int expectedSize) {
        UpdatableQHashSeparateKVFloatObjMapGO<V2> map = newUpdatableMap(expectedSize);
        Iterator<Float> keysIt = keys.iterator();
        Iterator<? extends V2> valuesIt = values.iterator();
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
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMapOf(
            float k1, V2 v1) {
        UpdatableQHashSeparateKVFloatObjMapGO<V2> map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMapOf(
            float k1, V2 v1, float k2, V2 v2) {
        UpdatableQHashSeparateKVFloatObjMapGO<V2> map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMapOf(
            float k1, V2 v1, float k2, V2 v2,
            float k3, V2 v3) {
        UpdatableQHashSeparateKVFloatObjMapGO<V2> map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMapOf(
            float k1, V2 v1, float k2, V2 v2,
            float k3, V2 v3, float k4, V2 v4) {
        UpdatableQHashSeparateKVFloatObjMapGO<V2> map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> UpdatableQHashSeparateKVFloatObjMapGO<V2> newUpdatableMapOf(
            float k1, V2 v1, float k2, V2 v2,
            float k3, V2 v3, float k4, V2 v4,
            float k5, V2 v5) {
        UpdatableQHashSeparateKVFloatObjMapGO<V2> map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMap(Map<Float, ? extends V2> map1,
            Map<Float, ? extends V2> map2, int expectedSize) {
        MutableQHashSeparateKVFloatObjMapGO<V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMap(Map<Float, ? extends V2> map1,
            Map<Float, ? extends V2> map2, Map<Float, ? extends V2> map3, int expectedSize) {
        MutableQHashSeparateKVFloatObjMapGO<V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMap(Map<Float, ? extends V2> map1,
            Map<Float, ? extends V2> map2, Map<Float, ? extends V2> map3,
            Map<Float, ? extends V2> map4, int expectedSize) {
        MutableQHashSeparateKVFloatObjMapGO<V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMap(Map<Float, ? extends V2> map1,
            Map<Float, ? extends V2> map2, Map<Float, ? extends V2> map3,
            Map<Float, ? extends V2> map4, Map<Float, ? extends V2> map5, int expectedSize) {
        MutableQHashSeparateKVFloatObjMapGO<V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMap(
            Consumer<net.openhft.koloboke.function.FloatObjConsumer<V2>> entriesSupplier
            , int expectedSize) {
        MutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMap(float[] keys,
            V2[] values, int expectedSize) {
        MutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMap(
            Float[] keys, V2[] values, int expectedSize) {
        MutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMap(Iterable<Float> keys,
            Iterable<? extends V2> values, int expectedSize) {
        MutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMap(
            Map<Float, ? extends V2> map) {
        MutableQHashSeparateKVFloatObjMapGO<V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMap(Map<Float, ? extends V2> map1,
            Map<Float, ? extends V2> map2) {
        MutableQHashSeparateKVFloatObjMapGO<V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMap(Map<Float, ? extends V2> map1,
            Map<Float, ? extends V2> map2, Map<Float, ? extends V2> map3) {
        MutableQHashSeparateKVFloatObjMapGO<V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMap(Map<Float, ? extends V2> map1,
            Map<Float, ? extends V2> map2, Map<Float, ? extends V2> map3,
            Map<Float, ? extends V2> map4) {
        MutableQHashSeparateKVFloatObjMapGO<V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMap(Map<Float, ? extends V2> map1,
            Map<Float, ? extends V2> map2, Map<Float, ? extends V2> map3,
            Map<Float, ? extends V2> map4, Map<Float, ? extends V2> map5) {
        MutableQHashSeparateKVFloatObjMapGO<V2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMap(
            Consumer<net.openhft.koloboke.function.FloatObjConsumer<V2>> entriesSupplier
            ) {
        MutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMap(float[] keys,
            V2[] values) {
        MutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMap(
            Float[] keys, V2[] values) {
        MutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMap(Iterable<Float> keys,
            Iterable<? extends V2> values) {
        MutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMapOf(float k1, V2 v1) {
        MutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMapOf(float k1, V2 v1,
             float k2, V2 v2) {
        MutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMapOf(float k1, V2 v1,
             float k2, V2 v2, float k3, V2 v3) {
        MutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMapOf(float k1, V2 v1,
             float k2, V2 v2, float k3, V2 v3,
             float k4, V2 v4) {
        MutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newMutableMapOf(float k1, V2 v1,
             float k2, V2 v2, float k3, V2 v3,
             float k4, V2 v4, float k5, V2 v5) {
        MutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMap(Map<Float, ? extends V2> map1,
            Map<Float, ? extends V2> map2, int expectedSize) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMap(Map<Float, ? extends V2> map1,
            Map<Float, ? extends V2> map2, Map<Float, ? extends V2> map3, int expectedSize) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMap(Map<Float, ? extends V2> map1,
            Map<Float, ? extends V2> map2, Map<Float, ? extends V2> map3,
            Map<Float, ? extends V2> map4, int expectedSize) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMap(Map<Float, ? extends V2> map1,
            Map<Float, ? extends V2> map2, Map<Float, ? extends V2> map3,
            Map<Float, ? extends V2> map4, Map<Float, ? extends V2> map5, int expectedSize) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMap(
            Consumer<net.openhft.koloboke.function.FloatObjConsumer<V2>> entriesSupplier
            , int expectedSize) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMap(float[] keys,
            V2[] values, int expectedSize) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMap(
            Float[] keys, V2[] values, int expectedSize) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMap(Iterable<Float> keys,
            Iterable<? extends V2> values, int expectedSize) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMap(
            Map<Float, ? extends V2> map) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMap(Map<Float, ? extends V2> map1,
            Map<Float, ? extends V2> map2) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMap(Map<Float, ? extends V2> map1,
            Map<Float, ? extends V2> map2, Map<Float, ? extends V2> map3) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMap(Map<Float, ? extends V2> map1,
            Map<Float, ? extends V2> map2, Map<Float, ? extends V2> map3,
            Map<Float, ? extends V2> map4) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMap(Map<Float, ? extends V2> map1,
            Map<Float, ? extends V2> map2, Map<Float, ? extends V2> map3,
            Map<Float, ? extends V2> map4, Map<Float, ? extends V2> map5) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMap(
            Consumer<net.openhft.koloboke.function.FloatObjConsumer<V2>> entriesSupplier
            ) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMap(float[] keys,
            V2[] values) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMap(
            Float[] keys, V2[] values) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMap(Iterable<Float> keys,
            Iterable<? extends V2> values) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMapOf(float k1, V2 v1) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMapOf(float k1, V2 v1,
             float k2, V2 v2) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMapOf(float k1, V2 v1,
             float k2, V2 v2, float k3, V2 v3) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMapOf(float k1, V2 v1,
             float k2, V2 v2, float k3, V2 v3,
             float k4, V2 v4) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public <V2 extends V> HashFloatObjMap<V2> newImmutableMapOf(float k1, V2 v1,
             float k2, V2 v2, float k3, V2 v3,
             float k4, V2 v4, float k5, V2 v5) {
        ImmutableQHashSeparateKVFloatObjMapGO<V2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}
