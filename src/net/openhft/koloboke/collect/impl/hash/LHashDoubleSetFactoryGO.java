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
import net.openhft.koloboke.collect.set.hash.HashDoubleSetFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.set.hash.HashDoubleSet;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashDoubleSetFactoryGO extends LHashDoubleSetFactorySO {

    public LHashDoubleSetFactoryGO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    abstract HashDoubleSetFactory thisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashDoubleSetFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashDoubleSetFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    @Override
    public final HashDoubleSetFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
    }

    @Override
    public final HashDoubleSetFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                );
    }


    @Override
    public String toString() {
        return "HashDoubleSetFactory[" + commonString() + keySpecialString() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof HashDoubleSetFactory) {
            HashDoubleSetFactory factory = (HashDoubleSetFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return keySpecialHashCode(commonHashCode());
    }

    

    

    

    private UpdatableLHashDoubleSetGO shrunk(UpdatableLHashDoubleSetGO set) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(set))
                set.shrink();
        }
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSet() {
        return newUpdatableSet(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public MutableLHashDoubleSetGO newMutableSet() {
        return newMutableSet(getDefaultExpectedSize());
    }

    private static int sizeOr(Iterable elems, int defaultSize) {
        return elems instanceof Collection ? ((Collection) elems).size() : defaultSize;
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSet(Iterable<Double> elements) {
        return newUpdatableSet(elements, sizeOr(elements, getDefaultExpectedSize()));
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSet(Iterable<Double> elems1,
            Iterable<Double> elems2) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        return newUpdatableSet(elems1, elems2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSet(Iterable<Double> elems1,
            Iterable<Double> elems2, Iterable<Double> elems3) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        return newUpdatableSet(elems1, elems2, elems3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSet(Iterable<Double> elems1,
            Iterable<Double> elems2, Iterable<Double> elems3,
            Iterable<Double> elems4) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        expectedSize += (long) sizeOr(elems4, 0);
        return newUpdatableSet(elems1, elems2, elems3, elems4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSet(Iterable<Double> elems1,
            Iterable<Double> elems2, Iterable<Double> elems3,
            Iterable<Double> elems4, Iterable<Double> elems5) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        expectedSize += (long) sizeOr(elems4, 0);
        expectedSize += (long) sizeOr(elems5, 0);
        return newUpdatableSet(elems1, elems2, elems3, elems4, elems5, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSet(Iterable<Double> elements,
            int expectedSize) {
        return shrunk(super.newUpdatableSet(elements, expectedSize));
    }


    private static  void addAll(UpdatableLHashDoubleSetGO set,
            Iterable<? extends Double> elems) {
        if (elems instanceof Collection) {
            // noinspection unchecked
            set.addAll((Collection<? extends Double>) elems);
        } else {
            for (double e : elems) {
                set.add(e);
            }
        }
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSet(Iterable<Double> elems1,
            Iterable<Double> elems2, int expectedSize) {
        UpdatableLHashDoubleSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSet(Iterable<Double> elems1,
            Iterable<Double> elems2, Iterable<Double> elems3,
            int expectedSize) {
        UpdatableLHashDoubleSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSet(Iterable<Double> elems1,
            Iterable<Double> elems2, Iterable<Double> elems3,
            Iterable<Double> elems4, int expectedSize) {
        UpdatableLHashDoubleSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        addAll(set, elems4);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSet(Iterable<Double> elems1,
            Iterable<Double> elems2, Iterable<Double> elems3,
            Iterable<Double> elems4, Iterable<Double> elems5,
            int expectedSize) {
        UpdatableLHashDoubleSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        addAll(set, elems4);
        addAll(set, elems5);
        return shrunk(set);
    }


    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSet(Iterator<Double> elements) {
        return newUpdatableSet(elements, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSet(Iterator<Double> elements,
            int expectedSize) {
        UpdatableLHashDoubleSetGO set = newUpdatableSet(expectedSize);
        while (elements.hasNext()) {
            set.add(elements.next());
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSet(
            Consumer<net.openhft.koloboke.function.DoubleConsumer> elementsSupplier) {
        return newUpdatableSet(elementsSupplier, getDefaultExpectedSize());
    }

    

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSet(
            Consumer<net.openhft.koloboke.function.DoubleConsumer> elementsSupplier,
            int expectedSize) {
        final UpdatableLHashDoubleSetGO set = newUpdatableSet(expectedSize);
        elementsSupplier.accept(new net.openhft.koloboke.function.DoubleConsumer() {
            @Override
            public void accept(double e) {
                set.add(e);
            }
        });
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSet(double[] elements) {
        return newUpdatableSet(elements, elements.length);
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSet(double[] elements,
            int expectedSize) {
        UpdatableLHashDoubleSetGO set = newUpdatableSet(expectedSize);
        for (double e : elements) {
            set.add(e);
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSet(Double[] elements) {
        return newUpdatableSet(elements, elements.length);
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSet(Double[] elements,
            int expectedSize) {
        UpdatableLHashDoubleSetGO set = newUpdatableSet(expectedSize);
        for (double e : elements) {
            set.add(e);
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSetOf(double e1) {
        UpdatableLHashDoubleSetGO set = newUpdatableSet(1);
        set.add(e1);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSetOf(
            double e1, double e2) {
        UpdatableLHashDoubleSetGO set = newUpdatableSet(2);
        set.add(e1);
        set.add(e2);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSetOf(
            double e1, double e2, double e3) {
        UpdatableLHashDoubleSetGO set = newUpdatableSet(3);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSetOf(
            double e1, double e2, double e3, double e4) {
        UpdatableLHashDoubleSetGO set = newUpdatableSet(4);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashDoubleSetGO newUpdatableSetOf(double e1,
            double e2, double e3, double e4, double e5,
            double... restElements) {
        UpdatableLHashDoubleSetGO set = newUpdatableSet(5 + restElements.length);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        set.add(e5);
        for (double e : restElements) {
            set.add(e);
        }
        return shrunk(set);
    }

    
    

    @Override
    @Nonnull
    public HashDoubleSet newMutableSet(Iterable<Double> elements, int expectedSize) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newMutableSet(Iterable<Double> elems1,
            Iterable<Double> elems2, int expectedSize) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newMutableSet(Iterable<Double> elems1,
            Iterable<Double> elems2, Iterable<Double> elems3, int expectedSize) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newMutableSet(Iterable<Double> elems1,
            Iterable<Double> elems2, Iterable<Double> elems3,
            Iterable<Double> elems4, int expectedSize) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newMutableSet(Iterable<Double> elems1,
            Iterable<Double> elems2, Iterable<Double> elems3,
            Iterable<Double> elems4, Iterable<Double> elems5, int expectedSize) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5, expectedSize));
        return set;
    }

    
    

    @Override
    @Nonnull
    public HashDoubleSet newMutableSet(Iterable<Double> elements) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newMutableSet(Iterable<Double> elems1,
            Iterable<Double> elems2) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newMutableSet(Iterable<Double> elems1,
            Iterable<Double> elems2, Iterable<Double> elems3) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newMutableSet(Iterable<Double> elems1,
            Iterable<Double> elems2, Iterable<Double> elems3,
            Iterable<Double> elems4) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newMutableSet(Iterable<Double> elems1,
            Iterable<Double> elems2, Iterable<Double> elems3,
            Iterable<Double> elems4, Iterable<Double> elems5) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5));
        return set;
    }


    @Override
    @Nonnull
    public HashDoubleSet newMutableSet(Iterator<Double> elements) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newMutableSet(Iterator<Double> elements,
            int expectedSize) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newMutableSet(
            Consumer<net.openhft.koloboke.function.DoubleConsumer> elementsSupplier) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elementsSupplier));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newMutableSet(
            Consumer<net.openhft.koloboke.function.DoubleConsumer> elementsSupplier,
            int expectedSize) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elementsSupplier, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newMutableSet(double[] elements) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newMutableSet(double[] elements, int expectedSize) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newMutableSet(Double[] elements) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newMutableSet(Double[] elements, int expectedSize) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newMutableSetOf(double e1) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newMutableSetOf(double e1, double e2) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newMutableSetOf(double e1, double e2,
            double e3) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newMutableSetOf(double e1, double e2,
            double e3, double e4) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newMutableSetOf(double e1, double e2,
            double e3, double e4, double e5,
            double... restElements) {
        MutableLHashDoubleSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4, e5, restElements));
        return set;
    }
    
    

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSet(Iterable<Double> elements, int expectedSize) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSet(Iterable<Double> elems1,
            Iterable<Double> elems2, int expectedSize) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSet(Iterable<Double> elems1,
            Iterable<Double> elems2, Iterable<Double> elems3, int expectedSize) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSet(Iterable<Double> elems1,
            Iterable<Double> elems2, Iterable<Double> elems3,
            Iterable<Double> elems4, int expectedSize) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSet(Iterable<Double> elems1,
            Iterable<Double> elems2, Iterable<Double> elems3,
            Iterable<Double> elems4, Iterable<Double> elems5, int expectedSize) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5, expectedSize));
        return set;
    }

    
    

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSet(Iterable<Double> elements) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSet(Iterable<Double> elems1,
            Iterable<Double> elems2) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSet(Iterable<Double> elems1,
            Iterable<Double> elems2, Iterable<Double> elems3) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSet(Iterable<Double> elems1,
            Iterable<Double> elems2, Iterable<Double> elems3,
            Iterable<Double> elems4) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSet(Iterable<Double> elems1,
            Iterable<Double> elems2, Iterable<Double> elems3,
            Iterable<Double> elems4, Iterable<Double> elems5) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5));
        return set;
    }


    @Override
    @Nonnull
    public HashDoubleSet newImmutableSet(Iterator<Double> elements) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSet(Iterator<Double> elements,
            int expectedSize) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSet(
            Consumer<net.openhft.koloboke.function.DoubleConsumer> elementsSupplier) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elementsSupplier));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSet(
            Consumer<net.openhft.koloboke.function.DoubleConsumer> elementsSupplier,
            int expectedSize) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elementsSupplier, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSet(double[] elements) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSet(double[] elements, int expectedSize) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSet(Double[] elements) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSet(Double[] elements, int expectedSize) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSetOf(double e1) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSetOf(double e1, double e2) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSetOf(double e1, double e2,
            double e3) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSetOf(double e1, double e2,
            double e3, double e4) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4));
        return set;
    }

    @Override
    @Nonnull
    public HashDoubleSet newImmutableSetOf(double e1, double e2,
            double e3, double e4, double e5,
            double... restElements) {
        ImmutableLHashDoubleSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4, e5, restElements));
        return set;
    }
}

