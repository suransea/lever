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
import android.graphics.Color;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;

import top.srsea.lever.common.DimensionUnit;

/**
 * A QR code object for building QR code bitmap.
 *
 * @author sea
 */
public class QRCode {

    /**
     * Content of this QR code.
     */
    private final String content;

    // config for zxing
    private int colorPrimary = Color.BLACK;
    private int colorBackground = Color.WHITE;
    private int height = 100;
    private int width = 100;
    private int margin = 0;
    private String characterSet = "UTF-8";
    private String errorCorrection = "L"; //L, M, H

    // config for bitmaps
    private Bitmap.Config config = Bitmap.Config.ARGB_8888;
    private Bitmap logo;

    /**
     * Constructs an instance with the specific content.
     *
     * @param content content in QR code
     */
    public QRCode(String content) {
        this.content = content;
    }

    /**
     * Creates a bitmap from this QR code.
     *
     * @return a bitmap from this QR code
     */
    public Bitmap toBitmap() {
        if (TextUtils.isEmpty(content)) return null;
        if (width < 0 || height < 0) return null;
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        if (!TextUtils.isEmpty(characterSet)) {
            hints.put(EncodeHintType.CHARACTER_SET, characterSet);
        }
        if (!TextUtils.isEmpty(errorCorrection)) {
            hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrection);
        }
        if (margin >= 0) {
            hints.put(EncodeHintType.MARGIN, String.valueOf(margin));
        }
        try {
            BitMatrix bitMatrix = new QRCodeWriter()
                    .encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = colorPrimary;
                    } else {
                        pixels[y * width + x] = colorBackground;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, config);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            if (logo != null) {
                Bitmap withLogo = QRCodeHelper.addLogo(bitmap, logo);
                if (!bitmap.isRecycled()) bitmap.recycle();
                bitmap = withLogo;
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public QRCode colorPrimary(int colorPrimary) {
        this.colorPrimary = colorPrimary;
        return this;
    }

    public QRCode colorBackground(int colorBackground) {
        this.colorBackground = colorBackground;
        return this;
    }

    public QRCode height(int height) {
        this.height = height;
        return this;
    }

    public QRCode width(int width) {
        this.width = width;
        return this;
    }

    public QRCode size(int size) {
        return width(size).height(size);
    }

    public QRCode height(int height, DimensionUnit unit) {
        this.height = (int) unit.toPx(height);
        return this;
    }

    public QRCode width(int width, DimensionUnit unit) {
        this.width = (int) unit.toPx(width);
        return this;
    }

    public QRCode size(int size, DimensionUnit unit) {
        return size((int) unit.toPx(size));
    }

    public QRCode margin(int margin) {
        this.margin = margin;
        return this;
    }

    public QRCode characterSet(String characterSet) {
        this.characterSet = characterSet;
        return this;
    }

    public QRCode errorCorrection(String errorCorrection) { //L, M, H
        this.errorCorrection = errorCorrection;
        return this;
    }

    public QRCode logo(Bitmap logo) {
        this.logo = logo;
        return this;
    }

    public QRCode config(Bitmap.Config config) {
        this.config = config;
        return this;
    }

    /**
     * Gets the content of this QR code.
     *
     * @return the content of this QR code.
     */
    @NonNull
    @Override
    public String toString() {
        return content == null ? "" : content;
    }
}
