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

package top.srsea.lever.graph;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.concurrent.Callable;

import top.srsea.lever.common.DimensionUnit;
import top.srsea.torque.common.Result;

public class QRCode {

    /**
     * Generate a QRCode with the specific content.
     */
    public static Encoding encode(String content) {
        return new Encoding(content);
    }

    /**
     * Read text from the specific bitmap.
     */
    public static Result<String> decode(final Bitmap bitmap) {
        return Result.from(new Callable<String>() {
            @Override
            public String call() throws Exception {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int[] pixels = new int[width * height];
                bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
                BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
                return new QRCodeReader().decode(binaryBitmap).getText();
            }
        });
    }

    /**
     * Returns a bitmap with the logo from the source bitmap.
     * The logo width is one-fifth of the source bitmap.
     *
     * @param src  source bitmap
     * @param logo logo bitmap
     * @return a bitmap with the logo from the source bitmap, null when source bitmap is null
     */
    public static Bitmap withLogo(Bitmap src, Bitmap logo) {
        if (src == null) return null;
        if (logo == null) return src;
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        if (srcWidth == 0 || srcHeight == 0) return src;
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
        if (logoWidth == 0 || logoHeight == 0) return src;
        float scaleFactor = srcWidth / 5f / logoWidth;
        Bitmap bitmap = src.copy(src.getConfig(), true); // copy a mutable bitmap for drawing
        Canvas canvas = new Canvas(bitmap);
        canvas.scale(scaleFactor, scaleFactor, srcWidth / 2f, srcHeight / 2f);
        canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2f, (srcHeight - logoHeight) / 2f, null);
        return bitmap;
    }

    public static class Encoding {

        private final String content;

        // config for zxing
        private int colorPrimary = Color.BLACK;
        private int colorBackground = Color.WHITE;
        private int height = 100;
        private int width = 100;
        private int margin = 0;
        private String characterSet = "UTF-8";
        private String errorCorrection = "L"; //L, M, Q, H

        // config for bitmaps
        private Bitmap.Config config = Bitmap.Config.ARGB_8888;
        private Bitmap logo;

        public Encoding(String content) {
            this.content = content;
        }

        public Encoding colorPrimary(int colorPrimary) {
            this.colorPrimary = colorPrimary;
            return this;
        }

        public Encoding colorBackground(int colorBackground) {
            this.colorBackground = colorBackground;
            return this;
        }

        public Encoding height(int height) {
            this.height = height;
            return this;
        }

        public Encoding width(int width) {
            this.width = width;
            return this;
        }

        public Encoding size(int size) {
            return width(size).height(size);
        }

        public Encoding height(int height, DimensionUnit unit) {
            this.height = (int) unit.toPx(height);
            return this;
        }

        public Encoding width(int width, DimensionUnit unit) {
            this.width = (int) unit.toPx(width);
            return this;
        }

        public Encoding size(int size, DimensionUnit unit) {
            return size((int) unit.toPx(size));
        }

        public Encoding margin(int margin) {
            this.margin = margin;
            return this;
        }

        public Encoding characterSet(String characterSet) {
            this.characterSet = characterSet;
            return this;
        }

        /**
         * L, M, Q, H
         */
        public Encoding errorCorrection(String errorCorrection) {
            this.errorCorrection = errorCorrection;
            return this;
        }

        public Encoding logo(Bitmap logo) {
            this.logo = logo;
            return this;
        }

        public Encoding config(Bitmap.Config config) {
            this.config = config;
            return this;
        }

        public Result<Bitmap> result() {
            if (content == null) return Result.failure(new NullPointerException("content"));
            if (width < 0 || height < 0) {
                return Result.failure(new IllegalArgumentException("width or height must be positive."));
            }
            final HashMap<EncodeHintType, Object> hints = new HashMap<>();
            if (!TextUtils.isEmpty(characterSet)) {
                hints.put(EncodeHintType.CHARACTER_SET, characterSet);
            }
            if (!TextUtils.isEmpty(errorCorrection)) {
                hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrection);
            }
            if (margin >= 0) {
                hints.put(EncodeHintType.MARGIN, String.valueOf(margin));
            }
            return Result.from(new Callable<Bitmap>() {
                @Override
                public Bitmap call() throws Exception {
                    BitMatrix bitMatrix = new QRCodeWriter()
                            .encode(content, BarcodeFormat.QR_CODE, width, height, hints);
                    int[] pixels = new int[width * height];
                    for (int y = 0; y < height; ++y) {
                        for (int x = 0; x < width; ++x) {
                            pixels[y * width + x] = bitMatrix.get(x, y) ? colorPrimary : colorBackground;
                        }
                    }
                    Bitmap bitmap = Bitmap.createBitmap(width, height, config);
                    bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
                    if (logo != null) {
                        Bitmap withLogo = QRCode.withLogo(bitmap, logo);
                        bitmap.recycle();
                        bitmap = withLogo;
                    }
                    return bitmap;
                }
            });
        }
    }
}
