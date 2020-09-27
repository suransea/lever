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
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.view.View;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import top.srsea.lever.Lever;
import top.srsea.torque.common.IOHelper;
import top.srsea.torque.common.Preconditions;
import top.srsea.torque.network.DownloadService;
import top.srsea.torque.network.RetrofitProvider;

/**
 * Bitmaps factory.
 *
 * @author sea
 * @see Bitmap
 */
public class Bitmaps {

    /**
     * Creates a bitmap from the specific view, which has been measured and layout.
     *
     * @param view   the specific view
     * @param config the bitmap config to create
     * @return a bitmap from the view
     * @throws IllegalArgumentException the view not measured or layout
     */
    public static Bitmap fromView(@NonNull View view, Bitmap.Config config) {
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), config);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * Decodes a bitmap from the specific image file.
     *
     * @param file the specific image file
     * @return an observable bitmap from the file
     */
    public static Single<Bitmap> fromFile(File file) {
        return fromUri(Uri.fromFile(file));
    }

    /**
     * Decodes a bitmap from a content, resource or file URI.
     * For a remote URI, use {@link Bitmaps#fromUrl(String)}.
     *
     * @param uri content, resource or file URI
     * @return an observable bitmap from URI
     * @see android.content.ContentResolver#SCHEME_FILE
     * @see android.content.ContentResolver#SCHEME_CONTENT
     * @see android.content.ContentResolver#SCHEME_ANDROID_RESOURCE
     */
    public static Single<Bitmap> fromUri(final Uri uri) {
        return Single.fromCallable(new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {
                Context context = Lever.getContext();
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                Preconditions.requireNonNull(pfd);
                Bitmap bitmap = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());
                IOHelper.close(pfd);
                return bitmap;
            }
        });
    }

    /**
     * Fetches a bitmap from the URL.
     *
     * @param url the URL to image file
     * @return an observable bitmap from the URL
     */
    public static Single<Bitmap> fromUrl(final String url) {
        return Single.fromObservable(RetrofitProvider.get()
                .create(DownloadService.class)
                .download(url)
                .map(new Function<ResponseBody, Bitmap>() {
                    @Override
                    public Bitmap apply(ResponseBody responseBody) {
                        InputStream stream = responseBody.byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(stream);
                        IOHelper.close(stream);
                        return bitmap;
                    }
                }));
    }
}
