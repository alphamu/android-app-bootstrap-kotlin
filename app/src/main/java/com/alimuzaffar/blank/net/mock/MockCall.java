package com.alimuzaffar.blank.net.mock;

import com.google.gson.Gson;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

public abstract class MockCall<T> implements Call<T> {
    public static Gson gson = new Gson();

    @Override
    abstract public void enqueue(Callback<T> callback);

    @Override
    public Response<T> execute() throws IOException {
        return null;
    }

    @Override
    public boolean isExecuted() {
        return false;
    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public Call<T> clone() {
        return null;
    }

    @Override
    public Request request() {
        return null;
    }
}
