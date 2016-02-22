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
import net.openhft.koloboke.collect.map.hash.HashObjLongMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashObjLongMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashSeparateKVObjLongMapFactoryGO<K>
        extends LHashSeparateKVObjLongMapFactorySO<K> {

    LHashSeparateKVObjLongMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , boolean isNullKeyAllowed
            ) {
        super(hashConf, defaultExpectedSize, isNullKeyAllowed
            );
    }

    

    abstract HashObjLongMapFactory<K> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            );

    abstract HashObjLongMapFactory<K> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            );

    abstract HashObjLongMapFactory<K> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullKeyAllowed
            );

    @Override
    public final HashObjLongMapFactory<K> withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            , isNullKeyAllowed());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            , isNullKeyAllowed());
    }

    @Override
    public final HashObjLongMapFactory<K> withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                , isNullKeyAllowed());
    }

    @Override
    public final HashObjLongMapFactory<K> withNullKeyAllowed(boolean nullKeyAllowed) {
        if (nullKeyAllowed == isNullKeyAllowed())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), nullKeyAllowed);
    }

    @Override
    public String toString() {
        return "HashObjLongMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashObjLongMapFactory) {
            HashObjLongMapFactory factory = (HashObjLongMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private <K2 extends K>
     UpdatableLHashSeparateKVObjLongMapGO<K2> shrunk(
            UpdatableLHashSeparateKVObjLongMapGO<K2> map) {
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
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public <K2 extends K>
     MutableLHashSeparateKVObjLongMapGO<K2> newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMap(
            Map<? extends K2, Long> map) {
        return newUpdatableMap(map, map.size());
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMap(
            Map<? extends K2, Long> map1, Map<? extends K2, Long> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMap(
            Map<? extends K2, Long> map1, Map<? extends K2, Long> map2,
            Map<? extends K2, Long> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMap(
            Map<? extends K2, Long> map1, Map<? extends K2, Long> map2,
            Map<? extends K2, Long> map3, Map<? extends K2, Long> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMap(
            Map<? extends K2, Long> map1, Map<? extends K2, Long> map2,
            Map<? extends K2, Long> map3, Map<? extends K2, Long> map4,
            Map<? extends K2, Long> map5) {
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
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMap(
            Map<? extends K2, Long> map, int expectedSize) {
        return shrunk(super.newUpdatableMap(map, expectedSize));
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMap(
            Map<? extends K2, Long> map1, Map<? extends K2, Long> map2,
            int expectedSize) {
        UpdatableLHashSeparateKVObjLongMapGO<K2> map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMap(
            Map<? extends K2, Long> map1, Map<? extends K2, Long> map2,
            Map<? extends K2, Long> map3, int expectedSize) {
        UpdatableLHashSeparateKVObjLongMapGO<K2> map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMap(
            Map<? extends K2, Long> map1, Map<? extends K2, Long> map2,
            Map<? extends K2, Long> map3, Map<? extends K2, Long> map4,
            int expectedSize) {
        UpdatableLHashSeparateKVObjLongMapGO<K2> map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMap(
            Map<? extends K2, Long> map1, Map<? extends K2, Long> map2,
            Map<? extends K2, Long> map3, Map<? extends K2, Long> map4,
            Map<? extends K2, Long> map5, int expectedSize) {
        UpdatableLHashSeparateKVObjLongMapGO<K2> map = newUpdatableMap(expectedSize);
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
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ObjLongConsumer<K2>> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ObjLongConsumer<K2>> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashSeparateKVObjLongMapGO<K2> map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.ObjLongConsumer<K2>() {
             @Override
             public void accept(K2 k, long v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMap(
            K2[] keys, long[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMap(
            K2[] keys, long[] values, int expectedSize) {
        UpdatableLHashSeparateKVObjLongMapGO<K2> map = newUpdatableMap(expectedSize);
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
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMap(
            K2[] keys, Long[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMap(
            K2[] keys, Long[] values, int expectedSize) {
        UpdatableLHashSeparateKVObjLongMapGO<K2> map = newUpdatableMap(expectedSize);
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
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMap(
            Iterable<? extends K2> keys, Iterable<Long> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMap(
            Iterable<? extends K2> keys, Iterable<Long> values, int expectedSize) {
        UpdatableLHashSeparateKVObjLongMapGO<K2> map = newUpdatableMap(expectedSize);
        Iterator<? extends K2> keysIt = keys.iterator();
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
    public <K2 extends K>
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMapOf(
            K2 k1, long v1) {
        UpdatableLHashSeparateKVObjLongMapGO<K2> map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public <K2 extends K>
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMapOf(
            K2 k1, long v1, K2 k2, long v2) {
        UpdatableLHashSeparateKVObjLongMapGO<K2> map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMapOf(
            K2 k1, long v1, K2 k2, long v2,
            K2 k3, long v3) {
        UpdatableLHashSeparateKVObjLongMapGO<K2> map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMapOf(
            K2 k1, long v1, K2 k2, long v2,
            K2 k3, long v3, K2 k4, long v4) {
        UpdatableLHashSeparateKVObjLongMapGO<K2> map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     UpdatableLHashSeparateKVObjLongMapGO<K2> newUpdatableMapOf(
            K2 k1, long v1, K2 k2, long v2,
            K2 k3, long v3, K2 k4, long v4,
            K2 k5, long v5) {
        UpdatableLHashSeparateKVObjLongMapGO<K2> map = newUpdatableMap(5);
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
     HashObjLongMap<K2> newMutableMap(
            Map<? extends K2, Long> map, int expectedSize) {
        MutableLHashSeparateKVObjLongMapGO<K2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMap(Map<? extends K2, Long> map1,
            Map<? extends K2, Long> map2, int expectedSize) {
        MutableLHashSeparateKVObjLongMapGO<K2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMap(Map<? extends K2, Long> map1,
            Map<? extends K2, Long> map2, Map<? extends K2, Long> map3, int expectedSize) {
        MutableLHashSeparateKVObjLongMapGO<K2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMap(Map<? extends K2, Long> map1,
            Map<? extends K2, Long> map2, Map<? extends K2, Long> map3,
            Map<? extends K2, Long> map4, int expectedSize) {
        MutableLHashSeparateKVObjLongMapGO<K2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMap(Map<? extends K2, Long> map1,
            Map<? extends K2, Long> map2, Map<? extends K2, Long> map3,
            Map<? extends K2, Long> map4, Map<? extends K2, Long> map5, int expectedSize) {
        MutableLHashSeparateKVObjLongMapGO<K2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMap(
            Consumer<net.openhft.koloboke.function.ObjLongConsumer<K2>> entriesSupplier
            , int expectedSize) {
        MutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMap(K2[] keys,
            long[] values, int expectedSize) {
        MutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMap(
            K2[] keys, Long[] values, int expectedSize) {
        MutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMap(Iterable<? extends K2> keys,
            Iterable<Long> values, int expectedSize) {
        MutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMap(
            Map<? extends K2, Long> map) {
        MutableLHashSeparateKVObjLongMapGO<K2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMap(Map<? extends K2, Long> map1,
            Map<? extends K2, Long> map2) {
        MutableLHashSeparateKVObjLongMapGO<K2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMap(Map<? extends K2, Long> map1,
            Map<? extends K2, Long> map2, Map<? extends K2, Long> map3) {
        MutableLHashSeparateKVObjLongMapGO<K2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMap(Map<? extends K2, Long> map1,
            Map<? extends K2, Long> map2, Map<? extends K2, Long> map3,
            Map<? extends K2, Long> map4) {
        MutableLHashSeparateKVObjLongMapGO<K2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMap(Map<? extends K2, Long> map1,
            Map<? extends K2, Long> map2, Map<? extends K2, Long> map3,
            Map<? extends K2, Long> map4, Map<? extends K2, Long> map5) {
        MutableLHashSeparateKVObjLongMapGO<K2> res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMap(
            Consumer<net.openhft.koloboke.function.ObjLongConsumer<K2>> entriesSupplier
            ) {
        MutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMap(K2[] keys,
            long[] values) {
        MutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMap(
            K2[] keys, Long[] values) {
        MutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMap(Iterable<? extends K2> keys,
            Iterable<Long> values) {
        MutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMapOf(K2 k1, long v1) {
        MutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMapOf(K2 k1, long v1,
             K2 k2, long v2) {
        MutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMapOf(K2 k1, long v1,
             K2 k2, long v2, K2 k3, long v3) {
        MutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMapOf(K2 k1, long v1,
             K2 k2, long v2, K2 k3, long v3,
             K2 k4, long v4) {
        MutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newMutableMapOf(K2 k1, long v1,
             K2 k2, long v2, K2 k3, long v3,
             K2 k4, long v4, K2 k5, long v5) {
        MutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMap(
            Map<? extends K2, Long> map, int expectedSize) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMap(Map<? extends K2, Long> map1,
            Map<? extends K2, Long> map2, int expectedSize) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMap(Map<? extends K2, Long> map1,
            Map<? extends K2, Long> map2, Map<? extends K2, Long> map3, int expectedSize) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMap(Map<? extends K2, Long> map1,
            Map<? extends K2, Long> map2, Map<? extends K2, Long> map3,
            Map<? extends K2, Long> map4, int expectedSize) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMap(Map<? extends K2, Long> map1,
            Map<? extends K2, Long> map2, Map<? extends K2, Long> map3,
            Map<? extends K2, Long> map4, Map<? extends K2, Long> map5, int expectedSize) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMap(
            Consumer<net.openhft.koloboke.function.ObjLongConsumer<K2>> entriesSupplier
            , int expectedSize) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMap(K2[] keys,
            long[] values, int expectedSize) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMap(
            K2[] keys, Long[] values, int expectedSize) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMap(Iterable<? extends K2> keys,
            Iterable<Long> values, int expectedSize) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMap(
            Map<? extends K2, Long> map) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMap(Map<? extends K2, Long> map1,
            Map<? extends K2, Long> map2) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMap(Map<? extends K2, Long> map1,
            Map<? extends K2, Long> map2, Map<? extends K2, Long> map3) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMap(Map<? extends K2, Long> map1,
            Map<? extends K2, Long> map2, Map<? extends K2, Long> map3,
            Map<? extends K2, Long> map4) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMap(Map<? extends K2, Long> map1,
            Map<? extends K2, Long> map2, Map<? extends K2, Long> map3,
            Map<? extends K2, Long> map4, Map<? extends K2, Long> map5) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMap(
            Consumer<net.openhft.koloboke.function.ObjLongConsumer<K2>> entriesSupplier
            ) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMap(K2[] keys,
            long[] values) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMap(
            K2[] keys, Long[] values) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMap(Iterable<? extends K2> keys,
            Iterable<Long> values) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMapOf(K2 k1, long v1) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMapOf(K2 k1, long v1,
             K2 k2, long v2) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMapOf(K2 k1, long v1,
             K2 k2, long v2, K2 k3, long v3) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMapOf(K2 k1, long v1,
             K2 k2, long v2, K2 k3, long v3,
             K2 k4, long v4) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public <K2 extends K>
     HashObjLongMap<K2> newImmutableMapOf(K2 k1, long v1,
             K2 k2, long v2, K2 k3, long v3,
             K2 k4, long v4, K2 k5, long v5) {
        ImmutableLHashSeparateKVObjLongMapGO<K2> map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

