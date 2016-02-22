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

import net.openhft.koloboke.collect.IntCollection;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import net.openhft.koloboke.collect.set.IntSet;

import java.util.Collection;


public final class CommonIntCollectionOps {

    public static boolean containsAll(final IntCollection collection,
            Collection<?> another) {
        if (collection == another)
            return true;
        if (another instanceof IntCollection) {
            IntCollection c2 = (IntCollection) another;
            if (collection instanceof IntSet && c2 instanceof IntSet &&
                    collection.size() < another.size()) {
                return false;
            }
            if (c2 instanceof InternalIntCollectionOps) {
                // noinspection unchecked
                return ((InternalIntCollectionOps) c2).allContainingIn(collection);
            }
            return c2.forEachWhile(new
                    IntPredicate() {
                @Override
                public boolean test(int value) {
                    return collection.contains(value);
                }
            });
        } else {
            for (Object o : another) {
                if (!collection.contains(((Integer) o).intValue()
                        ))
                    return false;
            }
            return true;
        }
    }

    public static  boolean addAll(final IntCollection collection,
            Collection<? extends Integer> another) {
        if (collection == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = collection.sizeAsLong() + Containers.sizeAsLong(another);
        collection.ensureCapacity(maxPossibleSize);
        if (another instanceof IntCollection) {
            if (another instanceof InternalIntCollectionOps) {
                return ((InternalIntCollectionOps) another).reverseAddAllTo(collection);
            } else {
                class AddAll implements IntConsumer {
                    boolean collectionChanged = false;
                    @Override
                    public void accept(int value) {
                        collectionChanged |= collection.add(value);
                    }
                }
                AddAll addAll = new AddAll();
                ((IntCollection) another).forEach(addAll);
                return addAll.collectionChanged;
            }
        } else {
            boolean collectionChanged = false;
            for (Integer v : another) {
                collectionChanged |= collection.add(v.intValue());
            }
            return collectionChanged;
        }
    }


    private CommonIntCollectionOps() {}
}

