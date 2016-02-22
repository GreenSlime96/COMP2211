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
import net.openhft.koloboke.collect.map.hash.HashDoubleLongMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashDoubleLongMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashParallelKVDoubleLongMapFactoryGO
        extends LHashParallelKVDoubleLongMapFactorySO {

    LHashParallelKVDoubleLongMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    abstract HashDoubleLongMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashDoubleLongMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashDoubleLongMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    @Override
    public final HashDoubleLongMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
    }

    @Override
    public final HashDoubleLongMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                );
    }


    @Override
    public String toString() {
        return "HashDoubleLongMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashDoubleLongMapFactory) {
            HashDoubleLongMapFactory factory = (HashDoubleLongMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashParallelKVDoubleLongMapGO shrunk(
            UpdatableLHashParallelKVDoubleLongMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashParallelKVDoubleLongMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMap(
            Map<Double, Long> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMap(
            Map<Double, Long> map1, Map<Double, Long> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMap(
            Map<Double, Long> map1, Map<Double, Long> map2,
            Map<Double, Long> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMap(
            Map<Double, Long> map1, Map<Double, Long> map2,
            Map<Double, Long> map3, Map<Double, Long> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMap(
            Map<Double, Long> map1, Map<Double, Long> map2,
            Map<Double, Long> map3, Map<Double, Long> map4,
            Map<Double, Long> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMap(
            Map<Double, Long> map1, Map<Double, Long> map2,
            int expectedSize) {
        UpdatableLHashParallelKVDoubleLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMap(
            Map<Double, Long> map1, Map<Double, Long> map2,
            Map<Double, Long> map3, int expectedSize) {
        UpdatableLHashParallelKVDoubleLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMap(
            Map<Double, Long> map1, Map<Double, Long> map2,
            Map<Double, Long> map3, Map<Double, Long> map4,
            int expectedSize) {
        UpdatableLHashParallelKVDoubleLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMap(
            Map<Double, Long> map1, Map<Double, Long> map2,
            Map<Double, Long> map3, Map<Double, Long> map4,
            Map<Double, Long> map5, int expectedSize) {
        UpdatableLHashParallelKVDoubleLongMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.DoubleLongConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.DoubleLongConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashParallelKVDoubleLongMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.DoubleLongConsumer() {
             @Override
             public void accept(double k, long v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMap(
            double[] keys, long[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMap(
            double[] keys, long[] values, int expectedSize) {
        UpdatableLHashParallelKVDoubleLongMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMap(
            Double[] keys, Long[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMap(
            Double[] keys, Long[] values, int expectedSize) {
        UpdatableLHashParallelKVDoubleLongMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMap(
            Iterable<Double> keys, Iterable<Long> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMap(
            Iterable<Double> keys, Iterable<Long> values, int expectedSize) {
        UpdatableLHashParallelKVDoubleLongMapGO map = newUpdatableMap(expectedSize);
        Iterator<Double> keysIt = keys.iterator();
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
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMapOf(
            double k1, long v1) {
        UpdatableLHashParallelKVDoubleLongMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMapOf(
            double k1, long v1, double k2, long v2) {
        UpdatableLHashParallelKVDoubleLongMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMapOf(
            double k1, long v1, double k2, long v2,
            double k3, long v3) {
        UpdatableLHashParallelKVDoubleLongMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMapOf(
            double k1, long v1, double k2, long v2,
            double k3, long v3, double k4, long v4) {
        UpdatableLHashParallelKVDoubleLongMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVDoubleLongMapGO newUpdatableMapOf(
            double k1, long v1, double k2, long v2,
            double k3, long v3, double k4, long v4,
            double k5, long v5) {
        UpdatableLHashParallelKVDoubleLongMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMap(Map<Double, Long> map1,
            Map<Double, Long> map2, int expectedSize) {
        MutableLHashParallelKVDoubleLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMap(Map<Double, Long> map1,
            Map<Double, Long> map2, Map<Double, Long> map3, int expectedSize) {
        MutableLHashParallelKVDoubleLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMap(Map<Double, Long> map1,
            Map<Double, Long> map2, Map<Double, Long> map3,
            Map<Double, Long> map4, int expectedSize) {
        MutableLHashParallelKVDoubleLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMap(Map<Double, Long> map1,
            Map<Double, Long> map2, Map<Double, Long> map3,
            Map<Double, Long> map4, Map<Double, Long> map5, int expectedSize) {
        MutableLHashParallelKVDoubleLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMap(
            Consumer<net.openhft.koloboke.function.DoubleLongConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashParallelKVDoubleLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMap(double[] keys,
            long[] values, int expectedSize) {
        MutableLHashParallelKVDoubleLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMap(
            Double[] keys, Long[] values, int expectedSize) {
        MutableLHashParallelKVDoubleLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMap(Iterable<Double> keys,
            Iterable<Long> values, int expectedSize) {
        MutableLHashParallelKVDoubleLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMap(
            Map<Double, Long> map) {
        MutableLHashParallelKVDoubleLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMap(Map<Double, Long> map1,
            Map<Double, Long> map2) {
        MutableLHashParallelKVDoubleLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMap(Map<Double, Long> map1,
            Map<Double, Long> map2, Map<Double, Long> map3) {
        MutableLHashParallelKVDoubleLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMap(Map<Double, Long> map1,
            Map<Double, Long> map2, Map<Double, Long> map3,
            Map<Double, Long> map4) {
        MutableLHashParallelKVDoubleLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMap(Map<Double, Long> map1,
            Map<Double, Long> map2, Map<Double, Long> map3,
            Map<Double, Long> map4, Map<Double, Long> map5) {
        MutableLHashParallelKVDoubleLongMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMap(
            Consumer<net.openhft.koloboke.function.DoubleLongConsumer> entriesSupplier
            ) {
        MutableLHashParallelKVDoubleLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMap(double[] keys,
            long[] values) {
        MutableLHashParallelKVDoubleLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMap(
            Double[] keys, Long[] values) {
        MutableLHashParallelKVDoubleLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMap(Iterable<Double> keys,
            Iterable<Long> values) {
        MutableLHashParallelKVDoubleLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMapOf(double k1, long v1) {
        MutableLHashParallelKVDoubleLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMapOf(double k1, long v1,
             double k2, long v2) {
        MutableLHashParallelKVDoubleLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMapOf(double k1, long v1,
             double k2, long v2, double k3, long v3) {
        MutableLHashParallelKVDoubleLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMapOf(double k1, long v1,
             double k2, long v2, double k3, long v3,
             double k4, long v4) {
        MutableLHashParallelKVDoubleLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newMutableMapOf(double k1, long v1,
             double k2, long v2, double k3, long v3,
             double k4, long v4, double k5, long v5) {
        MutableLHashParallelKVDoubleLongMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMap(Map<Double, Long> map1,
            Map<Double, Long> map2, int expectedSize) {
        ImmutableLHashParallelKVDoubleLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMap(Map<Double, Long> map1,
            Map<Double, Long> map2, Map<Double, Long> map3, int expectedSize) {
        ImmutableLHashParallelKVDoubleLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMap(Map<Double, Long> map1,
            Map<Double, Long> map2, Map<Double, Long> map3,
            Map<Double, Long> map4, int expectedSize) {
        ImmutableLHashParallelKVDoubleLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMap(Map<Double, Long> map1,
            Map<Double, Long> map2, Map<Double, Long> map3,
            Map<Double, Long> map4, Map<Double, Long> map5, int expectedSize) {
        ImmutableLHashParallelKVDoubleLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.DoubleLongConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashParallelKVDoubleLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMap(double[] keys,
            long[] values, int expectedSize) {
        ImmutableLHashParallelKVDoubleLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMap(
            Double[] keys, Long[] values, int expectedSize) {
        ImmutableLHashParallelKVDoubleLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMap(Iterable<Double> keys,
            Iterable<Long> values, int expectedSize) {
        ImmutableLHashParallelKVDoubleLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMap(
            Map<Double, Long> map) {
        ImmutableLHashParallelKVDoubleLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMap(Map<Double, Long> map1,
            Map<Double, Long> map2) {
        ImmutableLHashParallelKVDoubleLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMap(Map<Double, Long> map1,
            Map<Double, Long> map2, Map<Double, Long> map3) {
        ImmutableLHashParallelKVDoubleLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMap(Map<Double, Long> map1,
            Map<Double, Long> map2, Map<Double, Long> map3,
            Map<Double, Long> map4) {
        ImmutableLHashParallelKVDoubleLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMap(Map<Double, Long> map1,
            Map<Double, Long> map2, Map<Double, Long> map3,
            Map<Double, Long> map4, Map<Double, Long> map5) {
        ImmutableLHashParallelKVDoubleLongMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.DoubleLongConsumer> entriesSupplier
            ) {
        ImmutableLHashParallelKVDoubleLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMap(double[] keys,
            long[] values) {
        ImmutableLHashParallelKVDoubleLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMap(
            Double[] keys, Long[] values) {
        ImmutableLHashParallelKVDoubleLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMap(Iterable<Double> keys,
            Iterable<Long> values) {
        ImmutableLHashParallelKVDoubleLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMapOf(double k1, long v1) {
        ImmutableLHashParallelKVDoubleLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMapOf(double k1, long v1,
             double k2, long v2) {
        ImmutableLHashParallelKVDoubleLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMapOf(double k1, long v1,
             double k2, long v2, double k3, long v3) {
        ImmutableLHashParallelKVDoubleLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMapOf(double k1, long v1,
             double k2, long v2, double k3, long v3,
             double k4, long v4) {
        ImmutableLHashParallelKVDoubleLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashDoubleLongMap newImmutableMapOf(double k1, long v1,
             double k2, long v2, double k3, long v3,
             double k4, long v4, double k5, long v5) {
        ImmutableLHashParallelKVDoubleLongMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

