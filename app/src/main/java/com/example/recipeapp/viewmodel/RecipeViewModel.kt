package com.example.recipeapp.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp.db.repo.Repository
import com.example.recipeapp.db.response.RecipeModel
import kotlinx.coroutines.*

class RecipeViewModel constructor(private val repository: Repository) : ViewModel() {
    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val getRecipeList = MutableLiveData<MutableList<RecipeModel>>()
    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun getRecipeList() {
        //this list for adjust the list data
        //val _getRecipeList = mutableListOf<RecipeModel>()
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getRecipeList()
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        getRecipeList.postValue(response.body()!!.data)
                        loading.value = false
                    }
                } catch (e: Exception) {
                    getRecipeList.postValue(mutableListOf())
                    onError("Error : Failed ")
                }

            }
        }
    }

    fun addRecipe(imageUrl: Uri, imageFileName: String, recipeModel: RecipeModel, changePic: Boolean) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            withContext(Dispatchers.Main) {
                if (changePic) {
                    val uploadImage = async {
                        repository.uploadImageFile(imageUrl, imageFileName)
                    }
                    val imgUrl = uploadImage.await()
                    //val imagePath = "${FirebaseStorageManager.MEMBER_IMAGE_FOLDER}$imageFileName"
                    recipeModel.picture = imgUrl
                }
                val response = async {
                    repository.addRecipe(recipeModel)
                }

                Log.e("???", recipeModel.toString())
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}