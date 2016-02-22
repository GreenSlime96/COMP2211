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
import net.openhft.koloboke.collect.map.hash.HashLongFloatMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashLongFloatMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashSeparateKVLongFloatMapFactoryGO
        extends LHashSeparateKVLongFloatMapFactorySO {

    LHashSeparateKVLongFloatMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashLongFloatMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper);

    abstract HashLongFloatMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper);

    abstract HashLongFloatMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper);

    @Override
    public final HashLongFloatMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashLongFloatMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashLongFloatMapFactory withDomain(long lower, long upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashLongFloatMapFactory withKeysDomain(long lower, long upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashLongFloatMapFactory withKeysDomainComplement(long lower, long upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((long) (upper + 1), (long) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashLongFloatMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashLongFloatMapFactory) {
            HashLongFloatMapFactory factory = (HashLongFloatMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashSeparateKVLongFloatMapGO shrunk(
            UpdatableLHashSeparateKVLongFloatMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashSeparateKVLongFloatMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMap(
            Map<Long, Float> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMap(
            Map<Long, Float> map1, Map<Long, Float> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMap(
            Map<Long, Float> map1, Map<Long, Float> map2,
            Map<Long, Float> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMap(
            Map<Long, Float> map1, Map<Long, Float> map2,
            Map<Long, Float> map3, Map<Long, Float> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMap(
            Map<Long, Float> map1, Map<Long, Float> map2,
            Map<Long, Float> map3, Map<Long, Float> map4,
            Map<Long, Float> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMap(
            Map<Long, Float> map1, Map<Long, Float> map2,
            int expectedSize) {
        UpdatableLHashSeparateKVLongFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMap(
            Map<Long, Float> map1, Map<Long, Float> map2,
            Map<Long, Float> map3, int expectedSize) {
        UpdatableLHashSeparateKVLongFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMap(
            Map<Long, Float> map1, Map<Long, Float> map2,
            Map<Long, Float> map3, Map<Long, Float> map4,
            int expectedSize) {
        UpdatableLHashSeparateKVLongFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMap(
            Map<Long, Float> map1, Map<Long, Float> map2,
            Map<Long, Float> map3, Map<Long, Float> map4,
            Map<Long, Float> map5, int expectedSize) {
        UpdatableLHashSeparateKVLongFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.LongFloatConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.LongFloatConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashSeparateKVLongFloatMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.LongFloatConsumer() {
             @Override
             public void accept(long k, float v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMap(
            long[] keys, float[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMap(
            long[] keys, float[] values, int expectedSize) {
        UpdatableLHashSeparateKVLongFloatMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMap(
            Long[] keys, Float[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMap(
            Long[] keys, Float[] values, int expectedSize) {
        UpdatableLHashSeparateKVLongFloatMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMap(
            Iterable<Long> keys, Iterable<Float> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMap(
            Iterable<Long> keys, Iterable<Float> values, int expectedSize) {
        UpdatableLHashSeparateKVLongFloatMapGO map = newUpdatableMap(expectedSize);
        Iterator<Long> keysIt = keys.iterator();
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
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMapOf(
            long k1, float v1) {
        UpdatableLHashSeparateKVLongFloatMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMapOf(
            long k1, float v1, long k2, float v2) {
        UpdatableLHashSeparateKVLongFloatMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMapOf(
            long k1, float v1, long k2, float v2,
            long k3, float v3) {
        UpdatableLHashSeparateKVLongFloatMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMapOf(
            long k1, float v1, long k2, float v2,
            long k3, float v3, long k4, float v4) {
        UpdatableLHashSeparateKVLongFloatMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashSeparateKVLongFloatMapGO newUpdatableMapOf(
            long k1, float v1, long k2, float v2,
            long k3, float v3, long k4, float v4,
            long k5, float v5) {
        UpdatableLHashSeparateKVLongFloatMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMap(Map<Long, Float> map1,
            Map<Long, Float> map2, int expectedSize) {
        MutableLHashSeparateKVLongFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMap(Map<Long, Float> map1,
            Map<Long, Float> map2, Map<Long, Float> map3, int expectedSize) {
        MutableLHashSeparateKVLongFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMap(Map<Long, Float> map1,
            Map<Long, Float> map2, Map<Long, Float> map3,
            Map<Long, Float> map4, int expectedSize) {
        MutableLHashSeparateKVLongFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMap(Map<Long, Float> map1,
            Map<Long, Float> map2, Map<Long, Float> map3,
            Map<Long, Float> map4, Map<Long, Float> map5, int expectedSize) {
        MutableLHashSeparateKVLongFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMap(
            Consumer<net.openhft.koloboke.function.LongFloatConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashSeparateKVLongFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMap(long[] keys,
            float[] values, int expectedSize) {
        MutableLHashSeparateKVLongFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMap(
            Long[] keys, Float[] values, int expectedSize) {
        MutableLHashSeparateKVLongFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMap(Iterable<Long> keys,
            Iterable<Float> values, int expectedSize) {
        MutableLHashSeparateKVLongFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMap(
            Map<Long, Float> map) {
        MutableLHashSeparateKVLongFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMap(Map<Long, Float> map1,
            Map<Long, Float> map2) {
        MutableLHashSeparateKVLongFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMap(Map<Long, Float> map1,
            Map<Long, Float> map2, Map<Long, Float> map3) {
        MutableLHashSeparateKVLongFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMap(Map<Long, Float> map1,
            Map<Long, Float> map2, Map<Long, Float> map3,
            Map<Long, Float> map4) {
        MutableLHashSeparateKVLongFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMap(Map<Long, Float> map1,
            Map<Long, Float> map2, Map<Long, Float> map3,
            Map<Long, Float> map4, Map<Long, Float> map5) {
        MutableLHashSeparateKVLongFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMap(
            Consumer<net.openhft.koloboke.function.LongFloatConsumer> entriesSupplier
            ) {
        MutableLHashSeparateKVLongFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMap(long[] keys,
            float[] values) {
        MutableLHashSeparateKVLongFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMap(
            Long[] keys, Float[] values) {
        MutableLHashSeparateKVLongFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMap(Iterable<Long> keys,
            Iterable<Float> values) {
        MutableLHashSeparateKVLongFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMapOf(long k1, float v1) {
        MutableLHashSeparateKVLongFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMapOf(long k1, float v1,
             long k2, float v2) {
        MutableLHashSeparateKVLongFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMapOf(long k1, float v1,
             long k2, float v2, long k3, float v3) {
        MutableLHashSeparateKVLongFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMapOf(long k1, float v1,
             long k2, float v2, long k3, float v3,
             long k4, float v4) {
        MutableLHashSeparateKVLongFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newMutableMapOf(long k1, float v1,
             long k2, float v2, long k3, float v3,
             long k4, float v4, long k5, float v5) {
        MutableLHashSeparateKVLongFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMap(Map<Long, Float> map1,
            Map<Long, Float> map2, int expectedSize) {
        ImmutableLHashSeparateKVLongFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMap(Map<Long, Float> map1,
            Map<Long, Float> map2, Map<Long, Float> map3, int expectedSize) {
        ImmutableLHashSeparateKVLongFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMap(Map<Long, Float> map1,
            Map<Long, Float> map2, Map<Long, Float> map3,
            Map<Long, Float> map4, int expectedSize) {
        ImmutableLHashSeparateKVLongFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMap(Map<Long, Float> map1,
            Map<Long, Float> map2, Map<Long, Float> map3,
            Map<Long, Float> map4, Map<Long, Float> map5, int expectedSize) {
        ImmutableLHashSeparateKVLongFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.LongFloatConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashSeparateKVLongFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMap(long[] keys,
            float[] values, int expectedSize) {
        ImmutableLHashSeparateKVLongFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMap(
            Long[] keys, Float[] values, int expectedSize) {
        ImmutableLHashSeparateKVLongFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMap(Iterable<Long> keys,
            Iterable<Float> values, int expectedSize) {
        ImmutableLHashSeparateKVLongFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMap(
            Map<Long, Float> map) {
        ImmutableLHashSeparateKVLongFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMap(Map<Long, Float> map1,
            Map<Long, Float> map2) {
        ImmutableLHashSeparateKVLongFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMap(Map<Long, Float> map1,
            Map<Long, Float> map2, Map<Long, Float> map3) {
        ImmutableLHashSeparateKVLongFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMap(Map<Long, Float> map1,
            Map<Long, Float> map2, Map<Long, Float> map3,
            Map<Long, Float> map4) {
        ImmutableLHashSeparateKVLongFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMap(Map<Long, Float> map1,
            Map<Long, Float> map2, Map<Long, Float> map3,
            Map<Long, Float> map4, Map<Long, Float> map5) {
        ImmutableLHashSeparateKVLongFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.LongFloatConsumer> entriesSupplier
            ) {
        ImmutableLHashSeparateKVLongFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMap(long[] keys,
            float[] values) {
        ImmutableLHashSeparateKVLongFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMap(
            Long[] keys, Float[] values) {
        ImmutableLHashSeparateKVLongFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMap(Iterable<Long> keys,
            Iterable<Float> values) {
        ImmutableLHashSeparateKVLongFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMapOf(long k1, float v1) {
        ImmutableLHashSeparateKVLongFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMapOf(long k1, float v1,
             long k2, float v2) {
        ImmutableLHashSeparateKVLongFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMapOf(long k1, float v1,
             long k2, float v2, long k3, float v3) {
        ImmutableLHashSeparateKVLongFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMapOf(long k1, float v1,
             long k2, float v2, long k3, float v3,
             long k4, float v4) {
        ImmutableLHashSeparateKVLongFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashLongFloatMap newImmutableMapOf(long k1, float v1,
             long k2, float v2, long k3, float v3,
             long k4, float v4, long k5, float v5) {
        ImmutableLHashSeparateKVLongFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

