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
import net.openhft.koloboke.collect.set.hash.HashFloatSetFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.set.hash.HashFloatSet;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashFloatSetFactoryGO extends LHashFloatSetFactorySO {

    public LHashFloatSetFactoryGO(HashConfig hashConf, int defaultExpectedSize
            ) {
        super(hashConf, defaultExpectedSize);
    }

    

    abstract HashFloatSetFactory thisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashFloatSetFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    abstract HashFloatSetFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize);

    @Override
    public final HashFloatSetFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            );
    }

    @Override
    public final HashFloatSetFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                );
    }


    @Override
    public String toString() {
        return "HashFloatSetFactory[" + commonString() + keySpecialString() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof HashFloatSetFactory) {
            HashFloatSetFactory factory = (HashFloatSetFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return keySpecialHashCode(commonHashCode());
    }

    

    

    

    private UpdatableLHashFloatSetGO shrunk(UpdatableLHashFloatSetGO set) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(set))
                set.shrink();
        }
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSet() {
        return newUpdatableSet(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public MutableLHashFloatSetGO newMutableSet() {
        return newMutableSet(getDefaultExpectedSize());
    }

    private static int sizeOr(Iterable elems, int defaultSize) {
        return elems instanceof Collection ? ((Collection) elems).size() : defaultSize;
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSet(Iterable<Float> elements) {
        return newUpdatableSet(elements, sizeOr(elements, getDefaultExpectedSize()));
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSet(Iterable<Float> elems1,
            Iterable<Float> elems2) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        return newUpdatableSet(elems1, elems2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSet(Iterable<Float> elems1,
            Iterable<Float> elems2, Iterable<Float> elems3) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        return newUpdatableSet(elems1, elems2, elems3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSet(Iterable<Float> elems1,
            Iterable<Float> elems2, Iterable<Float> elems3,
            Iterable<Float> elems4) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        expectedSize += (long) sizeOr(elems4, 0);
        return newUpdatableSet(elems1, elems2, elems3, elems4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSet(Iterable<Float> elems1,
            Iterable<Float> elems2, Iterable<Float> elems3,
            Iterable<Float> elems4, Iterable<Float> elems5) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        expectedSize += (long) sizeOr(elems4, 0);
        expectedSize += (long) sizeOr(elems5, 0);
        return newUpdatableSet(elems1, elems2, elems3, elems4, elems5, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSet(Iterable<Float> elements,
            int expectedSize) {
        return shrunk(super.newUpdatableSet(elements, expectedSize));
    }


    private static  void addAll(UpdatableLHashFloatSetGO set,
            Iterable<? extends Float> elems) {
        if (elems instanceof Collection) {
            // noinspection unchecked
            set.addAll((Collection<? extends Float>) elems);
        } else {
            for (float e : elems) {
                set.add(e);
            }
        }
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSet(Iterable<Float> elems1,
            Iterable<Float> elems2, int expectedSize) {
        UpdatableLHashFloatSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSet(Iterable<Float> elems1,
            Iterable<Float> elems2, Iterable<Float> elems3,
            int expectedSize) {
        UpdatableLHashFloatSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSet(Iterable<Float> elems1,
            Iterable<Float> elems2, Iterable<Float> elems3,
            Iterable<Float> elems4, int expectedSize) {
        UpdatableLHashFloatSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        addAll(set, elems4);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSet(Iterable<Float> elems1,
            Iterable<Float> elems2, Iterable<Float> elems3,
            Iterable<Float> elems4, Iterable<Float> elems5,
            int expectedSize) {
        UpdatableLHashFloatSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        addAll(set, elems4);
        addAll(set, elems5);
        return shrunk(set);
    }


    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSet(Iterator<Float> elements) {
        return newUpdatableSet(elements, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSet(Iterator<Float> elements,
            int expectedSize) {
        UpdatableLHashFloatSetGO set = newUpdatableSet(expectedSize);
        while (elements.hasNext()) {
            set.add(elements.next());
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSet(
            Consumer<net.openhft.koloboke.function.FloatConsumer> elementsSupplier) {
        return newUpdatableSet(elementsSupplier, getDefaultExpectedSize());
    }

    

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSet(
            Consumer<net.openhft.koloboke.function.FloatConsumer> elementsSupplier,
            int expectedSize) {
        final UpdatableLHashFloatSetGO set = newUpdatableSet(expectedSize);
        elementsSupplier.accept(new net.openhft.koloboke.function.FloatConsumer() {
            @Override
            public void accept(float e) {
                set.add(e);
            }
        });
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSet(float[] elements) {
        return newUpdatableSet(elements, elements.length);
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSet(float[] elements,
            int expectedSize) {
        UpdatableLHashFloatSetGO set = newUpdatableSet(expectedSize);
        for (float e : elements) {
            set.add(e);
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSet(Float[] elements) {
        return newUpdatableSet(elements, elements.length);
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSet(Float[] elements,
            int expectedSize) {
        UpdatableLHashFloatSetGO set = newUpdatableSet(expectedSize);
        for (float e : elements) {
            set.add(e);
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSetOf(float e1) {
        UpdatableLHashFloatSetGO set = newUpdatableSet(1);
        set.add(e1);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSetOf(
            float e1, float e2) {
        UpdatableLHashFloatSetGO set = newUpdatableSet(2);
        set.add(e1);
        set.add(e2);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSetOf(
            float e1, float e2, float e3) {
        UpdatableLHashFloatSetGO set = newUpdatableSet(3);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSetOf(
            float e1, float e2, float e3, float e4) {
        UpdatableLHashFloatSetGO set = newUpdatableSet(4);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashFloatSetGO newUpdatableSetOf(float e1,
            float e2, float e3, float e4, float e5,
            float... restElements) {
        UpdatableLHashFloatSetGO set = newUpdatableSet(5 + restElements.length);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        set.add(e5);
        for (float e : restElements) {
            set.add(e);
        }
        return shrunk(set);
    }

    
    

    @Override
    @Nonnull
    public HashFloatSet newMutableSet(Iterable<Float> elements, int expectedSize) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newMutableSet(Iterable<Float> elems1,
            Iterable<Float> elems2, int expectedSize) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newMutableSet(Iterable<Float> elems1,
            Iterable<Float> elems2, Iterable<Float> elems3, int expectedSize) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newMutableSet(Iterable<Float> elems1,
            Iterable<Float> elems2, Iterable<Float> elems3,
            Iterable<Float> elems4, int expectedSize) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newMutableSet(Iterable<Float> elems1,
            Iterable<Float> elems2, Iterable<Float> elems3,
            Iterable<Float> elems4, Iterable<Float> elems5, int expectedSize) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5, expectedSize));
        return set;
    }

    
    

    @Override
    @Nonnull
    public HashFloatSet newMutableSet(Iterable<Float> elements) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newMutableSet(Iterable<Float> elems1,
            Iterable<Float> elems2) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newMutableSet(Iterable<Float> elems1,
            Iterable<Float> elems2, Iterable<Float> elems3) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newMutableSet(Iterable<Float> elems1,
            Iterable<Float> elems2, Iterable<Float> elems3,
            Iterable<Float> elems4) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newMutableSet(Iterable<Float> elems1,
            Iterable<Float> elems2, Iterable<Float> elems3,
            Iterable<Float> elems4, Iterable<Float> elems5) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5));
        return set;
    }


    @Override
    @Nonnull
    public HashFloatSet newMutableSet(Iterator<Float> elements) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newMutableSet(Iterator<Float> elements,
            int expectedSize) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newMutableSet(
            Consumer<net.openhft.koloboke.function.FloatConsumer> elementsSupplier) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elementsSupplier));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newMutableSet(
            Consumer<net.openhft.koloboke.function.FloatConsumer> elementsSupplier,
            int expectedSize) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elementsSupplier, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newMutableSet(float[] elements) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newMutableSet(float[] elements, int expectedSize) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newMutableSet(Float[] elements) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newMutableSet(Float[] elements, int expectedSize) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newMutableSetOf(float e1) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newMutableSetOf(float e1, float e2) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newMutableSetOf(float e1, float e2,
            float e3) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newMutableSetOf(float e1, float e2,
            float e3, float e4) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newMutableSetOf(float e1, float e2,
            float e3, float e4, float e5,
            float... restElements) {
        MutableLHashFloatSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4, e5, restElements));
        return set;
    }
    
    

    @Override
    @Nonnull
    public HashFloatSet newImmutableSet(Iterable<Float> elements, int expectedSize) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newImmutableSet(Iterable<Float> elems1,
            Iterable<Float> elems2, int expectedSize) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newImmutableSet(Iterable<Float> elems1,
            Iterable<Float> elems2, Iterable<Float> elems3, int expectedSize) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newImmutableSet(Iterable<Float> elems1,
            Iterable<Float> elems2, Iterable<Float> elems3,
            Iterable<Float> elems4, int expectedSize) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newImmutableSet(Iterable<Float> elems1,
            Iterable<Float> elems2, Iterable<Float> elems3,
            Iterable<Float> elems4, Iterable<Float> elems5, int expectedSize) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5, expectedSize));
        return set;
    }

    
    

    @Override
    @Nonnull
    public HashFloatSet newImmutableSet(Iterable<Float> elements) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newImmutableSet(Iterable<Float> elems1,
            Iterable<Float> elems2) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newImmutableSet(Iterable<Float> elems1,
            Iterable<Float> elems2, Iterable<Float> elems3) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newImmutableSet(Iterable<Float> elems1,
            Iterable<Float> elems2, Iterable<Float> elems3,
            Iterable<Float> elems4) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newImmutableSet(Iterable<Float> elems1,
            Iterable<Float> elems2, Iterable<Float> elems3,
            Iterable<Float> elems4, Iterable<Float> elems5) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5));
        return set;
    }


    @Override
    @Nonnull
    public HashFloatSet newImmutableSet(Iterator<Float> elements) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newImmutableSet(Iterator<Float> elements,
            int expectedSize) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newImmutableSet(
            Consumer<net.openhft.koloboke.function.FloatConsumer> elementsSupplier) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elementsSupplier));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newImmutableSet(
            Consumer<net.openhft.koloboke.function.FloatConsumer> elementsSupplier,
            int expectedSize) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elementsSupplier, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newImmutableSet(float[] elements) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newImmutableSet(float[] elements, int expectedSize) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newImmutableSet(Float[] elements) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newImmutableSet(Float[] elements, int expectedSize) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newImmutableSetOf(float e1) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newImmutableSetOf(float e1, float e2) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newImmutableSetOf(float e1, float e2,
            float e3) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newImmutableSetOf(float e1, float e2,
            float e3, float e4) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4));
        return set;
    }

    @Override
    @Nonnull
    public HashFloatSet newImmutableSetOf(float e1, float e2,
            float e3, float e4, float e5,
            float... restElements) {
        ImmutableLHashFloatSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4, e5, restElements));
        return set;
    }
}

