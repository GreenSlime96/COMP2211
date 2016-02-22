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
import net.openhft.koloboke.function.ObjShortConsumer;
import net.openhft.koloboke.function.ObjShortPredicate;
import net.openhft.koloboke.function.ObjShortToShortFunction;
import net.openhft.koloboke.function.ToShortFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.openhft.koloboke.function.ShortBinaryOperator;
import net.openhft.koloboke.function.ShortConsumer;
import net.openhft.koloboke.function.ShortPredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class MutableLHashSeparateKVObjShortMapGO<K>
        extends MutableLHashSeparateKVObjShortMapSO<K> {

    @Override
    final void copy(SeparateKVObjShortLHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVObjShortLHash hash) {
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
    public boolean containsEntry(Object key, short value) {
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
    public short getShort(Object key) {
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
    public short getOrDefault(Object key, short defaultValue) {
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
    public void forEach(BiConsumer<? super K, ? super Short> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        Object[] keys = set;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                action.accept(key, vals[i]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void forEach(ObjShortConsumer<? super K> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        Object[] keys = set;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                action.accept(key, vals[i]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public boolean forEachWhile(ObjShortPredicate<? super K> predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        Object[] keys = set;
        short[] vals = values;
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
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return !terminated;
    }

    @Nonnull
    @Override
    public ObjShortCursor<K> cursor() {
        int mc = modCount();
        return new NoRemovedMapCursor(mc);
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonObjShortMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalObjShortMapOps<?> m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        Object[] keys = set;
        short[] vals = values;
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
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return containsAll;
    }

    @Override
    public void reversePutAllTo(InternalObjShortMapOps<? super K> m) {
        if (isEmpty())
            return;
        int mc = modCount();
        Object[] keys = set;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                m.justPut(key, vals[i]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<K, Short>> entrySet() {
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
        Object[] keys = set;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                hashCode += nullableKeyHashCode(key) ^ vals[i];
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
        short[] vals = values;
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
        short[] vals = values;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        Object[] newKeys = set;
        int capacityMask = newKeys.length - 1;
        short[] newVals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                int index;
                if (newKeys[index = SeparateKVObjKeyMixing.mix(nullableKeyHashCode(key)) & capacityMask] != FREE) {
                    while (true) {
                        if (newKeys[(index = (index - 1) & capacityMask)] == FREE) {
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
    public Short put(K key, Short value) {
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
    public short put(K key, short value) {
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
    public Short putIfAbsent(K key, Short value) {
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
    public short putIfAbsent(K key, short value) {
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
    public void justPut(K key, short value) {
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
    public Short compute(K key,
            BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
        if (key != null) {
            if (remappingFunction == null)
                throw new java.lang.NullPointerException();
            // noinspection unchecked
            K[] keys = (K[]) set;
            short[] vals = values;
            int capacityMask, index;
            K cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(key)) & (capacityMask = keys.length - 1)]) != key) {
                keyAbsent:
                if (cur != FREE) {
                    if (keyEquals(key, cur)) {
                        break keyPresent;
                    } else {
                        while (true) {
                            if ((cur = keys[(index = (index - 1) & capacityMask)]) == key) {
                                break keyPresent;
                            } else if (cur == FREE) {
                                break keyAbsent;
                            }
                            else if (keyEquals(key, cur)) {
                                break keyPresent;
                            }
                        }
                    }
                }
                // key is absent
                Short newValue = remappingFunction.apply(key, null);
                if (newValue != null) {
                    incrementModCount();
                    keys[index] = key;
                    vals[index] = newValue;
                    postInsertHook();
                    return newValue;
                } else {
                    return null;
                }
            }
            // key is present
            Short newValue = remappingFunction.apply(key, vals[index]);
            if (newValue != null) {
                vals[index] = newValue;
                return newValue;
            } else {
                removeAt(index);
                return null;
            }
        } else {
            return computeNullKey(remappingFunction);
        }
    }

    Short computeNullKey(
            BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        // noinspection unchecked
        K[] keys = (K[]) set;
        short[] vals = values;
        int capacityMask, index;
        K cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            keyAbsent:
            if (cur != FREE) {
                capacityMask = keys.length - 1;
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == null) {
                        break keyPresent;
                    } else if (cur == FREE) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            Short newValue = remappingFunction.apply(null, null);
            if (newValue != null) {
                incrementModCount();
                keys[index] = null;
                vals[index] = newValue;
                postInsertHook();
                return newValue;
            } else {
                return null;
            }
        }
        // key is present
        Short newValue = remappingFunction.apply(null, vals[index]);
        if (newValue != null) {
            vals[index] = newValue;
            return newValue;
        } else {
            removeAt(index);
            return null;
        }
    }

    @Override
    public short compute(K key, ObjShortToShortFunction<? super K> remappingFunction) {
        if (key != null) {
            if (remappingFunction == null)
                throw new java.lang.NullPointerException();
            // noinspection unchecked
            K[] keys = (K[]) set;
            short[] vals = values;
            int capacityMask, index;
            K cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(key)) & (capacityMask = keys.length - 1)]) != key) {
                keyAbsent:
                if (cur != FREE) {
                    if (keyEquals(key, cur)) {
                        break keyPresent;
                    } else {
                        while (true) {
                            if ((cur = keys[(index = (index - 1) & capacityMask)]) == key) {
                                break keyPresent;
                            } else if (cur == FREE) {
                                break keyAbsent;
                            }
                            else if (keyEquals(key, cur)) {
                                break keyPresent;
                            }
                        }
                    }
                }
                // key is absent
                short newValue = remappingFunction.applyAsShort(key, defaultValue());
                incrementModCount();
                keys[index] = key;
                vals[index] = newValue;
                postInsertHook();
                return newValue;
            }
            // key is present
            short newValue = remappingFunction.applyAsShort(key, vals[index]);
            vals[index] = newValue;
            return newValue;
        } else {
            return computeNullKey(remappingFunction);
        }
    }

    short computeNullKey(ObjShortToShortFunction<? super K> remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        // noinspection unchecked
        K[] keys = (K[]) set;
        short[] vals = values;
        int capacityMask, index;
        K cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            keyAbsent:
            if (cur != FREE) {
                capacityMask = keys.length - 1;
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == null) {
                        break keyPresent;
                    } else if (cur == FREE) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            short newValue = remappingFunction.applyAsShort(null, defaultValue());
            incrementModCount();
            keys[index] = null;
            vals[index] = newValue;
            postInsertHook();
            return newValue;
        }
        // key is present
        short newValue = remappingFunction.applyAsShort(null, vals[index]);
        vals[index] = newValue;
        return newValue;
    }

    @Override
    public Short computeIfAbsent(K key,
            Function<? super K, ? extends Short> mappingFunction) {
        if (key != null) {
            if (mappingFunction == null)
                throw new java.lang.NullPointerException();
            // noinspection unchecked
            K[] keys = (K[]) set;
            short[] vals = values;
            int capacityMask, index;
            K cur;
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(key)) & (capacityMask = keys.length - 1)]) == key) {
                // key is present
                return vals[index];
            } else {
                keyAbsent:
                if (cur != FREE) {
                    if (keyEquals(key, cur)) {
                        // key is present
                        return vals[index];
                    } else {
                        while (true) {
                            if ((cur = keys[(index = (index - 1) & capacityMask)]) == key) {
                                // key is present
                                return vals[index];
                            } else if (cur == FREE) {
                                break keyAbsent;
                            }
                            else if (keyEquals(key, cur)) {
                                // key is present
                                return vals[index];
                            }
                        }
                    }
                }
                // key is absent
                Short value = mappingFunction.apply(key);
                if (value != null) {
                    incrementModCount();
                    keys[index] = key;
                    vals[index] = value;
                    postInsertHook();
                    return value;
                } else {
                    return null;
                }
            }
        } else {
            return computeIfAbsentNullKey(mappingFunction);
        }
    }

    Short computeIfAbsentNullKey(
            Function<? super K, ? extends Short> mappingFunction) {
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        // noinspection unchecked
        K[] keys = (K[]) set;
        short[] vals = values;
        int capacityMask, index;
        K cur;
        if ((cur = keys[index = 0]) == null) {
            // key is present
            return vals[index];
        } else {
            keyAbsent:
            if (cur != FREE) {
                capacityMask = keys.length - 1;
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == null) {
                        // key is present
                        return vals[index];
                    } else if (cur == FREE) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            Short value = mappingFunction.apply(null);
            if (value != null) {
                incrementModCount();
                keys[index] = null;
                vals[index] = value;
                postInsertHook();
                return value;
            } else {
                return null;
            }
        }
    }

    @Override
    public short computeIfAbsent(K key, ToShortFunction<? super K> mappingFunction) {
        if (key != null) {
            if (mappingFunction == null)
                throw new java.lang.NullPointerException();
            // noinspection unchecked
            K[] keys = (K[]) set;
            short[] vals = values;
            int capacityMask, index;
            K cur;
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(key)) & (capacityMask = keys.length - 1)]) == key) {
                // key is present
                return vals[index];
            } else {
                keyAbsent:
                if (cur != FREE) {
                    if (keyEquals(key, cur)) {
                        // key is present
                        return vals[index];
                    } else {
                        while (true) {
                            if ((cur = keys[(index = (index - 1) & capacityMask)]) == key) {
                                // key is present
                                return vals[index];
                            } else if (cur == FREE) {
                                break keyAbsent;
                            }
                            else if (keyEquals(key, cur)) {
                                // key is present
                                return vals[index];
                            }
                        }
                    }
                }
                // key is absent
                short value = mappingFunction.applyAsShort(key);
                incrementModCount();
                keys[index] = key;
                vals[index] = value;
                postInsertHook();
                return value;
            }
        } else {
            return computeIfAbsentNullKey(mappingFunction);
        }
    }

    short computeIfAbsentNullKey(ToShortFunction<? super K> mappingFunction) {
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        // noinspection unchecked
        K[] keys = (K[]) set;
        short[] vals = values;
        int capacityMask, index;
        K cur;
        if ((cur = keys[index = 0]) == null) {
            // key is present
            return vals[index];
        } else {
            keyAbsent:
            if (cur != FREE) {
                capacityMask = keys.length - 1;
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == null) {
                        // key is present
                        return vals[index];
                    } else if (cur == FREE) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            short value = mappingFunction.applyAsShort(null);
            incrementModCount();
            keys[index] = null;
            vals[index] = value;
            postInsertHook();
            return value;
        }
    }

    @Override
    public Short computeIfPresent(K key,
            BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(key);
        if (index >= 0) {
            // key is present
            short[] vals = values;
            Short newValue = remappingFunction.apply(key, vals[index]);
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
    public short computeIfPresent(K key, ObjShortToShortFunction<? super K> remappingFunction) {
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
    public Short merge(K key, Short value,
            BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        if (key != null) {
            if (value == null)
                throw new java.lang.NullPointerException();
            if (remappingFunction == null)
                throw new java.lang.NullPointerException();
            // noinspection unchecked
            K[] keys = (K[]) set;
            short[] vals = values;
            int capacityMask, index;
            K cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(key)) & (capacityMask = keys.length - 1)]) != key) {
                keyAbsent:
                if (cur != FREE) {
                    if (keyEquals(key, cur)) {
                        break keyPresent;
                    } else {
                        while (true) {
                            if ((cur = keys[(index = (index - 1) & capacityMask)]) == key) {
                                break keyPresent;
                            } else if (cur == FREE) {
                                break keyAbsent;
                            }
                            else if (keyEquals(key, cur)) {
                                break keyPresent;
                            }
                        }
                    }
                }
                // key is absent
                incrementModCount();
                keys[index] = key;
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
        } else {
            return mergeNullKey(value, remappingFunction);
        }
    }

    Short mergeNullKey(Short value,
            BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        if (value == null)
            throw new java.lang.NullPointerException();
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        // noinspection unchecked
        K[] keys = (K[]) set;
        short[] vals = values;
        int capacityMask, index;
        K cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            keyAbsent:
            if (cur != FREE) {
                capacityMask = keys.length - 1;
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == null) {
                        break keyPresent;
                    } else if (cur == FREE) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            incrementModCount();
            keys[index] = null;
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
    public short merge(K key, short value, ShortBinaryOperator remappingFunction) {
        if (key != null) {
            if (remappingFunction == null)
                throw new java.lang.NullPointerException();
            // noinspection unchecked
            K[] keys = (K[]) set;
            short[] vals = values;
            int capacityMask, index;
            K cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(key)) & (capacityMask = keys.length - 1)]) != key) {
                keyAbsent:
                if (cur != FREE) {
                    if (keyEquals(key, cur)) {
                        break keyPresent;
                    } else {
                        while (true) {
                            if ((cur = keys[(index = (index - 1) & capacityMask)]) == key) {
                                break keyPresent;
                            } else if (cur == FREE) {
                                break keyAbsent;
                            }
                            else if (keyEquals(key, cur)) {
                                break keyPresent;
                            }
                        }
                    }
                }
                // key is absent
                incrementModCount();
                keys[index] = key;
                vals[index] = value;
                postInsertHook();
                return value;
            }
            // key is present
            short newValue = remappingFunction.applyAsShort(vals[index], value);
            vals[index] = newValue;
            return newValue;
        } else {
            return mergeNullKey(value, remappingFunction);
        }
    }

    short mergeNullKey(short value, ShortBinaryOperator remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        // noinspection unchecked
        K[] keys = (K[]) set;
        short[] vals = values;
        int capacityMask, index;
        K cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            keyAbsent:
            if (cur != FREE) {
                capacityMask = keys.length - 1;
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == null) {
                        break keyPresent;
                    } else if (cur == FREE) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            incrementModCount();
            keys[index] = null;
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
    public short addValue(K key, short value) {
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
    public short addValue(K key, short addition, short defaultValue) {
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
    public void putAll(@Nonnull Map<? extends K, ? extends Short> m) {
        CommonObjShortMapOps.putAll(this, m);
    }


    @Override
    public Short replace(K key, Short value) {
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
    public short replace(K key, short value) {
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
    public boolean replace(K key, Short oldValue, Short newValue) {
        return replace(key,
                oldValue.shortValue(),
                newValue.shortValue());
    }

    @Override
    public boolean replace(K key, short oldValue, short newValue) {
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
            BiFunction<? super K, ? super Short, ? extends Short> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        Object[] keys = set;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                vals[i] = function.apply(key, vals[i]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void replaceAll(ObjShortToShortFunction<? super K> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        Object[] keys = set;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                vals[i] = function.applyAsShort(key, vals[i]);
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
        // noinspection unchecked
        K[] keys = (K[]) set;
        short[] vals = values;
        int capacityMask = keys.length - 1;
        incrementModCount();
        int indexToRemove = index;
        int indexToShift = indexToRemove;
        int shiftDistance = 1;
        while (true) {
            indexToShift = (indexToShift - 1) & capacityMask;
            K keyToShift;
            if ((keyToShift = keys[indexToShift]) == FREE) {
                break;
            }
            if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                keys[indexToRemove] = keyToShift;
                vals[indexToRemove] = vals[indexToShift];
                indexToRemove = indexToShift;
                shiftDistance = 1;
            } else {
                shiftDistance++;
            }
        }
        ((Object[]) keys)[indexToRemove] = FREE;
        postRemoveHook();
    }

    @Override
    public Short remove(Object key) {
        if (key != null) {
            // noinspection unchecked
            K k = (K) key;
            // noinspection unchecked
            K[] keys = (K[]) set;
            int capacityMask = keys.length - 1;
            int index;
            K cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(k)) & capacityMask]) != k) {
                if (cur == FREE) {
                    // key is absent
                    return null;
                } else {
                    if (keyEquals(k, cur)) {
                        break keyPresent;
                    } else {
                        while (true) {
                            if ((cur = keys[(index = (index - 1) & capacityMask)]) == k) {
                                break keyPresent;
                            } else if (cur == FREE) {
                                // key is absent
                                return null;
                            }
                            else if (keyEquals(k, cur)) {
                                break keyPresent;
                            }
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
                K keyToShift;
                if ((keyToShift = keys[indexToShift]) == FREE) {
                    break;
                }
                if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                    keys[indexToRemove] = keyToShift;
                    vals[indexToRemove] = vals[indexToShift];
                    indexToRemove = indexToShift;
                    shiftDistance = 1;
                } else {
                    shiftDistance++;
                }
            }
            ((Object[]) keys)[indexToRemove] = FREE;
            postRemoveHook();
            return val;
        } else {
            return removeNullKey();
        }
    }

    Short removeNullKey() {
        // noinspection unchecked
        K[] keys = (K[]) set;
        int capacityMask = keys.length - 1;
        int index;
        K cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            if (cur == FREE) {
                // key is absent
                return null;
            } else {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == null) {
                        break keyPresent;
                    } else if (cur == FREE) {
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
            K keyToShift;
            if ((keyToShift = keys[indexToShift]) == FREE) {
                break;
            }
            if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                keys[indexToRemove] = keyToShift;
                vals[indexToRemove] = vals[indexToShift];
                indexToRemove = indexToShift;
                shiftDistance = 1;
            } else {
                shiftDistance++;
            }
        }
        ((Object[]) keys)[indexToRemove] = FREE;
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
            int capacityMask = keys.length - 1;
            int index;
            K cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(k)) & capacityMask]) != k) {
                if (cur == FREE) {
                    // key is absent
                    return false;
                } else {
                    if (keyEquals(k, cur)) {
                        break keyPresent;
                    } else {
                        while (true) {
                            if ((cur = keys[(index = (index - 1) & capacityMask)]) == k) {
                                break keyPresent;
                            } else if (cur == FREE) {
                                // key is absent
                                return false;
                            }
                            else if (keyEquals(k, cur)) {
                                break keyPresent;
                            }
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
                K keyToShift;
                if ((keyToShift = keys[indexToShift]) == FREE) {
                    break;
                }
                if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                    keys[indexToRemove] = keyToShift;
                    vals[indexToRemove] = vals[indexToShift];
                    indexToRemove = indexToShift;
                    shiftDistance = 1;
                } else {
                    shiftDistance++;
                }
            }
            ((Object[]) keys)[indexToRemove] = FREE;
            postRemoveHook();
            return true;
        } else {
            return justRemoveNullKey();
        }
    }

    boolean justRemoveNullKey() {
        // noinspection unchecked
        K[] keys = (K[]) set;
        int capacityMask = keys.length - 1;
        int index;
        K cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            if (cur == FREE) {
                // key is absent
                return false;
            } else {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == null) {
                        break keyPresent;
                    } else if (cur == FREE) {
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
            K keyToShift;
            if ((keyToShift = keys[indexToShift]) == FREE) {
                break;
            }
            if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                keys[indexToRemove] = keyToShift;
                vals[indexToRemove] = vals[indexToShift];
                indexToRemove = indexToShift;
                shiftDistance = 1;
            } else {
                shiftDistance++;
            }
        }
        ((Object[]) keys)[indexToRemove] = FREE;
        postRemoveHook();
        return true;
    }


    

    @Override
    public short removeAsShort(Object key) {
        if (key != null) {
            // noinspection unchecked
            K k = (K) key;
            // noinspection unchecked
            K[] keys = (K[]) set;
            int capacityMask = keys.length - 1;
            int index;
            K cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(k)) & capacityMask]) != k) {
                if (cur == FREE) {
                    // key is absent
                    return defaultValue();
                } else {
                    if (keyEquals(k, cur)) {
                        break keyPresent;
                    } else {
                        while (true) {
                            if ((cur = keys[(index = (index - 1) & capacityMask)]) == k) {
                                break keyPresent;
                            } else if (cur == FREE) {
                                // key is absent
                                return defaultValue();
                            }
                            else if (keyEquals(k, cur)) {
                                break keyPresent;
                            }
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
                K keyToShift;
                if ((keyToShift = keys[indexToShift]) == FREE) {
                    break;
                }
                if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                    keys[indexToRemove] = keyToShift;
                    vals[indexToRemove] = vals[indexToShift];
                    indexToRemove = indexToShift;
                    shiftDistance = 1;
                } else {
                    shiftDistance++;
                }
            }
            ((Object[]) keys)[indexToRemove] = FREE;
            postRemoveHook();
            return val;
        } else {
            return removeAsShortNullKey();
        }
    }

    short removeAsShortNullKey() {
        // noinspection unchecked
        K[] keys = (K[]) set;
        int capacityMask = keys.length - 1;
        int index;
        K cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            if (cur == FREE) {
                // key is absent
                return defaultValue();
            } else {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == null) {
                        break keyPresent;
                    } else if (cur == FREE) {
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
            K keyToShift;
            if ((keyToShift = keys[indexToShift]) == FREE) {
                break;
            }
            if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                keys[indexToRemove] = keyToShift;
                vals[indexToRemove] = vals[indexToShift];
                indexToRemove = indexToShift;
                shiftDistance = 1;
            } else {
                shiftDistance++;
            }
        }
        ((Object[]) keys)[indexToRemove] = FREE;
        postRemoveHook();
        return val;
    }


    @Override
    public boolean remove(Object key, Object value) {
        return remove(key,
                ((Short) value).shortValue()
                );
    }

    @Override
    public boolean remove(Object key, short value) {
        if (key != null) {
            // noinspection unchecked
            K k = (K) key;
            // noinspection unchecked
            K[] keys = (K[]) set;
            int capacityMask = keys.length - 1;
            int index;
            K cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(k)) & capacityMask]) != k) {
                if (cur == FREE) {
                    // key is absent
                    return false;
                } else {
                    if (keyEquals(k, cur)) {
                        break keyPresent;
                    } else {
                        while (true) {
                            if ((cur = keys[(index = (index - 1) & capacityMask)]) == k) {
                                break keyPresent;
                            } else if (cur == FREE) {
                                // key is absent
                                return false;
                            }
                            else if (keyEquals(k, cur)) {
                                break keyPresent;
                            }
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
                    K keyToShift;
                    if ((keyToShift = keys[indexToShift]) == FREE) {
                        break;
                    }
                    if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                        keys[indexToRemove] = keyToShift;
                        vals[indexToRemove] = vals[indexToShift];
                        indexToRemove = indexToShift;
                        shiftDistance = 1;
                    } else {
                        shiftDistance++;
                    }
                }
                ((Object[]) keys)[indexToRemove] = FREE;
                postRemoveHook();
                return true;
            } else {
                return false;
            }
        } else {
            return removeEntryNullKey(value);
        }
    }

    boolean removeEntryNullKey(short value) {
        // noinspection unchecked
        K[] keys = (K[]) set;
        int capacityMask = keys.length - 1;
        int index;
        K cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            if (cur == FREE) {
                // key is absent
                return false;
            } else {
                while (true) {
                    if ((cur = keys[(index = (index - 1) & capacityMask)]) == null) {
                        break keyPresent;
                    } else if (cur == FREE) {
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
                K keyToShift;
                if ((keyToShift = keys[indexToShift]) == FREE) {
                    break;
                }
                if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                    keys[indexToRemove] = keyToShift;
                    vals[indexToRemove] = vals[indexToShift];
                    indexToRemove = indexToShift;
                    shiftDistance = 1;
                } else {
                    shiftDistance++;
                }
            }
            ((Object[]) keys)[indexToRemove] = FREE;
            postRemoveHook();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeIf(ObjShortPredicate<? super K> filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        Object[] keys = set;
        int capacityMask = keys.length - 1;
        int firstDelayedRemoved = -1;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                if (filter.test(key, vals[i])) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            K keyToShift;
                            // noinspection unchecked
                            if ((keyToShift = (K) keys[indexToShift]) == FREE) {
                                break;
                            }
                            if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    keys[indexToRemove] = REMOVED;
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
                            }
                        }
                        keys[indexToRemove] = FREE;
                        postRemoveHook();
                    } else {
                        keys[i] = REMOVED;
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




    // under this condition - operations, overridden from MutableSeparateKVObjLHashGO
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
        Object[] keys = set;
        int capacityMask = keys.length - 1;
        int firstDelayedRemoved = -1;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
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
                            K keyToShift;
                            // noinspection unchecked
                            if ((keyToShift = (K) keys[indexToShift]) == FREE) {
                                break;
                            }
                            if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    keys[indexToRemove] = REMOVED;
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
                            }
                        }
                        keys[indexToRemove] = FREE;
                        postRemoveHook();
                    } else {
                        keys[i] = REMOVED;
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
    public boolean removeAll(@Nonnull HashObjSet<K> thisC, @Nonnull Collection<?> c) {
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        Object[] keys = set;
        int capacityMask = keys.length - 1;
        int firstDelayedRemoved = -1;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
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
                            K keyToShift;
                            // noinspection unchecked
                            if ((keyToShift = (K) keys[indexToShift]) == FREE) {
                                break;
                            }
                            if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    keys[indexToRemove] = REMOVED;
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
                            }
                        }
                        keys[indexToRemove] = FREE;
                        postRemoveHook();
                    } else {
                        keys[i] = REMOVED;
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
        Object[] keys = set;
        int capacityMask = keys.length - 1;
        int firstDelayedRemoved = -1;
        short[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
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
                            K keyToShift;
                            // noinspection unchecked
                            if ((keyToShift = (K) keys[indexToShift]) == FREE) {
                                break;
                            }
                            if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    keys[indexToRemove] = REMOVED;
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
                            }
                        }
                        keys[indexToRemove] = FREE;
                        postRemoveHook();
                    } else {
                        keys[i] = REMOVED;
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
        // noinspection unchecked
        K[] keys = (K[]) set;
        short[] vals = values;
        int capacityMask = keys.length - 1;
        for (int i = firstDelayedRemoved; i >= 0; i--) {
            if (keys[i] == REMOVED) {
                int indexToRemove = i;
                int indexToShift = indexToRemove;
                int shiftDistance = 1;
                while (true) {
                    indexToShift = (indexToShift - 1) & capacityMask;
                    K keyToShift;
                    if ((keyToShift = keys[indexToShift]) == FREE) {
                        break;
                    }
                    if ((keyToShift != REMOVED) && (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance)) {
                        keys[indexToRemove] = keyToShift;
                        vals[indexToRemove] = vals[indexToShift];
                        indexToRemove = indexToShift;
                        shiftDistance = 1;
                    } else {
                        shiftDistance++;
                    }
                }
                ((Object[]) keys)[indexToRemove] = FREE;
                postRemoveHook();
            }
        }
    }



    @Override
    public ObjIterator<K> iterator() {
        int mc = modCount();
        return new NoRemovedKeyIterator(mc);
    }

    @Override
    public ObjCursor<K> setCursor() {
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
                    K[] keys = this.keys;
                    short[] vals = this.vals;
                    if (keys == set) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            K keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE) {
                                break;
                            }
                            if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.keys == keys) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 1) > 0) {
                                            this.keys = Arrays.copyOf(keys, slotsToCopy);
                                            this.vals = Arrays.copyOf(vals, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                ((Object[]) this.keys)[indexToRemove] = FREE;
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
                            }
                        }
                        ((Object[]) keys)[indexToRemove] = FREE;
                        postRemoveHook();
                    } else {
                        justRemove(keys[index]);
                        keys[index] = null;
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
            Object curKey;
            if ((curKey = this.curKey) != FREE) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE;
                    int index = this.index;
                    K[] keys = this.keys;
                    short[] vals = this.vals;
                    if (keys == set) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            K keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE) {
                                break;
                            }
                            if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.keys == keys) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.keys = Arrays.copyOf(keys, slotsToCopy);
                                            this.vals = Arrays.copyOf(vals, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                ((Object[]) this.keys)[indexToRemove] = FREE;
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
                            }
                        }
                        ((Object[]) keys)[indexToRemove] = FREE;
                        postRemoveHook();
                    } else {
                        justRemove((K) curKey);
                        keys[index] = null;
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }





    class EntryView extends AbstractSetView<Map.Entry<K, Short>>
            implements HashObjSet<Map.Entry<K, Short>>,
            InternalObjCollectionOps<Map.Entry<K, Short>> {

        @Nonnull
        @Override
        public Equivalence<Entry<K, Short>> equivalence() {
            return Equivalence.entryEquivalence(
                    keyEquivalence(),
                    Equivalence.<Short>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return MutableLHashSeparateKVObjShortMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return MutableLHashSeparateKVObjShortMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return MutableLHashSeparateKVObjShortMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<K, Short> e = (Map.Entry<K, Short>) o;
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
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
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
            Object[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
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
        public final void forEach(@Nonnull Consumer<? super Map.Entry<K, Short>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            Object[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    action.accept(new MutableEntry(mc, i, key, vals[i]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<K, Short>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            Object[] keys = set;
            short[] vals = values;
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
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return !terminated;
        }

        @Override
        @Nonnull
        public ObjIterator<Map.Entry<K, Short>> iterator() {
            int mc = modCount();
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<K, Short>> cursor() {
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
            Object[] keys = set;
            short[] vals = values;
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
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    changed |= s.remove(e.with(key, vals[i]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<K, Short>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            Object[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    changed |= c.add(new MutableEntry(mc, i, key, vals[i]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        public int hashCode() {
            return MutableLHashSeparateKVObjShortMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            Object[] keys = set;
            short[] vals = values;
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
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            sb.setCharAt(0, '[');
            sb.setCharAt(sb.length() - 1, ']');
            return sb.toString();
        }

        @Override
        public boolean shrink() {
            return MutableLHashSeparateKVObjShortMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<K, Short> e = (Map.Entry<K, Short>) o;
                K key = e.getKey();
                short value = e.getValue();
                return MutableLHashSeparateKVObjShortMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<K, Short>> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            Object[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
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
                                K keyToShift;
                                // noinspection unchecked
                                if ((keyToShift = (K) keys[indexToShift]) == FREE) {
                                    break;
                                }
                                if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        keys[indexToRemove] = REMOVED;
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
                                }
                            }
                            keys[indexToRemove] = FREE;
                            postRemoveHook();
                        } else {
                            keys[i] = REMOVED;
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
            Object[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
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
                                K keyToShift;
                                // noinspection unchecked
                                if ((keyToShift = (K) keys[indexToShift]) == FREE) {
                                    break;
                                }
                                if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        keys[indexToRemove] = REMOVED;
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
                                }
                            }
                            keys[indexToRemove] = FREE;
                            postRemoveHook();
                        } else {
                            keys[i] = REMOVED;
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
            Object[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
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
                                K keyToShift;
                                // noinspection unchecked
                                if ((keyToShift = (K) keys[indexToShift]) == FREE) {
                                    break;
                                }
                                if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        keys[indexToRemove] = REMOVED;
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
                                }
                            }
                            keys[indexToRemove] = FREE;
                            postRemoveHook();
                        } else {
                            keys[i] = REMOVED;
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
            MutableLHashSeparateKVObjShortMapGO.this.clear();
        }
    }


    abstract class ObjShortEntry extends AbstractEntry<K, Short> {

        abstract K key();

        @Override
        public final K getKey() {
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
            K k2;
            short v2;
            try {
                e2 = (Map.Entry) o;
                k2 = (K) e2.getKey();
                v2 = (Short) e2.getValue();
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


    class MutableEntry extends ObjShortEntry {
        final int modCount;
        private final int index;
        final K key;
        private short value;

        MutableEntry(int modCount, int index, K key, short value) {
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



    class ReusableEntry extends ObjShortEntry {
        private K key;
        private short value;

        ReusableEntry with(K key, short value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public K key() {
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
            return MutableLHashSeparateKVObjShortMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return MutableLHashSeparateKVObjShortMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return MutableLHashSeparateKVObjShortMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(short v) {
            return MutableLHashSeparateKVObjShortMapGO.this.containsValue(v);
        }



        @Override
        public void forEach(Consumer<? super Short> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            Object[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            Object[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            Object[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            Object[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            Object[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            Object[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            Object[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            Object[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            Object[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            Object[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            Object[] keys = set;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
            MutableLHashSeparateKVObjShortMapGO.this.clear();
        }

        
        public boolean removeIf(Predicate<? super Short> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            Object[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
                                K keyToShift;
                                // noinspection unchecked
                                if ((keyToShift = (K) keys[indexToShift]) == FREE) {
                                    break;
                                }
                                if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        keys[indexToRemove] = REMOVED;
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
                                }
                            }
                            keys[indexToRemove] = FREE;
                            postRemoveHook();
                        } else {
                            keys[i] = REMOVED;
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
            Object[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
                                K keyToShift;
                                // noinspection unchecked
                                if ((keyToShift = (K) keys[indexToShift]) == FREE) {
                                    break;
                                }
                                if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        keys[indexToRemove] = REMOVED;
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
                                }
                            }
                            keys[indexToRemove] = FREE;
                            postRemoveHook();
                        } else {
                            keys[i] = REMOVED;
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
            Object[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
                                K keyToShift;
                                // noinspection unchecked
                                if ((keyToShift = (K) keys[indexToShift]) == FREE) {
                                    break;
                                }
                                if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        keys[indexToRemove] = REMOVED;
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
                                }
                            }
                            keys[indexToRemove] = FREE;
                            postRemoveHook();
                        } else {
                            keys[i] = REMOVED;
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
            Object[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
                                K keyToShift;
                                // noinspection unchecked
                                if ((keyToShift = (K) keys[indexToShift]) == FREE) {
                                    break;
                                }
                                if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        keys[indexToRemove] = REMOVED;
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
                                }
                            }
                            keys[indexToRemove] = FREE;
                            postRemoveHook();
                        } else {
                            keys[i] = REMOVED;
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
            Object[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
                                K keyToShift;
                                // noinspection unchecked
                                if ((keyToShift = (K) keys[indexToShift]) == FREE) {
                                    break;
                                }
                                if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        keys[indexToRemove] = REMOVED;
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
                                }
                            }
                            keys[indexToRemove] = FREE;
                            postRemoveHook();
                        } else {
                            keys[i] = REMOVED;
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
            Object[] keys = set;
            int capacityMask = keys.length - 1;
            int firstDelayedRemoved = -1;
            short[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
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
                                K keyToShift;
                                // noinspection unchecked
                                if ((keyToShift = (K) keys[indexToShift]) == FREE) {
                                    break;
                                }
                                if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        keys[indexToRemove] = REMOVED;
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
                                }
                            }
                            keys[indexToRemove] = FREE;
                            postRemoveHook();
                        } else {
                            keys[i] = REMOVED;
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<K, Short>> {
        K[] keys;
        short[] vals;
        final int capacityMask;
        int expectedModCount;
        
        class MutableEntry2 extends MutableEntry {
            MutableEntry2(int modCount, int index, K key, short value) {
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
            // noinspection unchecked
            K[] keys = this.keys = (K[]) set;
            capacityMask = keys.length - 1;
            short[] vals = this.vals = values;
            int nextI = keys.length;
            while (--nextI >= 0) {
                Object key;
                if ((key = keys[nextI]) != FREE) {
                    // noinspection unchecked
                    next = new MutableEntry2(mc, nextI, (K) key, vals[nextI]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<K, Short>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            short[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE) {
                    // noinspection unchecked
                    action.accept(new MutableEntry2(mc, i, (K) key, vals[i]));
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
        public Map.Entry<K, Short> next() {
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
                            next = new MutableEntry2(mc, nextI, (K) key, vals[nextI]);
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
                    K[] keys = this.keys;
                    short[] vals = this.vals;
                    if (keys == set) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            K keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE) {
                                break;
                            }
                            if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.keys == keys) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 1) > 0) {
                                            this.keys = Arrays.copyOf(keys, slotsToCopy);
                                            this.vals = Arrays.copyOf(vals, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                ((Object[]) this.keys)[indexToRemove] = FREE;
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
                            }
                        }
                        ((Object[]) keys)[indexToRemove] = FREE;
                        postRemoveHook();
                    } else {
                        justRemove(keys[index]);
                        keys[index] = null;
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<K, Short>> {
        K[] keys;
        short[] vals;
        final int capacityMask;
        int expectedModCount;
        
        class MutableEntry2 extends MutableEntry {
            MutableEntry2(int modCount, int index, K key, short value) {
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
        Object curKey;
        short curValue;

        NoRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            // noinspection unchecked
            K[] keys = this.keys = (K[]) set;
            capacityMask = keys.length - 1;
            index = keys.length;
            vals = values;
            curKey = FREE;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<K, Short>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            short[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE) {
                    // noinspection unchecked
                    action.accept(new MutableEntry2(mc, i, (K) key, vals[i]));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE;
        }

        @Override
        public Map.Entry<K, Short> elem() {
            Object curKey;
            if ((curKey = this.curKey) != FREE) {
                // noinspection unchecked
                return new MutableEntry2(expectedModCount, index, (K) curKey, curValue);
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
            Object curKey;
            if ((curKey = this.curKey) != FREE) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE;
                    int index = this.index;
                    K[] keys = this.keys;
                    short[] vals = this.vals;
                    if (keys == set) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            K keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE) {
                                break;
                            }
                            if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.keys == keys) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.keys = Arrays.copyOf(keys, slotsToCopy);
                                            this.vals = Arrays.copyOf(vals, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                ((Object[]) this.keys)[indexToRemove] = FREE;
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
                            }
                        }
                        ((Object[]) keys)[indexToRemove] = FREE;
                        postRemoveHook();
                    } else {
                        justRemove((K) curKey);
                        keys[index] = null;
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
        K[] keys;
        short[] vals;
        final int capacityMask;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        short next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            // noinspection unchecked
            K[] keys = this.keys = (K[]) set;
            capacityMask = keys.length - 1;
            short[] vals = this.vals = values;
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
        public short nextShort() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                if (expectedModCount == modCount()) {
                    index = nextI;
                    K[] keys = this.keys;
                    short prev = next;
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
        public void forEachRemaining(Consumer<? super Short> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            short[] vals = this.vals;
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
        public void forEachRemaining(ShortConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            short[] vals = this.vals;
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
        public Short next() {
            return nextShort();
        }

        @Override
        public void remove() {
            int index;
            if ((index = this.index) >= 0) {
                if (expectedModCount++ == modCount()) {
                    this.index = -1;
                    K[] keys = this.keys;
                    short[] vals = this.vals;
                    if (keys == set) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            K keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE) {
                                break;
                            }
                            if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.keys == keys) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 1) > 0) {
                                            this.keys = Arrays.copyOf(keys, slotsToCopy);
                                            this.vals = Arrays.copyOf(vals, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                ((Object[]) this.keys)[indexToRemove] = FREE;
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
                            }
                        }
                        ((Object[]) keys)[indexToRemove] = FREE;
                        postRemoveHook();
                    } else {
                        justRemove(keys[index]);
                        keys[index] = null;
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
        K[] keys;
        short[] vals;
        final int capacityMask;
        int expectedModCount;
        int index;
        Object curKey;
        short curValue;

        NoRemovedValueCursor(int mc) {
            expectedModCount = mc;
            // noinspection unchecked
            K[] keys = this.keys = (K[]) set;
            capacityMask = keys.length - 1;
            index = keys.length;
            vals = values;
            curKey = FREE;
        }

        @Override
        public void forEachForward(ShortConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            short[] vals = this.vals;
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
        public short elem() {
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
            Object curKey;
            if ((curKey = this.curKey) != FREE) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE;
                    int index = this.index;
                    K[] keys = this.keys;
                    short[] vals = this.vals;
                    if (keys == set) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            K keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE) {
                                break;
                            }
                            if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.keys == keys) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.keys = Arrays.copyOf(keys, slotsToCopy);
                                            this.vals = Arrays.copyOf(vals, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                ((Object[]) this.keys)[indexToRemove] = FREE;
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
                            }
                        }
                        ((Object[]) keys)[indexToRemove] = FREE;
                        postRemoveHook();
                    } else {
                        justRemove((K) curKey);
                        keys[index] = null;
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }



    class NoRemovedMapCursor implements ObjShortCursor<K> {
        K[] keys;
        short[] vals;
        final int capacityMask;
        int expectedModCount;
        int index;
        Object curKey;
        short curValue;

        NoRemovedMapCursor(int mc) {
            expectedModCount = mc;
            // noinspection unchecked
            K[] keys = this.keys = (K[]) set;
            capacityMask = keys.length - 1;
            index = keys.length;
            vals = values;
            curKey = FREE;
        }

        @Override
        public void forEachForward(ObjShortConsumer<? super K> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            short[] vals = this.vals;
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
        public short value() {
            if (curKey != FREE) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(short value) {
            if (curKey != FREE) {
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
            Object curKey;
            if ((curKey = this.curKey) != FREE) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE;
                    int index = this.index;
                    K[] keys = this.keys;
                    short[] vals = this.vals;
                    if (keys == set) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            K keyToShift;
                            if ((keyToShift = keys[indexToShift]) == FREE) {
                                break;
                            }
                            if (((SeparateKVObjKeyMixing.mix(nullableKeyHashCode(keyToShift)) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.keys == keys) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.keys = Arrays.copyOf(keys, slotsToCopy);
                                            this.vals = Arrays.copyOf(vals, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                ((Object[]) this.keys)[indexToRemove] = FREE;
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
                            }
                        }
                        ((Object[]) keys)[indexToRemove] = FREE;
                        postRemoveHook();
                    } else {
                        justRemove((K) curKey);
                        keys[index] = null;
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

