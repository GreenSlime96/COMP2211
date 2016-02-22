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
import net.openhft.koloboke.function.ObjFloatConsumer;
import net.openhft.koloboke.function.ObjFloatPredicate;
import net.openhft.koloboke.function.ObjFloatToFloatFunction;
import net.openhft.koloboke.function.ToFloatFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.openhft.koloboke.function.FloatBinaryOperator;
import net.openhft.koloboke.function.FloatConsumer;
import net.openhft.koloboke.function.FloatPredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class UpdatableQHashSeparateKVObjFloatMapGO<K>
        extends UpdatableQHashSeparateKVObjFloatMapSO<K> {

    @Override
    final void copy(SeparateKVObjFloatQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVObjFloatQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.move(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }


    @Override
    public float defaultValue() {
        return 0.0f;
    }

    @Override
    public boolean containsEntry(Object key, float value) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return values[index] == Float.floatToIntBits(value);
        } else {
            // key is absent
            return false;
        }
    }

    @Override
    public boolean containsEntry(Object key, int value) {
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
    public Float get(Object key) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return Float.intBitsToFloat(values[index]);
        } else {
            // key is absent
            return null;
        }
    }

    

    @Override
    public float getFloat(Object key) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return Float.intBitsToFloat(values[index]);
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Float getOrDefault(Object key, Float defaultValue) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return Float.intBitsToFloat(values[index]);
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public float getOrDefault(Object key, float defaultValue) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return Float.intBitsToFloat(values[index]);
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super Float> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        Object[] keys = set;
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                action.accept(key, Float.intBitsToFloat(vals[i]));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void forEach(ObjFloatConsumer<? super K> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        Object[] keys = set;
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                action.accept(key, Float.intBitsToFloat(vals[i]));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public boolean forEachWhile(ObjFloatPredicate<? super K> predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        Object[] keys = set;
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                if (!predicate.test(key, Float.intBitsToFloat(vals[i]))) {
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
    public ObjFloatCursor<K> cursor() {
        int mc = modCount();
        return new NoRemovedMapCursor(mc);
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonObjFloatMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalObjFloatMapOps<?> m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        Object[] keys = set;
        int[] vals = values;
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
    public void reversePutAllTo(InternalObjFloatMapOps<? super K> m) {
        if (isEmpty())
            return;
        int mc = modCount();
        Object[] keys = set;
        int[] vals = values;
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
    public HashObjSet<Map.Entry<K, Float>> entrySet() {
        return new EntryView();
    }

    @Override
    @Nonnull
    public FloatCollection values() {
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
        int[] vals = values;
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
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                sb.append(' ');
                sb.append(key != this ? key : "(this Map)");
                sb.append('=');
                sb.append(Float.intBitsToFloat(vals[i]));
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
        int[] vals = values;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        Object[] newKeys = set;
        int capacity = newKeys.length;
        int[] newVals = values;
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
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }


    @Override
    public Float put(K key, Float value) {
        int index = insert(key, Float.floatToIntBits(value));
        if (index < 0) {
            // key was absent
            return null;
        } else {
            // key is present
            int[] vals = values;
            float prevValue = Float.intBitsToFloat(vals[index]);
            vals[index] = Float.floatToIntBits(value);
            return prevValue;
        }
    }

    @Override
    public float put(K key, float value) {
        int index = insert(key, Float.floatToIntBits(value));
        if (index < 0) {
            // key was absent
            return defaultValue();
        } else {
            // key is present
            int[] vals = values;
            float prevValue = Float.intBitsToFloat(vals[index]);
            vals[index] = Float.floatToIntBits(value);
            return prevValue;
        }
    }

    @Override
    public Float putIfAbsent(K key, Float value) {
        int index = insert(key, Float.floatToIntBits(value));
        if (index < 0) {
            // key was absent
            return null;
        } else {
            // key is present
            return Float.intBitsToFloat(values[index]);
        }
    }

    @Override
    public float putIfAbsent(K key, float value) {
        int index = insert(key, Float.floatToIntBits(value));
        if (index < 0) {
            // key was absent
            return defaultValue();
        } else {
            // key is present
            return Float.intBitsToFloat(values[index]);
        }
    }

    @Override
    public void justPut(K key, float value) {
        int index = insert(key, Float.floatToIntBits(value));
        if (index < 0) {
            // key was absent
            return;
        } else {
            // key is present
            values[index] = Float.floatToIntBits(value);
            return;
        }
    }

    @Override
    public void justPut(K key, int value) {
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
    public Float compute(K key,
            BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
        if (key != null) {
            if (remappingFunction == null)
                throw new java.lang.NullPointerException();
            // noinspection unchecked
            K[] keys = (K[]) set;
            int[] vals = values;
            int capacity, index;
            K cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(key)) % (capacity = keys.length)]) != key) {
                keyAbsent:
                if (cur != FREE) {
                    if (keyEquals(key, cur)) {
                        break keyPresent;
                    } else {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == key) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
                                index = bIndex;
                                break keyAbsent;
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
                                break keyAbsent;
                            }
                            else if (keyEquals(key, cur)) {
                                index = fIndex;
                                break keyPresent;
                            }
                            step += 2;
                        }
                    }
                }
                // key is absent
                Float newValue = remappingFunction.apply(key, null);
                if (newValue != null) {
                    incrementModCount();
                    keys[index] = key;
                    vals[index] = Float.floatToIntBits(newValue);
                    postInsertHook();
                    return newValue;
                } else {
                    return null;
                }
            }
            // key is present
            Float newValue = remappingFunction.apply(key, Float.intBitsToFloat(vals[index]));
            if (newValue != null) {
                vals[index] = Float.floatToIntBits(newValue);
                return newValue;
            } else {
                throw new java.lang.UnsupportedOperationException("Compute operation of updatable map doesn't support removals");
            }
        } else {
            return computeNullKey(remappingFunction);
        }
    }

    Float computeNullKey(
            BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        // noinspection unchecked
        K[] keys = (K[]) set;
        int[] vals = values;
        int capacity = keys.length;
        int index;
        K cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            keyAbsent:
            if (cur != FREE) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == null) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == null) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        index = fIndex;
                        break keyAbsent;
                    }
                    step += 2;
                }
            }
            // key is absent
            Float newValue = remappingFunction.apply(null, null);
            if (newValue != null) {
                incrementModCount();
                keys[index] = null;
                vals[index] = Float.floatToIntBits(newValue);
                postInsertHook();
                return newValue;
            } else {
                return null;
            }
        }
        // key is present
        Float newValue = remappingFunction.apply(null, Float.intBitsToFloat(vals[index]));
        if (newValue != null) {
            vals[index] = Float.floatToIntBits(newValue);
            return newValue;
        } else {
            throw new java.lang.UnsupportedOperationException("Compute operation of updatable map doesn't support removals");
        }
    }

    @Override
    public float compute(K key, ObjFloatToFloatFunction<? super K> remappingFunction) {
        if (key != null) {
            if (remappingFunction == null)
                throw new java.lang.NullPointerException();
            // noinspection unchecked
            K[] keys = (K[]) set;
            int[] vals = values;
            int capacity, index;
            K cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(key)) % (capacity = keys.length)]) != key) {
                keyAbsent:
                if (cur != FREE) {
                    if (keyEquals(key, cur)) {
                        break keyPresent;
                    } else {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == key) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
                                index = bIndex;
                                break keyAbsent;
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
                                break keyAbsent;
                            }
                            else if (keyEquals(key, cur)) {
                                index = fIndex;
                                break keyPresent;
                            }
                            step += 2;
                        }
                    }
                }
                // key is absent
                float newValue = remappingFunction.applyAsFloat(key, defaultValue());
                incrementModCount();
                keys[index] = key;
                vals[index] = Float.floatToIntBits(newValue);
                postInsertHook();
                return newValue;
            }
            // key is present
            float newValue = remappingFunction.applyAsFloat(key, Float.intBitsToFloat(vals[index]));
            vals[index] = Float.floatToIntBits(newValue);
            return newValue;
        } else {
            return computeNullKey(remappingFunction);
        }
    }

    float computeNullKey(ObjFloatToFloatFunction<? super K> remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        // noinspection unchecked
        K[] keys = (K[]) set;
        int[] vals = values;
        int capacity = keys.length;
        int index;
        K cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            keyAbsent:
            if (cur != FREE) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == null) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == null) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        index = fIndex;
                        break keyAbsent;
                    }
                    step += 2;
                }
            }
            // key is absent
            float newValue = remappingFunction.applyAsFloat(null, defaultValue());
            incrementModCount();
            keys[index] = null;
            vals[index] = Float.floatToIntBits(newValue);
            postInsertHook();
            return newValue;
        }
        // key is present
        float newValue = remappingFunction.applyAsFloat(null, Float.intBitsToFloat(vals[index]));
        vals[index] = Float.floatToIntBits(newValue);
        return newValue;
    }

    @Override
    public Float computeIfAbsent(K key,
            Function<? super K, ? extends Float> mappingFunction) {
        if (key != null) {
            if (mappingFunction == null)
                throw new java.lang.NullPointerException();
            // noinspection unchecked
            K[] keys = (K[]) set;
            int[] vals = values;
            int capacity, index;
            K cur;
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(key)) % (capacity = keys.length)]) == key) {
                // key is present
                return Float.intBitsToFloat(vals[index]);
            } else {
                keyAbsent:
                if (cur != FREE) {
                    if (keyEquals(key, cur)) {
                        // key is present
                        return Float.intBitsToFloat(vals[index]);
                    } else {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == key) {
                                // key is present
                                return Float.intBitsToFloat(vals[bIndex]);
                            } else if (cur == FREE) {
                                index = bIndex;
                                break keyAbsent;
                            }
                            else if (keyEquals(key, cur)) {
                                // key is present
                                return Float.intBitsToFloat(vals[bIndex]);
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == key) {
                                // key is present
                                return Float.intBitsToFloat(vals[fIndex]);
                            } else if (cur == FREE) {
                                index = fIndex;
                                break keyAbsent;
                            }
                            else if (keyEquals(key, cur)) {
                                // key is present
                                return Float.intBitsToFloat(vals[fIndex]);
                            }
                            step += 2;
                        }
                    }
                }
                // key is absent
                Float value = mappingFunction.apply(key);
                if (value != null) {
                    incrementModCount();
                    keys[index] = key;
                    vals[index] = Float.floatToIntBits(value);
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

    Float computeIfAbsentNullKey(
            Function<? super K, ? extends Float> mappingFunction) {
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        // noinspection unchecked
        K[] keys = (K[]) set;
        int[] vals = values;
        int capacity = keys.length;
        int index;
        K cur;
        if ((cur = keys[index = 0]) == null) {
            // key is present
            return Float.intBitsToFloat(vals[index]);
        } else {
            keyAbsent:
            if (cur != FREE) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == null) {
                        // key is present
                        return Float.intBitsToFloat(vals[bIndex]);
                    } else if (cur == FREE) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == null) {
                        // key is present
                        return Float.intBitsToFloat(vals[fIndex]);
                    } else if (cur == FREE) {
                        index = fIndex;
                        break keyAbsent;
                    }
                    step += 2;
                }
            }
            // key is absent
            Float value = mappingFunction.apply(null);
            if (value != null) {
                incrementModCount();
                keys[index] = null;
                vals[index] = Float.floatToIntBits(value);
                postInsertHook();
                return value;
            } else {
                return null;
            }
        }
    }

    @Override
    public float computeIfAbsent(K key, ToFloatFunction<? super K> mappingFunction) {
        if (key != null) {
            if (mappingFunction == null)
                throw new java.lang.NullPointerException();
            // noinspection unchecked
            K[] keys = (K[]) set;
            int[] vals = values;
            int capacity, index;
            K cur;
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(key)) % (capacity = keys.length)]) == key) {
                // key is present
                return Float.intBitsToFloat(vals[index]);
            } else {
                keyAbsent:
                if (cur != FREE) {
                    if (keyEquals(key, cur)) {
                        // key is present
                        return Float.intBitsToFloat(vals[index]);
                    } else {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == key) {
                                // key is present
                                return Float.intBitsToFloat(vals[bIndex]);
                            } else if (cur == FREE) {
                                index = bIndex;
                                break keyAbsent;
                            }
                            else if (keyEquals(key, cur)) {
                                // key is present
                                return Float.intBitsToFloat(vals[bIndex]);
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = keys[fIndex]) == key) {
                                // key is present
                                return Float.intBitsToFloat(vals[fIndex]);
                            } else if (cur == FREE) {
                                index = fIndex;
                                break keyAbsent;
                            }
                            else if (keyEquals(key, cur)) {
                                // key is present
                                return Float.intBitsToFloat(vals[fIndex]);
                            }
                            step += 2;
                        }
                    }
                }
                // key is absent
                float value = mappingFunction.applyAsFloat(key);
                incrementModCount();
                keys[index] = key;
                vals[index] = Float.floatToIntBits(value);
                postInsertHook();
                return value;
            }
        } else {
            return computeIfAbsentNullKey(mappingFunction);
        }
    }

    float computeIfAbsentNullKey(ToFloatFunction<? super K> mappingFunction) {
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        // noinspection unchecked
        K[] keys = (K[]) set;
        int[] vals = values;
        int capacity = keys.length;
        int index;
        K cur;
        if ((cur = keys[index = 0]) == null) {
            // key is present
            return Float.intBitsToFloat(vals[index]);
        } else {
            keyAbsent:
            if (cur != FREE) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == null) {
                        // key is present
                        return Float.intBitsToFloat(vals[bIndex]);
                    } else if (cur == FREE) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == null) {
                        // key is present
                        return Float.intBitsToFloat(vals[fIndex]);
                    } else if (cur == FREE) {
                        index = fIndex;
                        break keyAbsent;
                    }
                    step += 2;
                }
            }
            // key is absent
            float value = mappingFunction.applyAsFloat(null);
            incrementModCount();
            keys[index] = null;
            vals[index] = Float.floatToIntBits(value);
            postInsertHook();
            return value;
        }
    }

    @Override
    public Float computeIfPresent(K key,
            BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(key);
        if (index >= 0) {
            // key is present
            int[] vals = values;
            Float newValue = remappingFunction.apply(key, Float.intBitsToFloat(vals[index]));
            if (newValue != null) {
                vals[index] = Float.floatToIntBits(newValue);
                return newValue;
            } else {
                throw new java.lang.UnsupportedOperationException("ComputeIfPresent operation of updatable map doesn't support removals");
            }
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public float computeIfPresent(K key, ObjFloatToFloatFunction<? super K> remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(key);
        if (index >= 0) {
            // key is present
            int[] vals = values;
            float newValue = remappingFunction.applyAsFloat(key, Float.intBitsToFloat(vals[index]));
            vals[index] = Float.floatToIntBits(newValue);
            return newValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Float merge(K key, Float value,
            BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        if (key != null) {
            if (value == null)
                throw new java.lang.NullPointerException();
            if (remappingFunction == null)
                throw new java.lang.NullPointerException();
            // noinspection unchecked
            K[] keys = (K[]) set;
            int[] vals = values;
            int capacity, index;
            K cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(key)) % (capacity = keys.length)]) != key) {
                keyAbsent:
                if (cur != FREE) {
                    if (keyEquals(key, cur)) {
                        break keyPresent;
                    } else {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == key) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
                                index = bIndex;
                                break keyAbsent;
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
                                break keyAbsent;
                            }
                            else if (keyEquals(key, cur)) {
                                index = fIndex;
                                break keyPresent;
                            }
                            step += 2;
                        }
                    }
                }
                // key is absent
                incrementModCount();
                keys[index] = key;
                vals[index] = Float.floatToIntBits(value);
                postInsertHook();
                return value;
            }
            // key is present
            Float newValue = remappingFunction.apply(Float.intBitsToFloat(vals[index]), value);
            if (newValue != null) {
                vals[index] = Float.floatToIntBits(newValue);
                return newValue;
            } else {
                throw new java.lang.UnsupportedOperationException("Merge operation of updatable map doesn't support removals");
            }
        } else {
            return mergeNullKey(value, remappingFunction);
        }
    }

    Float mergeNullKey(Float value,
            BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        if (value == null)
            throw new java.lang.NullPointerException();
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        // noinspection unchecked
        K[] keys = (K[]) set;
        int[] vals = values;
        int capacity = keys.length;
        int index;
        K cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            keyAbsent:
            if (cur != FREE) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == null) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == null) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        index = fIndex;
                        break keyAbsent;
                    }
                    step += 2;
                }
            }
            // key is absent
            incrementModCount();
            keys[index] = null;
            vals[index] = Float.floatToIntBits(value);
            postInsertHook();
            return value;
        }
        // key is present
        Float newValue = remappingFunction.apply(Float.intBitsToFloat(vals[index]), value);
        if (newValue != null) {
            vals[index] = Float.floatToIntBits(newValue);
            return newValue;
        } else {
            throw new java.lang.UnsupportedOperationException("Merge operation of updatable map doesn't support removals");
        }
    }

    @Override
    public float merge(K key, float value, FloatBinaryOperator remappingFunction) {
        if (key != null) {
            if (remappingFunction == null)
                throw new java.lang.NullPointerException();
            // noinspection unchecked
            K[] keys = (K[]) set;
            int[] vals = values;
            int capacity, index;
            K cur;
            keyPresent:
            if ((cur = keys[index = SeparateKVObjKeyMixing.mix(keyHashCode(key)) % (capacity = keys.length)]) != key) {
                keyAbsent:
                if (cur != FREE) {
                    if (keyEquals(key, cur)) {
                        break keyPresent;
                    } else {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = keys[bIndex]) == key) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == FREE) {
                                index = bIndex;
                                break keyAbsent;
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
                                break keyAbsent;
                            }
                            else if (keyEquals(key, cur)) {
                                index = fIndex;
                                break keyPresent;
                            }
                            step += 2;
                        }
                    }
                }
                // key is absent
                incrementModCount();
                keys[index] = key;
                vals[index] = Float.floatToIntBits(value);
                postInsertHook();
                return value;
            }
            // key is present
            float newValue = remappingFunction.applyAsFloat(Float.intBitsToFloat(vals[index]), value);
            vals[index] = Float.floatToIntBits(newValue);
            return newValue;
        } else {
            return mergeNullKey(value, remappingFunction);
        }
    }

    float mergeNullKey(float value, FloatBinaryOperator remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        // noinspection unchecked
        K[] keys = (K[]) set;
        int[] vals = values;
        int capacity = keys.length;
        int index;
        K cur;
        keyPresent:
        if ((cur = keys[index = 0]) != null) {
            keyAbsent:
            if (cur != FREE) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == null) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == null) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE) {
                        index = fIndex;
                        break keyAbsent;
                    }
                    step += 2;
                }
            }
            // key is absent
            incrementModCount();
            keys[index] = null;
            vals[index] = Float.floatToIntBits(value);
            postInsertHook();
            return value;
        }
        // key is present
        float newValue = remappingFunction.applyAsFloat(Float.intBitsToFloat(vals[index]), value);
        vals[index] = Float.floatToIntBits(newValue);
        return newValue;
    }

    @Override
    public float addValue(K key, float value) {
        int index = insert(key, Float.floatToIntBits(value));
        if (index < 0) {
            // key was absent
            return value;
        } else {
            // key is present
            int[] vals = values;
            float newValue = Float.intBitsToFloat(vals[index]) + value;
            vals[index] = Float.floatToIntBits(newValue);
            return newValue;
        }
    }

    @Override
    public float addValue(K key, float addition, float defaultValue) {
        float value = defaultValue + addition;
        int index = insert(key, Float.floatToIntBits(value));
        if (index < 0) {
            // key was absent
            return value;
        } else {
            // key is present
            int[] vals = values;
            float newValue = Float.intBitsToFloat(vals[index]) + addition;
            vals[index] = Float.floatToIntBits(newValue);
            return newValue;
        }
    }


    @Override
    public void putAll(@Nonnull Map<? extends K, ? extends Float> m) {
        CommonObjFloatMapOps.putAll(this, m);
    }


    @Override
    public Float replace(K key, Float value) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            int[] vals = values;
            float oldValue = Float.intBitsToFloat(vals[index]);
            vals[index] = Float.floatToIntBits(value);
            return oldValue;
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public float replace(K key, float value) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            int[] vals = values;
            float oldValue = Float.intBitsToFloat(vals[index]);
            vals[index] = Float.floatToIntBits(value);
            return oldValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public boolean replace(K key, Float oldValue, Float newValue) {
        return replace(key,
                oldValue.floatValue(),
                newValue.floatValue());
    }

    @Override
    public boolean replace(K key, float oldValue, float newValue) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            int[] vals = values;
            if (vals[index] == Float.floatToIntBits(oldValue)) {
                vals[index] = Float.floatToIntBits(newValue);
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
            BiFunction<? super K, ? super Float, ? extends Float> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        Object[] keys = set;
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                vals[i] = Float.floatToIntBits(function.apply(key, Float.intBitsToFloat(vals[i])));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void replaceAll(ObjFloatToFloatFunction<? super K> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        Object[] keys = set;
        int[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                vals[i] = Float.floatToIntBits(function.applyAsFloat(key, Float.intBitsToFloat(vals[i])));
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
    public Float remove(Object key) {
        throw new java.lang.UnsupportedOperationException();
    }

    Float removeNullKey() {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean justRemove(Object key) {
        throw new java.lang.UnsupportedOperationException();
    }

    boolean justRemoveNullKey() {
        throw new java.lang.UnsupportedOperationException();
    }


    

    @Override
    public float removeAsFloat(Object key) {
        throw new java.lang.UnsupportedOperationException();
    }

    float removeAsFloatNullKey() {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean remove(Object key, Object value) {
        return remove(key,
                ((Float) value).floatValue()
                );
    }

    @Override
    public boolean remove(Object key, float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    boolean removeEntryNullKey(float value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(ObjFloatPredicate<? super K> filter) {
        throw new java.lang.UnsupportedOperationException();
    }






    class EntryView extends AbstractSetView<Map.Entry<K, Float>>
            implements HashObjSet<Map.Entry<K, Float>>,
            InternalObjCollectionOps<Map.Entry<K, Float>> {

        @Nonnull
        @Override
        public Equivalence<Entry<K, Float>> equivalence() {
            return Equivalence.entryEquivalence(
                    keyEquivalence(),
                    Equivalence.<Float>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return UpdatableQHashSeparateKVObjFloatMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return UpdatableQHashSeparateKVObjFloatMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return UpdatableQHashSeparateKVObjFloatMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<K, Float> e = (Map.Entry<K, Float>) o;
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
            int[] vals = values;
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
            int[] vals = values;
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
        public final void forEach(@Nonnull Consumer<? super Map.Entry<K, Float>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            Object[] keys = set;
            int[] vals = values;
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
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<K, Float>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            Object[] keys = set;
            int[] vals = values;
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
        public ObjIterator<Map.Entry<K, Float>> iterator() {
            int mc = modCount();
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<K, Float>> cursor() {
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
            int[] vals = values;
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
            int[] vals = values;
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
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<K, Float>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            Object[] keys = set;
            int[] vals = values;
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
            return UpdatableQHashSeparateKVObjFloatMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    sb.append(' ');
                    sb.append(key != this ? key : "(this Collection)");
                    sb.append('=');
                    sb.append(Float.intBitsToFloat(vals[i]));
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
            return UpdatableQHashSeparateKVObjFloatMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<K, Float> e = (Map.Entry<K, Float>) o;
                K key = e.getKey();
                float value = e.getValue();
                return UpdatableQHashSeparateKVObjFloatMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<K, Float>> filter) {
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
            UpdatableQHashSeparateKVObjFloatMapGO.this.clear();
        }
    }


    abstract class ObjFloatEntry extends AbstractEntry<K, Float> {

        abstract K key();

        @Override
        public final K getKey() {
            return key();
        }

        abstract int value();

        @Override
        public final Float getValue() {
            return Float.intBitsToFloat(value());
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object o) {
            Map.Entry e2;
            K k2;
            int v2;
            try {
                e2 = (Map.Entry) o;
                k2 = (K) e2.getKey();
                v2 = Float.floatToIntBits((Float) e2.getValue());
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


    class MutableEntry extends ObjFloatEntry {
        final int modCount;
        private final int index;
        final K key;
        private int value;

        MutableEntry(int modCount, int index, K key, int value) {
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
        public int value() {
            return value;
        }

        @Override
        public Float setValue(Float newValue) {
            if (modCount != modCount())
                throw new IllegalStateException();
            float oldValue = Float.intBitsToFloat(value);
            int unwrappedNewValue = Float.floatToIntBits(newValue);
            value = unwrappedNewValue;
            updateValueInTable(unwrappedNewValue);
            return oldValue;
        }

        void updateValueInTable(int newValue) {
            values[index] = newValue;
        }
    }



    class ReusableEntry extends ObjFloatEntry {
        private K key;
        private int value;

        ReusableEntry with(K key, int value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public K key() {
            return key;
        }

        @Override
        public int value() {
            return value;
        }
    }


    class ValueView extends AbstractFloatValueView {


        @Override
        public int size() {
            return UpdatableQHashSeparateKVObjFloatMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return UpdatableQHashSeparateKVObjFloatMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return UpdatableQHashSeparateKVObjFloatMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(float v) {
            return UpdatableQHashSeparateKVObjFloatMapGO.this.containsValue(v);
        }

        @Override
        public boolean contains(int bits) {
            return UpdatableQHashSeparateKVObjFloatMapGO.this.containsValue(bits);
        }


        @Override
        public void forEach(Consumer<? super Float> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    action.accept(Float.intBitsToFloat(vals[i]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public void forEach(FloatConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    action.accept(Float.intBitsToFloat(vals[i]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(FloatPredicate predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    if (!predicate.test(Float.intBitsToFloat(vals[i]))) {
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
        public boolean allContainingIn(FloatCollection c) {
            if (c instanceof InternalFloatCollectionOps)
                return allContainingIn((InternalFloatCollectionOps) c);
            if (isEmpty())
                return true;
            boolean containsAll = true;
            int mc = modCount();
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    if (!c.contains(Float.intBitsToFloat(vals[i]))) {
                        containsAll = false;
                        break;
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return containsAll;
        }

        private boolean allContainingIn(InternalFloatCollectionOps c) {
            if (isEmpty())
                return true;
            boolean containsAll = true;
            int mc = modCount();
            Object[] keys = set;
            int[] vals = values;
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
        public boolean reverseAddAllTo(FloatCollection c) {
            if (c instanceof InternalFloatCollectionOps)
                return reverseAddAllTo((InternalFloatCollectionOps) c);
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    changed |= c.add(Float.intBitsToFloat(vals[i]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        private boolean reverseAddAllTo(InternalFloatCollectionOps c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            Object[] keys = set;
            int[] vals = values;
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
        public boolean reverseRemoveAllFrom(FloatSet s) {
            if (s instanceof InternalFloatCollectionOps)
                return reverseRemoveAllFrom((InternalFloatCollectionOps) s);
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    changed |= s.removeFloat(Float.intBitsToFloat(vals[i]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        private boolean reverseRemoveAllFrom(InternalFloatCollectionOps s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    changed |= s.removeFloat(vals[i]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        @Override
        @Nonnull
        public FloatIterator iterator() {
            int mc = modCount();
            return new NoRemovedValueIterator(mc);
        }

        @Nonnull
        @Override
        public FloatCursor cursor() {
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
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    result[resultIndex++] = Float.intBitsToFloat(vals[i]);
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
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    a[resultIndex++] = (T) Float.valueOf(Float.intBitsToFloat(vals[i]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }

        @Override
        public float[] toFloatArray() {
            int size = size();
            float[] result = new float[size];
            if (size == 0)
                return result;
            int resultIndex = 0;
            int mc = modCount();
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    result[resultIndex++] = Float.intBitsToFloat(vals[i]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return result;
        }

        @Override
        public float[] toArray(float[] a) {
            int size = size();
            if (a.length < size)
                a = new float[size];
            if (size == 0) {
                if (a.length > 0)
                    a[0] = 0.0f;
                return a;
            }
            int resultIndex = 0;
            int mc = modCount();
            Object[] keys = set;
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    a[resultIndex++] = Float.intBitsToFloat(vals[i]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            if (a.length > resultIndex)
                a[resultIndex] = 0.0f;
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
            int[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    sb.append(' ').append(Float.intBitsToFloat(vals[i])).append(',');
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
            return removeFloat(( Float ) o);
        }

        @Override
        public boolean removeFloat(float v) {
            return removeValue(v);
        }

        @Override
        public boolean removeFloat(int bits) {
            return removeValue(bits);
        }


        @Override
        public void clear() {
            UpdatableQHashSeparateKVObjFloatMapGO.this.clear();
        }

        
        public boolean removeIf(Predicate<? super Float> filter) {
            throw new java.lang.UnsupportedOperationException();
        }

        @Override
        public boolean removeIf(FloatPredicate filter) {
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<K, Float>> {
        final K[] keys;
        final int[] vals;
        int expectedModCount;
        int nextIndex;
        MutableEntry next;

        NoRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            // noinspection unchecked
            K[] keys = this.keys = (K[]) set;
            int[] vals = this.vals = values;
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
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<K, Float>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            int[] vals = this.vals;
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
            nextIndex = -1;
        }

        @Override
        public boolean hasNext() {
            return nextIndex >= 0;
        }

        @Override
        public Map.Entry<K, Float> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
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
            throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<K, Float>> {
        final K[] keys;
        final int[] vals;
        int expectedModCount;
        int index;
        Object curKey;
        int curValue;

        NoRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            // noinspection unchecked
            this.keys = (K[]) set;
            index = keys.length;
            vals = values;
            curKey = FREE;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<K, Float>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            int[] vals = this.vals;
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
        public Map.Entry<K, Float> elem() {
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
            throw new java.lang.UnsupportedOperationException();
        }
    }




    class NoRemovedValueIterator implements FloatIterator {
        final K[] keys;
        final int[] vals;
        int expectedModCount;
        int nextIndex;
        float next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            // noinspection unchecked
            K[] keys = this.keys = (K[]) set;
            int[] vals = this.vals = values;
            int nextI = keys.length;
            while (--nextI >= 0) {
                if (keys[nextI] != FREE) {
                    // noinspection unchecked
                    next = Float.intBitsToFloat(vals[nextI]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public float nextFloat() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                if (expectedModCount == modCount()) {
                    K[] keys = this.keys;
                    float prev = next;
                    while (--nextI >= 0) {
                        if (keys[nextI] != FREE) {
                            // noinspection unchecked
                            next = Float.intBitsToFloat(vals[nextI]);
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
        public void forEachRemaining(Consumer<? super Float> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            int[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if (keys[i] != FREE) {
                    // noinspection unchecked
                    action.accept(Float.intBitsToFloat(vals[i]));
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            nextIndex = -1;
        }

        @Override
        public void forEachRemaining(FloatConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            int[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if (keys[i] != FREE) {
                    // noinspection unchecked
                    action.accept(Float.intBitsToFloat(vals[i]));
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
        public Float next() {
            return nextFloat();
        }

        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedValueCursor implements FloatCursor {
        final K[] keys;
        final int[] vals;
        int expectedModCount;
        int index;
        Object curKey;
        int curValue;

        NoRemovedValueCursor(int mc) {
            expectedModCount = mc;
            // noinspection unchecked
            this.keys = (K[]) set;
            index = keys.length;
            vals = values;
            curKey = FREE;
        }

        @Override
        public void forEachForward(FloatConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            int[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    // noinspection unchecked
                    action.accept(Float.intBitsToFloat(vals[i]));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE;
        }

        @Override
        public float elem() {
            if (curKey != FREE) {
                return Float.intBitsToFloat(curValue);
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
            throw new java.lang.UnsupportedOperationException();
        }
    }



    class NoRemovedMapCursor implements ObjFloatCursor<K> {
        final K[] keys;
        final int[] vals;
        int expectedModCount;
        int index;
        Object curKey;
        int curValue;

        NoRemovedMapCursor(int mc) {
            expectedModCount = mc;
            // noinspection unchecked
            this.keys = (K[]) set;
            index = keys.length;
            vals = values;
            curKey = FREE;
        }

        @Override
        public void forEachForward(ObjFloatConsumer<? super K> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            K[] keys = this.keys;
            int[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE) {
                    // noinspection unchecked
                    action.accept((K) key, Float.intBitsToFloat(vals[i]));
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
        public float value() {
            if (curKey != FREE) {
                return Float.intBitsToFloat(curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(float value) {
            if (curKey != FREE) {
                if (expectedModCount == modCount()) {
                    vals[index] = Float.floatToIntBits(value);
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
            throw new java.lang.UnsupportedOperationException();
        }
    }
}

