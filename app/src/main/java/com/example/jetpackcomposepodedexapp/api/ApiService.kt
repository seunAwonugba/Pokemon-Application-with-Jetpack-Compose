package com.example.jetpackcomposepodedexapp.api

import com.example.jetpackcomposepodedexapp.constant.Constants.DETAILS_END_POINT
import com.example.jetpackcomposepodedexapp.constant.Constants.LIST_END_POINT
import com.example.jetpackcomposepodedexapp.dataclass.details.PokemonDetailsDataClass
import com.example.jetpackcomposepodedexapp.dataclass.list.PokemonListDataClass
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(LIST_END_POINT)
    suspend fun getPokemonList(
        @Query("limit") limit : String,
        @Query("offset") offset : String
    ) : PokemonListDataClass

    @GET(DETAILS_END_POINT)
    suspend fun getPokemonDetails(
        @Path("name") name : String
    ) : PokemonDetailsDataClass
}