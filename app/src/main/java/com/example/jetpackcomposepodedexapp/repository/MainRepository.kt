package com.example.jetpackcomposepodedexapp.repository

import com.example.jetpackcomposepodedexapp.api.ApiService
import com.example.jetpackcomposepodedexapp.dataclass.details.PokemonDetailsDataClass
import com.example.jetpackcomposepodedexapp.dataclass.list.PokemonListDataClass
import com.example.jetpackcomposepodedexapp.utils.ApiCallErrorHandler
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val api : ApiService
) : TestRepository {
    override suspend fun pokemonList(
        limit: Int,
        offset: Int
    ): ApiCallErrorHandler<PokemonListDataClass> {
        return try {
            val apiResponse = api.getPokemonList(limit, offset)
            val apiResult = apiResponse.body()

            if (apiResponse.isSuccessful && apiResult != null) {
                ApiCallErrorHandler.Success(apiResult)
            }
            else if (apiResponse.code() == 400){
                ApiCallErrorHandler.Error("Bad Request")
            }
            else if (apiResponse.code() == 401){
                ApiCallErrorHandler.Error("Unauthorized")
            }
            else{
                ApiCallErrorHandler.Error(apiResponse.message())
            }
        } catch (e: Exception){
            ApiCallErrorHandler.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun pokemonDetails(name: String): ApiCallErrorHandler<PokemonDetailsDataClass> {
        return try {
            val apiResponse = api.getPokemonDetails(name)
            val apiResult = apiResponse.body()

            if (apiResponse.isSuccessful && apiResult != null) {
                ApiCallErrorHandler.Success(apiResult)
            }
            else if (apiResponse.code() == 400){
                ApiCallErrorHandler.Error("Bad Request")
            }
            else if (apiResponse.code() == 401){
                ApiCallErrorHandler.Error("Unauthorized")
            }
            else{
                ApiCallErrorHandler.Error(apiResponse.message())
            }
        } catch (e: Exception){
            ApiCallErrorHandler.Error(e.message ?: "An unknown error occurred")
        }
    }

}