package com.example.recipeapp.db.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RecipeResponseBody(
    @SerializedName("data") var data: MutableList<RecipeModel> = mutableListOf()
)

data class RecipeModel(
    @SerializedName("id") var id: String? = null,
    @SerializedName("foodName") var foodName: String? = null,
    @SerializedName("foodType") var foodType: String? = null,
    @SerializedName("picture") var picture: String? = null,
    @SerializedName("ingredient") var ingredient: MutableList<String> = mutableListOf(),
    @SerializedName("step") var step: MutableList<String> = mutableListOf()
) : Serializable