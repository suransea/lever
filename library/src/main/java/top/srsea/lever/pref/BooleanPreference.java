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
 * Boolean preference.
 *
 * @author sea
 * @see Preference
 */
public class BooleanPreference extends Preference<Boolean> {

    public BooleanPreference(String key, Boolean defaultValue) {
        super(key, defaultValue);
    }

    public BooleanPreference(String key, Boolean defaultValue, String preferenceName) {
        super(key, defaultValue, preferenceName);
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void blockingSet(Boolean value) {
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    @Override
    public void set(Boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    @Override
    public Boolean get() {
        return sharedPreferences.getBoolean(key, defaultValue);
    }
}
