package com.example.jetpackcomposepodedexapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.jetpackcomposepodedexapp.R
import com.example.jetpackcomposepodedexapp.Screens
import com.example.jetpackcomposepodedexapp.dataclass.PokemonListEntry
import com.example.jetpackcomposepodedexapp.viewmodels.list.PokemonListViewModel

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

            Spacer(modifier = Modifier.height(16.dp))

            PokemonList(navController = navController)

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

@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    //first get reference to states defined in the viewmodel
    val pokemonList by remember {
        viewModel.pokemonList
    }

    val endReached by remember {
        viewModel.endReached
    }

    val isLoading by remember {
        viewModel.isLoading
    }

    val loadError by remember {
        viewModel.loadError
    }

    LazyColumn(contentPadding = PaddingValues(16.dp)){
        val itemCount = if (pokemonList.size % 2 == 0) {
            pokemonList.size / 2
        }
        else{
            pokemonList.size / 2 + 1
        }

        items(itemCount){
            //detect when to paginate
            //if this is true, we know we have scrolled to the bottom, also check if end is not reached
            if (it >= itemCount - 1 && !endReached) {
                viewModel.paginationLoadingHandler()

            }
            PokemonInRow(rowIndex = it, entries = pokemonList, navController = navController )
        }
    }
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Center
    ) {
        if (isLoading){
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if (loadError.isNotEmpty()){
            Retry(error = loadError) {
                viewModel.paginationLoadingHandler()
            }
        }


    }
}






        //single pokemon
@ExperimentalCoilApi
@Composable
fun SinglePokemon(
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
            val painter = rememberImagePainter(
                data = pokemonListEntry.pokemonImageUrl,
                builder = {
                    error(R.drawable.error_image)
                    //place holder can go in here
                }
            )

            (painter.state as? ImagePainter.State.Success)
                ?.let { successState ->
                    LaunchedEffect(Unit) {
                        val drawable = successState.result.drawable
                        viewModel.calculateDominantColor(drawable) { color ->
                            dominantColor = color
                        }
                    }
                }

            val painterState = painter.state
            Image(
                painter = painter,
                contentDescription = pokemonListEntry.pokemonName,
                modifier = Modifier
                    .size(128.dp)
                    .align(CenterHorizontally),
            )
            if (painterState is ImagePainter.State.Loading){
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

@ExperimentalCoilApi
@Composable
fun PokemonInRow(
    rowIndex : Int,
    entries: List<PokemonListEntry>,
    navController: NavController
){
    Column {
        //inside this column i want rows because we want to display 2 Pokemon's
        Row {
            SinglePokemon(
                pokemonListEntry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))

            if (entries.size >= rowIndex * 2 + 2) {
                SinglePokemon(
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

//in case of error, retry loading the pokemon
@Composable
fun Retry(
    error : String,
    onRetry : () -> Unit
){
    Column {
        Text(text = error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onRetry() }, modifier = Modifier.align(CenterHorizontally)) {
            Text(text = "Retry")
            
        }
    }
}