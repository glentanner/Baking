/*
Copyright ©2018 Glen Tanner

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.grtanner.android.baking.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.grtanner.android.baking.adapter.RecipeStepAdapter;
import com.grtanner.android.baking.data.Step;
import java.util.ArrayList;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    private boolean mTwoPane;
    private ArrayList<Step> mRecipeSteps = new ArrayList<>();
    private String mRecipeName;
    private String mRecipeIngredients;
    //
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mRecyclerView;
    private RecipeStepAdapter mStepsAdapter;
    private static final String LIST_POSITION = "list_position";
    private int mListPosition = RecyclerView.NO_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState != null) {
            mTwoPane = savedInstanceState.getBoolean(getResources().getString(R.string.two_pane));
            mRecipeSteps = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.recipe_steps));
            mRecipeName = savedInstanceState.getString(getResources().getString(R.string.recipe_name));
            mRecipeIngredients = savedInstanceState.getString(getResources().getString(R.string.recipe_ingredients));
            mListPosition = savedInstanceState.getInt(LIST_POSITION);
        }

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            mTwoPane = bundle.getBoolean(getResources().getString(R.string.two_pane));
            mRecipeSteps = bundle.getParcelableArrayList(getResources().getString(R.string.recipe_steps));
            mRecipeName = bundle.getString(getResources().getString(R.string.recipe_name));
            mRecipeIngredients = bundle.getString(getResources().getString(R.string.recipe_ingredients));
        }

        CollapsingToolbarLayout appBarLayout = findViewById(R.id.activity_recipe_detail_toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(mRecipeName);
        }


        if (findViewById(R.id.recipe_step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        TextView ingTextView = findViewById(R.id.ingredients_title);

        TextView textView = findViewById(R.id.recipe_detail);

        ingTextView.setText(getString(R.string.recipe_name_and_ingredients, mRecipeName));
        textView.setText(mRecipeIngredients);

        mRecyclerView = findViewById(R.id.recycler_view_steps);
        mStepsAdapter = new RecipeStepAdapter(this, mRecipeSteps, mTwoPane);

        // Set the adapter
        mRecyclerView.setAdapter(mStepsAdapter);
        mStepsAdapter.setSelectedPosition(mListPosition);

        // Set the layout, for linear layout, it looks nicer on a tablet.
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, RecipeListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(getResources().getString(R.string.two_pane), mTwoPane);
        outState.putParcelableArrayList(getResources().getString(R.string.recipe_steps), mRecipeSteps);
        outState.putString(getResources().getString(R.string.recipe_name), mRecipeName);
        outState.putString(getResources().getString(R.string.recipe_ingredients), mRecipeIngredients);
        outState.putInt(LIST_POSITION, mStepsAdapter.getSelectedPosition());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTwoPane = savedInstanceState.getBoolean(getResources().getString(R.string.two_pane));
        mRecipeSteps = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.recipe_steps));
        mRecipeName = savedInstanceState.getString(getResources().getString(R.string.recipe_name));
        mRecipeIngredients = savedInstanceState.getString(getResources().getString(R.string.recipe_ingredients));
        mListPosition = savedInstanceState.getInt(LIST_POSITION);
    }
}
