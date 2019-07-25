package com.sinue.streetworkout.urbandictionary

//import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import com.sinue.streetworkout.urbandictionary.model.ItemSearch
import com.sinue.streetworkout.urbandictionary.networking.RestApiService
import com.sinue.streetworkout.urbandictionary.viewmodel.MainViewModelImpl
import com.sinue.streetworkout.urbandictionary.viewmodel.UrbanDictionaryRepository2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class RepositoryUnitTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val WORD_SEARCH = "Nike"
    private val coroutineScope = CoroutineScope( Dispatchers.Main+SupervisorJob())
    private val repository = UrbanDictionaryRepository2(RestApiService.createCorService())
    private val fakeList: MutableList<ItemSearch> = MutableList(10) {
            index -> ItemSearch("",6*index, 2*index)
    }
    /*Tests for class UrbanDictionaryRepository*/

    @Test
    fun callToPI_isCorrect() {

        coroutineScope.launch {
            val searchList = repository.getResults(WORD_SEARCH)
            searchList.forEach { System.out.println(it.thumbs_up) }
            assertTrue(searchList.isNotEmpty())
            assertTrue(searchList.size == 10)
        }

        //In order to give time to retrieve data, it's a workaround, the runBlockingTest doesn't work as expected
        Thread.sleep(5000)

    }

/*
    @Test
    fun whenTestCalled_thenObserving() {

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val viewModel = MainViewModelImpl()
            viewModel.searchTerm(WORD_SEARCH)
            viewModel.sortResults("thumbsUp", false)

            assert(viewModel.searchItemsResults.value!![0].thumbs_up
                .compareTo(viewModel.searchItemsResults.value!![1].thumbs_up) > 0
            )

        }

        Thread.sleep(5000)

    }
*/

}
