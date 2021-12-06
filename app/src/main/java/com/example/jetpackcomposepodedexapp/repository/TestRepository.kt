package com.example.jetpackcomposepodedexapp.repository

import com.example.jetpackcomposepodedexapp.dataclass.details.PokemonDetailsDataClass
import com.example.jetpackcomposepodedexapp.dataclass.list.PokemonListDataClass
import com.example.jetpackcomposepodedexapp.utils.ApiCallErrorHandler

interface TestRepository {

    suspend fun pokemonList(limit : Int, offset : Int) : ApiCallErrorHandler<PokemonListDataClass>

    suspend fun pokemonDetails(name : String) : ApiCallErrorHandler<PokemonDetailsDataClass>
}