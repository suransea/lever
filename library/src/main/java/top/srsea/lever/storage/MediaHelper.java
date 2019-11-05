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

package top.srsea.lever.storage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import androidx.annotation.Nullable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import top.srsea.lever.Lever;
import top.srsea.torque.common.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

public class MediaHelper {

    /**
     * 获取MimeType
     * 对于Content URI使用{@link android.content.ContentResolver#getType(Uri)}获取
     * 其他URI使用path中的扩展名获取
     * 不使用{@link android.media.MediaMetadataRetriever}
     *
     * @param uri 目标Uri
     * @return mimeType, or empty
     */
    @Nullable
    public static String obtainMimeType(Uri uri) {
        if ("content".equals(uri.getScheme())) {
            return Lever.getContext().getContentResolver().getType(uri);
        }
        return obtainMimeType(uri.getPath());
    }

    /**
     * 从path中获取MimeType
     *
     * @param path A uri or file path
     * @return mimeType, null when no extension name or cannot find a match
     */
    @Nullable
    public static String obtainMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    public static Single<Uri> insertToMediaStore(final Bitmap bitmap, final String name, final Bitmap.CompressFormat format, final int quality) {
        return Single.fromCallable(new Callable<Uri>() {
            @Override
            public Uri call() throws Exception {
                Uri collection = StorageHelper.isExternalMounted() ?
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI :
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI;
                String extension = format.name().toLowerCase();
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                Uri uri = newMediaUri(collection, name, mimeType);
                ContentResolver resolver = Lever.getContext().getContentResolver();
                OutputStream outputStream = resolver.openOutputStream(uri);
                if (outputStream == null) throw new NullPointerException();
                bitmap.compress(format, quality, outputStream);
                outputStream.close();
                if (Build.VERSION.SDK_INT >= 29) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.MediaColumns.IS_PENDING, 0);
                    resolver.update(uri, values, null, null);
                }
                return uri;
            }
        });
    }

    public static Single<Uri> insertToMediaStore(final File file) {
        return Single.just(file)
                .flatMap(new Function<File, SingleSource<Uri>>() {
                    @Override
                    public SingleSource<Uri> apply(File file) throws Exception {
                        final InputStream stream = new FileInputStream(file);
                        String extension = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
                        final String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                        return insertToMediaStore(stream, file.getName(), mimeType)
                                .doOnSuccess(new Consumer<Uri>() {
                                    @Override
                                    public void accept(Uri uri) {
                                        IOUtils.close(stream);
                                    }
                                });
                    }
                });
    }

    public static Single<Uri> insertToMediaStore(InputStream stream, String name, String mimeType) {
        boolean externalMounted = StorageHelper.isExternalMounted();
        Uri collection;
        if (TextUtils.isEmpty(mimeType)) {
            return Single.error(new RuntimeException("mimeType is empty."));
        } else if (mimeType.startsWith("audio")) {
            collection = externalMounted ? MediaStore.Audio.Media.EXTERNAL_CONTENT_URI :
                    MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        } else if (mimeType.startsWith("video")) {
            collection = externalMounted ? MediaStore.Video.Media.EXTERNAL_CONTENT_URI :
                    MediaStore.Video.Media.INTERNAL_CONTENT_URI;
        } else if (mimeType.startsWith("image")) {
            collection = externalMounted ? MediaStore.Images.Media.EXTERNAL_CONTENT_URI :
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        } else if (Build.VERSION.SDK_INT >= 29) {
            collection = externalMounted ? MediaStore.Downloads.EXTERNAL_CONTENT_URI :
                    MediaStore.Downloads.INTERNAL_CONTENT_URI;
        } else {
            collection = externalMounted ? MediaStore.Files.getContentUri("external") :
                    MediaStore.Files.getContentUri("internal");
        }
        return insertToMediaStore(stream, name, collection, mimeType);
    }

    public static Single<Uri> insertToMediaStore(final InputStream stream, final String name, final Uri collection, final String mimeType) {
        return Single.fromCallable(new Callable<Uri>() {
            @Override
            public Uri call() throws Exception {
                Uri uri = newMediaUri(collection, name, mimeType);
                ContentResolver resolver = Lever.getContext().getContentResolver();
                OutputStream outputStream = resolver.openOutputStream(uri);
                if (outputStream == null) throw new NullPointerException();
                IOUtils.transfer(stream, outputStream);
                IOUtils.close(outputStream);
                if (Build.VERSION.SDK_INT >= 29) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.MediaColumns.IS_PENDING, 0);
                    resolver.update(uri, values, null, null);
                }
                return uri;
            }
        });
    }

    public static Uri newMediaUri(Uri collection, String displayName, String mimeType) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        if (Build.VERSION.SDK_INT >= 29) {
            values.put(MediaStore.MediaColumns.IS_PENDING, 1);
        }
        return Lever.getContext().getContentResolver().insert(collection, values);
    }
}
