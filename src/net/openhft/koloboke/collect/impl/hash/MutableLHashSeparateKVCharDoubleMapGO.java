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
import net.openhft.koloboke.function.CharPredicate;
import net.openhft.koloboke.function.CharDoubleConsumer;
import net.openhft.koloboke.function.CharDoublePredicate;
import net.openhft.koloboke.function.CharDoubleToDoubleFunction;
import net.openhft.koloboke.function.CharToDoubleFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class MutableLHashSeparateKVCharDoubleMapGO
        extends MutableLHashSeparateKVCharDoubleMapSO {

    @Override
    final void copy(SeparateKVCharDoubleLHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVCharDoubleLHash hash) {
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
    public boolean containsEntry(char key, double value) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return values[index] == Double.doubleToLongBits(value);
        } else {
            // key is absent
            return false;
        }
    }

    @Override
    public boolean containsEntry(char key, long value) {
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
    public Double get(Object key) {
        int index = index((Character) key);
        if (index >= 0) {
            // key is present
            return Double.longBitsToDouble(values[index]);
        } else {
            // key is absent
            return null;
        }
    }

    

    @Override
    public double get(char key) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return Double.longBitsToDouble(values[index]);
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Double getOrDefault(Object key, Double defaultValue) {
        int index = index((Character) key);
        if (index >= 0) {
            // key is present
            return Double.longBitsToDouble(values[index]);
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public double getOrDefault(char key, double defaultValue) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return Double.longBitsToDouble(values[index]);
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public void forEach(BiConsumer<? super Character, ? super Double> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        char free = freeValue;
        char[] keys = set;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
                action.accept(key, Double.longBitsToDouble(vals[i]));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void forEach(CharDoubleConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        char free = freeValue;
        char[] keys = set;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
                action.accept(key, Double.longBitsToDouble(vals[i]));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public boolean forEachWhile(CharDoublePredicate predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        char free = freeValue;
        char[] keys = set;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
                if (!predicate.test(key, Double.longBitsToDouble(vals[i]))) {
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
    public CharDoubleCursor cursor() {
        int mc = modCount();
        return new NoRemovedMapCursor(mc);
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonCharDoubleMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalCharDoubleMapOps m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        char free = freeValue;
        char[] keys = set;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
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
    public void reversePutAllTo(InternalCharDoubleMapOps m) {
        if (isEmpty())
            return;
        int mc = modCount();
        char free = freeValue;
        char[] keys = set;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
                m.justPut(key, vals[i]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<Character, Double>> entrySet() {
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
        char free = freeValue;
        char[] keys = set;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
            long val;
                hashCode += key ^ ((int) ((val = vals[i]) ^ (val >>> 32)));
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
        char free = freeValue;
        char[] keys = set;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
                sb.append(' ');
                sb.append(key);
                sb.append('=');
                sb.append(Double.longBitsToDouble(vals[i]));
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
        char free = freeValue;
        char[] keys = set;
        long[] vals = values;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        char[] newKeys = set;
        int capacityMask = newKeys.length - 1;
        long[] newVals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
                int index;
                if (newKeys[index = SeparateKVCharKeyMixing.mix(key) & capacityMask] != free) {
                    while (true) {
                        if (newKeys[(index = (index - 1) & capacityMask)] == free) {
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
    public Double put(Character key, Double value) {
        int index = insert(key, Double.doubleToLongBits(value));
        if (index < 0) {
            // key was absent
            return null;
        } else {
            // key is present
            long[] vals = values;
            double prevValue = Double.longBitsToDouble(vals[index]);
            vals[index] = Double.doubleToLongBits(value);
            return prevValue;
        }
    }

    @Override
    public double put(char key, double value) {
        int index = insert(key, Double.doubleToLongBits(value));
        if (index < 0) {
            // key was absent
            return defaultValue();
        } else {
            // key is present
            long[] vals = values;
            double prevValue = Double.longBitsToDouble(vals[index]);
            vals[index] = Double.doubleToLongBits(value);
            return prevValue;
        }
    }

    @Override
    public Double putIfAbsent(Character key, Double value) {
        int index = insert(key, Double.doubleToLongBits(value));
        if (index < 0) {
            // key was absent
            return null;
        } else {
            // key is present
            return Double.longBitsToDouble(values[index]);
        }
    }

    @Override
    public double putIfAbsent(char key, double value) {
        int index = insert(key, Double.doubleToLongBits(value));
        if (index < 0) {
            // key was absent
            return defaultValue();
        } else {
            // key is present
            return Double.longBitsToDouble(values[index]);
        }
    }

    @Override
    public void justPut(char key, double value) {
        int index = insert(key, Double.doubleToLongBits(value));
        if (index < 0) {
            // key was absent
            return;
        } else {
            // key is present
            values[index] = Double.doubleToLongBits(value);
            return;
        }
    }

    @Override
    public void justPut(char key, long value) {
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
    public Double compute(Character key,
            BiFunction<? super Character, ? super Double, ? extends Double> remappingFunction) {
        char k = key;
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        char free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        char[] keys = set;
        long[] vals = values;
        int capacityMask, index;
        char cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVCharKeyMixing.mix(k) & (capacityMask = keys.length - 1)]) != k) {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == k) {
                        break keyPresent;
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            Double newValue = remappingFunction.apply(k, null);
            if (newValue != null) {
                incrementModCount();
                keys[index] = k;
                vals[index] = Double.doubleToLongBits(newValue);
                postInsertHook();
                return newValue;
            } else {
                return null;
            }
        }
        // key is present
        Double newValue = remappingFunction.apply(k, Double.longBitsToDouble(vals[index]));
        if (newValue != null) {
            vals[index] = Double.doubleToLongBits(newValue);
            return newValue;
        } else {
            removeAt(index);
            return null;
        }
    }


    @Override
    public double compute(char key, CharDoubleToDoubleFunction remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        char free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        char[] keys = set;
        long[] vals = values;
        int capacityMask, index;
        char cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVCharKeyMixing.mix(key) & (capacityMask = keys.length - 1)]) != key) {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == key) {
                        break keyPresent;
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            double newValue = remappingFunction.applyAsDouble(key, defaultValue());
            incrementModCount();
            keys[index] = key;
            vals[index] = Double.doubleToLongBits(newValue);
            postInsertHook();
            return newValue;
        }
        // key is present
        double newValue = remappingFunction.applyAsDouble(key, Double.longBitsToDouble(vals[index]));
        vals[index] = Double.doubleToLongBits(newValue);
        return newValue;
    }


    @Override
    public Double computeIfAbsent(Character key,
            Function<? super Character, ? extends Double> mappingFunction) {
        char k = key;
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        char free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        char[] keys = set;
        long[] vals = values;
        int capacityMask, index;
        char cur;
        if ((cur = keys[index = SeparateKVCharKeyMixing.mix(k) & (capacityMask = keys.length - 1)]) == k) {
            // key is present
            return Double.longBitsToDouble(vals[index]);
        } else {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == k) {
                        // key is present
                        return Double.longBitsToDouble(vals[index]);
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            Double value = mappingFunction.apply(k);
            if (value != null) {
                incrementModCount();
                keys[index] = k;
                vals[index] = Double.doubleToLongBits(value);
                postInsertHook();
                return value;
            } else {
                return null;
            }
        }
    }


    @Override
    public double computeIfAbsent(char key, CharToDoubleFunction mappingFunction) {
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        char free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        char[] keys = set;
        long[] vals = values;
        int capacityMask, index;
        char cur;
        if ((cur = keys[index = SeparateKVCharKeyMixing.mix(key) & (capacityMask = keys.length - 1)]) == key) {
            // key is present
            return Double.longBitsToDouble(vals[index]);
        } else {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == key) {
                        // key is present
                        return Double.longBitsToDouble(vals[index]);
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            double value = mappingFunction.applyAsDouble(key);
            incrementModCount();
            keys[index] = key;
            vals[index] = Double.doubleToLongBits(value);
            postInsertHook();
            return value;
        }
    }


    @Override
    public Double computeIfPresent(Character key,
            BiFunction<? super Character, ? super Double, ? extends Double> remappingFunction) {
        char k = key;
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(k);
        if (index >= 0) {
            // key is present
            long[] vals = values;
            Double newValue = remappingFunction.apply(k, Double.longBitsToDouble(vals[index]));
            if (newValue != null) {
                vals[index] = Double.doubleToLongBits(newValue);
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
    public double computeIfPresent(char key, CharDoubleToDoubleFunction remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(key);
        if (index >= 0) {
            // key is present
            long[] vals = values;
            double newValue = remappingFunction.applyAsDouble(key, Double.longBitsToDouble(vals[index]));
            vals[index] = Double.doubleToLongBits(newValue);
            return newValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Double merge(Character key, Double value,
            BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        char k = key;
        if (value == null)
            throw new java.lang.NullPointerException();
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        char free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        char[] keys = set;
        long[] vals = values;
        int capacityMask, index;
        char cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVCharKeyMixing.mix(k) & (capacityMask = keys.length - 1)]) != k) {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == k) {
                        break keyPresent;
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            incrementModCount();
            keys[index] = k;
            vals[index] = Double.doubleToLongBits(value);
            postInsertHook();
            return value;
        }
        // key is present
        Double newValue = remappingFunction.apply(Double.longBitsToDouble(vals[index]), value);
        if (newValue != null) {
            vals[index] = Double.doubleToLongBits(newValue);
            return newValue;
        } else {
            removeAt(index);
            return null;
        }
    }


    @Override
    public double merge(char key, double value, DoubleBinaryOperator remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        char free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        char[] keys = set;
        long[] vals = values;
        int capacityMask, index;
        char cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVCharKeyMixing.mix(key) & (capacityMask = keys.length - 1)]) != key) {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == key) {
                        break keyPresent;
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            incrementModCount();
            keys[index] = key;
            vals[index] = Double.doubleToLongBits(value);
            postInsertHook();
            return value;
        }
        // key is present
        double newValue = remappingFunction.applyAsDouble(Double.longBitsToDouble(vals[index]), value);
        vals[index] = Double.doubleToLongBits(newValue);
        return newValue;
    }


    @Override
    public double addValue(char key, double value) {
        int index = insert(key, Double.doubleToLongBits(value));
        if (index < 0) {
            // key was absent
            return value;
        } else {
            // key is present
            long[] vals = values;
            double newValue = Double.longBitsToDouble(vals[index]) + value;
            vals[index] = Double.doubleToLongBits(newValue);
            return newValue;
        }
    }

    @Override
    public double addValue(char key, double addition, double defaultValue) {
        double value = defaultValue + addition;
        int index = insert(key, Double.doubleToLongBits(value));
        if (index < 0) {
            // key was absent
            return value;
        } else {
            // key is present
            long[] vals = values;
            double newValue = Double.longBitsToDouble(vals[index]) + addition;
            vals[index] = Double.doubleToLongBits(newValue);
            return newValue;
        }
    }


    @Override
    public void putAll(@Nonnull Map<? extends Character, ? extends Double> m) {
        CommonCharDoubleMapOps.putAll(this, m);
    }


    @Override
    public Double replace(Character key, Double value) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            long[] vals = values;
            double oldValue = Double.longBitsToDouble(vals[index]);
            vals[index] = Double.doubleToLongBits(value);
            return oldValue;
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public double replace(char key, double value) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            long[] vals = values;
            double oldValue = Double.longBitsToDouble(vals[index]);
            vals[index] = Double.doubleToLongBits(value);
            return oldValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public boolean replace(Character key, Double oldValue, Double newValue) {
        return replace(key.charValue(),
                oldValue.doubleValue(),
                newValue.doubleValue());
    }

    @Override
    public boolean replace(char key, double oldValue, double newValue) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            long[] vals = values;
            if (vals[index] == Double.doubleToLongBits(oldValue)) {
                vals[index] = Double.doubleToLongBits(newValue);
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
            BiFunction<? super Character, ? super Double, ? extends Double> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        char free = freeValue;
        char[] keys = set;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
                vals[i] = Double.doubleToLongBits(function.apply(key, Double.longBitsToDouble(vals[i])));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void replaceAll(CharDoubleToDoubleFunction function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        char free = freeValue;
        char[] keys = set;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
                vals[i] = Double.doubleToLongBits(function.applyAsDouble(key, Double.longBitsToDouble(vals[i])));
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
        char free = freeValue;
        char[] keys = set;
        long[] vals = values;
        int capacityMask = keys.length - 1;
        incrementModCount();
        int indexToRemove = index;
        int indexToShift = indexToRemove;
        int shiftDistance = 1;
        while (true) {
            indexToShift = (indexToShift - 1) & capacityMask;
            char keyToShift;
            if ((keyToShift = keys[indexToShift]) == free) {
                break;
            }
            if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
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
        keys[indexToRemove] = free;
        postRemoveHook();
    }

    @Override
    public Double remove(Object key) {
        char k = (Character) key;
        char free;
        if (k != (free = freeValue)) {
            char[] keys = set;
            int capacityMask = keys.length - 1;
            int index;
            char cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVCharKeyMixing.mix(k) & capacityMask]) != k) {
                if (cur == free) {
                    // key is absent
                    return null;
                } else {
                    while (true) {
                        if ((cur = keys[(index = (index - 1) & capacityMask)]) == k) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return null;
                        }
                    }
                }
            }
            // key is present
            long[] vals = values;
            double val = Double.longBitsToDouble(vals[index]);
            incrementModCount();
            int indexToRemove = index;
            int indexToShift = indexToRemove;
            int shiftDistance = 1;
            while (true) {
                indexToShift = (indexToShift - 1) & capacityMask;
                char keyToShift;
                if ((keyToShift = keys[indexToShift]) == free) {
                    break;
                }
                if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
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
            keys[indexToRemove] = free;
            postRemoveHook();
            return val;
        } else {
            // key is absent
            return null;
        }
    }


    @Override
    public boolean justRemove(char key) {
        char free;
        if (key != (free = freeValue)) {
            char[] keys = set;
            int capacityMask = keys.length - 1;
            int index;
            char cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVCharKeyMixing.mix(key) & capacityMask]) != key) {
                if (cur == free) {
                    // key is absent
                    return false;
                } else {
                    while (true) {
                        if ((cur = keys[(index = (index - 1) & capacityMask)]) == key) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return false;
                        }
                    }
                }
            }
            // key is present
            long[] vals = values;
            incrementModCount();
            int indexToRemove = index;
            int indexToShift = indexToRemove;
            int shiftDistance = 1;
            while (true) {
                indexToShift = (indexToShift - 1) & capacityMask;
                char keyToShift;
                if ((keyToShift = keys[indexToShift]) == free) {
                    break;
                }
                if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
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
            keys[indexToRemove] = free;
            postRemoveHook();
            return true;
        } else {
            // key is absent
            return false;
        }
    }



    

    @Override
    public double remove(char key) {
        char free;
        if (key != (free = freeValue)) {
            char[] keys = set;
            int capacityMask = keys.length - 1;
            int index;
            char cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVCharKeyMixing.mix(key) & capacityMask]) != key) {
                if (cur == free) {
                    // key is absent
                    return defaultValue();
                } else {
                    while (true) {
                        if ((cur = keys[(index = (index - 1) & capacityMask)]) == key) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue();
                        }
                    }
                }
            }
            // key is present
            long[] vals = values;
            double val = Double.longBitsToDouble(vals[index]);
            incrementModCount();
            int indexToRemove = index;
            int indexToShift = indexToRemove;
            int shiftDistance = 1;
            while (true) {
                indexToShift = (indexToShift - 1) & capacityMask;
                char keyToShift;
                if ((keyToShift = keys[indexToShift]) == free) {
                    break;
                }
                if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
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
            keys[indexToRemove] = free;
            postRemoveHook();
            return val;
        } else {
            // key is absent
            return defaultValue();
        }
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Character) key).charValue(),
                ((Double) value).doubleValue()
                );
    }

    @Override
    public boolean remove(char key, double value) {
        char free;
        if (key != (free = freeValue)) {
            char[] keys = set;
            int capacityMask = keys.length - 1;
            int index;
            char cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVCharKeyMixing.mix(key) & capacityMask]) != key) {
                if (cur == free) {
                    // key is absent
                    return false;
                } else {
                    while (true) {
                        if ((cur = keys[(index = (index - 1) & capacityMask)]) == key) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return false;
                        }
                    }
                }
            }
            // key is present
            long[] vals = values;
            if (vals[index] == Double.doubleToLongBits(value)) {
                incrementModCount();
                int indexToRemove = index;
                int indexToShift = indexToRemove;
                int shiftDistance = 1;
                while (true) {
                    indexToShift = (indexToShift - 1) & capacityMask;
                    char keyToShift;
                    if ((keyToShift = keys[indexToShift]) == free) {
                        break;
                    }
                    if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
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
                keys[indexToRemove] = free;
                postRemoveHook();
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
    public boolean removeIf(CharDoublePredicate filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        char free = freeValue;
        char[] keys = set;
        int capacityMask = keys.length - 1;
        int firstDelayedRemoved = -1;
        char delayedRemoved = (char) 0;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
                if (filter.test(key, Double.longBitsToDouble(vals[i]))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            char keyToShift;
                            if ((keyToShift = keys[indexToShift]) == free) {
                                break;
                            }
                            if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    keys[indexToRemove] = key;
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
                        keys[indexToRemove] = free;
                        postRemoveHook();
                    } else {
                        keys[i] = delayedRemoved;
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }




    // under this condition - operations, overridden from MutableSeparateKVCharLHashGO
    // when values are objects - in order to set values to null on removing (for garbage collection)
    // when algo is LHash - because shift deletion should shift values to

    @Override
    public boolean removeIf(Predicate<? super Character> filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        char free = freeValue;
        char[] keys = set;
        int capacityMask = keys.length - 1;
        int firstDelayedRemoved = -1;
        char delayedRemoved = (char) 0;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
                if (filter.test(key)) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            char keyToShift;
                            if ((keyToShift = keys[indexToShift]) == free) {
                                break;
                            }
                            if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    keys[indexToRemove] = key;
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
                        keys[indexToRemove] = free;
                        postRemoveHook();
                    } else {
                        keys[i] = delayedRemoved;
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    @Override
    public boolean removeIf(CharPredicate filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        char free = freeValue;
        char[] keys = set;
        int capacityMask = keys.length - 1;
        int firstDelayedRemoved = -1;
        char delayedRemoved = (char) 0;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
                if (filter.test(key)) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            char keyToShift;
                            if ((keyToShift = keys[indexToShift]) == free) {
                                break;
                            }
                            if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    keys[indexToRemove] = key;
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
                        keys[indexToRemove] = free;
                        postRemoveHook();
                    } else {
                        keys[i] = delayedRemoved;
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    @Override
    public boolean removeAll(@Nonnull HashCharSet thisC, @Nonnull Collection<?> c) {
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        char free = freeValue;
        char[] keys = set;
        int capacityMask = keys.length - 1;
        int firstDelayedRemoved = -1;
        char delayedRemoved = (char) 0;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
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
                            char keyToShift;
                            if ((keyToShift = keys[indexToShift]) == free) {
                                break;
                            }
                            if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    keys[indexToRemove] = key;
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
                        keys[indexToRemove] = free;
                        postRemoveHook();
                    } else {
                        keys[i] = delayedRemoved;
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    @Override
    boolean removeAll(@Nonnull HashCharSet thisC, @Nonnull CharCollection c) {
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        char free = freeValue;
        char[] keys = set;
        int capacityMask = keys.length - 1;
        int firstDelayedRemoved = -1;
        char delayedRemoved = (char) 0;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
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
                            char keyToShift;
                            if ((keyToShift = keys[indexToShift]) == free) {
                                break;
                            }
                            if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    keys[indexToRemove] = key;
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
                        keys[indexToRemove] = free;
                        postRemoveHook();
                    } else {
                        keys[i] = delayedRemoved;
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }


    @Override
    public boolean retainAll(@Nonnull HashCharSet thisC, @Nonnull Collection<?> c) {
        if (c instanceof CharCollection)
            return retainAll(thisC, (CharCollection) c);
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
        char free = freeValue;
        char[] keys = set;
        int capacityMask = keys.length - 1;
        int firstDelayedRemoved = -1;
        char delayedRemoved = (char) 0;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
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
                            char keyToShift;
                            if ((keyToShift = keys[indexToShift]) == free) {
                                break;
                            }
                            if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    keys[indexToRemove] = key;
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
                        keys[indexToRemove] = free;
                        postRemoveHook();
                    } else {
                        keys[i] = delayedRemoved;
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    private boolean retainAll(@Nonnull HashCharSet thisC, @Nonnull CharCollection c) {
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
        char free = freeValue;
        char[] keys = set;
        int capacityMask = keys.length - 1;
        int firstDelayedRemoved = -1;
        char delayedRemoved = (char) 0;
        long[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            char key;
            if ((key = keys[i]) != free) {
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
                            char keyToShift;
                            if ((keyToShift = keys[indexToShift]) == free) {
                                break;
                            }
                            if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    keys[indexToRemove] = key;
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
                        keys[indexToRemove] = free;
                        postRemoveHook();
                    } else {
                        keys[i] = delayedRemoved;
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }


    @Override
    void closeDelayedRemoved(int firstDelayedRemoved
            , char delayedRemoved) {
        char free = freeValue;
        char[] keys = set;
        long[] vals = values;
        int capacityMask = keys.length - 1;
        for (int i = firstDelayedRemoved; i >= 0; i--) {
            if (keys[i] == delayedRemoved) {
                int indexToRemove = i;
                int indexToShift = indexToRemove;
                int shiftDistance = 1;
                while (true) {
                    indexToShift = (indexToShift - 1) & capacityMask;
                    char keyToShift;
                    if ((keyToShift = keys[indexToShift]) == free) {
                        break;
                    }
                    if ((keyToShift != delayedRemoved) && (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance)) {
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
                keys[indexToRemove] = free;
                postRemoveHook();
            }
        }
    }



    @Override
    public CharIterator iterator() {
        int mc = modCount();
        return new NoRemovedKeyIterator(mc);
    }

    @Override
    public CharCursor setCursor() {
        int mc = modCount();
        return new NoRemovedKeyCursor(mc);
    }


    class NoRemovedKeyIterator extends NoRemovedIterator {
         long[] vals;

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
                    char[] keys = this.keys;
                    long[] vals = this.vals;
                    if (keys == set) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            char keyToShift;
                            if ((keyToShift = keys[indexToShift]) == free) {
                                break;
                            }
                            if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.keys == keys) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 1) > 0) {
                                            this.keys = Arrays.copyOf(keys, slotsToCopy);
                                            this.vals = Arrays.copyOf(vals, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.keys[indexToRemove] = free;
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.nextIndex = index;
                                        if (indexToShift < index - 1) {
                                            this.next = keyToShift;
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
                        keys[indexToRemove] = free;
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
         long[] vals;

        private NoRemovedKeyCursor(int mc) {
            super(mc);
            vals = values;
        }

        @Override
        public void remove() {
            char curKey;
            char free;
            if ((curKey = this.curKey) != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    int index = this.index;
                    char[] keys = this.keys;
                    long[] vals = this.vals;
                    if (keys == set) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            char keyToShift;
                            if ((keyToShift = keys[indexToShift]) == free) {
                                break;
                            }
                            if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.keys == keys) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.keys = Arrays.copyOf(keys, slotsToCopy);
                                            this.vals = Arrays.copyOf(vals, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.keys[indexToRemove] = free;
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
                        keys[indexToRemove] = free;
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





    class EntryView extends AbstractSetView<Map.Entry<Character, Double>>
            implements HashObjSet<Map.Entry<Character, Double>>,
            InternalObjCollectionOps<Map.Entry<Character, Double>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Character, Double>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Character>defaultEquality()
                    ,
                    Equivalence.<Double>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return MutableLHashSeparateKVCharDoubleMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return MutableLHashSeparateKVCharDoubleMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return MutableLHashSeparateKVCharDoubleMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Character, Double> e = (Map.Entry<Character, Double>) o;
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
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
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
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
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
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Character, Double>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
                    action.accept(new MutableEntry(mc, i, key, vals[i]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Character, Double>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
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
        public ObjIterator<Map.Entry<Character, Double>> iterator() {
            int mc = modCount();
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Character, Double>> cursor() {
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
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
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
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
                    changed |= s.remove(e.with(key, vals[i]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Character, Double>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
                    changed |= c.add(new MutableEntry(mc, i, key, vals[i]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        public int hashCode() {
            return MutableLHashSeparateKVCharDoubleMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
                    sb.append(' ');
                    sb.append(key);
                    sb.append('=');
                    sb.append(Double.longBitsToDouble(vals[i]));
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
            return MutableLHashSeparateKVCharDoubleMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Character, Double> e = (Map.Entry<Character, Double>) o;
                char key = e.getKey();
                double value = e.getValue();
                return MutableLHashSeparateKVCharDoubleMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Character, Double>> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            char free = freeValue;
            char[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            char delayedRemoved = (char) 0;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
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
                                char keyToShift;
                                if ((keyToShift = keys[indexToShift]) == free) {
                                    break;
                                }
                                if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        keys[indexToRemove] = key;
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
                            keys[indexToRemove] = free;
                            postRemoveHook();
                        } else {
                            keys[i] = delayedRemoved;
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
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
            char free = freeValue;
            char[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            char delayedRemoved = (char) 0;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
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
                                char keyToShift;
                                if ((keyToShift = keys[indexToShift]) == free) {
                                    break;
                                }
                                if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        keys[indexToRemove] = key;
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
                            keys[indexToRemove] = free;
                            postRemoveHook();
                        } else {
                            keys[i] = delayedRemoved;
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
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
            char free = freeValue;
            char[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            char delayedRemoved = (char) 0;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
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
                                char keyToShift;
                                if ((keyToShift = keys[indexToShift]) == free) {
                                    break;
                                }
                                if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        keys[indexToRemove] = key;
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
                            keys[indexToRemove] = free;
                            postRemoveHook();
                        } else {
                            keys[i] = delayedRemoved;
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public void clear() {
            MutableLHashSeparateKVCharDoubleMapGO.this.clear();
        }
    }


    abstract class CharDoubleEntry extends AbstractEntry<Character, Double> {

        abstract char key();

        @Override
        public final Character getKey() {
            return key();
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
            char k2;
            long v2;
            try {
                e2 = (Map.Entry) o;
                k2 = (Character) e2.getKey();
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


    class MutableEntry extends CharDoubleEntry {
        final int modCount;
        private final int index;
        final char key;
        private long value;

        MutableEntry(int modCount, int index, char key, long value) {
            this.modCount = modCount;
            this.index = index;
            this.key = key;
            this.value = value;
        }

        @Override
        public char key() {
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
            values[index] = newValue;
        }
    }



    class ReusableEntry extends CharDoubleEntry {
        private char key;
        private long value;

        ReusableEntry with(char key, long value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public char key() {
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
            return MutableLHashSeparateKVCharDoubleMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return MutableLHashSeparateKVCharDoubleMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return MutableLHashSeparateKVCharDoubleMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(double v) {
            return MutableLHashSeparateKVCharDoubleMapGO.this.containsValue(v);
        }

        @Override
        public boolean contains(long bits) {
            return MutableLHashSeparateKVCharDoubleMapGO.this.containsValue(bits);
        }


        @Override
        public void forEach(Consumer<? super Double> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    action.accept(Double.longBitsToDouble(vals[i]));
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
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    action.accept(Double.longBitsToDouble(vals[i]));
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
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    if (!predicate.test(Double.longBitsToDouble(vals[i]))) {
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
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    if (!c.contains(Double.longBitsToDouble(vals[i]))) {
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
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
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
        public boolean reverseAddAllTo(DoubleCollection c) {
            if (c instanceof InternalDoubleCollectionOps)
                return reverseAddAllTo((InternalDoubleCollectionOps) c);
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    changed |= c.add(Double.longBitsToDouble(vals[i]));
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
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    changed |= c.add(vals[i]);
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
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    changed |= s.removeDouble(Double.longBitsToDouble(vals[i]));
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
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    changed |= s.removeDouble(vals[i]);
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
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    result[resultIndex++] = Double.longBitsToDouble(vals[i]);
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
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    a[resultIndex++] = (T) Double.valueOf(Double.longBitsToDouble(vals[i]));
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
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    result[resultIndex++] = Double.longBitsToDouble(vals[i]);
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
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    a[resultIndex++] = Double.longBitsToDouble(vals[i]);
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
            char free = freeValue;
            char[] keys = set;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    sb.append(' ').append(Double.longBitsToDouble(vals[i])).append(',');
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
            MutableLHashSeparateKVCharDoubleMapGO.this.clear();
        }

        
        public boolean removeIf(Predicate<? super Double> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            char free = freeValue;
            char[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            char delayedRemoved = (char) 0;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
                    if (filter.test(Double.longBitsToDouble(vals[i]))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                char keyToShift;
                                if ((keyToShift = keys[indexToShift]) == free) {
                                    break;
                                }
                                if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        keys[indexToRemove] = key;
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
                            keys[indexToRemove] = free;
                            postRemoveHook();
                        } else {
                            keys[i] = delayedRemoved;
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
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
            char free = freeValue;
            char[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            char delayedRemoved = (char) 0;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
                    if (filter.test(Double.longBitsToDouble(vals[i]))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                char keyToShift;
                                if ((keyToShift = keys[indexToShift]) == free) {
                                    break;
                                }
                                if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        keys[indexToRemove] = key;
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
                            keys[indexToRemove] = free;
                            postRemoveHook();
                        } else {
                            keys[i] = delayedRemoved;
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
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
            char free = freeValue;
            char[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            char delayedRemoved = (char) 0;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
                    if (c.contains(Double.longBitsToDouble(vals[i]))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                char keyToShift;
                                if ((keyToShift = keys[indexToShift]) == free) {
                                    break;
                                }
                                if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        keys[indexToRemove] = key;
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
                            keys[indexToRemove] = free;
                            postRemoveHook();
                        } else {
                            keys[i] = delayedRemoved;
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
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
            char free = freeValue;
            char[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            char delayedRemoved = (char) 0;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
                    if (c.contains(Double.longBitsToDouble(vals[i]))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                char keyToShift;
                                if ((keyToShift = keys[indexToShift]) == free) {
                                    break;
                                }
                                if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        keys[indexToRemove] = key;
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
                            keys[indexToRemove] = free;
                            postRemoveHook();
                        } else {
                            keys[i] = delayedRemoved;
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
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
            char free = freeValue;
            char[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            char delayedRemoved = (char) 0;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
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
                                char keyToShift;
                                if ((keyToShift = keys[indexToShift]) == free) {
                                    break;
                                }
                                if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        keys[indexToRemove] = key;
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
                            keys[indexToRemove] = free;
                            postRemoveHook();
                        } else {
                            keys[i] = delayedRemoved;
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
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
            char free = freeValue;
            char[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            char delayedRemoved = (char) 0;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
                    if (!c.contains(Double.longBitsToDouble(vals[i]))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                char keyToShift;
                                if ((keyToShift = keys[indexToShift]) == free) {
                                    break;
                                }
                                if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        keys[indexToRemove] = key;
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
                            keys[indexToRemove] = free;
                            postRemoveHook();
                        } else {
                            keys[i] = delayedRemoved;
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
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
            char free = freeValue;
            char[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            char delayedRemoved = (char) 0;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
                    if (!c.contains(Double.longBitsToDouble(vals[i]))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                char keyToShift;
                                if ((keyToShift = keys[indexToShift]) == free) {
                                    break;
                                }
                                if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        keys[indexToRemove] = key;
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
                            keys[indexToRemove] = free;
                            postRemoveHook();
                        } else {
                            keys[i] = delayedRemoved;
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
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
            char free = freeValue;
            char[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            char delayedRemoved = (char) 0;
            long[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
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
                                char keyToShift;
                                if ((keyToShift = keys[indexToShift]) == free) {
                                    break;
                                }
                                if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        keys[indexToRemove] = key;
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
                            keys[indexToRemove] = free;
                            postRemoveHook();
                        } else {
                            keys[i] = delayedRemoved;
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }
    }



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Character, Double>> {
        char[] keys;
        long[] vals;
        final char free;
        final int capacityMask;
        int expectedModCount;
        
        class MutableEntry2 extends MutableEntry {
            MutableEntry2(int modCount, int index, char key, long value) {
                super(modCount, index, key, value);
            }
            
            @Override
            void updateValueInTable(long newValue) {
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
            char[] keys = this.keys = set;
            capacityMask = keys.length - 1;
            long[] vals = this.vals = values;
            char free = this.free = freeValue;
            int nextI = keys.length;
            while (--nextI >= 0) {
                char key;
                if ((key = keys[nextI]) != free) {
                    next = new MutableEntry2(mc, nextI, key, vals[nextI]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Character, Double>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            char[] keys = this.keys;
            long[] vals = this.vals;
            char free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
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
        public Map.Entry<Character, Double> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
                    index = nextI;
                    char[] keys = this.keys;
                    char free = this.free;
                    MutableEntry prev = next;
                    while (--nextI >= 0) {
                        char key;
                        if ((key = keys[nextI]) != free) {
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
                    char[] keys = this.keys;
                    long[] vals = this.vals;
                    if (keys == set) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            char keyToShift;
                            if ((keyToShift = keys[indexToShift]) == free) {
                                break;
                            }
                            if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.keys == keys) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 1) > 0) {
                                            this.keys = Arrays.copyOf(keys, slotsToCopy);
                                            this.vals = Arrays.copyOf(vals, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.keys[indexToRemove] = free;
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
                        keys[indexToRemove] = free;
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


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Character, Double>> {
        char[] keys;
        long[] vals;
        final char free;
        final int capacityMask;
        int expectedModCount;
        
        class MutableEntry2 extends MutableEntry {
            MutableEntry2(int modCount, int index, char key, long value) {
                super(modCount, index, key, value);
            }
            
            @Override
            void updateValueInTable(long newValue) {
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
        char curKey;
        long curValue;

        NoRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            char[] keys = this.keys = set;
            capacityMask = keys.length - 1;
            index = keys.length;
            vals = values;
            char free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Character, Double>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            char[] keys = this.keys;
            long[] vals = this.vals;
            char free = this.free;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
                    action.accept(new MutableEntry2(mc, i, key, vals[i]));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public Map.Entry<Character, Double> elem() {
            char curKey;
            if ((curKey = this.curKey) != free) {
                return new MutableEntry2(expectedModCount, index, curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                char[] keys = this.keys;
                char free = this.free;
                for (int i = index - 1; i >= 0; i--) {
                    char key;
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
            } else {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            char curKey;
            char free;
            if ((curKey = this.curKey) != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    int index = this.index;
                    char[] keys = this.keys;
                    long[] vals = this.vals;
                    if (keys == set) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            char keyToShift;
                            if ((keyToShift = keys[indexToShift]) == free) {
                                break;
                            }
                            if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.keys == keys) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.keys = Arrays.copyOf(keys, slotsToCopy);
                                            this.vals = Arrays.copyOf(vals, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.keys[indexToRemove] = free;
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
                        keys[indexToRemove] = free;
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
        char[] keys;
        long[] vals;
        final char free;
        final int capacityMask;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        double next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            char[] keys = this.keys = set;
            capacityMask = keys.length - 1;
            long[] vals = this.vals = values;
            char free = this.free = freeValue;
            int nextI = keys.length;
            while (--nextI >= 0) {
                if (keys[nextI] != free) {
                    next = Double.longBitsToDouble(vals[nextI]);
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
                    char[] keys = this.keys;
                    char free = this.free;
                    double prev = next;
                    while (--nextI >= 0) {
                        if (keys[nextI] != free) {
                            next = Double.longBitsToDouble(vals[nextI]);
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
            char[] keys = this.keys;
            long[] vals = this.vals;
            char free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if (keys[i] != free) {
                    action.accept(Double.longBitsToDouble(vals[i]));
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
            char[] keys = this.keys;
            long[] vals = this.vals;
            char free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if (keys[i] != free) {
                    action.accept(Double.longBitsToDouble(vals[i]));
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
                    char[] keys = this.keys;
                    long[] vals = this.vals;
                    if (keys == set) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            char keyToShift;
                            if ((keyToShift = keys[indexToShift]) == free) {
                                break;
                            }
                            if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.keys == keys) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 1) > 0) {
                                            this.keys = Arrays.copyOf(keys, slotsToCopy);
                                            this.vals = Arrays.copyOf(vals, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.keys[indexToRemove] = free;
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.nextIndex = index;
                                        if (indexToShift < index - 1) {
                                            this.next = Double.longBitsToDouble(vals[indexToShift]);
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
                        keys[indexToRemove] = free;
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


    class NoRemovedValueCursor implements DoubleCursor {
        char[] keys;
        long[] vals;
        final char free;
        final int capacityMask;
        int expectedModCount;
        int index;
        char curKey;
        long curValue;

        NoRemovedValueCursor(int mc) {
            expectedModCount = mc;
            char[] keys = this.keys = set;
            capacityMask = keys.length - 1;
            index = keys.length;
            vals = values;
            char free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(DoubleConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            char[] keys = this.keys;
            long[] vals = this.vals;
            char free = this.free;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    action.accept(Double.longBitsToDouble(vals[i]));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public double elem() {
            if (curKey != free) {
                return Double.longBitsToDouble(curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                char[] keys = this.keys;
                char free = this.free;
                for (int i = index - 1; i >= 0; i--) {
                    char key;
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
            } else {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            char curKey;
            char free;
            if ((curKey = this.curKey) != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    int index = this.index;
                    char[] keys = this.keys;
                    long[] vals = this.vals;
                    if (keys == set) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            char keyToShift;
                            if ((keyToShift = keys[indexToShift]) == free) {
                                break;
                            }
                            if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.keys == keys) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.keys = Arrays.copyOf(keys, slotsToCopy);
                                            this.vals = Arrays.copyOf(vals, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.keys[indexToRemove] = free;
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
                        keys[indexToRemove] = free;
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



    class NoRemovedMapCursor implements CharDoubleCursor {
        char[] keys;
        long[] vals;
        final char free;
        final int capacityMask;
        int expectedModCount;
        int index;
        char curKey;
        long curValue;

        NoRemovedMapCursor(int mc) {
            expectedModCount = mc;
            char[] keys = this.keys = set;
            capacityMask = keys.length - 1;
            index = keys.length;
            vals = values;
            char free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(CharDoubleConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            char[] keys = this.keys;
            long[] vals = this.vals;
            char free = this.free;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                char key;
                if ((key = keys[i]) != free) {
                    action.accept(key, Double.longBitsToDouble(vals[i]));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public char key() {
            char curKey;
            if ((curKey = this.curKey) != free) {
                return curKey;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public double value() {
            if (curKey != free) {
                return Double.longBitsToDouble(curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(double value) {
            if (curKey != free) {
                if (expectedModCount == modCount()) {
                    vals[index] = Double.doubleToLongBits(value);
                    if (vals != values) {
                        values[index] = Double.doubleToLongBits(value);
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
                char[] keys = this.keys;
                char free = this.free;
                for (int i = index - 1; i >= 0; i--) {
                    char key;
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
            } else {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            char curKey;
            char free;
            if ((curKey = this.curKey) != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    int index = this.index;
                    char[] keys = this.keys;
                    long[] vals = this.vals;
                    if (keys == set) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            char keyToShift;
                            if ((keyToShift = keys[indexToShift]) == free) {
                                break;
                            }
                            if (((SeparateKVCharKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.keys == keys) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.keys = Arrays.copyOf(keys, slotsToCopy);
                                            this.vals = Arrays.copyOf(vals, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                this.keys[indexToRemove] = free;
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
                        keys[indexToRemove] = free;
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

