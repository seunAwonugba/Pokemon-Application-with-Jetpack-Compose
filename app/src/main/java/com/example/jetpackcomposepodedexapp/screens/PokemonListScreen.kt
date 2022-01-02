package com.example.jetpackcomposepodedexapp.screens

import android.view.RoundedCorner
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.jetpackcomposepodedexapp.Screens
import com.example.jetpackcomposepodedexapp.dataclass.PokemonListEntry
import com.example.jetpackcomposepodedexapp.viewmodels.list.PokemonListViewModel
import com.google.accompanist.coil.CoilImage

@Composable
fun PokemonListScreen(
    navController: NavController
){
    //surface serves as root element of a screen, just like body tag
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ){
        Column(modifier = Modifier.padding(4.dp,4.dp,4.dp,0.dp)) {
            //image
            Image(
                painter = rememberImagePainter("https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/International_Pok%C3%A9mon_logo.svg/1920px-International_Pok%C3%A9mon_logo.svg.png"),
                contentDescription = "Pokemon logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.2f)
                    .align(CenterHorizontally),
            )
            //searchbar
            SearchBar()

        }
    }
}

@Composable
fun SearchBar(
    onSearch : (String) -> Unit = {}
){
    var text by remember {
        mutableStateOf("")
    }
    
    Box(modifier = Modifier.fillMaxWidth()){
        OutlinedTextField(value = text, onValueChange ={
            text = it
            //trigger your onSearch function with the new string
            onSearch(it)
        },
            placeholder = {
                Text(text = "Search pokemon here...")
            },
            label = {
                Text(text = "Search pokemon here...")
            },
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(),
            shape = CircleShape,
            trailingIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icons.Filled.Search
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            )
        )
    }
}


//single pokemon
@Composable
fun PokemonInList(
    pokemonListEntry : PokemonListEntry,
    //want to click on this entry and navigate to the details screen
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel : PokemonListViewModel = hiltViewModel()

){
    //create a default dominant color if the view model dominant color has not been pocessed
    val defaultDominantColor = MaterialTheme.colors.surface

    //dominant color will be a state
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }
    
    Box(
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Brush.verticalGradient(listOf(dominantColor, defaultDominantColor)))
            .clickable {
                navController.navigate(
                    Screens.PokemonDetailsScreen.route + "/${dominantColor.toArgb()}" + "/${pokemonListEntry.pokemonName}"
                )
            },
        contentAlignment = Center
    ){
        Column {
            CoilImage(
                data = ImageRequest.Builder(LocalContext.current)
                    .data(pokemonListEntry.pokemonImageUrl)
                    .target{
                        viewModel.calculateDominantColor(it){ calculatedDominantColor ->
                            dominantColor = calculatedDominantColor

                        }
                    }
                    .build(),
                contentDescription = pokemonListEntry.pokemonName,
                fadeIn = true,
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally)
            ) {
                //here is used to display a composable while its loading
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.scale(.5f)
                )
            }
            //below image, display image name
            Text(
                text = pokemonListEntry.pokemonName,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

        }
    }
}

@Composable
fun PokemonInRow(
    rowIndex : Int,
    entries: List<PokemonListEntry>,
    navController: NavController
){
    Column {
        //inside this column i want rows because we want to display 2 Pokemon's
        Row {
            PokemonInList(
                pokemonListEntry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))

            if (entries.size >= rowIndex * 2 + 2) {
                PokemonInList(
                    pokemonListEntry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            }
            else {
                Spacer(modifier = Modifier.weight(1f))
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}