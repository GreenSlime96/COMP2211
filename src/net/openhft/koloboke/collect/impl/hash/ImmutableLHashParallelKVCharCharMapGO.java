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
import net.openhft.koloboke.function.CharCharConsumer;
import net.openhft.koloboke.function.CharCharPredicate;
import net.openhft.koloboke.function.CharCharToCharFunction;
import net.openhft.koloboke.function.CharUnaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.openhft.koloboke.function.CharBinaryOperator;
import net.openhft.koloboke.function.CharConsumer;
import net.openhft.koloboke.function.CharPredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class ImmutableLHashParallelKVCharCharMapGO
        extends ImmutableLHashParallelKVCharCharMapSO {

    
    final void copy(ParallelKVCharCharLHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    
    final void move(ParallelKVCharCharLHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.move(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }


    @Override
    public char defaultValue() {
        return (char) 0;
    }

    @Override
    public boolean containsEntry(char key, char value) {
        char free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            char cur;
            int entry;
            if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
                // key is present
                return (char) (entry >>> 16) == value;
            } else {
                if (cur == free) {
                    // key is absent
                    return false;
                } else {
                    while (true) {
                        if ((cur = (char) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            // key is present
                            return (char) (entry >>> 16) == value;
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
    public Character get(Object key) {
        char k = (Character) key;
        char free;
        if (k != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            char cur;
            int entry;
            if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
                // key is present
                return (char) (entry >>> 16);
            } else {
                if (cur == free) {
                    // key is absent
                    return null;
                } else {
                    while (true) {
                        if ((cur = (char) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                            // key is present
                            return (char) (entry >>> 16);
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
    public char get(char key) {
        char free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            char cur;
            int entry;
            if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
                // key is present
                return (char) (entry >>> 16);
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue();
                } else {
                    while (true) {
                        if ((cur = (char) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            // key is present
                            return (char) (entry >>> 16);
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
    public Character getOrDefault(Object key, Character defaultValue) {
        char k = (Character) key;
        char free;
        if (k != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            char cur;
            int entry;
            if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(k) & (capacityMask = tab.length - 1)])) == k) {
                // key is present
                return (char) (entry >>> 16);
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue;
                } else {
                    while (true) {
                        if ((cur = (char) (entry = tab[(index = (index - 1) & capacityMask)])) == k) {
                            // key is present
                            return (char) (entry >>> 16);
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
    public char getOrDefault(char key, char defaultValue) {
        char free;
        if (key != (free = freeValue)) {
            int[] tab = table;
            int capacityMask, index;
            char cur;
            int entry;
            if ((cur = (char) (entry = tab[index = ParallelKVCharKeyMixing.mix(key) & (capacityMask = tab.length - 1)])) == key) {
                // key is present
                return (char) (entry >>> 16);
            } else {
                if (cur == free) {
                    // key is absent
                    return defaultValue;
                } else {
                    while (true) {
                        if ((cur = (char) (entry = tab[(index = (index - 1) & capacityMask)])) == key) {
                            // key is present
                            return (char) (entry >>> 16);
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
    public void forEach(BiConsumer<? super Character, ? super Character> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        char free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
                action.accept(key, (char) (entry >>> 16));
            }
        }
    }

    @Override
    public void forEach(CharCharConsumer action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        char free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
                action.accept(key, (char) (entry >>> 16));
            }
        }
    }

    @Override
    public boolean forEachWhile(CharCharPredicate predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        char free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
                if (!predicate.test(key, (char) (entry >>> 16))) {
                    terminated = true;
                    break;
                }
            }
        }
        return !terminated;
    }

    @Nonnull
    @Override
    public CharCharCursor cursor() {
        
        return new NoRemovedMapCursor();
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonCharCharMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalCharCharMapOps m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        char free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
                if (!m.containsEntry(key, (char) (entry >>> 16))) {
                    containsAll = false;
                    break;
                }
            }
        }
        return containsAll;
    }

    @Override
    public void reversePutAllTo(InternalCharCharMapOps m) {
        if (isEmpty())
            return;
        char free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
                m.justPut(key, (char) (entry >>> 16));
            }
        }
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<Character, Character>> entrySet() {
        return new EntryView();
    }

    @Override
    @Nonnull
    public CharCollection values() {
        return new ValueView();
    }


    @Override
    public boolean equals(Object o) {
        return CommonMapOps.equals(this, o);
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        char free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
                hashCode += key ^ (char) (entry >>> 16);
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
        char free = freeValue;
        int[] tab = table;
        int entry;
        for (int i = tab.length - 1; i >= 0; i--) {
            char key;
            if ((key = (char) (entry = tab[i])) != free) {
                sb.append(' ');
                sb.append(key);
                sb.append('=');
                sb.append((char) (entry >>> 16));
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
    public Character put(Character key, Character value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public char put(char key, char value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Character putIfAbsent(Character key, Character value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public char putIfAbsent(char key, char value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void justPut(char key, char value) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public Character compute(Character key,
            BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public char compute(char key, CharCharToCharFunction remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public Character computeIfAbsent(Character key,
            Function<? super Character, ? extends Character> mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public char computeIfAbsent(char key, CharUnaryOperator mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public Character computeIfPresent(Character key,
            BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public char computeIfPresent(char key, CharCharToCharFunction remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Character merge(Character key, Character value,
            BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public char merge(char key, char value, CharBinaryOperator remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public char addValue(char key, char value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public char addValue(char key, char addition, char defaultValue) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public void putAll(@Nonnull Map<? extends Character, ? extends Character> m) {
        CommonCharCharMapOps.putAll(this, m);
    }


    @Override
    public Character replace(Character key, Character value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public char replace(char key, char value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean replace(Character key, Character oldValue, Character newValue) {
        return replace(key.charValue(),
                oldValue.charValue(),
                newValue.charValue());
    }

    @Override
    public boolean replace(char key, char oldValue, char newValue) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public void replaceAll(
            BiFunction<? super Character, ? super Character, ? extends Character> function) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void replaceAll(CharCharToCharFunction function) {
        throw new java.lang.UnsupportedOperationException();
    }





    @Override
    public Character remove(Object key) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean justRemove(char key) {
        throw new java.lang.UnsupportedOperationException();
    }



    

    @Override
    public char remove(char key) {
        throw new java.lang.UnsupportedOperationException();
    }



    @Override
    public boolean remove(Object key, Object value) {
        return remove(((Character) key).charValue(),
                ((Character) value).charValue()
                );
    }

    @Override
    public boolean remove(char key, char value) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean removeIf(CharCharPredicate filter) {
        throw new java.lang.UnsupportedOperationException();
    }






    class EntryView extends AbstractSetView<Map.Entry<Character, Character>>
            implements HashObjSet<Map.Entry<Character, Character>>,
            InternalObjCollectionOps<Map.Entry<Character, Character>> {

        @Nonnull
        @Override
        public Equivalence<Entry<Character, Character>> equivalence() {
            return Equivalence.entryEquivalence(
                    Equivalence.<Character>defaultEquality()
                    ,
                    Equivalence.<Character>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return ImmutableLHashParallelKVCharCharMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return ImmutableLHashParallelKVCharCharMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return ImmutableLHashParallelKVCharCharMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<Character, Character> e = (Map.Entry<Character, Character>) o;
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
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
                    result[resultIndex++] = new ImmutableEntry(key, (char) (entry >>> 16));
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
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
                    a[resultIndex++] = (T) new ImmutableEntry(key, (char) (entry >>> 16));
                }
            }
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }

        @Override
        public final void forEach(@Nonnull Consumer<? super Map.Entry<Character, Character>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
                    action.accept(new ImmutableEntry(key, (char) (entry >>> 16)));
                }
            }
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<Character, Character>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
                    if (!predicate.test(new ImmutableEntry(key, (char) (entry >>> 16)))) {
                        terminated = true;
                        break;
                    }
                }
            }
            return !terminated;
        }

        @Override
        @Nonnull
        public ObjIterator<Map.Entry<Character, Character>> iterator() {
            
            return new NoRemovedEntryIterator();
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<Character, Character>> cursor() {
            
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
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
                    if (!c.contains(e.with(key, (char) (entry >>> 16)))) {
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
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
                    changed |= s.remove(e.with(key, (char) (entry >>> 16)));
                }
            }
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<Character, Character>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
                    changed |= c.add(new ImmutableEntry(key, (char) (entry >>> 16)));
                }
            }
            return changed;
        }


        public int hashCode() {
            return ImmutableLHashParallelKVCharCharMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
                    sb.append(' ');
                    sb.append(key);
                    sb.append('=');
                    sb.append((char) (entry >>> 16));
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
            return ImmutableLHashParallelKVCharCharMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<Character, Character> e = (Map.Entry<Character, Character>) o;
                char key = e.getKey();
                char value = e.getValue();
                return ImmutableLHashParallelKVCharCharMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<Character, Character>> filter) {
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
            ImmutableLHashParallelKVCharCharMapGO.this.clear();
        }
    }


    abstract class CharCharEntry extends AbstractEntry<Character, Character> {

        abstract char key();

        @Override
        public final Character getKey() {
            return key();
        }

        abstract char value();

        @Override
        public final Character getValue() {
            return value();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object o) {
            Map.Entry e2;
            char k2;
            char v2;
            try {
                e2 = (Map.Entry) o;
                k2 = (Character) e2.getKey();
                v2 = (Character) e2.getValue();
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


    private class ImmutableEntry extends CharCharEntry {
        private final char key;
        private final char value;

        ImmutableEntry(char key, char value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public char key() {
            return key;
        }

        @Override
        public char value() {
            return value;
        }
    }


    class ReusableEntry extends CharCharEntry {
        private char key;
        private char value;

        ReusableEntry with(char key, char value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public char key() {
            return key;
        }

        @Override
        public char value() {
            return value;
        }
    }


    class ValueView extends AbstractCharValueView {


        @Override
        public int size() {
            return ImmutableLHashParallelKVCharCharMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return ImmutableLHashParallelKVCharCharMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return ImmutableLHashParallelKVCharCharMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(char v) {
            return ImmutableLHashParallelKVCharCharMapGO.this.containsValue(v);
        }



        @Override
        public void forEach(Consumer<? super Character> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
                    action.accept((char) (entry >>> 16));
                }
            }
        }

        @Override
        public void forEach(CharConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
                    action.accept((char) (entry >>> 16));
                }
            }
        }

        @Override
        public boolean forEachWhile(CharPredicate predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
                    if (!predicate.test((char) (entry >>> 16))) {
                        terminated = true;
                        break;
                    }
                }
            }
            return !terminated;
        }

        @Override
        public boolean allContainingIn(CharCollection c) {
            if (isEmpty())
                return true;
            boolean containsAll = true;
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
                    if (!c.contains((char) (entry >>> 16))) {
                        containsAll = false;
                        break;
                    }
                }
            }
            return containsAll;
        }


        @Override
        public boolean reverseAddAllTo(CharCollection c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
                    changed |= c.add((char) (entry >>> 16));
                }
            }
            return changed;
        }


        @Override
        public boolean reverseRemoveAllFrom(CharSet s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
                    changed |= s.removeChar((char) (entry >>> 16));
                }
            }
            return changed;
        }



        @Override
        @Nonnull
        public CharIterator iterator() {
            
            return new NoRemovedValueIterator();
        }

        @Nonnull
        @Override
        public CharCursor cursor() {
            
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
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
                    result[resultIndex++] = (char) (entry >>> 16);
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
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
                    a[resultIndex++] = (T) Character.valueOf((char) (entry >>> 16));
                }
            }
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }

        @Override
        public char[] toCharArray() {
            int size = size();
            char[] result = new char[size];
            if (size == 0)
                return result;
            int resultIndex = 0;
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
                    result[resultIndex++] = (char) (entry >>> 16);
                }
            }
            return result;
        }

        @Override
        public char[] toArray(char[] a) {
            int size = size();
            if (a.length < size)
                a = new char[size];
            if (size == 0) {
                if (a.length > 0)
                    a[0] = (char) 0;
                return a;
            }
            int resultIndex = 0;
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
                    a[resultIndex++] = (char) (entry >>> 16);
                }
            }
            if (a.length > resultIndex)
                a[resultIndex] = (char) 0;
            return a;
        }


        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            char free = freeValue;
            int[] tab = table;
            int entry;
            for (int i = tab.length - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
                    sb.append(' ').append((char) (entry >>> 16)).append(',');
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
            return removeChar(( Character ) o);
        }

        @Override
        public boolean removeChar(char v) {
            return removeValue(v);
        }



        @Override
        public void clear() {
            ImmutableLHashParallelKVCharCharMapGO.this.clear();
        }

        
        public boolean removeIf(Predicate<? super Character> filter) {
            throw new java.lang.UnsupportedOperationException();
        }

        @Override
        public boolean removeIf(CharPredicate filter) {
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<Character, Character>> {
        final int[] tab;
        final char free;
        int nextIndex;
        ImmutableEntry next;

        NoRemovedEntryIterator() {
            int[] tab = this.tab = table;
            char free = this.free = freeValue;
            int nextI = tab.length;
            int entry;
            while (--nextI >= 0) {
                char key;
                if ((key = (char) (entry = tab[nextI])) != free) {
                    next = new ImmutableEntry(key, (char) (entry >>> 16));
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<Character, Character>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int[] tab = this.tab;
            char free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
                    action.accept(new ImmutableEntry(key, (char) (entry >>> 16)));
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
        public Map.Entry<Character, Character> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int[] tab = this.tab;
                char free = this.free;
                ImmutableEntry prev = next;
                int entry;
                while (--nextI >= 0) {
                    char key;
                    if ((key = (char) (entry = tab[nextI])) != free) {
                        next = new ImmutableEntry(key, (char) (entry >>> 16));
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


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<Character, Character>> {
        final int[] tab;
        final char free;
        int index;
        char curKey;
        char curValue;

        NoRemovedEntryCursor() {
            this.tab = table;
            index = tab.length;
            char free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<Character, Character>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int[] tab = this.tab;
            char free = this.free;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
                    action.accept(new ImmutableEntry(key, (char) (entry >>> 16)));
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public Map.Entry<Character, Character> elem() {
            char curKey;
            if ((curKey = this.curKey) != free) {
                return new ImmutableEntry(curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            int[] tab = this.tab;
            char free = this.free;
            int entry;
            for (int i = index - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
                    index = i;
                    curKey = key;
                    curValue = (char) (entry >>> 16);
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




    class NoRemovedValueIterator implements CharIterator {
        final int[] tab;
        final char free;
        int nextIndex;
        char next;

        NoRemovedValueIterator() {
            int[] tab = this.tab = table;
            char free = this.free = freeValue;
            int nextI = tab.length;
            int entry;
            while (--nextI >= 0) {
                if ((char) (entry = tab[nextI]) != free) {
                    next = (char) (entry >>> 16);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public char nextChar() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                int[] tab = this.tab;
                char free = this.free;
                char prev = next;
                int entry;
                while (--nextI >= 0) {
                    if ((char) (entry = tab[nextI]) != free) {
                        next = (char) (entry >>> 16);
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
        public void forEachRemaining(Consumer<? super Character> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int[] tab = this.tab;
            char free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
                    action.accept((char) (entry >>> 16));
                }
            }
            if (nextI != nextIndex) {
                throw new java.util.ConcurrentModificationException();
            }
            nextIndex = -1;
        }

        @Override
        public void forEachRemaining(CharConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int[] tab = this.tab;
            char free = this.free;
            int entry;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
                    action.accept((char) (entry >>> 16));
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
        public Character next() {
            return nextChar();
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }


    class NoRemovedValueCursor implements CharCursor {
        final int[] tab;
        final char free;
        int index;
        char curKey;
        char curValue;

        NoRemovedValueCursor() {
            this.tab = table;
            index = tab.length;
            char free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(CharConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int[] tab = this.tab;
            char free = this.free;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                if ((char) (entry = tab[i]) != free) {
                    action.accept((char) (entry >>> 16));
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = free;
        }

        @Override
        public char elem() {
            if (curKey != free) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            int[] tab = this.tab;
            char free = this.free;
            int entry;
            for (int i = index - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
                    index = i;
                    curKey = key;
                    curValue = (char) (entry >>> 16);
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



    class NoRemovedMapCursor implements CharCharCursor {
        final int[] tab;
        final char free;
        int index;
        char curKey;
        char curValue;

        NoRemovedMapCursor() {
            this.tab = table;
            index = tab.length;
            char free = this.free = freeValue;
            curKey = free;
        }

        @Override
        public void forEachForward(CharCharConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            int[] tab = this.tab;
            char free = this.free;
            int entry;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
                    action.accept(key, (char) (entry >>> 16));
                }
            }
            if (index != this.index) {
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
        public char value() {
            if (curKey != free) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(char value) {
            if (curKey != free) {
                U.putChar(tab, INT_BASE + CHAR_VALUE_OFFSET + (((long) (index)) << INT_SCALE_SHIFT), value);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            int[] tab = this.tab;
            char free = this.free;
            int entry;
            for (int i = index - 1; i >= 0; i--) {
                char key;
                if ((key = (char) (entry = tab[i])) != free) {
                    index = i;
                    curKey = key;
                    curValue = (char) (entry >>> 16);
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
