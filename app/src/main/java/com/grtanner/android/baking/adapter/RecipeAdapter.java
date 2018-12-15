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
package com.grtanner.android.baking.adapter;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.grtanner.android.baking.BakingAppWidget;
import com.grtanner.android.baking.ui.R;
import com.grtanner.android.baking.ui.RecipeDetailActivity;
import com.grtanner.android.baking.ui.RecipeListActivity;
import com.grtanner.android.baking.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private RecipeListActivity mParentActivity;
    private ArrayList<Recipe> mRecipeList;
    private boolean mTwoPane;

    public RecipeAdapter(RecipeListActivity parent, ArrayList<Recipe> recipeList, boolean twoPane) {
        this.mParentActivity = parent;
        this.mRecipeList = recipeList;
        this.mTwoPane = twoPane;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.recipe_list_content, parent, false);

        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {

        final int pos = holder.getAdapterPosition();

        // Get the Recipe from the list.
        Recipe recipe = mRecipeList.get(position);

        // Set the recipe name for the RecipeViewHolder
        String tempRecipeName = recipe.getName();
        holder.recipeName.setText(tempRecipeName);

        // Get the recipe name, and remove any spaces in the name so that we can use it in a url.
        tempRecipeName = tempRecipeName.replaceAll("\\s", "").toLowerCase();

        // There are a lot of missing images in this API.
        // Check to see if there is an image.  If not, use a stock photo based on the recipe title.
        if (recipe.getImage() != null && !recipe.getImage().isEmpty()) {
            Picasso.get()
                    .load(recipe.getImage())
                    .into(holder.recipeImage);
        } else {
            Picasso.get()
                    .load(fetchImage(tempRecipeName))
                    .into(holder.recipeImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            // When the item is clicked, get the Recipe and pass the info to a fragment for display.
            @Override
            public void onClick(View view) {
                Recipe recipe = mRecipeList.get(pos);

                Intent intent = new Intent(view.getContext(), RecipeDetailActivity.class);
                intent.putExtra("TwoPane", mTwoPane);
                intent.putParcelableArrayListExtra("RecipeSteps", recipe.getSteps());
                intent.putExtra("RecipeName", recipe.getName());
                intent.putExtra("RecipeIngredients", recipe.getIngredientsAsString());
                view.getContext().startActivity(intent);

                // Update the AppWidgetProvider with a list of instructions.
                // Reference: htpps://stackoverflow.com/questions/3455123/programmatically-update-widget-from-activity-service-receiver
                /*Method 1:
                Context context = this;
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_2x1);
                ComponentName thisWidget = new ComponentName(context, MyWidget.class);
                remoteViews.setTextViewText(R.id.my_text_view, "myText" + System.currentTimeMillis());
                appWidgetManager.updateAppWidget(thisWidget, remoteViews);

                Method 2:
                Context context = getApplicationContext();
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName thisWidget = new ComponentName(context, StackWidgetProvider.class);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
                */

                // With ListView in the widget
                //AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mParentActivity);
                //ComponentName thisWidget = new ComponentName(mParentActivity, BakingAppWidget.class);
                //int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
                //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view);

                // Without the ListView in the widget - this works well if we just pass a String to the RemoteViews object.
                //
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mParentActivity);
                RemoteViews remoteViews = new RemoteViews(mParentActivity.getPackageName(), R.layout.widget_layout);
                ComponentName bakingWidget = new ComponentName(mParentActivity, BakingAppWidget.class);
                remoteViews.setTextViewText(R.id.appwidget_text, recipe.getName() + "\n\n" + recipe.getIngredientsAsString());
                appWidgetManager.updateAppWidget(bakingWidget, remoteViews);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    private int fetchImage(String recipeName) {
        switch (recipeName) {
            case "brownies":
                return R.drawable.brownies;
            case "yellowcake":
                return R.drawable.yellowcake;
            case "nutellapie":
                return R.drawable.nutellapie;
            case "cheesecake":
                return R.drawable.cheesecake;
            default:
                return R.drawable.defaultimage;
        }
    }

    /**
     *
     */
    class RecipeViewHolder extends RecyclerView.ViewHolder {

        ImageView recipeImage;
        TextView recipeName;

        RecipeViewHolder(final View itemView) {
            super(itemView);
            recipeImage = (ImageView) itemView.findViewById(R.id.recipeImage);
            recipeName = (TextView) itemView.findViewById(R.id.recipeName);
        }
    }
}
