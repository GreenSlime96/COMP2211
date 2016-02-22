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
import net.openhft.koloboke.collect.impl.*;
import net.openhft.koloboke.collect.map.*;
import net.openhft.koloboke.collect.map.hash.*;
import net.openhft.koloboke.collect.set.*;
import net.openhft.koloboke.collect.set.hash.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import net.openhft.koloboke.function.ShortPredicate;
import net.openhft.koloboke.function.ShortLongConsumer;
import net.openhft.koloboke.function.ShortLongPredicate;
import net.openhft.koloboke.function.ShortLongToLongFunction;
import net.openhft.koloboke.function.ShortToLongFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class ImmutableQHashSeparateKVShortLongMapGO
        extends ImmutableQHashSeparateKVShortLongMapSO {

    @Override
    final void copy(SeparateKVShortLongQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVShortLongQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.move(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }


    @Override
    public long defaultValue() {
        return 0L;
    }

    @Override
    public boolean containsEntry(short key, long value) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return values[index] == value;
        } else {
            // key is absent
            return false;
        }
    }


    @Override
    public Long get(Object key) {
        int index = index((Short) key);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return null;
        }
    }

    

    @Override
    public long get(short key) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Long getOrDefault(Object key, Long defaultValue) {
        int index = index((Short) key);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public long getOrDefault(short key, long defaultValue) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public void forEach(BiConsumer<? super Short, ? super Long> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        short free = freeValue;
        short[] keys = set;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            short key;
            if ((key = keys[i]) != free) {
                action.accept(key, vals[i]);
            }
        }
    }

    @Override
    public void forEach(ShortLongConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        short free = freeValue;
        short[] keys = set;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            short key;
            if ((key = keys[i]) != free) {
                action.accept(key, vals[i]);
            }
        }
    }

    @Override
    public boolean forEachWhile(ShortLongPredicate predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        short free = freeValue;
        short[] keys = set;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            short key;
            if ((key = keys[i]) != free) {
                if (!predicate.test(key, vals[i])) {
                    terminated = true;
                    break;
                }
            }
        }
        return !terminated;
    }

    @Nonnull
    @Override
    public ShortLongCursor cursor() {
        
        return new NoRemovedMapCursor();
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonShortLongMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalShortLongMapOps m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        short free = freeValue;
        short[] keys = set;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            short key;
            if ((key = keys[i]) != free) {
                if (!m.containsEntry(key, vals[i])) {
                    containsAll = false;
                    break;
                }
            }
        }
        return containsAll;
    }

    @Override
    public void reversePutAllTo(InternalShortLongMapOps m) {
        if (isEmpty())
            return;
        short free = freeValue;
        short[] keys = set;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            short key;
            if ((key = keys[i]) != free) {
                m.justPut(key, vals[i]);
            }
        }
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<Short, Long>> entrySet() {
        return new EntryView();
    }

    @Override
    @Nonnull
    public LongCollection values() {
        return new ValueView();
    }


    @Override
    public boolean equals(Object o) {
        return CommonMapOps.equals(this, o);
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        short free = freeValue;
        short[] keys = set;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            short key;
            if ((key = keys[i]) != free) {
            long val;
                hashCode += key ^ ((int) ((val = vals[i]) ^ (val >>> 32)));
            }
        }
        return hashCode;
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "{}";
        StringBuilder sb = new StringBuilder();
        int elementCount = 0;
        short free = freeValue;
        short[] keys = set;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            short key;
            if ((key = keys[i]) != free) {
                sb.append(' ');
                sb.append(key);
                sb.append('=');
                sb.append(vals[i]);
                sb.append(',');
                if (++elementCount == 8) {
                    int expectedLength = sb.length() * (size() / 8);
                    sb.ensureCapacity(expectedLength + (expectedLength / 2));
                }
            }
        }
        sb.setCharAt(0, '{');
        sb.setCharAt(sb.length() - 1, '}');
        return sb.toString();
    }




    @Override
    public Long put(Short key, Long value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public long put(short key, long value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Long putIfAbsent(Short key, Long value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public long putIfAbsent(short key, long value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void justPut(short key, long value) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public Long compute(Short key,
            BiFunction<? super Short, ? super Long, ? extends Long> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public long compute(short key, ShortLongToLongFunction remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public Long computeIfAbsent(Short key,
            Function<? super Short, ? extends Long> mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public long computeIfAbsent(short key, ShortToLongFunction mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public Long computeIfPresent(Short key,
            BiFunction<? super Short, ? super Long, ? extends Long> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public long computeIfPresent(short key, ShortLongToLongFunction remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Long merge(Short key, Long value,
            BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public long merge(short key, long value, LongBinaryOperator remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public long addValue(short key, long value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public long addValue(short key, long addition, long defaultValue) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public void putAll(@Nonnull Map<? extends Short, ? extends Long> m) {
        CommonShortLongMapOps.putAll(this, m);
    }


    @Override
    public Long replace(Short key, Long value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public long replace(short key, long value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean replace(Short key, Long oldValue, Long newValue) {
        return replace(key.shortValue(),
                oldValue.longValue(),
                newValue.longValue());
    }

    @Override
    public boolean replace(short key, long oldValue, long newValue) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public void replaceAll(
            BiFunction<? super Short, ? super Long, ? extends Long> function) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void replaceAll(ShortLongToLongFunction function) {
        throw new java.lang.UnsupportedOperationException();
    }





    @Override
    public Long remove(Object key) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean justRemove(short key) {
        throw new java.lang.UnsupportedOperationException();
    }



    

    @Override
    public long remove(short key) {
        throw new java.lang.UnsupportedOperationException();
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Short) key).shortValue(),
                ((Long) value).longValue()
                );
    }

    @Override
    public boolean remove(short key, long value) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean removeIf(ShortLongPredicate filter) {
        throw new java.lang.UnsupportedOperationException();
    }






    class EntryView extends AbstractSetView<Map.Entry<Short, Long>>
            implements HashObjSet<Map.Entry<Short, Long>>,
            InternalObjCollectionOps<Map.Entry<Short, Long>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Short, Long>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Short>defaultEquality()
                    ,
                    Equivalence.<Long>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return ImmutableQHashSeparateKVShortLongMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return ImmutableQHashSeparateKVShortLongMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return ImmutableQHashSeparateKVShortLongMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Short, Long> e = (Map.Entry<Short, Long>) o;
                return containsEntry(e.getKey(), e.getValue());
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        @Nonnull
        public final Object[] toArray() {
            int size = size();
            Object[] result = new Object[size];
            if (size == 0)
                return result;
            int resultIndex = 0;
            short free = freeValue;
            short[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    result[resultIndex++] = new ImmutableEntry(key, vals[i]);
                }
            }
            return result;
        }

        @Override
        @SuppressWarnings("unchecked")
        @Nonnull
        public final <T> T[] toArray(@Nonnull T[] a) {
            int size = size();
            if (a.length < size) {
                Class<?> elementType = a.getClass().getComponentType();
                a = (T[]) java.lang.reflect.Array.newInstance(elementType, size);
            }
            if (size == 0) {
                if (a.length > 0)
                    a[0] = null;
                return a;
            }
            int resultIndex = 0;
            short free = freeValue;
            short[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    a[resultIndex++] = (T) new ImmutableEntry(key, vals[i]);
                }
            }
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }

        @Override
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Short, Long>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            short free = freeValue;
            short[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    action.accept(new ImmutableEntry(key, vals[i]));
                }
            }
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Short, Long>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            short free = freeValue;
            short[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    if (!predicate.test(new ImmutableEntry(key, vals[i]))) {
                        terminated = true;
                        break;
                    }
                }
            }
            return !terminated;
        }

        @Override
        @Nonnull
        public ObjIterator<Map.Entry<Short, Long>> iterator() {
            
            return new NoRemovedEntryIterator();
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Short, Long>> cursor() {
            
            return new NoRemovedEntryCursor();
        }

        @Override
        public final boolean containsAll(@Nonnull Collection<?> c) {
            return CommonObjCollectionOps.containsAll(this, c);
        }

        @Override
        public final boolean allContainingIn(ObjCollection<?> c) {
            if (isEmpty())
                return true;
            boolean containsAll = true;
            ReusableEntry e = new ReusableEntry();
            short free = freeValue;
            short[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    if (!c.contains(e.with(key, vals[i]))) {
                        containsAll = false;
                        break;
                    }
                }
            }
            return containsAll;
        }

        @Override
        public boolean reverseRemoveAllFrom(ObjSet<?> s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            ReusableEntry e = new ReusableEntry();
            short free = freeValue;
            short[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    changed |= s.remove(e.with(key, vals[i]));
                }
            }
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Short, Long>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            short free = freeValue;
            short[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    changed |= c.add(new ImmutableEntry(key, vals[i]));
                }
            }
            return changed;
        }


        public int hashCode() {
            return ImmutableQHashSeparateKVShortLongMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            short free = freeValue;
            short[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    sb.append(' ');
                    sb.append(key);
                    sb.append('=');
                    sb.append(vals[i]);
                    sb.append(',');
                    if (++elementCount == 8) {
                        int expectedLength = sb.length() * (size() / 8);
                        sb.ensureCapacity(expectedLength + (expectedLength / 2));
                    }
                }
            }
            sb.setCharAt(0, '[');
            sb.setCharAt(sb.length() - 1, ']');
            return sb.toString();
        }

        @Override
        public boolean shrink() {
            return ImmutableQHashSeparateKVShortLongMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Short, Long> e = (Map.Entry<Short, Long>) o;
                short key = e.getKey();
                long value = e.getValue();
                return ImmutableQHashSeparateKVShortLongMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Short, Long>> filter) {
            throw new java.lang.UnsupportedOperationException();
        }

        @Override
        public final boolean removeAll(@Nonnull Collection<?> c) {
            if (c instanceof InternalObjCollectionOps) {
                InternalObjCollectionOps c2 = (InternalObjCollectionOps) c;
                if (equivalence().equals(c2.equivalence()) && c2.size() < this.size()) {
                    // noinspection unchecked
                    c2.reverseRemoveAllFrom(this);
                }
            }
            throw new java.lang.UnsupportedOperationException();
        }

        @Override
        public final boolean retainAll(@Nonnull Collection<?> c) {
            throw new java.lang.UnsupportedOperationException();
        }

        @Override
        public void clear() {
            ImmutableQHashSeparateKVShortLongMapGO.this.clear();
        }
    }


    abstract class ShortLongEntry extends AbstractEntry<Short, Long> {

        abstract short key();

        @Override
        public final Short getKey() {
            return key();
        }

        abstract long value();

        @Override
        public final Long getValue() {
            return value();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object o) {
            Map.Entry e2;
            short k2;
            long v2;
            try {
                e2 = (Map.Entry) o;
                k2 = (Short) e2.getKey();
                v2 = (Long) e2.getValue();
                return key() == k2
                        
                        &&
                        value() == v2
                        ;
            } catch (ClassCastException e) {
                return false;
            } catch (NullPointerException e) {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Primitives.hashCode(key())
                    
                    ^
                    Primitives.hashCode(value())
                    ;
        }
    }


    private class ImmutableEntry extends ShortLongEntry {
        private final short key;
        private final long value;

        ImmutableEntry(short key, long value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public short key() {
            return key;
        }

        @Override
        public long value() {
            return value;
        }
    }


    class ReusableEntry extends ShortLongEntry {
        private short key;
        private long value;

        ReusableEntry with(short key, long value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public short key() {
            return key;
        }

        @Override
        public long value() {
            return value;
        }
    }


    class ValueView extends AbstractLongValueView {


        @Override
        public int size() {
            return ImmutableQHashSeparateKVShortLongMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return ImmutableQHashSeparateKVShortLongMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return ImmutableQHashSeparateKVShortLongMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(long v) {
            return ImmutableQHashSeparateKVShortLongMapGO.this.containsValue(v);
        }



        @Override
        public void forEach(Consumer<? super Long> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            short free = freeValue;
            short[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    action.accept(vals[i]);
                }
            }
        }

        @Override
        public void forEach(LongConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            short free = freeValue;
            short[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    action.accept(vals[i]);
                }
            }
        }

        @Override
        public boolean forEachWhile(LongPredicate predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            short free = freeValue;
            short[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    if (!predicate.test(vals[i])) {
                        terminated = true;
                        break;
                    }
                }
            }
            return !terminated;
        }

        @Override
        public boolean allContainingIn(LongCollection c) {
            if (isEmpty())
                return true;
            boolean containsAll = true;
            short free = freeValue;
            short[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    if (!c.contains(vals[i])) {
                        containsAll = false;
                        break;
                    }
                }
            }
            return containsAll;
        }


        @Override
        public boolean reverseAddAllTo(LongCollection c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            short free = freeValue;
            short[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    changed |= c.add(vals[i]);
                }
            }
            return changed;
        }


        @Override
        public boolean reverseRemoveAllFrom(LongSet s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            short free = freeValue;
            short[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    changed |= s.removeLong(vals[i]);
                }
            }
            return changed;
        }



        @Override
        @Nonnull
        public LongIterator iterator() {
            
            return new NoRemovedValueIterator();
        }

        @Nonnull
        @Override
        public LongCursor cursor() {
            
            return new NoRemovedValueCursor();
        }

        @Override
        @Nonnull
        public Object[] toArray() {
            int size = size();
            Object[] result = new Object[size];
            if (size == 0)
                return result;
            int resultIndex = 0;
            short free = freeValue;
            short[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    result[resultIndex++] = vals[i];
                }
            }
            return result;
        }

        @Override
        @SuppressWarnings("unchecked")
        @Nonnull
        public <T> T[] toArray(@Nonnull T[] a) {
            int size = size();
            if (a.length < size) {
                Class<?> elementType = a.getClass().getComponentType();
                a = (T[]) java.lang.reflect.Array.newInstance(elementType, size);
            }
            if (size == 0) {
                if (a.length > 0)
                    a[0] = null;
                return a;
            }
            int resultIndex = 0;
            short free = freeValue;
            short[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    a[resultIndex++] = (T) Long.valueOf(vals[i]);
                }
            }
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }

        @Override
        public long[] toLongArray() {
            int size = size();
            long[] result = new long[size];
            if (size == 0)
                return result;
            int resultIndex = 0;
            short free = freeValue;
            short[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    result[resultIndex++] = vals[i];
                }
            }
            return result;
        }

        @Override
        public long[] toArray(long[] a) {
            int size = size();
            if (a.length < size)
                a = new long[size];
            if (size == 0) {
                if (a.length > 0)
                    a[0] = 0L;
                return a;
            }
            int resultIndex = 0;
            short free = freeValue;
            short[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    a[resultIndex++] = vals[i];
                }
            }
            if (a.length > resultIndex)
                a[resultIndex] = 0L;
            return a;
        }


        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            short free = freeValue;
            short[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    sb.append(' ').append(vals[i]).append(',');
                    if (++elementCount == 8) {
                        int expectedLength = sb.length() * (size() / 8);
                        sb.ensureCapacity(expectedLength + (expectedLength / 2));
                    }
                }
            }
            sb.setCharAt(0, '[');
            sb.setCharAt(sb.length() - 1, ']');
            return sb.toString();
        }


        @Override
        public boolean remove(Object o) {
            return removeLong(( Long ) o);
        }

        @Override
        public boolean removeLong(long v) {
            return removeValue(v);
        }



        @Override
        public void clear() {
            ImmutableQHashSeparateKVShortLongMapGO.this.clear();
        }

        
        public boolean removeIf(Predicate<? super Long> filter) {
            throw new java.lang.UnsupportedOperationException();
        }

        @Override
        public boolean removeIf(LongPredicate filter) {
            throw new java.lang.UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(@Nonnull Collection<?> c) {
            throw new java.lang.UnsupportedOperationException();
        }


        @Override
        public boolean retainAll(@Nonnull Collection<?> c) {
            throw new java.lang.UnsupportedOperationException();
        }

    }



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Short, Long>> {
        final short[] keys;
        final long[] vals;
        final short free;
        int nextIndex;
        ImmutableEntry next;

        NoRemovedEntryIterator() {
            short[] keys = this.keys = set;
            long[] vals = this.vals = values;
            short free = this.free = freeValue;
            int nextI = keys.length;
            while (--nextI >= 0) {
                short key;
                if ((key = keys[nextI]) != free) {
                    next = new ImmutableEntry(key, vals[nextI]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Short, Long>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            short[] keys = this.keys;
            long[] vals = this.vals;
            short free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    action.accept(new ImmutableEntry(key, vals[i]));
                }
            }
            if (nextI != nextIndex) {
                throw new java.util.ConcurrentModificationException();
            }
            nextIndex = -1;
        }

        @Override
        public boolean hasNext() {
            return nextIndex >= 0;
        }

        @Override
        public Map.Entry<Short, Long> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                short[] keys = this.keys;
                short free = this.free;
                ImmutableEntry prev = next;
                while (--nextI >= 0) {
                    short key;
                    if ((key = keys[nextI]) != free) {
                        next = new ImmutableEntry(key, vals[nextI]);
                        break;
                    }
                }
                nextIndex = nextI;
                return prev;
            } else {
                throw new java.util.NoSuchElementException();
            }
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Short, Long>> {
        final short[] keys;
        final long[] vals;
        final short free;
        int index;
        short curKey;
        long curValue;

        NoRemovedEntryCursor() {
            this.keys = set;
            index = keys.length;
            vals = values;
            short free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Short, Long>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            short[] keys = this.keys;
            long[] vals = this.vals;
            short free = this.free;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    action.accept(new ImmutableEntry(key, vals[i]));
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public Map.Entry<Short, Long> elem() {
            short curKey;
            if ((curKey = this.curKey) != free) {
                return new ImmutableEntry(curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            short[] keys = this.keys;
            short free = this.free;
            for (int i = index - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    index = i;
                    curKey = key;
                    curValue = vals[i];
                    return true;
                }
            }
            curKey = free;
            index = -1;
            return false;
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }




    class NoRemovedValueIterator implements LongIterator {
        final short[] keys;
        final long[] vals;
        final short free;
        int nextIndex;
        long next;

        NoRemovedValueIterator() {
            short[] keys = this.keys = set;
            long[] vals = this.vals = values;
            short free = this.free = freeValue;
            int nextI = keys.length;
            while (--nextI >= 0) {
                if (keys[nextI] != free) {
                    next = vals[nextI];
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public long nextLong() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                short[] keys = this.keys;
                short free = this.free;
                long prev = next;
                while (--nextI >= 0) {
                    if (keys[nextI] != free) {
                        next = vals[nextI];
                        break;
                    }
                }
                nextIndex = nextI;
                return prev;
            } else {
                throw new java.util.NoSuchElementException();
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super Long> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            short[] keys = this.keys;
            long[] vals = this.vals;
            short free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if (keys[i] != free) {
                    action.accept(vals[i]);
                }
            }
            if (nextI != nextIndex) {
                throw new java.util.ConcurrentModificationException();
            }
            nextIndex = -1;
        }

        @Override
        public void forEachRemaining(LongConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            short[] keys = this.keys;
            long[] vals = this.vals;
            short free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if (keys[i] != free) {
                    action.accept(vals[i]);
                }
            }
            if (nextI != nextIndex) {
                throw new java.util.ConcurrentModificationException();
            }
            nextIndex = -1;
        }

        @Override
        public boolean hasNext() {
            return nextIndex >= 0;
        }

        @Override
        public Long next() {
            return nextLong();
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedValueCursor implements LongCursor {
        final short[] keys;
        final long[] vals;
        final short free;
        int index;
        short curKey;
        long curValue;

        NoRemovedValueCursor() {
            this.keys = set;
            index = keys.length;
            vals = values;
            short free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(LongConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            short[] keys = this.keys;
            long[] vals = this.vals;
            short free = this.free;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    action.accept(vals[i]);
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public long elem() {
            if (curKey != free) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            short[] keys = this.keys;
            short free = this.free;
            for (int i = index - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    index = i;
                    curKey = key;
                    curValue = vals[i];
                    return true;
                }
            }
            curKey = free;
            index = -1;
            return false;
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }



    class NoRemovedMapCursor implements ShortLongCursor {
        final short[] keys;
        final long[] vals;
        final short free;
        int index;
        short curKey;
        long curValue;

        NoRemovedMapCursor() {
            this.keys = set;
            index = keys.length;
            vals = values;
            short free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ShortLongConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            short[] keys = this.keys;
            long[] vals = this.vals;
            short free = this.free;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    action.accept(key, vals[i]);
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public short key() {
            short curKey;
            if ((curKey = this.curKey) != free) {
                return curKey;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public long value() {
            if (curKey != free) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(long value) {
            if (curKey != free) {
                vals[index] = value;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            short[] keys = this.keys;
            short free = this.free;
            for (int i = index - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    index = i;
                    curKey = key;
                    curValue = vals[i];
                    return true;
                }
            }
            curKey = free;
            index = -1;
            return false;
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }
}

