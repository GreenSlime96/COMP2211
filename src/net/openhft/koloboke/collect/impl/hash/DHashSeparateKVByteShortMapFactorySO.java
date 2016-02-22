/* with
 DHash|QHash|LHash hash
 byte|char|short|int|long|float|double key
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

import net.openhft.koloboke.collect.hash.*;
import net.openhft.koloboke.collect.map.ByteShortMap;
import net.openhft.koloboke.collect.map.hash.HashByteShortMapFactory;

import javax.annotation.Nonnull;
import java.util.Map;


public abstract class DHashSeparateKVByteShortMapFactorySO/*<>*/
        extends ByteDHashFactory /* if !(float|double key) && !(LHash hash) */
                        <MutableDHashSeparateKVByteShortMapGO/*<>*/>/* endif */
        implements HashByteShortMapFactory/*<>*/ {

    DHashSeparateKVByteShortMapFactorySO(HashConfig hashConf, int defaultExpectedSize
            /* if !(float|double key) */, byte lower, byte upper/* endif */) {
        super(hashConf, defaultExpectedSize/* if !(float|double key) */, lower, upper/* endif */);
    }

    /* define p1 *//* if obj value //<V2 extends V>// endif *//* enddefine */

    /* define p2 *//* if obj value //<V2>// endif *//* enddefine */

    /* define pv *//* if !(obj value) //short// elif obj value //V2// endif *//* enddefine */

    /* if !(float|double key) && !(LHash hash) */
    @Override
    MutableDHashSeparateKVByteShortMapGO/*<>*/ createNewMutable(
            int expectedSize, byte free, byte removed) {
        MutableDHashSeparateKVByteShortMapGO/*<>*/ map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, free, removed);
        return map;
    }
    /* endif */

    /* with Mutable|Updatable|Immutable mutability */
    /*p1*/ MutableDHashSeparateKVByteShortMapGO/*p2*/ uninitializedMutableMap() {
        return new MutableDHashSeparateKVByteShortMap/*p2*/();
    }
    /* endwith */

    /* with Mutable|Updatable mutability */
    @Override
    @Nonnull
    public /*p1*/ MutableDHashSeparateKVByteShortMapGO/*p2*/ newMutableMap(int expectedSize) {
        /* if float|double key */
        MutableDHashSeparateKVByteShortMapGO/*p2*/ map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize);
        return map;
        /* elif !(float|double key) && !(LHash hash) && Mutable mutability */
        // noinspection unchecked
        return (MutableDHashSeparateKVByteShortMapGO/*p2*/) newMutableHash(expectedSize);
        /* elif LHash hash || Updatable mutability */
        MutableDHashSeparateKVByteShortMapGO/*p2*/ map = uninitializedMutableMap();
        map.init(configWrapper, expectedSize, getFree());
        return map;
        /* endif */
    }

    /* define ev */
    /* if !(obj value) //Short// elif obj value //? extends V2// endif */
    /* enddefine */

    /* if Updatable mutability */
    @Override
    @Nonnull
    public /*p1*/ MutableDHashSeparateKVByteShortMapGO/*p2*/ newMutableMap(
            Map<Byte, /*ev*/Short/**/> map) {
        if (map instanceof ByteShortMap) {
            if (map instanceof SeparateKVByteShortDHash) {
                SeparateKVByteShortDHash hash = (SeparateKVByteShortDHash) map;
                if (hash.hashConfig().equals(hashConf)) {
                    MutableDHashSeparateKVByteShortMapGO/*p2*/ res = uninitializedMutableMap();
                    res.copy(hash);
                    return res;
                }
            }
            MutableDHashSeparateKVByteShortMapGO/*p2*/ res = newMutableMap(map.size());
            res.putAll(map);
            return res;
        }
        MutableDHashSeparateKVByteShortMapGO/*p2*/ res = newMutableMap(map.size());
        for (Map.Entry<Byte, /*ev*/Short/**/> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
    /* endif */
    /* endwith */
}
