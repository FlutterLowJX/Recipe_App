package com.example.recipeapp.view.create

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView.ListOrientation.DirectionFlag.*
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ActivityCreateRecipeBinding
import com.example.recipeapp.db.response.RecipeModel
import com.squareup.picasso.Picasso
import com.swein.easyphotopicker.SystemPhotoPickManager
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

class CreateRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateRecipeBinding
    private lateinit var adapterStep: StepAdapter
    private lateinit var adapterIngredient: IngredientAdapter
    private var tempImageUri: Uri? = null
    private val provider = "com.example.recipeapp.provider"
    private var recipeModel: RecipeModel? = null
    private val systemPhotoPickManager = SystemPhotoPickManager(this, provider)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val message = intent.getStringExtra("from")
        recipeModel = intent.getSerializableExtra("ex") as? RecipeModel
        this.title = message
        image()
        ingredient()
        step()
        save()
        binding.edtFoodName.setText(if (recipeModel != null) recipeModel!!.foodName else "")

        ArrayAdapter.createFromResource(this, R.array.food_type, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.edtFoodType.adapter = adapter
            if (recipeModel != null)
                binding.edtFoodType.setSelection(adapter.getPosition(recipeModel!!.foodType))
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun ingredient() {
        binding.ingredientList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterIngredient = IngredientAdapter(setText = ::setText)
        if (recipeModel != null) {
            recipeModel!!.ingredient.forEach { item -> adapterIngredient.addItem(item) }
        }
        binding.ingredientList.adapter = adapterIngredient
        binding.ingredientList.disableSwipeDirection(LEFT)
        binding.ingredientList.disableSwipeDirection(RIGHT)
        binding.ingredientList.disableSwipeDirection(UP)
        binding.ingredientList.disableSwipeDirection(DOWN)
        binding.ingredientList.disableDragDirection(LEFT)
        binding.ingredientList.disableDragDirection(RIGHT)
        binding.ingredientList.disableDragDirection(UP)
        binding.ingredientList.disableDragDirection(DOWN)
        binding.addIngredient.setOnClickListener {
            adapterIngredient.addItem(binding.edtIngredient.text.toString().trim())
            adapterIngredient.notifyDataSetChanged()
            binding.edtIngredient.setText("")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun step() {
        binding.stepList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterStep = StepAdapter(setText = ::setText)
        if (recipeModel != null) {
            recipeModel!!.step.forEach { item -> adapterStep.addItem(item) }
        }
        binding.stepList.adapter = adapterStep
        binding.stepList.disableSwipeDirection(LEFT)
        binding.stepList.disableSwipeDirection(RIGHT)
        binding.stepList.disableSwipeDirection(UP)
        binding.stepList.disableSwipeDirection(DOWN)
        binding.stepList.disableDragDirection(LEFT)
        binding.stepList.disableDragDirection(RIGHT)
        binding.stepList.disableDragDirection(UP)
        binding.stepList.disableDragDirection(DOWN)
        binding.addStep.setOnClickListener {
            adapterStep.addItem(binding.edtStep.text.toString().trim())
            adapterStep.notifyDataSetChanged()
            binding.edtStep.setText("")
        }
    }

    private fun setText(type: String, data: String, position: Int?) {
        if (type == "step") {
            binding.edtStep.setText(data)
            if (position != null) {
                binding.updateStep.setOnClickListener {
                    adapterStep.removeItem(position)
                    adapterStep.insertItem(position, binding.edtStep.text.toString().trim())
                    adapterStep.notifyDataSetChanged()
                }
                binding.deleteStep.setOnClickListener {
                    adapterStep.removeItem(position)
                    adapterStep.notifyDataSetChanged()
                }
            }
        } else {
            binding.edtIngredient.setText(data)
            if (position != null) {
                binding.updateIngredient.setOnClickListener {
                    adapterIngredient.removeItem(position)
                    adapterIngredient.insertItem(position, binding.edtIngredient.text.toString().trim())
                    adapterIngredient.notifyDataSetChanged()
                }
                binding.deleteIngredient.setOnClickListener {
                    adapterIngredient.removeItem(position)
                    adapterIngredient.notifyDataSetChanged()
                }
            }
        }
    }

    private fun save() {
        binding.addButton.setOnClickListener {
            recipeModel = RecipeModel().apply {
                foodName = binding.edtFoodName.text.toString().trim()
                foodType = binding.edtFoodType.selectedItem.toString().trim()
                picture = tempImageUri.toString()
                ingredient = adapterIngredient.dataSet.toMutableList()
                step = adapterStep.dataSet.toMutableList()
            }
            Log.e("???", recipeModel.toString())
        }

    }

    private fun image() {
        if (recipeModel != null) {
            Picasso.get()
                .load(recipeModel!!.picture)
                .transform(RoundedCornersTransformation(10, 10))
                .placeholder(R.drawable.ic_add_photo_24)
                .into(binding.addRecipeImage)
        }

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