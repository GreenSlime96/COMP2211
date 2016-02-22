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
import net.openhft.koloboke.collect.set.hash.HashByteSetFactory;


public final class QHashByteSetFactoryImpl extends QHashByteSetFactoryGO {

    /** For ServiceLoader */
    public QHashByteSetFactoryImpl() {
        this(HashConfig.getDefault(), 10
                , Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    

    

    public QHashByteSetFactoryImpl(HashConfig hashConf, int defaultExpectedSize
        , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize
        , lower, upper);
    }

    @Override
    HashByteSetFactory thisWith(HashConfig hashConf, int defaultExpectedSize
        , byte lower, byte upper) {
        return new QHashByteSetFactoryImpl(hashConf, defaultExpectedSize
        , lower, upper);
    }

    @Override
    HashByteSetFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize
        , byte lower, byte upper) {
        return new QHashByteSetFactoryImpl(hashConf, defaultExpectedSize
        , lower, upper);
    }
    @Override
    HashByteSetFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize
        , byte lower, byte upper) {
        return new LHashByteSetFactoryImpl(hashConf, defaultExpectedSize
        , lower, upper);
    }

    HashByteSetFactory withDomain(byte lower, byte upper) {
        if (lower == getLowerKeyDomainBound() && upper == getUpperKeyDomainBound())
            return this;
        return new QHashByteSetFactoryImpl(getHashConfig(), getDefaultExpectedSize(), lower, upper);
    }

    @Override
    public HashByteSetFactory withKeysDomain(byte lower, byte upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minPossibleKey shouldn't be greater " +
                    "than maxPossibleKey");
        return withDomain(lower, upper);
    }

    @Override
    public HashByteSetFactory withKeysDomainComplement(byte lower, byte upper) {
        if (lower > upper)
            throw new IllegalArgumentException("minImpossibleKey shouldn't be greater " +
                    "than maxImpossibleKey");
        return withDomain((byte) (upper + 1), (byte) (lower - 1));
    }
}

