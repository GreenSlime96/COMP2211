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

import net.openhft.koloboke.collect.ShortCollection;
import net.openhft.koloboke.function.ShortConsumer;
import net.openhft.koloboke.function.ShortPredicate;
import net.openhft.koloboke.collect.set.ShortSet;

import java.util.Collection;


public final class CommonShortCollectionOps {

    public static boolean containsAll(final ShortCollection collection,
            Collection<?> another) {
        if (collection == another)
            return true;
        if (another instanceof ShortCollection) {
            ShortCollection c2 = (ShortCollection) another;
            if (collection instanceof ShortSet && c2 instanceof ShortSet &&
                    collection.size() < another.size()) {
                return false;
            }
            if (c2 instanceof InternalShortCollectionOps) {
                // noinspection unchecked
                return ((InternalShortCollectionOps) c2).allContainingIn(collection);
            }
            return c2.forEachWhile(new
                    ShortPredicate() {
                @Override
                public boolean test(short value) {
                    return collection.contains(value);
                }
            });
        } else {
            for (Object o : another) {
                if (!collection.contains(((Short) o).shortValue()
                        ))
                    return false;
            }
            return true;
        }
    }

    public static  boolean addAll(final ShortCollection collection,
            Collection<? extends Short> another) {
        if (collection == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = collection.sizeAsLong() + Containers.sizeAsLong(another);
        collection.ensureCapacity(maxPossibleSize);
        if (another instanceof ShortCollection) {
            if (another instanceof InternalShortCollectionOps) {
                return ((InternalShortCollectionOps) another).reverseAddAllTo(collection);
            } else {
                class AddAll implements ShortConsumer {
                    boolean collectionChanged = false;
                    @Override
                    public void accept(short value) {
                        collectionChanged |= collection.add(value);
                    }
                }
                AddAll addAll = new AddAll();
                ((ShortCollection) another).forEach(addAll);
                return addAll.collectionChanged;
            }
        } else {
            boolean collectionChanged = false;
            for (Short v : another) {
                collectionChanged |= collection.add(v.shortValue());
            }
            return collectionChanged;
        }
    }


    private CommonShortCollectionOps() {}
}

