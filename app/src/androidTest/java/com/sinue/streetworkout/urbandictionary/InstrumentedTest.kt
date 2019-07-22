package com.sinue.streetworkout.urbandictionary

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.sinue.streetworkout.urbandictionary.view.MainActivity
import junit.framework.Assert
import org.hamcrest.core.IsNot

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.runners.MethodSorters
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher


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

    //Won't pass, cannot perform click or focus to type text
    @Test
    fun canTypeInSearchView(){
        Espresso.onView(ViewMatchers.withId(R.id.searchTxt))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.searchTxt))
            .perform(ViewActions.doubleClick())

        Espresso.onView(ViewMatchers.withId(R.id.searchTxt))
            .perform(ViewActions.typeText(INPUT_STRING))

        Espresso.onView(ViewMatchers.withId(R.id.searchTxt))
            .check(ViewAssertions.matches(ViewMatchers.withText(INPUT_STRING)))
    }

    //Won't pass, cannot perform click or focus to type text
    @Test
    fun notEmptyResults(){

        val recView: RecyclerView = mainActivityRule.activity.findViewById(R.id.recView_search)

        Espresso.registerIdlingResources(mainActivityRule.activity.idlingRes)
        Espresso.onView(ViewMatchers.withId(R.id.searchTxt)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.searchTxt)).perform(ViewActions.typeText(INPUT_STRING))
        Espresso.onView(ViewMatchers.withId(R.id.searchTxt)).perform(ViewActions.pressImeActionButton())

        val itemCount = recView.adapter!!.itemCount

        assert(itemCount > 0)

    }

    /*
    fun typeSearchViewText(text: String): ViewAction {
        return object : ViewAction() {
            //Ensure that only apply if it is a SearchView and if it is visible.
            val constraints: Matcher<View>
                get() = allOf(isDisplayed(), isAssignableFrom(SearchView::class.java))

            val description: String
                get() = "Change view text"

            fun perform(uiController: UiController, view: View) {
                (view as SearchView).setQuery(text, false)
            }
        }
    }*/
}
