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
import java.util.function.DoublePredicate;
import net.openhft.koloboke.function.DoubleLongConsumer;
import net.openhft.koloboke.function.DoubleLongPredicate;
import net.openhft.koloboke.function.DoubleLongToLongFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class MutableLHashParallelKVDoubleLongMapGO
        extends MutableLHashParallelKVDoubleLongMapSO {

    
    final void copy(ParallelKVDoubleLongLHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    
    final void move(ParallelKVDoubleLongLHash hash) {
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
    public boolean containsEntry(double key, long value) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            return table[index + 1] == value;
        } else {
            // key is absent
            return false;
        }
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
        long k = Double.doubleToLongBits((Double) key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            return table[index + 1];
        } else {
            // key is absent
            return null;
        }
    }

    

    @Override
    public long get(double key) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
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
        long k = Double.doubleToLongBits((Double) key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            return table[index + 1];
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public long getOrDefault(double key, long defaultValue) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            return table[index + 1];
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public void forEach(BiConsumer<? super Double, ? super Long> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                action.accept(Double.longBitsToDouble(key), tab[i + 1]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void forEach(DoubleLongConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                action.accept(Double.longBitsToDouble(key), tab[i + 1]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public boolean forEachWhile(DoubleLongPredicate predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                if (!predicate.test(Double.longBitsToDouble(key), tab[i + 1])) {
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
    public DoubleLongCursor cursor() {
        int mc = modCount();
        return new NoRemovedMapCursor(mc);
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonDoubleLongMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalDoubleLongMapOps m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
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
    public void reversePutAllTo(InternalDoubleLongMapOps m) {
        if (isEmpty())
            return;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                m.justPut(key, tab[i + 1]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<Double, Long>> entrySet() {
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
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
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
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                sb.append(' ');
                sb.append(Double.longBitsToDouble(key));
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
        long[] tab = table;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        long[] newTab = table;
        int capacityMask = newTab.length - 2;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                int index;
                if (newTab[index = ParallelKVDoubleKeyMixing.mix(key) & capacityMask] != FREE_BITS) {
                    while (true) {
                        if (newTab[(index = (index - 2) & capacityMask)] == FREE_BITS) {
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
    public Long put(Double key, Long value) {
        long k = Double.doubleToLongBits(key);
        int index = insert(k, value);
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
    public long put(double key, long value) {
        long k = Double.doubleToLongBits(key);
        int index = insert(k, value);
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
    public Long putIfAbsent(Double key, Long value) {
        long k = Double.doubleToLongBits(key);
        int index = insert(k, value);
        if (index < 0) {
            // key was absent
            return null;
        } else {
            // key is present
            return table[index + 1];
        }
    }

    @Override
    public long putIfAbsent(double key, long value) {
        long k = Double.doubleToLongBits(key);
        int index = insert(k, value);
        if (index < 0) {
            // key was absent
            return defaultValue();
        } else {
            // key is present
            return table[index + 1];
        }
    }

    @Override
    public void justPut(double key, long value) {
        long k = Double.doubleToLongBits(key);
        int index = insert(k, value);
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
    public Long compute(Double key,
            BiFunction<? super Double, ? super Long, ? extends Long> remappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacityMask, index;
        long cur;
        keyPresent:
        if ((cur = tab[index = ParallelKVDoubleKeyMixing.mix(k) & (capacityMask = tab.length - 2)]) != k) {
            keyAbsent:
            if (cur != FREE_BITS) {
                while (true) {
                    if ((cur = tab[(index = (index - 2) & capacityMask)]) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            Long newValue = remappingFunction.apply(Double.longBitsToDouble(k), null);
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
        Long newValue = remappingFunction.apply(Double.longBitsToDouble(k), tab[index + 1]);
        if (newValue != null) {
            tab[index + 1] = newValue;
            return newValue;
        } else {
            removeAt(index);
            return null;
        }
    }


    @Override
    public long compute(double key, DoubleLongToLongFunction remappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacityMask, index;
        long cur;
        keyPresent:
        if ((cur = tab[index = ParallelKVDoubleKeyMixing.mix(k) & (capacityMask = tab.length - 2)]) != k) {
            keyAbsent:
            if (cur != FREE_BITS) {
                while (true) {
                    if ((cur = tab[(index = (index - 2) & capacityMask)]) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            long newValue = remappingFunction.applyAsLong(Double.longBitsToDouble(k), defaultValue());
            incrementModCount();
            tab[index] = k;
            tab[index + 1] = newValue;
            postInsertHook();
            return newValue;
        }
        // key is present
        long newValue = remappingFunction.applyAsLong(Double.longBitsToDouble(k), tab[index + 1]);
        tab[index + 1] = newValue;
        return newValue;
    }


    @Override
    public Long computeIfAbsent(Double key,
            Function<? super Double, ? extends Long> mappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacityMask, index;
        long cur;
        if ((cur = tab[index = ParallelKVDoubleKeyMixing.mix(k) & (capacityMask = tab.length - 2)]) == k) {
            // key is present
            return tab[index + 1];
        } else {
            keyAbsent:
            if (cur != FREE_BITS) {
                while (true) {
                    if ((cur = tab[(index = (index - 2) & capacityMask)]) == k) {
                        // key is present
                        return tab[index + 1];
                    } else if (cur == FREE_BITS) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            Long value = mappingFunction.apply(Double.longBitsToDouble(k));
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
    public long computeIfAbsent(double key, DoubleToLongFunction mappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacityMask, index;
        long cur;
        if ((cur = tab[index = ParallelKVDoubleKeyMixing.mix(k) & (capacityMask = tab.length - 2)]) == k) {
            // key is present
            return tab[index + 1];
        } else {
            keyAbsent:
            if (cur != FREE_BITS) {
                while (true) {
                    if ((cur = tab[(index = (index - 2) & capacityMask)]) == k) {
                        // key is present
                        return tab[index + 1];
                    } else if (cur == FREE_BITS) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            long value = mappingFunction.applyAsLong(Double.longBitsToDouble(k));
            incrementModCount();
            tab[index] = k;
            tab[index + 1] = value;
            postInsertHook();
            return value;
        }
    }


    @Override
    public Long computeIfPresent(Double key,
            BiFunction<? super Double, ? super Long, ? extends Long> remappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(k);
        if (index >= 0) {
            // key is present
            long[] tab = table;
            Long newValue = remappingFunction.apply(Double.longBitsToDouble(k), tab[index + 1]);
            if (newValue != null) {
                tab[index + 1] = newValue;
                return newValue;
            } else {
                removeAt(index);
                return null;
            }
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public long computeIfPresent(double key, DoubleLongToLongFunction remappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(k);
        if (index >= 0) {
            // key is present
            long[] tab = table;
            long newValue = remappingFunction.applyAsLong(Double.longBitsToDouble(k), tab[index + 1]);
            tab[index + 1] = newValue;
            return newValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Long merge(Double key, Long value,
            BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (value == null)
            throw new java.lang.NullPointerException();
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacityMask, index;
        long cur;
        keyPresent:
        if ((cur = tab[index = ParallelKVDoubleKeyMixing.mix(k) & (capacityMask = tab.length - 2)]) != k) {
            keyAbsent:
            if (cur != FREE_BITS) {
                while (true) {
                    if ((cur = tab[(index = (index - 2) & capacityMask)]) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
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
            removeAt(index);
            return null;
        }
    }


    @Override
    public long merge(double key, long value, LongBinaryOperator remappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacityMask, index;
        long cur;
        keyPresent:
        if ((cur = tab[index = ParallelKVDoubleKeyMixing.mix(k) & (capacityMask = tab.length - 2)]) != k) {
            keyAbsent:
            if (cur != FREE_BITS) {
                while (true) {
                    if ((cur = tab[(index = (index - 2) & capacityMask)]) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
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
        long newValue = remappingFunction.applyAsLong(tab[index + 1], value);
        tab[index + 1] = newValue;
        return newValue;
    }


    @Override
    public long addValue(double key, long value) {
        long k = Double.doubleToLongBits(key);
        int index = insert(k, value);
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
    public long addValue(double key, long addition, long defaultValue) {
        long k = Double.doubleToLongBits(key);
        long value = defaultValue + addition;
        int index = insert(k, value);
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
    public void putAll(@Nonnull Map<? extends Double, ? extends Long> m) {
        CommonDoubleLongMapOps.putAll(this, m);
    }


    @Override
    public Long replace(Double key, Long value) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
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
    public long replace(double key, long value) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
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
    public boolean replace(Double key, Long oldValue, Long newValue) {
        return replace(key.doubleValue(),
                oldValue.longValue(),
                newValue.longValue());
    }

    @Override
    public boolean replace(double key, long oldValue, long newValue) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
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
            BiFunction<? super Double, ? super Long, ? extends Long> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                tab[i + 1] = function.apply(Double.longBitsToDouble(key), tab[i + 1]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void replaceAll(DoubleLongToLongFunction function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                tab[i + 1] = function.applyAsLong(Double.longBitsToDouble(key), tab[i + 1]);
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
    void removeAt(int index) {
        long[] tab = table;
        int capacityMask = tab.length - 2;
        incrementModCount();
        int indexToRemove = index;
        int indexToShift = indexToRemove;
        int shiftDistance = 2;
        while (true) {
            indexToShift = (indexToShift - 2) & capacityMask;
            long keyToShift;
            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                break;
            }
            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                tab[indexToRemove] = keyToShift;
                tab[indexToRemove + 1] = tab[indexToShift + 1];
                indexToRemove = indexToShift;
                shiftDistance = 2;
            } else {
                shiftDistance += 2;
                if (indexToShift == 2 + index) {
                    throw new java.util.ConcurrentModificationException();
                }
            }
        }
        tab[indexToRemove] = FREE_BITS;
        postRemoveHook();
    }

    @Override
    public Long remove(Object key) {
        long k = Double.doubleToLongBits((Double) key);
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int index;
        long cur;
        keyPresent:
        if ((cur = tab[index = ParallelKVDoubleKeyMixing.mix(k) & capacityMask]) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return null;
            } else {
                while (true) {
                    if ((cur = tab[(index = (index - 2) & capacityMask)]) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return null;
                    }
                }
            }
        }
        // key is present
        long val = tab[index + 1];
        incrementModCount();
        int indexToRemove = index;
        int indexToShift = indexToRemove;
        int shiftDistance = 2;
        while (true) {
            indexToShift = (indexToShift - 2) & capacityMask;
            long keyToShift;
            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                break;
            }
            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                tab[indexToRemove] = keyToShift;
                tab[indexToRemove + 1] = tab[indexToShift + 1];
                indexToRemove = indexToShift;
                shiftDistance = 2;
            } else {
                shiftDistance += 2;
                if (indexToShift == 2 + index) {
                    throw new java.util.ConcurrentModificationException();
                }
            }
        }
        tab[indexToRemove] = FREE_BITS;
        postRemoveHook();
        return val;
    }


    @Override
    public boolean justRemove(double key) {
        long k = Double.doubleToLongBits(key);
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int index;
        long cur;
        keyPresent:
        if ((cur = tab[index = ParallelKVDoubleKeyMixing.mix(k) & capacityMask]) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return false;
            } else {
                while (true) {
                    if ((cur = tab[(index = (index - 2) & capacityMask)]) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return false;
                    }
                }
            }
        }
        // key is present
        incrementModCount();
        int indexToRemove = index;
        int indexToShift = indexToRemove;
        int shiftDistance = 2;
        while (true) {
            indexToShift = (indexToShift - 2) & capacityMask;
            long keyToShift;
            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                break;
            }
            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                tab[indexToRemove] = keyToShift;
                tab[indexToRemove + 1] = tab[indexToShift + 1];
                indexToRemove = indexToShift;
                shiftDistance = 2;
            } else {
                shiftDistance += 2;
                if (indexToShift == 2 + index) {
                    throw new java.util.ConcurrentModificationException();
                }
            }
        }
        tab[indexToRemove] = FREE_BITS;
        postRemoveHook();
        return true;
    }

    @Override
    public boolean justRemove(long key) {
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int index;
        long cur;
        keyPresent:
        if ((cur = tab[index = ParallelKVDoubleKeyMixing.mix(key) & capacityMask]) != key) {
            if (cur == FREE_BITS) {
                // key is absent
                return false;
            } else {
                while (true) {
                    if ((cur = tab[(index = (index - 2) & capacityMask)]) == key) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return false;
                    }
                }
            }
        }
        // key is present
        incrementModCount();
        int indexToRemove = index;
        int indexToShift = indexToRemove;
        int shiftDistance = 2;
        while (true) {
            indexToShift = (indexToShift - 2) & capacityMask;
            long keyToShift;
            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                break;
            }
            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                tab[indexToRemove] = keyToShift;
                tab[indexToRemove + 1] = tab[indexToShift + 1];
                indexToRemove = indexToShift;
                shiftDistance = 2;
            } else {
                shiftDistance += 2;
                if (indexToShift == 2 + index) {
                    throw new java.util.ConcurrentModificationException();
                }
            }
        }
        tab[indexToRemove] = FREE_BITS;
        postRemoveHook();
        return true;
    }


    

    @Override
    public long remove(double key) {
        long k = Double.doubleToLongBits(key);
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int index;
        long cur;
        keyPresent:
        if ((cur = tab[index = ParallelKVDoubleKeyMixing.mix(k) & capacityMask]) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return defaultValue();
            } else {
                while (true) {
                    if ((cur = tab[(index = (index - 2) & capacityMask)]) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return defaultValue();
                    }
                }
            }
        }
        // key is present
        long val = tab[index + 1];
        incrementModCount();
        int indexToRemove = index;
        int indexToShift = indexToRemove;
        int shiftDistance = 2;
        while (true) {
            indexToShift = (indexToShift - 2) & capacityMask;
            long keyToShift;
            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                break;
            }
            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                tab[indexToRemove] = keyToShift;
                tab[indexToRemove + 1] = tab[indexToShift + 1];
                indexToRemove = indexToShift;
                shiftDistance = 2;
            } else {
                shiftDistance += 2;
                if (indexToShift == 2 + index) {
                    throw new java.util.ConcurrentModificationException();
                }
            }
        }
        tab[indexToRemove] = FREE_BITS;
        postRemoveHook();
        return val;
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Double) key).doubleValue(),
                ((Long) value).longValue()
                );
    }

    @Override
    public boolean remove(double key, long value) {
        long k = Double.doubleToLongBits(key);
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int index;
        long cur;
        keyPresent:
        if ((cur = tab[index = ParallelKVDoubleKeyMixing.mix(k) & capacityMask]) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return false;
            } else {
                while (true) {
                    if ((cur = tab[(index = (index - 2) & capacityMask)]) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return false;
                    }
                }
            }
        }
        // key is present
        if (tab[index + 1] == value) {
            incrementModCount();
            int indexToRemove = index;
            int indexToShift = indexToRemove;
            int shiftDistance = 2;
            while (true) {
                indexToShift = (indexToShift - 2) & capacityMask;
                long keyToShift;
                if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                    break;
                }
                if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                    tab[indexToRemove] = keyToShift;
                    tab[indexToRemove + 1] = tab[indexToShift + 1];
                    indexToRemove = indexToShift;
                    shiftDistance = 2;
                } else {
                    shiftDistance += 2;
                    if (indexToShift == 2 + index) {
                        throw new java.util.ConcurrentModificationException();
                    }
                }
            }
            tab[indexToRemove] = FREE_BITS;
            postRemoveHook();
            return true;
        } else {
            return false;
        }
    }


    @Override
    public boolean removeIf(DoubleLongPredicate filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int firstDelayedRemoved = -1;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                if (filter.test(Double.longBitsToDouble(key), tab[i + 1])) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    tab[indexToRemove] = REMOVED_BITS;
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i += 2;
                                }
                                tab[indexToRemove] = keyToShift;
                                tab[indexToRemove + 1] = tab[indexToShift + 1];
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                                if (indexToShift == 2 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        tab[i] = REMOVED_BITS;
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }




    // under this condition - operations, overridden from MutableParallelKVDoubleLHashGO
    // when values are objects - in order to set values to null on removing (for garbage collection)
    // when algo is LHash - because shift deletion should shift values to

    @Override
    public boolean removeIf(Predicate<? super Double> filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int firstDelayedRemoved = -1;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                if (filter.test(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    tab[indexToRemove] = REMOVED_BITS;
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i += 2;
                                }
                                tab[indexToRemove] = keyToShift;
                                tab[indexToRemove + 1] = tab[indexToShift + 1];
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                                if (indexToShift == 2 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        tab[i] = REMOVED_BITS;
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    @Override
    public boolean removeIf(DoublePredicate filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int firstDelayedRemoved = -1;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                if (filter.test(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    tab[indexToRemove] = REMOVED_BITS;
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i += 2;
                                }
                                tab[indexToRemove] = keyToShift;
                                tab[indexToRemove + 1] = tab[indexToShift + 1];
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                                if (indexToShift == 2 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        tab[i] = REMOVED_BITS;
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    @Override
    public boolean removeAll(@Nonnull HashDoubleSet thisC, @Nonnull Collection<?> c) {
        if (c instanceof InternalDoubleCollectionOps)
            return removeAll(thisC, (InternalDoubleCollectionOps) c);
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int firstDelayedRemoved = -1;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                if (c.contains(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    tab[indexToRemove] = REMOVED_BITS;
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i += 2;
                                }
                                tab[indexToRemove] = keyToShift;
                                tab[indexToRemove + 1] = tab[indexToShift + 1];
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                                if (indexToShift == 2 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        tab[i] = REMOVED_BITS;
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    @Override
    boolean removeAll(@Nonnull HashDoubleSet thisC, @Nonnull DoubleCollection c) {
        if (c instanceof InternalDoubleCollectionOps)
            return removeAll(thisC, (InternalDoubleCollectionOps) c);
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int firstDelayedRemoved = -1;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                if (c.contains(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    tab[indexToRemove] = REMOVED_BITS;
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i += 2;
                                }
                                tab[indexToRemove] = keyToShift;
                                tab[indexToRemove + 1] = tab[indexToShift + 1];
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                                if (indexToShift == 2 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        tab[i] = REMOVED_BITS;
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    @Override
    boolean removeAll(@Nonnull HashDoubleSet thisC, @Nonnull InternalDoubleCollectionOps c) {
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int firstDelayedRemoved = -1;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                if (c.contains(key)) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    tab[indexToRemove] = REMOVED_BITS;
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i += 2;
                                }
                                tab[indexToRemove] = keyToShift;
                                tab[indexToRemove + 1] = tab[indexToShift + 1];
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                                if (indexToShift == 2 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        tab[i] = REMOVED_BITS;
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    @Override
    public boolean retainAll(@Nonnull HashDoubleSet thisC, @Nonnull Collection<?> c) {
        if (c instanceof DoubleCollection)
            return retainAll(thisC, (DoubleCollection) c);
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty())
            return false;
        if (c.isEmpty()) {
            clear();
            return true;
        }
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int firstDelayedRemoved = -1;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                if (!c.contains(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    tab[indexToRemove] = REMOVED_BITS;
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i += 2;
                                }
                                tab[indexToRemove] = keyToShift;
                                tab[indexToRemove + 1] = tab[indexToShift + 1];
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                                if (indexToShift == 2 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        tab[i] = REMOVED_BITS;
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    private boolean retainAll(@Nonnull HashDoubleSet thisC, @Nonnull DoubleCollection c) {
        if (c instanceof InternalDoubleCollectionOps)
            return retainAll(thisC, (InternalDoubleCollectionOps) c);
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty())
            return false;
        if (c.isEmpty()) {
            clear();
            return true;
        }
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int firstDelayedRemoved = -1;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                if (!c.contains(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    tab[indexToRemove] = REMOVED_BITS;
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i += 2;
                                }
                                tab[indexToRemove] = keyToShift;
                                tab[indexToRemove + 1] = tab[indexToShift + 1];
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                                if (indexToShift == 2 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        tab[i] = REMOVED_BITS;
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    private boolean retainAll(@Nonnull HashDoubleSet thisC,
            @Nonnull InternalDoubleCollectionOps c) {
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty())
            return false;
        if (c.isEmpty()) {
            clear();
            return true;
        }
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        int capacityMask = tab.length - 2;
        int firstDelayedRemoved = -1;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                if (!c.contains(key)) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    tab[indexToRemove] = REMOVED_BITS;
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i += 2;
                                }
                                tab[indexToRemove] = keyToShift;
                                tab[indexToRemove + 1] = tab[indexToShift + 1];
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                                if (indexToShift == 2 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        tab[i] = REMOVED_BITS;
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    @Override
    void closeDelayedRemoved(int firstDelayedRemoved
            ) {
        long[] tab = table;
        int capacityMask = tab.length - 2;
        for (int i = firstDelayedRemoved; i >= 0; i -= 2) {
            if (tab[i] == REMOVED_BITS) {
                int indexToRemove = i;
                int indexToShift = indexToRemove;
                int shiftDistance = 2;
                while (true) {
                    indexToShift = (indexToShift - 2) & capacityMask;
                    long keyToShift;
                    if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                        break;
                    }
                    if ((keyToShift != REMOVED_BITS) && (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance)) {
                        tab[indexToRemove] = keyToShift;
                        tab[indexToRemove + 1] = tab[indexToShift + 1];
                        indexToRemove = indexToShift;
                        shiftDistance = 2;
                    } else {
                        shiftDistance += 2;
                        if (indexToShift == 2 + i) {
                            throw new java.util.ConcurrentModificationException();
                        }
                    }
                }
                tab[indexToRemove] = FREE_BITS;
                postRemoveHook();
            }
        }
    }



    @Override
    public DoubleIterator iterator() {
        int mc = modCount();
        return new NoRemovedKeyIterator(mc);
    }

    @Override
    public DoubleCursor setCursor() {
        int mc = modCount();
        return new NoRemovedKeyCursor(mc);
    }


    class NoRemovedKeyIterator extends NoRemovedIterator {

        private NoRemovedKeyIterator(int mc) {
            super(mc);
            
        }

        @Override
        public void remove() {
            int index;
            if ((index = this.index) >= 0) {
                if (expectedModCount++ == modCount()) {
                    this.index = -1;
                    long[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 2) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.tab[indexToRemove] = FREE_BITS;
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.nextIndex = index;
                                        if (indexToShift < index - 2) {
                                            this.next = Double.longBitsToDouble(keyToShift);
                                        }
                                    }
                                }
                                tab[indexToRemove] = keyToShift;
                                tab[indexToRemove + 1] = tab[indexToShift + 1];
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                                if (indexToShift == 2 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        justRemove(tab[index]);
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedKeyCursor extends NoRemovedCursor {

        private NoRemovedKeyCursor(int mc) {
            super(mc);
            
        }

        @Override
        public void remove() {
            long curKey;
            if ((curKey = this.curKey) != FREE_BITS) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE_BITS;
                    int index = this.index;
                    long[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.tab[indexToRemove] = FREE_BITS;
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.index = (index += 2);
                                    }
                                }
                                tab[indexToRemove] = keyToShift;
                                tab[indexToRemove + 1] = tab[indexToShift + 1];
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                                if (indexToShift == 2 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        justRemove(curKey);
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }





    class EntryView extends AbstractSetView<Map.Entry<Double, Long>>
            implements HashObjSet<Map.Entry<Double, Long>>,
            InternalObjCollectionOps<Map.Entry<Double, Long>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Double, Long>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Double>defaultEquality()
                    ,
                    Equivalence.<Long>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return MutableLHashParallelKVDoubleLongMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return MutableLHashParallelKVDoubleLongMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return MutableLHashParallelKVDoubleLongMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Double, Long> e = (Map.Entry<Double, Long>) o;
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
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) < FREE_BITS) {
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
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) < FREE_BITS) {
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
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Double, Long>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) < FREE_BITS) {
                    action.accept(new MutableEntry(mc, i, key, tab[i + 1]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Double, Long>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) < FREE_BITS) {
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
        public ObjIterator<Map.Entry<Double, Long>> iterator() {
            int mc = modCount();
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Double, Long>> cursor() {
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
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) < FREE_BITS) {
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
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) < FREE_BITS) {
                    changed |= s.remove(e.with(key, tab[i + 1]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Double, Long>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) < FREE_BITS) {
                    changed |= c.add(new MutableEntry(mc, i, key, tab[i + 1]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        public int hashCode() {
            return MutableLHashParallelKVDoubleLongMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) < FREE_BITS) {
                    sb.append(' ');
                    sb.append(Double.longBitsToDouble(key));
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
            return MutableLHashParallelKVDoubleLongMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Double, Long> e = (Map.Entry<Double, Long>) o;
                double key = e.getKey();
                long value = e.getValue();
                return MutableLHashParallelKVDoubleLongMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Double, Long>> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] tab = table;
            int capacityMask = tab.length - 2;
            int firstDelayedRemoved = -1;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) < FREE_BITS) {
                    if (filter.test(new MutableEntry(mc, i, key, tab[i + 1]))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 2;
                            while (true) {
                                indexToShift = (indexToShift - 2) & capacityMask;
                                long keyToShift;
                                if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                    break;
                                }
                                if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        tab[indexToRemove] = REMOVED_BITS;
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i += 2;
                                    }
                                    tab[indexToRemove] = keyToShift;
                                    tab[indexToRemove + 1] = tab[indexToShift + 1];
                                    indexToRemove = indexToShift;
                                    shiftDistance = 2;
                                } else {
                                    shiftDistance += 2;
                                    if (indexToShift == 2 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            tab[indexToRemove] = FREE_BITS;
                            postRemoveHook();
                        } else {
                            tab[i] = REMOVED_BITS;
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved);
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
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
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            ReusableEntry e = new ReusableEntry();
            int mc = modCount();
            long[] tab = table;
            int capacityMask = tab.length - 2;
            int firstDelayedRemoved = -1;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) < FREE_BITS) {
                    if (c.contains(e.with(key, tab[i + 1]))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 2;
                            while (true) {
                                indexToShift = (indexToShift - 2) & capacityMask;
                                long keyToShift;
                                if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                    break;
                                }
                                if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        tab[indexToRemove] = REMOVED_BITS;
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i += 2;
                                    }
                                    tab[indexToRemove] = keyToShift;
                                    tab[indexToRemove + 1] = tab[indexToShift + 1];
                                    indexToRemove = indexToShift;
                                    shiftDistance = 2;
                                } else {
                                    shiftDistance += 2;
                                    if (indexToShift == 2 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            tab[indexToRemove] = FREE_BITS;
                            postRemoveHook();
                        } else {
                            tab[i] = REMOVED_BITS;
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved);
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public final boolean retainAll(@Nonnull Collection<?> c) {
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty())
                return false;
            if (c.isEmpty()) {
                clear();
                return true;
            }
            boolean changed = false;
            ReusableEntry e = new ReusableEntry();
            int mc = modCount();
            long[] tab = table;
            int capacityMask = tab.length - 2;
            int firstDelayedRemoved = -1;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) < FREE_BITS) {
                    if (!c.contains(e.with(key, tab[i + 1]))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 2;
                            while (true) {
                                indexToShift = (indexToShift - 2) & capacityMask;
                                long keyToShift;
                                if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                    break;
                                }
                                if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        tab[indexToRemove] = REMOVED_BITS;
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i += 2;
                                    }
                                    tab[indexToRemove] = keyToShift;
                                    tab[indexToRemove + 1] = tab[indexToShift + 1];
                                    indexToRemove = indexToShift;
                                    shiftDistance = 2;
                                } else {
                                    shiftDistance += 2;
                                    if (indexToShift == 2 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            tab[indexToRemove] = FREE_BITS;
                            postRemoveHook();
                        } else {
                            tab[i] = REMOVED_BITS;
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved);
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public void clear() {
            MutableLHashParallelKVDoubleLongMapGO.this.clear();
        }
    }


    abstract class DoubleLongEntry extends AbstractEntry<Double, Long> {

        abstract long key();

        @Override
        public final Double getKey() {
            return Double.longBitsToDouble(key());
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
                k2 = Double.doubleToLongBits((Double) e2.getKey());
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


    class MutableEntry extends DoubleLongEntry {
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



    class ReusableEntry extends DoubleLongEntry {
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
            return MutableLHashParallelKVDoubleLongMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return MutableLHashParallelKVDoubleLongMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return MutableLHashParallelKVDoubleLongMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(long v) {
            return MutableLHashParallelKVDoubleLongMapGO.this.containsValue(v);
        }



        @Override
        public void forEach(Consumer<? super Long> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
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
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
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
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
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
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
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
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
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
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
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
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
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
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
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
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
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
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
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
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
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
            MutableLHashParallelKVDoubleLongMapGO.this.clear();
        }

        
        public boolean removeIf(Predicate<? super Long> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] tab = table;
            int capacityMask = tab.length - 2;
            int firstDelayedRemoved = -1;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
                    if (filter.test(tab[i + 1])) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 2;
                            while (true) {
                                indexToShift = (indexToShift - 2) & capacityMask;
                                long keyToShift;
                                if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                    break;
                                }
                                if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        tab[indexToRemove] = REMOVED_BITS;
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i += 2;
                                    }
                                    tab[indexToRemove] = keyToShift;
                                    tab[indexToRemove + 1] = tab[indexToShift + 1];
                                    indexToRemove = indexToShift;
                                    shiftDistance = 2;
                                } else {
                                    shiftDistance += 2;
                                    if (indexToShift == 2 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            tab[indexToRemove] = FREE_BITS;
                            postRemoveHook();
                        } else {
                            tab[i] = REMOVED_BITS;
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved);
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public boolean removeIf(LongPredicate filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] tab = table;
            int capacityMask = tab.length - 2;
            int firstDelayedRemoved = -1;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
                    if (filter.test(tab[i + 1])) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 2;
                            while (true) {
                                indexToShift = (indexToShift - 2) & capacityMask;
                                long keyToShift;
                                if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                    break;
                                }
                                if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        tab[indexToRemove] = REMOVED_BITS;
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i += 2;
                                    }
                                    tab[indexToRemove] = keyToShift;
                                    tab[indexToRemove + 1] = tab[indexToShift + 1];
                                    indexToRemove = indexToShift;
                                    shiftDistance = 2;
                                } else {
                                    shiftDistance += 2;
                                    if (indexToShift == 2 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            tab[indexToRemove] = FREE_BITS;
                            postRemoveHook();
                        } else {
                            tab[i] = REMOVED_BITS;
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved);
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public boolean removeAll(@Nonnull Collection<?> c) {
            if (c instanceof LongCollection)
                return removeAll((LongCollection) c);
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] tab = table;
            int capacityMask = tab.length - 2;
            int firstDelayedRemoved = -1;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
                    if (c.contains(tab[i + 1])) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 2;
                            while (true) {
                                indexToShift = (indexToShift - 2) & capacityMask;
                                long keyToShift;
                                if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                    break;
                                }
                                if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        tab[indexToRemove] = REMOVED_BITS;
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i += 2;
                                    }
                                    tab[indexToRemove] = keyToShift;
                                    tab[indexToRemove + 1] = tab[indexToShift + 1];
                                    indexToRemove = indexToShift;
                                    shiftDistance = 2;
                                } else {
                                    shiftDistance += 2;
                                    if (indexToShift == 2 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            tab[indexToRemove] = FREE_BITS;
                            postRemoveHook();
                        } else {
                            tab[i] = REMOVED_BITS;
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved);
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        private boolean removeAll(LongCollection c) {
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] tab = table;
            int capacityMask = tab.length - 2;
            int firstDelayedRemoved = -1;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
                    if (c.contains(tab[i + 1])) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 2;
                            while (true) {
                                indexToShift = (indexToShift - 2) & capacityMask;
                                long keyToShift;
                                if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                    break;
                                }
                                if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        tab[indexToRemove] = REMOVED_BITS;
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i += 2;
                                    }
                                    tab[indexToRemove] = keyToShift;
                                    tab[indexToRemove + 1] = tab[indexToShift + 1];
                                    indexToRemove = indexToShift;
                                    shiftDistance = 2;
                                } else {
                                    shiftDistance += 2;
                                    if (indexToShift == 2 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            tab[indexToRemove] = FREE_BITS;
                            postRemoveHook();
                        } else {
                            tab[i] = REMOVED_BITS;
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved);
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        @Override
        public boolean retainAll(@Nonnull Collection<?> c) {
            if (c instanceof LongCollection)
                return retainAll((LongCollection) c);
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty())
                return false;
            if (c.isEmpty()) {
                clear();
                return true;
            }
            boolean changed = false;
            int mc = modCount();
            long[] tab = table;
            int capacityMask = tab.length - 2;
            int firstDelayedRemoved = -1;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
                    if (!c.contains(tab[i + 1])) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 2;
                            while (true) {
                                indexToShift = (indexToShift - 2) & capacityMask;
                                long keyToShift;
                                if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                    break;
                                }
                                if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        tab[indexToRemove] = REMOVED_BITS;
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i += 2;
                                    }
                                    tab[indexToRemove] = keyToShift;
                                    tab[indexToRemove + 1] = tab[indexToShift + 1];
                                    indexToRemove = indexToShift;
                                    shiftDistance = 2;
                                } else {
                                    shiftDistance += 2;
                                    if (indexToShift == 2 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            tab[indexToRemove] = FREE_BITS;
                            postRemoveHook();
                        } else {
                            tab[i] = REMOVED_BITS;
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved);
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        private boolean retainAll(LongCollection c) {
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty())
                return false;
            if (c.isEmpty()) {
                clear();
                return true;
            }
            boolean changed = false;
            int mc = modCount();
            long[] tab = table;
            int capacityMask = tab.length - 2;
            int firstDelayedRemoved = -1;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
                    if (!c.contains(tab[i + 1])) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 2;
                            while (true) {
                                indexToShift = (indexToShift - 2) & capacityMask;
                                long keyToShift;
                                if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                    break;
                                }
                                if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        tab[indexToRemove] = REMOVED_BITS;
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i += 2;
                                    }
                                    tab[indexToRemove] = keyToShift;
                                    tab[indexToRemove + 1] = tab[indexToShift + 1];
                                    indexToRemove = indexToShift;
                                    shiftDistance = 2;
                                } else {
                                    shiftDistance += 2;
                                    if (indexToShift == 2 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            tab[indexToRemove] = FREE_BITS;
                            postRemoveHook();
                        } else {
                            tab[i] = REMOVED_BITS;
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved);
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

    }



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Double, Long>> {
        long[] tab;
        final int capacityMask;
        int expectedModCount;
        
        class MutableEntry2 extends MutableEntry {
            MutableEntry2(int modCount, int index, long key, long value) {
                super(modCount, index, key, value);
            }
            
            @Override
            void updateValueInTable(long newValue) {
                if (tab == table) {
                    tab[index + 1] = newValue;
                } else {
                    justPut(key, newValue);
                    if (this.modCount != modCount()) {
                        throw new java.lang.IllegalStateException();
                    }
                }
            }
        }
        
        int index = -1;
        int nextIndex;
        MutableEntry next;

        NoRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            long[] tab = this.tab = table;
            capacityMask = tab.length - 2;
            int nextI = tab.length;
            while ((nextI -= 2) >= 0) {
                long key;
                if ((key = tab[nextI]) < FREE_BITS) {
                    next = new MutableEntry2(mc, nextI, key, tab[nextI + 1]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Double, Long>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) < FREE_BITS) {
                    action.accept(new MutableEntry2(mc, i, key, tab[i + 1]));
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            index = nextIndex = -1;
        }

        @Override
        public boolean hasNext() {
            return nextIndex >= 0;
        }

        @Override
        public Map.Entry<Double, Long> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
                    index = nextI;
                    long[] tab = this.tab;
                    MutableEntry prev = next;
                    while ((nextI -= 2) >= 0) {
                        long key;
                        if ((key = tab[nextI]) < FREE_BITS) {
                            next = new MutableEntry2(mc, nextI, key, tab[nextI + 1]);
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
            int index;
            if ((index = this.index) >= 0) {
                if (expectedModCount++ == modCount()) {
                    this.index = -1;
                    long[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 2) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.tab[indexToRemove] = FREE_BITS;
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.nextIndex = index;
                                        if (indexToShift < index - 2) {
                                            this.next = new MutableEntry2(modCount(), indexToShift, keyToShift, tab[indexToShift + 1]);
                                        }
                                    }
                                }
                                tab[indexToRemove] = keyToShift;
                                tab[indexToRemove + 1] = tab[indexToShift + 1];
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                                if (indexToShift == 2 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        justRemove(tab[index]);
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Double, Long>> {
        long[] tab;
        final int capacityMask;
        int expectedModCount;
        
        class MutableEntry2 extends MutableEntry {
            MutableEntry2(int modCount, int index, long key, long value) {
                super(modCount, index, key, value);
            }
            
            @Override
            void updateValueInTable(long newValue) {
                if (tab == table) {
                    tab[index + 1] = newValue;
                } else {
                    justPut(key, newValue);
                    if (this.modCount != modCount()) {
                        throw new java.lang.IllegalStateException();
                    }
                }
            }
        }
        
        int index;
        long curKey;
        long curValue;

        NoRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            long[] tab = this.tab = table;
            capacityMask = tab.length - 2;
            index = tab.length;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Double, Long>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            int index = this.index;
            for (int i = index - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) < FREE_BITS) {
                    action.accept(new MutableEntry2(mc, i, key, tab[i + 1]));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE_BITS;
        }

        @Override
        public Map.Entry<Double, Long> elem() {
            long curKey;
            if ((curKey = this.curKey) != FREE_BITS) {
                return new MutableEntry2(expectedModCount, index, curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                long[] tab = this.tab;
                for (int i = index - 2; i >= 0; i -= 2) {
                    long key;
                    if ((key = tab[i]) < FREE_BITS) {
                        index = i;
                        curKey = key;
                        curValue = tab[i + 1];
                        return true;
                    }
                }
                curKey = FREE_BITS;
                index = -1;
                return false;
            } else {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            long curKey;
            if ((curKey = this.curKey) != FREE_BITS) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE_BITS;
                    int index = this.index;
                    long[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.tab[indexToRemove] = FREE_BITS;
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.index = (index += 2);
                                    }
                                }
                                tab[indexToRemove] = keyToShift;
                                tab[indexToRemove + 1] = tab[indexToShift + 1];
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                                if (indexToShift == 2 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        justRemove(curKey);
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }




    class NoRemovedValueIterator implements LongIterator {
        long[] tab;
        final int capacityMask;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        long next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            long[] tab = this.tab = table;
            capacityMask = tab.length - 2;
            int nextI = tab.length;
            while ((nextI -= 2) >= 0) {
                if (tab[nextI] < FREE_BITS) {
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
                    index = nextI;
                    long[] tab = this.tab;
                    long prev = next;
                    while ((nextI -= 2) >= 0) {
                        if (tab[nextI] < FREE_BITS) {
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
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
                    action.accept(tab[i + 1]);
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            index = nextIndex = -1;
        }

        @Override
        public void forEachRemaining(LongConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
                    action.accept(tab[i + 1]);
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            index = nextIndex = -1;
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
            int index;
            if ((index = this.index) >= 0) {
                if (expectedModCount++ == modCount()) {
                    this.index = -1;
                    long[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 2) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.tab[indexToRemove] = FREE_BITS;
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.nextIndex = index;
                                        if (indexToShift < index - 2) {
                                            this.next = tab[indexToShift + 1];
                                        }
                                    }
                                }
                                tab[indexToRemove] = keyToShift;
                                tab[indexToRemove + 1] = tab[indexToShift + 1];
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                                if (indexToShift == 2 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        justRemove(tab[index]);
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedValueCursor implements LongCursor {
        long[] tab;
        final int capacityMask;
        int expectedModCount;
        int index;
        long curKey;
        long curValue;

        NoRemovedValueCursor(int mc) {
            expectedModCount = mc;
            long[] tab = this.tab = table;
            capacityMask = tab.length - 2;
            index = tab.length;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(LongConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            int index = this.index;
            for (int i = index - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
                    action.accept(tab[i + 1]);
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE_BITS;
        }

        @Override
        public long elem() {
            if (curKey != FREE_BITS) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                long[] tab = this.tab;
                for (int i = index - 2; i >= 0; i -= 2) {
                    long key;
                    if ((key = tab[i]) < FREE_BITS) {
                        index = i;
                        curKey = key;
                        curValue = tab[i + 1];
                        return true;
                    }
                }
                curKey = FREE_BITS;
                index = -1;
                return false;
            } else {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            long curKey;
            if ((curKey = this.curKey) != FREE_BITS) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE_BITS;
                    int index = this.index;
                    long[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.tab[indexToRemove] = FREE_BITS;
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.index = (index += 2);
                                    }
                                }
                                tab[indexToRemove] = keyToShift;
                                tab[indexToRemove + 1] = tab[indexToShift + 1];
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                                if (indexToShift == 2 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        justRemove(curKey);
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }



    class NoRemovedMapCursor implements DoubleLongCursor {
        long[] tab;
        final int capacityMask;
        int expectedModCount;
        int index;
        long curKey;
        long curValue;

        NoRemovedMapCursor(int mc) {
            expectedModCount = mc;
            long[] tab = this.tab = table;
            capacityMask = tab.length - 2;
            index = tab.length;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(DoubleLongConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            int index = this.index;
            for (int i = index - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) < FREE_BITS) {
                    action.accept(Double.longBitsToDouble(key), tab[i + 1]);
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE_BITS;
        }

        @Override
        public double key() {
            long curKey;
            if ((curKey = this.curKey) != FREE_BITS) {
                return Double.longBitsToDouble(curKey);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public long value() {
            if (curKey != FREE_BITS) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(long value) {
            if (curKey != FREE_BITS) {
                if (expectedModCount == modCount()) {
                    tab[index + 1] = value;
                    if (tab != table) {
                        table[index + 1] = value;
                    }
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
                for (int i = index - 2; i >= 0; i -= 2) {
                    long key;
                    if ((key = tab[i]) < FREE_BITS) {
                        index = i;
                        curKey = key;
                        curValue = tab[i + 1];
                        return true;
                    }
                }
                curKey = FREE_BITS;
                index = -1;
                return false;
            } else {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            long curKey;
            if ((curKey = this.curKey) != FREE_BITS) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE_BITS;
                    int index = this.index;
                    long[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 2;
                        while (true) {
                            indexToShift = (indexToShift - 2) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = tab[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.tab[indexToRemove] = FREE_BITS;
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.index = (index += 2);
                                    }
                                }
                                tab[indexToRemove] = keyToShift;
                                tab[indexToRemove + 1] = tab[indexToShift + 1];
                                indexToRemove = indexToShift;
                                shiftDistance = 2;
                            } else {
                                shiftDistance += 2;
                                if (indexToShift == 2 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        tab[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        justRemove(curKey);
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }
}

