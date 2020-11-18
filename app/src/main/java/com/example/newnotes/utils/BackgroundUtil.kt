package com.example.newnotes.utils

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import com.example.newnotes.R

class BackgroundUtil {
    companion object {
        fun getBackgroundDrawable(view: View): GradientDrawable {
            val background = view.background.mutate() as LayerDrawable
            val bgDrawable = background
                .findDrawableByLayerId(R.id.gradient_stroke)
                .mutate() as GradientDrawable

            return bgDrawable
        }
    }
}