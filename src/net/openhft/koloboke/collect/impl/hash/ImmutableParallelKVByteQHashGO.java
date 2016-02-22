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


public abstract class ImmutableParallelKVByteQHashGO
        extends ImmutableParallelKVByteQHashSO {

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
        byte free = freeValue;
        char[] tab = table;
        long base = CHAR_BASE + BYTE_KEY_OFFSET;
        for (long off = ((long) tab.length) << CHAR_SCALE_SHIFT; (off -= CHAR_SCALE) >= 0L;) {
            byte key;
            if ((key = U.getByte(tab, base + off)) != free) {
                action.accept(key);
            }
        }
    }

    public void forEach(ByteConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        byte free = freeValue;
        char[] tab = table;
        long base = CHAR_BASE + BYTE_KEY_OFFSET;
        for (long off = ((long) tab.length) << CHAR_SCALE_SHIFT; (off -= CHAR_SCALE) >= 0L;) {
            byte key;
            if ((key = U.getByte(tab, base + off)) != free) {
                action.accept(key);
            }
        }
    }

    public boolean forEachWhile(
            BytePredicate
            predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
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
        return !terminated;
    }

    public boolean allContainingIn(ByteCollection c) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
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
        return containsAll;
    }


    public boolean reverseAddAllTo(ByteCollection c) {
        if (isEmpty())
            return false;
        boolean changed = false;
        byte free = freeValue;
        char[] tab = table;
        long base = CHAR_BASE + BYTE_KEY_OFFSET;
        for (long off = ((long) tab.length) << CHAR_SCALE_SHIFT; (off -= CHAR_SCALE) >= 0L;) {
            byte key;
            if ((key = U.getByte(tab, base + off)) != free) {
                changed |= c.add(key);
            }
        }
        return changed;
    }


    public boolean reverseRemoveAllFrom(ByteSet s) {
        if (isEmpty() || s.isEmpty())
            return false;
        boolean changed = false;
        byte free = freeValue;
        char[] tab = table;
        long base = CHAR_BASE + BYTE_KEY_OFFSET;
        for (long off = ((long) tab.length) << CHAR_SCALE_SHIFT; (off -= CHAR_SCALE) >= 0L;) {
            byte key;
            if ((key = U.getByte(tab, base + off)) != free) {
                changed |= s.removeByte(key);
            }
        }
        return changed;
    }



    public ByteIterator iterator() {
        
        return new NoRemovedIterator();
    }

    public ByteCursor setCursor() {
        
        return new NoRemovedCursor();
    }

    @Nonnull
    public Object[] toArray() {
        int size = size();
        Object[] result = new Object[size];
        if (size == 0)
            return result;
        int resultIndex = 0;
        byte free = freeValue;
        char[] tab = table;
        long base = CHAR_BASE + BYTE_KEY_OFFSET;
        for (long off = ((long) tab.length) << CHAR_SCALE_SHIFT; (off -= CHAR_SCALE) >= 0L;) {
            byte key;
            if ((key = U.getByte(tab, base + off)) != free) {
                result[resultIndex++] = key;
            }
        }
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
        byte free = freeValue;
        char[] tab = table;
        long base = CHAR_BASE + BYTE_KEY_OFFSET;
        for (long off = ((long) tab.length) << CHAR_SCALE_SHIFT; (off -= CHAR_SCALE) >= 0L;) {
            byte key;
            if ((key = U.getByte(tab, base + off)) != free) {
                a[resultIndex++] = (T) Byte.valueOf(key);
            }
        }
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
        byte free = freeValue;
        char[] tab = table;
        long base = CHAR_BASE + BYTE_KEY_OFFSET;
        for (long off = ((long) tab.length) << CHAR_SCALE_SHIFT; (off -= CHAR_SCALE) >= 0L;) {
            byte key;
            if ((key = U.getByte(tab, base + off)) != free) {
                result[resultIndex++] = key;
            }
        }
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
        byte free = freeValue;
        char[] tab = table;
        long base = CHAR_BASE + BYTE_KEY_OFFSET;
        for (long off = ((long) tab.length) << CHAR_SCALE_SHIFT; (off -= CHAR_SCALE) >= 0L;) {
            byte key;
            if ((key = U.getByte(tab, base + off)) != free) {
                a[resultIndex++] = key;
            }
        }
        if (a.length > resultIndex)
            a[resultIndex] = (byte) 0;
        return a;
    }


    public int setHashCode() {
        int hashCode = 0;
        byte free = freeValue;
        char[] tab = table;
        long base = CHAR_BASE + BYTE_KEY_OFFSET;
        for (long off = ((long) tab.length) << CHAR_SCALE_SHIFT; (off -= CHAR_SCALE) >= 0L;) {
            byte key;
            if ((key = U.getByte(tab, base + off)) != free) {
                hashCode += key;
            }
        }
        return hashCode;
    }

    public String setToString() {
        if (isEmpty())
            return "[]";
        StringBuilder sb = new StringBuilder();
        int elementCount = 0;
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
        sb.setCharAt(0, '[');
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }


    abstract boolean justRemove(byte key);

    public boolean removeIf(Predicate<? super Byte> filter) {
        throw new java.lang.UnsupportedOperationException();
    }

    public boolean removeIf(BytePredicate filter) {
        throw new java.lang.UnsupportedOperationException();
    }

    boolean removeAll(@Nonnull HashByteSet thisC, @Nonnull Collection<?> c) {
        throw new java.lang.UnsupportedOperationException();
    }

    boolean removeAll(@Nonnull HashByteSet thisC, @Nonnull ByteCollection c) {
        throw new java.lang.UnsupportedOperationException();
    }


    boolean retainAll(@Nonnull HashByteSet thisC, @Nonnull Collection<?> c) {
        if (c instanceof ByteCollection)
            return retainAll(thisC, (ByteCollection) c);
        throw new java.lang.UnsupportedOperationException();
    }

    private boolean retainAll(@Nonnull HashByteSet thisC, @Nonnull ByteCollection c) {
        throw new java.lang.UnsupportedOperationException();
    }





    class NoRemovedIterator implements ByteIterator {
        final char[] tab;
        final byte free;
        int nextIndex;
        byte next;

        NoRemovedIterator() {
            char[] tab = this.tab = table;
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
                throw new java.util.NoSuchElementException();
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super Byte> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
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
            if (nextI != nextIndex) {
                throw new java.util.ConcurrentModificationException();
            }
            nextIndex = -1;
        }

        @Override
        public void forEachRemaining(ByteConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
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
            if (nextI != nextIndex) {
                throw new java.util.ConcurrentModificationException();
            }
            nextIndex = -1;
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
                throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedCursor implements ByteCursor {
        final char[] tab;
        final byte free;
        int index;
        byte curKey;

        NoRemovedCursor() {
            this.tab = table;
            index = tab.length;
            byte free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ByteConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
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
            if (index != this.index) {
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
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }

}

