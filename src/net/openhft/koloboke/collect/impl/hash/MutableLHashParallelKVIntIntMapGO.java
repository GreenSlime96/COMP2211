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
import java.util.function.IntPredicate;
import net.openhft.koloboke.function.IntIntConsumer;
import net.openhft.koloboke.function.IntIntPredicate;
import net.openhft.koloboke.function.IntIntToIntFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class MutableLHashParallelKVIntIntMapGO
        extends MutableLHashParallelKVIntIntMapSO {

    
    final void copy(ParallelKVIntIntLHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    
    final void move(ParallelKVIntIntLHash hash) {
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
    public boolean containsEntry(int key, int value) {
        int free;
        if (key != (free = freeValue)) {
            long[] tab = table;
            int capacityMask, index;
            int cur;
            long entry;
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
                // key is present
                return (int) (entry >>> 32) == value;
            } else {
                if (cur == free) {
                    // key is absent
                    return false;
                } else {
                    while (true) {
                        if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            // key is present
                            return (int) (entry >>> 32) == value;
                        } else if (cur == free) {
                            // key is absent
                            return false;
                        }
                    }
                }
            }
        } else {
            // key is absent
            return false;
        }
    }


    @Override
    public Integer get(Object key) {
        int k = (Integer) key;
        int free;
        if (k != (free = freeValue)) {
            long[] tab = table;
            int capacityMask, index;
            int cur;
            long entry;
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
                // key is present
                return (int) (entry >>> 32);
            } else {
                if (cur == free) {
                    // key is absent
                    return null;
                } else {
                    while (true) {
                        if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                            // key is present
                            return (int) (entry >>> 32);
                        } else if (cur == free) {
                            // key is absent
                            return null;
                        }
                    }
                }
            }
        } else {
            // key is absent
            return null;
        }
    }

    

    @Override
    public int get(int key) {
        int free;
        if (key != (free = freeValue)) {
            long[] tab = table;
            int capacityMask, index;
            int cur;
            long entry;
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
                // key is present
                return (int) (entry >>> 32);
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue();
                } else {
                    while (true) {
                        if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            // key is present
                            return (int) (entry >>> 32);
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue();
                        }
                    }
                }
            }
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Integer getOrDefault(Object key, Integer defaultValue) {
        int k = (Integer) key;
        int free;
        if (k != (free = freeValue)) {
            long[] tab = table;
            int capacityMask, index;
            int cur;
            long entry;
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
                // key is present
                return (int) (entry >>> 32);
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue;
                } else {
                    while (true) {
                        if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                            // key is present
                            return (int) (entry >>> 32);
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue;
                        }
                    }
                }
            }
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public int getOrDefault(int key, int defaultValue) {
        int free;
        if (key != (free = freeValue)) {
            long[] tab = table;
            int capacityMask, index;
            int cur;
            long entry;
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
                // key is present
                return (int) (entry >>> 32);
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue;
                } else {
                    while (true) {
                        if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            // key is present
                            return (int) (entry >>> 32);
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue;
                        }
                    }
                }
            }
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public void forEach(BiConsumer<? super Integer, ? super Integer> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        int free = freeValue;
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
                action.accept(key, (int) (entry >>> 32));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void forEach(IntIntConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        int free = freeValue;
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
                action.accept(key, (int) (entry >>> 32));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public boolean forEachWhile(IntIntPredicate predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        int free = freeValue;
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
                if (!predicate.test(key, (int) (entry >>> 32))) {
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
    public IntIntCursor cursor() {
        int mc = modCount();
        return new NoRemovedMapCursor(mc);
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonIntIntMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalIntIntMapOps m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        int free = freeValue;
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
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
    public void reversePutAllTo(InternalIntIntMapOps m) {
        if (isEmpty())
            return;
        int mc = modCount();
        int free = freeValue;
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
                m.justPut(key, (int) (entry >>> 32));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<Integer, Integer>> entrySet() {
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
        int free = freeValue;
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
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
        int free = freeValue;
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
                sb.append(' ');
                sb.append(key);
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
        int free = freeValue;
        long[] tab = table;
        long entry;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        long[] newTab = table;
        int capacityMask = newTab.length - 1;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
                int index;
                if (U.getInt(newTab, LONG_BASE + INT_KEY_OFFSET + (((long) (index = ParallelKVIntKeyMixing.mix(key) & capacityMask)) << LONG_SCALE_SHIFT)) != free) {
                    while (true) {
                        if (U.getInt(newTab, LONG_BASE + INT_KEY_OFFSET + (((long) ((index = (index - 1) & capacityMask))) << LONG_SCALE_SHIFT)) == free) {
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
    public Integer put(Integer key, Integer value) {
        int k = key;
        int free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = ((((long) k) & INT_MASK) | (((long) value) << 32));
            postInsertHook();
            return null;
        } else {
            keyPresent:
            if (cur != k) {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[index] = ((((long) k) & INT_MASK) | (((long) value) << 32));
                        postInsertHook();
                        return null;
                    } else if (cur == k) {
                        break keyPresent;
                    }
                }
            }
            // key is present
            int prevValue = (int) (entry >>> 32);
            U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), value);
            return prevValue;
        }
    }

    @Override
    public int put(int key, int value) {
        int free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = ((((long) key) & INT_MASK) | (((long) value) << 32));
            postInsertHook();
            return defaultValue();
        } else {
            keyPresent:
            if (cur != key) {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[index] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                        postInsertHook();
                        return defaultValue();
                    } else if (cur == key) {
                        break keyPresent;
                    }
                }
            }
            // key is present
            int prevValue = (int) (entry >>> 32);
            U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), value);
            return prevValue;
        }
    }

    @Override
    public Integer putIfAbsent(Integer key, Integer value) {
        int k = key;
        int free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = ((((long) k) & INT_MASK) | (((long) value) << 32));
            postInsertHook();
            return null;
        } else {
            if (cur == k) {
                // key is present
                return (int) (entry >>> 32);
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[index] = ((((long) k) & INT_MASK) | (((long) value) << 32));
                        postInsertHook();
                        return null;
                    } else if (cur == k) {
                        // key is present
                        return (int) (entry >>> 32);
                    }
                }
            }
        }
    }

    @Override
    public int putIfAbsent(int key, int value) {
        int free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = ((((long) key) & INT_MASK) | (((long) value) << 32));
            postInsertHook();
            return defaultValue();
        } else {
            if (cur == key) {
                // key is present
                return (int) (entry >>> 32);
            } else {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[index] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                        postInsertHook();
                        return defaultValue();
                    } else if (cur == key) {
                        // key is present
                        return (int) (entry >>> 32);
                    }
                }
            }
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
    public Integer compute(Integer key,
            BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        int k = key;
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) != k) {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        break keyPresent;
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            Integer newValue = remappingFunction.apply(k, null);
            if (newValue != null) {
                incrementModCount();
                tab[index] = ((((long) k) & INT_MASK) | (((long) newValue) << 32));
                postInsertHook();
                return newValue;
            } else {
                return null;
            }
        }
        // key is present
        Integer newValue = remappingFunction.apply(k, (int) (entry >>> 32));
        if (newValue != null) {
            U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
            return newValue;
        } else {
            removeAt(index);
            return null;
        }
    }


    @Override
    public int compute(int key, IntIntToIntFunction remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) != key) {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                        break keyPresent;
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            int newValue = remappingFunction.applyAsInt(key, defaultValue());
            incrementModCount();
            tab[index] = ((((long) key) & INT_MASK) | (((long) newValue) << 32));
            postInsertHook();
            return newValue;
        }
        // key is present
        int newValue = remappingFunction.applyAsInt(key, (int) (entry >>> 32));
        U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
        return newValue;
    }


    @Override
    public Integer computeIfAbsent(Integer key,
            Function<? super Integer, ? extends Integer> mappingFunction) {
        int k = key;
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        int free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
            // key is present
            return (int) (entry >>> 32);
        } else {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        // key is present
                        return (int) (entry >>> 32);
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            Integer value = mappingFunction.apply(k);
            if (value != null) {
                incrementModCount();
                tab[index] = ((((long) k) & INT_MASK) | (((long) value) << 32));
                postInsertHook();
                return value;
            } else {
                return null;
            }
        }
    }


    @Override
    public int computeIfAbsent(int key, IntUnaryOperator mappingFunction) {
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        int free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
            // key is present
            return (int) (entry >>> 32);
        } else {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                        // key is present
                        return (int) (entry >>> 32);
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            int value = mappingFunction.applyAsInt(key);
            incrementModCount();
            tab[index] = ((((long) key) & INT_MASK) | (((long) value) << 32));
            postInsertHook();
            return value;
        }
    }


    @Override
    public Integer computeIfPresent(Integer key,
            BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        int k = key;
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int free;
        if (k != (free = freeValue)) {
            long[] tab = table;
            int capacityMask, index;
            int cur;
            long entry;
            keyPresent:
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) != k) {
                if (cur == free) {
                    // key is absent
                    return null;
                } else {
                    while (true) {
                        if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return null;
                        }
                    }
                }
            }
            // key is present
            Integer newValue = remappingFunction.apply(k, (int) (entry >>> 32));
            if (newValue != null) {
                U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
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
    public int computeIfPresent(int key, IntIntToIntFunction remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int free;
        if (key != (free = freeValue)) {
            long[] tab = table;
            int capacityMask, index;
            int cur;
            long entry;
            keyPresent:
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) != key) {
                if (cur == free) {
                    // key is absent
                    return defaultValue();
                } else {
                    while (true) {
                        if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue();
                        }
                    }
                }
            }
            // key is present
            int newValue = remappingFunction.applyAsInt(key, (int) (entry >>> 32));
            U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
            return newValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Integer merge(Integer key, Integer value,
            BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        int k = key;
        if (value == null)
            throw new java.lang.NullPointerException();
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) != k) {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        break keyPresent;
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            incrementModCount();
            tab[index] = ((((long) k) & INT_MASK) | (((long) value) << 32));
            postInsertHook();
            return value;
        }
        // key is present
        Integer newValue = remappingFunction.apply((int) (entry >>> 32), value);
        if (newValue != null) {
            U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
            return newValue;
        } else {
            removeAt(index);
            return null;
        }
    }


    @Override
    public int merge(int key, int value, IntBinaryOperator remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) != key) {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                        break keyPresent;
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            incrementModCount();
            tab[index] = ((((long) key) & INT_MASK) | (((long) value) << 32));
            postInsertHook();
            return value;
        }
        // key is present
        int newValue = remappingFunction.applyAsInt((int) (entry >>> 32), value);
        U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
        return newValue;
    }


    @Override
    public int addValue(int key, int value) {
        int free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = ((((long) key) & INT_MASK) | (((long) value) << 32));
            postInsertHook();
            return value;
        } else {
            keyPresent:
            if (cur != key) {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[index] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                        postInsertHook();
                        return value;
                    } else if (cur == key) {
                        break keyPresent;
                    }
                }
            }
            // key is present
            int newValue = (int) (entry >>> 32) + value;
            U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
            return newValue;
        }
    }

    @Override
    public int addValue(int key, int addition, int defaultValue) {
        int value = defaultValue + addition;
        int free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        long[] tab = table;
        int capacityMask, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = ((((long) key) & INT_MASK) | (((long) value) << 32));
            postInsertHook();
            return value;
        } else {
            keyPresent:
            if (cur != key) {
                while (true) {
                    if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[index] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                        postInsertHook();
                        return value;
                    } else if (cur == key) {
                        break keyPresent;
                    }
                }
            }
            // key is present
            int newValue = (int) (entry >>> 32) + addition;
            U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
            return newValue;
        }
    }


    @Override
    public void putAll(@Nonnull Map<? extends Integer, ? extends Integer> m) {
        CommonIntIntMapOps.putAll(this, m);
    }


    @Override
    public Integer replace(Integer key, Integer value) {
        int k = key;
        int free;
        if (k != (free = freeValue)) {
            long[] tab = table;
            int capacityMask, index;
            int cur;
            long entry;
            keyPresent:
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) != k) {
                if (cur == free) {
                    // key is absent
                    return null;
                } else {
                    while (true) {
                        if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return null;
                        }
                    }
                }
            }
            // key is present
            int oldValue = (int) (entry >>> 32);
            U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), value);
            return oldValue;
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public int replace(int key, int value) {
        int free;
        if (key != (free = freeValue)) {
            long[] tab = table;
            int capacityMask, index;
            int cur;
            long entry;
            keyPresent:
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) != key) {
                if (cur == free) {
                    // key is absent
                    return defaultValue();
                } else {
                    while (true) {
                        if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue();
                        }
                    }
                }
            }
            // key is present
            int oldValue = (int) (entry >>> 32);
            U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), value);
            return oldValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public boolean replace(Integer key, Integer oldValue, Integer newValue) {
        return replace(key.intValue(),
                oldValue.intValue(),
                newValue.intValue());
    }

    @Override
    public boolean replace(int key, int oldValue, int newValue) {
        int free;
        if (key != (free = freeValue)) {
            long[] tab = table;
            int capacityMask, index;
            int cur;
            long entry;
            keyPresent:
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) != key) {
                if (cur == free) {
                    // key is absent
                    return false;
                } else {
                    while (true) {
                        if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return false;
                        }
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
        } else {
            // key is absent
            return false;
        }
    }


    @Override
    public void replaceAll(
            BiFunction<? super Integer, ? super Integer, ? extends Integer> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        int free = freeValue;
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
                U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), function.apply(key, (int) (entry >>> 32)));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void replaceAll(IntIntToIntFunction function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        int free = freeValue;
        long[] tab = table;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
                U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), function.applyAsInt(key, (int) (entry >>> 32)));
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
        int free = freeValue;
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
            if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                break;
            }
            if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
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
        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
        postRemoveHook();
    }

    @Override
    public Integer remove(Object key) {
        int k = (Integer) key;
        int free;
        if (k != (free = freeValue)) {
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int index;
            int cur;
            long entry;
            keyPresent:
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) & capacityMask])) != k) {
                if (cur == free) {
                    // key is absent
                    return null;
                } else {
                    while (true) {
                        if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return null;
                        }
                    }
                }
            }
            // key is present
            int val = (int) (entry >>> 32);
            incrementModCount();
            int indexToRemove = index;
            int indexToShift = indexToRemove;
            int shiftDistance = 1;
            while (true) {
                indexToShift = (indexToShift - 1) & capacityMask;
                int keyToShift;
                if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                    break;
                }
                if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
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
            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
            postRemoveHook();
            return val;
        } else {
            // key is absent
            return null;
        }
    }


    @Override
    public boolean justRemove(int key) {
        int free;
        if (key != (free = freeValue)) {
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int index;
            int cur;
            long entry;
            keyPresent:
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) & capacityMask])) != key) {
                if (cur == free) {
                    // key is absent
                    return false;
                } else {
                    while (true) {
                        if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            break keyPresent;
                        } else if (cur == free) {
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
                if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                    break;
                }
                if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
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
            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
            postRemoveHook();
            return true;
        } else {
            // key is absent
            return false;
        }
    }



    

    @Override
    public int remove(int key) {
        int free;
        if (key != (free = freeValue)) {
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int index;
            int cur;
            long entry;
            keyPresent:
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) & capacityMask])) != key) {
                if (cur == free) {
                    // key is absent
                    return defaultValue();
                } else {
                    while (true) {
                        if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue();
                        }
                    }
                }
            }
            // key is present
            int val = (int) (entry >>> 32);
            incrementModCount();
            int indexToRemove = index;
            int indexToShift = indexToRemove;
            int shiftDistance = 1;
            while (true) {
                indexToShift = (indexToShift - 1) & capacityMask;
                int keyToShift;
                if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                    break;
                }
                if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
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
            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
            postRemoveHook();
            return val;
        } else {
            // key is absent
            return defaultValue();
        }
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Integer) key).intValue(),
                ((Integer) value).intValue()
                );
    }

    @Override
    public boolean remove(int key, int value) {
        int free;
        if (key != (free = freeValue)) {
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int index;
            int cur;
            long entry;
            keyPresent:
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) & capacityMask])) != key) {
                if (cur == free) {
                    // key is absent
                    return false;
                } else {
                    while (true) {
                        if ((cur = (int) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return false;
                        }
                    }
                }
            }
            // key is present
            if ((int) (entry >>> 32) == value) {
                incrementModCount();
                int indexToRemove = index;
                int indexToShift = indexToRemove;
                int shiftDistance = 1;
                while (true) {
                    indexToShift = (indexToShift - 1) & capacityMask;
                    int keyToShift;
                    if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                        break;
                    }
                    if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
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
                U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
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
    public boolean removeIf(IntIntPredicate filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        int free = freeValue;
        long[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        int delayedRemoved = 0;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
                if (filter.test(key, (int) (entry >>> 32))) {
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
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), key);
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
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), delayedRemoved);
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }




    // under this condition - operations, overridden from MutableParallelKVIntLHashGO
    // when values are objects - in order to set values to null on removing (for garbage collection)
    // when algo is LHash - because shift deletion should shift values to

    @Override
    public boolean removeIf(Predicate<? super Integer> filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        int free = freeValue;
        long[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        int delayedRemoved = 0;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
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
                            int keyToShift;
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), key);
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
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), delayedRemoved);
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
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
        int free = freeValue;
        long[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        int delayedRemoved = 0;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
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
                            int keyToShift;
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), key);
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
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), delayedRemoved);
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    @Override
    public boolean removeAll(@Nonnull HashIntSet thisC, @Nonnull Collection<?> c) {
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        int free = freeValue;
        long[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        int delayedRemoved = 0;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
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
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), key);
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
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), delayedRemoved);
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    @Override
    boolean removeAll(@Nonnull HashIntSet thisC, @Nonnull IntCollection c) {
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        int free = freeValue;
        long[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        int delayedRemoved = 0;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
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
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), key);
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
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), delayedRemoved);
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }


    @Override
    public boolean retainAll(@Nonnull HashIntSet thisC, @Nonnull Collection<?> c) {
        if (c instanceof IntCollection)
            return retainAll(thisC, (IntCollection) c);
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
        int free = freeValue;
        long[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        int delayedRemoved = 0;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
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
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), key);
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
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), delayedRemoved);
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }

    private boolean retainAll(@Nonnull HashIntSet thisC, @Nonnull IntCollection c) {
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
        int free = freeValue;
        long[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        int delayedRemoved = 0;
        long entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            int key;
            if ((key = (int) (entry = tab[i])) != free) {
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
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), key);
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
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), delayedRemoved);
                    }
                    changed = true;
                }
            }
        }
        if (firstDelayedRemoved >= 0) {
            closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
        return changed;
    }


    @Override
    void closeDelayedRemoved(int firstDelayedRemoved
            , int delayedRemoved) {
        int free = freeValue;
        long[] tab = table;
        int capacityMask = tab.length - 1;
        long entry;
        for (int i = firstDelayedRemoved; i >= 0; i--) {
            if ((int) (entry = tab[i]) == delayedRemoved) {
                int indexToRemove = i;
                int indexToShift = indexToRemove;
                int shiftDistance = 1;
                while (true) {
                    indexToShift = (indexToShift - 1) & capacityMask;
                    int keyToShift;
                    if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                        break;
                    }
                    if ((keyToShift != delayedRemoved) && (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance)) {
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
                U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                postRemoveHook();
            }
        }
    }



    @Override
    public IntIterator iterator() {
        int mc = modCount();
        return new NoRemovedKeyIterator(mc);
    }

    @Override
    public IntCursor setCursor() {
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
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 1) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putInt(this.tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.nextIndex = index;
                                        if (indexToShift < index - 1) {
                                            this.next = keyToShift;
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
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        justRemove(U.getInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT)));
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
            int free;
            if ((curKey = this.curKey) != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
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
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putInt(this.tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
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
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
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





    class EntryView extends AbstractSetView<Map.Entry<Integer, Integer>>
            implements HashObjSet<Map.Entry<Integer, Integer>>,
            InternalObjCollectionOps<Map.Entry<Integer, Integer>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Integer, Integer>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Integer>defaultEquality()
                    ,
                    Equivalence.<Integer>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return MutableLHashParallelKVIntIntMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return MutableLHashParallelKVIntIntMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return MutableLHashParallelKVIntIntMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Integer, Integer> e = (Map.Entry<Integer, Integer>) o;
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
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
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Integer, Integer>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    action.accept(new MutableEntry(mc, i, key, (int) (entry >>> 32)));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Integer, Integer>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
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
        public ObjIterator<Map.Entry<Integer, Integer>> iterator() {
            int mc = modCount();
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Integer, Integer>> cursor() {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    changed |= s.remove(e.with(key, (int) (entry >>> 32)));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Integer, Integer>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    changed |= c.add(new MutableEntry(mc, i, key, (int) (entry >>> 32)));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        public int hashCode() {
            return MutableLHashParallelKVIntIntMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    sb.append(' ');
                    sb.append(key);
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
            return MutableLHashParallelKVIntIntMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Integer, Integer> e = (Map.Entry<Integer, Integer>) o;
                int key = e.getKey();
                int value = e.getValue();
                return MutableLHashParallelKVIntIntMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Integer, Integer>> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            int free = freeValue;
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            int delayedRemoved = 0;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
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
                                if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                    break;
                                }
                                if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), key);
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
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                            postRemoveHook();
                        } else {
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), delayedRemoved);
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
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
            int free = freeValue;
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            int delayedRemoved = 0;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
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
                                if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                    break;
                                }
                                if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), key);
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
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                            postRemoveHook();
                        } else {
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), delayedRemoved);
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
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
            int free = freeValue;
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            int delayedRemoved = 0;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
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
                                if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                    break;
                                }
                                if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), key);
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
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                            postRemoveHook();
                        } else {
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), delayedRemoved);
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public void clear() {
            MutableLHashParallelKVIntIntMapGO.this.clear();
        }
    }


    abstract class IntIntEntry extends AbstractEntry<Integer, Integer> {

        abstract int key();

        @Override
        public final Integer getKey() {
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
            int k2;
            int v2;
            try {
                e2 = (Map.Entry) o;
                k2 = (Integer) e2.getKey();
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


    class MutableEntry extends IntIntEntry {
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



    class ReusableEntry extends IntIntEntry {
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
            return MutableLHashParallelKVIntIntMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return MutableLHashParallelKVIntIntMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return MutableLHashParallelKVIntIntMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(int v) {
            return MutableLHashParallelKVIntIntMapGO.this.containsValue(v);
        }



        @Override
        public void forEach(Consumer<? super Integer> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            int free = freeValue;
            long[] tab = table;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
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
            MutableLHashParallelKVIntIntMapGO.this.clear();
        }

        
        public boolean removeIf(Predicate<? super Integer> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            int free = freeValue;
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            int delayedRemoved = 0;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    if (filter.test((int) (entry >>> 32))) {
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
                                if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                    break;
                                }
                                if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), key);
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
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                            postRemoveHook();
                        } else {
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), delayedRemoved);
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
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
            int free = freeValue;
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            int delayedRemoved = 0;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    if (filter.test((int) (entry >>> 32))) {
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
                                if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                    break;
                                }
                                if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), key);
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
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                            postRemoveHook();
                        } else {
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), delayedRemoved);
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
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
            int free = freeValue;
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            int delayedRemoved = 0;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
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
                                if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                    break;
                                }
                                if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), key);
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
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                            postRemoveHook();
                        } else {
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), delayedRemoved);
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
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
            int free = freeValue;
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            int delayedRemoved = 0;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
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
                                if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                    break;
                                }
                                if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), key);
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
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                            postRemoveHook();
                        } else {
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), delayedRemoved);
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
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
            int free = freeValue;
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            int delayedRemoved = 0;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
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
                                if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                    break;
                                }
                                if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), key);
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
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                            postRemoveHook();
                        } else {
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), delayedRemoved);
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
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
            int free = freeValue;
            long[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            int delayedRemoved = 0;
            long entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
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
                                if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                    break;
                                }
                                if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), key);
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
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                            postRemoveHook();
                        } else {
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), delayedRemoved);
                        }
                        changed = true;
                    }
                }
            }
            if (firstDelayedRemoved >= 0) {
                closeDelayedRemoved(firstDelayedRemoved, delayedRemoved);
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

    }



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Integer, Integer>> {
        long[] tab;
        final int free;
        final int capacityMask;
        int expectedModCount;
        
        class MutableEntry2 extends MutableEntry {
            MutableEntry2(int modCount, int index, int key, int value) {
                super(modCount, index, key, value);
            }
            
            @Override
            void updateValueInTable(int newValue) {
                if (tab == table) {
                    U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
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
            int free = this.free = freeValue;
            int nextI = tab.length;
            long entry;
            while (--nextI >= 0) {
                int key;
                if ((key = (int) (entry = tab[nextI])) != free) {
                    next = new MutableEntry2(mc, nextI, key, (int) (entry >>> 32));
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Integer, Integer>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            int free = this.free;
            long entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
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
        public Map.Entry<Integer, Integer> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
                    index = nextI;
                    long[] tab = this.tab;
                    int free = this.free;
                    MutableEntry prev = next;
                    long entry;
                    while (--nextI >= 0) {
                        int key;
                        if ((key = (int) (entry = tab[nextI])) != free) {
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
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 1) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putInt(this.tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
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
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        justRemove(U.getInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT)));
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Integer, Integer>> {
        long[] tab;
        final int free;
        final int capacityMask;
        int expectedModCount;
        
        class MutableEntry2 extends MutableEntry {
            MutableEntry2(int modCount, int index, int key, int value) {
                super(modCount, index, key, value);
            }
            
            @Override
            void updateValueInTable(int newValue) {
                if (tab == table) {
                    U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
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
            int free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Integer, Integer>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            int free = this.free;
            long entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    action.accept(new MutableEntry2(mc, i, key, (int) (entry >>> 32)));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public Map.Entry<Integer, Integer> elem() {
            int curKey;
            if ((curKey = this.curKey) != free) {
                return new MutableEntry2(expectedModCount, index, curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                long[] tab = this.tab;
                int free = this.free;
                long entry;
                for (int i = index - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free) {
                        index = i;
                        curKey = key;
                        curValue = (int) (entry >>> 32);
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
            int curKey;
            int free;
            if ((curKey = this.curKey) != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
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
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putInt(this.tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
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
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
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




    class NoRemovedValueIterator implements IntIterator {
        long[] tab;
        final int free;
        final int capacityMask;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        int next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            long[] tab = this.tab = table;
            capacityMask = tab.length - 1;
            int free = this.free = freeValue;
            int nextI = tab.length;
            long entry;
            while (--nextI >= 0) {
                if ((int) (entry = tab[nextI]) != free) {
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
                    index = nextI;
                    long[] tab = this.tab;
                    int free = this.free;
                    int prev = next;
                    long entry;
                    while (--nextI >= 0) {
                        if ((int) (entry = tab[nextI]) != free) {
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
            int free = this.free;
            long entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
                    action.accept((int) (entry >>> 32));
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
            long[] tab = this.tab;
            int free = this.free;
            long entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
                    action.accept((int) (entry >>> 32));
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
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 1) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putInt(this.tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.nextIndex = index;
                                        if (indexToShift < index - 1) {
                                            this.next = (int) (entry >>> 32);
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
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        justRemove(U.getInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT)));
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedValueCursor implements IntCursor {
        long[] tab;
        final int free;
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
            int free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(IntConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            int free = this.free;
            long entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                if ((int) (entry = tab[i]) != free) {
                    action.accept((int) (entry >>> 32));
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
                long[] tab = this.tab;
                int free = this.free;
                long entry;
                for (int i = index - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free) {
                        index = i;
                        curKey = key;
                        curValue = (int) (entry >>> 32);
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
            int curKey;
            int free;
            if ((curKey = this.curKey) != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
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
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putInt(this.tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
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
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
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



    class NoRemovedMapCursor implements IntIntCursor {
        long[] tab;
        final int free;
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
            int free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(IntIntConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            int free = this.free;
            long entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    action.accept(key, (int) (entry >>> 32));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public int key() {
            int curKey;
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
                    U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), value);
                    if (tab != table) {
                        U.putInt(table, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), value);
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
                int free = this.free;
                long entry;
                for (int i = index - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free) {
                        index = i;
                        curKey = key;
                        curValue = (int) (entry >>> 32);
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
            int curKey;
            int free;
            if ((curKey = this.curKey) != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
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
                            if ((keyToShift = (int) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVIntKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putInt(this.tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
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
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (indexToRemove)) << LONG_SCALE_SHIFT), free);
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

