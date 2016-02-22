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
import net.openhft.koloboke.collect.hash.HashConfig;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.impl.*;
import net.openhft.koloboke.collect.set.IntSet;
import net.openhft.koloboke.collect.set.hash.HashIntSet;
import javax.annotation.Nonnull;

import java.util.*;


public abstract class ImmutableLHashParallelKVIntKeyMap
        extends ImmutableParallelKVIntLHashGO {



    public final boolean containsKey(Object key) {
        return contains(key);
    }

    public boolean containsKey(int key) {
        return contains(key);
    }


    @Nonnull
    public HashIntSet keySet() {
        return new KeyView();
    }


    abstract boolean justRemove(int key);


    class KeyView extends AbstractIntKeyView
            implements HashIntSet, InternalIntCollectionOps, ParallelKVIntLHash {


        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return ImmutableLHashParallelKVIntKeyMap.this.hashConfig();
        }

        @Override
        public HashConfigWrapper configWrapper() {
            return ImmutableLHashParallelKVIntKeyMap.this.configWrapper();
        }

        @Override
        public int size() {
            return ImmutableLHashParallelKVIntKeyMap.this.size();
        }

        @Override
        public double currentLoad() {
            return ImmutableLHashParallelKVIntKeyMap.this.currentLoad();
        }

        @Override
        public int freeValue() {
            return ImmutableLHashParallelKVIntKeyMap.this.freeValue();
        }

        @Override
        public boolean supportRemoved() {
            return ImmutableLHashParallelKVIntKeyMap.this.supportRemoved();
        }

        @Override
        public int removedValue() {
            return ImmutableLHashParallelKVIntKeyMap.this.removedValue();
        }

        @Nonnull
        @Override
        public long[] table() {
            return ImmutableLHashParallelKVIntKeyMap.this.table();
        }

        @Override
        public int capacity() {
            return ImmutableLHashParallelKVIntKeyMap.this.capacity();
        }

        @Override
        public int freeSlots() {
            return ImmutableLHashParallelKVIntKeyMap.this.freeSlots();
        }

        @Override
        public boolean noRemoved() {
            return ImmutableLHashParallelKVIntKeyMap.this.noRemoved();
        }

        @Override
        public int removedSlots() {
            return ImmutableLHashParallelKVIntKeyMap.this.removedSlots();
        }

        @Override
        public int modCount() {
            return ImmutableLHashParallelKVIntKeyMap.this.modCount();
        }

        @Override
        public final boolean contains(Object o) {
            return ImmutableLHashParallelKVIntKeyMap.this.contains(o);
        }

        @Override
        public boolean contains(int key) {
            return ImmutableLHashParallelKVIntKeyMap.this.contains(key);
        }



        @Override
        public void forEach(Consumer<? super Integer> action) {
            ImmutableLHashParallelKVIntKeyMap.this.forEach(action);
        }

        @Override
        public void forEach(IntConsumer action) {
            ImmutableLHashParallelKVIntKeyMap.this.forEach(action);
        }

        @Override
        public boolean forEachWhile(IntPredicate
                predicate) {
            return ImmutableLHashParallelKVIntKeyMap.this.forEachWhile(predicate);
        }

        @Override
        public boolean allContainingIn(IntCollection c) {
            return ImmutableLHashParallelKVIntKeyMap.this.allContainingIn(c);
        }

        @Override
        public boolean reverseAddAllTo(IntCollection c) {
            return ImmutableLHashParallelKVIntKeyMap.this.reverseAddAllTo(c);
        }

        @Override
        public boolean reverseRemoveAllFrom(IntSet s) {
            return ImmutableLHashParallelKVIntKeyMap.this.reverseRemoveAllFrom(s);
        }

        @Override
        @Nonnull
        public IntIterator iterator() {
            return ImmutableLHashParallelKVIntKeyMap.this.iterator();
        }

        @Override
        @Nonnull
        public IntCursor cursor() {
            return setCursor();
        }

        @Override
        @Nonnull
        public Object[] toArray() {
            return ImmutableLHashParallelKVIntKeyMap.this.toArray();
        }

        @Override
        @Nonnull
        public <T> T[] toArray(@Nonnull T[] a) {
            return ImmutableLHashParallelKVIntKeyMap.this.toArray(a);
        }

        @Override
        public int[] toIntArray() {
            return ImmutableLHashParallelKVIntKeyMap.this.toIntArray();
        }

        @Override
        public int[] toArray(int[] a) {
            return ImmutableLHashParallelKVIntKeyMap.this.toArray(a);
        }


        @Override
        public int hashCode() {
            return setHashCode();
        }

        @Override
        public String toString() {
            return setToString();
        }


        @Override
        public boolean shrink() {
            return ImmutableLHashParallelKVIntKeyMap.this.shrink();
        }


        @Override
        public final boolean remove(Object o) {
            return justRemove((Integer) o);
        }

        @Override
        public boolean removeInt(int v) {
            return justRemove(v);
        }



        @Override
        public boolean removeIf(Predicate<? super Integer> filter) {
            return ImmutableLHashParallelKVIntKeyMap.this.removeIf(filter);
        }

        @Override
        public boolean removeIf(IntPredicate filter) {
            return ImmutableLHashParallelKVIntKeyMap.this.removeIf(filter);
        }

        @Override
        public boolean removeAll(@Nonnull Collection<?> c) {
            if (c instanceof IntCollection) {
                if (c instanceof InternalIntCollectionOps) {
                    InternalIntCollectionOps c2 = (InternalIntCollectionOps) c;
                    if (c2.size() < this.size()) {
                        
                        return c2.reverseRemoveAllFrom(this);
                    }
                }
                return ImmutableLHashParallelKVIntKeyMap.this.removeAll(this, (IntCollection) c);
            }
            return ImmutableLHashParallelKVIntKeyMap.this.removeAll(this, c);
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> c) {
            return ImmutableLHashParallelKVIntKeyMap.this.retainAll(this, c);
        }

        @Override
        public void clear() {
            ImmutableLHashParallelKVIntKeyMap.this.clear();
        }
    }
}

