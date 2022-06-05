package com.example.recipeapp.db.repo

import android.net.Uri
import com.example.recipeapp.db.firebasestorage.FirebaseStorageManager
import com.example.recipeapp.db.response.RecipeModel
import com.example.recipeapp.db.retrofit.RetrofitService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository constructor(private val retrofitService: RetrofitService) {
    suspend fun getRecipeList() = retrofitService.getRecipeList()
    suspend fun addRecipe(recipeModel: RecipeModel) =
        retrofitService.addRecipe(recipeModel.foodName!!, recipeModel.foodType!!, recipeModel.picture!!, Gson().toJson(recipeModel.ingredient), Gson().toJson(recipeModel.step))

    suspend fun uploadImageFile(uri: Uri, fileName: String) = withContext(Dispatchers.IO) {
        return@withContext FirebaseStorageManager.uploadImage(uri, FirebaseStorageManager.MEMBER_IMAGE_FOLDER, fileName)
    }

    suspend fun deleteImageFile(filePath: String) = withContext(Dispatchers.IO) {
        return@withContext FirebaseStorageManager.deleteImage(filePath)
    }
}
