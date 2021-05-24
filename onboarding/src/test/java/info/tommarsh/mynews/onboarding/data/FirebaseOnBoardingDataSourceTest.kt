package info.tommarsh.mynews.onboarding.data

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import info.tommarsh.mynews.core.model.Resource
import info.tommarsh.mynews.onboarding.MockProvider.choices
import info.tommarsh.mynews.onboarding.model.Choices
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock

class FirebaseOnBoardingDataSourceTest {

    private val adapter = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
        .adapter(Choices::class.java)

    private val remoteConfig = mock<FirebaseRemoteConfig> {
        on { getString("key") }.thenReturn(adapter.toJson(choices))
    }

    private val datasource = FirebaseOnBoardingDataSource(remoteConfig, adapter)


    @Test
    fun `Get onboarding choices from firebase`() {
        val expected = Resource.Data(choices)

        val actual = datasource.getOnBoardingChoices("key")

        assertEquals(expected, actual)
    }
}