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
import android.view.View;
import android.view.Window;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import top.srsea.torque.common.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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

    public static Observable<File> save(@NonNull final Bitmap source, @NonNull final File target,
                                        @Nullable final Bitmap.CompressFormat format) {
        return Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(ObservableEmitter<File> emitter) throws Exception {
                File path = target.getParentFile();
                if (!path.exists()) {
                    if (!path.mkdirs()) throw new IOException("cannot mkdirs.");
                }
                OutputStream stream = new FileOutputStream(target);
                source.compress(format == null ? Bitmap.CompressFormat.PNG : format, 100, stream);
                stream.flush();
                IOUtils.close(stream);
                emitter.onNext(target);
                emitter.onComplete();
            }
        });
    }
}
