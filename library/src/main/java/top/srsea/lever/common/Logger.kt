package top.srsea.lever.common

import android.util.Log

/**
 * Currying Log#println(int, String, String).
 */
fun logger(tag: String = Logger.GLOBAL_TAG): (priority: Int) -> (msg: String) -> Unit = { priority ->
    { msg ->
        Log.println(priority, tag, msg)
    }
}

/**
 * Create a new logger which the class name as tag.
 */
fun Any.logger(priority: Int) = logger(tag = this::class.java.simpleName)(priority)

fun Any.debugLogger() = logger(Log.DEBUG)

fun Any.errorLogger() = logger(Log.ERROR)

fun Any.warnLogger() = logger(Log.WARN)

fun Any.infoLogger() = logger(Log.INFO)

fun Any.verboseLogger() = logger(Log.VERBOSE)

fun Any.assertLogger() = logger(Log.ASSERT)

/**
 * Each access will create a new logger, just like Any#logger(Int).
 * It is recommended to use this extended attribute only when debugging.
 */
inline val Any.debug: (msg: String) -> Unit get() = debugLogger()

inline val Any.error: (msg: String) -> Unit get() = errorLogger()

inline val Any.warn: (msg: String) -> Unit get() = warnLogger()

inline val Any.info: (msg: String) -> Unit get() = infoLogger()

inline val Any.verbose: (msg: String) -> Unit get() = verboseLogger()

inline val Any.assert: (msg: String) -> Unit get() = assertLogger()
