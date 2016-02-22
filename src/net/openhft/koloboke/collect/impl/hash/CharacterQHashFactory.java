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

import java.util.Random;
import java.util.concurrent
     .ThreadLocalRandom;


abstract class CharacterQHashFactory
        <MT> extends CharHashFactorySO {

    CharacterQHashFactory(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    abstract MT createNewMutable(int expectedSize, char free, char removed);

    

    MT newMutableHash(int expectedSize) {
        char free, removed;
        if (randomRemoved) {
            Random random = ThreadLocalRandom.current();
            removed = (char) random.nextInt();
            if (randomFree) {
                free = (char) random.nextInt();
            } else {
                free = freeValue;
            }
            while (free == removed) {
                removed = (char) random.nextInt();
            }
        } else {
            removed = removedValue;
            free = freeValue;
        }
        return createNewMutable(expectedSize, free, removed);
    }

}
