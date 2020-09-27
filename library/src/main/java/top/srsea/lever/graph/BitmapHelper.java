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
import android.graphics.Point;
import android.graphics.PointF;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import top.srsea.lever.storage.MediaHelper;
import top.srsea.lever.storage.StorageHelper;
import top.srsea.torque.common.IOHelper;
import top.srsea.torque.common.Preconditions;

public class BitmapHelper {

    /**
     * Returns a bitmap with the specific watermark from the source bitmap.
     * The watermark pivot coincides with the bitmap position to locate the location.
     *
     * @param source           source bitmap
     * @param watermark        watermark bitmap
     * @param relativePosition relative position on source bitmap, range is [0, 1]
     * @param relativePivot    relative pivot on watermark bitmap, range is [0, 1]
     * @return the bitmap with the watermark
     */
    public static Bitmap addWatermark(Bitmap source, Bitmap watermark, PointF relativePosition, PointF relativePivot) {
        float[] params = new float[]{relativePosition.x, relativePosition.y, relativePivot.x, relativePivot.y};
        for (float value : params) {
            Preconditions.require(value >= 0 && value <= 1);
        }
        Point position = new Point((int) (source.getWidth() * relativePosition.x),
                (int) (source.getHeight() * relativePosition.y));
        Point pivot = new Point((int) (watermark.getWidth() * relativePivot.x),
                (int) (watermark.getHeight() * relativePivot.y));
        return addWatermark(source, watermark, position, pivot);
    }

    /**
     * Returns a bitmap with the specific watermark from the source bitmap.
     * The watermark pivot coincides with the bitmap position to locate the location.
     *
     * @param source    source bitmap
     * @param watermark watermark bitmap
     * @param position  the position on source bitmap
     * @param pivot     the pivot on watermark bitmap
     * @return the bitmap with the watermark
     */
    public static Bitmap addWatermark(Bitmap source, Bitmap watermark, Point position, Point pivot) {
        Bitmap target = source.copy(source.getConfig(), true);
        Canvas canvas = new Canvas(target);
        float left = position.x - pivot.x;
        float top = position.y - pivot.y;
        canvas.drawBitmap(watermark, left, top, null);
        return target;
    }

    /**
     * Saves the bitmap to the cache directory.
     *
     * @param bitmap  source bitmap
     * @param format  compress format, see {@link Bitmap#compress}
     * @param quality compress quality, see {@link Bitmap#compress}
     * @return an observable file object, filename generated via {@link UUID#randomUUID()}
     */
    public static Single<File> saveToCacheDir(final Bitmap bitmap, final Bitmap.CompressFormat format, final int quality) {
        return Single.fromCallable(new Callable<File>() {
            @Override
            public File call() throws Exception {
                File cacheDir = StorageHelper.getCacheDir();
                String filename = UUID.randomUUID().toString().concat(".").concat(format.name().toLowerCase());
                File target = new File(cacheDir, filename);
                OutputStream out = new FileOutputStream(target);
                bitmap.compress(format, quality, out);
                IOHelper.close(out);
                return target;
            }
        });
    }

    /**
     * Inserts bitmap to media store.
     *
     * @param bitmap  source bitmap
     * @param name    display name
     * @param format  compress format
     * @param quality compress quality
     * @return the observable content URI in the media store
     * @see MediaHelper#insertToMediaStore(Bitmap, String, Bitmap.CompressFormat, int)
     */
    public static Single<Uri> insertToMediaStore(final Bitmap bitmap, final String name, final Bitmap.CompressFormat format, final int quality) {
        return MediaHelper.insertToMediaStore(bitmap, name, format, quality);
    }
}
