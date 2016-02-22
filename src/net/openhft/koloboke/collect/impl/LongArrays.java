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

package net.openhft.koloboke.collect.impl;


public final class LongArrays implements UnsafeConstants {

    public static void replaceAll(long[] a, long oldValue, long newValue) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == oldValue) {
                a[i] = newValue;
            }
        }
    }

    public static void replaceAllKeys(long[] table, long oldKey, long newKey) {
        for (int i = 0; i < table.length; i += 2) {
            if (table[i] == oldKey) {
                table[i] = newKey;
            }
        }
    }

    public static void fillKeys(long[] table, long key) {
        for (int i = 0; i < table.length; i += 2) {
            table[i] = key;
        }
    }

    private LongArrays() {}
}

