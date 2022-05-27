package com.example.jetpackcomposepodedexapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.jetpackcomposepodedexapp.R
import com.example.jetpackcomposepodedexapp.dataclass.details.PokemonDetailsDataClass
import com.example.jetpackcomposepodedexapp.utils.ApiCallErrorHandler
import com.example.jetpackcomposepodedexapp.viewmodels.details.PokemonDetailsViewModel

@Composable
fun PokemonDetailsScreen(
    dominantColor : Color,
    pokemonName : String,
    navController: NavController,
    viewModel: PokemonDetailsViewModel = hiltViewModel()
){
    val pokemonDetails = produceState<ApiCallErrorHandler<PokemonDetailsDataClass>>(
        initialValue = ApiCallErrorHandler.Loading()
    ){
        value = viewModel.fetchPokemonDetails(pokemonName)
    }.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(16.dp)
    ) {
//        Spacer(modifier = Modifier.height(20.dp))

        //box to display the image
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        ) {

        //display the image if and only if we fetched data correctly,
        // that's after loading has been emitted first on first login to this page,
        // we achieve this by taking advantage of produce state to handle recomposition

            if (pokemonDetails is ApiCallErrorHandler.Success){
                pokemonDetails.data?.sprites?.other?.official_artwork.let {
                    Image(
                        painter = rememberImagePainter(
                            data = it?.front_default,
                            builder = {
                                error(R.drawable.error_image)
                            }
                        ),
                        contentDescription = pokemonDetails.data?.name,
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DetailsScreenTopSection(){

}