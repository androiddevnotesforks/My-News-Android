package info.tommarsh.mynews.presentation.ui.top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import info.tommarsh.mynews.core.article.data.ArticleRepository
import info.tommarsh.mynews.core.util.coroutines.DispatcherProvider
import info.tommarsh.mynews.presentation.model.mapper.ArticleViewModelMapper
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TopNewsViewModel
@Inject constructor(
    private val repository: ArticleRepository,
    private val mapper: ArticleViewModelMapper,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    val articles = repository.getBreakingNews("")
        .onStart { refreshBreakingNews() }
        .map { mapper.map(it) }
        .asLiveData(dispatcherProvider.main())

    val errors = repository.errors

    fun refreshBreakingNews() {
        viewModelScope.launch {
            withContext(dispatcherProvider.work()) {
                repository.refreshBreakingNews()
            }
        }
    }
}