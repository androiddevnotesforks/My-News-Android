package info.tommarsh.mynews.categories.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import info.tommarsh.mynews.categories.model.CategoryViewModel
import info.tommarsh.mynews.categories.ui.adapter.viewholder.CategoryChoiceViewholder
import info.tommarsh.mynews.core.util.getDiffUtilItemCallback

class CategoryChoiceAdapter(private val onClick: (category: CategoryViewModel, selected: Boolean) -> Unit) :
    ListAdapter<CategoryViewModel, CategoryChoiceViewholder>(
        callback
    ) {

    companion object {
        val callback =
            getDiffUtilItemCallback<CategoryViewModel> { old, new -> old.id == new.id }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CategoryChoiceViewholder(parent, onClick)

    override fun onBindViewHolder(holder: CategoryChoiceViewholder, position: Int) {
        holder.bind(getItem(position))
    }
}