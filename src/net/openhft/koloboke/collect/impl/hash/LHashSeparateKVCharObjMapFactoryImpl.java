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


public final class LHashSeparateKVCharObjMapFactoryImpl<V>
        extends LHashSeparateKVCharObjMapFactoryGO<V> {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public LHashSeparateKVCharObjMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , Character.MIN_VALUE, Character.MAX_VALUE);
    }

    

    

    

    LHashSeparateKVCharObjMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashCharObjMapFactory<V> thisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
        return new LHashSeparateKVCharObjMapFactoryImpl<V>(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashCharObjMapFactory<V> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
        return new QHashSeparateKVCharObjMapFactoryImpl<V>(hashConf, defaultExpectedSize, lower, upper);
    }
    @Override
    HashCharObjMapFactory<V> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
        return new LHashSeparateKVCharObjMapFactoryImpl<V>(hashConf, defaultExpectedSize, lower, upper);
    }


    @Override
    @Nonnull
    public HashCharObjMapFactory<V> withValueEquivalence(
            @Nonnull Equivalence<? super V> valueEquivalence) {
        if (valueEquivalence.equals(Equivalence.defaultEquality())) {
            // noinspection unchecked
            return (HashCharObjMapFactory<V>) this;
        }
        return new WithCustomValueEquivalence<V>(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(),
                (Equivalence<V>) valueEquivalence);
    }


    static final class WithCustomValueEquivalence<V>
            extends LHashSeparateKVCharObjMapFactoryGO<V> {

        private final Equivalence<V> valueEquivalence;
        WithCustomValueEquivalence(HashConfig hashConf, int defaultExpectedSize, char lower, char upper,
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
        <V2 extends V> MutableLHashSeparateKVCharObjMapGO<V2>
        uninitializedMutableMap() {
            MutableLHashSeparateKVCharObjMap.WithCustomValueEquivalence<V2> map =
                    new MutableLHashSeparateKVCharObjMap.WithCustomValueEquivalence<V2>();
            map.valueEquivalence = valueEquivalence;
            return map;
        }
        @Override
        <V2 extends V> UpdatableLHashSeparateKVCharObjMapGO<V2>
        uninitializedUpdatableMap() {
            UpdatableLHashSeparateKVCharObjMap.WithCustomValueEquivalence<V2> map =
                    new UpdatableLHashSeparateKVCharObjMap.WithCustomValueEquivalence<V2>();
            map.valueEquivalence = valueEquivalence;
            return map;
        }
        @Override
        <V2 extends V> ImmutableLHashSeparateKVCharObjMapGO<V2>
        uninitializedImmutableMap() {
            ImmutableLHashSeparateKVCharObjMap.WithCustomValueEquivalence<V2> map =
                    new ImmutableLHashSeparateKVCharObjMap.WithCustomValueEquivalence<V2>();
            map.valueEquivalence = valueEquivalence;
            return map;
        }


        @Override
        @Nonnull
        public HashCharObjMapFactory<V> withValueEquivalence(
                @Nonnull Equivalence<? super V> valueEquivalence) {
            if (valueEquivalence.equals(Equivalence.defaultEquality()))
                return new LHashSeparateKVCharObjMapFactoryImpl<V>(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound());
            if (valueEquivalence.equals(this.valueEquivalence))
                // noinspection unchecked
                return (HashCharObjMapFactory<V>) this;
            return new WithCustomValueEquivalence<V>(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(),
                    (Equivalence<V>) valueEquivalence);
        }

        @Override
        HashCharObjMapFactory<V> thisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
            return new WithCustomValueEquivalence<V>(hashConf, defaultExpectedSize, lower, upper,
                    valueEquivalence);
        }

        @Override
        HashCharObjMapFactory<V> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
            return new QHashSeparateKVCharObjMapFactoryImpl.WithCustomValueEquivalence<V>(
                    hashConf, defaultExpectedSize, lower, upper, valueEquivalence);
        }
        @Override
        HashCharObjMapFactory<V> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper) {
            return new LHashSeparateKVCharObjMapFactoryImpl.WithCustomValueEquivalence<V>(
                    hashConf, defaultExpectedSize, lower, upper, valueEquivalence);
        }
    }

}

