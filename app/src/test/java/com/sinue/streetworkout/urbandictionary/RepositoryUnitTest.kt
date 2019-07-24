package com.sinue.streetworkout.urbandictionary

//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.sinue.streetworkout.urbandictionary.model.ItemSearch
import com.sinue.streetworkout.urbandictionary.networking.RestApiService
import com.sinue.streetworkout.urbandictionary.viewmodel.MainViewModelImpl
import com.sinue.streetworkout.urbandictionary.viewmodel.UrbanDictionaryRepository2
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertThat

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
    //inline fun <reified T> lambdaMock(): T = mock(T::class.java)
    private val fakeList: MutableList<ItemSearch> = MutableList(10) {
            index -> ItemSearch("",6*index, 2*index)
    }
    private val fakeLiveData = MutableLiveData<ItemSearch>()


    /*Tests for class UrbanDictionaryRepository*/

    @Test
    fun callToPI_isCorrect() {

        coroutinesTestRule.testDispatcher.runBlockingTest {
            coroutineScope.launch {
                val searchList = repository.getResults(WORD_SEARCH)
                searchList.forEach { System.out.println(it.thumbs_up) }
                assertTrue(searchList.isNotEmpty())
                assertTrue(searchList.size == 10)
            }

        }

        //In order to give time to retrieve data, it's a workaround, the runBlockingTest doesn't work as expected
        Thread.sleep(5000)

    }


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

    /*Tests for class MainViewModelImpl*/

    /*
    @Test
    fun search_with_results() {
        val mainViewModel = MainViewModelImpl()
        mainViewModel.sortResults("thumbs_up", true)
        mainViewModel.searchItemsResults.value
        mainViewModel.sortResults("thumbs_up", false)
        mainViewModel.searchItemsResults.value


        mainViewModel.sortResults("thumbs_down", true)
        mainViewModel.sortResults("thumbs_down", false)

    }

    @Test
    fun results_are_sorted() {
        val mainViewModel = MainViewModelImpl()
        assertNotNull(mainViewModel.searchItemsResults)
        assertTrue(mainViewModel.searchItemsResults.value!!.isNotEmpty())
        assertTrue(mainViewModel.searchItemsResults.value!!.size > 0)
    }*/

}
