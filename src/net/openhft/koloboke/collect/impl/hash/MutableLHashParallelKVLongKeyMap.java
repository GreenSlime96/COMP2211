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


public abstract class MutableLHashParallelKVLongKeyMap
        extends MutableParallelKVLongLHashGO {



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
            implements HashLongSet, InternalLongCollectionOps, ParallelKVLongLHash {


        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return MutableLHashParallelKVLongKeyMap.this.hashConfig();
        }

        @Override
        public HashConfigWrapper configWrapper() {
            return MutableLHashParallelKVLongKeyMap.this.configWrapper();
        }

        @Override
        public int size() {
            return MutableLHashParallelKVLongKeyMap.this.size();
        }

        @Override
        public double currentLoad() {
            return MutableLHashParallelKVLongKeyMap.this.currentLoad();
        }

        @Override
        public long freeValue() {
            return MutableLHashParallelKVLongKeyMap.this.freeValue();
        }

        @Override
        public boolean supportRemoved() {
            return MutableLHashParallelKVLongKeyMap.this.supportRemoved();
        }

        @Override
        public long removedValue() {
            return MutableLHashParallelKVLongKeyMap.this.removedValue();
        }

        @Nonnull
        @Override
        public long[] table() {
            return MutableLHashParallelKVLongKeyMap.this.table();
        }

        @Override
        public int capacity() {
            return MutableLHashParallelKVLongKeyMap.this.capacity();
        }

        @Override
        public int freeSlots() {
            return MutableLHashParallelKVLongKeyMap.this.freeSlots();
        }

        @Override
        public boolean noRemoved() {
            return MutableLHashParallelKVLongKeyMap.this.noRemoved();
        }

        @Override
        public int removedSlots() {
            return MutableLHashParallelKVLongKeyMap.this.removedSlots();
        }

        @Override
        public int modCount() {
            return MutableLHashParallelKVLongKeyMap.this.modCount();
        }

        @Override
        public final boolean contains(Object o) {
            return MutableLHashParallelKVLongKeyMap.this.contains(o);
        }

        @Override
        public boolean contains(long key) {
            return MutableLHashParallelKVLongKeyMap.this.contains(key);
        }



        @Override
        public void forEach(Consumer<? super Long> action) {
            MutableLHashParallelKVLongKeyMap.this.forEach(action);
        }

        @Override
        public void forEach(LongConsumer action) {
            MutableLHashParallelKVLongKeyMap.this.forEach(action);
        }

        @Override
        public boolean forEachWhile(LongPredicate
                predicate) {
            return MutableLHashParallelKVLongKeyMap.this.forEachWhile(predicate);
        }

        @Override
        public boolean allContainingIn(LongCollection c) {
            return MutableLHashParallelKVLongKeyMap.this.allContainingIn(c);
        }

        @Override
        public boolean reverseAddAllTo(LongCollection c) {
            return MutableLHashParallelKVLongKeyMap.this.reverseAddAllTo(c);
        }

        @Override
        public boolean reverseRemoveAllFrom(LongSet s) {
            return MutableLHashParallelKVLongKeyMap.this.reverseRemoveAllFrom(s);
        }

        @Override
        @Nonnull
        public LongIterator iterator() {
            return MutableLHashParallelKVLongKeyMap.this.iterator();
        }

        @Override
        @Nonnull
        public LongCursor cursor() {
            return setCursor();
        }

        @Override
        @Nonnull
        public Object[] toArray() {
            return MutableLHashParallelKVLongKeyMap.this.toArray();
        }

        @Override
        @Nonnull
        public <T> T[] toArray(@Nonnull T[] a) {
            return MutableLHashParallelKVLongKeyMap.this.toArray(a);
        }

        @Override
        public long[] toLongArray() {
            return MutableLHashParallelKVLongKeyMap.this.toLongArray();
        }

        @Override
        public long[] toArray(long[] a) {
            return MutableLHashParallelKVLongKeyMap.this.toArray(a);
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
            return MutableLHashParallelKVLongKeyMap.this.shrink();
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
            return MutableLHashParallelKVLongKeyMap.this.removeIf(filter);
        }

        @Override
        public boolean removeIf(LongPredicate filter) {
            return MutableLHashParallelKVLongKeyMap.this.removeIf(filter);
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
                return MutableLHashParallelKVLongKeyMap.this.removeAll(this, (LongCollection) c);
            }
            return MutableLHashParallelKVLongKeyMap.this.removeAll(this, c);
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> c) {
            return MutableLHashParallelKVLongKeyMap.this.retainAll(this, c);
        }

        @Override
        public void clear() {
            MutableLHashParallelKVLongKeyMap.this.clear();
        }
    }
}

