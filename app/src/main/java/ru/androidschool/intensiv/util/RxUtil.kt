package ru.androidschool.intensiv.util

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun <T> applyObservableAsync(): ObservableTransformer<T, T> {
    return ObservableTransformer { observable ->
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> applyObservableEditText(timeout: Long): ObservableTransformer<T, T> {
    return ObservableTransformer { observable ->
        observable
            .debounce(timeout, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
    }
}
