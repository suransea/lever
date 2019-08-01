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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import top.srsea.lever.Lever;
import top.srsea.torque.value.Property;

import java.util.Set;

public abstract class Preference<T> implements Property<T> {
    protected SharedPreferences sharedPreferences;
    protected String key;
    protected T defaultValue;

    public Preference(String key, T defaultValue) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Lever.getContext());
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public Preference(String key, T defaultValue, String preferenceName) {
        this.sharedPreferences = Lever.getContext().getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public static Preference<String> create(String key, String defaultValue, String prefName) {
        return new StringPreference(key, defaultValue, prefName);
    }

    public static Preference<Integer> create(String key, int defaultValue, String prefName) {
        return new IntPreference(key, defaultValue, prefName);
    }

    public static Preference<Long> create(String key, long defaultValue, String prefName) {
        return new LongPreference(key, defaultValue, prefName);
    }

    public static Preference<Boolean> create(String key, boolean defaultValue, String prefName) {
        return new BooleanPreference(key, defaultValue, prefName);
    }

    public static Preference<Float> create(String key, float defaultValue, String prefName) {
        return new FloatPreference(key, defaultValue, prefName);
    }

    public static Preference<Set<String>> create(String key, Set<String> defaultValue, String prefName) {
        return new StringSetPreference(key, defaultValue, prefName);
    }
}
