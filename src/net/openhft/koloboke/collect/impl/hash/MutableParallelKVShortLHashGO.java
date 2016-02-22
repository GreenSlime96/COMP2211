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
import net.openhft.koloboke.collect.impl.InternalShortCollectionOps;
import net.openhft.koloboke.collect.set.hash.HashShortSet;
import net.openhft.koloboke.function.ShortConsumer;
import net.openhft.koloboke.function.ShortPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.impl.NotGenerated;

import net.openhft.koloboke.collect.set.ShortSet;
import javax.annotation.Nonnull;

import java.util.*;


public abstract class MutableParallelKVShortLHashGO
        extends MutableParallelKVShortLHashSO {

    @Nonnull
    @Override
    public int[] table() {
        return table;
    }


    @Override
    public int capacity() {
        return table.length;
    }

    public void forEach(Consumer<? super Short> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        long base = INT_BASE + SHORT_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            short key;
            if ((key = U.getShort(tab, base + off)) != free) {
                action.accept(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    public void forEach(ShortConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        long base = INT_BASE + SHORT_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            short key;
            if ((key = U.getShort(tab, base + off)) != free) {
                action.accept(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    public boolean forEachWhile(
            ShortPredicate
            predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        long base = INT_BASE + SHORT_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            short key;
            if ((key = U.getShort(tab, base + off)) != free) {
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

    public boolean allContainingIn(ShortCollection c) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        long base = INT_BASE + SHORT_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            short key;
            if ((key = U.getShort(tab, base + off)) != free) {
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


    public boolean reverseAddAllTo(ShortCollection c) {
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        long base = INT_BASE + SHORT_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            short key;
            if ((key = U.getShort(tab, base + off)) != free) {
                changed |= c.add(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }


    public boolean reverseRemoveAllFrom(ShortSet s) {
        if (isEmpty() || s.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        long base = INT_BASE + SHORT_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            short key;
            if ((key = U.getShort(tab, base + off)) != free) {
                changed |= s.removeShort(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }



    public ShortIterator iterator() {
        int mc = modCount();
        return new NoRemovedIterator(mc);
    }

    public ShortCursor setCursor() {
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
        short free = freeValue;
        int[] tab = table;
        long base = INT_BASE + SHORT_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            short key;
            if ((key = U.getShort(tab, base + off)) != free) {
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
        short free = freeValue;
        int[] tab = table;
        long base = INT_BASE + SHORT_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            short key;
            if ((key = U.getShort(tab, base + off)) != free) {
                a[resultIndex++] = (T) Short.valueOf(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        if (a.length > resultIndex)
            a[resultIndex] = null;
        return a;
    }

    @Nonnull
    public short[] toShortArray() {
        int size = size();
        short[] result = new short[size];
        if (size == 0)
            return result;
        int resultIndex = 0;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        long base = INT_BASE + SHORT_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            short key;
            if ((key = U.getShort(tab, base + off)) != free) {
                result[resultIndex++] = key;
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return result;
    }

    @Nonnull
    public short[] toArray(short[] a) {
        int size = size();
        if (a.length < size)
            a = new short[size];
        if (size == 0) {
            if (a.length > 0)
                a[0] = (short) 0;
            return a;
        }
        int resultIndex = 0;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        long base = INT_BASE + SHORT_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            short key;
            if ((key = U.getShort(tab, base + off)) != free) {
                a[resultIndex++] = key;
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        if (a.length > resultIndex)
            a[resultIndex] = (short) 0;
        return a;
    }


    public int setHashCode() {
        int hashCode = 0;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        long base = INT_BASE + SHORT_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            short key;
            if ((key = U.getShort(tab, base + off)) != free) {
                hashCode += key;
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
        short free = freeValue;
        int[] tab = table;
        long base = INT_BASE + SHORT_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            short key;
            if ((key = U.getShort(tab, base + off)) != free) {
                sb.append(' ').append(key).append(',');
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


    abstract boolean justRemove(short key);

    public boolean removeIf(Predicate<? super Short> filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        short delayedRemoved = (short) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                if (filter.test(key)) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    public boolean removeIf(ShortPredicate filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        short delayedRemoved = (short) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                if (filter.test(key)) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    boolean removeAll(@Nonnull HashShortSet thisC, @Nonnull Collection<?> c) {
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        short delayedRemoved = (short) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                if (c.contains(key)) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    boolean removeAll(@Nonnull HashShortSet thisC, @Nonnull ShortCollection c) {
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        short delayedRemoved = (short) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                if (c.contains(key)) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }


    boolean retainAll(@Nonnull HashShortSet thisC, @Nonnull Collection<?> c) {
        if (c instanceof ShortCollection)
            return retainAll(thisC, (ShortCollection) c);
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
        short free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        short delayedRemoved = (short) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                if (!c.contains(key)) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    private boolean retainAll(@Nonnull HashShortSet thisC, @Nonnull ShortCollection c) {
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
        short free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        short delayedRemoved = (short) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                if (!c.contains(key)) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }


    void closeDelayedRemoved(int firstDelayedRemoved
            , short delayedRemoved) {
        short free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int entry;
        for (int i = firstDelayedRemoved; i >= 0; i--) {
            if ((short) (entry = tab[i]) == delayedRemoved) {
                int indexToRemove = i;
                int indexToShift = indexToRemove;
                int shiftDistance = 1;
                while (true) {
                    indexToShift = (indexToShift - 1) & capacityMask;
                    short keyToShift;
                    if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                        break;
                    }
                    if ((keyToShift != delayedRemoved) && (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance)) {
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), keyToShift);
                        indexToRemove = indexToShift;
                        shiftDistance = 1;
                    } else {
                        shiftDistance++;
                        if (indexToShift == 1 + i) {
                            throw new java.util.ConcurrentModificationException();
                        }
                    }
                }
                U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                postRemoveHook();
            }
        }
    }



    class NoRemovedIterator implements ShortIterator {
        int[] tab;
        final short free;
        final int capacityMask;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        short next;

        NoRemovedIterator(int mc) {
            expectedModCount = mc;
            int[] tab = this.tab = table;
            capacityMask = tab.length - 1;
            short free = this.free = freeValue;
            int nextI = tab.length;
            int entry;
            while (--nextI >= 0) {
                short key;
                if ((key = (short) (entry = tab[nextI])) != free) {
                    next = key;
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public short nextShort() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                if (expectedModCount == modCount()) {
                    index = nextI;
                    int[] tab = this.tab;
                    short free = this.free;
                    short prev = next;
                    int entry;
                    while (--nextI >= 0) {
                        short key;
                        if ((key = (short) (entry = tab[nextI])) != free) {
                            next = key;
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
        public void forEachRemaining(Consumer<? super Short> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            int[] tab = this.tab;
            short free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    action.accept(key);
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            index = nextIndex = -1;
        }

        @Override
        public void forEachRemaining(ShortConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            int[] tab = this.tab;
            short free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    action.accept(key);
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
        public Short next() {
            return nextShort();
        }

        @Override
        public void remove() {
            int index;
            if ((index = this.index) >= 0) {
                if (expectedModCount++ == modCount()) {
                    this.index = -1;
                    int entry;
                    int[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 1) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putShort(this.tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.nextIndex = index;
                                        if (indexToShift < index - 1) {
                                            this.next = keyToShift;
                                        }
                                    }
                                }
                                U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        justRemove(U.getShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (index)) << INT_SCALE_SHIFT)));
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedCursor implements ShortCursor {
        int[] tab;
        final short free;
        final int capacityMask;
        int expectedModCount;
        int index;
        short curKey;

        NoRemovedCursor(int mc) {
            expectedModCount = mc;
            int[] tab = this.tab = table;
            capacityMask = tab.length - 1;
            index = tab.length;
            short free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ShortConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            int[] tab = this.tab;
            short free = this.free;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    action.accept(key);
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public short elem() {
            short curKey;
            if ((curKey = this.curKey) != free) {
                return curKey;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                int[] tab = this.tab;
                short free = this.free;
                int entry;
                for (int i = index - 1; i >= 0; i--) {
                    short key;
                    if ((key = (short) (entry = tab[i])) != free) {
                        index = i;
                        curKey = key;
                        return true;
                    }
                }
                curKey = free;
                index = -1;
                return false;
            } else {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            short curKey;
            short free;
            if ((curKey = this.curKey) != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    int entry;
                    int index = this.index;
                    int[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putShort(this.tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.index = ++index;
                                    }
                                }
                                U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
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

