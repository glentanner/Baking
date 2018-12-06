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
package com.grtanner.android.baking.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to convert json to a List<Recipe>
 *
 */
public class RecipeContent {


    public static ArrayList<Recipe> buildRecipeList(List<Recipe> jsonRecipeList) {

        ArrayList<Recipe> recipeList = new ArrayList<>();

        try {
            for (Recipe jsonRecipe : jsonRecipeList) {
                recipeList.add(new Recipe.RecipeBuilder(jsonRecipe.getId(), jsonRecipe.getName())
                        .ingredients(jsonRecipe.getIngredients())
                        .steps(jsonRecipe.getSteps())
                        .servings(jsonRecipe.getServings())
                        .image(jsonRecipe.getImage())
                        .build());
            }
        } catch(Exception e) {
            e.getMessage();
        }
        return recipeList;
    }
}
