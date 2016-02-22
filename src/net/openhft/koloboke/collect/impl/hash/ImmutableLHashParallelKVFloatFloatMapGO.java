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
import net.openhft.koloboke.function.FloatPredicate;
import net.openhft.koloboke.function.FloatFloatConsumer;
import net.openhft.koloboke.function.FloatFloatPredicate;
import net.openhft.koloboke.function.FloatFloatToFloatFunction;
import net.openhft.koloboke.function.FloatUnaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.openhft.koloboke.function.FloatBinaryOperator;
import net.openhft.koloboke.function.FloatConsumer;
import net.openhft.koloboke.function.FloatPredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class ImmutableLHashParallelKVFloatFloatMapGO
        extends ImmutableLHashParallelKVFloatFloatMapSO {

    
    final void copy(ParallelKVFloatFloatLHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    
    final void move(ParallelKVFloatFloatLHash hash) {
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
    public boolean containsEntry(float key, float value) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
            // key is present
            return (int) (entry >>> 32) == Float.floatToIntBits(value);
        } else {
            if (cur == FREE_BITS) {
                // key is absent
                return false;
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        // key is present
                        return (int) (entry >>> 32) == Float.floatToIntBits(value);
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return false;
                    }
                }
            }
        }
    }

    @Override
    public boolean containsEntry(int key, int value) {
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
            // key is present
            return (int) (entry >>> 32) == value;
        } else {
            if (cur == FREE_BITS) {
                // key is absent
                return false;
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                        // key is present
                        return (int) (entry >>> 32) == value;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return false;
                    }
                }
            }
        }
    }

    @Override
    public Float get(Object key) {
        int k = Float.floatToIntBits((Float) key);
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
            // key is present
            return Float.intBitsToFloat((int) (entry >>> 32));
        } else {
            if (cur == FREE_BITS) {
                // key is absent
                return null;
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        // key is present
                        return Float.intBitsToFloat((int) (entry >>> 32));
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return null;
                    }
                }
            }
        }
    }

    

    @Override
    public float get(float key) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
            // key is present
            return Float.intBitsToFloat((int) (entry >>> 32));
        } else {
            if (cur == FREE_BITS) {
                // key is absent
                return defaultValue();
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        // key is present
                        return Float.intBitsToFloat((int) (entry >>> 32));
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return defaultValue();
                    }
                }
            }
        }
    }

    @Override
    public Float getOrDefault(Object key, Float defaultValue) {
        int k = Float.floatToIntBits((Float) key);
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
            // key is present
            return Float.intBitsToFloat((int) (entry >>> 32));
        } else {
            if (cur == FREE_BITS) {
                // key is absent
                return defaultValue;
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        // key is present
                        return Float.intBitsToFloat((int) (entry >>> 32));
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return defaultValue;
                    }
                }
            }
        }
    }

    @Override
    public float getOrDefault(float key, float defaultValue) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
            // key is present
            return Float.intBitsToFloat((int) (entry >>> 32));
        } else {
            if (cur == FREE_BITS) {
                // key is absent
                return defaultValue;
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        // key is present
                        return Float.intBitsToFloat((int) (entry >>> 32));
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return defaultValue;
                    }
                }
            }
        }
    }

    @Override
    public void forEach(BiConsumer<? super Float, ? super Float> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                action.accept(Float.intBitsToFloat(key), Float.intBitsToFloat((int) (entry >>> 32)));
            }
        }
    }

    @Override
    public void forEach(FloatFloatConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                action.accept(Float.intBitsToFloat(key), Float.intBitsToFloat((int) (entry >>> 32)));
            }
        }
    }

    @Override
    public boolean forEachWhile(FloatFloatPredicate predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                if (!predicate.test(Float.intBitsToFloat(key), Float.intBitsToFloat((int) (entry >>> 32)))) {
                    terminated = true;
                    break;
                }
            }
        }
        return !terminated;
    }

    @Nonnull
    @Override
    public FloatFloatCursor cursor() {
        
        return new NoRemovedMapCursor();
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonFloatFloatMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalFloatFloatMapOps m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                if (!m.containsEntry(key, (int) (entry >>> 32))) {
                    containsAll = false;
                    break;
                }
            }
        }
        return containsAll;
    }

    @Override
    public void reversePutAllTo(InternalFloatFloatMapOps m) {
        if (isEmpty())
            return;
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                m.justPut(key, (int) (entry >>> 32));
            }
        }
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<Float, Float>> entrySet() {
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
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                hashCode += key ^ (int) (entry >>> 32);
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
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                sb.append(' ');
                sb.append(Float.intBitsToFloat(key));
                sb.append('=');
                sb.append(Float.intBitsToFloat((int) (entry >>> 32)));
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
    public Float put(Float key, Float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float put(float key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Float putIfAbsent(Float key, Float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float putIfAbsent(float key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void justPut(float key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void justPut(int key, int value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Float compute(Float key,
            BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public float compute(float key, FloatFloatToFloatFunction remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public Float computeIfAbsent(Float key,
            Function<? super Float, ? extends Float> mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public float computeIfAbsent(float key, FloatUnaryOperator mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public Float computeIfPresent(Float key,
            BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float computeIfPresent(float key, FloatFloatToFloatFunction remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Float merge(Float key, Float value,
            BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public float merge(float key, float value, FloatBinaryOperator remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public float addValue(float key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float addValue(float key, float addition, float defaultValue) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public void putAll(@Nonnull Map<? extends Float, ? extends Float> m) {
        CommonFloatFloatMapOps.putAll(this, m);
    }


    @Override
    public Float replace(Float key, Float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float replace(float key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean replace(Float key, Float oldValue, Float newValue) {
        return replace(key.floatValue(),
                oldValue.floatValue(),
                newValue.floatValue());
    }

    @Override
    public boolean replace(float key, float oldValue, float newValue) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public void replaceAll(
            BiFunction<? super Float, ? super Float, ? extends Float> function) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void replaceAll(FloatFloatToFloatFunction function) {
        throw new java.lang.UnsupportedOperationException();
    }





    @Override
    public Float remove(Object key) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean justRemove(float key) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean justRemove(int key) {
        throw new java.lang.UnsupportedOperationException();
    }


    

    @Override
    public float remove(float key) {
        throw new java.lang.UnsupportedOperationException();
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Float) key).floatValue(),
                ((Float) value).floatValue()
                );
    }

    @Override
    public boolean remove(float key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean removeIf(FloatFloatPredicate filter) {
        throw new java.lang.UnsupportedOperationException();
    }






    class EntryView extends AbstractSetView<Map.Entry<Float, Float>>
            implements HashObjSet<Map.Entry<Float, Float>>,
            InternalObjCollectionOps<Map.Entry<Float, Float>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Float, Float>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Float>defaultEquality()
                    ,
                    Equivalence.<Float>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return ImmutableLHashParallelKVFloatFloatMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return ImmutableLHashParallelKVFloatFloatMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return ImmutableLHashParallelKVFloatFloatMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Float, Float> e = (Map.Entry<Float, Float>) o;
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    result[resultIndex++] = new ImmutableEntry(key, (int) (entry >>> 32));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    a[resultIndex++] = (T) new ImmutableEntry(key, (int) (entry >>> 32));
                }
            }
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }

        @Override
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Float, Float>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    action.accept(new ImmutableEntry(key, (int) (entry >>> 32)));
                }
            }
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Float, Float>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    if (!predicate.test(new ImmutableEntry(key, (int) (entry >>> 32)))) {
                        terminated = true;
                        break;
                    }
                }
            }
            return !terminated;
        }

        @Override
        @Nonnull
        public ObjIterator<Map.Entry<Float, Float>> iterator() {
            
            return new NoRemovedEntryIterator();
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Float, Float>> cursor() {
            
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    if (!c.contains(e.with(key, (int) (entry >>> 32)))) {
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    changed |= s.remove(e.with(key, (int) (entry >>> 32)));
                }
            }
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Float, Float>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    changed |= c.add(new ImmutableEntry(key, (int) (entry >>> 32)));
                }
            }
            return changed;
        }


        public int hashCode() {
            return ImmutableLHashParallelKVFloatFloatMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    sb.append(' ');
                    sb.append(Float.intBitsToFloat(key));
                    sb.append('=');
                    sb.append(Float.intBitsToFloat((int) (entry >>> 32)));
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
            return ImmutableLHashParallelKVFloatFloatMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Float, Float> e = (Map.Entry<Float, Float>) o;
                float key = e.getKey();
                float value = e.getValue();
                return ImmutableLHashParallelKVFloatFloatMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Float, Float>> filter) {
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
            ImmutableLHashParallelKVFloatFloatMapGO.this.clear();
        }
    }


    abstract class FloatFloatEntry extends AbstractEntry<Float, Float> {

        abstract int key();

        @Override
        public final Float getKey() {
            return Float.intBitsToFloat(key());
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
            int k2;
            int v2;
            try {
                e2 = (Map.Entry) o;
                k2 = Float.floatToIntBits((Float) e2.getKey());
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


    private class ImmutableEntry extends FloatFloatEntry {
        private final int key;
        private final int value;

        ImmutableEntry(int key, int value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public int key() {
            return key;
        }

        @Override
        public int value() {
            return value;
        }
    }


    class ReusableEntry extends FloatFloatEntry {
        private int key;
        private int value;

        ReusableEntry with(int key, int value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public int key() {
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
            return ImmutableLHashParallelKVFloatFloatMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return ImmutableLHashParallelKVFloatFloatMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return ImmutableLHashParallelKVFloatFloatMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(float v) {
            return ImmutableLHashParallelKVFloatFloatMapGO.this.containsValue(v);
        }

        @Override
        public boolean contains(int bits) {
            return ImmutableLHashParallelKVFloatFloatMapGO.this.containsValue(bits);
        }


        @Override
        public void forEach(Consumer<? super Float> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    action.accept(Float.intBitsToFloat((int) (entry >>> 32)));
                }
            }
        }

        @Override
        public void forEach(FloatConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    action.accept(Float.intBitsToFloat((int) (entry >>> 32)));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    if (!predicate.test(Float.intBitsToFloat((int) (entry >>> 32)))) {
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    if (!c.contains(Float.intBitsToFloat((int) (entry >>> 32)))) {
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    if (!c.contains((int) (entry >>> 32))) {
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    changed |= c.add(Float.intBitsToFloat((int) (entry >>> 32)));
                }
            }
            return changed;
        }

        private boolean reverseAddAllTo(InternalFloatCollectionOps c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    changed |= c.add((int) (entry >>> 32));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    changed |= s.removeFloat(Float.intBitsToFloat((int) (entry >>> 32)));
                }
            }
            return changed;
        }

        private boolean reverseRemoveAllFrom(InternalFloatCollectionOps s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    changed |= s.removeFloat((int) (entry >>> 32));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    result[resultIndex++] = Float.intBitsToFloat((int) (entry >>> 32));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    a[resultIndex++] = (T) Float.valueOf(Float.intBitsToFloat((int) (entry >>> 32)));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    result[resultIndex++] = Float.intBitsToFloat((int) (entry >>> 32));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    a[resultIndex++] = Float.intBitsToFloat((int) (entry >>> 32));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    sb.append(' ').append(Float.intBitsToFloat((int) (entry >>> 32))).append(',');
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
            ImmutableLHashParallelKVFloatFloatMapGO.this.clear();
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Float, Float>> {
        final long[] tab;
        int nextIndex;
        ImmutableEntry next;

        NoRemovedEntryIterator() {
            long[] tab = this.tab = table;
            int nextI = tab.length;
            long entry;
            while (--nextI >= 0) {
                int key;
                if ((key = (int) (entry = tab[nextI])) < FREE_BITS) {
                    next = new ImmutableEntry(key, (int) (entry >>> 32));
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Float, Float>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            long[] tab = this.tab;
            long entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    action.accept(new ImmutableEntry(key, (int) (entry >>> 32)));
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
        public Map.Entry<Float, Float> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                long[] tab = this.tab;
                ImmutableEntry prev = next;
                long entry;
                while (--nextI >= 0) {
                    int key;
                    if ((key = (int) (entry = tab[nextI])) < FREE_BITS) {
                        next = new ImmutableEntry(key, (int) (entry >>> 32));
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


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Float, Float>> {
        final long[] tab;
        int index;
        int curKey;
        int curValue;

        NoRemovedEntryCursor() {
            this.tab = table;
            index = tab.length;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Float, Float>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            long[] tab = this.tab;
            long entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    action.accept(new ImmutableEntry(key, (int) (entry >>> 32)));
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE_BITS;
        }

        @Override
        public Map.Entry<Float, Float> elem() {
            int curKey;
            if ((curKey = this.curKey) != FREE_BITS) {
                return new ImmutableEntry(curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            long[] tab = this.tab;
            long entry;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    index = i;
                    curKey = key;
                    curValue = (int) (entry >>> 32);
                    return true;
                }
            }
            curKey = FREE_BITS;
            index = -1;
            return false;
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }




    class NoRemovedValueIterator implements FloatIterator {
        final long[] tab;
        int nextIndex;
        float next;

        NoRemovedValueIterator() {
            long[] tab = this.tab = table;
            int nextI = tab.length;
            long entry;
            while (--nextI >= 0) {
                if ((int) (entry = tab[nextI]) < FREE_BITS) {
                    next = Float.intBitsToFloat((int) (entry >>> 32));
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public float nextFloat() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                long[] tab = this.tab;
                float prev = next;
                long entry;
                while (--nextI >= 0) {
                    if ((int) (entry = tab[nextI]) < FREE_BITS) {
                        next = Float.intBitsToFloat((int) (entry >>> 32));
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
            long[] tab = this.tab;
            long entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    action.accept(Float.intBitsToFloat((int) (entry >>> 32)));
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
            long[] tab = this.tab;
            long entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    action.accept(Float.intBitsToFloat((int) (entry >>> 32)));
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
        final long[] tab;
        int index;
        int curKey;
        int curValue;

        NoRemovedValueCursor() {
            this.tab = table;
            index = tab.length;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(FloatConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            long[] tab = this.tab;
            long entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    action.accept(Float.intBitsToFloat((int) (entry >>> 32)));
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE_BITS;
        }

        @Override
        public float elem() {
            if (curKey != FREE_BITS) {
                return Float.intBitsToFloat(curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            long[] tab = this.tab;
            long entry;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    index = i;
                    curKey = key;
                    curValue = (int) (entry >>> 32);
                    return true;
                }
            }
            curKey = FREE_BITS;
            index = -1;
            return false;
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }



    class NoRemovedMapCursor implements FloatFloatCursor {
        final long[] tab;
        int index;
        int curKey;
        int curValue;

        NoRemovedMapCursor() {
            this.tab = table;
            index = tab.length;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(FloatFloatConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            long[] tab = this.tab;
            long entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    action.accept(Float.intBitsToFloat(key), Float.intBitsToFloat((int) (entry >>> 32)));
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE_BITS;
        }

        @Override
        public float key() {
            int curKey;
            if ((curKey = this.curKey) != FREE_BITS) {
                return Float.intBitsToFloat(curKey);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public float value() {
            if (curKey != FREE_BITS) {
                return Float.intBitsToFloat(curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(float value) {
            if (curKey != FREE_BITS) {
                U.putInt(tab, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), Float.floatToIntBits(value));
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            long[] tab = this.tab;
            long entry;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    index = i;
                    curKey = key;
                    curValue = (int) (entry >>> 32);
                    return true;
                }
            }
            curKey = FREE_BITS;
            index = -1;
            return false;
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }
}

