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

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import com.grtanner.android.baking.adapter.RecipeAdapter;
import com.grtanner.android.baking.api.ApiService;
import com.grtanner.android.baking.data.Recipe;
import com.grtanner.android.baking.data.RecipeContent;
import com.grtanner.android.baking.helper.RetroClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */
public class RecipeListActivity extends AppCompatActivity {

    private boolean mTwoPane;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(getTitle());
        }

        if (findViewById(R.id.recipe_step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        // Get a handle for the ApiService interface
        ApiService service = RetroClient.getApiService();
        // Fetch the JSON
        Call<List<Recipe>> call = service.getAllRecipes();
        // enqueue gets called when a response is received.
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                // Make sure response is not null
                if(response.body() != null) {
                    // Get the JSON list of "recipes", and build a ArrayList<Recipe>
                    populateRecipeList(RecipeContent.buildRecipeList(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    /**
     *
     * @param recipeList
     */
    private void populateRecipeList(ArrayList<Recipe> recipeList) {

        //mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recipe_list);

        // Set the layout, for linear layout, it looks nicer on a tablet.
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(RecipeListActivity.this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        RecipeAdapter mRecipeAdapter = new RecipeAdapter(this, recipeList,mTwoPane);

        // Set the adapter
        mRecyclerView.setAdapter(mRecipeAdapter);
    }
}
