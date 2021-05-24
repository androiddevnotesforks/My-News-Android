package info.tommarsh.mynews.core.video.data.remote.paging

import androidx.paging.PagingSource
import info.tommarsh.mynews.core.MockProvider.playlistModel
import info.tommarsh.mynews.core.model.Resource
import info.tommarsh.mynews.core.video.data.remote.source.VideoRemoteDataStore
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class VideoPagingSourceTest {

    private val remoteDataStore = mock<VideoRemoteDataStore>()

    private val pagingSource = VideoPagingSource(remoteDataStore)

    @Test
    fun `Successfully fetch videos`() = runBlockingTest {
        whenever(remoteDataStore.getPlaylist("token"))
            .thenReturn(Resource.Data(playlistModel))

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = "token",
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
    }

    @Test
    fun `Fail to fetch videos`() = runBlockingTest {
        whenever(remoteDataStore.getPlaylist("token"))
            .thenReturn(Resource.Data(playlistModel))

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = "token",
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
    }
}