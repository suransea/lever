/*
 * Copyright (C) 2019 sea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.srsea.lever.pref;

import android.annotation.SuppressLint;

import java.util.Set;

public class StringSetPreference extends Preference<Set<String>> {

    public StringSetPreference(String key, Set<String> defaultValue) {
        super(key, defaultValue);
    }

    public StringSetPreference(String key, Set<String> defaultValue, String preferenceName) {
        super(key, defaultValue, preferenceName);
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void blockingSet(Set<String> value) {
        sharedPreferences.edit().putStringSet(key, value).commit();
    }

    @Override
    public void set(Set<String> value) {
        sharedPreferences.edit().putStringSet(key, value).apply();
    }

    @Override
    public Set<String> get() {
        return sharedPreferences.getStringSet(key, defaultValue);
    }
}
