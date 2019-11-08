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
import android.widget.TextView;

import com.example.vinsolmeetingscheduler.R;
import com.example.vinsolmeetingscheduler.service.model.ScheduleMeetingResponse;
import com.example.vinsolmeetingscheduler.util.Constant;
import com.example.vinsolmeetingscheduler.view.adapter.MeetingAdapter;
import com.example.vinsolmeetingscheduler.viewmodel.ScheduleMeetingViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout previous_button_click, forward_button_click;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    MeetingAdapter adapter;
    ScheduleMeetingViewModel scheduleMeetingViewModel;
    ArrayList<ScheduleMeetingResponse> schedulelist = new ArrayList<>();
    Date date,decrementDate,incrementDate;
    Button btnScheduleMeeting;
    TextView emptyTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        date = new Date();
        Constant.saveDate(this,Constant.convertDateToString(date));

        initToolBar();
        initListeners();
        initRecyclerView();
        initViewModel();



    }

    private void initView() {
        btnScheduleMeeting = findViewById(R.id.schedule_meeting_button);
        emptyTextView=findViewById(R.id.emptyView);
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
        observeViewModel(scheduleMeetingViewModel,Constant.getDate(this));
    }

    private void observeViewModel(ScheduleMeetingViewModel scheduleMeetingViewModel, String date) {
        scheduleMeetingViewModel.getMeetingResponse(date).observe(this, new Observer<List<ScheduleMeetingResponse>>() {
            @Override
            public void onChanged(List<ScheduleMeetingResponse> scheduleMeetingResponses) {
                if(scheduleMeetingResponses!=null && scheduleMeetingResponses.size()!=0){
                    schedulelist.clear();
                    schedulelist.addAll(scheduleMeetingResponses);
                    adapter.notifyDataSetChanged();
                    emptyTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                else{
                  emptyTextView.setVisibility(View.VISIBLE);
recyclerView.setVisibility(View.GONE);
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
                decrementDate = Constant.decrementAdataByOneDay(Constant.convertStringToDate(Constant.getDate(this)));
                Constant.saveDate(this,Constant.convertDateToString(decrementDate));
                Log.e("TAG", decrementDate.toString());
                observeViewModel(scheduleMeetingViewModel,Constant.getDate(this));

                break;
            case R.id.forward_click:
                Log.e("TAG", "BACK");
                incrementDate = Constant.incerementAdateByOneDay(Constant.convertStringToDate(Constant.getDate(this)));
                Constant.saveDate(this,Constant.convertDateToString(incrementDate));
                Log.e("TAG", incrementDate.toString());
                observeViewModel(scheduleMeetingViewModel,Constant.getDate(this));
                break;
            case R.id.schedule_meeting_button:
                Intent intent = new Intent(this,ScheduleMeetingFormActivity.class);
                intent.putExtra("date",Constant.getDate(this));
                startActivity(intent);
        }
    }
}
