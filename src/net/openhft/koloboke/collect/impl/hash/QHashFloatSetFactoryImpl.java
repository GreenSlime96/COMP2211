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
import net.openhft.koloboke.collect.set.hash.HashFloatSetFactory;


public final class QHashFloatSetFactoryImpl extends QHashFloatSetFactoryGO {

    /** For ServiceLoader */
    public QHashFloatSetFactoryImpl() {
        this(HashConfig.getDefault(), 10
                );
    }

    

    

    public QHashFloatSetFactoryImpl(HashConfig hashConf, int defaultExpectedSize
        ) {
        super(hashConf, defaultExpectedSize
        );
    }

    @Override
    HashFloatSetFactory thisWith(HashConfig hashConf, int defaultExpectedSize
        ) {
        return new QHashFloatSetFactoryImpl(hashConf, defaultExpectedSize
        );
    }

    @Override
    HashFloatSetFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize
        ) {
        return new QHashFloatSetFactoryImpl(hashConf, defaultExpectedSize
        );
    }
    @Override
    HashFloatSetFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize
        ) {
        return new LHashFloatSetFactoryImpl(hashConf, defaultExpectedSize
        );
    }

}

