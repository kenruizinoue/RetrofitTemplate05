package com.kenruizinoue.retrofittemplate05.data

import retrofit2.http.GET

interface ApiService {
    @GET("character")
    suspend fun getRickAndMortyCharacters(): RickAndMortyCharacters
}