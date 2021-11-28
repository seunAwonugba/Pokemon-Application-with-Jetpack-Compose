package com.example.jetpackcomposepodedexapp

sealed class Screens(val route : String){
    object POKEMON_LIST_SCREEN : Screens("pokemon_list_screen")
    object POKEMON_DETAILS_SCREEN : Screens("pokemon_details_screen")

}
