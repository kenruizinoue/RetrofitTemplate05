package com.kenruizinoue.retrofittemplate05.data

class Repository(private val apiService: ApiService) {
    suspend fun getRickAndMortyCharacters() = apiService.getRickAndMortyCharacters()
}