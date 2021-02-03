/*
 * Copyright 2018 stfalcon.com
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

package com.mrkaz.imageviewer.common.pager

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager

internal class MultiTouchRecycleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    internal var isIdle = true
        private set

    private var isInterceptionDisallowed: Boolean = false
    private var pageChangeListener: SnapOnScrollListener? = null

    public fun setPageChangeListener(listener: SnapOnScrollListener){
        this.pageChangeListener = listener
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        pageChangeListener?.let { addOnScrollListener(it) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        pageChangeListener?.let { removeOnScrollListener(it) }
    }

    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        isInterceptionDisallowed = disallowIntercept
        super.requestDisallowInterceptTouchEvent(disallowIntercept)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return if (ev.pointerCount > 1 && isInterceptionDisallowed) {
            requestDisallowInterceptTouchEvent(false)
            val handled = super.dispatchTouchEvent(ev)
            requestDisallowInterceptTouchEvent(true)
            handled
        } else {
            super.dispatchTouchEvent(ev)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (ev.pointerCount > 1) {
            false
        } else {
            try {
                super.onInterceptTouchEvent(ev)
            } catch (ex: IllegalArgumentException) {
                false
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return try {
            super.onTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            false
        }
    }

    private fun onPageScrollStateChanged(state: Int) {
        isIdle = state == ViewPager.SCROLL_STATE_IDLE
    }
}