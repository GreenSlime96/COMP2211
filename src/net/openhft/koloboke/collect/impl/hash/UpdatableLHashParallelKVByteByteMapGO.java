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


public class UpdatableLHashParallelKVByteByteMapGO
        extends UpdatableLHashParallelKVByteByteMapSO {

    
    final void copy(ParallelKVByteByteLHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    
    final void move(ParallelKVByteByteLHash hash) {
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
        if (key != (free = freeValue)) {
            char[] tab = table;
            int capacityMask, index;
            byte cur;
            int entry;
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
                // key is present
                return (byte) (entry >>> 8) == value;
            } else {
                if (cur == free) {
                    // key is absent
                    return false;
                } else {
                    while (true) {
                        if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            // key is present
                            return (byte) (entry >>> 8) == value;
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
    public Byte get(Object key) {
        byte k = (Byte) key;
        byte free;
        if (k != (free = freeValue)) {
            char[] tab = table;
            int capacityMask, index;
            byte cur;
            int entry;
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
                // key is present
                return (byte) (entry >>> 8);
            } else {
                if (cur == free) {
                    // key is absent
                    return null;
                } else {
                    while (true) {
                        if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                            // key is present
                            return (byte) (entry >>> 8);
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
    public byte get(byte key) {
        byte free;
        if (key != (free = freeValue)) {
            char[] tab = table;
            int capacityMask, index;
            byte cur;
            int entry;
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
                // key is present
                return (byte) (entry >>> 8);
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue();
                } else {
                    while (true) {
                        if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            // key is present
                            return (byte) (entry >>> 8);
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
    public Byte getOrDefault(Object key, Byte defaultValue) {
        byte k = (Byte) key;
        byte free;
        if (k != (free = freeValue)) {
            char[] tab = table;
            int capacityMask, index;
            byte cur;
            int entry;
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
                // key is present
                return (byte) (entry >>> 8);
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue;
                } else {
                    while (true) {
                        if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                            // key is present
                            return (byte) (entry >>> 8);
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
    public byte getOrDefault(byte key, byte defaultValue) {
        byte free;
        if (key != (free = freeValue)) {
            char[] tab = table;
            int capacityMask, index;
            byte cur;
            int entry;
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
                // key is present
                return (byte) (entry >>> 8);
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue;
                } else {
                    while (true) {
                        if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            // key is present
                            return (byte) (entry >>> 8);
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
    public void forEach(BiConsumer<? super Byte, ? super Byte> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        byte free = freeValue;
        char[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            byte key;
            if ((key = (byte) (entry = tab[i])) != free) {
                action.accept(key, (byte) (entry >>> 8));
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
        char[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            byte key;
            if ((key = (byte) (entry = tab[i])) != free) {
                action.accept(key, (byte) (entry >>> 8));
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
        char[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            byte key;
            if ((key = (byte) (entry = tab[i])) != free) {
                if (!predicate.test(key, (byte) (entry >>> 8))) {
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
    public ByteByteCursor cursor() {
        int mc = modCount();
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
        char[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            byte key;
            if ((key = (byte) (entry = tab[i])) != free) {
                if (!m.containsEntry(key, (byte) (entry >>> 8))) {
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
    public void reversePutAllTo(InternalByteByteMapOps m) {
        if (isEmpty())
            return;
        int mc = modCount();
        byte free = freeValue;
        char[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            byte key;
            if ((key = (byte) (entry = tab[i])) != free) {
                m.justPut(key, (byte) (entry >>> 8));
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
        char[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            byte key;
            if ((key = (byte) (entry = tab[i])) != free) {
                hashCode += key ^ (byte) (entry >>> 8);
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
        char[] tab = table;
        int entry;
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
        char[] tab = table;
        int entry;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        char[] newTab = table;
        int capacityMask = newTab.length - 1;
        for (int i = tab.length - 1; i >= 0; i--) {
            byte key;
            if ((key = (byte) (entry = tab[i])) != free) {
                int index;
                if (U.getByte(newTab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) (index = ParallelKVByteKeyMixing.mix(key) & capacityMask)) << CHAR_SCALE_SHIFT)) != free) {
                    while (true) {
                        if (U.getByte(newTab, CHAR_BASE + BYTE_KEY_OFFSET + (((long) ((index = (index - 1) & capacityMask))) << CHAR_SCALE_SHIFT)) == free) {
                            break;
                        }
                    }
                }
                newTab[index] = (char) entry;
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }


    @Override
    public Byte put(Byte key, Byte value) {
        byte k = key;
        byte free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        char[] tab = table;
        int capacityMask, index;
        byte cur;
        int entry;
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
            postInsertHook();
            return null;
        } else {
            keyPresent:
            if (cur != k) {
                while (true) {
                    if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[index] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
                        postInsertHook();
                        return null;
                    } else if (cur == k) {
                        break keyPresent;
                    }
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
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        char[] tab = table;
        int capacityMask, index;
        byte cur;
        int entry;
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
            postInsertHook();
            return defaultValue();
        } else {
            keyPresent:
            if (cur != key) {
                while (true) {
                    if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[index] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
                        postInsertHook();
                        return defaultValue();
                    } else if (cur == key) {
                        break keyPresent;
                    }
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
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        char[] tab = table;
        int capacityMask, index;
        byte cur;
        int entry;
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
            postInsertHook();
            return null;
        } else {
            if (cur == k) {
                // key is present
                return (byte) (entry >>> 8);
            } else {
                while (true) {
                    if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[index] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
                        postInsertHook();
                        return null;
                    } else if (cur == k) {
                        // key is present
                        return (byte) (entry >>> 8);
                    }
                }
            }
        }
    }

    @Override
    public byte putIfAbsent(byte key, byte value) {
        byte free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        char[] tab = table;
        int capacityMask, index;
        byte cur;
        int entry;
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
            postInsertHook();
            return defaultValue();
        } else {
            if (cur == key) {
                // key is present
                return (byte) (entry >>> 8);
            } else {
                while (true) {
                    if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[index] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
                        postInsertHook();
                        return defaultValue();
                    } else if (cur == key) {
                        // key is present
                        return (byte) (entry >>> 8);
                    }
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
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        char[] tab = table;
        int capacityMask, index;
        byte cur;
        int entry;
        keyPresent:
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) != k) {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        break keyPresent;
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            Byte newValue = remappingFunction.apply(k, null);
            if (newValue != null) {
                incrementModCount();
                tab[index] = (char) ((((int) k) & BYTE_MASK) | (((int) newValue) << 8));
                postInsertHook();
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
            throw new java.lang.UnsupportedOperationException("Compute operation of updatable map doesn't support removals");
        }
    }


    @Override
    public byte compute(byte key, ByteByteToByteFunction remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        byte free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        char[] tab = table;
        int capacityMask, index;
        byte cur;
        int entry;
        keyPresent:
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) != key) {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                        break keyPresent;
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            byte newValue = remappingFunction.applyAsByte(key, defaultValue());
            incrementModCount();
            tab[index] = (char) ((((int) key) & BYTE_MASK) | (((int) newValue) << 8));
            postInsertHook();
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
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        char[] tab = table;
        int capacityMask, index;
        byte cur;
        int entry;
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
            // key is present
            return (byte) (entry >>> 8);
        } else {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        // key is present
                        return (byte) (entry >>> 8);
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            Byte value = mappingFunction.apply(k);
            if (value != null) {
                incrementModCount();
                tab[index] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
                postInsertHook();
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
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        char[] tab = table;
        int capacityMask, index;
        byte cur;
        int entry;
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
            // key is present
            return (byte) (entry >>> 8);
        } else {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                        // key is present
                        return (byte) (entry >>> 8);
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            byte value = mappingFunction.applyAsByte(key);
            incrementModCount();
            tab[index] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
            postInsertHook();
            return value;
        }
    }


    @Override
    public Byte computeIfPresent(Byte key,
            BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        byte k = key;
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        byte free;
        if (k != (free = freeValue)) {
            char[] tab = table;
            int capacityMask, index;
            byte cur;
            int entry;
            keyPresent:
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) != k) {
                if (cur == free) {
                    // key is absent
                    return null;
                } else {
                    while (true) {
                        if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return null;
                        }
                    }
                }
            }
            // key is present
            Byte newValue = remappingFunction.apply(k, (byte) (entry >>> 8));
            if (newValue != null) {
                U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), newValue);
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
    public byte computeIfPresent(byte key, ByteByteToByteFunction remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        byte free;
        if (key != (free = freeValue)) {
            char[] tab = table;
            int capacityMask, index;
            byte cur;
            int entry;
            keyPresent:
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) != key) {
                if (cur == free) {
                    // key is absent
                    return defaultValue();
                } else {
                    while (true) {
                        if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue();
                        }
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
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        char[] tab = table;
        int capacityMask, index;
        byte cur;
        int entry;
        keyPresent:
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) != k) {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        break keyPresent;
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            incrementModCount();
            tab[index] = (char) ((((int) k) & BYTE_MASK) | (((int) value) << 8));
            postInsertHook();
            return value;
        }
        // key is present
        Byte newValue = remappingFunction.apply((byte) (entry >>> 8), value);
        if (newValue != null) {
            U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), newValue);
            return newValue;
        } else {
            throw new java.lang.UnsupportedOperationException("Merge operation of updatable map doesn't support removals");
        }
    }


    @Override
    public byte merge(byte key, byte value, ByteBinaryOperator remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        byte free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        char[] tab = table;
        int capacityMask, index;
        byte cur;
        int entry;
        keyPresent:
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) != key) {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                        break keyPresent;
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            incrementModCount();
            tab[index] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
            postInsertHook();
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
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        char[] tab = table;
        int capacityMask, index;
        byte cur;
        int entry;
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
            postInsertHook();
            return value;
        } else {
            keyPresent:
            if (cur != key) {
                while (true) {
                    if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[index] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
                        postInsertHook();
                        return value;
                    } else if (cur == key) {
                        break keyPresent;
                    }
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
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        char[] tab = table;
        int capacityMask, index;
        byte cur;
        int entry;
        if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
            postInsertHook();
            return value;
        } else {
            keyPresent:
            if (cur != key) {
                while (true) {
                    if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[index] = (char) ((((int) key) & BYTE_MASK) | (((int) value) << 8));
                        postInsertHook();
                        return value;
                    } else if (cur == key) {
                        break keyPresent;
                    }
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
        if (k != (free = freeValue)) {
            char[] tab = table;
            int capacityMask, index;
            byte cur;
            int entry;
            keyPresent:
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) != k) {
                if (cur == free) {
                    // key is absent
                    return null;
                } else {
                    while (true) {
                        if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return null;
                        }
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
        if (key != (free = freeValue)) {
            char[] tab = table;
            int capacityMask, index;
            byte cur;
            int entry;
            keyPresent:
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) != key) {
                if (cur == free) {
                    // key is absent
                    return defaultValue();
                } else {
                    while (true) {
                        if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue();
                        }
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
        if (key != (free = freeValue)) {
            char[] tab = table;
            int capacityMask, index;
            byte cur;
            int entry;
            keyPresent:
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) != key) {
                if (cur == free) {
                    // key is absent
                    return false;
                } else {
                    while (true) {
                        if ((cur = (byte) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return false;
                        }
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
        char[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            byte key;
            if ((key = (byte) (entry = tab[i])) != free) {
                U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), function.apply(key, (byte) (entry >>> 8)));
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
        char[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            byte key;
            if ((key = (byte) (entry = tab[i])) != free) {
                U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (i)) << CHAR_SCALE_SHIFT), function.applyAsByte(key, (byte) (entry >>> 8)));
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
    public boolean justRemove(byte key) {
        throw new java.lang.UnsupportedOperationException();
    }



    

    @Override
    public byte remove(byte key) {
        throw new java.lang.UnsupportedOperationException();
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Byte) key).byteValue(),
                ((Byte) value).byteValue()
                );
    }

    @Override
    public boolean remove(byte key, byte value) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean removeIf(ByteBytePredicate filter) {
        throw new java.lang.UnsupportedOperationException();
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
            return UpdatableLHashParallelKVByteByteMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return UpdatableLHashParallelKVByteByteMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return UpdatableLHashParallelKVByteByteMapGO.this.currentLoad();
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
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    result[resultIndex++] = new MutableEntry(mc, i, key, (byte) (entry >>> 8));
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
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    a[resultIndex++] = (T) new MutableEntry(mc, i, key, (byte) (entry >>> 8));
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
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    action.accept(new MutableEntry(mc, i, key, (byte) (entry >>> 8)));
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
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    if (!predicate.test(new MutableEntry(mc, i, key, (byte) (entry >>> 8)))) {
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
        public ObjIterator<Map.Entry<Byte, Byte>> iterator() {
            int mc = modCount();
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Byte, Byte>> cursor() {
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
            byte free = freeValue;
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    if (!c.contains(e.with(key, (byte) (entry >>> 8)))) {
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
            byte free = freeValue;
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    changed |= s.remove(e.with(key, (byte) (entry >>> 8)));
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
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    changed |= c.add(new MutableEntry(mc, i, key, (byte) (entry >>> 8)));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        public int hashCode() {
            return UpdatableLHashParallelKVByteByteMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            byte free = freeValue;
            char[] tab = table;
            int entry;
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
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            sb.setCharAt(0, '[');
            sb.setCharAt(sb.length() - 1, ']');
            return sb.toString();
        }

        @Override
        public boolean shrink() {
            return UpdatableLHashParallelKVByteByteMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Byte, Byte> e = (Map.Entry<Byte, Byte>) o;
                byte key = e.getKey();
                byte value = e.getValue();
                return UpdatableLHashParallelKVByteByteMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Byte, Byte>> filter) {
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
            UpdatableLHashParallelKVByteByteMapGO.this.clear();
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
            return UpdatableLHashParallelKVByteByteMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return UpdatableLHashParallelKVByteByteMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return UpdatableLHashParallelKVByteByteMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(byte v) {
            return UpdatableLHashParallelKVByteByteMapGO.this.containsValue(v);
        }



        @Override
        public void forEach(Consumer<? super Byte> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            byte free = freeValue;
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    action.accept((byte) (entry >>> 8));
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
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    action.accept((byte) (entry >>> 8));
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
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    if (!predicate.test((byte) (entry >>> 8))) {
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
            byte free = freeValue;
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    if (!c.contains((byte) (entry >>> 8))) {
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
            byte free = freeValue;
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    changed |= c.add((byte) (entry >>> 8));
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
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    changed |= s.removeByte((byte) (entry >>> 8));
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
            byte free = freeValue;
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    result[resultIndex++] = (byte) (entry >>> 8);
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
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    a[resultIndex++] = (T) Byte.valueOf((byte) (entry >>> 8));
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
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    result[resultIndex++] = (byte) (entry >>> 8);
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
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    a[resultIndex++] = (byte) (entry >>> 8);
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
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    sb.append(' ').append((byte) (entry >>> 8)).append(',');
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
            UpdatableLHashParallelKVByteByteMapGO.this.clear();
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Byte, Byte>> {
        final char[] tab;
        final byte free;
        int expectedModCount;
        int nextIndex;
        MutableEntry next;

        NoRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            char[] tab = this.tab = table;
            byte free = this.free = freeValue;
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
            nextIndex = -1;
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
            throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Byte, Byte>> {
        final char[] tab;
        final byte free;
        int expectedModCount;
        int index;
        byte curKey;
        byte curValue;

        NoRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            byte free = this.free = freeValue;
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
            throw new java.lang.UnsupportedOperationException();
        }
    }




    class NoRemovedValueIterator implements ByteIterator {
        final char[] tab;
        final byte free;
        int expectedModCount;
        int nextIndex;
        byte next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            char[] tab = this.tab = table;
            byte free = this.free = freeValue;
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
            nextIndex = -1;
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
        final char[] tab;
        final byte free;
        int expectedModCount;
        int index;
        byte curKey;
        byte curValue;

        NoRemovedValueCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            byte free = this.free = freeValue;
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
            throw new java.lang.UnsupportedOperationException();
        }
    }



    class NoRemovedMapCursor implements ByteByteCursor {
        final char[] tab;
        final byte free;
        int expectedModCount;
        int index;
        byte curKey;
        byte curValue;

        NoRemovedMapCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            byte free = this.free = freeValue;
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
            throw new java.lang.UnsupportedOperationException();
        }
    }
}

