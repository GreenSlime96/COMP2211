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
import net.openhft.koloboke.collect.impl.InternalDoubleCollectionOps;
import net.openhft.koloboke.collect.set.hash.HashDoubleSet;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.impl.NotGenerated;

import net.openhft.koloboke.collect.set.DoubleSet;
import javax.annotation.Nonnull;

import java.util.*;


public abstract class MutableParallelKVDoubleLHashGO
        extends MutableParallelKVDoubleLHashSO {

    @Nonnull
    @Override
    public long[] table() {
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

    public void forEach(Consumer<? super Double> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                action.accept(Double.longBitsToDouble(key));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    public void forEach(DoubleConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                action.accept(Double.longBitsToDouble(key));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    public boolean forEachWhile(
            DoublePredicate
            predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                if (!predicate.test(Double.longBitsToDouble(key))) {
                    terminated = true;
                    break;
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return !terminated;
    }

    public boolean allContainingIn(DoubleCollection c) {
        if (c instanceof InternalDoubleCollectionOps)
            return allContainingIn((InternalDoubleCollectionOps) c);
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                if (!c.contains(Double.longBitsToDouble(key))) {
                    containsAll = false;
                    break;
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return containsAll;
    }

    boolean allContainingIn(InternalDoubleCollectionOps c) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
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

    public boolean reverseAddAllTo(DoubleCollection c) {
        if (c instanceof InternalDoubleCollectionOps)
            return reverseAddAllTo((InternalDoubleCollectionOps) c);
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                changed |= c.add(Double.longBitsToDouble(key));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    boolean reverseAddAllTo(InternalDoubleCollectionOps c) {
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                changed |= c.add(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    public boolean reverseRemoveAllFrom(DoubleSet s) {
        if (s instanceof InternalDoubleCollectionOps)
            return reverseRemoveAllFrom((InternalDoubleCollectionOps) s);
        if (isEmpty() || s.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                changed |= s.removeDouble(Double.longBitsToDouble(key));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    boolean reverseRemoveAllFrom(InternalDoubleCollectionOps s) {
        if (isEmpty() || s.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                changed |= s.removeDouble(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }


    public DoubleIterator iterator() {
        int mc = modCount();
        return new NoRemovedIterator(mc);
    }

    public DoubleCursor setCursor() {
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
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                result[resultIndex++] = Double.longBitsToDouble(key);
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
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                a[resultIndex++] = (T) Double.valueOf(Double.longBitsToDouble(key));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        if (a.length > resultIndex)
            a[resultIndex] = null;
        return a;
    }

    @Nonnull
    public double[] toDoubleArray() {
        int size = size();
        double[] result = new double[size];
        if (size == 0)
            return result;
        int resultIndex = 0;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                result[resultIndex++] = Double.longBitsToDouble(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return result;
    }

    @Nonnull
    public double[] toArray(double[] a) {
        int size = size();
        if (a.length < size)
            a = new double[size];
        if (size == 0) {
            if (a.length > 0)
                a[0] = 0.0;
            return a;
        }
        int resultIndex = 0;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                a[resultIndex++] = Double.longBitsToDouble(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        if (a.length > resultIndex)
            a[resultIndex] = 0.0;
        return a;
    }


    public int setHashCode() {
        int hashCode = 0;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                hashCode += ((int) (key ^ (key >>> 32)));
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
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                sb.append(' ').append(Double.longBitsToDouble(key)).append(',');
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


    abstract boolean justRemove(long key);

    public boolean removeIf(Predicate<? super Double> filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int firstDelayedRemoved = -1;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                if (filter.test(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    tab[indexToRemove] = REMOVED_BITS;
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
                                if (indexToShift == 2 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        tab[i] = REMOVED_BITS;
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

    public boolean removeIf(DoublePredicate filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int firstDelayedRemoved = -1;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                if (filter.test(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    tab[indexToRemove] = REMOVED_BITS;
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
                                if (indexToShift == 2 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        tab[i] = REMOVED_BITS;
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

    boolean removeAll(@Nonnull HashDoubleSet thisC, @Nonnull Collection<?> c) {
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int firstDelayedRemoved = -1;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                if (c.contains(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    tab[indexToRemove] = REMOVED_BITS;
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
                                if (indexToShift == 2 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        tab[i] = REMOVED_BITS;
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

    boolean removeAll(@Nonnull HashDoubleSet thisC, @Nonnull DoubleCollection c) {
        if (c instanceof InternalDoubleCollectionOps)
            return removeAll(thisC, (InternalDoubleCollectionOps) c);
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int firstDelayedRemoved = -1;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                if (c.contains(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    tab[indexToRemove] = REMOVED_BITS;
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
                                if (indexToShift == 2 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        tab[i] = REMOVED_BITS;
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

    boolean removeAll(@Nonnull HashDoubleSet thisC, @Nonnull InternalDoubleCollectionOps c) {
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int firstDelayedRemoved = -1;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
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
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    tab[indexToRemove] = REMOVED_BITS;
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
                                if (indexToShift == 2 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        tab[i] = REMOVED_BITS;
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

    boolean retainAll(@Nonnull HashDoubleSet thisC, @Nonnull Collection<?> c) {
        if (c instanceof DoubleCollection)
            return retainAll(thisC, (DoubleCollection) c);
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
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int firstDelayedRemoved = -1;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                if (!c.contains(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    tab[indexToRemove] = REMOVED_BITS;
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
                                if (indexToShift == 2 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        tab[i] = REMOVED_BITS;
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

    private boolean retainAll(@Nonnull HashDoubleSet thisC, @Nonnull DoubleCollection c) {
        if (c instanceof InternalDoubleCollectionOps)
            return retainAll(thisC, (InternalDoubleCollectionOps) c);
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
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int firstDelayedRemoved = -1;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                if (!c.contains(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    tab[indexToRemove] = REMOVED_BITS;
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
                                if (indexToShift == 2 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        tab[i] = REMOVED_BITS;
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

    private boolean retainAll(@Nonnull HashDoubleSet thisC,
            @Nonnull InternalDoubleCollectionOps c) {
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
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int firstDelayedRemoved = -1;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
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
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    tab[indexToRemove] = REMOVED_BITS;
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
                                if (indexToShift == 2 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        tab[i] = REMOVED_BITS;
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
        long[] tab = table;
        int capacityMask = tab.length - 2;
        for (int i = firstDelayedRemoved; i >= 0; i -= 2) {
            if (tab[i] == REMOVED_BITS) {
                int indexToRemove = i;
                int indexToShift = indexToRemove;
                int shiftDistance = 2;
                while (true) {
                    indexToShift = (indexToShift - 2) & capacityMask;
                    long keyToShift;
                    if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                        break;
                    }
                    if ((keyToShift != REMOVED_BITS) && (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance)) {
                        tab[indexToRemove] = keyToShift;
                        indexToRemove = indexToShift;
                        shiftDistance = 2;
                    } else {
                        shiftDistance += 2;
                        if (indexToShift == 2 + i) {
                            throw new java.util.ConcurrentModificationException();
                        }
                    }
                }
                tab[indexToRemove] = FREE_BITS;
                postRemoveHook();
            }
        }
    }



    class NoRemovedIterator implements DoubleIterator {
        long[] tab;
        final int capacityMask;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        double next;

        NoRemovedIterator(int mc) {
            expectedModCount = mc;
            long[] tab = this.tab = table;
            capacityMask = tab.length - 2;
            int nextI = tab.length;
            while ((nextI -= 2) >= 0) {
                long key;
                if ((key = tab[nextI]) < FREE_BITS) {
                    next = Double.longBitsToDouble(key);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public double nextDouble() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                if (expectedModCount == modCount()) {
                    index = nextI;
                    long[] tab = this.tab;
                    double prev = next;
                    while ((nextI -= 2) >= 0) {
                        long key;
                        if ((key = tab[nextI]) < FREE_BITS) {
                            next = Double.longBitsToDouble(key);
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
        public void forEachRemaining(Consumer<? super Double> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) < FREE_BITS) {
                    action.accept(Double.longBitsToDouble(key));
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            index = nextIndex = -1;
        }

        @Override
        public void forEachRemaining(DoubleConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) < FREE_BITS) {
                    action.accept(Double.longBitsToDouble(key));
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
        public Double next() {
            return nextDouble();
        }

        @Override
        public void remove() {
            int index;
            if ((index = this.index) >= 0) {
                if (expectedModCount++ == modCount()) {
                    this.index = -1;
                    long[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 2) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.tab[indexToRemove] = FREE_BITS;
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.nextIndex = index;
                                        if (indexToShift < index - 2) {
                                            this.next = Double.longBitsToDouble(keyToShift);
                                        }
                                    }
                                }
                                tab[indexToRemove] = keyToShift;
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                                if (indexToShift == 2 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        justRemove(tab[index]);
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedCursor implements DoubleCursor {
        long[] tab;
        final int capacityMask;
        int expectedModCount;
        int index;
        long curKey;

        NoRemovedCursor(int mc) {
            expectedModCount = mc;
            long[] tab = this.tab = table;
            capacityMask = tab.length - 2;
            index = tab.length;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(DoubleConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            int index = this.index;
            for (int i = index - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) < FREE_BITS) {
                    action.accept(Double.longBitsToDouble(key));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE_BITS;
        }

        @Override
        public double elem() {
            long curKey;
            if ((curKey = this.curKey) != FREE_BITS) {
                return Double.longBitsToDouble(curKey);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                long[] tab = this.tab;
                for (int i = index - 2; i >= 0; i -= 2) {
                    long key;
                    if ((key = tab[i]) < FREE_BITS) {
                        index = i;
                        curKey = key;
                        return true;
                    }
                }
                curKey = FREE_BITS;
                index = -1;
                return false;
            } else {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            long curKey;
            if ((curKey = this.curKey) != FREE_BITS) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE_BITS;
                    int index = this.index;
                    long[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.tab[indexToRemove] = FREE_BITS;
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
                                if (indexToShift == 2 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        justRemove(curKey);
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

