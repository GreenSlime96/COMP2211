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
import net.openhft.koloboke.function.BytePredicate;
import net.openhft.koloboke.function.ByteObjConsumer;
import net.openhft.koloboke.function.ByteObjPredicate;
import net.openhft.koloboke.function.ByteObjFunction;
import net.openhft.koloboke.function.ByteFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import java.util.*;


public class ImmutableQHashSeparateKVByteObjMapGO<V>
        extends ImmutableQHashSeparateKVByteObjMapSO<V> {

    @Override
    final void copy(SeparateKVByteObjQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVByteObjQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.move(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    @Nonnull
    public Equivalence<V> valueEquivalence() {
        return Equivalence.defaultEquality();
    }


    @Override
    public boolean containsEntry(byte key, Object value) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return nullableValueEquals(values[index], (V) value);
        } else {
            // key is absent
            return false;
        }
    }


    @Override
    public V get(Object key) {
        int index = index((Byte) key);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return null;
        }
    }

    

    @Override
    public V get(byte key) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        int index = index((Byte) key);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public V getOrDefault(byte key, V defaultValue) {
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
    public void forEach(BiConsumer<? super Byte, ? super V> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        byte free = freeValue;
        byte[] keys = set;
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            byte key;
            if ((key = keys[i]) != free) {
                action.accept(key, vals[i]);
            }
        }
    }

    @Override
    public void forEach(ByteObjConsumer<? super V> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        byte free = freeValue;
        byte[] keys = set;
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            byte key;
            if ((key = keys[i]) != free) {
                action.accept(key, vals[i]);
            }
        }
    }

    @Override
    public boolean forEachWhile(ByteObjPredicate<? super V> predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        byte free = freeValue;
        byte[] keys = set;
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            byte key;
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
    public ByteObjCursor<V> cursor() {
        
        return new NoRemovedMapCursor();
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonByteObjMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalByteObjMapOps<?> m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        byte free = freeValue;
        byte[] keys = set;
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            byte key;
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
    public void reversePutAllTo(InternalByteObjMapOps<? super V> m) {
        if (isEmpty())
            return;
        byte free = freeValue;
        byte[] keys = set;
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            byte key;
            if ((key = keys[i]) != free) {
                m.justPut(key, vals[i]);
            }
        }
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<Byte, V>> entrySet() {
        return new EntryView();
    }

    @Override
    @Nonnull
    public ObjCollection<V> values() {
        return new ValueView();
    }


    @Override
    public boolean equals(Object o) {
        return CommonMapOps.equals(this, o);
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        byte free = freeValue;
        byte[] keys = set;
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            byte key;
            if ((key = keys[i]) != free) {
                hashCode += key ^ nullableValueHashCode(vals[i]);
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
        byte free = freeValue;
        byte[] keys = set;
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            byte key;
            if ((key = keys[i]) != free) {
                sb.append(' ');
                sb.append(key);
                sb.append('=');
                Object val = vals[i];
                sb.append(val != this ? val : "(this Map)");
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
    public V put(Byte key, V value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public V put(byte key, V value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public V putIfAbsent(Byte key, V value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public V putIfAbsent(byte key, V value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void justPut(byte key, V value) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public V compute(Byte key,
            BiFunction<? super Byte, ? super V, ? extends V> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public V compute(byte key, ByteObjFunction<? super V, ? extends V> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public V computeIfAbsent(Byte key,
            Function<? super Byte, ? extends V> mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public V computeIfAbsent(byte key, ByteFunction<? extends V> mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public V computeIfPresent(Byte key,
            BiFunction<? super Byte, ? super V, ? extends V> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public V computeIfPresent(byte key, ByteObjFunction<? super V, ? extends V> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public V merge(Byte key, V value,
            BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public V merge(byte key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }




    @Override
    public void putAll(@Nonnull Map<? extends Byte, ? extends V> m) {
        CommonByteObjMapOps.putAll(this, m);
    }


    @Override
    public V replace(Byte key, V value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public V replace(byte key, V value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean replace(Byte key, V oldValue, V newValue) {
        return replace(key.byteValue(),
                oldValue,
                newValue);
    }

    @Override
    public boolean replace(byte key, V oldValue, V newValue) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public void replaceAll(
            BiFunction<? super Byte, ? super V, ? extends V> function) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void replaceAll(ByteObjFunction<? super V, ? extends V> function) {
        throw new java.lang.UnsupportedOperationException();
    }





    @Override
    public V remove(Object key) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean justRemove(byte key) {
        throw new java.lang.UnsupportedOperationException();
    }



    

    @Override
    public V remove(byte key) {
        throw new java.lang.UnsupportedOperationException();
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Byte) key).byteValue(),
                value);
    }

    @Override
    public boolean remove(byte key, Object value) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean removeIf(ByteObjPredicate<? super V> filter) {
        throw new java.lang.UnsupportedOperationException();
    }






    class EntryView extends AbstractSetView<Map.Entry<Byte, V>>
            implements HashObjSet<Map.Entry<Byte, V>>,
            InternalObjCollectionOps<Map.Entry<Byte, V>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Byte, V>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Byte>defaultEquality()
                    ,
                    valueEquivalence()
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return ImmutableQHashSeparateKVByteObjMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return ImmutableQHashSeparateKVByteObjMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return ImmutableQHashSeparateKVByteObjMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Byte, V> e = (Map.Entry<Byte, V>) o;
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
            byte free = freeValue;
            byte[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                byte key;
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
            byte free = freeValue;
            byte[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                byte key;
                if ((key = keys[i]) != free) {
                    a[resultIndex++] = (T) new ImmutableEntry(key, vals[i]);
                }
            }
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }

        @Override
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Byte, V>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            byte free = freeValue;
            byte[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                byte key;
                if ((key = keys[i]) != free) {
                    action.accept(new ImmutableEntry(key, vals[i]));
                }
            }
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Byte, V>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            byte free = freeValue;
            byte[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                byte key;
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
        public ObjIterator<Map.Entry<Byte, V>> iterator() {
            
            return new NoRemovedEntryIterator();
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Byte, V>> cursor() {
            
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
            byte free = freeValue;
            byte[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                byte key;
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
            byte free = freeValue;
            byte[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                byte key;
                if ((key = keys[i]) != free) {
                    changed |= s.remove(e.with(key, vals[i]));
                }
            }
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Byte, V>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            byte free = freeValue;
            byte[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                byte key;
                if ((key = keys[i]) != free) {
                    changed |= c.add(new ImmutableEntry(key, vals[i]));
                }
            }
            return changed;
        }


        public int hashCode() {
            return ImmutableQHashSeparateKVByteObjMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            byte free = freeValue;
            byte[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                byte key;
                if ((key = keys[i]) != free) {
                    sb.append(' ');
                    sb.append(key);
                    sb.append('=');
                    Object val = vals[i];
                    sb.append(val != this ? val : "(this Collection)");
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
            return ImmutableQHashSeparateKVByteObjMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Byte, V> e = (Map.Entry<Byte, V>) o;
                byte key = e.getKey();
                V value = e.getValue();
                return ImmutableQHashSeparateKVByteObjMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Byte, V>> filter) {
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
            ImmutableQHashSeparateKVByteObjMapGO.this.clear();
        }
    }


    abstract class ByteObjEntry extends AbstractEntry<Byte, V> {

        abstract byte key();

        @Override
        public final Byte getKey() {
            return key();
        }

        abstract V value();

        @Override
        public final V getValue() {
            return value();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object o) {
            Map.Entry e2;
            byte k2;
            V v2;
            try {
                e2 = (Map.Entry) o;
                k2 = (Byte) e2.getKey();
                v2 = (V) e2.getValue();
                return key() == k2
                        
                        &&
                        nullableValueEquals(value(), v2);
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
                    nullableValueHashCode(value());
        }
    }


    private class ImmutableEntry extends ByteObjEntry {
        private final byte key;
        private final V value;

        ImmutableEntry(byte key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public byte key() {
            return key;
        }

        @Override
        public V value() {
            return value;
        }
    }


    class ReusableEntry extends ByteObjEntry {
        private byte key;
        private V value;

        ReusableEntry with(byte key, V value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public byte key() {
            return key;
        }

        @Override
        public V value() {
            return value;
        }
    }


    class ValueView extends AbstractObjValueView<V> {

        @Override
        public Equivalence<V> equivalence() {
            return valueEquivalence();
        }

        @Override
        public int size() {
            return ImmutableQHashSeparateKVByteObjMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return ImmutableQHashSeparateKVByteObjMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return ImmutableQHashSeparateKVByteObjMapGO.this.containsValue(o);
        }



        @Override
        public void forEach(Consumer<? super V> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            byte free = freeValue;
            byte[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    action.accept(vals[i]);
                }
            }
        }


        @Override
        public boolean forEachWhile(Predicate<? super V> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            byte free = freeValue;
            byte[] keys = set;
            V[] vals = values;
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
        public boolean allContainingIn(ObjCollection<?> c) {
            if (isEmpty())
                return true;
            boolean containsAll = true;
            byte free = freeValue;
            byte[] keys = set;
            V[] vals = values;
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
        public boolean reverseAddAllTo(ObjCollection<? super V> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            byte free = freeValue;
            byte[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    changed |= c.add(vals[i]);
                }
            }
            return changed;
        }


        @Override
        public boolean reverseRemoveAllFrom(ObjSet<?> s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            byte free = freeValue;
            byte[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    changed |= s.remove(vals[i]);
                }
            }
            return changed;
        }



        @Override
        @Nonnull
        public ObjIterator<V> iterator() {
            
            return new NoRemovedValueIterator();
        }

        @Nonnull
        @Override
        public ObjCursor<V> cursor() {
            
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
            byte free = freeValue;
            byte[] keys = set;
            V[] vals = values;
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
            byte free = freeValue;
            byte[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    a[resultIndex++] = (T) vals[i];
                }
            }
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }



        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            byte free = freeValue;
            byte[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                V val;
                    sb.append(' ').append((val = vals[i]) != this ? val : "(this Collection)").append(',');
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
            return removeValue(o);
        }



        @Override
        public void clear() {
            ImmutableQHashSeparateKVByteObjMapGO.this.clear();
        }

        @Override
        public boolean removeIf(Predicate<? super V> filter) {
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Byte, V>> {
        final byte[] keys;
        final V[] vals;
        final byte free;
        int nextIndex;
        ImmutableEntry next;

        NoRemovedEntryIterator() {
            byte[] keys = this.keys = set;
            V[] vals = this.vals = values;
            byte free = this.free = freeValue;
            int nextI = keys.length;
            while (--nextI >= 0) {
                byte key;
                if ((key = keys[nextI]) != free) {
                    next = new ImmutableEntry(key, vals[nextI]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Byte, V>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            byte[] keys = this.keys;
            V[] vals = this.vals;
            byte free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                byte key;
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
        public Map.Entry<Byte, V> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                byte[] keys = this.keys;
                byte free = this.free;
                ImmutableEntry prev = next;
                while (--nextI >= 0) {
                    byte key;
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


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Byte, V>> {
        final byte[] keys;
        final V[] vals;
        final byte free;
        int index;
        byte curKey;
        V curValue;

        NoRemovedEntryCursor() {
            this.keys = set;
            index = keys.length;
            vals = values;
            byte free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Byte, V>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            byte[] keys = this.keys;
            V[] vals = this.vals;
            byte free = this.free;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                byte key;
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
        public Map.Entry<Byte, V> elem() {
            byte curKey;
            if ((curKey = this.curKey) != free) {
                return new ImmutableEntry(curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            byte[] keys = this.keys;
            byte free = this.free;
            for (int i = index - 1; i >= 0; i--) {
                byte key;
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




    class NoRemovedValueIterator implements ObjIterator<V> {
        final byte[] keys;
        final V[] vals;
        final byte free;
        int nextIndex;
        V next;

        NoRemovedValueIterator() {
            byte[] keys = this.keys = set;
            V[] vals = this.vals = values;
            byte free = this.free = freeValue;
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
        public void forEachRemaining(Consumer<? super V> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            byte[] keys = this.keys;
            V[] vals = this.vals;
            byte free = this.free;
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
        public V next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                byte[] keys = this.keys;
                byte free = this.free;
                V prev = next;
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
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedValueCursor implements ObjCursor<V> {
        final byte[] keys;
        final V[] vals;
        final byte free;
        int index;
        byte curKey;
        V curValue;

        NoRemovedValueCursor() {
            this.keys = set;
            index = keys.length;
            vals = values;
            byte free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super V> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            byte[] keys = this.keys;
            V[] vals = this.vals;
            byte free = this.free;
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
        public V elem() {
            if (curKey != free) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            byte[] keys = this.keys;
            byte free = this.free;
            for (int i = index - 1; i >= 0; i--) {
                byte key;
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



    class NoRemovedMapCursor implements ByteObjCursor<V> {
        final byte[] keys;
        final V[] vals;
        final byte free;
        int index;
        byte curKey;
        V curValue;

        NoRemovedMapCursor() {
            this.keys = set;
            index = keys.length;
            vals = values;
            byte free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ByteObjConsumer<? super V> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            byte[] keys = this.keys;
            V[] vals = this.vals;
            byte free = this.free;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                byte key;
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
        public byte key() {
            byte curKey;
            if ((curKey = this.curKey) != free) {
                return curKey;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public V value() {
            if (curKey != free) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(V value) {
            if (curKey != free) {
                vals[index] = value;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            byte[] keys = this.keys;
            byte free = this.free;
            for (int i = index - 1; i >= 0; i--) {
                byte key;
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

