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
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.impl.*;
import net.openhft.koloboke.collect.set.LongSet;
import net.openhft.koloboke.collect.set.hash.HashLongSet;
import javax.annotation.Nonnull;

import java.util.*;


public abstract class UpdatableQHashParallelKVLongKeyMap
        extends UpdatableParallelKVLongQHashGO {



    public final boolean containsKey(Object key) {
        return contains(key);
    }

    public boolean containsKey(long key) {
        return contains(key);
    }


    @Nonnull
    public HashLongSet keySet() {
        return new KeyView();
    }


    abstract boolean justRemove(long key);


    class KeyView extends AbstractLongKeyView
            implements HashLongSet, InternalLongCollectionOps, ParallelKVLongQHash {


        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return UpdatableQHashParallelKVLongKeyMap.this.hashConfig();
        }

        @Override
        public HashConfigWrapper configWrapper() {
            return UpdatableQHashParallelKVLongKeyMap.this.configWrapper();
        }

        @Override
        public int size() {
            return UpdatableQHashParallelKVLongKeyMap.this.size();
        }

        @Override
        public double currentLoad() {
            return UpdatableQHashParallelKVLongKeyMap.this.currentLoad();
        }

        @Override
        public long freeValue() {
            return UpdatableQHashParallelKVLongKeyMap.this.freeValue();
        }

        @Override
        public boolean supportRemoved() {
            return UpdatableQHashParallelKVLongKeyMap.this.supportRemoved();
        }

        @Override
        public long removedValue() {
            return UpdatableQHashParallelKVLongKeyMap.this.removedValue();
        }

        @Nonnull
        @Override
        public long[] table() {
            return UpdatableQHashParallelKVLongKeyMap.this.table();
        }

        @Override
        public int capacity() {
            return UpdatableQHashParallelKVLongKeyMap.this.capacity();
        }

        @Override
        public int freeSlots() {
            return UpdatableQHashParallelKVLongKeyMap.this.freeSlots();
        }

        @Override
        public boolean noRemoved() {
            return UpdatableQHashParallelKVLongKeyMap.this.noRemoved();
        }

        @Override
        public int removedSlots() {
            return UpdatableQHashParallelKVLongKeyMap.this.removedSlots();
        }

        @Override
        public int modCount() {
            return UpdatableQHashParallelKVLongKeyMap.this.modCount();
        }

        @Override
        public final boolean contains(Object o) {
            return UpdatableQHashParallelKVLongKeyMap.this.contains(o);
        }

        @Override
        public boolean contains(long key) {
            return UpdatableQHashParallelKVLongKeyMap.this.contains(key);
        }



        @Override
        public void forEach(Consumer<? super Long> action) {
            UpdatableQHashParallelKVLongKeyMap.this.forEach(action);
        }

        @Override
        public void forEach(LongConsumer action) {
            UpdatableQHashParallelKVLongKeyMap.this.forEach(action);
        }

        @Override
        public boolean forEachWhile(LongPredicate
                predicate) {
            return UpdatableQHashParallelKVLongKeyMap.this.forEachWhile(predicate);
        }

        @Override
        public boolean allContainingIn(LongCollection c) {
            return UpdatableQHashParallelKVLongKeyMap.this.allContainingIn(c);
        }

        @Override
        public boolean reverseAddAllTo(LongCollection c) {
            return UpdatableQHashParallelKVLongKeyMap.this.reverseAddAllTo(c);
        }

        @Override
        public boolean reverseRemoveAllFrom(LongSet s) {
            return UpdatableQHashParallelKVLongKeyMap.this.reverseRemoveAllFrom(s);
        }

        @Override
        @Nonnull
        public LongIterator iterator() {
            return UpdatableQHashParallelKVLongKeyMap.this.iterator();
        }

        @Override
        @Nonnull
        public LongCursor cursor() {
            return setCursor();
        }

        @Override
        @Nonnull
        public Object[] toArray() {
            return UpdatableQHashParallelKVLongKeyMap.this.toArray();
        }

        @Override
        @Nonnull
        public <T> T[] toArray(@Nonnull T[] a) {
            return UpdatableQHashParallelKVLongKeyMap.this.toArray(a);
        }

        @Override
        public long[] toLongArray() {
            return UpdatableQHashParallelKVLongKeyMap.this.toLongArray();
        }

        @Override
        public long[] toArray(long[] a) {
            return UpdatableQHashParallelKVLongKeyMap.this.toArray(a);
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
            return UpdatableQHashParallelKVLongKeyMap.this.shrink();
        }


        @Override
        public final boolean remove(Object o) {
            return justRemove((Long) o);
        }

        @Override
        public boolean removeLong(long v) {
            return justRemove(v);
        }



        @Override
        public boolean removeIf(Predicate<? super Long> filter) {
            return UpdatableQHashParallelKVLongKeyMap.this.removeIf(filter);
        }

        @Override
        public boolean removeIf(LongPredicate filter) {
            return UpdatableQHashParallelKVLongKeyMap.this.removeIf(filter);
        }

        @Override
        public boolean removeAll(@Nonnull Collection<?> c) {
            if (c instanceof LongCollection) {
                if (c instanceof InternalLongCollectionOps) {
                    InternalLongCollectionOps c2 = (InternalLongCollectionOps) c;
                    if (c2.size() < this.size()) {
                        
                        return c2.reverseRemoveAllFrom(this);
                    }
                }
                return UpdatableQHashParallelKVLongKeyMap.this.removeAll(this, (LongCollection) c);
            }
            return UpdatableQHashParallelKVLongKeyMap.this.removeAll(this, c);
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> c) {
            return UpdatableQHashParallelKVLongKeyMap.this.retainAll(this, c);
        }

        @Override
        public void clear() {
            UpdatableQHashParallelKVLongKeyMap.this.clear();
        }
    }
}

