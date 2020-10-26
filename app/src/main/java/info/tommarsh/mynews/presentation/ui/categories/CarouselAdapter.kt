package info.tommarsh.mynews.presentation.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import info.tommarsh.mynews.categories.model.CategoryViewModel
import info.tommarsh.mynews.core.ui.ListLoadStateAdapter
import info.tommarsh.mynews.core.util.createDiffItemCallback
import info.tommarsh.mynews.presentation.model.ArticleViewModel
import info.tommarsh.presentation.databinding.ItemCarouselBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

internal class CarouselAdapter(
    private val lifecycle: Lifecycle,
    private val pagingFactory: (category: String) -> Flow<PagingData<ArticleViewModel>>
) : ListAdapter<CategoryViewModel, CarouselViewHolder>(DIFFER) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding =
            ItemCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding, lifecycle, pagingFactory)
    }

    override fun onBindViewHolder(
        holder: CarouselViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if(payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            if(payloads.contains(CarouselPayload.Refresh)) {
                holder.refresh()
            }
        }
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewRecycled(holder: CarouselViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }

    fun refresh() {
        notifyItemRangeChanged(0, itemCount, CarouselPayload.Refresh)
    }

    companion object {
        private val DIFFER = createDiffItemCallback<CategoryViewModel> { old, new ->
            old.name == new.name
        }
    }
}

private sealed class CarouselPayload {
    object Refresh: CarouselPayload()
}

internal class CarouselViewHolder(
    private val binding: ItemCarouselBinding,
    private val lifecycle: Lifecycle,
    private val pagingFactory: (category: String) -> Flow<PagingData<ArticleViewModel>>
) :
    RecyclerView.ViewHolder(binding.root) {

    private var job: Job? = null

    private var adapter: CarouselItemAdapter? = null

    fun bind(carousel: CategoryViewModel) {
        adapter = CarouselItemAdapter()
        binding.carouselName.text = carousel.name
        binding.carouselItems.adapter =
            adapter!!.withLoadStateFooter(footer = ListLoadStateAdapter { adapter?.retry() })
        job = lifecycle.coroutineScope.launchWhenCreated {
            pagingFactory(carousel.id).collect { pagingData ->
                adapter!!.submitData(pagingData)
            }
        }
    }

    fun refresh() {
        adapter?.refresh()
    }

    fun unbind() {
        job?.cancel()
    }
}