package com.example.vinsolmeetingscheduler.view.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.vinsolmeetingscheduler.R;
import com.example.vinsolmeetingscheduler.service.model.ScheduleMeetingResponse;
import com.example.vinsolmeetingscheduler.view.adapter.MeetingAdapter;
import com.example.vinsolmeetingscheduler.viewmodel.ScheduleMeetingViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout previous_button_click, forward_button_click;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    MeetingAdapter adapter;
    ScheduleMeetingViewModel scheduleMeetingViewModel;
    ArrayList<ScheduleMeetingResponse> schedulelist = new ArrayList<>();

    Button btnScheduleMeeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initToolBar();
        initListeners();
        initRecyclerView();
        initViewModel();



    }

    private void initView() {
        btnScheduleMeeting = findViewById(R.id.schedule_meeting_button);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.schedule_list_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MeetingAdapter(schedulelist,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    private void initViewModel() {
        scheduleMeetingViewModel = ViewModelProviders.of(this).get(ScheduleMeetingViewModel.class);
        observeViewModel(scheduleMeetingViewModel);
    }

    private void observeViewModel(ScheduleMeetingViewModel scheduleMeetingViewModel) {
        scheduleMeetingViewModel.getMeetingResponse("7/8/2015").observe(this, new Observer<List<ScheduleMeetingResponse>>() {
            @Override
            public void onChanged(List<ScheduleMeetingResponse> scheduleMeetingResponses) {
                if(scheduleMeetingResponses!=null){
                    schedulelist.addAll(scheduleMeetingResponses);
                    adapter.notifyDataSetChanged();
                }
                else{
                    //todo snackbar

                }
            }
        });
    }

    private void initToolBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_bar_layout);
        View view = getSupportActionBar().getCustomView();
        previous_button_click = view.findViewById(R.id.back_click);
        forward_button_click = view.findViewById(R.id.forward_click);
    }
    private void initListeners() {
        previous_button_click.setOnClickListener(this);
        forward_button_click.setOnClickListener(this);
        btnScheduleMeeting.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_click:
                Log.e("TAG", "BACK");
                break;
            case R.id.forward_click:
                Log.e("TAG", "FORWARD");
                break;
            case R.id.schedule_meeting_button:
                Intent intent = new Intent(this,ScheduleMeetingFormActivity.class);
                intent.putExtra("date","date");
                startActivity(intent);
        }
    }
}
