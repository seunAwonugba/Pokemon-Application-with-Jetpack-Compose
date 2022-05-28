package com.example.jetpackcomposepodedexapp.viewmodels.details

import androidx.lifecycle.ViewModel
import com.example.jetpackcomposepodedexapp.dataclass.details.PokemonDetailsDataClass
import com.example.jetpackcomposepodedexapp.repository.TestRepository
import com.example.jetpackcomposepodedexapp.utils.ApiCallErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private var repository : TestRepository
) :ViewModel() {

   suspend fun fetchPokemonDetails(pokemonName : String) : ApiCallErrorHandler<PokemonDetailsDataClass>{
       return repository.getPokemonDetails(pokemonName)
   }

}