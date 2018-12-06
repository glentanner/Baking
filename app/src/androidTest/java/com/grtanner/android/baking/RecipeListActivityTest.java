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
import com.grtanner.android.baking.ui.RecipeListActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This test demos a user clicking on a Recipe List item in RecipeListActivity which opens up the
 * corresponding RecipeDetailActivity/Fragment.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {

    public static final String RECIPE_NAME = "Nutella Pie";

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule = new ActivityTestRule<>(RecipeListActivity.class);

    /**
     * Clicks on a RecyclerView item and checks it opens up the RecipeDetailActivity with the correct details.
     */
    @Test
    public void clickRecyclerViewItem_OpensRecipeDetailActivity() {
        onView(withId(R.id.recipe_list))
                .perform(click());
        onView(withText(RECIPE_NAME)).check(matches(isDisplayed()));
    }

}
