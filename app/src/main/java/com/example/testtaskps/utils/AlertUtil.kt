package com.example.testtaskps.utils

import android.content.Context
import android.widget.Toast
import com.example.testtaskps.R

object AlertUtil {

    fun Context.showDefaultError() {
        Toast.makeText(
            this,
            this.getString(R.string.something_went_wrong),
            Toast.LENGTH_LONG
        ).show()
    }

}