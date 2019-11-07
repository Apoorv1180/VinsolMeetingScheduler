package com.example.vinsolmeetingscheduler.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vinsolmeetingscheduler.R;

public class ScheduleMeetingFormActivity extends AppCompatActivity {

    private  String date;
    private LinearLayout previous_button_click;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_meeting_form);
        getIntentData();
        initToolBar();
        
    }

    private void getIntentData() {
       date = getIntent().getStringExtra("date");
    }
    private void initToolBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_bar_layout_meeting_schedule);
        View view = getSupportActionBar().getCustomView();
        previous_button_click = view.findViewById(R.id.back_click);
    }
}
