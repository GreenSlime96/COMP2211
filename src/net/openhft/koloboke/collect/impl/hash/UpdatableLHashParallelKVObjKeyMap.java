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


public abstract class UpdatableLHashParallelKVObjKeyMap<K>
        extends UpdatableParallelKVObjLHashGO<K> {

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
            implements HashObjSet<K>, InternalObjCollectionOps<K>, ParallelKVObjLHash {

        @Override
        @Nonnull
        public Equivalence<K> equivalence() {
            return UpdatableLHashParallelKVObjKeyMap.this.keyEquivalence();
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return UpdatableLHashParallelKVObjKeyMap.this.hashConfig();
        }

        @Override
        public HashConfigWrapper configWrapper() {
            return UpdatableLHashParallelKVObjKeyMap.this.configWrapper();
        }

        @Override
        public int size() {
            return UpdatableLHashParallelKVObjKeyMap.this.size();
        }

        @Override
        public double currentLoad() {
            return UpdatableLHashParallelKVObjKeyMap.this.currentLoad();
        }


        @Nonnull
        @Override
        public Object[] table() {
            return UpdatableLHashParallelKVObjKeyMap.this.table();
        }

        @Override
        public int capacity() {
            return UpdatableLHashParallelKVObjKeyMap.this.capacity();
        }

        @Override
        public int freeSlots() {
            return UpdatableLHashParallelKVObjKeyMap.this.freeSlots();
        }

        @Override
        public boolean noRemoved() {
            return UpdatableLHashParallelKVObjKeyMap.this.noRemoved();
        }

        @Override
        public int removedSlots() {
            return UpdatableLHashParallelKVObjKeyMap.this.removedSlots();
        }

        @Override
        public int modCount() {
            return UpdatableLHashParallelKVObjKeyMap.this.modCount();
        }

        @Override
        public final boolean contains(Object o) {
            return UpdatableLHashParallelKVObjKeyMap.this.contains(o);
        }



        @Override
        public void forEach(Consumer<? super K> action) {
            UpdatableLHashParallelKVObjKeyMap.this.forEach(action);
        }


        @Override
        public boolean forEachWhile(Predicate<? super K>
                predicate) {
            return UpdatableLHashParallelKVObjKeyMap.this.forEachWhile(predicate);
        }

        @Override
        public boolean allContainingIn(ObjCollection<?> c) {
            return UpdatableLHashParallelKVObjKeyMap.this.allContainingIn(c);
        }

        @Override
        public boolean reverseAddAllTo(ObjCollection<? super K> c) {
            return UpdatableLHashParallelKVObjKeyMap.this.reverseAddAllTo(c);
        }

        @Override
        public boolean reverseRemoveAllFrom(ObjSet<?> s) {
            return UpdatableLHashParallelKVObjKeyMap.this.reverseRemoveAllFrom(s);
        }

        @Override
        @Nonnull
        public ObjIterator<K> iterator() {
            return UpdatableLHashParallelKVObjKeyMap.this.iterator();
        }

        @Override
        @Nonnull
        public ObjCursor<K> cursor() {
            return setCursor();
        }

        @Override
        @Nonnull
        public Object[] toArray() {
            return UpdatableLHashParallelKVObjKeyMap.this.toArray();
        }

        @Override
        @Nonnull
        public <T> T[] toArray(@Nonnull T[] a) {
            return UpdatableLHashParallelKVObjKeyMap.this.toArray(a);
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
            return UpdatableLHashParallelKVObjKeyMap.this.shrink();
        }


        @Override
        public final boolean remove(Object o) {
            return justRemove(o);
        }



        @Override
        public boolean removeIf(Predicate<? super K> filter) {
            return UpdatableLHashParallelKVObjKeyMap.this.removeIf(filter);
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
            return UpdatableLHashParallelKVObjKeyMap.this.removeAll(this, c);
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> c) {
            return UpdatableLHashParallelKVObjKeyMap.this.retainAll(this, c);
        }

        @Override
        public void clear() {
            UpdatableLHashParallelKVObjKeyMap.this.clear();
        }
    }
}

