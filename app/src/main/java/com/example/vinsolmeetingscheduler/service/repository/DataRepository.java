package com.example.vinsolmeetingscheduler.service.repository;


import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.vinsolmeetingscheduler.service.model.ScheduleMeetingResponse;
import com.example.vinsolmeetingscheduler.service.network.ApiService;
import com.example.vinsolmeetingscheduler.util.Constant;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataRepository {

    private static DataRepository dataRepository;
    private static Context context;
    private ApiService apiService;


    private DataRepository(Application application) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

    }

    public synchronized static DataRepository getInstance(Application application) {
        if (dataRepository == null) {
            if (dataRepository == null) {
                dataRepository = new DataRepository(application);
                context = application.getApplicationContext();
            }
        }
        return dataRepository;
    }

    public LiveData<List<ScheduleMeetingResponse>> getScheduleLiveData(String date) {

        final MutableLiveData<List<ScheduleMeetingResponse>> meetingResponseMutableLiveData = new MutableLiveData<>();

        apiService.getSchedule(date).enqueue(new Callback<List<ScheduleMeetingResponse>>() {
            @Override
            public void onResponse(Call<List<ScheduleMeetingResponse>> call, Response<List<ScheduleMeetingResponse>> response) {
                meetingResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<ScheduleMeetingResponse>> call, Throwable t) {
                meetingResponseMutableLiveData.setValue(null);
            }
        });


        return meetingResponseMutableLiveData;
    }


}