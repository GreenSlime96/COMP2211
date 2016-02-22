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
import net.openhft.koloboke.function.FloatIntConsumer;
import net.openhft.koloboke.function.FloatIntPredicate;
import net.openhft.koloboke.function.FloatIntToIntFunction;
import net.openhft.koloboke.function.FloatToIntFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class UpdatableQHashParallelKVFloatIntMapGO
        extends UpdatableQHashParallelKVFloatIntMapSO {

    
    final void copy(ParallelKVFloatIntQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    
    final void move(ParallelKVFloatIntQHash hash) {
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
    public boolean containsEntry(float key, int value) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) == k) {
            // key is present
            return (int) (entry >>> 32) == value;
        } else {
            if (cur == FREE_BITS) {
                // key is absent
                return false;
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == k) {
                        // key is present
                        return (int) (entry >>> 32) == value;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return false;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == k) {
                        // key is present
                        return (int) (entry >>> 32) == value;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return false;
                    }
                    step += 2;
                }
            }
        }
    }

    @Override
    public boolean containsEntry(int key, int value) {
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(key) % (capacity = tab.length)])) == key) {
            // key is present
            return (int) (entry >>> 32) == value;
        } else {
            if (cur == FREE_BITS) {
                // key is absent
                return false;
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == key) {
                        // key is present
                        return (int) (entry >>> 32) == value;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return false;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == key) {
                        // key is present
                        return (int) (entry >>> 32) == value;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return false;
                    }
                    step += 2;
                }
            }
        }
    }

    @Override
    public Integer get(Object key) {
        int k = Float.floatToIntBits((Float) key);
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) == k) {
            // key is present
            return (int) (entry >>> 32);
        } else {
            if (cur == FREE_BITS) {
                // key is absent
                return null;
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == k) {
                        // key is present
                        return (int) (entry >>> 32);
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return null;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == k) {
                        // key is present
                        return (int) (entry >>> 32);
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return null;
                    }
                    step += 2;
                }
            }
        }
    }

    

    @Override
    public int get(float key) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) == k) {
            // key is present
            return (int) (entry >>> 32);
        } else {
            if (cur == FREE_BITS) {
                // key is absent
                return defaultValue();
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == k) {
                        // key is present
                        return (int) (entry >>> 32);
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return defaultValue();
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == k) {
                        // key is present
                        return (int) (entry >>> 32);
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return defaultValue();
                    }
                    step += 2;
                }
            }
        }
    }

    @Override
    public Integer getOrDefault(Object key, Integer defaultValue) {
        int k = Float.floatToIntBits((Float) key);
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) == k) {
            // key is present
            return (int) (entry >>> 32);
        } else {
            if (cur == FREE_BITS) {
                // key is absent
                return defaultValue;
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == k) {
                        // key is present
                        return (int) (entry >>> 32);
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return defaultValue;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == k) {
                        // key is present
                        return (int) (entry >>> 32);
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return defaultValue;
                    }
                    step += 2;
                }
            }
        }
    }

    @Override
    public int getOrDefault(float key, int defaultValue) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) == k) {
            // key is present
            return (int) (entry >>> 32);
        } else {
            if (cur == FREE_BITS) {
                // key is absent
                return defaultValue;
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == k) {
                        // key is present
                        return (int) (entry >>> 32);
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return defaultValue;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == k) {
                        // key is present
                        return (int) (entry >>> 32);
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return defaultValue;
                    }
                    step += 2;
                }
            }
        }
    }

    @Override
    public void forEach(BiConsumer<? super Float, ? super Integer> action) {
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
                action.accept(Float.intBitsToFloat(key), (int) (entry >>> 32));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void forEach(FloatIntConsumer action) {
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
                action.accept(Float.intBitsToFloat(key), (int) (entry >>> 32));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public boolean forEachWhile(FloatIntPredicate predicate) {
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
                if (!predicate.test(Float.intBitsToFloat(key), (int) (entry >>> 32))) {
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
    public FloatIntCursor cursor() {
        int mc = modCount();
        return new NoRemovedMapCursor(mc);
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonFloatIntMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalFloatIntMapOps m) {
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
    public void reversePutAllTo(InternalFloatIntMapOps m) {
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
    public HashObjSet<Map.Entry<Float, Integer>> entrySet() {
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
                sb.append((int) (entry >>> 32));
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
        int capacity = newTab.length;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                int index;
                if (U.getInt(newTab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (index = ParallelKVFloatKeyMixing.mix(key) % capacity)) << LONG_SCALE_SHIFT)) != FREE_BITS) {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if (U.getInt(newTab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (bIndex)) << LONG_SCALE_SHIFT)) == FREE_BITS) {
                            index = bIndex;
                            break;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if (U.getInt(newTab, LONG_BASE + FLOAT_KEY_OFFSET + (((long) (fIndex)) << LONG_SCALE_SHIFT)) == FREE_BITS) {
                            index = fIndex;
                            break;
                        }
                        step += 2;
                    }
                }
                newTab[index] = entry;
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }


    @Override
    public Integer put(Float key, Integer value) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) == FREE_BITS) {
            // key is absent
            incrementModCount();
            tab[index] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
            postInsertHook();
            return null;
        } else {
            keyPresent:
            if (cur != k) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == FREE_BITS) {
                        // key is absent
                        incrementModCount();
                        tab[bIndex] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
                        postInsertHook();
                        return null;
                    } else if (cur == k) {
                        index = bIndex;
                        break keyPresent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == FREE_BITS) {
                        // key is absent
                        incrementModCount();
                        tab[fIndex] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
                        postInsertHook();
                        return null;
                    } else if (cur == k) {
                        index = fIndex;
                        break keyPresent;
                    }
                    step += 2;
                }
            }
            // key is present
            int prevValue = (int) (entry >>> 32);
            U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), value);
            return prevValue;
        }
    }

    @Override
    public int put(float key, int value) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) == FREE_BITS) {
            // key is absent
            incrementModCount();
            tab[index] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
            postInsertHook();
            return defaultValue();
        } else {
            keyPresent:
            if (cur != k) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == FREE_BITS) {
                        // key is absent
                        incrementModCount();
                        tab[bIndex] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
                        postInsertHook();
                        return defaultValue();
                    } else if (cur == k) {
                        index = bIndex;
                        break keyPresent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == FREE_BITS) {
                        // key is absent
                        incrementModCount();
                        tab[fIndex] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
                        postInsertHook();
                        return defaultValue();
                    } else if (cur == k) {
                        index = fIndex;
                        break keyPresent;
                    }
                    step += 2;
                }
            }
            // key is present
            int prevValue = (int) (entry >>> 32);
            U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), value);
            return prevValue;
        }
    }

    @Override
    public Integer putIfAbsent(Float key, Integer value) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) == FREE_BITS) {
            // key is absent
            incrementModCount();
            tab[index] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
            postInsertHook();
            return null;
        } else {
            if (cur == k) {
                // key is present
                return (int) (entry >>> 32);
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == FREE_BITS) {
                        // key is absent
                        incrementModCount();
                        tab[bIndex] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
                        postInsertHook();
                        return null;
                    } else if (cur == k) {
                        // key is present
                        return (int) (entry >>> 32);
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == FREE_BITS) {
                        // key is absent
                        incrementModCount();
                        tab[fIndex] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
                        postInsertHook();
                        return null;
                    } else if (cur == k) {
                        // key is present
                        return (int) (entry >>> 32);
                    }
                    step += 2;
                }
            }
        }
    }

    @Override
    public int putIfAbsent(float key, int value) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) == FREE_BITS) {
            // key is absent
            incrementModCount();
            tab[index] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
            postInsertHook();
            return defaultValue();
        } else {
            if (cur == k) {
                // key is present
                return (int) (entry >>> 32);
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == FREE_BITS) {
                        // key is absent
                        incrementModCount();
                        tab[bIndex] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
                        postInsertHook();
                        return defaultValue();
                    } else if (cur == k) {
                        // key is present
                        return (int) (entry >>> 32);
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == FREE_BITS) {
                        // key is absent
                        incrementModCount();
                        tab[fIndex] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
                        postInsertHook();
                        return defaultValue();
                    } else if (cur == k) {
                        // key is present
                        return (int) (entry >>> 32);
                    }
                    step += 2;
                }
            }
        }
    }

    @Override
    public void justPut(float key, int value) {
        int k = Float.floatToIntBits(key);
        int index = insert(k, value);
        if (index < 0) {
            // key was absent
            return;
        } else {
            // key is present
            U.putInt(table, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), value);
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
            U.putInt(table, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), value);
            return;
        }
    }

    @Override
    public Integer compute(Float key,
            BiFunction<? super Float, ? super Integer, ? extends Integer> remappingFunction) {
        int k = Float.floatToIntBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
            keyAbsent:
            if (cur != FREE_BITS) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == k) {
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
            Integer newValue = remappingFunction.apply(Float.intBitsToFloat(k), null);
            if (newValue != null) {
                incrementModCount();
                tab[index] = ((((long) k) & FLOAT_MASK) | (((long) newValue) << 32));
                postInsertHook();
                return newValue;
            } else {
                return null;
            }
        }
        // key is present
        Integer newValue = remappingFunction.apply(Float.intBitsToFloat(k), (int) (entry >>> 32));
        if (newValue != null) {
            U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
            return newValue;
        } else {
            throw new java.lang.UnsupportedOperationException("Compute operation of updatable map doesn't support removals");
        }
    }


    @Override
    public int compute(float key, FloatIntToIntFunction remappingFunction) {
        int k = Float.floatToIntBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
            keyAbsent:
            if (cur != FREE_BITS) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == k) {
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
            int newValue = remappingFunction.applyAsInt(Float.intBitsToFloat(k), defaultValue());
            incrementModCount();
            tab[index] = ((((long) k) & FLOAT_MASK) | (((long) newValue) << 32));
            postInsertHook();
            return newValue;
        }
        // key is present
        int newValue = remappingFunction.applyAsInt(Float.intBitsToFloat(k), (int) (entry >>> 32));
        U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
        return newValue;
    }


    @Override
    public Integer computeIfAbsent(Float key,
            Function<? super Float, ? extends Integer> mappingFunction) {
        int k = Float.floatToIntBits(key);
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) == k) {
            // key is present
            return (int) (entry >>> 32);
        } else {
            keyAbsent:
            if (cur != FREE_BITS) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == k) {
                        // key is present
                        return (int) (entry >>> 32);
                    } else if (cur == FREE_BITS) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == k) {
                        // key is present
                        return (int) (entry >>> 32);
                    } else if (cur == FREE_BITS) {
                        index = fIndex;
                        break keyAbsent;
                    }
                    step += 2;
                }
            }
            // key is absent
            Integer value = mappingFunction.apply(Float.intBitsToFloat(k));
            if (value != null) {
                incrementModCount();
                tab[index] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
                postInsertHook();
                return value;
            } else {
                return null;
            }
        }
    }


    @Override
    public int computeIfAbsent(float key, FloatToIntFunction mappingFunction) {
        int k = Float.floatToIntBits(key);
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) == k) {
            // key is present
            return (int) (entry >>> 32);
        } else {
            keyAbsent:
            if (cur != FREE_BITS) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == k) {
                        // key is present
                        return (int) (entry >>> 32);
                    } else if (cur == FREE_BITS) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == k) {
                        // key is present
                        return (int) (entry >>> 32);
                    } else if (cur == FREE_BITS) {
                        index = fIndex;
                        break keyAbsent;
                    }
                    step += 2;
                }
            }
            // key is absent
            int value = mappingFunction.applyAsInt(Float.intBitsToFloat(k));
            incrementModCount();
            tab[index] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
            postInsertHook();
            return value;
        }
    }


    @Override
    public Integer computeIfPresent(Float key,
            BiFunction<? super Float, ? super Integer, ? extends Integer> remappingFunction) {
        int k = Float.floatToIntBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return null;
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return null;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == k) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return null;
                    }
                    step += 2;
                }
            }
        }
        // key is present
        Integer newValue = remappingFunction.apply(Float.intBitsToFloat(k), (int) (entry >>> 32));
        if (newValue != null) {
            U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
            return newValue;
        } else {
            throw new java.lang.UnsupportedOperationException("ComputeIfPresent operation of updatable map doesn't support removals");
        }
    }

    @Override
    public int computeIfPresent(float key, FloatIntToIntFunction remappingFunction) {
        int k = Float.floatToIntBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return defaultValue();
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return defaultValue();
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == k) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return defaultValue();
                    }
                    step += 2;
                }
            }
        }
        // key is present
        int newValue = remappingFunction.applyAsInt(Float.intBitsToFloat(k), (int) (entry >>> 32));
        U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
        return newValue;
    }

    @Override
    public Integer merge(Float key, Integer value,
            BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        int k = Float.floatToIntBits(key);
        if (value == null)
            throw new java.lang.NullPointerException();
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
            keyAbsent:
            if (cur != FREE_BITS) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == k) {
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
            tab[index] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
            postInsertHook();
            return value;
        }
        // key is present
        Integer newValue = remappingFunction.apply((int) (entry >>> 32), value);
        if (newValue != null) {
            U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
            return newValue;
        } else {
            throw new java.lang.UnsupportedOperationException("Merge operation of updatable map doesn't support removals");
        }
    }


    @Override
    public int merge(float key, int value, IntBinaryOperator remappingFunction) {
        int k = Float.floatToIntBits(key);
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
            keyAbsent:
            if (cur != FREE_BITS) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == k) {
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
            tab[index] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
            postInsertHook();
            return value;
        }
        // key is present
        int newValue = remappingFunction.applyAsInt((int) (entry >>> 32), value);
        U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
        return newValue;
    }


    @Override
    public int addValue(float key, int value) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) == FREE_BITS) {
            // key is absent
            incrementModCount();
            tab[index] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
            postInsertHook();
            return value;
        } else {
            keyPresent:
            if (cur != k) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == FREE_BITS) {
                        // key is absent
                        incrementModCount();
                        tab[bIndex] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
                        postInsertHook();
                        return value;
                    } else if (cur == k) {
                        index = bIndex;
                        break keyPresent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == FREE_BITS) {
                        // key is absent
                        incrementModCount();
                        tab[fIndex] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
                        postInsertHook();
                        return value;
                    } else if (cur == k) {
                        index = fIndex;
                        break keyPresent;
                    }
                    step += 2;
                }
            }
            // key is present
            int newValue = (int) (entry >>> 32) + value;
            U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
            return newValue;
        }
    }

    @Override
    public int addValue(float key, int addition, int defaultValue) {
        int k = Float.floatToIntBits(key);
        int value = defaultValue + addition;
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) == FREE_BITS) {
            // key is absent
            incrementModCount();
            tab[index] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
            postInsertHook();
            return value;
        } else {
            keyPresent:
            if (cur != k) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == FREE_BITS) {
                        // key is absent
                        incrementModCount();
                        tab[bIndex] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
                        postInsertHook();
                        return value;
                    } else if (cur == k) {
                        index = bIndex;
                        break keyPresent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == FREE_BITS) {
                        // key is absent
                        incrementModCount();
                        tab[fIndex] = ((((long) k) & FLOAT_MASK) | (((long) value) << 32));
                        postInsertHook();
                        return value;
                    } else if (cur == k) {
                        index = fIndex;
                        break keyPresent;
                    }
                    step += 2;
                }
            }
            // key is present
            int newValue = (int) (entry >>> 32) + addition;
            U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
            return newValue;
        }
    }


    @Override
    public void putAll(@Nonnull Map<? extends Float, ? extends Integer> m) {
        CommonFloatIntMapOps.putAll(this, m);
    }


    @Override
    public Integer replace(Float key, Integer value) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return null;
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return null;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == k) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return null;
                    }
                    step += 2;
                }
            }
        }
        // key is present
        int oldValue = (int) (entry >>> 32);
        U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), value);
        return oldValue;
    }

    @Override
    public int replace(float key, int value) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return defaultValue();
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return defaultValue();
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == k) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return defaultValue();
                    }
                    step += 2;
                }
            }
        }
        // key is present
        int oldValue = (int) (entry >>> 32);
        U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), value);
        return oldValue;
    }

    @Override
    public boolean replace(Float key, Integer oldValue, Integer newValue) {
        return replace(key.floatValue(),
                oldValue.intValue(),
                newValue.intValue());
    }

    @Override
    public boolean replace(float key, int oldValue, int newValue) {
        int k = Float.floatToIntBits(key);
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVFloatKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
            if (cur == FREE_BITS) {
                // key is absent
                return false;
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return false;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == k) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == FREE_BITS) {
                        // key is absent
                        return false;
                    }
                    step += 2;
                }
            }
        }
        // key is present
        if ((int) (entry >>> 32) == oldValue) {
            U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void replaceAll(
            BiFunction<? super Float, ? super Integer, ? extends Integer> function) {
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
                U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), function.apply(Float.intBitsToFloat(key), (int) (entry >>> 32)));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void replaceAll(FloatIntToIntFunction function) {
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
                U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), function.applyAsInt(Float.intBitsToFloat(key), (int) (entry >>> 32)));
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
    public Integer remove(Object key) {
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
    public int remove(float key) {
        throw new java.lang.UnsupportedOperationException();
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Float) key).floatValue(),
                ((Integer) value).intValue()
                );
    }

    @Override
    public boolean remove(float key, int value) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean removeIf(FloatIntPredicate filter) {
        throw new java.lang.UnsupportedOperationException();
    }






    class EntryView extends AbstractSetView<Map.Entry<Float, Integer>>
            implements HashObjSet<Map.Entry<Float, Integer>>,
            InternalObjCollectionOps<Map.Entry<Float, Integer>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Float, Integer>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Float>defaultEquality()
                    ,
                    Equivalence.<Integer>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return UpdatableQHashParallelKVFloatIntMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return UpdatableQHashParallelKVFloatIntMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return UpdatableQHashParallelKVFloatIntMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Float, Integer> e = (Map.Entry<Float, Integer>) o;
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
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Float, Integer>> action) {
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
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Float, Integer>> predicate) {
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
        public ObjIterator<Map.Entry<Float, Integer>> iterator() {
            int mc = modCount();
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Float, Integer>> cursor() {
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
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Float, Integer>> c) {
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
            return UpdatableQHashParallelKVFloatIntMapGO.this.hashCode();
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
                    sb.append((int) (entry >>> 32));
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
            return UpdatableQHashParallelKVFloatIntMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Float, Integer> e = (Map.Entry<Float, Integer>) o;
                float key = e.getKey();
                int value = e.getValue();
                return UpdatableQHashParallelKVFloatIntMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Float, Integer>> filter) {
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
            UpdatableQHashParallelKVFloatIntMapGO.this.clear();
        }
    }


    abstract class FloatIntEntry extends AbstractEntry<Float, Integer> {

        abstract int key();

        @Override
        public final Float getKey() {
            return Float.intBitsToFloat(key());
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
            int k2;
            int v2;
            try {
                e2 = (Map.Entry) o;
                k2 = Float.floatToIntBits((Float) e2.getKey());
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


    class MutableEntry extends FloatIntEntry {
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
            U.putInt(
                    table, LONG_BASE + INT_VALUE_OFFSET + (((long) index) << LONG_SCALE_SHIFT),
                    newValue);
        }
    }



    class ReusableEntry extends FloatIntEntry {
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


    class ValueView extends AbstractIntValueView {


        @Override
        public int size() {
            return UpdatableQHashParallelKVFloatIntMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return UpdatableQHashParallelKVFloatIntMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return UpdatableQHashParallelKVFloatIntMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(int v) {
            return UpdatableQHashParallelKVFloatIntMapGO.this.containsValue(v);
        }



        @Override
        public void forEach(Consumer<? super Integer> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    action.accept((int) (entry >>> 32));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    action.accept((int) (entry >>> 32));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    if (!predicate.test((int) (entry >>> 32))) {
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
        public boolean allContainingIn(IntCollection c) {
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
        public boolean reverseAddAllTo(IntCollection c) {
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
        public boolean reverseRemoveAllFrom(IntSet s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    changed |= s.removeInt((int) (entry >>> 32));
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
            return new NoRemovedValueIterator(mc);
        }

        @Nonnull
        @Override
        public IntCursor cursor() {
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
                    result[resultIndex++] = (int) (entry >>> 32);
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
                    a[resultIndex++] = (T) Integer.valueOf((int) (entry >>> 32));
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    result[resultIndex++] = (int) (entry >>> 32);
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    a[resultIndex++] = (int) (entry >>> 32);
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
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    sb.append(' ').append((int) (entry >>> 32)).append(',');
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
            return removeInt(( Integer ) o);
        }

        @Override
        public boolean removeInt(int v) {
            return removeValue(v);
        }



        @Override
        public void clear() {
            UpdatableQHashParallelKVFloatIntMapGO.this.clear();
        }

        
        public boolean removeIf(Predicate<? super Integer> filter) {
            throw new java.lang.UnsupportedOperationException();
        }

        @Override
        public boolean removeIf(IntPredicate filter) {
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Float, Integer>> {
        final long[] tab;
        int expectedModCount;
        int nextIndex;
        MutableEntry next;

        NoRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            long[] tab = this.tab = table;
            int nextI = tab.length;
            long entry;
            while (--nextI >= 0) {
                int key;
                if ((key = (int) (entry = tab[nextI])) < FREE_BITS) {
                    next = new MutableEntry(mc, nextI, key, (int) (entry >>> 32));
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Float, Integer>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            long entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    action.accept(new MutableEntry(mc, i, key, (int) (entry >>> 32)));
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
        public Map.Entry<Float, Integer> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
                    long[] tab = this.tab;
                    MutableEntry prev = next;
                    long entry;
                    while (--nextI >= 0) {
                        int key;
                        if ((key = (int) (entry = tab[nextI])) < FREE_BITS) {
                            next = new MutableEntry(mc, nextI, key, (int) (entry >>> 32));
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


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Float, Integer>> {
        final long[] tab;
        int expectedModCount;
        int index;
        int curKey;
        int curValue;

        NoRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Float, Integer>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            long entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    action.accept(new MutableEntry(mc, i, key, (int) (entry >>> 32)));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE_BITS;
        }

        @Override
        public Map.Entry<Float, Integer> elem() {
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
            throw new java.lang.UnsupportedOperationException();
        }
    }




    class NoRemovedValueIterator implements IntIterator {
        final long[] tab;
        int expectedModCount;
        int nextIndex;
        int next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            long[] tab = this.tab = table;
            int nextI = tab.length;
            long entry;
            while (--nextI >= 0) {
                if ((int) (entry = tab[nextI]) < FREE_BITS) {
                    next = (int) (entry >>> 32);
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
                    long[] tab = this.tab;
                    int prev = next;
                    long entry;
                    while (--nextI >= 0) {
                        if ((int) (entry = tab[nextI]) < FREE_BITS) {
                            next = (int) (entry >>> 32);
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
            long[] tab = this.tab;
            long entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    action.accept((int) (entry >>> 32));
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            nextIndex = -1;
        }

        @Override
        public void forEachRemaining(IntConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            long entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    action.accept((int) (entry >>> 32));
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
        public Integer next() {
            return nextInt();
        }

        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedValueCursor implements IntCursor {
        final long[] tab;
        int expectedModCount;
        int index;
        int curKey;
        int curValue;

        NoRemovedValueCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(IntConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            long entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) < FREE_BITS) {
                    action.accept((int) (entry >>> 32));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE_BITS;
        }

        @Override
        public int elem() {
            if (curKey != FREE_BITS) {
                return curValue;
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
            throw new java.lang.UnsupportedOperationException();
        }
    }



    class NoRemovedMapCursor implements FloatIntCursor {
        final long[] tab;
        int expectedModCount;
        int index;
        int curKey;
        int curValue;

        NoRemovedMapCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            curKey = FREE_BITS;
        }

        @Override
        public void forEachForward(FloatIntConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            long entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) < FREE_BITS) {
                    action.accept(Float.intBitsToFloat(key), (int) (entry >>> 32));
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
        public int value() {
            if (curKey != FREE_BITS) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(int value) {
            if (curKey != FREE_BITS) {
                if (expectedModCount == modCount()) {
                    U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), value);
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
            throw new java.lang.UnsupportedOperationException();
        }
    }
}

