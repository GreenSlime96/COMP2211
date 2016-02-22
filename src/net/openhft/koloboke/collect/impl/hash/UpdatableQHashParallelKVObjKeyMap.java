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
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.impl.*;
import net.openhft.koloboke.collect.set.ObjSet;
import net.openhft.koloboke.collect.set.hash.HashObjSet;
import javax.annotation.Nonnull;

import java.util.*;


public abstract class UpdatableQHashParallelKVObjKeyMap<K>
        extends UpdatableParallelKVObjQHashGO<K> {

    public Equivalence<K> keyEquivalence() {
        return Equivalence.defaultEquality();
    }


    public final boolean containsKey(Object key) {
        return contains(key);
    }



    @Nonnull
    public HashObjSet keySet() {
        return new KeyView();
    }


    abstract boolean justRemove(Object key);


    class KeyView extends AbstractObjKeyView<K>
            implements HashObjSet<K>, InternalObjCollectionOps<K>, ParallelKVObjQHash {

        @Override
        @Nonnull
        public Equivalence<K> equivalence() {
            return UpdatableQHashParallelKVObjKeyMap.this.keyEquivalence();
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return UpdatableQHashParallelKVObjKeyMap.this.hashConfig();
        }

        @Override
        public HashConfigWrapper configWrapper() {
            return UpdatableQHashParallelKVObjKeyMap.this.configWrapper();
        }

        @Override
        public int size() {
            return UpdatableQHashParallelKVObjKeyMap.this.size();
        }

        @Override
        public double currentLoad() {
            return UpdatableQHashParallelKVObjKeyMap.this.currentLoad();
        }


        @Nonnull
        @Override
        public Object[] table() {
            return UpdatableQHashParallelKVObjKeyMap.this.table();
        }

        @Override
        public int capacity() {
            return UpdatableQHashParallelKVObjKeyMap.this.capacity();
        }

        @Override
        public int freeSlots() {
            return UpdatableQHashParallelKVObjKeyMap.this.freeSlots();
        }

        @Override
        public boolean noRemoved() {
            return UpdatableQHashParallelKVObjKeyMap.this.noRemoved();
        }

        @Override
        public int removedSlots() {
            return UpdatableQHashParallelKVObjKeyMap.this.removedSlots();
        }

        @Override
        public int modCount() {
            return UpdatableQHashParallelKVObjKeyMap.this.modCount();
        }

        @Override
        public final boolean contains(Object o) {
            return UpdatableQHashParallelKVObjKeyMap.this.contains(o);
        }



        @Override
        public void forEach(Consumer<? super K> action) {
            UpdatableQHashParallelKVObjKeyMap.this.forEach(action);
        }


        @Override
        public boolean forEachWhile(Predicate<? super K>
                predicate) {
            return UpdatableQHashParallelKVObjKeyMap.this.forEachWhile(predicate);
        }

        @Override
        public boolean allContainingIn(ObjCollection<?> c) {
            return UpdatableQHashParallelKVObjKeyMap.this.allContainingIn(c);
        }

        @Override
        public boolean reverseAddAllTo(ObjCollection<? super K> c) {
            return UpdatableQHashParallelKVObjKeyMap.this.reverseAddAllTo(c);
        }

        @Override
        public boolean reverseRemoveAllFrom(ObjSet<?> s) {
            return UpdatableQHashParallelKVObjKeyMap.this.reverseRemoveAllFrom(s);
        }

        @Override
        @Nonnull
        public ObjIterator<K> iterator() {
            return UpdatableQHashParallelKVObjKeyMap.this.iterator();
        }

        @Override
        @Nonnull
        public ObjCursor<K> cursor() {
            return setCursor();
        }

        @Override
        @Nonnull
        public Object[] toArray() {
            return UpdatableQHashParallelKVObjKeyMap.this.toArray();
        }

        @Override
        @Nonnull
        public <T> T[] toArray(@Nonnull T[] a) {
            return UpdatableQHashParallelKVObjKeyMap.this.toArray(a);
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
            return UpdatableQHashParallelKVObjKeyMap.this.shrink();
        }


        @Override
        public final boolean remove(Object o) {
            return justRemove(o);
        }



        @Override
        public boolean removeIf(Predicate<? super K> filter) {
            return UpdatableQHashParallelKVObjKeyMap.this.removeIf(filter);
        }


        @Override
        public boolean removeAll(@Nonnull Collection<?> c) {
                if (c instanceof InternalObjCollectionOps) {
                    InternalObjCollectionOps c2 = (InternalObjCollectionOps) c;
                    if (c2.size() < this.size()
                            && equivalence().equals(c2.equivalence())
                            ) {
                        // noinspection unchecked
                        return c2.reverseRemoveAllFrom(this);
                    }
                }
            return UpdatableQHashParallelKVObjKeyMap.this.removeAll(this, c);
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> c) {
            return UpdatableQHashParallelKVObjKeyMap.this.retainAll(this, c);
        }

        @Override
        public void clear() {
            UpdatableQHashParallelKVObjKeyMap.this.clear();
        }
    }
}

