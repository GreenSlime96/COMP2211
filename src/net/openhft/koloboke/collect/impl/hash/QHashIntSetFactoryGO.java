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
import net.openhft.koloboke.collect.set.hash.HashIntSetFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.set.hash.HashIntSet;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashIntSetFactoryGO extends QHashIntSetFactorySO {

    public QHashIntSetFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , int lower, int upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashIntSetFactory thisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    abstract HashIntSetFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    abstract HashIntSetFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, int lower, int upper);

    @Override
    public final HashIntSetFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashIntSetFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }


    @Override
    public String toString() {
        return "HashIntSetFactory[" + commonString() + keySpecialString() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof HashIntSetFactory) {
            HashIntSetFactory factory = (HashIntSetFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return keySpecialHashCode(commonHashCode());
    }

    

    

    

    private UpdatableQHashIntSetGO shrunk(UpdatableQHashIntSetGO set) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(set))
                set.shrink();
        }
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSet() {
        return newUpdatableSet(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public MutableQHashIntSetGO newMutableSet() {
        return newMutableSet(getDefaultExpectedSize());
    }

    private static int sizeOr(Iterable elems, int defaultSize) {
        return elems instanceof Collection ? ((Collection) elems).size() : defaultSize;
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSet(Iterable<Integer> elements) {
        return newUpdatableSet(elements, sizeOr(elements, getDefaultExpectedSize()));
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        return newUpdatableSet(elems1, elems2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2, Iterable<Integer> elems3) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        return newUpdatableSet(elems1, elems2, elems3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2, Iterable<Integer> elems3,
            Iterable<Integer> elems4) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        expectedSize += (long) sizeOr(elems4, 0);
        return newUpdatableSet(elems1, elems2, elems3, elems4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2, Iterable<Integer> elems3,
            Iterable<Integer> elems4, Iterable<Integer> elems5) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        expectedSize += (long) sizeOr(elems4, 0);
        expectedSize += (long) sizeOr(elems5, 0);
        return newUpdatableSet(elems1, elems2, elems3, elems4, elems5, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSet(Iterable<Integer> elements,
            int expectedSize) {
        return shrunk(super.newUpdatableSet(elements, expectedSize));
    }


    private static  void addAll(UpdatableQHashIntSetGO set,
            Iterable<? extends Integer> elems) {
        if (elems instanceof Collection) {
            // noinspection unchecked
            set.addAll((Collection<? extends Integer>) elems);
        } else {
            for (int e : elems) {
                set.add(e);
            }
        }
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2, int expectedSize) {
        UpdatableQHashIntSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2, Iterable<Integer> elems3,
            int expectedSize) {
        UpdatableQHashIntSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2, Iterable<Integer> elems3,
            Iterable<Integer> elems4, int expectedSize) {
        UpdatableQHashIntSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        addAll(set, elems4);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2, Iterable<Integer> elems3,
            Iterable<Integer> elems4, Iterable<Integer> elems5,
            int expectedSize) {
        UpdatableQHashIntSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        addAll(set, elems4);
        addAll(set, elems5);
        return shrunk(set);
    }


    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSet(Iterator<Integer> elements) {
        return newUpdatableSet(elements, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSet(Iterator<Integer> elements,
            int expectedSize) {
        UpdatableQHashIntSetGO set = newUpdatableSet(expectedSize);
        while (elements.hasNext()) {
            set.add(elements.next());
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSet(
            Consumer<net.openhft.koloboke.function.IntConsumer> elementsSupplier) {
        return newUpdatableSet(elementsSupplier, getDefaultExpectedSize());
    }

    

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSet(
            Consumer<net.openhft.koloboke.function.IntConsumer> elementsSupplier,
            int expectedSize) {
        final UpdatableQHashIntSetGO set = newUpdatableSet(expectedSize);
        elementsSupplier.accept(new net.openhft.koloboke.function.IntConsumer() {
            @Override
            public void accept(int e) {
                set.add(e);
            }
        });
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSet(int[] elements) {
        return newUpdatableSet(elements, elements.length);
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSet(int[] elements,
            int expectedSize) {
        UpdatableQHashIntSetGO set = newUpdatableSet(expectedSize);
        for (int e : elements) {
            set.add(e);
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSet(Integer[] elements) {
        return newUpdatableSet(elements, elements.length);
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSet(Integer[] elements,
            int expectedSize) {
        UpdatableQHashIntSetGO set = newUpdatableSet(expectedSize);
        for (int e : elements) {
            set.add(e);
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSetOf(int e1) {
        UpdatableQHashIntSetGO set = newUpdatableSet(1);
        set.add(e1);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSetOf(
            int e1, int e2) {
        UpdatableQHashIntSetGO set = newUpdatableSet(2);
        set.add(e1);
        set.add(e2);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSetOf(
            int e1, int e2, int e3) {
        UpdatableQHashIntSetGO set = newUpdatableSet(3);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSetOf(
            int e1, int e2, int e3, int e4) {
        UpdatableQHashIntSetGO set = newUpdatableSet(4);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashIntSetGO newUpdatableSetOf(int e1,
            int e2, int e3, int e4, int e5,
            int... restElements) {
        UpdatableQHashIntSetGO set = newUpdatableSet(5 + restElements.length);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        set.add(e5);
        for (int e : restElements) {
            set.add(e);
        }
        return shrunk(set);
    }

    
    

    @Override
    @Nonnull
    public HashIntSet newMutableSet(Iterable<Integer> elements, int expectedSize) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newMutableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2, int expectedSize) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newMutableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2, Iterable<Integer> elems3, int expectedSize) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newMutableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2, Iterable<Integer> elems3,
            Iterable<Integer> elems4, int expectedSize) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newMutableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2, Iterable<Integer> elems3,
            Iterable<Integer> elems4, Iterable<Integer> elems5, int expectedSize) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5, expectedSize));
        return set;
    }

    
    

    @Override
    @Nonnull
    public HashIntSet newMutableSet(Iterable<Integer> elements) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newMutableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newMutableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2, Iterable<Integer> elems3) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newMutableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2, Iterable<Integer> elems3,
            Iterable<Integer> elems4) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newMutableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2, Iterable<Integer> elems3,
            Iterable<Integer> elems4, Iterable<Integer> elems5) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5));
        return set;
    }


    @Override
    @Nonnull
    public HashIntSet newMutableSet(Iterator<Integer> elements) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newMutableSet(Iterator<Integer> elements,
            int expectedSize) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newMutableSet(
            Consumer<net.openhft.koloboke.function.IntConsumer> elementsSupplier) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elementsSupplier));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newMutableSet(
            Consumer<net.openhft.koloboke.function.IntConsumer> elementsSupplier,
            int expectedSize) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elementsSupplier, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newMutableSet(int[] elements) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newMutableSet(int[] elements, int expectedSize) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newMutableSet(Integer[] elements) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newMutableSet(Integer[] elements, int expectedSize) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newMutableSetOf(int e1) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newMutableSetOf(int e1, int e2) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newMutableSetOf(int e1, int e2,
            int e3) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newMutableSetOf(int e1, int e2,
            int e3, int e4) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newMutableSetOf(int e1, int e2,
            int e3, int e4, int e5,
            int... restElements) {
        MutableQHashIntSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4, e5, restElements));
        return set;
    }
    
    

    @Override
    @Nonnull
    public HashIntSet newImmutableSet(Iterable<Integer> elements, int expectedSize) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newImmutableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2, int expectedSize) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newImmutableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2, Iterable<Integer> elems3, int expectedSize) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newImmutableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2, Iterable<Integer> elems3,
            Iterable<Integer> elems4, int expectedSize) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newImmutableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2, Iterable<Integer> elems3,
            Iterable<Integer> elems4, Iterable<Integer> elems5, int expectedSize) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5, expectedSize));
        return set;
    }

    
    

    @Override
    @Nonnull
    public HashIntSet newImmutableSet(Iterable<Integer> elements) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newImmutableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newImmutableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2, Iterable<Integer> elems3) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newImmutableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2, Iterable<Integer> elems3,
            Iterable<Integer> elems4) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newImmutableSet(Iterable<Integer> elems1,
            Iterable<Integer> elems2, Iterable<Integer> elems3,
            Iterable<Integer> elems4, Iterable<Integer> elems5) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5));
        return set;
    }


    @Override
    @Nonnull
    public HashIntSet newImmutableSet(Iterator<Integer> elements) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newImmutableSet(Iterator<Integer> elements,
            int expectedSize) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newImmutableSet(
            Consumer<net.openhft.koloboke.function.IntConsumer> elementsSupplier) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elementsSupplier));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newImmutableSet(
            Consumer<net.openhft.koloboke.function.IntConsumer> elementsSupplier,
            int expectedSize) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elementsSupplier, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newImmutableSet(int[] elements) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newImmutableSet(int[] elements, int expectedSize) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newImmutableSet(Integer[] elements) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newImmutableSet(Integer[] elements, int expectedSize) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newImmutableSetOf(int e1) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newImmutableSetOf(int e1, int e2) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newImmutableSetOf(int e1, int e2,
            int e3) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newImmutableSetOf(int e1, int e2,
            int e3, int e4) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4));
        return set;
    }

    @Override
    @Nonnull
    public HashIntSet newImmutableSetOf(int e1, int e2,
            int e3, int e4, int e5,
            int... restElements) {
        ImmutableQHashIntSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4, e5, restElements));
        return set;
    }
}

