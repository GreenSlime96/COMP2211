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
import net.openhft.koloboke.function.FloatPredicate;
import net.openhft.koloboke.function.FloatByteConsumer;
import net.openhft.koloboke.function.FloatBytePredicate;
import net.openhft.koloboke.function.FloatByteToByteFunction;
import net.openhft.koloboke.function.FloatToByteFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.openhft.koloboke.function.ByteBinaryOperator;
import net.openhft.koloboke.function.ByteConsumer;
import net.openhft.koloboke.function.BytePredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class UpdatableQHashSeparateKVFloatByteMapGO
        extends UpdatableQHashSeparateKVFloatByteMapSO {

    @Override
    final void copy(SeparateKVFloatByteQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVFloatByteQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.move(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }


    @Override
    public byte defaultValue() {
        return (byte) 0;
    }

    @Override
    public boolean containsEntry(float key, byte value) {
        int k = Float.floatToIntBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            return values[index] == value;
        } else {
            // key is absent
            return false;
        }
    }

    @Override
    public boolean containsEntry(int key, byte value) {
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
    public Byte get(Object key) {
        int k = Float.floatToIntBits((Float) key);
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
    public byte get(float key) {
        int k = Float.floatToIntBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Byte getOrDefault(Object key, Byte defaultValue) {
        int k = Float.floatToIntBits((Float) key);
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
    public byte getOrDefault(float key, byte defaultValue) {
        int k = Float.floatToIntBits(key);
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
    public void forEach(BiConsumer<? super Float, ? super Byte> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        int[] keys = set;
        byte[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                action.accept(Float.intBitsToFloat(key), vals[i]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void forEach(FloatByteConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        int[] keys = set;
        byte[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                action.accept(Float.intBitsToFloat(key), vals[i]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public boolean forEachWhile(FloatBytePredicate predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        int[] keys = set;
        byte[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                if (!predicate.test(Float.intBitsToFloat(key), vals[i])) {
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
    public FloatByteCursor cursor() {
        int mc = modCount();
        return new NoRemovedMapCursor(mc);
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonFloatByteMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalFloatByteMapOps m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        int[] keys = set;
        byte[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
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
    public void reversePutAllTo(InternalFloatByteMapOps m) {
        if (isEmpty())
            return;
        int mc = modCount();
        int[] keys = set;
        byte[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                m.justPut(key, vals[i]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<Float, Byte>> entrySet() {
        return new EntryView();
    }

    @Override
    @Nonnull
    public ByteCollection values() {
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
        int[] keys = set;
        byte[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                hashCode += key ^ vals[i];
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
        int[] keys = set;
        byte[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                sb.append(' ');
                sb.append(Float.intBitsToFloat(key));
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
        int[] keys = set;
        byte[] vals = values;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        int[] newKeys = set;
        int capacity = newKeys.length;
        byte[] newVals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                int index;
                if (newKeys[index = SeparateKVFloatKeyMixing.mix(key) % capacity] != FREE_BITS) {
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
    public Byte put(Float key, Byte value) {
        int k = Float.floatToIntBits(key);
        int index = insert(k, value);
        if (index < 0) {
            // key was absent
            return null;
        } else {
            // key is present
            byte[] vals = values;
            byte prevValue = vals[index];
            vals[index] = value;
            return prevValue;
        }
    }

    @Override
    public byte put(float key, byte value) {
        int k = Float.floatToIntBits(key);
        int index = insert(k, value);
        if (index < 0) {
            // key was absent
            return defaultValue();
        } else {
            // key is present
            byte[] vals = values;
            byte prevValue = vals[index];
            vals[index] = value;
            return prevValue;
        }
    }

    @Override
    public Byte putIfAbsent(Float key, Byte value) {
        int k = Float.floatToIntBits(key);
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
    public byte putIfAbsent(float key, byte value) {
        int k = Float.floatToIntBits(key);
        int index = insert(k, value);
        if (index < 0) {
            // key was absent
            return defaultValue();
        } else {
            // key is present
            return values[index];
        }
    }

    @Override
    public void justPut(float key, byte value) {
        int k = Float.floatToIntBits(key);
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
    public void justPut(int key, byte value) {
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
    public Byte compute(Float key,
            BiFunction<? super Float, ? super Byte, ? extends Byte> remappingFunction) {
        int k = Float.floatToIntBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int[] keys = set;
        byte[] vals = values;
        int capacity, index;
        int cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVFloatKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
            keyAbsent:
            if (cur != FREE_BITS) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == k) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        index = fIndex;
                        break keyAbsent;
                    }
                    step += 2;
                }
            }
            // key is absent
            Byte newValue = remappingFunction.apply(Float.intBitsToFloat(k), null);
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
        Byte newValue = remappingFunction.apply(Float.intBitsToFloat(k), vals[index]);
        if (newValue != null) {
            vals[index] = newValue;
            return newValue;
        } else {
            throw new java.lang.UnsupportedOperationException("Compute operation of updatable map doesn't support removals");
        }
    }


    @Override
    public byte compute(float key, FloatByteToByteFunction remappingFunction) {
        int k = Float.floatToIntBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int[] keys = set;
        byte[] vals = values;
        int capacity, index;
        int cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVFloatKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
            keyAbsent:
            if (cur != FREE_BITS) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == k) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        index = fIndex;
                        break keyAbsent;
                    }
                    step += 2;
                }
            }
            // key is absent
            byte newValue = remappingFunction.applyAsByte(Float.intBitsToFloat(k), defaultValue());
            incrementModCount();
            keys[index] = k;
            vals[index] = newValue;
            postInsertHook();
            return newValue;
        }
        // key is present
        byte newValue = remappingFunction.applyAsByte(Float.intBitsToFloat(k), vals[index]);
        vals[index] = newValue;
        return newValue;
    }


    @Override
    public Byte computeIfAbsent(Float key,
            Function<? super Float, ? extends Byte> mappingFunction) {
        int k = Float.floatToIntBits(key);
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        int[] keys = set;
        byte[] vals = values;
        int capacity, index;
        int cur;
        if ((cur = keys[index = SeparateKVFloatKeyMixing.mix(k) % (capacity = keys.length)]) == k) {
            // key is present
            return vals[index];
        } else {
            keyAbsent:
            if (cur != FREE_BITS) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == k) {
                        // key is present
                        return vals[bIndex];
                    } else if (cur == FREE_BITS) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == k) {
                        // key is present
                        return vals[fIndex];
                    } else if (cur == FREE_BITS) {
                        index = fIndex;
                        break keyAbsent;
                    }
                    step += 2;
                }
            }
            // key is absent
            Byte value = mappingFunction.apply(Float.intBitsToFloat(k));
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
    }


    @Override
    public byte computeIfAbsent(float key, FloatToByteFunction mappingFunction) {
        int k = Float.floatToIntBits(key);
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        int[] keys = set;
        byte[] vals = values;
        int capacity, index;
        int cur;
        if ((cur = keys[index = SeparateKVFloatKeyMixing.mix(k) % (capacity = keys.length)]) == k) {
            // key is present
            return vals[index];
        } else {
            keyAbsent:
            if (cur != FREE_BITS) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == k) {
                        // key is present
                        return vals[bIndex];
                    } else if (cur == FREE_BITS) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == k) {
                        // key is present
                        return vals[fIndex];
                    } else if (cur == FREE_BITS) {
                        index = fIndex;
                        break keyAbsent;
                    }
                    step += 2;
                }
            }
            // key is absent
            byte value = mappingFunction.applyAsByte(Float.intBitsToFloat(k));
            incrementModCount();
            keys[index] = k;
            vals[index] = value;
            postInsertHook();
            return value;
        }
    }


    @Override
    public Byte computeIfPresent(Float key,
            BiFunction<? super Float, ? super Byte, ? extends Byte> remappingFunction) {
        int k = Float.floatToIntBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(k);
        if (index >= 0) {
            // key is present
            byte[] vals = values;
            Byte newValue = remappingFunction.apply(Float.intBitsToFloat(k), vals[index]);
            if (newValue != null) {
                vals[index] = newValue;
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
    public byte computeIfPresent(float key, FloatByteToByteFunction remappingFunction) {
        int k = Float.floatToIntBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int index = index(k);
        if (index >= 0) {
            // key is present
            byte[] vals = values;
            byte newValue = remappingFunction.applyAsByte(Float.intBitsToFloat(k), vals[index]);
            vals[index] = newValue;
            return newValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Byte merge(Float key, Byte value,
            BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        int k = Float.floatToIntBits(key);
        if (value == null)
            throw new java.lang.NullPointerException();
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int[] keys = set;
        byte[] vals = values;
        int capacity, index;
        int cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVFloatKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
            keyAbsent:
            if (cur != FREE_BITS) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == k) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
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
        Byte newValue = remappingFunction.apply(vals[index], value);
        if (newValue != null) {
            vals[index] = newValue;
            return newValue;
        } else {
            throw new java.lang.UnsupportedOperationException("Merge operation of updatable map doesn't support removals");
        }
    }


    @Override
    public byte merge(float key, byte value, ByteBinaryOperator remappingFunction) {
        int k = Float.floatToIntBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int[] keys = set;
        byte[] vals = values;
        int capacity, index;
        int cur;
        keyPresent:
        if ((cur = keys[index = SeparateKVFloatKeyMixing.mix(k) % (capacity = keys.length)]) != k) {
            keyAbsent:
            if (cur != FREE_BITS) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = keys[bIndex]) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = keys[fIndex]) == k) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
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
        byte newValue = remappingFunction.applyAsByte(vals[index], value);
        vals[index] = newValue;
        return newValue;
    }


    @Override
    public byte addValue(float key, byte value) {
        int k = Float.floatToIntBits(key);
        int index = insert(k, value);
        if (index < 0) {
            // key was absent
            return value;
        } else {
            // key is present
            byte[] vals = values;
            byte newValue = (byte) (vals[index] + value);
            vals[index] = newValue;
            return newValue;
        }
    }

    @Override
    public byte addValue(float key, byte addition, byte defaultValue) {
        int k = Float.floatToIntBits(key);
        byte value = (byte) (defaultValue + addition);
        int index = insert(k, value);
        if (index < 0) {
            // key was absent
            return value;
        } else {
            // key is present
            byte[] vals = values;
            byte newValue = (byte) (vals[index] + addition);
            vals[index] = newValue;
            return newValue;
        }
    }


    @Override
    public void putAll(@Nonnull Map<? extends Float, ? extends Byte> m) {
        CommonFloatByteMapOps.putAll(this, m);
    }


    @Override
    public Byte replace(Float key, Byte value) {
        int k = Float.floatToIntBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            byte[] vals = values;
            byte oldValue = vals[index];
            vals[index] = value;
            return oldValue;
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public byte replace(float key, byte value) {
        int k = Float.floatToIntBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            byte[] vals = values;
            byte oldValue = vals[index];
            vals[index] = value;
            return oldValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public boolean replace(Float key, Byte oldValue, Byte newValue) {
        return replace(key.floatValue(),
                oldValue.byteValue(),
                newValue.byteValue());
    }

    @Override
    public boolean replace(float key, byte oldValue, byte newValue) {
        int k = Float.floatToIntBits(key);
        int index = index(k);
        if (index >= 0) {
            // key is present
            byte[] vals = values;
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
            BiFunction<? super Float, ? super Byte, ? extends Byte> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        int[] keys = set;
        byte[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                vals[i] = function.apply(Float.intBitsToFloat(key), vals[i]);
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void replaceAll(FloatByteToByteFunction function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        int[] keys = set;
        byte[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            int key;
            if ((key = keys[i]) < FREE_BITS) {
                vals[i] = function.applyAsByte(Float.intBitsToFloat(key), vals[i]);
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
    public Byte remove(Object key) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean justRemove(float key) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean justRemove(int key) {
        throw new java.lang.UnsupportedOperationException();
    }


    

    @Override
    public byte remove(float key) {
        throw new java.lang.UnsupportedOperationException();
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Float) key).floatValue(),
                ((Byte) value).byteValue()
                );
    }

    @Override
    public boolean remove(float key, byte value) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean removeIf(FloatBytePredicate filter) {
        throw new java.lang.UnsupportedOperationException();
    }






    class EntryView extends AbstractSetView<Map.Entry<Float, Byte>>
            implements HashObjSet<Map.Entry<Float, Byte>>,
            InternalObjCollectionOps<Map.Entry<Float, Byte>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Float, Byte>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Float>defaultEquality()
                    ,
                    Equivalence.<Byte>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return UpdatableQHashSeparateKVFloatByteMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return UpdatableQHashSeparateKVFloatByteMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return UpdatableQHashSeparateKVFloatByteMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Float, Byte> e = (Map.Entry<Float, Byte>) o;
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
            int[] keys = set;
            byte[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                int key;
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
            int[] keys = set;
            byte[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                int key;
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
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Float, Byte>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            int[] keys = set;
            byte[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                int key;
                if ((key = keys[i]) < FREE_BITS) {
                    action.accept(new MutableEntry(mc, i, key, vals[i]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Float, Byte>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            int[] keys = set;
            byte[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                int key;
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
        public ObjIterator<Map.Entry<Float, Byte>> iterator() {
            int mc = modCount();
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Float, Byte>> cursor() {
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
            int[] keys = set;
            byte[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                int key;
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
            int[] keys = set;
            byte[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                int key;
                if ((key = keys[i]) < FREE_BITS) {
                    changed |= s.remove(e.with(key, vals[i]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Float, Byte>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            int[] keys = set;
            byte[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                int key;
                if ((key = keys[i]) < FREE_BITS) {
                    changed |= c.add(new MutableEntry(mc, i, key, vals[i]));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        public int hashCode() {
            return UpdatableQHashSeparateKVFloatByteMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            int[] keys = set;
            byte[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                int key;
                if ((key = keys[i]) < FREE_BITS) {
                    sb.append(' ');
                    sb.append(Float.intBitsToFloat(key));
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
            return UpdatableQHashSeparateKVFloatByteMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Float, Byte> e = (Map.Entry<Float, Byte>) o;
                float key = e.getKey();
                byte value = e.getValue();
                return UpdatableQHashSeparateKVFloatByteMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Float, Byte>> filter) {
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
            UpdatableQHashSeparateKVFloatByteMapGO.this.clear();
        }
    }


    abstract class FloatByteEntry extends AbstractEntry<Float, Byte> {

        abstract int key();

        @Override
        public final Float getKey() {
            return Float.intBitsToFloat(key());
        }

        abstract byte value();

        @Override
        public final Byte getValue() {
            return value();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object o) {
            Map.Entry e2;
            int k2;
            byte v2;
            try {
                e2 = (Map.Entry) o;
                k2 = Float.floatToIntBits((Float) e2.getKey());
                v2 = (Byte) e2.getValue();
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


    class MutableEntry extends FloatByteEntry {
        final int modCount;
        private final int index;
        final int key;
        private byte value;

        MutableEntry(int modCount, int index, int key, byte value) {
            this.modCount = modCount;
            this.index = index;
            this.key = key;
            this.value = value;
        }

        @Override
        public int key() {
            return key;
        }

        @Override
        public byte value() {
            return value;
        }

        @Override
        public Byte setValue(Byte newValue) {
            if (modCount != modCount())
                throw new IllegalStateException();
            byte oldValue = value;
            byte unwrappedNewValue = newValue;
            value = unwrappedNewValue;
            updateValueInTable(unwrappedNewValue);
            return oldValue;
        }

        void updateValueInTable(byte newValue) {
            values[index] = newValue;
        }
    }



    class ReusableEntry extends FloatByteEntry {
        private int key;
        private byte value;

        ReusableEntry with(int key, byte value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public int key() {
            return key;
        }

        @Override
        public byte value() {
            return value;
        }
    }


    class ValueView extends AbstractByteValueView {


        @Override
        public int size() {
            return UpdatableQHashSeparateKVFloatByteMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return UpdatableQHashSeparateKVFloatByteMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return UpdatableQHashSeparateKVFloatByteMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(byte v) {
            return UpdatableQHashSeparateKVFloatByteMapGO.this.containsValue(v);
        }



        @Override
        public void forEach(Consumer<? super Byte> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            int[] keys = set;
            byte[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    action.accept(vals[i]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public void forEach(ByteConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            int[] keys = set;
            byte[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    action.accept(vals[i]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(BytePredicate predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            int[] keys = set;
            byte[] vals = values;
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
        public boolean allContainingIn(ByteCollection c) {
            if (isEmpty())
                return true;
            boolean containsAll = true;
            int mc = modCount();
            int[] keys = set;
            byte[] vals = values;
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
        public boolean reverseAddAllTo(ByteCollection c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            int[] keys = set;
            byte[] vals = values;
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
        public boolean reverseRemoveAllFrom(ByteSet s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            int[] keys = set;
            byte[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    changed |= s.removeByte(vals[i]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }



        @Override
        @Nonnull
        public ByteIterator iterator() {
            int mc = modCount();
            return new NoRemovedValueIterator(mc);
        }

        @Nonnull
        @Override
        public ByteCursor cursor() {
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
            int[] keys = set;
            byte[] vals = values;
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
            int[] keys = set;
            byte[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    a[resultIndex++] = (T) Byte.valueOf(vals[i]);
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }

        @Override
        public byte[] toByteArray() {
            int size = size();
            byte[] result = new byte[size];
            if (size == 0)
                return result;
            int resultIndex = 0;
            int mc = modCount();
            int[] keys = set;
            byte[] vals = values;
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
        public byte[] toArray(byte[] a) {
            int size = size();
            if (a.length < size)
                a = new byte[size];
            if (size == 0) {
                if (a.length > 0)
                    a[0] = (byte) 0;
                return a;
            }
            int resultIndex = 0;
            int mc = modCount();
            int[] keys = set;
            byte[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    a[resultIndex++] = vals[i];
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            if (a.length > resultIndex)
                a[resultIndex] = (byte) 0;
            return a;
        }


        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            int[] keys = set;
            byte[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
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
            return removeByte(( Byte ) o);
        }

        @Override
        public boolean removeByte(byte v) {
            return removeValue(v);
        }



        @Override
        public void clear() {
            UpdatableQHashSeparateKVFloatByteMapGO.this.clear();
        }

        
        public boolean removeIf(Predicate<? super Byte> filter) {
            throw new java.lang.UnsupportedOperationException();
        }

        @Override
        public boolean removeIf(BytePredicate filter) {
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Float, Byte>> {
        final int[] keys;
        final byte[] vals;
        int expectedModCount;
        int nextIndex;
        MutableEntry next;

        NoRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            int[] keys = this.keys = set;
            byte[] vals = this.vals = values;
            int nextI = keys.length;
            while (--nextI >= 0) {
                int key;
                if ((key = keys[nextI]) < FREE_BITS) {
                    next = new MutableEntry(mc, nextI, key, vals[nextI]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Float, Byte>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            int[] keys = this.keys;
            byte[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                int key;
                if ((key = keys[i]) < FREE_BITS) {
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
        public Map.Entry<Float, Byte> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
                    int[] keys = this.keys;
                    MutableEntry prev = next;
                    while (--nextI >= 0) {
                        int key;
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
            throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Float, Byte>> {
        final int[] keys;
        final byte[] vals;
        int expectedModCount;
        int index;
        int curKey;
        byte curValue;

        NoRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Float, Byte>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            int[] keys = this.keys;
            byte[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                int key;
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
        public Map.Entry<Float, Byte> elem() {
            int curKey;
            if ((curKey = this.curKey) != FREE_BITS) {
                return new MutableEntry(expectedModCount, index, curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                int[] keys = this.keys;
                for (int i = index - 1; i >= 0; i--) {
                    int key;
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
            throw new java.lang.UnsupportedOperationException();
        }
    }




    class NoRemovedValueIterator implements ByteIterator {
        final int[] keys;
        final byte[] vals;
        int expectedModCount;
        int nextIndex;
        byte next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            int[] keys = this.keys = set;
            byte[] vals = this.vals = values;
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
        public byte nextByte() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                if (expectedModCount == modCount()) {
                    int[] keys = this.keys;
                    byte prev = next;
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
        public void forEachRemaining(Consumer<? super Byte> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            int[] keys = this.keys;
            byte[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
                    action.accept(vals[i]);
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            nextIndex = -1;
        }

        @Override
        public void forEachRemaining(ByteConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            int[] keys = this.keys;
            byte[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if (keys[i] < FREE_BITS) {
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
        public Byte next() {
            return nextByte();
        }

        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedValueCursor implements ByteCursor {
        final int[] keys;
        final byte[] vals;
        int expectedModCount;
        int index;
        int curKey;
        byte curValue;

        NoRemovedValueCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(ByteConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            int[] keys = this.keys;
            byte[] vals = this.vals;
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
        public byte elem() {
            if (curKey != FREE_BITS) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                int[] keys = this.keys;
                for (int i = index - 1; i >= 0; i--) {
                    int key;
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
            throw new java.lang.UnsupportedOperationException();
        }
    }



    class NoRemovedMapCursor implements FloatByteCursor {
        final int[] keys;
        final byte[] vals;
        int expectedModCount;
        int index;
        int curKey;
        byte curValue;

        NoRemovedMapCursor(int mc) {
            expectedModCount = mc;
            this.keys = set;
            index = keys.length;
            vals = values;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(FloatByteConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            int[] keys = this.keys;
            byte[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = keys[i]) < FREE_BITS) {
                    action.accept(Float.intBitsToFloat(key), vals[i]);
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE_BITS;
        }

        @Override
        public float key() {
            int curKey;
            if ((curKey = this.curKey) != FREE_BITS) {
                return Float.intBitsToFloat(curKey);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public byte value() {
            if (curKey != FREE_BITS) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(byte value) {
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
                int[] keys = this.keys;
                for (int i = index - 1; i >= 0; i--) {
                    int key;
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
            throw new java.lang.UnsupportedOperationException();
        }
    }
}

