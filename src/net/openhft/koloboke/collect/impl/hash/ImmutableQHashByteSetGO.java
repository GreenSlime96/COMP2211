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
import net.openhft.koloboke.collect.impl.*;
import net.openhft.koloboke.collect.set.hash.HashByteSet;
import javax.annotation.Nonnull;

import java.util.*;


public class ImmutableQHashByteSetGO extends ImmutableByteQHashSetSO
        implements HashByteSet, InternalByteCollectionOps {

    @Override
    final void copy(SeparateKVByteQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.copy(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    @Override
    final void move(SeparateKVByteQHash hash) {
        int myMC = modCount(), hashMC = hash.modCount();
        super.move(hash);
        if (myMC != modCount() || hashMC != hash.modCount())
            throw new ConcurrentModificationException();
    }

    public int hashCode() {
        return setHashCode();
    }

    @Override
    public String toString() {
        return setToString();
    }

    @Override
    public boolean equals(Object obj) {
        return CommonSetOps.equals(this, obj);
    }

    @Override
    public boolean containsAll(@Nonnull Collection<?> c) {
        return CommonByteCollectionOps.containsAll(this, c);
    }

    @Nonnull
    @Override
    public ByteCursor cursor() {
        return setCursor();
    }


    @Override
    public boolean add(Byte e) {
        return add(e.byteValue());
    }

    @Override
    public boolean add(byte key) {
        throw new java.lang.UnsupportedOperationException();
    }


    @Override
    public boolean addAll(@Nonnull Collection<? extends Byte> c) {
        return CommonByteCollectionOps.addAll(this, c);
    }

    @Override
    public boolean remove(Object key) {
        return removeByte(((Byte) key).byteValue());
    }


    @Override
    boolean justRemove(byte key) {
        return removeByte(key);
    }

    @Override
    public boolean removeByte(byte key) {
        throw new java.lang.UnsupportedOperationException();
    }



    @Override
    public boolean removeAll(@Nonnull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(@Nonnull Collection<?> c) {
        return retainAll(this, c);
    }
}

