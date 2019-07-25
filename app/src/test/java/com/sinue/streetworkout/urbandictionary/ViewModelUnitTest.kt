package com.sinue.streetworkout.urbandictionary

import com.sinue.streetworkout.urbandictionary.viewmodel.MainViewModelImpl
import com.sinue.streetworkout.urbandictionary.viewmodel.UrbanDictionaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.rules.TestRule
import org.junit.Rule


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ViewModelUnitTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    val WORD_SEARCH = "Nike"

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun whenTestCalled_thenObserving() {
        val viewModel = MainViewModelImpl()
        viewModel.searchTerm(WORD_SEARCH)
        Thread.sleep(15000)
        viewModel.sortResults("thumbsUp", false)

        assert(viewModel.searchItemsResults.value!![0].thumbs_up
            .compareTo(viewModel.searchItemsResults.value!![1].thumbs_up) > 0
        )



    }

    /*Tests for class UrbanDictionaryRepository*/
/*
    @Rule
    @JvmField var rule: TestRule = InstantTaskExecutorRule()
    @Test
    fun callToPI_isCorrect() {

        val itemSearchRepository = UrbanDictionaryRepository()
        val searchList = itemSearchRepository.getSearchResults(null, term)
        Thread.sleep(5000)
        assertTrue(searchList.isNotEmpty())
    }*/

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
