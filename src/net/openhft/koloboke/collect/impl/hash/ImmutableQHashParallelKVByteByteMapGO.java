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


public class ImmutableQHashParallelKVByteByteMapGO
        extends ImmutableQHashParallelKVByteByteMapSO {

    
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
        if (key != (free = freeValue)) {
            char[] tab = table;
            int capacity, index;
            byte cur;
            int entry;
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) % (capacity = tab.length)])) == key) {
                // key is present
                return (byte) (entry >>> 8) == value;
            } else {
                if (cur == free) {
                    // key is absent
                    return false;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (byte) (entry = tab[bIndex])) == key) {
                            // key is present
                            return (byte) (entry >>> 8) == value;
                        } else if (cur == free) {
                            // key is absent
                            return false;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (byte) (entry = tab[fIndex])) == key) {
                            // key is present
                            return (byte) (entry >>> 8) == value;
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
    public Byte get(Object key) {
        byte k = (Byte) key;
        byte free;
        if (k != (free = freeValue)) {
            char[] tab = table;
            int capacity, index;
            byte cur;
            int entry;
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(k) % (capacity = tab.length)])) == k) {
                // key is present
                return (byte) (entry >>> 8);
            } else {
                if (cur == free) {
                    // key is absent
                    return null;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (byte) (entry = tab[bIndex])) == k) {
                            // key is present
                            return (byte) (entry >>> 8);
                        } else if (cur == free) {
                            // key is absent
                            return null;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (byte) (entry = tab[fIndex])) == k) {
                            // key is present
                            return (byte) (entry >>> 8);
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
    public byte get(byte key) {
        byte free;
        if (key != (free = freeValue)) {
            char[] tab = table;
            int capacity, index;
            byte cur;
            int entry;
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) % (capacity = tab.length)])) == key) {
                // key is present
                return (byte) (entry >>> 8);
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue();
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (byte) (entry = tab[bIndex])) == key) {
                            // key is present
                            return (byte) (entry >>> 8);
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue();
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (byte) (entry = tab[fIndex])) == key) {
                            // key is present
                            return (byte) (entry >>> 8);
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
    public Byte getOrDefault(Object key, Byte defaultValue) {
        byte k = (Byte) key;
        byte free;
        if (k != (free = freeValue)) {
            char[] tab = table;
            int capacity, index;
            byte cur;
            int entry;
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(k) % (capacity = tab.length)])) == k) {
                // key is present
                return (byte) (entry >>> 8);
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (byte) (entry = tab[bIndex])) == k) {
                            // key is present
                            return (byte) (entry >>> 8);
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (byte) (entry = tab[fIndex])) == k) {
                            // key is present
                            return (byte) (entry >>> 8);
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
    public byte getOrDefault(byte key, byte defaultValue) {
        byte free;
        if (key != (free = freeValue)) {
            char[] tab = table;
            int capacity, index;
            byte cur;
            int entry;
            if ((cur = (byte) (entry = tab[index = ParallelKVByteKeyMixing.mix(key) % (capacity = tab.length)])) == key) {
                // key is present
                return (byte) (entry >>> 8);
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue;
                } else {
                    int bIndex = index, fIndex = index, step = 1;
                    while (true) {
                        if ((bIndex -= step) < 0) bIndex += capacity;
                        if ((cur = (byte) (entry = tab[bIndex])) == key) {
                            // key is present
                            return (byte) (entry >>> 8);
                        } else if (cur == free) {
                            // key is absent
                            return defaultValue;
                        }
                        int t;
                        if ((t = (fIndex += step) - capacity) >= 0) fIndex = t;
                        if ((cur = (byte) (entry = tab[fIndex])) == key) {
                            // key is present
                            return (byte) (entry >>> 8);
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
    public void forEach(BiConsumer<? super Byte, ? super Byte> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        byte free = freeValue;
        char[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            byte key;
            if ((key = (byte) (entry = tab[i])) != free) {
                action.accept(key, (byte) (entry >>> 8));
            }
        }
    }

    @Override
    public void forEach(ByteByteConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        byte free = freeValue;
        char[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            byte key;
            if ((key = (byte) (entry = tab[i])) != free) {
                action.accept(key, (byte) (entry >>> 8));
            }
        }
    }

    @Override
    public boolean forEachWhile(ByteBytePredicate predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
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
        return !terminated;
    }

    @Nonnull
    @Override
    public ByteByteCursor cursor() {
        
        return new NoRemovedMapCursor();
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
        return containsAll;
    }

    @Override
    public void reversePutAllTo(InternalByteByteMapOps m) {
        if (isEmpty())
            return;
        byte free = freeValue;
        char[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            byte key;
            if ((key = (byte) (entry = tab[i])) != free) {
                m.justPut(key, (byte) (entry >>> 8));
            }
        }
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
        byte free = freeValue;
        char[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            byte key;
            if ((key = (byte) (entry = tab[i])) != free) {
                hashCode += key ^ (byte) (entry >>> 8);
            }
        }
        return hashCode;
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "{}";
        StringBuilder sb = new StringBuilder();
        int elementCount = 0;
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
        sb.setCharAt(0, '{');
        sb.setCharAt(sb.length() - 1, '}');
        return sb.toString();
    }




    @Override
    public Byte put(Byte key, Byte value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public byte put(byte key, byte value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Byte putIfAbsent(Byte key, Byte value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public byte putIfAbsent(byte key, byte value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void justPut(byte key, byte value) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public Byte compute(Byte key,
            BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public byte compute(byte key, ByteByteToByteFunction remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public Byte computeIfAbsent(Byte key,
            Function<? super Byte, ? extends Byte> mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public byte computeIfAbsent(byte key, ByteUnaryOperator mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public Byte computeIfPresent(Byte key,
            BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public byte computeIfPresent(byte key, ByteByteToByteFunction remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Byte merge(Byte key, Byte value,
            BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public byte merge(byte key, byte value, ByteBinaryOperator remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public byte addValue(byte key, byte value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public byte addValue(byte key, byte addition, byte defaultValue) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public void putAll(@Nonnull Map<? extends Byte, ? extends Byte> m) {
        CommonByteByteMapOps.putAll(this, m);
    }


    @Override
    public Byte replace(Byte key, Byte value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public byte replace(byte key, byte value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean replace(Byte key, Byte oldValue, Byte newValue) {
        return replace(key.byteValue(),
                oldValue.byteValue(),
                newValue.byteValue());
    }

    @Override
    public boolean replace(byte key, byte oldValue, byte newValue) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public void replaceAll(
            BiFunction<? super Byte, ? super Byte, ? extends Byte> function) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void replaceAll(ByteByteToByteFunction function) {
        throw new java.lang.UnsupportedOperationException();
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
            return ImmutableQHashParallelKVByteByteMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return ImmutableQHashParallelKVByteByteMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return ImmutableQHashParallelKVByteByteMapGO.this.currentLoad();
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
            byte free = freeValue;
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    result[resultIndex++] = new ImmutableEntry(key, (byte) (entry >>> 8));
                }
            }
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
            byte free = freeValue;
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    a[resultIndex++] = (T) new ImmutableEntry(key, (byte) (entry >>> 8));
                }
            }
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
            byte free = freeValue;
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    action.accept(new ImmutableEntry(key, (byte) (entry >>> 8)));
                }
            }
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Byte, Byte>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            byte free = freeValue;
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    if (!predicate.test(new ImmutableEntry(key, (byte) (entry >>> 8)))) {
                        terminated = true;
                        break;
                    }
                }
            }
            return !terminated;
        }

        @Override
        @Nonnull
        public ObjIterator<Map.Entry<Byte, Byte>> iterator() {
            
            return new NoRemovedEntryIterator();
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Byte, Byte>> cursor() {
            
            return new NoRemovedEntryCursor();
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
            return containsAll;
        }

        @Override
        public boolean reverseRemoveAllFrom(ObjSet<?> s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            ReusableEntry e = new ReusableEntry();
            byte free = freeValue;
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    changed |= s.remove(e.with(key, (byte) (entry >>> 8)));
                }
            }
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Byte, Byte>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            byte free = freeValue;
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    changed |= c.add(new ImmutableEntry(key, (byte) (entry >>> 8)));
                }
            }
            return changed;
        }


        public int hashCode() {
            return ImmutableQHashParallelKVByteByteMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
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
            sb.setCharAt(0, '[');
            sb.setCharAt(sb.length() - 1, ']');
            return sb.toString();
        }

        @Override
        public boolean shrink() {
            return ImmutableQHashParallelKVByteByteMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Byte, Byte> e = (Map.Entry<Byte, Byte>) o;
                byte key = e.getKey();
                byte value = e.getValue();
                return ImmutableQHashParallelKVByteByteMapGO.this.remove(key, value);
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
            ImmutableQHashParallelKVByteByteMapGO.this.clear();
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


    private class ImmutableEntry extends ByteByteEntry {
        private final byte key;
        private final byte value;

        ImmutableEntry(byte key, byte value) {
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
            return ImmutableQHashParallelKVByteByteMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return ImmutableQHashParallelKVByteByteMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return ImmutableQHashParallelKVByteByteMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(byte v) {
            return ImmutableQHashParallelKVByteByteMapGO.this.containsValue(v);
        }



        @Override
        public void forEach(Consumer<? super Byte> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            byte free = freeValue;
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    action.accept((byte) (entry >>> 8));
                }
            }
        }

        @Override
        public void forEach(ByteConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            byte free = freeValue;
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    action.accept((byte) (entry >>> 8));
                }
            }
        }

        @Override
        public boolean forEachWhile(BytePredicate predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
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
            return !terminated;
        }

        @Override
        public boolean allContainingIn(ByteCollection c) {
            if (isEmpty())
                return true;
            boolean containsAll = true;
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
            return containsAll;
        }


        @Override
        public boolean reverseAddAllTo(ByteCollection c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            byte free = freeValue;
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    changed |= c.add((byte) (entry >>> 8));
                }
            }
            return changed;
        }


        @Override
        public boolean reverseRemoveAllFrom(ByteSet s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            byte free = freeValue;
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    changed |= s.removeByte((byte) (entry >>> 8));
                }
            }
            return changed;
        }



        @Override
        @Nonnull
        public ByteIterator iterator() {
            
            return new NoRemovedValueIterator();
        }

        @Nonnull
        @Override
        public ByteCursor cursor() {
            
            return new NoRemovedValueCursor();
        }

        @Override
        @Nonnull
        public Object[] toArray() {
            int size = size();
            Object[] result = new Object[size];
            if (size == 0)
                return result;
            int resultIndex = 0;
            byte free = freeValue;
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    result[resultIndex++] = (byte) (entry >>> 8);
                }
            }
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
            byte free = freeValue;
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    a[resultIndex++] = (T) Byte.valueOf((byte) (entry >>> 8));
                }
            }
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
            byte free = freeValue;
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    result[resultIndex++] = (byte) (entry >>> 8);
                }
            }
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
            byte free = freeValue;
            char[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    a[resultIndex++] = (byte) (entry >>> 8);
                }
            }
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
            ImmutableQHashParallelKVByteByteMapGO.this.clear();
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
        int nextIndex;
        ImmutableEntry next;

        NoRemovedEntryIterator() {
            char[] tab = this.tab = table;
            byte free = this.free = freeValue;
            int nextI = tab.length;
            int entry;
            while (--nextI >= 0) {
                byte key;
                if ((key = (byte) (entry = tab[nextI])) != free) {
                    next = new ImmutableEntry(key, (byte) (entry >>> 8));
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Byte, Byte>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            char[] tab = this.tab;
            byte free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    action.accept(new ImmutableEntry(key, (byte) (entry >>> 8)));
                }
            }
            if (nextI != nextIndex) {
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
                char[] tab = this.tab;
                byte free = this.free;
                ImmutableEntry prev = next;
                int entry;
                while (--nextI >= 0) {
                    byte key;
                    if ((key = (byte) (entry = tab[nextI])) != free) {
                        next = new ImmutableEntry(key, (byte) (entry >>> 8));
                        break;
                    }
                }
                nextIndex = nextI;
                return prev;
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
        int index;
        byte curKey;
        byte curValue;

        NoRemovedEntryCursor() {
            this.tab = table;
            index = tab.length;
            byte free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Byte, Byte>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            char[] tab = this.tab;
            byte free = this.free;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                byte key;
                if ((key = (byte) (entry = tab[i])) != free) {
                    action.accept(new ImmutableEntry(key, (byte) (entry >>> 8)));
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public Map.Entry<Byte, Byte> elem() {
            byte curKey;
            if ((curKey = this.curKey) != free) {
                return new ImmutableEntry(curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
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
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }




    class NoRemovedValueIterator implements ByteIterator {
        final char[] tab;
        final byte free;
        int nextIndex;
        byte next;

        NoRemovedValueIterator() {
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
                throw new java.util.NoSuchElementException();
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super Byte> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            char[] tab = this.tab;
            byte free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    action.accept((byte) (entry >>> 8));
                }
            }
            if (nextI != nextIndex) {
                throw new java.util.ConcurrentModificationException();
            }
            nextIndex = -1;
        }

        @Override
        public void forEachRemaining(ByteConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            char[] tab = this.tab;
            byte free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    action.accept((byte) (entry >>> 8));
                }
            }
            if (nextI != nextIndex) {
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
        int index;
        byte curKey;
        byte curValue;

        NoRemovedValueCursor() {
            this.tab = table;
            index = tab.length;
            byte free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ByteConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            char[] tab = this.tab;
            byte free = this.free;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                if ((byte) (entry = tab[i]) != free) {
                    action.accept((byte) (entry >>> 8));
                }
            }
            if (index != this.index) {
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
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }



    class NoRemovedMapCursor implements ByteByteCursor {
        final char[] tab;
        final byte free;
        int index;
        byte curKey;
        byte curValue;

        NoRemovedMapCursor() {
            this.tab = table;
            index = tab.length;
            byte free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(ByteByteConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
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
            if (index != this.index) {
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
                U.putByte(tab, CHAR_BASE + BYTE_VALUE_OFFSET + (((long) (index)) << CHAR_SCALE_SHIFT), value);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
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
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }
}

