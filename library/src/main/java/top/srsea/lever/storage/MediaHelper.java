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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import top.srsea.lever.Lever;
import top.srsea.torque.common.IOHelper;
import top.srsea.torque.common.Preconditions;

/**
 * Utilities for Media.
 *
 * @author sea
 */
public class MediaHelper {
    private MediaHelper() {
    }

    /**
     * Return the MIME type for the given uri.
     * For content URIs, use {@link ContentResolver#getType(Uri)},
     * others obtain from paths.
     *
     * <p>Won't read metadata.
     *
     * @param uri URI to obtain MIME type
     * @return the MIME type string, or null
     */
    @Nullable
    public static String obtainMimeType(Uri uri) {
        if ("content".equals(uri.getScheme())) {
            return Lever.getContext().getContentResolver().getType(uri);
        }
        return obtainMimeType(uri.getPath());
    }

    /**
     * Return the MIME type for the given path.
     *
     * @param path a path from URI or file
     * @return the MIME type string, null when no extension name or cannot find a match
     */
    @Nullable
    public static String obtainMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    /**
     * Inserts bitmap to media store.
     *
     * @param bitmap  source bitmap
     * @param name    display name
     * @param format  compress format
     * @param quality compress quality
     * @return the observable content URI in the media store
     */
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
                Preconditions.requireNonNull(outputStream);
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

    /**
     * Inserts the media file to media store.
     * The file will be copied to the corresponding bucket.
     *
     * @param file the file to insert
     * @return an observable content URI in media store
     */
    public static Single<Uri> insertToMediaStore(final File file) {
        return Single.just(file)
                .flatMap(new Function<File, SingleSource<Uri>>() {
                    @Override
                    public SingleSource<Uri> apply(@NonNull File file) throws Exception {
                        final InputStream stream = new FileInputStream(file);
                        final String mimeType = obtainMimeType(file.getAbsolutePath());
                        return insertToMediaStore(stream, file.getName(), mimeType)
                                .doAfterTerminate(new Action() {
                                    @Override
                                    public void run() {
                                        IOHelper.close(stream);
                                    }
                                });
                    }
                });
    }

    /**
     * Inserts the data of stream to media store.
     *
     * @param stream   source
     * @param name     display name
     * @param mimeType the MIME type of data
     * @return an observable content URI in media store
     */
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

    /**
     * Inserts the data of stream to media store, using the specific collection.
     *
     * @param stream     source
     * @param name       display name
     * @param collection the specific collection
     * @param mimeType   the MIME type of data
     * @return an observable content URI in media store
     */
    public static Single<Uri> insertToMediaStore(final InputStream stream, final String name, final Uri collection, final String mimeType) {
        return Single.fromCallable(new Callable<Uri>() {
            @Override
            public Uri call() throws Exception {
                Uri uri = newMediaUri(collection, name, mimeType);
                ContentResolver resolver = Lever.getContext().getContentResolver();
                OutputStream outputStream = resolver.openOutputStream(uri);
                if (outputStream == null) throw new NullPointerException();
                IOHelper.transfer(stream, outputStream);
                IOHelper.close(outputStream);
                if (Build.VERSION.SDK_INT >= 29) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.MediaColumns.IS_PENDING, 0);
                    resolver.update(uri, values, null, null);
                }
                return uri;
            }
        });
    }

    /**
     * Inserts a record in media store and returns its URI.
     * If the platform API level is greater or equal to 29, the record will be marked as "pending",
     * you need clear it after data written, like:
     * <pre>
     * ContentValues values = new ContentValues();
     * values.put(MediaStore.MediaColumns.IS_PENDING, 0);
     * resolver.update(uri, values, null, null);
     * </pre>
     *
     * @param collection  the specific collection
     * @param displayName the display name required by media store
     * @param mimeType    the MIME type of this record
     * @return new content URI of this record
     * @see MediaStore.MediaColumns#IS_PENDING
     */
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
