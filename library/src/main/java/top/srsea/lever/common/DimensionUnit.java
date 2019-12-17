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

/**
 * A {@code DimenUnit} represents the elements and font sizes in the android UI.
 *
 * @author sea
 * @see DimensionUnit#DP
 * @see DimensionUnit#PX
 * @see DimensionUnit#SP
 */
public enum DimensionUnit {

    /**
     * Device independent pixel.
     */
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

        public float convert(float value, DimensionUnit unit) {
            return unit.toDp(value);
        }
    },

    /**
     * Scale-independent pixel.
     */
    SP {
        public float toPx(float value) {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value,
                    Lever.getContext().getResources().getDisplayMetrics());
        }

        public float toDp(float value) {
            return PX.toDp(toPx(value));
        }

        public float toSp(float value) {
            return value;
        }

        public float convert(float value, DimensionUnit unit) {
            return unit.toSp(value);
        }
    },

    /**
     * Pixel.
     */
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

        public float convert(float value, DimensionUnit unit) {
            return unit.toPx(value);
        }
    };

    /**
     * Converts the given dimension in this unit to px.
     *
     * @param value the dimension in this unit
     * @return the converted dimension
     */
    public float toPx(float value) {
        throw new AbstractMethodError();
    }

    /**
     * Converts the given dimension in this unit to dp.
     *
     * @param value the dimension in this unit
     * @return the converted dimension
     */
    public float toDp(float value) {
        throw new AbstractMethodError();
    }

    /**
     * Converts the given dimension in this unit to sp.
     *
     * @param value the dimension in this unit
     * @return the converted dimension
     */
    public float toSp(float value) {
        throw new AbstractMethodError();
    }

    /**
     * Converts the given dimension in the given unit to this unit.
     *
     * @param value the dimension in the given {@code unit}
     * @param unit  the unit of the {@code value} argument
     * @return the converted dimension in this unit
     */
    public float convert(float value, DimensionUnit unit) {
        throw new AbstractMethodError();
    }
}
