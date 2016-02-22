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


public abstract class LHashShortSetFactoryGO extends LHashShortSetFactorySO {

    public LHashShortSetFactoryGO(HashConfig hashConf, int defaultExpectedSize
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

    

    

    

    private UpdatableLHashShortSetGO shrunk(UpdatableLHashShortSetGO set) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(set))
                set.shrink();
        }
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSet() {
        return newUpdatableSet(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public MutableLHashShortSetGO newMutableSet() {
        return newMutableSet(getDefaultExpectedSize());
    }

    private static int sizeOr(Iterable elems, int defaultSize) {
        return elems instanceof Collection ? ((Collection) elems).size() : defaultSize;
    }

    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSet(Iterable<Short> elements) {
        return newUpdatableSet(elements, sizeOr(elements, getDefaultExpectedSize()));
    }

    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSet(Iterable<Short> elems1,
            Iterable<Short> elems2) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        return newUpdatableSet(elems1, elems2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        return newUpdatableSet(elems1, elems2, elems3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSet(Iterable<Short> elems1,
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
    public UpdatableLHashShortSetGO newUpdatableSet(Iterable<Short> elems1,
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
    public UpdatableLHashShortSetGO newUpdatableSet(Iterable<Short> elements,
            int expectedSize) {
        return shrunk(super.newUpdatableSet(elements, expectedSize));
    }


    private static  void addAll(UpdatableLHashShortSetGO set,
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
    public UpdatableLHashShortSetGO newUpdatableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, int expectedSize) {
        UpdatableLHashShortSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            int expectedSize) {
        UpdatableLHashShortSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4, int expectedSize) {
        UpdatableLHashShortSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        addAll(set, elems4);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4, Iterable<Short> elems5,
            int expectedSize) {
        UpdatableLHashShortSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        addAll(set, elems4);
        addAll(set, elems5);
        return shrunk(set);
    }


    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSet(Iterator<Short> elements) {
        return newUpdatableSet(elements, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSet(Iterator<Short> elements,
            int expectedSize) {
        UpdatableLHashShortSetGO set = newUpdatableSet(expectedSize);
        while (elements.hasNext()) {
            set.add(elements.next());
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSet(
            Consumer<net.openhft.koloboke.function.ShortConsumer> elementsSupplier) {
        return newUpdatableSet(elementsSupplier, getDefaultExpectedSize());
    }

    

    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSet(
            Consumer<net.openhft.koloboke.function.ShortConsumer> elementsSupplier,
            int expectedSize) {
        final UpdatableLHashShortSetGO set = newUpdatableSet(expectedSize);
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
    public UpdatableLHashShortSetGO newUpdatableSet(short[] elements) {
        return newUpdatableSet(elements, elements.length);
    }

    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSet(short[] elements,
            int expectedSize) {
        UpdatableLHashShortSetGO set = newUpdatableSet(expectedSize);
        for (short e : elements) {
            set.add(e);
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSet(Short[] elements) {
        return newUpdatableSet(elements, elements.length);
    }

    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSet(Short[] elements,
            int expectedSize) {
        UpdatableLHashShortSetGO set = newUpdatableSet(expectedSize);
        for (short e : elements) {
            set.add(e);
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSetOf(short e1) {
        UpdatableLHashShortSetGO set = newUpdatableSet(1);
        set.add(e1);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSetOf(
            short e1, short e2) {
        UpdatableLHashShortSetGO set = newUpdatableSet(2);
        set.add(e1);
        set.add(e2);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSetOf(
            short e1, short e2, short e3) {
        UpdatableLHashShortSetGO set = newUpdatableSet(3);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSetOf(
            short e1, short e2, short e3, short e4) {
        UpdatableLHashShortSetGO set = newUpdatableSet(4);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashShortSetGO newUpdatableSetOf(short e1,
            short e2, short e3, short e4, short e5,
            short... restElements) {
        UpdatableLHashShortSetGO set = newUpdatableSet(5 + restElements.length);
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
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, int expectedSize) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3, int expectedSize) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4, int expectedSize) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4, Iterable<Short> elems5, int expectedSize) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5, expectedSize));
        return set;
    }

    
    

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterable<Short> elements) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4, Iterable<Short> elems5) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5));
        return set;
    }


    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterator<Short> elements) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Iterator<Short> elements,
            int expectedSize) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(
            Consumer<net.openhft.koloboke.function.ShortConsumer> elementsSupplier) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elementsSupplier));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(
            Consumer<net.openhft.koloboke.function.ShortConsumer> elementsSupplier,
            int expectedSize) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elementsSupplier, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(short[] elements) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(short[] elements, int expectedSize) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Short[] elements) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSet(Short[] elements, int expectedSize) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSetOf(short e1) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSetOf(short e1, short e2) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSetOf(short e1, short e2,
            short e3) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSetOf(short e1, short e2,
            short e3, short e4) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newMutableSetOf(short e1, short e2,
            short e3, short e4, short e5,
            short... restElements) {
        MutableLHashShortSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4, e5, restElements));
        return set;
    }
    
    

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterable<Short> elements, int expectedSize) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, int expectedSize) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3, int expectedSize) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4, int expectedSize) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4, Iterable<Short> elems5, int expectedSize) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5, expectedSize));
        return set;
    }

    
    

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterable<Short> elements) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterable<Short> elems1,
            Iterable<Short> elems2, Iterable<Short> elems3,
            Iterable<Short> elems4, Iterable<Short> elems5) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5));
        return set;
    }


    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterator<Short> elements) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Iterator<Short> elements,
            int expectedSize) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(
            Consumer<net.openhft.koloboke.function.ShortConsumer> elementsSupplier) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elementsSupplier));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(
            Consumer<net.openhft.koloboke.function.ShortConsumer> elementsSupplier,
            int expectedSize) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elementsSupplier, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(short[] elements) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(short[] elements, int expectedSize) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Short[] elements) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSet(Short[] elements, int expectedSize) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSetOf(short e1) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSetOf(short e1, short e2) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSetOf(short e1, short e2,
            short e3) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSetOf(short e1, short e2,
            short e3, short e4) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4));
        return set;
    }

    @Override
    @Nonnull
    public HashShortSet newImmutableSetOf(short e1, short e2,
            short e3, short e4, short e5,
            short... restElements) {
        ImmutableLHashShortSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4, e5, restElements));
        return set;
    }
}

