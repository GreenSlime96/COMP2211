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
import net.openhft.koloboke.function.ShortShortConsumer;
import net.openhft.koloboke.function.ShortShortPredicate;
import net.openhft.koloboke.function.ShortShortToShortFunction;
import net.openhft.koloboke.function.ShortUnaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.openhft.koloboke.function.ShortBinaryOperator;
import net.openhft.koloboke.function.ShortConsumer;
import net.openhft.koloboke.function.ShortPredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class ImmutableLHashParallelKVShortShortMapGO
        extends ImmutableLHashParallelKVShortShortMapSO {

    
    final void copy(ParallelKVShortShortLHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    
    final void move(ParallelKVShortShortLHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.move(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }


    @Override
    public short defaultValue() {
        return (short) 0;
    }

    @Override
    public boolean containsEntry(short key, short value) {
        short free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            short cur;
            int entry;
            if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
                // key is present
                return (short) (entry >>> 16) == value;
            } else {
                if (cur == free) {
                    // key is absent
                    return false;
                } else {
                    while (true) {
                        if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            // key is present
                            return (short) (entry >>> 16) == value;
                        } else if (cur == free) {
                            // key is absent
                            return false;
                        }
                    }
                }
            }
        } else {
            // key is absent
            return false;
        }
    }


    @Override
    public Short get(Object key) {
        short k = (Short) key;
        short free;
        if (k != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            short cur;
            int entry;
            if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
                // key is present
                return (short) (entry >>> 16);
            } else {
                if (cur == free) {
                    // key is absent
                    return null;
                } else {
                    while (true) {
                        if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                            // key is present
                            return (short) (entry >>> 16);
                        } else if (cur == free) {
                            // key is absent
                            return null;
                        }
                    }
                }
            }
        } else {
            // key is absent
            return null;
        }
    }

    

    @Override
    public short get(short key) {
        short free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            short cur;
            int entry;
            if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
                // key is present
                return (short) (entry >>> 16);
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue();
                } else {
                    while (true) {
                        if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            // key is present
                            return (short) (entry >>> 16);
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue();
                        }
                    }
                }
            }
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Short getOrDefault(Object key, Short defaultValue) {
        short k = (Short) key;
        short free;
        if (k != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            short cur;
            int entry;
            if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
                // key is present
                return (short) (entry >>> 16);
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue;
                } else {
                    while (true) {
                        if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                            // key is present
                            return (short) (entry >>> 16);
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue;
                        }
                    }
                }
            }
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public short getOrDefault(short key, short defaultValue) {
        short free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            short cur;
            int entry;
            if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
                // key is present
                return (short) (entry >>> 16);
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue;
                } else {
                    while (true) {
                        if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            // key is present
                            return (short) (entry >>> 16);
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue;
                        }
                    }
                }
            }
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public void forEach(BiConsumer<? super Short, ? super Short> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        short free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                action.accept(key, (short) (entry >>> 16));
            }
        }
    }

    @Override
    public void forEach(ShortShortConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        short free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                action.accept(key, (short) (entry >>> 16));
            }
        }
    }

    @Override
    public boolean forEachWhile(ShortShortPredicate predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        short free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                if (!predicate.test(key, (short) (entry >>> 16))) {
                    terminated = true;
                    break;
                }
            }
        }
        return !terminated;
    }

    @Nonnull
    @Override
    public ShortShortCursor cursor() {
        
        return new NoRemovedMapCursor();
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonShortShortMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalShortShortMapOps m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        short free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                if (!m.containsEntry(key, (short) (entry >>> 16))) {
                    containsAll = false;
                    break;
                }
            }
        }
        return containsAll;
    }

    @Override
    public void reversePutAllTo(InternalShortShortMapOps m) {
        if (isEmpty())
            return;
        short free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                m.justPut(key, (short) (entry >>> 16));
            }
        }
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<Short, Short>> entrySet() {
        return new EntryView();
    }

    @Override
    @Nonnull
    public ShortCollection values() {
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
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                hashCode += key ^ (short) (entry >>> 16);
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
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                sb.append(' ');
                sb.append(key);
                sb.append('=');
                sb.append((short) (entry >>> 16));
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
    public Short put(Short key, Short value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public short put(short key, short value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Short putIfAbsent(Short key, Short value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public short putIfAbsent(short key, short value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void justPut(short key, short value) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public Short compute(Short key,
            BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public short compute(short key, ShortShortToShortFunction remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public Short computeIfAbsent(Short key,
            Function<? super Short, ? extends Short> mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public short computeIfAbsent(short key, ShortUnaryOperator mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public Short computeIfPresent(Short key,
            BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public short computeIfPresent(short key, ShortShortToShortFunction remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Short merge(Short key, Short value,
            BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public short merge(short key, short value, ShortBinaryOperator remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public short addValue(short key, short value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public short addValue(short key, short addition, short defaultValue) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public void putAll(@Nonnull Map<? extends Short, ? extends Short> m) {
        CommonShortShortMapOps.putAll(this, m);
    }


    @Override
    public Short replace(Short key, Short value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public short replace(short key, short value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean replace(Short key, Short oldValue, Short newValue) {
        return replace(key.shortValue(),
                oldValue.shortValue(),
                newValue.shortValue());
    }

    @Override
    public boolean replace(short key, short oldValue, short newValue) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public void replaceAll(
            BiFunction<? super Short, ? super Short, ? extends Short> function) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void replaceAll(ShortShortToShortFunction function) {
        throw new java.lang.UnsupportedOperationException();
    }





    @Override
    public Short remove(Object key) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean justRemove(short key) {
        throw new java.lang.UnsupportedOperationException();
    }



    

    @Override
    public short remove(short key) {
        throw new java.lang.UnsupportedOperationException();
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Short) key).shortValue(),
                ((Short) value).shortValue()
                );
    }

    @Override
    public boolean remove(short key, short value) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean removeIf(ShortShortPredicate filter) {
        throw new java.lang.UnsupportedOperationException();
    }






    class EntryView extends AbstractSetView<Map.Entry<Short, Short>>
            implements HashObjSet<Map.Entry<Short, Short>>,
            InternalObjCollectionOps<Map.Entry<Short, Short>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Short, Short>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Short>defaultEquality()
                    ,
                    Equivalence.<Short>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return ImmutableLHashParallelKVShortShortMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return ImmutableLHashParallelKVShortShortMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return ImmutableLHashParallelKVShortShortMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Short, Short> e = (Map.Entry<Short, Short>) o;
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
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    result[resultIndex++] = new ImmutableEntry(key, (short) (entry >>> 16));
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
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    a[resultIndex++] = (T) new ImmutableEntry(key, (short) (entry >>> 16));
                }
            }
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }

        @Override
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Short, Short>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    action.accept(new ImmutableEntry(key, (short) (entry >>> 16)));
                }
            }
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Short, Short>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    if (!predicate.test(new ImmutableEntry(key, (short) (entry >>> 16)))) {
                        terminated = true;
                        break;
                    }
                }
            }
            return !terminated;
        }

        @Override
        @Nonnull
        public ObjIterator<Map.Entry<Short, Short>> iterator() {
            
            return new NoRemovedEntryIterator();
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Short, Short>> cursor() {
            
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
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    if (!c.contains(e.with(key, (short) (entry >>> 16)))) {
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
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    changed |= s.remove(e.with(key, (short) (entry >>> 16)));
                }
            }
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Short, Short>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    changed |= c.add(new ImmutableEntry(key, (short) (entry >>> 16)));
                }
            }
            return changed;
        }


        public int hashCode() {
            return ImmutableLHashParallelKVShortShortMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    sb.append(' ');
                    sb.append(key);
                    sb.append('=');
                    sb.append((short) (entry >>> 16));
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
            return ImmutableLHashParallelKVShortShortMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Short, Short> e = (Map.Entry<Short, Short>) o;
                short key = e.getKey();
                short value = e.getValue();
                return ImmutableLHashParallelKVShortShortMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Short, Short>> filter) {
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
            ImmutableLHashParallelKVShortShortMapGO.this.clear();
        }
    }


    abstract class ShortShortEntry extends AbstractEntry<Short, Short> {

        abstract short key();

        @Override
        public final Short getKey() {
            return key();
        }

        abstract short value();

        @Override
        public final Short getValue() {
            return value();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object o) {
            Map.Entry e2;
            short k2;
            short v2;
            try {
                e2 = (Map.Entry) o;
                k2 = (Short) e2.getKey();
                v2 = (Short) e2.getValue();
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


    private class ImmutableEntry extends ShortShortEntry {
        private final short key;
        private final short value;

        ImmutableEntry(short key, short value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public short key() {
            return key;
        }

        @Override
        public short value() {
            return value;
        }
    }


    class ReusableEntry extends ShortShortEntry {
        private short key;
        private short value;

        ReusableEntry with(short key, short value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public short key() {
            return key;
        }

        @Override
        public short value() {
            return value;
        }
    }


    class ValueView extends AbstractShortValueView {


        @Override
        public int size() {
            return ImmutableLHashParallelKVShortShortMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return ImmutableLHashParallelKVShortShortMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return ImmutableLHashParallelKVShortShortMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(short v) {
            return ImmutableLHashParallelKVShortShortMapGO.this.containsValue(v);
        }



        @Override
        public void forEach(Consumer<? super Short> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    action.accept((short) (entry >>> 16));
                }
            }
        }

        @Override
        public void forEach(ShortConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    action.accept((short) (entry >>> 16));
                }
            }
        }

        @Override
        public boolean forEachWhile(ShortPredicate predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    if (!predicate.test((short) (entry >>> 16))) {
                        terminated = true;
                        break;
                    }
                }
            }
            return !terminated;
        }

        @Override
        public boolean allContainingIn(ShortCollection c) {
            if (isEmpty())
                return true;
            boolean containsAll = true;
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    if (!c.contains((short) (entry >>> 16))) {
                        containsAll = false;
                        break;
                    }
                }
            }
            return containsAll;
        }


        @Override
        public boolean reverseAddAllTo(ShortCollection c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    changed |= c.add((short) (entry >>> 16));
                }
            }
            return changed;
        }


        @Override
        public boolean reverseRemoveAllFrom(ShortSet s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    changed |= s.removeShort((short) (entry >>> 16));
                }
            }
            return changed;
        }



        @Override
        @Nonnull
        public ShortIterator iterator() {
            
            return new NoRemovedValueIterator();
        }

        @Nonnull
        @Override
        public ShortCursor cursor() {
            
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
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    result[resultIndex++] = (short) (entry >>> 16);
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
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    a[resultIndex++] = (T) Short.valueOf((short) (entry >>> 16));
                }
            }
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }

        @Override
        public short[] toShortArray() {
            int size = size();
            short[] result = new short[size];
            if (size == 0)
                return result;
            int resultIndex = 0;
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    result[resultIndex++] = (short) (entry >>> 16);
                }
            }
            return result;
        }

        @Override
        public short[] toArray(short[] a) {
            int size = size();
            if (a.length < size)
                a = new short[size];
            if (size == 0) {
                if (a.length > 0)
                    a[0] = (short) 0;
                return a;
            }
            int resultIndex = 0;
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    a[resultIndex++] = (short) (entry >>> 16);
                }
            }
            if (a.length > resultIndex)
                a[resultIndex] = (short) 0;
            return a;
        }


        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    sb.append(' ').append((short) (entry >>> 16)).append(',');
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
            return removeShort(( Short ) o);
        }

        @Override
        public boolean removeShort(short v) {
            return removeValue(v);
        }



        @Override
        public void clear() {
            ImmutableLHashParallelKVShortShortMapGO.this.clear();
        }

        
        public boolean removeIf(Predicate<? super Short> filter) {
            throw new java.lang.UnsupportedOperationException();
        }

        @Override
        public boolean removeIf(ShortPredicate filter) {
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Short, Short>> {
        final int[] tab;
        final short free;
        int nextIndex;
        ImmutableEntry next;

        NoRemovedEntryIterator() {
            int[] tab = this.tab = table;
            short free = this.free = freeValue;
            int nextI = tab.length;
            int entry;
            while (--nextI >= 0) {
                short key;
                if ((key = (short) (entry = tab[nextI])) != free) {
                    next = new ImmutableEntry(key, (short) (entry >>> 16));
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Short, Short>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int[] tab = this.tab;
            short free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    action.accept(new ImmutableEntry(key, (short) (entry >>> 16)));
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
        public Map.Entry<Short, Short> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int[] tab = this.tab;
                short free = this.free;
                ImmutableEntry prev = next;
                int entry;
                while (--nextI >= 0) {
                    short key;
                    if ((key = (short) (entry = tab[nextI])) != free) {
                        next = new ImmutableEntry(key, (short) (entry >>> 16));
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


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Short, Short>> {
        final int[] tab;
        final short free;
        int index;
        short curKey;
        short curValue;

        NoRemovedEntryCursor() {
            this.tab = table;
            index = tab.length;
            short free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Short, Short>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int[] tab = this.tab;
            short free = this.free;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    action.accept(new ImmutableEntry(key, (short) (entry >>> 16)));
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public Map.Entry<Short, Short> elem() {
            short curKey;
            if ((curKey = this.curKey) != free) {
                return new ImmutableEntry(curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            int[] tab = this.tab;
            short free = this.free;
            int entry;
            for (int i = index - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    index = i;
                    curKey = key;
                    curValue = (short) (entry >>> 16);
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




    class NoRemovedValueIterator implements ShortIterator {
        final int[] tab;
        final short free;
        int nextIndex;
        short next;

        NoRemovedValueIterator() {
            int[] tab = this.tab = table;
            short free = this.free = freeValue;
            int nextI = tab.length;
            int entry;
            while (--nextI >= 0) {
                if ((short) (entry = tab[nextI]) != free) {
                    next = (short) (entry >>> 16);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public short nextShort() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int[] tab = this.tab;
                short free = this.free;
                short prev = next;
                int entry;
                while (--nextI >= 0) {
                    if ((short) (entry = tab[nextI]) != free) {
                        next = (short) (entry >>> 16);
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
        public void forEachRemaining(Consumer<? super Short> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int[] tab = this.tab;
            short free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    action.accept((short) (entry >>> 16));
                }
            }
            if (nextI != nextIndex) {
                throw new java.util.ConcurrentModificationException();
            }
            nextIndex = -1;
        }

        @Override
        public void forEachRemaining(ShortConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int[] tab = this.tab;
            short free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    action.accept((short) (entry >>> 16));
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
        public Short next() {
            return nextShort();
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedValueCursor implements ShortCursor {
        final int[] tab;
        final short free;
        int index;
        short curKey;
        short curValue;

        NoRemovedValueCursor() {
            this.tab = table;
            index = tab.length;
            short free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ShortConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int[] tab = this.tab;
            short free = this.free;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    action.accept((short) (entry >>> 16));
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public short elem() {
            if (curKey != free) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            int[] tab = this.tab;
            short free = this.free;
            int entry;
            for (int i = index - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    index = i;
                    curKey = key;
                    curValue = (short) (entry >>> 16);
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



    class NoRemovedMapCursor implements ShortShortCursor {
        final int[] tab;
        final short free;
        int index;
        short curKey;
        short curValue;

        NoRemovedMapCursor() {
            this.tab = table;
            index = tab.length;
            short free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ShortShortConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int[] tab = this.tab;
            short free = this.free;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    action.accept(key, (short) (entry >>> 16));
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
        public short value() {
            if (curKey != free) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(short value) {
            if (curKey != free) {
                U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), value);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            int[] tab = this.tab;
            short free = this.free;
            int entry;
            for (int i = index - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    index = i;
                    curKey = key;
                    curValue = (short) (entry >>> 16);
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

