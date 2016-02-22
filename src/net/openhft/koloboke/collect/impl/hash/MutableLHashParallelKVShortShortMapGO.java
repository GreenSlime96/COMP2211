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
import net.openhft.koloboke.function.ShortShortConsumer;
import net.openhft.koloboke.function.ShortShortPredicate;
import net.openhft.koloboke.function.ShortShortToShortFunction;
import net.openhft.koloboke.function.ShortUnaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.openhft.koloboke.function.ShortBinaryOperator;
import net.openhft.koloboke.function.ShortConsumer;
import net.openhft.koloboke.function.ShortPredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class MutableLHashParallelKVShortShortMapGO
        extends MutableLHashParallelKVShortShortMapSO {

    
    final void copy(ParallelKVShortShortLHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    
    final void move(ParallelKVShortShortLHash hash) {
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
    public boolean containsEntry(short key, short value) {
        short free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            short cur;
            int entry;
            if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
                // key is present
                return (short) (entry >>> 16) == value;
            } else {
                if (cur == free) {
                    // key is absent
                    return false;
                } else {
                    while (true) {
                        if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            // key is present
                            return (short) (entry >>> 16) == value;
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
    public Short get(Object key) {
        short k = (Short) key;
        short free;
        if (k != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            short cur;
            int entry;
            if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
                // key is present
                return (short) (entry >>> 16);
            } else {
                if (cur == free) {
                    // key is absent
                    return null;
                } else {
                    while (true) {
                        if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                            // key is present
                            return (short) (entry >>> 16);
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
    public short get(short key) {
        short free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            short cur;
            int entry;
            if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
                // key is present
                return (short) (entry >>> 16);
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue();
                } else {
                    while (true) {
                        if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            // key is present
                            return (short) (entry >>> 16);
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
    public Short getOrDefault(Object key, Short defaultValue) {
        short k = (Short) key;
        short free;
        if (k != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            short cur;
            int entry;
            if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
                // key is present
                return (short) (entry >>> 16);
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue;
                } else {
                    while (true) {
                        if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                            // key is present
                            return (short) (entry >>> 16);
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
    public short getOrDefault(short key, short defaultValue) {
        short free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            short cur;
            int entry;
            if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
                // key is present
                return (short) (entry >>> 16);
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue;
                } else {
                    while (true) {
                        if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            // key is present
                            return (short) (entry >>> 16);
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
    public void forEach(BiConsumer<? super Short, ? super Short> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                action.accept(key, (short) (entry >>> 16));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void forEach(ShortShortConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                action.accept(key, (short) (entry >>> 16));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public boolean forEachWhile(ShortShortPredicate predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                if (!predicate.test(key, (short) (entry >>> 16))) {
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
    public ShortShortCursor cursor() {
        int mc = modCount();
        return new NoRemovedMapCursor(mc);
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonShortShortMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalShortShortMapOps m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                if (!m.containsEntry(key, (short) (entry >>> 16))) {
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
    public void reversePutAllTo(InternalShortShortMapOps m) {
        if (isEmpty())
            return;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                m.justPut(key, (short) (entry >>> 16));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<Short, Short>> entrySet() {
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
        short free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                hashCode += key ^ (short) (entry >>> 16);
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
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                sb.append(' ');
                sb.append(key);
                sb.append('=');
                sb.append((short) (entry >>> 16));
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
        int[] tab = table;
        int entry;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        int[] newTab = table;
        int capacityMask = newTab.length - 1;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                int index;
                if (U.getShort(newTab, INT_BASE + SHORT_KEY_OFFSET + (((long) (index = ParallelKVShortKeyMixing.mix(key) & capacityMask)) << INT_SCALE_SHIFT)) != free) {
                    while (true) {
                        if (U.getShort(newTab, INT_BASE + SHORT_KEY_OFFSET + (((long) ((index = (index - 1) & capacityMask))) << INT_SCALE_SHIFT)) == free) {
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
    public Short put(Short key, Short value) {
        short k = key;
        short free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacityMask, index;
        short cur;
        int entry;
        if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = ((((int) k) & SHORT_MASK) | (((int) value) << 16));
            postInsertHook();
            return null;
        } else {
            keyPresent:
            if (cur != k) {
                while (true) {
                    if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[index] = ((((int) k) & SHORT_MASK) | (((int) value) << 16));
                        postInsertHook();
                        return null;
                    } else if (cur == k) {
                        break keyPresent;
                    }
                }
            }
            // key is present
            short prevValue = (short) (entry >>> 16);
            U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), value);
            return prevValue;
        }
    }

    @Override
    public short put(short key, short value) {
        short free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacityMask, index;
        short cur;
        int entry;
        if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = ((((int) key) & SHORT_MASK) | (((int) value) << 16));
            postInsertHook();
            return defaultValue();
        } else {
            keyPresent:
            if (cur != key) {
                while (true) {
                    if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[index] = ((((int) key) & SHORT_MASK) | (((int) value) << 16));
                        postInsertHook();
                        return defaultValue();
                    } else if (cur == key) {
                        break keyPresent;
                    }
                }
            }
            // key is present
            short prevValue = (short) (entry >>> 16);
            U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), value);
            return prevValue;
        }
    }

    @Override
    public Short putIfAbsent(Short key, Short value) {
        short k = key;
        short free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacityMask, index;
        short cur;
        int entry;
        if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = ((((int) k) & SHORT_MASK) | (((int) value) << 16));
            postInsertHook();
            return null;
        } else {
            if (cur == k) {
                // key is present
                return (short) (entry >>> 16);
            } else {
                while (true) {
                    if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[index] = ((((int) k) & SHORT_MASK) | (((int) value) << 16));
                        postInsertHook();
                        return null;
                    } else if (cur == k) {
                        // key is present
                        return (short) (entry >>> 16);
                    }
                }
            }
        }
    }

    @Override
    public short putIfAbsent(short key, short value) {
        short free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacityMask, index;
        short cur;
        int entry;
        if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = ((((int) key) & SHORT_MASK) | (((int) value) << 16));
            postInsertHook();
            return defaultValue();
        } else {
            if (cur == key) {
                // key is present
                return (short) (entry >>> 16);
            } else {
                while (true) {
                    if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[index] = ((((int) key) & SHORT_MASK) | (((int) value) << 16));
                        postInsertHook();
                        return defaultValue();
                    } else if (cur == key) {
                        // key is present
                        return (short) (entry >>> 16);
                    }
                }
            }
        }
    }

    @Override
    public void justPut(short key, short value) {
        int index = insert(key, value);
        if (index < 0) {
            // key was absent
            return;
        } else {
            // key is present
            U.putShort(table, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), value);
            return;
        }
    }


    @Override
    public Short compute(Short key,
            BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        short k = key;
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        short free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacityMask, index;
        short cur;
        int entry;
        keyPresent:
        if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) != k) {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        break keyPresent;
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            Short newValue = remappingFunction.apply(k, null);
            if (newValue != null) {
                incrementModCount();
                tab[index] = ((((int) k) & SHORT_MASK) | (((int) newValue) << 16));
                postInsertHook();
                return newValue;
            } else {
                return null;
            }
        }
        // key is present
        Short newValue = remappingFunction.apply(k, (short) (entry >>> 16));
        if (newValue != null) {
            U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), newValue);
            return newValue;
        } else {
            removeAt(index);
            return null;
        }
    }


    @Override
    public short compute(short key, ShortShortToShortFunction remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        short free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacityMask, index;
        short cur;
        int entry;
        keyPresent:
        if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) != key) {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                        break keyPresent;
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            short newValue = remappingFunction.applyAsShort(key, defaultValue());
            incrementModCount();
            tab[index] = ((((int) key) & SHORT_MASK) | (((int) newValue) << 16));
            postInsertHook();
            return newValue;
        }
        // key is present
        short newValue = remappingFunction.applyAsShort(key, (short) (entry >>> 16));
        U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), newValue);
        return newValue;
    }


    @Override
    public Short computeIfAbsent(Short key,
            Function<? super Short, ? extends Short> mappingFunction) {
        short k = key;
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        short free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacityMask, index;
        short cur;
        int entry;
        if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
            // key is present
            return (short) (entry >>> 16);
        } else {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        // key is present
                        return (short) (entry >>> 16);
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            Short value = mappingFunction.apply(k);
            if (value != null) {
                incrementModCount();
                tab[index] = ((((int) k) & SHORT_MASK) | (((int) value) << 16));
                postInsertHook();
                return value;
            } else {
                return null;
            }
        }
    }


    @Override
    public short computeIfAbsent(short key, ShortUnaryOperator mappingFunction) {
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        short free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacityMask, index;
        short cur;
        int entry;
        if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
            // key is present
            return (short) (entry >>> 16);
        } else {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                        // key is present
                        return (short) (entry >>> 16);
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            short value = mappingFunction.applyAsShort(key);
            incrementModCount();
            tab[index] = ((((int) key) & SHORT_MASK) | (((int) value) << 16));
            postInsertHook();
            return value;
        }
    }


    @Override
    public Short computeIfPresent(Short key,
            BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        short k = key;
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        short free;
        if (k != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            short cur;
            int entry;
            keyPresent:
            if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) != k) {
                if (cur == free) {
                    // key is absent
                    return null;
                } else {
                    while (true) {
                        if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return null;
                        }
                    }
                }
            }
            // key is present
            Short newValue = remappingFunction.apply(k, (short) (entry >>> 16));
            if (newValue != null) {
                U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), newValue);
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
    public short computeIfPresent(short key, ShortShortToShortFunction remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        short free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            short cur;
            int entry;
            keyPresent:
            if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) != key) {
                if (cur == free) {
                    // key is absent
                    return defaultValue();
                } else {
                    while (true) {
                        if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue();
                        }
                    }
                }
            }
            // key is present
            short newValue = remappingFunction.applyAsShort(key, (short) (entry >>> 16));
            U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), newValue);
            return newValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Short merge(Short key, Short value,
            BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        short k = key;
        if (value == null)
            throw new java.lang.NullPointerException();
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        short free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacityMask, index;
        short cur;
        int entry;
        keyPresent:
        if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) != k) {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                        break keyPresent;
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            incrementModCount();
            tab[index] = ((((int) k) & SHORT_MASK) | (((int) value) << 16));
            postInsertHook();
            return value;
        }
        // key is present
        Short newValue = remappingFunction.apply((short) (entry >>> 16), value);
        if (newValue != null) {
            U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), newValue);
            return newValue;
        } else {
            removeAt(index);
            return null;
        }
    }


    @Override
    public short merge(short key, short value, ShortBinaryOperator remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        short free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacityMask, index;
        short cur;
        int entry;
        keyPresent:
        if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) != key) {
            keyAbsent:
            if (cur != free) {
                while (true) {
                    if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                        break keyPresent;
                    } else if (cur == free) {
                        break keyAbsent;
                    }
                }
            }
            // key is absent
            incrementModCount();
            tab[index] = ((((int) key) & SHORT_MASK) | (((int) value) << 16));
            postInsertHook();
            return value;
        }
        // key is present
        short newValue = remappingFunction.applyAsShort((short) (entry >>> 16), value);
        U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), newValue);
        return newValue;
    }


    @Override
    public short addValue(short key, short value) {
        short free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacityMask, index;
        short cur;
        int entry;
        if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = ((((int) key) & SHORT_MASK) | (((int) value) << 16));
            postInsertHook();
            return value;
        } else {
            keyPresent:
            if (cur != key) {
                while (true) {
                    if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[index] = ((((int) key) & SHORT_MASK) | (((int) value) << 16));
                        postInsertHook();
                        return value;
                    } else if (cur == key) {
                        break keyPresent;
                    }
                }
            }
            // key is present
            short newValue = (short) ((short) (entry >>> 16) + value);
            U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), newValue);
            return newValue;
        }
    }

    @Override
    public short addValue(short key, short addition, short defaultValue) {
        short value = (short) (defaultValue + addition);
        short free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacityMask, index;
        short cur;
        int entry;
        if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = ((((int) key) & SHORT_MASK) | (((int) value) << 16));
            postInsertHook();
            return value;
        } else {
            keyPresent:
            if (cur != key) {
                while (true) {
                    if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[index] = ((((int) key) & SHORT_MASK) | (((int) value) << 16));
                        postInsertHook();
                        return value;
                    } else if (cur == key) {
                        break keyPresent;
                    }
                }
            }
            // key is present
            short newValue = (short) ((short) (entry >>> 16) + addition);
            U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), newValue);
            return newValue;
        }
    }


    @Override
    public void putAll(@Nonnull Map<? extends Short, ? extends Short> m) {
        CommonShortShortMapOps.putAll(this, m);
    }


    @Override
    public Short replace(Short key, Short value) {
        short k = key;
        short free;
        if (k != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            short cur;
            int entry;
            keyPresent:
            if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) != k) {
                if (cur == free) {
                    // key is absent
                    return null;
                } else {
                    while (true) {
                        if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return null;
                        }
                    }
                }
            }
            // key is present
            short oldValue = (short) (entry >>> 16);
            U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), value);
            return oldValue;
        } else {
            // key is absent
            return null;
        }
    }

    @Override
    public short replace(short key, short value) {
        short free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            short cur;
            int entry;
            keyPresent:
            if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) != key) {
                if (cur == free) {
                    // key is absent
                    return defaultValue();
                } else {
                    while (true) {
                        if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue();
                        }
                    }
                }
            }
            // key is present
            short oldValue = (short) (entry >>> 16);
            U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), value);
            return oldValue;
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public boolean replace(Short key, Short oldValue, Short newValue) {
        return replace(key.shortValue(),
                oldValue.shortValue(),
                newValue.shortValue());
    }

    @Override
    public boolean replace(short key, short oldValue, short newValue) {
        short free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            short cur;
            int entry;
            keyPresent:
            if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) != key) {
                if (cur == free) {
                    // key is absent
                    return false;
                } else {
                    while (true) {
                        if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return false;
                        }
                    }
                }
            }
            // key is present
            if ((short) (entry >>> 16) == oldValue) {
                U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), newValue);
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
            BiFunction<? super Short, ? super Short, ? extends Short> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), function.apply(key, (short) (entry >>> 16)));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void replaceAll(ShortShortToShortFunction function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), function.applyAsShort(key, (short) (entry >>> 16)));
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
        short free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int entry;
        incrementModCount();
        int indexToRemove = index;
        int indexToShift = indexToRemove;
        int shiftDistance = 1;
        while (true) {
            indexToShift = (indexToShift - 1) & capacityMask;
            short keyToShift;
            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                break;
            }
            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
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
        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
        postRemoveHook();
    }

    @Override
    public Short remove(Object key) {
        short k = (Short) key;
        short free;
        if (k != (free = freeValue)) {
            int[] tab = table;
            int capacityMask = tab.length - 1;
            int index;
            short cur;
            int entry;
            keyPresent:
            if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(k) & capacityMask])) != k) {
                if (cur == free) {
                    // key is absent
                    return null;
                } else {
                    while (true) {
                        if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return null;
                        }
                    }
                }
            }
            // key is present
            short val = (short) (entry >>> 16);
            incrementModCount();
            int indexToRemove = index;
            int indexToShift = indexToRemove;
            int shiftDistance = 1;
            while (true) {
                indexToShift = (indexToShift - 1) & capacityMask;
                short keyToShift;
                if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                    break;
                }
                if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
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
            U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
            postRemoveHook();
            return val;
        } else {
            // key is absent
            return null;
        }
    }


    @Override
    public boolean justRemove(short key) {
        short free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacityMask = tab.length - 1;
            int index;
            short cur;
            int entry;
            keyPresent:
            if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(key) & capacityMask])) != key) {
                if (cur == free) {
                    // key is absent
                    return false;
                } else {
                    while (true) {
                        if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
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
                short keyToShift;
                if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                    break;
                }
                if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
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
            U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
            postRemoveHook();
            return true;
        } else {
            // key is absent
            return false;
        }
    }



    

    @Override
    public short remove(short key) {
        short free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacityMask = tab.length - 1;
            int index;
            short cur;
            int entry;
            keyPresent:
            if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(key) & capacityMask])) != key) {
                if (cur == free) {
                    // key is absent
                    return defaultValue();
                } else {
                    while (true) {
                        if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue();
                        }
                    }
                }
            }
            // key is present
            short val = (short) (entry >>> 16);
            incrementModCount();
            int indexToRemove = index;
            int indexToShift = indexToRemove;
            int shiftDistance = 1;
            while (true) {
                indexToShift = (indexToShift - 1) & capacityMask;
                short keyToShift;
                if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                    break;
                }
                if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
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
            U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
            postRemoveHook();
            return val;
        } else {
            // key is absent
            return defaultValue();
        }
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Short) key).shortValue(),
                ((Short) value).shortValue()
                );
    }

    @Override
    public boolean remove(short key, short value) {
        short free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacityMask = tab.length - 1;
            int index;
            short cur;
            int entry;
            keyPresent:
            if ((cur = (short) (entry = tab[index = ParallelKVShortKeyMixing.mix(key) & capacityMask])) != key) {
                if (cur == free) {
                    // key is absent
                    return false;
                } else {
                    while (true) {
                        if ((cur = (short) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return false;
                        }
                    }
                }
            }
            // key is present
            if ((short) (entry >>> 16) == value) {
                incrementModCount();
                int indexToRemove = index;
                int indexToShift = indexToRemove;
                int shiftDistance = 1;
                while (true) {
                    indexToShift = (indexToShift - 1) & capacityMask;
                    short keyToShift;
                    if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                        break;
                    }
                    if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
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
                U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
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
    public boolean removeIf(ShortShortPredicate filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        short delayedRemoved = (short) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
                if (filter.test(key, (short) (entry >>> 16))) {
                    incrementModCount();
                    mc++;
                    closeDeletion:
                    if (firstDelayedRemoved < 0) {
                        int indexToRemove = i;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
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
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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




    // under this condition - operations, overridden from MutableParallelKVShortLHashGO
    // when values are objects - in order to set values to null on removing (for garbage collection)
    // when algo is LHash - because shift deletion should shift values to

    @Override
    public boolean removeIf(Predicate<? super Short> filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        short delayedRemoved = (short) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
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
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
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
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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
    public boolean removeIf(ShortPredicate filter) {
        if (filter == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        short delayedRemoved = (short) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
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
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
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
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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
    public boolean removeAll(@Nonnull HashShortSet thisC, @Nonnull Collection<?> c) {
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        short delayedRemoved = (short) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
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
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
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
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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
    boolean removeAll(@Nonnull HashShortSet thisC, @Nonnull ShortCollection c) {
        if (thisC == c)
            throw new IllegalArgumentException();
        if (isEmpty() || c.isEmpty())
            return false;
        boolean changed = false;
        int mc = modCount();
        short free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        short delayedRemoved = (short) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
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
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
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
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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
    public boolean retainAll(@Nonnull HashShortSet thisC, @Nonnull Collection<?> c) {
        if (c instanceof ShortCollection)
            return retainAll(thisC, (ShortCollection) c);
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
        short free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        short delayedRemoved = (short) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
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
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
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
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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

    private boolean retainAll(@Nonnull HashShortSet thisC, @Nonnull ShortCollection c) {
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
        short free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int firstDelayedRemoved = -1;
        short delayedRemoved = (short) 0;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            short key;
            if ((key = (short) (entry = tab[i])) != free) {
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
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (indexToShift > indexToRemove) {
                                    firstDelayedRemoved = i;
                                    delayedRemoved = key;
                                    U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
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
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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
            , short delayedRemoved) {
        short free = freeValue;
        int[] tab = table;
        int capacityMask = tab.length - 1;
        int entry;
        for (int i = firstDelayedRemoved; i >= 0; i--) {
            if ((short) (entry = tab[i]) == delayedRemoved) {
                int indexToRemove = i;
                int indexToShift = indexToRemove;
                int shiftDistance = 1;
                while (true) {
                    indexToShift = (indexToShift - 1) & capacityMask;
                    short keyToShift;
                    if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                        break;
                    }
                    if ((keyToShift != delayedRemoved) && (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance)) {
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
                U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                postRemoveHook();
            }
        }
    }



    @Override
    public ShortIterator iterator() {
        int mc = modCount();
        return new NoRemovedKeyIterator(mc);
    }

    @Override
    public ShortCursor setCursor() {
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
                    int entry;
                    int[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 1) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putShort(this.tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
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
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        justRemove(U.getShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (index)) << INT_SCALE_SHIFT)));
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
            short curKey;
            short free;
            if ((curKey = this.curKey) != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    int entry;
                    int index = this.index;
                    int[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putShort(this.tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
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
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
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





    class EntryView extends AbstractSetView<Map.Entry<Short, Short>>
            implements HashObjSet<Map.Entry<Short, Short>>,
            InternalObjCollectionOps<Map.Entry<Short, Short>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Short, Short>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Short>defaultEquality()
                    ,
                    Equivalence.<Short>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return MutableLHashParallelKVShortShortMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return MutableLHashParallelKVShortShortMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return MutableLHashParallelKVShortShortMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Short, Short> e = (Map.Entry<Short, Short>) o;
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
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    result[resultIndex++] = new MutableEntry(mc, i, key, (short) (entry >>> 16));
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
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    a[resultIndex++] = (T) new MutableEntry(mc, i, key, (short) (entry >>> 16));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }

        @Override
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Short, Short>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    action.accept(new MutableEntry(mc, i, key, (short) (entry >>> 16)));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Short, Short>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    if (!predicate.test(new MutableEntry(mc, i, key, (short) (entry >>> 16)))) {
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
        public ObjIterator<Map.Entry<Short, Short>> iterator() {
            int mc = modCount();
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Short, Short>> cursor() {
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
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    if (!c.contains(e.with(key, (short) (entry >>> 16)))) {
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
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    changed |= s.remove(e.with(key, (short) (entry >>> 16)));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Short, Short>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    changed |= c.add(new MutableEntry(mc, i, key, (short) (entry >>> 16)));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        public int hashCode() {
            return MutableLHashParallelKVShortShortMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    sb.append(' ');
                    sb.append(key);
                    sb.append('=');
                    sb.append((short) (entry >>> 16));
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
            return MutableLHashParallelKVShortShortMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Short, Short> e = (Map.Entry<Short, Short>) o;
                short key = e.getKey();
                short value = e.getValue();
                return MutableLHashParallelKVShortShortMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Short, Short>> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            short free = freeValue;
            int[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            short delayedRemoved = (short) 0;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    if (filter.test(new MutableEntry(mc, i, key, (short) (entry >>> 16)))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                short keyToShift;
                                if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                    break;
                                }
                                if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
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
                            U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                            postRemoveHook();
                        } else {
                            U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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
            short free = freeValue;
            int[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            short delayedRemoved = (short) 0;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    if (c.contains(e.with(key, (short) (entry >>> 16)))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                short keyToShift;
                                if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                    break;
                                }
                                if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
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
                            U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                            postRemoveHook();
                        } else {
                            U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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
            short free = freeValue;
            int[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            short delayedRemoved = (short) 0;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    if (!c.contains(e.with(key, (short) (entry >>> 16)))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                short keyToShift;
                                if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                    break;
                                }
                                if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
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
                            U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                            postRemoveHook();
                        } else {
                            U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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
            MutableLHashParallelKVShortShortMapGO.this.clear();
        }
    }


    abstract class ShortShortEntry extends AbstractEntry<Short, Short> {

        abstract short key();

        @Override
        public final Short getKey() {
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
            short k2;
            short v2;
            try {
                e2 = (Map.Entry) o;
                k2 = (Short) e2.getKey();
                v2 = (Short) e2.getValue();
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


    class MutableEntry extends ShortShortEntry {
        final int modCount;
        private final int index;
        final short key;
        private short value;

        MutableEntry(int modCount, int index, short key, short value) {
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
            U.putShort(
                    table, INT_BASE + SHORT_VALUE_OFFSET + (((long) index) << INT_SCALE_SHIFT),
                    newValue);
        }
    }



    class ReusableEntry extends ShortShortEntry {
        private short key;
        private short value;

        ReusableEntry with(short key, short value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public short key() {
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
            return MutableLHashParallelKVShortShortMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return MutableLHashParallelKVShortShortMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return MutableLHashParallelKVShortShortMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(short v) {
            return MutableLHashParallelKVShortShortMapGO.this.containsValue(v);
        }



        @Override
        public void forEach(Consumer<? super Short> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    action.accept((short) (entry >>> 16));
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
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    action.accept((short) (entry >>> 16));
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
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    if (!predicate.test((short) (entry >>> 16))) {
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
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    if (!c.contains((short) (entry >>> 16))) {
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
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    changed |= c.add((short) (entry >>> 16));
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
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    changed |= s.removeShort((short) (entry >>> 16));
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
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    result[resultIndex++] = (short) (entry >>> 16);
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
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    a[resultIndex++] = (T) Short.valueOf((short) (entry >>> 16));
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
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    result[resultIndex++] = (short) (entry >>> 16);
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
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    a[resultIndex++] = (short) (entry >>> 16);
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
            short free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    sb.append(' ').append((short) (entry >>> 16)).append(',');
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
            MutableLHashParallelKVShortShortMapGO.this.clear();
        }

        
        public boolean removeIf(Predicate<? super Short> filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            short free = freeValue;
            int[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            short delayedRemoved = (short) 0;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    if (filter.test((short) (entry >>> 16))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                short keyToShift;
                                if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                    break;
                                }
                                if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
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
                            U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                            postRemoveHook();
                        } else {
                            U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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
        public boolean removeIf(ShortPredicate filter) {
            if (filter == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            short free = freeValue;
            int[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            short delayedRemoved = (short) 0;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    if (filter.test((short) (entry >>> 16))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                short keyToShift;
                                if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                    break;
                                }
                                if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
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
                            U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                            postRemoveHook();
                        } else {
                            U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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
            if (c instanceof ShortCollection)
                return removeAll((ShortCollection) c);
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            short free = freeValue;
            int[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            short delayedRemoved = (short) 0;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    if (c.contains((short) (entry >>> 16))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                short keyToShift;
                                if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                    break;
                                }
                                if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
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
                            U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                            postRemoveHook();
                        } else {
                            U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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

        private boolean removeAll(ShortCollection c) {
            if (this == c)
                throw new IllegalArgumentException();
            if (isEmpty() || c.isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            short free = freeValue;
            int[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            short delayedRemoved = (short) 0;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    if (c.contains((short) (entry >>> 16))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                short keyToShift;
                                if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                    break;
                                }
                                if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
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
                            U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                            postRemoveHook();
                        } else {
                            U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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
            short free = freeValue;
            int[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            short delayedRemoved = (short) 0;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    if (!c.contains((short) (entry >>> 16))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                short keyToShift;
                                if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                    break;
                                }
                                if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
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
                            U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                            postRemoveHook();
                        } else {
                            U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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
            short free = freeValue;
            int[] tab = table;
            int capacityMask = tab.length - 1;
            int firstDelayedRemoved = -1;
            short delayedRemoved = (short) 0;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    if (!c.contains((short) (entry >>> 16))) {
                        incrementModCount();
                        mc++;
                        closeDeletion:
                        if (firstDelayedRemoved < 0) {
                            int indexToRemove = i;
                            int indexToShift = indexToRemove;
                            int shiftDistance = 1;
                            while (true) {
                                indexToShift = (indexToShift - 1) & capacityMask;
                                short keyToShift;
                                if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                    break;
                                }
                                if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                    if (indexToShift > indexToRemove) {
                                        firstDelayedRemoved = i;
                                        delayedRemoved = key;
                                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), key);
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
                            U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                            postRemoveHook();
                        } else {
                            U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), delayedRemoved);
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Short, Short>> {
        int[] tab;
        final short free;
        final int capacityMask;
        int expectedModCount;
        
        class MutableEntry2 extends MutableEntry {
            MutableEntry2(int modCount, int index, short key, short value) {
                super(modCount, index, key, value);
            }
            
            @Override
            void updateValueInTable(short newValue) {
                if (tab == table) {
                    U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), newValue);
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
            int[] tab = this.tab = table;
            capacityMask = tab.length - 1;
            short free = this.free = freeValue;
            int nextI = tab.length;
            int entry;
            while (--nextI >= 0) {
                short key;
                if ((key = (short) (entry = tab[nextI])) != free) {
                    next = new MutableEntry2(mc, nextI, key, (short) (entry >>> 16));
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Short, Short>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            int[] tab = this.tab;
            short free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    action.accept(new MutableEntry2(mc, i, key, (short) (entry >>> 16)));
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
        public Map.Entry<Short, Short> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
                    index = nextI;
                    int[] tab = this.tab;
                    short free = this.free;
                    MutableEntry prev = next;
                    int entry;
                    while (--nextI >= 0) {
                        short key;
                        if ((key = (short) (entry = tab[nextI])) != free) {
                            next = new MutableEntry2(mc, nextI, key, (short) (entry >>> 16));
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
                    int entry;
                    int[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 1) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putShort(this.tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.nextIndex = index;
                                        if (indexToShift < index - 1) {
                                            this.next = new MutableEntry2(modCount(), indexToShift, keyToShift, (short) (entry >>> 16));
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
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        justRemove(U.getShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (index)) << INT_SCALE_SHIFT)));
                    }
                } else {
                    throw new java.util.ConcurrentModificationException();
                }
            } else {
                throw new java.lang.IllegalStateException();
            }
        }
    }


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Short, Short>> {
        int[] tab;
        final short free;
        final int capacityMask;
        int expectedModCount;
        
        class MutableEntry2 extends MutableEntry {
            MutableEntry2(int modCount, int index, short key, short value) {
                super(modCount, index, key, value);
            }
            
            @Override
            void updateValueInTable(short newValue) {
                if (tab == table) {
                    U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), newValue);
                } else {
                    justPut(key, newValue);
                    if (this.modCount != modCount()) {
                        throw new java.lang.IllegalStateException();
                    }
                }
            }
        }
        
        int index;
        short curKey;
        short curValue;

        NoRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            int[] tab = this.tab = table;
            capacityMask = tab.length - 1;
            index = tab.length;
            short free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Short, Short>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            int[] tab = this.tab;
            short free = this.free;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    action.accept(new MutableEntry2(mc, i, key, (short) (entry >>> 16)));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public Map.Entry<Short, Short> elem() {
            short curKey;
            if ((curKey = this.curKey) != free) {
                return new MutableEntry2(expectedModCount, index, curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                int[] tab = this.tab;
                short free = this.free;
                int entry;
                for (int i = index - 1; i >= 0; i--) {
                    short key;
                    if ((key = (short) (entry = tab[i])) != free) {
                        index = i;
                        curKey = key;
                        curValue = (short) (entry >>> 16);
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
            short curKey;
            short free;
            if ((curKey = this.curKey) != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    int entry;
                    int index = this.index;
                    int[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putShort(this.tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
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
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
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




    class NoRemovedValueIterator implements ShortIterator {
        int[] tab;
        final short free;
        final int capacityMask;
        int expectedModCount;
        int index = -1;
        int nextIndex;
        short next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            int[] tab = this.tab = table;
            capacityMask = tab.length - 1;
            short free = this.free = freeValue;
            int nextI = tab.length;
            int entry;
            while (--nextI >= 0) {
                if ((short) (entry = tab[nextI]) != free) {
                    next = (short) (entry >>> 16);
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
                    int[] tab = this.tab;
                    short free = this.free;
                    short prev = next;
                    int entry;
                    while (--nextI >= 0) {
                        if ((short) (entry = tab[nextI]) != free) {
                            next = (short) (entry >>> 16);
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
            int[] tab = this.tab;
            short free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    action.accept((short) (entry >>> 16));
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
            int[] tab = this.tab;
            short free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    action.accept((short) (entry >>> 16));
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
                    int entry;
                    int[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = nextIndex + 1) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putShort(this.tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                                            }
                                        }
                                    } else if (indexToRemove == index) {
                                        this.nextIndex = index;
                                        if (indexToShift < index - 1) {
                                            this.next = (short) (entry >>> 16);
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
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
                        postRemoveHook();
                    } else {
                        justRemove(U.getShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (index)) << INT_SCALE_SHIFT)));
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
        int[] tab;
        final short free;
        final int capacityMask;
        int expectedModCount;
        int index;
        short curKey;
        short curValue;

        NoRemovedValueCursor(int mc) {
            expectedModCount = mc;
            int[] tab = this.tab = table;
            capacityMask = tab.length - 1;
            index = tab.length;
            short free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ShortConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            int[] tab = this.tab;
            short free = this.free;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                if ((short) (entry = tab[i]) != free) {
                    action.accept((short) (entry >>> 16));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public short elem() {
            if (curKey != free) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                int[] tab = this.tab;
                short free = this.free;
                int entry;
                for (int i = index - 1; i >= 0; i--) {
                    short key;
                    if ((key = (short) (entry = tab[i])) != free) {
                        index = i;
                        curKey = key;
                        curValue = (short) (entry >>> 16);
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
            short curKey;
            short free;
            if ((curKey = this.curKey) != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    int entry;
                    int index = this.index;
                    int[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putShort(this.tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
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
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
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



    class NoRemovedMapCursor implements ShortShortCursor {
        int[] tab;
        final short free;
        final int capacityMask;
        int expectedModCount;
        int index;
        short curKey;
        short curValue;

        NoRemovedMapCursor(int mc) {
            expectedModCount = mc;
            int[] tab = this.tab = table;
            capacityMask = tab.length - 1;
            index = tab.length;
            short free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ShortShortConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            int[] tab = this.tab;
            short free = this.free;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                short key;
                if ((key = (short) (entry = tab[i])) != free) {
                    action.accept(key, (short) (entry >>> 16));
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
        public short value() {
            if (curKey != free) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(short value) {
            if (curKey != free) {
                if (expectedModCount == modCount()) {
                    U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), value);
                    if (tab != table) {
                        U.putShort(table, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), value);
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
                int[] tab = this.tab;
                short free = this.free;
                int entry;
                for (int i = index - 1; i >= 0; i--) {
                    short key;
                    if ((key = (short) (entry = tab[i])) != free) {
                        index = i;
                        curKey = key;
                        curValue = (short) (entry >>> 16);
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
            short curKey;
            short free;
            if ((curKey = this.curKey) != (free = this.free)) {
                if (expectedModCount++ == modCount()) {
                    this.curKey = free;
                    int entry;
                    int index = this.index;
                    int[] tab = this.tab;
                    if (tab == table) {
                        int capacityMask = this.capacityMask;
                        incrementModCount();
                        int indexToRemove = index;
                        int indexToShift = indexToRemove;
                        int shiftDistance = 1;
                        while (true) {
                            indexToShift = (indexToShift - 1) & capacityMask;
                            short keyToShift;
                            if ((keyToShift = (short) (entry = tab[indexToShift])) == free) {
                                break;
                            }
                            if (((ParallelKVShortKeyMixing.mix(keyToShift) - indexToShift) & capacityMask) >= shiftDistance) {
                                if (this.tab == tab) {
                                    if (indexToShift > indexToRemove) {
                                        int slotsToCopy;
                                        if ((slotsToCopy = index) > 0) {
                                            this.tab = Arrays.copyOf(tab, slotsToCopy);
                                            if (indexToRemove < slotsToCopy) {
                                                U.putShort(this.tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
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
                        U.putShort(tab, INT_BASE + SHORT_KEY_OFFSET + (((long) (indexToRemove)) << INT_SCALE_SHIFT), free);
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

