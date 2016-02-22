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
import net.openhft.koloboke.function.DoubleObjConsumer;
import net.openhft.koloboke.function.DoubleObjPredicate;
import net.openhft.koloboke.function.DoubleObjFunction;
import java.util.function.DoubleFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import java.util.*;


public class MutableQHashSeparateKVDoubleObjMapGO<V>
        extends MutableQHashSeparateKVDoubleObjMapSO<V> {

    @Override
    final void copy(SeparateKVDoubleObjQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVDoubleObjQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.move(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    @Nonnull
    public Equivalence<V> valueEquivalence() {
        return Equivalence.defaultEquality();
    }


    @Override
    public boolean containsEntry(double key, Object value) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            return nullableValueEquals(values[index], (V) value);
        } else {
            // key is absent
            return false;
        }
    }

    @Override
    public boolean containsEntry(long key, Object value) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return nullableValueEquals(values[index], (V) value);
        } else {
            // key is absent
            return false;
        }
    }

    @Override
    public V get(Object key) {
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
    public V get(double key) {
        long k = Double.doubleToLongBits(key);
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
    public V getOrDefault(Object key, V defaultValue) {
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
    public V getOrDefault(double key, V defaultValue) {
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
    public void forEach(BiConsumer<? super Double, ? super V> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] keys = set;
        V[] vals = values;
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
    public void forEach(DoubleObjConsumer<? super V> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] keys = set;
        V[] vals = values;
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
    public boolean forEachWhile(DoubleObjPredicate<? super V> predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        long[] keys = set;
        V[] vals = values;
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
    public DoubleObjCursor<V> cursor() {
        int mc = modCount();
        if (!noRemoved())
            return new SomeRemovedMapCursor(mc);
        return new NoRemovedMapCursor(mc);
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonDoubleObjMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalDoubleObjMapOps<?> m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        long[] keys = set;
        V[] vals = values;
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
    public void reversePutAllTo(InternalDoubleObjMapOps<? super V> m) {
        if (isEmpty())
            return;
        int mc = modCount();
        long[] keys = set;
        V[] vals = values;
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
    public HashObjSet<Map.Entry<Double, V>> entrySet() {
        return new EntryView();
    }

    @Override
    @Nonnull
    public ObjCollection<V> values() {
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
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                hashCode += ((int) (key ^ (key >>> 32))) ^ nullableValueHashCode(vals[i]);
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
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                sb.append(' ');
                sb.append(Double.longBitsToDouble(key));
                sb.append('=');
                Object val = vals[i];
                sb.append(val != this ? val : "(this Map)");
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
        V[] vals = values;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        long[] newKeys = set;
        int capacity = newKeys.length;
        V[] newVals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                int index;
                if (newKeys[index = SeparateKVDoubleKeyMixing.mix(key) % capacity] != FREE_BITS) {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if (newKeys[bIndex] == FREE_BITS) {
                            index = bIndex;
                            break;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if (newKeys[fIndex] == FREE_BITS) {
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
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }


    @Override
    public V put(Double key, V value) {
        long k = Double.doubleToLongBits(key);
        int index = insert(k, value);
        if (index < 0) {
            // key was absent
            return null;
        } else {
            // key is present
            V[] vals = values;
            V prevValue = vals[index];
            vals[index] = value;
            return prevValue;
        }
    }

    @Override
    public V put(double key, V value) {
        long k = Double.doubleToLongBits(key);
        int index = insert(k, value);
        if (index < 0) {
            // key was absent
            return null;
        } else {
            // key is present
            V[] vals = values;
            V prevValue = vals[index];
            vals[index] = value;
            return prevValue;
        }
    }

    @Override
    public V putIfAbsent(Double key, V value) {
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
    public V putIfAbsent(double key, V value) {
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
    public void justPut(double key, V value) {
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
    public void justPut(long key, V value) {
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
    public V compute(Double key,
            BiFunction<? super Double, ? super V, ? extends V> remappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] keys = set;
        V[] vals = values;
        int capacity, index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
            keyAbsentFreeSlot:
            if (cur != FREE_BITS) {
                int firstRemoved;
                if (cur <= FREE_BITS) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == k) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == FREE_BITS) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == k) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == FREE_BITS) {
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
                        } else if (cur == FREE_BITS) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur > FREE_BITS && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == k) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == FREE_BITS) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur > FREE_BITS && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 2;
                    }
                }
                // key is absent, removed slot
                V newValue = remappingFunction.apply(Double.longBitsToDouble(k), null);
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
            V newValue = remappingFunction.apply(Double.longBitsToDouble(k), null);
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
        V newValue = remappingFunction.apply(Double.longBitsToDouble(k), vals[index]);
        if (newValue != null) {
            vals[index] = newValue;
            return newValue;
        } else {
            incrementModCount();
            keys[index] = REMOVED_BITS;
            vals[index] = null;
            postRemoveHook();
            return null;
        }
    }


    @Override
    public V compute(double key, DoubleObjFunction<? super V, ? extends V> remappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] keys = set;
        V[] vals = values;
        int capacity, index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
            keyAbsentFreeSlot:
            if (cur != FREE_BITS) {
                int firstRemoved;
                if (cur <= FREE_BITS) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == k) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == FREE_BITS) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == k) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == FREE_BITS) {
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
                        } else if (cur == FREE_BITS) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur > FREE_BITS && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == k) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == FREE_BITS) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur > FREE_BITS && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 2;
                    }
                }
                // key is absent, removed slot
                V newValue = remappingFunction.apply(Double.longBitsToDouble(k), null);
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
            V newValue = remappingFunction.apply(Double.longBitsToDouble(k), null);
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
        V newValue = remappingFunction.apply(Double.longBitsToDouble(k), vals[index]);
        if (newValue != null) {
            vals[index] = newValue;
            return newValue;
        } else {
            incrementModCount();
            keys[index] = REMOVED_BITS;
            vals[index] = null;
            postRemoveHook();
            return null;
        }
    }


    @Override
    public V computeIfAbsent(Double key,
            Function<? super Double, ? extends V> mappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] keys = set;
        V[] vals = values;
        int capacity, index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
            keyAbsentFreeSlot:
            if (cur != FREE_BITS) {
                int firstRemoved;
                if (cur <= FREE_BITS) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == k) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == FREE_BITS) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == k) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == FREE_BITS) {
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
                        } else if (cur == FREE_BITS) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur > FREE_BITS && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == k) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == FREE_BITS) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur > FREE_BITS && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 2;
                    }
                }
                // key is absent, removed slot
                V value = mappingFunction.apply(Double.longBitsToDouble(k));
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
            V value = mappingFunction.apply(Double.longBitsToDouble(k));
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
        // key is present
        V val;
        if ((val = vals[index]) != null) {
            return val;
        } else {
            V value = mappingFunction.apply(Double.longBitsToDouble(k));
            if (value != null) {
                vals[index] = value;
                return value;
            } else {
                return null;
            }
        }
    }


    @Override
    public V computeIfAbsent(double key, DoubleFunction<? extends V> mappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] keys = set;
        V[] vals = values;
        int capacity, index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
            keyAbsentFreeSlot:
            if (cur != FREE_BITS) {
                int firstRemoved;
                if (cur <= FREE_BITS) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == k) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == FREE_BITS) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == k) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == FREE_BITS) {
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
                        } else if (cur == FREE_BITS) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur > FREE_BITS && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == k) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == FREE_BITS) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur > FREE_BITS && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 2;
                    }
                }
                // key is absent, removed slot
                V value = mappingFunction.apply(Double.longBitsToDouble(k));
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
            V value = mappingFunction.apply(Double.longBitsToDouble(k));
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
        // key is present
        V val;
        if ((val = vals[index]) != null) {
            return val;
        } else {
            V value = mappingFunction.apply(Double.longBitsToDouble(k));
            if (value != null) {
                vals[index] = value;
                return value;
            } else {
                return null;
            }
        }
    }


    @Override
    public V computeIfPresent(Double key,
            BiFunction<? super Double, ? super V, ? extends V> remappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(k);
        if (index >= 0) {
            // key is present
            V[] vals = values;
            V val;
            if ((val = vals[index]) != null) {
                V newValue = remappingFunction.apply(Double.longBitsToDouble(k), val);
                if (newValue != null) {
                    vals[index] = newValue;
                    return newValue;
                } else {
                    incrementModCount();
                    set[index] = REMOVED_BITS;
                    vals[index] = null;
                    postRemoveHook();
                    return null;
                }
            } else {
                return null;
            }
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public V computeIfPresent(double key, DoubleObjFunction<? super V, ? extends V> remappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(k);
        if (index >= 0) {
            // key is present
            V[] vals = values;
            V val;
            if ((val = vals[index]) != null) {
                V newValue = remappingFunction.apply(Double.longBitsToDouble(k), val);
                if (newValue != null) {
                    vals[index] = newValue;
                    return newValue;
                } else {
                    incrementModCount();
                    set[index] = REMOVED_BITS;
                    vals[index] = null;
                    postRemoveHook();
                    return null;
                }
            } else {
                return null;
            }
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public V merge(Double key, V value,
            BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (value == null)
            throw new java.lang.NullPointerException();
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] keys = set;
        V[] vals = values;
        int capacity, index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
            keyAbsentFreeSlot:
            if (cur != FREE_BITS) {
                int firstRemoved;
                if (cur <= FREE_BITS) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == k) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == FREE_BITS) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == k) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == FREE_BITS) {
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
                        } else if (cur == FREE_BITS) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur > FREE_BITS && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == k) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == FREE_BITS) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur > FREE_BITS && firstRemoved < 0) {
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
        V val;
        if ((val = vals[index]) != null) {
            V newValue = remappingFunction.apply(val, value);
            if (newValue != null) {
                vals[index] = newValue;
                return newValue;
            } else {
                incrementModCount();
                keys[index] = REMOVED_BITS;
                vals[index] = null;
                postRemoveHook();
                return null;
            }
        } else {
            vals[index] = value;
            return value;
        }
    }


    @Override
    public V merge(double key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        long k = Double.doubleToLongBits(key);
        if (value == null)
            throw new java.lang.NullPointerException();
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] keys = set;
        V[] vals = values;
        int capacity, index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
            keyAbsentFreeSlot:
            if (cur != FREE_BITS) {
                int firstRemoved;
                if (cur <= FREE_BITS) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == k) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == FREE_BITS) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == k) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == FREE_BITS) {
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
                        } else if (cur == FREE_BITS) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur > FREE_BITS && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == k) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == FREE_BITS) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur > FREE_BITS && firstRemoved < 0) {
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
        V val;
        if ((val = vals[index]) != null) {
            V newValue = remappingFunction.apply(val, value);
            if (newValue != null) {
                vals[index] = newValue;
                return newValue;
            } else {
                incrementModCount();
                keys[index] = REMOVED_BITS;
                vals[index] = null;
                postRemoveHook();
                return null;
            }
        } else {
            vals[index] = value;
            return value;
        }
    }




    @Override
    public void putAll(@Nonnull Map<? extends Double, ? extends V> m) {
        CommonDoubleObjMapOps.putAll(this, m);
    }


    @Override
    public V replace(Double key, V value) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            V[] vals = values;
            V oldValue = vals[index];
            vals[index] = value;
            return oldValue;
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public V replace(double key, V value) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            V[] vals = values;
            V oldValue = vals[index];
            vals[index] = value;
            return oldValue;
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public boolean replace(Double key, V oldValue, V newValue) {
        return replace(key.doubleValue(),
                oldValue,
                newValue);
    }

    @Override
    public boolean replace(double key, V oldValue, V newValue) {
        long k = Double.doubleToLongBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            V[] vals = values;
            if (nullableValueEquals(vals[index], (V) oldValue)) {
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
            BiFunction<? super Double, ? super V, ? extends V> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] keys = set;
        V[] vals = values;
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
    public void replaceAll(DoubleObjFunction<? super V, ? extends V> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] keys = set;
        V[] vals = values;
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
        values[index] = null;
        postRemoveHook();
    }

    @Override
    public V remove(Object key) {
        long k = Double.doubleToLongBits((Double) key);
        long[] keys = set;
        int capacity, index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
            if (cur == FREE_BITS) {
                // key is absent, free slot
                return null;
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent, free slot
                        return null;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == k) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent, free slot
                        return null;
                    }
                    step += 2;
                }
            }
        }
        // key is present
        V[] vals = values;
        V val = vals[index];
        incrementModCount();
        keys[index] = REMOVED_BITS;
        vals[index] = null;
        postRemoveHook();
        return val;
    }


    @Override
    public boolean justRemove(double key) {
        long k = Double.doubleToLongBits(key);
        long[] keys = set;
        int capacity, index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
            if (cur == FREE_BITS) {
                // key is absent, free slot
                return false;
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent, free slot
                        return false;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == k) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent, free slot
                        return false;
                    }
                    step += 2;
                }
            }
        }
        // key is present
        incrementModCount();
        keys[index] = REMOVED_BITS;
        values[index] = null;
        postRemoveHook();
        return true;
    }

    @Override
    public boolean justRemove(long key) {
        long[] keys = set;
        int capacity, index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(key) % (capacity = keys.length)]) != key) {
            if (cur == FREE_BITS) {
                // key is absent, free slot
                return false;
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == key) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent, free slot
                        return false;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == key) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent, free slot
                        return false;
                    }
                    step += 2;
                }
            }
        }
        // key is present
        incrementModCount();
        keys[index] = REMOVED_BITS;
        values[index] = null;
        postRemoveHook();
        return true;
    }


    

    @Override
    public V remove(double key) {
        long k = Double.doubleToLongBits(key);
        long[] keys = set;
        int capacity, index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
            if (cur == FREE_BITS) {
                // key is absent, free slot
                return null;
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent, free slot
                        return null;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == k) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent, free slot
                        return null;
                    }
                    step += 2;
                }
            }
        }
        // key is present
        V[] vals = values;
        V val = vals[index];
        incrementModCount();
        keys[index] = REMOVED_BITS;
        vals[index] = null;
        postRemoveHook();
        return val;
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Double) key).doubleValue(),
                value);
    }

    @Override
    public boolean remove(double key, Object value) {
        long k = Double.doubleToLongBits(key);
        long[] keys = set;
        int capacity, index;
        long cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVDoubleKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
            if (cur == FREE_BITS) {
                // key is absent, free slot
                return false;
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent, free slot
                        return false;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == k) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent, free slot
                        return false;
                    }
                    step += 2;
                }
            }
        }
        // key is present
        V[] vals = values;
        if (nullableValueEquals(vals[index], (V) value)) {
            incrementModCount();
            keys[index] = REMOVED_BITS;
            vals[index] = null;
            postRemoveHook();
            return true;
        } else {
            return false;
        }
    }


    @Override
    public boolean removeIf(DoubleObjPredicate<? super V> filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] keys = set;
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                if (filter.test(Double.longBitsToDouble(key), vals[i])) {
                    incrementModCount();
                    mc++;
                    keys[i] = REMOVED_BITS;
                    vals[i] = null;
                    postRemoveHook();
                    changed = true;
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }




    // under this condition - operations, overridden from MutableSeparateKVDoubleQHashGO
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
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                if (filter.test(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    keys[i] = REMOVED_BITS;
                    vals[i] = null;
                    postRemoveHook();
                    changed = true;
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
        long[] keys = set;
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                if (filter.test(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    keys[i] = REMOVED_BITS;
                    vals[i] = null;
                    postRemoveHook();
                    changed = true;
                }
            }
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
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                if (c.contains(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    keys[i] = REMOVED_BITS;
                    vals[i] = null;
                    postRemoveHook();
                    changed = true;
                }
            }
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
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                if (c.contains(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    keys[i] = REMOVED_BITS;
                    vals[i] = null;
                    postRemoveHook();
                    changed = true;
                }
            }
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
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                if (c.contains(key)) {
                    incrementModCount();
                    mc++;
                    keys[i] = REMOVED_BITS;
                    vals[i] = null;
                    postRemoveHook();
                    changed = true;
                }
            }
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
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                if (!c.contains(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    keys[i] = REMOVED_BITS;
                    vals[i] = null;
                    postRemoveHook();
                    changed = true;
                }
            }
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
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                if (!c.contains(Double.longBitsToDouble(key))) {
                    incrementModCount();
                    mc++;
                    keys[i] = REMOVED_BITS;
                    vals[i] = null;
                    postRemoveHook();
                    changed = true;
                }
            }
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
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            long key;
            if ((key = keys[i]) < FREE_BITS) {
                if (!c.contains(key)) {
                    incrementModCount();
                    mc++;
                    keys[i] = REMOVED_BITS;
                    vals[i] = null;
                    postRemoveHook();
                    changed = true;
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }




    @Override
    public DoubleIterator iterator() {
        int mc = modCount();
        if (!noRemoved())
            return new SomeRemovedKeyIterator(mc);
        return new NoRemovedKeyIterator(mc);
    }

    @Override
    public DoubleCursor setCursor() {
        int mc = modCount();
        if (!noRemoved())
            return new SomeRemovedKeyCursor(mc);
        return new NoRemovedKeyCursor(mc);
    }


    class NoRemovedKeyIterator extends NoRemovedIterator {
        final V[] vals;

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
                    incrementModCount();
                    keys[index] = REMOVED_BITS;
                    vals[index] = null;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedKeyCursor extends NoRemovedCursor {
        final V[] vals;

        private NoRemovedKeyCursor(int mc) {
            super(mc);
            vals = values;
        }

        @Override
        public void remove() {
            if (curKey != FREE_BITS) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE_BITS;
                    incrementModCount();
                    int index;
                    keys[index = this.index] = REMOVED_BITS;
                    vals[index] = null;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedKeyIterator extends SomeRemovedIterator {
        final V[] vals;

        private SomeRemovedKeyIterator(int mc) {
            super(mc);
            vals = values;
        }

        @Override
        public void remove() {
            int index;
            if ((index = this.index) >= 0) {
                if (expectedModCount++ == modCount()) {
                    this.index = -1;
                    incrementModCount();
                    keys[index] = REMOVED_BITS;
                    vals[index] = null;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedKeyCursor extends SomeRemovedCursor {
        final V[] vals;

        private SomeRemovedKeyCursor(int mc) {
            super(mc);
            vals = values;
        }

        @Override
        public void remove() {
            if (curKey != FREE_BITS) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE_BITS;
                    incrementModCount();
                    int index;
                    keys[index = this.index] = REMOVED_BITS;
                    vals[index] = null;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }





    class EntryView extends AbstractSetView<Map.Entry<Double, V>>
            implements HashObjSet<Map.Entry<Double, V>>,
            InternalObjCollectionOps<Map.Entry<Double, V>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Double, V>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Double>defaultEquality()
                    ,
                    valueEquivalence()
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return MutableQHashSeparateKVDoubleObjMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return MutableQHashSeparateKVDoubleObjMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return MutableQHashSeparateKVDoubleObjMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Double, V> e = (Map.Entry<Double, V>) o;
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
            V[] vals = values;
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
            V[] vals = values;
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
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Double, V>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            long[] keys = set;
            V[] vals = values;
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
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Double, V>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            long[] keys = set;
            V[] vals = values;
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
        public ObjIterator<Map.Entry<Double, V>> iterator() {
            int mc = modCount();
            if (!noRemoved())
                return new SomeRemovedEntryIterator(mc);
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Double, V>> cursor() {
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
            long[] keys = set;
            V[] vals = values;
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
            V[] vals = values;
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
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Double, V>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] keys = set;
            V[] vals = values;
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
            return MutableQHashSeparateKVDoubleObjMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            long[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
                    sb.append(' ');
                    sb.append(Double.longBitsToDouble(key));
                    sb.append('=');
                    Object val = vals[i];
                    sb.append(val != this ? val : "(this Collection)");
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
            return MutableQHashSeparateKVDoubleObjMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Double, V> e = (Map.Entry<Double, V>) o;
                double key = e.getKey();
                V value = e.getValue();
                return MutableQHashSeparateKVDoubleObjMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Double, V>> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
                    if (filter.test(new MutableEntry(mc, i, key, vals[i]))) {
                        incrementModCount();
                        mc++;
                        keys[i] = REMOVED_BITS;
                        vals[i] = null;
                        postRemoveHook();
                        changed = true;
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
            long[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
                    if (c.contains(e.with(key, vals[i]))) {
                        incrementModCount();
                        mc++;
                        keys[i] = REMOVED_BITS;
                        vals[i] = null;
                        postRemoveHook();
                        changed = true;
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
            long[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
                    if (!c.contains(e.with(key, vals[i]))) {
                        incrementModCount();
                        mc++;
                        keys[i] = REMOVED_BITS;
                        vals[i] = null;
                        postRemoveHook();
                        changed = true;
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public void clear() {
            MutableQHashSeparateKVDoubleObjMapGO.this.clear();
        }
    }


    abstract class DoubleObjEntry extends AbstractEntry<Double, V> {

        abstract long key();

        @Override
        public final Double getKey() {
            return Double.longBitsToDouble(key());
        }

        abstract V value();

        @Override
        public final V getValue() {
            return value();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object o) {
            Map.Entry e2;
            long k2;
            V v2;
            try {
                e2 = (Map.Entry) o;
                k2 = Double.doubleToLongBits((Double) e2.getKey());
                v2 = (V) e2.getValue();
                return key() == k2
                        
                        &&
                        nullableValueEquals(value(), v2);
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
                    nullableValueHashCode(value());
        }
    }


    class MutableEntry extends DoubleObjEntry {
        final int modCount;
        private final int index;
        final long key;
        private V value;

        MutableEntry(int modCount, int index, long key, V value) {
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
        public V value() {
            return value;
        }

        @Override
        public V setValue(V newValue) {
            if (modCount != modCount())
                throw new IllegalStateException();
            V oldValue = value;
            V unwrappedNewValue = newValue;
            value = unwrappedNewValue;
            updateValueInTable(unwrappedNewValue);
            return oldValue;
        }

        void updateValueInTable(V newValue) {
            values[index] = newValue;
        }
    }



    class ReusableEntry extends DoubleObjEntry {
        private long key;
        private V value;

        ReusableEntry with(long key, V value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public long key() {
            return key;
        }

        @Override
        public V value() {
            return value;
        }
    }


    class ValueView extends AbstractObjValueView<V> {

        @Override
        public Equivalence<V> equivalence() {
            return valueEquivalence();
        }

        @Override
        public int size() {
            return MutableQHashSeparateKVDoubleObjMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return MutableQHashSeparateKVDoubleObjMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return MutableQHashSeparateKVDoubleObjMapGO.this.containsValue(o);
        }



        @Override
        public void forEach(Consumer<? super V> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            long[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    action.accept(vals[i]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }


        @Override
        public boolean forEachWhile(Predicate<? super V> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            long[] keys = set;
            V[] vals = values;
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
        public boolean allContainingIn(ObjCollection<?> c) {
            if (isEmpty())
                return true;
            boolean containsAll = true;
            int mc = modCount();
            long[] keys = set;
            V[] vals = values;
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
        public boolean reverseAddAllTo(ObjCollection<? super V> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] keys = set;
            V[] vals = values;
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
        public boolean reverseRemoveAllFrom(ObjSet<?> s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    changed |= s.remove(vals[i]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }



        @Override
        @Nonnull
        public ObjIterator<V> iterator() {
            int mc = modCount();
            if (!noRemoved())
                return new SomeRemovedValueIterator(mc);
            return new NoRemovedValueIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<V> cursor() {
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
            long[] keys = set;
            V[] vals = values;
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
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    a[resultIndex++] = (T) vals[i];
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            if (a.length > resultIndex)
                a[resultIndex] = null;
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
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                V val;
                    sb.append(' ').append((val = vals[i]) != this ? val : "(this Collection)").append(',');
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
            return removeValue(o);
        }



        @Override
        public void clear() {
            MutableQHashSeparateKVDoubleObjMapGO.this.clear();
        }

        @Override
        public boolean removeIf(Predicate<? super V> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    if (filter.test(vals[i])) {
                        incrementModCount();
                        mc++;
                        keys[i] = REMOVED_BITS;
                        vals[i] = null;
                        postRemoveHook();
                        changed = true;
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        @Override
        public boolean removeAll(@Nonnull Collection<?> c) {
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    if (c.contains(vals[i])) {
                        incrementModCount();
                        mc++;
                        keys[i] = REMOVED_BITS;
                        vals[i] = null;
                        postRemoveHook();
                        changed = true;
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        @Override
        public boolean retainAll(@Nonnull Collection<?> c) {
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
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    if (!c.contains(vals[i])) {
                        incrementModCount();
                        mc++;
                        keys[i] = REMOVED_BITS;
                        vals[i] = null;
                        postRemoveHook();
                        changed = true;
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

    }



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Double, V>> {
        final long[] keys;
        final V[] vals;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        MutableEntry next;

        NoRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            long[] keys = this.keys = set;
            V[] vals = this.vals = values;
            int nextI = keys.length;
            while (--nextI >= 0) {
                long key;
                if ((key = keys[nextI]) < FREE_BITS) {
                    next = new MutableEntry(mc, nextI, key, vals[nextI]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Double, V>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            V[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
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
        public Map.Entry<Double, V> next() {
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
                    keys[index] = REMOVED_BITS;
                    vals[index] = null;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Double, V>> {
        final long[] keys;
        final V[] vals;
        int expectedModCount;
        int index;
        long curKey;
        V curValue;

        NoRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Double, V>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            V[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
                    action.accept(new MutableEntry(mc, i, key, vals[i]));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE_BITS;
        }

        @Override
        public Map.Entry<Double, V> elem() {
            long curKey;
            if ((curKey = this.curKey) != FREE_BITS) {
                return new MutableEntry(expectedModCount, index, curKey, curValue);
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
            if (curKey != FREE_BITS) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE_BITS;
                    incrementModCount();
                    int index;
                    keys[index = this.index] = REMOVED_BITS;
                    vals[index] = null;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedEntryIterator implements ObjIterator<Map.Entry<Double, V>> {
        final long[] keys;
        final V[] vals;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        MutableEntry next;

        SomeRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            long[] keys = this.keys = set;
            V[] vals = this.vals = values;
            int nextI = keys.length;
            while (--nextI >= 0) {
                long key;
                if ((key = keys[nextI]) < FREE_BITS) {
                    next = new MutableEntry(mc, nextI, key, vals[nextI]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Double, V>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            V[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
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
        public Map.Entry<Double, V> next() {
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
                    keys[index] = REMOVED_BITS;
                    vals[index] = null;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedEntryCursor implements ObjCursor<Map.Entry<Double, V>> {
        final long[] keys;
        final V[] vals;
        int expectedModCount;
        int index;
        long curKey;
        V curValue;

        SomeRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Double, V>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            V[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                long key;
                if ((key = keys[i]) < FREE_BITS) {
                    action.accept(new MutableEntry(mc, i, key, vals[i]));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE_BITS;
        }

        @Override
        public Map.Entry<Double, V> elem() {
            long curKey;
            if ((curKey = this.curKey) != FREE_BITS) {
                return new MutableEntry(expectedModCount, index, curKey, curValue);
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
            if (curKey != FREE_BITS) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE_BITS;
                    incrementModCount();
                    int index;
                    keys[index = this.index] = REMOVED_BITS;
                    vals[index] = null;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }




    class NoRemovedValueIterator implements ObjIterator<V> {
        final long[] keys;
        final V[] vals;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        V next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            long[] keys = this.keys = set;
            V[] vals = this.vals = values;
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
        public void forEachRemaining(Consumer<? super V> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            V[] vals = this.vals;
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
        public V next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                if (expectedModCount == modCount()) {
                    index = nextI;
                    long[] keys = this.keys;
                    V prev = next;
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
        public void remove() {
            int index;
            if ((index = this.index) >= 0) {
                if (expectedModCount++ == modCount()) {
                    this.index = -1;
                    incrementModCount();
                    keys[index] = REMOVED_BITS;
                    vals[index] = null;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedValueCursor implements ObjCursor<V> {
        final long[] keys;
        final V[] vals;
        int expectedModCount;
        int index;
        long curKey;
        V curValue;

        NoRemovedValueCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(Consumer<? super V> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            V[] vals = this.vals;
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
        public V elem() {
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
            if (curKey != FREE_BITS) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE_BITS;
                    incrementModCount();
                    int index;
                    keys[index = this.index] = REMOVED_BITS;
                    vals[index] = null;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedValueIterator implements ObjIterator<V> {
        final long[] keys;
        final V[] vals;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        V next;

        SomeRemovedValueIterator(int mc) {
            expectedModCount = mc;
            long[] keys = this.keys = set;
            V[] vals = this.vals = values;
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
        public void forEachRemaining(Consumer<? super V> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            V[] vals = this.vals;
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
        public V next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                if (expectedModCount == modCount()) {
                    index = nextI;
                    long[] keys = this.keys;
                    V prev = next;
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
        public void remove() {
            int index;
            if ((index = this.index) >= 0) {
                if (expectedModCount++ == modCount()) {
                    this.index = -1;
                    incrementModCount();
                    keys[index] = REMOVED_BITS;
                    vals[index] = null;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedValueCursor implements ObjCursor<V> {
        final long[] keys;
        final V[] vals;
        int expectedModCount;
        int index;
        long curKey;
        V curValue;

        SomeRemovedValueCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(Consumer<? super V> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            V[] vals = this.vals;
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
        public V elem() {
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
            if (curKey != FREE_BITS) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE_BITS;
                    incrementModCount();
                    int index;
                    keys[index = this.index] = REMOVED_BITS;
                    vals[index] = null;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }



    class NoRemovedMapCursor implements DoubleObjCursor<V> {
        final long[] keys;
        final V[] vals;
        int expectedModCount;
        int index;
        long curKey;
        V curValue;

        NoRemovedMapCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(DoubleObjConsumer<? super V> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            V[] vals = this.vals;
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
        public V value() {
            if (curKey != FREE_BITS) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(V value) {
            if (curKey != FREE_BITS) {
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
            if (curKey != FREE_BITS) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE_BITS;
                    incrementModCount();
                    int index;
                    keys[index = this.index] = REMOVED_BITS;
                    vals[index] = null;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }

    class SomeRemovedMapCursor implements DoubleObjCursor<V> {
        final long[] keys;
        final V[] vals;
        int expectedModCount;
        int index;
        long curKey;
        V curValue;

        SomeRemovedMapCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(DoubleObjConsumer<? super V> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] keys = this.keys;
            V[] vals = this.vals;
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
        public V value() {
            if (curKey != FREE_BITS) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(V value) {
            if (curKey != FREE_BITS) {
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
            if (curKey != FREE_BITS) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE_BITS;
                    incrementModCount();
                    int index;
                    keys[index = this.index] = REMOVED_BITS;
                    vals[index] = null;
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

