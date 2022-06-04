package com.example.recipeapp.view.create

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import com.example.recipeapp.R

class IngredientAdapter(dataSet: MutableList<String> = mutableListOf(), private val setText: (String, String, Int?) -> Unit) : DragDropSwipeAdapter<String, IngredientAdapter.ViewHolder>(dataSet) {
    class ViewHolder(itemView: View) : DragDropSwipeAdapter.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.step_image)!!
        val number = itemView.findViewById<TextView>(R.id.step_number)!!
        val card = itemView.findViewById<CardView>(R.id.step_card)!!
        val context = itemView.context!!
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(item: String, viewHolder: ViewHolder, position: Int) {
        viewHolder.number.text = (position + 1).toString()
        viewHolder.image.setOnClickListener {
            setText("ingredient", item, position)
        }
    }

    override fun getViewHolder(itemView: View) = ViewHolder(itemView)

    override fun getViewToTouchToStartDraggingItem(item: String, viewHolder: ViewHolder, position: Int): Nothing? = null
}