package com.sinue.streetworkout.urbandictionary.view

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.sinue.streetworkout.urbandictionary.R


open class BaseActivity: AppCompatActivity() {

    private var frameProgressBar: FrameLayout? = null

    open fun showProgressDialog(idContainer: Int) {

        frameProgressBar = layoutInflater.inflate(R.layout.progress_bar_template, null) as FrameLayout
        frameProgressBar!!.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val viewGroup = findViewById<ViewGroup>(idContainer)

        if (viewGroup != null) {
            viewGroup.addView(frameProgressBar)
        }

    }

    open fun dismissProgressDialog() {
        if (frameProgressBar != null && frameProgressBar!!.visibility == View.VISIBLE) {
            frameProgressBar!!.visibility = View.GONE
        }

    }
}