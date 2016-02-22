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
import net.openhft.koloboke.function.ObjCharConsumer;
import net.openhft.koloboke.function.ObjCharPredicate;
import net.openhft.koloboke.function.ObjCharToCharFunction;
import net.openhft.koloboke.function.ToCharFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.openhft.koloboke.function.CharBinaryOperator;
import net.openhft.koloboke.function.CharConsumer;
import net.openhft.koloboke.function.CharPredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class MutableQHashSeparateKVObjCharMapGO<K>
        extends MutableQHashSeparateKVObjCharMapSO<K> {

    @Override
    final void copy(SeparateKVObjCharQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVObjCharQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.move(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }


    @Override
    public char defaultValue() {
        return (char) 0;
    }

    @Override
    public boolean containsEntry(Object key, char value) {
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
    public Character get(Object key) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return null;
        }
    }

    

    @Override
    public char getChar(Object key) {
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
    public Character getOrDefault(Object key, Character defaultValue) {
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
    public char getOrDefault(Object key, char defaultValue) {
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
    public void forEach(BiConsumer<? super K, ? super Character> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        Object[] keys = set;
        char[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    action.accept(key, vals[i]);
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE && key != REMOVED) {
                    action.accept(key, vals[i]);
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void forEach(ObjCharConsumer<? super K> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        Object[] keys = set;
        char[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    action.accept(key, vals[i]);
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE && key != REMOVED) {
                    action.accept(key, vals[i]);
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public boolean forEachWhile(ObjCharPredicate<? super K> predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        Object[] keys = set;
        char[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    if (!predicate.test(key, vals[i])) {
                        terminated = true;
                        break;
                    }
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE && key != REMOVED) {
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
    public ObjCharCursor<K> cursor() {
        int mc = modCount();
        if (!noRemoved())
            return new SomeRemovedMapCursor(mc);
        return new NoRemovedMapCursor(mc);
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonObjCharMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalObjCharMapOps<?> m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        Object[] keys = set;
        char[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    if (!m.containsEntry(key, vals[i])) {
                        containsAll = false;
                        break;
                    }
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE && key != REMOVED) {
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
    public void reversePutAllTo(InternalObjCharMapOps<? super K> m) {
        if (isEmpty())
            return;
        int mc = modCount();
        Object[] keys = set;
        char[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    m.justPut(key, vals[i]);
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE && key != REMOVED) {
                    m.justPut(key, vals[i]);
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<K, Character>> entrySet() {
        return new EntryView();
    }

    @Override
    @Nonnull
    public CharCollection values() {
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
        Object[] keys = set;
        char[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    hashCode += nullableKeyHashCode(key) ^ vals[i];
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE && key != REMOVED) {
                    hashCode += nullableKeyHashCode(key) ^ vals[i];
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
        Object[] keys = set;
        char[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    sb.append(' ');
                    sb.append(key != this ? key : "(this Map)");
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
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE && key != REMOVED) {
                    sb.append(' ');
                    sb.append(key != this ? key : "(this Map)");
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
        Object[] keys = set;
        char[] vals = values;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        Object[] newKeys = set;
        int capacity = newKeys.length;
        char[] newVals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    int index;
                    if (newKeys[index = SeparateKVObjKeyMixing.mix(nullableKeyHashCode(key)) % capacity] != FREE) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if (newKeys[bIndex] == FREE) {
                                index = bIndex;
                                break;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if (newKeys[fIndex] == FREE) {
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
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE && key != REMOVED) {
                    int index;
                    if (newKeys[index = SeparateKVObjKeyMixing.mix(nullableKeyHashCode(key)) % capacity] != FREE) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if (newKeys[bIndex] == FREE) {
                                index = bIndex;
                                break;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if (newKeys[fIndex] == FREE) {
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
    public Character put(K key, Character value) {
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return null;
        } else {
            // key is present
            char[] vals = values;
            char prevValue = vals[index];
            vals[index] = value;
            return prevValue;
        }
    }

    @Override
    public char put(K key, char value) {
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return defaultValue();
        } else {
            // key is present
            char[] vals = values;
            char prevValue = vals[index];
            vals[index] = value;
            return prevValue;
        }
    }

    @Override
    public Character putIfAbsent(K key, Character value) {
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
    public char putIfAbsent(K key, char value) {
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
    public void justPut(K key, char value) {
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
    public Character compute(K key,
            BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
        if (key != null) {
            if (remappingFunction == null)
                throw new java.lang.NullPointerException();
            // noinspection unchecked
            K[] keys = (K[]) set;
            char[] vals = values;
            int capacity, index;
            K cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(key)) % (capacity = keys.length)]) != key) {
                keyAbsentFreeSlot:
                if (cur != FREE) {
                    int firstRemoved;
                    if (cur != REMOVED) {
                        if (keyEquals(key, cur)) {
                            break keyPresent;
                        } else {
                            if (noRemoved()) {
                                int bIndex = index, fIndex = index, step = 1;
                                while (true) {
                                    if ((bIndex -= step) < 0) bIndex += capacity;
                                    if ((cur = keys[bIndex]) == key) {
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
                                    if ((cur = keys[fIndex]) == key) {
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
                                    step += 2;
                                }
                            } else {
                                firstRemoved = -1;
                            }
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
                            if ((cur = keys[fIndex]) == key) {
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
                            step += 2;
                        }
                    }
                    // key is absent, removed slot
                    Character newValue = remappingFunction.apply(key, null);
                    if (newValue != null) {
                        incrementModCount();
                        keys[firstRemoved] = key;
                        vals[firstRemoved] = newValue;
                        postRemovedSlotInsertHook();
                        return newValue;
                    } else {
                        return null;
                    }
                }
                // key is absent, free slot
                Character newValue = remappingFunction.apply(key, null);
                if (newValue != null) {
                    incrementModCount();
                    keys[index] = key;
                    vals[index] = newValue;
                    postFreeSlotInsertHook();
                    return newValue;
                } else {
                    return null;
                }
            }
            // key is present
            Character newValue = remappingFunction.apply(key, vals[index]);
            if (newValue != null) {
                vals[index] = newValue;
                return newValue;
            } else {
                incrementModCount();
                ((Object[]) keys)[index] = REMOVED;
                postRemoveHook();
                return null;
            }
        } else {
            return computeNullKey(remappingFunction);
        }
    }

    Character computeNullKey(
            BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        // noinspection unchecked
        K[] keys = (K[]) set;
        char[] vals = values;
        int capacity = keys.length;
        int index;
        K cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            keyAbsentFreeSlot:
            if (cur != FREE) {
                int firstRemoved;
                if (cur != REMOVED) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == null) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == null) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
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
                        if ((cur = keys[bIndex]) == null) {
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
                        if ((cur = keys[fIndex]) == null) {
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
                        step += 2;
                    }
                }
                // key is absent, removed slot
                Character newValue = remappingFunction.apply(null, null);
                if (newValue != null) {
                    incrementModCount();
                    keys[firstRemoved] = null;
                    vals[firstRemoved] = newValue;
                    postRemovedSlotInsertHook();
                    return newValue;
                } else {
                    return null;
                }
            }
            // key is absent, free slot
            Character newValue = remappingFunction.apply(null, null);
            if (newValue != null) {
                incrementModCount();
                keys[index] = null;
                vals[index] = newValue;
                postFreeSlotInsertHook();
                return newValue;
            } else {
                return null;
            }
        }
        // key is present
        Character newValue = remappingFunction.apply(null, vals[index]);
        if (newValue != null) {
            vals[index] = newValue;
            return newValue;
        } else {
            incrementModCount();
            ((Object[]) keys)[index] = REMOVED;
            postRemoveHook();
            return null;
        }
    }

    @Override
    public char compute(K key, ObjCharToCharFunction<? super K> remappingFunction) {
        if (key != null) {
            if (remappingFunction == null)
                throw new java.lang.NullPointerException();
            // noinspection unchecked
            K[] keys = (K[]) set;
            char[] vals = values;
            int capacity, index;
            K cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(key)) % (capacity = keys.length)]) != key) {
                keyAbsentFreeSlot:
                if (cur != FREE) {
                    int firstRemoved;
                    if (cur != REMOVED) {
                        if (keyEquals(key, cur)) {
                            break keyPresent;
                        } else {
                            if (noRemoved()) {
                                int bIndex = index, fIndex = index, step = 1;
                                while (true) {
                                    if ((bIndex -= step) < 0) bIndex += capacity;
                                    if ((cur = keys[bIndex]) == key) {
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
                                    if ((cur = keys[fIndex]) == key) {
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
                                    step += 2;
                                }
                            } else {
                                firstRemoved = -1;
                            }
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
                            if ((cur = keys[fIndex]) == key) {
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
                            step += 2;
                        }
                    }
                    // key is absent, removed slot
                    char newValue = remappingFunction.applyAsChar(key, defaultValue());
                    incrementModCount();
                    keys[firstRemoved] = key;
                    vals[firstRemoved] = newValue;
                    postRemovedSlotInsertHook();
                    return newValue;
                }
                // key is absent, free slot
                char newValue = remappingFunction.applyAsChar(key, defaultValue());
                incrementModCount();
                keys[index] = key;
                vals[index] = newValue;
                postFreeSlotInsertHook();
                return newValue;
            }
            // key is present
            char newValue = remappingFunction.applyAsChar(key, vals[index]);
            vals[index] = newValue;
            return newValue;
        } else {
            return computeNullKey(remappingFunction);
        }
    }

    char computeNullKey(ObjCharToCharFunction<? super K> remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        // noinspection unchecked
        K[] keys = (K[]) set;
        char[] vals = values;
        int capacity = keys.length;
        int index;
        K cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            keyAbsentFreeSlot:
            if (cur != FREE) {
                int firstRemoved;
                if (cur != REMOVED) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == null) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == null) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
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
                        if ((cur = keys[bIndex]) == null) {
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
                        if ((cur = keys[fIndex]) == null) {
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
                        step += 2;
                    }
                }
                // key is absent, removed slot
                char newValue = remappingFunction.applyAsChar(null, defaultValue());
                incrementModCount();
                keys[firstRemoved] = null;
                vals[firstRemoved] = newValue;
                postRemovedSlotInsertHook();
                return newValue;
            }
            // key is absent, free slot
            char newValue = remappingFunction.applyAsChar(null, defaultValue());
            incrementModCount();
            keys[index] = null;
            vals[index] = newValue;
            postFreeSlotInsertHook();
            return newValue;
        }
        // key is present
        char newValue = remappingFunction.applyAsChar(null, vals[index]);
        vals[index] = newValue;
        return newValue;
    }

    @Override
    public Character computeIfAbsent(K key,
            Function<? super K, ? extends Character> mappingFunction) {
        if (key != null) {
            if (mappingFunction == null)
                throw new java.lang.NullPointerException();
            // noinspection unchecked
            K[] keys = (K[]) set;
            char[] vals = values;
            int capacity, index;
            K cur;
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(key)) % (capacity = keys.length)]) == key) {
                // key is present
                return vals[index];
            } else {
                keyAbsentFreeSlot:
                if (cur != FREE) {
                    int firstRemoved;
                    if (cur != REMOVED) {
                        if (keyEquals(key, cur)) {
                            // key is present
                            return vals[index];
                        } else {
                            if (noRemoved()) {
                                int bIndex = index, fIndex = index, step = 1;
                                while (true) {
                                    if ((bIndex -= step) < 0) bIndex += capacity;
                                    if ((cur = keys[bIndex]) == key) {
                                        // key is present
                                        return vals[bIndex];
                                    } else if (cur == FREE) {
                                        index = bIndex;
                                        break keyAbsentFreeSlot;
                                    }
                                    else if (keyEquals(key, cur)) {
                                        // key is present
                                        return vals[bIndex];
                                    }
                                    int t;
                                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                                    if ((cur = keys[fIndex]) == key) {
                                        // key is present
                                        return vals[fIndex];
                                    } else if (cur == FREE) {
                                        index = fIndex;
                                        break keyAbsentFreeSlot;
                                    }
                                    else if (keyEquals(key, cur)) {
                                        // key is present
                                        return vals[fIndex];
                                    }
                                    step += 2;
                                }
                            } else {
                                firstRemoved = -1;
                            }
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
                            } else if (cur == FREE) {
                                if (firstRemoved < 0) {
                                    index = bIndex;
                                    break keyAbsentFreeSlot;
                                } else {
                                    break keyAbsentRemovedSlot;
                                }
                            } else if (cur != REMOVED) {
                                if (keyEquals(key, cur)) {
                                    // key is present
                                    return vals[bIndex];
                                }
                            } else if (firstRemoved < 0) {
                                firstRemoved = bIndex;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == key) {
                                // key is present
                                return vals[fIndex];
                            } else if (cur == FREE) {
                                if (firstRemoved < 0) {
                                    index = fIndex;
                                    break keyAbsentFreeSlot;
                                } else {
                                    break keyAbsentRemovedSlot;
                                }
                            } else if (cur != REMOVED) {
                                if (keyEquals(key, cur)) {
                                    // key is present
                                    return vals[fIndex];
                                }
                            } else if (firstRemoved < 0) {
                                firstRemoved = fIndex;
                            }
                            step += 2;
                        }
                    }
                    // key is absent, removed slot
                    Character value = mappingFunction.apply(key);
                    if (value != null) {
                        incrementModCount();
                        keys[firstRemoved] = key;
                        vals[firstRemoved] = value;
                        postRemovedSlotInsertHook();
                        return value;
                    } else {
                        return null;
                    }
                }
                // key is absent, free slot
                Character value = mappingFunction.apply(key);
                if (value != null) {
                    incrementModCount();
                    keys[index] = key;
                    vals[index] = value;
                    postFreeSlotInsertHook();
                    return value;
                } else {
                    return null;
                }
            }
        } else {
            return computeIfAbsentNullKey(mappingFunction);
        }
    }

    Character computeIfAbsentNullKey(
            Function<? super K, ? extends Character> mappingFunction) {
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        // noinspection unchecked
        K[] keys = (K[]) set;
        char[] vals = values;
        int capacity = keys.length;
        int index;
        K cur;
        if ((cur = keys[index = 0]) == null) {
            // key is present
            return vals[index];
        } else {
            keyAbsentFreeSlot:
            if (cur != FREE) {
                int firstRemoved;
                if (cur != REMOVED) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == null) {
                                // key is present
                                return vals[bIndex];
                            } else if (cur == FREE) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == null) {
                                // key is present
                                return vals[fIndex];
                            } else if (cur == FREE) {
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
                        if ((cur = keys[bIndex]) == null) {
                            // key is present
                            return vals[bIndex];
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
                        if ((cur = keys[fIndex]) == null) {
                            // key is present
                            return vals[fIndex];
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
                        step += 2;
                    }
                }
                // key is absent, removed slot
                Character value = mappingFunction.apply(null);
                if (value != null) {
                    incrementModCount();
                    keys[firstRemoved] = null;
                    vals[firstRemoved] = value;
                    postRemovedSlotInsertHook();
                    return value;
                } else {
                    return null;
                }
            }
            // key is absent, free slot
            Character value = mappingFunction.apply(null);
            if (value != null) {
                incrementModCount();
                keys[index] = null;
                vals[index] = value;
                postFreeSlotInsertHook();
                return value;
            } else {
                return null;
            }
        }
    }

    @Override
    public char computeIfAbsent(K key, ToCharFunction<? super K> mappingFunction) {
        if (key != null) {
            if (mappingFunction == null)
                throw new java.lang.NullPointerException();
            // noinspection unchecked
            K[] keys = (K[]) set;
            char[] vals = values;
            int capacity, index;
            K cur;
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(key)) % (capacity = keys.length)]) == key) {
                // key is present
                return vals[index];
            } else {
                keyAbsentFreeSlot:
                if (cur != FREE) {
                    int firstRemoved;
                    if (cur != REMOVED) {
                        if (keyEquals(key, cur)) {
                            // key is present
                            return vals[index];
                        } else {
                            if (noRemoved()) {
                                int bIndex = index, fIndex = index, step = 1;
                                while (true) {
                                    if ((bIndex -= step) < 0) bIndex += capacity;
                                    if ((cur = keys[bIndex]) == key) {
                                        // key is present
                                        return vals[bIndex];
                                    } else if (cur == FREE) {
                                        index = bIndex;
                                        break keyAbsentFreeSlot;
                                    }
                                    else if (keyEquals(key, cur)) {
                                        // key is present
                                        return vals[bIndex];
                                    }
                                    int t;
                                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                                    if ((cur = keys[fIndex]) == key) {
                                        // key is present
                                        return vals[fIndex];
                                    } else if (cur == FREE) {
                                        index = fIndex;
                                        break keyAbsentFreeSlot;
                                    }
                                    else if (keyEquals(key, cur)) {
                                        // key is present
                                        return vals[fIndex];
                                    }
                                    step += 2;
                                }
                            } else {
                                firstRemoved = -1;
                            }
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
                            } else if (cur == FREE) {
                                if (firstRemoved < 0) {
                                    index = bIndex;
                                    break keyAbsentFreeSlot;
                                } else {
                                    break keyAbsentRemovedSlot;
                                }
                            } else if (cur != REMOVED) {
                                if (keyEquals(key, cur)) {
                                    // key is present
                                    return vals[bIndex];
                                }
                            } else if (firstRemoved < 0) {
                                firstRemoved = bIndex;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == key) {
                                // key is present
                                return vals[fIndex];
                            } else if (cur == FREE) {
                                if (firstRemoved < 0) {
                                    index = fIndex;
                                    break keyAbsentFreeSlot;
                                } else {
                                    break keyAbsentRemovedSlot;
                                }
                            } else if (cur != REMOVED) {
                                if (keyEquals(key, cur)) {
                                    // key is present
                                    return vals[fIndex];
                                }
                            } else if (firstRemoved < 0) {
                                firstRemoved = fIndex;
                            }
                            step += 2;
                        }
                    }
                    // key is absent, removed slot
                    char value = mappingFunction.applyAsChar(key);
                    incrementModCount();
                    keys[firstRemoved] = key;
                    vals[firstRemoved] = value;
                    postRemovedSlotInsertHook();
                    return value;
                }
                // key is absent, free slot
                char value = mappingFunction.applyAsChar(key);
                incrementModCount();
                keys[index] = key;
                vals[index] = value;
                postFreeSlotInsertHook();
                return value;
            }
        } else {
            return computeIfAbsentNullKey(mappingFunction);
        }
    }

    char computeIfAbsentNullKey(ToCharFunction<? super K> mappingFunction) {
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        // noinspection unchecked
        K[] keys = (K[]) set;
        char[] vals = values;
        int capacity = keys.length;
        int index;
        K cur;
        if ((cur = keys[index = 0]) == null) {
            // key is present
            return vals[index];
        } else {
            keyAbsentFreeSlot:
            if (cur != FREE) {
                int firstRemoved;
                if (cur != REMOVED) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == null) {
                                // key is present
                                return vals[bIndex];
                            } else if (cur == FREE) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == null) {
                                // key is present
                                return vals[fIndex];
                            } else if (cur == FREE) {
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
                        if ((cur = keys[bIndex]) == null) {
                            // key is present
                            return vals[bIndex];
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
                        if ((cur = keys[fIndex]) == null) {
                            // key is present
                            return vals[fIndex];
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
                        step += 2;
                    }
                }
                // key is absent, removed slot
                char value = mappingFunction.applyAsChar(null);
                incrementModCount();
                keys[firstRemoved] = null;
                vals[firstRemoved] = value;
                postRemovedSlotInsertHook();
                return value;
            }
            // key is absent, free slot
            char value = mappingFunction.applyAsChar(null);
            incrementModCount();
            keys[index] = null;
            vals[index] = value;
            postFreeSlotInsertHook();
            return value;
        }
    }

    @Override
    public Character computeIfPresent(K key,
            BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(key);
        if (index >= 0) {
            // key is present
            char[] vals = values;
            Character newValue = remappingFunction.apply(key, vals[index]);
            if (newValue != null) {
                vals[index] = newValue;
                return newValue;
            } else {
                incrementModCount();
                set[index] = REMOVED;
                postRemoveHook();
                return null;
            }
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public char computeIfPresent(K key, ObjCharToCharFunction<? super K> remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(key);
        if (index >= 0) {
            // key is present
            char[] vals = values;
            char newValue = remappingFunction.applyAsChar(key, vals[index]);
            vals[index] = newValue;
            return newValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Character merge(K key, Character value,
            BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        if (key != null) {
            if (value == null)
                throw new java.lang.NullPointerException();
            if (remappingFunction == null)
                throw new java.lang.NullPointerException();
            // noinspection unchecked
            K[] keys = (K[]) set;
            char[] vals = values;
            int capacity, index;
            K cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(key)) % (capacity = keys.length)]) != key) {
                keyAbsentFreeSlot:
                if (cur != FREE) {
                    int firstRemoved;
                    if (cur != REMOVED) {
                        if (keyEquals(key, cur)) {
                            break keyPresent;
                        } else {
                            if (noRemoved()) {
                                int bIndex = index, fIndex = index, step = 1;
                                while (true) {
                                    if ((bIndex -= step) < 0) bIndex += capacity;
                                    if ((cur = keys[bIndex]) == key) {
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
                                    if ((cur = keys[fIndex]) == key) {
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
                                    step += 2;
                                }
                            } else {
                                firstRemoved = -1;
                            }
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
                            if ((cur = keys[fIndex]) == key) {
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
            Character newValue = remappingFunction.apply(vals[index], value);
            if (newValue != null) {
                vals[index] = newValue;
                return newValue;
            } else {
                incrementModCount();
                ((Object[]) keys)[index] = REMOVED;
                postRemoveHook();
                return null;
            }
        } else {
            return mergeNullKey(value, remappingFunction);
        }
    }

    Character mergeNullKey(Character value,
            BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        if (value == null)
            throw new java.lang.NullPointerException();
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        // noinspection unchecked
        K[] keys = (K[]) set;
        char[] vals = values;
        int capacity = keys.length;
        int index;
        K cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            keyAbsentFreeSlot:
            if (cur != FREE) {
                int firstRemoved;
                if (cur != REMOVED) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == null) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == null) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
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
                        if ((cur = keys[bIndex]) == null) {
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
                        if ((cur = keys[fIndex]) == null) {
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
                        step += 2;
                    }
                }
                // key is absent, removed slot
                incrementModCount();
                keys[firstRemoved] = null;
                vals[firstRemoved] = value;
                postRemovedSlotInsertHook();
                return value;
            }
            // key is absent, free slot
            incrementModCount();
            keys[index] = null;
            vals[index] = value;
            postFreeSlotInsertHook();
            return value;
        }
        // key is present
        Character newValue = remappingFunction.apply(vals[index], value);
        if (newValue != null) {
            vals[index] = newValue;
            return newValue;
        } else {
            incrementModCount();
            ((Object[]) keys)[index] = REMOVED;
            postRemoveHook();
            return null;
        }
    }

    @Override
    public char merge(K key, char value, CharBinaryOperator remappingFunction) {
        if (key != null) {
            if (remappingFunction == null)
                throw new java.lang.NullPointerException();
            // noinspection unchecked
            K[] keys = (K[]) set;
            char[] vals = values;
            int capacity, index;
            K cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(key)) % (capacity = keys.length)]) != key) {
                keyAbsentFreeSlot:
                if (cur != FREE) {
                    int firstRemoved;
                    if (cur != REMOVED) {
                        if (keyEquals(key, cur)) {
                            break keyPresent;
                        } else {
                            if (noRemoved()) {
                                int bIndex = index, fIndex = index, step = 1;
                                while (true) {
                                    if ((bIndex -= step) < 0) bIndex += capacity;
                                    if ((cur = keys[bIndex]) == key) {
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
                                    if ((cur = keys[fIndex]) == key) {
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
                                    step += 2;
                                }
                            } else {
                                firstRemoved = -1;
                            }
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
                            if ((cur = keys[fIndex]) == key) {
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
            char newValue = remappingFunction.applyAsChar(vals[index], value);
            vals[index] = newValue;
            return newValue;
        } else {
            return mergeNullKey(value, remappingFunction);
        }
    }

    char mergeNullKey(char value, CharBinaryOperator remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        // noinspection unchecked
        K[] keys = (K[]) set;
        char[] vals = values;
        int capacity = keys.length;
        int index;
        K cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            keyAbsentFreeSlot:
            if (cur != FREE) {
                int firstRemoved;
                if (cur != REMOVED) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == null) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == null) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
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
                        if ((cur = keys[bIndex]) == null) {
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
                        if ((cur = keys[fIndex]) == null) {
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
                        step += 2;
                    }
                }
                // key is absent, removed slot
                incrementModCount();
                keys[firstRemoved] = null;
                vals[firstRemoved] = value;
                postRemovedSlotInsertHook();
                return value;
            }
            // key is absent, free slot
            incrementModCount();
            keys[index] = null;
            vals[index] = value;
            postFreeSlotInsertHook();
            return value;
        }
        // key is present
        char newValue = remappingFunction.applyAsChar(vals[index], value);
        vals[index] = newValue;
        return newValue;
    }

    @Override
    public char addValue(K key, char value) {
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return value;
        } else {
            // key is present
            char[] vals = values;
            char newValue = (char) (vals[index] + value);
            vals[index] = newValue;
            return newValue;
        }
    }

    @Override
    public char addValue(K key, char addition, char defaultValue) {
        char value = (char) (defaultValue + addition);
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return value;
        } else {
            // key is present
            char[] vals = values;
            char newValue = (char) (vals[index] + addition);
            vals[index] = newValue;
            return newValue;
        }
    }


    @Override
    public void putAll(@Nonnull Map<? extends K, ? extends Character> m) {
        CommonObjCharMapOps.putAll(this, m);
    }


    @Override
    public Character replace(K key, Character value) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            char[] vals = values;
            char oldValue = vals[index];
            vals[index] = value;
            return oldValue;
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public char replace(K key, char value) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            char[] vals = values;
            char oldValue = vals[index];
            vals[index] = value;
            return oldValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public boolean replace(K key, Character oldValue, Character newValue) {
        return replace(key,
                oldValue.charValue(),
                newValue.charValue());
    }

    @Override
    public boolean replace(K key, char oldValue, char newValue) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            char[] vals = values;
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
            BiFunction<? super K, ? super Character, ? extends Character> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        Object[] keys = set;
        char[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    vals[i] = function.apply(key, vals[i]);
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE && key != REMOVED) {
                    vals[i] = function.apply(key, vals[i]);
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void replaceAll(ObjCharToCharFunction<? super K> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        Object[] keys = set;
        char[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    vals[i] = function.applyAsChar(key, vals[i]);
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE && key != REMOVED) {
                    vals[i] = function.applyAsChar(key, vals[i]);
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
    public Character remove(Object key) {
        if (key != null) {
            // noinspection unchecked
            K k = (K) key;
            // noinspection unchecked
            K[] keys = (K[]) set;
            int capacity, index;
            K cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(k)) % (capacity = keys.length)]) != k) {
                if (cur == FREE) {
                    // key is absent, free slot
                    return null;
                } else {
                    if (cur != REMOVED) {
                        if (keyEquals(k, cur)) {
                            break keyPresent;
                        } else {
                            if (noRemoved()) {
                                int bIndex = index, fIndex = index, step = 1;
                                while (true) {
                                    if ((bIndex -= step) < 0) bIndex += capacity;
                                    if ((cur = keys[bIndex]) == k) {
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
                                    if ((cur = keys[fIndex]) == k) {
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
                                    step += 2;
                                }
                            }
                        }
                    }
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = keys[bIndex]) == k) {
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
                        if ((cur = keys[fIndex]) == k) {
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
                        step += 2;
                    }
                }
            }
            // key is present
            char val = values[index];
            incrementModCount();
            ((Object[]) keys)[index] = REMOVED;
            postRemoveHook();
            return val;
        } else {
            return removeNullKey();
        }
    }

    Character removeNullKey() {
        // noinspection unchecked
        K[] keys = (K[]) set;
        int capacity = keys.length;
        int index;
        K cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            if (cur == FREE) {
                // key is absent, free slot
                return null;
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == null) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        // key is absent, free slot
                        return null;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == null) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        // key is absent, free slot
                        return null;
                    }
                    step += 2;
                }
            }
        }
        // key is present
        char val = values[index];
        incrementModCount();
        ((Object[]) keys)[index] = REMOVED;
        postRemoveHook();
        return val;
    }

    @Override
    public boolean justRemove(Object key) {
        if (key != null) {
            // noinspection unchecked
            K k = (K) key;
            // noinspection unchecked
            K[] keys = (K[]) set;
            int capacity, index;
            K cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(k)) % (capacity = keys.length)]) != k) {
                if (cur == FREE) {
                    // key is absent, free slot
                    return false;
                } else {
                    if (cur != REMOVED) {
                        if (keyEquals(k, cur)) {
                            break keyPresent;
                        } else {
                            if (noRemoved()) {
                                int bIndex = index, fIndex = index, step = 1;
                                while (true) {
                                    if ((bIndex -= step) < 0) bIndex += capacity;
                                    if ((cur = keys[bIndex]) == k) {
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
                                    if ((cur = keys[fIndex]) == k) {
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
                                    step += 2;
                                }
                            }
                        }
                    }
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = keys[bIndex]) == k) {
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
                        if ((cur = keys[fIndex]) == k) {
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
                        step += 2;
                    }
                }
            }
            // key is present
            incrementModCount();
            ((Object[]) keys)[index] = REMOVED;
            postRemoveHook();
            return true;
        } else {
            return justRemoveNullKey();
        }
    }

    boolean justRemoveNullKey() {
        // noinspection unchecked
        K[] keys = (K[]) set;
        int capacity = keys.length;
        int index;
        K cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            if (cur == FREE) {
                // key is absent, free slot
                return false;
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == null) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        // key is absent, free slot
                        return false;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == null) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        // key is absent, free slot
                        return false;
                    }
                    step += 2;
                }
            }
        }
        // key is present
        incrementModCount();
        ((Object[]) keys)[index] = REMOVED;
        postRemoveHook();
        return true;
    }


    

    @Override
    public char removeAsChar(Object key) {
        if (key != null) {
            // noinspection unchecked
            K k = (K) key;
            // noinspection unchecked
            K[] keys = (K[]) set;
            int capacity, index;
            K cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(k)) % (capacity = keys.length)]) != k) {
                if (cur == FREE) {
                    // key is absent, free slot
                    return defaultValue();
                } else {
                    if (cur != REMOVED) {
                        if (keyEquals(k, cur)) {
                            break keyPresent;
                        } else {
                            if (noRemoved()) {
                                int bIndex = index, fIndex = index, step = 1;
                                while (true) {
                                    if ((bIndex -= step) < 0) bIndex += capacity;
                                    if ((cur = keys[bIndex]) == k) {
                                        index = bIndex;
                                        break keyPresent;
                                    } else if (cur == FREE) {
                                        // key is absent, free slot
                                        return defaultValue();
                                    }
                                    else if (keyEquals(k, cur)) {
                                        index = bIndex;
                                        break keyPresent;
                                    }
                                    int t;
                                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                                    if ((cur = keys[fIndex]) == k) {
                                        index = fIndex;
                                        break keyPresent;
                                    } else if (cur == FREE) {
                                        // key is absent, free slot
                                        return defaultValue();
                                    }
                                    else if (keyEquals(k, cur)) {
                                        index = fIndex;
                                        break keyPresent;
                                    }
                                    step += 2;
                                }
                            }
                        }
                    }
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = keys[bIndex]) == k) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == FREE) {
                            // key is absent, free slot
                            return defaultValue();
                        }
                        else if (cur != REMOVED && keyEquals(k, cur)) {
                            index = bIndex;
                            break keyPresent;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = keys[fIndex]) == k) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == FREE) {
                            // key is absent, free slot
                            return defaultValue();
                        }
                        else if (cur != REMOVED && keyEquals(k, cur)) {
                            index = fIndex;
                            break keyPresent;
                        }
                        step += 2;
                    }
                }
            }
            // key is present
            char val = values[index];
            incrementModCount();
            ((Object[]) keys)[index] = REMOVED;
            postRemoveHook();
            return val;
        } else {
            return removeAsCharNullKey();
        }
    }

    char removeAsCharNullKey() {
        // noinspection unchecked
        K[] keys = (K[]) set;
        int capacity = keys.length;
        int index;
        K cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            if (cur == FREE) {
                // key is absent, free slot
                return defaultValue();
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == null) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        // key is absent, free slot
                        return defaultValue();
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == null) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        // key is absent, free slot
                        return defaultValue();
                    }
                    step += 2;
                }
            }
        }
        // key is present
        char val = values[index];
        incrementModCount();
        ((Object[]) keys)[index] = REMOVED;
        postRemoveHook();
        return val;
    }


    @Override
    public boolean remove(Object key, Object value) {
        return remove(key,
                ((Character) value).charValue()
                );
    }

    @Override
    public boolean remove(Object key, char value) {
        if (key != null) {
            // noinspection unchecked
            K k = (K) key;
            // noinspection unchecked
            K[] keys = (K[]) set;
            int capacity, index;
            K cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(k)) % (capacity = keys.length)]) != k) {
                if (cur == FREE) {
                    // key is absent, free slot
                    return false;
                } else {
                    if (cur != REMOVED) {
                        if (keyEquals(k, cur)) {
                            break keyPresent;
                        } else {
                            if (noRemoved()) {
                                int bIndex = index, fIndex = index, step = 1;
                                while (true) {
                                    if ((bIndex -= step) < 0) bIndex += capacity;
                                    if ((cur = keys[bIndex]) == k) {
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
                                    if ((cur = keys[fIndex]) == k) {
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
                                    step += 2;
                                }
                            }
                        }
                    }
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = keys[bIndex]) == k) {
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
                        if ((cur = keys[fIndex]) == k) {
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
                        step += 2;
                    }
                }
            }
            // key is present
            if (values[index] == value) {
                incrementModCount();
                ((Object[]) keys)[index] = REMOVED;
                postRemoveHook();
                return true;
            } else {
                return false;
            }
        } else {
            return removeEntryNullKey(value);
        }
    }

    boolean removeEntryNullKey(char value) {
        // noinspection unchecked
        K[] keys = (K[]) set;
        int capacity = keys.length;
        int index;
        K cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            if (cur == FREE) {
                // key is absent, free slot
                return false;
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == null) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        // key is absent, free slot
                        return false;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == null) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
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
            ((Object[]) keys)[index] = REMOVED;
            postRemoveHook();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeIf(ObjCharPredicate<? super K> filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        Object[] keys = set;
        char[] vals = values;
        if (noRemoved()) {
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    if (filter.test(key, vals[i])) {
                        incrementModCount();
                        mc++;
                        keys[i] = REMOVED;
                        postRemoveHook();
                        changed = true;
                    }
                }
            }
        } else {
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE && key != REMOVED) {
                    if (filter.test(key, vals[i])) {
                        incrementModCount();
                        mc++;
                        keys[i] = REMOVED;
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






    class EntryView extends AbstractSetView<Map.Entry<K, Character>>
            implements HashObjSet<Map.Entry<K, Character>>,
            InternalObjCollectionOps<Map.Entry<K, Character>> {

        @Nonnull
        @Override
        public Equivalence<Entry<K, Character>> equivalence() {
            return Equivalence.entryEquivalence(
                    keyEquivalence(),
                    Equivalence.<Character>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return MutableQHashSeparateKVObjCharMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return MutableQHashSeparateKVObjCharMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return MutableQHashSeparateKVObjCharMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<K, Character> e = (Map.Entry<K, Character>) o;
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
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE) {
                        result[resultIndex++] = new MutableEntry(mc, i, key, vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE && key != REMOVED) {
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
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE) {
                        a[resultIndex++] = (T) new MutableEntry(mc, i, key, vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE && key != REMOVED) {
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
        public final void forEach(@Nonnull Consumer<? super Map.Entry<K, Character>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE) {
                        action.accept(new MutableEntry(mc, i, key, vals[i]));
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE && key != REMOVED) {
                        action.accept(new MutableEntry(mc, i, key, vals[i]));
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<K, Character>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE) {
                        if (!predicate.test(new MutableEntry(mc, i, key, vals[i]))) {
                            terminated = true;
                            break;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE && key != REMOVED) {
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
        public ObjIterator<Map.Entry<K, Character>> iterator() {
            int mc = modCount();
            if (!noRemoved())
                return new SomeRemovedEntryIterator(mc);
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<K, Character>> cursor() {
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
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE) {
                        if (!c.contains(e.with(key, vals[i]))) {
                            containsAll = false;
                            break;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE && key != REMOVED) {
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
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE) {
                        changed |= s.remove(e.with(key, vals[i]));
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE && key != REMOVED) {
                        changed |= s.remove(e.with(key, vals[i]));
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<K, Character>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE) {
                        changed |= c.add(new MutableEntry(mc, i, key, vals[i]));
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE && key != REMOVED) {
                        changed |= c.add(new MutableEntry(mc, i, key, vals[i]));
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        public int hashCode() {
            return MutableQHashSeparateKVObjCharMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE) {
                        sb.append(' ');
                        sb.append(key != this ? key : "(this Collection)");
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
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE && key != REMOVED) {
                        sb.append(' ');
                        sb.append(key != this ? key : "(this Collection)");
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
            return MutableQHashSeparateKVObjCharMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<K, Character> e = (Map.Entry<K, Character>) o;
                K key = e.getKey();
                char value = e.getValue();
                return MutableQHashSeparateKVObjCharMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<K, Character>> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE) {
                        if (filter.test(new MutableEntry(mc, i, key, vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = REMOVED;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE && key != REMOVED) {
                        if (filter.test(new MutableEntry(mc, i, key, vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = REMOVED;
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
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE) {
                        if (c.contains(e.with(key, vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = REMOVED;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE && key != REMOVED) {
                        if (c.contains(e.with(key, vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = REMOVED;
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
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE) {
                        if (!c.contains(e.with(key, vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = REMOVED;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    K key;
                    // noinspection unchecked
                    if ((key = (K) keys[i]) != FREE && key != REMOVED) {
                        if (!c.contains(e.with(key, vals[i]))) {
                            incrementModCount();
                            mc++;
                            keys[i] = REMOVED;
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
            MutableQHashSeparateKVObjCharMapGO.this.clear();
        }
    }


    abstract class ObjCharEntry extends AbstractEntry<K, Character> {

        abstract K key();

        @Override
        public final K getKey() {
            return key();
        }

        abstract char value();

        @Override
        public final Character getValue() {
            return value();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object o) {
            Map.Entry e2;
            K k2;
            char v2;
            try {
                e2 = (Map.Entry) o;
                k2 = (K) e2.getKey();
                v2 = (Character) e2.getValue();
                return nullableKeyEquals(key(), k2)
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
            return nullableKeyHashCode(key())
                    ^
                    Primitives.hashCode(value())
                    ;
        }
    }


    class MutableEntry extends ObjCharEntry {
        final int modCount;
        private final int index;
        final K key;
        private char value;

        MutableEntry(int modCount, int index, K key, char value) {
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
        public char value() {
            return value;
        }

        @Override
        public Character setValue(Character newValue) {
            if (modCount != modCount())
                throw new IllegalStateException();
            char oldValue = value;
            char unwrappedNewValue = newValue;
            value = unwrappedNewValue;
            updateValueInTable(unwrappedNewValue);
            return oldValue;
        }

        void updateValueInTable(char newValue) {
            values[index] = newValue;
        }
    }



    class ReusableEntry extends ObjCharEntry {
        private K key;
        private char value;

        ReusableEntry with(K key, char value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public K key() {
            return key;
        }

        @Override
        public char value() {
            return value;
        }
    }


    class ValueView extends AbstractCharValueView {


        @Override
        public int size() {
            return MutableQHashSeparateKVObjCharMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return MutableQHashSeparateKVObjCharMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return MutableQHashSeparateKVObjCharMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(char v) {
            return MutableQHashSeparateKVObjCharMapGO.this.containsValue(v);
        }



        @Override
        public void forEach(Consumer<? super Character> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != FREE) {
                        action.accept(vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE && key != REMOVED) {
                        action.accept(vals[i]);
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public void forEach(CharConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != FREE) {
                        action.accept(vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE && key != REMOVED) {
                        action.accept(vals[i]);
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(CharPredicate predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != FREE) {
                        if (!predicate.test(vals[i])) {
                            terminated = true;
                            break;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE && key != REMOVED) {
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
        public boolean allContainingIn(CharCollection c) {
            if (isEmpty())
                return true;
            boolean containsAll = true;
            int mc = modCount();
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != FREE) {
                        if (!c.contains(vals[i])) {
                            containsAll = false;
                            break;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE && key != REMOVED) {
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
        public boolean reverseAddAllTo(CharCollection c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != FREE) {
                        changed |= c.add(vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE && key != REMOVED) {
                        changed |= c.add(vals[i]);
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        @Override
        public boolean reverseRemoveAllFrom(CharSet s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != FREE) {
                        changed |= s.removeChar(vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE && key != REMOVED) {
                        changed |= s.removeChar(vals[i]);
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }



        @Override
        @Nonnull
        public CharIterator iterator() {
            int mc = modCount();
            if (!noRemoved())
                return new SomeRemovedValueIterator(mc);
            return new NoRemovedValueIterator(mc);
        }

        @Nonnull
        @Override
        public CharCursor cursor() {
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
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != FREE) {
                        result[resultIndex++] = vals[i];
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE && key != REMOVED) {
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
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != FREE) {
                        a[resultIndex++] = (T) Character.valueOf(vals[i]);
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE && key != REMOVED) {
                        a[resultIndex++] = (T) Character.valueOf(vals[i]);
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
        public char[] toCharArray() {
            int size = size();
            char[] result = new char[size];
            if (size == 0)
                return result;
            int resultIndex = 0;
            int mc = modCount();
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != FREE) {
                        result[resultIndex++] = vals[i];
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE && key != REMOVED) {
                        result[resultIndex++] = vals[i];
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return result;
        }

        @Override
        public char[] toArray(char[] a) {
            int size = size();
            if (a.length < size)
                a = new char[size];
            if (size == 0) {
                if (a.length > 0)
                    a[0] = (char) 0;
                return a;
            }
            int resultIndex = 0;
            int mc = modCount();
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != FREE) {
                        a[resultIndex++] = vals[i];
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE && key != REMOVED) {
                        a[resultIndex++] = vals[i];
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            if (a.length > resultIndex)
                a[resultIndex] = (char) 0;
            return a;
        }


        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != FREE) {
                        sb.append(' ').append(vals[i]).append(',');
                        if (++elementCount == 8) {
                            int expectedLength = sb.length() * (size() / 8);
                            sb.ensureCapacity(expectedLength + (expectedLength / 2));
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE && key != REMOVED) {
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
            return removeChar(( Character ) o);
        }

        @Override
        public boolean removeChar(char v) {
            return removeValue(v);
        }



        @Override
        public void clear() {
            MutableQHashSeparateKVObjCharMapGO.this.clear();
        }

        
        public boolean removeIf(Predicate<? super Character> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != FREE) {
                        if (filter.test(vals[i])) {
                            incrementModCount();
                            mc++;
                            keys[i] = REMOVED;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE && key != REMOVED) {
                        if (filter.test(vals[i])) {
                            incrementModCount();
                            mc++;
                            keys[i] = REMOVED;
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
        public boolean removeIf(CharPredicate filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != FREE) {
                        if (filter.test(vals[i])) {
                            incrementModCount();
                            mc++;
                            keys[i] = REMOVED;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE && key != REMOVED) {
                        if (filter.test(vals[i])) {
                            incrementModCount();
                            mc++;
                            keys[i] = REMOVED;
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
            if (c instanceof CharCollection)
                return removeAll((CharCollection) c);
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != FREE) {
                        if (c.contains(vals[i])) {
                            incrementModCount();
                            mc++;
                            keys[i] = REMOVED;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE && key != REMOVED) {
                        if (c.contains(vals[i])) {
                            incrementModCount();
                            mc++;
                            keys[i] = REMOVED;
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

        private boolean removeAll(CharCollection c) {
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != FREE) {
                        if (c.contains(vals[i])) {
                            incrementModCount();
                            mc++;
                            keys[i] = REMOVED;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE && key != REMOVED) {
                        if (c.contains(vals[i])) {
                            incrementModCount();
                            mc++;
                            keys[i] = REMOVED;
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
            if (c instanceof CharCollection)
                return retainAll((CharCollection) c);
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
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != FREE) {
                        if (!c.contains(vals[i])) {
                            incrementModCount();
                            mc++;
                            keys[i] = REMOVED;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE && key != REMOVED) {
                        if (!c.contains(vals[i])) {
                            incrementModCount();
                            mc++;
                            keys[i] = REMOVED;
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

        private boolean retainAll(CharCollection c) {
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
            Object[] keys = set;
            char[] vals = values;
            if (noRemoved()) {
                for (int i = keys.length - 1; i >= 0; i--) {
                    if (keys[i] != FREE) {
                        if (!c.contains(vals[i])) {
                            incrementModCount();
                            mc++;
                            keys[i] = REMOVED;
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = keys.length - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE && key != REMOVED) {
                        if (!c.contains(vals[i])) {
                            incrementModCount();
                            mc++;
                            keys[i] = REMOVED;
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<K, Character>> {
        final K[] keys;
        final char[] vals;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        MutableEntry next;

        NoRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            // noinspection unchecked
            K[] keys = this.keys = (K[]) set;
            char[] vals = this.vals = values;
            int nextI = keys.length;
            while (--nextI >= 0) {
                Object key;
                if ((key = keys[nextI]) != FREE) {
                    // noinspection unchecked
                    next = new MutableEntry(mc, nextI, (K) key, vals[nextI]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<K, Character>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            char[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE) {
                    // noinspection unchecked
                    action.accept(new MutableEntry(mc, i, (K) key, vals[i]));
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
        public Map.Entry<K, Character> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
                    index = nextI;
                    K[] keys = this.keys;
                    MutableEntry prev = next;
                    while (--nextI >= 0) {
                        Object key;
                        if ((key = keys[nextI]) != FREE) {
                            // noinspection unchecked
                            next = new MutableEntry(mc, nextI, (K) key, vals[nextI]);
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
                    ((Object[]) keys)[index] = REMOVED;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<K, Character>> {
        final K[] keys;
        final char[] vals;
        int expectedModCount;
        int index;
        Object curKey;
        char curValue;

        NoRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            // noinspection unchecked
            this.keys = (K[]) set;
            index = keys.length;
            vals = values;
            curKey = FREE;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<K, Character>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            char[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE) {
                    // noinspection unchecked
                    action.accept(new MutableEntry(mc, i, (K) key, vals[i]));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE;
        }

        @Override
        public Map.Entry<K, Character> elem() {
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
                K[] keys = this.keys;
                for (int i = index - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE) {
                        index = i;
                        curKey = key;
                        curValue = vals[i];
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
                    ((Object[]) keys)[index] = REMOVED;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedEntryIterator implements ObjIterator<Map.Entry<K, Character>> {
        final K[] keys;
        final char[] vals;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        MutableEntry next;

        SomeRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            // noinspection unchecked
            K[] keys = this.keys = (K[]) set;
            char[] vals = this.vals = values;
            int nextI = keys.length;
            while (--nextI >= 0) {
                Object key;
                if ((key = keys[nextI]) != FREE && key != REMOVED) {
                    // noinspection unchecked
                    next = new MutableEntry(mc, nextI, (K) key, vals[nextI]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<K, Character>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            char[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE && key != REMOVED) {
                    // noinspection unchecked
                    action.accept(new MutableEntry(mc, i, (K) key, vals[i]));
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
        public Map.Entry<K, Character> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
                    index = nextI;
                    K[] keys = this.keys;
                    MutableEntry prev = next;
                    while (--nextI >= 0) {
                        Object key;
                        if ((key = keys[nextI]) != FREE && key != REMOVED) {
                            // noinspection unchecked
                            next = new MutableEntry(mc, nextI, (K) key, vals[nextI]);
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
                    ((Object[]) keys)[index] = REMOVED;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedEntryCursor implements ObjCursor<Map.Entry<K, Character>> {
        final K[] keys;
        final char[] vals;
        int expectedModCount;
        int index;
        Object curKey;
        char curValue;

        SomeRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            // noinspection unchecked
            this.keys = (K[]) set;
            index = keys.length;
            vals = values;
            curKey = FREE;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<K, Character>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            char[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE && key != REMOVED) {
                    // noinspection unchecked
                    action.accept(new MutableEntry(mc, i, (K) key, vals[i]));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE;
        }

        @Override
        public Map.Entry<K, Character> elem() {
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
                K[] keys = this.keys;
                for (int i = index - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE && key != REMOVED) {
                        index = i;
                        curKey = key;
                        curValue = vals[i];
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
                    ((Object[]) keys)[index] = REMOVED;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }




    class NoRemovedValueIterator implements CharIterator {
        final K[] keys;
        final char[] vals;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        char next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            // noinspection unchecked
            K[] keys = this.keys = (K[]) set;
            char[] vals = this.vals = values;
            int nextI = keys.length;
            while (--nextI >= 0) {
                if (keys[nextI] != FREE) {
                    // noinspection unchecked
                    next = vals[nextI];
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public char nextChar() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                if (expectedModCount == modCount()) {
                    index = nextI;
                    K[] keys = this.keys;
                    char prev = next;
                    while (--nextI >= 0) {
                        if (keys[nextI] != FREE) {
                            // noinspection unchecked
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
        public void forEachRemaining(Consumer<? super Character> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            char[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if (keys[i] != FREE) {
                    // noinspection unchecked
                    action.accept(vals[i]);
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            index = nextIndex = -1;
        }

        @Override
        public void forEachRemaining(CharConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            char[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if (keys[i] != FREE) {
                    // noinspection unchecked
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
        public Character next() {
            return nextChar();
        }

        @Override
        public void remove() {
            int index;
            if ((index = this.index) >= 0) {
                if (expectedModCount++ == modCount()) {
                    this.index = -1;
                    incrementModCount();
                    ((Object[]) keys)[index] = REMOVED;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedValueCursor implements CharCursor {
        final K[] keys;
        final char[] vals;
        int expectedModCount;
        int index;
        Object curKey;
        char curValue;

        NoRemovedValueCursor(int mc) {
            expectedModCount = mc;
            // noinspection unchecked
            this.keys = (K[]) set;
            index = keys.length;
            vals = values;
            curKey = FREE;
        }

        @Override
        public void forEachForward(CharConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            char[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    // noinspection unchecked
                    action.accept(vals[i]);
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE;
        }

        @Override
        public char elem() {
            if (curKey != FREE) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                K[] keys = this.keys;
                for (int i = index - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE) {
                        index = i;
                        curKey = key;
                        curValue = vals[i];
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
                    ((Object[]) keys)[index] = REMOVED;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedValueIterator implements CharIterator {
        final K[] keys;
        final char[] vals;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        char next;

        SomeRemovedValueIterator(int mc) {
            expectedModCount = mc;
            // noinspection unchecked
            K[] keys = this.keys = (K[]) set;
            char[] vals = this.vals = values;
            int nextI = keys.length;
            while (--nextI >= 0) {
                Object key;
                if ((key = keys[nextI]) != FREE && key != REMOVED) {
                    // noinspection unchecked
                    next = vals[nextI];
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public char nextChar() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                if (expectedModCount == modCount()) {
                    index = nextI;
                    K[] keys = this.keys;
                    char prev = next;
                    while (--nextI >= 0) {
                        Object key;
                        if ((key = keys[nextI]) != FREE && key != REMOVED) {
                            // noinspection unchecked
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
        public void forEachRemaining(Consumer<? super Character> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            char[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE && key != REMOVED) {
                    // noinspection unchecked
                    action.accept(vals[i]);
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            index = nextIndex = -1;
        }

        @Override
        public void forEachRemaining(CharConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            char[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE && key != REMOVED) {
                    // noinspection unchecked
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
        public Character next() {
            return nextChar();
        }

        @Override
        public void remove() {
            int index;
            if ((index = this.index) >= 0) {
                if (expectedModCount++ == modCount()) {
                    this.index = -1;
                    incrementModCount();
                    ((Object[]) keys)[index] = REMOVED;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedValueCursor implements CharCursor {
        final K[] keys;
        final char[] vals;
        int expectedModCount;
        int index;
        Object curKey;
        char curValue;

        SomeRemovedValueCursor(int mc) {
            expectedModCount = mc;
            // noinspection unchecked
            this.keys = (K[]) set;
            index = keys.length;
            vals = values;
            curKey = FREE;
        }

        @Override
        public void forEachForward(CharConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            char[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE && key != REMOVED) {
                    // noinspection unchecked
                    action.accept(vals[i]);
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE;
        }

        @Override
        public char elem() {
            if (curKey != FREE) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                K[] keys = this.keys;
                for (int i = index - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE && key != REMOVED) {
                        index = i;
                        curKey = key;
                        curValue = vals[i];
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
                    ((Object[]) keys)[index] = REMOVED;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }



    class NoRemovedMapCursor implements ObjCharCursor<K> {
        final K[] keys;
        final char[] vals;
        int expectedModCount;
        int index;
        Object curKey;
        char curValue;

        NoRemovedMapCursor(int mc) {
            expectedModCount = mc;
            // noinspection unchecked
            this.keys = (K[]) set;
            index = keys.length;
            vals = values;
            curKey = FREE;
        }

        @Override
        public void forEachForward(ObjCharConsumer<? super K> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            char[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE) {
                    // noinspection unchecked
                    action.accept((K) key, vals[i]);
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
        public char value() {
            if (curKey != FREE) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(char value) {
            if (curKey != FREE) {
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
                K[] keys = this.keys;
                for (int i = index - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE) {
                        index = i;
                        curKey = key;
                        curValue = vals[i];
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
                    ((Object[]) keys)[index] = REMOVED;
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }

    class SomeRemovedMapCursor implements ObjCharCursor<K> {
        final K[] keys;
        final char[] vals;
        int expectedModCount;
        int index;
        Object curKey;
        char curValue;

        SomeRemovedMapCursor(int mc) {
            expectedModCount = mc;
            // noinspection unchecked
            this.keys = (K[]) set;
            index = keys.length;
            vals = values;
            curKey = FREE;
        }

        @Override
        public void forEachForward(ObjCharConsumer<? super K> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            char[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE && key != REMOVED) {
                    // noinspection unchecked
                    action.accept((K) key, vals[i]);
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
        public char value() {
            if (curKey != FREE) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(char value) {
            if (curKey != FREE) {
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
                K[] keys = this.keys;
                for (int i = index - 1; i >= 0; i--) {
                    Object key;
                    if ((key = keys[i]) != FREE && key != REMOVED) {
                        index = i;
                        curKey = key;
                        curValue = vals[i];
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
                    ((Object[]) keys)[index] = REMOVED;
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

