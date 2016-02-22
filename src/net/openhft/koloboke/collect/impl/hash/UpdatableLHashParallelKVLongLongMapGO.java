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
import net.openhft.koloboke.function.LongLongConsumer;
import net.openhft.koloboke.function.LongLongPredicate;
import net.openhft.koloboke.function.LongLongToLongFunction;
import java.util.function.LongUnaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class UpdatableLHashParallelKVLongLongMapGO
        extends UpdatableLHashParallelKVLongLongMapSO {

    
    final void copy(ParallelKVLongLongLHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    
    final void move(ParallelKVLongLongLHash hash) {
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
    public boolean containsEntry(long key, long value) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return table[index + 1] == value;
        } else {
            // key is absent
            return false;
        }
    }


    @Override
    public Long get(Object key) {
        int index = index((Long) key);
        if (index >= 0) {
            // key is present
            return table[index + 1];
        } else {
            // key is absent
            return null;
        }
    }

    

    @Override
    public long get(long key) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return table[index + 1];
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Long getOrDefault(Object key, Long defaultValue) {
        int index = index((Long) key);
        if (index >= 0) {
            // key is present
            return table[index + 1];
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public long getOrDefault(long key, long defaultValue) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return table[index + 1];
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public void forEach(BiConsumer<? super Long, ? super Long> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long free = freeValue;
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) != free) {
                action.accept(key, tab[i + 1]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void forEach(LongLongConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long free = freeValue;
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) != free) {
                action.accept(key, tab[i + 1]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public boolean forEachWhile(LongLongPredicate predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        long free = freeValue;
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) != free) {
                if (!predicate.test(key, tab[i + 1])) {
                    terminated = true;
                    break;
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return !terminated;
    }

    @Nonnull
    @Override
    public LongLongCursor cursor() {
        int mc = modCount();
        return new NoRemovedMapCursor(mc);
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonLongLongMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalLongLongMapOps m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        long free = freeValue;
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) != free) {
                if (!m.containsEntry(key, tab[i + 1])) {
                    containsAll = false;
                    break;
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return containsAll;
    }

    @Override
    public void reversePutAllTo(InternalLongLongMapOps m) {
        if (isEmpty())
            return;
        int mc = modCount();
        long free = freeValue;
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) != free) {
                m.justPut(key, tab[i + 1]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<Long, Long>> entrySet() {
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
        int mc = modCount();
        long free = freeValue;
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) != free) {
            long val;
                hashCode += ((int) (key ^ (key >>> 32))) ^ ((int) ((val = tab[i + 1]) ^ (val >>> 32)));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return hashCode;
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "{}";
        StringBuilder sb = new StringBuilder();
        int elementCount = 0;
        int mc = modCount();
        long free = freeValue;
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) != free) {
                sb.append(' ');
                sb.append(key);
                sb.append('=');
                sb.append(tab[i + 1]);
                sb.append(',');
                if (++elementCount == 8) {
                    int expectedLength = sb.length() * (size() / 8);
                    sb.ensureCapacity(expectedLength + (expectedLength / 2));
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        sb.setCharAt(0, '{');
        sb.setCharAt(sb.length() - 1, '}');
        return sb.toString();
    }


    @Override
    void rehash(int newCapacity) {
        int mc = modCount();
        long free = freeValue;
        long[] tab = table;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        long[] newTab = table;
        int capacityMask = newTab.length - 2;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) != free) {
                int index;
                if (newTab[index = ParallelKVLongKeyMixing.mix(key) & capacityMask] != free) {
                    while (true) {
                        if (newTab[(index = (index - 2) & capacityMask)] == free) {
                            break;
                        }
                    }
                }
                newTab[index] = key;
                newTab[index + 1] = tab[i + 1];
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }


    @Override
    public Long put(Long key, Long value) {
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return null;
        } else {
            // key is present
            long[] tab = table;
            long prevValue = tab[index + 1];
            tab[index + 1] = value;
            return prevValue;
        }
    }

    @Override
    public long put(long key, long value) {
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return defaultValue();
        } else {
            // key is present
            long[] tab = table;
            long prevValue = tab[index + 1];
            tab[index + 1] = value;
            return prevValue;
        }
    }

    @Override
    public Long putIfAbsent(Long key, Long value) {
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return null;
        } else {
            // key is present
            return table[index + 1];
        }
    }

    @Override
    public long putIfAbsent(long key, long value) {
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return defaultValue();
        } else {
            // key is present
            return table[index + 1];
        }
    }

    @Override
    public void justPut(long key, long value) {
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return;
        } else {
            // key is present
            table[index + 1] = value;
            return;
        }
    }


    @Override
    public Long compute(Long key,
            BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        long k = key;
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        long[] tab = table;
        int capacityMask, index;
        long cur;
        keyPresent:
        if ((cur = tab[index = ParallelKVLongKeyMixing.mix(k) & (capacityMask = tab.length - 2)]) != k) {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = tab[(index = (index - 2) & capacityMask)]) == k) {
                        break keyPresent;
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            Long newValue = remappingFunction.apply(k, null);
            if (newValue != null) {
                incrementModCount();
                tab[index] = k;
                tab[index + 1] = newValue;
                postInsertHook();
                return newValue;
            } else {
                return null;
            }
        }
        // key is present
        Long newValue = remappingFunction.apply(k, tab[index + 1]);
        if (newValue != null) {
            tab[index + 1] = newValue;
            return newValue;
        } else {
            throw new java.lang.UnsupportedOperationException("Compute operation of updatable map doesn't support removals");
        }
    }


    @Override
    public long compute(long key, LongLongToLongFunction remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        long[] tab = table;
        int capacityMask, index;
        long cur;
        keyPresent:
        if ((cur = tab[index = ParallelKVLongKeyMixing.mix(key) & (capacityMask = tab.length - 2)]) != key) {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = tab[(index = (index - 2) & capacityMask)]) == key) {
                        break keyPresent;
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            long newValue = remappingFunction.applyAsLong(key, defaultValue());
            incrementModCount();
            tab[index] = key;
            tab[index + 1] = newValue;
            postInsertHook();
            return newValue;
        }
        // key is present
        long newValue = remappingFunction.applyAsLong(key, tab[index + 1]);
        tab[index + 1] = newValue;
        return newValue;
    }


    @Override
    public Long computeIfAbsent(Long key,
            Function<? super Long, ? extends Long> mappingFunction) {
        long k = key;
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        long free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        long[] tab = table;
        int capacityMask, index;
        long cur;
        if ((cur = tab[index = ParallelKVLongKeyMixing.mix(k) & (capacityMask = tab.length - 2)]) == k) {
            // key is present
            return tab[index + 1];
        } else {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = tab[(index = (index - 2) & capacityMask)]) == k) {
                        // key is present
                        return tab[index + 1];
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            Long value = mappingFunction.apply(k);
            if (value != null) {
                incrementModCount();
                tab[index] = k;
                tab[index + 1] = value;
                postInsertHook();
                return value;
            } else {
                return null;
            }
        }
    }


    @Override
    public long computeIfAbsent(long key, LongUnaryOperator mappingFunction) {
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        long free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        long[] tab = table;
        int capacityMask, index;
        long cur;
        if ((cur = tab[index = ParallelKVLongKeyMixing.mix(key) & (capacityMask = tab.length - 2)]) == key) {
            // key is present
            return tab[index + 1];
        } else {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = tab[(index = (index - 2) & capacityMask)]) == key) {
                        // key is present
                        return tab[index + 1];
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            long value = mappingFunction.applyAsLong(key);
            incrementModCount();
            tab[index] = key;
            tab[index + 1] = value;
            postInsertHook();
            return value;
        }
    }


    @Override
    public Long computeIfPresent(Long key,
            BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        long k = key;
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(k);
        if (index >= 0) {
            // key is present
            long[] tab = table;
            Long newValue = remappingFunction.apply(k, tab[index + 1]);
            if (newValue != null) {
                tab[index + 1] = newValue;
                return newValue;
            } else {
                throw new java.lang.UnsupportedOperationException("ComputeIfPresent operation of updatable map doesn't support removals");
            }
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public long computeIfPresent(long key, LongLongToLongFunction remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(key);
        if (index >= 0) {
            // key is present
            long[] tab = table;
            long newValue = remappingFunction.applyAsLong(key, tab[index + 1]);
            tab[index + 1] = newValue;
            return newValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Long merge(Long key, Long value,
            BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        long k = key;
        if (value == null)
            throw new java.lang.NullPointerException();
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        long[] tab = table;
        int capacityMask, index;
        long cur;
        keyPresent:
        if ((cur = tab[index = ParallelKVLongKeyMixing.mix(k) & (capacityMask = tab.length - 2)]) != k) {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = tab[(index = (index - 2) & capacityMask)]) == k) {
                        break keyPresent;
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            incrementModCount();
            tab[index] = k;
            tab[index + 1] = value;
            postInsertHook();
            return value;
        }
        // key is present
        Long newValue = remappingFunction.apply(tab[index + 1], value);
        if (newValue != null) {
            tab[index + 1] = newValue;
            return newValue;
        } else {
            throw new java.lang.UnsupportedOperationException("Merge operation of updatable map doesn't support removals");
        }
    }


    @Override
    public long merge(long key, long value, LongBinaryOperator remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        long[] tab = table;
        int capacityMask, index;
        long cur;
        keyPresent:
        if ((cur = tab[index = ParallelKVLongKeyMixing.mix(key) & (capacityMask = tab.length - 2)]) != key) {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = tab[(index = (index - 2) & capacityMask)]) == key) {
                        break keyPresent;
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            incrementModCount();
            tab[index] = key;
            tab[index + 1] = value;
            postInsertHook();
            return value;
        }
        // key is present
        long newValue = remappingFunction.applyAsLong(tab[index + 1], value);
        tab[index + 1] = newValue;
        return newValue;
    }


    @Override
    public long addValue(long key, long value) {
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return value;
        } else {
            // key is present
            long[] tab = table;
            long newValue = tab[index + 1] + value;
            tab[index + 1] = newValue;
            return newValue;
        }
    }

    @Override
    public long addValue(long key, long addition, long defaultValue) {
        long value = defaultValue + addition;
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return value;
        } else {
            // key is present
            long[] tab = table;
            long newValue = tab[index + 1] + addition;
            tab[index + 1] = newValue;
            return newValue;
        }
    }


    @Override
    public void putAll(@Nonnull Map<? extends Long, ? extends Long> m) {
        CommonLongLongMapOps.putAll(this, m);
    }


    @Override
    public Long replace(Long key, Long value) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            long[] tab = table;
            long oldValue = tab[index + 1];
            tab[index + 1] = value;
            return oldValue;
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public long replace(long key, long value) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            long[] tab = table;
            long oldValue = tab[index + 1];
            tab[index + 1] = value;
            return oldValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public boolean replace(Long key, Long oldValue, Long newValue) {
        return replace(key.longValue(),
                oldValue.longValue(),
                newValue.longValue());
    }

    @Override
    public boolean replace(long key, long oldValue, long newValue) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            long[] tab = table;
            if (tab[index + 1] == oldValue) {
                tab[index + 1] = newValue;
                return true;
            } else {
                return false;
            }
        } else {
            // key is absent
            return false;
        }
    }


    @Override
    public void replaceAll(
            BiFunction<? super Long, ? super Long, ? extends Long> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long free = freeValue;
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) != free) {
                tab[i + 1] = function.apply(key, tab[i + 1]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void replaceAll(LongLongToLongFunction function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long free = freeValue;
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) != free) {
                tab[i + 1] = function.applyAsLong(key, tab[i + 1]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }


    @Override
    public void clear() {
        int mc = modCount() + 1;
        super.clear();
        if (mc != modCount())
            throw new ConcurrentModificationException();
    }



    @Override
    public Long remove(Object key) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean justRemove(long key) {
        throw new java.lang.UnsupportedOperationException();
    }



    

    @Override
    public long remove(long key) {
        throw new java.lang.UnsupportedOperationException();
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Long) key).longValue(),
                ((Long) value).longValue()
                );
    }

    @Override
    public boolean remove(long key, long value) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean removeIf(LongLongPredicate filter) {
        throw new java.lang.UnsupportedOperationException();
    }






    class EntryView extends AbstractSetView<Map.Entry<Long, Long>>
            implements HashObjSet<Map.Entry<Long, Long>>,
            InternalObjCollectionOps<Map.Entry<Long, Long>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Long, Long>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Long>defaultEquality()
                    ,
                    Equivalence.<Long>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return UpdatableLHashParallelKVLongLongMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return UpdatableLHashParallelKVLongLongMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return UpdatableLHashParallelKVLongLongMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Long, Long> e = (Map.Entry<Long, Long>) o;
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
            int mc = modCount();
            long free = freeValue;
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) != free) {
                    result[resultIndex++] = new MutableEntry(mc, i, key, tab[i + 1]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
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
            int mc = modCount();
            long free = freeValue;
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) != free) {
                    a[resultIndex++] = (T) new MutableEntry(mc, i, key, tab[i + 1]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }

        @Override
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Long, Long>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            long free = freeValue;
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) != free) {
                    action.accept(new MutableEntry(mc, i, key, tab[i + 1]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Long, Long>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            long free = freeValue;
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) != free) {
                    if (!predicate.test(new MutableEntry(mc, i, key, tab[i + 1]))) {
                        terminated = true;
                        break;
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return !terminated;
        }

        @Override
        @Nonnull
        public ObjIterator<Map.Entry<Long, Long>> iterator() {
            int mc = modCount();
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Long, Long>> cursor() {
            int mc = modCount();
            return new NoRemovedEntryCursor(mc);
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
            int mc = modCount();
            long free = freeValue;
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) != free) {
                    if (!c.contains(e.with(key, tab[i + 1]))) {
                        containsAll = false;
                        break;
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return containsAll;
        }

        @Override
        public boolean reverseRemoveAllFrom(ObjSet<?> s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            ReusableEntry e = new ReusableEntry();
            int mc = modCount();
            long free = freeValue;
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) != free) {
                    changed |= s.remove(e.with(key, tab[i + 1]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Long, Long>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long free = freeValue;
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) != free) {
                    changed |= c.add(new MutableEntry(mc, i, key, tab[i + 1]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        public int hashCode() {
            return UpdatableLHashParallelKVLongLongMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            long free = freeValue;
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) != free) {
                    sb.append(' ');
                    sb.append(key);
                    sb.append('=');
                    sb.append(tab[i + 1]);
                    sb.append(',');
                    if (++elementCount == 8) {
                        int expectedLength = sb.length() * (size() / 8);
                        sb.ensureCapacity(expectedLength + (expectedLength / 2));
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            sb.setCharAt(0, '[');
            sb.setCharAt(sb.length() - 1, ']');
            return sb.toString();
        }

        @Override
        public boolean shrink() {
            return UpdatableLHashParallelKVLongLongMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Long, Long> e = (Map.Entry<Long, Long>) o;
                long key = e.getKey();
                long value = e.getValue();
                return UpdatableLHashParallelKVLongLongMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Long, Long>> filter) {
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
            UpdatableLHashParallelKVLongLongMapGO.this.clear();
        }
    }


    abstract class LongLongEntry extends AbstractEntry<Long, Long> {

        abstract long key();

        @Override
        public final Long getKey() {
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
            long k2;
            long v2;
            try {
                e2 = (Map.Entry) o;
                k2 = (Long) e2.getKey();
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


    class MutableEntry extends LongLongEntry {
        final int modCount;
        private final int index;
        final long key;
        private long value;

        MutableEntry(int modCount, int index, long key, long value) {
            this.modCount = modCount;
            this.index = index;
            this.key = key;
            this.value = value;
        }

        @Override
        public long key() {
            return key;
        }

        @Override
        public long value() {
            return value;
        }

        @Override
        public Long setValue(Long newValue) {
            if (modCount != modCount())
                throw new IllegalStateException();
            long oldValue = value;
            long unwrappedNewValue = newValue;
            value = unwrappedNewValue;
            updateValueInTable(unwrappedNewValue);
            return oldValue;
        }

        void updateValueInTable(long newValue) {
            table[index + 1] = newValue;
        }
    }



    class ReusableEntry extends LongLongEntry {
        private long key;
        private long value;

        ReusableEntry with(long key, long value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public long key() {
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
            return UpdatableLHashParallelKVLongLongMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return UpdatableLHashParallelKVLongLongMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return UpdatableLHashParallelKVLongLongMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(long v) {
            return UpdatableLHashParallelKVLongLongMapGO.this.containsValue(v);
        }



        @Override
        public void forEach(Consumer<? super Long> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            long free = freeValue;
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] != free) {
                    action.accept(tab[i + 1]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public void forEach(LongConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            long free = freeValue;
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] != free) {
                    action.accept(tab[i + 1]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(LongPredicate predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            long free = freeValue;
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] != free) {
                    if (!predicate.test(tab[i + 1])) {
                        terminated = true;
                        break;
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return !terminated;
        }

        @Override
        public boolean allContainingIn(LongCollection c) {
            if (isEmpty())
                return true;
            boolean containsAll = true;
            int mc = modCount();
            long free = freeValue;
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] != free) {
                    if (!c.contains(tab[i + 1])) {
                        containsAll = false;
                        break;
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return containsAll;
        }


        @Override
        public boolean reverseAddAllTo(LongCollection c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long free = freeValue;
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] != free) {
                    changed |= c.add(tab[i + 1]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        @Override
        public boolean reverseRemoveAllFrom(LongSet s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long free = freeValue;
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] != free) {
                    changed |= s.removeLong(tab[i + 1]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }



        @Override
        @Nonnull
        public LongIterator iterator() {
            int mc = modCount();
            return new NoRemovedValueIterator(mc);
        }

        @Nonnull
        @Override
        public LongCursor cursor() {
            int mc = modCount();
            return new NoRemovedValueCursor(mc);
        }

        @Override
        @Nonnull
        public Object[] toArray() {
            int size = size();
            Object[] result = new Object[size];
            if (size == 0)
                return result;
            int resultIndex = 0;
            int mc = modCount();
            long free = freeValue;
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] != free) {
                    result[resultIndex++] = tab[i + 1];
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
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
            int mc = modCount();
            long free = freeValue;
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] != free) {
                    a[resultIndex++] = (T) Long.valueOf(tab[i + 1]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
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
            int mc = modCount();
            long free = freeValue;
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] != free) {
                    result[resultIndex++] = tab[i + 1];
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
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
            int mc = modCount();
            long free = freeValue;
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] != free) {
                    a[resultIndex++] = tab[i + 1];
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
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
            int mc = modCount();
            long free = freeValue;
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] != free) {
                    sb.append(' ').append(tab[i + 1]).append(',');
                    if (++elementCount == 8) {
                        int expectedLength = sb.length() * (size() / 8);
                        sb.ensureCapacity(expectedLength + (expectedLength / 2));
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
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
            UpdatableLHashParallelKVLongLongMapGO.this.clear();
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Long, Long>> {
        final long[] tab;
        final long free;
        int expectedModCount;
        int nextIndex;
        MutableEntry next;

        NoRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            long[] tab = this.tab = table;
            long free = this.free = freeValue;
            int nextI = tab.length;
            while ((nextI -= 2) >= 0) {
                long key;
                if ((key = tab[nextI]) != free) {
                    next = new MutableEntry(mc, nextI, key, tab[nextI + 1]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Long, Long>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            long free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) != free) {
                    action.accept(new MutableEntry(mc, i, key, tab[i + 1]));
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            nextIndex = -1;
        }

        @Override
        public boolean hasNext() {
            return nextIndex >= 0;
        }

        @Override
        public Map.Entry<Long, Long> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
                    long[] tab = this.tab;
                    long free = this.free;
                    MutableEntry prev = next;
                    while ((nextI -= 2) >= 0) {
                        long key;
                        if ((key = tab[nextI]) != free) {
                            next = new MutableEntry(mc, nextI, key, tab[nextI + 1]);
                            break;
                        }
                    }
                    nextIndex = nextI;
                    return prev;
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.util.NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Long, Long>> {
        final long[] tab;
        final long free;
        int expectedModCount;
        int index;
        long curKey;
        long curValue;

        NoRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            long free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Long, Long>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            long free = this.free;
            int index = this.index;
            for (int i = index - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) != free) {
                    action.accept(new MutableEntry(mc, i, key, tab[i + 1]));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public Map.Entry<Long, Long> elem() {
            long curKey;
            if ((curKey = this.curKey) != free) {
                return new MutableEntry(expectedModCount, index, curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                long[] tab = this.tab;
                long free = this.free;
                for (int i = index - 2; i >= 0; i -= 2) {
                    long key;
                    if ((key = tab[i]) != free) {
                        index = i;
                        curKey = key;
                        curValue = tab[i + 1];
                        return true;
                    }
                }
                curKey = free;
                index = -1;
                return false;
            } else {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }




    class NoRemovedValueIterator implements LongIterator {
        final long[] tab;
        final long free;
        int expectedModCount;
        int nextIndex;
        long next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            long[] tab = this.tab = table;
            long free = this.free = freeValue;
            int nextI = tab.length;
            while ((nextI -= 2) >= 0) {
                if (tab[nextI] != free) {
                    next = tab[nextI + 1];
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public long nextLong() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                if (expectedModCount == modCount()) {
                    long[] tab = this.tab;
                    long free = this.free;
                    long prev = next;
                    while ((nextI -= 2) >= 0) {
                        if (tab[nextI] != free) {
                            next = tab[nextI + 1];
                            break;
                        }
                    }
                    nextIndex = nextI;
                    return prev;
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.util.NoSuchElementException();
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super Long> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            long free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i -= 2) {
                if (tab[i] != free) {
                    action.accept(tab[i + 1]);
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            nextIndex = -1;
        }

        @Override
        public void forEachRemaining(LongConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            long free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i -= 2) {
                if (tab[i] != free) {
                    action.accept(tab[i + 1]);
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
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
        final long[] tab;
        final long free;
        int expectedModCount;
        int index;
        long curKey;
        long curValue;

        NoRemovedValueCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            long free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(LongConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            long free = this.free;
            int index = this.index;
            for (int i = index - 2; i >= 0; i -= 2) {
                if (tab[i] != free) {
                    action.accept(tab[i + 1]);
                }
            }
            if (index != this.index || mc != modCount()) {
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
            if (expectedModCount == modCount()) {
                long[] tab = this.tab;
                long free = this.free;
                for (int i = index - 2; i >= 0; i -= 2) {
                    long key;
                    if ((key = tab[i]) != free) {
                        index = i;
                        curKey = key;
                        curValue = tab[i + 1];
                        return true;
                    }
                }
                curKey = free;
                index = -1;
                return false;
            } else {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }



    class NoRemovedMapCursor implements LongLongCursor {
        final long[] tab;
        final long free;
        int expectedModCount;
        int index;
        long curKey;
        long curValue;

        NoRemovedMapCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            long free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(LongLongConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            long free = this.free;
            int index = this.index;
            for (int i = index - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) != free) {
                    action.accept(key, tab[i + 1]);
                }
            }
            if (index != this.index || mc != modCount()) {
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
                if (expectedModCount == modCount()) {
                    tab[index + 1] = value;
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                long[] tab = this.tab;
                long free = this.free;
                for (int i = index - 2; i >= 0; i -= 2) {
                    long key;
                    if ((key = tab[i]) != free) {
                        index = i;
                        curKey = key;
                        curValue = tab[i + 1];
                        return true;
                    }
                }
                curKey = free;
                index = -1;
                return false;
            } else {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }
}

