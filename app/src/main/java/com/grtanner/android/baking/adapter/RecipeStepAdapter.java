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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.grtanner.android.baking.ui.R;
import com.grtanner.android.baking.ui.RecipeDetailActivity;
import com.grtanner.android.baking.ui.RecipeStepActivity;
import com.grtanner.android.baking.ui.RecipeStepFragment;
import com.grtanner.android.baking.data.Step;
import java.util.List;

// Ref: https://www.sitepoint.com/mastering-complex-lists-with-the-android-recyclerview/
public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepViewHolder> {

    List<Step> mStepsList;
    RecipeDetailActivity mParentActivity;
    private boolean mTwoPane;

    public RecipeStepAdapter(RecipeDetailActivity parent, List<Step> stepsList, boolean twoPane) {
        this.mParentActivity = parent;
        this.mStepsList = stepsList;
        this.mTwoPane = twoPane;
    }

    @Override
    public RecipeStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_steps, parent, false);
        return new RecipeStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeStepViewHolder holder, int position) {

        final Step step = mStepsList.get(position);

        String title = step.getShortDescription();
        String stepNum = "Step " +step.getId() + ": ";

        holder.stepShortDesc.setText(stepNum + title);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            // When the item is clicked, get the Recipe and pass the info to a fragment for display.
            @Override
            public void onClick(View view) {

                if(mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable("STEP", step);
                    RecipeStepFragment fragment = new RecipeStepFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_step_detail_container, fragment)
                            .commit();
                } else {
                    Intent intent = new Intent(view.getContext(), RecipeStepActivity.class);
                    intent.putExtra("STEP", step);
                    view.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return mStepsList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Step data) {
        mStepsList.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Step data) {
        int position = mStepsList.indexOf(data);
        mStepsList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     *
     */
    class RecipeStepViewHolder extends RecyclerView.ViewHolder {

        TextView stepShortDesc;

        RecipeStepViewHolder(final View itemView) {
            super(itemView);
            stepShortDesc = (TextView) itemView.findViewById(R.id.step_title);
        }
    }
}
