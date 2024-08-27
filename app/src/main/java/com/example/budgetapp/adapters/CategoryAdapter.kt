package com.example.budgetapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetapp.R
import com.example.budgetapp.databinding.SampleCategoryItemBinding
import com.example.budgetapp.models.Category

class CategoryAdapter(
    var context: Context,
    private var categories: ArrayList<Category>,
    private var categoryClickListener: CategoryClickListener
) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    interface CategoryClickListener {
        fun onCategoryClicked(category: Category?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.sample_category_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.binding.categoryText.text = category.categoryName
        holder.binding.categoryIcon.setImageResource(category.categoryImage)

        holder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(category.categoryColor))

        holder.itemView.setOnClickListener {
            categoryClickListener.onCategoryClicked(category)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: SampleCategoryItemBinding = SampleCategoryItemBinding.bind(itemView)
    }
}