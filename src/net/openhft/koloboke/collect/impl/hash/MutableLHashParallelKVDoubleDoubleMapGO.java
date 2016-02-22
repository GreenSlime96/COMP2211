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
import net.openhft.koloboke.function.DoubleDoubleConsumer;
import net.openhft.koloboke.function.DoubleDoublePredicate;
import net.openhft.koloboke.function.DoubleDoubleToDoubleFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class MutableLHashParallelKVDoubleDoubleMapGO
        extends MutableLHashParallelKVDoubleDoubleMapSO {

    
    final void copy(ParallelKVDoubleDoubleLHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    
    final void move(ParallelKVDoubleDoubleLHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.move(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }


    @Override
    public double defaultValue() {
        return 0.0;
    }

    @Override
    public boolean containsEntry(double key, double value) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            return table[index + 1] == Double.doubleToLongBits(value);
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
    public Double get(Object key) {
        long k = Double.doubleToLongBits((Double) key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            return Double.longBitsToDouble(table[index + 1]);
        } else {
            // key is absent
            return null;
        }
    }

    

    @Override
    public double get(double key) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            return Double.longBitsToDouble(table[index + 1]);
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Double getOrDefault(Object key, Double defaultValue) {
        long k = Double.doubleToLongBits((Double) key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            return Double.longBitsToDouble(table[index + 1]);
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public double getOrDefault(double key, double defaultValue) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            return Double.longBitsToDouble(table[index + 1]);
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public void forEach(BiConsumer<? super Double, ? super Double> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                action.accept(Double.longBitsToDouble(key), Double.longBitsToDouble(tab[i + 1]));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void forEach(DoubleDoubleConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                action.accept(Double.longBitsToDouble(key), Double.longBitsToDouble(tab[i + 1]));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public boolean forEachWhile(DoubleDoublePredicate predicate) {
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
                if (!predicate.test(Double.longBitsToDouble(key), Double.longBitsToDouble(tab[i + 1]))) {
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
    public DoubleDoubleCursor cursor() {
        int mc = modCount();
        return new NoRemovedMapCursor(mc);
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonDoubleDoubleMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalDoubleDoubleMapOps m) {
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
    public void reversePutAllTo(InternalDoubleDoubleMapOps m) {
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
    public HashObjSet<Map.Entry<Double, Double>> entrySet() {
        return new EntryView();
    }

    @Override
    @Nonnull
    public DoubleCollection values() {
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
                sb.append(Double.longBitsToDouble(tab[i + 1]));
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
    public Double put(Double key, Double value) {
        long k = Double.doubleToLongBits(key);
        int index = insert(k, Double.doubleToLongBits(value));
        if (index < 0) {
            // key was absent
            return null;
        } else {
            // key is present
            long[] tab = table;
            double prevValue = Double.longBitsToDouble(tab[index + 1]);
            tab[index + 1] = Double.doubleToLongBits(value);
            return prevValue;
        }
    }

    @Override
    public double put(double key, double value) {
        long k = Double.doubleToLongBits(key);
        int index = insert(k, Double.doubleToLongBits(value));
        if (index < 0) {
            // key was absent
            return defaultValue();
        } else {
            // key is present
            long[] tab = table;
            double prevValue = Double.longBitsToDouble(tab[index + 1]);
            tab[index + 1] = Double.doubleToLongBits(value);
            return prevValue;
        }
    }

    @Override
    public Double putIfAbsent(Double key, Double value) {
        long k = Double.doubleToLongBits(key);
        int index = insert(k, Double.doubleToLongBits(value));
        if (index < 0) {
            // key was absent
            return null;
        } else {
            // key is present
            return Double.longBitsToDouble(table[index + 1]);
        }
    }

    @Override
    public double putIfAbsent(double key, double value) {
        long k = Double.doubleToLongBits(key);
        int index = insert(k, Double.doubleToLongBits(value));
        if (index < 0) {
            // key was absent
            return defaultValue();
        } else {
            // key is present
            return Double.longBitsToDouble(table[index + 1]);
        }
    }

    @Override
    public void justPut(double key, double value) {
        long k = Double.doubleToLongBits(key);
        int index = insert(k, Double.doubleToLongBits(value));
        if (index < 0) {
            // key was absent
            return;
        } else {
            // key is present
            table[index + 1] = Double.doubleToLongBits(value);
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
    public Double compute(Double key,
            BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
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
            Double newValue = remappingFunction.apply(Double.longBitsToDouble(k), null);
            if (newValue != null) {
                incrementModCount();
                tab[index] = k;
                tab[index + 1] = Double.doubleToLongBits(newValue);
                postInsertHook();
                return newValue;
            } else {
                return null;
            }
        }
        // key is present
        Double newValue = remappingFunction.apply(Double.longBitsToDouble(k), Double.longBitsToDouble(tab[index + 1]));
        if (newValue != null) {
            tab[index + 1] = Double.doubleToLongBits(newValue);
            return newValue;
        } else {
            removeAt(index);
            return null;
        }
    }


    @Override
    public double compute(double key, DoubleDoubleToDoubleFunction remappingFunction) {
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
            double newValue = remappingFunction.applyAsDouble(Double.longBitsToDouble(k), defaultValue());
            incrementModCount();
            tab[index] = k;
            tab[index + 1] = Double.doubleToLongBits(newValue);
            postInsertHook();
            return newValue;
        }
        // key is present
        double newValue = remappingFunction.applyAsDouble(Double.longBitsToDouble(k), Double.longBitsToDouble(tab[index + 1]));
        tab[index + 1] = Double.doubleToLongBits(newValue);
        return newValue;
    }


    @Override
    public Double computeIfAbsent(Double key,
            Function<? super Double, ? extends Double> mappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacityMask, index;
        long cur;
        if ((cur = tab[index = ParallelKVDoubleKeyMixing.mix(k) & (capacityMask = tab.length - 2)]) == k) {
            // key is present
            return Double.longBitsToDouble(tab[index + 1]);
        } else {
            keyAbsent:
            if (cur != FREE_BITS) {
                while (true) {
                    if ((cur = tab[(index = (index - 2) & capacityMask)]) == k) {
                        // key is present
                        return Double.longBitsToDouble(tab[index + 1]);
                    } else if (cur == FREE_BITS) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            Double value = mappingFunction.apply(Double.longBitsToDouble(k));
            if (value != null) {
                incrementModCount();
                tab[index] = k;
                tab[index + 1] = Double.doubleToLongBits(value);
                postInsertHook();
                return value;
            } else {
                return null;
            }
        }
    }


    @Override
    public double computeIfAbsent(double key, DoubleUnaryOperator mappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacityMask, index;
        long cur;
        if ((cur = tab[index = ParallelKVDoubleKeyMixing.mix(k) & (capacityMask = tab.length - 2)]) == k) {
            // key is present
            return Double.longBitsToDouble(tab[index + 1]);
        } else {
            keyAbsent:
            if (cur != FREE_BITS) {
                while (true) {
                    if ((cur = tab[(index = (index - 2) & capacityMask)]) == k) {
                        // key is present
                        return Double.longBitsToDouble(tab[index + 1]);
                    } else if (cur == FREE_BITS) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            double value = mappingFunction.applyAsDouble(Double.longBitsToDouble(k));
            incrementModCount();
            tab[index] = k;
            tab[index + 1] = Double.doubleToLongBits(value);
            postInsertHook();
            return value;
        }
    }


    @Override
    public Double computeIfPresent(Double key,
            BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(k);
        if (index >= 0) {
            // key is present
            long[] tab = table;
            Double newValue = remappingFunction.apply(Double.longBitsToDouble(k), Double.longBitsToDouble(tab[index + 1]));
            if (newValue != null) {
                tab[index + 1] = Double.doubleToLongBits(newValue);
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
    public double computeIfPresent(double key, DoubleDoubleToDoubleFunction remappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(k);
        if (index >= 0) {
            // key is present
            long[] tab = table;
            double newValue = remappingFunction.applyAsDouble(Double.longBitsToDouble(k), Double.longBitsToDouble(tab[index + 1]));
            tab[index + 1] = Double.doubleToLongBits(newValue);
            return newValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Double merge(Double key, Double value,
            BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
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
            tab[index + 1] = Double.doubleToLongBits(value);
            postInsertHook();
            return value;
        }
        // key is present
        Double newValue = remappingFunction.apply(Double.longBitsToDouble(tab[index + 1]), value);
        if (newValue != null) {
            tab[index + 1] = Double.doubleToLongBits(newValue);
            return newValue;
        } else {
            removeAt(index);
            return null;
        }
    }


    @Override
    public double merge(double key, double value, DoubleBinaryOperator remappingFunction) {
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
            tab[index + 1] = Double.doubleToLongBits(value);
            postInsertHook();
            return value;
        }
        // key is present
        double newValue = remappingFunction.applyAsDouble(Double.longBitsToDouble(tab[index + 1]), value);
        tab[index + 1] = Double.doubleToLongBits(newValue);
        return newValue;
    }


    @Override
    public double addValue(double key, double value) {
        long k = Double.doubleToLongBits(key);
        int index = insert(k, Double.doubleToLongBits(value));
        if (index < 0) {
            // key was absent
            return value;
        } else {
            // key is present
            long[] tab = table;
            double newValue = Double.longBitsToDouble(tab[index + 1]) + value;
            tab[index + 1] = Double.doubleToLongBits(newValue);
            return newValue;
        }
    }

    @Override
    public double addValue(double key, double addition, double defaultValue) {
        long k = Double.doubleToLongBits(key);
        double value = defaultValue + addition;
        int index = insert(k, Double.doubleToLongBits(value));
        if (index < 0) {
            // key was absent
            return value;
        } else {
            // key is present
            long[] tab = table;
            double newValue = Double.longBitsToDouble(tab[index + 1]) + addition;
            tab[index + 1] = Double.doubleToLongBits(newValue);
            return newValue;
        }
    }


    @Override
    public void putAll(@Nonnull Map<? extends Double, ? extends Double> m) {
        CommonDoubleDoubleMapOps.putAll(this, m);
    }


    @Override
    public Double replace(Double key, Double value) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            long[] tab = table;
            double oldValue = Double.longBitsToDouble(tab[index + 1]);
            tab[index + 1] = Double.doubleToLongBits(value);
            return oldValue;
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public double replace(double key, double value) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            long[] tab = table;
            double oldValue = Double.longBitsToDouble(tab[index + 1]);
            tab[index + 1] = Double.doubleToLongBits(value);
            return oldValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public boolean replace(Double key, Double oldValue, Double newValue) {
        return replace(key.doubleValue(),
                oldValue.doubleValue(),
                newValue.doubleValue());
    }

    @Override
    public boolean replace(double key, double oldValue, double newValue) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            long[] tab = table;
            if (tab[index + 1] == Double.doubleToLongBits(oldValue)) {
                tab[index + 1] = Double.doubleToLongBits(newValue);
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
            BiFunction<? super Double, ? super Double, ? extends Double> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                tab[i + 1] = Double.doubleToLongBits(function.apply(Double.longBitsToDouble(key), Double.longBitsToDouble(tab[i + 1])));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void replaceAll(DoubleDoubleToDoubleFunction function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] tab = table;
        for (int i = tab.length - 2; i >= 0; i -= 2) {
            long key;
            if ((key = tab[i]) < FREE_BITS) {
                tab[i + 1] = Double.doubleToLongBits(function.applyAsDouble(Double.longBitsToDouble(key), Double.longBitsToDouble(tab[i + 1])));
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
    public Double remove(Object key) {
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
        double val = Double.longBitsToDouble(tab[index + 1]);
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
    public double remove(double key) {
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
        double val = Double.longBitsToDouble(tab[index + 1]);
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
                ((Double) value).doubleValue()
                );
    }

    @Override
    public boolean remove(double key, double value) {
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
        if (tab[index + 1] == Double.doubleToLongBits(value)) {
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
    public boolean removeIf(DoubleDoublePredicate filter) {
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
                if (filter.test(Double.longBitsToDouble(key), Double.longBitsToDouble(tab[i + 1]))) {
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





    class EntryView extends AbstractSetView<Map.Entry<Double, Double>>
            implements HashObjSet<Map.Entry<Double, Double>>,
            InternalObjCollectionOps<Map.Entry<Double, Double>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Double, Double>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Double>defaultEquality()
                    ,
                    Equivalence.<Double>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return MutableLHashParallelKVDoubleDoubleMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return MutableLHashParallelKVDoubleDoubleMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return MutableLHashParallelKVDoubleDoubleMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Double, Double> e = (Map.Entry<Double, Double>) o;
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
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Double, Double>> action) {
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
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Double, Double>> predicate) {
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
        public ObjIterator<Map.Entry<Double, Double>> iterator() {
            int mc = modCount();
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Double, Double>> cursor() {
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
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Double, Double>> c) {
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
            return MutableLHashParallelKVDoubleDoubleMapGO.this.hashCode();
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
                    sb.append(Double.longBitsToDouble(tab[i + 1]));
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
            return MutableLHashParallelKVDoubleDoubleMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Double, Double> e = (Map.Entry<Double, Double>) o;
                double key = e.getKey();
                double value = e.getValue();
                return MutableLHashParallelKVDoubleDoubleMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Double, Double>> filter) {
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
            MutableLHashParallelKVDoubleDoubleMapGO.this.clear();
        }
    }


    abstract class DoubleDoubleEntry extends AbstractEntry<Double, Double> {

        abstract long key();

        @Override
        public final Double getKey() {
            return Double.longBitsToDouble(key());
        }

        abstract long value();

        @Override
        public final Double getValue() {
            return Double.longBitsToDouble(value());
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
                v2 = Double.doubleToLongBits((Double) e2.getValue());
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


    class MutableEntry extends DoubleDoubleEntry {
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
        public Double setValue(Double newValue) {
            if (modCount != modCount())
                throw new IllegalStateException();
            double oldValue = Double.longBitsToDouble(value);
            long unwrappedNewValue = Double.doubleToLongBits(newValue);
            value = unwrappedNewValue;
            updateValueInTable(unwrappedNewValue);
            return oldValue;
        }

        void updateValueInTable(long newValue) {
            table[index + 1] = newValue;
        }
    }



    class ReusableEntry extends DoubleDoubleEntry {
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


    class ValueView extends AbstractDoubleValueView {


        @Override
        public int size() {
            return MutableLHashParallelKVDoubleDoubleMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return MutableLHashParallelKVDoubleDoubleMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return MutableLHashParallelKVDoubleDoubleMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(double v) {
            return MutableLHashParallelKVDoubleDoubleMapGO.this.containsValue(v);
        }

        @Override
        public boolean contains(long bits) {
            return MutableLHashParallelKVDoubleDoubleMapGO.this.containsValue(bits);
        }


        @Override
        public void forEach(Consumer<? super Double> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
                    action.accept(Double.longBitsToDouble(tab[i + 1]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public void forEach(DoubleConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
                    action.accept(Double.longBitsToDouble(tab[i + 1]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(DoublePredicate predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
                    if (!predicate.test(Double.longBitsToDouble(tab[i + 1]))) {
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
        public boolean allContainingIn(DoubleCollection c) {
            if (c instanceof InternalDoubleCollectionOps)
                return allContainingIn((InternalDoubleCollectionOps) c);
            if (isEmpty())
                return true;
            boolean containsAll = true;
            int mc = modCount();
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
                    if (!c.contains(Double.longBitsToDouble(tab[i + 1]))) {
                        containsAll = false;
                        break;
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return containsAll;
        }

        private boolean allContainingIn(InternalDoubleCollectionOps c) {
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
        public boolean reverseAddAllTo(DoubleCollection c) {
            if (c instanceof InternalDoubleCollectionOps)
                return reverseAddAllTo((InternalDoubleCollectionOps) c);
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
                    changed |= c.add(Double.longBitsToDouble(tab[i + 1]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        private boolean reverseAddAllTo(InternalDoubleCollectionOps c) {
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
        public boolean reverseRemoveAllFrom(DoubleSet s) {
            if (s instanceof InternalDoubleCollectionOps)
                return reverseRemoveAllFrom((InternalDoubleCollectionOps) s);
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
                    changed |= s.removeDouble(Double.longBitsToDouble(tab[i + 1]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        private boolean reverseRemoveAllFrom(InternalDoubleCollectionOps s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
                    changed |= s.removeDouble(tab[i + 1]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        @Override
        @Nonnull
        public DoubleIterator iterator() {
            int mc = modCount();
            return new NoRemovedValueIterator(mc);
        }

        @Nonnull
        @Override
        public DoubleCursor cursor() {
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
                    result[resultIndex++] = Double.longBitsToDouble(tab[i + 1]);
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
                    a[resultIndex++] = (T) Double.valueOf(Double.longBitsToDouble(tab[i + 1]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }

        @Override
        public double[] toDoubleArray() {
            int size = size();
            double[] result = new double[size];
            if (size == 0)
                return result;
            int resultIndex = 0;
            int mc = modCount();
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
                    result[resultIndex++] = Double.longBitsToDouble(tab[i + 1]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return result;
        }

        @Override
        public double[] toArray(double[] a) {
            int size = size();
            if (a.length < size)
                a = new double[size];
            if (size == 0) {
                if (a.length > 0)
                    a[0] = 0.0;
                return a;
            }
            int resultIndex = 0;
            int mc = modCount();
            long[] tab = table;
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
                    a[resultIndex++] = Double.longBitsToDouble(tab[i + 1]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            if (a.length > resultIndex)
                a[resultIndex] = 0.0;
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
                    sb.append(' ').append(Double.longBitsToDouble(tab[i + 1])).append(',');
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
            return removeDouble(( Double ) o);
        }

        @Override
        public boolean removeDouble(double v) {
            return removeValue(v);
        }

        @Override
        public boolean removeDouble(long bits) {
            return removeValue(bits);
        }


        @Override
        public void clear() {
            MutableLHashParallelKVDoubleDoubleMapGO.this.clear();
        }

        
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
                if (tab[i] < FREE_BITS) {
                    if (filter.test(Double.longBitsToDouble(tab[i + 1]))) {
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
                if (tab[i] < FREE_BITS) {
                    if (filter.test(Double.longBitsToDouble(tab[i + 1]))) {
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
            if (c instanceof DoubleCollection)
                return removeAll((DoubleCollection) c);
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
                    if (c.contains(Double.longBitsToDouble(tab[i + 1]))) {
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

        private boolean removeAll(DoubleCollection c) {
            if (c instanceof InternalDoubleCollectionOps)
                return removeAll((InternalDoubleCollectionOps) c);
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
                    if (c.contains(Double.longBitsToDouble(tab[i + 1]))) {
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

        private boolean removeAll(InternalDoubleCollectionOps c) {
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
            if (c instanceof DoubleCollection)
                return retainAll((DoubleCollection) c);
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
                    if (!c.contains(Double.longBitsToDouble(tab[i + 1]))) {
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

        private boolean retainAll(DoubleCollection c) {
            if (c instanceof InternalDoubleCollectionOps)
                return retainAll((InternalDoubleCollectionOps) c);
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
                    if (!c.contains(Double.longBitsToDouble(tab[i + 1]))) {
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

        private boolean retainAll(InternalDoubleCollectionOps c) {
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Double, Double>> {
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
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Double, Double>> action) {
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
        public Map.Entry<Double, Double> next() {
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


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Double, Double>> {
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
        public void forEachForward(Consumer<? super Map.Entry<Double, Double>> action) {
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
        public Map.Entry<Double, Double> elem() {
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




    class NoRemovedValueIterator implements DoubleIterator {
        long[] tab;
        final int capacityMask;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        double next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            long[] tab = this.tab = table;
            capacityMask = tab.length - 2;
            int nextI = tab.length;
            while ((nextI -= 2) >= 0) {
                if (tab[nextI] < FREE_BITS) {
                    next = Double.longBitsToDouble(tab[nextI + 1]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public double nextDouble() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                if (expectedModCount == modCount()) {
                    index = nextI;
                    long[] tab = this.tab;
                    double prev = next;
                    while ((nextI -= 2) >= 0) {
                        if (tab[nextI] < FREE_BITS) {
                            next = Double.longBitsToDouble(tab[nextI + 1]);
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
        public void forEachRemaining(Consumer<? super Double> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
                    action.accept(Double.longBitsToDouble(tab[i + 1]));
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            index = nextIndex = -1;
        }

        @Override
        public void forEachRemaining(DoubleConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
                    action.accept(Double.longBitsToDouble(tab[i + 1]));
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
        public Double next() {
            return nextDouble();
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
                                            this.next = Double.longBitsToDouble(tab[indexToShift + 1]);
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


    class NoRemovedValueCursor implements DoubleCursor {
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
        public void forEachForward(DoubleConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            int index = this.index;
            for (int i = index - 2; i >= 0; i -= 2) {
                if (tab[i] < FREE_BITS) {
                    action.accept(Double.longBitsToDouble(tab[i + 1]));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE_BITS;
        }

        @Override
        public double elem() {
            if (curKey != FREE_BITS) {
                return Double.longBitsToDouble(curValue);
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



    class NoRemovedMapCursor implements DoubleDoubleCursor {
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
        public void forEachForward(DoubleDoubleConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            int index = this.index;
            for (int i = index - 2; i >= 0; i -= 2) {
                long key;
                if ((key = tab[i]) < FREE_BITS) {
                    action.accept(Double.longBitsToDouble(key), Double.longBitsToDouble(tab[i + 1]));
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
        public double value() {
            if (curKey != FREE_BITS) {
                return Double.longBitsToDouble(curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(double value) {
            if (curKey != FREE_BITS) {
                if (expectedModCount == modCount()) {
                    tab[index + 1] = Double.doubleToLongBits(value);
                    if (tab != table) {
                        table[index + 1] = Double.doubleToLongBits(value);
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

