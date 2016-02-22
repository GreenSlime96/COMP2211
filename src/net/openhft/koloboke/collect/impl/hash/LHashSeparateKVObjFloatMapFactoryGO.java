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
import net.openhft.koloboke.collect.map.hash.HashObjFloatMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashObjFloatMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashSeparateKVObjFloatMapFactoryGO<K>
        extends LHashSeparateKVObjFloatMapFactorySO<K> {

    LHashSeparateKVObjFloatMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , boolean isNullKeyAllowed
            ) {
        super(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }

    

    abstract HashObjFloatMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            );

    abstract HashObjFloatMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            );

    abstract HashObjFloatMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            );

    @Override
    public final HashObjFloatMapFactory<K> withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            , isNullKeyAllowed());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            , isNullKeyAllowed());
    }

    @Override
    public final HashObjFloatMapFactory<K> withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                , isNullKeyAllowed());
    }

    @Override
    public final HashObjFloatMapFactory<K> withNullKeyAllowed(boolean nullKeyAllowed) {
        if (nullKeyAllowed == isNullKeyAllowed())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), nullKeyAllowed);
    }

    @Override
    public String toString() {
        return "HashObjFloatMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashObjFloatMapFactory) {
            HashObjFloatMapFactory factory = (HashObjFloatMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> shrunk(
            UpdatableLHashSeparateKVObjFloatMapGO<K2> map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public <K2 extends K>
     MutableLHashSeparateKVObjFloatMapGO<K2> newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMap(
            Map<? extends K2, Float> map) {
        return newUpdatableMap(map, map.size());
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMap(
            Map<? extends K2, Float> map1, Map<? extends K2, Float> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMap(
            Map<? extends K2, Float> map1, Map<? extends K2, Float> map2,
            Map<? extends K2, Float> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMap(
            Map<? extends K2, Float> map1, Map<? extends K2, Float> map2,
            Map<? extends K2, Float> map3, Map<? extends K2, Float> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMap(
            Map<? extends K2, Float> map1, Map<? extends K2, Float> map2,
            Map<? extends K2, Float> map3, Map<? extends K2, Float> map4,
            Map<? extends K2, Float> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMap(
            Map<? extends K2, Float> map, int expectedSize) {
        return shrunk(super.newUpdatableMap(map, expectedSize));
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMap(
            Map<? extends K2, Float> map1, Map<? extends K2, Float> map2,
            int expectedSize) {
        UpdatableLHashSeparateKVObjFloatMapGO<K2> map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMap(
            Map<? extends K2, Float> map1, Map<? extends K2, Float> map2,
            Map<? extends K2, Float> map3, int expectedSize) {
        UpdatableLHashSeparateKVObjFloatMapGO<K2> map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMap(
            Map<? extends K2, Float> map1, Map<? extends K2, Float> map2,
            Map<? extends K2, Float> map3, Map<? extends K2, Float> map4,
            int expectedSize) {
        UpdatableLHashSeparateKVObjFloatMapGO<K2> map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMap(
            Map<? extends K2, Float> map1, Map<? extends K2, Float> map2,
            Map<? extends K2, Float> map3, Map<? extends K2, Float> map4,
            Map<? extends K2, Float> map5, int expectedSize) {
        UpdatableLHashSeparateKVObjFloatMapGO<K2> map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ObjFloatConsumer<K2>> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ObjFloatConsumer<K2>> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashSeparateKVObjFloatMapGO<K2> map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.ObjFloatConsumer<K2>() {
             @Override
             public void accept(K2 k, float v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMap(
            K2[] keys, float[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMap(
            K2[] keys, float[] values, int expectedSize) {
        UpdatableLHashSeparateKVObjFloatMapGO<K2> map = newUpdatableMap(expectedSize);
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
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMap(
            K2[] keys, Float[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMap(
            K2[] keys, Float[] values, int expectedSize) {
        UpdatableLHashSeparateKVObjFloatMapGO<K2> map = newUpdatableMap(expectedSize);
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
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMap(
            Iterable<? extends K2> keys, Iterable<Float> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMap(
            Iterable<? extends K2> keys, Iterable<Float> values, int expectedSize) {
        UpdatableLHashSeparateKVObjFloatMapGO<K2> map = newUpdatableMap(expectedSize);
        Iterator<? extends K2> keysIt = keys.iterator();
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
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMapOf(
            K2 k1, float v1) {
        UpdatableLHashSeparateKVObjFloatMapGO<K2> map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMapOf(
            K2 k1, float v1, K2 k2, float v2) {
        UpdatableLHashSeparateKVObjFloatMapGO<K2> map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMapOf(
            K2 k1, float v1, K2 k2, float v2,
            K2 k3, float v3) {
        UpdatableLHashSeparateKVObjFloatMapGO<K2> map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMapOf(
            K2 k1, float v1, K2 k2, float v2,
            K2 k3, float v3, K2 k4, float v4) {
        UpdatableLHashSeparateKVObjFloatMapGO<K2> map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjFloatMapGO<K2> newUpdatableMapOf(
            K2 k1, float v1, K2 k2, float v2,
            K2 k3, float v3, K2 k4, float v4,
            K2 k5, float v5) {
        UpdatableLHashSeparateKVObjFloatMapGO<K2> map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMap(
            Map<? extends K2, Float> map, int expectedSize) {
        MutableLHashSeparateKVObjFloatMapGO<K2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMap(Map<? extends K2, Float> map1,
            Map<? extends K2, Float> map2, int expectedSize) {
        MutableLHashSeparateKVObjFloatMapGO<K2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMap(Map<? extends K2, Float> map1,
            Map<? extends K2, Float> map2, Map<? extends K2, Float> map3, int expectedSize) {
        MutableLHashSeparateKVObjFloatMapGO<K2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMap(Map<? extends K2, Float> map1,
            Map<? extends K2, Float> map2, Map<? extends K2, Float> map3,
            Map<? extends K2, Float> map4, int expectedSize) {
        MutableLHashSeparateKVObjFloatMapGO<K2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMap(Map<? extends K2, Float> map1,
            Map<? extends K2, Float> map2, Map<? extends K2, Float> map3,
            Map<? extends K2, Float> map4, Map<? extends K2, Float> map5, int expectedSize) {
        MutableLHashSeparateKVObjFloatMapGO<K2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMap(
            Consumer<net.openhft.koloboke.function.ObjFloatConsumer<K2>> entriesSupplier
            , int expectedSize) {
        MutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMap(K2[] keys,
            float[] values, int expectedSize) {
        MutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMap(
            K2[] keys, Float[] values, int expectedSize) {
        MutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMap(Iterable<? extends K2> keys,
            Iterable<Float> values, int expectedSize) {
        MutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMap(
            Map<? extends K2, Float> map) {
        MutableLHashSeparateKVObjFloatMapGO<K2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMap(Map<? extends K2, Float> map1,
            Map<? extends K2, Float> map2) {
        MutableLHashSeparateKVObjFloatMapGO<K2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMap(Map<? extends K2, Float> map1,
            Map<? extends K2, Float> map2, Map<? extends K2, Float> map3) {
        MutableLHashSeparateKVObjFloatMapGO<K2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMap(Map<? extends K2, Float> map1,
            Map<? extends K2, Float> map2, Map<? extends K2, Float> map3,
            Map<? extends K2, Float> map4) {
        MutableLHashSeparateKVObjFloatMapGO<K2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMap(Map<? extends K2, Float> map1,
            Map<? extends K2, Float> map2, Map<? extends K2, Float> map3,
            Map<? extends K2, Float> map4, Map<? extends K2, Float> map5) {
        MutableLHashSeparateKVObjFloatMapGO<K2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMap(
            Consumer<net.openhft.koloboke.function.ObjFloatConsumer<K2>> entriesSupplier
            ) {
        MutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMap(K2[] keys,
            float[] values) {
        MutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMap(
            K2[] keys, Float[] values) {
        MutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMap(Iterable<? extends K2> keys,
            Iterable<Float> values) {
        MutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMapOf(K2 k1, float v1) {
        MutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMapOf(K2 k1, float v1,
             K2 k2, float v2) {
        MutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMapOf(K2 k1, float v1,
             K2 k2, float v2, K2 k3, float v3) {
        MutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMapOf(K2 k1, float v1,
             K2 k2, float v2, K2 k3, float v3,
             K2 k4, float v4) {
        MutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newMutableMapOf(K2 k1, float v1,
             K2 k2, float v2, K2 k3, float v3,
             K2 k4, float v4, K2 k5, float v5) {
        MutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMap(
            Map<? extends K2, Float> map, int expectedSize) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMap(Map<? extends K2, Float> map1,
            Map<? extends K2, Float> map2, int expectedSize) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMap(Map<? extends K2, Float> map1,
            Map<? extends K2, Float> map2, Map<? extends K2, Float> map3, int expectedSize) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMap(Map<? extends K2, Float> map1,
            Map<? extends K2, Float> map2, Map<? extends K2, Float> map3,
            Map<? extends K2, Float> map4, int expectedSize) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMap(Map<? extends K2, Float> map1,
            Map<? extends K2, Float> map2, Map<? extends K2, Float> map3,
            Map<? extends K2, Float> map4, Map<? extends K2, Float> map5, int expectedSize) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMap(
            Consumer<net.openhft.koloboke.function.ObjFloatConsumer<K2>> entriesSupplier
            , int expectedSize) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMap(K2[] keys,
            float[] values, int expectedSize) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMap(
            K2[] keys, Float[] values, int expectedSize) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMap(Iterable<? extends K2> keys,
            Iterable<Float> values, int expectedSize) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMap(
            Map<? extends K2, Float> map) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMap(Map<? extends K2, Float> map1,
            Map<? extends K2, Float> map2) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMap(Map<? extends K2, Float> map1,
            Map<? extends K2, Float> map2, Map<? extends K2, Float> map3) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMap(Map<? extends K2, Float> map1,
            Map<? extends K2, Float> map2, Map<? extends K2, Float> map3,
            Map<? extends K2, Float> map4) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMap(Map<? extends K2, Float> map1,
            Map<? extends K2, Float> map2, Map<? extends K2, Float> map3,
            Map<? extends K2, Float> map4, Map<? extends K2, Float> map5) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMap(
            Consumer<net.openhft.koloboke.function.ObjFloatConsumer<K2>> entriesSupplier
            ) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMap(K2[] keys,
            float[] values) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMap(
            K2[] keys, Float[] values) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMap(Iterable<? extends K2> keys,
            Iterable<Float> values) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMapOf(K2 k1, float v1) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMapOf(K2 k1, float v1,
             K2 k2, float v2) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMapOf(K2 k1, float v1,
             K2 k2, float v2, K2 k3, float v3) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMapOf(K2 k1, float v1,
             K2 k2, float v2, K2 k3, float v3,
             K2 k4, float v4) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjFloatMap<K2> newImmutableMapOf(K2 k1, float v1,
             K2 k2, float v2, K2 k3, float v3,
             K2 k4, float v4, K2 k5, float v5) {
        ImmutableLHashSeparateKVObjFloatMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

