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
import java.util.function.Predicate;
import net.openhft.koloboke.function.ObjFloatConsumer;
import net.openhft.koloboke.function.ObjFloatPredicate;
import net.openhft.koloboke.function.ObjFloatToFloatFunction;
import net.openhft.koloboke.function.ToFloatFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.openhft.koloboke.function.FloatBinaryOperator;
import net.openhft.koloboke.function.FloatConsumer;
import net.openhft.koloboke.function.FloatPredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class ImmutableQHashSeparateKVObjFloatMapGO<K>
        extends ImmutableQHashSeparateKVObjFloatMapSO<K> {

    @Override
    final void copy(SeparateKVObjFloatQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVObjFloatQHash hash) {
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
    public boolean containsEntry(Object key, float value) {
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
    public boolean containsEntry(Object key, int value) {
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
        int index = index(key);
        if (index >= 0) {
            // key is present
            return Float.intBitsToFloat(values[index]);
        } else {
            // key is absent
            return null;
        }
    }

    

    @Override
    public float getFloat(Object key) {
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
    public float getOrDefault(Object key, float defaultValue) {
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
    public void forEach(BiConsumer<? super K, ? super Float> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        Object[] keys = set;
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                action.accept(key, Float.intBitsToFloat(vals[i]));
            }
        }
    }

    @Override
    public void forEach(ObjFloatConsumer<? super K> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        Object[] keys = set;
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                action.accept(key, Float.intBitsToFloat(vals[i]));
            }
        }
    }

    @Override
    public boolean forEachWhile(ObjFloatPredicate<? super K> predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        Object[] keys = set;
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
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
    public ObjFloatCursor<K> cursor() {
        
        return new NoRemovedMapCursor();
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonObjFloatMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalObjFloatMapOps<?> m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        Object[] keys = set;
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                if (!m.containsEntry(key, vals[i])) {
                    containsAll = false;
                    break;
                }
            }
        }
        return containsAll;
    }

    @Override
    public void reversePutAllTo(InternalObjFloatMapOps<? super K> m) {
        if (isEmpty())
            return;
        Object[] keys = set;
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                m.justPut(key, vals[i]);
            }
        }
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<K, Float>> entrySet() {
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
        Object[] keys = set;
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                hashCode += nullableKeyHashCode(key) ^ vals[i];
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
        Object[] keys = set;
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                sb.append(' ');
                sb.append(key != this ? key : "(this Map)");
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
    public Float put(K key, Float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float put(K key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Float putIfAbsent(K key, Float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float putIfAbsent(K key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void justPut(K key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void justPut(K key, int value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Float compute(K key,
            BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    Float computeNullKey(
            BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float compute(K key, ObjFloatToFloatFunction<? super K> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    float computeNullKey(ObjFloatToFloatFunction<? super K> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Float computeIfAbsent(K key,
            Function<? super K, ? extends Float> mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    Float computeIfAbsentNullKey(
            Function<? super K, ? extends Float> mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float computeIfAbsent(K key, ToFloatFunction<? super K> mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    float computeIfAbsentNullKey(ToFloatFunction<? super K> mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Float computeIfPresent(K key,
            BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float computeIfPresent(K key, ObjFloatToFloatFunction<? super K> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Float merge(K key, Float value,
            BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    Float mergeNullKey(Float value,
            BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float merge(K key, float value, FloatBinaryOperator remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    float mergeNullKey(float value, FloatBinaryOperator remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float addValue(K key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float addValue(K key, float addition, float defaultValue) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public void putAll(@Nonnull Map<? extends K, ? extends Float> m) {
        CommonObjFloatMapOps.putAll(this, m);
    }


    @Override
    public Float replace(K key, Float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float replace(K key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean replace(K key, Float oldValue, Float newValue) {
        return replace(key,
                oldValue.floatValue(),
                newValue.floatValue());
    }

    @Override
    public boolean replace(K key, float oldValue, float newValue) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public void replaceAll(
            BiFunction<? super K, ? super Float, ? extends Float> function) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void replaceAll(ObjFloatToFloatFunction<? super K> function) {
        throw new java.lang.UnsupportedOperationException();
    }





    @Override
    public Float remove(Object key) {
        throw new java.lang.UnsupportedOperationException();
    }

    Float removeNullKey() {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean justRemove(Object key) {
        throw new java.lang.UnsupportedOperationException();
    }

    boolean justRemoveNullKey() {
        throw new java.lang.UnsupportedOperationException();
    }


    

    @Override
    public float removeAsFloat(Object key) {
        throw new java.lang.UnsupportedOperationException();
    }

    float removeAsFloatNullKey() {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean remove(Object key, Object value) {
        return remove(key,
                ((Float) value).floatValue()
                );
    }

    @Override
    public boolean remove(Object key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    boolean removeEntryNullKey(float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(ObjFloatPredicate<? super K> filter) {
        throw new java.lang.UnsupportedOperationException();
    }






    class EntryView extends AbstractSetView<Map.Entry<K, Float>>
            implements HashObjSet<Map.Entry<K, Float>>,
            InternalObjCollectionOps<Map.Entry<K, Float>> {

        @Nonnull
        @Override
        public Equivalence<Entry<K, Float>> equivalence() {
            return Equivalence.entryEquivalence(
                    keyEquivalence(),
                    Equivalence.<Float>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return ImmutableQHashSeparateKVObjFloatMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return ImmutableQHashSeparateKVObjFloatMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return ImmutableQHashSeparateKVObjFloatMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<K, Float> e = (Map.Entry<K, Float>) o;
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
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
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
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    a[resultIndex++] = (T) new ImmutableEntry(key, vals[i]);
                }
            }
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }

        @Override
        public final void forEach(@Nonnull Consumer<? super Map.Entry<K, Float>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    action.accept(new ImmutableEntry(key, vals[i]));
                }
            }
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<K, Float>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
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
        public ObjIterator<Map.Entry<K, Float>> iterator() {
            
            return new NoRemovedEntryIterator();
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<K, Float>> cursor() {
            
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
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
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
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    changed |= s.remove(e.with(key, vals[i]));
                }
            }
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<K, Float>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    changed |= c.add(new ImmutableEntry(key, vals[i]));
                }
            }
            return changed;
        }


        public int hashCode() {
            return ImmutableQHashSeparateKVObjFloatMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    sb.append(' ');
                    sb.append(key != this ? key : "(this Collection)");
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
            return ImmutableQHashSeparateKVObjFloatMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<K, Float> e = (Map.Entry<K, Float>) o;
                K key = e.getKey();
                float value = e.getValue();
                return ImmutableQHashSeparateKVObjFloatMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<K, Float>> filter) {
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
            ImmutableQHashSeparateKVObjFloatMapGO.this.clear();
        }
    }


    abstract class ObjFloatEntry extends AbstractEntry<K, Float> {

        abstract K key();

        @Override
        public final K getKey() {
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
            K k2;
            int v2;
            try {
                e2 = (Map.Entry) o;
                k2 = (K) e2.getKey();
                v2 = Float.floatToIntBits((Float) e2.getValue());
                return nullableKeyEquals(key(), k2)
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
            return nullableKeyHashCode(key())
                    ^
                    Primitives.hashCode(value())
                    ;
        }
    }


    private class ImmutableEntry extends ObjFloatEntry {
        private final K key;
        private final int value;

        ImmutableEntry(K key, int value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K key() {
            return key;
        }

        @Override
        public int value() {
            return value;
        }
    }


    class ReusableEntry extends ObjFloatEntry {
        private K key;
        private int value;

        ReusableEntry with(K key, int value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public K key() {
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
            return ImmutableQHashSeparateKVObjFloatMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return ImmutableQHashSeparateKVObjFloatMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return ImmutableQHashSeparateKVObjFloatMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(float v) {
            return ImmutableQHashSeparateKVObjFloatMapGO.this.containsValue(v);
        }

        @Override
        public boolean contains(int bits) {
            return ImmutableQHashSeparateKVObjFloatMapGO.this.containsValue(bits);
        }


        @Override
        public void forEach(Consumer<? super Float> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    changed |= c.add(Float.intBitsToFloat(vals[i]));
                }
            }
            return changed;
        }

        private boolean reverseAddAllTo(InternalFloatCollectionOps c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    changed |= s.removeFloat(Float.intBitsToFloat(vals[i]));
                }
            }
            return changed;
        }

        private boolean reverseRemoveAllFrom(InternalFloatCollectionOps s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            ImmutableQHashSeparateKVObjFloatMapGO.this.clear();
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<K, Float>> {
        final K[] keys;
        final int[] vals;
        int nextIndex;
        ImmutableEntry next;

        NoRemovedEntryIterator() {
            // noinspection unchecked
            K[] keys = this.keys = (K[]) set;
            int[] vals = this.vals = values;
            int nextI = keys.length;
            while (--nextI >= 0) {
                Object key;
                if ((key = keys[nextI]) != FREE) {
                    // noinspection unchecked
                    next = new ImmutableEntry((K) key, vals[nextI]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<K, Float>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            K[] keys = this.keys;
            int[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE) {
                    // noinspection unchecked
                    action.accept(new ImmutableEntry((K) key, vals[i]));
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
        public Map.Entry<K, Float> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                K[] keys = this.keys;
                ImmutableEntry prev = next;
                while (--nextI >= 0) {
                    Object key;
                    if ((key = keys[nextI]) != FREE) {
                        // noinspection unchecked
                        next = new ImmutableEntry((K) key, vals[nextI]);
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


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<K, Float>> {
        final K[] keys;
        final int[] vals;
        int index;
        Object curKey;
        int curValue;

        NoRemovedEntryCursor() {
            // noinspection unchecked
            this.keys = (K[]) set;
            index = keys.length;
            vals = values;
            curKey = FREE;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<K, Float>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            K[] keys = this.keys;
            int[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE) {
                    // noinspection unchecked
                    action.accept(new ImmutableEntry((K) key, vals[i]));
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE;
        }

        @Override
        public Map.Entry<K, Float> elem() {
            Object curKey;
            if ((curKey = this.curKey) != FREE) {
                // noinspection unchecked
                return new ImmutableEntry((K) curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            K[] keys = this.keys;
            for (int i = index - 1; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE) {
                    index = i;
                    curKey = key;
                    curValue = vals[i];
                    return true;
                }
            }
            curKey = FREE;
            index = -1;
            return false;
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }




    class NoRemovedValueIterator implements FloatIterator {
        final K[] keys;
        final int[] vals;
        int nextIndex;
        float next;

        NoRemovedValueIterator() {
            // noinspection unchecked
            K[] keys = this.keys = (K[]) set;
            int[] vals = this.vals = values;
            int nextI = keys.length;
            while (--nextI >= 0) {
                if (keys[nextI] != FREE) {
                    // noinspection unchecked
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
                K[] keys = this.keys;
                float prev = next;
                while (--nextI >= 0) {
                    if (keys[nextI] != FREE) {
                        // noinspection unchecked
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
            K[] keys = this.keys;
            int[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if (keys[i] != FREE) {
                    // noinspection unchecked
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
            K[] keys = this.keys;
            int[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if (keys[i] != FREE) {
                    // noinspection unchecked
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
        final K[] keys;
        final int[] vals;
        int index;
        Object curKey;
        int curValue;

        NoRemovedValueCursor() {
            // noinspection unchecked
            this.keys = (K[]) set;
            index = keys.length;
            vals = values;
            curKey = FREE;
        }

        @Override
        public void forEachForward(FloatConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            K[] keys = this.keys;
            int[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    // noinspection unchecked
                    action.accept(Float.intBitsToFloat(vals[i]));
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE;
        }

        @Override
        public float elem() {
            if (curKey != FREE) {
                return Float.intBitsToFloat(curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            K[] keys = this.keys;
            for (int i = index - 1; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE) {
                    index = i;
                    curKey = key;
                    curValue = vals[i];
                    return true;
                }
            }
            curKey = FREE;
            index = -1;
            return false;
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }



    class NoRemovedMapCursor implements ObjFloatCursor<K> {
        final K[] keys;
        final int[] vals;
        int index;
        Object curKey;
        int curValue;

        NoRemovedMapCursor() {
            // noinspection unchecked
            this.keys = (K[]) set;
            index = keys.length;
            vals = values;
            curKey = FREE;
        }

        @Override
        public void forEachForward(ObjFloatConsumer<? super K> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            K[] keys = this.keys;
            int[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE) {
                    // noinspection unchecked
                    action.accept((K) key, Float.intBitsToFloat(vals[i]));
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE;
        }

        @Override
        public K key() {
            Object curKey;
            if ((curKey = this.curKey) != FREE) {
                // noinspection unchecked
                return (K) curKey;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public float value() {
            if (curKey != FREE) {
                return Float.intBitsToFloat(curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(float value) {
            if (curKey != FREE) {
                vals[index] = Float.floatToIntBits(value);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            K[] keys = this.keys;
            for (int i = index - 1; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE) {
                    index = i;
                    curKey = key;
                    curValue = vals[i];
                    return true;
                }
            }
            curKey = FREE;
            index = -1;
            return false;
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }
}

