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

import net.openhft.koloboke.collect.*;
import net.openhft.koloboke.collect.impl.InternalObjCollectionOps;
import net.openhft.koloboke.collect.set.hash.HashObjSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.impl.NotGenerated;

import net.openhft.koloboke.collect.set.ObjSet;
import javax.annotation.Nonnull;

import java.util.*;


public abstract class MutableParallelKVObjLHashGO<E>
        extends MutableParallelKVObjLHashSO<E> {

    @Nonnull
    @Override
    public Object[] table() {
        return table;
    }

    @Override
    boolean doubleSizedArrays() {
        return true;
    }

    @Override
    public int capacity() {
        return table.length >> 1;
    }

    public void forEach(Consumer<? super E> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        Object[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            E key;
            // noinspection unchecked
            if ((key = (E) (E) tab[i]) != FREE) {
                action.accept(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }


    public boolean forEachWhile(
            Predicate<? super E>
            predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        Object[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            E key;
            // noinspection unchecked
            if ((key = (E) (E) tab[i]) != FREE) {
                if (!predicate.test(key)) {
                    terminated = true;
                    break;
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return !terminated;
    }

    public boolean allContainingIn(ObjCollection<?> c) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        Object[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            E key;
            // noinspection unchecked
            if ((key = (E) (E) tab[i]) != FREE) {
                if (!c.contains(key)) {
                    containsAll = false;
                    break;
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return containsAll;
    }


    public boolean reverseAddAllTo(ObjCollection<? super E> c) {
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        Object[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            E key;
            // noinspection unchecked
            if ((key = (E) (E) tab[i]) != FREE) {
                changed |= c.add(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }


    public boolean reverseRemoveAllFrom(ObjSet<?> s) {
        if (isEmpty() || s.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        Object[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            E key;
            // noinspection unchecked
            if ((key = (E) (E) tab[i]) != FREE) {
                changed |= s.remove(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }



    public ObjIterator<E> iterator() {
        int mc = modCount();
        return new NoRemovedIterator(mc);
    }

    public ObjCursor<E> setCursor() {
        int mc = modCount();
        return new NoRemovedCursor(mc);
    }

    @Nonnull
    public Object[] toArray() {
        int size = size();
        Object[] result = new Object[size];
        if (size == 0)
            return result;
        int resultIndex = 0;
        int mc = modCount();
        Object[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            E key;
            // noinspection unchecked
            if ((key = (E) (E) tab[i]) != FREE) {
                result[resultIndex++] = key;
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return result;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public <T> T[] toArray(@Nonnull T[] a) {
        int size = size();
        if (a.length < size) {
            Class<?> elementType = a.getClass().getComponentType();
            a = (T[]) java.lang.reflect.Array.newInstance(elementType, size);
        }
        if (size == 0) {
            if (a.length > 0)
                a[0] = null;
            return a;
        }
        int resultIndex = 0;
        int mc = modCount();
        Object[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            E key;
            // noinspection unchecked
            if ((key = (E) (E) tab[i]) != FREE) {
                a[resultIndex++] = (T) key;
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        if (a.length > resultIndex)
            a[resultIndex] = null;
        return a;
    }



    public int setHashCode() {
        int hashCode = 0;
        int mc = modCount();
        Object[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            E key;
            // noinspection unchecked
            if ((key = (E) (E) tab[i]) != FREE) {
                hashCode += nullableKeyHashCode(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return hashCode;
    }

    public String setToString() {
        if (isEmpty())
            return "[]";
        StringBuilder sb = new StringBuilder();
        int elementCount = 0;
        int mc = modCount();
        Object[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            E key;
            // noinspection unchecked
            if ((key = (E) (E) tab[i]) != FREE) {
                sb.append(' ').append(key != this ? key : "(this Collection)").append(',');
                if (++elementCount == 8) {
                    int expectedLength = sb.length() * (size() / 8);
                    sb.ensureCapacity(expectedLength + (expectedLength / 2));
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        sb.setCharAt(0, '[');
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }


    abstract boolean justRemove(E key);

    public boolean removeIf(Predicate<? super E> filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        Object[] tab = table;
        int capacityMask = tab.length - 2;
        int firstDelayedRemoved = -1;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            E key;
            // noinspection unchecked
            if ((key = (E) (E) tab[i]) != FREE) {
                if (filter.test(key)) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            E keyToShift;
                            // noinspection unchecked
                            if ((keyToShift = (E) (E) tab[indexToShift]) == FREE) {
                                break;
                            }
                            if (((ParallelKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    tab[indexToRemove] = REMOVED;
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i += 2;
                                }
                                tab[indexToRemove] = keyToShift;
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                            }
                        }
                        tab[indexToRemove] = FREE;
                        postRemoveHook();
                    } else {
                        tab[i] = REMOVED;
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }


    boolean removeAll(@Nonnull HashObjSet<E> thisC, @Nonnull Collection<?> c) {
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        Object[] tab = table;
        int capacityMask = tab.length - 2;
        int firstDelayedRemoved = -1;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            E key;
            // noinspection unchecked
            if ((key = (E) (E) tab[i]) != FREE) {
                if (c.contains(key)) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            E keyToShift;
                            // noinspection unchecked
                            if ((keyToShift = (E) (E) tab[indexToShift]) == FREE) {
                                break;
                            }
                            if (((ParallelKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    tab[indexToRemove] = REMOVED;
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i += 2;
                                }
                                tab[indexToRemove] = keyToShift;
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                            }
                        }
                        tab[indexToRemove] = FREE;
                        postRemoveHook();
                    } else {
                        tab[i] = REMOVED;
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }


    boolean retainAll(@Nonnull HashObjSet<E> thisC, @Nonnull Collection<?> c) {
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty())
            return false;
        if (c.isEmpty()) {
            clear();
            return true;
        }
        boolean changed = false;
        int mc = modCount();
        Object[] tab = table;
        int capacityMask = tab.length - 2;
        int firstDelayedRemoved = -1;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            E key;
            // noinspection unchecked
            if ((key = (E) (E) tab[i]) != FREE) {
                if (!c.contains(key)) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            E keyToShift;
                            // noinspection unchecked
                            if ((keyToShift = (E) (E) tab[indexToShift]) == FREE) {
                                break;
                            }
                            if (((ParallelKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    tab[indexToRemove] = REMOVED;
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i += 2;
                                }
                                tab[indexToRemove] = keyToShift;
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                            }
                        }
                        tab[indexToRemove] = FREE;
                        postRemoveHook();
                    } else {
                        tab[i] = REMOVED;
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }


    void closeDelayedRemoved(int firstDelayedRemoved
            ) {
        Object[] tab = table;
        int capacityMask = tab.length - 2;
        for (int i = firstDelayedRemoved; i >= 0; i -= 2) {
            if ((E) tab[i] == REMOVED) {
                int indexToRemove = i;
                int indexToShift = indexToRemove;
                int shiftDistance = 2;
                while (true) {
                    indexToShift = (indexToShift - 2) & capacityMask;
                    E keyToShift;
                    if ((keyToShift = (E) tab[indexToShift]) == FREE) {
                        break;
                    }
                    if ((keyToShift != REMOVED) && (((ParallelKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance)) {
                        tab[indexToRemove] = keyToShift;
                        indexToRemove = indexToShift;
                        shiftDistance = 2;
                    } else {
                        shiftDistance += 2;
                    }
                }
                tab[indexToRemove] = FREE;
                postRemoveHook();
            }
        }
    }



    class NoRemovedIterator implements ObjIterator<E> {
        Object[] tab;
        final int capacityMask;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        E next;

        NoRemovedIterator(int mc) {
            expectedModCount = mc;
            Object[] tab = this.tab = table;
            capacityMask = tab.length - 2;
            int nextI = tab.length;
            while ((nextI -= 2) >= 0) {
                Object key;
                if ((key = (E) tab[nextI]) != FREE) {
                    // noinspection unchecked
                    next = (E) key;
                    break;
                }
            }
            nextIndex = nextI;
        }


        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            Object[] tab = this.tab;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i -= 2) {
                Object key;
                if ((key = (E) tab[i]) != FREE) {
                    // noinspection unchecked
                    action.accept((E) key);
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            index = nextIndex = -1;
        }


        @Override
        public boolean hasNext() {
            return nextIndex >= 0;
        }

        @Override
        public E next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                if (expectedModCount == modCount()) {
                    index = nextI;
                    Object[] tab = this.tab;
                    E prev = next;
                    while ((nextI -= 2) >= 0) {
                        Object key;
                        if ((key = (E) tab[nextI]) != FREE) {
                            // noinspection unchecked
                            next = (E) key;
                            break;
                        }
                    }
                    nextIndex = nextI;
                    return prev;
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.util.NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            int index;
            if ((index = this.index) >= 0) {
                if (expectedModCount++ == modCount()) {
                    this.index = -1;
                    Object[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            E keyToShift;
                            if ((keyToShift = (E) tab[indexToShift]) == FREE) {
                                break;
                            }
                            if (((ParallelKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 2) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.tab[indexToRemove] = FREE;
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.nextIndex = index;
                                        if (indexToShift < index - 2) {
                                            this.next = keyToShift;
                                        }
                                    }
                                }
                                tab[indexToRemove] = keyToShift;
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                            }
                        }
                        tab[indexToRemove] = FREE;
                        postRemoveHook();
                    } else {
                        justRemove((E) tab[index]);
                        tab[index] = null;
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedCursor implements ObjCursor<E> {
        Object[] tab;
        final int capacityMask;
        int expectedModCount;
        int index;
        Object curKey;

        NoRemovedCursor(int mc) {
            expectedModCount = mc;
            Object[] tab = this.tab = table;
            capacityMask = tab.length - 2;
            index = tab.length;
            curKey = FREE;
        }

        @Override
        public void forEachForward(Consumer<? super E> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            Object[] tab = this.tab;
            int index = this.index;
            for (int i = index - 2; i >= 0; i -= 2) {
                Object key;
                if ((key = (E) tab[i]) != FREE) {
                    // noinspection unchecked
                    action.accept((E) key);
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE;
        }

        @Override
        public E elem() {
            Object curKey;
            if ((curKey = this.curKey) != FREE) {
                // noinspection unchecked
                return (E) curKey;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                Object[] tab = this.tab;
                for (int i = index - 2; i >= 0; i -= 2) {
                    Object key;
                    if ((key = (E) tab[i]) != FREE) {
                        index = i;
                        curKey = key;
                        return true;
                    }
                }
                curKey = FREE;
                index = -1;
                return false;
            } else {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            Object curKey;
            if ((curKey = this.curKey) != FREE) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE;
                    int index = this.index;
                    Object[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            E keyToShift;
                            if ((keyToShift = (E) tab[indexToShift]) == FREE) {
                                break;
                            }
                            if (((ParallelKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.tab[indexToRemove] = FREE;
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.index = (index += 2);
                                    }
                                }
                                tab[indexToRemove] = keyToShift;
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                            }
                        }
                        tab[indexToRemove] = FREE;
                        postRemoveHook();
                    } else {
                        justRemove((E) curKey);
                        tab[index] = null;
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }

}

