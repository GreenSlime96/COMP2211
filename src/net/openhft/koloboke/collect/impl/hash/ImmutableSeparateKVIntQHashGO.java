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
import net.openhft.koloboke.collect.impl.InternalIntCollectionOps;
import net.openhft.koloboke.collect.set.hash.HashIntSet;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.impl.NotGenerated;

import net.openhft.koloboke.collect.set.IntSet;
import javax.annotation.Nonnull;

import java.util.*;


public abstract class ImmutableSeparateKVIntQHashGO
        extends ImmutableSeparateKVIntQHashSO {

    @Nonnull
    @Override
    public int[] keys() {
        return set;
    }


    @Override
    public int capacity() {
        return set.length;
    }

    public void forEach(Consumer<? super Integer> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int free = freeValue;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) != free) {
                action.accept(key);
            }
        }
    }

    public void forEach(IntConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int free = freeValue;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) != free) {
                action.accept(key);
            }
        }
    }

    public boolean forEachWhile(
            IntPredicate
            predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int free = freeValue;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) != free) {
                if (!predicate.test(key)) {
                    terminated = true;
                    break;
                }
            }
        }
        return !terminated;
    }

    public boolean allContainingIn(IntCollection c) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int free = freeValue;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) != free) {
                if (!c.contains(key)) {
                    containsAll = false;
                    break;
                }
            }
        }
        return containsAll;
    }


    public boolean reverseAddAllTo(IntCollection c) {
        if (isEmpty())
            return false;
        boolean changed = false;
        int free = freeValue;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) != free) {
                changed |= c.add(key);
            }
        }
        return changed;
    }


    public boolean reverseRemoveAllFrom(IntSet s) {
        if (isEmpty() || s.isEmpty())
            return false;
        boolean changed = false;
        int free = freeValue;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) != free) {
                changed |= s.removeInt(key);
            }
        }
        return changed;
    }



    public IntIterator iterator() {
        
        return new NoRemovedIterator();
    }

    public IntCursor setCursor() {
        
        return new NoRemovedCursor();
    }

    @Nonnull
    public Object[] toArray() {
        int size = size();
        Object[] result = new Object[size];
        if (size == 0)
            return result;
        int resultIndex = 0;
        int free = freeValue;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) != free) {
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
        int free = freeValue;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) != free) {
                a[resultIndex++] = (T) Integer.valueOf(key);
            }
        }
        if (a.length > resultIndex)
            a[resultIndex] = null;
        return a;
    }

    @Nonnull
    public int[] toIntArray() {
        int size = size();
        int[] result = new int[size];
        if (size == 0)
            return result;
        int resultIndex = 0;
        int free = freeValue;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) != free) {
                result[resultIndex++] = key;
            }
        }
        return result;
    }

    @Nonnull
    public int[] toArray(int[] a) {
        int size = size();
        if (a.length < size)
            a = new int[size];
        if (size == 0) {
            if (a.length > 0)
                a[0] = 0;
            return a;
        }
        int resultIndex = 0;
        int free = freeValue;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) != free) {
                a[resultIndex++] = key;
            }
        }
        if (a.length > resultIndex)
            a[resultIndex] = 0;
        return a;
    }


    public int setHashCode() {
        int hashCode = 0;
        int free = freeValue;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) != free) {
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
        int free = freeValue;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) != free) {
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


    abstract boolean justRemove(int key);

    public boolean removeIf(Predicate<? super Integer> filter) {
        throw new java.lang.UnsupportedOperationException();
    }

    public boolean removeIf(IntPredicate filter) {
        throw new java.lang.UnsupportedOperationException();
    }

    boolean removeAll(@Nonnull HashIntSet thisC, @Nonnull Collection<?> c) {
        throw new java.lang.UnsupportedOperationException();
    }

    boolean removeAll(@Nonnull HashIntSet thisC, @Nonnull IntCollection c) {
        throw new java.lang.UnsupportedOperationException();
    }


    boolean retainAll(@Nonnull HashIntSet thisC, @Nonnull Collection<?> c) {
        if (c instanceof IntCollection)
            return retainAll(thisC, (IntCollection) c);
        throw new java.lang.UnsupportedOperationException();
    }

    private boolean retainAll(@Nonnull HashIntSet thisC, @Nonnull IntCollection c) {
        throw new java.lang.UnsupportedOperationException();
    }





    class NoRemovedIterator implements IntIterator {
        final int[] keys;
        final int free;
        int nextIndex;
        int next;

        NoRemovedIterator() {
            int[] keys = this.keys = set;
            int free = this.free = freeValue;
            int nextI = keys.length;
            while (--nextI >= 0) {
                int key;
                if ((key = keys[nextI]) != free) {
                    next = key;
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public int nextInt() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int[] keys = this.keys;
                int free = this.free;
                int prev = next;
                while (--nextI >= 0) {
                    int key;
                    if ((key = keys[nextI]) != free) {
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
        public void forEachRemaining(Consumer<? super Integer> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int[] keys = this.keys;
            int free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                int key;
                if ((key = keys[i]) != free) {
                    action.accept(key);
                }
            }
            if (nextI != nextIndex) {
                throw new java.util.ConcurrentModificationException();
            }
            nextIndex = -1;
        }

        @Override
        public void forEachRemaining(IntConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int[] keys = this.keys;
            int free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                int key;
                if ((key = keys[i]) != free) {
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
        public Integer next() {
            return nextInt();
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedCursor implements IntCursor {
        final int[] keys;
        final int free;
        int index;
        int curKey;

        NoRemovedCursor() {
            this.keys = set;
            index = keys.length;
            int free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(IntConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int[] keys = this.keys;
            int free = this.free;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = keys[i]) != free) {
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
        public int elem() {
            int curKey;
            if ((curKey = this.curKey) != free) {
                return curKey;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            int[] keys = this.keys;
            int free = this.free;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = keys[i]) != free) {
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

