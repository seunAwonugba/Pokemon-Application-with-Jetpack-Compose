package com.example.jetpackcomposepodedexapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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

        DetailsScreenTopSection(
            navController = navController,
            modifier = Modifier.fillMaxWidth()
        )
        
        PokemonDetailsStateWrapper(
            pokemonDetails = pokemonDetails,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 200.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            loadingStateModifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
        )

        //box to display the image
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        ) {

        //display the image if and only if we fetched data correctly,
        // that's after loading has been emitted first on first login to this page,
        // we achieve this by taking advantage of produce state to handle recomposition

            if (pokemonDetails is ApiCallErrorHandler.Success){
                val image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemonDetails.data?.id}.png"
                Image(
                    painter = rememberImagePainter(
                        data = image,
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

@Composable
fun DetailsScreenTopSection(
    navController: NavController,
    modifier: Modifier = Modifier
){
    Box{
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back arrow",
            tint = Color.White,
            modifier = Modifier
                .size(24.dp)
                .padding(16.dp)
                .clickable {
                    navController.navigateUp()
                }
        )
    }
}

//the white container where the details seat
@Composable
fun PokemonDetailsStateWrapper(
    pokemonDetails : ApiCallErrorHandler<PokemonDetailsDataClass>,
    modifier: Modifier = Modifier,
    loadingStateModifier : Modifier = Modifier
) {
    when(pokemonDetails){
        is ApiCallErrorHandler.Success -> {

        }
        is ApiCallErrorHandler.Error -> {
            Text(
                text = pokemonDetails.message.toString(),
                color = Color.Red
            )
        }
        is ApiCallErrorHandler.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = loadingStateModifier
            )
        }
    }
    
}