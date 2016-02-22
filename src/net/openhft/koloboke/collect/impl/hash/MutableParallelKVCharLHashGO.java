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
import net.openhft.koloboke.collect.impl.InternalCharCollectionOps;
import net.openhft.koloboke.collect.set.hash.HashCharSet;
import net.openhft.koloboke.function.CharConsumer;
import net.openhft.koloboke.function.CharPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.impl.NotGenerated;

import net.openhft.koloboke.collect.set.CharSet;
import javax.annotation.Nonnull;

import java.util.*;


public abstract class MutableParallelKVCharLHashGO
        extends MutableParallelKVCharLHashSO {

    @Nonnull
    @Override
    public int[] table() {
        return table;
    }


    @Override
    public int capacity() {
        return table.length;
    }

    public void forEach(Consumer<? super Character> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        char free = freeValue;
        int[] tab = table;
        long base = INT_BASE + CHAR_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            char key;
            if ((key = U.getChar(tab, base + off)) != free) {
                action.accept(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    public void forEach(CharConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        char free = freeValue;
        int[] tab = table;
        long base = INT_BASE + CHAR_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            char key;
            if ((key = U.getChar(tab, base + off)) != free) {
                action.accept(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    public boolean forEachWhile(
            CharPredicate
            predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        char free = freeValue;
        int[] tab = table;
        long base = INT_BASE + CHAR_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            char key;
            if ((key = U.getChar(tab, base + off)) != free) {
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

    public boolean allContainingIn(CharCollection c) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        char free = freeValue;
        int[] tab = table;
        long base = INT_BASE + CHAR_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            char key;
            if ((key = U.getChar(tab, base + off)) != free) {
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


    public boolean reverseAddAllTo(CharCollection c) {
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        char free = freeValue;
        int[] tab = table;
        long base = INT_BASE + CHAR_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            char key;
            if ((key = U.getChar(tab, base + off)) != free) {
                changed |= c.add(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }


    public boolean reverseRemoveAllFrom(CharSet s) {
        if (isEmpty() || s.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        char free = freeValue;
        int[] tab = table;
        long base = INT_BASE + CHAR_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            char key;
            if ((key = U.getChar(tab, base + off)) != free) {
                changed |= s.removeChar(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }



    public CharIterator iterator() {
        int mc = modCount();
        return new NoRemovedIterator(mc);
    }

    public CharCursor setCursor() {
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
        char free = freeValue;
        int[] tab = table;
        long base = INT_BASE + CHAR_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            char key;
            if ((key = U.getChar(tab, base + off)) != free) {
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
        char free = freeValue;
        int[] tab = table;
        long base = INT_BASE + CHAR_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            char key;
            if ((key = U.getChar(tab, base + off)) != free) {
                a[resultIndex++] = (T) Character.valueOf(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        if (a.length > resultIndex)
            a[resultIndex] = null;
        return a;
    }

    @Nonnull
    public char[] toCharArray() {
        int size = size();
        char[] result = new char[size];
        if (size == 0)
            return result;
        int resultIndex = 0;
        int mc = modCount();
        char free = freeValue;
        int[] tab = table;
        long base = INT_BASE + CHAR_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            char key;
            if ((key = U.getChar(tab, base + off)) != free) {
                result[resultIndex++] = key;
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return result;
    }

    @Nonnull
    public char[] toArray(char[] a) {
        int size = size();
        if (a.length < size)
            a = new char[size];
        if (size == 0) {
            if (a.length > 0)
                a[0] = (char) 0;
            return a;
        }
        int resultIndex = 0;
        int mc = modCount();
        char free = freeValue;
        int[] tab = table;
        long base = INT_BASE + CHAR_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            char key;
            if ((key = U.getChar(tab, base + off)) != free) {
                a[resultIndex++] = key;
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        if (a.length > resultIndex)
            a[resultIndex] = (char) 0;
        return a;
    }


    public int setHashCode() {
        int hashCode = 0;
        int mc = modCount();
        char free = freeValue;
        int[] tab = table;
        long base = INT_BASE + CHAR_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            char key;
            if ((key = U.getChar(tab, base + off)) != free) {
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
        char free = freeValue;
        int[] tab = table;
        long base = INT_BASE + CHAR_KEY_OFFSET;
        for (long off = ((long) tab.length) << INT_SCALE_SHIFT; (off -= INT_SCALE) >= 0L;) {
            char key;
            if ((key = U.getChar(tab, base + off)) != free) {
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


    abstract boolean justRemove(char key);

    public boolean removeIf(Predicate<? super Character> filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        char free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        char delayedRemoved = (char) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
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
                            char keyToShift;
                            if ((keyToShift = (char) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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

    public boolean removeIf(CharPredicate filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        char free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        char delayedRemoved = (char) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
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
                            char keyToShift;
                            if ((keyToShift = (char) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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

    boolean removeAll(@Nonnull HashCharSet thisC, @Nonnull Collection<?> c) {
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        char free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        char delayedRemoved = (char) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
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
                            char keyToShift;
                            if ((keyToShift = (char) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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

    boolean removeAll(@Nonnull HashCharSet thisC, @Nonnull CharCollection c) {
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        char free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        char delayedRemoved = (char) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
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
                            char keyToShift;
                            if ((keyToShift = (char) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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


    boolean retainAll(@Nonnull HashCharSet thisC, @Nonnull Collection<?> c) {
        if (c instanceof CharCollection)
            return retainAll(thisC, (CharCollection) c);
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
        char free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        char delayedRemoved = (char) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
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
                            char keyToShift;
                            if ((keyToShift = (char) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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

    private boolean retainAll(@Nonnull HashCharSet thisC, @Nonnull CharCollection c) {
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
        char free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        char delayedRemoved = (char) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
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
                            char keyToShift;
                            if ((keyToShift = (char) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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
            , char delayedRemoved) {
        char free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int entry;
        for (int i = firstDelayedRemoved; i >= 0; i--) {
            if ((char) (entry = tab[i]) == delayedRemoved) {
                int indexToRemove = i;
                int indexToShift = indexToRemove;
                int shiftDistance = 1;
                while (true) {
                    indexToShift = (indexToShift - 1) & capacityMask;
                    char keyToShift;
                    if ((keyToShift = (char) (entry = tab[indexToShift])) == free) {
                        break;
                    }
                    if ((keyToShift != delayedRemoved) && (((ParallelKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance)) {
                        U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), keyToShift);
                        indexToRemove = indexToShift;
                        shiftDistance = 1;
                    } else {
                        shiftDistance++;
                        if (indexToShift == 1 + i) {
                            throw new java.util.ConcurrentModificationException();
                        }
                    }
                }
                U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                postRemoveHook();
            }
        }
    }



    class NoRemovedIterator implements CharIterator {
        int[] tab;
        final char free;
        final int capacityMask;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        char next;

        NoRemovedIterator(int mc) {
            expectedModCount = mc;
            int[] tab = this.tab = table;
            capacityMask = tab.length - 1;
            char free = this.free = freeValue;
            int nextI = tab.length;
            int entry;
            while (--nextI >= 0) {
                char key;
                if ((key = (char) (entry = tab[nextI])) != free) {
                    next = key;
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public char nextChar() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                if (expectedModCount == modCount()) {
                    index = nextI;
                    int[] tab = this.tab;
                    char free = this.free;
                    char prev = next;
                    int entry;
                    while (--nextI >= 0) {
                        char key;
                        if ((key = (char) (entry = tab[nextI])) != free) {
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
        public void forEachRemaining(Consumer<? super Character> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            int[] tab = this.tab;
            char free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
                    action.accept(key);
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            index = nextIndex = -1;
        }

        @Override
        public void forEachRemaining(CharConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            int[] tab = this.tab;
            char free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
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
        public Character next() {
            return nextChar();
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
                            char keyToShift;
                            if ((keyToShift = (char) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 1) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putChar(this.tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.nextIndex = index;
                                        if (indexToShift < index - 1) {
                                            this.next = keyToShift;
                                        }
                                    }
                                }
                                U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        justRemove(U.getChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (index)) << INT_SCALE_SHIFT)));
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedCursor implements CharCursor {
        int[] tab;
        final char free;
        final int capacityMask;
        int expectedModCount;
        int index;
        char curKey;

        NoRemovedCursor(int mc) {
            expectedModCount = mc;
            int[] tab = this.tab = table;
            capacityMask = tab.length - 1;
            index = tab.length;
            char free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(CharConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            int[] tab = this.tab;
            char free = this.free;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
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
        public char elem() {
            char curKey;
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
                char free = this.free;
                int entry;
                for (int i = index - 1; i >= 0; i--) {
                    char key;
                    if ((key = (char) (entry = tab[i])) != free) {
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
            char curKey;
            char free;
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
                            char keyToShift;
                            if ((keyToShift = (char) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putChar(this.tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.index = ++index;
                                    }
                                }
                                U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putChar(tab, INT_BASE + CHAR_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
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

