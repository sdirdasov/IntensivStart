package ru.androidschool.intensiv.extensions

import android.widget.ImageView
import com.squareup.picasso.Picasso

fun ImageView.load(path: String?) {
    Picasso.get()
        .load(path)
        .into(this)
}
