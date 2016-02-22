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
import net.openhft.koloboke.function.BytePredicate;
import net.openhft.koloboke.function.ByteByteConsumer;
import net.openhft.koloboke.function.ByteBytePredicate;
import net.openhft.koloboke.function.ByteByteToByteFunction;
import net.openhft.koloboke.function.ByteUnaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.openhft.koloboke.function.ByteBinaryOperator;
import net.openhft.koloboke.function.ByteConsumer;
import net.openhft.koloboke.function.BytePredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class MutableQHashParallelKVByteByteMapGO
        extends MutableQHashParallelKVByteByteMapSO {

    
    final void copy(ParallelKVByteByteQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    
    final void move(ParallelKVByteByteQHash hash) {
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
    public boolean containsEntry(byte key, byte value) {
        byte free;
        if (key != (free = freeValue) && key != removedValue) {
            char[] tab = table;
            int capacity, index;
            byte cur;
            int entry;
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) % (capacity = tab.length)])) == key) {
                // key is present
                return (byte) (entry >>> 8) == value;
            } else {
                if (cur == free) {
                    // key is absent, free slot
                    return false;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (byte) (entry = tab[bIndex])) == key) {
                            // key is present
                            return (byte) (entry >>> 8) == value;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return false;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (byte) (entry = tab[fIndex])) == key) {
                            // key is present
                            return (byte) (entry >>> 8) == value;
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
    public Byte get(Object key) {
        byte k = (Byte) key;
        byte free;
        if (k != (free = freeValue) && k != removedValue) {
            char[] tab = table;
            int capacity, index;
            byte cur;
            int entry;
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(k) % (capacity = tab.length)])) == k) {
                // key is present
                return (byte) (entry >>> 8);
            } else {
                if (cur == free) {
                    // key is absent, free slot
                    return null;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (byte) (entry = tab[bIndex])) == k) {
                            // key is present
                            return (byte) (entry >>> 8);
                        } else if (cur == free) {
                            // key is absent, free slot
                            return null;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (byte) (entry = tab[fIndex])) == k) {
                            // key is present
                            return (byte) (entry >>> 8);
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
    public byte get(byte key) {
        byte free;
        if (key != (free = freeValue) && key != removedValue) {
            char[] tab = table;
            int capacity, index;
            byte cur;
            int entry;
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) % (capacity = tab.length)])) == key) {
                // key is present
                return (byte) (entry >>> 8);
            } else {
                if (cur == free) {
                    // key is absent, free slot
                    return defaultValue();
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (byte) (entry = tab[bIndex])) == key) {
                            // key is present
                            return (byte) (entry >>> 8);
                        } else if (cur == free) {
                            // key is absent, free slot
                            return defaultValue();
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (byte) (entry = tab[fIndex])) == key) {
                            // key is present
                            return (byte) (entry >>> 8);
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
    public Byte getOrDefault(Object key, Byte defaultValue) {
        byte k = (Byte) key;
        byte free;
        if (k != (free = freeValue) && k != removedValue) {
            char[] tab = table;
            int capacity, index;
            byte cur;
            int entry;
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(k) % (capacity = tab.length)])) == k) {
                // key is present
                return (byte) (entry >>> 8);
            } else {
                if (cur == free) {
                    // key is absent, free slot
                    return defaultValue;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (byte) (entry = tab[bIndex])) == k) {
                            // key is present
                            return (byte) (entry >>> 8);
                        } else if (cur == free) {
                            // key is absent, free slot
                            return defaultValue;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (byte) (entry = tab[fIndex])) == k) {
                            // key is present
                            return (byte) (entry >>> 8);
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
    public byte getOrDefault(byte key, byte defaultValue) {
        byte free;
        if (key != (free = freeValue) && key != removedValue) {
            char[] tab = table;
            int capacity, index;
            byte cur;
            int entry;
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) % (capacity = tab.length)])) == key) {
                // key is present
                return (byte) (entry >>> 8);
            } else {
                if (cur == free) {
                    // key is absent, free slot
                    return defaultValue;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (byte) (entry = tab[bIndex])) == key) {
                            // key is present
                            return (byte) (entry >>> 8);
                        } else if (cur == free) {
                            // key is absent, free slot
                            return defaultValue;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (byte) (entry = tab[fIndex])) == key) {
                            // key is present
                            return (byte) (entry >>> 8);
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
    public void forEach(BiConsumer<? super Byte, ? super Byte> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        byte free = freeValue;
        byte removed = removedValue;
        char[] tab = table;
        int entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    action.accept(key, (byte) (entry >>> 8));
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                    action.accept(key, (byte) (entry >>> 8));
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void forEach(ByteByteConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        byte free = freeValue;
        byte removed = removedValue;
        char[] tab = table;
        int entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    action.accept(key, (byte) (entry >>> 8));
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                    action.accept(key, (byte) (entry >>> 8));
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public boolean forEachWhile(ByteBytePredicate predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        byte free = freeValue;
        byte removed = removedValue;
        char[] tab = table;
        int entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    if (!predicate.test(key, (byte) (entry >>> 8))) {
                        terminated = true;
                        break;
                    }
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                    if (!predicate.test(key, (byte) (entry >>> 8))) {
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
    public ByteByteCursor cursor() {
        int mc = modCount();
        if (!noRemoved())
            return new SomeRemovedMapCursor(mc);
        return new NoRemovedMapCursor(mc);
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonByteByteMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalByteByteMapOps m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        byte free = freeValue;
        byte removed = removedValue;
        char[] tab = table;
        int entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    if (!m.containsEntry(key, (byte) (entry >>> 8))) {
                        containsAll = false;
                        break;
                    }
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                    if (!m.containsEntry(key, (byte) (entry >>> 8))) {
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
    public void reversePutAllTo(InternalByteByteMapOps m) {
        if (isEmpty())
            return;
        int mc = modCount();
        byte free = freeValue;
        byte removed = removedValue;
        char[] tab = table;
        int entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    m.justPut(key, (byte) (entry >>> 8));
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                    m.justPut(key, (byte) (entry >>> 8));
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<Byte, Byte>> entrySet() {
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
        byte free = freeValue;
        byte removed = removedValue;
        char[] tab = table;
        int entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    hashCode += key ^ (byte) (entry >>> 8);
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                    hashCode += key ^ (byte) (entry >>> 8);
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
        byte free = freeValue;
        byte removed = removedValue;
        char[] tab = table;
        int entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    sb.append(' ');
                    sb.append(key);
                    sb.append('=');
                    sb.append((byte) (entry >>> 8));
                    sb.append(',');
                    if (++elementCount == 8) {
                        int expectedLength = sb.length() * (size() / 8);
                        sb.ensureCapacity(expectedLength + (expectedLength / 2));
                    }
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                    sb.append(' ');
                    sb.append(key);
                    sb.append('=');
                    sb.append((byte) (entry >>> 8));
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
        byte free = freeValue;
        byte removed = removedValue;
        char[] tab = table;
        int entry;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        char[] newTab = table;
        int capacity = newTab.length;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    int index;
                    if (U.getByte(newTab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (index = ParallelKVByteKeyMixing.mix(key) % capacity)) << CHAR_SCALE_SHIFT)) != free) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if (U.getByte(newTab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (bIndex)) << CHAR_SCALE_SHIFT)) == free) {
                                index = bIndex;
                                break;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if (U.getByte(newTab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (fIndex)) << CHAR_SCALE_SHIFT)) == free) {
                                index = fIndex;
                                break;
                            }
                            step += 2;
                        }
                    }
                    newTab[index] = (char) entry;
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                    int index;
                    if (U.getByte(newTab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (index = ParallelKVByteKeyMixing.mix(key) % capacity)) << CHAR_SCALE_SHIFT)) != free) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if (U.getByte(newTab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (bIndex)) << CHAR_SCALE_SHIFT)) == free) {
                                index = bIndex;
                                break;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if (U.getByte(newTab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (fIndex)) << CHAR_SCALE_SHIFT)) == free) {
                                index = fIndex;
                                break;
                            }
                            step += 2;
                        }
                    }
                    newTab[index] = (char) entry;
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }


    @Override
    public Byte put(Byte key, Byte value) {
        byte k = key;
        byte free;
        byte removed = removedValue;
        if (k == (free = freeValue)) {
            free = changeFree();
        } else if (k == removed) {
            removed = changeRemoved();
        }
        char[] tab = table;
        int capacity, index;
        byte cur;
        int entry;
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(k) % (capacity = tab.length)])) == free) {
            // key is absent, free slot
            incrementModCount();
            tab[index] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
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
                            if ((cur = (byte) (entry = tab[bIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[bIndex] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
                                postFreeSlotInsertHook();
                                return null;
                            } else if (cur == k) {
                                index = bIndex;
                                break keyPresent;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (byte) (entry = tab[fIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[fIndex] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
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
                    if ((cur = (byte) (entry = tab[bIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[bIndex] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
                            postFreeSlotInsertHook();
                            return null;
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
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
                    if ((cur = (byte) (entry = tab[fIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[fIndex] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
                            postFreeSlotInsertHook();
                            return null;
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
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
            byte prevValue = (byte) (entry >>> 8);
            U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), value);
            return prevValue;
        }
    }

    @Override
    public byte put(byte key, byte value) {
        byte free;
        byte removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        char[] tab = table;
        int capacity, index;
        byte cur;
        int entry;
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) % (capacity = tab.length)])) == free) {
            // key is absent, free slot
            incrementModCount();
            tab[index] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
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
                            if ((cur = (byte) (entry = tab[bIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[bIndex] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
                                postFreeSlotInsertHook();
                                return defaultValue();
                            } else if (cur == key) {
                                index = bIndex;
                                break keyPresent;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (byte) (entry = tab[fIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[fIndex] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
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
                    if ((cur = (byte) (entry = tab[bIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[bIndex] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
                            postFreeSlotInsertHook();
                            return defaultValue();
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
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
                    if ((cur = (byte) (entry = tab[fIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[fIndex] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
                            postFreeSlotInsertHook();
                            return defaultValue();
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
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
            byte prevValue = (byte) (entry >>> 8);
            U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), value);
            return prevValue;
        }
    }

    @Override
    public Byte putIfAbsent(Byte key, Byte value) {
        byte k = key;
        byte free;
        byte removed = removedValue;
        if (k == (free = freeValue)) {
            free = changeFree();
        } else if (k == removed) {
            removed = changeRemoved();
        }
        char[] tab = table;
        int capacity, index;
        byte cur;
        int entry;
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(k) % (capacity = tab.length)])) == free) {
            // key is absent, free slot
            incrementModCount();
            tab[index] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
            postFreeSlotInsertHook();
            return null;
        } else {
            if (cur == k) {
                // key is present
                return (byte) (entry >>> 8);
            } else {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (byte) (entry = tab[bIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[bIndex] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
                                postFreeSlotInsertHook();
                                return null;
                            } else if (cur == k) {
                                // key is present
                                return (byte) (entry >>> 8);
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (byte) (entry = tab[fIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[fIndex] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
                                postFreeSlotInsertHook();
                                return null;
                            } else if (cur == k) {
                                // key is present
                                return (byte) (entry >>> 8);
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
                    if ((cur = (byte) (entry = tab[bIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[bIndex] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
                            postFreeSlotInsertHook();
                            return null;
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
                            postRemovedSlotInsertHook();
                            return null;
                        }
                    } else if (cur == k) {
                        // key is present
                        return (byte) (entry >>> 8);
                    } else if (cur == removed && firstRemoved < 0) {
                        firstRemoved = bIndex;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (byte) (entry = tab[fIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[fIndex] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
                            postFreeSlotInsertHook();
                            return null;
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
                            postRemovedSlotInsertHook();
                            return null;
                        }
                    } else if (cur == k) {
                        // key is present
                        return (byte) (entry >>> 8);
                    } else if (cur == removed && firstRemoved < 0) {
                        firstRemoved = fIndex;
                    }
                    step += 2;
                }
            }
        }
    }

    @Override
    public byte putIfAbsent(byte key, byte value) {
        byte free;
        byte removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        char[] tab = table;
        int capacity, index;
        byte cur;
        int entry;
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) % (capacity = tab.length)])) == free) {
            // key is absent, free slot
            incrementModCount();
            tab[index] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
            postFreeSlotInsertHook();
            return defaultValue();
        } else {
            if (cur == key) {
                // key is present
                return (byte) (entry >>> 8);
            } else {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (byte) (entry = tab[bIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[bIndex] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
                                postFreeSlotInsertHook();
                                return defaultValue();
                            } else if (cur == key) {
                                // key is present
                                return (byte) (entry >>> 8);
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (byte) (entry = tab[fIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[fIndex] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
                                postFreeSlotInsertHook();
                                return defaultValue();
                            } else if (cur == key) {
                                // key is present
                                return (byte) (entry >>> 8);
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
                    if ((cur = (byte) (entry = tab[bIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[bIndex] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
                            postFreeSlotInsertHook();
                            return defaultValue();
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
                            postRemovedSlotInsertHook();
                            return defaultValue();
                        }
                    } else if (cur == key) {
                        // key is present
                        return (byte) (entry >>> 8);
                    } else if (cur == removed && firstRemoved < 0) {
                        firstRemoved = bIndex;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (byte) (entry = tab[fIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[fIndex] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
                            postFreeSlotInsertHook();
                            return defaultValue();
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
                            postRemovedSlotInsertHook();
                            return defaultValue();
                        }
                    } else if (cur == key) {
                        // key is present
                        return (byte) (entry >>> 8);
                    } else if (cur == removed && firstRemoved < 0) {
                        firstRemoved = fIndex;
                    }
                    step += 2;
                }
            }
        }
    }

    @Override
    public void justPut(byte key, byte value) {
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return;
        } else {
            // key is present
            U.putByte(table, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), value);
            return;
        }
    }


    @Override
    public Byte compute(Byte key,
            BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        byte k = key;
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        byte free;
        byte removed = removedValue;
        if (k == (free = freeValue)) {
            free = changeFree();
        } else if (k == removed) {
            removed = changeRemoved();
        }
        char[] tab = table;
        int capacity, index;
        byte cur;
        int entry;
        keyPresent:
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
            keyAbsentFreeSlot:
            if (cur != free) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (byte) (entry = tab[bIndex])) == k) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (byte) (entry = tab[fIndex])) == k) {
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
                        if ((cur = (byte) (entry = tab[bIndex])) == k) {
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
                        if ((cur = (byte) (entry = tab[fIndex])) == k) {
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
                Byte newValue = remappingFunction.apply(k, null);
                if (newValue != null) {
                    incrementModCount();
                    tab[firstRemoved] = (char) ((((int) k) & BYTE_MASK) | (((int) newValue) << 8));
                    postRemovedSlotInsertHook();
                    return newValue;
                } else {
                    return null;
                }
            }
            // key is absent, free slot
            Byte newValue = remappingFunction.apply(k, null);
            if (newValue != null) {
                incrementModCount();
                tab[index] = (char) ((((int) k) & BYTE_MASK) | (((int) newValue) << 8));
                postFreeSlotInsertHook();
                return newValue;
            } else {
                return null;
            }
        }
        // key is present
        Byte newValue = remappingFunction.apply(k, (byte) (entry >>> 8));
        if (newValue != null) {
            U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), newValue);
            return newValue;
        } else {
            incrementModCount();
            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), removed);
            postRemoveHook();
            return null;
        }
    }


    @Override
    public byte compute(byte key, ByteByteToByteFunction remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        byte free;
        byte removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        char[] tab = table;
        int capacity, index;
        byte cur;
        int entry;
        keyPresent:
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) % (capacity = tab.length)])) != key) {
            keyAbsentFreeSlot:
            if (cur != free) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (byte) (entry = tab[bIndex])) == key) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (byte) (entry = tab[fIndex])) == key) {
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
                        if ((cur = (byte) (entry = tab[bIndex])) == key) {
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
                        if ((cur = (byte) (entry = tab[fIndex])) == key) {
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
                byte newValue = remappingFunction.applyAsByte(key, defaultValue());
                incrementModCount();
                tab[firstRemoved] = (char) ((((int) key) & BYTE_MASK) | (((int) newValue) << 8));
                postRemovedSlotInsertHook();
                return newValue;
            }
            // key is absent, free slot
            byte newValue = remappingFunction.applyAsByte(key, defaultValue());
            incrementModCount();
            tab[index] = (char) ((((int) key) & BYTE_MASK) | (((int) newValue) << 8));
            postFreeSlotInsertHook();
            return newValue;
        }
        // key is present
        byte newValue = remappingFunction.applyAsByte(key, (byte) (entry >>> 8));
        U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), newValue);
        return newValue;
    }


    @Override
    public Byte computeIfAbsent(Byte key,
            Function<? super Byte, ? extends Byte> mappingFunction) {
        byte k = key;
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        byte free;
        byte removed = removedValue;
        if (k == (free = freeValue)) {
            free = changeFree();
        } else if (k == removed) {
            removed = changeRemoved();
        }
        char[] tab = table;
        int capacity, index;
        byte cur;
        int entry;
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(k) % (capacity = tab.length)])) == k) {
            // key is present
            return (byte) (entry >>> 8);
        } else {
            keyAbsentFreeSlot:
            if (cur != free) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (byte) (entry = tab[bIndex])) == k) {
                                // key is present
                                return (byte) (entry >>> 8);
                            } else if (cur == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (byte) (entry = tab[fIndex])) == k) {
                                // key is present
                                return (byte) (entry >>> 8);
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
                        if ((cur = (byte) (entry = tab[bIndex])) == k) {
                            // key is present
                            return (byte) (entry >>> 8);
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
                        if ((cur = (byte) (entry = tab[fIndex])) == k) {
                            // key is present
                            return (byte) (entry >>> 8);
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
                Byte value = mappingFunction.apply(k);
                if (value != null) {
                    incrementModCount();
                    tab[firstRemoved] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
                    postRemovedSlotInsertHook();
                    return value;
                } else {
                    return null;
                }
            }
            // key is absent, free slot
            Byte value = mappingFunction.apply(k);
            if (value != null) {
                incrementModCount();
                tab[index] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
                postFreeSlotInsertHook();
                return value;
            } else {
                return null;
            }
        }
    }


    @Override
    public byte computeIfAbsent(byte key, ByteUnaryOperator mappingFunction) {
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        byte free;
        byte removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        char[] tab = table;
        int capacity, index;
        byte cur;
        int entry;
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) % (capacity = tab.length)])) == key) {
            // key is present
            return (byte) (entry >>> 8);
        } else {
            keyAbsentFreeSlot:
            if (cur != free) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (byte) (entry = tab[bIndex])) == key) {
                                // key is present
                                return (byte) (entry >>> 8);
                            } else if (cur == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (byte) (entry = tab[fIndex])) == key) {
                                // key is present
                                return (byte) (entry >>> 8);
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
                        if ((cur = (byte) (entry = tab[bIndex])) == key) {
                            // key is present
                            return (byte) (entry >>> 8);
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
                        if ((cur = (byte) (entry = tab[fIndex])) == key) {
                            // key is present
                            return (byte) (entry >>> 8);
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
                byte value = mappingFunction.applyAsByte(key);
                incrementModCount();
                tab[firstRemoved] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
                postRemovedSlotInsertHook();
                return value;
            }
            // key is absent, free slot
            byte value = mappingFunction.applyAsByte(key);
            incrementModCount();
            tab[index] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
            postFreeSlotInsertHook();
            return value;
        }
    }


    @Override
    public Byte computeIfPresent(Byte key,
            BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        byte k = key;
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        byte free, removed;
        if (k != (free = freeValue) && k != (removed = removedValue)) {
            char[] tab = table;
            int capacity, index;
            byte cur;
            int entry;
            keyPresent:
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
                if (cur == free) {
                    // key is absent, free slot
                    return null;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (byte) (entry = tab[bIndex])) == k) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return null;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (byte) (entry = tab[fIndex])) == k) {
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
            Byte newValue = remappingFunction.apply(k, (byte) (entry >>> 8));
            if (newValue != null) {
                U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), newValue);
                return newValue;
            } else {
                incrementModCount();
                U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), removed);
                postRemoveHook();
                return null;
            }
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public byte computeIfPresent(byte key, ByteByteToByteFunction remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        byte free;
        if (key != (free = freeValue) && key != removedValue) {
            char[] tab = table;
            int capacity, index;
            byte cur;
            int entry;
            keyPresent:
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) % (capacity = tab.length)])) != key) {
                if (cur == free) {
                    // key is absent, free slot
                    return defaultValue();
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (byte) (entry = tab[bIndex])) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return defaultValue();
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (byte) (entry = tab[fIndex])) == key) {
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
            byte newValue = remappingFunction.applyAsByte(key, (byte) (entry >>> 8));
            U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), newValue);
            return newValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Byte merge(Byte key, Byte value,
            BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        byte k = key;
        if (value == null)
            throw new java.lang.NullPointerException();
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        byte free;
        byte removed = removedValue;
        if (k == (free = freeValue)) {
            free = changeFree();
        } else if (k == removed) {
            removed = changeRemoved();
        }
        char[] tab = table;
        int capacity, index;
        byte cur;
        int entry;
        keyPresent:
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
            keyAbsentFreeSlot:
            if (cur != free) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (byte) (entry = tab[bIndex])) == k) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (byte) (entry = tab[fIndex])) == k) {
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
                        if ((cur = (byte) (entry = tab[bIndex])) == k) {
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
                        if ((cur = (byte) (entry = tab[fIndex])) == k) {
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
                tab[firstRemoved] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
                postRemovedSlotInsertHook();
                return value;
            }
            // key is absent, free slot
            incrementModCount();
            tab[index] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
            postFreeSlotInsertHook();
            return value;
        }
        // key is present
        Byte newValue = remappingFunction.apply((byte) (entry >>> 8), value);
        if (newValue != null) {
            U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), newValue);
            return newValue;
        } else {
            incrementModCount();
            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), removed);
            postRemoveHook();
            return null;
        }
    }


    @Override
    public byte merge(byte key, byte value, ByteBinaryOperator remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        byte free;
        byte removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        char[] tab = table;
        int capacity, index;
        byte cur;
        int entry;
        keyPresent:
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) % (capacity = tab.length)])) != key) {
            keyAbsentFreeSlot:
            if (cur != free) {
                int firstRemoved;
                if (cur != removed) {
                    if (noRemoved()) {
                        int bIndex = index, fIndex = index, step = 1;
                        while (true) {
                            if ((bIndex -= step) < 0) bIndex += capacity;
                            if ((cur = (byte) (entry = tab[bIndex])) == key) {
                                index = bIndex;
                                break keyPresent;
                            } else if (cur == free) {
                                index = bIndex;
                                break keyAbsentFreeSlot;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (byte) (entry = tab[fIndex])) == key) {
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
                        if ((cur = (byte) (entry = tab[bIndex])) == key) {
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
                        if ((cur = (byte) (entry = tab[fIndex])) == key) {
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
                tab[firstRemoved] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
                postRemovedSlotInsertHook();
                return value;
            }
            // key is absent, free slot
            incrementModCount();
            tab[index] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
            postFreeSlotInsertHook();
            return value;
        }
        // key is present
        byte newValue = remappingFunction.applyAsByte((byte) (entry >>> 8), value);
        U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), newValue);
        return newValue;
    }


    @Override
    public byte addValue(byte key, byte value) {
        byte free;
        byte removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        char[] tab = table;
        int capacity, index;
        byte cur;
        int entry;
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) % (capacity = tab.length)])) == free) {
            // key is absent, free slot
            incrementModCount();
            tab[index] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
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
                            if ((cur = (byte) (entry = tab[bIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[bIndex] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
                                postFreeSlotInsertHook();
                                return value;
                            } else if (cur == key) {
                                index = bIndex;
                                break keyPresent;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (byte) (entry = tab[fIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[fIndex] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
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
                    if ((cur = (byte) (entry = tab[bIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[bIndex] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
                            postFreeSlotInsertHook();
                            return value;
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
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
                    if ((cur = (byte) (entry = tab[fIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[fIndex] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
                            postFreeSlotInsertHook();
                            return value;
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
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
            byte newValue = (byte) ((byte) (entry >>> 8) + value);
            U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), newValue);
            return newValue;
        }
    }

    @Override
    public byte addValue(byte key, byte addition, byte defaultValue) {
        byte value = (byte) (defaultValue + addition);
        byte free;
        byte removed = removedValue;
        if (key == (free = freeValue)) {
            free = changeFree();
        } else if (key == removed) {
            removed = changeRemoved();
        }
        char[] tab = table;
        int capacity, index;
        byte cur;
        int entry;
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) % (capacity = tab.length)])) == free) {
            // key is absent, free slot
            incrementModCount();
            tab[index] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
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
                            if ((cur = (byte) (entry = tab[bIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[bIndex] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
                                postFreeSlotInsertHook();
                                return value;
                            } else if (cur == key) {
                                index = bIndex;
                                break keyPresent;
                            }
                            int t;
                            if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                            if ((cur = (byte) (entry = tab[fIndex])) == free) {
                                // key is absent, free slot
                                incrementModCount();
                                tab[fIndex] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
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
                    if ((cur = (byte) (entry = tab[bIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[bIndex] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
                            postFreeSlotInsertHook();
                            return value;
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
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
                    if ((cur = (byte) (entry = tab[fIndex])) == free) {
                        if (firstRemoved < 0) {
                            // key is absent, free slot
                            incrementModCount();
                            tab[fIndex] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
                            postFreeSlotInsertHook();
                            return value;
                        } else {
                            // key is absent, removed slot
                            incrementModCount();
                            tab[firstRemoved] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
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
            byte newValue = (byte) ((byte) (entry >>> 8) + addition);
            U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), newValue);
            return newValue;
        }
    }


    @Override
    public void putAll(@Nonnull Map<? extends Byte, ? extends Byte> m) {
        CommonByteByteMapOps.putAll(this, m);
    }


    @Override
    public Byte replace(Byte key, Byte value) {
        byte k = key;
        byte free;
        if (k != (free = freeValue) && k != removedValue) {
            char[] tab = table;
            int capacity, index;
            byte cur;
            int entry;
            keyPresent:
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
                if (cur == free) {
                    // key is absent, free slot
                    return null;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (byte) (entry = tab[bIndex])) == k) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return null;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (byte) (entry = tab[fIndex])) == k) {
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
            byte oldValue = (byte) (entry >>> 8);
            U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), value);
            return oldValue;
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public byte replace(byte key, byte value) {
        byte free;
        if (key != (free = freeValue) && key != removedValue) {
            char[] tab = table;
            int capacity, index;
            byte cur;
            int entry;
            keyPresent:
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) % (capacity = tab.length)])) != key) {
                if (cur == free) {
                    // key is absent, free slot
                    return defaultValue();
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (byte) (entry = tab[bIndex])) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return defaultValue();
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (byte) (entry = tab[fIndex])) == key) {
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
            byte oldValue = (byte) (entry >>> 8);
            U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), value);
            return oldValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public boolean replace(Byte key, Byte oldValue, Byte newValue) {
        return replace(key.byteValue(),
                oldValue.byteValue(),
                newValue.byteValue());
    }

    @Override
    public boolean replace(byte key, byte oldValue, byte newValue) {
        byte free;
        if (key != (free = freeValue) && key != removedValue) {
            char[] tab = table;
            int capacity, index;
            byte cur;
            int entry;
            keyPresent:
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) % (capacity = tab.length)])) != key) {
                if (cur == free) {
                    // key is absent, free slot
                    return false;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (byte) (entry = tab[bIndex])) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return false;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (byte) (entry = tab[fIndex])) == key) {
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
            if ((byte) (entry >>> 8) == oldValue) {
                U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), newValue);
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
            BiFunction<? super Byte, ? super Byte, ? extends Byte> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        byte free = freeValue;
        byte removed = removedValue;
        char[] tab = table;
        int entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), function.apply(key, (byte) (entry >>> 8)));
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                    U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), function.apply(key, (byte) (entry >>> 8)));
                }
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void replaceAll(ByteByteToByteFunction function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        byte free = freeValue;
        byte removed = removedValue;
        char[] tab = table;
        int entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), function.applyAsByte(key, (byte) (entry >>> 8)));
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                    U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), function.applyAsByte(key, (byte) (entry >>> 8)));
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
    public Byte remove(Object key) {
        byte k = (Byte) key;
        byte free, removed;
        if (k != (free = freeValue) && k != (removed = removedValue)) {
            char[] tab = table;
            int capacity, index;
            byte cur;
            int entry;
            keyPresent:
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
                if (cur == free) {
                    // key is absent, free slot
                    return null;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (byte) (entry = tab[bIndex])) == k) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return null;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (byte) (entry = tab[fIndex])) == k) {
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
            byte val = (byte) (entry >>> 8);
            incrementModCount();
            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), removed);
            postRemoveHook();
            return val;
        } else {
            // key is absent
            return null;
        }
    }


    @Override
    public boolean justRemove(byte key) {
        byte free, removed;
        if (key != (free = freeValue) && key != (removed = removedValue)) {
            char[] tab = table;
            int capacity, index;
            byte cur;
            int entry;
            keyPresent:
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) % (capacity = tab.length)])) != key) {
                if (cur == free) {
                    // key is absent, free slot
                    return false;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (byte) (entry = tab[bIndex])) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return false;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (byte) (entry = tab[fIndex])) == key) {
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
            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), removed);
            postRemoveHook();
            return true;
        } else {
            // key is absent
            return false;
        }
    }



    

    @Override
    public byte remove(byte key) {
        byte free, removed;
        if (key != (free = freeValue) && key != (removed = removedValue)) {
            char[] tab = table;
            int capacity, index;
            byte cur;
            int entry;
            keyPresent:
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) % (capacity = tab.length)])) != key) {
                if (cur == free) {
                    // key is absent, free slot
                    return defaultValue();
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (byte) (entry = tab[bIndex])) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return defaultValue();
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (byte) (entry = tab[fIndex])) == key) {
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
            byte val = (byte) (entry >>> 8);
            incrementModCount();
            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), removed);
            postRemoveHook();
            return val;
        } else {
            // key is absent
            return defaultValue();
        }
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Byte) key).byteValue(),
                ((Byte) value).byteValue()
                );
    }

    @Override
    public boolean remove(byte key, byte value) {
        byte free, removed;
        if (key != (free = freeValue) && key != (removed = removedValue)) {
            char[] tab = table;
            int capacity, index;
            byte cur;
            int entry;
            keyPresent:
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) % (capacity = tab.length)])) != key) {
                if (cur == free) {
                    // key is absent, free slot
                    return false;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (byte) (entry = tab[bIndex])) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent, free slot
                            return false;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (byte) (entry = tab[fIndex])) == key) {
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
            if ((byte) (entry >>> 8) == value) {
                incrementModCount();
                U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), removed);
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
    public boolean removeIf(ByteBytePredicate filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        byte free = freeValue;
        byte removed = removedValue;
        char[] tab = table;
        int entry;
        if (noRemoved()) {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    if (filter.test(key, (byte) (entry >>> 8))) {
                        incrementModCount();
                        mc++;
                        U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), removed);
                        postRemoveHook();
                        changed = true;
                    }
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                    if (filter.test(key, (byte) (entry >>> 8))) {
                        incrementModCount();
                        mc++;
                        U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), removed);
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






    class EntryView extends AbstractSetView<Map.Entry<Byte, Byte>>
            implements HashObjSet<Map.Entry<Byte, Byte>>,
            InternalObjCollectionOps<Map.Entry<Byte, Byte>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Byte, Byte>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Byte>defaultEquality()
                    ,
                    Equivalence.<Byte>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return MutableQHashParallelKVByteByteMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return MutableQHashParallelKVByteByteMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return MutableQHashParallelKVByteByteMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Byte, Byte> e = (Map.Entry<Byte, Byte>) o;
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
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free) {
                        result[resultIndex++] = new MutableEntry(mc, i, key, (byte) (entry >>> 8));
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        result[resultIndex++] = new MutableEntry(mc, i, key, (byte) (entry >>> 8));
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
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free) {
                        a[resultIndex++] = (T) new MutableEntry(mc, i, key, (byte) (entry >>> 8));
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        a[resultIndex++] = (T) new MutableEntry(mc, i, key, (byte) (entry >>> 8));
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
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Byte, Byte>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free) {
                        action.accept(new MutableEntry(mc, i, key, (byte) (entry >>> 8)));
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        action.accept(new MutableEntry(mc, i, key, (byte) (entry >>> 8)));
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Byte, Byte>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free) {
                        if (!predicate.test(new MutableEntry(mc, i, key, (byte) (entry >>> 8)))) {
                            terminated = true;
                            break;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        if (!predicate.test(new MutableEntry(mc, i, key, (byte) (entry >>> 8)))) {
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
        public ObjIterator<Map.Entry<Byte, Byte>> iterator() {
            int mc = modCount();
            if (!noRemoved())
                return new SomeRemovedEntryIterator(mc);
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Byte, Byte>> cursor() {
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
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free) {
                        if (!c.contains(e.with(key, (byte) (entry >>> 8)))) {
                            containsAll = false;
                            break;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        if (!c.contains(e.with(key, (byte) (entry >>> 8)))) {
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
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free) {
                        changed |= s.remove(e.with(key, (byte) (entry >>> 8)));
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        changed |= s.remove(e.with(key, (byte) (entry >>> 8)));
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Byte, Byte>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free) {
                        changed |= c.add(new MutableEntry(mc, i, key, (byte) (entry >>> 8)));
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        changed |= c.add(new MutableEntry(mc, i, key, (byte) (entry >>> 8)));
                    }
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        public int hashCode() {
            return MutableQHashParallelKVByteByteMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free) {
                        sb.append(' ');
                        sb.append(key);
                        sb.append('=');
                        sb.append((byte) (entry >>> 8));
                        sb.append(',');
                        if (++elementCount == 8) {
                            int expectedLength = sb.length() * (size() / 8);
                            sb.ensureCapacity(expectedLength + (expectedLength / 2));
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        sb.append(' ');
                        sb.append(key);
                        sb.append('=');
                        sb.append((byte) (entry >>> 8));
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
            return MutableQHashParallelKVByteByteMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Byte, Byte> e = (Map.Entry<Byte, Byte>) o;
                byte key = e.getKey();
                byte value = e.getValue();
                return MutableQHashParallelKVByteByteMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Byte, Byte>> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free) {
                        if (filter.test(new MutableEntry(mc, i, key, (byte) (entry >>> 8)))) {
                            incrementModCount();
                            mc++;
                            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), removed);
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        if (filter.test(new MutableEntry(mc, i, key, (byte) (entry >>> 8)))) {
                            incrementModCount();
                            mc++;
                            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), removed);
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
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free) {
                        if (c.contains(e.with(key, (byte) (entry >>> 8)))) {
                            incrementModCount();
                            mc++;
                            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), removed);
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        if (c.contains(e.with(key, (byte) (entry >>> 8)))) {
                            incrementModCount();
                            mc++;
                            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), removed);
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
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free) {
                        if (!c.contains(e.with(key, (byte) (entry >>> 8)))) {
                            incrementModCount();
                            mc++;
                            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), removed);
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        if (!c.contains(e.with(key, (byte) (entry >>> 8)))) {
                            incrementModCount();
                            mc++;
                            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), removed);
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
            MutableQHashParallelKVByteByteMapGO.this.clear();
        }
    }


    abstract class ByteByteEntry extends AbstractEntry<Byte, Byte> {

        abstract byte key();

        @Override
        public final Byte getKey() {
            return key();
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
            byte k2;
            byte v2;
            try {
                e2 = (Map.Entry) o;
                k2 = (Byte) e2.getKey();
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


    class MutableEntry extends ByteByteEntry {
        final int modCount;
        private final int index;
        final byte key;
        private byte value;

        MutableEntry(int modCount, int index, byte key, byte value) {
            this.modCount = modCount;
            this.index = index;
            this.key = key;
            this.value = value;
        }

        @Override
        public byte key() {
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
            U.putByte(
                    table, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) index) << CHAR_SCALE_SHIFT),
                    newValue);
        }
    }



    class ReusableEntry extends ByteByteEntry {
        private byte key;
        private byte value;

        ReusableEntry with(byte key, byte value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public byte key() {
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
            return MutableQHashParallelKVByteByteMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return MutableQHashParallelKVByteByteMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return MutableQHashParallelKVByteByteMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(byte v) {
            return MutableQHashParallelKVByteByteMapGO.this.containsValue(v);
        }



        @Override
        public void forEach(Consumer<? super Byte> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((byte) (entry = tab[i]) != free) {
                        action.accept((byte) (entry >>> 8));
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        action.accept((byte) (entry >>> 8));
                    }
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
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((byte) (entry = tab[i]) != free) {
                        action.accept((byte) (entry >>> 8));
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        action.accept((byte) (entry >>> 8));
                    }
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
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((byte) (entry = tab[i]) != free) {
                        if (!predicate.test((byte) (entry >>> 8))) {
                            terminated = true;
                            break;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        if (!predicate.test((byte) (entry >>> 8))) {
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
        public boolean allContainingIn(ByteCollection c) {
            if (isEmpty())
                return true;
            boolean containsAll = true;
            int mc = modCount();
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((byte) (entry = tab[i]) != free) {
                        if (!c.contains((byte) (entry >>> 8))) {
                            containsAll = false;
                            break;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        if (!c.contains((byte) (entry >>> 8))) {
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
        public boolean reverseAddAllTo(ByteCollection c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((byte) (entry = tab[i]) != free) {
                        changed |= c.add((byte) (entry >>> 8));
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        changed |= c.add((byte) (entry >>> 8));
                    }
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
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((byte) (entry = tab[i]) != free) {
                        changed |= s.removeByte((byte) (entry >>> 8));
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        changed |= s.removeByte((byte) (entry >>> 8));
                    }
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
            if (!noRemoved())
                return new SomeRemovedValueIterator(mc);
            return new NoRemovedValueIterator(mc);
        }

        @Nonnull
        @Override
        public ByteCursor cursor() {
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
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((byte) (entry = tab[i]) != free) {
                        result[resultIndex++] = (byte) (entry >>> 8);
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        result[resultIndex++] = (byte) (entry >>> 8);
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
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((byte) (entry = tab[i]) != free) {
                        a[resultIndex++] = (T) Byte.valueOf((byte) (entry >>> 8));
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        a[resultIndex++] = (T) Byte.valueOf((byte) (entry >>> 8));
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
        public byte[] toByteArray() {
            int size = size();
            byte[] result = new byte[size];
            if (size == 0)
                return result;
            int resultIndex = 0;
            int mc = modCount();
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((byte) (entry = tab[i]) != free) {
                        result[resultIndex++] = (byte) (entry >>> 8);
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        result[resultIndex++] = (byte) (entry >>> 8);
                    }
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
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((byte) (entry = tab[i]) != free) {
                        a[resultIndex++] = (byte) (entry >>> 8);
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        a[resultIndex++] = (byte) (entry >>> 8);
                    }
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
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((byte) (entry = tab[i]) != free) {
                        sb.append(' ').append((byte) (entry >>> 8)).append(',');
                        if (++elementCount == 8) {
                            int expectedLength = sb.length() * (size() / 8);
                            sb.ensureCapacity(expectedLength + (expectedLength / 2));
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        sb.append(' ').append((byte) (entry >>> 8)).append(',');
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
            return removeByte(( Byte ) o);
        }

        @Override
        public boolean removeByte(byte v) {
            return removeValue(v);
        }



        @Override
        public void clear() {
            MutableQHashParallelKVByteByteMapGO.this.clear();
        }

        
        public boolean removeIf(Predicate<? super Byte> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((byte) (entry = tab[i]) != free) {
                        if (filter.test((byte) (entry >>> 8))) {
                            incrementModCount();
                            mc++;
                            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), removed);
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        if (filter.test((byte) (entry >>> 8))) {
                            incrementModCount();
                            mc++;
                            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), removed);
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
        public boolean removeIf(BytePredicate filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((byte) (entry = tab[i]) != free) {
                        if (filter.test((byte) (entry >>> 8))) {
                            incrementModCount();
                            mc++;
                            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), removed);
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        if (filter.test((byte) (entry >>> 8))) {
                            incrementModCount();
                            mc++;
                            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), removed);
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
            if (c instanceof ByteCollection)
                return removeAll((ByteCollection) c);
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((byte) (entry = tab[i]) != free) {
                        if (c.contains((byte) (entry >>> 8))) {
                            incrementModCount();
                            mc++;
                            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), removed);
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        if (c.contains((byte) (entry >>> 8))) {
                            incrementModCount();
                            mc++;
                            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), removed);
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

        private boolean removeAll(ByteCollection c) {
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((byte) (entry = tab[i]) != free) {
                        if (c.contains((byte) (entry >>> 8))) {
                            incrementModCount();
                            mc++;
                            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), removed);
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        if (c.contains((byte) (entry >>> 8))) {
                            incrementModCount();
                            mc++;
                            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), removed);
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
            if (c instanceof ByteCollection)
                return retainAll((ByteCollection) c);
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
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((byte) (entry = tab[i]) != free) {
                        if (!c.contains((byte) (entry >>> 8))) {
                            incrementModCount();
                            mc++;
                            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), removed);
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        if (!c.contains((byte) (entry >>> 8))) {
                            incrementModCount();
                            mc++;
                            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), removed);
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

        private boolean retainAll(ByteCollection c) {
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
            byte free = freeValue;
            byte removed = removedValue;
            char[] tab = table;
            int entry;
            if (noRemoved()) {
                for (int i = tab.length - 1; i >= 0; i--) {
                    if ((byte) (entry = tab[i]) != free) {
                        if (!c.contains((byte) (entry >>> 8))) {
                            incrementModCount();
                            mc++;
                            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), removed);
                            postRemoveHook();
                            changed = true;
                        }
                    }
                }
            } else {
                for (int i = tab.length - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        if (!c.contains((byte) (entry >>> 8))) {
                            incrementModCount();
                            mc++;
                            U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), removed);
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Byte, Byte>> {
        final char[] tab;
        final byte free;
        final byte removed;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        MutableEntry next;

        NoRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            char[] tab = this.tab = table;
            byte free = this.free = freeValue;
            this.removed = removedValue;
            int nextI = tab.length;
            int entry;
            while (--nextI >= 0) {
                byte key;
                if ((key = (byte) (entry = tab[nextI])) != free) {
                    next = new MutableEntry(mc, nextI, key, (byte) (entry >>> 8));
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Byte, Byte>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            char[] tab = this.tab;
            byte free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    action.accept(new MutableEntry(mc, i, key, (byte) (entry >>> 8)));
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
        public Map.Entry<Byte, Byte> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
                    index = nextI;
                    char[] tab = this.tab;
                    byte free = this.free;
                    MutableEntry prev = next;
                    int entry;
                    while (--nextI >= 0) {
                        byte key;
                        if ((key = (byte) (entry = tab[nextI])) != free) {
                            next = new MutableEntry(mc, nextI, key, (byte) (entry >>> 8));
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
                    U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), removed);
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Byte, Byte>> {
        final char[] tab;
        final byte free;
        final byte removed;
        int expectedModCount;
        int index;
        byte curKey;
        byte curValue;

        NoRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            byte free = this.free = freeValue;
            this.removed = removedValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Byte, Byte>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            char[] tab = this.tab;
            byte free = this.free;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    action.accept(new MutableEntry(mc, i, key, (byte) (entry >>> 8)));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public Map.Entry<Byte, Byte> elem() {
            byte curKey;
            if ((curKey = this.curKey) != free) {
                return new MutableEntry(expectedModCount, index, curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                char[] tab = this.tab;
                byte free = this.free;
                int entry;
                for (int i = index - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free) {
                        index = i;
                        curKey = key;
                        curValue = (byte) (entry >>> 8);
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
            byte free;
            if (curKey != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    incrementModCount();
                    U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), removed);
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedEntryIterator implements ObjIterator<Map.Entry<Byte, Byte>> {
        final char[] tab;
        final byte free;
        final byte removed;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        MutableEntry next;

        SomeRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            char[] tab = this.tab = table;
            byte free = this.free = freeValue;
            byte removed = this.removed = removedValue;
            int nextI = tab.length;
            int entry;
            while (--nextI >= 0) {
                byte key;
                if ((key = (byte) (entry = tab[nextI])) != free && key != removed) {
                    next = new MutableEntry(mc, nextI, key, (byte) (entry >>> 8));
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Byte, Byte>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            char[] tab = this.tab;
            byte free = this.free;
            byte removed = this.removed;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                    action.accept(new MutableEntry(mc, i, key, (byte) (entry >>> 8)));
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
        public Map.Entry<Byte, Byte> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
                    index = nextI;
                    char[] tab = this.tab;
                    byte free = this.free;
                    byte removed = this.removed;
                    MutableEntry prev = next;
                    int entry;
                    while (--nextI >= 0) {
                        byte key;
                        if ((key = (byte) (entry = tab[nextI])) != free && key != removed) {
                            next = new MutableEntry(mc, nextI, key, (byte) (entry >>> 8));
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
                    U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), removed);
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedEntryCursor implements ObjCursor<Map.Entry<Byte, Byte>> {
        final char[] tab;
        final byte free;
        final byte removed;
        int expectedModCount;
        int index;
        byte curKey;
        byte curValue;

        SomeRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            byte free = this.free = freeValue;
            this.removed = removedValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Byte, Byte>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            char[] tab = this.tab;
            byte free = this.free;
            byte removed = this.removed;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                    action.accept(new MutableEntry(mc, i, key, (byte) (entry >>> 8)));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public Map.Entry<Byte, Byte> elem() {
            byte curKey;
            if ((curKey = this.curKey) != free) {
                return new MutableEntry(expectedModCount, index, curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                char[] tab = this.tab;
                byte free = this.free;
                byte removed = this.removed;
                int entry;
                for (int i = index - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        index = i;
                        curKey = key;
                        curValue = (byte) (entry >>> 8);
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
            byte free;
            if (curKey != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    incrementModCount();
                    U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), removed);
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }




    class NoRemovedValueIterator implements ByteIterator {
        final char[] tab;
        final byte free;
        final byte removed;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        byte next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            char[] tab = this.tab = table;
            byte free = this.free = freeValue;
            this.removed = removedValue;
            int nextI = tab.length;
            int entry;
            while (--nextI >= 0) {
                if ((byte) (entry = tab[nextI]) != free) {
                    next = (byte) (entry >>> 8);
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
                    index = nextI;
                    char[] tab = this.tab;
                    byte free = this.free;
                    byte prev = next;
                    int entry;
                    while (--nextI >= 0) {
                        if ((byte) (entry = tab[nextI]) != free) {
                            next = (byte) (entry >>> 8);
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
            char[] tab = this.tab;
            byte free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    action.accept((byte) (entry >>> 8));
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            index = nextIndex = -1;
        }

        @Override
        public void forEachRemaining(ByteConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            char[] tab = this.tab;
            byte free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    action.accept((byte) (entry >>> 8));
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
        public Byte next() {
            return nextByte();
        }

        @Override
        public void remove() {
            int index;
            if ((index = this.index) >= 0) {
                if (expectedModCount++ == modCount()) {
                    this.index = -1;
                    incrementModCount();
                    U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), removed);
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedValueCursor implements ByteCursor {
        final char[] tab;
        final byte free;
        final byte removed;
        int expectedModCount;
        int index;
        byte curKey;
        byte curValue;

        NoRemovedValueCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            byte free = this.free = freeValue;
            this.removed = removedValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ByteConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            char[] tab = this.tab;
            byte free = this.free;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    action.accept((byte) (entry >>> 8));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public byte elem() {
            if (curKey != free) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                char[] tab = this.tab;
                byte free = this.free;
                int entry;
                for (int i = index - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free) {
                        index = i;
                        curKey = key;
                        curValue = (byte) (entry >>> 8);
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
            byte free;
            if (curKey != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    incrementModCount();
                    U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), removed);
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedValueIterator implements ByteIterator {
        final char[] tab;
        final byte free;
        final byte removed;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        byte next;

        SomeRemovedValueIterator(int mc) {
            expectedModCount = mc;
            char[] tab = this.tab = table;
            byte free = this.free = freeValue;
            byte removed = this.removed = removedValue;
            int nextI = tab.length;
            int entry;
            while (--nextI >= 0) {
                byte key;
                if ((key = (byte) (entry = tab[nextI])) != free && key != removed) {
                    next = (byte) (entry >>> 8);
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
                    index = nextI;
                    char[] tab = this.tab;
                    byte free = this.free;
                    byte removed = this.removed;
                    byte prev = next;
                    int entry;
                    while (--nextI >= 0) {
                        byte key;
                        if ((key = (byte) (entry = tab[nextI])) != free && key != removed) {
                            next = (byte) (entry >>> 8);
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
            char[] tab = this.tab;
            byte free = this.free;
            byte removed = this.removed;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                    action.accept((byte) (entry >>> 8));
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            index = nextIndex = -1;
        }

        @Override
        public void forEachRemaining(ByteConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            char[] tab = this.tab;
            byte free = this.free;
            byte removed = this.removed;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                    action.accept((byte) (entry >>> 8));
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
        public Byte next() {
            return nextByte();
        }

        @Override
        public void remove() {
            int index;
            if ((index = this.index) >= 0) {
                if (expectedModCount++ == modCount()) {
                    this.index = -1;
                    incrementModCount();
                    U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), removed);
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class SomeRemovedValueCursor implements ByteCursor {
        final char[] tab;
        final byte free;
        final byte removed;
        int expectedModCount;
        int index;
        byte curKey;
        byte curValue;

        SomeRemovedValueCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            byte free = this.free = freeValue;
            this.removed = removedValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ByteConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            char[] tab = this.tab;
            byte free = this.free;
            byte removed = this.removed;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                    action.accept((byte) (entry >>> 8));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public byte elem() {
            if (curKey != free) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                char[] tab = this.tab;
                byte free = this.free;
                byte removed = this.removed;
                int entry;
                for (int i = index - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        index = i;
                        curKey = key;
                        curValue = (byte) (entry >>> 8);
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
            byte free;
            if (curKey != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    incrementModCount();
                    U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), removed);
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }



    class NoRemovedMapCursor implements ByteByteCursor {
        final char[] tab;
        final byte free;
        final byte removed;
        int expectedModCount;
        int index;
        byte curKey;
        byte curValue;

        NoRemovedMapCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            byte free = this.free = freeValue;
            this.removed = removedValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ByteByteConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            char[] tab = this.tab;
            byte free = this.free;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    action.accept(key, (byte) (entry >>> 8));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public byte key() {
            byte curKey;
            if ((curKey = this.curKey) != free) {
                return curKey;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public byte value() {
            if (curKey != free) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(byte value) {
            if (curKey != free) {
                if (expectedModCount == modCount()) {
                    U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), value);
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
                char[] tab = this.tab;
                byte free = this.free;
                int entry;
                for (int i = index - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free) {
                        index = i;
                        curKey = key;
                        curValue = (byte) (entry >>> 8);
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
            byte free;
            if (curKey != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    incrementModCount();
                    U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), removed);
                    postRemoveHook();
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }

    class SomeRemovedMapCursor implements ByteByteCursor {
        final char[] tab;
        final byte free;
        final byte removed;
        int expectedModCount;
        int index;
        byte curKey;
        byte curValue;

        SomeRemovedMapCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            byte free = this.free = freeValue;
            this.removed = removedValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ByteByteConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            char[] tab = this.tab;
            byte free = this.free;
            byte removed = this.removed;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                    action.accept(key, (byte) (entry >>> 8));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public byte key() {
            byte curKey;
            if ((curKey = this.curKey) != free) {
                return curKey;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public byte value() {
            if (curKey != free) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(byte value) {
            if (curKey != free) {
                if (expectedModCount == modCount()) {
                    U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), value);
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
                char[] tab = this.tab;
                byte free = this.free;
                byte removed = this.removed;
                int entry;
                for (int i = index - 1; i >= 0; i--) {
                    byte key;
                    if ((key = (byte) (entry = tab[i])) != free && key != removed) {
                        index = i;
                        curKey = key;
                        curValue = (byte) (entry >>> 8);
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
            byte free;
            if (curKey != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    incrementModCount();
                    U.putByte(tab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), removed);
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

