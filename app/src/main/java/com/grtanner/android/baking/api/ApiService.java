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
package com.grtanner.android.baking.api;

import com.grtanner.android.baking.data.Recipe;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Reference: http://www.androiddeft.com/2017/10/08/retrofit-android/
 */
public interface ApiService {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getAllRecipes();
}
