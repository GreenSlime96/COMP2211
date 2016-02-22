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


public abstract class UpdatableSeparateKVByteLHashGO
        extends UpdatableSeparateKVByteLHashSO {

    @Nonnull
    @Override
    public byte[] keys() {
        return set;
    }


    @Override
    public int capacity() {
        return set.length;
    }

    public void forEach(Consumer<? super Byte> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        byte free = freeValue;
        byte[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            byte key;
            if ((key = keys[i]) != free) {
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
        byte[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            byte key;
            if ((key = keys[i]) != free) {
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
        byte[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            byte key;
            if ((key = keys[i]) != free) {
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
        byte[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            byte key;
            if ((key = keys[i]) != free) {
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
        byte[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            byte key;
            if ((key = keys[i]) != free) {
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
        byte[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            byte key;
            if ((key = keys[i]) != free) {
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
        byte[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            byte key;
            if ((key = keys[i]) != free) {
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
        byte[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            byte key;
            if ((key = keys[i]) != free) {
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
        byte[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            byte key;
            if ((key = keys[i]) != free) {
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
        byte[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            byte key;
            if ((key = keys[i]) != free) {
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
        byte[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            byte key;
            if ((key = keys[i]) != free) {
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
        byte[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            byte key;
            if ((key = keys[i]) != free) {
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
        final byte[] keys;
        final byte free;
        int expectedModCount;
        int nextIndex;
        byte next;

        NoRemovedIterator(int mc) {
            expectedModCount = mc;
            byte[] keys = this.keys = set;
            byte free = this.free = freeValue;
            int nextI = keys.length;
            while (--nextI >= 0) {
                byte key;
                if ((key = keys[nextI]) != free) {
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
                    byte[] keys = this.keys;
                    byte free = this.free;
                    byte prev = next;
                    while (--nextI >= 0) {
                        byte key;
                        if ((key = keys[nextI]) != free) {
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
            byte[] keys = this.keys;
            byte free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                byte key;
                if ((key = keys[i]) != free) {
                    action.accept(key);
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            nextIndex = -1;
        }

        @Override
        public void forEachRemaining(ByteConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            byte[] keys = this.keys;
            byte free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                byte key;
                if ((key = keys[i]) != free) {
                    action.accept(key);
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
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
        final byte[] keys;
        final byte free;
        int expectedModCount;
        int index;
        byte curKey;

        NoRemovedCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            byte free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ByteConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            byte[] keys = this.keys;
            byte free = this.free;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                byte key;
                if ((key = keys[i]) != free) {
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
                byte[] keys = this.keys;
                byte free = this.free;
                for (int i = index - 1; i >= 0; i--) {
                    byte key;
                    if ((key = keys[i]) != free) {
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
            throw new java.lang.UnsupportedOperationException();
        }
    }

}

