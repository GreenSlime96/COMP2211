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
import net.openhft.koloboke.collect.impl.InternalFloatCollectionOps;
import net.openhft.koloboke.collect.set.hash.HashFloatSet;
import net.openhft.koloboke.function.FloatConsumer;
import net.openhft.koloboke.function.FloatPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.impl.NotGenerated;

import net.openhft.koloboke.collect.set.FloatSet;
import javax.annotation.Nonnull;

import java.util.*;


public abstract class ImmutableSeparateKVFloatLHashGO
        extends ImmutableSeparateKVFloatLHashSO {

    @Nonnull
    @Override
    public int[] keys() {
        return set;
    }


    @Override
    public int capacity() {
        return set.length;
    }

    public void forEach(Consumer<? super Float> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                action.accept(Float.intBitsToFloat(key));
            }
        }
    }

    public void forEach(FloatConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                action.accept(Float.intBitsToFloat(key));
            }
        }
    }

    public boolean forEachWhile(
            FloatPredicate
            predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                if (!predicate.test(Float.intBitsToFloat(key))) {
                    terminated = true;
                    break;
                }
            }
        }
        return !terminated;
    }

    public boolean allContainingIn(FloatCollection c) {
        if (c instanceof InternalFloatCollectionOps)
            return allContainingIn((InternalFloatCollectionOps) c);
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                if (!c.contains(Float.intBitsToFloat(key))) {
                    containsAll = false;
                    break;
                }
            }
        }
        return containsAll;
    }

    boolean allContainingIn(InternalFloatCollectionOps c) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                if (!c.contains(key)) {
                    containsAll = false;
                    break;
                }
            }
        }
        return containsAll;
    }

    public boolean reverseAddAllTo(FloatCollection c) {
        if (c instanceof InternalFloatCollectionOps)
            return reverseAddAllTo((InternalFloatCollectionOps) c);
        if (isEmpty())
            return false;
        boolean changed = false;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                changed |= c.add(Float.intBitsToFloat(key));
            }
        }
        return changed;
    }

    boolean reverseAddAllTo(InternalFloatCollectionOps c) {
        if (isEmpty())
            return false;
        boolean changed = false;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                changed |= c.add(key);
            }
        }
        return changed;
    }

    public boolean reverseRemoveAllFrom(FloatSet s) {
        if (s instanceof InternalFloatCollectionOps)
            return reverseRemoveAllFrom((InternalFloatCollectionOps) s);
        if (isEmpty() || s.isEmpty())
            return false;
        boolean changed = false;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                changed |= s.removeFloat(Float.intBitsToFloat(key));
            }
        }
        return changed;
    }

    boolean reverseRemoveAllFrom(InternalFloatCollectionOps s) {
        if (isEmpty() || s.isEmpty())
            return false;
        boolean changed = false;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                changed |= s.removeFloat(key);
            }
        }
        return changed;
    }


    public FloatIterator iterator() {
        
        return new NoRemovedIterator();
    }

    public FloatCursor setCursor() {
        
        return new NoRemovedCursor();
    }

    @Nonnull
    public Object[] toArray() {
        int size = size();
        Object[] result = new Object[size];
        if (size == 0)
            return result;
        int resultIndex = 0;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                result[resultIndex++] = Float.intBitsToFloat(key);
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
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                a[resultIndex++] = (T) Float.valueOf(Float.intBitsToFloat(key));
            }
        }
        if (a.length > resultIndex)
            a[resultIndex] = null;
        return a;
    }

    @Nonnull
    public float[] toFloatArray() {
        int size = size();
        float[] result = new float[size];
        if (size == 0)
            return result;
        int resultIndex = 0;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                result[resultIndex++] = Float.intBitsToFloat(key);
            }
        }
        return result;
    }

    @Nonnull
    public float[] toArray(float[] a) {
        int size = size();
        if (a.length < size)
            a = new float[size];
        if (size == 0) {
            if (a.length > 0)
                a[0] = 0.0f;
            return a;
        }
        int resultIndex = 0;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                a[resultIndex++] = Float.intBitsToFloat(key);
            }
        }
        if (a.length > resultIndex)
            a[resultIndex] = 0.0f;
        return a;
    }


    public int setHashCode() {
        int hashCode = 0;
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
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
        int[] keys = set;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                sb.append(' ').append(Float.intBitsToFloat(key)).append(',');
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

    public boolean removeIf(Predicate<? super Float> filter) {
        throw new java.lang.UnsupportedOperationException();
    }

    public boolean removeIf(FloatPredicate filter) {
        throw new java.lang.UnsupportedOperationException();
    }

    boolean removeAll(@Nonnull HashFloatSet thisC, @Nonnull Collection<?> c) {
        throw new java.lang.UnsupportedOperationException();
    }

    boolean removeAll(@Nonnull HashFloatSet thisC, @Nonnull FloatCollection c) {
        throw new java.lang.UnsupportedOperationException();
    }

    boolean removeAll(@Nonnull HashFloatSet thisC, @Nonnull InternalFloatCollectionOps c) {
        throw new java.lang.UnsupportedOperationException();
    }

    boolean retainAll(@Nonnull HashFloatSet thisC, @Nonnull Collection<?> c) {
        if (c instanceof FloatCollection)
            return retainAll(thisC, (FloatCollection) c);
        throw new java.lang.UnsupportedOperationException();
    }

    private boolean retainAll(@Nonnull HashFloatSet thisC, @Nonnull FloatCollection c) {
        throw new java.lang.UnsupportedOperationException();
    }

    private boolean retainAll(@Nonnull HashFloatSet thisC,
            @Nonnull InternalFloatCollectionOps c) {
        throw new java.lang.UnsupportedOperationException();
    }




    class NoRemovedIterator implements FloatIterator {
        final int[] keys;
        int nextIndex;
        float next;

        NoRemovedIterator() {
            int[] keys = this.keys = set;
            int nextI = keys.length;
            while (--nextI >= 0) {
                int key;
                if ((key = keys[nextI]) < FREE_BITS) {
                    next = Float.intBitsToFloat(key);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public float nextFloat() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int[] keys = this.keys;
                float prev = next;
                while (--nextI >= 0) {
                    int key;
                    if ((key = keys[nextI]) < FREE_BITS) {
                        next = Float.intBitsToFloat(key);
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
        public void forEachRemaining(Consumer<? super Float> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int[] keys = this.keys;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                int key;
                if ((key = keys[i]) < FREE_BITS) {
                    action.accept(Float.intBitsToFloat(key));
                }
            }
            if (nextI != nextIndex) {
                throw new java.util.ConcurrentModificationException();
            }
            nextIndex = -1;
        }

        @Override
        public void forEachRemaining(FloatConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int[] keys = this.keys;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                int key;
                if ((key = keys[i]) < FREE_BITS) {
                    action.accept(Float.intBitsToFloat(key));
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
        public Float next() {
            return nextFloat();
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedCursor implements FloatCursor {
        final int[] keys;
        int index;
        int curKey;

        NoRemovedCursor() {
            this.keys = set;
            index = keys.length;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(FloatConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int[] keys = this.keys;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = keys[i]) < FREE_BITS) {
                    action.accept(Float.intBitsToFloat(key));
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE_BITS;
        }

        @Override
        public float elem() {
            int curKey;
            if ((curKey = this.curKey) != FREE_BITS) {
                return Float.intBitsToFloat(curKey);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            int[] keys = this.keys;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = keys[i]) < FREE_BITS) {
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

