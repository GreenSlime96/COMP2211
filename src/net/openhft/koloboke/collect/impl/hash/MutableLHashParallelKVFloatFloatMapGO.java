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
import net.openhft.koloboke.function.FloatFloatConsumer;
import net.openhft.koloboke.function.FloatFloatPredicate;
import net.openhft.koloboke.function.FloatFloatToFloatFunction;
import net.openhft.koloboke.function.FloatUnaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.openhft.koloboke.function.FloatBinaryOperator;
import net.openhft.koloboke.function.FloatConsumer;
import net.openhft.koloboke.function.FloatPredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class MutableLHashParallelKVFloatFloatMapGO
        extends MutableLHashParallelKVFloatFloatMapSO {

    
    final void copy(ParallelKVFloatFloatLHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    
    final void move(ParallelKVFloatFloatLHash hash) {
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
    public boolean containsEntry(float key, float value) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
            // key is present
            return (int) (entry >>> 32) == Float.floatToIntBits(value);
        } else {
            if (cur == FREE_BITS) {
                // key is absent
                return false;
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        // key is present
                        return (int) (entry >>> 32) == Float.floatToIntBits(value);
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return false;
                    }
                }
            }
        }
    }

    @Override
    public boolean containsEntry(int key, int value) {
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
            // key is present
            return (int) (entry >>> 32) == value;
        } else {
            if (cur == FREE_BITS) {
                // key is absent
                return false;
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                        // key is present
                        return (int) (entry >>> 32) == value;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return false;
                    }
                }
            }
        }
    }

    @Override
    public Float get(Object key) {
        int k = Float.floatToIntBits((Float) key);
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
            // key is present
            return Float.intBitsToFloat((int) (entry >>> 32));
        } else {
            if (cur == FREE_BITS) {
                // key is absent
                return null;
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        // key is present
                        return Float.intBitsToFloat((int) (entry >>> 32));
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return null;
                    }
                }
            }
        }
    }

    

    @Override
    public float get(float key) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
            // key is present
            return Float.intBitsToFloat((int) (entry >>> 32));
        } else {
            if (cur == FREE_BITS) {
                // key is absent
                return defaultValue();
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        // key is present
                        return Float.intBitsToFloat((int) (entry >>> 32));
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return defaultValue();
                    }
                }
            }
        }
    }

    @Override
    public Float getOrDefault(Object key, Float defaultValue) {
        int k = Float.floatToIntBits((Float) key);
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
            // key is present
            return Float.intBitsToFloat((int) (entry >>> 32));
        } else {
            if (cur == FREE_BITS) {
                // key is absent
                return defaultValue;
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        // key is present
                        return Float.intBitsToFloat((int) (entry >>> 32));
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return defaultValue;
                    }
                }
            }
        }
    }

    @Override
    public float getOrDefault(float key, float defaultValue) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
            // key is present
            return Float.intBitsToFloat((int) (entry >>> 32));
        } else {
            if (cur == FREE_BITS) {
                // key is absent
                return defaultValue;
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        // key is present
                        return Float.intBitsToFloat((int) (entry >>> 32));
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return defaultValue;
                    }
                }
            }
        }
    }

    @Override
    public void forEach(BiConsumer<? super Float, ? super Float> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                action.accept(Float.intBitsToFloat(key), Float.intBitsToFloat((int) (entry >>> 32)));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void forEach(FloatFloatConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                action.accept(Float.intBitsToFloat(key), Float.intBitsToFloat((int) (entry >>> 32)));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public boolean forEachWhile(FloatFloatPredicate predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                if (!predicate.test(Float.intBitsToFloat(key), Float.intBitsToFloat((int) (entry >>> 32)))) {
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
    public FloatFloatCursor cursor() {
        int mc = modCount();
        return new NoRemovedMapCursor(mc);
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonFloatFloatMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalFloatFloatMapOps m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                if (!m.containsEntry(key, (int) (entry >>> 32))) {
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
    public void reversePutAllTo(InternalFloatFloatMapOps m) {
        if (isEmpty())
            return;
        int mc = modCount();
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                m.justPut(key, (int) (entry >>> 32));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<Float, Float>> entrySet() {
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
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                hashCode += key ^ (int) (entry >>> 32);
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
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                sb.append(' ');
                sb.append(Float.intBitsToFloat(key));
                sb.append('=');
                sb.append(Float.intBitsToFloat((int) (entry >>> 32)));
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
        long[] tab = table;
        long entry;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        long[] newTab = table;
        int capacityMask = newTab.length - 1;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                int index;
                if (U.getInt(newTab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (index = ParallelKVFloatKeyMixing.mix(key) & capacityMask)) << LONG_SCALE_SHIFT)) != FREE_BITS) {
                    while (true) {
                        if (U.getInt(newTab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) ((index = (index - 1) & capacityMask))) << LONG_SCALE_SHIFT)) == FREE_BITS) {
                            break;
                        }
                    }
                }
                newTab[index] = entry;
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }


    @Override
    public Float put(Float key, Float value) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == FREE_BITS) {
            // key is absent
            incrementModCount();
            tab[index] = ((((long) k) & FLOAT_MASK) | (((long) Float.floatToIntBits(value)) << 32));
            postInsertHook();
            return null;
        } else {
            keyPresent:
            if (cur != k) {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == FREE_BITS) {
                        // key is absent
                        incrementModCount();
                        tab[index] = ((((long) k) & FLOAT_MASK) | (((long) Float.floatToIntBits(value)) << 32));
                        postInsertHook();
                        return null;
                    } else if (cur == k) {
                        break keyPresent;
                    }
                }
            }
            // key is present
            float prevValue = Float.intBitsToFloat((int) (entry >>> 32));
            U.putInt(tab, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), Float.floatToIntBits(value));
            return prevValue;
        }
    }

    @Override
    public float put(float key, float value) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == FREE_BITS) {
            // key is absent
            incrementModCount();
            tab[index] = ((((long) k) & FLOAT_MASK) | (((long) Float.floatToIntBits(value)) << 32));
            postInsertHook();
            return defaultValue();
        } else {
            keyPresent:
            if (cur != k) {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == FREE_BITS) {
                        // key is absent
                        incrementModCount();
                        tab[index] = ((((long) k) & FLOAT_MASK) | (((long) Float.floatToIntBits(value)) << 32));
                        postInsertHook();
                        return defaultValue();
                    } else if (cur == k) {
                        break keyPresent;
                    }
                }
            }
            // key is present
            float prevValue = Float.intBitsToFloat((int) (entry >>> 32));
            U.putInt(tab, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), Float.floatToIntBits(value));
            return prevValue;
        }
    }

    @Override
    public Float putIfAbsent(Float key, Float value) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == FREE_BITS) {
            // key is absent
            incrementModCount();
            tab[index] = ((((long) k) & FLOAT_MASK) | (((long) Float.floatToIntBits(value)) << 32));
            postInsertHook();
            return null;
        } else {
            if (cur == k) {
                // key is present
                return Float.intBitsToFloat((int) (entry >>> 32));
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == FREE_BITS) {
                        // key is absent
                        incrementModCount();
                        tab[index] = ((((long) k) & FLOAT_MASK) | (((long) Float.floatToIntBits(value)) << 32));
                        postInsertHook();
                        return null;
                    } else if (cur == k) {
                        // key is present
                        return Float.intBitsToFloat((int) (entry >>> 32));
                    }
                }
            }
        }
    }

    @Override
    public float putIfAbsent(float key, float value) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == FREE_BITS) {
            // key is absent
            incrementModCount();
            tab[index] = ((((long) k) & FLOAT_MASK) | (((long) Float.floatToIntBits(value)) << 32));
            postInsertHook();
            return defaultValue();
        } else {
            if (cur == k) {
                // key is present
                return Float.intBitsToFloat((int) (entry >>> 32));
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == FREE_BITS) {
                        // key is absent
                        incrementModCount();
                        tab[index] = ((((long) k) & FLOAT_MASK) | (((long) Float.floatToIntBits(value)) << 32));
                        postInsertHook();
                        return defaultValue();
                    } else if (cur == k) {
                        // key is present
                        return Float.intBitsToFloat((int) (entry >>> 32));
                    }
                }
            }
        }
    }

    @Override
    public void justPut(float key, float value) {
        int k = Float.floatToIntBits(key);
        int index = insert(k, Float.floatToIntBits(value));
        if (index < 0) {
            // key was absent
            return;
        } else {
            // key is present
            U.putInt(table, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), Float.floatToIntBits(value));
            return;
        }
    }

    @Override
    public void justPut(int key, int value) {
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return;
        } else {
            // key is present
            U.putInt(table, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), value);
            return;
        }
    }

    @Override
    public Float compute(Float key,
            BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        int k = Float.floatToIntBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) != k) {
            keyAbsent:
            if (cur != FREE_BITS) {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            Float newValue = remappingFunction.apply(Float.intBitsToFloat(k), null);
            if (newValue != null) {
                incrementModCount();
                tab[index] = ((((long) k) & FLOAT_MASK) | (((long) Float.floatToIntBits(newValue)) << 32));
                postInsertHook();
                return newValue;
            } else {
                return null;
            }
        }
        // key is present
        Float newValue = remappingFunction.apply(Float.intBitsToFloat(k), Float.intBitsToFloat((int) (entry >>> 32)));
        if (newValue != null) {
            U.putInt(tab, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), Float.floatToIntBits(newValue));
            return newValue;
        } else {
            removeAt(index);
            return null;
        }
    }


    @Override
    public float compute(float key, FloatFloatToFloatFunction remappingFunction) {
        int k = Float.floatToIntBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) != k) {
            keyAbsent:
            if (cur != FREE_BITS) {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            float newValue = remappingFunction.applyAsFloat(Float.intBitsToFloat(k), defaultValue());
            incrementModCount();
            tab[index] = ((((long) k) & FLOAT_MASK) | (((long) Float.floatToIntBits(newValue)) << 32));
            postInsertHook();
            return newValue;
        }
        // key is present
        float newValue = remappingFunction.applyAsFloat(Float.intBitsToFloat(k), Float.intBitsToFloat((int) (entry >>> 32)));
        U.putInt(tab, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), Float.floatToIntBits(newValue));
        return newValue;
    }


    @Override
    public Float computeIfAbsent(Float key,
            Function<? super Float, ? extends Float> mappingFunction) {
        int k = Float.floatToIntBits(key);
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
            // key is present
            return Float.intBitsToFloat((int) (entry >>> 32));
        } else {
            keyAbsent:
            if (cur != FREE_BITS) {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        // key is present
                        return Float.intBitsToFloat((int) (entry >>> 32));
                    } else if (cur == FREE_BITS) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            Float value = mappingFunction.apply(Float.intBitsToFloat(k));
            if (value != null) {
                incrementModCount();
                tab[index] = ((((long) k) & FLOAT_MASK) | (((long) Float.floatToIntBits(value)) << 32));
                postInsertHook();
                return value;
            } else {
                return null;
            }
        }
    }


    @Override
    public float computeIfAbsent(float key, FloatUnaryOperator mappingFunction) {
        int k = Float.floatToIntBits(key);
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
            // key is present
            return Float.intBitsToFloat((int) (entry >>> 32));
        } else {
            keyAbsent:
            if (cur != FREE_BITS) {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        // key is present
                        return Float.intBitsToFloat((int) (entry >>> 32));
                    } else if (cur == FREE_BITS) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            float value = mappingFunction.applyAsFloat(Float.intBitsToFloat(k));
            incrementModCount();
            tab[index] = ((((long) k) & FLOAT_MASK) | (((long) Float.floatToIntBits(value)) << 32));
            postInsertHook();
            return value;
        }
    }


    @Override
    public Float computeIfPresent(Float key,
            BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        int k = Float.floatToIntBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return null;
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return null;
                    }
                }
            }
        }
        // key is present
        Float newValue = remappingFunction.apply(Float.intBitsToFloat(k), Float.intBitsToFloat((int) (entry >>> 32)));
        if (newValue != null) {
            U.putInt(tab, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), Float.floatToIntBits(newValue));
            return newValue;
        } else {
            removeAt(index);
            return null;
        }
    }

    @Override
    public float computeIfPresent(float key, FloatFloatToFloatFunction remappingFunction) {
        int k = Float.floatToIntBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return defaultValue();
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return defaultValue();
                    }
                }
            }
        }
        // key is present
        float newValue = remappingFunction.applyAsFloat(Float.intBitsToFloat(k), Float.intBitsToFloat((int) (entry >>> 32)));
        U.putInt(tab, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), Float.floatToIntBits(newValue));
        return newValue;
    }

    @Override
    public Float merge(Float key, Float value,
            BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        int k = Float.floatToIntBits(key);
        if (value == null)
            throw new java.lang.NullPointerException();
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) != k) {
            keyAbsent:
            if (cur != FREE_BITS) {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            incrementModCount();
            tab[index] = ((((long) k) & FLOAT_MASK) | (((long) Float.floatToIntBits(value)) << 32));
            postInsertHook();
            return value;
        }
        // key is present
        Float newValue = remappingFunction.apply(Float.intBitsToFloat((int) (entry >>> 32)), value);
        if (newValue != null) {
            U.putInt(tab, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), Float.floatToIntBits(newValue));
            return newValue;
        } else {
            removeAt(index);
            return null;
        }
    }


    @Override
    public float merge(float key, float value, FloatBinaryOperator remappingFunction) {
        int k = Float.floatToIntBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) != k) {
            keyAbsent:
            if (cur != FREE_BITS) {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            incrementModCount();
            tab[index] = ((((long) k) & FLOAT_MASK) | (((long) Float.floatToIntBits(value)) << 32));
            postInsertHook();
            return value;
        }
        // key is present
        float newValue = remappingFunction.applyAsFloat(Float.intBitsToFloat((int) (entry >>> 32)), value);
        U.putInt(tab, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), Float.floatToIntBits(newValue));
        return newValue;
    }


    @Override
    public float addValue(float key, float value) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == FREE_BITS) {
            // key is absent
            incrementModCount();
            tab[index] = ((((long) k) & FLOAT_MASK) | (((long) Float.floatToIntBits(value)) << 32));
            postInsertHook();
            return value;
        } else {
            keyPresent:
            if (cur != k) {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == FREE_BITS) {
                        // key is absent
                        incrementModCount();
                        tab[index] = ((((long) k) & FLOAT_MASK) | (((long) Float.floatToIntBits(value)) << 32));
                        postInsertHook();
                        return value;
                    } else if (cur == k) {
                        break keyPresent;
                    }
                }
            }
            // key is present
            float newValue = Float.intBitsToFloat((int) (entry >>> 32)) + value;
            U.putInt(tab, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), Float.floatToIntBits(newValue));
            return newValue;
        }
    }

    @Override
    public float addValue(float key, float addition, float defaultValue) {
        int k = Float.floatToIntBits(key);
        float value = defaultValue + addition;
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == FREE_BITS) {
            // key is absent
            incrementModCount();
            tab[index] = ((((long) k) & FLOAT_MASK) | (((long) Float.floatToIntBits(value)) << 32));
            postInsertHook();
            return value;
        } else {
            keyPresent:
            if (cur != k) {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == FREE_BITS) {
                        // key is absent
                        incrementModCount();
                        tab[index] = ((((long) k) & FLOAT_MASK) | (((long) Float.floatToIntBits(value)) << 32));
                        postInsertHook();
                        return value;
                    } else if (cur == k) {
                        break keyPresent;
                    }
                }
            }
            // key is present
            float newValue = Float.intBitsToFloat((int) (entry >>> 32)) + addition;
            U.putInt(tab, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), Float.floatToIntBits(newValue));
            return newValue;
        }
    }


    @Override
    public void putAll(@Nonnull Map<? extends Float, ? extends Float> m) {
        CommonFloatFloatMapOps.putAll(this, m);
    }


    @Override
    public Float replace(Float key, Float value) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return null;
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return null;
                    }
                }
            }
        }
        // key is present
        float oldValue = Float.intBitsToFloat((int) (entry >>> 32));
        U.putInt(tab, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), Float.floatToIntBits(value));
        return oldValue;
    }

    @Override
    public float replace(float key, float value) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return defaultValue();
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return defaultValue();
                    }
                }
            }
        }
        // key is present
        float oldValue = Float.intBitsToFloat((int) (entry >>> 32));
        U.putInt(tab, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), Float.floatToIntBits(value));
        return oldValue;
    }

    @Override
    public boolean replace(Float key, Float oldValue, Float newValue) {
        return replace(key.floatValue(),
                oldValue.floatValue(),
                newValue.floatValue());
    }

    @Override
    public boolean replace(float key, float oldValue, float newValue) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return false;
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return false;
                    }
                }
            }
        }
        // key is present
        if ((int) (entry >>> 32) == Float.floatToIntBits(oldValue)) {
            U.putInt(tab, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), Float.floatToIntBits(newValue));
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void replaceAll(
            BiFunction<? super Float, ? super Float, ? extends Float> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                U.putInt(tab, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), Float.floatToIntBits(function.apply(Float.intBitsToFloat(key), Float.intBitsToFloat((int) (entry >>> 32)))));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void replaceAll(FloatFloatToFloatFunction function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                U.putInt(tab, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), Float.floatToIntBits(function.applyAsFloat(Float.intBitsToFloat(key), Float.intBitsToFloat((int) (entry >>> 32)))));
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
        long[] tab = table;
        int capacityMask = tab.length - 1;
        long entry;
        incrementModCount();
        int indexToRemove = index;
        int indexToShift = indexToRemove;
        int shiftDistance = 1;
        while (true) {
            indexToShift = (indexToShift - 1) & capacityMask;
            int keyToShift;
            if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                break;
            }
            if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                tab[indexToRemove] = entry;
                indexToRemove = indexToShift;
                shiftDistance = 1;
            } else {
                shiftDistance++;
                if (indexToShift == 1 + index) {
                    throw new java.util.ConcurrentModificationException();
                }
            }
        }
        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
        postRemoveHook();
    }

    @Override
    public Float remove(Object key) {
        int k = Float.floatToIntBits((Float) key);
        long[] tab = table;
        int capacityMask = tab.length - 1;
        int index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & capacityMask])) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return null;
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return null;
                    }
                }
            }
        }
        // key is present
        float val = Float.intBitsToFloat((int) (entry >>> 32));
        incrementModCount();
        int indexToRemove = index;
        int indexToShift = indexToRemove;
        int shiftDistance = 1;
        while (true) {
            indexToShift = (indexToShift - 1) & capacityMask;
            int keyToShift;
            if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                break;
            }
            if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                tab[indexToRemove] = entry;
                indexToRemove = indexToShift;
                shiftDistance = 1;
            } else {
                shiftDistance++;
                if (indexToShift == 1 + index) {
                    throw new java.util.ConcurrentModificationException();
                }
            }
        }
        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
        postRemoveHook();
        return val;
    }


    @Override
    public boolean justRemove(float key) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacityMask = tab.length - 1;
        int index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & capacityMask])) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return false;
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return false;
                    }
                }
            }
        }
        // key is present
        incrementModCount();
        int indexToRemove = index;
        int indexToShift = indexToRemove;
        int shiftDistance = 1;
        while (true) {
            indexToShift = (indexToShift - 1) & capacityMask;
            int keyToShift;
            if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                break;
            }
            if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                tab[indexToRemove] = entry;
                indexToRemove = indexToShift;
                shiftDistance = 1;
            } else {
                shiftDistance++;
                if (indexToShift == 1 + index) {
                    throw new java.util.ConcurrentModificationException();
                }
            }
        }
        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
        postRemoveHook();
        return true;
    }

    @Override
    public boolean justRemove(int key) {
        long[] tab = table;
        int capacityMask = tab.length - 1;
        int index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(key) & capacityMask])) != key) {
            if (cur == FREE_BITS) {
                // key is absent
                return false;
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return false;
                    }
                }
            }
        }
        // key is present
        incrementModCount();
        int indexToRemove = index;
        int indexToShift = indexToRemove;
        int shiftDistance = 1;
        while (true) {
            indexToShift = (indexToShift - 1) & capacityMask;
            int keyToShift;
            if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                break;
            }
            if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                tab[indexToRemove] = entry;
                indexToRemove = indexToShift;
                shiftDistance = 1;
            } else {
                shiftDistance++;
                if (indexToShift == 1 + index) {
                    throw new java.util.ConcurrentModificationException();
                }
            }
        }
        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
        postRemoveHook();
        return true;
    }


    

    @Override
    public float remove(float key) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacityMask = tab.length - 1;
        int index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & capacityMask])) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return defaultValue();
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return defaultValue();
                    }
                }
            }
        }
        // key is present
        float val = Float.intBitsToFloat((int) (entry >>> 32));
        incrementModCount();
        int indexToRemove = index;
        int indexToShift = indexToRemove;
        int shiftDistance = 1;
        while (true) {
            indexToShift = (indexToShift - 1) & capacityMask;
            int keyToShift;
            if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                break;
            }
            if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                tab[indexToRemove] = entry;
                indexToRemove = indexToShift;
                shiftDistance = 1;
            } else {
                shiftDistance++;
                if (indexToShift == 1 + index) {
                    throw new java.util.ConcurrentModificationException();
                }
            }
        }
        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
        postRemoveHook();
        return val;
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Float) key).floatValue(),
                ((Float) value).floatValue()
                );
    }

    @Override
    public boolean remove(float key, float value) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacityMask = tab.length - 1;
        int index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) & capacityMask])) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return false;
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return false;
                    }
                }
            }
        }
        // key is present
        if ((int) (entry >>> 32) == Float.floatToIntBits(value)) {
            incrementModCount();
            int indexToRemove = index;
            int indexToShift = indexToRemove;
            int shiftDistance = 1;
            while (true) {
                indexToShift = (indexToShift - 1) & capacityMask;
                int keyToShift;
                if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                    break;
                }
                if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                    tab[indexToRemove] = entry;
                    indexToRemove = indexToShift;
                    shiftDistance = 1;
                } else {
                    shiftDistance++;
                    if (indexToShift == 1 + index) {
                        throw new java.util.ConcurrentModificationException();
                    }
                }
            }
            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
            postRemoveHook();
            return true;
        } else {
            return false;
        }
    }


    @Override
    public boolean removeIf(FloatFloatPredicate filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                if (filter.test(Float.intBitsToFloat(key), Float.intBitsToFloat((int) (entry >>> 32)))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            int keyToShift;
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), REMOVED_BITS);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                tab[indexToRemove] = entry;
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                        postRemoveHook();
                    } else {
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), REMOVED_BITS);
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




    // under this condition - operations, overridden from MutableParallelKVFloatLHashGO
    // when values are objects - in order to set values to null on removing (for garbage collection)
    // when algo is LHash - because shift deletion should shift values to

    @Override
    public boolean removeIf(Predicate<? super Float> filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                if (filter.test(Float.intBitsToFloat(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            int keyToShift;
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), REMOVED_BITS);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                tab[indexToRemove] = entry;
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                        postRemoveHook();
                    } else {
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), REMOVED_BITS);
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
    public boolean removeIf(FloatPredicate filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                if (filter.test(Float.intBitsToFloat(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            int keyToShift;
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), REMOVED_BITS);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                tab[indexToRemove] = entry;
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                        postRemoveHook();
                    } else {
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), REMOVED_BITS);
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
    public boolean removeAll(@Nonnull HashFloatSet thisC, @Nonnull Collection<?> c) {
        if (c instanceof InternalFloatCollectionOps)
            return removeAll(thisC, (InternalFloatCollectionOps) c);
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                if (c.contains(Float.intBitsToFloat(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            int keyToShift;
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), REMOVED_BITS);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                tab[indexToRemove] = entry;
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                        postRemoveHook();
                    } else {
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), REMOVED_BITS);
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
    boolean removeAll(@Nonnull HashFloatSet thisC, @Nonnull FloatCollection c) {
        if (c instanceof InternalFloatCollectionOps)
            return removeAll(thisC, (InternalFloatCollectionOps) c);
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                if (c.contains(Float.intBitsToFloat(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            int keyToShift;
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), REMOVED_BITS);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                tab[indexToRemove] = entry;
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                        postRemoveHook();
                    } else {
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), REMOVED_BITS);
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
    boolean removeAll(@Nonnull HashFloatSet thisC, @Nonnull InternalFloatCollectionOps c) {
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        long[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
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
                            int keyToShift;
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), REMOVED_BITS);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                tab[indexToRemove] = entry;
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                        postRemoveHook();
                    } else {
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), REMOVED_BITS);
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
    public boolean retainAll(@Nonnull HashFloatSet thisC, @Nonnull Collection<?> c) {
        if (c instanceof FloatCollection)
            return retainAll(thisC, (FloatCollection) c);
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
        long[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                if (!c.contains(Float.intBitsToFloat(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            int keyToShift;
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), REMOVED_BITS);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                tab[indexToRemove] = entry;
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                        postRemoveHook();
                    } else {
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), REMOVED_BITS);
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

    private boolean retainAll(@Nonnull HashFloatSet thisC, @Nonnull FloatCollection c) {
        if (c instanceof InternalFloatCollectionOps)
            return retainAll(thisC, (InternalFloatCollectionOps) c);
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
        long[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                if (!c.contains(Float.intBitsToFloat(key))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            int keyToShift;
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), REMOVED_BITS);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                tab[indexToRemove] = entry;
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                        postRemoveHook();
                    } else {
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), REMOVED_BITS);
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

    private boolean retainAll(@Nonnull HashFloatSet thisC,
            @Nonnull InternalFloatCollectionOps c) {
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
        long[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
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
                            int keyToShift;
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), REMOVED_BITS);
                                    break closeDeletion;
                                }
                                if (indexToRemove == i) {
                                    i++;
                                }
                                tab[indexToRemove] = entry;
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + i) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                        postRemoveHook();
                    } else {
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), REMOVED_BITS);
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
        long[] tab = table;
        int capacityMask = tab.length - 1;
        long entry;
        for (int i = firstDelayedRemoved; i >= 0; i--) {
            if ((int) (entry = tab[i]) == REMOVED_BITS) {
                int indexToRemove = i;
                int indexToShift = indexToRemove;
                int shiftDistance = 1;
                while (true) {
                    indexToShift = (indexToShift - 1) & capacityMask;
                    int keyToShift;
                    if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                        break;
                    }
                    if ((keyToShift != REMOVED_BITS) && (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance)) {
                        tab[indexToRemove] = entry;
                        indexToRemove = indexToShift;
                        shiftDistance = 1;
                    } else {
                        shiftDistance++;
                        if (indexToShift == 1 + i) {
                            throw new java.util.ConcurrentModificationException();
                        }
                    }
                }
                U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                postRemoveHook();
            }
        }
    }



    @Override
    public FloatIterator iterator() {
        int mc = modCount();
        return new NoRemovedKeyIterator(mc);
    }

    @Override
    public FloatCursor setCursor() {
        int mc = modCount();
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
                    long entry;
                    long[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            int keyToShift;
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 1) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putInt(this.tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.nextIndex = index;
                                        if (indexToShift < index - 1) {
                                            this.next = Float.intBitsToFloat(keyToShift);
                                        }
                                    }
                                }
                                tab[indexToRemove] = entry;
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                        postRemoveHook();
                    } else {
                        justRemove(U.getInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT)));
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

        private NoRemovedKeyCursor(int mc) {
            super(mc);
            
        }

        @Override
        public void remove() {
            int curKey;
            if ((curKey = this.curKey) != FREE_BITS) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE_BITS;
                    long entry;
                    int index = this.index;
                    long[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            int keyToShift;
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putInt(this.tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.index = ++index;
                                    }
                                }
                                tab[indexToRemove] = entry;
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                        postRemoveHook();
                    } else {
                        justRemove(curKey);
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }





    class EntryView extends AbstractSetView<Map.Entry<Float, Float>>
            implements HashObjSet<Map.Entry<Float, Float>>,
            InternalObjCollectionOps<Map.Entry<Float, Float>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Float, Float>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Float>defaultEquality()
                    ,
                    Equivalence.<Float>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return MutableLHashParallelKVFloatFloatMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return MutableLHashParallelKVFloatFloatMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return MutableLHashParallelKVFloatFloatMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Float, Float> e = (Map.Entry<Float, Float>) o;
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    result[resultIndex++] = new MutableEntry(mc, i, key, (int) (entry >>> 32));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    a[resultIndex++] = (T) new MutableEntry(mc, i, key, (int) (entry >>> 32));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }

        @Override
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Float, Float>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    action.accept(new MutableEntry(mc, i, key, (int) (entry >>> 32)));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Float, Float>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    if (!predicate.test(new MutableEntry(mc, i, key, (int) (entry >>> 32)))) {
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
        public ObjIterator<Map.Entry<Float, Float>> iterator() {
            int mc = modCount();
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Float, Float>> cursor() {
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    if (!c.contains(e.with(key, (int) (entry >>> 32)))) {
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    changed |= s.remove(e.with(key, (int) (entry >>> 32)));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Float, Float>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    changed |= c.add(new MutableEntry(mc, i, key, (int) (entry >>> 32)));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        public int hashCode() {
            return MutableLHashParallelKVFloatFloatMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    sb.append(' ');
                    sb.append(Float.intBitsToFloat(key));
                    sb.append('=');
                    sb.append(Float.intBitsToFloat((int) (entry >>> 32)));
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
            return MutableLHashParallelKVFloatFloatMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Float, Float> e = (Map.Entry<Float, Float>) o;
                float key = e.getKey();
                float value = e.getValue();
                return MutableLHashParallelKVFloatFloatMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Float, Float>> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    if (filter.test(new MutableEntry(mc, i, key, (int) (entry >>> 32)))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                int keyToShift;
                                if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                    break;
                                }
                                if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), REMOVED_BITS);
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i++;
                                    }
                                    tab[indexToRemove] = entry;
                                    indexToRemove = indexToShift;
                                    shiftDistance = 1;
                                } else {
                                    shiftDistance++;
                                    if (indexToShift == 1 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                            postRemoveHook();
                        } else {
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), REMOVED_BITS);
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
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    if (c.contains(e.with(key, (int) (entry >>> 32)))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                int keyToShift;
                                if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                    break;
                                }
                                if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), REMOVED_BITS);
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i++;
                                    }
                                    tab[indexToRemove] = entry;
                                    indexToRemove = indexToShift;
                                    shiftDistance = 1;
                                } else {
                                    shiftDistance++;
                                    if (indexToShift == 1 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                            postRemoveHook();
                        } else {
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), REMOVED_BITS);
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
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    if (!c.contains(e.with(key, (int) (entry >>> 32)))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                int keyToShift;
                                if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                    break;
                                }
                                if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), REMOVED_BITS);
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i++;
                                    }
                                    tab[indexToRemove] = entry;
                                    indexToRemove = indexToShift;
                                    shiftDistance = 1;
                                } else {
                                    shiftDistance++;
                                    if (indexToShift == 1 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                            postRemoveHook();
                        } else {
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), REMOVED_BITS);
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
            MutableLHashParallelKVFloatFloatMapGO.this.clear();
        }
    }


    abstract class FloatFloatEntry extends AbstractEntry<Float, Float> {

        abstract int key();

        @Override
        public final Float getKey() {
            return Float.intBitsToFloat(key());
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
            int k2;
            int v2;
            try {
                e2 = (Map.Entry) o;
                k2 = Float.floatToIntBits((Float) e2.getKey());
                v2 = Float.floatToIntBits((Float) e2.getValue());
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


    class MutableEntry extends FloatFloatEntry {
        final int modCount;
        private final int index;
        final int key;
        private int value;

        MutableEntry(int modCount, int index, int key, int value) {
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
            U.putInt(
                    table, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) index) << LONG_SCALE_SHIFT),
                    newValue);
        }
    }



    class ReusableEntry extends FloatFloatEntry {
        private int key;
        private int value;

        ReusableEntry with(int key, int value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public int key() {
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
            return MutableLHashParallelKVFloatFloatMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return MutableLHashParallelKVFloatFloatMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return MutableLHashParallelKVFloatFloatMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(float v) {
            return MutableLHashParallelKVFloatFloatMapGO.this.containsValue(v);
        }

        @Override
        public boolean contains(int bits) {
            return MutableLHashParallelKVFloatFloatMapGO.this.containsValue(bits);
        }


        @Override
        public void forEach(Consumer<? super Float> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    action.accept(Float.intBitsToFloat((int) (entry >>> 32)));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    action.accept(Float.intBitsToFloat((int) (entry >>> 32)));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    if (!predicate.test(Float.intBitsToFloat((int) (entry >>> 32)))) {
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    if (!c.contains(Float.intBitsToFloat((int) (entry >>> 32)))) {
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    if (!c.contains((int) (entry >>> 32))) {
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    changed |= c.add(Float.intBitsToFloat((int) (entry >>> 32)));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    changed |= c.add((int) (entry >>> 32));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    changed |= s.removeFloat(Float.intBitsToFloat((int) (entry >>> 32)));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    changed |= s.removeFloat((int) (entry >>> 32));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    result[resultIndex++] = Float.intBitsToFloat((int) (entry >>> 32));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    a[resultIndex++] = (T) Float.valueOf(Float.intBitsToFloat((int) (entry >>> 32)));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    result[resultIndex++] = Float.intBitsToFloat((int) (entry >>> 32));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    a[resultIndex++] = Float.intBitsToFloat((int) (entry >>> 32));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    sb.append(' ').append(Float.intBitsToFloat((int) (entry >>> 32))).append(',');
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
            MutableLHashParallelKVFloatFloatMapGO.this.clear();
        }

        
        public boolean removeIf(Predicate<? super Float> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    if (filter.test(Float.intBitsToFloat((int) (entry >>> 32)))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                int keyToShift;
                                if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                    break;
                                }
                                if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), REMOVED_BITS);
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i++;
                                    }
                                    tab[indexToRemove] = entry;
                                    indexToRemove = indexToShift;
                                    shiftDistance = 1;
                                } else {
                                    shiftDistance++;
                                    if (indexToShift == 1 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                            postRemoveHook();
                        } else {
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), REMOVED_BITS);
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
        public boolean removeIf(FloatPredicate filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    if (filter.test(Float.intBitsToFloat((int) (entry >>> 32)))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                int keyToShift;
                                if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                    break;
                                }
                                if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), REMOVED_BITS);
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i++;
                                    }
                                    tab[indexToRemove] = entry;
                                    indexToRemove = indexToShift;
                                    shiftDistance = 1;
                                } else {
                                    shiftDistance++;
                                    if (indexToShift == 1 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                            postRemoveHook();
                        } else {
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), REMOVED_BITS);
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
            if (c instanceof FloatCollection)
                return removeAll((FloatCollection) c);
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    if (c.contains(Float.intBitsToFloat((int) (entry >>> 32)))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                int keyToShift;
                                if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                    break;
                                }
                                if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), REMOVED_BITS);
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i++;
                                    }
                                    tab[indexToRemove] = entry;
                                    indexToRemove = indexToShift;
                                    shiftDistance = 1;
                                } else {
                                    shiftDistance++;
                                    if (indexToShift == 1 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                            postRemoveHook();
                        } else {
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), REMOVED_BITS);
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

        private boolean removeAll(FloatCollection c) {
            if (c instanceof InternalFloatCollectionOps)
                return removeAll((InternalFloatCollectionOps) c);
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    if (c.contains(Float.intBitsToFloat((int) (entry >>> 32)))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                int keyToShift;
                                if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                    break;
                                }
                                if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), REMOVED_BITS);
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i++;
                                    }
                                    tab[indexToRemove] = entry;
                                    indexToRemove = indexToShift;
                                    shiftDistance = 1;
                                } else {
                                    shiftDistance++;
                                    if (indexToShift == 1 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                            postRemoveHook();
                        } else {
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), REMOVED_BITS);
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

        private boolean removeAll(InternalFloatCollectionOps c) {
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    if (c.contains((int) (entry >>> 32))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                int keyToShift;
                                if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                    break;
                                }
                                if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), REMOVED_BITS);
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i++;
                                    }
                                    tab[indexToRemove] = entry;
                                    indexToRemove = indexToShift;
                                    shiftDistance = 1;
                                } else {
                                    shiftDistance++;
                                    if (indexToShift == 1 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                            postRemoveHook();
                        } else {
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), REMOVED_BITS);
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
            if (c instanceof FloatCollection)
                return retainAll((FloatCollection) c);
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
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    if (!c.contains(Float.intBitsToFloat((int) (entry >>> 32)))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                int keyToShift;
                                if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                    break;
                                }
                                if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), REMOVED_BITS);
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i++;
                                    }
                                    tab[indexToRemove] = entry;
                                    indexToRemove = indexToShift;
                                    shiftDistance = 1;
                                } else {
                                    shiftDistance++;
                                    if (indexToShift == 1 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                            postRemoveHook();
                        } else {
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), REMOVED_BITS);
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

        private boolean retainAll(FloatCollection c) {
            if (c instanceof InternalFloatCollectionOps)
                return retainAll((InternalFloatCollectionOps) c);
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
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    if (!c.contains(Float.intBitsToFloat((int) (entry >>> 32)))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                int keyToShift;
                                if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                    break;
                                }
                                if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), REMOVED_BITS);
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i++;
                                    }
                                    tab[indexToRemove] = entry;
                                    indexToRemove = indexToShift;
                                    shiftDistance = 1;
                                } else {
                                    shiftDistance++;
                                    if (indexToShift == 1 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                            postRemoveHook();
                        } else {
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), REMOVED_BITS);
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

        private boolean retainAll(InternalFloatCollectionOps c) {
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
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    if (!c.contains((int) (entry >>> 32))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                int keyToShift;
                                if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                    break;
                                }
                                if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), REMOVED_BITS);
                                        break closeDeletion;
                                    }
                                    if (indexToRemove == i) {
                                        i++;
                                    }
                                    tab[indexToRemove] = entry;
                                    indexToRemove = indexToShift;
                                    shiftDistance = 1;
                                } else {
                                    shiftDistance++;
                                    if (indexToShift == 1 + i) {
                                        throw new java.util.ConcurrentModificationException();
                                    }
                                }
                            }
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                            postRemoveHook();
                        } else {
                            U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), REMOVED_BITS);
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Float, Float>> {
        long[] tab;
        final int capacityMask;
        int expectedModCount;
        
        class MutableEntry2 extends MutableEntry {
            MutableEntry2(int modCount, int index, int key, int value) {
                super(modCount, index, key, value);
            }
            
            @Override
            void updateValueInTable(int newValue) {
                if (tab == table) {
                    U.putInt(tab, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
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
            long[] tab = this.tab = table;
            capacityMask = tab.length - 1;
            int nextI = tab.length;
            long entry;
            while (--nextI >= 0) {
                int key;
                if ((key = (int) (entry = tab[nextI])) < FREE_BITS) {
                    next = new MutableEntry2(mc, nextI, key, (int) (entry >>> 32));
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Float, Float>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            long entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    action.accept(new MutableEntry2(mc, i, key, (int) (entry >>> 32)));
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
        public Map.Entry<Float, Float> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
                    index = nextI;
                    long[] tab = this.tab;
                    MutableEntry prev = next;
                    long entry;
                    while (--nextI >= 0) {
                        int key;
                        if ((key = (int) (entry = tab[nextI])) < FREE_BITS) {
                            next = new MutableEntry2(mc, nextI, key, (int) (entry >>> 32));
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
                    long entry;
                    long[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            int keyToShift;
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 1) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putInt(this.tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.nextIndex = index;
                                        if (indexToShift < index - 1) {
                                            this.next = new MutableEntry2(modCount(), indexToShift, keyToShift, (int) (entry >>> 32));
                                        }
                                    }
                                }
                                tab[indexToRemove] = entry;
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                        postRemoveHook();
                    } else {
                        justRemove(U.getInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT)));
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Float, Float>> {
        long[] tab;
        final int capacityMask;
        int expectedModCount;
        
        class MutableEntry2 extends MutableEntry {
            MutableEntry2(int modCount, int index, int key, int value) {
                super(modCount, index, key, value);
            }
            
            @Override
            void updateValueInTable(int newValue) {
                if (tab == table) {
                    U.putInt(tab, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
                } else {
                    justPut(key, newValue);
                    if (this.modCount != modCount()) {
                        throw new java.lang.IllegalStateException();
                    }
                }
            }
        }
        
        int index;
        int curKey;
        int curValue;

        NoRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            long[] tab = this.tab = table;
            capacityMask = tab.length - 1;
            index = tab.length;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Float, Float>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            long entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    action.accept(new MutableEntry2(mc, i, key, (int) (entry >>> 32)));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE_BITS;
        }

        @Override
        public Map.Entry<Float, Float> elem() {
            int curKey;
            if ((curKey = this.curKey) != FREE_BITS) {
                return new MutableEntry2(expectedModCount, index, curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                long[] tab = this.tab;
                long entry;
                for (int i = index - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                        index = i;
                        curKey = key;
                        curValue = (int) (entry >>> 32);
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
            int curKey;
            if ((curKey = this.curKey) != FREE_BITS) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE_BITS;
                    long entry;
                    int index = this.index;
                    long[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            int keyToShift;
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putInt(this.tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.index = ++index;
                                    }
                                }
                                tab[indexToRemove] = entry;
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                        postRemoveHook();
                    } else {
                        justRemove(curKey);
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }




    class NoRemovedValueIterator implements FloatIterator {
        long[] tab;
        final int capacityMask;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        float next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            long[] tab = this.tab = table;
            capacityMask = tab.length - 1;
            int nextI = tab.length;
            long entry;
            while (--nextI >= 0) {
                if ((int) (entry = tab[nextI]) < FREE_BITS) {
                    next = Float.intBitsToFloat((int) (entry >>> 32));
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
                    index = nextI;
                    long[] tab = this.tab;
                    float prev = next;
                    long entry;
                    while (--nextI >= 0) {
                        if ((int) (entry = tab[nextI]) < FREE_BITS) {
                            next = Float.intBitsToFloat((int) (entry >>> 32));
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
            long[] tab = this.tab;
            long entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    action.accept(Float.intBitsToFloat((int) (entry >>> 32)));
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            index = nextIndex = -1;
        }

        @Override
        public void forEachRemaining(FloatConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            long entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    action.accept(Float.intBitsToFloat((int) (entry >>> 32)));
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
        public Float next() {
            return nextFloat();
        }

        @Override
        public void remove() {
            int index;
            if ((index = this.index) >= 0) {
                if (expectedModCount++ == modCount()) {
                    this.index = -1;
                    long entry;
                    long[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            int keyToShift;
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 1) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putInt(this.tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.nextIndex = index;
                                        if (indexToShift < index - 1) {
                                            this.next = Float.intBitsToFloat((int) (entry >>> 32));
                                        }
                                    }
                                }
                                tab[indexToRemove] = entry;
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                        postRemoveHook();
                    } else {
                        justRemove(U.getInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT)));
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedValueCursor implements FloatCursor {
        long[] tab;
        final int capacityMask;
        int expectedModCount;
        int index;
        int curKey;
        int curValue;

        NoRemovedValueCursor(int mc) {
            expectedModCount = mc;
            long[] tab = this.tab = table;
            capacityMask = tab.length - 1;
            index = tab.length;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(FloatConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            long entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    action.accept(Float.intBitsToFloat((int) (entry >>> 32)));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE_BITS;
        }

        @Override
        public float elem() {
            if (curKey != FREE_BITS) {
                return Float.intBitsToFloat(curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                long[] tab = this.tab;
                long entry;
                for (int i = index - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                        index = i;
                        curKey = key;
                        curValue = (int) (entry >>> 32);
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
            int curKey;
            if ((curKey = this.curKey) != FREE_BITS) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE_BITS;
                    long entry;
                    int index = this.index;
                    long[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            int keyToShift;
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putInt(this.tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.index = ++index;
                                    }
                                }
                                tab[indexToRemove] = entry;
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                        postRemoveHook();
                    } else {
                        justRemove(curKey);
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }



    class NoRemovedMapCursor implements FloatFloatCursor {
        long[] tab;
        final int capacityMask;
        int expectedModCount;
        int index;
        int curKey;
        int curValue;

        NoRemovedMapCursor(int mc) {
            expectedModCount = mc;
            long[] tab = this.tab = table;
            capacityMask = tab.length - 1;
            index = tab.length;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(FloatFloatConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            long entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    action.accept(Float.intBitsToFloat(key), Float.intBitsToFloat((int) (entry >>> 32)));
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
        public float value() {
            if (curKey != FREE_BITS) {
                return Float.intBitsToFloat(curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(float value) {
            if (curKey != FREE_BITS) {
                if (expectedModCount == modCount()) {
                    U.putInt(tab, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), Float.floatToIntBits(value));
                    if (tab != table) {
                        U.putInt(table, LONG_BASE + FLOAT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), Float.floatToIntBits(value));
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
                long[] tab = this.tab;
                long entry;
                for (int i = index - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                        index = i;
                        curKey = key;
                        curValue = (int) (entry >>> 32);
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
            int curKey;
            if ((curKey = this.curKey) != FREE_BITS) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = FREE_BITS;
                    long entry;
                    int index = this.index;
                    long[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            int keyToShift;
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == FREE_BITS) {
                                break;
                            }
                            if (((ParallelKVFloatKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putInt(this.tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.index = ++index;
                                    }
                                }
                                tab[indexToRemove] = entry;
                                indexToRemove = indexToShift;
                                shiftDistance = 1;
                            } else {
                                shiftDistance++;
                                if (indexToShift == 1 + index) {
                                    throw new java.util.ConcurrentModificationException();
                                }
                            }
                        }
                        U.putInt(tab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), FREE_BITS);
                        postRemoveHook();
                    } else {
                        justRemove(curKey);
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

