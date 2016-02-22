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
import net.openhft.koloboke.function.ByteConsumer;
import net.openhft.koloboke.function.BytePredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.impl.*;
import net.openhft.koloboke.collect.set.ByteSet;
import net.openhft.koloboke.collect.set.hash.HashByteSet;
import javax.annotation.Nonnull;

import java.util.*;


public abstract class ImmutableLHashParallelKVByteKeyMap
        extends ImmutableParallelKVByteLHashGO {



    public final boolean containsKey(Object key) {
        return contains(key);
    }

    public boolean containsKey(byte key) {
        return contains(key);
    }


    @Nonnull
    public HashByteSet keySet() {
        return new KeyView();
    }


    abstract boolean justRemove(byte key);


    class KeyView extends AbstractByteKeyView
            implements HashByteSet, InternalByteCollectionOps, ParallelKVByteLHash {


        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return ImmutableLHashParallelKVByteKeyMap.this.hashConfig();
        }

        @Override
        public HashConfigWrapper configWrapper() {
            return ImmutableLHashParallelKVByteKeyMap.this.configWrapper();
        }

        @Override
        public int size() {
            return ImmutableLHashParallelKVByteKeyMap.this.size();
        }

        @Override
        public double currentLoad() {
            return ImmutableLHashParallelKVByteKeyMap.this.currentLoad();
        }

        @Override
        public byte freeValue() {
            return ImmutableLHashParallelKVByteKeyMap.this.freeValue();
        }

        @Override
        public boolean supportRemoved() {
            return ImmutableLHashParallelKVByteKeyMap.this.supportRemoved();
        }

        @Override
        public byte removedValue() {
            return ImmutableLHashParallelKVByteKeyMap.this.removedValue();
        }

        @Nonnull
        @Override
        public char[] table() {
            return ImmutableLHashParallelKVByteKeyMap.this.table();
        }

        @Override
        public int capacity() {
            return ImmutableLHashParallelKVByteKeyMap.this.capacity();
        }

        @Override
        public int freeSlots() {
            return ImmutableLHashParallelKVByteKeyMap.this.freeSlots();
        }

        @Override
        public boolean noRemoved() {
            return ImmutableLHashParallelKVByteKeyMap.this.noRemoved();
        }

        @Override
        public int removedSlots() {
            return ImmutableLHashParallelKVByteKeyMap.this.removedSlots();
        }

        @Override
        public int modCount() {
            return ImmutableLHashParallelKVByteKeyMap.this.modCount();
        }

        @Override
        public final boolean contains(Object o) {
            return ImmutableLHashParallelKVByteKeyMap.this.contains(o);
        }

        @Override
        public boolean contains(byte key) {
            return ImmutableLHashParallelKVByteKeyMap.this.contains(key);
        }



        @Override
        public void forEach(Consumer<? super Byte> action) {
            ImmutableLHashParallelKVByteKeyMap.this.forEach(action);
        }

        @Override
        public void forEach(ByteConsumer action) {
            ImmutableLHashParallelKVByteKeyMap.this.forEach(action);
        }

        @Override
        public boolean forEachWhile(BytePredicate
                predicate) {
            return ImmutableLHashParallelKVByteKeyMap.this.forEachWhile(predicate);
        }

        @Override
        public boolean allContainingIn(ByteCollection c) {
            return ImmutableLHashParallelKVByteKeyMap.this.allContainingIn(c);
        }

        @Override
        public boolean reverseAddAllTo(ByteCollection c) {
            return ImmutableLHashParallelKVByteKeyMap.this.reverseAddAllTo(c);
        }

        @Override
        public boolean reverseRemoveAllFrom(ByteSet s) {
            return ImmutableLHashParallelKVByteKeyMap.this.reverseRemoveAllFrom(s);
        }

        @Override
        @Nonnull
        public ByteIterator iterator() {
            return ImmutableLHashParallelKVByteKeyMap.this.iterator();
        }

        @Override
        @Nonnull
        public ByteCursor cursor() {
            return setCursor();
        }

        @Override
        @Nonnull
        public Object[] toArray() {
            return ImmutableLHashParallelKVByteKeyMap.this.toArray();
        }

        @Override
        @Nonnull
        public <T> T[] toArray(@Nonnull T[] a) {
            return ImmutableLHashParallelKVByteKeyMap.this.toArray(a);
        }

        @Override
        public byte[] toByteArray() {
            return ImmutableLHashParallelKVByteKeyMap.this.toByteArray();
        }

        @Override
        public byte[] toArray(byte[] a) {
            return ImmutableLHashParallelKVByteKeyMap.this.toArray(a);
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
            return ImmutableLHashParallelKVByteKeyMap.this.shrink();
        }


        @Override
        public final boolean remove(Object o) {
            return justRemove((Byte) o);
        }

        @Override
        public boolean removeByte(byte v) {
            return justRemove(v);
        }



        @Override
        public boolean removeIf(Predicate<? super Byte> filter) {
            return ImmutableLHashParallelKVByteKeyMap.this.removeIf(filter);
        }

        @Override
        public boolean removeIf(BytePredicate filter) {
            return ImmutableLHashParallelKVByteKeyMap.this.removeIf(filter);
        }

        @Override
        public boolean removeAll(@Nonnull Collection<?> c) {
            if (c instanceof ByteCollection) {
                if (c instanceof InternalByteCollectionOps) {
                    InternalByteCollectionOps c2 = (InternalByteCollectionOps) c;
                    if (c2.size() < this.size()) {
                        
                        return c2.reverseRemoveAllFrom(this);
                    }
                }
                return ImmutableLHashParallelKVByteKeyMap.this.removeAll(this, (ByteCollection) c);
            }
            return ImmutableLHashParallelKVByteKeyMap.this.removeAll(this, c);
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> c) {
            return ImmutableLHashParallelKVByteKeyMap.this.retainAll(this, c);
        }

        @Override
        public void clear() {
            ImmutableLHashParallelKVByteKeyMap.this.clear();
        }
    }
}

