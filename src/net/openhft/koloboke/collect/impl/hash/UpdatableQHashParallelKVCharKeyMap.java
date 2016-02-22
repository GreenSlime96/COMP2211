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


public abstract class UpdatableQHashParallelKVCharKeyMap
        extends UpdatableParallelKVCharQHashGO {



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
            implements HashCharSet, InternalCharCollectionOps, ParallelKVCharQHash {


        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return UpdatableQHashParallelKVCharKeyMap.this.hashConfig();
        }

        @Override
        public HashConfigWrapper configWrapper() {
            return UpdatableQHashParallelKVCharKeyMap.this.configWrapper();
        }

        @Override
        public int size() {
            return UpdatableQHashParallelKVCharKeyMap.this.size();
        }

        @Override
        public double currentLoad() {
            return UpdatableQHashParallelKVCharKeyMap.this.currentLoad();
        }

        @Override
        public char freeValue() {
            return UpdatableQHashParallelKVCharKeyMap.this.freeValue();
        }

        @Override
        public boolean supportRemoved() {
            return UpdatableQHashParallelKVCharKeyMap.this.supportRemoved();
        }

        @Override
        public char removedValue() {
            return UpdatableQHashParallelKVCharKeyMap.this.removedValue();
        }

        @Nonnull
        @Override
        public int[] table() {
            return UpdatableQHashParallelKVCharKeyMap.this.table();
        }

        @Override
        public int capacity() {
            return UpdatableQHashParallelKVCharKeyMap.this.capacity();
        }

        @Override
        public int freeSlots() {
            return UpdatableQHashParallelKVCharKeyMap.this.freeSlots();
        }

        @Override
        public boolean noRemoved() {
            return UpdatableQHashParallelKVCharKeyMap.this.noRemoved();
        }

        @Override
        public int removedSlots() {
            return UpdatableQHashParallelKVCharKeyMap.this.removedSlots();
        }

        @Override
        public int modCount() {
            return UpdatableQHashParallelKVCharKeyMap.this.modCount();
        }

        @Override
        public final boolean contains(Object o) {
            return UpdatableQHashParallelKVCharKeyMap.this.contains(o);
        }

        @Override
        public boolean contains(char key) {
            return UpdatableQHashParallelKVCharKeyMap.this.contains(key);
        }



        @Override
        public void forEach(Consumer<? super Character> action) {
            UpdatableQHashParallelKVCharKeyMap.this.forEach(action);
        }

        @Override
        public void forEach(CharConsumer action) {
            UpdatableQHashParallelKVCharKeyMap.this.forEach(action);
        }

        @Override
        public boolean forEachWhile(CharPredicate
                predicate) {
            return UpdatableQHashParallelKVCharKeyMap.this.forEachWhile(predicate);
        }

        @Override
        public boolean allContainingIn(CharCollection c) {
            return UpdatableQHashParallelKVCharKeyMap.this.allContainingIn(c);
        }

        @Override
        public boolean reverseAddAllTo(CharCollection c) {
            return UpdatableQHashParallelKVCharKeyMap.this.reverseAddAllTo(c);
        }

        @Override
        public boolean reverseRemoveAllFrom(CharSet s) {
            return UpdatableQHashParallelKVCharKeyMap.this.reverseRemoveAllFrom(s);
        }

        @Override
        @Nonnull
        public CharIterator iterator() {
            return UpdatableQHashParallelKVCharKeyMap.this.iterator();
        }

        @Override
        @Nonnull
        public CharCursor cursor() {
            return setCursor();
        }

        @Override
        @Nonnull
        public Object[] toArray() {
            return UpdatableQHashParallelKVCharKeyMap.this.toArray();
        }

        @Override
        @Nonnull
        public <T> T[] toArray(@Nonnull T[] a) {
            return UpdatableQHashParallelKVCharKeyMap.this.toArray(a);
        }

        @Override
        public char[] toCharArray() {
            return UpdatableQHashParallelKVCharKeyMap.this.toCharArray();
        }

        @Override
        public char[] toArray(char[] a) {
            return UpdatableQHashParallelKVCharKeyMap.this.toArray(a);
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
            return UpdatableQHashParallelKVCharKeyMap.this.shrink();
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
            return UpdatableQHashParallelKVCharKeyMap.this.removeIf(filter);
        }

        @Override
        public boolean removeIf(CharPredicate filter) {
            return UpdatableQHashParallelKVCharKeyMap.this.removeIf(filter);
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
                return UpdatableQHashParallelKVCharKeyMap.this.removeAll(this, (CharCollection) c);
            }
            return UpdatableQHashParallelKVCharKeyMap.this.removeAll(this, c);
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> c) {
            return UpdatableQHashParallelKVCharKeyMap.this.retainAll(this, c);
        }

        @Override
        public void clear() {
            UpdatableQHashParallelKVCharKeyMap.this.clear();
        }
    }
}

