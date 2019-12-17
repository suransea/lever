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

/**
 * String preference.
 *
 * @author sea
 * @see Preference
 */
public class StringPreference extends Preference<String> {

    public StringPreference(String key, String defaultValue) {
        super(key, defaultValue);
    }

    public StringPreference(String key, String defaultValue, String preferenceName) {
        super(key, defaultValue, preferenceName);
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void blockingSet(String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    @Override
    public void set(String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    @Override
    public String get() {
        return sharedPreferences.getString(key, defaultValue);
    }
}
