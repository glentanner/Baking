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
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 */
public class Recipe implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ingredients")
    @Expose
    private List<Ingredient> ingredients = null;
    @SerializedName("steps")
    @Expose
    private ArrayList<Step> steps = null;
    @SerializedName("servings")
    @Expose
    private Integer servings;
    @SerializedName("image")
    @Expose
    private String image;

    public final static Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return (new Recipe[size]);
        }

    }
            ;

    protected Recipe(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.ingredients = new ArrayList<>();
        in.readList(this.ingredients, (Ingredient.class.getClassLoader()));
        //this.steps = new ArrayList<>();
        //in.readList(this.steps, (Step.class.getClassLoader()));
        in.readTypedList(this.steps, Step.CREATOR);
        this.servings = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
    }

    private Recipe(RecipeBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.ingredients = builder.ingredients;
        this.steps = builder.steps;
        this.servings = builder.servings;
        this.image = builder.image;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getIngredientsAsString() {
        StringBuilder str = new StringBuilder();

        for(Ingredient temp : ingredients) {
            str.append(temp.getQuantity().intValue()
                    + " " + temp.getMeasure().toLowerCase()
                    + "   " + temp.getIngredient().toLowerCase() +"\n\n");
        }

        return str.toString();
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeList(ingredients);
        dest.writeTypedList(steps);
        dest.writeValue(servings);
        dest.writeValue(image);
    }

    public int describeContents() {
        return 0;
    }

    public String showDetails() {
        return "Here are the details.";
    }

    public static class RecipeBuilder {
        private final Integer id;
        private final String name;
        private List<Ingredient> ingredients = null;
        private ArrayList<Step> steps = null;
        private Integer servings;
        private String image;

        public RecipeBuilder(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public RecipeBuilder ingredients(List<Ingredient> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public RecipeBuilder steps(ArrayList<Step> steps) {
            this.steps = steps;
            return this;
        }

        public RecipeBuilder servings(Integer servings) {
            this.servings = servings;
            return this;
        }

        public RecipeBuilder image(String image) {
            this.image = image;
            return this;
        }

        public Recipe build() {
            return new Recipe(this);
        }
    }
}
