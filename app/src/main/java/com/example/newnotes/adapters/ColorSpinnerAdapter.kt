package com.example.newnotes.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.example.newnotes.ColorRow
import com.example.newnotes.R

class ColorSpinnerAdapter(context: Context, colors: List<ColorRow>) :
    ArrayAdapter<ColorRow>(context, 0, colors) {

    var current: ColorRow? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)!!
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)!!
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var mConvertView = convertView
        if (mConvertView == null) {
            mConvertView = LayoutInflater.from(context)
                .inflate(R.layout.color_row, parent, false)
        }

        val circle = mConvertView?.findViewById<ImageView>(R.id.color_circle)
        val currentColorRow = getItem(position)

        if (currentColorRow != null) {
            val color = circle?.drawable?.mutate() as GradientDrawable
            color.setColor(Color.parseColor(currentColorRow.color))
        }

        return mConvertView
    }

}
