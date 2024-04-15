package com.example.testtaskps.utils

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.example.testtaskps.databinding.DialogDefaultErrorBinding

class DefaultErrorDialog : DialogFragment() {

    enum class DialogType {
        OK,
        REFRESH
    }

    fun interface RefreshCallback {
        fun refresh()
    }

    private val binding by lazy { DialogDefaultErrorBinding.inflate(layoutInflater) }
    private var callback: RefreshCallback? = null
    private var type: DialogType = DialogType.OK
    private var title: String? = null
    private var description: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            isCancelable = false
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title?.let {
            binding.textTitle.text = it
        }
        description?.let {
            binding.textDescription.text = it
        }
        when (type) {
            DialogType.REFRESH -> binding.imageRefresh.isVisible = true
            else -> binding.textOK.isVisible = true
        }
        binding.textOK.setOnClickListener {
            dismiss()
        }
        binding.imageRefresh.setOnClickListener {
            callback?.refresh()
            dismiss()
        }
    }

    fun setCallback(refreshCallback: RefreshCallback?) : DefaultErrorDialog {
        callback = refreshCallback
        return this
    }

    fun setType(type: DialogType): DefaultErrorDialog {
        this.type = type
        return this
    }

    fun setTitle(title: String?): DefaultErrorDialog {
        this.title = title
        return this
    }

    fun setDescription(description: String?): DefaultErrorDialog {
        this.description = description
        return this
    }

    fun recreate() : DefaultErrorDialog {
        return DefaultErrorDialog()
            .setCallback(callback)
            .setType(type)
            .setTitle(title)
            .setDescription(description)
    }
}