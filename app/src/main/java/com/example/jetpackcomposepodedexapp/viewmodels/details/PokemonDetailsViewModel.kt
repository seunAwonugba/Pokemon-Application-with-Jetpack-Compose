package com.example.jetpackcomposepodedexapp.viewmodels.details

import androidx.lifecycle.ViewModel
import com.example.jetpackcomposepodedexapp.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private var repository : TestRepository
) :ViewModel() {

}