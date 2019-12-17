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
import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

/**
 * Utilities for QR codes.
 *
 * @author sea
 */
public class QRCodes {

    /**
     * Creates a QRCode with the specific content.
     *
     * @param content the specific content
     * @return a QRCode object
     */
    public static QRCode from(String content) {
        return new QRCode(content);
    }

    /**
     * Creates a QRCode read from the specific bitmap.
     *
     * @param bitmap the QR code bitmap
     * @return a QRCode object
     */
    public static QRCode from(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            Result re = reader.decode(binaryBitmap);
            return new QRCode(re.getText());
        } catch (FormatException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return new QRCode("");
    }

    /**
     * Returns a bitmap with the logo from the source bitmap.
     * The logo width is one-fifth of the source bitmap.
     *
     * @param src  source bitmap
     * @param logo logo bitmap
     * @return a bitmap with the logo from the source bitmap, null when source bitmap is null
     */
    public static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) return null;
        if (logo == null) return src;
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
        if (srcWidth == 0 || srcHeight == 0) return null;
        if (logoWidth == 0 || logoHeight == 0) return src;
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = src.copy(src.getConfig(), true); //copy a mutable bitmap for drawing
        Canvas canvas = new Canvas(bitmap);
        canvas.scale(scaleFactor, scaleFactor, srcWidth / 2f, srcHeight / 2f);
        canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2f, (srcHeight - logoHeight) / 2f, null);
        return bitmap;
    }
}
