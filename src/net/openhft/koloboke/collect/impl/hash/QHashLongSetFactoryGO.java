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
import net.openhft.koloboke.collect.set.hash.HashLongSetFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.set.hash.HashLongSet;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashLongSetFactoryGO extends QHashLongSetFactorySO {

    public QHashLongSetFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , long lower, long upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashLongSetFactory thisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper);

    abstract HashLongSetFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper);

    abstract HashLongSetFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, long lower, long upper);

    @Override
    public final HashLongSetFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashLongSetFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }


    @Override
    public String toString() {
        return "HashLongSetFactory[" + commonString() + keySpecialString() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof HashLongSetFactory) {
            HashLongSetFactory factory = (HashLongSetFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return keySpecialHashCode(commonHashCode());
    }

    

    

    

    private UpdatableQHashLongSetGO shrunk(UpdatableQHashLongSetGO set) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(set))
                set.shrink();
        }
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSet() {
        return newUpdatableSet(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public MutableQHashLongSetGO newMutableSet() {
        return newMutableSet(getDefaultExpectedSize());
    }

    private static int sizeOr(Iterable elems, int defaultSize) {
        return elems instanceof Collection ? ((Collection) elems).size() : defaultSize;
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSet(Iterable<Long> elements) {
        return newUpdatableSet(elements, sizeOr(elements, getDefaultExpectedSize()));
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSet(Iterable<Long> elems1,
            Iterable<Long> elems2) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        return newUpdatableSet(elems1, elems2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSet(Iterable<Long> elems1,
            Iterable<Long> elems2, Iterable<Long> elems3) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        return newUpdatableSet(elems1, elems2, elems3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSet(Iterable<Long> elems1,
            Iterable<Long> elems2, Iterable<Long> elems3,
            Iterable<Long> elems4) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        expectedSize += (long) sizeOr(elems4, 0);
        return newUpdatableSet(elems1, elems2, elems3, elems4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSet(Iterable<Long> elems1,
            Iterable<Long> elems2, Iterable<Long> elems3,
            Iterable<Long> elems4, Iterable<Long> elems5) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        expectedSize += (long) sizeOr(elems4, 0);
        expectedSize += (long) sizeOr(elems5, 0);
        return newUpdatableSet(elems1, elems2, elems3, elems4, elems5, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSet(Iterable<Long> elements,
            int expectedSize) {
        return shrunk(super.newUpdatableSet(elements, expectedSize));
    }


    private static  void addAll(UpdatableQHashLongSetGO set,
            Iterable<? extends Long> elems) {
        if (elems instanceof Collection) {
            // noinspection unchecked
            set.addAll((Collection<? extends Long>) elems);
        } else {
            for (long e : elems) {
                set.add(e);
            }
        }
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSet(Iterable<Long> elems1,
            Iterable<Long> elems2, int expectedSize) {
        UpdatableQHashLongSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSet(Iterable<Long> elems1,
            Iterable<Long> elems2, Iterable<Long> elems3,
            int expectedSize) {
        UpdatableQHashLongSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSet(Iterable<Long> elems1,
            Iterable<Long> elems2, Iterable<Long> elems3,
            Iterable<Long> elems4, int expectedSize) {
        UpdatableQHashLongSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        addAll(set, elems4);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSet(Iterable<Long> elems1,
            Iterable<Long> elems2, Iterable<Long> elems3,
            Iterable<Long> elems4, Iterable<Long> elems5,
            int expectedSize) {
        UpdatableQHashLongSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        addAll(set, elems4);
        addAll(set, elems5);
        return shrunk(set);
    }


    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSet(Iterator<Long> elements) {
        return newUpdatableSet(elements, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSet(Iterator<Long> elements,
            int expectedSize) {
        UpdatableQHashLongSetGO set = newUpdatableSet(expectedSize);
        while (elements.hasNext()) {
            set.add(elements.next());
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSet(
            Consumer<net.openhft.koloboke.function.LongConsumer> elementsSupplier) {
        return newUpdatableSet(elementsSupplier, getDefaultExpectedSize());
    }

    

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSet(
            Consumer<net.openhft.koloboke.function.LongConsumer> elementsSupplier,
            int expectedSize) {
        final UpdatableQHashLongSetGO set = newUpdatableSet(expectedSize);
        elementsSupplier.accept(new net.openhft.koloboke.function.LongConsumer() {
            @Override
            public void accept(long e) {
                set.add(e);
            }
        });
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSet(long[] elements) {
        return newUpdatableSet(elements, elements.length);
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSet(long[] elements,
            int expectedSize) {
        UpdatableQHashLongSetGO set = newUpdatableSet(expectedSize);
        for (long e : elements) {
            set.add(e);
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSet(Long[] elements) {
        return newUpdatableSet(elements, elements.length);
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSet(Long[] elements,
            int expectedSize) {
        UpdatableQHashLongSetGO set = newUpdatableSet(expectedSize);
        for (long e : elements) {
            set.add(e);
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSetOf(long e1) {
        UpdatableQHashLongSetGO set = newUpdatableSet(1);
        set.add(e1);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSetOf(
            long e1, long e2) {
        UpdatableQHashLongSetGO set = newUpdatableSet(2);
        set.add(e1);
        set.add(e2);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSetOf(
            long e1, long e2, long e3) {
        UpdatableQHashLongSetGO set = newUpdatableSet(3);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSetOf(
            long e1, long e2, long e3, long e4) {
        UpdatableQHashLongSetGO set = newUpdatableSet(4);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashLongSetGO newUpdatableSetOf(long e1,
            long e2, long e3, long e4, long e5,
            long... restElements) {
        UpdatableQHashLongSetGO set = newUpdatableSet(5 + restElements.length);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        set.add(e5);
        for (long e : restElements) {
            set.add(e);
        }
        return shrunk(set);
    }

    
    

    @Override
    @Nonnull
    public HashLongSet newMutableSet(Iterable<Long> elements, int expectedSize) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newMutableSet(Iterable<Long> elems1,
            Iterable<Long> elems2, int expectedSize) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newMutableSet(Iterable<Long> elems1,
            Iterable<Long> elems2, Iterable<Long> elems3, int expectedSize) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newMutableSet(Iterable<Long> elems1,
            Iterable<Long> elems2, Iterable<Long> elems3,
            Iterable<Long> elems4, int expectedSize) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newMutableSet(Iterable<Long> elems1,
            Iterable<Long> elems2, Iterable<Long> elems3,
            Iterable<Long> elems4, Iterable<Long> elems5, int expectedSize) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5, expectedSize));
        return set;
    }

    
    

    @Override
    @Nonnull
    public HashLongSet newMutableSet(Iterable<Long> elements) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newMutableSet(Iterable<Long> elems1,
            Iterable<Long> elems2) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newMutableSet(Iterable<Long> elems1,
            Iterable<Long> elems2, Iterable<Long> elems3) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newMutableSet(Iterable<Long> elems1,
            Iterable<Long> elems2, Iterable<Long> elems3,
            Iterable<Long> elems4) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newMutableSet(Iterable<Long> elems1,
            Iterable<Long> elems2, Iterable<Long> elems3,
            Iterable<Long> elems4, Iterable<Long> elems5) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5));
        return set;
    }


    @Override
    @Nonnull
    public HashLongSet newMutableSet(Iterator<Long> elements) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newMutableSet(Iterator<Long> elements,
            int expectedSize) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newMutableSet(
            Consumer<net.openhft.koloboke.function.LongConsumer> elementsSupplier) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elementsSupplier));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newMutableSet(
            Consumer<net.openhft.koloboke.function.LongConsumer> elementsSupplier,
            int expectedSize) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elementsSupplier, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newMutableSet(long[] elements) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newMutableSet(long[] elements, int expectedSize) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newMutableSet(Long[] elements) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newMutableSet(Long[] elements, int expectedSize) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newMutableSetOf(long e1) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newMutableSetOf(long e1, long e2) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newMutableSetOf(long e1, long e2,
            long e3) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newMutableSetOf(long e1, long e2,
            long e3, long e4) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newMutableSetOf(long e1, long e2,
            long e3, long e4, long e5,
            long... restElements) {
        MutableQHashLongSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4, e5, restElements));
        return set;
    }
    
    

    @Override
    @Nonnull
    public HashLongSet newImmutableSet(Iterable<Long> elements, int expectedSize) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newImmutableSet(Iterable<Long> elems1,
            Iterable<Long> elems2, int expectedSize) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newImmutableSet(Iterable<Long> elems1,
            Iterable<Long> elems2, Iterable<Long> elems3, int expectedSize) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newImmutableSet(Iterable<Long> elems1,
            Iterable<Long> elems2, Iterable<Long> elems3,
            Iterable<Long> elems4, int expectedSize) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newImmutableSet(Iterable<Long> elems1,
            Iterable<Long> elems2, Iterable<Long> elems3,
            Iterable<Long> elems4, Iterable<Long> elems5, int expectedSize) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5, expectedSize));
        return set;
    }

    
    

    @Override
    @Nonnull
    public HashLongSet newImmutableSet(Iterable<Long> elements) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newImmutableSet(Iterable<Long> elems1,
            Iterable<Long> elems2) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newImmutableSet(Iterable<Long> elems1,
            Iterable<Long> elems2, Iterable<Long> elems3) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newImmutableSet(Iterable<Long> elems1,
            Iterable<Long> elems2, Iterable<Long> elems3,
            Iterable<Long> elems4) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newImmutableSet(Iterable<Long> elems1,
            Iterable<Long> elems2, Iterable<Long> elems3,
            Iterable<Long> elems4, Iterable<Long> elems5) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5));
        return set;
    }


    @Override
    @Nonnull
    public HashLongSet newImmutableSet(Iterator<Long> elements) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newImmutableSet(Iterator<Long> elements,
            int expectedSize) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newImmutableSet(
            Consumer<net.openhft.koloboke.function.LongConsumer> elementsSupplier) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elementsSupplier));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newImmutableSet(
            Consumer<net.openhft.koloboke.function.LongConsumer> elementsSupplier,
            int expectedSize) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elementsSupplier, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newImmutableSet(long[] elements) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newImmutableSet(long[] elements, int expectedSize) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newImmutableSet(Long[] elements) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newImmutableSet(Long[] elements, int expectedSize) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newImmutableSetOf(long e1) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newImmutableSetOf(long e1, long e2) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newImmutableSetOf(long e1, long e2,
            long e3) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newImmutableSetOf(long e1, long e2,
            long e3, long e4) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4));
        return set;
    }

    @Override
    @Nonnull
    public HashLongSet newImmutableSetOf(long e1, long e2,
            long e3, long e4, long e5,
            long... restElements) {
        ImmutableQHashLongSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4, e5, restElements));
        return set;
    }
}

