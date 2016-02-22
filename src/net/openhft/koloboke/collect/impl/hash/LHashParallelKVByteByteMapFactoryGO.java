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
import net.openhft.koloboke.collect.map.hash.HashByteByteMapFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.map.hash.HashByteByteMap;

import javax.annotation.Nonnull;
import java.util.*;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashParallelKVByteByteMapFactoryGO
        extends LHashParallelKVByteByteMapFactorySO {

    LHashParallelKVByteByteMapFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashByteByteMapFactory thisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper);

    abstract HashByteByteMapFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper);

    abstract HashByteByteMapFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper);

    @Override
    public final HashByteByteMapFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashByteByteMapFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    final HashByteByteMapFactory withDomain(byte lower, byte upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return thisWith(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public final HashByteByteMapFactory withKeysDomain(byte lower, byte upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public final HashByteByteMapFactory withKeysDomainComplement(byte lower, byte upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((byte) (upper + 1), (byte) (lower - 1));
    }

    @Override
    public String toString() {
        return "HashByteByteMapFactory[" + commonString() + keySpecialString() +
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
        if (obj instanceof HashByteByteMapFactory) {
            HashByteByteMapFactory factory = (HashByteByteMapFactory) obj;
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

    

    

    

    

    

    

    
    

    
    

    private  UpdatableLHashParallelKVByteByteMapGO shrunk(
            UpdatableLHashParallelKVByteByteMapGO map) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(map))
                map.shrink();
        }
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMap() {
        return newUpdatableMap(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public  MutableLHashParallelKVByteByteMapGO newMutableMap() {
        return newMutableMap(getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMap(
            Map<Byte, Byte> map) {
        return shrunk(super.newUpdatableMap(map));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMap(
            Map<Byte, Byte> map1, Map<Byte, Byte> map2) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        return newUpdatableMap(map1, map2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMap(
            Map<Byte, Byte> map1, Map<Byte, Byte> map2,
            Map<Byte, Byte> map3) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        return newUpdatableMap(map1, map2, map3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMap(
            Map<Byte, Byte> map1, Map<Byte, Byte> map2,
            Map<Byte, Byte> map3, Map<Byte, Byte> map4) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        return newUpdatableMap(map1, map2, map3, map4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMap(
            Map<Byte, Byte> map1, Map<Byte, Byte> map2,
            Map<Byte, Byte> map3, Map<Byte, Byte> map4,
            Map<Byte, Byte> map5) {
        long expectedSize = (long) map1.size();
        expectedSize += (long) map2.size();
        expectedSize += (long) map3.size();
        expectedSize += (long) map4.size();
        expectedSize += (long) map5.size();
        return newUpdatableMap(map1, map2, map3, map4, map5, sizeAsInt(expectedSize));
    }


    @Override
    @Nonnull
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMap(
            Map<Byte, Byte> map1, Map<Byte, Byte> map2,
            int expectedSize) {
        UpdatableLHashParallelKVByteByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMap(
            Map<Byte, Byte> map1, Map<Byte, Byte> map2,
            Map<Byte, Byte> map3, int expectedSize) {
        UpdatableLHashParallelKVByteByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMap(
            Map<Byte, Byte> map1, Map<Byte, Byte> map2,
            Map<Byte, Byte> map3, Map<Byte, Byte> map4,
            int expectedSize) {
        UpdatableLHashParallelKVByteByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMap(
            Map<Byte, Byte> map1, Map<Byte, Byte> map2,
            Map<Byte, Byte> map3, Map<Byte, Byte> map4,
            Map<Byte, Byte> map5, int expectedSize) {
        UpdatableLHashParallelKVByteByteMapGO map = newUpdatableMap(expectedSize);
        map.putAll(map1);
        map.putAll(map2);
        map.putAll(map3);
        map.putAll(map4);
        map.putAll(map5);
        return shrunk(map);
    }


    @Override
    @Nonnull
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ByteByteConsumer> entriesSupplier) {
        return newUpdatableMap(entriesSupplier, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMap(
            Consumer<net.openhft.koloboke.function.ByteByteConsumer> entriesSupplier,
            int expectedSize) {
        final UpdatableLHashParallelKVByteByteMapGO map = newUpdatableMap(expectedSize);
        entriesSupplier.accept(new net.openhft.koloboke.function.ByteByteConsumer() {
             @Override
             public void accept(byte k, byte v) {
                 map.put(k, v);
             }
         });
        return shrunk(map);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMap(
            byte[] keys, byte[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMap(
            byte[] keys, byte[] values, int expectedSize) {
        UpdatableLHashParallelKVByteByteMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMap(
            Byte[] keys, Byte[] values) {
        return newUpdatableMap(keys, values, keys.length);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMap(
            Byte[] keys, Byte[] values, int expectedSize) {
        UpdatableLHashParallelKVByteByteMapGO map = newUpdatableMap(expectedSize);
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
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMap(
            Iterable<Byte> keys, Iterable<Byte> values) {
        int expectedSize = keys instanceof Collection ? ((Collection) keys).size() :
                getDefaultExpectedSize();
        return newUpdatableMap(keys, values, expectedSize);
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMap(
            Iterable<Byte> keys, Iterable<Byte> values, int expectedSize) {
        UpdatableLHashParallelKVByteByteMapGO map = newUpdatableMap(expectedSize);
        Iterator<Byte> keysIt = keys.iterator();
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
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMapOf(
            byte k1, byte v1) {
        UpdatableLHashParallelKVByteByteMapGO map = newUpdatableMap(1);
        map.put(k1, v1);
        return map;
    }

    @Override
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMapOf(
            byte k1, byte v1, byte k2, byte v2) {
        UpdatableLHashParallelKVByteByteMapGO map = newUpdatableMap(2);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMapOf(
            byte k1, byte v1, byte k2, byte v2,
            byte k3, byte v3) {
        UpdatableLHashParallelKVByteByteMapGO map = newUpdatableMap(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMapOf(
            byte k1, byte v1, byte k2, byte v2,
            byte k3, byte v3, byte k4, byte v4) {
        UpdatableLHashParallelKVByteByteMapGO map = newUpdatableMap(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    @Override
    @Nonnull
    public  UpdatableLHashParallelKVByteByteMapGO newUpdatableMapOf(
            byte k1, byte v1, byte k2, byte v2,
            byte k3, byte v3, byte k4, byte v4,
            byte k5, byte v5) {
        UpdatableLHashParallelKVByteByteMapGO map = newUpdatableMap(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    
    


    @Override
    @Nonnull
    public  HashByteByteMap newMutableMap(Map<Byte, Byte> map1,
            Map<Byte, Byte> map2, int expectedSize) {
        MutableLHashParallelKVByteByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newMutableMap(Map<Byte, Byte> map1,
            Map<Byte, Byte> map2, Map<Byte, Byte> map3, int expectedSize) {
        MutableLHashParallelKVByteByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newMutableMap(Map<Byte, Byte> map1,
            Map<Byte, Byte> map2, Map<Byte, Byte> map3,
            Map<Byte, Byte> map4, int expectedSize) {
        MutableLHashParallelKVByteByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newMutableMap(Map<Byte, Byte> map1,
            Map<Byte, Byte> map2, Map<Byte, Byte> map3,
            Map<Byte, Byte> map4, Map<Byte, Byte> map5, int expectedSize) {
        MutableLHashParallelKVByteByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteByteMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ByteByteConsumer> entriesSupplier
            , int expectedSize) {
        MutableLHashParallelKVByteByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newMutableMap(byte[] keys,
            byte[] values, int expectedSize) {
        MutableLHashParallelKVByteByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newMutableMap(
            Byte[] keys, Byte[] values, int expectedSize) {
        MutableLHashParallelKVByteByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newMutableMap(Iterable<Byte> keys,
            Iterable<Byte> values, int expectedSize) {
        MutableLHashParallelKVByteByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashByteByteMap newMutableMap(
            Map<Byte, Byte> map) {
        MutableLHashParallelKVByteByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newMutableMap(Map<Byte, Byte> map1,
            Map<Byte, Byte> map2) {
        MutableLHashParallelKVByteByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newMutableMap(Map<Byte, Byte> map1,
            Map<Byte, Byte> map2, Map<Byte, Byte> map3) {
        MutableLHashParallelKVByteByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newMutableMap(Map<Byte, Byte> map1,
            Map<Byte, Byte> map2, Map<Byte, Byte> map3,
            Map<Byte, Byte> map4) {
        MutableLHashParallelKVByteByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newMutableMap(Map<Byte, Byte> map1,
            Map<Byte, Byte> map2, Map<Byte, Byte> map3,
            Map<Byte, Byte> map4, Map<Byte, Byte> map5) {
        MutableLHashParallelKVByteByteMapGO res = uninitializedMutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteByteMap newMutableMap(
            Consumer<net.openhft.koloboke.function.ByteByteConsumer> entriesSupplier
            ) {
        MutableLHashParallelKVByteByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newMutableMap(byte[] keys,
            byte[] values) {
        MutableLHashParallelKVByteByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newMutableMap(
            Byte[] keys, Byte[] values) {
        MutableLHashParallelKVByteByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newMutableMap(Iterable<Byte> keys,
            Iterable<Byte> values) {
        MutableLHashParallelKVByteByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashByteByteMap newMutableMapOf(byte k1, byte v1) {
        MutableLHashParallelKVByteByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newMutableMapOf(byte k1, byte v1,
             byte k2, byte v2) {
        MutableLHashParallelKVByteByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newMutableMapOf(byte k1, byte v1,
             byte k2, byte v2, byte k3, byte v3) {
        MutableLHashParallelKVByteByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newMutableMapOf(byte k1, byte v1,
             byte k2, byte v2, byte k3, byte v3,
             byte k4, byte v4) {
        MutableLHashParallelKVByteByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newMutableMapOf(byte k1, byte v1,
             byte k2, byte v2, byte k3, byte v3,
             byte k4, byte v4, byte k5, byte v5) {
        MutableLHashParallelKVByteByteMapGO map = uninitializedMutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
    
    


    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMap(Map<Byte, Byte> map1,
            Map<Byte, Byte> map2, int expectedSize) {
        ImmutableLHashParallelKVByteByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMap(Map<Byte, Byte> map1,
            Map<Byte, Byte> map2, Map<Byte, Byte> map3, int expectedSize) {
        ImmutableLHashParallelKVByteByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMap(Map<Byte, Byte> map1,
            Map<Byte, Byte> map2, Map<Byte, Byte> map3,
            Map<Byte, Byte> map4, int expectedSize) {
        ImmutableLHashParallelKVByteByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, expectedSize));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMap(Map<Byte, Byte> map1,
            Map<Byte, Byte> map2, Map<Byte, Byte> map3,
            Map<Byte, Byte> map4, Map<Byte, Byte> map5, int expectedSize) {
        ImmutableLHashParallelKVByteByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5, expectedSize));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ByteByteConsumer> entriesSupplier
            , int expectedSize) {
        ImmutableLHashParallelKVByteByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMap(byte[] keys,
            byte[] values, int expectedSize) {
        ImmutableLHashParallelKVByteByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMap(
            Byte[] keys, Byte[] values, int expectedSize) {
        ImmutableLHashParallelKVByteByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMap(Iterable<Byte> keys,
            Iterable<Byte> values, int expectedSize) {
        ImmutableLHashParallelKVByteByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values, expectedSize));
        return map;
    }

    
    

    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMap(
            Map<Byte, Byte> map) {
        ImmutableLHashParallelKVByteByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMap(Map<Byte, Byte> map1,
            Map<Byte, Byte> map2) {
        ImmutableLHashParallelKVByteByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMap(Map<Byte, Byte> map1,
            Map<Byte, Byte> map2, Map<Byte, Byte> map3) {
        ImmutableLHashParallelKVByteByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMap(Map<Byte, Byte> map1,
            Map<Byte, Byte> map2, Map<Byte, Byte> map3,
            Map<Byte, Byte> map4) {
        ImmutableLHashParallelKVByteByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4));
        return res;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMap(Map<Byte, Byte> map1,
            Map<Byte, Byte> map2, Map<Byte, Byte> map3,
            Map<Byte, Byte> map4, Map<Byte, Byte> map5) {
        ImmutableLHashParallelKVByteByteMapGO res = uninitializedImmutableMap();
        res.move(newUpdatableMap(map1, map2, map3, map4, map5));
        return res;
    }



    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMap(
            Consumer<net.openhft.koloboke.function.ByteByteConsumer> entriesSupplier
            ) {
        ImmutableLHashParallelKVByteByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(entriesSupplier));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMap(byte[] keys,
            byte[] values) {
        ImmutableLHashParallelKVByteByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMap(
            Byte[] keys, Byte[] values) {
        ImmutableLHashParallelKVByteByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMap(Iterable<Byte> keys,
            Iterable<Byte> values) {
        ImmutableLHashParallelKVByteByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMap(keys, values));
        return map;
    }


    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMapOf(byte k1, byte v1) {
        ImmutableLHashParallelKVByteByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMapOf(byte k1, byte v1,
             byte k2, byte v2) {
        ImmutableLHashParallelKVByteByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMapOf(byte k1, byte v1,
             byte k2, byte v2, byte k3, byte v3) {
        ImmutableLHashParallelKVByteByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMapOf(byte k1, byte v1,
             byte k2, byte v2, byte k3, byte v3,
             byte k4, byte v4) {
        ImmutableLHashParallelKVByteByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4));
        return map;
    }

    @Override
    @Nonnull
    public  HashByteByteMap newImmutableMapOf(byte k1, byte v1,
             byte k2, byte v2, byte k3, byte v3,
             byte k4, byte v4, byte k5, byte v5) {
        ImmutableLHashParallelKVByteByteMapGO map = uninitializedImmutableMap();
        map.move(newUpdatableMapOf(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
        return map;
    }
}

