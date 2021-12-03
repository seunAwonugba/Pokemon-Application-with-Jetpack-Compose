package com.example.jetpackcomposepodedexapp.repository

import com.example.jetpackcomposepodedexapp.dataclass.details.PokemonDetailsDataClass
import com.example.jetpackcomposepodedexapp.dataclass.list.PokemonListDataClass
import javax.inject.Inject

class MainRepository @Inject constructor() : TestRepository {
    override suspend fun pokemonList(limit: Int, offset: Int): PokemonListDataClass {
        TODO("Not yet implemented")
    }

    override suspend fun pokemonDetails(name: String): PokemonDetailsDataClass {
        TODO("Not yet implemented")
    }
}