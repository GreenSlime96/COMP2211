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
import net.openhft.koloboke.function.LongShortConsumer;
import net.openhft.koloboke.function.LongShortPredicate;
import net.openhft.koloboke.function.LongShortToShortFunction;
import net.openhft.koloboke.function.LongToShortFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.openhft.koloboke.function.ShortBinaryOperator;
import net.openhft.koloboke.function.ShortConsumer;
import net.openhft.koloboke.function.ShortPredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class MutableQHashSeparateKVLongShortMapGO
        extends MutableQHashSeparateKVLongShortMapSO {

    @Override
    final void copy(SeparateKVLongShortQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVLongShortQHash hash) {
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
        int index = index((Long) key);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return null;
        }
    }

    

    @Override
    public short get(long key) {
        int index = index(key);
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
        int index = index((Long) key);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public short getOrDefault(long key, short defaultValue) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public void forEach(BiConsumer<? super Long, ? super Short> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long free = freeValue;
        long removed = removedValue;
        long[] keys = set;
        short[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free) {
                    action.accept(key, vals[i]);
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free && key != removed) {
                    action.accept(key, vals[i]);
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void forEach(LongShortConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long free = freeValue;
        long removed = removedValue;
        long[] keys = set;
        short[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free) {
                    action.accept(key, vals[i]);
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free && key != removed) {
                    action.accept(key, vals[i]);
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public boolean forEachWhile(LongShortPredicate predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        long free = freeValue;
        long removed = removedValue;
        long[] keys = set;
        short[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free) {
                    if (!predicate.test(key, vals[i])) {
                        terminated = true;
                        break;
                    }
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free && key != removed) {
                    if (!predicate.test(key, vals[i])) {
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
    public LongShortCursor cursor() {
        int mc = modCount();
        if (!noRemoved())
            return new SomeRemovedMapCursor(mc);
        return new NoRemovedMapCursor(mc);
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonLongShortMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalLongShortMapOps m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        long free = freeValue;
        long removed = removedValue;
        long[] keys = set;
        short[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free) {
                    if (!m.containsEntry(key, vals[i])) {
                        containsAll = false;
                        break;
                    }
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
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
    public void reversePutAllTo(InternalLongShortMapOps m) {
        if (isEmpty())
            return;
        int mc = modCount();
        long free = freeValue;
        long removed = removedValue;
        long[] keys = set;
        short[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free) {
                    m.justPut(key, vals[i]);
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
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
    public HashObjSet<Map.Entry<Long, Short>> entrySet() {
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
        long free = freeValue;
        long removed = removedValue;
        long[] keys = set;
        short[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free) {
                    hashCode += ((int) (key ^ (key >>> 32))) ^ vals[i];
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free && key != removed) {
                    hashCode += ((int) (key ^ (key >>> 32))) ^ vals[i];
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
        long free = freeValue;
        long removed = removedValue;
        long[] keys = set;
        short[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free) {
                    sb.append(' ');
                    sb.append(key);
                    sb.append('=');
                    sb.append(vals[i]);
                    sb.append(',');
                    if (++elementCount == 8) {
                        int expectedLength = sb.length() * (size() / 8);
                        sb.ensureCapacity(expectedLength + (expectedLength / 2));
                    }
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free && key != removed) {
                    sb.append(' ');
                    sb.append(key);
                    sb.append('=');
                    sb.append(vals[i]);
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
        long free = freeValue;
        long removed = removedValue;
        long[] keys = set;
        short[] vals = values;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        long[] newKeys = set;
        int capacity = newKeys.length;
        short[] newVals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free) {
                    int index;
                    if (newKeys[index = SeparateKVLongKeyMixing.mix(key) % capacity] != free) {
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
                long key;
                if ((key = keys[i]) != free && key != removed) {
                    int index;
                    if (newKeys[index = SeparateKVLongKeyMixing.mix(key) % capacity] != free) {
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
    public Short put(Long key, Short value) {
        int index = insert(key, value);
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
    public short put(long key, short value) {
        int index = insert(key, value);
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
    public Short putIfAbsent(Long key, Short value) {
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return null;
        } else {
            // key is present
            return values[index];
        }
    }

    @Override
    public short putIfAbsent(long key, short value) {
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return defaultValue();
        } else {
            // key is present
            return values[index];
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
    public Short compute(Long key,
            BiFunction<? super Long, ? super Short, ? extends Short> remappingFunction) {
        long k = key;
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long free;
        long removed = removedValue;
        if (k == (free = freeValue)) {
            free = changeFree();
        } else if (k == removed) {
            removed = changeRemoved();
        }
        long[] keys = set;
        short[] vals = values;
        int capacity, index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVLongKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
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
                Short newValue = remappingFunction.apply(k, null);
                if (newValue != null) {
                    incrementModCount();
                    keys[firstRemoved] = k;
                    vals[firstRemoved] = newValue;
                    postRemovedSlotInsertHook();
                    return newValue;
                } else {
                    return null;
                }
            }
            // key is absent, free slot
            Short newValue = remappingFunction.apply(k, null);
            if (newValue != null) {
                incrementModCount();
                keys[index] = k;
                vals[index] = newValue;
                postFreeSlotInsertHook();
                return newValue;
            } else {
                return null;
            }
        }
        // key is present
        Short newValue = remappingFunction.apply(k, vals[index]);
        if (newValue != null) {
            vals[index] = newValue;
            return newValue;
        } else {
            incrementModCount();
            keys[index] = removed;
            postRemoveHook();
            return null;
        }
    }


    @Override
    public short compute(long key, LongShortToShortFunction remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long free;
        long removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        long[] keys = set;
        short[] vals = values;
        int capacity, index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVLongKeyMixing.mix(key) % (capacity = keys.length)]) != key) {
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
                short newValue = remappingFunction.applyAsShort(key, defaultValue());
                incrementModCount();
                keys[firstRemoved] = key;
                vals[firstRemoved] = newValue;
                postRemovedSlotInsertHook();
                return newValue;
            }
            // key is absent, free slot
            short newValue = remappingFunction.applyAsShort(key, defaultValue());
            incrementModCount();
            keys[index] = key;
            vals[index] = newValue;
            postFreeSlotInsertHook();
            return newValue;
        }
        // key is present
        short newValue = remappingFunction.applyAsShort(key, vals[index]);
        vals[index] = newValue;
        return newValue;
    }


    @Override
    public Short computeIfAbsent(Long key,
            Function<? super Long, ? extends Short> mappingFunction) {
        long k = key;
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        long free;
        long removed = removedValue;
        if (k == (free = freeValue)) {
            free = changeFree();
        } else if (k == removed) {
            removed = changeRemoved();
        }
        long[] keys = set;
        short[] vals = values;
        int capacity, index;
        long cur;
        if ((cur = keys[index = SeparateKVLongKeyMixing.mix(k) % (capacity = keys.length)]) == k) {
            // key is present
            return vals[index];
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
                                return vals[bIndex];
                            } else if (cur == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == k) {
                                // key is present
                                return vals[fIndex];
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
                            return vals[bIndex];
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
                            return vals[fIndex];
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
                Short value = mappingFunction.apply(k);
                if (value != null) {
                    incrementModCount();
                    keys[firstRemoved] = k;
                    vals[firstRemoved] = value;
                    postRemovedSlotInsertHook();
                    return value;
                } else {
                    return null;
                }
            }
            // key is absent, free slot
            Short value = mappingFunction.apply(k);
            if (value != null) {
                incrementModCount();
                keys[index] = k;
                vals[index] = value;
                postFreeSlotInsertHook();
                return value;
            } else {
                return null;
            }
        }
    }


    @Override
    public short computeIfAbsent(long key, LongToShortFunction mappingFunction) {
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        long free;
        long removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        long[] keys = set;
        short[] vals = values;
        int capacity, index;
        long cur;
        if ((cur = keys[index = SeparateKVLongKeyMixing.mix(key) % (capacity = keys.length)]) == key) {
            // key is present
            return vals[index];
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
                                return vals[bIndex];
                            } else if (cur == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == key) {
                                // key is present
                                return vals[fIndex];
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
                            return vals[bIndex];
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
                            return vals[fIndex];
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
                short value = mappingFunction.applyAsShort(key);
                incrementModCount();
                keys[firstRemoved] = key;
                vals[firstRemoved] = value;
                postRemovedSlotInsertHook();
                return value;
            }
            // key is absent, free slot
            short value = mappingFunction.applyAsShort(key);
            incrementModCount();
            keys[index] = key;
            vals[index] = value;
            postFreeSlotInsertHook();
            return value;
        }
    }


    @Override
    public Short computeIfPresent(Long key,
            BiFunction<? super Long, ? super Short, ? extends Short> remappingFunction) {
        long k = key;
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(k);
        if (index >= 0) {
            // key is present
            short[] vals = values;
            Short newValue = remappingFunction.apply(k, vals[index]);
            if (newValue != null) {
                vals[index] = newValue;
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
    public short computeIfPresent(long key, LongShortToShortFunction remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(key);
        if (index >= 0) {
            // key is present
            short[] vals = values;
            short newValue = remappingFunction.applyAsShort(key, vals[index]);
            vals[index] = newValue;
            return newValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Short merge(Long key, Short value,
            BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        long k = key;
        if (value == null)
            throw new java.lang.NullPointerException();
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long free;
        long removed = removedValue;
        if (k == (free = freeValue)) {
            free = changeFree();
        } else if (k == removed) {
            removed = changeRemoved();
        }
        long[] keys = set;
        short[] vals = values;
        int capacity, index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVLongKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
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
                vals[firstRemoved] = value;
                postRemovedSlotInsertHook();
                return value;
            }
            // key is absent, free slot
            incrementModCount();
            keys[index] = k;
            vals[index] = value;
            postFreeSlotInsertHook();
            return value;
        }
        // key is present
        Short newValue = remappingFunction.apply(vals[index], value);
        if (newValue != null) {
            vals[index] = newValue;
            return newValue;
        } else {
            incrementModCount();
            keys[index] = removed;
            postRemoveHook();
            return null;
        }
    }


    @Override
    public short merge(long key, short value, ShortBinaryOperator remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long free;
        long removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        long[] keys = set;
        short[] vals = values;
        int capacity, index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVLongKeyMixing.mix(key) % (capacity = keys.length)]) != key) {
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
                vals[firstRemoved] = value;
                postRemovedSlotInsertHook();
                return value;
            }
            // key is absent, free slot
            incrementModCount();
            keys[index] = key;
            vals[index] = value;
            postFreeSlotInsertHook();
            return value;
        }
        // key is present
        short newValue = remappingFunction.applyAsShort(vals[index], value);
        vals[index] = newValue;
        return newValue;
    }


    @Override
    public short addValue(long key, short value) {
        int index = insert(key, value);
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
    public short addValue(long key, short addition, short defaultValue) {
        short value = (short) (defaultValue + addition);
        int index = insert(key, value);
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
    public void putAll(@Nonnull Map<? extends Long, ? extends Short> m) {
        CommonLongShortMapOps.putAll(this, m);
    }


    @Override
    public Short replace(Long key, Short value) {
        int index = index(key);
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
    public short replace(long key, short value) {
        int index = index(key);
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
    public boolean replace(Long key, Short oldValue, Short newValue) {
        return replace(key.longValue(),
                oldValue.shortValue(),
                newValue.shortValue());
    }

    @Override
    public boolean replace(long key, short oldValue, short newValue) {
        int index = index(key);
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
            BiFunction<? super Long, ? super Short, ? extends Short> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long free = freeValue;
        long removed = removedValue;
        long[] keys = set;
        short[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free) {
                    vals[i] = function.apply(key, vals[i]);
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free && key != removed) {
                    vals[i] = function.apply(key, vals[i]);
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void replaceAll(LongShortToShortFunction function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long free = freeValue;
        long removed = removedValue;
        long[] keys = set;
        short[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free) {
                    vals[i] = function.applyAsShort(key, vals[i]);
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free && key != removed) {
                    vals[i] = function.applyAsShort(key, vals[i]);
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
    public Short remove(Object key) {
        long k = (Long) key;
        long free, removed;
        if (k != (free = freeValue) && k != (removed = removedValue)) {
            long[] keys = set;
            int capacity, index;
            long cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVLongKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
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
            short val = values[index];
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
    public boolean justRemove(long key) {
        long free, removed;
        if (key != (free = freeValue) && key != (removed = removedValue)) {
            long[] keys = set;
            int capacity, index;
            long cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVLongKeyMixing.mix(key) % (capacity = keys.length)]) != key) {
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
    public short remove(long key) {
        long free, removed;
        if (key != (free = freeValue) && key != (removed = removedValue)) {
            long[] keys = set;
            int capacity, index;
            long cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVLongKeyMixing.mix(key) % (capacity = keys.length)]) != key) {
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
            short val = values[index];
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
        return remove(((Long) key).longValue(),
                ((Short) value).shortValue()
                );
    }

    @Override
    public boolean remove(long key, short value) {
        long free, removed;
        if (key != (free = freeValue) && key != (removed = removedValue)) {
            long[] keys = set;
            int capacity, index;
            long cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVLongKeyMixing.mix(key) % (capacity = keys.length)]) != key) {
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
            if (values[index] == value) {
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
    public boolean removeIf(LongShortPredicate filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long free = freeValue;
        long removed = removedValue;
        long[] keys = set;
        short[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free) {
                    if (filter.test(key, vals[i])) {
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
                long key;
                if ((key = keys[i]) != free && key != removed) {
                    if (filter.test(key, vals[i])) {
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






    class EntryView extends AbstractSetView<Map.Entry<Long, Short>>
            implements HashObjSet<Map.Entry<Long, Short>>,
            InternalObjCollectionOps<Map.Entry<Long, Short>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Long, Short>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Long>defaultEquality()
                    ,
                    Equivalence.<Short>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return MutableQHashSeparateKVLongShortMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return MutableQHashSeparateKVLongShortMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return MutableQHashSeparateKVLongShortMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Long, Short> e = (Map.Entry<Long, Short>) o;
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
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) != free) {
                        result[resultIndex++] = new MutableEntry(mc, i, key, vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
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
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) != free) {
                        a[resultIndex++] = (T) new MutableEntry(mc, i, key, vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
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
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Long, Short>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) != free) {
                        action.accept(new MutableEntry(mc, i, key, vals[i]));
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) != free && key != removed) {
                        action.accept(new MutableEntry(mc, i, key, vals[i]));
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Long, Short>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) != free) {
                        if (!predicate.test(new MutableEntry(mc, i, key, vals[i]))) {
                            terminated = true;
                            break;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
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
        public ObjIterator<Map.Entry<Long, Short>> iterator() {
            int mc = modCount();
            if (!noRemoved())
                return new SomeRemovedEntryIterator(mc);
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Long, Short>> cursor() {
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
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) != free) {
                        if (!c.contains(e.with(key, vals[i]))) {
                            containsAll = false;
                            break;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
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
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) != free) {
                        changed |= s.remove(e.with(key, vals[i]));
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
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
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Long, Short>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) != free) {
                        changed |= c.add(new MutableEntry(mc, i, key, vals[i]));
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
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
            return MutableQHashSeparateKVLongShortMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) != free) {
                        sb.append(' ');
                        sb.append(key);
                        sb.append('=');
                        sb.append(vals[i]);
                        sb.append(',');
                        if (++elementCount == 8) {
                            int expectedLength = sb.length() * (size() / 8);
                            sb.ensureCapacity(expectedLength + (expectedLength / 2));
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) != free && key != removed) {
                        sb.append(' ');
                        sb.append(key);
                        sb.append('=');
                        sb.append(vals[i]);
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
            return MutableQHashSeparateKVLongShortMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Long, Short> e = (Map.Entry<Long, Short>) o;
                long key = e.getKey();
                short value = e.getValue();
                return MutableQHashSeparateKVLongShortMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Long, Short>> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
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
                    long key;
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
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
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
                    long key;
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
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
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
                    long key;
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
            MutableQHashSeparateKVLongShortMapGO.this.clear();
        }
    }


    abstract class LongShortEntry extends AbstractEntry<Long, Short> {

        abstract long key();

        @Override
        public final Long getKey() {
            return key();
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
                k2 = (Long) e2.getKey();
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


    class MutableEntry extends LongShortEntry {
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



    class ReusableEntry extends LongShortEntry {
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
            return MutableQHashSeparateKVLongShortMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return MutableQHashSeparateKVLongShortMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return MutableQHashSeparateKVLongShortMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(short v) {
            return MutableQHashSeparateKVLongShortMapGO.this.containsValue(v);
        }



        @Override
        public void forEach(Consumer<? super Short> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        action.accept(vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) != free && key != removed) {
                        action.accept(vals[i]);
                    }
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
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        action.accept(vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) != free && key != removed) {
                        action.accept(vals[i]);
                    }
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
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        if (!predicate.test(vals[i])) {
                            terminated = true;
                            break;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) != free && key != removed) {
                        if (!predicate.test(vals[i])) {
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
        public boolean allContainingIn(ShortCollection c) {
            if (isEmpty())
                return true;
            boolean containsAll = true;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
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
                    long key;
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
        public boolean reverseAddAllTo(ShortCollection c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        changed |= c.add(vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
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
        public boolean reverseRemoveAllFrom(ShortSet s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        changed |= s.removeShort(vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) != free && key != removed) {
                        changed |= s.removeShort(vals[i]);
                    }
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
            if (!noRemoved())
                return new SomeRemovedValueIterator(mc);
            return new NoRemovedValueIterator(mc);
        }

        @Nonnull
        @Override
        public ShortCursor cursor() {
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
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        result[resultIndex++] = vals[i];
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) != free && key != removed) {
                        result[resultIndex++] = vals[i];
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
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        a[resultIndex++] = (T) Short.valueOf(vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) != free && key != removed) {
                        a[resultIndex++] = (T) Short.valueOf(vals[i]);
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
        public short[] toShortArray() {
            int size = size();
            short[] result = new short[size];
            if (size == 0)
                return result;
            int resultIndex = 0;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        result[resultIndex++] = vals[i];
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) != free && key != removed) {
                        result[resultIndex++] = vals[i];
                    }
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
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        a[resultIndex++] = vals[i];
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) != free && key != removed) {
                        a[resultIndex++] = vals[i];
                    }
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
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        sb.append(' ').append(vals[i]).append(',');
                        if (++elementCount == 8) {
                            int expectedLength = sb.length() * (size() / 8);
                            sb.ensureCapacity(expectedLength + (expectedLength / 2));
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) != free && key != removed) {
                        sb.append(' ').append(vals[i]).append(',');
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
            return removeShort(( Short ) o);
        }

        @Override
        public boolean removeShort(short v) {
            return removeValue(v);
        }



        @Override
        public void clear() {
            MutableQHashSeparateKVLongShortMapGO.this.clear();
        }

        
        public boolean removeIf(Predicate<? super Short> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        if (filter.test(vals[i])) {
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
                    long key;
                    if ((key = keys[i]) != free && key != removed) {
                        if (filter.test(vals[i])) {
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
        public boolean removeIf(ShortPredicate filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        if (filter.test(vals[i])) {
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
                    long key;
                    if ((key = keys[i]) != free && key != removed) {
                        if (filter.test(vals[i])) {
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
            if (c instanceof ShortCollection)
                return removeAll((ShortCollection) c);
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
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
                    long key;
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

        private boolean removeAll(ShortCollection c) {
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
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
                    long key;
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
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
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
                    long key;
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
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            short[] vals = values;
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
                    long key;
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Long, Short>> {
        final long[] keys;
        final short[] vals;
        final long free;
        final long removed;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        MutableEntry next;

        NoRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            long[] keys = this.keys = set;
            short[] vals = this.vals = values;
            long free = this.free = freeValue;
            this.removed = removedValue;
            int nextI = keys.length;
            while (--nextI >= 0) {
                long key;
                if ((key = keys[nextI]) != free) {
                    next = new MutableEntry(mc, nextI, key, vals[nextI]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Long, Short>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            short[] vals = this.vals;
            long free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                long key;
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
        public Map.Entry<Long, Short> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
                    index = nextI;
                    long[] keys = this.keys;
                    long free = this.free;
                    MutableEntry prev = next;
                    while (--nextI >= 0) {
                        long key;
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


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Long, Short>> {
        final long[] keys;
        final short[] vals;
        final long free;
        final long removed;
        int expectedModCount;
        int index;
        long curKey;
        short curValue;

        NoRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            long free = this.free = freeValue;
            this.removed = removedValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Long, Short>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            short[] vals = this.vals;
            long free = this.free;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                long key;
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
        public Map.Entry<Long, Short> elem() {
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
            } else {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            long free;
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


    class SomeRemovedEntryIterator implements ObjIterator<Map.Entry<Long, Short>> {
        final long[] keys;
        final short[] vals;
        final long free;
        final long removed;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        MutableEntry next;

        SomeRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            long[] keys = this.keys = set;
            short[] vals = this.vals = values;
            long free = this.free = freeValue;
            long removed = this.removed = removedValue;
            int nextI = keys.length;
            while (--nextI >= 0) {
                long key;
                if ((key = keys[nextI]) != free && key != removed) {
                    next = new MutableEntry(mc, nextI, key, vals[nextI]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Long, Short>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            short[] vals = this.vals;
            long free = this.free;
            long removed = this.removed;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                long key;
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
        public Map.Entry<Long, Short> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
                    index = nextI;
                    long[] keys = this.keys;
                    long free = this.free;
                    long removed = this.removed;
                    MutableEntry prev = next;
                    while (--nextI >= 0) {
                        long key;
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


    class SomeRemovedEntryCursor implements ObjCursor<Map.Entry<Long, Short>> {
        final long[] keys;
        final short[] vals;
        final long free;
        final long removed;
        int expectedModCount;
        int index;
        long curKey;
        short curValue;

        SomeRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            long free = this.free = freeValue;
            this.removed = removedValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Long, Short>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            short[] vals = this.vals;
            long free = this.free;
            long removed = this.removed;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                long key;
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
        public Map.Entry<Long, Short> elem() {
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
                long[] keys = this.keys;
                long free = this.free;
                long removed = this.removed;
                for (int i = index - 1; i >= 0; i--) {
                    long key;
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
            long free;
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




    class NoRemovedValueIterator implements ShortIterator {
        final long[] keys;
        final short[] vals;
        final long free;
        final long removed;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        short next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            long[] keys = this.keys = set;
            short[] vals = this.vals = values;
            long free = this.free = freeValue;
            this.removed = removedValue;
            int nextI = keys.length;
            while (--nextI >= 0) {
                if (keys[nextI] != free) {
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
                    long free = this.free;
                    short prev = next;
                    while (--nextI >= 0) {
                        if (keys[nextI] != free) {
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
            long free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if (keys[i] != free) {
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
            long free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if (keys[i] != free) {
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


    class NoRemovedValueCursor implements ShortCursor {
        final long[] keys;
        final short[] vals;
        final long free;
        final long removed;
        int expectedModCount;
        int index;
        long curKey;
        short curValue;

        NoRemovedValueCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            long free = this.free = freeValue;
            this.removed = removedValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ShortConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            short[] vals = this.vals;
            long free = this.free;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                if (keys[i] != free) {
                    action.accept(vals[i]);
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public short elem() {
            if (curKey != free) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
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
            } else {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            long free;
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


    class SomeRemovedValueIterator implements ShortIterator {
        final long[] keys;
        final short[] vals;
        final long free;
        final long removed;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        short next;

        SomeRemovedValueIterator(int mc) {
            expectedModCount = mc;
            long[] keys = this.keys = set;
            short[] vals = this.vals = values;
            long free = this.free = freeValue;
            long removed = this.removed = removedValue;
            int nextI = keys.length;
            while (--nextI >= 0) {
                long key;
                if ((key = keys[nextI]) != free && key != removed) {
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
                    long free = this.free;
                    long removed = this.removed;
                    short prev = next;
                    while (--nextI >= 0) {
                        long key;
                        if ((key = keys[nextI]) != free && key != removed) {
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
            long free = this.free;
            long removed = this.removed;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free && key != removed) {
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
            long free = this.free;
            long removed = this.removed;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free && key != removed) {
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


    class SomeRemovedValueCursor implements ShortCursor {
        final long[] keys;
        final short[] vals;
        final long free;
        final long removed;
        int expectedModCount;
        int index;
        long curKey;
        short curValue;

        SomeRemovedValueCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            long free = this.free = freeValue;
            this.removed = removedValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ShortConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            short[] vals = this.vals;
            long free = this.free;
            long removed = this.removed;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free && key != removed) {
                    action.accept(vals[i]);
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public short elem() {
            if (curKey != free) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                long[] keys = this.keys;
                long free = this.free;
                long removed = this.removed;
                for (int i = index - 1; i >= 0; i--) {
                    long key;
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
            long free;
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



    class NoRemovedMapCursor implements LongShortCursor {
        final long[] keys;
        final short[] vals;
        final long free;
        final long removed;
        int expectedModCount;
        int index;
        long curKey;
        short curValue;

        NoRemovedMapCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            long free = this.free = freeValue;
            this.removed = removedValue;
            curKey = free;
        }

        @Override
        public void forEachForward(LongShortConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            short[] vals = this.vals;
            long free = this.free;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free) {
                    action.accept(key, vals[i]);
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
        public short value() {
            if (curKey != free) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(short value) {
            if (curKey != free) {
                if (expectedModCount == modCount()) {
                    vals[index] = value;
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
            } else {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            long free;
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

    class SomeRemovedMapCursor implements LongShortCursor {
        final long[] keys;
        final short[] vals;
        final long free;
        final long removed;
        int expectedModCount;
        int index;
        long curKey;
        short curValue;

        SomeRemovedMapCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            long free = this.free = freeValue;
            this.removed = removedValue;
            curKey = free;
        }

        @Override
        public void forEachForward(LongShortConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            short[] vals = this.vals;
            long free = this.free;
            long removed = this.removed;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free && key != removed) {
                    action.accept(key, vals[i]);
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
        public short value() {
            if (curKey != free) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(short value) {
            if (curKey != free) {
                if (expectedModCount == modCount()) {
                    vals[index] = value;
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
                long free = this.free;
                long removed = this.removed;
                for (int i = index - 1; i >= 0; i--) {
                    long key;
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
            long free;
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

