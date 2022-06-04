package com.example.recipeapp.view.retrieve

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import com.example.recipeapp.R
import com.example.recipeapp.db.response.RecipeModel
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation


class RecipeAdapter(
    dataSet: List<RecipeModel> = emptyList(),
    private val showDetail: (RecipeModel) -> Unit
) : DragDropSwipeAdapter<RecipeModel, RecipeAdapter.ViewHolder>(dataSet) {

    class ViewHolder(itemView: View) : DragDropSwipeAdapter.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.foodImage)!!
        val name = itemView.findViewById<TextView>(R.id.foodName)!!
        val drag = itemView.findViewById<ImageView>(R.id.foodImage)!!
        val card = itemView.findViewById<CardView>(R.id.item_card)!!
        val context = itemView.context!!
    }

    override fun getViewHolder(itemView: View) = ViewHolder(itemView)

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(item: RecipeModel, viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = item.foodName
        Picasso.get().load(item.picture).transform(CropCircleTransformation()).resize(100, 100).centerCrop().placeholder(R.drawable.ic_android_black_24dp).into(viewHolder.image)
        viewHolder.card.setOnClickListener {
            showDetail(item)
        }
        Log.e("test", Gson().toJson(item.step))
    }

    override fun getViewToTouchToStartDraggingItem(item: RecipeModel, viewHolder: ViewHolder, position: Int) = viewHolder.drag
}
