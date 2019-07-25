package com.sinue.streetworkout.urbandictionary

//import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import com.sinue.streetworkout.urbandictionary.networking.RestApiService
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class NetworkUnitTesting {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun testFoo() = runBlockingTest {

        launch {
            val response = RestApiService.createCorService().getSearchList(BuildConfig.API_KEY_URBAN_DIC, "ha").await()
            assert(response.body()!!.list.isNotEmpty())
        }
        //In order to give time to retrieve data, it's a workaround, the runBlockingTest doesn't work as expected
        Thread.sleep(5000)
    }

}
