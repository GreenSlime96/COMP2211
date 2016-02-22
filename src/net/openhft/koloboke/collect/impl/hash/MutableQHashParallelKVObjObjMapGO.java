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
import java.util.function.Predicate;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import java.util.*;


public class MutableQHashParallelKVObjObjMapGO<K, V>
        extends MutableQHashParallelKVObjObjMapSO<K, V> {

    
    final void copy(ParallelKVObjObjQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    
    final void move(ParallelKVObjObjQHash hash) {
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
    public boolean containsEntry(Object key, Object value) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return nullableValueEquals((V) table[index + 1], (V) value);
        } else {
            // key is absent
            return false;
        }
    }



    

    @Override
    public V get(Object key) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return (V) table[index + 1];
        } else {
            // key is absent
            return null;
        }
    }


    @Override
    public V getOrDefault(Object key, V defaultValue) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return (V) table[index + 1];
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        Object[] tab = table;
        if (noRemoved()) {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE) {
                    action.accept(key, (V) tab[i + 1]);
                }
            }
        } else {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                    action.accept(key, (V) tab[i + 1]);
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }


    @Override
    public boolean forEachWhile(BiPredicate<? super K, ? super V> predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        Object[] tab = table;
        if (noRemoved()) {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE) {
                    if (!predicate.test(key, (V) tab[i + 1])) {
                        terminated = true;
                        break;
                    }
                }
            }
        } else {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                    if (!predicate.test(key, (V) tab[i + 1])) {
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
    public ObjObjCursor<K, V> cursor() {
        int mc = modCount();
        if (!noRemoved())
            return new SomeRemovedMapCursor(mc);
        return new NoRemovedMapCursor(mc);
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonObjObjMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalObjObjMapOps<?, ?> m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        Object[] tab = table;
        if (noRemoved()) {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE) {
                    if (!m.containsEntry(key, (V) tab[i + 1])) {
                        containsAll = false;
                        break;
                    }
                }
            }
        } else {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                    if (!m.containsEntry(key, (V) tab[i + 1])) {
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
    public void reversePutAllTo(InternalObjObjMapOps<? super K, ? super V> m) {
        if (isEmpty())
            return;
        int mc = modCount();
        Object[] tab = table;
        if (noRemoved()) {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE) {
                    m.justPut(key, (V) tab[i + 1]);
                }
            }
        } else {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                    m.justPut(key, (V) tab[i + 1]);
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<K, V>> entrySet() {
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
        Object[] tab = table;
        if (noRemoved()) {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE) {
                    hashCode += nullableKeyHashCode(key) ^ nullableValueHashCode((V) tab[i + 1]);
                }
            }
        } else {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                    hashCode += nullableKeyHashCode(key) ^ nullableValueHashCode((V) tab[i + 1]);
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
        Object[] tab = table;
        if (noRemoved()) {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE) {
                    sb.append(' ');
                    sb.append(key != this ? key : "(this Map)");
                    sb.append('=');
                    Object val = (V) tab[i + 1];
                    sb.append(val != this ? val : "(this Map)");
                    sb.append(',');
                    if (++elementCount == 8) {
                        int expectedLength = sb.length() * (size() / 8);
                        sb.ensureCapacity(expectedLength + (expectedLength / 2));
                    }
                }
            }
        } else {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                    sb.append(' ');
                    sb.append(key != this ? key : "(this Map)");
                    sb.append('=');
                    Object val = (V) tab[i + 1];
                    sb.append(val != this ? val : "(this Map)");
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
        Object[] tab = table;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        Object[] newTab = table;
        int capacity = newTab.length;
        if (noRemoved()) {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE) {
                    int index;
                    // noinspection unchecked
                    if ((K) newTab[index = ParallelKVObjKeyMixing.mix(nullableKeyHashCode(key)) % capacity] != FREE) {
                        int bIndex = index, fIndex = index, step = 2;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            // noinspection unchecked
                            if ((K) newTab[bIndex] == FREE) {
                                index = bIndex;
                                break;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            // noinspection unchecked
                            if ((K) newTab[fIndex] == FREE) {
                                index = fIndex;
                                break;
                            }
                            step += 4;
                        }
                    }
                    newTab[index] = key;
                    newTab[index + 1] = (V) tab[i + 1];
                }
            }
        } else {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                    int index;
                    // noinspection unchecked
                    if ((K) newTab[index = ParallelKVObjKeyMixing.mix(nullableKeyHashCode(key)) % capacity] != FREE) {
                        int bIndex = index, fIndex = index, step = 2;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            // noinspection unchecked
                            if ((K) newTab[bIndex] == FREE) {
                                index = bIndex;
                                break;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            // noinspection unchecked
                            if ((K) newTab[fIndex] == FREE) {
                                index = fIndex;
                                break;
                            }
                            step += 4;
                        }
                    }
                    newTab[index] = key;
                    newTab[index + 1] = (V) tab[i + 1];
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }



    @Override
    public V put(K key, V value) {
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return null;
        } else {
            // key is present
            Object[] tab = table;
            V prevValue = (V) tab[index + 1];
            tab[index + 1] = value;
            return prevValue;
        }
    }


    @Override
    public V putIfAbsent(K key, V value) {
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return null;
        } else {
            // key is present
            return (V) table[index + 1];
        }
    }

    @Override
    public void justPut(K key, V value) {
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
    public V compute(K key,
            BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if (key != null) {
            if (remappingFunction == null)
                throw new java.lang.NullPointerException();
            Object[] tab = table;
            int capacity, index;
            K cur;
            keyPresent:
            if ((cur = (K) tab[index = ParallelKVObjKeyMixing.mix(keyHashCode(key)) % (capacity = tab.length)]) != key) {
                keyAbsentFreeSlot:
                if (cur != FREE) {
                    int firstRemoved;
                    if (cur != REMOVED) {
                        if (keyEquals(key, cur)) {
                            break keyPresent;
                        } else {
                            if (noRemoved()) {
                                int bIndex = index, fIndex = index, step = 2;
                                while (true) {
                                    if ((bIndex -= step) < 0) bIndex += capacity;
                                    if ((cur = (K) tab[bIndex]) == key) {
                                        index = bIndex;
                                        break keyPresent;
                                    } else if (cur == FREE) {
                                        index = bIndex;
                                        break keyAbsentFreeSlot;
                                    }
                                    else if (keyEquals(key, cur)) {
                                        index = bIndex;
                                        break keyPresent;
                                    }
                                    int t;
                                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                                    if ((cur = (K) tab[fIndex]) == key) {
                                        index = fIndex;
                                        break keyPresent;
                                    } else if (cur == FREE) {
                                        index = fIndex;
                                        break keyAbsentFreeSlot;
                                    }
                                    else if (keyEquals(key, cur)) {
                                        index = fIndex;
                                        break keyPresent;
                                    }
                                    step += 4;
                                }
                            } else {
                                firstRemoved = -1;
                            }
                        }
                    } else {
                        firstRemoved = index;
                    }
                    keyAbsentRemovedSlot: {
                        int bIndex = index, fIndex = index, step = 2;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (K) tab[bIndex]) == key) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
                                if (firstRemoved < 0) {
                                    index = bIndex;
                                    break keyAbsentFreeSlot;
                                } else {
                                    break keyAbsentRemovedSlot;
                                }
                            } else if (cur != REMOVED) {
                                if (keyEquals(key, cur)) {
                                    index = bIndex;
                                    break keyPresent;
                                }
                            } else if (firstRemoved < 0) {
                                firstRemoved = bIndex;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (K) tab[fIndex]) == key) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
                                if (firstRemoved < 0) {
                                    index = fIndex;
                                    break keyAbsentFreeSlot;
                                } else {
                                    break keyAbsentRemovedSlot;
                                }
                            } else if (cur != REMOVED) {
                                if (keyEquals(key, cur)) {
                                    index = fIndex;
                                    break keyPresent;
                                }
                            } else if (firstRemoved < 0) {
                                firstRemoved = fIndex;
                            }
                            step += 4;
                        }
                    }
                    // key is absent, removed slot
                    V newValue = remappingFunction.apply(key, null);
                    if (newValue != null) {
                        incrementModCount();
                        tab[firstRemoved] = key;
                        tab[firstRemoved + 1] = newValue;
                        postRemovedSlotInsertHook();
                        return newValue;
                    } else {
                        return null;
                    }
                }
                // key is absent, free slot
                V newValue = remappingFunction.apply(key, null);
                if (newValue != null) {
                    incrementModCount();
                    tab[index] = key;
                    tab[index + 1] = newValue;
                    postFreeSlotInsertHook();
                    return newValue;
                } else {
                    return null;
                }
            }
            // key is present
            V newValue = remappingFunction.apply(key, (V) tab[index + 1]);
            if (newValue != null) {
                tab[index + 1] = newValue;
                return newValue;
            } else {
                incrementModCount();
                tab[index] = REMOVED;
                tab[index + 1] = null;
                postRemoveHook();
                return null;
            }
        } else {
            return computeNullKey(remappingFunction);
        }
    }

    V computeNullKey(
            BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        Object[] tab = table;
        int capacity = tab.length;
        int index;
        K cur;
        keyPresent:
        if ((cur = (K) tab[index = 0]) != null) {
            keyAbsentFreeSlot:
            if (cur != FREE) {
                int firstRemoved;
                if (cur != REMOVED) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 2;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (K) tab[bIndex]) == null) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (K) tab[fIndex]) == null) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            }
                            step += 4;
                        }
                    } else {
                        firstRemoved = -1;
                    }
                } else {
                    firstRemoved = index;
                }
                keyAbsentRemovedSlot: {
                    int bIndex = index, fIndex = index, step = 2;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (K) tab[bIndex]) == null) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == FREE) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == REMOVED && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (K) tab[fIndex]) == null) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == FREE) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == REMOVED && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 4;
                    }
                }
                // key is absent, removed slot
                V newValue = remappingFunction.apply(null, null);
                if (newValue != null) {
                    incrementModCount();
                    tab[firstRemoved] = null;
                    tab[firstRemoved + 1] = newValue;
                    postRemovedSlotInsertHook();
                    return newValue;
                } else {
                    return null;
                }
            }
            // key is absent, free slot
            V newValue = remappingFunction.apply(null, null);
            if (newValue != null) {
                incrementModCount();
                tab[index] = null;
                tab[index + 1] = newValue;
                postFreeSlotInsertHook();
                return newValue;
            } else {
                return null;
            }
        }
        // key is present
        V newValue = remappingFunction.apply(null, (V) tab[index + 1]);
        if (newValue != null) {
            tab[index + 1] = newValue;
            return newValue;
        } else {
            incrementModCount();
            tab[index] = REMOVED;
            tab[index + 1] = null;
            postRemoveHook();
            return null;
        }
    }


    @Override
    public V computeIfAbsent(K key,
            Function<? super K, ? extends V> mappingFunction) {
        if (key != null) {
            if (mappingFunction == null)
                throw new java.lang.NullPointerException();
            Object[] tab = table;
            int capacity, index;
            K cur;
            keyPresent:
            if ((cur = (K) tab[index = ParallelKVObjKeyMixing.mix(keyHashCode(key)) % (capacity = tab.length)]) != key) {
                keyAbsentFreeSlot:
                if (cur != FREE) {
                    int firstRemoved;
                    if (cur != REMOVED) {
                        if (keyEquals(key, cur)) {
                            break keyPresent;
                        } else {
                            if (noRemoved()) {
                                int bIndex = index, fIndex = index, step = 2;
                                while (true) {
                                    if ((bIndex -= step) < 0) bIndex += capacity;
                                    if ((cur = (K) tab[bIndex]) == key) {
                                        index = bIndex;
                                        break keyPresent;
                                    } else if (cur == FREE) {
                                        index = bIndex;
                                        break keyAbsentFreeSlot;
                                    }
                                    else if (keyEquals(key, cur)) {
                                        index = bIndex;
                                        break keyPresent;
                                    }
                                    int t;
                                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                                    if ((cur = (K) tab[fIndex]) == key) {
                                        index = fIndex;
                                        break keyPresent;
                                    } else if (cur == FREE) {
                                        index = fIndex;
                                        break keyAbsentFreeSlot;
                                    }
                                    else if (keyEquals(key, cur)) {
                                        index = fIndex;
                                        break keyPresent;
                                    }
                                    step += 4;
                                }
                            } else {
                                firstRemoved = -1;
                            }
                        }
                    } else {
                        firstRemoved = index;
                    }
                    keyAbsentRemovedSlot: {
                        int bIndex = index, fIndex = index, step = 2;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (K) tab[bIndex]) == key) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
                                if (firstRemoved < 0) {
                                    index = bIndex;
                                    break keyAbsentFreeSlot;
                                } else {
                                    break keyAbsentRemovedSlot;
                                }
                            } else if (cur != REMOVED) {
                                if (keyEquals(key, cur)) {
                                    index = bIndex;
                                    break keyPresent;
                                }
                            } else if (firstRemoved < 0) {
                                firstRemoved = bIndex;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (K) tab[fIndex]) == key) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
                                if (firstRemoved < 0) {
                                    index = fIndex;
                                    break keyAbsentFreeSlot;
                                } else {
                                    break keyAbsentRemovedSlot;
                                }
                            } else if (cur != REMOVED) {
                                if (keyEquals(key, cur)) {
                                    index = fIndex;
                                    break keyPresent;
                                }
                            } else if (firstRemoved < 0) {
                                firstRemoved = fIndex;
                            }
                            step += 4;
                        }
                    }
                    // key is absent, removed slot
                    V value = mappingFunction.apply(key);
                    if (value != null) {
                        incrementModCount();
                        tab[firstRemoved] = key;
                        tab[firstRemoved + 1] = value;
                        postRemovedSlotInsertHook();
                        return value;
                    } else {
                        return null;
                    }
                }
                // key is absent, free slot
                V value = mappingFunction.apply(key);
                if (value != null) {
                    incrementModCount();
                    tab[index] = key;
                    tab[index + 1] = value;
                    postFreeSlotInsertHook();
                    return value;
                } else {
                    return null;
                }
            }
            // key is present
            V val;
            if ((val = (V) tab[index + 1]) != null) {
                return val;
            } else {
                V value = mappingFunction.apply(key);
                if (value != null) {
                    tab[index + 1] = value;
                    return value;
                } else {
                    return null;
                }
            }
        } else {
            return computeIfAbsentNullKey(mappingFunction);
        }
    }

    V computeIfAbsentNullKey(
            Function<? super K, ? extends V> mappingFunction) {
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        Object[] tab = table;
        int capacity = tab.length;
        int index;
        K cur;
        keyPresent:
        if ((cur = (K) tab[index = 0]) != null) {
            keyAbsentFreeSlot:
            if (cur != FREE) {
                int firstRemoved;
                if (cur != REMOVED) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 2;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (K) tab[bIndex]) == null) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (K) tab[fIndex]) == null) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            }
                            step += 4;
                        }
                    } else {
                        firstRemoved = -1;
                    }
                } else {
                    firstRemoved = index;
                }
                keyAbsentRemovedSlot: {
                    int bIndex = index, fIndex = index, step = 2;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (K) tab[bIndex]) == null) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == FREE) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == REMOVED && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (K) tab[fIndex]) == null) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == FREE) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == REMOVED && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 4;
                    }
                }
                // key is absent, removed slot
                V value = mappingFunction.apply(null);
                if (value != null) {
                    incrementModCount();
                    tab[firstRemoved] = null;
                    tab[firstRemoved + 1] = value;
                    postRemovedSlotInsertHook();
                    return value;
                } else {
                    return null;
                }
            }
            // key is absent, free slot
            V value = mappingFunction.apply(null);
            if (value != null) {
                incrementModCount();
                tab[index] = null;
                tab[index + 1] = value;
                postFreeSlotInsertHook();
                return value;
            } else {
                return null;
            }
        }
        // key is present
        V val;
        if ((val = (V) tab[index + 1]) != null) {
            return val;
        } else {
            V value = mappingFunction.apply(null);
            if (value != null) {
                tab[index + 1] = value;
                return value;
            } else {
                return null;
            }
        }
    }


    @Override
    public V computeIfPresent(K key,
            BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(key);
        if (index >= 0) {
            // key is present
            Object[] tab = table;
            V val;
            if ((val = (V) tab[index + 1]) != null) {
                V newValue = remappingFunction.apply(key, val);
                if (newValue != null) {
                    tab[index + 1] = newValue;
                    return newValue;
                } else {
                    incrementModCount();
                    table[index] = REMOVED;
                    tab[index + 1] = null;
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
    public V merge(K key, V value,
            BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        if (key != null) {
            if (value == null)
                throw new java.lang.NullPointerException();
            if (remappingFunction == null)
                throw new java.lang.NullPointerException();
            Object[] tab = table;
            int capacity, index;
            K cur;
            keyPresent:
            if ((cur = (K) tab[index = ParallelKVObjKeyMixing.mix(keyHashCode(key)) % (capacity = tab.length)]) != key) {
                keyAbsentFreeSlot:
                if (cur != FREE) {
                    int firstRemoved;
                    if (cur != REMOVED) {
                        if (keyEquals(key, cur)) {
                            break keyPresent;
                        } else {
                            if (noRemoved()) {
                                int bIndex = index, fIndex = index, step = 2;
                                while (true) {
                                    if ((bIndex -= step) < 0) bIndex += capacity;
                                    if ((cur = (K) tab[bIndex]) == key) {
                                        index = bIndex;
                                        break keyPresent;
                                    } else if (cur == FREE) {
                                        index = bIndex;
                                        break keyAbsentFreeSlot;
                                    }
                                    else if (keyEquals(key, cur)) {
                                        index = bIndex;
                                        break keyPresent;
                                    }
                                    int t;
                                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                                    if ((cur = (K) tab[fIndex]) == key) {
                                        index = fIndex;
                                        break keyPresent;
                                    } else if (cur == FREE) {
                                        index = fIndex;
                                        break keyAbsentFreeSlot;
                                    }
                                    else if (keyEquals(key, cur)) {
                                        index = fIndex;
                                        break keyPresent;
                                    }
                                    step += 4;
                                }
                            } else {
                                firstRemoved = -1;
                            }
                        }
                    } else {
                        firstRemoved = index;
                    }
                    keyAbsentRemovedSlot: {
                        int bIndex = index, fIndex = index, step = 2;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (K) tab[bIndex]) == key) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
                                if (firstRemoved < 0) {
                                    index = bIndex;
                                    break keyAbsentFreeSlot;
                                } else {
                                    break keyAbsentRemovedSlot;
                                }
                            } else if (cur != REMOVED) {
                                if (keyEquals(key, cur)) {
                                    index = bIndex;
                                    break keyPresent;
                                }
                            } else if (firstRemoved < 0) {
                                firstRemoved = bIndex;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (K) tab[fIndex]) == key) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
                                if (firstRemoved < 0) {
                                    index = fIndex;
                                    break keyAbsentFreeSlot;
                                } else {
                                    break keyAbsentRemovedSlot;
                                }
                            } else if (cur != REMOVED) {
                                if (keyEquals(key, cur)) {
                                    index = fIndex;
                                    break keyPresent;
                                }
                            } else if (firstRemoved < 0) {
                                firstRemoved = fIndex;
                            }
                            step += 4;
                        }
                    }
                    // key is absent, removed slot
                    incrementModCount();
                    tab[firstRemoved] = key;
                    tab[firstRemoved + 1] = value;
                    postRemovedSlotInsertHook();
                    return value;
                }
                // key is absent, free slot
                incrementModCount();
                tab[index] = key;
                tab[index + 1] = value;
                postFreeSlotInsertHook();
                return value;
            }
            // key is present
            V val;
            if ((val = (V) tab[index + 1]) != null) {
                V newValue = remappingFunction.apply(val, value);
                if (newValue != null) {
                    tab[index + 1] = newValue;
                    return newValue;
                } else {
                    incrementModCount();
                    tab[index] = REMOVED;
                    tab[index + 1] = null;
                    postRemoveHook();
                    return null;
                }
            } else {
                tab[index + 1] = value;
                return value;
            }
        } else {
            return mergeNullKey(value, remappingFunction);
        }
    }

    V mergeNullKey(V value,
            BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        if (value == null)
            throw new java.lang.NullPointerException();
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        Object[] tab = table;
        int capacity = tab.length;
        int index;
        K cur;
        keyPresent:
        if ((cur = (K) tab[index = 0]) != null) {
            keyAbsentFreeSlot:
            if (cur != FREE) {
                int firstRemoved;
                if (cur != REMOVED) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 2;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (K) tab[bIndex]) == null) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (K) tab[fIndex]) == null) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            }
                            step += 4;
                        }
                    } else {
                        firstRemoved = -1;
                    }
                } else {
                    firstRemoved = index;
                }
                keyAbsentRemovedSlot: {
                    int bIndex = index, fIndex = index, step = 2;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (K) tab[bIndex]) == null) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == FREE) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == REMOVED && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (K) tab[fIndex]) == null) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == FREE) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == REMOVED && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 4;
                    }
                }
                // key is absent, removed slot
                incrementModCount();
                tab[firstRemoved] = null;
                tab[firstRemoved + 1] = value;
                postRemovedSlotInsertHook();
                return value;
            }
            // key is absent, free slot
            incrementModCount();
            tab[index] = null;
            tab[index + 1] = value;
            postFreeSlotInsertHook();
            return value;
        }
        // key is present
        V val;
        if ((val = (V) tab[index + 1]) != null) {
            V newValue = remappingFunction.apply(val, value);
            if (newValue != null) {
                tab[index + 1] = newValue;
                return newValue;
            } else {
                incrementModCount();
                tab[index] = REMOVED;
                tab[index + 1] = null;
                postRemoveHook();
                return null;
            }
        } else {
            tab[index + 1] = value;
            return value;
        }
    }




    @Override
    public void putAll(@Nonnull Map<? extends K, ? extends V> m) {
        CommonObjObjMapOps.putAll(this, m);
    }


    @Override
    public V replace(K key, V value) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            Object[] tab = table;
            V oldValue = (V) tab[index + 1];
            tab[index + 1] = value;
            return oldValue;
        } else {
            // key is absent
            return null;
        }
    }



    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            Object[] tab = table;
            if (nullableValueEquals((V) tab[index + 1], (V) oldValue)) {
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
            BiFunction<? super K, ? super V, ? extends V> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        Object[] tab = table;
        if (noRemoved()) {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE) {
                    tab[i + 1] = function.apply(key, (V) tab[i + 1]);
                }
            }
        } else {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                    tab[i + 1] = function.apply(key, (V) tab[i + 1]);
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
        table[index + 1] = null;
        postRemoveHook();
    }

    @Override
    public V remove(Object key) {
        if (key != null) {
            // noinspection unchecked
            K k = (K) key;
            Object[] tab = table;
            int capacity, index;
            K cur;
            keyPresent:
            if ((cur = (K) tab[index = ParallelKVObjKeyMixing.mix(keyHashCode(k)) % (capacity = tab.length)]) != k) {
                if (cur == FREE) {
                    // key is absent, free slot
                    return null;
                } else {
                    if (cur != REMOVED) {
                        if (keyEquals(k, cur)) {
                            break keyPresent;
                        } else {
                            if (noRemoved()) {
                                int bIndex = index, fIndex = index, step = 2;
                                while (true) {
                                    if ((bIndex -= step) < 0) bIndex += capacity;
                                    if ((cur = (K) tab[bIndex]) == k) {
                                        index = bIndex;
                                        break keyPresent;
                                    } else if (cur == FREE) {
                                        // key is absent, free slot
                                        return null;
                                    }
                                    else if (keyEquals(k, cur)) {
                                        index = bIndex;
                                        break keyPresent;
                                    }
                                    int t;
                                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                                    if ((cur = (K) tab[fIndex]) == k) {
                                        index = fIndex;
                                        break keyPresent;
                                    } else if (cur == FREE) {
                                        // key is absent, free slot
                                        return null;
                                    }
                                    else if (keyEquals(k, cur)) {
                                        index = fIndex;
                                        break keyPresent;
                                    }
                                    step += 4;
                                }
                            }
                        }
                    }
                    int bIndex = index, fIndex = index, step = 2;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (K) tab[bIndex]) == k) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == FREE) {
                            // key is absent, free slot
                            return null;
                        }
                        else if (cur != REMOVED && keyEquals(k, cur)) {
                            index = bIndex;
                            break keyPresent;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (K) tab[fIndex]) == k) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == FREE) {
                            // key is absent, free slot
                            return null;
                        }
                        else if (cur != REMOVED && keyEquals(k, cur)) {
                            index = fIndex;
                            break keyPresent;
                        }
                        step += 4;
                    }
                }
            }
            // key is present
            V val = (V) tab[index + 1];
            incrementModCount();
            tab[index] = REMOVED;
            tab[index + 1] = null;
            postRemoveHook();
            return val;
        } else {
            return removeNullKey();
        }
    }

    V removeNullKey() {
        Object[] tab = table;
        int capacity = tab.length;
        int index;
        K cur;
        keyPresent:
        if ((cur = (K) tab[index = 0]) != null) {
            if (cur == FREE) {
                // key is absent, free slot
                return null;
            } else {
                int bIndex = index, fIndex = index, step = 2;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (K) tab[bIndex]) == null) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        // key is absent, free slot
                        return null;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (K) tab[fIndex]) == null) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        // key is absent, free slot
                        return null;
                    }
                    step += 4;
                }
            }
        }
        // key is present
        V val = (V) tab[index + 1];
        incrementModCount();
        tab[index] = REMOVED;
        tab[index + 1] = null;
        postRemoveHook();
        return val;
    }

    @Override
    public boolean justRemove(Object key) {
        if (key != null) {
            // noinspection unchecked
            K k = (K) key;
            Object[] tab = table;
            int capacity, index;
            K cur;
            keyPresent:
            if ((cur = (K) tab[index = ParallelKVObjKeyMixing.mix(keyHashCode(k)) % (capacity = tab.length)]) != k) {
                if (cur == FREE) {
                    // key is absent, free slot
                    return false;
                } else {
                    if (cur != REMOVED) {
                        if (keyEquals(k, cur)) {
                            break keyPresent;
                        } else {
                            if (noRemoved()) {
                                int bIndex = index, fIndex = index, step = 2;
                                while (true) {
                                    if ((bIndex -= step) < 0) bIndex += capacity;
                                    if ((cur = (K) tab[bIndex]) == k) {
                                        index = bIndex;
                                        break keyPresent;
                                    } else if (cur == FREE) {
                                        // key is absent, free slot
                                        return false;
                                    }
                                    else if (keyEquals(k, cur)) {
                                        index = bIndex;
                                        break keyPresent;
                                    }
                                    int t;
                                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                                    if ((cur = (K) tab[fIndex]) == k) {
                                        index = fIndex;
                                        break keyPresent;
                                    } else if (cur == FREE) {
                                        // key is absent, free slot
                                        return false;
                                    }
                                    else if (keyEquals(k, cur)) {
                                        index = fIndex;
                                        break keyPresent;
                                    }
                                    step += 4;
                                }
                            }
                        }
                    }
                    int bIndex = index, fIndex = index, step = 2;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (K) tab[bIndex]) == k) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == FREE) {
                            // key is absent, free slot
                            return false;
                        }
                        else if (cur != REMOVED && keyEquals(k, cur)) {
                            index = bIndex;
                            break keyPresent;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (K) tab[fIndex]) == k) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == FREE) {
                            // key is absent, free slot
                            return false;
                        }
                        else if (cur != REMOVED && keyEquals(k, cur)) {
                            index = fIndex;
                            break keyPresent;
                        }
                        step += 4;
                    }
                }
            }
            // key is present
            incrementModCount();
            tab[index] = REMOVED;
            tab[index + 1] = null;
            postRemoveHook();
            return true;
        } else {
            return justRemoveNullKey();
        }
    }

    boolean justRemoveNullKey() {
        Object[] tab = table;
        int capacity = tab.length;
        int index;
        K cur;
        keyPresent:
        if ((cur = (K) tab[index = 0]) != null) {
            if (cur == FREE) {
                // key is absent, free slot
                return false;
            } else {
                int bIndex = index, fIndex = index, step = 2;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (K) tab[bIndex]) == null) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        // key is absent, free slot
                        return false;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (K) tab[fIndex]) == null) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        // key is absent, free slot
                        return false;
                    }
                    step += 4;
                }
            }
        }
        // key is present
        incrementModCount();
        tab[index] = REMOVED;
        tab[index + 1] = null;
        postRemoveHook();
        return true;
    }


    




    @Override
    public boolean remove(Object key, Object value) {
        if (key != null) {
            // noinspection unchecked
            K k = (K) key;
            Object[] tab = table;
            int capacity, index;
            K cur;
            keyPresent:
            if ((cur = (K) tab[index = ParallelKVObjKeyMixing.mix(keyHashCode(k)) % (capacity = tab.length)]) != k) {
                if (cur == FREE) {
                    // key is absent, free slot
                    return false;
                } else {
                    if (cur != REMOVED) {
                        if (keyEquals(k, cur)) {
                            break keyPresent;
                        } else {
                            if (noRemoved()) {
                                int bIndex = index, fIndex = index, step = 2;
                                while (true) {
                                    if ((bIndex -= step) < 0) bIndex += capacity;
                                    if ((cur = (K) tab[bIndex]) == k) {
                                        index = bIndex;
                                        break keyPresent;
                                    } else if (cur == FREE) {
                                        // key is absent, free slot
                                        return false;
                                    }
                                    else if (keyEquals(k, cur)) {
                                        index = bIndex;
                                        break keyPresent;
                                    }
                                    int t;
                                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                                    if ((cur = (K) tab[fIndex]) == k) {
                                        index = fIndex;
                                        break keyPresent;
                                    } else if (cur == FREE) {
                                        // key is absent, free slot
                                        return false;
                                    }
                                    else if (keyEquals(k, cur)) {
                                        index = fIndex;
                                        break keyPresent;
                                    }
                                    step += 4;
                                }
                            }
                        }
                    }
                    int bIndex = index, fIndex = index, step = 2;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (K) tab[bIndex]) == k) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == FREE) {
                            // key is absent, free slot
                            return false;
                        }
                        else if (cur != REMOVED && keyEquals(k, cur)) {
                            index = bIndex;
                            break keyPresent;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (K) tab[fIndex]) == k) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == FREE) {
                            // key is absent, free slot
                            return false;
                        }
                        else if (cur != REMOVED && keyEquals(k, cur)) {
                            index = fIndex;
                            break keyPresent;
                        }
                        step += 4;
                    }
                }
            }
            // key is present
            if (nullableValueEquals((V) tab[index + 1], (V) value)) {
                incrementModCount();
                tab[index] = REMOVED;
                tab[index + 1] = null;
                postRemoveHook();
                return true;
            } else {
                return false;
            }
        } else {
            return removeEntryNullKey(value);
        }
    }

    boolean removeEntryNullKey(Object value) {
        Object[] tab = table;
        int capacity = tab.length;
        int index;
        K cur;
        keyPresent:
        if ((cur = (K) tab[index = 0]) != null) {
            if (cur == FREE) {
                // key is absent, free slot
                return false;
            } else {
                int bIndex = index, fIndex = index, step = 2;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (K) tab[bIndex]) == null) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        // key is absent, free slot
                        return false;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (K) tab[fIndex]) == null) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        // key is absent, free slot
                        return false;
                    }
                    step += 4;
                }
            }
        }
        // key is present
        if (nullableValueEquals((V) tab[index + 1], (V) value)) {
            incrementModCount();
            tab[index] = REMOVED;
            tab[index + 1] = null;
            postRemoveHook();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeIf(BiPredicate<? super K, ? super V> filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        Object[] tab = table;
        if (noRemoved()) {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE) {
                    if (filter.test(key, (V) tab[i + 1])) {
                        incrementModCount();
                        mc++;
                        tab[i] = REMOVED;
                        tab[i + 1] = null;
                        postRemoveHook();
                        changed = true;
                    }
                }
            }
        } else {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                    if (filter.test(key, (V) tab[i + 1])) {
                        incrementModCount();
                        mc++;
                        tab[i] = REMOVED;
                        tab[i + 1] = null;
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




    // under this condition - operations, overridden from MutableParallelKVObjQHashGO
    // when values are objects - in order to set values to null on removing (for garbage collection)
    // when algo is LHash - because shift deletion should shift values to

    @Override
    public boolean removeIf(Predicate<? super K> filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        Object[] tab = table;
        if (noRemoved()) {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE) {
                    if (filter.test(key)) {
                        incrementModCount();
                        mc++;
                        tab[i] = REMOVED;
                        tab[i + 1] = null;
                        postRemoveHook();
                        changed = true;
                    }
                }
            }
        } else {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                    if (filter.test(key)) {
                        incrementModCount();
                        mc++;
                        tab[i] = REMOVED;
                        tab[i + 1] = null;
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
    public boolean removeAll(@Nonnull HashObjSet<K> thisC, @Nonnull Collection<?> c) {
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        Object[] tab = table;
        if (noRemoved()) {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE) {
                    if (c.contains(key)) {
                        incrementModCount();
                        mc++;
                        tab[i] = REMOVED;
                        tab[i + 1] = null;
                        postRemoveHook();
                        changed = true;
                    }
                }
            }
        } else {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                    if (c.contains(key)) {
                        incrementModCount();
                        mc++;
                        tab[i] = REMOVED;
                        tab[i + 1] = null;
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
    public boolean retainAll(@Nonnull HashObjSet<K> thisC, @Nonnull Collection<?> c) {
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
        Object[] tab = table;
        if (noRemoved()) {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE) {
                    if (!c.contains(key)) {
                        incrementModCount();
                        mc++;
                        tab[i] = REMOVED;
                        tab[i + 1] = null;
                        postRemoveHook();
                        changed = true;
                    }
                }
            }
        } else {
            for (int i = tab.length - 2; i >= 0; i -= 2) {
                K key;
                // noinspection unchecked
                if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                    if (!c.contains(key)) {
                        incrementModCount();
                        mc++;
                        tab[i] = REMOVED;
                        tab[i + 1] = null;
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
    public ObjIterator<K> iterator() {
        int mc = modCount();
        if (!noRemoved())
            return new SomeRemovedKeyIterator(mc);
        return new NoRemovedKeyIterator(mc);
    }

    @Override
    public ObjCursor<K> setCursor() {
        int mc = modCount();
        if (!noRemoved())
            return new SomeRemovedKeyCursor(mc);
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
                    incrementModCount();
                    tab[index] = REMOVED;
                    tab[index + 1] = null;
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

        private NoRemovedKeyCursor(int mc) {
            super(mc);
            
        }

        @Override
        public void remove() {
            if (curKey != FREE) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE;
                    incrementModCount();
                    int index;
                    tab[index = this.index] = REMOVED;
                    tab[index + 1] = null;
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

        private SomeRemovedKeyIterator(int mc) {
            super(mc);
            
        }

        @Override
        public void remove() {
            int index;
            if ((index = this.index) >= 0) {
                if (expectedModCount++ == modCount()) {
                    this.index = -1;
                    incrementModCount();
                    tab[index] = REMOVED;
                    tab[index + 1] = null;
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

        private SomeRemovedKeyCursor(int mc) {
            super(mc);
            
        }

        @Override
        public void remove() {
            if (curKey != FREE) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE;
                    incrementModCount();
                    int index;
                    tab[index = this.index] = REMOVED;
                    tab[index + 1] = null;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }





    class EntryView extends AbstractSetView<Map.Entry<K, V>>
            implements HashObjSet<Map.Entry<K, V>>,
            InternalObjCollectionOps<Map.Entry<K, V>> {

        @Nonnull
        @Override
        public Equivalence<Entry<K, V>> equivalence() {
            return Equivalence.entryEquivalence(
                    keyEquivalence(),
                    valueEquivalence()
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return MutableQHashParallelKVObjObjMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return MutableQHashParallelKVObjObjMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return MutableQHashParallelKVObjObjMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<K, V> e = (Map.Entry<K, V>) o;
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
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE) {
                        result[resultIndex++] = new MutableEntry(mc, i, key, (V) tab[i + 1]);
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                        result[resultIndex++] = new MutableEntry(mc, i, key, (V) tab[i + 1]);
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
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE) {
                        a[resultIndex++] = (T) new MutableEntry(mc, i, key, (V) tab[i + 1]);
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                        a[resultIndex++] = (T) new MutableEntry(mc, i, key, (V) tab[i + 1]);
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
        public final void forEach(@Nonnull Consumer<? super Map.Entry<K, V>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE) {
                        action.accept(new MutableEntry(mc, i, key, (V) tab[i + 1]));
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                        action.accept(new MutableEntry(mc, i, key, (V) tab[i + 1]));
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<K, V>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE) {
                        if (!predicate.test(new MutableEntry(mc, i, key, (V) tab[i + 1]))) {
                            terminated = true;
                            break;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                        if (!predicate.test(new MutableEntry(mc, i, key, (V) tab[i + 1]))) {
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
        public ObjIterator<Map.Entry<K, V>> iterator() {
            int mc = modCount();
            if (!noRemoved())
                return new SomeRemovedEntryIterator(mc);
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<K, V>> cursor() {
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
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE) {
                        if (!c.contains(e.with(key, (V) tab[i + 1]))) {
                            containsAll = false;
                            break;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                        if (!c.contains(e.with(key, (V) tab[i + 1]))) {
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
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE) {
                        changed |= s.remove(e.with(key, (V) tab[i + 1]));
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                        changed |= s.remove(e.with(key, (V) tab[i + 1]));
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<K, V>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE) {
                        changed |= c.add(new MutableEntry(mc, i, key, (V) tab[i + 1]));
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                        changed |= c.add(new MutableEntry(mc, i, key, (V) tab[i + 1]));
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        public int hashCode() {
            return MutableQHashParallelKVObjObjMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE) {
                        sb.append(' ');
                        sb.append(key != this ? key : "(this Collection)");
                        sb.append('=');
                        Object val = (V) tab[i + 1];
                        sb.append(val != this ? val : "(this Collection)");
                        sb.append(',');
                        if (++elementCount == 8) {
                            int expectedLength = sb.length() * (size() / 8);
                            sb.ensureCapacity(expectedLength + (expectedLength / 2));
                        }
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                        sb.append(' ');
                        sb.append(key != this ? key : "(this Collection)");
                        sb.append('=');
                        Object val = (V) tab[i + 1];
                        sb.append(val != this ? val : "(this Collection)");
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
            return MutableQHashParallelKVObjObjMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<K, V> e = (Map.Entry<K, V>) o;
                K key = e.getKey();
                V value = e.getValue();
                return MutableQHashParallelKVObjObjMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<K, V>> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE) {
                        if (filter.test(new MutableEntry(mc, i, key, (V) tab[i + 1]))) {
                            incrementModCount();
                            mc++;
                            tab[i] = REMOVED;
                            tab[i + 1] = null;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                        if (filter.test(new MutableEntry(mc, i, key, (V) tab[i + 1]))) {
                            incrementModCount();
                            mc++;
                            tab[i] = REMOVED;
                            tab[i + 1] = null;
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
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE) {
                        if (c.contains(e.with(key, (V) tab[i + 1]))) {
                            incrementModCount();
                            mc++;
                            tab[i] = REMOVED;
                            tab[i + 1] = null;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                        if (c.contains(e.with(key, (V) tab[i + 1]))) {
                            incrementModCount();
                            mc++;
                            tab[i] = REMOVED;
                            tab[i + 1] = null;
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
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE) {
                        if (!c.contains(e.with(key, (V) tab[i + 1]))) {
                            incrementModCount();
                            mc++;
                            tab[i] = REMOVED;
                            tab[i + 1] = null;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) (K) tab[i]) != FREE && key != REMOVED) {
                        if (!c.contains(e.with(key, (V) tab[i + 1]))) {
                            incrementModCount();
                            mc++;
                            tab[i] = REMOVED;
                            tab[i + 1] = null;
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
            MutableQHashParallelKVObjObjMapGO.this.clear();
        }
    }


    abstract class ObjObjEntry extends AbstractEntry<K, V> {

        abstract K key();

        @Override
        public final K getKey() {
            return key();
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
            K k2;
            V v2;
            try {
                e2 = (Map.Entry) o;
                k2 = (K) e2.getKey();
                v2 = (V) e2.getValue();
                return nullableKeyEquals(key(), k2)
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
            return nullableKeyHashCode(key())
                    ^
                    nullableValueHashCode(value());
        }
    }


    class MutableEntry extends ObjObjEntry {
        final int modCount;
        private final int index;
        final K key;
        private V value;

        MutableEntry(int modCount, int index, K key, V value) {
            this.modCount = modCount;
            this.index = index;
            this.key = key;
            this.value = value;
        }

        @Override
        public K key() {
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
            table[index + 1] = newValue;
        }
    }



    class ReusableEntry extends ObjObjEntry {
        private K key;
        private V value;

        ReusableEntry with(K key, V value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public K key() {
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
            return MutableQHashParallelKVObjObjMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return MutableQHashParallelKVObjObjMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return MutableQHashParallelKVObjObjMapGO.this.containsValue(o);
        }



        @Override
        public void forEach(Consumer<? super V> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    // noinspection unchecked
                    if ((K) tab[i] != FREE) {
                        action.accept((V) tab[i + 1]);
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    Object key;
                    // noinspection unchecked
                    if ((key = (K) tab[i]) != FREE && key != REMOVED) {
                        action.accept((V) tab[i + 1]);
                    }
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
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    // noinspection unchecked
                    if ((K) tab[i] != FREE) {
                        if (!predicate.test((V) tab[i + 1])) {
                            terminated = true;
                            break;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    Object key;
                    // noinspection unchecked
                    if ((key = (K) tab[i]) != FREE && key != REMOVED) {
                        if (!predicate.test((V) tab[i + 1])) {
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
        public boolean allContainingIn(ObjCollection<?> c) {
            if (isEmpty())
                return true;
            boolean containsAll = true;
            int mc = modCount();
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    // noinspection unchecked
                    if ((K) tab[i] != FREE) {
                        if (!c.contains((V) tab[i + 1])) {
                            containsAll = false;
                            break;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    Object key;
                    // noinspection unchecked
                    if ((key = (K) tab[i]) != FREE && key != REMOVED) {
                        if (!c.contains((V) tab[i + 1])) {
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
        public boolean reverseAddAllTo(ObjCollection<? super V> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    // noinspection unchecked
                    if ((K) tab[i] != FREE) {
                        changed |= c.add((V) tab[i + 1]);
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    Object key;
                    // noinspection unchecked
                    if ((key = (K) tab[i]) != FREE && key != REMOVED) {
                        changed |= c.add((V) tab[i + 1]);
                    }
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
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    // noinspection unchecked
                    if ((K) tab[i] != FREE) {
                        changed |= s.remove((V) tab[i + 1]);
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    Object key;
                    // noinspection unchecked
                    if ((key = (K) tab[i]) != FREE && key != REMOVED) {
                        changed |= s.remove((V) tab[i + 1]);
                    }
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
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    // noinspection unchecked
                    if ((K) tab[i] != FREE) {
                        result[resultIndex++] = (V) tab[i + 1];
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    Object key;
                    // noinspection unchecked
                    if ((key = (K) tab[i]) != FREE && key != REMOVED) {
                        result[resultIndex++] = (V) tab[i + 1];
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
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    // noinspection unchecked
                    if ((K) tab[i] != FREE) {
                        a[resultIndex++] = (T) (V) tab[i + 1];
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    Object key;
                    // noinspection unchecked
                    if ((key = (K) tab[i]) != FREE && key != REMOVED) {
                        a[resultIndex++] = (T) (V) tab[i + 1];
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
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    // noinspection unchecked
                    if ((K) tab[i] != FREE) {
                    V val;
                        sb.append(' ').append((val = (V) tab[i + 1]) != this ? val : "(this Collection)").append(',');
                        if (++elementCount == 8) {
                            int expectedLength = sb.length() * (size() / 8);
                            sb.ensureCapacity(expectedLength + (expectedLength / 2));
                        }
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    Object key;
                    // noinspection unchecked
                    if ((key = (K) tab[i]) != FREE && key != REMOVED) {
                    V val;
                        sb.append(' ').append((val = (V) tab[i + 1]) != this ? val : "(this Collection)").append(',');
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
            return removeValue(o);
        }



        @Override
        public void clear() {
            MutableQHashParallelKVObjObjMapGO.this.clear();
        }

        @Override
        public boolean removeIf(Predicate<? super V> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    // noinspection unchecked
                    if ((K) tab[i] != FREE) {
                        if (filter.test((V) tab[i + 1])) {
                            incrementModCount();
                            mc++;
                            tab[i] = REMOVED;
                            tab[i + 1] = null;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    Object key;
                    // noinspection unchecked
                    if ((key = (K) tab[i]) != FREE && key != REMOVED) {
                        if (filter.test((V) tab[i + 1])) {
                            incrementModCount();
                            mc++;
                            tab[i] = REMOVED;
                            tab[i + 1] = null;
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
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    // noinspection unchecked
                    if ((K) tab[i] != FREE) {
                        if (c.contains((V) tab[i + 1])) {
                            incrementModCount();
                            mc++;
                            tab[i] = REMOVED;
                            tab[i + 1] = null;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    Object key;
                    // noinspection unchecked
                    if ((key = (K) tab[i]) != FREE && key != REMOVED) {
                        if (c.contains((V) tab[i + 1])) {
                            incrementModCount();
                            mc++;
                            tab[i] = REMOVED;
                            tab[i + 1] = null;
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
            Object[] tab = table;
            if (noRemoved()) {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    // noinspection unchecked
                    if ((K) tab[i] != FREE) {
                        if (!c.contains((V) tab[i + 1])) {
                            incrementModCount();
                            mc++;
                            tab[i] = REMOVED;
                            tab[i + 1] = null;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 2; i >= 0; i -= 2) {
                    Object key;
                    // noinspection unchecked
                    if ((key = (K) tab[i]) != FREE && key != REMOVED) {
                        if (!c.contains((V) tab[i + 1])) {
                            incrementModCount();
                            mc++;
                            tab[i] = REMOVED;
                            tab[i + 1] = null;
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<K, V>> {
        final Object[] tab;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        MutableEntry next;

        NoRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            Object[] tab = this.tab = table;
            int nextI = tab.length;
            while ((nextI -= 2) >= 0) {
                Object key;
                if ((key = (K) tab[nextI]) != FREE) {
                    // noinspection unchecked
                    next = new MutableEntry(mc, nextI, (K) key, (V) tab[nextI + 1]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<K, V>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            Object[] tab = this.tab;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i -= 2) {
                Object key;
                if ((key = (K) tab[i]) != FREE) {
                    // noinspection unchecked
                    action.accept(new MutableEntry(mc, i, (K) key, (V) tab[i + 1]));
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
        public Map.Entry<K, V> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
                    index = nextI;
                    Object[] tab = this.tab;
                    MutableEntry prev = next;
                    while ((nextI -= 2) >= 0) {
                        Object key;
                        if ((key = (K) tab[nextI]) != FREE) {
                            // noinspection unchecked
                            next = new MutableEntry(mc, nextI, (K) key, (V) tab[nextI + 1]);
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
                    tab[index] = REMOVED;
                    tab[index + 1] = null;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<K, V>> {
        final Object[] tab;
        int expectedModCount;
        int index;
        Object curKey;
        V curValue;

        NoRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            curKey = FREE;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<K, V>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            Object[] tab = this.tab;
            int index = this.index;
            for (int i = index - 2; i >= 0; i -= 2) {
                Object key;
                if ((key = (K) tab[i]) != FREE) {
                    // noinspection unchecked
                    action.accept(new MutableEntry(mc, i, (K) key, (V) tab[i + 1]));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE;
        }

        @Override
        public Map.Entry<K, V> elem() {
            Object curKey;
            if ((curKey = this.curKey) != FREE) {
                // noinspection unchecked
                return new MutableEntry(expectedModCount, index, (K) curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                Object[] tab = this.tab;
                for (int i = index - 2; i >= 0; i -= 2) {
                    Object key;
                    if ((key = (K) tab[i]) != FREE) {
                        index = i;
                        curKey = key;
                        curValue = (V) tab[i + 1];
                        return true;
                    }
                }
                curKey = FREE;
                index = -1;
                return false;
            } else {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            if (curKey != FREE) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE;
                    incrementModCount();
                    int index;
                    tab[index = this.index] = REMOVED;
                    tab[index + 1] = null;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedEntryIterator implements ObjIterator<Map.Entry<K, V>> {
        final Object[] tab;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        MutableEntry next;

        SomeRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            Object[] tab = this.tab = table;
            int nextI = tab.length;
            while ((nextI -= 2) >= 0) {
                Object key;
                if ((key = (K) tab[nextI]) != FREE && key != REMOVED) {
                    // noinspection unchecked
                    next = new MutableEntry(mc, nextI, (K) key, (V) tab[nextI + 1]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<K, V>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            Object[] tab = this.tab;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i -= 2) {
                Object key;
                if ((key = (K) tab[i]) != FREE && key != REMOVED) {
                    // noinspection unchecked
                    action.accept(new MutableEntry(mc, i, (K) key, (V) tab[i + 1]));
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
        public Map.Entry<K, V> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
                    index = nextI;
                    Object[] tab = this.tab;
                    MutableEntry prev = next;
                    while ((nextI -= 2) >= 0) {
                        Object key;
                        if ((key = (K) tab[nextI]) != FREE && key != REMOVED) {
                            // noinspection unchecked
                            next = new MutableEntry(mc, nextI, (K) key, (V) tab[nextI + 1]);
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
                    tab[index] = REMOVED;
                    tab[index + 1] = null;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedEntryCursor implements ObjCursor<Map.Entry<K, V>> {
        final Object[] tab;
        int expectedModCount;
        int index;
        Object curKey;
        V curValue;

        SomeRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            curKey = FREE;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<K, V>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            Object[] tab = this.tab;
            int index = this.index;
            for (int i = index - 2; i >= 0; i -= 2) {
                Object key;
                if ((key = (K) tab[i]) != FREE && key != REMOVED) {
                    // noinspection unchecked
                    action.accept(new MutableEntry(mc, i, (K) key, (V) tab[i + 1]));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE;
        }

        @Override
        public Map.Entry<K, V> elem() {
            Object curKey;
            if ((curKey = this.curKey) != FREE) {
                // noinspection unchecked
                return new MutableEntry(expectedModCount, index, (K) curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                Object[] tab = this.tab;
                for (int i = index - 2; i >= 0; i -= 2) {
                    Object key;
                    if ((key = (K) tab[i]) != FREE && key != REMOVED) {
                        index = i;
                        curKey = key;
                        curValue = (V) tab[i + 1];
                        return true;
                    }
                }
                curKey = FREE;
                index = -1;
                return false;
            } else {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            if (curKey != FREE) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE;
                    incrementModCount();
                    int index;
                    tab[index = this.index] = REMOVED;
                    tab[index + 1] = null;
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
        final Object[] tab;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        V next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            Object[] tab = this.tab = table;
            int nextI = tab.length;
            while ((nextI -= 2) >= 0) {
                if ((K) tab[nextI] != FREE) {
                    // noinspection unchecked
                    next = (V) tab[nextI + 1];
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
            Object[] tab = this.tab;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i -= 2) {
                if ((K) tab[i] != FREE) {
                    // noinspection unchecked
                    action.accept((V) tab[i + 1]);
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
                    Object[] tab = this.tab;
                    V prev = next;
                    while ((nextI -= 2) >= 0) {
                        if ((K) tab[nextI] != FREE) {
                            // noinspection unchecked
                            next = (V) tab[nextI + 1];
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
                    tab[index] = REMOVED;
                    tab[index + 1] = null;
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
        final Object[] tab;
        int expectedModCount;
        int index;
        Object curKey;
        V curValue;

        NoRemovedValueCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            curKey = FREE;
        }

        @Override
        public void forEachForward(Consumer<? super V> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            Object[] tab = this.tab;
            int index = this.index;
            for (int i = index - 2; i >= 0; i -= 2) {
                if ((K) tab[i] != FREE) {
                    // noinspection unchecked
                    action.accept((V) tab[i + 1]);
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE;
        }

        @Override
        public V elem() {
            if (curKey != FREE) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                Object[] tab = this.tab;
                for (int i = index - 2; i >= 0; i -= 2) {
                    Object key;
                    if ((key = (K) tab[i]) != FREE) {
                        index = i;
                        curKey = key;
                        curValue = (V) tab[i + 1];
                        return true;
                    }
                }
                curKey = FREE;
                index = -1;
                return false;
            } else {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            if (curKey != FREE) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE;
                    incrementModCount();
                    int index;
                    tab[index = this.index] = REMOVED;
                    tab[index + 1] = null;
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
        final Object[] tab;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        V next;

        SomeRemovedValueIterator(int mc) {
            expectedModCount = mc;
            Object[] tab = this.tab = table;
            int nextI = tab.length;
            while ((nextI -= 2) >= 0) {
                Object key;
                if ((key = (K) tab[nextI]) != FREE && key != REMOVED) {
                    // noinspection unchecked
                    next = (V) tab[nextI + 1];
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
            Object[] tab = this.tab;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i -= 2) {
                Object key;
                if ((key = (K) tab[i]) != FREE && key != REMOVED) {
                    // noinspection unchecked
                    action.accept((V) tab[i + 1]);
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
                    Object[] tab = this.tab;
                    V prev = next;
                    while ((nextI -= 2) >= 0) {
                        Object key;
                        if ((key = (K) tab[nextI]) != FREE && key != REMOVED) {
                            // noinspection unchecked
                            next = (V) tab[nextI + 1];
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
                    tab[index] = REMOVED;
                    tab[index + 1] = null;
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
        final Object[] tab;
        int expectedModCount;
        int index;
        Object curKey;
        V curValue;

        SomeRemovedValueCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            curKey = FREE;
        }

        @Override
        public void forEachForward(Consumer<? super V> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            Object[] tab = this.tab;
            int index = this.index;
            for (int i = index - 2; i >= 0; i -= 2) {
                Object key;
                if ((key = (K) tab[i]) != FREE && key != REMOVED) {
                    // noinspection unchecked
                    action.accept((V) tab[i + 1]);
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE;
        }

        @Override
        public V elem() {
            if (curKey != FREE) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                Object[] tab = this.tab;
                for (int i = index - 2; i >= 0; i -= 2) {
                    Object key;
                    if ((key = (K) tab[i]) != FREE && key != REMOVED) {
                        index = i;
                        curKey = key;
                        curValue = (V) tab[i + 1];
                        return true;
                    }
                }
                curKey = FREE;
                index = -1;
                return false;
            } else {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            if (curKey != FREE) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE;
                    incrementModCount();
                    int index;
                    tab[index = this.index] = REMOVED;
                    tab[index + 1] = null;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }



    class NoRemovedMapCursor implements ObjObjCursor<K, V> {
        final Object[] tab;
        int expectedModCount;
        int index;
        Object curKey;
        V curValue;

        NoRemovedMapCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            curKey = FREE;
        }

        @Override
        public void forEachForward(BiConsumer<? super K, ? super V> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            Object[] tab = this.tab;
            int index = this.index;
            for (int i = index - 2; i >= 0; i -= 2) {
                Object key;
                if ((key = (K) tab[i]) != FREE) {
                    // noinspection unchecked
                    action.accept((K) key, (V) tab[i + 1]);
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE;
        }

        @Override
        public K key() {
            Object curKey;
            if ((curKey = this.curKey) != FREE) {
                // noinspection unchecked
                return (K) curKey;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public V value() {
            if (curKey != FREE) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(V value) {
            if (curKey != FREE) {
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
                Object[] tab = this.tab;
                for (int i = index - 2; i >= 0; i -= 2) {
                    Object key;
                    if ((key = (K) tab[i]) != FREE) {
                        index = i;
                        curKey = key;
                        curValue = (V) tab[i + 1];
                        return true;
                    }
                }
                curKey = FREE;
                index = -1;
                return false;
            } else {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            if (curKey != FREE) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE;
                    incrementModCount();
                    int index;
                    tab[index = this.index] = REMOVED;
                    tab[index + 1] = null;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }

    class SomeRemovedMapCursor implements ObjObjCursor<K, V> {
        final Object[] tab;
        int expectedModCount;
        int index;
        Object curKey;
        V curValue;

        SomeRemovedMapCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            curKey = FREE;
        }

        @Override
        public void forEachForward(BiConsumer<? super K, ? super V> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            Object[] tab = this.tab;
            int index = this.index;
            for (int i = index - 2; i >= 0; i -= 2) {
                Object key;
                if ((key = (K) tab[i]) != FREE && key != REMOVED) {
                    // noinspection unchecked
                    action.accept((K) key, (V) tab[i + 1]);
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE;
        }

        @Override
        public K key() {
            Object curKey;
            if ((curKey = this.curKey) != FREE) {
                // noinspection unchecked
                return (K) curKey;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public V value() {
            if (curKey != FREE) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(V value) {
            if (curKey != FREE) {
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
                Object[] tab = this.tab;
                for (int i = index - 2; i >= 0; i -= 2) {
                    Object key;
                    if ((key = (K) tab[i]) != FREE && key != REMOVED) {
                        index = i;
                        curKey = key;
                        curValue = (V) tab[i + 1];
                        return true;
                    }
                }
                curKey = FREE;
                index = -1;
                return false;
            } else {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            if (curKey != FREE) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE;
                    incrementModCount();
                    int index;
                    tab[index = this.index] = REMOVED;
                    tab[index + 1] = null;
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
