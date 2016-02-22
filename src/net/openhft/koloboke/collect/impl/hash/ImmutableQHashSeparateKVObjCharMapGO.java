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
import java.util.function.Predicate;
import net.openhft.koloboke.function.ObjCharConsumer;
import net.openhft.koloboke.function.ObjCharPredicate;
import net.openhft.koloboke.function.ObjCharToCharFunction;
import net.openhft.koloboke.function.ToCharFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.openhft.koloboke.function.CharBinaryOperator;
import net.openhft.koloboke.function.CharConsumer;
import net.openhft.koloboke.function.CharPredicate;

import javax.annotation.Nonnull;
import java.util.*;


public class ImmutableQHashSeparateKVObjCharMapGO<K>
        extends ImmutableQHashSeparateKVObjCharMapSO<K> {

    @Override
    final void copy(SeparateKVObjCharQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVObjCharQHash hash) {
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
    public boolean containsEntry(Object key, char value) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return values[index] == value;
        } else {
            // key is absent
            return false;
        }
    }


    @Override
    public Character get(Object key) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return null;
        }
    }

    

    @Override
    public char getChar(Object key) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return defaultValue();
        }
    }

    @Override
    public Character getOrDefault(Object key, Character defaultValue) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public char getOrDefault(Object key, char defaultValue) {
        int index = index(key);
        if (index >= 0) {
            // key is present
            return values[index];
        } else {
            // key is absent
            return defaultValue;
        }
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super Character> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        Object[] keys = set;
        char[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                action.accept(key, vals[i]);
            }
        }
    }

    @Override
    public void forEach(ObjCharConsumer<? super K> action) {
        if (action == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return;
        Object[] keys = set;
        char[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                action.accept(key, vals[i]);
            }
        }
    }

    @Override
    public boolean forEachWhile(ObjCharPredicate<? super K> predicate) {
        if (predicate == null)
            throw new java.lang.NullPointerException();
        if (isEmpty())
            return true;
        boolean terminated = false;
        Object[] keys = set;
        char[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                if (!predicate.test(key, vals[i])) {
                    terminated = true;
                    break;
                }
            }
        }
        return !terminated;
    }

    @Nonnull
    @Override
    public ObjCharCursor<K> cursor() {
        
        return new NoRemovedMapCursor();
    }


    @Override
    public boolean containsAllEntries(Map<?, ?> m) {
        return CommonObjCharMapOps.containsAllEntries(this, m);
    }

    @Override
    public boolean allEntriesContainingIn(InternalObjCharMapOps<?> m) {
        if (isEmpty())
            return true;
        boolean containsAll = true;
        Object[] keys = set;
        char[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                if (!m.containsEntry(key, vals[i])) {
                    containsAll = false;
                    break;
                }
            }
        }
        return containsAll;
    }

    @Override
    public void reversePutAllTo(InternalObjCharMapOps<? super K> m) {
        if (isEmpty())
            return;
        Object[] keys = set;
        char[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                m.justPut(key, vals[i]);
            }
        }
    }

    @Override
    @Nonnull
    public HashObjSet<Map.Entry<K, Character>> entrySet() {
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
        Object[] keys = set;
        char[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                hashCode += nullableKeyHashCode(key) ^ vals[i];
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
        Object[] keys = set;
        char[] vals = values;
        for (int i = keys.length - 1; i >= 0; i--) {
            K key;
            // noinspection unchecked
            if ((key = (K) keys[i]) != FREE) {
                sb.append(' ');
                sb.append(key != this ? key : "(this Map)");
                sb.append('=');
                sb.append(vals[i]);
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
    public Character put(K key, Character value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public char put(K key, char value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Character putIfAbsent(K key, Character value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public char putIfAbsent(K key, char value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void justPut(K key, char value) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public Character compute(K key,
            BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    Character computeNullKey(
            BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public char compute(K key, ObjCharToCharFunction<? super K> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    char computeNullKey(ObjCharToCharFunction<? super K> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Character computeIfAbsent(K key,
            Function<? super K, ? extends Character> mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    Character computeIfAbsentNullKey(
            Function<? super K, ? extends Character> mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public char computeIfAbsent(K key, ToCharFunction<? super K> mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    char computeIfAbsentNullKey(ToCharFunction<? super K> mappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Character computeIfPresent(K key,
            BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public char computeIfPresent(K key, ObjCharToCharFunction<? super K> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public Character merge(K key, Character value,
            BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    Character mergeNullKey(Character value,
            BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public char merge(K key, char value, CharBinaryOperator remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    char mergeNullKey(char value, CharBinaryOperator remappingFunction) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public char addValue(K key, char value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public char addValue(K key, char addition, char defaultValue) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public void putAll(@Nonnull Map<? extends K, ? extends Character> m) {
        CommonObjCharMapOps.putAll(this, m);
    }


    @Override
    public Character replace(K key, Character value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public char replace(K key, char value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean replace(K key, Character oldValue, Character newValue) {
        return replace(key,
                oldValue.charValue(),
                newValue.charValue());
    }

    @Override
    public boolean replace(K key, char oldValue, char newValue) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public void replaceAll(
            BiFunction<? super K, ? super Character, ? extends Character> function) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public void replaceAll(ObjCharToCharFunction<? super K> function) {
        throw new java.lang.UnsupportedOperationException();
    }





    @Override
    public Character remove(Object key) {
        throw new java.lang.UnsupportedOperationException();
    }

    Character removeNullKey() {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean justRemove(Object key) {
        throw new java.lang.UnsupportedOperationException();
    }

    boolean justRemoveNullKey() {
        throw new java.lang.UnsupportedOperationException();
    }


    

    @Override
    public char removeAsChar(Object key) {
        throw new java.lang.UnsupportedOperationException();
    }

    char removeAsCharNullKey() {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean remove(Object key, Object value) {
        return remove(key,
                ((Character) value).charValue()
                );
    }

    @Override
    public boolean remove(Object key, char value) {
        throw new java.lang.UnsupportedOperationException();
    }

    boolean removeEntryNullKey(char value) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(ObjCharPredicate<? super K> filter) {
        throw new java.lang.UnsupportedOperationException();
    }






    class EntryView extends AbstractSetView<Map.Entry<K, Character>>
            implements HashObjSet<Map.Entry<K, Character>>,
            InternalObjCollectionOps<Map.Entry<K, Character>> {

        @Nonnull
        @Override
        public Equivalence<Entry<K, Character>> equivalence() {
            return Equivalence.entryEquivalence(
                    keyEquivalence(),
                    Equivalence.<Character>defaultEquality()
                    
            );
        }

        @Nonnull
        @Override
        public HashConfig hashConfig() {
            return ImmutableQHashSeparateKVObjCharMapGO.this.hashConfig();
        }


        @Override
        public int size() {
            return ImmutableQHashSeparateKVObjCharMapGO.this.size();
        }

        @Override
        public double currentLoad() {
            return ImmutableQHashSeparateKVObjCharMapGO.this.currentLoad();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            try {
                Map.Entry<K, Character> e = (Map.Entry<K, Character>) o;
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
            Object[] keys = set;
            char[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    result[resultIndex++] = new ImmutableEntry(key, vals[i]);
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
            Object[] keys = set;
            char[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    a[resultIndex++] = (T) new ImmutableEntry(key, vals[i]);
                }
            }
            if (a.length > resultIndex)
                a[resultIndex] = null;
            return a;
        }

        @Override
        public final void forEach(@Nonnull Consumer<? super Map.Entry<K, Character>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            Object[] keys = set;
            char[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    action.accept(new ImmutableEntry(key, vals[i]));
                }
            }
        }

        @Override
        public boolean forEachWhile(@Nonnull  Predicate<? super Map.Entry<K, Character>> predicate) {
            if (predicate == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return true;
            boolean terminated = false;
            Object[] keys = set;
            char[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    if (!predicate.test(new ImmutableEntry(key, vals[i]))) {
                        terminated = true;
                        break;
                    }
                }
            }
            return !terminated;
        }

        @Override
        @Nonnull
        public ObjIterator<Map.Entry<K, Character>> iterator() {
            
            return new NoRemovedEntryIterator();
        }

        @Nonnull
        @Override
        public ObjCursor<Map.Entry<K, Character>> cursor() {
            
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
            Object[] keys = set;
            char[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    if (!c.contains(e.with(key, vals[i]))) {
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
            Object[] keys = set;
            char[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    changed |= s.remove(e.with(key, vals[i]));
                }
            }
            return changed;
        }

        @Override
        public final boolean reverseAddAllTo(ObjCollection<? super Map.Entry<K, Character>> c) {
            if (isEmpty())
                return false;
            boolean changed = false;
            Object[] keys = set;
            char[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    changed |= c.add(new ImmutableEntry(key, vals[i]));
                }
            }
            return changed;
        }


        public int hashCode() {
            return ImmutableQHashSeparateKVObjCharMapGO.this.hashCode();
        }

        @Override
        public String toString() {
            if (isEmpty())
                return "[]";
            StringBuilder sb = new StringBuilder();
            int elementCount = 0;
            Object[] keys = set;
            char[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                K key;
                // noinspection unchecked
                if ((key = (K) keys[i]) != FREE) {
                    sb.append(' ');
                    sb.append(key != this ? key : "(this Collection)");
                    sb.append('=');
                    sb.append(vals[i]);
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
            return ImmutableQHashSeparateKVObjCharMapGO.this.shrink();
        }


        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object o) {
            try {
                Map.Entry<K, Character> e = (Map.Entry<K, Character>) o;
                K key = e.getKey();
                char value = e.getValue();
                return ImmutableQHashSeparateKVObjCharMapGO.this.remove(key, value);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }


        @Override
        public final boolean removeIf(@Nonnull Predicate<? super Map.Entry<K, Character>> filter) {
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
            ImmutableQHashSeparateKVObjCharMapGO.this.clear();
        }
    }


    abstract class ObjCharEntry extends AbstractEntry<K, Character> {

        abstract K key();

        @Override
        public final K getKey() {
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
            K k2;
            char v2;
            try {
                e2 = (Map.Entry) o;
                k2 = (K) e2.getKey();
                v2 = (Character) e2.getValue();
                return nullableKeyEquals(key(), k2)
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
            return nullableKeyHashCode(key())
                    ^
                    Primitives.hashCode(value())
                    ;
        }
    }


    private class ImmutableEntry extends ObjCharEntry {
        private final K key;
        private final char value;

        ImmutableEntry(K key, char value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K key() {
            return key;
        }

        @Override
        public char value() {
            return value;
        }
    }


    class ReusableEntry extends ObjCharEntry {
        private K key;
        private char value;

        ReusableEntry with(K key, char value) {
            this.key = key;
            this.value = value;
            return this;
        }

        @Override
        public K key() {
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
            return ImmutableQHashSeparateKVObjCharMapGO.this.size();
        }

        @Override
        public boolean shrink() {
            return ImmutableQHashSeparateKVObjCharMapGO.this.shrink();
        }

        @Override
        public boolean contains(Object o) {
            return ImmutableQHashSeparateKVObjCharMapGO.this.containsValue(o);
        }

        @Override
        public boolean contains(char v) {
            return ImmutableQHashSeparateKVObjCharMapGO.this.containsValue(v);
        }



        @Override
        public void forEach(Consumer<? super Character> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            Object[] keys = set;
            char[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    action.accept(vals[i]);
                }
            }
        }

        @Override
        public void forEach(CharConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            if (isEmpty())
                return;
            Object[] keys = set;
            char[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    action.accept(vals[i]);
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
            Object[] keys = set;
            char[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    if (!predicate.test(vals[i])) {
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
            Object[] keys = set;
            char[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    if (!c.contains(vals[i])) {
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
            Object[] keys = set;
            char[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    changed |= c.add(vals[i]);
                }
            }
            return changed;
        }


        @Override
        public boolean reverseRemoveAllFrom(CharSet s) {
            if (isEmpty() || s.isEmpty())
                return false;
            boolean changed = false;
            Object[] keys = set;
            char[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    changed |= s.removeChar(vals[i]);
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
            Object[] keys = set;
            char[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    result[resultIndex++] = vals[i];
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
            Object[] keys = set;
            char[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    a[resultIndex++] = (T) Character.valueOf(vals[i]);
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
            Object[] keys = set;
            char[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    result[resultIndex++] = vals[i];
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
            Object[] keys = set;
            char[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    a[resultIndex++] = vals[i];
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
            Object[] keys = set;
            char[] vals = values;
            for (int i = keys.length - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    sb.append(' ').append(vals[i]).append(',');
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
            ImmutableQHashSeparateKVObjCharMapGO.this.clear();
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



    class NoRemovedEntryIterator implements ObjIterator<Map.Entry<K, Character>> {
        final K[] keys;
        final char[] vals;
        int nextIndex;
        ImmutableEntry next;

        NoRemovedEntryIterator() {
            // noinspection unchecked
            K[] keys = this.keys = (K[]) set;
            char[] vals = this.vals = values;
            int nextI = keys.length;
            while (--nextI >= 0) {
                Object key;
                if ((key = keys[nextI]) != FREE) {
                    // noinspection unchecked
                    next = new ImmutableEntry((K) key, vals[nextI]);
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public void forEachRemaining(@Nonnull Consumer<? super Map.Entry<K, Character>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            K[] keys = this.keys;
            char[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE) {
                    // noinspection unchecked
                    action.accept(new ImmutableEntry((K) key, vals[i]));
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
        public Map.Entry<K, Character> next() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                K[] keys = this.keys;
                ImmutableEntry prev = next;
                while (--nextI >= 0) {
                    Object key;
                    if ((key = keys[nextI]) != FREE) {
                        // noinspection unchecked
                        next = new ImmutableEntry((K) key, vals[nextI]);
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


    class NoRemovedEntryCursor implements ObjCursor<Map.Entry<K, Character>> {
        final K[] keys;
        final char[] vals;
        int index;
        Object curKey;
        char curValue;

        NoRemovedEntryCursor() {
            // noinspection unchecked
            this.keys = (K[]) set;
            index = keys.length;
            vals = values;
            curKey = FREE;
        }

        @Override
        public void forEachForward(Consumer<? super Map.Entry<K, Character>> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            K[] keys = this.keys;
            char[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE) {
                    // noinspection unchecked
                    action.accept(new ImmutableEntry((K) key, vals[i]));
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE;
        }

        @Override
        public Map.Entry<K, Character> elem() {
            Object curKey;
            if ((curKey = this.curKey) != FREE) {
                // noinspection unchecked
                return new ImmutableEntry((K) curKey, curValue);
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            K[] keys = this.keys;
            for (int i = index - 1; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE) {
                    index = i;
                    curKey = key;
                    curValue = vals[i];
                    return true;
                }
            }
            curKey = FREE;
            index = -1;
            return false;
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }




    class NoRemovedValueIterator implements CharIterator {
        final K[] keys;
        final char[] vals;
        int nextIndex;
        char next;

        NoRemovedValueIterator() {
            // noinspection unchecked
            K[] keys = this.keys = (K[]) set;
            char[] vals = this.vals = values;
            int nextI = keys.length;
            while (--nextI >= 0) {
                if (keys[nextI] != FREE) {
                    // noinspection unchecked
                    next = vals[nextI];
                    break;
                }
            }
            nextIndex = nextI;
        }

        @Override
        public char nextChar() {
            int nextI;
            if ((nextI = nextIndex) >= 0) {
                K[] keys = this.keys;
                char prev = next;
                while (--nextI >= 0) {
                    if (keys[nextI] != FREE) {
                        // noinspection unchecked
                        next = vals[nextI];
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
            K[] keys = this.keys;
            char[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if (keys[i] != FREE) {
                    // noinspection unchecked
                    action.accept(vals[i]);
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
            K[] keys = this.keys;
            char[] vals = this.vals;
            int nextI = nextIndex;
            for (int i = nextI; i >= 0; i--) {
                if (keys[i] != FREE) {
                    // noinspection unchecked
                    action.accept(vals[i]);
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
        final K[] keys;
        final char[] vals;
        int index;
        Object curKey;
        char curValue;

        NoRemovedValueCursor() {
            // noinspection unchecked
            this.keys = (K[]) set;
            index = keys.length;
            vals = values;
            curKey = FREE;
        }

        @Override
        public void forEachForward(CharConsumer action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            K[] keys = this.keys;
            char[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                if (keys[i] != FREE) {
                    // noinspection unchecked
                    action.accept(vals[i]);
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE;
        }

        @Override
        public char elem() {
            if (curKey != FREE) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            K[] keys = this.keys;
            for (int i = index - 1; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE) {
                    index = i;
                    curKey = key;
                    curValue = vals[i];
                    return true;
                }
            }
            curKey = FREE;
            index = -1;
            return false;
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }



    class NoRemovedMapCursor implements ObjCharCursor<K> {
        final K[] keys;
        final char[] vals;
        int index;
        Object curKey;
        char curValue;

        NoRemovedMapCursor() {
            // noinspection unchecked
            this.keys = (K[]) set;
            index = keys.length;
            vals = values;
            curKey = FREE;
        }

        @Override
        public void forEachForward(ObjCharConsumer<? super K> action) {
            if (action == null)
                throw new java.lang.NullPointerException();
            K[] keys = this.keys;
            char[] vals = this.vals;
            int index = this.index;
            for (int i = index - 1; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE) {
                    // noinspection unchecked
                    action.accept((K) key, vals[i]);
                }
            }
            if (index != this.index) {
                throw new java.util.ConcurrentModificationException();
            }
            this.index = -1;
            curKey = FREE;
        }

        @Override
        public K key() {
            Object curKey;
            if ((curKey = this.curKey) != FREE) {
                // noinspection unchecked
                return (K) curKey;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public char value() {
            if (curKey != FREE) {
                return curValue;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }


        @Override
        public void setValue(char value) {
            if (curKey != FREE) {
                vals[index] = value;
            } else {
                throw new java.lang.IllegalStateException();
            }
        }

        @Override
        public boolean moveNext() {
            K[] keys = this.keys;
            for (int i = index - 1; i >= 0; i--) {
                Object key;
                if ((key = keys[i]) != FREE) {
                    index = i;
                    curKey = key;
                    curValue = vals[i];
                    return true;
                }
            }
            curKey = FREE;
            index = -1;
            return false;
        }

        @Override
        public void remove() {
                throw new java.lang.UnsupportedOperationException();
        }
    }
}

