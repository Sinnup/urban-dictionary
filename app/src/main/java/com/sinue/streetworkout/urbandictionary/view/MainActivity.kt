package com.sinue.streetworkout.urbandictionary.view

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.idling.CountingIdlingResource
import com.sinue.streetworkout.urbandictionary.R
import com.sinue.streetworkout.urbandictionary.model.ItemSearch
import com.sinue.streetworkout.urbandictionary.viewmodel.MainViewModelImpl
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), DialogFragmentSort.DialogListener {

    var idlingRes = CountingIdlingResource("DATA_LOADER")

    private var mainViewModel: MainViewModelImpl? = null
    private var searchResultsAdapter: SearchResultsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModelImpl::class.java)

        initializeListeners()

    }

    private fun initializeListeners(){
        searchTxt.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0 != null && p0.isNotEmpty()){
                    idlingRes.increment()
                    searchWord(p0)
                    showProgressDialog(R.id.constraintLayout_main)

                }

                return true
            }
        })

        //Converting to lambda expression, without using the view
        imageButton_sort.setOnClickListener{
            showDialogFragment(1)
        }
    }

    fun searchWord(term: String) {

        mainViewModel!!.searchTerm(this, term)

        mainViewModel!!.waitingForResponse.observe(this, Observer {
            dismissProgressDialog()
            idlingRes.decrement()

        })

        mainViewModel!!.searchItemsResults.observe(this, Observer {
            prepareRecyclerView(it)
            dismissProgressDialog()
            idlingRes.decrement()

        })

    }

    private fun prepareRecyclerView(listItemSearch: List<ItemSearch>){

        val viewManager: RecyclerView.LayoutManager
        searchResultsAdapter = SearchResultsAdapter(listItemSearch)

        if (resources.configuration.orientation != Configuration.ORIENTATION_PORTRAIT){
            viewManager = GridLayoutManager(this, 2)

        } else {
            viewManager = LinearLayoutManager(this)

        }

        recView_search.apply {
            layoutManager = viewManager
            itemAnimator = DefaultItemAnimator()
            adapter = searchResultsAdapter
        }

        imageButton_sort.isEnabled = true
        imageButton_sort.isClickable = true
        dismissProgressDialog()

    }

    private fun showDialogFragment(tag: Int) {
        val dialogFragment = DialogFragmentSort()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        dialogFragment.show(fragmentTransaction, tag.toString())
    }

    override fun onSelectDoneDialog(sortBy: String, inputTag: String) {

        //The observer will be in charge of updating the RecyclerView
        mainViewModel!!.sortResults(sortBy, false)
        mainViewModel!!.searchItemsResults.observe(this, Observer {
            prepareRecyclerView(it)
        })
    }

    override fun onCancelDialog(cancelDialog: Unit) {
        //Any action when user closes Dialog Fragment
        //Toast.makeText(applicationContext, "Nothing to sort", Toast.LENGTH_SHORT).show()
    }


}
