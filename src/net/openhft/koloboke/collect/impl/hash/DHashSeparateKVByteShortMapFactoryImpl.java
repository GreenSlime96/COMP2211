/* with
 DHash|QHash|LHash hash
 byte|char|short|int|long|float|double|obj key
 short|byte|char|int|long|float|double|obj value
 Separate|Parallel kv
*/
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
import net.openhft.koloboke.collect.hash.*;
import net.openhft.koloboke.collect.map.hash.*;

import javax.annotation.Nonnull;


public final class DHashSeparateKVByteShortMapFactoryImpl/*<>*/
        extends DHashSeparateKVByteShortMapFactoryGO/*<>*/ {

    /* define p1 */
    /* if obj key obj value //<K2 extends K, V2 extends V>// elif obj key //<K2 extends K>
    // elif obj value //<V2 extends V>// endif */
    /* enddefine */

    /* define p2 */
    /* if obj key obj value //<K2, V2>// elif obj key //<K2>// elif obj value //<V2>// endif */
    /* enddefine */

    /* define kAnd *//* if obj key //K, // endif *//* enddefine */
    /* define p1And *//* if obj key //K2 extends K, // endif *//* enddefine */
    /* define p2And *//* if obj key //K2, // endif *//* enddefine */

    /* define andV *//* if obj value //, V// endif *//* enddefine */
    /* define andP1 *//* if obj value //, V2 extends V// endif *//* enddefine */
    /* define andP2 *//* if obj value //, V2// endif *//* enddefine */

    /** For ServiceLoader */
    public DHashSeparateKVByteShortMapFactoryImpl() {
        this(HashConfig.getDefault(), 10
            /* if obj key */, false
            /* elif !(float|double key) */, Byte.MIN_VALUE, Byte.MAX_VALUE/* endif */);
    }

    /* define commonArgDef //
    HashConfig hashConf, int defaultExpectedSize// if obj key //, boolean isNullKeyAllowed
            // elif !(float|double key) //, byte lower, byte upper// endif //
    // enddefine */

    /* define commonArgApply //
    hashConf, defaultExpectedSize// if obj key //, isNullKeyAllowed
            // elif !(float|double key) //, lower, upper// endif //
    // enddefine */

    /* define commonArgGet //
    getHashConfig(), getDefaultExpectedSize()
        // if obj key //, isNullKeyAllowed()// elif !(float|double key) //
            , getLowerKeyDomainBound(), getUpperKeyDomainBound()// endif //
    // enddefine */

    DHashSeparateKVByteShortMapFactoryImpl(/* commonArgDef */) {
        super(/* commonArgApply */);
    }

    @Override
    HashByteShortMapFactory/*<>*/ thisWith(/* commonArgDef */) {
        return new DHashSeparateKVByteShortMapFactoryImpl/*<>*/(/* commonArgApply */);
    }

    /* with DHash|QHash|LHash hash */
    @Override
    HashByteShortMapFactory/*<>*/ dHashLikeThisWith(/* commonArgDef */) {
        return new DHashSeparateKVByteShortMapFactoryImpl/*<>*/(/* commonArgApply */);
    }
    /* endwith */

    /* if obj key */
    @Override
    @Nonnull
    public HashObjShortMapFactory/*<>*/ withKeyEquivalence(
            @Nonnull Equivalence<? super K> keyEquivalence) {
        if (keyEquivalence.equals(Equivalence.defaultEquality())) {
            // noinspection unchecked
            return (HashObjShortMapFactory/*<>*/) this;
        }
        return new WithCustomKeyEquivalence/*<>*/(/* commonArgGet */,
                (Equivalence<K>) keyEquivalence);
    }
    /* endif */

    /* if !(obj value) */
    @Override
    @Nonnull
    public HashByteShortMapFactory/*<>*/ withDefaultValue(short defaultValue) {
        if (defaultValue == /* const value 0 */0)
            return this;
        return new WithCustomDefaultValue/*<>*/(/* commonArgGet */, defaultValue);
    }
    /* elif obj value */
    @Override
    @Nonnull
    public HashByteObjMapFactory/*<>*/ withValueEquivalence(
            @Nonnull Equivalence<? super V> valueEquivalence) {
        if (valueEquivalence.equals(Equivalence.defaultEquality())) {
            // noinspection unchecked
            return (HashByteObjMapFactory/*<>*/) this;
        }
        return new WithCustomValueEquivalence/*<>*/(/* commonArgGet */,
                (Equivalence<V>) valueEquivalence);
    }
    /* endif */

    /* if obj key */
    static class WithCustomKeyEquivalence<K/*andV*/>
            extends DHashSeparateKVObjShortMapFactoryGO<K/*andV*/> {

        private final Equivalence<K> keyEquivalence;

        WithCustomKeyEquivalence(/* commonArgDef */, Equivalence<K> keyEquivalence) {
            super(/* commonArgApply */);
            this.keyEquivalence = keyEquivalence;
        }

        @Override
        @Nonnull
        public Equivalence<K> getKeyEquivalence() {
            return keyEquivalence;
        }

        /* with Mutable|Updatable|Immutable mutability */
        @Override
        <K2 extends K/*andP1*/> MutableDHashSeparateKVObjShortMapGO<K2/*andP2*/>
        uninitializedMutableMap() {
            MutableDHashSeparateKVObjShortMap.WithCustomKeyEquivalence<K2/*andP2*/> map =
                    new MutableDHashSeparateKVObjShortMap.WithCustomKeyEquivalence<K2/*andP2*/>();
            map.keyEquivalence = keyEquivalence;
            return map;
        }
        /* endwith */

        @Override
        @Nonnull
        public HashObjShortMapFactory/*<>*/ withKeyEquivalence(
                @Nonnull Equivalence<? super K> keyEquivalence) {
            if (keyEquivalence.equals(Equivalence.defaultEquality()))
                return new DHashSeparateKVObjShortMapFactoryImpl/*<>*/(/* commonArgGet */);
            if (keyEquivalence.equals(this.keyEquivalence)) {
                // noinspection unchecked
                return (HashObjShortMapFactory/*<>*/) this;
            }
            return new WithCustomKeyEquivalence/*<>*/(/* commonArgGet */,
                    (Equivalence<K>) keyEquivalence);
        }

        /* if !(obj value) */
        @Override
        @Nonnull
        public HashObjShortMapFactory<K/*andV*/> withDefaultValue(short defaultValue) {
            if (defaultValue == /* const value 0 */0)
                return this;
            return new WithCustomKeyEquivalenceAndDefaultValue<K/*andV*/>(
                    /* commonArgGet */, keyEquivalence, defaultValue);
        }
        /* elif obj value */
        @Override
        @Nonnull
        public HashObjObjMapFactory/*<>*/ withValueEquivalence(
                @Nonnull Equivalence<? super V> valueEquivalence) {
            if (valueEquivalence.equals(Equivalence.defaultEquality())) {
                // noinspection unchecked
                return (HashObjObjMapFactory/*<>*/) this;
            }
            return new WithCustomEquivalences/*<>*/(/* commonArgGet */,
                    keyEquivalence, (Equivalence<V>) valueEquivalence);
        }
        /* endif */

        @Override
        HashByteShortMapFactory/*<>*/ thisWith(/* commonArgDef */) {
            return new WithCustomKeyEquivalence<K/*andV*/>(/* commonArgApply */, keyEquivalence);
        }

        /* with DHash|QHash|LHash hash */
        @Override
        HashByteShortMapFactory/*<>*/ dHashLikeThisWith(/* commonArgDef */) {
            return new DHashSeparateKVByteShortMapFactoryImpl.WithCustomKeyEquivalence<K/*andV*/>(
                    /* commonArgApply */, keyEquivalence);
        }
        /* endwith */
    }
    /* endif */

    /* if !(obj value) */
    static final class WithCustomDefaultValue/*<>*/
            extends DHashSeparateKVByteShortMapFactoryGO/*<>*/ {
        private final short defaultValue;

        WithCustomDefaultValue(/* commonArgDef */, short defaultValue) {
            super(/* commonArgApply */);
            this.defaultValue = defaultValue;
        }

        @Override
        public short getDefaultValue() {
            return defaultValue;
        }

        /* with Mutable|Updatable|Immutable mutability */
        @Override
        /*p1*/ MutableDHashSeparateKVByteShortMapGO/*p2*/ uninitializedMutableMap() {
            MutableDHashSeparateKVByteShortMap.WithCustomDefaultValue/*p2*/ map =
                    new MutableDHashSeparateKVByteShortMap.WithCustomDefaultValue/*p2*/();
            map.defaultValue = defaultValue;
            return map;
        }
        /* endwith */

        /* if obj key */
        @Override
        @Nonnull
        public HashObjShortMapFactory/*<>*/ withKeyEquivalence(
                @Nonnull Equivalence<? super K> keyEquivalence) {
            if (keyEquivalence.equals(Equivalence.defaultEquality())) {
                // noinspection unchecked
                return (HashObjShortMapFactory/*<>*/) this;
            }
            return new WithCustomKeyEquivalenceAndDefaultValue/*<>*/(/* commonArgGet */,
                    (Equivalence<K>) keyEquivalence, defaultValue);
        }
        /* endif */

        @Override
        @Nonnull
        public HashByteShortMapFactory/*<>*/ withDefaultValue(short defaultValue) {
            if (defaultValue == /* const value 0 */0)
                return new DHashSeparateKVByteShortMapFactoryImpl/*<>*/(/* commonArgGet */);
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomDefaultValue/*<>*/(/* commonArgGet */, defaultValue);
        }

        @Override
        HashByteShortMapFactory/*<>*/ thisWith(/* commonArgDef */) {
            return new WithCustomDefaultValue/*<>*/(/* commonArgApply */, defaultValue);
        }

        /* with DHash|QHash|LHash hash */
        @Override
        HashByteShortMapFactory/*<>*/ dHashLikeThisWith(/* commonArgDef */) {
            return new DHashSeparateKVByteShortMapFactoryImpl.WithCustomDefaultValue/*<>*/(
                    /* commonArgApply */, defaultValue);
        }
        /* endwith */
    }
    /* elif obj value */
    static final class WithCustomValueEquivalence</*kAnd*/V>
            extends DHashSeparateKVByteObjMapFactoryGO</*kAnd*/V> {

        private final Equivalence<V> valueEquivalence;
        WithCustomValueEquivalence(/* commonArgDef */,
                Equivalence<V> valueEquivalence) {
            super(/* commonArgApply */);
            this.valueEquivalence = valueEquivalence;
        }

        @Override
        @Nonnull
        public Equivalence<V> getValueEquivalence() {
            return valueEquivalence;
        }

        /* with Mutable|Updatable|Immutable mutability */
        @Override
        </*p1And*/V2 extends V> MutableDHashSeparateKVByteObjMapGO</*p2And*/V2>
        uninitializedMutableMap() {
            MutableDHashSeparateKVByteObjMap.WithCustomValueEquivalence</*p2And*/V2> map =
                    new MutableDHashSeparateKVByteObjMap.WithCustomValueEquivalence</*p2And*/V2>();
            map.valueEquivalence = valueEquivalence;
            return map;
        }
        /* endwith */

        /* if obj key */
        @Override
        @Nonnull
        public HashObjObjMapFactory/*<>*/ withKeyEquivalence(
                @Nonnull Equivalence<? super K> keyEquivalence) {
            if (keyEquivalence.equals(Equivalence.defaultEquality())) {
                // noinspection unchecked
                return (HashObjObjMapFactory/*<>*/) this;
            }
            return new WithCustomEquivalences/*<>*/(/* commonArgGet */,
                    (Equivalence<K>) keyEquivalence, valueEquivalence);
        }
        /* endif */

        @Override
        @Nonnull
        public HashByteObjMapFactory/*<>*/ withValueEquivalence(
                @Nonnull Equivalence<? super V> valueEquivalence) {
            if (valueEquivalence.equals(Equivalence.defaultEquality()))
                return new DHashSeparateKVByteObjMapFactoryImpl/*<>*/(/* commonArgGet */);
            if (valueEquivalence.equals(this.valueEquivalence))
                // noinspection unchecked
                return (HashByteObjMapFactory/*<>*/) this;
            return new WithCustomValueEquivalence/*<>*/(/* commonArgGet */,
                    (Equivalence<V>) valueEquivalence);
        }

        @Override
        HashByteShortMapFactory/*<>*/ thisWith(/* commonArgDef */) {
            return new WithCustomValueEquivalence</*kAnd*/V>(/* commonArgApply */,
                    valueEquivalence);
        }

        /* with DHash|QHash|LHash hash */
        @Override
        HashByteShortMapFactory/*<>*/ dHashLikeThisWith(/* commonArgDef */) {
            return new DHashSeparateKVByteShortMapFactoryImpl.WithCustomValueEquivalence</*kAnd*/V>(
                    /* commonArgApply */, valueEquivalence);
        }
        /* endwith */
    }
    /* endif */

    /* if obj key && !(obj value) */
    static final class WithCustomKeyEquivalenceAndDefaultValue<K>
            extends DHashSeparateKVObjShortMapFactoryGO<K> {
        private final Equivalence<K> keyEquivalence;
        private final short defaultValue;

        WithCustomKeyEquivalenceAndDefaultValue(/* commonArgDef */,
                Equivalence<K> keyEquivalence, short defaultValue) {
            super(/* commonArgApply */);
            this.keyEquivalence = keyEquivalence;
            this.defaultValue = defaultValue;
        }

        @Override
        @Nonnull
        public Equivalence<K> getKeyEquivalence() {
            return keyEquivalence;
        }

        @Override
        public short getDefaultValue() {
            return defaultValue;
        }

        /* with Mutable|Updatable|Immutable mutability */
        @Override
        <K2 extends K> MutableDHashSeparateKVObjShortMapGO<K2> uninitializedMutableMap() {
            MutableDHashSeparateKVObjShortMap.WithCustomKeyEquivalenceAndDefaultValue<K2> map =
                    new MutableDHashSeparateKVObjShortMap
                            .WithCustomKeyEquivalenceAndDefaultValue<K2>();
            map.keyEquivalence = keyEquivalence;
            map.defaultValue = defaultValue;
            return map;
        }
        /* endwith */

        @Override
        @Nonnull
        public HashObjShortMapFactory/*<>*/ withKeyEquivalence(
                @Nonnull Equivalence<? super K> keyEquivalence) {
            if (keyEquivalence.equals(Equivalence.defaultEquality()))
                return new WithCustomDefaultValue/*<>*/(/* commonArgGet */, defaultValue);
            if (keyEquivalence.equals(this.keyEquivalence)) {
                // noinspection unchecked
                return (HashObjShortMapFactory/*<>*/) this;
            }
            return new WithCustomKeyEquivalenceAndDefaultValue/*<>*/(
                    /* commonArgGet */, (Equivalence<K>) keyEquivalence, defaultValue);
        }

        @Override
        @Nonnull
        public HashObjShortMapFactory<K> withDefaultValue(short defaultValue) {
            if (defaultValue == /* const value 0 */0)
                return new WithCustomKeyEquivalence<K>(/* commonArgGet */, keyEquivalence);
            if (defaultValue == this.defaultValue)
                return this;
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(
                    /* commonArgGet */, keyEquivalence, defaultValue);
        }

        @Override
        HashByteShortMapFactory/*<>*/ thisWith(/* commonArgDef */) {
            return new WithCustomKeyEquivalenceAndDefaultValue<K>(/* commonArgApply */,
                    keyEquivalence, defaultValue);
        }

        /* with DHash|QHash|LHash hash */
        @Override
        HashByteShortMapFactory/*<>*/ dHashLikeThisWith(/* commonArgDef */) {
            return new DHashSeparateKVByteShortMapFactoryImpl
                        .WithCustomKeyEquivalenceAndDefaultValue<K>(/* commonArgApply */,
                    keyEquivalence, defaultValue);
        }
        /* endwith */
    }
    /* elif obj key obj value */
    static final class WithCustomEquivalences<K, V>
            extends DHashSeparateKVObjObjMapFactoryGO<K, V> {
        private final Equivalence<K> keyEquivalence;
        private final Equivalence<V> valueEquivalence;

        WithCustomEquivalences(/* commonArgDef */,
                Equivalence<K> keyEquivalence, Equivalence<V> valueEquivalence) {
            super(/* commonArgApply */);
            this.keyEquivalence = keyEquivalence;
            this.valueEquivalence = valueEquivalence;
        }

        @Override
        @Nonnull
        public Equivalence<K> getKeyEquivalence() {
            return keyEquivalence;
        }

        @Override
        @Nonnull
        public Equivalence<V> getValueEquivalence() {
            return valueEquivalence;
        }

        /* with Mutable|Updatable|Immutable mutability */
        @Override
        <K2 extends K, V2 extends V> MutableDHashSeparateKVObjObjMapGO<K2, V2>
        uninitializedMutableMap() {
            MutableDHashSeparateKVObjObjMap.WithCustomEquivalences<K2, V2> map =
                    new MutableDHashSeparateKVObjObjMap.WithCustomEquivalences<K2, V2>();
            map.keyEquivalence = keyEquivalence;
            map.valueEquivalence = valueEquivalence;
            return map;
        }
        /* endwith */

        @Override
        @Nonnull
        public HashObjObjMapFactory/*<>*/ withKeyEquivalence(
                @Nonnull Equivalence<? super K> keyEquivalence) {
            if (keyEquivalence.equals(Equivalence.defaultEquality()))
                return new WithCustomValueEquivalence/*<>*/(/* commonArgGet */,
                        valueEquivalence);
            if (keyEquivalence.equals(this.keyEquivalence)) {
                // noinspection unchecked
                return (HashObjObjMapFactory/*<>*/) this;
            }
            return new WithCustomEquivalences/*<>*/(/* commonArgGet */,
                    (Equivalence<K>) keyEquivalence, valueEquivalence);
        }

        @Override
        @Nonnull
        public HashObjObjMapFactory/*<>*/ withValueEquivalence(
                @Nonnull Equivalence<? super V> valueEquivalence) {
            if (valueEquivalence.equals(Equivalence.defaultEquality()))
                return new WithCustomKeyEquivalence/*<>*/(/* commonArgGet */, keyEquivalence);
            if (valueEquivalence.equals(this.valueEquivalence)) {
                // noinspection unchecked
                return (HashObjObjMapFactory/*<>*/) this;
            }
            return new WithCustomEquivalences/*<>*/(/* commonArgGet */,
                    keyEquivalence, (Equivalence<V>) valueEquivalence);
        }

        @Override
        HashByteShortMapFactory/*<>*/ thisWith(/* commonArgDef */) {
            return new WithCustomEquivalences<K, V>(/* commonArgApply */,
                    keyEquivalence, valueEquivalence);
        }

        /* with DHash|QHash|LHash hash */
        @Override
        HashByteShortMapFactory/*<>*/ dHashLikeThisWith(/* commonArgDef */) {
            return new DHashSeparateKVByteShortMapFactoryImpl.WithCustomEquivalences<K, V>(
                    /* commonArgApply */, keyEquivalence, valueEquivalence);
        }
        /* endwith */
    }
    /* endif */
}
