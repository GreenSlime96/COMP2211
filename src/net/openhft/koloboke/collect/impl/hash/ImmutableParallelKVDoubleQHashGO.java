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


public abstract class ImmutableParallelKVDoubleQHashGO
        extends ImmutableParallelKVDoubleQHashSO {

    @Nonnull
    @Override
    public long[] table() {
        return table;
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
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                action.accept(Double.longBitsToDouble(key));
            }
        }
    }

    public void forEach(DoubleConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                action.accept(Double.longBitsToDouble(key));
            }
        }
    }

    public boolean forEachWhile(
            DoublePredicate
            predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
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
        return !terminated;
    }

    public boolean allContainingIn(DoubleCollection c) {
        if (c instanceof InternalDoubleCollectionOps)
            return allContainingIn((InternalDoubleCollectionOps) c);
        if (isEmpty())
            return true;
        boolean containsAll = true;
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
        return containsAll;
    }

    boolean allContainingIn(InternalDoubleCollectionOps c) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
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
        return containsAll;
    }

    public boolean reverseAddAllTo(DoubleCollection c) {
        if (c instanceof InternalDoubleCollectionOps)
            return reverseAddAllTo((InternalDoubleCollectionOps) c);
        if (isEmpty())
            return false;
        boolean changed = false;
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                changed |= c.add(Double.longBitsToDouble(key));
            }
        }
        return changed;
    }

    boolean reverseAddAllTo(InternalDoubleCollectionOps c) {
        if (isEmpty())
            return false;
        boolean changed = false;
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                changed |= c.add(key);
            }
        }
        return changed;
    }

    public boolean reverseRemoveAllFrom(DoubleSet s) {
        if (s instanceof InternalDoubleCollectionOps)
            return reverseRemoveAllFrom((InternalDoubleCollectionOps) s);
        if (isEmpty() || s.isEmpty())
            return false;
        boolean changed = false;
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                changed |= s.removeDouble(Double.longBitsToDouble(key));
            }
        }
        return changed;
    }

    boolean reverseRemoveAllFrom(InternalDoubleCollectionOps s) {
        if (isEmpty() || s.isEmpty())
            return false;
        boolean changed = false;
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                changed |= s.removeDouble(key);
            }
        }
        return changed;
    }


    public DoubleIterator iterator() {
        
        return new NoRemovedIterator();
    }

    public DoubleCursor setCursor() {
        
        return new NoRemovedCursor();
    }

    @Nonnull
    public Object[] toArray() {
        int size = size();
        Object[] result = new Object[size];
        if (size == 0)
            return result;
        int resultIndex = 0;
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                result[resultIndex++] = Double.longBitsToDouble(key);
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
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                a[resultIndex++] = (T) Double.valueOf(Double.longBitsToDouble(key));
            }
        }
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
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                result[resultIndex++] = Double.longBitsToDouble(key);
            }
        }
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
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                a[resultIndex++] = Double.longBitsToDouble(key);
            }
        }
        if (a.length > resultIndex)
            a[resultIndex] = 0.0;
        return a;
    }


    public int setHashCode() {
        int hashCode = 0;
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                hashCode += ((int) (key ^ (key >>> 32)));
            }
        }
        return hashCode;
    }

    public String setToString() {
        if (isEmpty())
            return "[]";
        StringBuilder sb = new StringBuilder();
        int elementCount = 0;
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
        sb.setCharAt(0, '[');
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }


    abstract boolean justRemove(long key);

    public boolean removeIf(Predicate<? super Double> filter) {
        throw new java.lang.UnsupportedOperationException();
    }

    public boolean removeIf(DoublePredicate filter) {
        throw new java.lang.UnsupportedOperationException();
    }

    boolean removeAll(@Nonnull HashDoubleSet thisC, @Nonnull Collection<?> c) {
        throw new java.lang.UnsupportedOperationException();
    }

    boolean removeAll(@Nonnull HashDoubleSet thisC, @Nonnull DoubleCollection c) {
        throw new java.lang.UnsupportedOperationException();
    }

    boolean removeAll(@Nonnull HashDoubleSet thisC, @Nonnull InternalDoubleCollectionOps c) {
        throw new java.lang.UnsupportedOperationException();
    }

    boolean retainAll(@Nonnull HashDoubleSet thisC, @Nonnull Collection<?> c) {
        if (c instanceof DoubleCollection)
            return retainAll(thisC, (DoubleCollection) c);
        throw new java.lang.UnsupportedOperationException();
    }

    private boolean retainAll(@Nonnull HashDoubleSet thisC, @Nonnull DoubleCollection c) {
        throw new java.lang.UnsupportedOperationException();
    }

    private boolean retainAll(@Nonnull HashDoubleSet thisC,
            @Nonnull InternalDoubleCollectionOps c) {
        throw new java.lang.UnsupportedOperationException();
    }




    class NoRemovedIterator implements DoubleIterator {
        final long[] tab;
        int nextIndex;
        double next;

        NoRemovedIterator() {
            long[] tab = this.tab = table;
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
                throw new java.util.NoSuchElementException();
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super Double> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            long[] tab = this.tab;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) < FREE_BITS) {
                    action.accept(Double.longBitsToDouble(key));
                }
            }
            if (nextI != nextIndex) {
                throw new java.util.ConcurrentModificationException();
            }
            nextIndex = -1;
        }

        @Override
        public void forEachRemaining(DoubleConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            long[] tab = this.tab;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) < FREE_BITS) {
                    action.accept(Double.longBitsToDouble(key));
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
        public Double next() {
            return nextDouble();
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedCursor implements DoubleCursor {
        final long[] tab;
        int index;
        long curKey;

        NoRemovedCursor() {
            this.tab = table;
            index = tab.length;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(DoubleConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            long[] tab = this.tab;
            int index = this.index;
            for (int i = index - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) < FREE_BITS) {
                    action.accept(Double.longBitsToDouble(key));
                }
            }
            if (index != this.index) {
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
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }

}

