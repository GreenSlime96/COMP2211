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
import net.openhft.koloboke.collect.map.hash.HashByteFloatMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashByteFloatMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashSeparateKVByteFloatMapFactoryGO
        extends QHashSeparateKVByteFloatMapFactorySO {

    QHashSeparateKVByteFloatMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashByteFloatMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper);

    abstract HashByteFloatMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper);

    abstract HashByteFloatMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper);

    @Override
    public final HashByteFloatMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashByteFloatMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashByteFloatMapFactory withDomain(byte lower, byte upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashByteFloatMapFactory withKeysDomain(byte lower, byte upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashByteFloatMapFactory withKeysDomainComplement(byte lower, byte upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((byte) (upper + 1), (byte) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashByteFloatMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashByteFloatMapFactory) {
            HashByteFloatMapFactory factory = (HashByteFloatMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableQHashSeparateKVByteFloatMapGO shrunk(
            UpdatableQHashSeparateKVByteFloatMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableQHashSeparateKVByteFloatMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMap(
            Map<Byte, Float> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMap(
            Map<Byte, Float> map1, Map<Byte, Float> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMap(
            Map<Byte, Float> map1, Map<Byte, Float> map2,
            Map<Byte, Float> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMap(
            Map<Byte, Float> map1, Map<Byte, Float> map2,
            Map<Byte, Float> map3, Map<Byte, Float> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMap(
            Map<Byte, Float> map1, Map<Byte, Float> map2,
            Map<Byte, Float> map3, Map<Byte, Float> map4,
            Map<Byte, Float> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMap(
            Map<Byte, Float> map1, Map<Byte, Float> map2,
            int expectedSize) {
        UpdatableQHashSeparateKVByteFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMap(
            Map<Byte, Float> map1, Map<Byte, Float> map2,
            Map<Byte, Float> map3, int expectedSize) {
        UpdatableQHashSeparateKVByteFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMap(
            Map<Byte, Float> map1, Map<Byte, Float> map2,
            Map<Byte, Float> map3, Map<Byte, Float> map4,
            int expectedSize) {
        UpdatableQHashSeparateKVByteFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMap(
            Map<Byte, Float> map1, Map<Byte, Float> map2,
            Map<Byte, Float> map3, Map<Byte, Float> map4,
            Map<Byte, Float> map5, int expectedSize) {
        UpdatableQHashSeparateKVByteFloatMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ByteFloatConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ByteFloatConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableQHashSeparateKVByteFloatMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.ByteFloatConsumer() {
             @Override
             public void accept(byte k, float v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMap(
            byte[] keys, float[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMap(
            byte[] keys, float[] values, int expectedSize) {
        UpdatableQHashSeparateKVByteFloatMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMap(
            Byte[] keys, Float[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMap(
            Byte[] keys, Float[] values, int expectedSize) {
        UpdatableQHashSeparateKVByteFloatMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMap(
            Iterable<Byte> keys, Iterable<Float> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMap(
            Iterable<Byte> keys, Iterable<Float> values, int expectedSize) {
        UpdatableQHashSeparateKVByteFloatMapGO map = newUpdatableMap(expectedSize);
        Iterator<Byte> keysIt = keys.iterator();
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
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMapOf(
            byte k1, float v1) {
        UpdatableQHashSeparateKVByteFloatMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMapOf(
            byte k1, float v1, byte k2, float v2) {
        UpdatableQHashSeparateKVByteFloatMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMapOf(
            byte k1, float v1, byte k2, float v2,
            byte k3, float v3) {
        UpdatableQHashSeparateKVByteFloatMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMapOf(
            byte k1, float v1, byte k2, float v2,
            byte k3, float v3, byte k4, float v4) {
        UpdatableQHashSeparateKVByteFloatMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableQHashSeparateKVByteFloatMapGO newUpdatableMapOf(
            byte k1, float v1, byte k2, float v2,
            byte k3, float v3, byte k4, float v4,
            byte k5, float v5) {
        UpdatableQHashSeparateKVByteFloatMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMap(Map<Byte, Float> map1,
            Map<Byte, Float> map2, int expectedSize) {
        MutableQHashSeparateKVByteFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMap(Map<Byte, Float> map1,
            Map<Byte, Float> map2, Map<Byte, Float> map3, int expectedSize) {
        MutableQHashSeparateKVByteFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMap(Map<Byte, Float> map1,
            Map<Byte, Float> map2, Map<Byte, Float> map3,
            Map<Byte, Float> map4, int expectedSize) {
        MutableQHashSeparateKVByteFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMap(Map<Byte, Float> map1,
            Map<Byte, Float> map2, Map<Byte, Float> map3,
            Map<Byte, Float> map4, Map<Byte, Float> map5, int expectedSize) {
        MutableQHashSeparateKVByteFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ByteFloatConsumer> entriesSupplier
            , int expectedSize) {
        MutableQHashSeparateKVByteFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMap(byte[] keys,
            float[] values, int expectedSize) {
        MutableQHashSeparateKVByteFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMap(
            Byte[] keys, Float[] values, int expectedSize) {
        MutableQHashSeparateKVByteFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMap(Iterable<Byte> keys,
            Iterable<Float> values, int expectedSize) {
        MutableQHashSeparateKVByteFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMap(
            Map<Byte, Float> map) {
        MutableQHashSeparateKVByteFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMap(Map<Byte, Float> map1,
            Map<Byte, Float> map2) {
        MutableQHashSeparateKVByteFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMap(Map<Byte, Float> map1,
            Map<Byte, Float> map2, Map<Byte, Float> map3) {
        MutableQHashSeparateKVByteFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMap(Map<Byte, Float> map1,
            Map<Byte, Float> map2, Map<Byte, Float> map3,
            Map<Byte, Float> map4) {
        MutableQHashSeparateKVByteFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMap(Map<Byte, Float> map1,
            Map<Byte, Float> map2, Map<Byte, Float> map3,
            Map<Byte, Float> map4, Map<Byte, Float> map5) {
        MutableQHashSeparateKVByteFloatMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ByteFloatConsumer> entriesSupplier
            ) {
        MutableQHashSeparateKVByteFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMap(byte[] keys,
            float[] values) {
        MutableQHashSeparateKVByteFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMap(
            Byte[] keys, Float[] values) {
        MutableQHashSeparateKVByteFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMap(Iterable<Byte> keys,
            Iterable<Float> values) {
        MutableQHashSeparateKVByteFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMapOf(byte k1, float v1) {
        MutableQHashSeparateKVByteFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMapOf(byte k1, float v1,
             byte k2, float v2) {
        MutableQHashSeparateKVByteFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMapOf(byte k1, float v1,
             byte k2, float v2, byte k3, float v3) {
        MutableQHashSeparateKVByteFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMapOf(byte k1, float v1,
             byte k2, float v2, byte k3, float v3,
             byte k4, float v4) {
        MutableQHashSeparateKVByteFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newMutableMapOf(byte k1, float v1,
             byte k2, float v2, byte k3, float v3,
             byte k4, float v4, byte k5, float v5) {
        MutableQHashSeparateKVByteFloatMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMap(Map<Byte, Float> map1,
            Map<Byte, Float> map2, int expectedSize) {
        ImmutableQHashSeparateKVByteFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMap(Map<Byte, Float> map1,
            Map<Byte, Float> map2, Map<Byte, Float> map3, int expectedSize) {
        ImmutableQHashSeparateKVByteFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMap(Map<Byte, Float> map1,
            Map<Byte, Float> map2, Map<Byte, Float> map3,
            Map<Byte, Float> map4, int expectedSize) {
        ImmutableQHashSeparateKVByteFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMap(Map<Byte, Float> map1,
            Map<Byte, Float> map2, Map<Byte, Float> map3,
            Map<Byte, Float> map4, Map<Byte, Float> map5, int expectedSize) {
        ImmutableQHashSeparateKVByteFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ByteFloatConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableQHashSeparateKVByteFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMap(byte[] keys,
            float[] values, int expectedSize) {
        ImmutableQHashSeparateKVByteFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMap(
            Byte[] keys, Float[] values, int expectedSize) {
        ImmutableQHashSeparateKVByteFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMap(Iterable<Byte> keys,
            Iterable<Float> values, int expectedSize) {
        ImmutableQHashSeparateKVByteFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMap(
            Map<Byte, Float> map) {
        ImmutableQHashSeparateKVByteFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMap(Map<Byte, Float> map1,
            Map<Byte, Float> map2) {
        ImmutableQHashSeparateKVByteFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMap(Map<Byte, Float> map1,
            Map<Byte, Float> map2, Map<Byte, Float> map3) {
        ImmutableQHashSeparateKVByteFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMap(Map<Byte, Float> map1,
            Map<Byte, Float> map2, Map<Byte, Float> map3,
            Map<Byte, Float> map4) {
        ImmutableQHashSeparateKVByteFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMap(Map<Byte, Float> map1,
            Map<Byte, Float> map2, Map<Byte, Float> map3,
            Map<Byte, Float> map4, Map<Byte, Float> map5) {
        ImmutableQHashSeparateKVByteFloatMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ByteFloatConsumer> entriesSupplier
            ) {
        ImmutableQHashSeparateKVByteFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMap(byte[] keys,
            float[] values) {
        ImmutableQHashSeparateKVByteFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMap(
            Byte[] keys, Float[] values) {
        ImmutableQHashSeparateKVByteFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMap(Iterable<Byte> keys,
            Iterable<Float> values) {
        ImmutableQHashSeparateKVByteFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMapOf(byte k1, float v1) {
        ImmutableQHashSeparateKVByteFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMapOf(byte k1, float v1,
             byte k2, float v2) {
        ImmutableQHashSeparateKVByteFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMapOf(byte k1, float v1,
             byte k2, float v2, byte k3, float v3) {
        ImmutableQHashSeparateKVByteFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMapOf(byte k1, float v1,
             byte k2, float v2, byte k3, float v3,
             byte k4, float v4) {
        ImmutableQHashSeparateKVByteFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteFloatMap newImmutableMapOf(byte k1, float v1,
             byte k2, float v2, byte k3, float v3,
             byte k4, float v4, byte k5, float v5) {
        ImmutableQHashSeparateKVByteFloatMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

