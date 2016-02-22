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
import net.openhft.koloboke.function.ShortObjConsumer;
import net.openhft.koloboke.function.ShortObjPredicate;
import net.openhft.koloboke.function.ShortObjFunction;
import net.openhft.koloboke.function.ShortFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import java.util.*;


public class UpdatableQHashSeparateKVShortObjMapGO<V>
        extends UpdatableQHashSeparateKVShortObjMapSO<V> {

    @Override
    final void copy(SeparateKVShortObjQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVShortObjQHash hash) {
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
    public boolean containsEntry(short key, Object value) {
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
        int index = index((Short) key);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return null;
        }
    }

    

    @Override
    public V get(short key) {
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
    public V getOrDefault(Object key, V defaultValue) {
        int index = index((Short) key);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public V getOrDefault(short key, V defaultValue) {
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
    public void forEach(BiConsumer<? super Short, ? super V> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        short free = freeValue;
        short[] keys = set;
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            short key;
            if ((key = keys[i]) != free) {
                action.accept(key, vals[i]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void forEach(ShortObjConsumer<? super V> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        short free = freeValue;
        short[] keys = set;
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            short key;
            if ((key = keys[i]) != free) {
                action.accept(key, vals[i]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public boolean forEachWhile(ShortObjPredicate<? super V> predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        short free = freeValue;
        short[] keys = set;
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            short key;
            if ((key = keys[i]) != free) {
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
    public ShortObjCursor<V> cursor() {
        int mc = modCount();
        return new NoRemovedMapCursor(mc);
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonShortObjMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalShortObjMapOps<?> m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        short free = freeValue;
        short[] keys = set;
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            short key;
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
    public void reversePutAllTo(InternalShortObjMapOps<? super V> m) {
        if (isEmpty())
            return;
        int mc = modCount();
        short free = freeValue;
        short[] keys = set;
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            short key;
            if ((key = keys[i]) != free) {
                m.justPut(key, vals[i]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<Short, V>> entrySet() {
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
        short free = freeValue;
        short[] keys = set;
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            short key;
            if ((key = keys[i]) != free) {
                hashCode += key ^ nullableValueHashCode(vals[i]);
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
        short[] keys = set;
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            short key;
            if ((key = keys[i]) != free) {
                sb.append(' ');
                sb.append(key);
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
        short free = freeValue;
        short[] keys = set;
        V[] vals = values;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        short[] newKeys = set;
        int capacity = newKeys.length;
        V[] newVals = values;
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
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }


    @Override
    public V put(Short key, V value) {
        int index = insert(key, value);
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
    public V put(short key, V value) {
        int index = insert(key, value);
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
    public V putIfAbsent(Short key, V value) {
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
    public V putIfAbsent(short key, V value) {
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
    public void justPut(short key, V value) {
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
    public V compute(Short key,
            BiFunction<? super Short, ? super V, ? extends V> remappingFunction) {
        short k = key;
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        short free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        short[] keys = set;
        V[] vals = values;
        int capacity, index;
        short cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVShortKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
            keyAbsent:
            if (cur != free) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == free) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == k) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == free) {
                        index = fIndex;
                        break keyAbsent;
                    }
                    step += 2;
                }
            }
            // key is absent
            V newValue = remappingFunction.apply(k, null);
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
        V newValue = remappingFunction.apply(k, vals[index]);
        if (newValue != null) {
            vals[index] = newValue;
            return newValue;
        } else {
            throw new java.lang.UnsupportedOperationException("Compute operation of updatable map doesn't support removals");
        }
    }


    @Override
    public V compute(short key, ShortObjFunction<? super V, ? extends V> remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        short free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        short[] keys = set;
        V[] vals = values;
        int capacity, index;
        short cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVShortKeyMixing.mix(key) % (capacity = keys.length)]) != key) {
            keyAbsent:
            if (cur != free) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == key) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == free) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == key) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == free) {
                        index = fIndex;
                        break keyAbsent;
                    }
                    step += 2;
                }
            }
            // key is absent
            V newValue = remappingFunction.apply(key, null);
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
        V newValue = remappingFunction.apply(key, vals[index]);
        if (newValue != null) {
            vals[index] = newValue;
            return newValue;
        } else {
            throw new java.lang.UnsupportedOperationException("Compute operation of updatable map doesn't support removals");
        }
    }


    @Override
    public V computeIfAbsent(Short key,
            Function<? super Short, ? extends V> mappingFunction) {
        short k = key;
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        short free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        short[] keys = set;
        V[] vals = values;
        int capacity, index;
        short cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVShortKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
            keyAbsent:
            if (cur != free) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == free) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == k) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == free) {
                        index = fIndex;
                        break keyAbsent;
                    }
                    step += 2;
                }
            }
            // key is absent
            V value = mappingFunction.apply(k);
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
        // key is present
        V val;
        if ((val = vals[index]) != null) {
            return val;
        } else {
            V value = mappingFunction.apply(k);
            if (value != null) {
                vals[index] = value;
                return value;
            } else {
                return null;
            }
        }
    }


    @Override
    public V computeIfAbsent(short key, ShortFunction<? extends V> mappingFunction) {
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        short free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        short[] keys = set;
        V[] vals = values;
        int capacity, index;
        short cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVShortKeyMixing.mix(key) % (capacity = keys.length)]) != key) {
            keyAbsent:
            if (cur != free) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == key) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == free) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == key) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == free) {
                        index = fIndex;
                        break keyAbsent;
                    }
                    step += 2;
                }
            }
            // key is absent
            V value = mappingFunction.apply(key);
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
        // key is present
        V val;
        if ((val = vals[index]) != null) {
            return val;
        } else {
            V value = mappingFunction.apply(key);
            if (value != null) {
                vals[index] = value;
                return value;
            } else {
                return null;
            }
        }
    }


    @Override
    public V computeIfPresent(Short key,
            BiFunction<? super Short, ? super V, ? extends V> remappingFunction) {
        short k = key;
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(k);
        if (index >= 0) {
            // key is present
            V[] vals = values;
            V val;
            if ((val = vals[index]) != null) {
                V newValue = remappingFunction.apply(k, val);
                if (newValue != null) {
                    vals[index] = newValue;
                    return newValue;
                } else {
                    throw new java.lang.UnsupportedOperationException("ComputeIfPresent operation of updatable map doesn't support removals");
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
    public V computeIfPresent(short key, ShortObjFunction<? super V, ? extends V> remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(key);
        if (index >= 0) {
            // key is present
            V[] vals = values;
            V val;
            if ((val = vals[index]) != null) {
                V newValue = remappingFunction.apply(key, val);
                if (newValue != null) {
                    vals[index] = newValue;
                    return newValue;
                } else {
                    throw new java.lang.UnsupportedOperationException("ComputeIfPresent operation of updatable map doesn't support removals");
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
    public V merge(Short key, V value,
            BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        short k = key;
        if (value == null)
            throw new java.lang.NullPointerException();
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        short free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        short[] keys = set;
        V[] vals = values;
        int capacity, index;
        short cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVShortKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
            keyAbsent:
            if (cur != free) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == free) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == k) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == free) {
                        index = fIndex;
                        break keyAbsent;
                    }
                    step += 2;
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
        V val;
        if ((val = vals[index]) != null) {
            V newValue = remappingFunction.apply(val, value);
            if (newValue != null) {
                vals[index] = newValue;
                return newValue;
            } else {
                throw new java.lang.UnsupportedOperationException("Merge operation of updatable map doesn't support removals");
            }
        } else {
            vals[index] = value;
            return value;
        }
    }


    @Override
    public V merge(short key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        if (value == null)
            throw new java.lang.NullPointerException();
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        short free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        short[] keys = set;
        V[] vals = values;
        int capacity, index;
        short cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVShortKeyMixing.mix(key) % (capacity = keys.length)]) != key) {
            keyAbsent:
            if (cur != free) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == key) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == free) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == key) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == free) {
                        index = fIndex;
                        break keyAbsent;
                    }
                    step += 2;
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
        V val;
        if ((val = vals[index]) != null) {
            V newValue = remappingFunction.apply(val, value);
            if (newValue != null) {
                vals[index] = newValue;
                return newValue;
            } else {
                throw new java.lang.UnsupportedOperationException("Merge operation of updatable map doesn't support removals");
            }
        } else {
            vals[index] = value;
            return value;
        }
    }




    @Override
    public void putAll(@Nonnull Map<? extends Short, ? extends V> m) {
        CommonShortObjMapOps.putAll(this, m);
    }


    @Override
    public V replace(Short key, V value) {
        int index = index(key);
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
    public V replace(short key, V value) {
        int index = index(key);
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
    public boolean replace(Short key, V oldValue, V newValue) {
        return replace(key.shortValue(),
                oldValue,
                newValue);
    }

    @Override
    public boolean replace(short key, V oldValue, V newValue) {
        int index = index(key);
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
            BiFunction<? super Short, ? super V, ? extends V> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        short free = freeValue;
        short[] keys = set;
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            short key;
            if ((key = keys[i]) != free) {
                vals[i] = function.apply(key, vals[i]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void replaceAll(ShortObjFunction<? super V, ? extends V> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        short free = freeValue;
        short[] keys = set;
        V[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            short key;
            if ((key = keys[i]) != free) {
                vals[i] = function.apply(key, vals[i]);
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
    public V remove(Object key) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean justRemove(short key) {
        throw new java.lang.UnsupportedOperationException();
    }



    

    @Override
    public V remove(short key) {
        throw new java.lang.UnsupportedOperationException();
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Short) key).shortValue(),
                value);
    }

    @Override
    public boolean remove(short key, Object value) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean removeIf(ShortObjPredicate<? super V> filter) {
        throw new java.lang.UnsupportedOperationException();
    }






    class EntryView extends AbstractSetView<Map.Entry<Short, V>>
            implements HashObjSet<Map.Entry<Short, V>>,
            InternalObjCollectionOps<Map.Entry<Short, V>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Short, V>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Short>defaultEquality()
                    ,
                    valueEquivalence()
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return UpdatableQHashSeparateKVShortObjMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return UpdatableQHashSeparateKVShortObjMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return UpdatableQHashSeparateKVShortObjMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Short, V> e = (Map.Entry<Short, V>) o;
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
            short[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
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
            short free = freeValue;
            short[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
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
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Short, V>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            short free = freeValue;
            short[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    action.accept(new MutableEntry(mc, i, key, vals[i]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Short, V>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            short free = freeValue;
            short[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
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
        public ObjIterator<Map.Entry<Short, V>> iterator() {
            int mc = modCount();
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Short, V>> cursor() {
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
            short free = freeValue;
            short[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
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
            short free = freeValue;
            short[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    changed |= s.remove(e.with(key, vals[i]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Short, V>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            short free = freeValue;
            short[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    changed |= c.add(new MutableEntry(mc, i, key, vals[i]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        public int hashCode() {
            return UpdatableQHashSeparateKVShortObjMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            short free = freeValue;
            short[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                short key;
                if ((key = keys[i]) != free) {
                    sb.append(' ');
                    sb.append(key);
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
            return UpdatableQHashSeparateKVShortObjMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Short, V> e = (Map.Entry<Short, V>) o;
                short key = e.getKey();
                V value = e.getValue();
                return UpdatableQHashSeparateKVShortObjMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Short, V>> filter) {
            throw new java.lang.UnsupportedOperationException();
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
            throw new java.lang.UnsupportedOperationException();
        }

        @Override
        public final boolean retainAll(@Nonnull Collection<?> c) {
            throw new java.lang.UnsupportedOperationException();
        }

        @Override
        public void clear() {
            UpdatableQHashSeparateKVShortObjMapGO.this.clear();
        }
    }


    abstract class ShortObjEntry extends AbstractEntry<Short, V> {

        abstract short key();

        @Override
        public final Short getKey() {
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
            short k2;
            V v2;
            try {
                e2 = (Map.Entry) o;
                k2 = (Short) e2.getKey();
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


    class MutableEntry extends ShortObjEntry {
        final int modCount;
        private final int index;
        final short key;
        private V value;

        MutableEntry(int modCount, int index, short key, V value) {
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



    class ReusableEntry extends ShortObjEntry {
        private short key;
        private V value;

        ReusableEntry with(short key, V value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public short key() {
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
            return UpdatableQHashSeparateKVShortObjMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return UpdatableQHashSeparateKVShortObjMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return UpdatableQHashSeparateKVShortObjMapGO.this.containsValue(o);
        }



        @Override
        public void forEach(Consumer<? super V> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            short free = freeValue;
            short[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
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
            short free = freeValue;
            short[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
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
            short free = freeValue;
            short[] keys = set;
            V[] vals = values;
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
        public boolean reverseAddAllTo(ObjCollection<? super V> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            short free = freeValue;
            short[] keys = set;
            V[] vals = values;
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
        public boolean reverseRemoveAllFrom(ObjSet<?> s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            short free = freeValue;
            short[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
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
            return new NoRemovedValueIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<V> cursor() {
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
            short free = freeValue;
            short[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
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
            short free = freeValue;
            short[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
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
            short free = freeValue;
            short[] keys = set;
            V[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != free) {
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
            UpdatableQHashSeparateKVShortObjMapGO.this.clear();
        }

        @Override
        public boolean removeIf(Predicate<? super V> filter) {
            throw new java.lang.UnsupportedOperationException();
        }


        @Override
        public boolean removeAll(@Nonnull Collection<?> c) {
            throw new java.lang.UnsupportedOperationException();
        }


        @Override
        public boolean retainAll(@Nonnull Collection<?> c) {
            throw new java.lang.UnsupportedOperationException();
        }

    }



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Short, V>> {
        final short[] keys;
        final V[] vals;
        final short free;
        int expectedModCount;
        int nextIndex;
        MutableEntry next;

        NoRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            short[] keys = this.keys = set;
            V[] vals = this.vals = values;
            short free = this.free = freeValue;
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
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Short, V>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            short[] keys = this.keys;
            V[] vals = this.vals;
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
            nextIndex = -1;
        }

        @Override
        public boolean hasNext() {
            return nextIndex >= 0;
        }

        @Override
        public Map.Entry<Short, V> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
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
            throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Short, V>> {
        final short[] keys;
        final V[] vals;
        final short free;
        int expectedModCount;
        int index;
        short curKey;
        V curValue;

        NoRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            short free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Short, V>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            short[] keys = this.keys;
            V[] vals = this.vals;
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
        public Map.Entry<Short, V> elem() {
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
            throw new java.lang.UnsupportedOperationException();
        }
    }




    class NoRemovedValueIterator implements ObjIterator<V> {
        final short[] keys;
        final V[] vals;
        final short free;
        int expectedModCount;
        int nextIndex;
        V next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            short[] keys = this.keys = set;
            V[] vals = this.vals = values;
            short free = this.free = freeValue;
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
        public void forEachRemaining(Consumer<? super V> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            short[] keys = this.keys;
            V[] vals = this.vals;
            short free = this.free;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if (keys[i] != free) {
                    action.accept(vals[i]);
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            nextIndex = -1;
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
                    short[] keys = this.keys;
                    short free = this.free;
                    V prev = next;
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
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedValueCursor implements ObjCursor<V> {
        final short[] keys;
        final V[] vals;
        final short free;
        int expectedModCount;
        int index;
        short curKey;
        V curValue;

        NoRemovedValueCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            short free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super V> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            short[] keys = this.keys;
            V[] vals = this.vals;
            short free = this.free;
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
        public V elem() {
            if (curKey != free) {
                return curValue;
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
            throw new java.lang.UnsupportedOperationException();
        }
    }



    class NoRemovedMapCursor implements ShortObjCursor<V> {
        final short[] keys;
        final V[] vals;
        final short free;
        int expectedModCount;
        int index;
        short curKey;
        V curValue;

        NoRemovedMapCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            short free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ShortObjConsumer<? super V> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            short[] keys = this.keys;
            V[] vals = this.vals;
            short free = this.free;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                short key;
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
        public short key() {
            short curKey;
            if ((curKey = this.curKey) != free) {
                return curKey;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public V value() {
            if (curKey != free) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(V value) {
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
            throw new java.lang.UnsupportedOperationException();
        }
    }
}

