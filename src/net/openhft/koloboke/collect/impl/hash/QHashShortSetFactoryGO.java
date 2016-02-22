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
import net.openhft.koloboke.collect.set.hash.HashShortSetFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.set.hash.HashShortSet;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashShortSetFactoryGO extends QHashShortSetFactorySO {

    public QHashShortSetFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , short lower, short upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashShortSetFactory thisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper);

    abstract HashShortSetFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper);

    abstract HashShortSetFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, short lower, short upper);

    @Override
    public final HashShortSetFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashShortSetFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }


    @Override
    public String toString() {
        return "HashShortSetFactory[" + commonString() + keySpecialString() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof HashShortSetFactory) {
            HashShortSetFactory factory = (HashShortSetFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return keySpecialHashCode(commonHashCode());
    }

    

    

    

    private UpdatableQHashShortSetGO shrunk(UpdatableQHashShortSetGO set) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(set))
                set.shrink();
        }
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSet() {
        return newUpdatableSet(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public MutableQHashShortSetGO newMutableSet() {
        return newMutableSet(getDefaultExpectedSize());
    }

    private static int sizeOr(Iterable elems, int defaultSize) {
        return elems instanceof Collection ? ((Collection) elems).size() : defaultSize;
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSet(Iterable<Short> elements) {
        return newUpdatableSet(elements, sizeOr(elements, getDefaultExpectedSize()));
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSet(Iterable<Short> elems1,
            Iterable<Short> elems2) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        return newUpdatableSet(elems1, elems2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        return newUpdatableSet(elems1, elems2, elems3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        expectedSize += (long) sizeOr(elems4, 0);
        return newUpdatableSet(elems1, elems2, elems3, elems4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4, Iterable<Short> elems5) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        expectedSize += (long) sizeOr(elems4, 0);
        expectedSize += (long) sizeOr(elems5, 0);
        return newUpdatableSet(elems1, elems2, elems3, elems4, elems5, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSet(Iterable<Short> elements,
            int expectedSize) {
        return shrunk(super.newUpdatableSet(elements, expectedSize));
    }


    private static  void addAll(UpdatableQHashShortSetGO set,
            Iterable<? extends Short> elems) {
        if (elems instanceof Collection) {
            // noinspection unchecked
            set.addAll((Collection<? extends Short>) elems);
        } else {
            for (short e : elems) {
                set.add(e);
            }
        }
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, int expectedSize) {
        UpdatableQHashShortSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            int expectedSize) {
        UpdatableQHashShortSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4, int expectedSize) {
        UpdatableQHashShortSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        addAll(set, elems4);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4, Iterable<Short> elems5,
            int expectedSize) {
        UpdatableQHashShortSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        addAll(set, elems4);
        addAll(set, elems5);
        return shrunk(set);
    }


    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSet(Iterator<Short> elements) {
        return newUpdatableSet(elements, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSet(Iterator<Short> elements,
            int expectedSize) {
        UpdatableQHashShortSetGO set = newUpdatableSet(expectedSize);
        while (elements.hasNext()) {
            set.add(elements.next());
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSet(
            Consumer<net.openhft.koloboke.function.ShortConsumer> elementsSupplier) {
        return newUpdatableSet(elementsSupplier, getDefaultExpectedSize());
    }

    

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSet(
            Consumer<net.openhft.koloboke.function.ShortConsumer> elementsSupplier,
            int expectedSize) {
        final UpdatableQHashShortSetGO set = newUpdatableSet(expectedSize);
        elementsSupplier.accept(new net.openhft.koloboke.function.ShortConsumer() {
            @Override
            public void accept(short e) {
                set.add(e);
            }
        });
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSet(short[] elements) {
        return newUpdatableSet(elements, elements.length);
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSet(short[] elements,
            int expectedSize) {
        UpdatableQHashShortSetGO set = newUpdatableSet(expectedSize);
        for (short e : elements) {
            set.add(e);
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSet(Short[] elements) {
        return newUpdatableSet(elements, elements.length);
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSet(Short[] elements,
            int expectedSize) {
        UpdatableQHashShortSetGO set = newUpdatableSet(expectedSize);
        for (short e : elements) {
            set.add(e);
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSetOf(short e1) {
        UpdatableQHashShortSetGO set = newUpdatableSet(1);
        set.add(e1);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSetOf(
            short e1, short e2) {
        UpdatableQHashShortSetGO set = newUpdatableSet(2);
        set.add(e1);
        set.add(e2);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSetOf(
            short e1, short e2, short e3) {
        UpdatableQHashShortSetGO set = newUpdatableSet(3);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSetOf(
            short e1, short e2, short e3, short e4) {
        UpdatableQHashShortSetGO set = newUpdatableSet(4);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashShortSetGO newUpdatableSetOf(short e1,
            short e2, short e3, short e4, short e5,
            short... restElements) {
        UpdatableQHashShortSetGO set = newUpdatableSet(5 + restElements.length);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        set.add(e5);
        for (short e : restElements) {
            set.add(e);
        }
        return shrunk(set);
    }

    
    

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterable<Short> elements, int expectedSize) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, int expectedSize) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3, int expectedSize) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4, int expectedSize) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4, Iterable<Short> elems5, int expectedSize) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5, expectedSize));
        return set;
    }

    
    

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterable<Short> elements) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4, Iterable<Short> elems5) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5));
        return set;
    }


    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterator<Short> elements) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterator<Short> elements,
            int expectedSize) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(
            Consumer<net.openhft.koloboke.function.ShortConsumer> elementsSupplier) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elementsSupplier));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(
            Consumer<net.openhft.koloboke.function.ShortConsumer> elementsSupplier,
            int expectedSize) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elementsSupplier, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(short[] elements) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(short[] elements, int expectedSize) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Short[] elements) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Short[] elements, int expectedSize) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSetOf(short e1) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSetOf(short e1, short e2) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSetOf(short e1, short e2,
            short e3) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSetOf(short e1, short e2,
            short e3, short e4) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSetOf(short e1, short e2,
            short e3, short e4, short e5,
            short... restElements) {
        MutableQHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4, e5, restElements));
        return set;
    }
    
    

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterable<Short> elements, int expectedSize) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, int expectedSize) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3, int expectedSize) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4, int expectedSize) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4, Iterable<Short> elems5, int expectedSize) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5, expectedSize));
        return set;
    }

    
    

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterable<Short> elements) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4, Iterable<Short> elems5) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5));
        return set;
    }


    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterator<Short> elements) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterator<Short> elements,
            int expectedSize) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(
            Consumer<net.openhft.koloboke.function.ShortConsumer> elementsSupplier) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elementsSupplier));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(
            Consumer<net.openhft.koloboke.function.ShortConsumer> elementsSupplier,
            int expectedSize) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elementsSupplier, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(short[] elements) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(short[] elements, int expectedSize) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Short[] elements) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Short[] elements, int expectedSize) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSetOf(short e1) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSetOf(short e1, short e2) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSetOf(short e1, short e2,
            short e3) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSetOf(short e1, short e2,
            short e3, short e4) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSetOf(short e1, short e2,
            short e3, short e4, short e5,
            short... restElements) {
        ImmutableQHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4, e5, restElements));
        return set;
    }
}

