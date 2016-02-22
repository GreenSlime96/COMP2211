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


public final class QHashSeparateKVDoubleObjMapFactoryImpl<V>
        extends QHashSeparateKVDoubleObjMapFactoryGO<V> {

    

    

    
    
    

    
    
    

    /** For ServiceLoader */
    public QHashSeparateKVDoubleObjMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            );
    }

    

    

    

    QHashSeparateKVDoubleObjMapFactoryImpl(HashConfig hashConf, int defaultExpectedSize) {
        super(hashConf, defaultExpectedSize);
    }

    @Override
    HashDoubleObjMapFactory<V> thisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new QHashSeparateKVDoubleObjMapFactoryImpl<V>(hashConf, defaultExpectedSize);
    }

    @Override
    HashDoubleObjMapFactory<V> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new QHashSeparateKVDoubleObjMapFactoryImpl<V>(hashConf, defaultExpectedSize);
    }
    @Override
    HashDoubleObjMapFactory<V> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
        return new LHashSeparateKVDoubleObjMapFactoryImpl<V>(hashConf, defaultExpectedSize);
    }


    @Override
    @Nonnull
    public HashDoubleObjMapFactory<V> withValueEquivalence(
            @Nonnull Equivalence<? super V> valueEquivalence) {
        if (valueEquivalence.equals(Equivalence.defaultEquality())) {
            // noinspection unchecked
            return (HashDoubleObjMapFactory<V>) this;
        }
        return new WithCustomValueEquivalence<V>(getHashConfig(), getDefaultExpectedSize()
        ,
                (Equivalence<V>) valueEquivalence);
    }


    static final class WithCustomValueEquivalence<V>
            extends QHashSeparateKVDoubleObjMapFactoryGO<V> {

        private final Equivalence<V> valueEquivalence;
        WithCustomValueEquivalence(HashConfig hashConf, int defaultExpectedSize,
                Equivalence<V> valueEquivalence) {
            super(hashConf, defaultExpectedSize);
            this.valueEquivalence = valueEquivalence;
        }

        @Override
        @Nonnull
        public Equivalence<V> getValueEquivalence() {
            return valueEquivalence;
        }

        @Override
        <V2 extends V> MutableQHashSeparateKVDoubleObjMapGO<V2>
        uninitializedMutableMap() {
            MutableQHashSeparateKVDoubleObjMap.WithCustomValueEquivalence<V2> map =
                    new MutableQHashSeparateKVDoubleObjMap.WithCustomValueEquivalence<V2>();
            map.valueEquivalence = valueEquivalence;
            return map;
        }
        @Override
        <V2 extends V> UpdatableQHashSeparateKVDoubleObjMapGO<V2>
        uninitializedUpdatableMap() {
            UpdatableQHashSeparateKVDoubleObjMap.WithCustomValueEquivalence<V2> map =
                    new UpdatableQHashSeparateKVDoubleObjMap.WithCustomValueEquivalence<V2>();
            map.valueEquivalence = valueEquivalence;
            return map;
        }
        @Override
        <V2 extends V> ImmutableQHashSeparateKVDoubleObjMapGO<V2>
        uninitializedImmutableMap() {
            ImmutableQHashSeparateKVDoubleObjMap.WithCustomValueEquivalence<V2> map =
                    new ImmutableQHashSeparateKVDoubleObjMap.WithCustomValueEquivalence<V2>();
            map.valueEquivalence = valueEquivalence;
            return map;
        }


        @Override
        @Nonnull
        public HashDoubleObjMapFactory<V> withValueEquivalence(
                @Nonnull Equivalence<? super V> valueEquivalence) {
            if (valueEquivalence.equals(Equivalence.defaultEquality()))
                return new QHashSeparateKVDoubleObjMapFactoryImpl<V>(getHashConfig(), getDefaultExpectedSize()
        );
            if (valueEquivalence.equals(this.valueEquivalence))
                // noinspection unchecked
                return (HashDoubleObjMapFactory<V>) this;
            return new WithCustomValueEquivalence<V>(getHashConfig(), getDefaultExpectedSize()
        ,
                    (Equivalence<V>) valueEquivalence);
        }

        @Override
        HashDoubleObjMapFactory<V> thisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new WithCustomValueEquivalence<V>(hashConf, defaultExpectedSize,
                    valueEquivalence);
        }

        @Override
        HashDoubleObjMapFactory<V> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new QHashSeparateKVDoubleObjMapFactoryImpl.WithCustomValueEquivalence<V>(
                    hashConf, defaultExpectedSize, valueEquivalence);
        }
        @Override
        HashDoubleObjMapFactory<V> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize) {
            return new LHashSeparateKVDoubleObjMapFactoryImpl.WithCustomValueEquivalence<V>(
                    hashConf, defaultExpectedSize, valueEquivalence);
        }
    }

}

