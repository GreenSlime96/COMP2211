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
import net.openhft.koloboke.collect.impl.InternalByteCollectionOps;
import net.openhft.koloboke.collect.set.hash.HashByteSet;
import net.openhft.koloboke.function.ByteConsumer;
import net.openhft.koloboke.function.BytePredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.impl.NotGenerated;

import net.openhft.koloboke.collect.set.ByteSet;
import javax.annotation.Nonnull;

import java.util.*;


public abstract class MutableParallelKVByteLHashGO
        extends MutableParallelKVByteLHashSO {

    @Nonnull
    @Override
    public char[] table() {
        return table;
    }


    @Override
    public int capacity() {
        return table.length;
    }

    public void forEach(Consumer<? super Byte> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        byte free = freeValue;
        char[] tab = table;
        long base = CHAR_BASE + BYTE_KEY_OFFSET;
        for (long off = ((long) tab.length) << CHAR_SCALE_SHIFT; (off -= CHAR_SCALE) >= 0L;) {
            byte key;
            if ((key = U.getByte(tab, base + off)) != free) {
                action.accept(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    public void forEach(ByteConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        byte free = freeValue;
        char[] tab = table;
        long base = CHAR_BASE + BYTE_KEY_OFFSET;
        for (long off = ((long) tab.length) << CHAR_SCALE_SHIFT; (off -= CHAR_SCALE) >= 0L;) {
            byte key;
            if ((key = U.getByte(tab, base + off)) != free) {
                action.accept(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    public boolean forEachWhile(
            BytePredicate
            predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        byte free = freeValue;
        char[] tab = table;
        long base = CHAR_BASE + BYTE_KEY_OFFSET;
        for (long off = ((long) tab.length) << CHAR_SCALE_SHIFT; (off -= CHAR_SCALE) >= 0L;) {
            byte key;
            if ((key = U.getByte(tab, base + off)) != free) {
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

    public boolean allContainingIn(ByteCollection c) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        byte free = freeValue;
        char[] tab = table;
        long base = CHAR_BASE + BYTE_KEY_OFFSET;
        for (long off = ((long) tab.length) << CHAR_SCALE_SHIFT; (off -= CHAR_SCALE) >= 0L;) {
            byte key;
            if ((key = U.getByte(tab, base + off)) != free) {
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


    public boolean reverseAddAllTo(ByteCollection c) {
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        byte free = freeValue;
        char[] tab = table;
        long base = CHAR_BASE + BYTE_KEY_OFFSET;
        for (long off = ((long) tab.length) << CHAR_SCALE_SHIFT; (off -= CHAR_SCALE) >= 0L;) {
            byte key;
            if ((key = U.getByte(tab, base + off)) != free) {
                changed |= c.add(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }


    public boolean reverseRemoveAllFrom(ByteSet s) {
        if (isEmpty() || s.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        byte free = freeValue;
        char[] tab = table;
        long base = CHAR_BASE + BYTE_KEY_OFFSET;
        for (long off = ((long) tab.length) << CHAR_SCALE_SHIFT; (off -= CHAR_SCALE) >= 0L;) {
            byte key;
            if ((key = U.getByte(tab, base + off)) != free) {
                changed |= s.removeByte(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }



    public ByteIterator iterator() {
        int mc = modCount();
        return new NoRemovedIterator(mc);
    }

    public ByteCursor setCursor() {
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
        byte free = freeValue;
        char[] tab = table;
        long base = CHAR_BASE + BYTE_KEY_OFFSET;
        for (long off = ((long) tab.length) << CHAR_SCALE_SHIFT; (off -= CHAR_SCALE) >= 0L;) {
            byte key;
            if ((key = U.getByte(tab, base + off)) != free) {
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
        byte free = freeValue;
        char[] tab = table;
        long base = CHAR_BASE + BYTE_KEY_OFFSET;
        for (long off = ((long) tab.length) << CHAR_SCALE_SHIFT; (off -= CHAR_SCALE) >= 0L;) {
            byte key;
            if ((key = U.getByte(tab, base + off)) != free) {
                a[resultIndex++] = (T) Byte.valueOf(key);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        if (a.length > resultIndex)
            a[resultIndex] = null;
        return a;
    }

    @Nonnull
    public byte[] toByteArray() {
        int size = size();
        byte[] result = new byte[size];
        if (size == 0)
            return result;
        int resultIndex = 0;
        int mc = modCount();
        byte free = freeValue;
        char[] tab = table;
        long base = CHAR_BASE + BYTE_KEY_OFFSET;
        for (long off = ((long) tab.length) << CHAR_SCALE_SHIFT; (off -= CHAR_SCALE) >= 0L;) {
            byte key;
            if ((key = U.getByte(tab, base + off)) != free) {
                result[resultIndex++] = key;
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return result;
    }

    @Nonnull
    public byte[] toArray(byte[] a) {
        int size = size();
        if (a.length < size)
            a = new byte[size];
        if (size == 0) {
            if (a.length > 0)
                a[0] = (byte) 0;
            return a;
        }
        int resultIndex = 0;
        int mc = modCount();
        byte free = freeValue;
        char[] tab = table;
        long base = CHAR_BASE + BYTE_KEY_OFFSET;
        for (long off = ((long) tab.length) << CHAR_SCALE_SHIFT; (off -= CHAR_SCALE) >= 0L;) {
            byte key;
            if ((key = U.getByte(tab, base + off)) != free) {
                a[resultIndex++] = key;
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        if (a.length > resultIndex)
            a[resultIndex] = (byte) 0;
        return a;
    }


    public int setHashCode() {
        int hashCode = 0;
        int mc = modCount();
        byte free = freeValue;
        char[] tab = table;
        long base = CHAR_BASE + BYTE_KEY_OFFSET;
        for (long off = ((long) tab.length) << CHAR_SCALE_SHIFT; (off -= CHAR_SCALE) >= 0L;) {
            byte key;
            if ((key = U.getByte(tab, base + off)) != free) {
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
        byte free = freeValue;
        char[] tab = table;
        long base = CHAR_BASE + BYTE_KEY_OFFSET;
        for (long off = ((long) tab.length) << CHAR_SCALE_SHIFT; (off -= CHAR_SCALE) >= 0L;) {
            byte key;
            if ((key = U.getByte(tab, base + off)) != free) {
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


    abstract boolean justRemove(byte key);

    public boolean removeIf(Predicate<? super Byte> filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        byte free = freeValue;
        char[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        byte delayedRemoved = (byte) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            byte key;
            if ((key = (byte) (entry = tab[i])) != free) {
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
                            byte keyToShift;
                            if ((keyToShift = (byte) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVByteKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), key);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), delayedRemoved);
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

    public boolean removeIf(BytePredicate filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        byte free = freeValue;
        char[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        byte delayedRemoved = (byte) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            byte key;
            if ((key = (byte) (entry = tab[i])) != free) {
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
                            byte keyToShift;
                            if ((keyToShift = (byte) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVByteKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), key);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), delayedRemoved);
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

    boolean removeAll(@Nonnull HashByteSet thisC, @Nonnull Collection<?> c) {
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        byte free = freeValue;
        char[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        byte delayedRemoved = (byte) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            byte key;
            if ((key = (byte) (entry = tab[i])) != free) {
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
                            byte keyToShift;
                            if ((keyToShift = (byte) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVByteKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), key);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), delayedRemoved);
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

    boolean removeAll(@Nonnull HashByteSet thisC, @Nonnull ByteCollection c) {
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        byte free = freeValue;
        char[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        byte delayedRemoved = (byte) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            byte key;
            if ((key = (byte) (entry = tab[i])) != free) {
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
                            byte keyToShift;
                            if ((keyToShift = (byte) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVByteKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), key);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), delayedRemoved);
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


    boolean retainAll(@Nonnull HashByteSet thisC, @Nonnull Collection<?> c) {
        if (c instanceof ByteCollection)
            return retainAll(thisC, (ByteCollection) c);
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
        byte free = freeValue;
        char[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        byte delayedRemoved = (byte) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            byte key;
            if ((key = (byte) (entry = tab[i])) != free) {
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
                            byte keyToShift;
                            if ((keyToShift = (byte) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVByteKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), key);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), delayedRemoved);
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

    private boolean retainAll(@Nonnull HashByteSet thisC, @Nonnull ByteCollection c) {
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
        byte free = freeValue;
        char[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        byte delayedRemoved = (byte) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            byte key;
            if ((key = (byte) (entry = tab[i])) != free) {
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
                            byte keyToShift;
                            if ((keyToShift = (byte) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVByteKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), key);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), delayedRemoved);
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
            , byte delayedRemoved) {
        byte free = freeValue;
        char[] tab = table;
        int capacityMask = tab.length - 1;
        int entry;
        for (int i = firstDelayedRemoved; i >= 0; i--) {
            if ((byte) (entry = tab[i]) == delayedRemoved) {
                int indexToRemove = i;
                int indexToShift = indexToRemove;
                int shiftDistance = 1;
                while (true) {
                    indexToShift = (indexToShift - 1) & capacityMask;
                    byte keyToShift;
                    if ((keyToShift = (byte) (entry = tab[indexToShift])) == free) {
                        break;
                    }
                    if ((keyToShift != delayedRemoved) && (((ParallelKVByteKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance)) {
                        U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), keyToShift);
                        indexToRemove = indexToShift;
                        shiftDistance = 1;
                    } else {
                        shiftDistance++;
                        if (indexToShift == 1 + i) {
                            throw new java.util.ConcurrentModificationException();
                        }
                    }
                }
                U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), free);
                postRemoveHook();
            }
        }
    }



    class NoRemovedIterator implements ByteIterator {
        char[] tab;
        final byte free;
        final int capacityMask;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        byte next;

        NoRemovedIterator(int mc) {
            expectedModCount = mc;
            char[] tab = this.tab = table;
            capacityMask = tab.length - 1;
            byte free = this.free = freeValue;
            int nextI = tab.length;
            int entry;
            while (--nextI >= 0) {
                byte key;
                if ((key = (byte) (entry = tab[nextI])) != free) {
                    next = key;
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public byte nextByte() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                if (expectedModCount == modCount()) {
                    index = nextI;
                    char[] tab = this.tab;
                    byte free = this.free;
                    byte prev = next;
                    int entry;
                    while (--nextI >= 0) {
                        byte key;
                        if ((key = (byte) (entry = tab[nextI])) != free) {
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
        public void forEachRemaining(Consumer<? super Byte> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            char[] tab = this.tab;
            byte free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    action.accept(key);
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            index = nextIndex = -1;
        }

        @Override
        public void forEachRemaining(ByteConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            char[] tab = this.tab;
            byte free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
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
        public Byte next() {
            return nextByte();
        }

        @Override
        public void remove() {
            int index;
            if ((index = this.index) >= 0) {
                if (expectedModCount++ == modCount()) {
                    this.index = -1;
                    int entry;
                    char[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            byte keyToShift;
                            if ((keyToShift = (byte) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVByteKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 1) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putByte(this.tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), free);
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.nextIndex = index;
                                        if (indexToShift < index - 1) {
                                            this.next = keyToShift;
                                        }
                                    }
                                }
                                U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        justRemove(U.getByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT)));
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedCursor implements ByteCursor {
        char[] tab;
        final byte free;
        final int capacityMask;
        int expectedModCount;
        int index;
        byte curKey;

        NoRemovedCursor(int mc) {
            expectedModCount = mc;
            char[] tab = this.tab = table;
            capacityMask = tab.length - 1;
            index = tab.length;
            byte free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ByteConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            char[] tab = this.tab;
            byte free = this.free;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
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
        public byte elem() {
            byte curKey;
            if ((curKey = this.curKey) != free) {
                return curKey;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                char[] tab = this.tab;
                byte free = this.free;
                int entry;
                for (int i = index - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free) {
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
            byte curKey;
            byte free;
            if ((curKey = this.curKey) != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    int entry;
                    int index = this.index;
                    char[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            byte keyToShift;
                            if ((keyToShift = (byte) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVByteKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putByte(this.tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), free);
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.index = ++index;
                                    }
                                }
                                U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), keyToShift);
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (indexToRemove)) << CHAR_SCALE_SHIFT), free);
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

