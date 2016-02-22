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

package net.openhft.koloboke.collect.impl;

import net.openhft.koloboke.collect.FloatCollection;
import net.openhft.koloboke.function.FloatConsumer;
import net.openhft.koloboke.function.FloatPredicate;
import net.openhft.koloboke.collect.set.FloatSet;

import java.util.Collection;


public final class CommonFloatCollectionOps {

    public static boolean containsAll(final FloatCollection collection,
            Collection<?> another) {
        if (collection == another)
            return true;
        if (another instanceof FloatCollection) {
            FloatCollection c2 = (FloatCollection) another;
            if (collection instanceof FloatSet && c2 instanceof FloatSet &&
                    collection.size() < another.size()) {
                return false;
            }
            if (c2 instanceof InternalFloatCollectionOps) {
                // noinspection unchecked
                return ((InternalFloatCollectionOps) c2).allContainingIn(collection);
            }
            return c2.forEachWhile(new
                    FloatPredicate() {
                @Override
                public boolean test(float value) {
                    return collection.contains(value);
                }
            });
        } else {
            for (Object o : another) {
                if (!collection.contains(((Float) o).floatValue()
                        ))
                    return false;
            }
            return true;
        }
    }

    public static  boolean addAll(final FloatCollection collection,
            Collection<? extends Float> another) {
        if (collection == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = collection.sizeAsLong() + Containers.sizeAsLong(another);
        collection.ensureCapacity(maxPossibleSize);
        if (another instanceof FloatCollection) {
            if (another instanceof InternalFloatCollectionOps) {
                return ((InternalFloatCollectionOps) another).reverseAddAllTo(collection);
            } else {
                class AddAll implements FloatConsumer {
                    boolean collectionChanged = false;
                    @Override
                    public void accept(float value) {
                        collectionChanged |= collection.add(value);
                    }
                }
                AddAll addAll = new AddAll();
                ((FloatCollection) another).forEach(addAll);
                return addAll.collectionChanged;
            }
        } else {
            boolean collectionChanged = false;
            for (Float v : another) {
                collectionChanged |= collection.add(v.floatValue());
            }
            return collectionChanged;
        }
    }


    private CommonFloatCollectionOps() {}
}

