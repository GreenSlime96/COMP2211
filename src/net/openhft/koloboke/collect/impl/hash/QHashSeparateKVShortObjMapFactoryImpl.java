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


public final class QHashSeparateKVShortObjMapFactoryImpl<V>
        extends QHashSeparateKVShortObjMapFactoryGO<V> {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public QHashSeparateKVShortObjMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            , Short.MIN_VALUE, Short.MAX_VALUE);
    }

    

    

    

    QHashSeparateKVShortObjMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashShortObjMapFactory<V> thisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
        return new QHashSeparateKVShortObjMapFactoryImpl<V>(hashConf, defaultExpectedSize, lower, upper);
    }

    @Override
    HashShortObjMapFactory<V> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
        return new QHashSeparateKVShortObjMapFactoryImpl<V>(hashConf, defaultExpectedSize, lower, upper);
    }
    @Override
    HashShortObjMapFactory<V> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
        return new LHashSeparateKVShortObjMapFactoryImpl<V>(hashConf, defaultExpectedSize, lower, upper);
    }


    @Override
    @Nonnull
    public HashShortObjMapFactory<V> withValueEquivalence(
            @Nonnull Equivalence<? super V> valueEquivalence) {
        if (valueEquivalence.equals(Equivalence.defaultEquality())) {
            // noinspection unchecked
            return (HashShortObjMapFactory<V>) this;
        }
        return new WithCustomValueEquivalence<V>(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(),
                (Equivalence<V>) valueEquivalence);
    }


    static final class WithCustomValueEquivalence<V>
            extends QHashSeparateKVShortObjMapFactoryGO<V> {

        private final Equivalence<V> valueEquivalence;
        WithCustomValueEquivalence(HashConfig hashConf, int defaultExpectedSize, short lower, short upper,
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
        <V2 extends V> MutableQHashSeparateKVShortObjMapGO<V2>
        uninitializedMutableMap() {
            MutableQHashSeparateKVShortObjMap.WithCustomValueEquivalence<V2> map =
                    new MutableQHashSeparateKVShortObjMap.WithCustomValueEquivalence<V2>();
            map.valueEquivalence = valueEquivalence;
            return map;
        }
        @Override
        <V2 extends V> UpdatableQHashSeparateKVShortObjMapGO<V2>
        uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVShortObjMap.WithCustomValueEquivalence<V2> map =
                    new UpdatableQHashSeparateKVShortObjMap.WithCustomValueEquivalence<V2>();
            map.valueEquivalence = valueEquivalence;
            return map;
        }
        @Override
        <V2 extends V> ImmutableQHashSeparateKVShortObjMapGO<V2>
        uninitializedImmutableMap() {
            ImmutableQHashSeparateKVShortObjMap.WithCustomValueEquivalence<V2> map =
                    new ImmutableQHashSeparateKVShortObjMap.WithCustomValueEquivalence<V2>();
            map.valueEquivalence = valueEquivalence;
            return map;
        }


        @Override
        @Nonnull
        public HashShortObjMapFactory<V> withValueEquivalence(
                @Nonnull Equivalence<? super V> valueEquivalence) {
            if (valueEquivalence.equals(Equivalence.defaultEquality()))
                return new QHashSeparateKVShortObjMapFactoryImpl<V>(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound());
            if (valueEquivalence.equals(this.valueEquivalence))
                // noinspection unchecked
                return (HashShortObjMapFactory<V>) this;
            return new WithCustomValueEquivalence<V>(getHashConfig(), getDefaultExpectedSize()
        
            , getLowerKeyDomainBound(), getUpperKeyDomainBound(),
                    (Equivalence<V>) valueEquivalence);
        }

        @Override
        HashShortObjMapFactory<V> thisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
            return new WithCustomValueEquivalence<V>(hashConf, defaultExpectedSize, lower, upper,
                    valueEquivalence);
        }

        @Override
        HashShortObjMapFactory<V> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
            return new QHashSeparateKVShortObjMapFactoryImpl.WithCustomValueEquivalence<V>(
                    hashConf, defaultExpectedSize, lower, upper, valueEquivalence);
        }
        @Override
        HashShortObjMapFactory<V> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper) {
            return new LHashSeparateKVShortObjMapFactoryImpl.WithCustomValueEquivalence<V>(
                    hashConf, defaultExpectedSize, lower, upper, valueEquivalence);
        }
    }

}

