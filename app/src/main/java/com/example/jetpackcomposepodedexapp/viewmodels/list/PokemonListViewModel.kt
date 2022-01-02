package com.example.jetpackcomposepodedexapp.viewmodels.list

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.palette.graphics.Palette
import com.example.jetpackcomposepodedexapp.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    repository: TestRepository
) : ViewModel() {

    //calculate dominant color
    //because of the async operation, we need a drawback hence onFinished to provide the color when
    //the async task is done
    fun calculateDominantColor(drawable: Drawable, onFinish : (Color) -> Unit ){
        //convert image to bitmap, to use color pallet u must first convert the image to bitmap
        val bmp = (drawable as BitmapDrawable)
            .bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let {
                onFinish(Color(it))

            }
        }

    }
}