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
import net.openhft.koloboke.collect.set.hash.HashByteSetFactory;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.set.hash.HashByteSet;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;

import static net.openhft.koloboke.collect.impl.Containers.sizeAsInt;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.configIsSuitableForMutableLHash;


public abstract class LHashByteSetFactoryGO extends LHashByteSetFactorySO {

    public LHashByteSetFactoryGO(HashConfig hashConf, int defaultExpectedSize
            , byte lower, byte upper) {
        super(hashConf, defaultExpectedSize, lower, upper);
    }

    

    abstract HashByteSetFactory thisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper);

    abstract HashByteSetFactory lHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper);

    abstract HashByteSetFactory qHashLikeThisWith(HashConfig hashConf, int defaultExpectedSize, byte lower, byte upper);

    @Override
    public final HashByteSetFactory withHashConfig(@Nonnull HashConfig hashConf) {
        if (configIsSuitableForMutableLHash(hashConf))
            return lHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                    , getLowerKeyDomainBound(), getUpperKeyDomainBound());
        return qHashLikeThisWith(hashConf, getDefaultExpectedSize()
            
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }

    @Override
    public final HashByteSetFactory withDefaultExpectedSize(int defaultExpectedSize) {
        if (defaultExpectedSize == getDefaultExpectedSize())
            return this;
        return thisWith(getHashConfig(), defaultExpectedSize
                
                , getLowerKeyDomainBound(), getUpperKeyDomainBound());
    }


    @Override
    public String toString() {
        return "HashByteSetFactory[" + commonString() + keySpecialString() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof HashByteSetFactory) {
            HashByteSetFactory factory = (HashByteSetFactory) obj;
            return commonEquals(factory) && keySpecialEquals(factory);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return keySpecialHashCode(commonHashCode());
    }

    

    

    

    private UpdatableLHashByteSetGO shrunk(UpdatableLHashByteSetGO set) {
        Predicate<HashContainer> shrinkCondition;
        if ((shrinkCondition = hashConf.getShrinkCondition()) != null) {
            if (shrinkCondition.test(set))
                set.shrink();
        }
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSet() {
        return newUpdatableSet(getDefaultExpectedSize());
    }
    @Override
    @Nonnull
    public MutableLHashByteSetGO newMutableSet() {
        return newMutableSet(getDefaultExpectedSize());
    }

    private static int sizeOr(Iterable elems, int defaultSize) {
        return elems instanceof Collection ? ((Collection) elems).size() : defaultSize;
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSet(Iterable<Byte> elements) {
        return newUpdatableSet(elements, sizeOr(elements, getDefaultExpectedSize()));
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        return newUpdatableSet(elems1, elems2, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2, Iterable<Byte> elems3) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        return newUpdatableSet(elems1, elems2, elems3, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2, Iterable<Byte> elems3,
            Iterable<Byte> elems4) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        expectedSize += (long) sizeOr(elems4, 0);
        return newUpdatableSet(elems1, elems2, elems3, elems4, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2, Iterable<Byte> elems3,
            Iterable<Byte> elems4, Iterable<Byte> elems5) {
        long expectedSize = (long) sizeOr(elems1, 0);
        expectedSize += (long) sizeOr(elems2, 0);
        expectedSize += (long) sizeOr(elems3, 0);
        expectedSize += (long) sizeOr(elems4, 0);
        expectedSize += (long) sizeOr(elems5, 0);
        return newUpdatableSet(elems1, elems2, elems3, elems4, elems5, sizeAsInt(expectedSize));
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSet(Iterable<Byte> elements,
            int expectedSize) {
        return shrunk(super.newUpdatableSet(elements, expectedSize));
    }


    private static  void addAll(UpdatableLHashByteSetGO set,
            Iterable<? extends Byte> elems) {
        if (elems instanceof Collection) {
            // noinspection unchecked
            set.addAll((Collection<? extends Byte>) elems);
        } else {
            for (byte e : elems) {
                set.add(e);
            }
        }
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2, int expectedSize) {
        UpdatableLHashByteSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2, Iterable<Byte> elems3,
            int expectedSize) {
        UpdatableLHashByteSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2, Iterable<Byte> elems3,
            Iterable<Byte> elems4, int expectedSize) {
        UpdatableLHashByteSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        addAll(set, elems4);
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2, Iterable<Byte> elems3,
            Iterable<Byte> elems4, Iterable<Byte> elems5,
            int expectedSize) {
        UpdatableLHashByteSetGO set = newUpdatableSet(expectedSize);
        addAll(set, elems1);
        addAll(set, elems2);
        addAll(set, elems3);
        addAll(set, elems4);
        addAll(set, elems5);
        return shrunk(set);
    }


    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSet(Iterator<Byte> elements) {
        return newUpdatableSet(elements, getDefaultExpectedSize());
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSet(Iterator<Byte> elements,
            int expectedSize) {
        UpdatableLHashByteSetGO set = newUpdatableSet(expectedSize);
        while (elements.hasNext()) {
            set.add(elements.next());
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSet(
            Consumer<net.openhft.koloboke.function.ByteConsumer> elementsSupplier) {
        return newUpdatableSet(elementsSupplier, getDefaultExpectedSize());
    }

    

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSet(
            Consumer<net.openhft.koloboke.function.ByteConsumer> elementsSupplier,
            int expectedSize) {
        final UpdatableLHashByteSetGO set = newUpdatableSet(expectedSize);
        elementsSupplier.accept(new net.openhft.koloboke.function.ByteConsumer() {
            @Override
            public void accept(byte e) {
                set.add(e);
            }
        });
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSet(byte[] elements) {
        return newUpdatableSet(elements, elements.length);
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSet(byte[] elements,
            int expectedSize) {
        UpdatableLHashByteSetGO set = newUpdatableSet(expectedSize);
        for (byte e : elements) {
            set.add(e);
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSet(Byte[] elements) {
        return newUpdatableSet(elements, elements.length);
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSet(Byte[] elements,
            int expectedSize) {
        UpdatableLHashByteSetGO set = newUpdatableSet(expectedSize);
        for (byte e : elements) {
            set.add(e);
        }
        return shrunk(set);
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSetOf(byte e1) {
        UpdatableLHashByteSetGO set = newUpdatableSet(1);
        set.add(e1);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSetOf(
            byte e1, byte e2) {
        UpdatableLHashByteSetGO set = newUpdatableSet(2);
        set.add(e1);
        set.add(e2);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSetOf(
            byte e1, byte e2, byte e3) {
        UpdatableLHashByteSetGO set = newUpdatableSet(3);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSetOf(
            byte e1, byte e2, byte e3, byte e4) {
        UpdatableLHashByteSetGO set = newUpdatableSet(4);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        return set;
    }

    @Override
    @Nonnull
    public UpdatableLHashByteSetGO newUpdatableSetOf(byte e1,
            byte e2, byte e3, byte e4, byte e5,
            byte... restElements) {
        UpdatableLHashByteSetGO set = newUpdatableSet(5 + restElements.length);
        set.add(e1);
        set.add(e2);
        set.add(e3);
        set.add(e4);
        set.add(e5);
        for (byte e : restElements) {
            set.add(e);
        }
        return shrunk(set);
    }

    
    

    @Override
    @Nonnull
    public HashByteSet newMutableSet(Iterable<Byte> elements, int expectedSize) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newMutableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2, int expectedSize) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newMutableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2, Iterable<Byte> elems3, int expectedSize) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newMutableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2, Iterable<Byte> elems3,
            Iterable<Byte> elems4, int expectedSize) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newMutableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2, Iterable<Byte> elems3,
            Iterable<Byte> elems4, Iterable<Byte> elems5, int expectedSize) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5, expectedSize));
        return set;
    }

    
    

    @Override
    @Nonnull
    public HashByteSet newMutableSet(Iterable<Byte> elements) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newMutableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newMutableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2, Iterable<Byte> elems3) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newMutableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2, Iterable<Byte> elems3,
            Iterable<Byte> elems4) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newMutableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2, Iterable<Byte> elems3,
            Iterable<Byte> elems4, Iterable<Byte> elems5) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5));
        return set;
    }


    @Override
    @Nonnull
    public HashByteSet newMutableSet(Iterator<Byte> elements) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newMutableSet(Iterator<Byte> elements,
            int expectedSize) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newMutableSet(
            Consumer<net.openhft.koloboke.function.ByteConsumer> elementsSupplier) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elementsSupplier));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newMutableSet(
            Consumer<net.openhft.koloboke.function.ByteConsumer> elementsSupplier,
            int expectedSize) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elementsSupplier, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newMutableSet(byte[] elements) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newMutableSet(byte[] elements, int expectedSize) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newMutableSet(Byte[] elements) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newMutableSet(Byte[] elements, int expectedSize) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newMutableSetOf(byte e1) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newMutableSetOf(byte e1, byte e2) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newMutableSetOf(byte e1, byte e2,
            byte e3) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newMutableSetOf(byte e1, byte e2,
            byte e3, byte e4) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newMutableSetOf(byte e1, byte e2,
            byte e3, byte e4, byte e5,
            byte... restElements) {
        MutableLHashByteSetGO set = uninitializedMutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4, e5, restElements));
        return set;
    }
    
    

    @Override
    @Nonnull
    public HashByteSet newImmutableSet(Iterable<Byte> elements, int expectedSize) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newImmutableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2, int expectedSize) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newImmutableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2, Iterable<Byte> elems3, int expectedSize) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newImmutableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2, Iterable<Byte> elems3,
            Iterable<Byte> elems4, int expectedSize) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newImmutableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2, Iterable<Byte> elems3,
            Iterable<Byte> elems4, Iterable<Byte> elems5, int expectedSize) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5, expectedSize));
        return set;
    }

    
    

    @Override
    @Nonnull
    public HashByteSet newImmutableSet(Iterable<Byte> elements) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newImmutableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newImmutableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2, Iterable<Byte> elems3) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newImmutableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2, Iterable<Byte> elems3,
            Iterable<Byte> elems4) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newImmutableSet(Iterable<Byte> elems1,
            Iterable<Byte> elems2, Iterable<Byte> elems3,
            Iterable<Byte> elems4, Iterable<Byte> elems5) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elems1, elems2, elems3, elems4, elems5));
        return set;
    }


    @Override
    @Nonnull
    public HashByteSet newImmutableSet(Iterator<Byte> elements) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newImmutableSet(Iterator<Byte> elements,
            int expectedSize) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newImmutableSet(
            Consumer<net.openhft.koloboke.function.ByteConsumer> elementsSupplier) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elementsSupplier));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newImmutableSet(
            Consumer<net.openhft.koloboke.function.ByteConsumer> elementsSupplier,
            int expectedSize) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elementsSupplier, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newImmutableSet(byte[] elements) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newImmutableSet(byte[] elements, int expectedSize) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newImmutableSet(Byte[] elements) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newImmutableSet(Byte[] elements, int expectedSize) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSet(elements, expectedSize));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newImmutableSetOf(byte e1) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newImmutableSetOf(byte e1, byte e2) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newImmutableSetOf(byte e1, byte e2,
            byte e3) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newImmutableSetOf(byte e1, byte e2,
            byte e3, byte e4) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4));
        return set;
    }

    @Override
    @Nonnull
    public HashByteSet newImmutableSetOf(byte e1, byte e2,
            byte e3, byte e4, byte e5,
            byte... restElements) {
        ImmutableLHashByteSetGO set = uninitializedImmutableSet();
        set.move(newUpdatableSetOf(e1, e2, e3, e4, e5, restElements));
        return set;
    }
}

