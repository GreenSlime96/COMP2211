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


public abstract class MutableLHashParallelKVByteKeyMap
        extends MutableParallelKVByteLHashGO {



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
            return MutableLHashParallelKVByteKeyMap.this.hashConfig();
        }

        @Override
        public HashConfigWrapper configWrapper() {
            return MutableLHashParallelKVByteKeyMap.this.configWrapper();
        }

        @Override
        public int size() {
            return MutableLHashParallelKVByteKeyMap.this.size();
        }

        @Override
        public double currentLoad() {
            return MutableLHashParallelKVByteKeyMap.this.currentLoad();
        }

        @Override
        public byte freeValue() {
            return MutableLHashParallelKVByteKeyMap.this.freeValue();
        }

        @Override
        public boolean supportRemoved() {
            return MutableLHashParallelKVByteKeyMap.this.supportRemoved();
        }

        @Override
        public byte removedValue() {
            return MutableLHashParallelKVByteKeyMap.this.removedValue();
        }

        @Nonnull
        @Override
        public char[] table() {
            return MutableLHashParallelKVByteKeyMap.this.table();
        }

        @Override
        public int capacity() {
            return MutableLHashParallelKVByteKeyMap.this.capacity();
        }

        @Override
        public int freeSlots() {
            return MutableLHashParallelKVByteKeyMap.this.freeSlots();
        }

        @Override
        public boolean noRemoved() {
            return MutableLHashParallelKVByteKeyMap.this.noRemoved();
        }

        @Override
        public int removedSlots() {
            return MutableLHashParallelKVByteKeyMap.this.removedSlots();
        }

        @Override
        public int modCount() {
            return MutableLHashParallelKVByteKeyMap.this.modCount();
        }

        @Override
        public final boolean contains(Object o) {
            return MutableLHashParallelKVByteKeyMap.this.contains(o);
        }

        @Override
        public boolean contains(byte key) {
            return MutableLHashParallelKVByteKeyMap.this.contains(key);
        }



        @Override
        public void forEach(Consumer<? super Byte> action) {
            MutableLHashParallelKVByteKeyMap.this.forEach(action);
        }

        @Override
        public void forEach(ByteConsumer action) {
            MutableLHashParallelKVByteKeyMap.this.forEach(action);
        }

        @Override
        public boolean forEachWhile(BytePredicate
                predicate) {
            return MutableLHashParallelKVByteKeyMap.this.forEachWhile(predicate);
        }

        @Override
        public boolean allContainingIn(ByteCollection c) {
            return MutableLHashParallelKVByteKeyMap.this.allContainingIn(c);
        }

        @Override
        public boolean reverseAddAllTo(ByteCollection c) {
            return MutableLHashParallelKVByteKeyMap.this.reverseAddAllTo(c);
        }

        @Override
        public boolean reverseRemoveAllFrom(ByteSet s) {
            return MutableLHashParallelKVByteKeyMap.this.reverseRemoveAllFrom(s);
        }

        @Override
        @Nonnull
        public ByteIterator iterator() {
            return MutableLHashParallelKVByteKeyMap.this.iterator();
        }

        @Override
        @Nonnull
        public ByteCursor cursor() {
            return setCursor();
        }

        @Override
        @Nonnull
        public Object[] toArray() {
            return MutableLHashParallelKVByteKeyMap.this.toArray();
        }

        @Override
        @Nonnull
        public <T> T[] toArray(@Nonnull T[] a) {
            return MutableLHashParallelKVByteKeyMap.this.toArray(a);
        }

        @Override
        public byte[] toByteArray() {
            return MutableLHashParallelKVByteKeyMap.this.toByteArray();
        }

        @Override
        public byte[] toArray(byte[] a) {
            return MutableLHashParallelKVByteKeyMap.this.toArray(a);
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
            return MutableLHashParallelKVByteKeyMap.this.shrink();
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
            return MutableLHashParallelKVByteKeyMap.this.removeIf(filter);
        }

        @Override
        public boolean removeIf(BytePredicate filter) {
            return MutableLHashParallelKVByteKeyMap.this.removeIf(filter);
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
                return MutableLHashParallelKVByteKeyMap.this.removeAll(this, (ByteCollection) c);
            }
            return MutableLHashParallelKVByteKeyMap.this.removeAll(this, c);
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> c) {
            return MutableLHashParallelKVByteKeyMap.this.retainAll(this, c);
        }

        @Override
        public void clear() {
            MutableLHashParallelKVByteKeyMap.this.clear();
        }
    }
}

