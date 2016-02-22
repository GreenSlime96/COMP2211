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
import net.openhft.koloboke.function.FloatConsumer;
import net.openhft.koloboke.function.FloatPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.impl.*;
import net.openhft.koloboke.collect.set.FloatSet;
import net.openhft.koloboke.collect.set.hash.HashFloatSet;
import javax.annotation.Nonnull;

import java.util.*;


public abstract class ImmutableQHashParallelKVFloatKeyMap
        extends ImmutableParallelKVFloatQHashGO {



    public final boolean containsKey(Object key) {
        return contains(key);
    }

    public boolean containsKey(float key) {
        return contains(key);
    }


    @Nonnull
    public HashFloatSet keySet() {
        return new KeyView();
    }


    abstract boolean justRemove(float key);

    @Override
    abstract boolean justRemove(int key);

    class KeyView extends AbstractFloatKeyView
            implements HashFloatSet, InternalFloatCollectionOps, ParallelKVFloatQHash {


        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return ImmutableQHashParallelKVFloatKeyMap.this.hashConfig();
        }

        @Override
        public HashConfigWrapper configWrapper() {
            return ImmutableQHashParallelKVFloatKeyMap.this.configWrapper();
        }

        @Override
        public int size() {
            return ImmutableQHashParallelKVFloatKeyMap.this.size();
        }

        @Override
        public double currentLoad() {
            return ImmutableQHashParallelKVFloatKeyMap.this.currentLoad();
        }


        @Nonnull
        @Override
        public long[] table() {
            return ImmutableQHashParallelKVFloatKeyMap.this.table();
        }

        @Override
        public int capacity() {
            return ImmutableQHashParallelKVFloatKeyMap.this.capacity();
        }

        @Override
        public int freeSlots() {
            return ImmutableQHashParallelKVFloatKeyMap.this.freeSlots();
        }

        @Override
        public boolean noRemoved() {
            return ImmutableQHashParallelKVFloatKeyMap.this.noRemoved();
        }

        @Override
        public int removedSlots() {
            return ImmutableQHashParallelKVFloatKeyMap.this.removedSlots();
        }

        @Override
        public int modCount() {
            return ImmutableQHashParallelKVFloatKeyMap.this.modCount();
        }

        @Override
        public final boolean contains(Object o) {
            return ImmutableQHashParallelKVFloatKeyMap.this.contains(o);
        }

        @Override
        public boolean contains(float key) {
            return ImmutableQHashParallelKVFloatKeyMap.this.contains(key);
        }

        @Override
        public boolean contains(int bits) {
            return ImmutableQHashParallelKVFloatKeyMap.this.contains(bits);
        }


        @Override
        public void forEach(Consumer<? super Float> action) {
            ImmutableQHashParallelKVFloatKeyMap.this.forEach(action);
        }

        @Override
        public void forEach(FloatConsumer action) {
            ImmutableQHashParallelKVFloatKeyMap.this.forEach(action);
        }

        @Override
        public boolean forEachWhile(FloatPredicate
                predicate) {
            return ImmutableQHashParallelKVFloatKeyMap.this.forEachWhile(predicate);
        }

        @Override
        public boolean allContainingIn(FloatCollection c) {
            return ImmutableQHashParallelKVFloatKeyMap.this.allContainingIn(c);
        }

        @Override
        public boolean reverseAddAllTo(FloatCollection c) {
            return ImmutableQHashParallelKVFloatKeyMap.this.reverseAddAllTo(c);
        }

        @Override
        public boolean reverseRemoveAllFrom(FloatSet s) {
            return ImmutableQHashParallelKVFloatKeyMap.this.reverseRemoveAllFrom(s);
        }

        @Override
        @Nonnull
        public FloatIterator iterator() {
            return ImmutableQHashParallelKVFloatKeyMap.this.iterator();
        }

        @Override
        @Nonnull
        public FloatCursor cursor() {
            return setCursor();
        }

        @Override
        @Nonnull
        public Object[] toArray() {
            return ImmutableQHashParallelKVFloatKeyMap.this.toArray();
        }

        @Override
        @Nonnull
        public <T> T[] toArray(@Nonnull T[] a) {
            return ImmutableQHashParallelKVFloatKeyMap.this.toArray(a);
        }

        @Override
        public float[] toFloatArray() {
            return ImmutableQHashParallelKVFloatKeyMap.this.toFloatArray();
        }

        @Override
        public float[] toArray(float[] a) {
            return ImmutableQHashParallelKVFloatKeyMap.this.toArray(a);
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
            return ImmutableQHashParallelKVFloatKeyMap.this.shrink();
        }


        @Override
        public final boolean remove(Object o) {
            return justRemove((Float) o);
        }

        @Override
        public boolean removeFloat(float v) {
            return justRemove(v);
        }

        @Override
        public boolean removeFloat(int bits) {
            return justRemove(bits);
        }


        @Override
        public boolean removeIf(Predicate<? super Float> filter) {
            return ImmutableQHashParallelKVFloatKeyMap.this.removeIf(filter);
        }

        @Override
        public boolean removeIf(FloatPredicate filter) {
            return ImmutableQHashParallelKVFloatKeyMap.this.removeIf(filter);
        }

        @Override
        public boolean removeAll(@Nonnull Collection<?> c) {
            if (c instanceof FloatCollection) {
                if (c instanceof InternalFloatCollectionOps) {
                    InternalFloatCollectionOps c2 = (InternalFloatCollectionOps) c;
                    if (c2.size() < this.size()) {
                        
                        return c2.reverseRemoveAllFrom(this);
                    }
                }
                return ImmutableQHashParallelKVFloatKeyMap.this.removeAll(this, (FloatCollection) c);
            }
            return ImmutableQHashParallelKVFloatKeyMap.this.removeAll(this, c);
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> c) {
            return ImmutableQHashParallelKVFloatKeyMap.this.retainAll(this, c);
        }

        @Override
        public void clear() {
            ImmutableQHashParallelKVFloatKeyMap.this.clear();
        }
    }
}

