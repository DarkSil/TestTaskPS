package com.example.testtaskps.utils

import com.example.testtaskps.main.MainViewModel
import com.example.testtaskps.main.transfer.TransferDialog

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

    fun prepareTransferDialog(
        account: String,
        callback: MainViewModel.TransferCallback
    ) : TransferDialog {
        return TransferDialog()
            .setCurrentAccount(account)
            .setCallback(callback)
    }

}