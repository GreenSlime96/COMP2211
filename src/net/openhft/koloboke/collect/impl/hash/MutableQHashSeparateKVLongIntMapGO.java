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
import net.openhft.koloboke.function.LongIntConsumer;
import net.openhft.koloboke.function.LongIntPredicate;
import net.openhft.koloboke.function.LongIntToIntFunction;
import java.util.function.LongToIntFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class MutableQHashSeparateKVLongIntMapGO
        extends MutableQHashSeparateKVLongIntMapSO {

    @Override
    final void copy(SeparateKVLongIntQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVLongIntQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.move(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }


    @Override
    public int defaultValue() {
        return 0;
    }

    @Override
    public boolean containsEntry(long key, int value) {
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
    public Integer get(Object key) {
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
    public int get(long key) {
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
    public Integer getOrDefault(Object key, Integer defaultValue) {
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
    public int getOrDefault(long key, int defaultValue) {
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
    public void forEach(BiConsumer<? super Long, ? super Integer> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long free = freeValue;
        long removed = removedValue;
        long[] keys = set;
        int[] vals = values;
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
    public void forEach(LongIntConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long free = freeValue;
        long removed = removedValue;
        long[] keys = set;
        int[] vals = values;
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
    public boolean forEachWhile(LongIntPredicate predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        long free = freeValue;
        long removed = removedValue;
        long[] keys = set;
        int[] vals = values;
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
    public LongIntCursor cursor() {
        int mc = modCount();
        if (!noRemoved())
            return new SomeRemovedMapCursor(mc);
        return new NoRemovedMapCursor(mc);
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonLongIntMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalLongIntMapOps m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        long free = freeValue;
        long removed = removedValue;
        long[] keys = set;
        int[] vals = values;
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
    public void reversePutAllTo(InternalLongIntMapOps m) {
        if (isEmpty())
            return;
        int mc = modCount();
        long free = freeValue;
        long removed = removedValue;
        long[] keys = set;
        int[] vals = values;
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
    public HashObjSet<Map.Entry<Long, Integer>> entrySet() {
        return new EntryView();
    }

    @Override
    @Nonnull
    public IntCollection values() {
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
        int[] vals = values;
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
        int[] vals = values;
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
        int[] vals = values;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        long[] newKeys = set;
        int capacity = newKeys.length;
        int[] newVals = values;
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
    public Integer put(Long key, Integer value) {
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return null;
        } else {
            // key is present
            int[] vals = values;
            int prevValue = vals[index];
            vals[index] = value;
            return prevValue;
        }
    }

    @Override
    public int put(long key, int value) {
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return defaultValue();
        } else {
            // key is present
            int[] vals = values;
            int prevValue = vals[index];
            vals[index] = value;
            return prevValue;
        }
    }

    @Override
    public Integer putIfAbsent(Long key, Integer value) {
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
    public int putIfAbsent(long key, int value) {
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
    public void justPut(long key, int value) {
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
    public Integer compute(Long key,
            BiFunction<? super Long, ? super Integer, ? extends Integer> remappingFunction) {
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
        int[] vals = values;
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
                Integer newValue = remappingFunction.apply(k, null);
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
            Integer newValue = remappingFunction.apply(k, null);
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
        Integer newValue = remappingFunction.apply(k, vals[index]);
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
    public int compute(long key, LongIntToIntFunction remappingFunction) {
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
        int[] vals = values;
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
                int newValue = remappingFunction.applyAsInt(key, defaultValue());
                incrementModCount();
                keys[firstRemoved] = key;
                vals[firstRemoved] = newValue;
                postRemovedSlotInsertHook();
                return newValue;
            }
            // key is absent, free slot
            int newValue = remappingFunction.applyAsInt(key, defaultValue());
            incrementModCount();
            keys[index] = key;
            vals[index] = newValue;
            postFreeSlotInsertHook();
            return newValue;
        }
        // key is present
        int newValue = remappingFunction.applyAsInt(key, vals[index]);
        vals[index] = newValue;
        return newValue;
    }


    @Override
    public Integer computeIfAbsent(Long key,
            Function<? super Long, ? extends Integer> mappingFunction) {
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
        int[] vals = values;
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
                Integer value = mappingFunction.apply(k);
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
            Integer value = mappingFunction.apply(k);
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
    public int computeIfAbsent(long key, LongToIntFunction mappingFunction) {
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
        int[] vals = values;
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
                int value = mappingFunction.applyAsInt(key);
                incrementModCount();
                keys[firstRemoved] = key;
                vals[firstRemoved] = value;
                postRemovedSlotInsertHook();
                return value;
            }
            // key is absent, free slot
            int value = mappingFunction.applyAsInt(key);
            incrementModCount();
            keys[index] = key;
            vals[index] = value;
            postFreeSlotInsertHook();
            return value;
        }
    }


    @Override
    public Integer computeIfPresent(Long key,
            BiFunction<? super Long, ? super Integer, ? extends Integer> remappingFunction) {
        long k = key;
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(k);
        if (index >= 0) {
            // key is present
            int[] vals = values;
            Integer newValue = remappingFunction.apply(k, vals[index]);
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
    public int computeIfPresent(long key, LongIntToIntFunction remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(key);
        if (index >= 0) {
            // key is present
            int[] vals = values;
            int newValue = remappingFunction.applyAsInt(key, vals[index]);
            vals[index] = newValue;
            return newValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Integer merge(Long key, Integer value,
            BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
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
        int[] vals = values;
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
        Integer newValue = remappingFunction.apply(vals[index], value);
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
    public int merge(long key, int value, IntBinaryOperator remappingFunction) {
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
        int[] vals = values;
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
        int newValue = remappingFunction.applyAsInt(vals[index], value);
        vals[index] = newValue;
        return newValue;
    }


    @Override
    public int addValue(long key, int value) {
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return value;
        } else {
            // key is present
            int[] vals = values;
            int newValue = vals[index] + value;
            vals[index] = newValue;
            return newValue;
        }
    }

    @Override
    public int addValue(long key, int addition, int defaultValue) {
        int value = defaultValue + addition;
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return value;
        } else {
            // key is present
            int[] vals = values;
            int newValue = vals[index] + addition;
            vals[index] = newValue;
            return newValue;
        }
    }


    @Override
    public void putAll(@Nonnull Map<? extends Long, ? extends Integer> m) {
        CommonLongIntMapOps.putAll(this, m);
    }


    @Override
    public Integer replace(Long key, Integer value) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            int[] vals = values;
            int oldValue = vals[index];
            vals[index] = value;
            return oldValue;
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public int replace(long key, int value) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            int[] vals = values;
            int oldValue = vals[index];
            vals[index] = value;
            return oldValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public boolean replace(Long key, Integer oldValue, Integer newValue) {
        return replace(key.longValue(),
                oldValue.intValue(),
                newValue.intValue());
    }

    @Override
    public boolean replace(long key, int oldValue, int newValue) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            int[] vals = values;
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
            BiFunction<? super Long, ? super Integer, ? extends Integer> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long free = freeValue;
        long removed = removedValue;
        long[] keys = set;
        int[] vals = values;
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
    public void replaceAll(LongIntToIntFunction function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long free = freeValue;
        long removed = removedValue;
        long[] keys = set;
        int[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free) {
                    vals[i] = function.applyAsInt(key, vals[i]);
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) != free && key != removed) {
                    vals[i] = function.applyAsInt(key, vals[i]);
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
    public Integer remove(Object key) {
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
            int val = values[index];
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
    public int remove(long key) {
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
            int val = values[index];
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
                ((Integer) value).intValue()
                );
    }

    @Override
    public boolean remove(long key, int value) {
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
    public boolean removeIf(LongIntPredicate filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long free = freeValue;
        long removed = removedValue;
        long[] keys = set;
        int[] vals = values;
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






    class EntryView extends AbstractSetView<Map.Entry<Long, Integer>>
            implements HashObjSet<Map.Entry<Long, Integer>>,
            InternalObjCollectionOps<Map.Entry<Long, Integer>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Long, Integer>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Long>defaultEquality()
                    ,
                    Equivalence.<Integer>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return MutableQHashSeparateKVLongIntMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return MutableQHashSeparateKVLongIntMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return MutableQHashSeparateKVLongIntMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Long, Integer> e = (Map.Entry<Long, Integer>) o;
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
            int[] vals = values;
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
            int[] vals = values;
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
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Long, Integer>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            int[] vals = values;
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
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Long, Integer>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            int[] vals = values;
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
        public ObjIterator<Map.Entry<Long, Integer>> iterator() {
            int mc = modCount();
            if (!noRemoved())
                return new SomeRemovedEntryIterator(mc);
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Long, Integer>> cursor() {
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
            int[] vals = values;
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
            int[] vals = values;
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
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Long, Integer>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            int[] vals = values;
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
            return MutableQHashSeparateKVLongIntMapGO.this.hashCode();
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
            int[] vals = values;
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
            return MutableQHashSeparateKVLongIntMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Long, Integer> e = (Map.Entry<Long, Integer>) o;
                long key = e.getKey();
                int value = e.getValue();
                return MutableQHashSeparateKVLongIntMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Long, Integer>> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            int[] vals = values;
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
            int[] vals = values;
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
            int[] vals = values;
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
            MutableQHashSeparateKVLongIntMapGO.this.clear();
        }
    }


    abstract class LongIntEntry extends AbstractEntry<Long, Integer> {

        abstract long key();

        @Override
        public final Long getKey() {
            return key();
        }

        abstract int value();

        @Override
        public final Integer getValue() {
            return value();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object o) {
            Map.Entry e2;
            long k2;
            int v2;
            try {
                e2 = (Map.Entry) o;
                k2 = (Long) e2.getKey();
                v2 = (Integer) e2.getValue();
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


    class MutableEntry extends LongIntEntry {
        final int modCount;
        private final int index;
        final long key;
        private int value;

        MutableEntry(int modCount, int index, long key, int value) {
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
        public int value() {
            return value;
        }

        @Override
        public Integer setValue(Integer newValue) {
            if (modCount != modCount())
                throw new IllegalStateException();
            int oldValue = value;
            int unwrappedNewValue = newValue;
            value = unwrappedNewValue;
            updateValueInTable(unwrappedNewValue);
            return oldValue;
        }

        void updateValueInTable(int newValue) {
            values[index] = newValue;
        }
    }



    class ReusableEntry extends LongIntEntry {
        private long key;
        private int value;

        ReusableEntry with(long key, int value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public long key() {
            return key;
        }

        @Override
        public int value() {
            return value;
        }
    }


    class ValueView extends AbstractIntValueView {


        @Override
        public int size() {
            return MutableQHashSeparateKVLongIntMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return MutableQHashSeparateKVLongIntMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return MutableQHashSeparateKVLongIntMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(int v) {
            return MutableQHashSeparateKVLongIntMapGO.this.containsValue(v);
        }



        @Override
        public void forEach(Consumer<? super Integer> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            int[] vals = values;
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
        public void forEach(IntConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            int[] vals = values;
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
        public boolean forEachWhile(IntPredicate predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            int[] vals = values;
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
        public boolean allContainingIn(IntCollection c) {
            if (isEmpty())
                return true;
            boolean containsAll = true;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            int[] vals = values;
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
        public boolean reverseAddAllTo(IntCollection c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            int[] vals = values;
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
        public boolean reverseRemoveAllFrom(IntSet s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            int[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        changed |= s.removeInt(vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) != free && key != removed) {
                        changed |= s.removeInt(vals[i]);
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }



        @Override
        @Nonnull
        public IntIterator iterator() {
            int mc = modCount();
            if (!noRemoved())
                return new SomeRemovedValueIterator(mc);
            return new NoRemovedValueIterator(mc);
        }

        @Nonnull
        @Override
        public IntCursor cursor() {
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
            int[] vals = values;
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
            int[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != free) {
                        a[resultIndex++] = (T) Integer.valueOf(vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    long key;
                    if ((key = keys[i]) != free && key != removed) {
                        a[resultIndex++] = (T) Integer.valueOf(vals[i]);
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
        public int[] toIntArray() {
            int size = size();
            int[] result = new int[size];
            if (size == 0)
                return result;
            int resultIndex = 0;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            int[] vals = values;
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
        public int[] toArray(int[] a) {
            int size = size();
            if (a.length < size)
                a = new int[size];
            if (size == 0) {
                if (a.length > 0)
                    a[0] = 0;
                return a;
            }
            int resultIndex = 0;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            int[] vals = values;
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
                a[resultIndex] = 0;
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
            int[] vals = values;
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
            return removeInt(( Integer ) o);
        }

        @Override
        public boolean removeInt(int v) {
            return removeValue(v);
        }



        @Override
        public void clear() {
            MutableQHashSeparateKVLongIntMapGO.this.clear();
        }

        
        public boolean removeIf(Predicate<? super Integer> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            int[] vals = values;
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
        public boolean removeIf(IntPredicate filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            int[] vals = values;
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
            if (c instanceof IntCollection)
                return removeAll((IntCollection) c);
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            int[] vals = values;
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

        private boolean removeAll(IntCollection c) {
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long free = freeValue;
            long removed = removedValue;
            long[] keys = set;
            int[] vals = values;
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
            if (c instanceof IntCollection)
                return retainAll((IntCollection) c);
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
            int[] vals = values;
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

        private boolean retainAll(IntCollection c) {
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
            int[] vals = values;
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Long, Integer>> {
        final long[] keys;
        final int[] vals;
        final long free;
        final long removed;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        MutableEntry next;

        NoRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            long[] keys = this.keys = set;
            int[] vals = this.vals = values;
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
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Long, Integer>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            int[] vals = this.vals;
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
        public Map.Entry<Long, Integer> next() {
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


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Long, Integer>> {
        final long[] keys;
        final int[] vals;
        final long free;
        final long removed;
        int expectedModCount;
        int index;
        long curKey;
        int curValue;

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
        public void forEachForward(Consumer<? super Map.Entry<Long, Integer>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            int[] vals = this.vals;
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
        public Map.Entry<Long, Integer> elem() {
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


    class SomeRemovedEntryIterator implements ObjIterator<Map.Entry<Long, Integer>> {
        final long[] keys;
        final int[] vals;
        final long free;
        final long removed;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        MutableEntry next;

        SomeRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            long[] keys = this.keys = set;
            int[] vals = this.vals = values;
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
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Long, Integer>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            int[] vals = this.vals;
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
        public Map.Entry<Long, Integer> next() {
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


    class SomeRemovedEntryCursor implements ObjCursor<Map.Entry<Long, Integer>> {
        final long[] keys;
        final int[] vals;
        final long free;
        final long removed;
        int expectedModCount;
        int index;
        long curKey;
        int curValue;

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
        public void forEachForward(Consumer<? super Map.Entry<Long, Integer>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            int[] vals = this.vals;
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
        public Map.Entry<Long, Integer> elem() {
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




    class NoRemovedValueIterator implements IntIterator {
        final long[] keys;
        final int[] vals;
        final long free;
        final long removed;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        int next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            long[] keys = this.keys = set;
            int[] vals = this.vals = values;
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
        public int nextInt() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                if (expectedModCount == modCount()) {
                    index = nextI;
                    long[] keys = this.keys;
                    long free = this.free;
                    int prev = next;
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
        public void forEachRemaining(Consumer<? super Integer> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            int[] vals = this.vals;
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
        public void forEachRemaining(IntConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            int[] vals = this.vals;
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
        public Integer next() {
            return nextInt();
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


    class NoRemovedValueCursor implements IntCursor {
        final long[] keys;
        final int[] vals;
        final long free;
        final long removed;
        int expectedModCount;
        int index;
        long curKey;
        int curValue;

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
        public void forEachForward(IntConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            int[] vals = this.vals;
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
        public int elem() {
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


    class SomeRemovedValueIterator implements IntIterator {
        final long[] keys;
        final int[] vals;
        final long free;
        final long removed;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        int next;

        SomeRemovedValueIterator(int mc) {
            expectedModCount = mc;
            long[] keys = this.keys = set;
            int[] vals = this.vals = values;
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
        public int nextInt() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                if (expectedModCount == modCount()) {
                    index = nextI;
                    long[] keys = this.keys;
                    long free = this.free;
                    long removed = this.removed;
                    int prev = next;
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
        public void forEachRemaining(Consumer<? super Integer> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            int[] vals = this.vals;
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
        public void forEachRemaining(IntConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            int[] vals = this.vals;
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
        public Integer next() {
            return nextInt();
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


    class SomeRemovedValueCursor implements IntCursor {
        final long[] keys;
        final int[] vals;
        final long free;
        final long removed;
        int expectedModCount;
        int index;
        long curKey;
        int curValue;

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
        public void forEachForward(IntConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            int[] vals = this.vals;
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
        public int elem() {
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



    class NoRemovedMapCursor implements LongIntCursor {
        final long[] keys;
        final int[] vals;
        final long free;
        final long removed;
        int expectedModCount;
        int index;
        long curKey;
        int curValue;

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
        public void forEachForward(LongIntConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            int[] vals = this.vals;
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
        public int value() {
            if (curKey != free) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(int value) {
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

    class SomeRemovedMapCursor implements LongIntCursor {
        final long[] keys;
        final int[] vals;
        final long free;
        final long removed;
        int expectedModCount;
        int index;
        long curKey;
        int curValue;

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
        public void forEachForward(LongIntConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            int[] vals = this.vals;
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
        public int value() {
            if (curKey != free) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(int value) {
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
