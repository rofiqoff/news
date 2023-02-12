package com.rofiqoff.news.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

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

fun Context?.getDimensInt(id: Int): Int? = this?.resources?.getDimension(id)?.toInt()

fun getWidthOfScreen(): Int = Resources.getSystem().displayMetrics.widthPixels

fun Int?.orNull(defaultNull: Int = 0): Int = this ?: defaultNull

fun String?.toDateFormat(
    context: Context,
    inputFormat: String = "yyyy-MM-dd'T'HH:mm:ss",
    outputFormat: String = "dd MMM yyyy",
    locale: Locale = getCurrentLocale(context),
): String {
    return try {
        SimpleDateFormat(inputFormat, locale).parse(this.orEmpty())?.let { date ->
            SimpleDateFormat(outputFormat, locale).format(date)
        }.orEmpty()
    } catch (ex: ParseException) {
        ex.printStackTrace()
        ""
    }
}

private fun getCurrentLocale(context: Context?): Locale {
    context?.let {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else {
            context.resources.configuration.locale
        }
    }
    return Locale.getDefault()
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun <T> RecyclerView.Adapter<*>.autoNotify(
    old: List<T>,
    new: List<T>,
    compare: (T, T, Int) -> Boolean
) {
    val diffCallback = object : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return old.size
        }

        override fun getNewListSize(): Int {
            return new.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = old[oldItemPosition]
            val newItem = new[newItemPosition]
            return compare.invoke(oldItem, newItem, oldItemPosition)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition] == new[newItemPosition]
        }
    }

    val diff = DiffUtil.calculateDiff(diffCallback)
    diff.dispatchUpdatesTo(this)
}
