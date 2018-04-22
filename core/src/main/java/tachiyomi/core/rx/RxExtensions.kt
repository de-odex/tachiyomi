package tachiyomi.core.rx

import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableContainer

/**
 * Adds this disposable to a [disposables] container.
 */
fun Disposable.addTo(disposables: DisposableContainer) {
  disposables.add(this)
}

/**
 * Returns a flowable that emits the current emission paired with the previous one.
 */
@Suppress("UNCHECKED_CAST")
fun <T> Flowable<T>.scanWithPrevious(): Flowable<Pair<T, T?>> {
  return scan(Pair<T?, T?>(null, null), { prev, newValue -> Pair(newValue, prev.first) })
    .skip(1) as Flowable<Pair<T, T?>>
}

/**
 * Returns a flowable that skips the null values of the result of the given [block] function.
 */
inline fun <T, R> Flowable<T>.mapNullable(crossinline block: (T) -> R?): Flowable<R> {
  return flatMap { block(it)?.let { Flowable.just(it) } ?: Flowable.empty() }
}
