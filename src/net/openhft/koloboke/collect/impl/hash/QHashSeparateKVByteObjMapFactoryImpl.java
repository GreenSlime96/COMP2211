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
import net.openhft.koloboke.collect.map.hash.*;

import javax.annotation.Nonnull;


public final class QHashSeparateKVByteObjMapFactoryImpl<V>
        extends QHashSeparateKVByteObjMapFactoryGO<V> {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public QHashSeparateKVByteObjMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    

    

    

    QHashSeparateKVByteObjMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashByteObjMapFactory<V> thisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
        return new QHashSeparateKVByteObjMapFactoryImpl<V>(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashByteObjMapFactory<V> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
        return new QHashSeparateKVByteObjMapFactoryImpl<V>(hashConf, defaultExpectedSize, lower, upper);
    }
    @Override
    HashByteObjMapFactory<V> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
        return new LHashSeparateKVByteObjMapFactoryImpl<V>(hashConf, defaultExpectedSize, lower, upper);
    }


    @Override
    @Nonnull
    public HashByteObjMapFactory<V> withValueEquivalence(
            @Nonnull Equivalence<? super V> valueEquivalence) {
        if (valueEquivalence.equals(Equivalence.defaultEquality())) {
            // noinspection unchecked
            return (HashByteObjMapFactory<V>) this;
        }
        return new WithCustomValueEquivalence<V>(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(),
                (Equivalence<V>) valueEquivalence);
    }


    static final class WithCustomValueEquivalence<V>
            extends QHashSeparateKVByteObjMapFactoryGO<V> {

        private final Equivalence<V> valueEquivalence;
        WithCustomValueEquivalence(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper,
                Equivalence<V> valueEquivalence) {
            super(hashConf, defaultExpectedSize, lower, upper);
            this.valueEquivalence = valueEquivalence;
        }

        @Override
        @Nonnull
        public Equivalence<V> getValueEquivalence() {
            return valueEquivalence;
        }

        @Override
        <V2 extends V> MutableQHashSeparateKVByteObjMapGO<V2>
        uninitializedMutableMap() {
            MutableQHashSeparateKVByteObjMap.WithCustomValueEquivalence<V2> map =
                    new MutableQHashSeparateKVByteObjMap.WithCustomValueEquivalence<V2>();
            map.valueEquivalence = valueEquivalence;
            return map;
        }
        @Override
        <V2 extends V> UpdatableQHashSeparateKVByteObjMapGO<V2>
        uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVByteObjMap.WithCustomValueEquivalence<V2> map =
                    new UpdatableQHashSeparateKVByteObjMap.WithCustomValueEquivalence<V2>();
            map.valueEquivalence = valueEquivalence;
            return map;
        }
        @Override
        <V2 extends V> ImmutableQHashSeparateKVByteObjMapGO<V2>
        uninitializedImmutableMap() {
            ImmutableQHashSeparateKVByteObjMap.WithCustomValueEquivalence<V2> map =
                    new ImmutableQHashSeparateKVByteObjMap.WithCustomValueEquivalence<V2>();
            map.valueEquivalence = valueEquivalence;
            return map;
        }


        @Override
        @Nonnull
        public HashByteObjMapFactory<V> withValueEquivalence(
                @Nonnull Equivalence<? super V> valueEquivalence) {
            if (valueEquivalence.equals(Equivalence.defaultEquality()))
                return new QHashSeparateKVByteObjMapFactoryImpl<V>(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound());
            if (valueEquivalence.equals(this.valueEquivalence))
                // noinspection unchecked
                return (HashByteObjMapFactory<V>) this;
            return new WithCustomValueEquivalence<V>(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(),
                    (Equivalence<V>) valueEquivalence);
        }

        @Override
        HashByteObjMapFactory<V> thisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
            return new WithCustomValueEquivalence<V>(hashConf, defaultExpectedSize, lower, upper,
                    valueEquivalence);
        }

        @Override
        HashByteObjMapFactory<V> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
            return new QHashSeparateKVByteObjMapFactoryImpl.WithCustomValueEquivalence<V>(
                    hashConf, defaultExpectedSize, lower, upper, valueEquivalence);
        }
        @Override
        HashByteObjMapFactory<V> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper) {
            return new LHashSeparateKVByteObjMapFactoryImpl.WithCustomValueEquivalence<V>(
                    hashConf, defaultExpectedSize, lower, upper, valueEquivalence);
        }
    }

}

