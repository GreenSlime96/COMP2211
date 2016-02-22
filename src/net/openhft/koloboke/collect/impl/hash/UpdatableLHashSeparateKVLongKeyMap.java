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


public abstract class UpdatableLHashSeparateKVLongKeyMap
        extends UpdatableSeparateKVLongLHashGO {



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
            implements HashLongSet, InternalLongCollectionOps, SeparateKVLongLHash {


        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return UpdatableLHashSeparateKVLongKeyMap.this.hashConfig();
        }

        @Override
        public HashConfigWrapper configWrapper() {
            return UpdatableLHashSeparateKVLongKeyMap.this.configWrapper();
        }

        @Override
        public int size() {
            return UpdatableLHashSeparateKVLongKeyMap.this.size();
        }

        @Override
        public double currentLoad() {
            return UpdatableLHashSeparateKVLongKeyMap.this.currentLoad();
        }

        @Override
        public long freeValue() {
            return UpdatableLHashSeparateKVLongKeyMap.this.freeValue();
        }

        @Override
        public boolean supportRemoved() {
            return UpdatableLHashSeparateKVLongKeyMap.this.supportRemoved();
        }

        @Override
        public long removedValue() {
            return UpdatableLHashSeparateKVLongKeyMap.this.removedValue();
        }

        @Nonnull
        @Override
        public long[] keys() {
            return UpdatableLHashSeparateKVLongKeyMap.this.keys();
        }

        @Override
        public int capacity() {
            return UpdatableLHashSeparateKVLongKeyMap.this.capacity();
        }

        @Override
        public int freeSlots() {
            return UpdatableLHashSeparateKVLongKeyMap.this.freeSlots();
        }

        @Override
        public boolean noRemoved() {
            return UpdatableLHashSeparateKVLongKeyMap.this.noRemoved();
        }

        @Override
        public int removedSlots() {
            return UpdatableLHashSeparateKVLongKeyMap.this.removedSlots();
        }

        @Override
        public int modCount() {
            return UpdatableLHashSeparateKVLongKeyMap.this.modCount();
        }

        @Override
        public final boolean contains(Object o) {
            return UpdatableLHashSeparateKVLongKeyMap.this.contains(o);
        }

        @Override
        public boolean contains(long key) {
            return UpdatableLHashSeparateKVLongKeyMap.this.contains(key);
        }



        @Override
        public void forEach(Consumer<? super Long> action) {
            UpdatableLHashSeparateKVLongKeyMap.this.forEach(action);
        }

        @Override
        public void forEach(LongConsumer action) {
            UpdatableLHashSeparateKVLongKeyMap.this.forEach(action);
        }

        @Override
        public boolean forEachWhile(LongPredicate
                predicate) {
            return UpdatableLHashSeparateKVLongKeyMap.this.forEachWhile(predicate);
        }

        @Override
        public boolean allContainingIn(LongCollection c) {
            return UpdatableLHashSeparateKVLongKeyMap.this.allContainingIn(c);
        }

        @Override
        public boolean reverseAddAllTo(LongCollection c) {
            return UpdatableLHashSeparateKVLongKeyMap.this.reverseAddAllTo(c);
        }

        @Override
        public boolean reverseRemoveAllFrom(LongSet s) {
            return UpdatableLHashSeparateKVLongKeyMap.this.reverseRemoveAllFrom(s);
        }

        @Override
        @Nonnull
        public LongIterator iterator() {
            return UpdatableLHashSeparateKVLongKeyMap.this.iterator();
        }

        @Override
        @Nonnull
        public LongCursor cursor() {
            return setCursor();
        }

        @Override
        @Nonnull
        public Object[] toArray() {
            return UpdatableLHashSeparateKVLongKeyMap.this.toArray();
        }

        @Override
        @Nonnull
        public <T> T[] toArray(@Nonnull T[] a) {
            return UpdatableLHashSeparateKVLongKeyMap.this.toArray(a);
        }

        @Override
        public long[] toLongArray() {
            return UpdatableLHashSeparateKVLongKeyMap.this.toLongArray();
        }

        @Override
        public long[] toArray(long[] a) {
            return UpdatableLHashSeparateKVLongKeyMap.this.toArray(a);
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
            return UpdatableLHashSeparateKVLongKeyMap.this.shrink();
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
            return UpdatableLHashSeparateKVLongKeyMap.this.removeIf(filter);
        }

        @Override
        public boolean removeIf(LongPredicate filter) {
            return UpdatableLHashSeparateKVLongKeyMap.this.removeIf(filter);
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
                return UpdatableLHashSeparateKVLongKeyMap.this.removeAll(this, (LongCollection) c);
            }
            return UpdatableLHashSeparateKVLongKeyMap.this.removeAll(this, c);
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> c) {
            return UpdatableLHashSeparateKVLongKeyMap.this.retainAll(this, c);
        }

        @Override
        public void clear() {
            UpdatableLHashSeparateKVLongKeyMap.this.clear();
        }
    }
}

