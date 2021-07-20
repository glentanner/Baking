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

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grtanner.android.baking.ui.R;
import com.grtanner.android.baking.ui.RecipeDetailActivity;
import com.grtanner.android.baking.ui.RecipeStepActivity;
import com.grtanner.android.baking.ui.RecipeStepFragment;
import com.grtanner.android.baking.data.Step;

import java.util.ArrayList;

// Ref: https://www.sitepoint.com/mastering-complex-lists-with-the-android-recyclerview/
public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepViewHolder> {

    private ArrayList<Step> mStepsList;
    private RecipeDetailActivity mParentActivity;
    private boolean mTwoPane;
    private int mSelectedPosition = RecyclerView.NO_POSITION;

    public RecipeStepAdapter(RecipeDetailActivity parent, ArrayList<Step> stepsList, boolean twoPane) {
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

        //
        holder.itemView.setBackgroundColor(mSelectedPosition == position ?
                mParentActivity.getResources().getColor(R.color.light_gray) :
                mParentActivity.getResources().getColor(R.color.colorWhite));

        final int mPosition = position;

        final Step step = mStepsList.get(position);

        String title = step.getShortDescription();
        String stepNum = mParentActivity.getResources().getString(R.string.step_num) + step.getId() + ": ";

        holder.stepShortDesc.setText(stepNum + title);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            // When the item is clicked, get the Recipe and pass the info to a fragment for display.
            @Override
            public void onClick(View view) {

                notifyItemChanged(mSelectedPosition);
                mSelectedPosition = mPosition;
                notifyItemChanged(mSelectedPosition);

                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(mParentActivity.getResources().getString(R.string.step), step);
                    RecipeStepFragment fragment = new RecipeStepFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_step_detail_container, fragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Intent intent = new Intent(view.getContext(), RecipeStepActivity.class);
                    intent.putExtra(mParentActivity.getResources().getString(R.string.step), step);
                    view.getContext().startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        // Remember for an ArrayList, size() returns number of elements, length returns capacity.
        if (mStepsList != null) {
            return mStepsList.size();
        } else return 0;
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

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
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
