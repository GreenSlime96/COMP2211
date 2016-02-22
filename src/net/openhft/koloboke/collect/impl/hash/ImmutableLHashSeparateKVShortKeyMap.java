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
import net.openhft.koloboke.function.ShortConsumer;
import net.openhft.koloboke.function.ShortPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.impl.*;
import net.openhft.koloboke.collect.set.ShortSet;
import net.openhft.koloboke.collect.set.hash.HashShortSet;
import javax.annotation.Nonnull;

import java.util.*;


public abstract class ImmutableLHashSeparateKVShortKeyMap
        extends ImmutableSeparateKVShortLHashGO {



    public final boolean containsKey(Object key) {
        return contains(key);
    }

    public boolean containsKey(short key) {
        return contains(key);
    }


    @Nonnull
    public HashShortSet keySet() {
        return new KeyView();
    }


    abstract boolean justRemove(short key);


    class KeyView extends AbstractShortKeyView
            implements HashShortSet, InternalShortCollectionOps, SeparateKVShortLHash {


        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return ImmutableLHashSeparateKVShortKeyMap.this.hashConfig();
        }

        @Override
        public HashConfigWrapper configWrapper() {
            return ImmutableLHashSeparateKVShortKeyMap.this.configWrapper();
        }

        @Override
        public int size() {
            return ImmutableLHashSeparateKVShortKeyMap.this.size();
        }

        @Override
        public double currentLoad() {
            return ImmutableLHashSeparateKVShortKeyMap.this.currentLoad();
        }

        @Override
        public short freeValue() {
            return ImmutableLHashSeparateKVShortKeyMap.this.freeValue();
        }

        @Override
        public boolean supportRemoved() {
            return ImmutableLHashSeparateKVShortKeyMap.this.supportRemoved();
        }

        @Override
        public short removedValue() {
            return ImmutableLHashSeparateKVShortKeyMap.this.removedValue();
        }

        @Nonnull
        @Override
        public short[] keys() {
            return ImmutableLHashSeparateKVShortKeyMap.this.keys();
        }

        @Override
        public int capacity() {
            return ImmutableLHashSeparateKVShortKeyMap.this.capacity();
        }

        @Override
        public int freeSlots() {
            return ImmutableLHashSeparateKVShortKeyMap.this.freeSlots();
        }

        @Override
        public boolean noRemoved() {
            return ImmutableLHashSeparateKVShortKeyMap.this.noRemoved();
        }

        @Override
        public int removedSlots() {
            return ImmutableLHashSeparateKVShortKeyMap.this.removedSlots();
        }

        @Override
        public int modCount() {
            return ImmutableLHashSeparateKVShortKeyMap.this.modCount();
        }

        @Override
        public final boolean contains(Object o) {
            return ImmutableLHashSeparateKVShortKeyMap.this.contains(o);
        }

        @Override
        public boolean contains(short key) {
            return ImmutableLHashSeparateKVShortKeyMap.this.contains(key);
        }



        @Override
        public void forEach(Consumer<? super Short> action) {
            ImmutableLHashSeparateKVShortKeyMap.this.forEach(action);
        }

        @Override
        public void forEach(ShortConsumer action) {
            ImmutableLHashSeparateKVShortKeyMap.this.forEach(action);
        }

        @Override
        public boolean forEachWhile(ShortPredicate
                predicate) {
            return ImmutableLHashSeparateKVShortKeyMap.this.forEachWhile(predicate);
        }

        @Override
        public boolean allContainingIn(ShortCollection c) {
            return ImmutableLHashSeparateKVShortKeyMap.this.allContainingIn(c);
        }

        @Override
        public boolean reverseAddAllTo(ShortCollection c) {
            return ImmutableLHashSeparateKVShortKeyMap.this.reverseAddAllTo(c);
        }

        @Override
        public boolean reverseRemoveAllFrom(ShortSet s) {
            return ImmutableLHashSeparateKVShortKeyMap.this.reverseRemoveAllFrom(s);
        }

        @Override
        @Nonnull
        public ShortIterator iterator() {
            return ImmutableLHashSeparateKVShortKeyMap.this.iterator();
        }

        @Override
        @Nonnull
        public ShortCursor cursor() {
            return setCursor();
        }

        @Override
        @Nonnull
        public Object[] toArray() {
            return ImmutableLHashSeparateKVShortKeyMap.this.toArray();
        }

        @Override
        @Nonnull
        public <T> T[] toArray(@Nonnull T[] a) {
            return ImmutableLHashSeparateKVShortKeyMap.this.toArray(a);
        }

        @Override
        public short[] toShortArray() {
            return ImmutableLHashSeparateKVShortKeyMap.this.toShortArray();
        }

        @Override
        public short[] toArray(short[] a) {
            return ImmutableLHashSeparateKVShortKeyMap.this.toArray(a);
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
            return ImmutableLHashSeparateKVShortKeyMap.this.shrink();
        }


        @Override
        public final boolean remove(Object o) {
            return justRemove((Short) o);
        }

        @Override
        public boolean removeShort(short v) {
            return justRemove(v);
        }



        @Override
        public boolean removeIf(Predicate<? super Short> filter) {
            return ImmutableLHashSeparateKVShortKeyMap.this.removeIf(filter);
        }

        @Override
        public boolean removeIf(ShortPredicate filter) {
            return ImmutableLHashSeparateKVShortKeyMap.this.removeIf(filter);
        }

        @Override
        public boolean removeAll(@Nonnull Collection<?> c) {
            if (c instanceof ShortCollection) {
                if (c instanceof InternalShortCollectionOps) {
                    InternalShortCollectionOps c2 = (InternalShortCollectionOps) c;
                    if (c2.size() < this.size()) {
                        
                        return c2.reverseRemoveAllFrom(this);
                    }
                }
                return ImmutableLHashSeparateKVShortKeyMap.this.removeAll(this, (ShortCollection) c);
            }
            return ImmutableLHashSeparateKVShortKeyMap.this.removeAll(this, c);
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> c) {
            return ImmutableLHashSeparateKVShortKeyMap.this.retainAll(this, c);
        }

        @Override
        public void clear() {
            ImmutableLHashSeparateKVShortKeyMap.this.clear();
        }
    }
}

