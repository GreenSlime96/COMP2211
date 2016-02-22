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

import net.openhft.koloboke.collect.LongCollection;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;
import net.openhft.koloboke.collect.set.LongSet;

import java.util.Collection;


public final class CommonLongCollectionOps {

    public static boolean containsAll(final LongCollection collection,
            Collection<?> another) {
        if (collection == another)
            return true;
        if (another instanceof LongCollection) {
            LongCollection c2 = (LongCollection) another;
            if (collection instanceof LongSet && c2 instanceof LongSet &&
                    collection.size() < another.size()) {
                return false;
            }
            if (c2 instanceof InternalLongCollectionOps) {
                // noinspection unchecked
                return ((InternalLongCollectionOps) c2).allContainingIn(collection);
            }
            return c2.forEachWhile(new
                    LongPredicate() {
                @Override
                public boolean test(long value) {
                    return collection.contains(value);
                }
            });
        } else {
            for (Object o : another) {
                if (!collection.contains(((Long) o).longValue()
                        ))
                    return false;
            }
            return true;
        }
    }

    public static  boolean addAll(final LongCollection collection,
            Collection<? extends Long> another) {
        if (collection == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = collection.sizeAsLong() + Containers.sizeAsLong(another);
        collection.ensureCapacity(maxPossibleSize);
        if (another instanceof LongCollection) {
            if (another instanceof InternalLongCollectionOps) {
                return ((InternalLongCollectionOps) another).reverseAddAllTo(collection);
            } else {
                class AddAll implements LongConsumer {
                    boolean collectionChanged = false;
                    @Override
                    public void accept(long value) {
                        collectionChanged |= collection.add(value);
                    }
                }
                AddAll addAll = new AddAll();
                ((LongCollection) another).forEach(addAll);
                return addAll.collectionChanged;
            }
        } else {
            boolean collectionChanged = false;
            for (Long v : another) {
                collectionChanged |= collection.add(v.longValue());
            }
            return collectionChanged;
        }
    }


    private CommonLongCollectionOps() {}
}

