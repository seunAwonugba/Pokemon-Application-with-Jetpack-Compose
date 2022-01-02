package com.example.jetpackcomposepodedexapp.dataclass

//i created this data class to hold the data to be displayed on the screen, i know list data class
//is sufficient but i really dont understand why lackner created it
data class PokemonListEntry (
    val pokemonName : String,
    val pokemonImageUrl : String,
    val pokemonNumber : Int
        )