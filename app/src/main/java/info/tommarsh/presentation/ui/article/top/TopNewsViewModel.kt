package info.tommarsh.presentation.ui.article.top

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import info.tommarsh.domain.source.ArticleRepository
import info.tommarsh.presentation.model.ArticleViewModel
import info.tommarsh.presentation.model.mapper.ArticleViewModelMapper
import info.tommarsh.presentation.ui.common.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopNewsViewModel
@Inject constructor(
    private val repository: ArticleRepository,
    private val mapper: ArticleViewModelMapper
) : BaseViewModel() {

    init {
        refreshBreakingNews()
    }

    fun getArticlesObservable(): LiveData<List<ArticleViewModel>> {
        return Transformations.map(repository.getBreakingNews("")) { domain ->
            domain.map { mapper.map(it) }
        }
    }

    fun getErrors() = repository.errors

    fun refreshBreakingNews() = launch { repository.refreshBreakingNews() }
}