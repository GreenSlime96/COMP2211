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

import net.openhft.koloboke.collect.DoubleCollection;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import net.openhft.koloboke.collect.set.DoubleSet;

import java.util.Collection;


public final class CommonDoubleCollectionOps {

    public static boolean containsAll(final DoubleCollection collection,
            Collection<?> another) {
        if (collection == another)
            return true;
        if (another instanceof DoubleCollection) {
            DoubleCollection c2 = (DoubleCollection) another;
            if (collection instanceof DoubleSet && c2 instanceof DoubleSet &&
                    collection.size() < another.size()) {
                return false;
            }
            if (c2 instanceof InternalDoubleCollectionOps) {
                // noinspection unchecked
                return ((InternalDoubleCollectionOps) c2).allContainingIn(collection);
            }
            return c2.forEachWhile(new
                    DoublePredicate() {
                @Override
                public boolean test(double value) {
                    return collection.contains(value);
                }
            });
        } else {
            for (Object o : another) {
                if (!collection.contains(((Double) o).doubleValue()
                        ))
                    return false;
            }
            return true;
        }
    }

    public static  boolean addAll(final DoubleCollection collection,
            Collection<? extends Double> another) {
        if (collection == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = collection.sizeAsLong() + Containers.sizeAsLong(another);
        collection.ensureCapacity(maxPossibleSize);
        if (another instanceof DoubleCollection) {
            if (another instanceof InternalDoubleCollectionOps) {
                return ((InternalDoubleCollectionOps) another).reverseAddAllTo(collection);
            } else {
                class AddAll implements DoubleConsumer {
                    boolean collectionChanged = false;
                    @Override
                    public void accept(double value) {
                        collectionChanged |= collection.add(value);
                    }
                }
                AddAll addAll = new AddAll();
                ((DoubleCollection) another).forEach(addAll);
                return addAll.collectionChanged;
            }
        } else {
            boolean collectionChanged = false;
            for (Double v : another) {
                collectionChanged |= collection.add(v.doubleValue());
            }
            return collectionChanged;
        }
    }


    private CommonDoubleCollectionOps() {}
}

