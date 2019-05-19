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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;

import androidx.annotation.NonNull;

public class QRCode {
    private final String content;
    private int colorPrimary = Color.BLACK;
    private int colorBackground = Color.WHITE;
    private int height = 100;
    private int width = 100;
    private int margin = 0;
    private String characterSet = "UTF-8";
    private String errorCorrection = "L";

    public QRCode(String content) {
        this.content = content;
    }

    public Bitmap toBitmap() {
        return toBitmap(null);
    }

    public Bitmap toBitmap(Bitmap logo) {
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
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            if (logo != null) {
                bitmap = QRCodes.addLogo(bitmap, logo);
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
        width = (height = size);
        return this;
    }

    public QRCode margin(int margin) {
        this.margin = margin;
        return this;
    }

    public QRCode characterSet(String characterSet) {
        this.characterSet = characterSet;
        return this;
    }

    public QRCode errorCorrection(String errorCorrection) {
        this.errorCorrection = errorCorrection;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return content == null ? "" : content;
    }
}
