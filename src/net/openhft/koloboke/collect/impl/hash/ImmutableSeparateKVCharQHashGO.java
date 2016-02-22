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


public abstract class ImmutableSeparateKVCharQHashGO
        extends ImmutableSeparateKVCharQHashSO {

    @Nonnull
    @Override
    public char[] keys() {
        return set;
    }


    @Override
    public int capacity() {
        return set.length;
    }

    public void forEach(Consumer<? super Character> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        char free = freeValue;
        char[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
                action.accept(key);
            }
        }
    }

    public void forEach(CharConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        char free = freeValue;
        char[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
                action.accept(key);
            }
        }
    }

    public boolean forEachWhile(
            CharPredicate
            predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        char free = freeValue;
        char[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
                if (!predicate.test(key)) {
                    terminated = true;
                    break;
                }
            }
        }
        return !terminated;
    }

    public boolean allContainingIn(CharCollection c) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        char free = freeValue;
        char[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
                if (!c.contains(key)) {
                    containsAll = false;
                    break;
                }
            }
        }
        return containsAll;
    }


    public boolean reverseAddAllTo(CharCollection c) {
        if (isEmpty())
            return false;
        boolean changed = false;
        char free = freeValue;
        char[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
                changed |= c.add(key);
            }
        }
        return changed;
    }


    public boolean reverseRemoveAllFrom(CharSet s) {
        if (isEmpty() || s.isEmpty())
            return false;
        boolean changed = false;
        char free = freeValue;
        char[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
                changed |= s.removeChar(key);
            }
        }
        return changed;
    }



    public CharIterator iterator() {
        
        return new NoRemovedIterator();
    }

    public CharCursor setCursor() {
        
        return new NoRemovedCursor();
    }

    @Nonnull
    public Object[] toArray() {
        int size = size();
        Object[] result = new Object[size];
        if (size == 0)
            return result;
        int resultIndex = 0;
        char free = freeValue;
        char[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
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
        char free = freeValue;
        char[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
                a[resultIndex++] = (T) Character.valueOf(key);
            }
        }
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
        char free = freeValue;
        char[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
                result[resultIndex++] = key;
            }
        }
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
        char free = freeValue;
        char[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
                a[resultIndex++] = key;
            }
        }
        if (a.length > resultIndex)
            a[resultIndex] = (char) 0;
        return a;
    }


    public int setHashCode() {
        int hashCode = 0;
        char free = freeValue;
        char[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
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
        char free = freeValue;
        char[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
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


    abstract boolean justRemove(char key);

    public boolean removeIf(Predicate<? super Character> filter) {
        throw new java.lang.UnsupportedOperationException();
    }

    public boolean removeIf(CharPredicate filter) {
        throw new java.lang.UnsupportedOperationException();
    }

    boolean removeAll(@Nonnull HashCharSet thisC, @Nonnull Collection<?> c) {
        throw new java.lang.UnsupportedOperationException();
    }

    boolean removeAll(@Nonnull HashCharSet thisC, @Nonnull CharCollection c) {
        throw new java.lang.UnsupportedOperationException();
    }


    boolean retainAll(@Nonnull HashCharSet thisC, @Nonnull Collection<?> c) {
        if (c instanceof CharCollection)
            return retainAll(thisC, (CharCollection) c);
        throw new java.lang.UnsupportedOperationException();
    }

    private boolean retainAll(@Nonnull HashCharSet thisC, @Nonnull CharCollection c) {
        throw new java.lang.UnsupportedOperationException();
    }





    class NoRemovedIterator implements CharIterator {
        final char[] keys;
        final char free;
        int nextIndex;
        char next;

        NoRemovedIterator() {
            char[] keys = this.keys = set;
            char free = this.free = freeValue;
            int nextI = keys.length;
            while (--nextI >= 0) {
                char key;
                if ((key = keys[nextI]) != free) {
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
                char[] keys = this.keys;
                char free = this.free;
                char prev = next;
                while (--nextI >= 0) {
                    char key;
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
        public void forEachRemaining(Consumer<? super Character> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            char[] keys = this.keys;
            char free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                char key;
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
        public void forEachRemaining(CharConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            char[] keys = this.keys;
            char free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                char key;
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
        public Character next() {
            return nextChar();
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedCursor implements CharCursor {
        final char[] keys;
        final char free;
        int index;
        char curKey;

        NoRemovedCursor() {
            this.keys = set;
            index = keys.length;
            char free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(CharConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            char[] keys = this.keys;
            char free = this.free;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                char key;
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
            char[] keys = this.keys;
            char free = this.free;
            for (int i = index - 1; i >= 0; i--) {
                char key;
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

