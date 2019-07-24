package com.sinue.streetworkout.urbandictionary

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.sinue.streetworkout.urbandictionary.model.ItemSearch
import com.sinue.streetworkout.urbandictionary.view.MainActivity
import junit.framework.Assert
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


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
        onView(withId(R.id.editTxt_search))
            .perform(click())
        onView(withId(R.id.editTxt_search))
            .perform(doubleClick())

        onView(withId(R.id.editTxt_search))
            .perform(typeText(INPUT_STRING))

        onView(withId(R.id.editTxt_search))
            .check(matches(withText(INPUT_STRING)))
    }

    //Won't pass, cannot perform click or focus to type text
    @Test
    fun notEmptyResults(){

        val recView: RecyclerView = mainActivityRule.activity.findViewById(R.id.recView_search)
        onView(withId(R.id.editTxt_search)).perform(click())
        onView(withId(R.id.editTxt_search)).perform(typeText(INPUT_STRING))
        onView(withId(R.id.editTxt_search)).perform(pressImeActionButton())

        Thread.sleep(15000)
        onData(allOf(instanceOf(ItemSearch::class.java)))
        val itemCount = recView.adapter!!.itemCount
        assert(itemCount > 0)

    }

    /*Copied from the Instrumented test file created by Espresso Recorder
    * */
    private fun childAtPosition(parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

}
