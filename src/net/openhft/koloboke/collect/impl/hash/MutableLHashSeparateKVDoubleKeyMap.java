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
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.openhft.koloboke.collect.impl.*;
import net.openhft.koloboke.collect.set.DoubleSet;
import net.openhft.koloboke.collect.set.hash.HashDoubleSet;
import javax.annotation.Nonnull;

import java.util.*;


public abstract class MutableLHashSeparateKVDoubleKeyMap
        extends MutableSeparateKVDoubleLHashGO {



    public final boolean containsKey(Object key) {
        return contains(key);
    }

    public boolean containsKey(double key) {
        return contains(key);
    }


    @Nonnull
    public HashDoubleSet keySet() {
        return new KeyView();
    }


    abstract boolean justRemove(double key);

    @Override
    abstract boolean justRemove(long key);

    class KeyView extends AbstractDoubleKeyView
            implements HashDoubleSet, InternalDoubleCollectionOps, SeparateKVDoubleLHash {


        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return MutableLHashSeparateKVDoubleKeyMap.this.hashConfig();
        }

        @Override
        public HashConfigWrapper configWrapper() {
            return MutableLHashSeparateKVDoubleKeyMap.this.configWrapper();
        }

        @Override
        public int size() {
            return MutableLHashSeparateKVDoubleKeyMap.this.size();
        }

        @Override
        public double currentLoad() {
            return MutableLHashSeparateKVDoubleKeyMap.this.currentLoad();
        }


        @Nonnull
        @Override
        public long[] keys() {
            return MutableLHashSeparateKVDoubleKeyMap.this.keys();
        }

        @Override
        public int capacity() {
            return MutableLHashSeparateKVDoubleKeyMap.this.capacity();
        }

        @Override
        public int freeSlots() {
            return MutableLHashSeparateKVDoubleKeyMap.this.freeSlots();
        }

        @Override
        public boolean noRemoved() {
            return MutableLHashSeparateKVDoubleKeyMap.this.noRemoved();
        }

        @Override
        public int removedSlots() {
            return MutableLHashSeparateKVDoubleKeyMap.this.removedSlots();
        }

        @Override
        public int modCount() {
            return MutableLHashSeparateKVDoubleKeyMap.this.modCount();
        }

        @Override
        public final boolean contains(Object o) {
            return MutableLHashSeparateKVDoubleKeyMap.this.contains(o);
        }

        @Override
        public boolean contains(double key) {
            return MutableLHashSeparateKVDoubleKeyMap.this.contains(key);
        }

        @Override
        public boolean contains(long bits) {
            return MutableLHashSeparateKVDoubleKeyMap.this.contains(bits);
        }


        @Override
        public void forEach(Consumer<? super Double> action) {
            MutableLHashSeparateKVDoubleKeyMap.this.forEach(action);
        }

        @Override
        public void forEach(DoubleConsumer action) {
            MutableLHashSeparateKVDoubleKeyMap.this.forEach(action);
        }

        @Override
        public boolean forEachWhile(DoublePredicate
                predicate) {
            return MutableLHashSeparateKVDoubleKeyMap.this.forEachWhile(predicate);
        }

        @Override
        public boolean allContainingIn(DoubleCollection c) {
            return MutableLHashSeparateKVDoubleKeyMap.this.allContainingIn(c);
        }

        @Override
        public boolean reverseAddAllTo(DoubleCollection c) {
            return MutableLHashSeparateKVDoubleKeyMap.this.reverseAddAllTo(c);
        }

        @Override
        public boolean reverseRemoveAllFrom(DoubleSet s) {
            return MutableLHashSeparateKVDoubleKeyMap.this.reverseRemoveAllFrom(s);
        }

        @Override
        @Nonnull
        public DoubleIterator iterator() {
            return MutableLHashSeparateKVDoubleKeyMap.this.iterator();
        }

        @Override
        @Nonnull
        public DoubleCursor cursor() {
            return setCursor();
        }

        @Override
        @Nonnull
        public Object[] toArray() {
            return MutableLHashSeparateKVDoubleKeyMap.this.toArray();
        }

        @Override
        @Nonnull
        public <T> T[] toArray(@Nonnull T[] a) {
            return MutableLHashSeparateKVDoubleKeyMap.this.toArray(a);
        }

        @Override
        public double[] toDoubleArray() {
            return MutableLHashSeparateKVDoubleKeyMap.this.toDoubleArray();
        }

        @Override
        public double[] toArray(double[] a) {
            return MutableLHashSeparateKVDoubleKeyMap.this.toArray(a);
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
            return MutableLHashSeparateKVDoubleKeyMap.this.shrink();
        }


        @Override
        public final boolean remove(Object o) {
            return justRemove((Double) o);
        }

        @Override
        public boolean removeDouble(double v) {
            return justRemove(v);
        }

        @Override
        public boolean removeDouble(long bits) {
            return justRemove(bits);
        }


        @Override
        public boolean removeIf(Predicate<? super Double> filter) {
            return MutableLHashSeparateKVDoubleKeyMap.this.removeIf(filter);
        }

        @Override
        public boolean removeIf(DoublePredicate filter) {
            return MutableLHashSeparateKVDoubleKeyMap.this.removeIf(filter);
        }

        @Override
        public boolean removeAll(@Nonnull Collection<?> c) {
            if (c instanceof DoubleCollection) {
                if (c instanceof InternalDoubleCollectionOps) {
                    InternalDoubleCollectionOps c2 = (InternalDoubleCollectionOps) c;
                    if (c2.size() < this.size()) {
                        
                        return c2.reverseRemoveAllFrom(this);
                    }
                }
                return MutableLHashSeparateKVDoubleKeyMap.this.removeAll(this, (DoubleCollection) c);
            }
            return MutableLHashSeparateKVDoubleKeyMap.this.removeAll(this, c);
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> c) {
            return MutableLHashSeparateKVDoubleKeyMap.this.retainAll(this, c);
        }

        @Override
        public void clear() {
            MutableLHashSeparateKVDoubleKeyMap.this.clear();
        }
    }
}

