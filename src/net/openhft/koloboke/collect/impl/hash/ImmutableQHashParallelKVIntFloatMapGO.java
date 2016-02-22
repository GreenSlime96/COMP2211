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
import java.util.function.IntPredicate;
import net.openhft.koloboke.function.IntFloatConsumer;
import net.openhft.koloboke.function.IntFloatPredicate;
import net.openhft.koloboke.function.IntFloatToFloatFunction;
import net.openhft.koloboke.function.IntToFloatFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.openhft.koloboke.function.FloatBinaryOperator;
import net.openhft.koloboke.function.FloatConsumer;
import net.openhft.koloboke.function.FloatPredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class ImmutableQHashParallelKVIntFloatMapGO
        extends ImmutableQHashParallelKVIntFloatMapSO {

    
    final void copy(ParallelKVIntFloatQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    
    final void move(ParallelKVIntFloatQHash hash) {
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
    public boolean containsEntry(int key, float value) {
        int free;
        if (key != (free = freeValue)) {
            long[] tab = table;
            int capacity, index;
            int cur;
            long entry;
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) % (capacity = tab.length)])) == key) {
                // key is present
                return (int) (entry >>> 32) == Float.floatToIntBits(value);
            } else {
                if (cur == free) {
                    // key is absent
                    return false;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (int) (entry = tab[bIndex])) == key) {
                            // key is present
                            return (int) (entry >>> 32) == Float.floatToIntBits(value);
                        } else if (cur == free) {
                            // key is absent
                            return false;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == key) {
                            // key is present
                            return (int) (entry >>> 32) == Float.floatToIntBits(value);
                        } else if (cur == free) {
                            // key is absent
                            return false;
                        }
                        step += 2;
                    }
                }
            }
        } else {
            // key is absent
            return false;
        }
    }

    @Override
    public boolean containsEntry(int key, int value) {
        int free;
        if (key != (free = freeValue)) {
            long[] tab = table;
            int capacity, index;
            int cur;
            long entry;
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) % (capacity = tab.length)])) == key) {
                // key is present
                return (int) (entry >>> 32) == value;
            } else {
                if (cur == free) {
                    // key is absent
                    return false;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (int) (entry = tab[bIndex])) == key) {
                            // key is present
                            return (int) (entry >>> 32) == value;
                        } else if (cur == free) {
                            // key is absent
                            return false;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == key) {
                            // key is present
                            return (int) (entry >>> 32) == value;
                        } else if (cur == free) {
                            // key is absent
                            return false;
                        }
                        step += 2;
                    }
                }
            }
        } else {
            // key is absent
            return false;
        }
    }

    @Override
    public Float get(Object key) {
        int k = (Integer) key;
        int free;
        if (k != (free = freeValue)) {
            long[] tab = table;
            int capacity, index;
            int cur;
            long entry;
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) % (capacity = tab.length)])) == k) {
                // key is present
                return Float.intBitsToFloat((int) (entry >>> 32));
            } else {
                if (cur == free) {
                    // key is absent
                    return null;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (int) (entry = tab[bIndex])) == k) {
                            // key is present
                            return Float.intBitsToFloat((int) (entry >>> 32));
                        } else if (cur == free) {
                            // key is absent
                            return null;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == k) {
                            // key is present
                            return Float.intBitsToFloat((int) (entry >>> 32));
                        } else if (cur == free) {
                            // key is absent
                            return null;
                        }
                        step += 2;
                    }
                }
            }
        } else {
            // key is absent
            return null;
        }
    }

    

    @Override
    public float get(int key) {
        int free;
        if (key != (free = freeValue)) {
            long[] tab = table;
            int capacity, index;
            int cur;
            long entry;
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) % (capacity = tab.length)])) == key) {
                // key is present
                return Float.intBitsToFloat((int) (entry >>> 32));
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue();
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (int) (entry = tab[bIndex])) == key) {
                            // key is present
                            return Float.intBitsToFloat((int) (entry >>> 32));
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue();
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == key) {
                            // key is present
                            return Float.intBitsToFloat((int) (entry >>> 32));
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue();
                        }
                        step += 2;
                    }
                }
            }
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Float getOrDefault(Object key, Float defaultValue) {
        int k = (Integer) key;
        int free;
        if (k != (free = freeValue)) {
            long[] tab = table;
            int capacity, index;
            int cur;
            long entry;
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) % (capacity = tab.length)])) == k) {
                // key is present
                return Float.intBitsToFloat((int) (entry >>> 32));
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (int) (entry = tab[bIndex])) == k) {
                            // key is present
                            return Float.intBitsToFloat((int) (entry >>> 32));
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == k) {
                            // key is present
                            return Float.intBitsToFloat((int) (entry >>> 32));
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue;
                        }
                        step += 2;
                    }
                }
            }
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public float getOrDefault(int key, float defaultValue) {
        int free;
        if (key != (free = freeValue)) {
            long[] tab = table;
            int capacity, index;
            int cur;
            long entry;
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) % (capacity = tab.length)])) == key) {
                // key is present
                return Float.intBitsToFloat((int) (entry >>> 32));
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (int) (entry = tab[bIndex])) == key) {
                            // key is present
                            return Float.intBitsToFloat((int) (entry >>> 32));
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == key) {
                            // key is present
                            return Float.intBitsToFloat((int) (entry >>> 32));
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue;
                        }
                        step += 2;
                    }
                }
            }
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public void forEach(BiConsumer<? super Integer, ? super Float> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int free = freeValue;
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
                action.accept(key, Float.intBitsToFloat((int) (entry >>> 32)));
            }
        }
    }

    @Override
    public void forEach(IntFloatConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int free = freeValue;
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
                action.accept(key, Float.intBitsToFloat((int) (entry >>> 32)));
            }
        }
    }

    @Override
    public boolean forEachWhile(IntFloatPredicate predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int free = freeValue;
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
                if (!predicate.test(key, Float.intBitsToFloat((int) (entry >>> 32)))) {
                    terminated = true;
                    break;
                }
            }
        }
        return !terminated;
    }

    @Nonnull
    @Override
    public IntFloatCursor cursor() {
        
        return new NoRemovedMapCursor();
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonIntFloatMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalIntFloatMapOps m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int free = freeValue;
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
                if (!m.containsEntry(key, (int) (entry >>> 32))) {
                    containsAll = false;
                    break;
                }
            }
        }
        return containsAll;
    }

    @Override
    public void reversePutAllTo(InternalIntFloatMapOps m) {
        if (isEmpty())
            return;
        int free = freeValue;
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
                m.justPut(key, (int) (entry >>> 32));
            }
        }
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<Integer, Float>> entrySet() {
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
        int free = freeValue;
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
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
        int free = freeValue;
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
                sb.append(' ');
                sb.append(key);
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
    public Float put(Integer key, Float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float put(int key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Float putIfAbsent(Integer key, Float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float putIfAbsent(int key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void justPut(int key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void justPut(int key, int value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Float compute(Integer key,
            BiFunction<? super Integer, ? super Float, ? extends Float> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public float compute(int key, IntFloatToFloatFunction remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public Float computeIfAbsent(Integer key,
            Function<? super Integer, ? extends Float> mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public float computeIfAbsent(int key, IntToFloatFunction mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public Float computeIfPresent(Integer key,
            BiFunction<? super Integer, ? super Float, ? extends Float> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float computeIfPresent(int key, IntFloatToFloatFunction remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Float merge(Integer key, Float value,
            BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public float merge(int key, float value, FloatBinaryOperator remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public float addValue(int key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float addValue(int key, float addition, float defaultValue) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public void putAll(@Nonnull Map<? extends Integer, ? extends Float> m) {
        CommonIntFloatMapOps.putAll(this, m);
    }


    @Override
    public Float replace(Integer key, Float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public float replace(int key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean replace(Integer key, Float oldValue, Float newValue) {
        return replace(key.intValue(),
                oldValue.floatValue(),
                newValue.floatValue());
    }

    @Override
    public boolean replace(int key, float oldValue, float newValue) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public void replaceAll(
            BiFunction<? super Integer, ? super Float, ? extends Float> function) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void replaceAll(IntFloatToFloatFunction function) {
        throw new java.lang.UnsupportedOperationException();
    }





    @Override
    public Float remove(Object key) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean justRemove(int key) {
        throw new java.lang.UnsupportedOperationException();
    }



    

    @Override
    public float remove(int key) {
        throw new java.lang.UnsupportedOperationException();
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Integer) key).intValue(),
                ((Float) value).floatValue()
                );
    }

    @Override
    public boolean remove(int key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean removeIf(IntFloatPredicate filter) {
        throw new java.lang.UnsupportedOperationException();
    }






    class EntryView extends AbstractSetView<Map.Entry<Integer, Float>>
            implements HashObjSet<Map.Entry<Integer, Float>>,
            InternalObjCollectionOps<Map.Entry<Integer, Float>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Integer, Float>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Integer>defaultEquality()
                    ,
                    Equivalence.<Float>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return ImmutableQHashParallelKVIntFloatMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return ImmutableQHashParallelKVIntFloatMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return ImmutableQHashParallelKVIntFloatMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Integer, Float> e = (Map.Entry<Integer, Float>) o;
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    a[resultIndex++] = (T) new ImmutableEntry(key, (int) (entry >>> 32));
                }
            }
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }

        @Override
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Integer, Float>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    action.accept(new ImmutableEntry(key, (int) (entry >>> 32)));
                }
            }
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Integer, Float>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
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
        public ObjIterator<Map.Entry<Integer, Float>> iterator() {
            
            return new NoRemovedEntryIterator();
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Integer, Float>> cursor() {
            
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    changed |= s.remove(e.with(key, (int) (entry >>> 32)));
                }
            }
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Integer, Float>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    changed |= c.add(new ImmutableEntry(key, (int) (entry >>> 32)));
                }
            }
            return changed;
        }


        public int hashCode() {
            return ImmutableQHashParallelKVIntFloatMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    sb.append(' ');
                    sb.append(key);
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
            return ImmutableQHashParallelKVIntFloatMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Integer, Float> e = (Map.Entry<Integer, Float>) o;
                int key = e.getKey();
                float value = e.getValue();
                return ImmutableQHashParallelKVIntFloatMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Integer, Float>> filter) {
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
            ImmutableQHashParallelKVIntFloatMapGO.this.clear();
        }
    }


    abstract class IntFloatEntry extends AbstractEntry<Integer, Float> {

        abstract int key();

        @Override
        public final Integer getKey() {
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
            int k2;
            int v2;
            try {
                e2 = (Map.Entry) o;
                k2 = (Integer) e2.getKey();
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


    private class ImmutableEntry extends IntFloatEntry {
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


    class ReusableEntry extends IntFloatEntry {
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
            return ImmutableQHashParallelKVIntFloatMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return ImmutableQHashParallelKVIntFloatMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return ImmutableQHashParallelKVIntFloatMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(float v) {
            return ImmutableQHashParallelKVIntFloatMapGO.this.containsValue(v);
        }

        @Override
        public boolean contains(int bits) {
            return ImmutableQHashParallelKVIntFloatMapGO.this.containsValue(bits);
        }


        @Override
        public void forEach(Consumer<? super Float> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
                    changed |= c.add(Float.intBitsToFloat((int) (entry >>> 32)));
                }
            }
            return changed;
        }

        private boolean reverseAddAllTo(InternalFloatCollectionOps c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
                    changed |= s.removeFloat(Float.intBitsToFloat((int) (entry >>> 32)));
                }
            }
            return changed;
        }

        private boolean reverseRemoveAllFrom(InternalFloatCollectionOps s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            ImmutableQHashParallelKVIntFloatMapGO.this.clear();
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Integer, Float>> {
        final long[] tab;
        final int free;
        int nextIndex;
        ImmutableEntry next;

        NoRemovedEntryIterator() {
            long[] tab = this.tab = table;
            int free = this.free = freeValue;
            int nextI = tab.length;
            long entry;
            while (--nextI >= 0) {
                int key;
                if ((key = (int) (entry = tab[nextI])) != free) {
                    next = new ImmutableEntry(key, (int) (entry >>> 32));
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Integer, Float>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            long[] tab = this.tab;
            int free = this.free;
            long entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
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
        public Map.Entry<Integer, Float> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                long[] tab = this.tab;
                int free = this.free;
                ImmutableEntry prev = next;
                long entry;
                while (--nextI >= 0) {
                    int key;
                    if ((key = (int) (entry = tab[nextI])) != free) {
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


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Integer, Float>> {
        final long[] tab;
        final int free;
        int index;
        int curKey;
        int curValue;

        NoRemovedEntryCursor() {
            this.tab = table;
            index = tab.length;
            int free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Integer, Float>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            long[] tab = this.tab;
            int free = this.free;
            long entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    action.accept(new ImmutableEntry(key, (int) (entry >>> 32)));
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public Map.Entry<Integer, Float> elem() {
            int curKey;
            if ((curKey = this.curKey) != free) {
                return new ImmutableEntry(curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            long[] tab = this.tab;
            int free = this.free;
            long entry;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    index = i;
                    curKey = key;
                    curValue = (int) (entry >>> 32);
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
        final long[] tab;
        final int free;
        int nextIndex;
        float next;

        NoRemovedValueIterator() {
            long[] tab = this.tab = table;
            int free = this.free = freeValue;
            int nextI = tab.length;
            long entry;
            while (--nextI >= 0) {
                if ((int) (entry = tab[nextI]) != free) {
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
                int free = this.free;
                float prev = next;
                long entry;
                while (--nextI >= 0) {
                    if ((int) (entry = tab[nextI]) != free) {
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
            int free = this.free;
            long entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = this.free;
            long entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
        final int free;
        int index;
        int curKey;
        int curValue;

        NoRemovedValueCursor() {
            this.tab = table;
            index = tab.length;
            int free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(FloatConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            long[] tab = this.tab;
            int free = this.free;
            long entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
                    action.accept(Float.intBitsToFloat((int) (entry >>> 32)));
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
            long[] tab = this.tab;
            int free = this.free;
            long entry;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    index = i;
                    curKey = key;
                    curValue = (int) (entry >>> 32);
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



    class NoRemovedMapCursor implements IntFloatCursor {
        final long[] tab;
        final int free;
        int index;
        int curKey;
        int curValue;

        NoRemovedMapCursor() {
            this.tab = table;
            index = tab.length;
            int free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(IntFloatConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            long[] tab = this.tab;
            int free = this.free;
            long entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    action.accept(key, Float.intBitsToFloat((int) (entry >>> 32)));
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public int key() {
            int curKey;
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
                U.putInt(tab, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), Float.floatToIntBits(value));
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            long[] tab = this.tab;
            int free = this.free;
            long entry;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    index = i;
                    curKey = key;
                    curValue = (int) (entry >>> 32);
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

