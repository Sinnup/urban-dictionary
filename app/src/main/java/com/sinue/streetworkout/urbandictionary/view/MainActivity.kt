package com.sinue.streetworkout.urbandictionary.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.idling.CountingIdlingResource
import com.sinue.streetworkout.urbandictionary.R
import com.sinue.streetworkout.urbandictionary.databinding.ActivityMainBinding
import com.sinue.streetworkout.urbandictionary.model.ItemSearch
import com.sinue.streetworkout.urbandictionary.viewmodel.MainViewModelImpl
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), DialogFragmentSort.DialogListener {

    @VisibleForTesting
    var idlingRes = CountingIdlingResource("DATA_LOADER")

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModelImpl
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    var termToSearch: String = "You have searched"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.mainView = this
        binding.lifecycleOwner = this

        //Obtaining ViewModel for MVVM pattern
        mainViewModel = ViewModelProviders.of(this).get(MainViewModelImpl::class.java)
        val list = if (mainViewModel.searchItemsResults.value != null) mainViewModel.searchItemsResults.value else listOf()
        prepareRecyclerView(list!!)

        initializeListeners()



        //mainViewModel.requesting.obser

    }

    private fun initializeListeners(){

        editTxt_search.setOnEditorActionListener(object: TextView.OnEditorActionListener{
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_SEARCH){
                    if (p0 != null && p0.text.isNotEmpty()){
                        termToSearch = p0.text.toString()
                        binding.invalidateAll()
                        searchWord(p0.text.toString())
                        //showProgressDialog(R.id.constraintLayout_main)

                    }
                }

                return true
            }
        })

        imageButton_sort.setOnClickListener{
            showDialogFragment(1)
        }
    }

    fun searchWord(term: String) {

        mainViewModel.requesting.observe(this, Observer {
            if (it) showProgressDialog(R.id.constraintLayout_main) else dismissProgressDialog()
        })

        mainViewModel.searchTerm(this, term)


        mainViewModel.searchItemsResults.observe(this, Observer {
            prepareRecyclerView(it)

            //dismissProgressDialog()

            //idlingRes.decrement()
        })




        /*
        mainViewModel!!.waitingForResponse.observe(this, Observer {
            dismissProgressDialog()
            idlingRes.decrement()

        })*/



    }

    private fun prepareRecyclerView(listItemSearch: List<ItemSearch>){

        val viewManager: RecyclerView.LayoutManager
        searchResultsAdapter = SearchResultsAdapter(listItemSearch)

        if (resources.configuration.orientation != Configuration.ORIENTATION_PORTRAIT){
            viewManager = GridLayoutManager(this, 2) as RecyclerView.LayoutManager

        } else {
            viewManager = LinearLayoutManager(this)

        }

        recView_search.apply {
            layoutManager = viewManager
            itemAnimator = DefaultItemAnimator()
            adapter = searchResultsAdapter
        }

        //dismissProgressDialog()
        imageButton_sort.isEnabled = true
        //imageButton_sort.isClickable = true

    }

    private fun showDialogFragment(tag: Int) {
        val dialogFragment = DialogFragmentSort()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        dialogFragment.show(fragmentTransaction, tag.toString())
    }

    override fun onSelectDoneDialog(sortBy: String, inputTag: String) {

        //The observer will be in charge of updating the RecyclerView
        mainViewModel.sortResults(sortBy, false)
        mainViewModel.searchItemsResults.observe(this, Observer {
            prepareRecyclerView(it)
        })
    }

    override fun onCancelDialog(cancelDialog: Unit) {
        //Any action when user closes Dialog Fragment
        //Toast.makeText(applicationContext, "Nothing to sort", Toast.LENGTH_SHORT).show()
    }


}
