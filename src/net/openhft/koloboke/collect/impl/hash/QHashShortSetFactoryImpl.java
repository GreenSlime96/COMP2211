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
import net.openhft.koloboke.collect.set.hash.HashShortSetFactory;


public final class QHashShortSetFactoryImpl extends QHashShortSetFactoryGO {

    /** For ServiceLoader */
    public QHashShortSetFactoryImpl() {
        this(HashConfig.getDefault(), 10
                , Short.MIN_VALUE, Short.MAX_VALUE);
    }

    

    

    public QHashShortSetFactoryImpl(HashConfig hashConf, int defaultExpectedSize
        , short lower, short upper) {
        super(hashConf, defaultExpectedSize
        , lower, upper);
    }

    @Override
    HashShortSetFactory thisWith(HashConfig hashConf, int defaultExpectedSize
        , short lower, short upper) {
        return new QHashShortSetFactoryImpl(hashConf, defaultExpectedSize
        , lower, upper);
    }

    @Override
    HashShortSetFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize
        , short lower, short upper) {
        return new QHashShortSetFactoryImpl(hashConf, defaultExpectedSize
        , lower, upper);
    }
    @Override
    HashShortSetFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize
        , short lower, short upper) {
        return new LHashShortSetFactoryImpl(hashConf, defaultExpectedSize
        , lower, upper);
    }

    HashShortSetFactory withDomain(short lower, short upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return new QHashShortSetFactoryImpl(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public HashShortSetFactory withKeysDomain(short lower, short upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public HashShortSetFactory withKeysDomainComplement(short lower, short upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((short) (upper + 1), (short) (lower - 1));
    }
}

