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
import net.openhft.koloboke.collect.set.hash.HashCharSetFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.set.hash.HashCharSet;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class QHashCharSetFactoryGO extends QHashCharSetFactorySO {

    public QHashCharSetFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , char lower, char upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashCharSetFactory thisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper);

    abstract HashCharSetFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper);

    abstract HashCharSetFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, char lower, char upper);

    @Override
    public final HashCharSetFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashCharSetFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }


    @Override
    public String toString() {
        return "HashCharSetFactory[" + commonString() + keySpecialString() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof HashCharSetFactory) {
            HashCharSetFactory factory = (HashCharSetFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return keySpecialHashCode(commonHashCode());
    }

    

    

    

    private UpdatableQHashCharSetGO shrunk(UpdatableQHashCharSetGO set) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(set))
                set.shrink();
        }
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSet() {
        return newUpdatableSet(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public MutableQHashCharSetGO newMutableSet() {
        return newMutableSet(getDefaultExpectedSize());
    }

    private static int sizeOr(Iterable elems, int defaultSize) {
        return elems instanceof Collection ? ((Collection) elems).size() : defaultSize;
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSet(Iterable<Character> elements) {
        return newUpdatableSet(elements, sizeOr(elements, getDefaultExpectedSize()));
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSet(Iterable<Character> elems1,
            Iterable<Character> elems2) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        return newUpdatableSet(elems1, elems2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSet(Iterable<Character> elems1,
            Iterable<Character> elems2, Iterable<Character> elems3) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        return newUpdatableSet(elems1, elems2, elems3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSet(Iterable<Character> elems1,
            Iterable<Character> elems2, Iterable<Character> elems3,
            Iterable<Character> elems4) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        expectedSize += (long) sizeOr(elems4, 0);
        return newUpdatableSet(elems1, elems2, elems3, elems4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSet(Iterable<Character> elems1,
            Iterable<Character> elems2, Iterable<Character> elems3,
            Iterable<Character> elems4, Iterable<Character> elems5) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        expectedSize += (long) sizeOr(elems4, 0);
        expectedSize += (long) sizeOr(elems5, 0);
        return newUpdatableSet(elems1, elems2, elems3, elems4, elems5, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSet(Iterable<Character> elements,
            int expectedSize) {
        return shrunk(super.newUpdatableSet(elements, expectedSize));
    }


    private static  void addAll(UpdatableQHashCharSetGO set,
            Iterable<? extends Character> elems) {
        if (elems instanceof Collection) {
            // noinspection unchecked
            set.addAll((Collection<? extends Character>) elems);
        } else {
            for (char e : elems) {
                set.add(e);
            }
        }
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSet(Iterable<Character> elems1,
            Iterable<Character> elems2, int expectedSize) {
        UpdatableQHashCharSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSet(Iterable<Character> elems1,
            Iterable<Character> elems2, Iterable<Character> elems3,
            int expectedSize) {
        UpdatableQHashCharSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSet(Iterable<Character> elems1,
            Iterable<Character> elems2, Iterable<Character> elems3,
            Iterable<Character> elems4, int expectedSize) {
        UpdatableQHashCharSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        addAll(set, elems4);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSet(Iterable<Character> elems1,
            Iterable<Character> elems2, Iterable<Character> elems3,
            Iterable<Character> elems4, Iterable<Character> elems5,
            int expectedSize) {
        UpdatableQHashCharSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        addAll(set, elems4);
        addAll(set, elems5);
        return shrunk(set);
    }


    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSet(Iterator<Character> elements) {
        return newUpdatableSet(elements, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSet(Iterator<Character> elements,
            int expectedSize) {
        UpdatableQHashCharSetGO set = newUpdatableSet(expectedSize);
        while (elements.hasNext()) {
            set.add(elements.next());
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSet(
            Consumer<net.openhft.koloboke.function.CharConsumer> elementsSupplier) {
        return newUpdatableSet(elementsSupplier, getDefaultExpectedSize());
    }

    

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSet(
            Consumer<net.openhft.koloboke.function.CharConsumer> elementsSupplier,
            int expectedSize) {
        final UpdatableQHashCharSetGO set = newUpdatableSet(expectedSize);
        elementsSupplier.accept(new net.openhft.koloboke.function.CharConsumer() {
            @Override
            public void accept(char e) {
                set.add(e);
            }
        });
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSet(char[] elements) {
        return newUpdatableSet(elements, elements.length);
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSet(char[] elements,
            int expectedSize) {
        UpdatableQHashCharSetGO set = newUpdatableSet(expectedSize);
        for (char e : elements) {
            set.add(e);
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSet(Character[] elements) {
        return newUpdatableSet(elements, elements.length);
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSet(Character[] elements,
            int expectedSize) {
        UpdatableQHashCharSetGO set = newUpdatableSet(expectedSize);
        for (char e : elements) {
            set.add(e);
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSetOf(char e1) {
        UpdatableQHashCharSetGO set = newUpdatableSet(1);
        set.add(e1);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSetOf(
            char e1, char e2) {
        UpdatableQHashCharSetGO set = newUpdatableSet(2);
        set.add(e1);
        set.add(e2);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSetOf(
            char e1, char e2, char e3) {
        UpdatableQHashCharSetGO set = newUpdatableSet(3);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSetOf(
            char e1, char e2, char e3, char e4) {
        UpdatableQHashCharSetGO set = newUpdatableSet(4);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableQHashCharSetGO newUpdatableSetOf(char e1,
            char e2, char e3, char e4, char e5,
            char... restElements) {
        UpdatableQHashCharSetGO set = newUpdatableSet(5 + restElements.length);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        set.add(e5);
        for (char e : restElements) {
            set.add(e);
        }
        return shrunk(set);
    }

    
    

    @Override
    @Nonnull
    public HashCharSet newMutableSet(Iterable<Character> elements, int expectedSize) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newMutableSet(Iterable<Character> elems1,
            Iterable<Character> elems2, int expectedSize) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newMutableSet(Iterable<Character> elems1,
            Iterable<Character> elems2, Iterable<Character> elems3, int expectedSize) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newMutableSet(Iterable<Character> elems1,
            Iterable<Character> elems2, Iterable<Character> elems3,
            Iterable<Character> elems4, int expectedSize) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newMutableSet(Iterable<Character> elems1,
            Iterable<Character> elems2, Iterable<Character> elems3,
            Iterable<Character> elems4, Iterable<Character> elems5, int expectedSize) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5, expectedSize));
        return set;
    }

    
    

    @Override
    @Nonnull
    public HashCharSet newMutableSet(Iterable<Character> elements) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newMutableSet(Iterable<Character> elems1,
            Iterable<Character> elems2) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newMutableSet(Iterable<Character> elems1,
            Iterable<Character> elems2, Iterable<Character> elems3) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newMutableSet(Iterable<Character> elems1,
            Iterable<Character> elems2, Iterable<Character> elems3,
            Iterable<Character> elems4) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newMutableSet(Iterable<Character> elems1,
            Iterable<Character> elems2, Iterable<Character> elems3,
            Iterable<Character> elems4, Iterable<Character> elems5) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5));
        return set;
    }


    @Override
    @Nonnull
    public HashCharSet newMutableSet(Iterator<Character> elements) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newMutableSet(Iterator<Character> elements,
            int expectedSize) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newMutableSet(
            Consumer<net.openhft.koloboke.function.CharConsumer> elementsSupplier) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elementsSupplier));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newMutableSet(
            Consumer<net.openhft.koloboke.function.CharConsumer> elementsSupplier,
            int expectedSize) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elementsSupplier, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newMutableSet(char[] elements) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newMutableSet(char[] elements, int expectedSize) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newMutableSet(Character[] elements) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newMutableSet(Character[] elements, int expectedSize) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newMutableSetOf(char e1) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newMutableSetOf(char e1, char e2) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newMutableSetOf(char e1, char e2,
            char e3) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newMutableSetOf(char e1, char e2,
            char e3, char e4) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newMutableSetOf(char e1, char e2,
            char e3, char e4, char e5,
            char... restElements) {
        MutableQHashCharSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4, e5, restElements));
        return set;
    }
    
    

    @Override
    @Nonnull
    public HashCharSet newImmutableSet(Iterable<Character> elements, int expectedSize) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newImmutableSet(Iterable<Character> elems1,
            Iterable<Character> elems2, int expectedSize) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newImmutableSet(Iterable<Character> elems1,
            Iterable<Character> elems2, Iterable<Character> elems3, int expectedSize) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newImmutableSet(Iterable<Character> elems1,
            Iterable<Character> elems2, Iterable<Character> elems3,
            Iterable<Character> elems4, int expectedSize) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newImmutableSet(Iterable<Character> elems1,
            Iterable<Character> elems2, Iterable<Character> elems3,
            Iterable<Character> elems4, Iterable<Character> elems5, int expectedSize) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5, expectedSize));
        return set;
    }

    
    

    @Override
    @Nonnull
    public HashCharSet newImmutableSet(Iterable<Character> elements) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newImmutableSet(Iterable<Character> elems1,
            Iterable<Character> elems2) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newImmutableSet(Iterable<Character> elems1,
            Iterable<Character> elems2, Iterable<Character> elems3) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newImmutableSet(Iterable<Character> elems1,
            Iterable<Character> elems2, Iterable<Character> elems3,
            Iterable<Character> elems4) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newImmutableSet(Iterable<Character> elems1,
            Iterable<Character> elems2, Iterable<Character> elems3,
            Iterable<Character> elems4, Iterable<Character> elems5) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5));
        return set;
    }


    @Override
    @Nonnull
    public HashCharSet newImmutableSet(Iterator<Character> elements) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newImmutableSet(Iterator<Character> elements,
            int expectedSize) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newImmutableSet(
            Consumer<net.openhft.koloboke.function.CharConsumer> elementsSupplier) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elementsSupplier));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newImmutableSet(
            Consumer<net.openhft.koloboke.function.CharConsumer> elementsSupplier,
            int expectedSize) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elementsSupplier, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newImmutableSet(char[] elements) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newImmutableSet(char[] elements, int expectedSize) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newImmutableSet(Character[] elements) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newImmutableSet(Character[] elements, int expectedSize) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newImmutableSetOf(char e1) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newImmutableSetOf(char e1, char e2) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newImmutableSetOf(char e1, char e2,
            char e3) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newImmutableSetOf(char e1, char e2,
            char e3, char e4) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4));
        return set;
    }

    @Override
    @Nonnull
    public HashCharSet newImmutableSetOf(char e1, char e2,
            char e3, char e4, char e5,
            char... restElements) {
        ImmutableQHashCharSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4, e5, restElements));
        return set;
    }
}

