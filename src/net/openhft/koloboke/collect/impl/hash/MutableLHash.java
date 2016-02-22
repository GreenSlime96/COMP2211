/* with LHash|QHash|DHash hash Mutable|Updatable mutability */
/* if !(QHash|DHash hash Mutable mutability) */
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

import net.openhft.koloboke.collect.hash.HashConfig;

import static net.openhft.koloboke.collect.impl.Maths.isPowerOf2;
import static net.openhft.koloboke.collect.impl.hash.LHashCapacities.*;


public abstract class MutableLHash extends HashWithoutRemovedSlots implements LHash {

    /* if LHash hash */
    static void verifyConfig(HashConfig config) {
        assert config.getGrowFactor() == 2.0;
    }
    /* endif */

    ////////////////////////////
    // Fields

    private HashConfigWrapper configWrapper;

    private int size;

    private int maxSize;

    private int modCount = 0;


    /////////////////////////////
    // Getters

    @Override
    public final HashConfigWrapper configWrapper() {
        return configWrapper;
    }

    @Override
    public final int size() {
        return size;
    }

    @Override
    public final int modCount() {
        return modCount;
    }

    final void incrementModCount() {
        modCount++;
    }


    ////////////////////////
    // Initialization and construction operations

    final void copy(LHash hash) {
        configWrapper = hash.configWrapper();
        size = hash.size();
        int capacity = hash.capacity();
        maxSize = maxSize(capacity);
    }

    final void init(HashConfigWrapper configWrapper, int size) {
        /* if LHash hash */verifyConfig(configWrapper.config());/* endif */
        this.configWrapper = configWrapper;
        this.size = 0;
        internalInit(targetCapacity(size));
    }

    private void internalInit(int capacity) {
        /* if LHash hash */assert isPowerOf2(capacity);/* endif */
        maxSize = maxSize(capacity);
        allocateArrays(capacity);
    }

    private int maxSize(int capacity) {
        // No sense in trying to rehash after each insertion
        // if the capacity is already reached the limit.
        return !isMaxCapacity(capacity) ? configWrapper.maxSize(capacity) : capacity - 1;
    }

    /**
     * Allocates arrays of {@code capacity} length to hold states, elements, keys or values in.
     *
     * <p>Subclasses should override this method, but MUST NOT call it. This method is called
     * in {@code MutableLHash} from {@link #initForRehash(int)} and
     * {@link #init(HashConfigWrapper, int)} methods.
     *
     * @param capacity length of arrays, comprising the hashtable
     */
    abstract void allocateArrays(int capacity);

    /**
     * Moves elements to the new arrays of {@code newCapacity} length.
     *
     * <p>This method should be implemented as follows:
     *
     *  1. Copy references to the old arrays comprising the hashtable from fields to local variables
     *
     *  2. Call {@link #initForRehash(int)}
     *
     *  3. Move elements, entries, etc. from the old arrays to the new ones.
     *
     * <p>Subclasses should implement, but MUST NOT call this method. This method is called
     * in {@code MutableLHash} from {@link #postInsertHook()}, {@link #ensureCapacity(long)}
     * and {@link #shrink()} methods.
     */
    abstract void rehash(int newCapacity);

    /**
     * This method just increments modification count (see {@link #modCount()})
     * and calls {@link #internalInit(int)}. Should be called by subclasses
     * in {@link #rehash(int)} implementation.
     */
    final void initForRehash(int newCapacity) {
        modCount++;
        internalInit(newCapacity);
    }


    //////////////////////////////
    // Roots of chain operations

    /**
     * Empties the hash.
     */
    @Override
    public void clear() {
        modCount++;
        size = 0;
    }

    /* if Mutable mutability */
    abstract void removeAt(int index);
    /* endif */


    /////////////////////////////
    // Modification hooks and rehash logic

    @Override
    public boolean shrink() {
        int newCapacity = targetCapacity(size);
        if (newCapacity < capacity()) {
            rehash(newCapacity);
            return true;
        } else {
            return false;
        }
    }

    private boolean tryRehashForExpansion(int newCapacity) {
        if (newCapacity > capacity()) {
            rehash(newCapacity);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public final boolean ensureCapacity(long minSize) {
        int intMinSize = (int) Math.min(minSize, (long) Integer.MAX_VALUE);
        if (minSize < 0L)
            throw new IllegalArgumentException(
                    "Min size should be positive, " + minSize + " given.");
        return intMinSize > maxSize &&
                tryRehashForExpansion(targetCapacity(intMinSize));
    }

    /* if Mutable mutability */
    final void postRemoveHook() {
        size--;
    }
    /* endif */

    final void postInsertHook() {
        if (++size > maxSize) {
            /* if LHash hash */
            int capacity = capacity();
            if (!isMaxCapacity(capacity)) {
                rehash(capacity << 1);
            }
            /* elif !(LHash hash) */
            tryRehashForExpansion(grownCapacity());
            /* endif */
        }
    }

    /**
     * LongLong, LongDouble, DoubleDouble and DoubleLong maps might use array of doubled size
     * as table to layout keys and values in parallel. They should override this method to return
     * {@code true}.
     *
     * <p>IntInt and smaller maps would better use array of larger primitives ({@code long[]} in
     * this particular case), because it 1) guarantees that each key and value pair lay on the same
     * cache line and 2) allows higher max capacity.
     *
     * <p>It is MutableLHash's concern in order to treat edge cases of capacities near to Java
     * array size limit correctly.
     *
     * @return if the hash is a map which use double sized array to layout keys and values
     *         in parallel
     */
    boolean doubleSizedArrays() {
        return false;
    }

    private int targetCapacity(int size) {
        return LHashCapacities.capacity(configWrapper, size, doubleSizedArrays());
    }

    private boolean isMaxCapacity(int capacity) {
        return LHashCapacities.isMaxCapacity(capacity, doubleSizedArrays());
    }

    /* if !(LHash hash) */
    private int grownCapacity() {
        return nearestGreaterCapacity(configWrapper.grow(capacity()), size, doubleSizedArrays());
    }
    /* endif */
}
