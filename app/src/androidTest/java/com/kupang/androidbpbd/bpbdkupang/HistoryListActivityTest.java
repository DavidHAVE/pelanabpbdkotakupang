package com.kupang.androidbpbd.bpbdkupang;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.kupang.androidbpbd.bpbdkupang.ui.DetilHistoryActivity;
import com.kupang.androidbpbd.bpbdkupang.ui.HistoryActivity;
import com.kupang.androidbpbd.bpbdkupang.ui.MainActivity;
import com.kupang.androidbpbd.bpbdkupang.ui.SendReportActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.junit.runner.RunWith;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class HistoryListActivityTest {

    @Rule
    public IntentsTestRule<HistoryActivity> intentsRule = new IntentsTestRule<>(HistoryActivity.class);

    @Test
    public void testShouldShowTheItemDetailWhenAnItemIsClicked() throws Exception {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        final RecyclerView recyclerView = (RecyclerView) intentsRule.getActivity().findViewById(R.id.report_history_recycler_view);

        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                int position = 0;
                recyclerView.performClick();
            }
        });

        Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(DetilHistoryActivity.class.getName(), null, false);
        Activity itemDetailActivity = instrumentation.waitForMonitorWithTimeout(monitor, 5000);

        EditText detailView = (EditText) itemDetailActivity.findViewById(R.id.disaster_type_edit_text);
        assertThat(detailView.getText().toString(), is("Kekeringan"));
    }
}