package com.example.recipeapp.view.retrieve

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView.ListOrientation.DirectionFlag.*
import com.example.recipeapp.databinding.ActivityRecipeBinding
import com.example.recipeapp.db.repo.Repository
import com.example.recipeapp.db.response.RecipeModel
import com.example.recipeapp.db.retrofit.RetrofitService
import com.example.recipeapp.view.create.CreateRecipeActivity
import com.example.recipeapp.viewmodel.RecipeViewModel
import com.example.recipeapp.viewmodel.ViewModelFactory
import java.io.Serializable

class RecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeBinding
    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var adapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        recipeViewModel = ViewModelProvider(this, ViewModelFactory(Repository(RetrofitService.getInstance())))[RecipeViewModel::class.java]
        adapter = RecipeAdapter(showDetail = ::showDetail, editDetail = ::editDetail)
        recipeViewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        recipeViewModel.loading.observe(this) {
            binding.progressFrameLayout.visibility = if (it) View.VISIBLE else View.GONE
        }
        recipeViewModel.getRecipeList()
        recipeViewModel.getRecipeList.observe(this) {
            Log.e("Recipe", it.toString())
            if (adapter.itemCount != 0) for (i in 0 until adapter.itemCount) {
                adapter.removeItem(i)
                adapter.notifyItemRemoved(i)
            }
            it.forEach { item -> adapter.addItem(item) }
            binding.recipeList.layoutManager = LinearLayoutManager(this)
            binding.recipeList.adapter = adapter
            binding.recipeList.disableSwipeDirection(LEFT)
            binding.recipeList.disableSwipeDirection(RIGHT)
            binding.recipeList.disableSwipeDirection(UP)
            binding.recipeList.disableSwipeDirection(DOWN)
            binding.recipeList.disableDragDirection(LEFT)
            binding.recipeList.disableDragDirection(RIGHT)
            binding.recipeList.disableDragDirection(UP)
            binding.recipeList.disableDragDirection(DOWN)
        }
        binding.recipeFab.setOnClickListener {
            val intent = Intent(this, CreateRecipeActivity::class.java)
            intent.putExtra("from", "Add Recipe")
            startActivity(intent)
        }
        setContentView(binding.root)
    }

    private fun editDetail(recipeModel: RecipeModel) {
        val intent = Intent(this, CreateRecipeActivity::class.java)
        intent.putExtra("ex", recipeModel as Serializable)
        intent.putExtra("from", "Edit Recipe")
        this.startActivity(intent)
    }

    private fun showDetail(recipeModel: RecipeModel) {
        val intent = Intent(this, RecipeDetailActivity::class.java)
        intent.putExtra("ex", recipeModel as Serializable)
        this.startActivity(intent)
    }
}
