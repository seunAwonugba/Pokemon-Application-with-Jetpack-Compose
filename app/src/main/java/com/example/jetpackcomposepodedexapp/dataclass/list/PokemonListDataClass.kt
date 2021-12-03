package com.example.jetpackcomposepodedexapp.dataclass.list

data class PokemonListDataClass(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)