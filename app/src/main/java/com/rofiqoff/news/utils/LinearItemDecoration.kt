package com.rofiqoff.news.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LinearItemDecoration(
    private val space: Int?,
    private var orientation: Int = LinearLayoutManager.VERTICAL,
    private val spaceFirstItem: Int? = 0,
    private val spaceMiddle: Int? = 0,
    private val spaceEndItem: Int? = 0,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val itemPosition = parent.getChildLayoutPosition(view)
        val itemCount = state.itemCount

        if (orientation == LinearLayoutManager.HORIZONTAL) {
            with(outRect) {
                when {
                    itemCount == 1 -> {
                        left = space.orNull()
                        right = space.orNull()
                    }
                    /** First Item */
                    itemPosition == 0 -> {
                        left = space.orNull()
                        right = spaceMiddle.orNull()
                    }
                    /** Last Item */
                    itemCount > 0 && itemPosition == itemCount - 1 -> {
                        right = if (spaceEndItem == 0) space.orNull() else spaceEndItem.orNull()
                        left = spaceMiddle.orNull()
                    }
                    itemCount > 0 && itemPosition == itemCount - 2 -> {
                        right = 0
                    }
                    else -> {
                        right = spaceMiddle.orNull()
                    }

                }

                top = space.orNull()
                bottom = space.orNull()
            }
        } else {
            with(outRect) {
                when {
                    /** First Item */
                    itemPosition == 0 -> {
                        top = if (spaceFirstItem == 0) space.orNull() else spaceFirstItem.orNull()
                    }
                    /** Last Item */
                    itemCount > 0 && itemPosition == itemCount - 1 -> {
                        top = space.orNull()
                        bottom = if (spaceEndItem == 0) space.orNull() else spaceEndItem.orNull()
                    }
                    else -> {
                        top = space.orNull()
                    }
                }
            }
        }
    }

}
