package top.srsea.lever

import android.support.annotation.StringRes
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonElement
import io.reactivex.disposables.Disposable
import top.srsea.lever.common.DimensionUnit
import top.srsea.lever.common.Res
import top.srsea.lever.rx.DisposeBag
import top.srsea.lever.ui.Toasts
import top.srsea.torque.value.Property
import java.io.Reader
import kotlin.reflect.KProperty

class ToastOption {
    var mainThread = false
    var duration = Toast.LENGTH_SHORT
    var margin = -1f to -1f
    var gravity = -1
    var offset = -1 to -1
}

fun toast(content: String, option: ToastOption.() -> Unit = {}) {
    val opt = ToastOption()
    opt.option()
    val builder = Toasts.of(content)
            .gravity(opt.gravity, opt.offset.first, opt.offset.second)
            .margin(opt.margin.first, opt.margin.second)
    if (opt.mainThread) {
        builder.mainThread()
    }
    when (opt.duration) {
        Toast.LENGTH_SHORT -> builder.showShort()
        Toast.LENGTH_LONG -> builder.showLong()
    }
}

fun toast(@StringRes id: Int, option: ToastOption.() -> Unit = {}) {
    toast(Res.string(id), option)
}

fun Disposable.addTo(bag: DisposeBag) {
    bag.add(this)
}

fun Disposable.ignoreDisposable() {}

val Float.dp get() = DimensionUnit.DP.toPx(this).toInt()

val Float.sp get() = DimensionUnit.SP.toPx(this).toInt()

val Int.dp get() = toFloat().dp

val Int.sp get() = toFloat().sp

inline fun <reified T> Gson.fromJson(json: String): T = fromJson(json, T::class.java)

inline fun <reified T> Gson.fromJson(json: JsonElement): T = fromJson(json, T::class.java)

inline fun <reified T> Gson.fromJson(json: Reader): T = fromJson(json, T::class.java)

operator fun <T> Property<T>.getValue(thisRef: Any?, property: KProperty<*>): T = get()
