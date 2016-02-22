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


public final class LHashSeparateKVLongObjMapFactoryImpl<V>
        extends LHashSeparateKVLongObjMapFactoryGO<V> {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public LHashSeparateKVLongObjMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , Long.MIN_VALUE, Long.MAX_VALUE);
    }

    

    

    

    LHashSeparateKVLongObjMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashLongObjMapFactory<V> thisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
        return new LHashSeparateKVLongObjMapFactoryImpl<V>(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashLongObjMapFactory<V> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
        return new QHashSeparateKVLongObjMapFactoryImpl<V>(hashConf, defaultExpectedSize, lower, upper);
    }
    @Override
    HashLongObjMapFactory<V> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
        return new LHashSeparateKVLongObjMapFactoryImpl<V>(hashConf, defaultExpectedSize, lower, upper);
    }


    @Override
    @Nonnull
    public HashLongObjMapFactory<V> withValueEquivalence(
            @Nonnull Equivalence<? super V> valueEquivalence) {
        if (valueEquivalence.equals(Equivalence.defaultEquality())) {
            // noinspection unchecked
            return (HashLongObjMapFactory<V>) this;
        }
        return new WithCustomValueEquivalence<V>(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(),
                (Equivalence<V>) valueEquivalence);
    }


    static final class WithCustomValueEquivalence<V>
            extends LHashSeparateKVLongObjMapFactoryGO<V> {

        private final Equivalence<V> valueEquivalence;
        WithCustomValueEquivalence(HashConfig hashConf, int defaultExpectedSize, long lower, long upper,
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
        <V2 extends V> MutableLHashSeparateKVLongObjMapGO<V2>
        uninitializedMutableMap() {
            MutableLHashSeparateKVLongObjMap.WithCustomValueEquivalence<V2> map =
                    new MutableLHashSeparateKVLongObjMap.WithCustomValueEquivalence<V2>();
            map.valueEquivalence = valueEquivalence;
            return map;
        }
        @Override
        <V2 extends V> UpdatableLHashSeparateKVLongObjMapGO<V2>
        uninitializedUpdatableMap() {
            UpdatableLHashSeparateKVLongObjMap.WithCustomValueEquivalence<V2> map =
                    new UpdatableLHashSeparateKVLongObjMap.WithCustomValueEquivalence<V2>();
            map.valueEquivalence = valueEquivalence;
            return map;
        }
        @Override
        <V2 extends V> ImmutableLHashSeparateKVLongObjMapGO<V2>
        uninitializedImmutableMap() {
            ImmutableLHashSeparateKVLongObjMap.WithCustomValueEquivalence<V2> map =
                    new ImmutableLHashSeparateKVLongObjMap.WithCustomValueEquivalence<V2>();
            map.valueEquivalence = valueEquivalence;
            return map;
        }


        @Override
        @Nonnull
        public HashLongObjMapFactory<V> withValueEquivalence(
                @Nonnull Equivalence<? super V> valueEquivalence) {
            if (valueEquivalence.equals(Equivalence.defaultEquality()))
                return new LHashSeparateKVLongObjMapFactoryImpl<V>(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound());
            if (valueEquivalence.equals(this.valueEquivalence))
                // noinspection unchecked
                return (HashLongObjMapFactory<V>) this;
            return new WithCustomValueEquivalence<V>(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(),
                    (Equivalence<V>) valueEquivalence);
        }

        @Override
        HashLongObjMapFactory<V> thisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
            return new WithCustomValueEquivalence<V>(hashConf, defaultExpectedSize, lower, upper,
                    valueEquivalence);
        }

        @Override
        HashLongObjMapFactory<V> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
            return new QHashSeparateKVLongObjMapFactoryImpl.WithCustomValueEquivalence<V>(
                    hashConf, defaultExpectedSize, lower, upper, valueEquivalence);
        }
        @Override
        HashLongObjMapFactory<V> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper) {
            return new LHashSeparateKVLongObjMapFactoryImpl.WithCustomValueEquivalence<V>(
                    hashConf, defaultExpectedSize, lower, upper, valueEquivalence);
        }
    }

}

