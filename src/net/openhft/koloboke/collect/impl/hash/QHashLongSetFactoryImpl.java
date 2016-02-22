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
import net.openhft.koloboke.collect.set.hash.HashLongSetFactory;


public final class QHashLongSetFactoryImpl extends QHashLongSetFactoryGO {

    /** For ServiceLoader */
    public QHashLongSetFactoryImpl() {
        this(HashConfig.getDefault(), 10
                , Long.MIN_VALUE, Long.MAX_VALUE);
    }

    

    

    public QHashLongSetFactoryImpl(HashConfig hashConf, int defaultExpectedSize
        , long lower, long upper) {
        super(hashConf, defaultExpectedSize
        , lower, upper);
    }

    @Override
    HashLongSetFactory thisWith(HashConfig hashConf, int defaultExpectedSize
        , long lower, long upper) {
        return new QHashLongSetFactoryImpl(hashConf, defaultExpectedSize
        , lower, upper);
    }

    @Override
    HashLongSetFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize
        , long lower, long upper) {
        return new QHashLongSetFactoryImpl(hashConf, defaultExpectedSize
        , lower, upper);
    }
    @Override
    HashLongSetFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize
        , long lower, long upper) {
        return new LHashLongSetFactoryImpl(hashConf, defaultExpectedSize
        , lower, upper);
    }

    HashLongSetFactory withDomain(long lower, long upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return new QHashLongSetFactoryImpl(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public HashLongSetFactory withKeysDomain(long lower, long upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public HashLongSetFactory withKeysDomainComplement(long lower, long upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((long) (upper + 1), (long) (lower - 1));
    }
}

