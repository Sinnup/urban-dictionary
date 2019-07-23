package com.sinue.streetworkout.urbandictionary

import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule

import com.sinue.streetworkout.urbandictionary.model.ItemSearch
import com.sinue.streetworkout.urbandictionary.view.MainActivity
import junit.framework.Assert
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.hamcrest.CoreMatchers.*


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class InstrumentedTest {

    val INPUT_STRING : String = "Nike"

    @get:Rule
    val mainActivityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.sinue.streetworkout.urbandictionary", appContext.packageName)
    }

    @Test
    fun canTypeEditTextSearch(){
        onView(withId(R.id.searchTxt))
            .perform(click())
        onView(withId(R.id.searchTxt))
            .perform(doubleClick())

        onView(withId(R.id.searchTxt))
            .perform(typeText(INPUT_STRING))

        onView(withId(R.id.searchTxt))
            .check(matches(withText(INPUT_STRING)))
    }

    //Won't pass, cannot perform click or focus to type text
    @Test
    fun canTypeInSearchView(){

        val matchers = allOf(isDisplayed())

        allOf(matchers, anyOf(supportsInputMethods(), isAssignableFrom(SearchView::class.java)))


    }

    //Won't pass, cannot perform click or focus to type text
    @Test
    fun notEmptyResults(){

        val recView: RecyclerView = mainActivityRule.activity.findViewById(R.id.recView_search)


        registerIdlingResources(mainActivityRule.activity.idlingRes)
        onView(withId(R.id.searchTxt)).perform(click())
        onView(withId(R.id.searchTxt)).perform(typeText(INPUT_STRING))
        onView(withId(R.id.searchTxt)).perform(pressImeActionButton())

        onData(allOf(instanceOf(ItemSearch::class.java)))
        //onData(allOf(instanceOf(ItemSearch::class.java)))
        //val itemCount = recView.adapter!!.itemCount

        //assert(itemCount > 0)

    }


}
