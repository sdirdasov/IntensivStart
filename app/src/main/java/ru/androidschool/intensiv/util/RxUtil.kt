package ru.androidschool.intensiv.util

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.CompletableTransformer
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.SingleTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun <T> applyObservableAsync(): SingleTransformer<T, T> {
    return SingleTransformer { observable ->
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> applyObservableEditText(timeout: Long): ObservableTransformer<T, T> {
    return ObservableTransformer { observable ->
        observable
            .debounce(timeout, TimeUnit.MILLISECONDS, Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

fun applyCompletableAsync(): CompletableTransformer {
    return CompletableTransformer { observable ->
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
