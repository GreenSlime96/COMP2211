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
import net.openhft.koloboke.collect.set.hash.HashObjSetFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.set.hash.HashObjSet;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashObjSetFactoryGO<E> extends QHashObjSetFactorySO<E> {

    public QHashObjSetFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , boolean isNullAllowed
            ) {
        super(hashConf, defaultExpectedSize, isNullAllowed
            );
    }

    

    abstract HashObjSetFactory<E> thisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullAllowed
            );

    abstract HashObjSetFactory<E> lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullAllowed
            );

    abstract HashObjSetFactory<E> qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, boolean isNullAllowed
            );

    @Override
    public final HashObjSetFactory<E> withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            , isNullKeyAllowed());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            , isNullKeyAllowed());
    }

    @Override
    public final HashObjSetFactory<E> withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                , isNullKeyAllowed());
    }


    @Override
    public String toString() {
        return "HashObjSetFactory[" + commonString() + keySpecialString() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof HashObjSetFactory) {
            HashObjSetFactory factory = (HashObjSetFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return keySpecialHashCode(commonHashCode());
    }

    

    

    

    private <E2 extends E> UpdatableQHashObjSetGO<E2> shrunk(UpdatableQHashObjSetGO<E2> set) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(set))
                set.shrink();
        }
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSet() {
        return newUpdatableSet(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public <E2 extends E> MutableQHashObjSetGO<E2> newMutableSet() {
        return newMutableSet(getDefaultExpectedSize());
    }

    private static int sizeOr(Iterable elems, int defaultSize) {
        return elems instanceof Collection ? ((Collection) elems).size() : defaultSize;
    }

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSet(Iterable<? extends E2> elements) {
        return newUpdatableSet(elements, sizeOr(elements, getDefaultExpectedSize()));
    }

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        return newUpdatableSet(elems1, elems2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2, Iterable<? extends E2> elems3) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        return newUpdatableSet(elems1, elems2, elems3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2, Iterable<? extends E2> elems3,
            Iterable<? extends E2> elems4) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        expectedSize += (long) sizeOr(elems4, 0);
        return newUpdatableSet(elems1, elems2, elems3, elems4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2, Iterable<? extends E2> elems3,
            Iterable<? extends E2> elems4, Iterable<? extends E2> elems5) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        expectedSize += (long) sizeOr(elems4, 0);
        expectedSize += (long) sizeOr(elems5, 0);
        return newUpdatableSet(elems1, elems2, elems3, elems4, elems5, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSet(Iterable<? extends E2> elements,
            int expectedSize) {
        return shrunk(super.newUpdatableSet(elements, expectedSize));
    }


    private static <E> void addAll(UpdatableQHashObjSetGO<E> set,
            Iterable<? extends E> elems) {
        if (elems instanceof Collection) {
            // noinspection unchecked
            set.addAll((Collection<? extends E>) elems);
        } else {
            for (E e : elems) {
                set.add(e);
            }
        }
    }

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2, int expectedSize) {
        UpdatableQHashObjSetGO<E2> set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2, Iterable<? extends E2> elems3,
            int expectedSize) {
        UpdatableQHashObjSetGO<E2> set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2, Iterable<? extends E2> elems3,
            Iterable<? extends E2> elems4, int expectedSize) {
        UpdatableQHashObjSetGO<E2> set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        addAll(set, elems4);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2, Iterable<? extends E2> elems3,
            Iterable<? extends E2> elems4, Iterable<? extends E2> elems5,
            int expectedSize) {
        UpdatableQHashObjSetGO<E2> set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        addAll(set, elems4);
        addAll(set, elems5);
        return shrunk(set);
    }


    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSet(Iterator<? extends E2> elements) {
        return newUpdatableSet(elements, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSet(Iterator<? extends E2> elements,
            int expectedSize) {
        UpdatableQHashObjSetGO<E2> set = newUpdatableSet(expectedSize);
        while (elements.hasNext()) {
            set.add(elements.next());
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSet(
            Consumer<net.openhft.koloboke.function.Consumer<E2>> elementsSupplier) {
        return newUpdatableSet(elementsSupplier, getDefaultExpectedSize());
    }

    

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSet(
            Consumer<net.openhft.koloboke.function.Consumer<E2>> elementsSupplier,
            int expectedSize) {
        final UpdatableQHashObjSetGO<E2> set = newUpdatableSet(expectedSize);
        elementsSupplier.accept(new net.openhft.koloboke.function.Consumer<E2>() {
            @Override
            public void accept(E2 e) {
                set.add(e);
            }
        });
        return shrunk(set);
    }

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSet(E2[] elements) {
        return newUpdatableSet(elements, elements.length);
    }

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSet(E2[] elements,
            int expectedSize) {
        UpdatableQHashObjSetGO<E2> set = newUpdatableSet(expectedSize);
        for (E2 e : elements) {
            set.add(e);
        }
        return shrunk(set);
    }


    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSetOf(E2 e1) {
        UpdatableQHashObjSetGO<E2> set = newUpdatableSet(1);
        set.add(e1);
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSetOf(
            E2 e1, E2 e2) {
        UpdatableQHashObjSetGO<E2> set = newUpdatableSet(2);
        set.add(e1);
        set.add(e2);
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSetOf(
            E2 e1, E2 e2, E2 e3) {
        UpdatableQHashObjSetGO<E2> set = newUpdatableSet(3);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSetOf(
            E2 e1, E2 e2, E2 e3, E2 e4) {
        UpdatableQHashObjSetGO<E2> set = newUpdatableSet(4);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> UpdatableQHashObjSetGO<E2> newUpdatableSetOf(E2 e1,
            E2 e2, E2 e3, E2 e4, E2 e5,
            E2... restElements) {
        UpdatableQHashObjSetGO<E2> set = newUpdatableSet(5 + restElements.length);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        set.add(e5);
        for (E2 e : restElements) {
            set.add(e);
        }
        return shrunk(set);
    }

    
    

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newMutableSet(Iterable<? extends E2> elements, int expectedSize) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newMutableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2, int expectedSize) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newMutableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2, Iterable<? extends E2> elems3, int expectedSize) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newMutableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2, Iterable<? extends E2> elems3,
            Iterable<? extends E2> elems4, int expectedSize) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newMutableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2, Iterable<? extends E2> elems3,
            Iterable<? extends E2> elems4, Iterable<? extends E2> elems5, int expectedSize) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5, expectedSize));
        return set;
    }

    
    

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newMutableSet(Iterable<? extends E2> elements) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newMutableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newMutableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2, Iterable<? extends E2> elems3) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newMutableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2, Iterable<? extends E2> elems3,
            Iterable<? extends E2> elems4) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newMutableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2, Iterable<? extends E2> elems3,
            Iterable<? extends E2> elems4, Iterable<? extends E2> elems5) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5));
        return set;
    }


    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newMutableSet(Iterator<? extends E2> elements) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newMutableSet(Iterator<? extends E2> elements,
            int expectedSize) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newMutableSet(
            Consumer<net.openhft.koloboke.function.Consumer<E2>> elementsSupplier) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.move(newUpdatableSet(elementsSupplier));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newMutableSet(
            Consumer<net.openhft.koloboke.function.Consumer<E2>> elementsSupplier,
            int expectedSize) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.move(newUpdatableSet(elementsSupplier, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newMutableSet(E2[] elements) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newMutableSet(E2[] elements, int expectedSize) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }


    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newMutableSetOf(E2 e1) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newMutableSetOf(E2 e1, E2 e2) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newMutableSetOf(E2 e1, E2 e2,
            E2 e3) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newMutableSetOf(E2 e1, E2 e2,
            E2 e3, E2 e4) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newMutableSetOf(E2 e1, E2 e2,
            E2 e3, E2 e4, E2 e5,
            E2... restElements) {
        MutableQHashObjSetGO<E2> set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4, e5, restElements));
        return set;
    }
    
    

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newImmutableSet(Iterable<? extends E2> elements, int expectedSize) {
        ImmutableQHashObjSetGO<E2> set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newImmutableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2, int expectedSize) {
        ImmutableQHashObjSetGO<E2> set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newImmutableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2, Iterable<? extends E2> elems3, int expectedSize) {
        ImmutableQHashObjSetGO<E2> set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newImmutableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2, Iterable<? extends E2> elems3,
            Iterable<? extends E2> elems4, int expectedSize) {
        ImmutableQHashObjSetGO<E2> set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newImmutableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2, Iterable<? extends E2> elems3,
            Iterable<? extends E2> elems4, Iterable<? extends E2> elems5, int expectedSize) {
        ImmutableQHashObjSetGO<E2> set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5, expectedSize));
        return set;
    }

    
    

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newImmutableSet(Iterable<? extends E2> elements) {
        ImmutableQHashObjSetGO<E2> set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newImmutableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2) {
        ImmutableQHashObjSetGO<E2> set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newImmutableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2, Iterable<? extends E2> elems3) {
        ImmutableQHashObjSetGO<E2> set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newImmutableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2, Iterable<? extends E2> elems3,
            Iterable<? extends E2> elems4) {
        ImmutableQHashObjSetGO<E2> set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newImmutableSet(Iterable<? extends E2> elems1,
            Iterable<? extends E2> elems2, Iterable<? extends E2> elems3,
            Iterable<? extends E2> elems4, Iterable<? extends E2> elems5) {
        ImmutableQHashObjSetGO<E2> set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5));
        return set;
    }


    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newImmutableSet(Iterator<? extends E2> elements) {
        ImmutableQHashObjSetGO<E2> set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newImmutableSet(Iterator<? extends E2> elements,
            int expectedSize) {
        ImmutableQHashObjSetGO<E2> set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newImmutableSet(
            Consumer<net.openhft.koloboke.function.Consumer<E2>> elementsSupplier) {
        ImmutableQHashObjSetGO<E2> set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elementsSupplier));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newImmutableSet(
            Consumer<net.openhft.koloboke.function.Consumer<E2>> elementsSupplier,
            int expectedSize) {
        ImmutableQHashObjSetGO<E2> set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elementsSupplier, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newImmutableSet(E2[] elements) {
        ImmutableQHashObjSetGO<E2> set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newImmutableSet(E2[] elements, int expectedSize) {
        ImmutableQHashObjSetGO<E2> set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }


    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newImmutableSetOf(E2 e1) {
        ImmutableQHashObjSetGO<E2> set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newImmutableSetOf(E2 e1, E2 e2) {
        ImmutableQHashObjSetGO<E2> set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newImmutableSetOf(E2 e1, E2 e2,
            E2 e3) {
        ImmutableQHashObjSetGO<E2> set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newImmutableSetOf(E2 e1, E2 e2,
            E2 e3, E2 e4) {
        ImmutableQHashObjSetGO<E2> set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4));
        return set;
    }

    @Override
    @Nonnull
    public <E2 extends E> HashObjSet<E2> newImmutableSetOf(E2 e1, E2 e2,
            E2 e3, E2 e4, E2 e5,
            E2... restElements) {
        ImmutableQHashObjSetGO<E2> set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4, e5, restElements));
        return set;
    }
}

