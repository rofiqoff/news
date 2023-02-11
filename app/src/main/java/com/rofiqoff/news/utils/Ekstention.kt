package com.rofiqoff.news.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

inline fun <T, reified R> List<T>?.orNullListNot(transform: (T) -> R): List<R> {
    return if (this == null) listOf()
    else {
        val arrayListOf = arrayListOf<R>()
        this.forEach { arrayListOf.add(transform(it)) }
        return arrayListOf
    }
}

fun <T> MutableLiveData<T>.toLiveData(): LiveData<T> = this

fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)
}
