package com.example.jetpackcomposepodedexapp.viewmodels.list

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.jetpackcomposepodedexapp.constant.Constants.PAGE_SIZE
import com.example.jetpackcomposepodedexapp.dataclass.PokemonListEntry
import com.example.jetpackcomposepodedexapp.repository.TestRepository
import com.example.jetpackcomposepodedexapp.utils.ApiCallErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private var repository: TestRepository
) : ViewModel() {

    //calculate dominant color
    //because of the async operation, we need a drawback hence onFinished to provide the color when
    //the async task is done
    fun calculateDominantColor(drawable: Drawable, onFinish : (Color) -> Unit ){
        //convert image to bitmap, to use color pallet u must first convert the image to bitmap
        val bmp = (drawable as BitmapDrawable)
            .bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let {
                onFinish(Color(it))

            }
        }

    }

    //implement pagination
    private var currentPage = 0
    //using compose state fetch data from api

    //list of pokemons
    var pokemonList = mutableStateOf<List<PokemonListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    //implement searching
    //before we start the search save pokemonList inside cachedPokemonList, so that after the
    //search we can then collect our list back from the cachedPokemonList
    private var cachedPokemonList = listOf<PokemonListEntry>()
    //used to inform us when searching wants to start so we can save pokemonList inside cachedPokemonList
    private var isSearchStarting = true
    //this is true as long as search results are being displayed
    var isSearching = mutableStateOf(false)

    init {
        paginationLoadingHandler()
    }

    fun searchPokemonList(searchQuery : String){
        //first thing first, lets determine the list of pokemon we want to search in
        //originally before search occurs, we search in pokemonList, the moment searching occurs
        //we transfer our list to the cachedPokemonList then boom, we want to start searching in the
        //cachedPokemonList
        val listToSearch = if(isSearchStarting){
            pokemonList.value
        }else{
            cachedPokemonList
        }

        //Dispatchers.Default is good for cpu actions just like IO is goo for input and output actions
        viewModelScope.launch(Dispatchers.Default) {
            //now lets say i start searching, then i clear search field, what happens
            //simply collect your list back from the cachedPokemonList
            if (searchQuery.isEmpty()){
                pokemonList.value = cachedPokemonList
                //i'm not searching again
                isSearching.value = false
                //i can still start a search again, but presently i no day search
                isSearchStarting = true
                return@launch
            }
            //now lets say searchQuery is not now empty i.e texts inside search view
            val searchResults = listToSearch.filter {
                it.pokemonName.contains(searchQuery.trim(), ignoreCase = true) ||
                        it.pokemonNumber.toString() == searchQuery.trim()
            }
            //the very moment i begin to search, cache the pokemon list
            if (isSearchStarting){
                cachedPokemonList = pokemonList.value
                isSearchStarting = false
            }
            //finally we set our pokemon list to our search value
            pokemonList.value = searchResults
            isSearching.value = true
        }
    }

    //loads next set of data while making a network call
    fun paginationLoadingHandler(){
        viewModelScope.launch {
            // before setting result emit loading state
            isLoading.value = true
            //load pokemon, how many do u want to load, PAGE_SIZE, from where do u want to load from, that the offset
            var result = repository.pokemonList(PAGE_SIZE, currentPage * PAGE_SIZE)

            when(result){
                is ApiCallErrorHandler.Success -> {
                    endReached.value = currentPage * PAGE_SIZE >= result.data!!.count
                    //get each pokemon
                    val pokemonEntry = result.data!!.results.mapIndexed { index, entry ->
                        //get pokemon number from the URL
                        val number = if (entry.url.endsWith("/")){
                            entry.url.dropLast(1).takeLastWhile {
                                it.isDigit()
                            }
                        }
                        //just in case URL does not end with a slash
                        else{
                            entry.url.takeLastWhile {
                                it.isDigit()
                            }
                        }

                        //right now we can access image of the pokemon because originally
                        //from the list of pokemons i can access the sprites, so now i get its number
                        //pass it to data fetched from details then access the image

                        val myCalculatedImageUrl =
                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${number}.png"
                        //construct the data passed to the PokemonListEntry i created
                        PokemonListEntry(
                            entry.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                            myCalculatedImageUrl,
                            number.toInt()
                        )
                    }

                    currentPage++
                    //since its success no load error
                    loadError.value = ""
                    isLoading.value = false
                    pokemonList.value += pokemonEntry

                }
                is ApiCallErrorHandler.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false

                }
            }
        }

    }
}