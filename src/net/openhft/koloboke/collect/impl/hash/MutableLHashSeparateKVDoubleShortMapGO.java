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
import net.openhft.koloboke.function.DoubleShortConsumer;
import net.openhft.koloboke.function.DoubleShortPredicate;
import net.openhft.koloboke.function.DoubleShortToShortFunction;
import net.openhft.koloboke.function.DoubleToShortFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.openhft.koloboke.function.ShortBinaryOperator;
import net.openhft.koloboke.function.ShortConsumer;
import net.openhft.koloboke.function.ShortPredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class MutableLHashSeparateKVDoubleShortMapGO
        extends MutableLHashSeparateKVDoubleShortMapSO {

    @Override
    final void copy(SeparateKVDoubleShortLHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVDoubleShortLHash hash) {
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
    public boolean containsEntry(double key, short value) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            return values[index] == value;
        } else {
            // key is absent
            return false;
        }
    }

    @Override
    public boolean containsEntry(long key, short value) {
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
    public Short get(Object key) {
        long k = Double.doubleToLongBits((Double) key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return null;
        }
    }

    

    @Override
    public short get(double key) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Short getOrDefault(Object key, Short defaultValue) {
        long k = Double.doubleToLongBits((Double) key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public short getOrDefault(double key, short defaultValue) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public void forEach(BiConsumer<? super Double, ? super Short> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] keys = set;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                action.accept(Double.longBitsToDouble(key), vals[i]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void forEach(DoubleShortConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] keys = set;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                action.accept(Double.longBitsToDouble(key), vals[i]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public boolean forEachWhile(DoubleShortPredicate predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        long[] keys = set;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                if (!predicate.test(Double.longBitsToDouble(key), vals[i])) {
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
    public DoubleShortCursor cursor() {
        int mc = modCount();
        return new NoRemovedMapCursor(mc);
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonDoubleShortMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalDoubleShortMapOps m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        long[] keys = set;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                if (!m.containsEntry(key, vals[i])) {
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
    public void reversePutAllTo(InternalDoubleShortMapOps m) {
        if (isEmpty())
            return;
        int mc = modCount();
        long[] keys = set;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                m.justPut(key, vals[i]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<Double, Short>> entrySet() {
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
        int mc = modCount();
        long[] keys = set;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                hashCode += ((int) (key ^ (key >>> 32))) ^ vals[i];
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
        long[] keys = set;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                sb.append(' ');
                sb.append(Double.longBitsToDouble(key));
                sb.append('=');
                sb.append(vals[i]);
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
        long[] keys = set;
        short[] vals = values;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        long[] newKeys = set;
        int capacityMask = newKeys.length - 1;
        short[] newVals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                int index;
                if (newKeys[index = SeparateKVDoubleKeyMixing.mix(key) & capacityMask] != FREE_BITS) {
                    while (true) {
                        if (newKeys[(index = (index - 1) & capacityMask)] == FREE_BITS) {
                            break;
                        }
                    }
                }
                newKeys[index] = key;
                newVals[index] = vals[i];
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }


    @Override
    public Short put(Double key, Short value) {
        long k = Double.doubleToLongBits(key);
        int index = insert(k, value);
        if (index < 0) {
            // key was absent
            return null;
        } else {
            // key is present
            short[] vals = values;
            short prevValue = vals[index];
            vals[index] = value;
            return prevValue;
        }
    }

    @Override
    public short put(double key, short value) {
        long k = Double.doubleToLongBits(key);
        int index = insert(k, value);
        if (index < 0) {
            // key was absent
            return defaultValue();
        } else {
            // key is present
            short[] vals = values;
            short prevValue = vals[index];
            vals[index] = value;
            return prevValue;
        }
    }

    @Override
    public Short putIfAbsent(Double key, Short value) {
        long k = Double.doubleToLongBits(key);
        int index = insert(k, value);
        if (index < 0) {
            // key was absent
            return null;
        } else {
            // key is present
            return values[index];
        }
    }

    @Override
    public short putIfAbsent(double key, short value) {
        long k = Double.doubleToLongBits(key);
        int index = insert(k, value);
        if (index < 0) {
            // key was absent
            return defaultValue();
        } else {
            // key is present
            return values[index];
        }
    }

    @Override
    public void justPut(double key, short value) {
        long k = Double.doubleToLongBits(key);
        int index = insert(k, value);
        if (index < 0) {
            // key was absent
            return;
        } else {
            // key is present
            values[index] = value;
            return;
        }
    }

    @Override
    public void justPut(long key, short value) {
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return;
        } else {
            // key is present
            values[index] = value;
            return;
        }
    }

    @Override
    public Short compute(Double key,
            BiFunction<? super Double, ? super Short, ? extends Short> remappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] keys = set;
        short[] vals = values;
        int capacityMask, index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(k) & (capacityMask = keys.length - 1)]) != k) {
            keyAbsent:
            if (cur != FREE_BITS) {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            Short newValue = remappingFunction.apply(Double.longBitsToDouble(k), null);
            if (newValue != null) {
                incrementModCount();
                keys[index] = k;
                vals[index] = newValue;
                postInsertHook();
                return newValue;
            } else {
                return null;
            }
        }
        // key is present
        Short newValue = remappingFunction.apply(Double.longBitsToDouble(k), vals[index]);
        if (newValue != null) {
            vals[index] = newValue;
            return newValue;
        } else {
            removeAt(index);
            return null;
        }
    }


    @Override
    public short compute(double key, DoubleShortToShortFunction remappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] keys = set;
        short[] vals = values;
        int capacityMask, index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(k) & (capacityMask = keys.length - 1)]) != k) {
            keyAbsent:
            if (cur != FREE_BITS) {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            short newValue = remappingFunction.applyAsShort(Double.longBitsToDouble(k), defaultValue());
            incrementModCount();
            keys[index] = k;
            vals[index] = newValue;
            postInsertHook();
            return newValue;
        }
        // key is present
        short newValue = remappingFunction.applyAsShort(Double.longBitsToDouble(k), vals[index]);
        vals[index] = newValue;
        return newValue;
    }


    @Override
    public Short computeIfAbsent(Double key,
            Function<? super Double, ? extends Short> mappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] keys = set;
        short[] vals = values;
        int capacityMask, index;
        long cur;
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(k) & (capacityMask = keys.length - 1)]) == k) {
            // key is present
            return vals[index];
        } else {
            keyAbsent:
            if (cur != FREE_BITS) {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == k) {
                        // key is present
                        return vals[index];
                    } else if (cur == FREE_BITS) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            Short value = mappingFunction.apply(Double.longBitsToDouble(k));
            if (value != null) {
                incrementModCount();
                keys[index] = k;
                vals[index] = value;
                postInsertHook();
                return value;
            } else {
                return null;
            }
        }
    }


    @Override
    public short computeIfAbsent(double key, DoubleToShortFunction mappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] keys = set;
        short[] vals = values;
        int capacityMask, index;
        long cur;
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(k) & (capacityMask = keys.length - 1)]) == k) {
            // key is present
            return vals[index];
        } else {
            keyAbsent:
            if (cur != FREE_BITS) {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == k) {
                        // key is present
                        return vals[index];
                    } else if (cur == FREE_BITS) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            short value = mappingFunction.applyAsShort(Double.longBitsToDouble(k));
            incrementModCount();
            keys[index] = k;
            vals[index] = value;
            postInsertHook();
            return value;
        }
    }


    @Override
    public Short computeIfPresent(Double key,
            BiFunction<? super Double, ? super Short, ? extends Short> remappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(k);
        if (index >= 0) {
            // key is present
            short[] vals = values;
            Short newValue = remappingFunction.apply(Double.longBitsToDouble(k), vals[index]);
            if (newValue != null) {
                vals[index] = newValue;
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
    public short computeIfPresent(double key, DoubleShortToShortFunction remappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(k);
        if (index >= 0) {
            // key is present
            short[] vals = values;
            short newValue = remappingFunction.applyAsShort(Double.longBitsToDouble(k), vals[index]);
            vals[index] = newValue;
            return newValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Short merge(Double key, Short value,
            BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (value == null)
            throw new java.lang.NullPointerException();
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] keys = set;
        short[] vals = values;
        int capacityMask, index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(k) & (capacityMask = keys.length - 1)]) != k) {
            keyAbsent:
            if (cur != FREE_BITS) {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            incrementModCount();
            keys[index] = k;
            vals[index] = value;
            postInsertHook();
            return value;
        }
        // key is present
        Short newValue = remappingFunction.apply(vals[index], value);
        if (newValue != null) {
            vals[index] = newValue;
            return newValue;
        } else {
            removeAt(index);
            return null;
        }
    }


    @Override
    public short merge(double key, short value, ShortBinaryOperator remappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] keys = set;
        short[] vals = values;
        int capacityMask, index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(k) & (capacityMask = keys.length - 1)]) != k) {
            keyAbsent:
            if (cur != FREE_BITS) {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            incrementModCount();
            keys[index] = k;
            vals[index] = value;
            postInsertHook();
            return value;
        }
        // key is present
        short newValue = remappingFunction.applyAsShort(vals[index], value);
        vals[index] = newValue;
        return newValue;
    }


    @Override
    public short addValue(double key, short value) {
        long k = Double.doubleToLongBits(key);
        int index = insert(k, value);
        if (index < 0) {
            // key was absent
            return value;
        } else {
            // key is present
            short[] vals = values;
            short newValue = (short) (vals[index] + value);
            vals[index] = newValue;
            return newValue;
        }
    }

    @Override
    public short addValue(double key, short addition, short defaultValue) {
        long k = Double.doubleToLongBits(key);
        short value = (short) (defaultValue + addition);
        int index = insert(k, value);
        if (index < 0) {
            // key was absent
            return value;
        } else {
            // key is present
            short[] vals = values;
            short newValue = (short) (vals[index] + addition);
            vals[index] = newValue;
            return newValue;
        }
    }


    @Override
    public void putAll(@Nonnull Map<? extends Double, ? extends Short> m) {
        CommonDoubleShortMapOps.putAll(this, m);
    }


    @Override
    public Short replace(Double key, Short value) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            short[] vals = values;
            short oldValue = vals[index];
            vals[index] = value;
            return oldValue;
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public short replace(double key, short value) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            short[] vals = values;
            short oldValue = vals[index];
            vals[index] = value;
            return oldValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public boolean replace(Double key, Short oldValue, Short newValue) {
        return replace(key.doubleValue(),
                oldValue.shortValue(),
                newValue.shortValue());
    }

    @Override
    public boolean replace(double key, short oldValue, short newValue) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            short[] vals = values;
            if (vals[index] == oldValue) {
                vals[index] = newValue;
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
            BiFunction<? super Double, ? super Short, ? extends Short> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] keys = set;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                vals[i] = function.apply(Double.longBitsToDouble(key), vals[i]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void replaceAll(DoubleShortToShortFunction function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] keys = set;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                vals[i] = function.applyAsShort(Double.longBitsToDouble(key), vals[i]);
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
        long[] keys = set;
        short[] vals = values;
        int capacityMask = keys.length - 1;
        incrementModCount();
        int indexToRemove = index;
        int indexToShift = indexToRemove;
        int shiftDistance = 1;
        while (true) {
            indexToShift = (indexToShift - 1) & capacityMask;
            long keyToShift;
            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                break;
            }
            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                keys[indexToRemove] = keyToShift;
                vals[indexToRemove] = vals[indexToShift];
                indexToRemove = indexToShift;
                shiftDistance = 1;
            } else {
                shiftDistance++;
                if (indexToShift == 1 + index) {
                    throw new java.util.ConcurrentModificationException();
                }
            }
        }
        keys[indexToRemove] = FREE_BITS;
        postRemoveHook();
    }

    @Override
    public Short remove(Object key) {
        long k = Double.doubleToLongBits((Double) key);
        long[] keys = set;
        int capacityMask = keys.length - 1;
        int index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(k) & capacityMask]) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return null;
            } else {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return null;
                    }
                }
            }
        }
        // key is present
        short[] vals = values;
        short val = vals[index];
        incrementModCount();
        int indexToRemove = index;
        int indexToShift = indexToRemove;
        int shiftDistance = 1;
        while (true) {
            indexToShift = (indexToShift - 1) & capacityMask;
            long keyToShift;
            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                break;
            }
            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                keys[indexToRemove] = keyToShift;
                vals[indexToRemove] = vals[indexToShift];
                indexToRemove = indexToShift;
                shiftDistance = 1;
            } else {
                shiftDistance++;
                if (indexToShift == 1 + index) {
                    throw new java.util.ConcurrentModificationException();
                }
            }
        }
        keys[indexToRemove] = FREE_BITS;
        postRemoveHook();
        return val;
    }


    @Override
    public boolean justRemove(double key) {
        long k = Double.doubleToLongBits(key);
        long[] keys = set;
        int capacityMask = keys.length - 1;
        int index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(k) & capacityMask]) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return false;
            } else {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return false;
                    }
                }
            }
        }
        // key is present
        short[] vals = values;
        incrementModCount();
        int indexToRemove = index;
        int indexToShift = indexToRemove;
        int shiftDistance = 1;
        while (true) {
            indexToShift = (indexToShift - 1) & capacityMask;
            long keyToShift;
            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                break;
            }
            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                keys[indexToRemove] = keyToShift;
                vals[indexToRemove] = vals[indexToShift];
                indexToRemove = indexToShift;
                shiftDistance = 1;
            } else {
                shiftDistance++;
                if (indexToShift == 1 + index) {
                    throw new java.util.ConcurrentModificationException();
                }
            }
        }
        keys[indexToRemove] = FREE_BITS;
        postRemoveHook();
        return true;
    }

    @Override
    public boolean justRemove(long key) {
        long[] keys = set;
        int capacityMask = keys.length - 1;
        int index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(key) & capacityMask]) != key) {
            if (cur == FREE_BITS) {
                // key is absent
                return false;
            } else {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == key) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return false;
                    }
                }
            }
        }
        // key is present
        short[] vals = values;
        incrementModCount();
        int indexToRemove = index;
        int indexToShift = indexToRemove;
        int shiftDistance = 1;
        while (true) {
            indexToShift = (indexToShift - 1) & capacityMask;
            long keyToShift;
            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                break;
            }
            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                keys[indexToRemove] = keyToShift;
                vals[indexToRemove] = vals[indexToShift];
                indexToRemove = indexToShift;
                shiftDistance = 1;
            } else {
                shiftDistance++;
                if (indexToShift == 1 + index) {
                    throw new java.util.ConcurrentModificationException();
                }
            }
        }
        keys[indexToRemove] = FREE_BITS;
        postRemoveHook();
        return true;
    }


    

    @Override
    public short remove(double key) {
        long k = Double.doubleToLongBits(key);
        long[] keys = set;
        int capacityMask = keys.length - 1;
        int index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(k) & capacityMask]) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return defaultValue();
            } else {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return defaultValue();
                    }
                }
            }
        }
        // key is present
        short[] vals = values;
        short val = vals[index];
        incrementModCount();
        int indexToRemove = index;
        int indexToShift = indexToRemove;
        int shiftDistance = 1;
        while (true) {
            indexToShift = (indexToShift - 1) & capacityMask;
            long keyToShift;
            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                break;
            }
            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                keys[indexToRemove] = keyToShift;
                vals[indexToRemove] = vals[indexToShift];
                indexToRemove = indexToShift;
                shiftDistance = 1;
            } else {
                shiftDistance++;
                if (indexToShift == 1 + index) {
                    throw new java.util.ConcurrentModificationException();
                }
            }
        }
        keys[indexToRemove] = FREE_BITS;
        postRemoveHook();
        return val;
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Double) key).doubleValue(),
                ((Short) value).shortValue()
                );
    }

    @Override
    public boolean remove(double key, short value) {
        long k = Double.doubleToLongBits(key);
        long[] keys = set;
        int capacityMask = keys.length - 1;
        int index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(k) & capacityMask]) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return false;
            } else {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return false;
                    }
                }
            }
        }
        // key is present
        short[] vals = values;
        if (vals[index] == value) {
            incrementModCount();
            int indexToRemove = index;
            int indexToShift = indexToRemove;
            int shiftDistance = 1;
            while (true) {
                indexToShift = (indexToShift - 1) & capacityMask;
                long keyToShift;
                if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                    break;
                }
                if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                    keys[indexToRemove] = keyToShift;
                    vals[indexToRemove] = vals[indexToShift];
                    indexToRemove = indexToShift;
                    shiftDistance = 1;
                } else {
                    shiftDistance++;
                    if (indexToShift == 1 + index) {
                        throw new java.util.ConcurrentModificationException();
                    }
                }
            }
            keys[indexToRemove] = FREE_BITS;
            postRemoveHook();
            return true;
        } else {
            return false;
        }
    }


    @Override
    public boolean removeIf(DoubleShortPredicate filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] keys = set;
        int capacityMask = keys.length - 1;
        int firstDelayedRemoved = -1;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                if (filter.test(Double.longBitsToDouble(key), vals[i])) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    keys[indexToRemove] = REMOVED_BITS;
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                keys[indexToRemove] = keyToShift;
                                vals[indexToRemove] = vals[indexToShift];
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        keys[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        keys[i] = REMOVED_BITS;
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




    // under this condition - operations, overridden from MutableSeparateKVDoubleLHashGO
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
        long[] keys = set;
        int capacityMask = keys.length - 1;
        int firstDelayedRemoved = -1;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                if (filter.test(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    keys[indexToRemove] = REMOVED_BITS;
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                keys[indexToRemove] = keyToShift;
                                vals[indexToRemove] = vals[indexToShift];
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        keys[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        keys[i] = REMOVED_BITS;
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
        long[] keys = set;
        int capacityMask = keys.length - 1;
        int firstDelayedRemoved = -1;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                if (filter.test(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    keys[indexToRemove] = REMOVED_BITS;
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                keys[indexToRemove] = keyToShift;
                                vals[indexToRemove] = vals[indexToShift];
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        keys[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        keys[i] = REMOVED_BITS;
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
        long[] keys = set;
        int capacityMask = keys.length - 1;
        int firstDelayedRemoved = -1;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                if (c.contains(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    keys[indexToRemove] = REMOVED_BITS;
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                keys[indexToRemove] = keyToShift;
                                vals[indexToRemove] = vals[indexToShift];
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        keys[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        keys[i] = REMOVED_BITS;
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
        long[] keys = set;
        int capacityMask = keys.length - 1;
        int firstDelayedRemoved = -1;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                if (c.contains(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    keys[indexToRemove] = REMOVED_BITS;
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                keys[indexToRemove] = keyToShift;
                                vals[indexToRemove] = vals[indexToShift];
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        keys[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        keys[i] = REMOVED_BITS;
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
        long[] keys = set;
        int capacityMask = keys.length - 1;
        int firstDelayedRemoved = -1;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                if (c.contains(key)) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    keys[indexToRemove] = REMOVED_BITS;
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                keys[indexToRemove] = keyToShift;
                                vals[indexToRemove] = vals[indexToShift];
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        keys[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        keys[i] = REMOVED_BITS;
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
        long[] keys = set;
        int capacityMask = keys.length - 1;
        int firstDelayedRemoved = -1;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                if (!c.contains(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    keys[indexToRemove] = REMOVED_BITS;
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                keys[indexToRemove] = keyToShift;
                                vals[indexToRemove] = vals[indexToShift];
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        keys[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        keys[i] = REMOVED_BITS;
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
        long[] keys = set;
        int capacityMask = keys.length - 1;
        int firstDelayedRemoved = -1;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                if (!c.contains(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    keys[indexToRemove] = REMOVED_BITS;
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                keys[indexToRemove] = keyToShift;
                                vals[indexToRemove] = vals[indexToShift];
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        keys[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        keys[i] = REMOVED_BITS;
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
        long[] keys = set;
        int capacityMask = keys.length - 1;
        int firstDelayedRemoved = -1;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                if (!c.contains(key)) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    keys[indexToRemove] = REMOVED_BITS;
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                keys[indexToRemove] = keyToShift;
                                vals[indexToRemove] = vals[indexToShift];
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        keys[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        keys[i] = REMOVED_BITS;
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
        long[] keys = set;
        short[] vals = values;
        int capacityMask = keys.length - 1;
        for (int i = firstDelayedRemoved; i >= 0; i--) {
            if (keys[i] == REMOVED_BITS) {
                int indexToRemove = i;
                int indexToShift = indexToRemove;
                int shiftDistance = 1;
                while (true) {
                    indexToShift = (indexToShift - 1) & capacityMask;
                    long keyToShift;
                    if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                        break;
                    }
                    if ((keyToShift != REMOVED_BITS) && (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance)) {
                        keys[indexToRemove] = keyToShift;
                        vals[indexToRemove] = vals[indexToShift];
                        indexToRemove = indexToShift;
                        shiftDistance = 1;
                    } else {
                        shiftDistance++;
                        if (indexToShift == 1 + i) {
                            throw new java.util.ConcurrentModificationException();
                        }
                    }
                }
                keys[indexToRemove] = FREE_BITS;
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
         short[] vals;

        private NoRemovedKeyIterator(int mc) {
            super(mc);
            vals = values;
        }

        @Override
        public void remove() {
            int index;
            if ((index = this.index) >= 0) {
                if (expectedModCount++ == modCount()) {
                    this.index = -1;
                    long[] keys = this.keys;
                    short[] vals = this.vals;
                    if (keys == set) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.keys == keys) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 1) > 0) {
                                            this.keys = Arrays.copyOf(keys, slotsToCopy);
                                            this.vals = Arrays.copyOf(vals, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.keys[indexToRemove] = FREE_BITS;
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.nextIndex = index;
                                        if (indexToShift < index - 1) {
                                            this.next = Double.longBitsToDouble(keyToShift);
                                        }
                                    }
                                }
                                keys[indexToRemove] = keyToShift;
                                vals[indexToRemove] = vals[indexToShift];
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        keys[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        justRemove(keys[index]);
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
         short[] vals;

        private NoRemovedKeyCursor(int mc) {
            super(mc);
            vals = values;
        }

        @Override
        public void remove() {
            long curKey;
            if ((curKey = this.curKey) != FREE_BITS) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE_BITS;
                    int index = this.index;
                    long[] keys = this.keys;
                    short[] vals = this.vals;
                    if (keys == set) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.keys == keys) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.keys = Arrays.copyOf(keys, slotsToCopy);
                                            this.vals = Arrays.copyOf(vals, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.keys[indexToRemove] = FREE_BITS;
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.index = ++index;
                                    }
                                }
                                keys[indexToRemove] = keyToShift;
                                vals[indexToRemove] = vals[indexToShift];
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        keys[indexToRemove] = FREE_BITS;
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





    class EntryView extends AbstractSetView<Map.Entry<Double, Short>>
            implements HashObjSet<Map.Entry<Double, Short>>,
            InternalObjCollectionOps<Map.Entry<Double, Short>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Double, Short>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Double>defaultEquality()
                    ,
                    Equivalence.<Short>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return MutableLHashSeparateKVDoubleShortMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return MutableLHashSeparateKVDoubleShortMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return MutableLHashSeparateKVDoubleShortMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Double, Short> e = (Map.Entry<Double, Short>) o;
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
            long[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
                    result[resultIndex++] = new MutableEntry(mc, i, key, vals[i]);
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
            long[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
                    a[resultIndex++] = (T) new MutableEntry(mc, i, key, vals[i]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }

        @Override
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Double, Short>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            long[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
                    action.accept(new MutableEntry(mc, i, key, vals[i]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Double, Short>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            long[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
                    if (!predicate.test(new MutableEntry(mc, i, key, vals[i]))) {
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
        public ObjIterator<Map.Entry<Double, Short>> iterator() {
            int mc = modCount();
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Double, Short>> cursor() {
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
            long[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
                    if (!c.contains(e.with(key, vals[i]))) {
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
            long[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
                    changed |= s.remove(e.with(key, vals[i]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Double, Short>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
                    changed |= c.add(new MutableEntry(mc, i, key, vals[i]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        public int hashCode() {
            return MutableLHashSeparateKVDoubleShortMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            long[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
                    sb.append(' ');
                    sb.append(Double.longBitsToDouble(key));
                    sb.append('=');
                    sb.append(vals[i]);
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
            return MutableLHashSeparateKVDoubleShortMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Double, Short> e = (Map.Entry<Double, Short>) o;
                double key = e.getKey();
                short value = e.getValue();
                return MutableLHashSeparateKVDoubleShortMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Double, Short>> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
                    if (filter.test(new MutableEntry(mc, i, key, vals[i]))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                long keyToShift;
                                if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                    break;
                                }
                                if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        keys[indexToRemove] = REMOVED_BITS;
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i++;
                                    }
                                    keys[indexToRemove] = keyToShift;
                                    vals[indexToRemove] = vals[indexToShift];
                                    indexToRemove = indexToShift;
                                    shiftDistance = 1;
                                } else {
                                    shiftDistance++;
                                    if (indexToShift == 1 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            keys[indexToRemove] = FREE_BITS;
                            postRemoveHook();
                        } else {
                            keys[i] = REMOVED_BITS;
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
            long[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
                    if (c.contains(e.with(key, vals[i]))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                long keyToShift;
                                if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                    break;
                                }
                                if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        keys[indexToRemove] = REMOVED_BITS;
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i++;
                                    }
                                    keys[indexToRemove] = keyToShift;
                                    vals[indexToRemove] = vals[indexToShift];
                                    indexToRemove = indexToShift;
                                    shiftDistance = 1;
                                } else {
                                    shiftDistance++;
                                    if (indexToShift == 1 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            keys[indexToRemove] = FREE_BITS;
                            postRemoveHook();
                        } else {
                            keys[i] = REMOVED_BITS;
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
            long[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
                    if (!c.contains(e.with(key, vals[i]))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                long keyToShift;
                                if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                    break;
                                }
                                if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        keys[indexToRemove] = REMOVED_BITS;
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i++;
                                    }
                                    keys[indexToRemove] = keyToShift;
                                    vals[indexToRemove] = vals[indexToShift];
                                    indexToRemove = indexToShift;
                                    shiftDistance = 1;
                                } else {
                                    shiftDistance++;
                                    if (indexToShift == 1 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            keys[indexToRemove] = FREE_BITS;
                            postRemoveHook();
                        } else {
                            keys[i] = REMOVED_BITS;
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
            MutableLHashSeparateKVDoubleShortMapGO.this.clear();
        }
    }


    abstract class DoubleShortEntry extends AbstractEntry<Double, Short> {

        abstract long key();

        @Override
        public final Double getKey() {
            return Double.longBitsToDouble(key());
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
            long k2;
            short v2;
            try {
                e2 = (Map.Entry) o;
                k2 = Double.doubleToLongBits((Double) e2.getKey());
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


    class MutableEntry extends DoubleShortEntry {
        final int modCount;
        private final int index;
        final long key;
        private short value;

        MutableEntry(int modCount, int index, long key, short value) {
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
        public short value() {
            return value;
        }

        @Override
        public Short setValue(Short newValue) {
            if (modCount != modCount())
                throw new IllegalStateException();
            short oldValue = value;
            short unwrappedNewValue = newValue;
            value = unwrappedNewValue;
            updateValueInTable(unwrappedNewValue);
            return oldValue;
        }

        void updateValueInTable(short newValue) {
            values[index] = newValue;
        }
    }



    class ReusableEntry extends DoubleShortEntry {
        private long key;
        private short value;

        ReusableEntry with(long key, short value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public long key() {
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
            return MutableLHashSeparateKVDoubleShortMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return MutableLHashSeparateKVDoubleShortMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return MutableLHashSeparateKVDoubleShortMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(short v) {
            return MutableLHashSeparateKVDoubleShortMapGO.this.containsValue(v);
        }



        @Override
        public void forEach(Consumer<? super Short> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            long[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    action.accept(vals[i]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public void forEach(ShortConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            long[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    action.accept(vals[i]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(ShortPredicate predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            long[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    if (!predicate.test(vals[i])) {
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
        public boolean allContainingIn(ShortCollection c) {
            if (isEmpty())
                return true;
            boolean containsAll = true;
            int mc = modCount();
            long[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    if (!c.contains(vals[i])) {
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
        public boolean reverseAddAllTo(ShortCollection c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    changed |= c.add(vals[i]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        @Override
        public boolean reverseRemoveAllFrom(ShortSet s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    changed |= s.removeShort(vals[i]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }



        @Override
        @Nonnull
        public ShortIterator iterator() {
            int mc = modCount();
            return new NoRemovedValueIterator(mc);
        }

        @Nonnull
        @Override
        public ShortCursor cursor() {
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
            long[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    result[resultIndex++] = vals[i];
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
            long[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    a[resultIndex++] = (T) Short.valueOf(vals[i]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
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
            int mc = modCount();
            long[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    result[resultIndex++] = vals[i];
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
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
            int mc = modCount();
            long[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    a[resultIndex++] = vals[i];
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
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
            int mc = modCount();
            long[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    sb.append(' ').append(vals[i]).append(',');
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
            return removeShort(( Short ) o);
        }

        @Override
        public boolean removeShort(short v) {
            return removeValue(v);
        }



        @Override
        public void clear() {
            MutableLHashSeparateKVDoubleShortMapGO.this.clear();
        }

        
        public boolean removeIf(Predicate<? super Short> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    if (filter.test(vals[i])) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                long keyToShift;
                                if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                    break;
                                }
                                if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        keys[indexToRemove] = REMOVED_BITS;
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i++;
                                    }
                                    keys[indexToRemove] = keyToShift;
                                    vals[indexToRemove] = vals[indexToShift];
                                    indexToRemove = indexToShift;
                                    shiftDistance = 1;
                                } else {
                                    shiftDistance++;
                                    if (indexToShift == 1 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            keys[indexToRemove] = FREE_BITS;
                            postRemoveHook();
                        } else {
                            keys[i] = REMOVED_BITS;
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
        public boolean removeIf(ShortPredicate filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    if (filter.test(vals[i])) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                long keyToShift;
                                if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                    break;
                                }
                                if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        keys[indexToRemove] = REMOVED_BITS;
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i++;
                                    }
                                    keys[indexToRemove] = keyToShift;
                                    vals[indexToRemove] = vals[indexToShift];
                                    indexToRemove = indexToShift;
                                    shiftDistance = 1;
                                } else {
                                    shiftDistance++;
                                    if (indexToShift == 1 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            keys[indexToRemove] = FREE_BITS;
                            postRemoveHook();
                        } else {
                            keys[i] = REMOVED_BITS;
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
            if (c instanceof ShortCollection)
                return removeAll((ShortCollection) c);
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    if (c.contains(vals[i])) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                long keyToShift;
                                if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                    break;
                                }
                                if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        keys[indexToRemove] = REMOVED_BITS;
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i++;
                                    }
                                    keys[indexToRemove] = keyToShift;
                                    vals[indexToRemove] = vals[indexToShift];
                                    indexToRemove = indexToShift;
                                    shiftDistance = 1;
                                } else {
                                    shiftDistance++;
                                    if (indexToShift == 1 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            keys[indexToRemove] = FREE_BITS;
                            postRemoveHook();
                        } else {
                            keys[i] = REMOVED_BITS;
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

        private boolean removeAll(ShortCollection c) {
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    if (c.contains(vals[i])) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                long keyToShift;
                                if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                    break;
                                }
                                if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        keys[indexToRemove] = REMOVED_BITS;
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i++;
                                    }
                                    keys[indexToRemove] = keyToShift;
                                    vals[indexToRemove] = vals[indexToShift];
                                    indexToRemove = indexToShift;
                                    shiftDistance = 1;
                                } else {
                                    shiftDistance++;
                                    if (indexToShift == 1 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            keys[indexToRemove] = FREE_BITS;
                            postRemoveHook();
                        } else {
                            keys[i] = REMOVED_BITS;
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
            if (c instanceof ShortCollection)
                return retainAll((ShortCollection) c);
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
            long[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    if (!c.contains(vals[i])) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                long keyToShift;
                                if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                    break;
                                }
                                if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        keys[indexToRemove] = REMOVED_BITS;
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i++;
                                    }
                                    keys[indexToRemove] = keyToShift;
                                    vals[indexToRemove] = vals[indexToShift];
                                    indexToRemove = indexToShift;
                                    shiftDistance = 1;
                                } else {
                                    shiftDistance++;
                                    if (indexToShift == 1 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            keys[indexToRemove] = FREE_BITS;
                            postRemoveHook();
                        } else {
                            keys[i] = REMOVED_BITS;
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

        private boolean retainAll(ShortCollection c) {
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
            long[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    if (!c.contains(vals[i])) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                long keyToShift;
                                if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                    break;
                                }
                                if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        keys[indexToRemove] = REMOVED_BITS;
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i++;
                                    }
                                    keys[indexToRemove] = keyToShift;
                                    vals[indexToRemove] = vals[indexToShift];
                                    indexToRemove = indexToShift;
                                    shiftDistance = 1;
                                } else {
                                    shiftDistance++;
                                    if (indexToShift == 1 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            keys[indexToRemove] = FREE_BITS;
                            postRemoveHook();
                        } else {
                            keys[i] = REMOVED_BITS;
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Double, Short>> {
        long[] keys;
        short[] vals;
        final int capacityMask;
        int expectedModCount;
        
        class MutableEntry2 extends MutableEntry {
            MutableEntry2(int modCount, int index, long key, short value) {
                super(modCount, index, key, value);
            }
            
            @Override
            void updateValueInTable(short newValue) {
                if (vals == values) {
                    vals[index] = newValue;
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
            long[] keys = this.keys = set;
            capacityMask = keys.length - 1;
            short[] vals = this.vals = values;
            int nextI = keys.length;
            while (--nextI >= 0) {
                long key;
                if ((key = keys[nextI]) < FREE_BITS) {
                    next = new MutableEntry2(mc, nextI, key, vals[nextI]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Double, Short>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            short[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
                    action.accept(new MutableEntry2(mc, i, key, vals[i]));
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
        public Map.Entry<Double, Short> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
                    index = nextI;
                    long[] keys = this.keys;
                    MutableEntry prev = next;
                    while (--nextI >= 0) {
                        long key;
                        if ((key = keys[nextI]) < FREE_BITS) {
                            next = new MutableEntry2(mc, nextI, key, vals[nextI]);
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
                    long[] keys = this.keys;
                    short[] vals = this.vals;
                    if (keys == set) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.keys == keys) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 1) > 0) {
                                            this.keys = Arrays.copyOf(keys, slotsToCopy);
                                            this.vals = Arrays.copyOf(vals, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.keys[indexToRemove] = FREE_BITS;
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.nextIndex = index;
                                        if (indexToShift < index - 1) {
                                            this.next = new MutableEntry2(modCount(), indexToShift, keyToShift, vals[indexToShift]);
                                        }
                                    }
                                }
                                keys[indexToRemove] = keyToShift;
                                vals[indexToRemove] = vals[indexToShift];
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        keys[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        justRemove(keys[index]);
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Double, Short>> {
        long[] keys;
        short[] vals;
        final int capacityMask;
        int expectedModCount;
        
        class MutableEntry2 extends MutableEntry {
            MutableEntry2(int modCount, int index, long key, short value) {
                super(modCount, index, key, value);
            }
            
            @Override
            void updateValueInTable(short newValue) {
                if (vals == values) {
                    vals[index] = newValue;
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
        short curValue;

        NoRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            long[] keys = this.keys = set;
            capacityMask = keys.length - 1;
            index = keys.length;
            vals = values;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Double, Short>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            short[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
                    action.accept(new MutableEntry2(mc, i, key, vals[i]));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE_BITS;
        }

        @Override
        public Map.Entry<Double, Short> elem() {
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
                long[] keys = this.keys;
                for (int i = index - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) < FREE_BITS) {
                        index = i;
                        curKey = key;
                        curValue = vals[i];
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
                    long[] keys = this.keys;
                    short[] vals = this.vals;
                    if (keys == set) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.keys == keys) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.keys = Arrays.copyOf(keys, slotsToCopy);
                                            this.vals = Arrays.copyOf(vals, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.keys[indexToRemove] = FREE_BITS;
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.index = ++index;
                                    }
                                }
                                keys[indexToRemove] = keyToShift;
                                vals[indexToRemove] = vals[indexToShift];
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        keys[indexToRemove] = FREE_BITS;
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




    class NoRemovedValueIterator implements ShortIterator {
        long[] keys;
        short[] vals;
        final int capacityMask;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        short next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            long[] keys = this.keys = set;
            capacityMask = keys.length - 1;
            short[] vals = this.vals = values;
            int nextI = keys.length;
            while (--nextI >= 0) {
                if (keys[nextI] < FREE_BITS) {
                    next = vals[nextI];
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public short nextShort() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                if (expectedModCount == modCount()) {
                    index = nextI;
                    long[] keys = this.keys;
                    short prev = next;
                    while (--nextI >= 0) {
                        if (keys[nextI] < FREE_BITS) {
                            next = vals[nextI];
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
        public void forEachRemaining(Consumer<? super Short> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            short[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    action.accept(vals[i]);
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            index = nextIndex = -1;
        }

        @Override
        public void forEachRemaining(ShortConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            short[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    action.accept(vals[i]);
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
        public Short next() {
            return nextShort();
        }

        @Override
        public void remove() {
            int index;
            if ((index = this.index) >= 0) {
                if (expectedModCount++ == modCount()) {
                    this.index = -1;
                    long[] keys = this.keys;
                    short[] vals = this.vals;
                    if (keys == set) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.keys == keys) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 1) > 0) {
                                            this.keys = Arrays.copyOf(keys, slotsToCopy);
                                            this.vals = Arrays.copyOf(vals, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.keys[indexToRemove] = FREE_BITS;
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.nextIndex = index;
                                        if (indexToShift < index - 1) {
                                            this.next = vals[indexToShift];
                                        }
                                    }
                                }
                                keys[indexToRemove] = keyToShift;
                                vals[indexToRemove] = vals[indexToShift];
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        keys[indexToRemove] = FREE_BITS;
                        postRemoveHook();
                    } else {
                        justRemove(keys[index]);
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedValueCursor implements ShortCursor {
        long[] keys;
        short[] vals;
        final int capacityMask;
        int expectedModCount;
        int index;
        long curKey;
        short curValue;

        NoRemovedValueCursor(int mc) {
            expectedModCount = mc;
            long[] keys = this.keys = set;
            capacityMask = keys.length - 1;
            index = keys.length;
            vals = values;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(ShortConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            short[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    action.accept(vals[i]);
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE_BITS;
        }

        @Override
        public short elem() {
            if (curKey != FREE_BITS) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                long[] keys = this.keys;
                for (int i = index - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) < FREE_BITS) {
                        index = i;
                        curKey = key;
                        curValue = vals[i];
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
                    long[] keys = this.keys;
                    short[] vals = this.vals;
                    if (keys == set) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.keys == keys) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.keys = Arrays.copyOf(keys, slotsToCopy);
                                            this.vals = Arrays.copyOf(vals, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.keys[indexToRemove] = FREE_BITS;
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.index = ++index;
                                    }
                                }
                                keys[indexToRemove] = keyToShift;
                                vals[indexToRemove] = vals[indexToShift];
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        keys[indexToRemove] = FREE_BITS;
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



    class NoRemovedMapCursor implements DoubleShortCursor {
        long[] keys;
        short[] vals;
        final int capacityMask;
        int expectedModCount;
        int index;
        long curKey;
        short curValue;

        NoRemovedMapCursor(int mc) {
            expectedModCount = mc;
            long[] keys = this.keys = set;
            capacityMask = keys.length - 1;
            index = keys.length;
            vals = values;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(DoubleShortConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            short[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
                    action.accept(Double.longBitsToDouble(key), vals[i]);
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
        public short value() {
            if (curKey != FREE_BITS) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(short value) {
            if (curKey != FREE_BITS) {
                if (expectedModCount == modCount()) {
                    vals[index] = value;
                    if (vals != values) {
                        values[index] = value;
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
                long[] keys = this.keys;
                for (int i = index - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) < FREE_BITS) {
                        index = i;
                        curKey = key;
                        curValue = vals[i];
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
                    long[] keys = this.keys;
                    short[] vals = this.vals;
                    if (keys == set) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            long keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE_BITS) {
                                break;
                            }
                            if (((SeparateKVDoubleKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.keys == keys) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.keys = Arrays.copyOf(keys, slotsToCopy);
                                            this.vals = Arrays.copyOf(vals, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.keys[indexToRemove] = FREE_BITS;
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.index = ++index;
                                    }
                                }
                                keys[indexToRemove] = keyToShift;
                                vals[indexToRemove] = vals[indexToShift];
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        keys[indexToRemove] = FREE_BITS;
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

