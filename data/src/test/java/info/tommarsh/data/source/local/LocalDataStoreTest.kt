package info.tommarsh.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import info.tommarsh.data.model.MockProvider.article
import info.tommarsh.data.model.MockProvider.articleModel
import info.tommarsh.data.model.local.Article
import info.tommarsh.data.model.local.mapper.ArticleDataToDomainMapper
import info.tommarsh.data.model.local.mapper.ArticleDomainToDataMapper
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class LocalDataStoreTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val dao = mock<ArticlesDao>()
    private val dataToDomainMapper = mock<ArticleDataToDomainMapper> {
        on { map(article) }.thenReturn(articleModel)
    }
    private val domainToDataMapper = mock<ArticleDomainToDataMapper> {
        on { map(articleModel) }.thenReturn(article)
    }
    private val localDataStore = LocalDataStore(dao, dataToDomainMapper, domainToDataMapper)
    private val articleLiveData = MutableLiveData<List<Article>>()

    @Test
    fun `get breaking news from DB`() {
        whenever(dao.getBreakingArticles()).thenReturn(articleLiveData as LiveData<List<Article>>)

        localDataStore.getBreakingNews()

        verify(dao).getBreakingArticles()
    }

    @Test
    fun `Save breaking news to DB`() {

        localDataStore.saveBreakingNews(listOf(articleModel, articleModel))

        verify(dao).insertBreakingArticles(article, article)
    }
}