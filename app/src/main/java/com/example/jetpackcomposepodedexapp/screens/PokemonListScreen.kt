package com.example.jetpackcomposepodedexapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter

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