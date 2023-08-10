package com.planlegger.core


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import com.planlegger.databinding.DialogCustomBinding

class CustomProgressDialog(context: Context) : Dialog(context) {
    var binding: DialogCustomBinding

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (window != null)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding = DialogCustomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCancelable(false)

        if (window != null)
            window!!.setDimAmount(0.2f)

        if (window != null)
            window!!.setGravity(Gravity.CENTER)
    }
}