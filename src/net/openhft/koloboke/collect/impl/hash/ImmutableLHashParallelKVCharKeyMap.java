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
import net.openhft.koloboke.function.CharConsumer;
import net.openhft.koloboke.function.CharPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.impl.*;
import net.openhft.koloboke.collect.set.CharSet;
import net.openhft.koloboke.collect.set.hash.HashCharSet;
import javax.annotation.Nonnull;

import java.util.*;


public abstract class ImmutableLHashParallelKVCharKeyMap
        extends ImmutableParallelKVCharLHashGO {



    public final boolean containsKey(Object key) {
        return contains(key);
    }

    public boolean containsKey(char key) {
        return contains(key);
    }


    @Nonnull
    public HashCharSet keySet() {
        return new KeyView();
    }


    abstract boolean justRemove(char key);


    class KeyView extends AbstractCharKeyView
            implements HashCharSet, InternalCharCollectionOps, ParallelKVCharLHash {


        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return ImmutableLHashParallelKVCharKeyMap.this.hashConfig();
        }

        @Override
        public HashConfigWrapper configWrapper() {
            return ImmutableLHashParallelKVCharKeyMap.this.configWrapper();
        }

        @Override
        public int size() {
            return ImmutableLHashParallelKVCharKeyMap.this.size();
        }

        @Override
        public double currentLoad() {
            return ImmutableLHashParallelKVCharKeyMap.this.currentLoad();
        }

        @Override
        public char freeValue() {
            return ImmutableLHashParallelKVCharKeyMap.this.freeValue();
        }

        @Override
        public boolean supportRemoved() {
            return ImmutableLHashParallelKVCharKeyMap.this.supportRemoved();
        }

        @Override
        public char removedValue() {
            return ImmutableLHashParallelKVCharKeyMap.this.removedValue();
        }

        @Nonnull
        @Override
        public int[] table() {
            return ImmutableLHashParallelKVCharKeyMap.this.table();
        }

        @Override
        public int capacity() {
            return ImmutableLHashParallelKVCharKeyMap.this.capacity();
        }

        @Override
        public int freeSlots() {
            return ImmutableLHashParallelKVCharKeyMap.this.freeSlots();
        }

        @Override
        public boolean noRemoved() {
            return ImmutableLHashParallelKVCharKeyMap.this.noRemoved();
        }

        @Override
        public int removedSlots() {
            return ImmutableLHashParallelKVCharKeyMap.this.removedSlots();
        }

        @Override
        public int modCount() {
            return ImmutableLHashParallelKVCharKeyMap.this.modCount();
        }

        @Override
        public final boolean contains(Object o) {
            return ImmutableLHashParallelKVCharKeyMap.this.contains(o);
        }

        @Override
        public boolean contains(char key) {
            return ImmutableLHashParallelKVCharKeyMap.this.contains(key);
        }



        @Override
        public void forEach(Consumer<? super Character> action) {
            ImmutableLHashParallelKVCharKeyMap.this.forEach(action);
        }

        @Override
        public void forEach(CharConsumer action) {
            ImmutableLHashParallelKVCharKeyMap.this.forEach(action);
        }

        @Override
        public boolean forEachWhile(CharPredicate
                predicate) {
            return ImmutableLHashParallelKVCharKeyMap.this.forEachWhile(predicate);
        }

        @Override
        public boolean allContainingIn(CharCollection c) {
            return ImmutableLHashParallelKVCharKeyMap.this.allContainingIn(c);
        }

        @Override
        public boolean reverseAddAllTo(CharCollection c) {
            return ImmutableLHashParallelKVCharKeyMap.this.reverseAddAllTo(c);
        }

        @Override
        public boolean reverseRemoveAllFrom(CharSet s) {
            return ImmutableLHashParallelKVCharKeyMap.this.reverseRemoveAllFrom(s);
        }

        @Override
        @Nonnull
        public CharIterator iterator() {
            return ImmutableLHashParallelKVCharKeyMap.this.iterator();
        }

        @Override
        @Nonnull
        public CharCursor cursor() {
            return setCursor();
        }

        @Override
        @Nonnull
        public Object[] toArray() {
            return ImmutableLHashParallelKVCharKeyMap.this.toArray();
        }

        @Override
        @Nonnull
        public <T> T[] toArray(@Nonnull T[] a) {
            return ImmutableLHashParallelKVCharKeyMap.this.toArray(a);
        }

        @Override
        public char[] toCharArray() {
            return ImmutableLHashParallelKVCharKeyMap.this.toCharArray();
        }

        @Override
        public char[] toArray(char[] a) {
            return ImmutableLHashParallelKVCharKeyMap.this.toArray(a);
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
            return ImmutableLHashParallelKVCharKeyMap.this.shrink();
        }


        @Override
        public final boolean remove(Object o) {
            return justRemove((Character) o);
        }

        @Override
        public boolean removeChar(char v) {
            return justRemove(v);
        }



        @Override
        public boolean removeIf(Predicate<? super Character> filter) {
            return ImmutableLHashParallelKVCharKeyMap.this.removeIf(filter);
        }

        @Override
        public boolean removeIf(CharPredicate filter) {
            return ImmutableLHashParallelKVCharKeyMap.this.removeIf(filter);
        }

        @Override
        public boolean removeAll(@Nonnull Collection<?> c) {
            if (c instanceof CharCollection) {
                if (c instanceof InternalCharCollectionOps) {
                    InternalCharCollectionOps c2 = (InternalCharCollectionOps) c;
                    if (c2.size() < this.size()) {
                        
                        return c2.reverseRemoveAllFrom(this);
                    }
                }
                return ImmutableLHashParallelKVCharKeyMap.this.removeAll(this, (CharCollection) c);
            }
            return ImmutableLHashParallelKVCharKeyMap.this.removeAll(this, c);
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> c) {
            return ImmutableLHashParallelKVCharKeyMap.this.retainAll(this, c);
        }

        @Override
        public void clear() {
            ImmutableLHashParallelKVCharKeyMap.this.clear();
        }
    }
}

