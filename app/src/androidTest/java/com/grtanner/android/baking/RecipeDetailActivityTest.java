package com.grtanner.android.baking;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import com.grtanner.android.baking.ui.R;
import com.grtanner.android.baking.ui.RecipeDetailActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This test demos a user clicking on a Recipe Step List item in RecipeDetailActivity which opens up the
 * corresponding RecipeStepActivity/Fragment.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {

    public static final String STEP_NAME = "Nutella Pie";

    @Rule
    public ActivityTestRule<RecipeDetailActivity> mActivityTestRule = new ActivityTestRule<>(RecipeDetailActivity.class);

    /**
     * Clicks on a RecyclerView item and checks it opens up the RecipeDetailActivity with the correct details.
     */
    @Test
    public void clickRecyclerViewItem_OpensRecipeDetailActivity() {
        onView(withId(R.id.recycler_view_steps))
                .perform(click());
        onView(withText(STEP_NAME)).check(matches(isDisplayed()));
    }

}
