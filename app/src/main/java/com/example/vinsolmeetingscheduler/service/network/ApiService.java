package com.example.vinsolmeetingscheduler.service.network;

import com.example.vinsolmeetingscheduler.service.model.ScheduleMeetingResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("schedule")
    Call<List<ScheduleMeetingResponse>> getSchedule(@Query("date") String date);
}