package com.example.jetpackcomposepodedexapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetpackcomposepodedexapp.screens.PokemonListScreen

@Composable
fun Navigation() {
    //You can create a NavController by using the rememberNavController() method in your composable:
    val navController = rememberNavController()

    //create a navHost
    NavHost(navController = navController, startDestination = Screens.POKEMON_LIST_SCREEN.route){
        composable(Screens.POKEMON_LIST_SCREEN.route){
            PokemonListScreen(navController = navController)

        }
        composable(
            Screens.POKEMON_DETAILS_SCREEN.route + "/{dominantColor}" + "/{pokemonName}" ,
            //Note we passing to items as arguments 1. Dominant color, 2. Pokemon name
            arguments = listOf(
                navArgument("dominantColor"){
                    //what we pass here is color code, hence type int
                    type = NavType.IntType
                },
                navArgument("pokemonName"){
                    type = NavType.StringType
                }
            )
        ){
            //access passed arguments
            val dominantColor = remember {
                //remember what we passed is color code and its an int, so assign a variable to it
                val color = it.arguments?.getInt("dominantColor")
                //convert the color hex code to compose color
                //logic here is "If color !=null color?.let, use the color{Color(it)}, else use white ?: White"
                color?.let { Color(it) } ?: Color.White
            }

            //access pokemon name
            val pokemonName = remember {
                it.arguments?.getString("pokemonName")
            }
        }

    }

}