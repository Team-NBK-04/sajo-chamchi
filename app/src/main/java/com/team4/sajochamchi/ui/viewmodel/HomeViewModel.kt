package com.team4.sajochamchi.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.team4.sajochamchi.data.model.SaveCategory
import com.team4.sajochamchi.data.model.SaveChannel
import com.team4.sajochamchi.data.model.SaveItem
import com.team4.sajochamchi.data.model.category.toSaveCategory
import com.team4.sajochamchi.data.model.channel.toSaveChannel
import com.team4.sajochamchi.data.model.video.toSaveItem
import com.team4.sajochamchi.data.repository.TotalRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: TotalRepository) : ViewModel() {
    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _saveList: MutableLiveData<List<SaveItem>> = MutableLiveData()
    val saveList: LiveData<List<SaveItem>>
        get() = _saveList

    private val _channel: MutableLiveData<List<SaveChannel>> = MutableLiveData()
    val channel: LiveData<List<SaveChannel>>
        get() = _channel

    private val _categories: MutableLiveData<List<SaveCategory>> = MutableLiveData()
    val categories: LiveData<List<SaveCategory>>
        get() = _categories

    init {
        _categories.value = repository.getCateoryListPefs()
    }

    fun getAllMostPopular() = viewModelScope.launch {
        val response = repository.getAllMostPopular()
        if (response.isSuccessful) {
            response.body()?.let { body ->
                val saveItemList = body.items?.map { it.toSaveItem() }
                saveItemList?.forEach {
                    Log.d(TAG, "getAllMostPopular: $it")
                }
                if (saveItemList != null) _saveList.value = saveItemList!!
            }
        } else {
            Log.d(TAG, "getAllMostPopular.isNotSuccessful")
            Log.d(TAG, response.message())
        }
    }

    fun getAllMostPopularWithCategoryId(videoCategoryId: String) = viewModelScope.launch {
        val response = repository.getAllMostPopularWithCategoryId(videoCategoryId = videoCategoryId)
        if (response.isSuccessful) {
            response.body()?.let { body ->
                val saveItemList = body.items?.map { it.toSaveItem() }
                saveItemList?.forEach {
                    Log.d(TAG, "getAllMostPopularWithCategoryId: $it")
                    if (it.channelId != null) getChannelWithId(id = it.channelId)
                }
            }
        } else {
            Log.d(TAG, "getAllMostPopularWithCategoryId.isNotSuccessful")
            Log.d(TAG, response.message())
        }
    }

    fun getAllCategories() = viewModelScope.launch {
        val response = repository.getAllCategories()
        if (response.isSuccessful) {
            response.body()?.let { body ->
                val saveItemList = body.items?.map { it.toSaveCategory() }
                saveItemList?.forEach {
                    Log.d(TAG, "getAllCategories: $it")
                }
            }
        } else {
            Log.d(TAG, "getAllCategories.isNotSuccessful")
            Log.d(TAG, response.message())
        }
    }

    private fun getChannelWithId(id: String) = viewModelScope.launch {
        val response = repository.getChannelWithId(id)
        if (response.isSuccessful) {
            response.body()?.let { body ->
                val saveItemList = body.items?.map { it.toSaveChannel() }
                saveItemList?.forEach {
                    Log.d(TAG, "getChannelWithId: $it")
                }
            }
        } else {
            Log.d(TAG, "getAllCategories.isNotSuccessful")
            Log.d(TAG, response.message())
        }
    }
}

class HomeViewModelFactory(private val totalRepository: TotalRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(totalRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}