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
import net.openhft.koloboke.function.ShortDoubleConsumer;
import net.openhft.koloboke.function.ShortDoublePredicate;
import net.openhft.koloboke.function.ShortDoubleToDoubleFunction;
import net.openhft.koloboke.function.ShortToDoubleFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class MutableQHashSeparateKVShortDoubleMapGO
        extends MutableQHashSeparateKVShortDoubleMapSO {

    @Override
    final void copy(SeparateKVShortDoubleQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVShortDoubleQHash hash) {
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
    public boolean containsEntry(short key, double value) {
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
    public boolean containsEntry(short key, long value) {
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
        int index = index((Short) key);
        if (index >= 0) {
            // key is present
            return Double.longBitsToDouble(values[index]);
        } else {
            // key is absent
            return null;
        }
    }

    

    @Override
    public double get(short key) {
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
        int index = index((Short) key);
        if (index >= 0) {
            // key is present
            return Double.longBitsToDouble(values[index]);
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public double getOrDefault(short key, double defaultValue) {
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
    public void forEach(BiConsumer<? super Short, ? super Double> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        short free = freeValue;
        short removed = removedValue;
        short[] keys = set;
        long[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    action.accept(key, Double.longBitsToDouble(vals[i]));
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free && key != removed) {
                    action.accept(key, Double.longBitsToDouble(vals[i]));
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void forEach(ShortDoubleConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        short free = freeValue;
        short removed = removedValue;
        short[] keys = set;
        long[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    action.accept(key, Double.longBitsToDouble(vals[i]));
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free && key != removed) {
                    action.accept(key, Double.longBitsToDouble(vals[i]));
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public boolean forEachWhile(ShortDoublePredicate predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        short free = freeValue;
        short removed = removedValue;
        short[] keys = set;
        long[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    if (!predicate.test(key, Double.longBitsToDouble(vals[i]))) {
                        terminated = true;
                        break;
                    }
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free && key != removed) {
                    if (!predicate.test(key, Double.longBitsToDouble(vals[i]))) {
                        terminated = true;
                        break;
                    }
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return !terminated;
    }

    @Nonnull
    @Override
    public ShortDoubleCursor cursor() {
        int mc = modCount();
        if (!noRemoved())
            return new SomeRemovedMapCursor(mc);
        return new NoRemovedMapCursor(mc);
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonShortDoubleMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalShortDoubleMapOps m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        short free = freeValue;
        short removed = removedValue;
        short[] keys = set;
        long[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    if (!m.containsEntry(key, vals[i])) {
                        containsAll = false;
                        break;
                    }
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free && key != removed) {
                    if (!m.containsEntry(key, vals[i])) {
                        containsAll = false;
                        break;
                    }
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return containsAll;
    }

    @Override
    public void reversePutAllTo(InternalShortDoubleMapOps m) {
        if (isEmpty())
            return;
        int mc = modCount();
        short free = freeValue;
        short removed = removedValue;
        short[] keys = set;
        long[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    m.justPut(key, vals[i]);
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free && key != removed) {
                    m.justPut(key, vals[i]);
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<Short, Double>> entrySet() {
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
        short free = freeValue;
        short removed = removedValue;
        short[] keys = set;
        long[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                long val;
                    hashCode += key ^ ((int) ((val = vals[i]) ^ (val >>> 32)));
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free && key != removed) {
                long val;
                    hashCode += key ^ ((int) ((val = vals[i]) ^ (val >>> 32)));
                }
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
        short free = freeValue;
        short removed = removedValue;
        short[] keys = set;
        long[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
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
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free && key != removed) {
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
        short free = freeValue;
        short removed = removedValue;
        short[] keys = set;
        long[] vals = values;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        short[] newKeys = set;
        int capacity = newKeys.length;
        long[] newVals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    int index;
                    if (newKeys[index = SeparateKVShortKeyMixing.mix(key) % capacity] != free) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if (newKeys[bIndex] == free) {
                                index = bIndex;
                                break;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if (newKeys[fIndex] == free) {
                                index = fIndex;
                                break;
                            }
                            step += 2;
                        }
                    }
                    newKeys[index] = key;
                    newVals[index] = vals[i];
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free && key != removed) {
                    int index;
                    if (newKeys[index = SeparateKVShortKeyMixing.mix(key) % capacity] != free) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if (newKeys[bIndex] == free) {
                                index = bIndex;
                                break;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if (newKeys[fIndex] == free) {
                                index = fIndex;
                                break;
                            }
                            step += 2;
                        }
                    }
                    newKeys[index] = key;
                    newVals[index] = vals[i];
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }


    @Override
    public Double put(Short key, Double value) {
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
    public double put(short key, double value) {
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
    public Double putIfAbsent(Short key, Double value) {
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
    public double putIfAbsent(short key, double value) {
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
    public void justPut(short key, double value) {
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
    public void justPut(short key, long value) {
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
    public Double compute(Short key,
            BiFunction<? super Short, ? super Double, ? extends Double> remappingFunction) {
        short k = key;
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        short free;
        short removed = removedValue;
        if (k == (free = freeValue)) {
            free = changeFree();
        } else if (k == removed) {
            removed = changeRemoved();
        }
        short[] keys = set;
        long[] vals = values;
        int capacity, index;
        short cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVShortKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
            keyAbsentFreeSlot:
            if (cur != free) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == k) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == k) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == free) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            }
                            step += 2;
                        }
                    } else {
                        firstRemoved = -1;
                    }
                } else {
                    firstRemoved = index;
                }
                keyAbsentRemovedSlot: {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = keys[bIndex]) == k) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == k) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 2;
                    }
                }
                // key is absent, removed slot
                Double newValue = remappingFunction.apply(k, null);
                if (newValue != null) {
                    incrementModCount();
                    keys[firstRemoved] = k;
                    vals[firstRemoved] = Double.doubleToLongBits(newValue);
                    postRemovedSlotInsertHook();
                    return newValue;
                } else {
                    return null;
                }
            }
            // key is absent, free slot
            Double newValue = remappingFunction.apply(k, null);
            if (newValue != null) {
                incrementModCount();
                keys[index] = k;
                vals[index] = Double.doubleToLongBits(newValue);
                postFreeSlotInsertHook();
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
            incrementModCount();
            keys[index] = removed;
            postRemoveHook();
            return null;
        }
    }


    @Override
    public double compute(short key, ShortDoubleToDoubleFunction remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        short free;
        short removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        short[] keys = set;
        long[] vals = values;
        int capacity, index;
        short cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVShortKeyMixing.mix(key) % (capacity = keys.length)]) != key) {
            keyAbsentFreeSlot:
            if (cur != free) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == key) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == key) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == free) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            }
                            step += 2;
                        }
                    } else {
                        firstRemoved = -1;
                    }
                } else {
                    firstRemoved = index;
                }
                keyAbsentRemovedSlot: {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = keys[bIndex]) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == key) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 2;
                    }
                }
                // key is absent, removed slot
                double newValue = remappingFunction.applyAsDouble(key, defaultValue());
                incrementModCount();
                keys[firstRemoved] = key;
                vals[firstRemoved] = Double.doubleToLongBits(newValue);
                postRemovedSlotInsertHook();
                return newValue;
            }
            // key is absent, free slot
            double newValue = remappingFunction.applyAsDouble(key, defaultValue());
            incrementModCount();
            keys[index] = key;
            vals[index] = Double.doubleToLongBits(newValue);
            postFreeSlotInsertHook();
            return newValue;
        }
        // key is present
        double newValue = remappingFunction.applyAsDouble(key, Double.longBitsToDouble(vals[index]));
        vals[index] = Double.doubleToLongBits(newValue);
        return newValue;
    }


    @Override
    public Double computeIfAbsent(Short key,
            Function<? super Short, ? extends Double> mappingFunction) {
        short k = key;
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        short free;
        short removed = removedValue;
        if (k == (free = freeValue)) {
            free = changeFree();
        } else if (k == removed) {
            removed = changeRemoved();
        }
        short[] keys = set;
        long[] vals = values;
        int capacity, index;
        short cur;
        if ((cur = keys[index = SeparateKVShortKeyMixing.mix(k) % (capacity = keys.length)]) == k) {
            // key is present
            return Double.longBitsToDouble(vals[index]);
        } else {
            keyAbsentFreeSlot:
            if (cur != free) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == k) {
                                // key is present
                                return Double.longBitsToDouble(vals[bIndex]);
                            } else if (cur == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == k) {
                                // key is present
                                return Double.longBitsToDouble(vals[fIndex]);
                            } else if (cur == free) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            }
                            step += 2;
                        }
                    } else {
                        firstRemoved = -1;
                    }
                } else {
                    firstRemoved = index;
                }
                keyAbsentRemovedSlot: {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = keys[bIndex]) == k) {
                            // key is present
                            return Double.longBitsToDouble(vals[bIndex]);
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == k) {
                            // key is present
                            return Double.longBitsToDouble(vals[fIndex]);
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 2;
                    }
                }
                // key is absent, removed slot
                Double value = mappingFunction.apply(k);
                if (value != null) {
                    incrementModCount();
                    keys[firstRemoved] = k;
                    vals[firstRemoved] = Double.doubleToLongBits(value);
                    postRemovedSlotInsertHook();
                    return value;
                } else {
                    return null;
                }
            }
            // key is absent, free slot
            Double value = mappingFunction.apply(k);
            if (value != null) {
                incrementModCount();
                keys[index] = k;
                vals[index] = Double.doubleToLongBits(value);
                postFreeSlotInsertHook();
                return value;
            } else {
                return null;
            }
        }
    }


    @Override
    public double computeIfAbsent(short key, ShortToDoubleFunction mappingFunction) {
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        short free;
        short removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        short[] keys = set;
        long[] vals = values;
        int capacity, index;
        short cur;
        if ((cur = keys[index = SeparateKVShortKeyMixing.mix(key) % (capacity = keys.length)]) == key) {
            // key is present
            return Double.longBitsToDouble(vals[index]);
        } else {
            keyAbsentFreeSlot:
            if (cur != free) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == key) {
                                // key is present
                                return Double.longBitsToDouble(vals[bIndex]);
                            } else if (cur == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == key) {
                                // key is present
                                return Double.longBitsToDouble(vals[fIndex]);
                            } else if (cur == free) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            }
                            step += 2;
                        }
                    } else {
                        firstRemoved = -1;
                    }
                } else {
                    firstRemoved = index;
                }
                keyAbsentRemovedSlot: {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = keys[bIndex]) == key) {
                            // key is present
                            return Double.longBitsToDouble(vals[bIndex]);
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == key) {
                            // key is present
                            return Double.longBitsToDouble(vals[fIndex]);
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 2;
                    }
                }
                // key is absent, removed slot
                double value = mappingFunction.applyAsDouble(key);
                incrementModCount();
                keys[firstRemoved] = key;
                vals[firstRemoved] = Double.doubleToLongBits(value);
                postRemovedSlotInsertHook();
                return value;
            }
            // key is absent, free slot
            double value = mappingFunction.applyAsDouble(key);
            incrementModCount();
            keys[index] = key;
            vals[index] = Double.doubleToLongBits(value);
            postFreeSlotInsertHook();
            return value;
        }
    }


    @Override
    public Double computeIfPresent(Short key,
            BiFunction<? super Short, ? super Double, ? extends Double> remappingFunction) {
        short k = key;
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
                incrementModCount();
                set[index] = removedValue;
                postRemoveHook();
                return null;
            }
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public double computeIfPresent(short key, ShortDoubleToDoubleFunction remappingFunction) {
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
    public Double merge(Short key, Double value,
            BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        short k = key;
        if (value == null)
            throw new java.lang.NullPointerException();
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        short free;
        short removed = removedValue;
        if (k == (free = freeValue)) {
            free = changeFree();
        } else if (k == removed) {
            removed = changeRemoved();
        }
        short[] keys = set;
        long[] vals = values;
        int capacity, index;
        short cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVShortKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
            keyAbsentFreeSlot:
            if (cur != free) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == k) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == k) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == free) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            }
                            step += 2;
                        }
                    } else {
                        firstRemoved = -1;
                    }
                } else {
                    firstRemoved = index;
                }
                keyAbsentRemovedSlot: {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = keys[bIndex]) == k) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == k) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 2;
                    }
                }
                // key is absent, removed slot
                incrementModCount();
                keys[firstRemoved] = k;
                vals[firstRemoved] = Double.doubleToLongBits(value);
                postRemovedSlotInsertHook();
                return value;
            }
            // key is absent, free slot
            incrementModCount();
            keys[index] = k;
            vals[index] = Double.doubleToLongBits(value);
            postFreeSlotInsertHook();
            return value;
        }
        // key is present
        Double newValue = remappingFunction.apply(Double.longBitsToDouble(vals[index]), value);
        if (newValue != null) {
            vals[index] = Double.doubleToLongBits(newValue);
            return newValue;
        } else {
            incrementModCount();
            keys[index] = removed;
            postRemoveHook();
            return null;
        }
    }


    @Override
    public double merge(short key, double value, DoubleBinaryOperator remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        short free;
        short removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        short[] keys = set;
        long[] vals = values;
        int capacity, index;
        short cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVShortKeyMixing.mix(key) % (capacity = keys.length)]) != key) {
            keyAbsentFreeSlot:
            if (cur != free) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == key) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == key) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == free) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            }
                            step += 2;
                        }
                    } else {
                        firstRemoved = -1;
                    }
                } else {
                    firstRemoved = index;
                }
                keyAbsentRemovedSlot: {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = keys[bIndex]) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == key) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 2;
                    }
                }
                // key is absent, removed slot
                incrementModCount();
                keys[firstRemoved] = key;
                vals[firstRemoved] = Double.doubleToLongBits(value);
                postRemovedSlotInsertHook();
                return value;
            }
            // key is absent, free slot
            incrementModCount();
            keys[index] = key;
            vals[index] = Double.doubleToLongBits(value);
            postFreeSlotInsertHook();
            return value;
        }
        // key is present
        double newValue = remappingFunction.applyAsDouble(Double.longBitsToDouble(vals[index]), value);
        vals[index] = Double.doubleToLongBits(newValue);
        return newValue;
    }


    @Override
    public double addValue(short key, double value) {
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
    public double addValue(short key, double addition, double defaultValue) {
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
    public void putAll(@Nonnull Map<? extends Short, ? extends Double> m) {
        CommonShortDoubleMapOps.putAll(this, m);
    }


    @Override
    public Double replace(Short key, Double value) {
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
    public double replace(short key, double value) {
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
    public boolean replace(Short key, Double oldValue, Double newValue) {
        return replace(key.shortValue(),
                oldValue.doubleValue(),
                newValue.doubleValue());
    }

    @Override
    public boolean replace(short key, double oldValue, double newValue) {
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
            BiFunction<? super Short, ? super Double, ? extends Double> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        short free = freeValue;
        short removed = removedValue;
        short[] keys = set;
        long[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    vals[i] = Double.doubleToLongBits(function.apply(key, Double.longBitsToDouble(vals[i])));
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free && key != removed) {
                    vals[i] = Double.doubleToLongBits(function.apply(key, Double.longBitsToDouble(vals[i])));
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void replaceAll(ShortDoubleToDoubleFunction function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        short free = freeValue;
        short removed = removedValue;
        short[] keys = set;
        long[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    vals[i] = Double.doubleToLongBits(function.applyAsDouble(key, Double.longBitsToDouble(vals[i])));
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free && key != removed) {
                    vals[i] = Double.doubleToLongBits(function.applyAsDouble(key, Double.longBitsToDouble(vals[i])));
                }
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
        incrementModCount();
        super.removeAt(index);
        postRemoveHook();
    }

    @Override
    public Double remove(Object key) {
        short k = (Short) key;
        short free, removed;
        if (k != (free = freeValue) && k != (removed = removedValue)) {
            short[] keys = set;
            int capacity, index;
            short cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVShortKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
                if (cur == free) {
                    // key is absent, free slot
                    return null;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = keys[bIndex]) == k) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return null;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == k) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return null;
                        }
                        step += 2;
                    }
                }
            }
            // key is present
            double val = Double.longBitsToDouble(values[index]);
            incrementModCount();
            keys[index] = removed;
            postRemoveHook();
            return val;
        } else {
            // key is absent
            return null;
        }
    }


    @Override
    public boolean justRemove(short key) {
        short free, removed;
        if (key != (free = freeValue) && key != (removed = removedValue)) {
            short[] keys = set;
            int capacity, index;
            short cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVShortKeyMixing.mix(key) % (capacity = keys.length)]) != key) {
                if (cur == free) {
                    // key is absent, free slot
                    return false;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = keys[bIndex]) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return false;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == key) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return false;
                        }
                        step += 2;
                    }
                }
            }
            // key is present
            incrementModCount();
            keys[index] = removed;
            postRemoveHook();
            return true;
        } else {
            // key is absent
            return false;
        }
    }



    

    @Override
    public double remove(short key) {
        short free, removed;
        if (key != (free = freeValue) && key != (removed = removedValue)) {
            short[] keys = set;
            int capacity, index;
            short cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVShortKeyMixing.mix(key) % (capacity = keys.length)]) != key) {
                if (cur == free) {
                    // key is absent, free slot
                    return defaultValue();
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = keys[bIndex]) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return defaultValue();
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == key) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return defaultValue();
                        }
                        step += 2;
                    }
                }
            }
            // key is present
            double val = Double.longBitsToDouble(values[index]);
            incrementModCount();
            keys[index] = removed;
            postRemoveHook();
            return val;
        } else {
            // key is absent
            return defaultValue();
        }
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Short) key).shortValue(),
                ((Double) value).doubleValue()
                );
    }

    @Override
    public boolean remove(short key, double value) {
        short free, removed;
        if (key != (free = freeValue) && key != (removed = removedValue)) {
            short[] keys = set;
            int capacity, index;
            short cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVShortKeyMixing.mix(key) % (capacity = keys.length)]) != key) {
                if (cur == free) {
                    // key is absent, free slot
                    return false;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = keys[bIndex]) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return false;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == key) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return false;
                        }
                        step += 2;
                    }
                }
            }
            // key is present
            if (values[index] == Double.doubleToLongBits(value)) {
                incrementModCount();
                keys[index] = removed;
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
    public boolean removeIf(ShortDoublePredicate filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        short free = freeValue;
        short removed = removedValue;
        short[] keys = set;
        long[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    if (filter.test(key, Double.longBitsToDouble(vals[i]))) {
                        incrementModCount();
                        mc++;
                        keys[i] = removed;
                        postRemoveHook();
                        changed = true;
                    }
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free && key != removed) {
                    if (filter.test(key, Double.longBitsToDouble(vals[i]))) {
                        incrementModCount();
                        mc++;
                        keys[i] = removed;
                        postRemoveHook();
                        changed = true;
                    }
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }






    class EntryView extends AbstractSetView<Map.Entry<Short, Double>>
            implements HashObjSet<Map.Entry<Short, Double>>,
            InternalObjCollectionOps<Map.Entry<Short, Double>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Short, Double>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Short>defaultEquality()
                    ,
                    Equivalence.<Double>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return MutableQHashSeparateKVShortDoubleMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return MutableQHashSeparateKVShortDoubleMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return MutableQHashSeparateKVShortDoubleMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Short, Double> e = (Map.Entry<Short, Double>) o;
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free) {
                        result[resultIndex++] = new MutableEntry(mc, i, key, vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        result[resultIndex++] = new MutableEntry(mc, i, key, vals[i]);
                    }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free) {
                        a[resultIndex++] = (T) new MutableEntry(mc, i, key, vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        a[resultIndex++] = (T) new MutableEntry(mc, i, key, vals[i]);
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }

        @Override
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Short, Double>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free) {
                        action.accept(new MutableEntry(mc, i, key, vals[i]));
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        action.accept(new MutableEntry(mc, i, key, vals[i]));
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Short, Double>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free) {
                        if (!predicate.test(new MutableEntry(mc, i, key, vals[i]))) {
                            terminated = true;
                            break;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        if (!predicate.test(new MutableEntry(mc, i, key, vals[i]))) {
                            terminated = true;
                            break;
                        }
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return !terminated;
        }

        @Override
        @Nonnull
        public ObjIterator<Map.Entry<Short, Double>> iterator() {
            int mc = modCount();
            if (!noRemoved())
                return new SomeRemovedEntryIterator(mc);
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Short, Double>> cursor() {
            int mc = modCount();
            if (!noRemoved())
                return new SomeRemovedEntryCursor(mc);
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free) {
                        if (!c.contains(e.with(key, vals[i]))) {
                            containsAll = false;
                            break;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        if (!c.contains(e.with(key, vals[i]))) {
                            containsAll = false;
                            break;
                        }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free) {
                        changed |= s.remove(e.with(key, vals[i]));
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        changed |= s.remove(e.with(key, vals[i]));
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Short, Double>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free) {
                        changed |= c.add(new MutableEntry(mc, i, key, vals[i]));
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        changed |= c.add(new MutableEntry(mc, i, key, vals[i]));
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        public int hashCode() {
            return MutableQHashSeparateKVShortDoubleMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
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
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
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
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            sb.setCharAt(0, '[');
            sb.setCharAt(sb.length() - 1, ']');
            return sb.toString();
        }

        @Override
        public boolean shrink() {
            return MutableQHashSeparateKVShortDoubleMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Short, Double> e = (Map.Entry<Short, Double>) o;
                short key = e.getKey();
                double value = e.getValue();
                return MutableQHashSeparateKVShortDoubleMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Short, Double>> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free) {
                        if (filter.test(new MutableEntry(mc, i, key, vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        if (filter.test(new MutableEntry(mc, i, key, vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free) {
                        if (c.contains(e.with(key, vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        if (c.contains(e.with(key, vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free) {
                        if (!c.contains(e.with(key, vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        if (!c.contains(e.with(key, vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public void clear() {
            MutableQHashSeparateKVShortDoubleMapGO.this.clear();
        }
    }


    abstract class ShortDoubleEntry extends AbstractEntry<Short, Double> {

        abstract short key();

        @Override
        public final Short getKey() {
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
            short k2;
            long v2;
            try {
                e2 = (Map.Entry) o;
                k2 = (Short) e2.getKey();
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


    class MutableEntry extends ShortDoubleEntry {
        final int modCount;
        private final int index;
        final short key;
        private long value;

        MutableEntry(int modCount, int index, short key, long value) {
            this.modCount = modCount;
            this.index = index;
            this.key = key;
            this.value = value;
        }

        @Override
        public short key() {
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



    class ReusableEntry extends ShortDoubleEntry {
        private short key;
        private long value;

        ReusableEntry with(short key, long value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public short key() {
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
            return MutableQHashSeparateKVShortDoubleMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return MutableQHashSeparateKVShortDoubleMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return MutableQHashSeparateKVShortDoubleMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(double v) {
            return MutableQHashSeparateKVShortDoubleMapGO.this.containsValue(v);
        }

        @Override
        public boolean contains(long bits) {
            return MutableQHashSeparateKVShortDoubleMapGO.this.containsValue(bits);
        }


        @Override
        public void forEach(Consumer<? super Double> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        action.accept(Double.longBitsToDouble(vals[i]));
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        action.accept(Double.longBitsToDouble(vals[i]));
                    }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        action.accept(Double.longBitsToDouble(vals[i]));
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        action.accept(Double.longBitsToDouble(vals[i]));
                    }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        if (!predicate.test(Double.longBitsToDouble(vals[i]))) {
                            terminated = true;
                            break;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        if (!predicate.test(Double.longBitsToDouble(vals[i]))) {
                            terminated = true;
                            break;
                        }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        if (!c.contains(Double.longBitsToDouble(vals[i]))) {
                            containsAll = false;
                            break;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        if (!c.contains(Double.longBitsToDouble(vals[i]))) {
                            containsAll = false;
                            break;
                        }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        if (!c.contains(vals[i])) {
                            containsAll = false;
                            break;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        if (!c.contains(vals[i])) {
                            containsAll = false;
                            break;
                        }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        changed |= c.add(Double.longBitsToDouble(vals[i]));
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        changed |= c.add(Double.longBitsToDouble(vals[i]));
                    }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        changed |= c.add(vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        changed |= c.add(vals[i]);
                    }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        changed |= s.removeDouble(Double.longBitsToDouble(vals[i]));
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        changed |= s.removeDouble(Double.longBitsToDouble(vals[i]));
                    }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        changed |= s.removeDouble(vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        changed |= s.removeDouble(vals[i]);
                    }
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
            if (!noRemoved())
                return new SomeRemovedValueIterator(mc);
            return new NoRemovedValueIterator(mc);
        }

        @Nonnull
        @Override
        public DoubleCursor cursor() {
            int mc = modCount();
            if (!noRemoved())
                return new SomeRemovedValueCursor(mc);
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        result[resultIndex++] = Double.longBitsToDouble(vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        result[resultIndex++] = Double.longBitsToDouble(vals[i]);
                    }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        a[resultIndex++] = (T) Double.valueOf(Double.longBitsToDouble(vals[i]));
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        a[resultIndex++] = (T) Double.valueOf(Double.longBitsToDouble(vals[i]));
                    }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        result[resultIndex++] = Double.longBitsToDouble(vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        result[resultIndex++] = Double.longBitsToDouble(vals[i]);
                    }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        a[resultIndex++] = Double.longBitsToDouble(vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        a[resultIndex++] = Double.longBitsToDouble(vals[i]);
                    }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        sb.append(' ').append(Double.longBitsToDouble(vals[i])).append(',');
                        if (++elementCount == 8) {
                            int expectedLength = sb.length() * (size() / 8);
                            sb.ensureCapacity(expectedLength + (expectedLength / 2));
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        sb.append(' ').append(Double.longBitsToDouble(vals[i])).append(',');
                        if (++elementCount == 8) {
                            int expectedLength = sb.length() * (size() / 8);
                            sb.ensureCapacity(expectedLength + (expectedLength / 2));
                        }
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
            MutableQHashSeparateKVShortDoubleMapGO.this.clear();
        }

        
        public boolean removeIf(Predicate<? super Double> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        if (filter.test(Double.longBitsToDouble(vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        if (filter.test(Double.longBitsToDouble(vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        if (filter.test(Double.longBitsToDouble(vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        if (filter.test(Double.longBitsToDouble(vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        if (c.contains(Double.longBitsToDouble(vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        if (c.contains(Double.longBitsToDouble(vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        if (c.contains(Double.longBitsToDouble(vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        if (c.contains(Double.longBitsToDouble(vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        if (c.contains(vals[i])) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        if (c.contains(vals[i])) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        if (!c.contains(Double.longBitsToDouble(vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        if (!c.contains(Double.longBitsToDouble(vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        if (!c.contains(Double.longBitsToDouble(vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        if (!c.contains(Double.longBitsToDouble(vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
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
            short free = freeValue;
            short removed = removedValue;
            short[] keys = set;
            long[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        if (!c.contains(vals[i])) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
                        if (!c.contains(vals[i])) {
                            incrementModCount();
                            mc++;
                            keys[i] = removed;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }
    }



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Short, Double>> {
        final short[] keys;
        final long[] vals;
        final short free;
        final short removed;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        MutableEntry next;

        NoRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            short[] keys = this.keys = set;
            long[] vals = this.vals = values;
            short free = this.free = freeValue;
            this.removed = removedValue;
            int nextI = keys.length;
            while (--nextI >= 0) {
                short key;
                if ((key = keys[nextI]) != free) {
                    next = new MutableEntry(mc, nextI, key, vals[nextI]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Short, Double>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            short[] keys = this.keys;
            long[] vals = this.vals;
            short free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    action.accept(new MutableEntry(mc, i, key, vals[i]));
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
        public Map.Entry<Short, Double> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
                    index = nextI;
                    short[] keys = this.keys;
                    short free = this.free;
                    MutableEntry prev = next;
                    while (--nextI >= 0) {
                        short key;
                        if ((key = keys[nextI]) != free) {
                            next = new MutableEntry(mc, nextI, key, vals[nextI]);
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
                    incrementModCount();
                    keys[index] = removed;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Short, Double>> {
        final short[] keys;
        final long[] vals;
        final short free;
        final short removed;
        int expectedModCount;
        int index;
        short curKey;
        long curValue;

        NoRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            short free = this.free = freeValue;
            this.removed = removedValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Short, Double>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            short[] keys = this.keys;
            long[] vals = this.vals;
            short free = this.free;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    action.accept(new MutableEntry(mc, i, key, vals[i]));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public Map.Entry<Short, Double> elem() {
            short curKey;
            if ((curKey = this.curKey) != free) {
                return new MutableEntry(expectedModCount, index, curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                short[] keys = this.keys;
                short free = this.free;
                for (int i = index - 1; i >= 0; i--) {
                    short key;
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
            short free;
            if (curKey != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    incrementModCount();
                    keys[index] = removed;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedEntryIterator implements ObjIterator<Map.Entry<Short, Double>> {
        final short[] keys;
        final long[] vals;
        final short free;
        final short removed;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        MutableEntry next;

        SomeRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            short[] keys = this.keys = set;
            long[] vals = this.vals = values;
            short free = this.free = freeValue;
            short removed = this.removed = removedValue;
            int nextI = keys.length;
            while (--nextI >= 0) {
                short key;
                if ((key = keys[nextI]) != free && key != removed) {
                    next = new MutableEntry(mc, nextI, key, vals[nextI]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Short, Double>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            short[] keys = this.keys;
            long[] vals = this.vals;
            short free = this.free;
            short removed = this.removed;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free && key != removed) {
                    action.accept(new MutableEntry(mc, i, key, vals[i]));
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
        public Map.Entry<Short, Double> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
                    index = nextI;
                    short[] keys = this.keys;
                    short free = this.free;
                    short removed = this.removed;
                    MutableEntry prev = next;
                    while (--nextI >= 0) {
                        short key;
                        if ((key = keys[nextI]) != free && key != removed) {
                            next = new MutableEntry(mc, nextI, key, vals[nextI]);
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
                    incrementModCount();
                    keys[index] = removed;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedEntryCursor implements ObjCursor<Map.Entry<Short, Double>> {
        final short[] keys;
        final long[] vals;
        final short free;
        final short removed;
        int expectedModCount;
        int index;
        short curKey;
        long curValue;

        SomeRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            short free = this.free = freeValue;
            this.removed = removedValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Short, Double>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            short[] keys = this.keys;
            long[] vals = this.vals;
            short free = this.free;
            short removed = this.removed;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free && key != removed) {
                    action.accept(new MutableEntry(mc, i, key, vals[i]));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public Map.Entry<Short, Double> elem() {
            short curKey;
            if ((curKey = this.curKey) != free) {
                return new MutableEntry(expectedModCount, index, curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                short[] keys = this.keys;
                short free = this.free;
                short removed = this.removed;
                for (int i = index - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
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
            short free;
            if (curKey != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    incrementModCount();
                    keys[index] = removed;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }




    class NoRemovedValueIterator implements DoubleIterator {
        final short[] keys;
        final long[] vals;
        final short free;
        final short removed;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        double next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            short[] keys = this.keys = set;
            long[] vals = this.vals = values;
            short free = this.free = freeValue;
            this.removed = removedValue;
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
                    short[] keys = this.keys;
                    short free = this.free;
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
            short[] keys = this.keys;
            long[] vals = this.vals;
            short free = this.free;
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
            short[] keys = this.keys;
            long[] vals = this.vals;
            short free = this.free;
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
                    incrementModCount();
                    keys[index] = removed;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedValueCursor implements DoubleCursor {
        final short[] keys;
        final long[] vals;
        final short free;
        final short removed;
        int expectedModCount;
        int index;
        short curKey;
        long curValue;

        NoRemovedValueCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            short free = this.free = freeValue;
            this.removed = removedValue;
            curKey = free;
        }

        @Override
        public void forEachForward(DoubleConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            short[] keys = this.keys;
            long[] vals = this.vals;
            short free = this.free;
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
                short[] keys = this.keys;
                short free = this.free;
                for (int i = index - 1; i >= 0; i--) {
                    short key;
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
            short free;
            if (curKey != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    incrementModCount();
                    keys[index] = removed;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedValueIterator implements DoubleIterator {
        final short[] keys;
        final long[] vals;
        final short free;
        final short removed;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        double next;

        SomeRemovedValueIterator(int mc) {
            expectedModCount = mc;
            short[] keys = this.keys = set;
            long[] vals = this.vals = values;
            short free = this.free = freeValue;
            short removed = this.removed = removedValue;
            int nextI = keys.length;
            while (--nextI >= 0) {
                short key;
                if ((key = keys[nextI]) != free && key != removed) {
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
                    short[] keys = this.keys;
                    short free = this.free;
                    short removed = this.removed;
                    double prev = next;
                    while (--nextI >= 0) {
                        short key;
                        if ((key = keys[nextI]) != free && key != removed) {
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
            short[] keys = this.keys;
            long[] vals = this.vals;
            short free = this.free;
            short removed = this.removed;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free && key != removed) {
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
            short[] keys = this.keys;
            long[] vals = this.vals;
            short free = this.free;
            short removed = this.removed;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free && key != removed) {
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
                    incrementModCount();
                    keys[index] = removed;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedValueCursor implements DoubleCursor {
        final short[] keys;
        final long[] vals;
        final short free;
        final short removed;
        int expectedModCount;
        int index;
        short curKey;
        long curValue;

        SomeRemovedValueCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            short free = this.free = freeValue;
            this.removed = removedValue;
            curKey = free;
        }

        @Override
        public void forEachForward(DoubleConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            short[] keys = this.keys;
            long[] vals = this.vals;
            short free = this.free;
            short removed = this.removed;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free && key != removed) {
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
                short[] keys = this.keys;
                short free = this.free;
                short removed = this.removed;
                for (int i = index - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
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
            short free;
            if (curKey != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    incrementModCount();
                    keys[index] = removed;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }



    class NoRemovedMapCursor implements ShortDoubleCursor {
        final short[] keys;
        final long[] vals;
        final short free;
        final short removed;
        int expectedModCount;
        int index;
        short curKey;
        long curValue;

        NoRemovedMapCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            short free = this.free = freeValue;
            this.removed = removedValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ShortDoubleConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            short[] keys = this.keys;
            long[] vals = this.vals;
            short free = this.free;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                short key;
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
        public short key() {
            short curKey;
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
                short[] keys = this.keys;
                short free = this.free;
                for (int i = index - 1; i >= 0; i--) {
                    short key;
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
            short free;
            if (curKey != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    incrementModCount();
                    keys[index] = removed;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }

    class SomeRemovedMapCursor implements ShortDoubleCursor {
        final short[] keys;
        final long[] vals;
        final short free;
        final short removed;
        int expectedModCount;
        int index;
        short curKey;
        long curValue;

        SomeRemovedMapCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            short free = this.free = freeValue;
            this.removed = removedValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ShortDoubleConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            short[] keys = this.keys;
            long[] vals = this.vals;
            short free = this.free;
            short removed = this.removed;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free && key != removed) {
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
        public short key() {
            short curKey;
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
                short[] keys = this.keys;
                short free = this.free;
                short removed = this.removed;
                for (int i = index - 1; i >= 0; i--) {
                    short key;
                    if ((key = keys[i]) != free && key != removed) {
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
            short free;
            if (curKey != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    incrementModCount();
                    keys[index] = removed;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }
}

