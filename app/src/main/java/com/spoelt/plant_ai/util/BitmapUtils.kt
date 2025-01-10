package com.spoelt.plant_ai.util

import android.graphics.Bitmap
import android.graphics.Matrix

fun Bitmap.rotateBitmap(degrees: Float): Bitmap {
    val matrix = Matrix().apply {
        postRotate(degrees)
    }
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}