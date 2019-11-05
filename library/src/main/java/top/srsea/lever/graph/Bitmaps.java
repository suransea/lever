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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.View;
import androidx.annotation.NonNull;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import top.srsea.lever.Lever;
import top.srsea.lever.storage.MediaHelper;
import top.srsea.lever.storage.StorageHelper;
import top.srsea.torque.common.IOUtils;
import top.srsea.torque.common.Preconditions;
import top.srsea.torque.network.DownloadService;
import top.srsea.torque.network.RetrofitProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.Callable;

public class Bitmaps {

    public static Bitmap fromView(@NonNull View view, Bitmap.Config config) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), config);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static Single<Bitmap> fromUri(final Uri uri) {
        return Single.fromCallable(new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {
                Context context = Lever.getContext();
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                Preconditions.requireNonNull(pfd);
                Bitmap bitmap = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());
                IOUtils.close(pfd);
                return bitmap;
            }
        });
    }

    public static Single<Bitmap> fromHttp(final String url) {
        return Single.fromObservable(RetrofitProvider.get()
                .create(DownloadService.class)
                .download(url)
                .map(new Function<ResponseBody, Bitmap>() {
                    @Override
                    public Bitmap apply(ResponseBody responseBody) {
                        InputStream stream = responseBody.byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(stream);
                        IOUtils.close(stream);
                        return bitmap;
                    }
                }));
    }

    public static Bitmap addWatermark(Bitmap source, Bitmap watermark, PointF position, PointF pivot) {
        float[] params = new float[]{position.x, position.y, pivot.x, pivot.y};
        for (float value : params) {
            Preconditions.require(value >= 0 && value <= 1);
        }
        float sourceWidth = source.getWidth();
        float sourceHeight = source.getHeight();
        float watermarkWidth = watermark.getWidth();
        float watermarkHeight = watermark.getHeight();
        Bitmap target = source.copy(source.getConfig(), true);
        Canvas canvas = new Canvas(target);
        PointF watermarkPosition = new PointF(sourceWidth * position.x, sourceHeight * position.y);
        float left = watermarkPosition.x - watermarkWidth * pivot.x;
        float top = watermarkPosition.y - watermarkHeight * pivot.y;
        canvas.drawBitmap(watermark, left, top, null);
        return target;
    }

    /**
     * 将bitmap保存到缓存文件夹
     *
     * @param bitmap  源bitmap
     * @param format  压缩格式 {@link Bitmap#compress}
     * @param quality 质量 {@link Bitmap#compress}
     * @return 保存后的文件对象, 文件名为随机UUID {@link UUID#randomUUID()}
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
                IOUtils.close(out);
                return target;
            }
        });
    }

    public static Single<Uri> insertToMediaStore(final Bitmap bitmap, final String name, final Bitmap.CompressFormat format, final int quality) {
        return MediaHelper.insertToMediaStore(bitmap, name, format, quality);
    }
}
