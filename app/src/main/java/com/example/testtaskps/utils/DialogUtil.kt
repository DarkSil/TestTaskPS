package com.example.testtaskps.utils

object DialogUtil {

    fun prepareDialog(
        type: DefaultErrorDialog.DialogType,
        callback: DefaultErrorDialog.RefreshCallback? = null,
        title: String? = null,
        description: String? = null
    ) : DefaultErrorDialog {
        return DefaultErrorDialog()
            .setType(type)
            .setCallback(callback)
            .setTitle(title)
            .setDescription(description)
    }

}