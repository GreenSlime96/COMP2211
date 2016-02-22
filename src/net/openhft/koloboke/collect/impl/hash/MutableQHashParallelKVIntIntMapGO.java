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


public class MutableQHashParallelKVIntIntMapGO
        extends MutableQHashParallelKVIntIntMapSO {

    
    final void copy(ParallelKVIntIntQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    
    final void move(ParallelKVIntIntQHash hash) {
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
        if (key != (free = freeValue) && key != removedValue) {
            long[] tab = table;
            int capacity, index;
            int cur;
            long entry;
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) % (capacity = tab.length)])) == key) {
                // key is present
                return (int) (entry >>> 32) == value;
            } else {
                if (cur == free) {
                    // key is absent, free slot
                    return false;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (int) (entry = tab[bIndex])) == key) {
                            // key is present
                            return (int) (entry >>> 32) == value;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return false;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == key) {
                            // key is present
                            return (int) (entry >>> 32) == value;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return false;
                        }
                        step += 2;
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
        if (k != (free = freeValue) && k != removedValue) {
            long[] tab = table;
            int capacity, index;
            int cur;
            long entry;
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) % (capacity = tab.length)])) == k) {
                // key is present
                return (int) (entry >>> 32);
            } else {
                if (cur == free) {
                    // key is absent, free slot
                    return null;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (int) (entry = tab[bIndex])) == k) {
                            // key is present
                            return (int) (entry >>> 32);
                        } else if (cur == free) {
                            // key is absent, free slot
                            return null;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == k) {
                            // key is present
                            return (int) (entry >>> 32);
                        } else if (cur == free) {
                            // key is absent, free slot
                            return null;
                        }
                        step += 2;
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
        if (key != (free = freeValue) && key != removedValue) {
            long[] tab = table;
            int capacity, index;
            int cur;
            long entry;
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) % (capacity = tab.length)])) == key) {
                // key is present
                return (int) (entry >>> 32);
            } else {
                if (cur == free) {
                    // key is absent, free slot
                    return defaultValue();
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (int) (entry = tab[bIndex])) == key) {
                            // key is present
                            return (int) (entry >>> 32);
                        } else if (cur == free) {
                            // key is absent, free slot
                            return defaultValue();
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == key) {
                            // key is present
                            return (int) (entry >>> 32);
                        } else if (cur == free) {
                            // key is absent, free slot
                            return defaultValue();
                        }
                        step += 2;
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
        if (k != (free = freeValue) && k != removedValue) {
            long[] tab = table;
            int capacity, index;
            int cur;
            long entry;
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) % (capacity = tab.length)])) == k) {
                // key is present
                return (int) (entry >>> 32);
            } else {
                if (cur == free) {
                    // key is absent, free slot
                    return defaultValue;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (int) (entry = tab[bIndex])) == k) {
                            // key is present
                            return (int) (entry >>> 32);
                        } else if (cur == free) {
                            // key is absent, free slot
                            return defaultValue;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == k) {
                            // key is present
                            return (int) (entry >>> 32);
                        } else if (cur == free) {
                            // key is absent, free slot
                            return defaultValue;
                        }
                        step += 2;
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
        if (key != (free = freeValue) && key != removedValue) {
            long[] tab = table;
            int capacity, index;
            int cur;
            long entry;
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) % (capacity = tab.length)])) == key) {
                // key is present
                return (int) (entry >>> 32);
            } else {
                if (cur == free) {
                    // key is absent, free slot
                    return defaultValue;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (int) (entry = tab[bIndex])) == key) {
                            // key is present
                            return (int) (entry >>> 32);
                        } else if (cur == free) {
                            // key is absent, free slot
                            return defaultValue;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == key) {
                            // key is present
                            return (int) (entry >>> 32);
                        } else if (cur == free) {
                            // key is absent, free slot
                            return defaultValue;
                        }
                        step += 2;
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
        int removed = removedValue;
        long[] tab = table;
        long entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    action.accept(key, (int) (entry >>> 32));
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free && key != removed) {
                    action.accept(key, (int) (entry >>> 32));
                }
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
        int removed = removedValue;
        long[] tab = table;
        long entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    action.accept(key, (int) (entry >>> 32));
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free && key != removed) {
                    action.accept(key, (int) (entry >>> 32));
                }
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
        int removed = removedValue;
        long[] tab = table;
        long entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    if (!predicate.test(key, (int) (entry >>> 32))) {
                        terminated = true;
                        break;
                    }
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free && key != removed) {
                    if (!predicate.test(key, (int) (entry >>> 32))) {
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
    public IntIntCursor cursor() {
        int mc = modCount();
        if (!noRemoved())
            return new SomeRemovedMapCursor(mc);
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
        int removed = removedValue;
        long[] tab = table;
        long entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    if (!m.containsEntry(key, (int) (entry >>> 32))) {
                        containsAll = false;
                        break;
                    }
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free && key != removed) {
                    if (!m.containsEntry(key, (int) (entry >>> 32))) {
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
    public void reversePutAllTo(InternalIntIntMapOps m) {
        if (isEmpty())
            return;
        int mc = modCount();
        int free = freeValue;
        int removed = removedValue;
        long[] tab = table;
        long entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    m.justPut(key, (int) (entry >>> 32));
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free && key != removed) {
                    m.justPut(key, (int) (entry >>> 32));
                }
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
        int removed = removedValue;
        long[] tab = table;
        long entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    hashCode += key ^ (int) (entry >>> 32);
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free && key != removed) {
                    hashCode += key ^ (int) (entry >>> 32);
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
        int free = freeValue;
        int removed = removedValue;
        long[] tab = table;
        long entry;
        if (noRemoved()) {
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
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free && key != removed) {
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
        int removed = removedValue;
        long[] tab = table;
        long entry;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        long[] newTab = table;
        int capacity = newTab.length;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    int index;
                    if (U.getInt(newTab, LONG_BASE + INT_KEY_OFFSET + (((long) (index = ParallelKVIntKeyMixing.mix(key) % capacity)) << LONG_SCALE_SHIFT)) != free) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if (U.getInt(newTab, LONG_BASE + INT_KEY_OFFSET + (((long) (bIndex)) << LONG_SCALE_SHIFT)) == free) {
                                index = bIndex;
                                break;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if (U.getInt(newTab, LONG_BASE + INT_KEY_OFFSET + (((long) (fIndex)) << LONG_SCALE_SHIFT)) == free) {
                                index = fIndex;
                                break;
                            }
                            step += 2;
                        }
                    }
                    newTab[index] = entry;
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free && key != removed) {
                    int index;
                    if (U.getInt(newTab, LONG_BASE + INT_KEY_OFFSET + (((long) (index = ParallelKVIntKeyMixing.mix(key) % capacity)) << LONG_SCALE_SHIFT)) != free) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if (U.getInt(newTab, LONG_BASE + INT_KEY_OFFSET + (((long) (bIndex)) << LONG_SCALE_SHIFT)) == free) {
                                index = bIndex;
                                break;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if (U.getInt(newTab, LONG_BASE + INT_KEY_OFFSET + (((long) (fIndex)) << LONG_SCALE_SHIFT)) == free) {
                                index = fIndex;
                                break;
                            }
                            step += 2;
                        }
                    }
                    newTab[index] = entry;
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }


    @Override
    public Integer put(Integer key, Integer value) {
        int k = key;
        int free;
        int removed = removedValue;
        if (k == (free = freeValue)) {
            free = changeFree();
        } else if (k == removed) {
            removed = changeRemoved();
        }
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) % (capacity = tab.length)])) == free) {
            // key is absent, free slot
            incrementModCount();
            tab[index] = ((((long) k) & INT_MASK) | (((long) value) << 32));
            postFreeSlotInsertHook();
            return null;
        } else {
            keyPresent:
            if (cur != k) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (int) (entry = tab[bIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[bIndex] = ((((long) k) & INT_MASK) | (((long) value) << 32));
                                postFreeSlotInsertHook();
                                return null;
                            } else if (cur == k) {
                                index = bIndex;
                                break keyPresent;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (int) (entry = tab[fIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[fIndex] = ((((long) k) & INT_MASK) | (((long) value) << 32));
                                postFreeSlotInsertHook();
                                return null;
                            } else if (cur == k) {
                                index = fIndex;
                                break keyPresent;
                            }
                            step += 2;
                        }
                    } else {
                        firstRemoved = -1;
                    }
                } else {
                    firstRemoved = index;
                }
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[bIndex] = ((((long) k) & INT_MASK) | (((long) value) << 32));
                            postFreeSlotInsertHook();
                            return null;
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = ((((long) k) & INT_MASK) | (((long) value) << 32));
                            postRemovedSlotInsertHook();
                            return null;
                        }
                    } else if (cur == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == removed && firstRemoved < 0) {
                        firstRemoved = bIndex;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[fIndex] = ((((long) k) & INT_MASK) | (((long) value) << 32));
                            postFreeSlotInsertHook();
                            return null;
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = ((((long) k) & INT_MASK) | (((long) value) << 32));
                            postRemovedSlotInsertHook();
                            return null;
                        }
                    } else if (cur == k) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == removed && firstRemoved < 0) {
                        firstRemoved = fIndex;
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
    public int put(int key, int value) {
        int free;
        int removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) % (capacity = tab.length)])) == free) {
            // key is absent, free slot
            incrementModCount();
            tab[index] = ((((long) key) & INT_MASK) | (((long) value) << 32));
            postFreeSlotInsertHook();
            return defaultValue();
        } else {
            keyPresent:
            if (cur != key) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (int) (entry = tab[bIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[bIndex] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                                postFreeSlotInsertHook();
                                return defaultValue();
                            } else if (cur == key) {
                                index = bIndex;
                                break keyPresent;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (int) (entry = tab[fIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[fIndex] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                                postFreeSlotInsertHook();
                                return defaultValue();
                            } else if (cur == key) {
                                index = fIndex;
                                break keyPresent;
                            }
                            step += 2;
                        }
                    } else {
                        firstRemoved = -1;
                    }
                } else {
                    firstRemoved = index;
                }
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[bIndex] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                            postFreeSlotInsertHook();
                            return defaultValue();
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                            postRemovedSlotInsertHook();
                            return defaultValue();
                        }
                    } else if (cur == key) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == removed && firstRemoved < 0) {
                        firstRemoved = bIndex;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[fIndex] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                            postFreeSlotInsertHook();
                            return defaultValue();
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                            postRemovedSlotInsertHook();
                            return defaultValue();
                        }
                    } else if (cur == key) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == removed && firstRemoved < 0) {
                        firstRemoved = fIndex;
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
    public Integer putIfAbsent(Integer key, Integer value) {
        int k = key;
        int free;
        int removed = removedValue;
        if (k == (free = freeValue)) {
            free = changeFree();
        } else if (k == removed) {
            removed = changeRemoved();
        }
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) % (capacity = tab.length)])) == free) {
            // key is absent, free slot
            incrementModCount();
            tab[index] = ((((long) k) & INT_MASK) | (((long) value) << 32));
            postFreeSlotInsertHook();
            return null;
        } else {
            if (cur == k) {
                // key is present
                return (int) (entry >>> 32);
            } else {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (int) (entry = tab[bIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[bIndex] = ((((long) k) & INT_MASK) | (((long) value) << 32));
                                postFreeSlotInsertHook();
                                return null;
                            } else if (cur == k) {
                                // key is present
                                return (int) (entry >>> 32);
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (int) (entry = tab[fIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[fIndex] = ((((long) k) & INT_MASK) | (((long) value) << 32));
                                postFreeSlotInsertHook();
                                return null;
                            } else if (cur == k) {
                                // key is present
                                return (int) (entry >>> 32);
                            }
                            step += 2;
                        }
                    } else {
                        firstRemoved = -1;
                    }
                } else {
                    firstRemoved = index;
                }
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[bIndex] = ((((long) k) & INT_MASK) | (((long) value) << 32));
                            postFreeSlotInsertHook();
                            return null;
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = ((((long) k) & INT_MASK) | (((long) value) << 32));
                            postRemovedSlotInsertHook();
                            return null;
                        }
                    } else if (cur == k) {
                        // key is present
                        return (int) (entry >>> 32);
                    } else if (cur == removed && firstRemoved < 0) {
                        firstRemoved = bIndex;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[fIndex] = ((((long) k) & INT_MASK) | (((long) value) << 32));
                            postFreeSlotInsertHook();
                            return null;
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = ((((long) k) & INT_MASK) | (((long) value) << 32));
                            postRemovedSlotInsertHook();
                            return null;
                        }
                    } else if (cur == k) {
                        // key is present
                        return (int) (entry >>> 32);
                    } else if (cur == removed && firstRemoved < 0) {
                        firstRemoved = fIndex;
                    }
                    step += 2;
                }
            }
        }
    }

    @Override
    public int putIfAbsent(int key, int value) {
        int free;
        int removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) % (capacity = tab.length)])) == free) {
            // key is absent, free slot
            incrementModCount();
            tab[index] = ((((long) key) & INT_MASK) | (((long) value) << 32));
            postFreeSlotInsertHook();
            return defaultValue();
        } else {
            if (cur == key) {
                // key is present
                return (int) (entry >>> 32);
            } else {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (int) (entry = tab[bIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[bIndex] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                                postFreeSlotInsertHook();
                                return defaultValue();
                            } else if (cur == key) {
                                // key is present
                                return (int) (entry >>> 32);
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (int) (entry = tab[fIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[fIndex] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                                postFreeSlotInsertHook();
                                return defaultValue();
                            } else if (cur == key) {
                                // key is present
                                return (int) (entry >>> 32);
                            }
                            step += 2;
                        }
                    } else {
                        firstRemoved = -1;
                    }
                } else {
                    firstRemoved = index;
                }
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[bIndex] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                            postFreeSlotInsertHook();
                            return defaultValue();
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                            postRemovedSlotInsertHook();
                            return defaultValue();
                        }
                    } else if (cur == key) {
                        // key is present
                        return (int) (entry >>> 32);
                    } else if (cur == removed && firstRemoved < 0) {
                        firstRemoved = bIndex;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[fIndex] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                            postFreeSlotInsertHook();
                            return defaultValue();
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                            postRemovedSlotInsertHook();
                            return defaultValue();
                        }
                    } else if (cur == key) {
                        // key is present
                        return (int) (entry >>> 32);
                    } else if (cur == removed && firstRemoved < 0) {
                        firstRemoved = fIndex;
                    }
                    step += 2;
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
        int removed = removedValue;
        if (k == (free = freeValue)) {
            free = changeFree();
        } else if (k == removed) {
            removed = changeRemoved();
        }
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
            keyAbsentFreeSlot:
            if (cur != free) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (int) (entry = tab[bIndex])) == k) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (int) (entry = tab[fIndex])) == k) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == free) {
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
                        if ((cur = (int) (entry = tab[bIndex])) == k) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == k) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 2;
                    }
                }
                // key is absent, removed slot
                Integer newValue = remappingFunction.apply(k, null);
                if (newValue != null) {
                    incrementModCount();
                    tab[firstRemoved] = ((((long) k) & INT_MASK) | (((long) newValue) << 32));
                    postRemovedSlotInsertHook();
                    return newValue;
                } else {
                    return null;
                }
            }
            // key is absent, free slot
            Integer newValue = remappingFunction.apply(k, null);
            if (newValue != null) {
                incrementModCount();
                tab[index] = ((((long) k) & INT_MASK) | (((long) newValue) << 32));
                postFreeSlotInsertHook();
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
            incrementModCount();
            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), removed);
            postRemoveHook();
            return null;
        }
    }


    @Override
    public int compute(int key, IntIntToIntFunction remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int free;
        int removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) % (capacity = tab.length)])) != key) {
            keyAbsentFreeSlot:
            if (cur != free) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (int) (entry = tab[bIndex])) == key) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (int) (entry = tab[fIndex])) == key) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == free) {
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
                        if ((cur = (int) (entry = tab[bIndex])) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == key) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 2;
                    }
                }
                // key is absent, removed slot
                int newValue = remappingFunction.applyAsInt(key, defaultValue());
                incrementModCount();
                tab[firstRemoved] = ((((long) key) & INT_MASK) | (((long) newValue) << 32));
                postRemovedSlotInsertHook();
                return newValue;
            }
            // key is absent, free slot
            int newValue = remappingFunction.applyAsInt(key, defaultValue());
            incrementModCount();
            tab[index] = ((((long) key) & INT_MASK) | (((long) newValue) << 32));
            postFreeSlotInsertHook();
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
        int removed = removedValue;
        if (k == (free = freeValue)) {
            free = changeFree();
        } else if (k == removed) {
            removed = changeRemoved();
        }
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) % (capacity = tab.length)])) == k) {
            // key is present
            return (int) (entry >>> 32);
        } else {
            keyAbsentFreeSlot:
            if (cur != free) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (int) (entry = tab[bIndex])) == k) {
                                // key is present
                                return (int) (entry >>> 32);
                            } else if (cur == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (int) (entry = tab[fIndex])) == k) {
                                // key is present
                                return (int) (entry >>> 32);
                            } else if (cur == free) {
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
                        if ((cur = (int) (entry = tab[bIndex])) == k) {
                            // key is present
                            return (int) (entry >>> 32);
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == k) {
                            // key is present
                            return (int) (entry >>> 32);
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 2;
                    }
                }
                // key is absent, removed slot
                Integer value = mappingFunction.apply(k);
                if (value != null) {
                    incrementModCount();
                    tab[firstRemoved] = ((((long) k) & INT_MASK) | (((long) value) << 32));
                    postRemovedSlotInsertHook();
                    return value;
                } else {
                    return null;
                }
            }
            // key is absent, free slot
            Integer value = mappingFunction.apply(k);
            if (value != null) {
                incrementModCount();
                tab[index] = ((((long) k) & INT_MASK) | (((long) value) << 32));
                postFreeSlotInsertHook();
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
        int removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) % (capacity = tab.length)])) == key) {
            // key is present
            return (int) (entry >>> 32);
        } else {
            keyAbsentFreeSlot:
            if (cur != free) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (int) (entry = tab[bIndex])) == key) {
                                // key is present
                                return (int) (entry >>> 32);
                            } else if (cur == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (int) (entry = tab[fIndex])) == key) {
                                // key is present
                                return (int) (entry >>> 32);
                            } else if (cur == free) {
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
                        if ((cur = (int) (entry = tab[bIndex])) == key) {
                            // key is present
                            return (int) (entry >>> 32);
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == key) {
                            // key is present
                            return (int) (entry >>> 32);
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 2;
                    }
                }
                // key is absent, removed slot
                int value = mappingFunction.applyAsInt(key);
                incrementModCount();
                tab[firstRemoved] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                postRemovedSlotInsertHook();
                return value;
            }
            // key is absent, free slot
            int value = mappingFunction.applyAsInt(key);
            incrementModCount();
            tab[index] = ((((long) key) & INT_MASK) | (((long) value) << 32));
            postFreeSlotInsertHook();
            return value;
        }
    }


    @Override
    public Integer computeIfPresent(Integer key,
            BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        int k = key;
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int free, removed;
        if (k != (free = freeValue) && k != (removed = removedValue)) {
            long[] tab = table;
            int capacity, index;
            int cur;
            long entry;
            keyPresent:
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
                if (cur == free) {
                    // key is absent, free slot
                    return null;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (int) (entry = tab[bIndex])) == k) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return null;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == k) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return null;
                        }
                        step += 2;
                    }
                }
            }
            // key is present
            Integer newValue = remappingFunction.apply(k, (int) (entry >>> 32));
            if (newValue != null) {
                U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
                return newValue;
            } else {
                incrementModCount();
                U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), removed);
                postRemoveHook();
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
        if (key != (free = freeValue) && key != removedValue) {
            long[] tab = table;
            int capacity, index;
            int cur;
            long entry;
            keyPresent:
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) % (capacity = tab.length)])) != key) {
                if (cur == free) {
                    // key is absent, free slot
                    return defaultValue();
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (int) (entry = tab[bIndex])) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return defaultValue();
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == key) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return defaultValue();
                        }
                        step += 2;
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
        int removed = removedValue;
        if (k == (free = freeValue)) {
            free = changeFree();
        } else if (k == removed) {
            removed = changeRemoved();
        }
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
            keyAbsentFreeSlot:
            if (cur != free) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (int) (entry = tab[bIndex])) == k) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (int) (entry = tab[fIndex])) == k) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == free) {
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
                        if ((cur = (int) (entry = tab[bIndex])) == k) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == k) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 2;
                    }
                }
                // key is absent, removed slot
                incrementModCount();
                tab[firstRemoved] = ((((long) k) & INT_MASK) | (((long) value) << 32));
                postRemovedSlotInsertHook();
                return value;
            }
            // key is absent, free slot
            incrementModCount();
            tab[index] = ((((long) k) & INT_MASK) | (((long) value) << 32));
            postFreeSlotInsertHook();
            return value;
        }
        // key is present
        Integer newValue = remappingFunction.apply((int) (entry >>> 32), value);
        if (newValue != null) {
            U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), newValue);
            return newValue;
        } else {
            incrementModCount();
            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), removed);
            postRemoveHook();
            return null;
        }
    }


    @Override
    public int merge(int key, int value, IntBinaryOperator remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        int free;
        int removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        keyPresent:
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) % (capacity = tab.length)])) != key) {
            keyAbsentFreeSlot:
            if (cur != free) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (int) (entry = tab[bIndex])) == key) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (int) (entry = tab[fIndex])) == key) {
                                index = fIndex;
                                break keyPresent;
                            } else if (cur == free) {
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
                        if ((cur = (int) (entry = tab[bIndex])) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = bIndex;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == key) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            if (firstRemoved < 0) {
                                index = fIndex;
                                break keyAbsentFreeSlot;
                            } else {
                                break keyAbsentRemovedSlot;
                            }
                        } else if (cur == removed && firstRemoved < 0) {
                            firstRemoved = fIndex;
                        }
                        step += 2;
                    }
                }
                // key is absent, removed slot
                incrementModCount();
                tab[firstRemoved] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                postRemovedSlotInsertHook();
                return value;
            }
            // key is absent, free slot
            incrementModCount();
            tab[index] = ((((long) key) & INT_MASK) | (((long) value) << 32));
            postFreeSlotInsertHook();
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
        int removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) % (capacity = tab.length)])) == free) {
            // key is absent, free slot
            incrementModCount();
            tab[index] = ((((long) key) & INT_MASK) | (((long) value) << 32));
            postFreeSlotInsertHook();
            return value;
        } else {
            keyPresent:
            if (cur != key) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (int) (entry = tab[bIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[bIndex] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                                postFreeSlotInsertHook();
                                return value;
                            } else if (cur == key) {
                                index = bIndex;
                                break keyPresent;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (int) (entry = tab[fIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[fIndex] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                                postFreeSlotInsertHook();
                                return value;
                            } else if (cur == key) {
                                index = fIndex;
                                break keyPresent;
                            }
                            step += 2;
                        }
                    } else {
                        firstRemoved = -1;
                    }
                } else {
                    firstRemoved = index;
                }
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[bIndex] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                            postFreeSlotInsertHook();
                            return value;
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                            postRemovedSlotInsertHook();
                            return value;
                        }
                    } else if (cur == key) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == removed && firstRemoved < 0) {
                        firstRemoved = bIndex;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[fIndex] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                            postFreeSlotInsertHook();
                            return value;
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                            postRemovedSlotInsertHook();
                            return value;
                        }
                    } else if (cur == key) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == removed && firstRemoved < 0) {
                        firstRemoved = fIndex;
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
    public int addValue(int key, int addition, int defaultValue) {
        int value = defaultValue + addition;
        int free;
        int removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        long[] tab = table;
        int capacity, index;
        int cur;
        long entry;
        if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) % (capacity = tab.length)])) == free) {
            // key is absent, free slot
            incrementModCount();
            tab[index] = ((((long) key) & INT_MASK) | (((long) value) << 32));
            postFreeSlotInsertHook();
            return value;
        } else {
            keyPresent:
            if (cur != key) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (int) (entry = tab[bIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[bIndex] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                                postFreeSlotInsertHook();
                                return value;
                            } else if (cur == key) {
                                index = bIndex;
                                break keyPresent;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (int) (entry = tab[fIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[fIndex] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                                postFreeSlotInsertHook();
                                return value;
                            } else if (cur == key) {
                                index = fIndex;
                                break keyPresent;
                            }
                            step += 2;
                        }
                    } else {
                        firstRemoved = -1;
                    }
                } else {
                    firstRemoved = index;
                }
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (int) (entry = tab[bIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[bIndex] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                            postFreeSlotInsertHook();
                            return value;
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                            postRemovedSlotInsertHook();
                            return value;
                        }
                    } else if (cur == key) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == removed && firstRemoved < 0) {
                        firstRemoved = bIndex;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (int) (entry = tab[fIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[fIndex] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                            postFreeSlotInsertHook();
                            return value;
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = ((((long) key) & INT_MASK) | (((long) value) << 32));
                            postRemovedSlotInsertHook();
                            return value;
                        }
                    } else if (cur == key) {
                        index = fIndex;
                        break keyPresent;
                    } else if (cur == removed && firstRemoved < 0) {
                        firstRemoved = fIndex;
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
    public void putAll(@Nonnull Map<? extends Integer, ? extends Integer> m) {
        CommonIntIntMapOps.putAll(this, m);
    }


    @Override
    public Integer replace(Integer key, Integer value) {
        int k = key;
        int free;
        if (k != (free = freeValue) && k != removedValue) {
            long[] tab = table;
            int capacity, index;
            int cur;
            long entry;
            keyPresent:
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
                if (cur == free) {
                    // key is absent, free slot
                    return null;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (int) (entry = tab[bIndex])) == k) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return null;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == k) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
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
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public int replace(int key, int value) {
        int free;
        if (key != (free = freeValue) && key != removedValue) {
            long[] tab = table;
            int capacity, index;
            int cur;
            long entry;
            keyPresent:
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) % (capacity = tab.length)])) != key) {
                if (cur == free) {
                    // key is absent, free slot
                    return defaultValue();
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (int) (entry = tab[bIndex])) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return defaultValue();
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == key) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
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
        if (key != (free = freeValue) && key != removedValue) {
            long[] tab = table;
            int capacity, index;
            int cur;
            long entry;
            keyPresent:
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) % (capacity = tab.length)])) != key) {
                if (cur == free) {
                    // key is absent, free slot
                    return false;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (int) (entry = tab[bIndex])) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return false;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == key) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
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
        int removed = removedValue;
        long[] tab = table;
        long entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), function.apply(key, (int) (entry >>> 32)));
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free && key != removed) {
                    U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), function.apply(key, (int) (entry >>> 32)));
                }
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
        int removed = removedValue;
        long[] tab = table;
        long entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), function.applyAsInt(key, (int) (entry >>> 32)));
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free && key != removed) {
                    U.putInt(tab, LONG_BASE + INT_VALUE_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), function.applyAsInt(key, (int) (entry >>> 32)));
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
    public Integer remove(Object key) {
        int k = (Integer) key;
        int free, removed;
        if (k != (free = freeValue) && k != (removed = removedValue)) {
            long[] tab = table;
            int capacity, index;
            int cur;
            long entry;
            keyPresent:
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
                if (cur == free) {
                    // key is absent, free slot
                    return null;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (int) (entry = tab[bIndex])) == k) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return null;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == k) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return null;
                        }
                        step += 2;
                    }
                }
            }
            // key is present
            int val = (int) (entry >>> 32);
            incrementModCount();
            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), removed);
            postRemoveHook();
            return val;
        } else {
            // key is absent
            return null;
        }
    }


    @Override
    public boolean justRemove(int key) {
        int free, removed;
        if (key != (free = freeValue) && key != (removed = removedValue)) {
            long[] tab = table;
            int capacity, index;
            int cur;
            long entry;
            keyPresent:
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) % (capacity = tab.length)])) != key) {
                if (cur == free) {
                    // key is absent, free slot
                    return false;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (int) (entry = tab[bIndex])) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return false;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == key) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return false;
                        }
                        step += 2;
                    }
                }
            }
            // key is present
            incrementModCount();
            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), removed);
            postRemoveHook();
            return true;
        } else {
            // key is absent
            return false;
        }
    }



    

    @Override
    public int remove(int key) {
        int free, removed;
        if (key != (free = freeValue) && key != (removed = removedValue)) {
            long[] tab = table;
            int capacity, index;
            int cur;
            long entry;
            keyPresent:
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) % (capacity = tab.length)])) != key) {
                if (cur == free) {
                    // key is absent, free slot
                    return defaultValue();
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (int) (entry = tab[bIndex])) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return defaultValue();
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == key) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return defaultValue();
                        }
                        step += 2;
                    }
                }
            }
            // key is present
            int val = (int) (entry >>> 32);
            incrementModCount();
            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), removed);
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
        int free, removed;
        if (key != (free = freeValue) && key != (removed = removedValue)) {
            long[] tab = table;
            int capacity, index;
            int cur;
            long entry;
            keyPresent:
            if ((cur = (int) (entry = tab[index = ParallelKVIntKeyMixing.mix(key) % (capacity = tab.length)])) != key) {
                if (cur == free) {
                    // key is absent, free slot
                    return false;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (int) (entry = tab[bIndex])) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return false;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (int) (entry = tab[fIndex])) == key) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return false;
                        }
                        step += 2;
                    }
                }
            }
            // key is present
            if ((int) (entry >>> 32) == value) {
                incrementModCount();
                U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), removed);
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
        int removed = removedValue;
        long[] tab = table;
        long entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free) {
                    if (filter.test(key, (int) (entry >>> 32))) {
                        incrementModCount();
                        mc++;
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), removed);
                        postRemoveHook();
                        changed = true;
                    }
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free && key != removed) {
                    if (filter.test(key, (int) (entry >>> 32))) {
                        incrementModCount();
                        mc++;
                        U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), removed);
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
            return MutableQHashParallelKVIntIntMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return MutableQHashParallelKVIntIntMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return MutableQHashParallelKVIntIntMapGO.this.currentLoad();
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
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free) {
                        result[resultIndex++] = new MutableEntry(mc, i, key, (int) (entry >>> 32));
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        result[resultIndex++] = new MutableEntry(mc, i, key, (int) (entry >>> 32));
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
            int free = freeValue;
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free) {
                        a[resultIndex++] = (T) new MutableEntry(mc, i, key, (int) (entry >>> 32));
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        a[resultIndex++] = (T) new MutableEntry(mc, i, key, (int) (entry >>> 32));
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
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Integer, Integer>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            int free = freeValue;
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free) {
                        action.accept(new MutableEntry(mc, i, key, (int) (entry >>> 32)));
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        action.accept(new MutableEntry(mc, i, key, (int) (entry >>> 32)));
                    }
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
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free) {
                        if (!predicate.test(new MutableEntry(mc, i, key, (int) (entry >>> 32)))) {
                            terminated = true;
                            break;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        if (!predicate.test(new MutableEntry(mc, i, key, (int) (entry >>> 32)))) {
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
        public ObjIterator<Map.Entry<Integer, Integer>> iterator() {
            int mc = modCount();
            if (!noRemoved())
                return new SomeRemovedEntryIterator(mc);
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Integer, Integer>> cursor() {
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
            int free = freeValue;
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free) {
                        if (!c.contains(e.with(key, (int) (entry >>> 32)))) {
                            containsAll = false;
                            break;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        if (!c.contains(e.with(key, (int) (entry >>> 32)))) {
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
            int free = freeValue;
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free) {
                        changed |= s.remove(e.with(key, (int) (entry >>> 32)));
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        changed |= s.remove(e.with(key, (int) (entry >>> 32)));
                    }
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
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free) {
                        changed |= c.add(new MutableEntry(mc, i, key, (int) (entry >>> 32)));
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        changed |= c.add(new MutableEntry(mc, i, key, (int) (entry >>> 32)));
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        public int hashCode() {
            return MutableQHashParallelKVIntIntMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            int free = freeValue;
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
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
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
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
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            sb.setCharAt(0, '[');
            sb.setCharAt(sb.length() - 1, ']');
            return sb.toString();
        }

        @Override
        public boolean shrink() {
            return MutableQHashParallelKVIntIntMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Integer, Integer> e = (Map.Entry<Integer, Integer>) o;
                int key = e.getKey();
                int value = e.getValue();
                return MutableQHashParallelKVIntIntMapGO.this.remove(key, value);
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
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free) {
                        if (filter.test(new MutableEntry(mc, i, key, (int) (entry >>> 32)))) {
                            incrementModCount();
                            mc++;
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), removed);
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        if (filter.test(new MutableEntry(mc, i, key, (int) (entry >>> 32)))) {
                            incrementModCount();
                            mc++;
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), removed);
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
            int free = freeValue;
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free) {
                        if (c.contains(e.with(key, (int) (entry >>> 32)))) {
                            incrementModCount();
                            mc++;
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), removed);
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        if (c.contains(e.with(key, (int) (entry >>> 32)))) {
                            incrementModCount();
                            mc++;
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), removed);
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
            int free = freeValue;
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free) {
                        if (!c.contains(e.with(key, (int) (entry >>> 32)))) {
                            incrementModCount();
                            mc++;
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), removed);
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        if (!c.contains(e.with(key, (int) (entry >>> 32)))) {
                            incrementModCount();
                            mc++;
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), removed);
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
            MutableQHashParallelKVIntIntMapGO.this.clear();
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
            return MutableQHashParallelKVIntIntMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return MutableQHashParallelKVIntIntMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return MutableQHashParallelKVIntIntMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(int v) {
            return MutableQHashParallelKVIntIntMapGO.this.containsValue(v);
        }



        @Override
        public void forEach(Consumer<? super Integer> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            int free = freeValue;
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((int) (entry = tab[i]) != free) {
                        action.accept((int) (entry >>> 32));
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        action.accept((int) (entry >>> 32));
                    }
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
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((int) (entry = tab[i]) != free) {
                        action.accept((int) (entry >>> 32));
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        action.accept((int) (entry >>> 32));
                    }
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
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((int) (entry = tab[i]) != free) {
                        if (!predicate.test((int) (entry >>> 32))) {
                            terminated = true;
                            break;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        if (!predicate.test((int) (entry >>> 32))) {
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
        public boolean allContainingIn(IntCollection c) {
            if (isEmpty())
                return true;
            boolean containsAll = true;
            int mc = modCount();
            int free = freeValue;
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((int) (entry = tab[i]) != free) {
                        if (!c.contains((int) (entry >>> 32))) {
                            containsAll = false;
                            break;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        if (!c.contains((int) (entry >>> 32))) {
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
        public boolean reverseAddAllTo(IntCollection c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            int free = freeValue;
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((int) (entry = tab[i]) != free) {
                        changed |= c.add((int) (entry >>> 32));
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        changed |= c.add((int) (entry >>> 32));
                    }
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
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((int) (entry = tab[i]) != free) {
                        changed |= s.removeInt((int) (entry >>> 32));
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        changed |= s.removeInt((int) (entry >>> 32));
                    }
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
            if (!noRemoved())
                return new SomeRemovedValueIterator(mc);
            return new NoRemovedValueIterator(mc);
        }

        @Nonnull
        @Override
        public IntCursor cursor() {
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
            int free = freeValue;
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((int) (entry = tab[i]) != free) {
                        result[resultIndex++] = (int) (entry >>> 32);
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        result[resultIndex++] = (int) (entry >>> 32);
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
            int free = freeValue;
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((int) (entry = tab[i]) != free) {
                        a[resultIndex++] = (T) Integer.valueOf((int) (entry >>> 32));
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        a[resultIndex++] = (T) Integer.valueOf((int) (entry >>> 32));
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
        public int[] toIntArray() {
            int size = size();
            int[] result = new int[size];
            if (size == 0)
                return result;
            int resultIndex = 0;
            int mc = modCount();
            int free = freeValue;
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((int) (entry = tab[i]) != free) {
                        result[resultIndex++] = (int) (entry >>> 32);
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        result[resultIndex++] = (int) (entry >>> 32);
                    }
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
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((int) (entry = tab[i]) != free) {
                        a[resultIndex++] = (int) (entry >>> 32);
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        a[resultIndex++] = (int) (entry >>> 32);
                    }
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
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((int) (entry = tab[i]) != free) {
                        sb.append(' ').append((int) (entry >>> 32)).append(',');
                        if (++elementCount == 8) {
                            int expectedLength = sb.length() * (size() / 8);
                            sb.ensureCapacity(expectedLength + (expectedLength / 2));
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        sb.append(' ').append((int) (entry >>> 32)).append(',');
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
            return removeInt(( Integer ) o);
        }

        @Override
        public boolean removeInt(int v) {
            return removeValue(v);
        }



        @Override
        public void clear() {
            MutableQHashParallelKVIntIntMapGO.this.clear();
        }

        
        public boolean removeIf(Predicate<? super Integer> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            int free = freeValue;
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((int) (entry = tab[i]) != free) {
                        if (filter.test((int) (entry >>> 32))) {
                            incrementModCount();
                            mc++;
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), removed);
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        if (filter.test((int) (entry >>> 32))) {
                            incrementModCount();
                            mc++;
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), removed);
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
        public boolean removeIf(IntPredicate filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            int free = freeValue;
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((int) (entry = tab[i]) != free) {
                        if (filter.test((int) (entry >>> 32))) {
                            incrementModCount();
                            mc++;
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), removed);
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        if (filter.test((int) (entry >>> 32))) {
                            incrementModCount();
                            mc++;
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), removed);
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
            if (c instanceof IntCollection)
                return removeAll((IntCollection) c);
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            int free = freeValue;
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((int) (entry = tab[i]) != free) {
                        if (c.contains((int) (entry >>> 32))) {
                            incrementModCount();
                            mc++;
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), removed);
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        if (c.contains((int) (entry >>> 32))) {
                            incrementModCount();
                            mc++;
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), removed);
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

        private boolean removeAll(IntCollection c) {
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            int free = freeValue;
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((int) (entry = tab[i]) != free) {
                        if (c.contains((int) (entry >>> 32))) {
                            incrementModCount();
                            mc++;
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), removed);
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        if (c.contains((int) (entry >>> 32))) {
                            incrementModCount();
                            mc++;
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), removed);
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
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((int) (entry = tab[i]) != free) {
                        if (!c.contains((int) (entry >>> 32))) {
                            incrementModCount();
                            mc++;
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), removed);
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        if (!c.contains((int) (entry >>> 32))) {
                            incrementModCount();
                            mc++;
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), removed);
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
            int removed = removedValue;
            long[] tab = table;
            long entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((int) (entry = tab[i]) != free) {
                        if (!c.contains((int) (entry >>> 32))) {
                            incrementModCount();
                            mc++;
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), removed);
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
                        if (!c.contains((int) (entry >>> 32))) {
                            incrementModCount();
                            mc++;
                            U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (i)) << LONG_SCALE_SHIFT), removed);
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Integer, Integer>> {
        final long[] tab;
        final int free;
        final int removed;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        MutableEntry next;

        NoRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            long[] tab = this.tab = table;
            int free = this.free = freeValue;
            this.removed = removedValue;
            int nextI = tab.length;
            long entry;
            while (--nextI >= 0) {
                int key;
                if ((key = (int) (entry = tab[nextI])) != free) {
                    next = new MutableEntry(mc, nextI, key, (int) (entry >>> 32));
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
                    action.accept(new MutableEntry(mc, i, key, (int) (entry >>> 32)));
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
            int index;
            if ((index = this.index) >= 0) {
                if (expectedModCount++ == modCount()) {
                    this.index = -1;
                    incrementModCount();
                    U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), removed);
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Integer, Integer>> {
        final long[] tab;
        final int free;
        final int removed;
        int expectedModCount;
        int index;
        int curKey;
        int curValue;

        NoRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            int free = this.free = freeValue;
            this.removed = removedValue;
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
                    action.accept(new MutableEntry(mc, i, key, (int) (entry >>> 32)));
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
                return new MutableEntry(expectedModCount, index, curKey, curValue);
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
            int free;
            if (curKey != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    incrementModCount();
                    U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), removed);
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedEntryIterator implements ObjIterator<Map.Entry<Integer, Integer>> {
        final long[] tab;
        final int free;
        final int removed;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        MutableEntry next;

        SomeRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            long[] tab = this.tab = table;
            int free = this.free = freeValue;
            int removed = this.removed = removedValue;
            int nextI = tab.length;
            long entry;
            while (--nextI >= 0) {
                int key;
                if ((key = (int) (entry = tab[nextI])) != free && key != removed) {
                    next = new MutableEntry(mc, nextI, key, (int) (entry >>> 32));
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
            int removed = this.removed;
            long entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free && key != removed) {
                    action.accept(new MutableEntry(mc, i, key, (int) (entry >>> 32)));
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
                    int removed = this.removed;
                    MutableEntry prev = next;
                    long entry;
                    while (--nextI >= 0) {
                        int key;
                        if ((key = (int) (entry = tab[nextI])) != free && key != removed) {
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
            int index;
            if ((index = this.index) >= 0) {
                if (expectedModCount++ == modCount()) {
                    this.index = -1;
                    incrementModCount();
                    U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), removed);
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedEntryCursor implements ObjCursor<Map.Entry<Integer, Integer>> {
        final long[] tab;
        final int free;
        final int removed;
        int expectedModCount;
        int index;
        int curKey;
        int curValue;

        SomeRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            int free = this.free = freeValue;
            this.removed = removedValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Integer, Integer>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            int free = this.free;
            int removed = this.removed;
            long entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free && key != removed) {
                    action.accept(new MutableEntry(mc, i, key, (int) (entry >>> 32)));
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
                return new MutableEntry(expectedModCount, index, curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                long[] tab = this.tab;
                int free = this.free;
                int removed = this.removed;
                long entry;
                for (int i = index - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
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
            int free;
            if (curKey != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    incrementModCount();
                    U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), removed);
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }




    class NoRemovedValueIterator implements IntIterator {
        final long[] tab;
        final int free;
        final int removed;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        int next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            long[] tab = this.tab = table;
            int free = this.free = freeValue;
            this.removed = removedValue;
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
                    incrementModCount();
                    U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), removed);
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedValueCursor implements IntCursor {
        final long[] tab;
        final int free;
        final int removed;
        int expectedModCount;
        int index;
        int curKey;
        int curValue;

        NoRemovedValueCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            int free = this.free = freeValue;
            this.removed = removedValue;
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
            int free;
            if (curKey != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    incrementModCount();
                    U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), removed);
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedValueIterator implements IntIterator {
        final long[] tab;
        final int free;
        final int removed;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        int next;

        SomeRemovedValueIterator(int mc) {
            expectedModCount = mc;
            long[] tab = this.tab = table;
            int free = this.free = freeValue;
            int removed = this.removed = removedValue;
            int nextI = tab.length;
            long entry;
            while (--nextI >= 0) {
                int key;
                if ((key = (int) (entry = tab[nextI])) != free && key != removed) {
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
                    int removed = this.removed;
                    int prev = next;
                    long entry;
                    while (--nextI >= 0) {
                        int key;
                        if ((key = (int) (entry = tab[nextI])) != free && key != removed) {
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
            int removed = this.removed;
            long entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free && key != removed) {
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
            int removed = this.removed;
            long entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free && key != removed) {
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
                    incrementModCount();
                    U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), removed);
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedValueCursor implements IntCursor {
        final long[] tab;
        final int free;
        final int removed;
        int expectedModCount;
        int index;
        int curKey;
        int curValue;

        SomeRemovedValueCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            int free = this.free = freeValue;
            this.removed = removedValue;
            curKey = free;
        }

        @Override
        public void forEachForward(IntConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            int free = this.free;
            int removed = this.removed;
            long entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free && key != removed) {
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
                int removed = this.removed;
                long entry;
                for (int i = index - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
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
            int free;
            if (curKey != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    incrementModCount();
                    U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), removed);
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }



    class NoRemovedMapCursor implements IntIntCursor {
        final long[] tab;
        final int free;
        final int removed;
        int expectedModCount;
        int index;
        int curKey;
        int curValue;

        NoRemovedMapCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            int free = this.free = freeValue;
            this.removed = removedValue;
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
            int free;
            if (curKey != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    incrementModCount();
                    U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), removed);
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }

    class SomeRemovedMapCursor implements IntIntCursor {
        final long[] tab;
        final int free;
        final int removed;
        int expectedModCount;
        int index;
        int curKey;
        int curValue;

        SomeRemovedMapCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            int free = this.free = freeValue;
            this.removed = removedValue;
            curKey = free;
        }

        @Override
        public void forEachForward(IntIntConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            long[] tab = this.tab;
            int free = this.free;
            int removed = this.removed;
            long entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                int key;
                if ((key = (int) (entry = tab[i])) != free && key != removed) {
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
                int removed = this.removed;
                long entry;
                for (int i = index - 1; i >= 0; i--) {
                    int key;
                    if ((key = (int) (entry = tab[i])) != free && key != removed) {
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
            int free;
            if (curKey != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    incrementModCount();
                    U.putInt(tab, LONG_BASE + INT_KEY_OFFSET + (((long) (index)) << LONG_SCALE_SHIFT), removed);
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

