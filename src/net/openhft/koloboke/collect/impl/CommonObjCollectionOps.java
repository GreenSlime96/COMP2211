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

import net.openhft.koloboke.collect.ObjCollection;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.set.ObjSet;

import java.util.Collection;


public final class CommonObjCollectionOps {

    public static boolean containsAll(final ObjCollection<?> collection,
            Collection<?> another) {
        if (collection == another)
            return true;
        if (another instanceof ObjCollection) {
            ObjCollection c2 = (ObjCollection) another;
            if (collection.equivalence().equals(c2.equivalence())) {
            if (collection instanceof ObjSet && c2 instanceof ObjSet &&
                    collection.size() < another.size()) {
                return false;
            }
            if (c2 instanceof InternalObjCollectionOps) {
                // noinspection unchecked
                return ((InternalObjCollectionOps) c2).allContainingIn(collection);
            }
            }
            // noinspection unchecked
            return c2.forEachWhile(new
                    Predicate() {
                @Override
                public boolean test(Object value) {
                    return collection.contains(value);
                }
            });
        } else {
            for (Object o : another) {
                if (!collection.contains(o))
                    return false;
            }
            return true;
        }
    }

    public static <E> boolean addAll(final ObjCollection<E> collection,
            Collection<? extends E> another) {
        if (collection == another)
            throw new IllegalArgumentException();
        long maxPossibleSize = collection.sizeAsLong() + Containers.sizeAsLong(another);
        collection.ensureCapacity(maxPossibleSize);
        if (another instanceof ObjCollection) {
            if (another instanceof InternalObjCollectionOps) {
                return ((InternalObjCollectionOps) another).reverseAddAllTo(collection);
            } else {
                class AddAll implements Consumer<E> {
                    boolean collectionChanged = false;
                    @Override
                    public void accept(E value) {
                        collectionChanged |= collection.add(value);
                    }
                }
                AddAll addAll = new AddAll();
                ((ObjCollection) another).forEach(addAll);
                return addAll.collectionChanged;
            }
        } else {
            boolean collectionChanged = false;
            for (E v : another) {
                collectionChanged |= collection.add(v);
            }
            return collectionChanged;
        }
    }


    private CommonObjCollectionOps() {}
}

