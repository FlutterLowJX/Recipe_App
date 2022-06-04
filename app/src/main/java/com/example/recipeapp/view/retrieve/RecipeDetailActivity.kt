package com.example.recipeapp.view.retrieve

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ActivityRecipeDetailBinding
import com.example.recipeapp.db.response.RecipeModel
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

class RecipeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent.getSerializableExtra("ex") as? RecipeModel
        binding.text.text = intent!!.foodName


        Picasso.get().load(intent.picture).transform(RoundedCornersTransformation(10, 10)).placeholder(R.drawable.ic_android_black_24dp).into(binding.foodDetailImage)

    }
}