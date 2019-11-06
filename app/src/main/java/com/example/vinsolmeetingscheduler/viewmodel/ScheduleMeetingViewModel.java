package com.example.vinsolmeetingscheduler.viewmodel;

import android.app.Application;
import android.app.ListActivity;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.vinsolmeetingscheduler.service.model.ScheduleMeetingResponse;
import com.example.vinsolmeetingscheduler.service.repository.DataRepository;

import java.util.List;


public class ScheduleMeetingViewModel extends AndroidViewModel {

    DataRepository dataRepository;

    public ScheduleMeetingViewModel(Application mApplication) {
        super(mApplication);
        try {
            dataRepository = DataRepository.getInstance(mApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LiveData<List<ScheduleMeetingResponse>> getMeetingResponse(String date) {
        return (LiveData<List<ScheduleMeetingResponse>>) dataRepository.getScheduleLiveData(date);
    }
}
