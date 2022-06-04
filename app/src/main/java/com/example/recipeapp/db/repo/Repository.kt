package com.example.recipeapp.db.repo

import com.example.recipeapp.db.retrofit.RetrofitService

class Repository constructor(private val retrofitService: RetrofitService) {
    suspend fun getRecipeList() = retrofitService.getRecipeList()
}
