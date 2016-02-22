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
import net.openhft.koloboke.function.CharPredicate;
import net.openhft.koloboke.function.CharShortConsumer;
import net.openhft.koloboke.function.CharShortPredicate;
import net.openhft.koloboke.function.CharShortToShortFunction;
import net.openhft.koloboke.function.CharToShortFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.openhft.koloboke.function.ShortBinaryOperator;
import net.openhft.koloboke.function.ShortConsumer;
import net.openhft.koloboke.function.ShortPredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class UpdatableQHashParallelKVCharShortMapGO
        extends UpdatableQHashParallelKVCharShortMapSO {

    
    final void copy(ParallelKVCharShortQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    
    final void move(ParallelKVCharShortQHash hash) {
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
    public boolean containsEntry(char key, short value) {
        char free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacity, index;
            char cur;
            int entry;
            if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(key) % (capacity = tab.length)])) == key) {
                // key is present
                return (short) (entry >>> 16) == value;
            } else {
                if (cur == free) {
                    // key is absent
                    return false;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (char) (entry = tab[bIndex])) == key) {
                            // key is present
                            return (short) (entry >>> 16) == value;
                        } else if (cur == free) {
                            // key is absent
                            return false;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (char) (entry = tab[fIndex])) == key) {
                            // key is present
                            return (short) (entry >>> 16) == value;
                        } else if (cur == free) {
                            // key is absent
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
    public Short get(Object key) {
        char k = (Character) key;
        char free;
        if (k != (free = freeValue)) {
            int[] tab = table;
            int capacity, index;
            char cur;
            int entry;
            if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(k) % (capacity = tab.length)])) == k) {
                // key is present
                return (short) (entry >>> 16);
            } else {
                if (cur == free) {
                    // key is absent
                    return null;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (char) (entry = tab[bIndex])) == k) {
                            // key is present
                            return (short) (entry >>> 16);
                        } else if (cur == free) {
                            // key is absent
                            return null;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (char) (entry = tab[fIndex])) == k) {
                            // key is present
                            return (short) (entry >>> 16);
                        } else if (cur == free) {
                            // key is absent
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
    public short get(char key) {
        char free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacity, index;
            char cur;
            int entry;
            if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(key) % (capacity = tab.length)])) == key) {
                // key is present
                return (short) (entry >>> 16);
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue();
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (char) (entry = tab[bIndex])) == key) {
                            // key is present
                            return (short) (entry >>> 16);
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue();
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (char) (entry = tab[fIndex])) == key) {
                            // key is present
                            return (short) (entry >>> 16);
                        } else if (cur == free) {
                            // key is absent
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
    public Short getOrDefault(Object key, Short defaultValue) {
        char k = (Character) key;
        char free;
        if (k != (free = freeValue)) {
            int[] tab = table;
            int capacity, index;
            char cur;
            int entry;
            if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(k) % (capacity = tab.length)])) == k) {
                // key is present
                return (short) (entry >>> 16);
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (char) (entry = tab[bIndex])) == k) {
                            // key is present
                            return (short) (entry >>> 16);
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (char) (entry = tab[fIndex])) == k) {
                            // key is present
                            return (short) (entry >>> 16);
                        } else if (cur == free) {
                            // key is absent
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
    public short getOrDefault(char key, short defaultValue) {
        char free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacity, index;
            char cur;
            int entry;
            if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(key) % (capacity = tab.length)])) == key) {
                // key is present
                return (short) (entry >>> 16);
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (char) (entry = tab[bIndex])) == key) {
                            // key is present
                            return (short) (entry >>> 16);
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (char) (entry = tab[fIndex])) == key) {
                            // key is present
                            return (short) (entry >>> 16);
                        } else if (cur == free) {
                            // key is absent
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
    public void forEach(BiConsumer<? super Character, ? super Short> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        char free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
                action.accept(key, (short) (entry >>> 16));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void forEach(CharShortConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        char free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
                action.accept(key, (short) (entry >>> 16));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public boolean forEachWhile(CharShortPredicate predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        int mc = modCount();
        char free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
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
    public CharShortCursor cursor() {
        int mc = modCount();
        return new NoRemovedMapCursor(mc);
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonCharShortMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalCharShortMapOps m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        int mc = modCount();
        char free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
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
    public void reversePutAllTo(InternalCharShortMapOps m) {
        if (isEmpty())
            return;
        int mc = modCount();
        char free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
                m.justPut(key, (short) (entry >>> 16));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<Character, Short>> entrySet() {
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
        char free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
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
        char free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
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
        char free = freeValue;
        int[] tab = table;
        int entry;
        initForRehash(newCapacity);
        mc++; // modCount is incremented in initForRehash()
        int[] newTab = table;
        int capacity = newTab.length;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
                int index;
                if (U.getChar(newTab, INT_BASE + CHAR_KEY_OFFSET + (((long) (index = ParallelKVCharKeyMixing.mix(key) % capacity)) << INT_SCALE_SHIFT)) != free) {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if (U.getChar(newTab, INT_BASE + CHAR_KEY_OFFSET + (((long) (bIndex)) << INT_SCALE_SHIFT)) == free) {
                            index = bIndex;
                            break;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if (U.getChar(newTab, INT_BASE + CHAR_KEY_OFFSET + (((long) (fIndex)) << INT_SCALE_SHIFT)) == free) {
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
    public Short put(Character key, Short value) {
        char k = key;
        char free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacity, index;
        char cur;
        int entry;
        if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(k) % (capacity = tab.length)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = (((int) k) | (((int) value) << 16));
            postInsertHook();
            return null;
        } else {
            keyPresent:
            if (cur != k) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (char) (entry = tab[bIndex])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[bIndex] = (((int) k) | (((int) value) << 16));
                        postInsertHook();
                        return null;
                    } else if (cur == k) {
                        index = bIndex;
                        break keyPresent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (char) (entry = tab[fIndex])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[fIndex] = (((int) k) | (((int) value) << 16));
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
            short prevValue = (short) (entry >>> 16);
            U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), value);
            return prevValue;
        }
    }

    @Override
    public short put(char key, short value) {
        char free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacity, index;
        char cur;
        int entry;
        if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(key) % (capacity = tab.length)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = (((int) key) | (((int) value) << 16));
            postInsertHook();
            return defaultValue();
        } else {
            keyPresent:
            if (cur != key) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (char) (entry = tab[bIndex])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[bIndex] = (((int) key) | (((int) value) << 16));
                        postInsertHook();
                        return defaultValue();
                    } else if (cur == key) {
                        index = bIndex;
                        break keyPresent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (char) (entry = tab[fIndex])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[fIndex] = (((int) key) | (((int) value) << 16));
                        postInsertHook();
                        return defaultValue();
                    } else if (cur == key) {
                        index = fIndex;
                        break keyPresent;
                    }
                    step += 2;
                }
            }
            // key is present
            short prevValue = (short) (entry >>> 16);
            U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), value);
            return prevValue;
        }
    }

    @Override
    public Short putIfAbsent(Character key, Short value) {
        char k = key;
        char free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacity, index;
        char cur;
        int entry;
        if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(k) % (capacity = tab.length)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = (((int) k) | (((int) value) << 16));
            postInsertHook();
            return null;
        } else {
            if (cur == k) {
                // key is present
                return (short) (entry >>> 16);
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (char) (entry = tab[bIndex])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[bIndex] = (((int) k) | (((int) value) << 16));
                        postInsertHook();
                        return null;
                    } else if (cur == k) {
                        // key is present
                        return (short) (entry >>> 16);
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (char) (entry = tab[fIndex])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[fIndex] = (((int) k) | (((int) value) << 16));
                        postInsertHook();
                        return null;
                    } else if (cur == k) {
                        // key is present
                        return (short) (entry >>> 16);
                    }
                    step += 2;
                }
            }
        }
    }

    @Override
    public short putIfAbsent(char key, short value) {
        char free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacity, index;
        char cur;
        int entry;
        if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(key) % (capacity = tab.length)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = (((int) key) | (((int) value) << 16));
            postInsertHook();
            return defaultValue();
        } else {
            if (cur == key) {
                // key is present
                return (short) (entry >>> 16);
            } else {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (char) (entry = tab[bIndex])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[bIndex] = (((int) key) | (((int) value) << 16));
                        postInsertHook();
                        return defaultValue();
                    } else if (cur == key) {
                        // key is present
                        return (short) (entry >>> 16);
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (char) (entry = tab[fIndex])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[fIndex] = (((int) key) | (((int) value) << 16));
                        postInsertHook();
                        return defaultValue();
                    } else if (cur == key) {
                        // key is present
                        return (short) (entry >>> 16);
                    }
                    step += 2;
                }
            }
        }
    }

    @Override
    public void justPut(char key, short value) {
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
    public Short compute(Character key,
            BiFunction<? super Character, ? super Short, ? extends Short> remappingFunction) {
        char k = key;
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        char free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacity, index;
        char cur;
        int entry;
        keyPresent:
        if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
            keyAbsent:
            if (cur != free) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (char) (entry = tab[bIndex])) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == free) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (char) (entry = tab[fIndex])) == k) {
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
            Short newValue = remappingFunction.apply(k, null);
            if (newValue != null) {
                incrementModCount();
                tab[index] = (((int) k) | (((int) newValue) << 16));
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
            throw new java.lang.UnsupportedOperationException("Compute operation of updatable map doesn't support removals");
        }
    }


    @Override
    public short compute(char key, CharShortToShortFunction remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        char free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacity, index;
        char cur;
        int entry;
        keyPresent:
        if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(key) % (capacity = tab.length)])) != key) {
            keyAbsent:
            if (cur != free) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (char) (entry = tab[bIndex])) == key) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == free) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (char) (entry = tab[fIndex])) == key) {
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
            short newValue = remappingFunction.applyAsShort(key, defaultValue());
            incrementModCount();
            tab[index] = (((int) key) | (((int) newValue) << 16));
            postInsertHook();
            return newValue;
        }
        // key is present
        short newValue = remappingFunction.applyAsShort(key, (short) (entry >>> 16));
        U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), newValue);
        return newValue;
    }


    @Override
    public Short computeIfAbsent(Character key,
            Function<? super Character, ? extends Short> mappingFunction) {
        char k = key;
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        char free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacity, index;
        char cur;
        int entry;
        if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(k) % (capacity = tab.length)])) == k) {
            // key is present
            return (short) (entry >>> 16);
        } else {
            keyAbsent:
            if (cur != free) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (char) (entry = tab[bIndex])) == k) {
                        // key is present
                        return (short) (entry >>> 16);
                    } else if (cur == free) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (char) (entry = tab[fIndex])) == k) {
                        // key is present
                        return (short) (entry >>> 16);
                    } else if (cur == free) {
                        index = fIndex;
                        break keyAbsent;
                    }
                    step += 2;
                }
            }
            // key is absent
            Short value = mappingFunction.apply(k);
            if (value != null) {
                incrementModCount();
                tab[index] = (((int) k) | (((int) value) << 16));
                postInsertHook();
                return value;
            } else {
                return null;
            }
        }
    }


    @Override
    public short computeIfAbsent(char key, CharToShortFunction mappingFunction) {
        if (mappingFunction == null)
            throw new java.lang.NullPointerException();
        char free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacity, index;
        char cur;
        int entry;
        if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(key) % (capacity = tab.length)])) == key) {
            // key is present
            return (short) (entry >>> 16);
        } else {
            keyAbsent:
            if (cur != free) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (char) (entry = tab[bIndex])) == key) {
                        // key is present
                        return (short) (entry >>> 16);
                    } else if (cur == free) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (char) (entry = tab[fIndex])) == key) {
                        // key is present
                        return (short) (entry >>> 16);
                    } else if (cur == free) {
                        index = fIndex;
                        break keyAbsent;
                    }
                    step += 2;
                }
            }
            // key is absent
            short value = mappingFunction.applyAsShort(key);
            incrementModCount();
            tab[index] = (((int) key) | (((int) value) << 16));
            postInsertHook();
            return value;
        }
    }


    @Override
    public Short computeIfPresent(Character key,
            BiFunction<? super Character, ? super Short, ? extends Short> remappingFunction) {
        char k = key;
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        char free;
        if (k != (free = freeValue)) {
            int[] tab = table;
            int capacity, index;
            char cur;
            int entry;
            keyPresent:
            if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
                if (cur == free) {
                    // key is absent
                    return null;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (char) (entry = tab[bIndex])) == k) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return null;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (char) (entry = tab[fIndex])) == k) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return null;
                        }
                        step += 2;
                    }
                }
            }
            // key is present
            Short newValue = remappingFunction.apply(k, (short) (entry >>> 16));
            if (newValue != null) {
                U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), newValue);
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
    public short computeIfPresent(char key, CharShortToShortFunction remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        char free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacity, index;
            char cur;
            int entry;
            keyPresent:
            if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(key) % (capacity = tab.length)])) != key) {
                if (cur == free) {
                    // key is absent
                    return defaultValue();
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (char) (entry = tab[bIndex])) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue();
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (char) (entry = tab[fIndex])) == key) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue();
                        }
                        step += 2;
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
    public Short merge(Character key, Short value,
            BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        char k = key;
        if (value == null)
            throw new java.lang.NullPointerException();
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        char free;
        if (k == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacity, index;
        char cur;
        int entry;
        keyPresent:
        if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
            keyAbsent:
            if (cur != free) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (char) (entry = tab[bIndex])) == k) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == free) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (char) (entry = tab[fIndex])) == k) {
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
            tab[index] = (((int) k) | (((int) value) << 16));
            postInsertHook();
            return value;
        }
        // key is present
        Short newValue = remappingFunction.apply((short) (entry >>> 16), value);
        if (newValue != null) {
            U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), newValue);
            return newValue;
        } else {
            throw new java.lang.UnsupportedOperationException("Merge operation of updatable map doesn't support removals");
        }
    }


    @Override
    public short merge(char key, short value, ShortBinaryOperator remappingFunction) {
        if (remappingFunction == null)
            throw new java.lang.NullPointerException();
        char free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacity, index;
        char cur;
        int entry;
        keyPresent:
        if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(key) % (capacity = tab.length)])) != key) {
            keyAbsent:
            if (cur != free) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (char) (entry = tab[bIndex])) == key) {
                        index = bIndex;
                        break keyPresent;
                    } else if (cur == free) {
                        index = bIndex;
                        break keyAbsent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (char) (entry = tab[fIndex])) == key) {
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
            tab[index] = (((int) key) | (((int) value) << 16));
            postInsertHook();
            return value;
        }
        // key is present
        short newValue = remappingFunction.applyAsShort((short) (entry >>> 16), value);
        U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), newValue);
        return newValue;
    }


    @Override
    public short addValue(char key, short value) {
        char free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacity, index;
        char cur;
        int entry;
        if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(key) % (capacity = tab.length)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = (((int) key) | (((int) value) << 16));
            postInsertHook();
            return value;
        } else {
            keyPresent:
            if (cur != key) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (char) (entry = tab[bIndex])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[bIndex] = (((int) key) | (((int) value) << 16));
                        postInsertHook();
                        return value;
                    } else if (cur == key) {
                        index = bIndex;
                        break keyPresent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (char) (entry = tab[fIndex])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[fIndex] = (((int) key) | (((int) value) << 16));
                        postInsertHook();
                        return value;
                    } else if (cur == key) {
                        index = fIndex;
                        break keyPresent;
                    }
                    step += 2;
                }
            }
            // key is present
            short newValue = (short) ((short) (entry >>> 16) + value);
            U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), newValue);
            return newValue;
        }
    }

    @Override
    public short addValue(char key, short addition, short defaultValue) {
        short value = (short) (defaultValue + addition);
        char free;
        if (key == (free = freeValue)) {
            free = changeFree();
        }
        int[] tab = table;
        int capacity, index;
        char cur;
        int entry;
        if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(key) % (capacity = tab.length)])) == free) {
            // key is absent
            incrementModCount();
            tab[index] = (((int) key) | (((int) value) << 16));
            postInsertHook();
            return value;
        } else {
            keyPresent:
            if (cur != key) {
                int bIndex = index, fIndex = index, step = 1;
                while (true) {
                    if ((bIndex -= step) < 0) bIndex += capacity;
                    if ((cur = (char) (entry = tab[bIndex])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[bIndex] = (((int) key) | (((int) value) << 16));
                        postInsertHook();
                        return value;
                    } else if (cur == key) {
                        index = bIndex;
                        break keyPresent;
                    }
                    int t;
                    if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                    if ((cur = (char) (entry = tab[fIndex])) == free) {
                        // key is absent
                        incrementModCount();
                        tab[fIndex] = (((int) key) | (((int) value) << 16));
                        postInsertHook();
                        return value;
                    } else if (cur == key) {
                        index = fIndex;
                        break keyPresent;
                    }
                    step += 2;
                }
            }
            // key is present
            short newValue = (short) ((short) (entry >>> 16) + addition);
            U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), newValue);
            return newValue;
        }
    }


    @Override
    public void putAll(@Nonnull Map<? extends Character, ? extends Short> m) {
        CommonCharShortMapOps.putAll(this, m);
    }


    @Override
    public Short replace(Character key, Short value) {
        char k = key;
        char free;
        if (k != (free = freeValue)) {
            int[] tab = table;
            int capacity, index;
            char cur;
            int entry;
            keyPresent:
            if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(k) % (capacity = tab.length)])) != k) {
                if (cur == free) {
                    // key is absent
                    return null;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (char) (entry = tab[bIndex])) == k) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return null;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (char) (entry = tab[fIndex])) == k) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return null;
                        }
                        step += 2;
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
    public short replace(char key, short value) {
        char free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacity, index;
            char cur;
            int entry;
            keyPresent:
            if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(key) % (capacity = tab.length)])) != key) {
                if (cur == free) {
                    // key is absent
                    return defaultValue();
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (char) (entry = tab[bIndex])) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue();
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (char) (entry = tab[fIndex])) == key) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue();
                        }
                        step += 2;
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
    public boolean replace(Character key, Short oldValue, Short newValue) {
        return replace(key.charValue(),
                oldValue.shortValue(),
                newValue.shortValue());
    }

    @Override
    public boolean replace(char key, short oldValue, short newValue) {
        char free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacity, index;
            char cur;
            int entry;
            keyPresent:
            if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(key) % (capacity = tab.length)])) != key) {
                if (cur == free) {
                    // key is absent
                    return false;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (char) (entry = tab[bIndex])) == key) {
                            index = bIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return false;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (char) (entry = tab[fIndex])) == key) {
                            index = fIndex;
                            break keyPresent;
                        } else if (cur == free) {
                            // key is absent
                            return false;
                        }
                        step += 2;
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
            BiFunction<? super Character, ? super Short, ? extends Short> function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        char free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
                U.putShort(tab, INT_BASE + SHORT_VALUE_OFFSET + (((long) (i)) << INT_SCALE_SHIFT), function.apply(key, (short) (entry >>> 16)));
            }
        }
        if (mc != modCount())
            throw new java.util.ConcurrentModificationException();
    }

    @Override
    public void replaceAll(CharShortToShortFunction function) {
        if (function == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        int mc = modCount();
        char free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
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
    public Short remove(Object key) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean justRemove(char key) {
        throw new java.lang.UnsupportedOperationException();
    }



    

    @Override
    public short remove(char key) {
        throw new java.lang.UnsupportedOperationException();
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Character) key).charValue(),
                ((Short) value).shortValue()
                );
    }

    @Override
    public boolean remove(char key, short value) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean removeIf(CharShortPredicate filter) {
        throw new java.lang.UnsupportedOperationException();
    }






    class EntryView extends AbstractSetView<Map.Entry<Character, Short>>
            implements HashObjSet<Map.Entry<Character, Short>>,
            InternalObjCollectionOps<Map.Entry<Character, Short>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Character, Short>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Character>defaultEquality()
                    ,
                    Equivalence.<Short>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return UpdatableQHashParallelKVCharShortMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return UpdatableQHashParallelKVCharShortMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return UpdatableQHashParallelKVCharShortMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Character, Short> e = (Map.Entry<Character, Short>) o;
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
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
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
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
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
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Character, Short>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
                    action.accept(new MutableEntry(mc, i, key, (short) (entry >>> 16)));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Character, Short>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            int mc = modCount();
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
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
        public ObjIterator<Map.Entry<Character, Short>> iterator() {
            int mc = modCount();
            return new NoRemovedEntryIterator(mc);
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Character, Short>> cursor() {
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
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
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
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
                    changed |= s.remove(e.with(key, (short) (entry >>> 16)));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Character, Short>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            int mc = modCount();
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
                    changed |= c.add(new MutableEntry(mc, i, key, (short) (entry >>> 16)));
                }
            }
            if (mc != modCount())
                throw new java.util.ConcurrentModificationException();
            return changed;
        }


        public int hashCode() {
            return UpdatableQHashParallelKVCharShortMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            int mc = modCount();
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
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
            return UpdatableQHashParallelKVCharShortMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Character, Short> e = (Map.Entry<Character, Short>) o;
                char key = e.getKey();
                short value = e.getValue();
                return UpdatableQHashParallelKVCharShortMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Character, Short>> filter) {
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
            UpdatableQHashParallelKVCharShortMapGO.this.clear();
        }
    }


    abstract class CharShortEntry extends AbstractEntry<Character, Short> {

        abstract char key();

        @Override
        public final Character getKey() {
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
            char k2;
            short v2;
            try {
                e2 = (Map.Entry) o;
                k2 = (Character) e2.getKey();
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


    class MutableEntry extends CharShortEntry {
        final int modCount;
        private final int index;
        final char key;
        private short value;

        MutableEntry(int modCount, int index, char key, short value) {
            this.modCount = modCount;
            this.index = index;
            this.key = key;
            this.value = value;
        }

        @Override
        public char key() {
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



    class ReusableEntry extends CharShortEntry {
        private char key;
        private short value;

        ReusableEntry with(char key, short value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public char key() {
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
            return UpdatableQHashParallelKVCharShortMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return UpdatableQHashParallelKVCharShortMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return UpdatableQHashParallelKVCharShortMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(short v) {
            return UpdatableQHashParallelKVCharShortMapGO.this.containsValue(v);
        }



        @Override
        public void forEach(Consumer<? super Short> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            int mc = modCount();
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
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
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
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
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
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
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
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
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
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
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
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
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
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
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
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
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
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
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
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
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
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
            UpdatableQHashParallelKVCharShortMapGO.this.clear();
        }

        
        public boolean removeIf(Predicate<? super Short> filter) {
            throw new java.lang.UnsupportedOperationException();
        }

        @Override
        public boolean removeIf(ShortPredicate filter) {
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Character, Short>> {
        final int[] tab;
        final char free;
        int expectedModCount;
        int nextIndex;
        MutableEntry next;

        NoRemovedEntryIterator(int mc) {
            expectedModCount = mc;
            int[] tab = this.tab = table;
            char free = this.free = freeValue;
            int nextI = tab.length;
            int entry;
            while (--nextI >= 0) {
                char key;
                if ((key = (char) (entry = tab[nextI])) != free) {
                    next = new MutableEntry(mc, nextI, key, (short) (entry >>> 16));
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Character, Short>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            int[] tab = this.tab;
            char free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
                    action.accept(new MutableEntry(mc, i, key, (short) (entry >>> 16)));
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
        public Map.Entry<Character, Short> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int mc;
                if ((mc = expectedModCount) == modCount()) {
                    int[] tab = this.tab;
                    char free = this.free;
                    MutableEntry prev = next;
                    int entry;
                    while (--nextI >= 0) {
                        char key;
                        if ((key = (char) (entry = tab[nextI])) != free) {
                            next = new MutableEntry(mc, nextI, key, (short) (entry >>> 16));
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


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Character, Short>> {
        final int[] tab;
        final char free;
        int expectedModCount;
        int index;
        char curKey;
        short curValue;

        NoRemovedEntryCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            char free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Character, Short>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            int[] tab = this.tab;
            char free = this.free;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
                    action.accept(new MutableEntry(mc, i, key, (short) (entry >>> 16)));
                }
            }
            if (index != this.index || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public Map.Entry<Character, Short> elem() {
            char curKey;
            if ((curKey = this.curKey) != free) {
                return new MutableEntry(expectedModCount, index, curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            if (expectedModCount == modCount()) {
                int[] tab = this.tab;
                char free = this.free;
                int entry;
                for (int i = index - 1; i >= 0; i--) {
                    char key;
                    if ((key = (char) (entry = tab[i])) != free) {
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
            throw new java.lang.UnsupportedOperationException();
        }
    }




    class NoRemovedValueIterator implements ShortIterator {
        final int[] tab;
        final char free;
        int expectedModCount;
        int nextIndex;
        short next;

        NoRemovedValueIterator(int mc) {
            expectedModCount = mc;
            int[] tab = this.tab = table;
            char free = this.free = freeValue;
            int nextI = tab.length;
            int entry;
            while (--nextI >= 0) {
                if ((char) (entry = tab[nextI]) != free) {
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
                    int[] tab = this.tab;
                    char free = this.free;
                    short prev = next;
                    int entry;
                    while (--nextI >= 0) {
                        if ((char) (entry = tab[nextI]) != free) {
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
            char free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
                    action.accept((short) (entry >>> 16));
                }
            }
            if (nextI != nextIndex || mc != modCount()) {
                throw new java.util.ConcurrentModificationException();
            }
            nextIndex = -1;
        }

        @Override
        public void forEachRemaining(ShortConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            int[] tab = this.tab;
            char free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
                    action.accept((short) (entry >>> 16));
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
        public Short next() {
            return nextShort();
        }

        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedValueCursor implements ShortCursor {
        final int[] tab;
        final char free;
        int expectedModCount;
        int index;
        char curKey;
        short curValue;

        NoRemovedValueCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            char free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ShortConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            int[] tab = this.tab;
            char free = this.free;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
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
                char free = this.free;
                int entry;
                for (int i = index - 1; i >= 0; i--) {
                    char key;
                    if ((key = (char) (entry = tab[i])) != free) {
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
            throw new java.lang.UnsupportedOperationException();
        }
    }



    class NoRemovedMapCursor implements CharShortCursor {
        final int[] tab;
        final char free;
        int expectedModCount;
        int index;
        char curKey;
        short curValue;

        NoRemovedMapCursor(int mc) {
            expectedModCount = mc;
            this.tab = table;
            index = tab.length;
            char free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(CharShortConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int mc = expectedModCount;
            int[] tab = this.tab;
            char free = this.free;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
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
        public char key() {
            char curKey;
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
                char free = this.free;
                int entry;
                for (int i = index - 1; i >= 0; i--) {
                    char key;
                    if ((key = (char) (entry = tab[i])) != free) {
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
            throw new java.lang.UnsupportedOperationException();
        }
    }
}

