package com.kupang.androidbpbd.bpbdkupang;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android.volley.toolbox.HurlStack;
import com.kupang.androidbpbd.bpbdkupang.ui.autentikasi.RegisterActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {

    String inputName = "user";
    String inputEmail = "user@gmail.com";
    String inputTelephoneNumber = "0853666111";
    @Rule
    public ActivityTestRule<RegisterActivity> mActivityRule = new ActivityTestRule<>(RegisterActivity.class);

    @Test
    public void checkAllViewsIsValid_sameActivity(){
        onView(withId(R.id.name_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.email_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.telephone_number_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.register_account_button)).check(matches(isDisplayed()));
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }


    @Test
    public void typeText_sameActivity(){
        onView(withId(R.id.name_edit_text)).perform(ViewActions.typeText(inputName),
                ViewActions.closeSoftKeyboard());
        onView(withId(R.id.email_edit_text)).perform(ViewActions.typeText(inputEmail),
        ViewActions.closeSoftKeyboard());
        onView(withId(R.id.telephone_number_edit_text)).perform(ViewActions.typeText(inputTelephoneNumber),
                ViewActions.closeSoftKeyboard());

        onView(withId(R.id.name_edit_text)).check(matches(withText(inputName)));
        onView(withId(R.id.email_edit_text)).check(matches(withText(inputEmail)));
        onView(withId(R.id.telephone_number_edit_text)).check(matches(withText(inputTelephoneNumber)));

        onView(withId(R.id.register_account_button)).perform(ViewActions.click());
    }
}
