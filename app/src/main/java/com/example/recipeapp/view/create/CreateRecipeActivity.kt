package com.example.recipeapp.view.create

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ActivityCreateRecipeBinding
import com.example.recipeapp.db.response.RecipeModel
import com.swein.easyphotopicker.SystemPhotoPickManager

class CreateRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateRecipeBinding
    private var tempImageUri: Uri? = null
    private val provider = "com.example.recipeapp.provider"
    private var recipeModel: RecipeModel? = null
    private val systemPhotoPickManager = SystemPhotoPickManager(this, provider)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ArrayAdapter.createFromResource(this, R.array.food_type, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.edtFoodType.adapter = adapter
        }
        save()
        image()
    }

    private fun save() {
        binding.addButton.setOnClickListener {
            recipeModel = RecipeModel().apply {
                foodName = binding.edtFoodName.text.toString().trim()
                foodType = binding.edtFoodType.selectedItem.toString().trim()
                picture = tempImageUri.toString()
                ingredient = mutableListOf("test", "test")
                step = mutableListOf("test", "test")
            }
            Log.e("???", recipeModel.toString())
        }

    }

    private fun image() {
        binding.buttonCamera.setOnClickListener {
            systemPhotoPickManager.requestPermission {
                it.takePictureWithUri { uri ->
                    binding.linearLayoutPhotoSelector.visibility = View.GONE
                    tempImageUri = uri
                    binding.addRecipeImage.setImageURI(uri)
                }
            }
        }
        binding.buttonGallery.setOnClickListener {
            systemPhotoPickManager.requestPermission {
                it.selectPicture { uri ->
                    binding.linearLayoutPhotoSelector.visibility = View.GONE
                    tempImageUri = uri
                    binding.addRecipeImage.setImageURI(uri)
                }
            }
        }
        binding.addRecipeImage.setOnClickListener {
            binding.linearLayoutPhotoSelector.visibility = View.VISIBLE
        }
    }
}