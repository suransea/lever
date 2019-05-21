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

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.view.PixelCopy;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import top.srsea.lever.rx.SchedulerTransformers;
import top.srsea.torque.common.IOUtils;

public class Bitmaps {

    public static Bitmap from(@NonNull View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static Bitmap from(@NonNull Activity activity) {
        Window window = activity.getWindow();
        View view = window.getDecorView();
        return from(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Bitmap from(@NonNull Activity activity, final Runnable callback, Handler callbackHandler) {
        Window window = activity.getWindow();
        View view = window.getDecorView();
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        PixelCopy.request(window, bitmap, new PixelCopy.OnPixelCopyFinishedListener() {
            @Override
            public void onPixelCopyFinished(int copyResult) {
                if (callback == null) return;
                callback.run();
            }
        }, callbackHandler);
        return bitmap;
    }

    public static void save(@NonNull final Bitmap source, @NonNull final File target,
                            @Nullable final Bitmap.CompressFormat format, @Nullable final SaveObserver observer) {
        Observable.just(source)
                .compose(SchedulerTransformers.<Bitmap>android())
                .doOnNext(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        File path = target.getParentFile();
                        if (!path.exists()) {
                            if (!path.mkdirs()) throw new IOException();
                        }
                        OutputStream stream = new FileOutputStream(target);
                        bitmap.compress(format == null ? Bitmap.CompressFormat.PNG : format, 100, stream);
                        stream.flush();
                        IOUtils.close(stream);
                    }
                })
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (observer == null) return;
                        observer.onError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        if (observer == null) return;
                        observer.onComplete();
                    }
                });
    }
}
