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

package top.srsea.lever.common;

import android.util.TypedValue;
import top.srsea.lever.Lever;

public enum DimensionUnit {
    DP {
        public float toPx(float value) {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                    Lever.getContext().getResources().getDisplayMetrics());
        }

        public float toDp(float value) {
            return value;
        }

        public float toSp(float value) {
            return PX.toSp(toPx(value));
        }

        public float convert(float value, DimensionUnit unit){
            return unit.toDp(value);
        }
    },
    SP {
        public float toPx(float value) {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value,
                    Lever.getContext().getResources().getDisplayMetrics());
        }

        public float toDp(float value) {
            return PX.toSp(toPx(value));
        }

        public float toSp(float value) {
            return value;
        }

        public float convert(float value, DimensionUnit unit){
            return unit.toSp(value);
        }
    },
    PX {
        public float toPx(float value) {
            return value;
        }

        public float toDp(float value) {
            return value / Lever.getContext().getResources().getDisplayMetrics().density;
        }

        public float toSp(float value) {
            return value / Lever.getContext().getResources().getDisplayMetrics().scaledDensity;
        }

        public float convert(float value, DimensionUnit unit){
            return unit.toPx(value);
        }
    };

    public float toPx(float value) {
        throw new AbstractMethodError();
    }

    public float toDp(float value) {
        throw new AbstractMethodError();
    }

    public float toSp(float value) {
        throw new AbstractMethodError();
    }

    public float convert(float value, DimensionUnit unit){
        throw new AbstractMethodError();
    }
}
