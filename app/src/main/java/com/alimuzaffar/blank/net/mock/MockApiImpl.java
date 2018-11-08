package com.alimuzaffar.blank.net.mock;


import android.os.Handler;
import android.os.Looper;
import com.alimuzaffar.blank.database.entity.Sample;
import com.alimuzaffar.blank.net.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MockApiImpl implements ApiInterface {

    Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    public Call<Sample> getUser(String userId) {

        return new MockCall<Sample>() {
            @Override
            public void enqueue(Callback<Sample> callback) {
                // Delay response 200ms to simulate background processing
                mainThreadHandler.postDelayed(() -> {
                    callback.onResponse(this,
                            Response.success(
                                    new Sample("123")));
                }, 200);

            }
        };
    }

}
