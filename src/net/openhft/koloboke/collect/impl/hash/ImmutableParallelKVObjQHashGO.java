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


public abstract class ImmutableParallelKVObjQHashGO<E>
        extends ImmutableParallelKVObjQHashSO<E> {

    @Nonnull
    @Override
    public Object[] table() {
        return table;
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
        Object[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            E key;
            // noinspection unchecked
            if ((key = (E) (E) tab[i]) != FREE) {
                action.accept(key);
            }
        }
    }


    public boolean forEachWhile(
            Predicate<? super E>
            predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
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
        return !terminated;
    }

    public boolean allContainingIn(ObjCollection<?> c) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
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
        return containsAll;
    }


    public boolean reverseAddAllTo(ObjCollection<? super E> c) {
        if (isEmpty())
            return false;
        boolean changed = false;
        Object[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            E key;
            // noinspection unchecked
            if ((key = (E) (E) tab[i]) != FREE) {
                changed |= c.add(key);
            }
        }
        return changed;
    }


    public boolean reverseRemoveAllFrom(ObjSet<?> s) {
        if (isEmpty() || s.isEmpty())
            return false;
        boolean changed = false;
        Object[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            E key;
            // noinspection unchecked
            if ((key = (E) (E) tab[i]) != FREE) {
                changed |= s.remove(key);
            }
        }
        return changed;
    }



    public ObjIterator<E> iterator() {
        
        return new NoRemovedIterator();
    }

    public ObjCursor<E> setCursor() {
        
        return new NoRemovedCursor();
    }

    @Nonnull
    public Object[] toArray() {
        int size = size();
        Object[] result = new Object[size];
        if (size == 0)
            return result;
        int resultIndex = 0;
        Object[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            E key;
            // noinspection unchecked
            if ((key = (E) (E) tab[i]) != FREE) {
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
        Object[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            E key;
            // noinspection unchecked
            if ((key = (E) (E) tab[i]) != FREE) {
                a[resultIndex++] = (T) key;
            }
        }
        if (a.length > resultIndex)
            a[resultIndex] = null;
        return a;
    }



    public int setHashCode() {
        int hashCode = 0;
        Object[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            E key;
            // noinspection unchecked
            if ((key = (E) (E) tab[i]) != FREE) {
                hashCode += nullableKeyHashCode(key);
            }
        }
        return hashCode;
    }

    public String setToString() {
        if (isEmpty())
            return "[]";
        StringBuilder sb = new StringBuilder();
        int elementCount = 0;
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
        sb.setCharAt(0, '[');
        sb.setCharAt(sb.length() - 1, ']');
        return sb.toString();
    }


    abstract boolean justRemove(E key);

    public boolean removeIf(Predicate<? super E> filter) {
        throw new java.lang.UnsupportedOperationException();
    }


    boolean removeAll(@Nonnull HashObjSet<E> thisC, @Nonnull Collection<?> c) {
        throw new java.lang.UnsupportedOperationException();
    }


    boolean retainAll(@Nonnull HashObjSet<E> thisC, @Nonnull Collection<?> c) {
        throw new java.lang.UnsupportedOperationException();
    }





    class NoRemovedIterator implements ObjIterator<E> {
        final Object[] tab;
        int nextIndex;
        E next;

        NoRemovedIterator() {
            Object[] tab = this.tab = table;
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
            Object[] tab = this.tab;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i -= 2) {
                Object key;
                if ((key = (E) tab[i]) != FREE) {
                    // noinspection unchecked
                    action.accept((E) key);
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
        public E next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
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
                throw new java.util.NoSuchElementException();
            }
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedCursor implements ObjCursor<E> {
        final Object[] tab;
        int index;
        Object curKey;

        NoRemovedCursor() {
            this.tab = table;
            index = tab.length;
            curKey = FREE;
        }

        @Override
        public void forEachForward(Consumer<? super E> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            Object[] tab = this.tab;
            int index = this.index;
            for (int i = index - 2; i >= 0; i -= 2) {
                Object key;
                if ((key = (E) tab[i]) != FREE) {
                    // noinspection unchecked
                    action.accept((E) key);
                }
            }
            if (index != this.index) {
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
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }

}
