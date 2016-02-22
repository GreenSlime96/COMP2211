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
import java.util.function.LongPredicate;
import net.openhft.koloboke.function.LongFloatConsumer;
import net.openhft.koloboke.function.LongFloatPredicate;
import net.openhft.koloboke.function.LongFloatToFloatFunction;
import net.openhft.koloboke.function.LongToFloatFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.openhft.koloboke.function.FloatBinaryOperator;
import net.openhft.koloboke.function.FloatConsumer;
import net.openhft.koloboke.function.FloatPredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class ImmutableQHashSeparateKVLongFloatMapGO
        extends ImmutableQHashSeparateKVLongFloatMapSO {

    @Override
    final void copy(SeparateKVLongFloatQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVLongFloatQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.move(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }


    @Override
    public float defaultValue() {
        return 0.0f;
    }

    @Override
    public boolean containsEntry(long key, float value) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return values[index] == Float.floatToIntBits(value);
        } else {
            // key is absent
            return false;
        }
    }

    @Override
    public boolean containsEntry(long key, int value) {
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
    public Float get(Object key) {
        int index = index((Long) key);
        if (index >= 0) {
            // key is present
            return Float.intBitsToFloat(values[index]);
        } else {
            // key is absent
            return null;
        }
    }

    

    @Override
    public float get(long key) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return Float.intBitsToFloat(values[index]);
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Float getOrDefault(Object key, Float defaultValue) {
        int index = index((Long) key);
        if (index >= 0) {
            // key is present
            return Float.intBitsToFloat(values[index]);
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public float getOrDefault(long key, float defaultValue) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return Float.intBitsToFloat(values[index]);
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public void forEach(BiConsumer<? super Long, ? super Float> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        long free = freeValue;
        long[] keys = set;
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) != free) {
                action.accept(key, Float.intBitsToFloat(vals[i]));
            }
        }
    }

    @Override
    public void forEach(LongFloatConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        long free = freeValue;
        long[] keys = set;
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) != free) {
                action.accept(key, Float.intBitsToFloat(vals[i]));
            }
        }
    }

    @Override
    public boolean forEachWhile(LongFloatPredicate predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        long free = freeValue;
        long[] keys = set;
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) != free) {
                if (!predicate.test(key, Float.intBitsToFloat(vals[i]))) {
                    terminated = true;
                    break;
                }
            }
        }
        return !terminated;
    }

    @Nonnull
    @Override
    public LongFloatCursor cursor() {
        
        return new NoRemovedMapCursor();
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonLongFloatMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalLongFloatMapOps m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        long free = freeValue;
        long[] keys = set;
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
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
    public void reversePutAllTo(InternalLongFloatMapOps m) {
        if (isEmpty())
            return;
        long free = freeValue;
        long[] keys = set;
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) != free) {
                m.justPut(key, vals[i]);
            }
        }
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<Long, Float>> entrySet() {
        return new EntryView();
    }

    @Override
    @Nonnull
    public FloatCollection values() {
        return new ValueView();
    }


    @Override
    public boolean equals(Object o) {
        return CommonMapOps.equals(this, o);
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        long free = freeValue;
        long[] keys = set;
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) != free) {
                hashCode += ((int) (key ^ (key >>> 32))) ^ vals[i];
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
        long free = freeValue;
        long[] keys = set;
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) != free) {
                sb.append(' ');
                sb.append(key);
                sb.append('=');
                sb.append(Float.intBitsToFloat(vals[i]));
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
    public Float put(Long key, Float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float put(long key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Float putIfAbsent(Long key, Float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float putIfAbsent(long key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void justPut(long key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void justPut(long key, int value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Float compute(Long key,
            BiFunction<? super Long, ? super Float, ? extends Float> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public float compute(long key, LongFloatToFloatFunction remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public Float computeIfAbsent(Long key,
            Function<? super Long, ? extends Float> mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public float computeIfAbsent(long key, LongToFloatFunction mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public Float computeIfPresent(Long key,
            BiFunction<? super Long, ? super Float, ? extends Float> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float computeIfPresent(long key, LongFloatToFloatFunction remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Float merge(Long key, Float value,
            BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public float merge(long key, float value, FloatBinaryOperator remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public float addValue(long key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float addValue(long key, float addition, float defaultValue) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public void putAll(@Nonnull Map<? extends Long, ? extends Float> m) {
        CommonLongFloatMapOps.putAll(this, m);
    }


    @Override
    public Float replace(Long key, Float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float replace(long key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean replace(Long key, Float oldValue, Float newValue) {
        return replace(key.longValue(),
                oldValue.floatValue(),
                newValue.floatValue());
    }

    @Override
    public boolean replace(long key, float oldValue, float newValue) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public void replaceAll(
            BiFunction<? super Long, ? super Float, ? extends Float> function) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void replaceAll(LongFloatToFloatFunction function) {
        throw new java.lang.UnsupportedOperationException();
    }





    @Override
    public Float remove(Object key) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean justRemove(long key) {
        throw new java.lang.UnsupportedOperationException();
    }



    

    @Override
    public float remove(long key) {
        throw new java.lang.UnsupportedOperationException();
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Long) key).longValue(),
                ((Float) value).floatValue()
                );
    }

    @Override
    public boolean remove(long key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean removeIf(LongFloatPredicate filter) {
        throw new java.lang.UnsupportedOperationException();
    }






    class EntryView extends AbstractSetView<Map.Entry<Long, Float>>
            implements HashObjSet<Map.Entry<Long, Float>>,
            InternalObjCollectionOps<Map.Entry<Long, Float>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Long, Float>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Long>defaultEquality()
                    ,
                    Equivalence.<Float>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return ImmutableQHashSeparateKVLongFloatMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return ImmutableQHashSeparateKVLongFloatMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return ImmutableQHashSeparateKVLongFloatMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Long, Float> e = (Map.Entry<Long, Float>) o;
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
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
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
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free) {
                    a[resultIndex++] = (T) new ImmutableEntry(key, vals[i]);
                }
            }
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }

        @Override
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Long, Float>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free) {
                    action.accept(new ImmutableEntry(key, vals[i]));
                }
            }
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Long, Float>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
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
        public ObjIterator<Map.Entry<Long, Float>> iterator() {
            
            return new NoRemovedEntryIterator();
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Long, Float>> cursor() {
            
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
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
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
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free) {
                    changed |= s.remove(e.with(key, vals[i]));
                }
            }
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Long, Float>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free) {
                    changed |= c.add(new ImmutableEntry(key, vals[i]));
                }
            }
            return changed;
        }


        public int hashCode() {
            return ImmutableQHashSeparateKVLongFloatMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free) {
                    sb.append(' ');
                    sb.append(key);
                    sb.append('=');
                    sb.append(Float.intBitsToFloat(vals[i]));
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
            return ImmutableQHashSeparateKVLongFloatMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Long, Float> e = (Map.Entry<Long, Float>) o;
                long key = e.getKey();
                float value = e.getValue();
                return ImmutableQHashSeparateKVLongFloatMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Long, Float>> filter) {
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
            ImmutableQHashSeparateKVLongFloatMapGO.this.clear();
        }
    }


    abstract class LongFloatEntry extends AbstractEntry<Long, Float> {

        abstract long key();

        @Override
        public final Long getKey() {
            return key();
        }

        abstract int value();

        @Override
        public final Float getValue() {
            return Float.intBitsToFloat(value());
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object o) {
            Map.Entry e2;
            long k2;
            int v2;
            try {
                e2 = (Map.Entry) o;
                k2 = (Long) e2.getKey();
                v2 = Float.floatToIntBits((Float) e2.getValue());
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


    private class ImmutableEntry extends LongFloatEntry {
        private final long key;
        private final int value;

        ImmutableEntry(long key, int value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public long key() {
            return key;
        }

        @Override
        public int value() {
            return value;
        }
    }


    class ReusableEntry extends LongFloatEntry {
        private long key;
        private int value;

        ReusableEntry with(long key, int value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public long key() {
            return key;
        }

        @Override
        public int value() {
            return value;
        }
    }


    class ValueView extends AbstractFloatValueView {


        @Override
        public int size() {
            return ImmutableQHashSeparateKVLongFloatMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return ImmutableQHashSeparateKVLongFloatMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return ImmutableQHashSeparateKVLongFloatMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(float v) {
            return ImmutableQHashSeparateKVLongFloatMapGO.this.containsValue(v);
        }

        @Override
        public boolean contains(int bits) {
            return ImmutableQHashSeparateKVLongFloatMapGO.this.containsValue(bits);
        }


        @Override
        public void forEach(Consumer<? super Float> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    action.accept(Float.intBitsToFloat(vals[i]));
                }
            }
        }

        @Override
        public void forEach(FloatConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    action.accept(Float.intBitsToFloat(vals[i]));
                }
            }
        }

        @Override
        public boolean forEachWhile(FloatPredicate predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    if (!predicate.test(Float.intBitsToFloat(vals[i]))) {
                        terminated = true;
                        break;
                    }
                }
            }
            return !terminated;
        }

        @Override
        public boolean allContainingIn(FloatCollection c) {
            if (c instanceof InternalFloatCollectionOps)
                return allContainingIn((InternalFloatCollectionOps) c);
            if (isEmpty())
                return true;
            boolean containsAll = true;
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    if (!c.contains(Float.intBitsToFloat(vals[i]))) {
                        containsAll = false;
                        break;
                    }
                }
            }
            return containsAll;
        }

        private boolean allContainingIn(InternalFloatCollectionOps c) {
            if (isEmpty())
                return true;
            boolean containsAll = true;
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
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
        public boolean reverseAddAllTo(FloatCollection c) {
            if (c instanceof InternalFloatCollectionOps)
                return reverseAddAllTo((InternalFloatCollectionOps) c);
            if (isEmpty())
                return false;
            boolean changed = false;
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    changed |= c.add(Float.intBitsToFloat(vals[i]));
                }
            }
            return changed;
        }

        private boolean reverseAddAllTo(InternalFloatCollectionOps c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    changed |= c.add(vals[i]);
                }
            }
            return changed;
        }

        @Override
        public boolean reverseRemoveAllFrom(FloatSet s) {
            if (s instanceof InternalFloatCollectionOps)
                return reverseRemoveAllFrom((InternalFloatCollectionOps) s);
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    changed |= s.removeFloat(Float.intBitsToFloat(vals[i]));
                }
            }
            return changed;
        }

        private boolean reverseRemoveAllFrom(InternalFloatCollectionOps s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    changed |= s.removeFloat(vals[i]);
                }
            }
            return changed;
        }


        @Override
        @Nonnull
        public FloatIterator iterator() {
            
            return new NoRemovedValueIterator();
        }

        @Nonnull
        @Override
        public FloatCursor cursor() {
            
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
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    result[resultIndex++] = Float.intBitsToFloat(vals[i]);
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
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    a[resultIndex++] = (T) Float.valueOf(Float.intBitsToFloat(vals[i]));
                }
            }
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }

        @Override
        public float[] toFloatArray() {
            int size = size();
            float[] result = new float[size];
            if (size == 0)
                return result;
            int resultIndex = 0;
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    result[resultIndex++] = Float.intBitsToFloat(vals[i]);
                }
            }
            return result;
        }

        @Override
        public float[] toArray(float[] a) {
            int size = size();
            if (a.length < size)
                a = new float[size];
            if (size == 0) {
                if (a.length > 0)
                    a[0] = 0.0f;
                return a;
            }
            int resultIndex = 0;
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    a[resultIndex++] = Float.intBitsToFloat(vals[i]);
                }
            }
            if (a.length > resultIndex)
                a[resultIndex] = 0.0f;
            return a;
        }


        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            long free = freeValue;
            long[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    sb.append(' ').append(Float.intBitsToFloat(vals[i])).append(',');
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
            return removeFloat(( Float ) o);
        }

        @Override
        public boolean removeFloat(float v) {
            return removeValue(v);
        }

        @Override
        public boolean removeFloat(int bits) {
            return removeValue(bits);
        }


        @Override
        public void clear() {
            ImmutableQHashSeparateKVLongFloatMapGO.this.clear();
        }

        
        public boolean removeIf(Predicate<? super Float> filter) {
            throw new java.lang.UnsupportedOperationException();
        }

        @Override
        public boolean removeIf(FloatPredicate filter) {
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Long, Float>> {
        final long[] keys;
        final int[] vals;
        final long free;
        int nextIndex;
        ImmutableEntry next;

        NoRemovedEntryIterator() {
            long[] keys = this.keys = set;
            int[] vals = this.vals = values;
            long free = this.free = freeValue;
            int nextI = keys.length;
            while (--nextI >= 0) {
                long key;
                if ((key = keys[nextI]) != free) {
                    next = new ImmutableEntry(key, vals[nextI]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Long, Float>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            long[] keys = this.keys;
            int[] vals = this.vals;
            long free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                long key;
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
        public Map.Entry<Long, Float> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                long[] keys = this.keys;
                long free = this.free;
                ImmutableEntry prev = next;
                while (--nextI >= 0) {
                    long key;
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


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Long, Float>> {
        final long[] keys;
        final int[] vals;
        final long free;
        int index;
        long curKey;
        int curValue;

        NoRemovedEntryCursor() {
            this.keys = set;
            index = keys.length;
            vals = values;
            long free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Long, Float>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            long[] keys = this.keys;
            int[] vals = this.vals;
            long free = this.free;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                long key;
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
        public Map.Entry<Long, Float> elem() {
            long curKey;
            if ((curKey = this.curKey) != free) {
                return new ImmutableEntry(curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            long[] keys = this.keys;
            long free = this.free;
            for (int i = index - 1; i >= 0; i--) {
                long key;
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




    class NoRemovedValueIterator implements FloatIterator {
        final long[] keys;
        final int[] vals;
        final long free;
        int nextIndex;
        float next;

        NoRemovedValueIterator() {
            long[] keys = this.keys = set;
            int[] vals = this.vals = values;
            long free = this.free = freeValue;
            int nextI = keys.length;
            while (--nextI >= 0) {
                if (keys[nextI] != free) {
                    next = Float.intBitsToFloat(vals[nextI]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public float nextFloat() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                long[] keys = this.keys;
                long free = this.free;
                float prev = next;
                while (--nextI >= 0) {
                    if (keys[nextI] != free) {
                        next = Float.intBitsToFloat(vals[nextI]);
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
        public void forEachRemaining(Consumer<? super Float> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            long[] keys = this.keys;
            int[] vals = this.vals;
            long free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if (keys[i] != free) {
                    action.accept(Float.intBitsToFloat(vals[i]));
                }
            }
            if (nextI != nextIndex) {
                throw new java.util.ConcurrentModificationException();
            }
            nextIndex = -1;
        }

        @Override
        public void forEachRemaining(FloatConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            long[] keys = this.keys;
            int[] vals = this.vals;
            long free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if (keys[i] != free) {
                    action.accept(Float.intBitsToFloat(vals[i]));
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
        public Float next() {
            return nextFloat();
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedValueCursor implements FloatCursor {
        final long[] keys;
        final int[] vals;
        final long free;
        int index;
        long curKey;
        int curValue;

        NoRemovedValueCursor() {
            this.keys = set;
            index = keys.length;
            vals = values;
            long free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(FloatConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            long[] keys = this.keys;
            int[] vals = this.vals;
            long free = this.free;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    action.accept(Float.intBitsToFloat(vals[i]));
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public float elem() {
            if (curKey != free) {
                return Float.intBitsToFloat(curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            long[] keys = this.keys;
            long free = this.free;
            for (int i = index - 1; i >= 0; i--) {
                long key;
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



    class NoRemovedMapCursor implements LongFloatCursor {
        final long[] keys;
        final int[] vals;
        final long free;
        int index;
        long curKey;
        int curValue;

        NoRemovedMapCursor() {
            this.keys = set;
            index = keys.length;
            vals = values;
            long free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(LongFloatConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            long[] keys = this.keys;
            int[] vals = this.vals;
            long free = this.free;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free) {
                    action.accept(key, Float.intBitsToFloat(vals[i]));
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public long key() {
            long curKey;
            if ((curKey = this.curKey) != free) {
                return curKey;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public float value() {
            if (curKey != free) {
                return Float.intBitsToFloat(curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(float value) {
            if (curKey != free) {
                vals[index] = Float.floatToIntBits(value);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            long[] keys = this.keys;
            long free = this.free;
            for (int i = index - 1; i >= 0; i--) {
                long key;
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

