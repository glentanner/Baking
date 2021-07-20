package com.grtanner.android.baking.helper;

import com.grtanner.android.baking.api.ApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Reference: http://www.androiddeft.com/2017/10/08/retrofit-android/ and
 *            https://medium.com/@prakash_pun/retrofit-a-simple-android-tutorial-48437e4e5a23
 */
public class RetroClient {

    private static Retrofit mRetrofit;
    private static final String ROOT_URL = "https://d17h27t6h515a5.cloudfront.net/";

    private static Retrofit getRetrofitInstance() {
        if(mRetrofit == null) {
            mRetrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(ROOT_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

    /**
     *
     * @return instance of the ApiService
     */
    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }
}
