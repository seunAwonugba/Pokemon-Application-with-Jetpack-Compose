package com.example.jetpackcomposepodedexapp.repository

import com.example.jetpackcomposepodedexapp.dataclass.details.PokemonDetailsDataClass
import com.example.jetpackcomposepodedexapp.dataclass.list.PokemonListDataClass

interface TestRepository {

    suspend fun pokemonList(limit : Int, offset : Int) : PokemonListDataClass

    suspend fun pokemonDetails(name : String) : PokemonDetailsDataClass
}