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

import net.openhft.koloboke.collect.hash.*;
import net.openhft.koloboke.collect.set.hash.HashIntSetFactory;


public final class QHashIntSetFactoryImpl extends QHashIntSetFactoryGO {

    /** For ServiceLoader */
    public QHashIntSetFactoryImpl() {
        this(HashConfig.getDefault(), 10
                , Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    

    

    public QHashIntSetFactoryImpl(HashConfig hashConf, int defaultExpectedSize
        , int lower, int upper) {
        super(hashConf, defaultExpectedSize
        , lower, upper);
    }

    @Override
    HashIntSetFactory thisWith(HashConfig hashConf, int defaultExpectedSize
        , int lower, int upper) {
        return new QHashIntSetFactoryImpl(hashConf, defaultExpectedSize
        , lower, upper);
    }

    @Override
    HashIntSetFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize
        , int lower, int upper) {
        return new QHashIntSetFactoryImpl(hashConf, defaultExpectedSize
        , lower, upper);
    }
    @Override
    HashIntSetFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize
        , int lower, int upper) {
        return new LHashIntSetFactoryImpl(hashConf, defaultExpectedSize
        , lower, upper);
    }

    HashIntSetFactory withDomain(int lower, int upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return new QHashIntSetFactoryImpl(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public HashIntSetFactory withKeysDomain(int lower, int upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public HashIntSetFactory withKeysDomainComplement(int lower, int upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((int) (upper + 1), (int) (lower - 1));
    }
}

