package com.kenruizinoue.retrofittemplate05.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kenruizinoue.retrofittemplate05.data.Repository
import com.kenruizinoue.retrofittemplate05.data.RickAndMortyCharacters
import com.kenruizinoue.retrofittemplate05.data.ServiceBuilder
import kotlinx.coroutines.Dispatchers.IO

class MainViewModel: ViewModel() {
    private val repository: Repository = Repository(ServiceBuilder.getApiService())
    val data: LiveData<RickAndMortyCharacters> = liveData(IO) {
        val retrievedData = repository.getRickAndMortyCharacters()
        emit(retrievedData)
    }
}