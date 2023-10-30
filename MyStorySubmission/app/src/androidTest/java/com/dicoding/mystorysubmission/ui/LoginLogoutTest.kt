package com.dicoding.mystorysubmission.ui

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.dicoding.mystorysubmission.utlis.EspressoIdlingResource
import com.dicoding.mystorysubmission.ui.landingScreen.LandingActivity
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import com.dicoding.mystorysubmission.R
import com.dicoding.mystorysubmission.ui.login.LoginActivity
import com.dicoding.mystorysubmission.ui.main.MainActivity


@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginLogoutTest {

    @get:Rule
    val activity = ActivityScenarioRule(LandingActivity::class.java)

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        Intents.release()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loginLogoutTest() {
        Intents.init()
        onView(withId(R.id.btn_login_welcome)).perform(click())

        Intents.intended(hasComponent(LoginActivity::class.java.name))
        onView(withId(R.id.et_email)).perform(typeText(EMAIL), closeSoftKeyboard())
        onView(withId(R.id.et_password)).perform(typeText(PASSWORD), closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())

        Intents.intended(hasComponent(MainActivity::class.java.name))
        onView(withId(R.id.rv_story)).check(matches(isDisplayed()))

        onView(withId(R.id.settings)).perform(click())
        onView(withText(R.string.log_out)).perform(click())
        Intents.intended(hasComponent(LandingActivity::class.java.name))
    }

    companion object {
        const val EMAIL = "azz@gmail.com"
        const val PASSWORD = "12345678"
    }
}