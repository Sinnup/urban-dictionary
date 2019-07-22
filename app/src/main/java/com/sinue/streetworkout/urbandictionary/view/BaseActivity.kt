package com.sinue.streetworkout.urbandictionary.view

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import android.R
import android.view.View
import android.widget.LinearLayout
import android.view.ViewGroup



open class BaseActivity: AppCompatActivity() {

    private var mProgressDialog: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    open fun showProgressDialog(idContainer: Int) {

        // Create progressBar dynamically...
        mProgressDialog = ProgressBar(this)
        mProgressDialog!!.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val viewGroup = findViewById<ViewGroup>(idContainer)
        // Add ProgressBar to LinearLayout
        if (viewGroup != null) {
            viewGroup.addView(mProgressDialog)
        }

    }

    open fun dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog!!.visibility = View.GONE
        }

    }
}